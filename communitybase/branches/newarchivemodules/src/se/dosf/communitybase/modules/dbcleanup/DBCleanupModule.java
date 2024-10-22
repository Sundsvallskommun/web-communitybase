package se.dosf.communitybase.modules.dbcleanup;

import java.sql.SQLException;
import java.util.List;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import it.sauronsoftware.cron4j.Scheduler;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.cache.BaseModuleCache;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.Module;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.validation.StringIntegerValidator;
import se.unlogic.webutils.http.URIParser;

public class DBCleanupModule extends AnnotatedForegroundModule {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Run Hour", description = "Hour to run at, 24H format.", required = true, formatValidator = ZeroToTwentyThreeStringIntegerValidator.class)
	protected Integer runHour = 0;

	private Scheduler scheduler;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		scheduler.setDaemon(true);
		
		scheduler.schedule("0 " + runHour + " * * *", new Runnable() {

			@Override
			public void run() {

				dbCleanup();
			}
		});

		scheduler.start();
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		log.info("User " + user + " running DB cleanup");

		dbCleanup();

		return new SimpleForegroundModuleResponse("Running DB cleanup, see log for results.", getDefaultBreadcrumb());
	}

	@Override
	public void unload() throws Exception {

		super.unload();

		if(scheduler != null) {
			scheduler.stop();
			scheduler = null;
		}
	}

	public void dbCleanup() {

		log.info("Running DB cleanup...");
		RecursiveDBCleanup(systemInterface.getRootSection());
		log.info("DB cleanup completed");
	}

	private void RecursiveDBCleanup(SectionInterface section) {

		CleanModules(section.getForegroundModuleCache());
		CleanModules(section.getBackgroundModuleCache());

		for(SectionInterface subSection : section.getSectionCache().getSectionMap().values()) {
			RecursiveDBCleanup(subSection);
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void CleanModules(BaseModuleCache moduleCache) {

		List<Entry<ModuleDescriptor, Module<?>>> modules = moduleCache.getCachedModules();

		for(Entry<ModuleDescriptor, Module<?>> entry : modules) {
			Module<?> module = entry.getValue();

			if(DBCleaner.class.isAssignableFrom(module.getClass())) {
				DBCleaner cleaner = (DBCleaner) module;

				try {
					cleaner.cleanDB();
				} catch (SQLException e) {
					log.error("Module " + module + " threw exception while cleaning DB.", e);
				}
			}

		}
	}

	public static class ZeroToTwentyThreeStringIntegerValidator extends StringIntegerValidator {

		private static final long serialVersionUID = 4064652322318660798L;

		public ZeroToTwentyThreeStringIntegerValidator() {

			super(0, 23);
		}
	}

}
