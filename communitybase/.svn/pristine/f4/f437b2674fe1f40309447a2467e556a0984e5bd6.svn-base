package se.dosf.communitybase.utils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.ModuleCacheException;
import se.unlogic.hierarchy.core.exceptions.ModuleConfigurationException;
import se.unlogic.hierarchy.core.exceptions.ModuleNotCachedException;
import se.unlogic.hierarchy.core.exceptions.ModuleUnloadException;
import se.unlogic.hierarchy.core.exceptions.ModuleUpdatedInProgressException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.dao.AnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.querys.ArrayListQuery;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.populators.IntegerPopulator;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.webutils.http.URIParser;

/** Migrates pictures image data from DB to disk storage.
 * 
 * @author exuvo */
public abstract class DBtoDiskDataMigrator<T> extends AnnotatedForegroundModule implements Runnable {

	protected Logger log = Logger.getLogger(this.getClass());

	public static final SimpleDateFormat DATE_DAYS_FORMATTER = new SimpleDateFormat("MM-dd HH:mm:ss");

	private Integer sourceModuleID, sourceSectionID;
	private String tableGroupName;
	private XMLDBScriptProvider scriptProvider;
	protected String filestore;

	protected AnnotatedDAOWrapper<T, Integer> dataDAO;

	private Boolean started = false;
	private boolean running;
	private boolean requestStop;

	private String estimatedCompletion;
	private int progressPercent;

	@SuppressWarnings("rawtypes")
	protected abstract Class getClass2();

	@SuppressWarnings("unchecked")
	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		if (!systemInterface.getInstanceHandler().addInstance(getClass2(), this)) {

			throw new IllegalStateException("Another instance has already been registered in instance handler for class " + getClass2().getName());
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void unload() throws Exception {

		stop();

		super.unload();
		
		if (this.equals(systemInterface.getInstanceHandler().getInstance(getClass2()))) {

			systemInterface.getInstanceHandler().removeInstance(getClass2());
		}
	}

	protected abstract XMLDBScriptProvider getScriptProvider() throws SAXException, IOException, ParserConfigurationException;

	protected abstract String getTableGroupName();

	@Override
	protected void moduleConfigured() throws Exception {

		super.moduleConfigured();

		this.sourceModuleID = moduleDescriptor.getMutableSettingHandler().getInt("moduleID");
		this.sourceSectionID = moduleDescriptor.getMutableSettingHandler().getInt("sectionID");
		filestore = moduleDescriptor.getMutableSettingHandler().getString("filestore");
		
		tableGroupName = getTableGroupName();
		scriptProvider = getScriptProvider();

		if (StringUtils.isEmpty(filestore)) {
			throw new ModuleConfigurationException("Filestore not set");
		}

		start();
	}

	public void start() {

		synchronized(started) {
			if (started) {
				throw new IllegalStateException();
			}

			new Thread(this).start();
			started = true;
		}
	}

	protected abstract String getSelectIDQuery();
	
	protected abstract String getDataName();

	protected abstract String getDataNames();

	protected abstract int getFromVersion();

	protected abstract int getToVersion();

	@Override
	public void run() {

		running = true;
		long startTime = System.currentTimeMillis();

		try {

			log.info("Starting " + getDataName() + " migration from DB to disk");

			ArrayListQuery<Integer> query = new ArrayListQuery<Integer>(dataSource, getSelectIDQuery(), new IntegerPopulator());

			List<Integer> dataIDs = query.executeQuery();
			
			for (int i = 0; i < dataIDs.size(); i++) {

				if (requestStop) {
					throw new InterruptedException();
				}

				Integer dataID = dataIDs.get(i);

				T data = dataDAO.get(dataID);

				writeToDisk(data);
				
				int skip = 10;

				if (i % skip == 0) {
					long elapsedMillis = System.currentTimeMillis() - startTime;
					// assume linear progress to estimate remaining time
					float progress = (float) i / dataIDs.size();
					long totalMillis = (long) (elapsedMillis / progress);
					int remainingSeconds = (int) ((totalMillis - elapsedMillis) / 1000);

					progressPercent = (int) (progress * 100);

					Calendar calendar = Calendar.getInstance();
					calendar.add(Calendar.SECOND, remainingSeconds);
					estimatedCompletion = DATE_DAYS_FORMATTER.format(new Date(calendar.getTimeInMillis()));

					log.info(String.format("Migrated %d " + getDataNames() + " from DB to disk, prev id %6d, %6d " + getDataNames() + " remaining. %3d%% complete. Estimated completion at %s", skip, dataID, dataIDs.size() - i, progressPercent, estimatedCompletion));
				}
			}

			log.info(getDataName() + " copy from DB to disk complete. Removing blobs from database.");

			// Run normal upgrade to upgrade to latest version
			UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, tableGroupName, scriptProvider, getFromVersion(), getToVersion());

			if (upgradeResult.isUpgrade()) {
				log.info(upgradeResult.toString());
			}
			
			long updateTime = System.currentTimeMillis() - startTime;

			SimpleForegroundModuleDescriptor descriptor = systemInterface.getCoreDaoFactory().getForegroundModuleDAO().getModule(sourceModuleID);

			log.info(getDataName() + " migration from DB to disk complete. Took " + getDurationBreakdown(updateTime) + ". Restarting module " + descriptor.getName());

			systemInterface.getSectionInterface(sourceSectionID).getForegroundModuleCache().cache(descriptor);

		} catch (InterruptedException e) {

			log.error(getDataName() + " migration interrupted", e);

		} catch (ModuleCacheException e) {

			log.error("Error restarting parent module", e);

		} catch (Exception e) {

			log.error("Error migrating " + getDataNames() + " data to disk", e);

		} finally {

			running = false;

			new Thread(new Runnable() {

				@Override
				public void run() {

					try {
						sectionInterface.getForegroundModuleCache().unload(moduleDescriptor);
					} catch (ModuleNotCachedException e) {
						e.printStackTrace();
					} catch (ModuleUnloadException e) {
						e.printStackTrace();
					} catch (ModuleUpdatedInProgressException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	public void stop() {

		synchronized(started) {

			if (started) {
				requestStop = true;

				while (running) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public abstract void writeToDisk(T picture) throws IOException, SQLException;

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		StringBuilder stringBuilder = new StringBuilder();

		stringBuilder.append("<div class=\"contentitem\">");
		stringBuilder.append("<h1>" + this.moduleDescriptor.getName() + "</h1>");

		stringBuilder.append("<p>" + progressPercent + "% migrated</p>");
		stringBuilder.append("<p>Estimated time of completion " + estimatedCompletion + "</p>");

		stringBuilder.append("</div>");

		return new SimpleForegroundModuleResponse(stringBuilder.toString(), moduleDescriptor.getName(), this.getDefaultBreadcrumb());
	}

	public static String getDurationBreakdown(long millis) {

		if (millis < 0) {
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		long days = TimeUnit.MILLISECONDS.toDays(millis);
		millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder(64);
		
		if (days > 0) {
			sb.append(days);
			sb.append(" Days ");
		}
	
		if (hours > 0) {
			sb.append(hours);
			sb.append(" Hours ");
		}

		if (minutes > 0) {
			sb.append(minutes);
			sb.append(" Minutes ");
		}

		sb.append(seconds);
		sb.append(" Seconds");

		return (sb.toString());
	}

}
