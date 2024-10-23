package se.dosf.communitybase.modules.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.tika.config.TikaConfig;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;

import se.dosf.communitybase.enums.SectionAccessMode;
import se.dosf.communitybase.events.CBSearchableItemAddEvent;
import se.dosf.communitybase.events.CBSearchableItemClearEvent;
import se.dosf.communitybase.events.CBSearchableItemDeleteEvent;
import se.dosf.communitybase.events.CBSearchableItemUpdateEvent;
import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.interfaces.CBSearchable;
import se.dosf.communitybase.interfaces.CBSearchableItem;
import se.dosf.communitybase.interfaces.TagCache;
import se.dosf.communitybase.modules.search.beans.SearchType;
import se.dosf.communitybase.modules.search.enums.SearchMode;
import se.dosf.communitybase.modules.search.events.AddEvent;
import se.dosf.communitybase.modules.search.events.ClearModuleEvent;
import se.dosf.communitybase.modules.search.events.DeleteEvent;
import se.dosf.communitybase.modules.search.events.QueuedSearchEvent;
import se.dosf.communitybase.modules.search.events.UpdateEvent;
import se.dosf.communitybase.modules.search.interfaces.SearchPlugin;
import se.dosf.communitybase.modules.search.interfaces.SearchPluginHandler;
import se.dosf.communitybase.modules.search.searchitems.CBContentPlugin;
import se.dosf.communitybase.modules.search.searchitems.SectionsPlugin;
import se.dosf.communitybase.modules.search.searchitems.TagsPlugin;
import se.dosf.communitybase.modules.search.searchitems.UsersPlugin;
import se.dosf.communitybase.modules.userprofile.UserProfileProvider;
import se.dosf.communitybase.modules.util.CBUtilityModule;
import se.dosf.communitybase.utils.CBAccessUtils;
import se.dosf.communitybase.utils.CBSectionAttributeHelper;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.ServerStartupListener;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.annotations.XSLVariable;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleSectionDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.comparators.PriorityComparator;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.ViewFragment;
import se.unlogic.hierarchy.core.interfaces.listeners.ForegroundModuleCacheListener;
import se.unlogic.hierarchy.core.interfaces.listeners.SectionCacheListener;
import se.unlogic.hierarchy.core.interfaces.modules.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.modules.descriptors.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.settings.MutableSettingHandler;
import se.unlogic.hierarchy.core.sections.Section;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.log4jutils.Log4jUtils;
import se.unlogic.standardutils.annotations.SplitOnLineBreak;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.KeyAlreadyCachedException;
import se.unlogic.standardutils.collections.KeyNotCachedException;
import se.unlogic.standardutils.enums.Order;
import se.unlogic.standardutils.html.HTMLUtils;
import se.unlogic.standardutils.io.CloseUtils;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.threads.PriorityThreadFactory;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

import it.sauronsoftware.cron4j.Scheduler;

@SuppressWarnings("deprecation")
public class SearchModule extends AnnotatedForegroundModule implements SectionCacheListener, ForegroundModuleCacheListener, SearchPluginHandler, Runnable {

	private static final PriorityComparator PRIORITY_COMPARATOR = new PriorityComparator(Order.ASC);

	static {
		//Fix PDFBox logging
		Log4jUtils.disableLogger("org.apache.pdfbox.pdfparser.BaseParser");
		Log4jUtils.disableLogger("org.apache.pdfbox.pdmodel.font.PDType0Font");
		Log4jUtils.disableLogger("org.apache.pdfbox.pdmodel.font.PDType1Font");
		Log4jUtils.disableLogger("org.apache.pdfbox.pdmodel.font.PDFont");
		Log4jUtils.disableLogger("org.apache.fontbox.ttf.CmapSubtable");
		Log4jUtils.disableLogger("org.apache.pdfbox.pdmodel.font.PDCIDFontType0");
		Log4jUtils.disableLogger("org.apache.pdfbox.pdmodel.font.PDCIDFontType2");
		Log4jUtils.disableLogger("org.apache.pdfbox.pdmodel.font.PDSimpleFont");
		Log4jUtils.disableLogger("org.apache.pdfbox.pdmodel.font.PDTrueTypeFont");
		Log4jUtils.disableLogger("org.apache.fontbox.ttf.GlyphSubstitutionTable");
	}

	private static final String[] DEFAULT_FIELDS = new String[] { SearchConstants.TITLE_FIELD, SearchConstants.CONTENT_FIELD, SearchConstants.TAG_FIELD };

	@XSLVariable(name = "i18n.IsExternal")
	private String isExternalText;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Users search priority", description = "Priority for users search", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int usersSearchPriority = 1;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Users search name", description = "Name for users search", required = true)
	protected String usersSearchName = "Personer";

	@ModuleSetting
	@SplitOnLineBreak
	@TextAreaSettingDescriptor(name = "User attributes", description = "User attributes to index in search, one per row")
	private List<String> userSearchAttributes;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Section search priority", description = "Priority for section search", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int sectionSearchPriority = 2;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Section search name", description = "Name for section search", required = true)
	protected String sectionSearchName = "Samarbetsrum";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "File search priority", description = "Priority for file search", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int fileSearchPriority = 3;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "File search name", description = "Name for file search", required = true)
	protected String fileSearchName = "Filer";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Task search priority", description = "Priority for task search", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int taskSearchPriority = 4;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Task search name", description = "Name for task search", required = true)
	protected String taskSearchName = "Uppgifter";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Post search priority", description = "Priority for post search", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int postSearchPriority = 5;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Post search name", description = "Name for post search", required = true)
	protected String postSearchName = "Inlägg";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Calendar search priority", description = "Priority for calendar search", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int calendarSearchPriority = 6;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Calendar search name", description = "Name for calendar search", required = true)
	protected String calendarSearchName = "Kalender";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Page search priority", description = "Priority for page search", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int pageSearchPriority = 7;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Page search name", description = "Name for page search", required = true)
	protected String pageSearchName = "Sidor i samarbetsrum";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Tags search priority", description = "Priority for tags search", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int tagsSearchPriority = 10;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Tags search name", description = "Name for tags search", required = true)
	protected String tagsSearchName = "Taggar";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Tag search priority", description = "Priority for tag search", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int tagSearchPriority = 11;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Tag search name", description = "Name for tag search", required = true)
	protected String tagSearchName = "Taggat innehåll";

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max plugins (json)", description = "Maximum number of plugins to return results from when using JSON", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int maxJsonPlugins = 4;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max hits (json)", description = "Maximum number of hits to get from plugin when searching using JSON", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int maxJsonHitCount = 5;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max hits", description = "Maximum number of hits to get from plugin when searching", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int maxHitCount = 20;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max content hits", description = "Maximum number of hits to get when searching for content", formatValidator = PositiveStringIntegerValidator.class)
	protected int maxContentHitCount = 100;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Thread pool size", description = "The size of the thread pool used for indexing. The default size is determined by (Runtime.getRuntime().availableProcessors()).", formatValidator = PositiveStringIntegerValidator.class)
	protected int poolSize = Runtime.getRuntime().availableProcessors();

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Log item parsing", description = "Controls if parsing of searchable items should be logged or not")
	boolean logItemParsing = true;

	@ModuleSetting
	@CheckboxSettingDescriptor(name = "Register as search pluginhandler", description = "Register as search plugin handler in instance handler")
	protected boolean registerPluginHandler = false;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Start time expression for user recache", description = "Cron expression for when to run user recache", required = true)
	protected String cronExp = "0 5 * * *";

	@InstanceManagerDependency(required = true)
	protected CBInterface cbInterface;

	@InstanceManagerDependency(required = true)
	protected TagCache tagCache;

	@InstanceManagerDependency(required = false)
	private UserProfileProvider userProfileProvider;

	@InstanceManagerDependency(required = false)
	private CBUtilityModule cbUtilityModule;

	private Scheduler taskScheduler;

	private Lock indexLock;

	private SectionIndexer sectionIndexer;

	private UserIndexer userIndexer;

	private PerFieldAnalyzerWrapper perFieldAnalyzer = new PerFieldAnalyzerWrapper(new StandardAnalyzer(), Collections.singletonMap(SearchConstants.TAG_FIELD, (Analyzer) new KeywordAnalyzer()));
	private Directory index = new RAMDirectory();
	private IndexWriter indexWriter;
	private IndexReader indexReader;
	private IndexSearcher searcher;
	private AutoDetectParser parser;

	private LinkedBlockingQueue<QueuedSearchEvent> eventQueue = new LinkedBlockingQueue<QueuedSearchEvent>();

	private CallbackThreadPoolExecutor threadPoolExecutor;

	private ForegroundModuleDescriptor currentEventSource;

	private ConcurrentHashMap<String, SearchPlugin> searchPlugins = new ConcurrentHashMap<String, SearchPlugin>();

	private ArrayList<SearchType> searchTypes = new ArrayList<SearchType>();

	private boolean serverStarted;

	private UsersPlugin usersPlugin = null;

	private SectionsPlugin sectionsPlugin = null;

	private CBContentPlugin filesPlugin = null;

	private CBContentPlugin tasksPlugin = null;

	private CBContentPlugin postsPlugin = null;

	private CBContentPlugin calendarPlugin = null;

	private CBContentPlugin pagesPlugin = null;

	private TagsPlugin tagsPlugin = null;

	private SearchType tagSearchType;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		TikaConfig tikaConfig = new TikaConfig(this.getClass().getResourceAsStream("/se/dosf/communitybase/modules/search/tika-config.xml"));

		parser = new AutoDetectParser(tikaConfig);

		indexWriter = new IndexWriter(index, new IndexWriterConfig(perFieldAnalyzer));

		if (!systemInterface.getInstanceHandler().addInstance(SearchModule.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + SearchModule.class.getName());
		}

		if (registerPluginHandler) {
			if (!systemInterface.getInstanceHandler().addInstance(SearchPluginHandler.class, this)) {
				log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + SearchPluginHandler.class.getName());
			}
		}
	}

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);

		this.stopTaskScheduler();

		this.initTaskScheduler();
	}

	@Override
	public void unload() throws Exception {

		if (registerPluginHandler) {
			systemInterface.getInstanceHandler().removeInstance(SearchPluginHandler.class, this);
		}

		sectionIndexer.close();

		sectionIndexer = null;

		userIndexer.close();

		userIndexer = null;

		systemInterface.getInstanceHandler().removeInstance(SearchModule.class, this);

		systemInterface.removeForegroundModuleCacheListener(this);

		if (threadPoolExecutor != null) {

			threadPoolExecutor.shutdownNow();
			threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS);
		}

		this.eventQueue.clear();

		if (threadPoolExecutor != null) {

			threadPoolExecutor.purge();
		}

		try {
			indexWriter.close();
		} catch (IOException e) {
			log.warn("Error closing index writer", e);
		}

		try {
			index.close();
		} catch (IOException e) {
			log.warn("Error closing index", e);
		}

		stopTaskScheduler();

		super.unload();
	}

	private void initTaskScheduler() {

		this.taskScheduler = new Scheduler(systemInterface.getApplicationName() + " - " + moduleDescriptor.toString());

		this.taskScheduler.setDaemon(true);

		this.taskScheduler.schedule(cronExp, this);

		this.taskScheduler.start();
	}

	private void stopTaskScheduler() {

		if (this.taskScheduler != null && this.taskScheduler.isStarted()) {

			this.taskScheduler.stop();
		}
	}

	@Override
	protected void moduleConfigured() throws Exception {

		if (userIndexer != null) {
			userIndexer.setAttributeNames(userSearchAttributes);
		}

		if (usersPlugin != null) {
			usersPlugin.setPriority(usersSearchPriority);
			usersPlugin.setPluginName(usersSearchName);
		}

		if (sectionsPlugin != null) {
			sectionsPlugin.setPriority(sectionSearchPriority);
			sectionsPlugin.setPluginName(sectionSearchName);
		}

		if (filesPlugin != null) {
			filesPlugin.setPriority(fileSearchPriority);
			filesPlugin.setPluginName(fileSearchName);
		}

		if (tasksPlugin != null) {
			tasksPlugin.setPriority(taskSearchPriority);
			tasksPlugin.setPluginName(taskSearchName);
		}

		if (postsPlugin != null) {
			postsPlugin.setPriority(postSearchPriority);
			postsPlugin.setPluginName(postSearchName);
		}

		if (calendarPlugin != null) {
			calendarPlugin.setPriority(calendarSearchPriority);
			calendarPlugin.setPluginName(calendarSearchName);
		}

		if (pagesPlugin != null) {
			pagesPlugin.setPriority(pageSearchPriority);
			pagesPlugin.setPluginName(pageSearchName);
		}

		if (tagsPlugin != null) {
			tagsPlugin.setPriority(tagsSearchPriority);
			tagsPlugin.setPluginName(tagsSearchName);
		}

		if (tagSearchType != null) {
			tagSearchType.setPriority(tagSearchPriority);
			tagSearchType.setName(tagSearchName);
		}

		Collections.sort(searchTypes, PRIORITY_COMPARATOR);
	}

	private void createSearchPlugins() throws TransformerConfigurationException {

		usersPlugin = new UsersPlugin(usersSearchName, usersSearchPriority, this);

		addSearchPlugin(usersPlugin);

		sectionsPlugin = new SectionsPlugin(sectionSearchName, sectionSearchPriority, this);

		addSearchPlugin(sectionsPlugin);

		filesPlugin = new CBContentPlugin(fileSearchName, "file", fileSearchPriority, this);

		addSearchPlugin(filesPlugin);

		tasksPlugin = new CBContentPlugin(taskSearchName, "task", taskSearchPriority, this);

		addSearchPlugin(tasksPlugin);

		postsPlugin = new CBContentPlugin(postSearchName, "post", postSearchPriority, this);

		addSearchPlugin(postsPlugin);

		calendarPlugin = new CBContentPlugin(calendarSearchName, "calendar", calendarSearchPriority, this);

		addSearchPlugin(calendarPlugin);

		pagesPlugin = new CBContentPlugin(pageSearchName, "page", pageSearchPriority, this);

		addSearchPlugin(pagesPlugin);

		tagsPlugin = new TagsPlugin(tagsSearchName, tagsSearchPriority, this);

		addSearchPlugin(tagsPlugin);

		tagSearchType = new SearchType("tag", tagSearchName, tagSearchPriority);

		searchTypes.add(tagSearchType);

		Collections.sort(searchTypes, PRIORITY_COMPARATOR);
	}

	@Override
	protected void parseSettings(MutableSettingHandler mutableSettingHandler) throws Exception {

		super.parseSettings(mutableSettingHandler);

		if (threadPoolExecutor == null) {

			threadPoolExecutor = new CallbackThreadPoolExecutor(poolSize, poolSize, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), this);
			threadPoolExecutor.setThreadFactory(new PriorityThreadFactory(Thread.MIN_PRIORITY, "Search module pool"));

		} else {

			threadPoolExecutor.setMaximumPoolSize(poolSize);
			threadPoolExecutor.setCorePoolSize(poolSize);
		}
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		String type = req.getParameter("t");
		String queryString = req.getParameter("q");

		if (!"UTF-8".equals(req.getCharacterEncoding()) && req.getParameter("enc") != null) {
			queryString = StringUtils.parseUTF8(queryString);
		}

		Document doc = XMLUtils.createDomDocument();

		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);

		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));

		Element searchElement = doc.createElement("Search");
		documentElement.appendChild(searchElement);

		XMLUtils.append(doc, searchElement, "SearchTypes", searchTypes);

		XMLUtils.appendNewCDATAElement(doc, searchElement, "Type", type);

		if (!StringUtils.isEmpty(queryString)) {

			log.info("User " + user + " searching for: " + StringUtils.toLogFormat(queryString, 50) + " type: " + type);

			XMLUtils.appendNewCDATAElement(doc, searchElement, "Query", queryString);

			XMLUtils.append(doc, searchElement, getViewFragments(req, user, type, queryString, SearchMode.REGULAR));

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
		}

		log.info("User " + user + " requested search form");

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}

	public List<ViewFragment> getViewFragments(HttpServletRequest req, User user, String type, String queryString, SearchMode searchMode) throws IOException, Exception {

		SearchAccessMode accessMode = getSearchAccessMode(user);

		List<ViewFragment> viewFragments = null;

		if (StringUtils.isEmpty(type)) {
			HashMap<String, List<ContentHit>> contentHits = getContentHits(queryString, null, user, accessMode, false);

			if (!CollectionUtils.isEmpty(searchTypes)) {
				int i = 0;

				for (SearchType searchType : searchTypes) {
					SearchPlugin searchPlugin = searchPlugins.get(searchType.getType());

					if (searchPlugin != null) {
						if (searchMode == SearchMode.REGULAR) {
							viewFragments = CollectionUtils.addAndInstantiateIfNeeded(viewFragments, searchPlugin.getFragment(queryString, accessMode, getTypeHits(searchPlugin, contentHits), maxHitCount, user, req));
						} else {
							ViewFragment viewFragment = searchPlugin.getJsonFragment(queryString, accessMode, getTypeHits(searchPlugin, contentHits), maxJsonHitCount, user, req);

							if (viewFragment != null) {
								i++;

								viewFragments = CollectionUtils.addAndInstantiateIfNeeded(viewFragments, viewFragment);

								if (i == maxJsonPlugins) {
									break;
								}
							}
						}
					}
				}
			}
		} else if (type.equals("tag")) {
			HashMap<String, List<ContentHit>> contentHits = getContentHits(queryString, null, user, accessMode, true);

			if (!CollectionUtils.isEmpty(searchTypes)) {
				int i = 0;

				for (SearchType searchType : searchTypes) {
					SearchPlugin searchPlugin = searchPlugins.get(searchType.getType());

					if (searchPlugin != null && searchPlugin.supportsTags()) {
						if (searchMode == SearchMode.REGULAR) {
							viewFragments = CollectionUtils.addAndInstantiateIfNeeded(viewFragments, searchPlugin.getFragment(queryString, accessMode, getTypeHits(searchPlugin, contentHits), maxHitCount, user, req));
						} else {
							ViewFragment viewFragment = searchPlugin.getJsonFragment(queryString, accessMode, getTypeHits(searchPlugin, contentHits), maxJsonHitCount, user, req);

							if (viewFragment != null) {
								i++;

								viewFragments = CollectionUtils.addAndInstantiateIfNeeded(viewFragments, viewFragment);

								if (i == maxJsonPlugins) {
									break;
								}
							}
						}
					}
				}
			}
		} else if (!CollectionUtils.isEmpty(searchPlugins)) {
			SearchPlugin pluginType = searchPlugins.get(type);

			if (pluginType != null) {
				HashMap<String, List<ContentHit>> contentHits = null;

				if (!pluginType.hasOwnIndex()) {
					contentHits = getContentHits(queryString, type, user, accessMode, false);
				}

				if (searchMode == SearchMode.REGULAR) {
					viewFragments = CollectionUtils.addAndInstantiateIfNeeded(viewFragments, pluginType.getFragment(queryString, accessMode, getTypeHits(pluginType, contentHits), maxHitCount, user, req));
				} else {
					viewFragments = CollectionUtils.addAndInstantiateIfNeeded(viewFragments, pluginType.getJsonFragment(queryString, accessMode, getTypeHits(pluginType, contentHits), maxJsonHitCount, user, req));
				}
			} else {
				log.warn("User " + user + " searching for " + queryString + " with unknown type " + type);
			}
		} else {
			log.warn("User " + user + " searching for " + queryString + " with unknown type " + type);
		}

		return viewFragments;
	}

	private List<ContentHit> getTypeHits(SearchPlugin pluginType, HashMap<String, List<ContentHit>> contentHits) {

		if (!pluginType.hasOwnIndex() && contentHits != null) {
			return contentHits.get(pluginType.getAlias());
		}

		return null;
	}

	private HashMap<String, List<ContentHit>> getContentHits(String queryString, String type, User user, SearchAccessMode accessMode, boolean isTagSearch) throws IOException {

		List<Integer> sectionIDs = CBAccessUtils.getUserSections(user, cbInterface);

		List<ContentHit> contentHits = null;

		String[] fields = null;

		if (isTagSearch) {
			fields = new String[] { SearchConstants.TAG_FIELD };
		} else {
			fields = DEFAULT_FIELDS;
		}

		MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, perFieldAnalyzer);
		parser.setDefaultOperator(Operator.AND);

		Query query;

		try {

			queryString = SearchUtils.rewriteQueryString(QueryParser.escape(queryString));

			query = parser.parse(queryString);

		} catch (ParseException e) {

			log.warn("Unable to parse query string " + StringUtils.toLogFormat(queryString, 50) + " requested by user " + user);

			return null;
		}

		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(query, Occur.MUST);

		if (accessMode == SearchAccessMode.RESTRICTED) {

			if (sectionIDs != null) {

				appendRestrictedSearchFilter(builder, sectionIDs, type);

				contentHits = searchContent(builder.build(), user, accessMode, maxContentHitCount);
			}
		} else if (accessMode == SearchAccessMode.DEFAULT) {
			
			if (sectionIDs == null) {
				
				appendOpenSearchFilter(builder, type);

				contentHits = searchContent(builder.build(), user, accessMode, maxContentHitCount);
			} else {
				
				LinkedHashSet<ContentHit> contentHitsSet = new LinkedHashSet<ContentHit>(maxContentHitCount);

				appendRestrictedSearchFilter(builder, sectionIDs, type);

				List<ContentHit> restrictedHits = searchContent(builder.build(), user, accessMode, maxContentHitCount);

				if (!CollectionUtils.isEmpty(restrictedHits)) {
					contentHitsSet.addAll(restrictedHits);
				}

				if (CollectionUtils.getSize(restrictedHits) < maxContentHitCount) {
					List<ContentHit> openHits = null;

					BooleanQuery.Builder openHitsBuilder = new BooleanQuery.Builder();
					openHitsBuilder.add(query, Occur.MUST);

					appendOpenSearchFilter(openHitsBuilder, type);

					openHits = searchContent(openHitsBuilder.build(), user, accessMode, maxContentHitCount - CollectionUtils.getSize(restrictedHits));

					if (!CollectionUtils.isEmpty(openHits)) {
						contentHitsSet.addAll(openHits);
					}
				}

				contentHits = new ArrayList<ContentHit>(contentHitsSet);
			}
		} else if (accessMode == SearchAccessMode.ADMIN) {
			
			if (type != null) {
				appendTypeFilter(builder, type);
			}

			contentHits = searchContent(builder.build(), user, accessMode, maxContentHitCount);
		} else {
			throw new RuntimeException("Unsupported access mode " + accessMode);
		}

		if (!CollectionUtils.isEmpty(contentHits)) {
			HashMap<String, List<ContentHit>> contentHitsMap = new HashMap<String, List<ContentHit>>();

			for (ContentHit hit : contentHits) {
				List<ContentHit> hits = contentHitsMap.get(hit.getType());

				if (hits == null) {
					hits = new ArrayList<ContentHit>();
					contentHitsMap.put(hit.getType(), hits);
				}

				hits.add(hit);
			}

			return contentHitsMap;
		}

		return null;
	}

	private List<ContentHit> searchContent(Query query, User user, SearchAccessMode accessMode, int maxHits) throws IOException {

		//Check if the index contains any documents
		if (indexReader == null || indexReader.numDocs() == 0) {

			return null;
		}

		TopDocs results;

		results = searcher.search(query, maxHits);

		if (results.scoreDocs.length == 0) {

			return null;
		}

		QueryScorer scorer = new QueryScorer(query, SearchConstants.CONTENT_FIELD);
		Highlighter highlighter = new Highlighter(scorer);

		List<ContentHit> filteredHits = new ArrayList<ContentHit>(results.scoreDocs.length);

		for (ScoreDoc scoreDoc : results.scoreDocs) {
			
			org.apache.lucene.document.Document doc = searcher.doc(scoreDoc.doc);
			
			int sectionID = NumberUtils.toInt(doc.get(SearchConstants.SECTION_ID_FIELD_STRING));

			SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);

			if (sectionInterface == null) {

				continue;
			}

			String fragment = null;

			//Get fragment
			TokenStream stream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), scoreDoc.doc, SearchConstants.CONTENT_FIELD, doc, perFieldAnalyzer);

			if (stream != null) {

				Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);

				highlighter.setTextFragmenter(fragmenter);

				String storedField = doc.get(SearchConstants.CONTENT_FIELD);

				try {
					fragment = highlighter.getBestFragment(stream, storedField);
				} catch (InvalidTokenOffsetsException e) {}

				if (StringUtils.isEmpty(fragment)) {

					fragment = StringUtils.substring(storedField, 50, "...");
				}

			} else {

				fragment = StringUtils.substring(doc.get(SearchConstants.CONTENT_FIELD), 50, "...");
			}

			filteredHits.add(new ContentHit(doc.get(SearchConstants.ALIAS_FIELD), doc.get(SearchConstants.TITLE_FIELD), doc.get(SearchConstants.INFO_LINE_FIELD), fragment, doc.get(SearchConstants.ITEM_TYPE_FIELD), sectionInterface.getSectionDescriptor().getName(), sectionInterface.getSectionDescriptor().getFullAlias()));
		}

		if (filteredHits.isEmpty()) {

			return null;
		}

		return filteredHits;
	}
	
	private void appendOpenSearchFilter(BooleanQuery.Builder builder, String type) {

		builder.add(new TermQuery(new Term(SearchConstants.SECTION_ACCESS_MODE_FIELD, SectionAccessMode.OPEN.toString())), Occur.MUST);

		if (type != null) {
			appendTypedFilter(builder, type);
		}
	}

	private void appendRestrictedSearchFilter(BooleanQuery.Builder builder, List<Integer> sectionIDs, String type) {

		BooleanQuery.Builder accessQuery = new BooleanQuery.Builder();

		for (Integer sectionID : sectionIDs) {

			accessQuery.add(IntPoint.newRangeQuery(SearchConstants.SECTION_ID_FIELD, sectionID, sectionID), Occur.SHOULD);
		}
		
		builder.add(accessQuery.build(), Occur.MUST);

		if (type != null) {

			appendTypedFilter(builder, type);
		}
	}

	private void appendTypedFilter(BooleanQuery.Builder builder, String type) {

		builder.add(new TermQuery(new Term(SearchConstants.ITEM_TYPE_FIELD, type)), Occur.MUST);
	}

	private void appendTypeFilter(BooleanQuery.Builder builder, String type) {

		builder.add(new TermQuery(new Term(SearchConstants.ITEM_TYPE_FIELD, type)), Occur.MUST);
	}

	private void indexSections(SectionInterface sectionInterface, boolean recursive) {

		try {

			if (CBSectionAttributeHelper.getSectionTypeID(sectionInterface.getSectionDescriptor()) != null) {

				sectionIndexer.addSection(sectionInterface.getSectionDescriptor());
			}

		} catch (SQLException e) {

			log.error("Unable to get section type for section ID " + sectionInterface.getSectionDescriptor().getSectionID() + " from DB", e);
		}

		if (recursive) {

			for (Section subSection : sectionInterface.getSectionCache().getSections()) {

				indexSections(subSection, true);
			}
		}
	}

	@WebPublic(alias = "search")
	public ForegroundModuleResponse jsonSearch(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		String type = req.getParameter("t");
		String queryString = req.getParameter("q");

		if (queryString != null) {
			queryString = new String(queryString.getBytes(), "UTF-8");
		}

		log.info("User " + user + " searching for: " + StringUtils.toLogFormat(queryString, 50) + " type: " + type);

		JsonObject jsonObject = new JsonObject();

		if (!StringUtils.isEmpty(queryString)) {

			List<ViewFragment> searchFragments = getViewFragments(req, user, type, queryString, SearchMode.JSON);

			if (!CollectionUtils.isEmpty(searchFragments)) {
				JsonArray viewFragments = new JsonArray();

				for (ViewFragment searchFragment : searchFragments) {
					viewFragments.addNode(searchFragment.getHTML());
				}

				jsonObject.putField("viewFragments", viewFragments);
			}
		}

		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);

		return null;
	}

	private SearchAccessMode getSearchAccessMode(User user) {

		if (CBAccessUtils.isExternalUser(user)) {

			return SearchAccessMode.RESTRICTED;

		} else if (this.cbInterface.isGlobalAdmin(user)) {

			return SearchAccessMode.ADMIN;

		} else {

			return SearchAccessMode.DEFAULT;
		}
	}

	public JsonObject searchSection(String queryString, User user, SearchAccessMode accessMode, Integer maxHitCount) throws IOException {

		return sectionIndexer.jsonSearch(queryString, user, accessMode, maxHitCount, cbInterface);
	}

	@Override
	public void sectionCached(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyAlreadyCachedException {

		if (sectionIndexer != null) {

			Lock lock = this.indexLock;

			if (lock != null) {

				lock.lock();
			}

			try {

				if (CBSectionAttributeHelper.getSectionTypeID(sectionDescriptor) != null) {

					sectionIndexer.addSection(sectionDescriptor);
				}

			} catch (Exception e) {

				log.error("Error adding section " + sectionDescriptor + " to index", e);

			} finally {

				if (lock != null) {

					lock.unlock();
				}
			}

		}

	}

	@Override
	public void sectionUpdated(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyNotCachedException {

		if (sectionIndexer != null) {

			Lock lock = this.indexLock;

			if (lock != null) {

				lock.lock();
			}

			try {

				if (CBSectionAttributeHelper.getSectionTypeID(sectionDescriptor) != null) {

					sectionIndexer.updateSection(sectionDescriptor);

				}

			} catch (Exception e) {

				log.error("Error updating index for section " + sectionDescriptor, e);

			} finally {

				if (lock != null) {

					lock.unlock();
				}
			}

		}

	}

	@Override
	public void sectionUnloaded(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyNotCachedException {

		if (sectionIndexer != null) {

			Lock lock = this.indexLock;

			if (lock != null) {

				lock.lock();
			}

			try {

				if (CBSectionAttributeHelper.getSectionTypeID(sectionDescriptor) != null) {

					sectionIndexer.deleteSection(sectionDescriptor);

				}

			} catch (Exception e) {

				log.error("Error removing section " + sectionDescriptor + " from index", e);

			} finally {

				if (lock != null) {

					lock.unlock();
				}
			}

		}

	}

	@Override
	public void moduleCached(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule instance) {

		if (instance instanceof CBSearchable) {

			try {
				//Add module items to index
				List<? extends CBSearchableItem> items = ((CBSearchable) instance).getSearchableItems();

				if (CollectionUtils.isEmpty(items)) {

					log.debug("CBSearchable module " + moduleDescriptor + " returned no items, skipping.");

					return;
				}

				queueAddEvent(moduleDescriptor, items);

			} catch (Exception e) {

				log.error("Error getting searchable items from module " + moduleDescriptor, e);
			}
		}
	}

	@Override
	public void moduleUpdated(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule moduleInstance) {

	}

	@Override
	public void moduleUnloaded(ForegroundModuleDescriptor moduleDescriptor, ForegroundModule instance) {

		if (systemInterface.getSystemStatus() == SystemStatus.STOPPING) {

			//Don't bother clearing the index if the system is stopping

			return;
		}

		if (instance instanceof CBSearchable) {

			queueClearEvent(moduleDescriptor);
		}
	}

	private void queueAddEvent(ForegroundModuleDescriptor moduleDescriptor, List<? extends CBSearchableItem> items) {

		if (moduleDescriptor.getModuleID() == null) {

			log.info("Ignoring add event from module " + moduleDescriptor + " containing " + items.size() + " items since the module has no moduleID set.");
			return;
		}

		log.info("Queued add event from module " + moduleDescriptor + " containing " + items.size() + " items.");
		eventQueue.add(new AddEvent(moduleDescriptor, items));
		checkQueueState(false);
	}

	private synchronized void queueClearEvent(ForegroundModuleDescriptor moduleDescriptor) {

		//Clear any previously queued events from this module
		Iterator<QueuedSearchEvent> iterator = eventQueue.iterator();

		while (iterator.hasNext()) {

			if (iterator.next().getModuleDescriptor().equals(moduleDescriptor)) {

				iterator.remove();
			}
		}

		if (currentEventSource != null && currentEventSource.equals(moduleDescriptor)) {

			threadPoolExecutor.getQueue().clear();
		}

		log.info("Queued clear event from module " + moduleDescriptor);
		eventQueue.add(new ClearModuleEvent(moduleDescriptor));
		checkQueueState(false);
	}

	public org.apache.lucene.document.Document parseItem(ForegroundModuleDescriptor moduleDescriptor, CBSearchableItem item) throws Exception {

		if (logItemParsing) {

			log.info("Parsing item " + item.getTitle() + " (ID: " + item.getID() + ") from module " + moduleDescriptor);
		}

		SimpleSectionDescriptor sectionDescriptor = systemInterface.getCoreDaoFactory().getSectionDAO().getSection(moduleDescriptor.getSectionID(), false);

		if (sectionDescriptor == null || CBSectionAttributeHelper.getSectionTypeID(sectionDescriptor) == null) {

			throw new RuntimeException("Unable to find section type for sectionID " + moduleDescriptor.getSectionID());
		}

		StringWriter writer = new StringWriter();
		ContentHandler contentHandler = new BodyContentHandler(writer);

		Metadata metadata = new Metadata();
		metadata.set(Metadata.RESOURCE_NAME_KEY, item.getTitle());
		metadata.set(Metadata.CONTENT_TYPE, item.getContentType());

		InputStream inputStream = null;

		try {
			inputStream = item.getData();

			if (inputStream == null) {

				return null;
			}

			boolean itemParsed = true;

			try {
				parser.parse(inputStream, contentHandler, metadata);
			} catch (Throwable e) {

				// TODO log error when PDF-box is upgraded
				log.warn("Error parsing item " + item + " from module " + moduleDescriptor, e);

				itemParsed = false;
			}

			org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
			doc.add(new TextField(SearchConstants.TITLE_FIELD, item.getTitle(), Field.Store.YES));

			if (itemParsed) {
				doc.add(new TextField(SearchConstants.CONTENT_FIELD, HTMLUtils.escapeHTML(StringUtils.replaceUTF8Quotes(writer.toString())), Field.Store.YES));
			} else {
				doc.add(new TextField(SearchConstants.CONTENT_FIELD, "", Field.Store.YES));
			}

			doc.add(new StringField(SearchConstants.ITEM_ID_FIELD, item.getID(), Field.Store.YES));
			doc.add(new StringField(SearchConstants.ITEM_TYPE_FIELD, item.getType(), Field.Store.YES));
			doc.add(new StringField(SearchConstants.MODULE_ID_FIELD, moduleDescriptor.getModuleID().toString(), Field.Store.YES));
			doc.add(new IntPoint(SearchConstants.SECTION_ID_FIELD, moduleDescriptor.getSectionID()));
			doc.add(new StringField(SearchConstants.SECTION_ID_FIELD_STRING, moduleDescriptor.getSectionID().toString(), Field.Store.YES));
			doc.add(new StringField(SearchConstants.ALIAS_FIELD, item.getAlias(), Field.Store.YES));
			doc.add(new StringField(SearchConstants.SECTION_ACCESS_MODE_FIELD, CBSectionAttributeHelper.getAccessMode(sectionDescriptor).toString(), Field.Store.YES));

			if (item.getInfoLine() != null) {

				doc.add(new TextField(SearchConstants.INFO_LINE_FIELD, item.getInfoLine(), Field.Store.YES));
			}

			if (item.getTags() != null) {

				for (String tag : item.getTags()) {

					doc.add(new StringField(SearchConstants.TAG_FIELD, tag, Field.Store.NO));
				}
			}

			return doc;

		} finally {

			CloseUtils.close(inputStream);
		}
	}

	@EventListener(channel = CBSearchableItem.class)
	public void processEvent(CBSearchableItemAddEvent event, EventSource source) {

		queueAddEvent(event.getModuleDescriptor(), event.getItems());
	}

	@EventListener(channel = CBSearchableItem.class)
	public void processEvent(CBSearchableItemUpdateEvent event, EventSource source) {

		if (event.getModuleDescriptor().getModuleID() == null) {

			log.info("Ignoring update event from module " + moduleDescriptor + " containing " + event.getItems().size() + " items since the module has no moduleID set.");
			return;
		}

		log.info("Queued update event from module " + event.getModuleDescriptor() + " containing " + event.getItems().size() + " items.");
		eventQueue.add(new UpdateEvent(event.getModuleDescriptor(), event.getItems()));
		checkQueueState(false);
	}

	@EventListener(channel = CBSearchableItem.class)
	public void processEvent(CBSearchableItemDeleteEvent event, EventSource source) {

		if (event.getModuleDescriptor().getModuleID() == null) {

			log.info("Ignored delete event from module " + event.getModuleDescriptor() + " containing " + event.getItemIDs().size() + " item ID's since the module has no moduleID set.");
			return;
		}

		log.info("Queued delete event from module " + event.getModuleDescriptor() + " containing " + event.getItemIDs().size() + " item ID's.");
		eventQueue.add(new DeleteEvent(event.getModuleDescriptor(), event.getItemIDs()));
		checkQueueState(false);
	}

	@EventListener(channel = CBSearchableItem.class)
	public void processEvent(CBSearchableItemClearEvent event, EventSource source) {

		if (event.getModuleDescriptor().getModuleID() == null) {

			log.info("Ignored clear event from module " + event.getModuleDescriptor() + " since the module has no moduleID set.");
			return;
		}

		log.info("Queued clear event from module " + event.getModuleDescriptor());
		queueClearEvent(event.getModuleDescriptor());
	}

	public synchronized void checkQueueState(boolean commit) {

		if (systemInterface.getSystemStatus() != SystemStatus.STARTED || !serverStarted) {

			return;
		}

		synchronized (this) {

			if (threadPoolExecutor.getExecutingThreadCount() == 0 && threadPoolExecutor.getQueue().isEmpty()) {

				try {
					if (commit) {

						log.info("Committing index changes from last event.");
						this.indexWriter.commit();
						this.indexReader = DirectoryReader.open(index);
						this.searcher = new IndexSearcher(indexReader);
					}

					QueuedSearchEvent nextEvent = eventQueue.poll();

					if (nextEvent == null) {

						currentEventSource = null;
						log.info("No queued search events found, thread pool idle.");
						return;
					}

					log.info("Processing " + nextEvent);
					currentEventSource = nextEvent.getModuleDescriptor();

					nextEvent.queueTasks(threadPoolExecutor, this);

				} catch (Throwable e) {
					log.error("Unable to commit index", e);
				}
			}
		}
	}

	public void addDocument(Iterable<? extends IndexableField> doc) throws IOException {

		indexWriter.addDocument(doc);
	}

	public void deleteDocuments(Term... terms) throws IOException {

		BooleanQuery.Builder query = new BooleanQuery.Builder();

		for (Term term : terms) {

			query.add(new TermQuery(term), Occur.MUST);
		}

		indexWriter.deleteDocuments(query.build());
	}

	@ServerStartupListener
	public void serverStarted() throws TransformerConfigurationException, IOException {

		serverStarted = true;

		createSearchPlugins();

		Map<ForegroundModuleDescriptor, CBSearchable> moduleMap = ModuleUtils.findForegroundModules(CBSearchable.class, true, true, systemInterface.getRootSection());

		for (Entry<ForegroundModuleDescriptor, CBSearchable> entry : moduleMap.entrySet()) {

			moduleCached(entry.getKey(), (ForegroundModule) entry.getValue());
		}

		systemInterface.addForegroundModuleCacheListener(this);

		indexLock = new ReentrantLock();

		indexLock.lock();

		try {
			sectionIndexer = new SectionIndexer();

			systemInterface.addSectionCacheListener(this);

			indexSections(systemInterface.getRootSection(), true);

		} finally {
			indexLock.unlock();
			indexLock = null;
		}

		userIndexer = new UserIndexer(userSearchAttributes);

		userIndexer.addUsers(systemInterface.getUserHandler().getUsers(false, true));

		this.initTaskScheduler();

		checkQueueState(false);
	}

	public boolean isValidSystemState() {

		return systemInterface.getSystemStatus() == SystemStatus.STARTED;
	}

	@Override
	public synchronized boolean addSearchPlugin(SearchPlugin searchPlugin) {

		boolean result = searchPlugins.putIfAbsent(searchPlugin.getAlias(), searchPlugin) == null;

		if (result) {
			log.info("Search plugin " + searchPlugin + " added");

			SearchType type = new SearchType();
			type.setType(searchPlugin.getAlias());
			type.setName(searchPlugin.getName());
			type.setPriority(searchPlugin.getPriority());

			searchTypes.add(type);

			Collections.sort(searchTypes, PRIORITY_COMPARATOR);
		}

		log.info("add. searchPlugins.size(): " + searchPlugins.size());

		return result;
	}

	@Override
	public synchronized boolean removeSearchPlugin(SearchPlugin searchPlugin) {

		boolean result = searchPlugins.remove(searchPlugin.getAlias()) != null;

		if (result) {
			log.info("Search plugin " + searchPlugin + " removed");

			Iterator<SearchType> iterator = searchTypes.iterator();

			while (iterator.hasNext()) {
				SearchType type = iterator.next();

				if (type.getType().equals(searchPlugin.getAlias())) {
					iterator.remove();
					break;
				}
			}
		}

		log.info("remove. searchPlugins.size(): " + searchPlugins.size());

		return result;
	}

	public SectionIndexer getSectionIndexer() {

		return sectionIndexer;
	}

	public Transformer getTransformer() throws TransformerConfigurationException {

		return sectionInterface.getForegroundModuleXSLTCache().getModuleTranformer(moduleDescriptor);
	}

	public CBUtilityModule getCBUtilityModule() {

		return cbUtilityModule;
	}

	public CBInterface getCBInterface() {

		return cbInterface;
	}

	public String getEncoding() {

		return systemInterface.getEncoding();
	}

	public TagCache getTagCache() {

		return tagCache;
	}

	public List<SearchType> getSearchTypes() {

		return searchTypes;
	}

	public UserIndexer getUserIndexer() {

		return userIndexer;
	}

	public UserProfileProvider getUserProfileProvider() {

		return userProfileProvider;
	}

	@Override
	public void run() {

		try {
			log.info("Recaching user index...");

			userIndexer.addUsers(systemInterface.getUserHandler().getUsers(false, true));

			log.info("Done recaching user index");
		} catch (IOException e) {
			log.error("Unable to recache user index", e);
		}
	}
}