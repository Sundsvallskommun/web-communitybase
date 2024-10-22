package se.dosf.communitybase.modules.tagcache;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.NavigableSet;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import it.sauronsoftware.cron4j.Scheduler;
import se.dosf.communitybase.interfaces.TagCache;
import se.dosf.communitybase.interfaces.TagProvider;
import se.unlogic.cron4jutils.CronStringValidator;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.listeners.SystemStartupListener;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.utils.MultiForegroundModuleTracker;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.webutils.http.HTTPUtils;

public class TagCacheModule extends AnnotatedForegroundModule implements TagCache, Runnable, SystemStartupListener {

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Tag reload interval", description = "How often the tag chache should be reloaded to remove unused tags (specified in crontab format)", required = true, formatValidator = CronStringValidator.class)
	private String tagReloadInterval = "0 * * * *";

	private Scheduler scheduler;

	private ConcurrentSkipListSet<String> tagSet = new ConcurrentSkipListSet<String>();

	private MultiForegroundModuleTracker<TagProvider> tagProviderTracker;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		tagProviderTracker = new MultiForegroundModuleTracker<TagProvider>(TagProvider.class, systemInterface, systemInterface.getRootSection(), true, true);

		if (!systemInterface.getInstanceHandler().addInstance(TagCache.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + TagCache.class.getName());
		}

		systemInterface.addSystemStartupListener(this);
	}

	@Override
	public void update(ForegroundModuleDescriptor moduleDescriptor, DataSource dataSource) throws Exception {

		super.update(moduleDescriptor, dataSource);

		stopScheduler();
		initScheduler();
	}

	@Override
	public void unload() throws Exception {

		stopScheduler();

		tagProviderTracker.shutdown();

		systemInterface.getInstanceHandler().removeInstance(TagCache.class, this);

		super.unload();
	}

	protected void cacheTags() {

		log.info("Caching tags");

		ConcurrentSkipListSet<String> tempTagSet = new ConcurrentSkipListSet<String>();
		
		List<String> checkedProviders = new ArrayList<String>();

		for (TagProvider tagProvider : tagProviderTracker.getInstances()) {
			
			if (checkedProviders.contains(tagProvider.getTagProviderAlias())) {
				continue;
			}

			try {
				Collection<String> tags = tagProvider.getTags();

				if (tags != null) {

					log.debug("Got " + tags.size() + " from tag provider " + tagProvider);

					tempTagSet.addAll(tags);

				}
				
				checkedProviders.add(tagProvider.getTagProviderAlias());

			} catch (Exception e) {

				log.error("Error getting tags from tag provider " + tagProvider);
			}
		}

		log.debug("Got " + tempTagSet.size() + " tags from " + tagProviderTracker.getInstances().size() + " tag providers");

		tagSet = tempTagSet;
	}

	@Override
	public List<String> getMatchingTags(String term, int maxHits) {

		String endTerm = String.valueOf((char) (term.charAt(0) + 1));

		NavigableSet<String> subSet = tagSet.subSet(term, true, endTerm, false);

		if (subSet.isEmpty()) {

			return Collections.emptyList();
		}

		List<String> matchingTags = new ArrayList<String>(maxHits);

		for (String tag : subSet) {

			if (tag.startsWith(term)) {

				matchingTags.add(tag);
			}

			if (matchingTags.size() == maxHits) {

				break;
			}
		}

		if (matchingTags.isEmpty()) {

			return null;
		}

		return matchingTags;
	}

	@Override
	public void addTags(Collection<String> tags) {

		tagSet.addAll(tags);
	}

	@Override
	public void sendMatchingTagsAsJSON(HttpServletRequest req, HttpServletResponse res, int maxHits) throws IOException {

		String query = req.getParameter("query");

		if (!StringUtils.isEmpty(query)) {

			List<String> matchingTags = getMatchingTags(query, maxHits);

			if (matchingTags != null) {

				JsonArray jsonArray = new JsonArray();

				for (String tag : matchingTags) {

					jsonArray.addNode(tag);
				}

				sendJSONResponse(jsonArray, res);

				return;
			}
		}

		sendJSONResponse(new JsonArray(), res);
	}

	protected void sendJSONResponse(JsonArray jsonArray, HttpServletResponse res) throws IOException {

		HTTPUtils.sendReponse(jsonArray.toJson(), JsonUtils.getContentType(), res);
	}

	@Override
	public void run() {

		cacheTags();
	}

	protected synchronized void initScheduler() {

		scheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());
		
		scheduler.setDaemon(true);

		scheduler.schedule(this.tagReloadInterval, this);
		scheduler.start();
	}

	protected synchronized void stopScheduler(){

		try {

			if(scheduler != null){

				scheduler.stop();
				scheduler = null;
			}

		} catch (IllegalStateException e) {
			log.error("Error stopping scheduler", e);
		}
	}

	@Override
	public void systemStarted() {

		cacheTags();
		initScheduler();
	}
}
