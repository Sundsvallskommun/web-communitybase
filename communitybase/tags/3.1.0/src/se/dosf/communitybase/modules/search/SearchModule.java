package se.dosf.communitybase.modules.search;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
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
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.QueryWrapperFilter;
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
import org.apache.lucene.util.Version;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.ContentHandler;

import se.dosf.communitybase.CBConstants;
import se.dosf.communitybase.enums.SectionAccessMode;
import se.dosf.communitybase.events.CBSearchableItemAddEvent;
import se.dosf.communitybase.events.CBSearchableItemClearEvent;
import se.dosf.communitybase.events.CBSearchableItemDeleteEvent;
import se.dosf.communitybase.events.CBSearchableItemUpdateEvent;
import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.interfaces.CBSearchable;
import se.dosf.communitybase.interfaces.CBSearchableItem;
import se.dosf.communitybase.interfaces.TagCache;
import se.dosf.communitybase.modules.search.events.AddEvent;
import se.dosf.communitybase.modules.search.events.ClearModuleEvent;
import se.dosf.communitybase.modules.search.events.DeleteEvent;
import se.dosf.communitybase.modules.search.events.QueuedSearchEvent;
import se.dosf.communitybase.modules.search.events.UpdateEvent;
import se.dosf.communitybase.modules.userprofile.UserProfileProvider;
import se.dosf.communitybase.modules.util.CBUtilityModule;
import se.dosf.communitybase.utils.CBAccessUtils;
import se.dosf.communitybase.utils.CBSectionAttributeHelper;
import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EventListener;
import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextAreaSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.SimpleSectionDescriptor;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventSource;
import se.unlogic.hierarchy.core.enums.SystemStatus;
import se.unlogic.hierarchy.core.interfaces.AttributeHandler;
import se.unlogic.hierarchy.core.interfaces.ForegroundModule;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleCacheListener;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MutableSettingHandler;
import se.unlogic.hierarchy.core.interfaces.SectionCacheListener;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.interfaces.SystemStartupListener;
import se.unlogic.hierarchy.core.sections.Section;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.collections.KeyAlreadyCachedException;
import se.unlogic.standardutils.collections.KeyNotCachedException;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.json.JsonUtils;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.streams.StreamUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;


public class SearchModule extends AnnotatedForegroundModule implements SectionCacheListener, ForegroundModuleCacheListener, SystemStartupListener {

	private static final BooleanQuery OPEN_SECTIONS_QUERY;
	private static final Filter OPEN_SECTIONS_FILTER;

	static{

		BooleanQuery booleanQuery = new BooleanQuery();

		booleanQuery.add(new TermQuery(new Term(SearchConstants.SECTION_ACCESS_MODE_FIELD, SectionAccessMode.OPEN.toString())), Occur.SHOULD);

		OPEN_SECTIONS_QUERY = booleanQuery;
		OPEN_SECTIONS_FILTER = new QueryWrapperFilter(booleanQuery);
	}

	private static final String[] DEFAULT_FIELDS = new String[] { SearchConstants.TITLE_FIELD, SearchConstants.CONTENT_FIELD };

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max section hits (json)", description = "Maximum number of hits to get from index when searching for sections using JSON", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int maxJsonSectionHitCount = 5;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max section hits", description = "Maximum number of hits to get from index when searching for sections", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int maxSectionHitCount = 20;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max users hits (json)", description = "Maximum number of hits to get from index when searching for users using JSON", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int maxJsonUserHitCount = 5;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max users hits", description = "Maximum number of hits to get from index when searching for users", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int maxUserHitCount = 20;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max tag hits (json)", description = "Maximum number of hits to get from index when searching for tags using JSONs", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int maxJsonTagsHitCount = 5;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Max tag hits (json)", description = "Maximum number of hits to get from index when searching for tags", formatValidator = PositiveStringIntegerValidator.class, required = true)
	protected int maxTagsHitCount = 20;

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
	@TextAreaSettingDescriptor(name="Item types", description="Item types to listed in the drop down menu")
	protected List<String> listedItemTypes;

	@InstanceManagerDependency(required = true)
	protected CBInterface cbInterface;

	@InstanceManagerDependency(required = true)
	protected TagCache tagCache;

	@InstanceManagerDependency(required=false)
	private UserProfileProvider userProfileProvider;

	@InstanceManagerDependency(required=false)
	private CBUtilityModule cbUtilityModule;

	private Lock indexLock;

	private SectionIndexer sectionIndexer;

	private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
	private Directory index = new RAMDirectory();
	private IndexWriter indexWriter;
	private IndexReader indexReader;
	private IndexSearcher searcher;
	private AutoDetectParser parser = new AutoDetectParser();

	private LinkedBlockingQueue<QueuedSearchEvent> eventQueue = new LinkedBlockingQueue<QueuedSearchEvent>();

	private CallbackThreadPoolExecutor threadPoolExecutor;

	private ForegroundModuleDescriptor currentEventSource;

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface, DataSource dataSource) throws Exception {

		super.init(moduleDescriptor, sectionInterface, dataSource);

		indexWriter = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_44, analyzer));

		Map<ForegroundModuleDescriptor, CBSearchable> moduleMap = ModuleUtils.findForegroundModules(CBSearchable.class, true, true, systemInterface.getRootSection());

		for (Entry<ForegroundModuleDescriptor, CBSearchable> entry : moduleMap.entrySet()) {

			moduleCached(entry.getKey(), (ForegroundModule) entry.getValue());
		}

		systemInterface.addForegroundModuleCacheListener(this);

		if (systemInterface.getSystemStatus() != SystemStatus.STARTED) {

			systemInterface.addStartupListener(this);
		}

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

		if (!systemInterface.getInstanceHandler().addInstance(SearchModule.class, this)) {

			log.warn("Unable to register module " + moduleDescriptor + " in instance handler, another module is already registered for class " + SearchModule.class.getName());
		}
	}

	@Override
	public void update(ForegroundModuleDescriptor descriptor, DataSource dataSource) throws Exception {

		super.update(descriptor, dataSource);
	}

	@Override
	public void unload() throws Exception {

		sectionIndexer.close();

		sectionIndexer = null;

		systemInterface.getInstanceHandler().removeInstance(SearchModule.class, this);

		systemInterface.removeForegroundModuleCacheListener(this);

		if(threadPoolExecutor != null){

			threadPoolExecutor.shutdownNow();
			threadPoolExecutor.awaitTermination(30, TimeUnit.SECONDS);
		}

		this.eventQueue.clear();

		if(threadPoolExecutor != null){

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

		super.unload();
	}

	@Override
	protected void parseSettings(MutableSettingHandler mutableSettingHandler) throws Exception {

		super.parseSettings(mutableSettingHandler);

		if (threadPoolExecutor == null) {

			threadPoolExecutor = new CallbackThreadPoolExecutor(poolSize, poolSize, 30, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), this);

		} else {
			threadPoolExecutor.setMaximumPoolSize(poolSize);
			threadPoolExecutor.setCorePoolSize(poolSize);
		}
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		String type = req.getParameter("t");
		String queryString = req.getParameter("q");

		Document doc = XMLUtils.createDomDocument();

		Element documentElement = doc.createElement("Document");
		doc.appendChild(documentElement);

		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));

		Element searchElement = doc.createElement("Search");
		documentElement.appendChild(searchElement);

		XMLUtils.appendNewCDATAElement(doc, searchElement, "Type", type);

		if(!StringUtils.isEmpty(queryString)){

			log.info("User " + user + " searching for: " + StringUtils.toLogFormat(queryString, 50) + " type: " + type);

			XMLUtils.appendNewCDATAElement(doc, searchElement, "Query", queryString);

			SearchAccessMode accessMode = getSearchAccessMode(user);

			List<Integer> sectionIDs = CBAccessUtils.getUserSections(user);

			if(StringUtils.isEmpty(type)){

				//Do a full search

				//Get sections
				XMLUtils.append(doc, searchElement, "SectionHits", sectionIndexer.search(queryString, user, accessMode, maxSectionHitCount, true, cbInterface));

				if(cbUtilityModule != null) {
					XMLUtils.appendNewElement(doc, documentElement, "SectionLogoURI", req.getContextPath() + cbUtilityModule.getFullAlias());
				}

				if(accessMode != SearchAccessMode.RESTRICTED){

					//Get users
					List<User> users = systemInterface.getUserHandler().searchUsers(queryString, false, true, maxUserHitCount);

					appendUsers(doc, searchElement, users);
				}

				//Get tags
				XMLUtils.append(doc, searchElement, "TagHits", "Tag", tagCache.getMatchingTags(queryString, maxTagsHitCount));

				//Get content
				if(accessMode == SearchAccessMode.RESTRICTED){

					if(sectionIDs != null){

						XMLUtils.append(doc, searchElement, "ContentHits", searchContent(queryString, user, accessMode, maxContentHitCount, getRestrictedSearchFilter(sectionIDs, null), DEFAULT_FIELDS));
					}

				}else if(accessMode == SearchAccessMode.DEFAULT){

					if(sectionIDs == null){

						XMLUtils.append(doc, searchElement, "ContentHits", searchContent(queryString, user, accessMode, maxContentHitCount, OPEN_SECTIONS_FILTER, DEFAULT_FIELDS));

					}else{

						XMLUtils.append(doc, searchElement, "ContentHits", searchContent(queryString, user, accessMode, maxContentHitCount, getDefaultSearchFilter(sectionIDs, null), DEFAULT_FIELDS));
					}

				}else if(accessMode == SearchAccessMode.ADMIN){

					XMLUtils.append(doc, searchElement, "ContentHits", searchContent(queryString, user, accessMode, maxContentHitCount, null, DEFAULT_FIELDS));

				}else{

					throw new RuntimeException("Unsupported access mode " + accessMode);
				}

			}else if(type.equals("users")){

				//Search only for users, ignore search of user is external
				if(accessMode != SearchAccessMode.RESTRICTED){

					List<User> users = systemInterface.getUserHandler().searchUsers(queryString, false, true, maxUserHitCount);

					appendUsers(doc, searchElement, users);
				}

			}else if(type.equals("sections")){

				//Search only for sections
				XMLUtils.append(doc, searchElement, "SectionHits", sectionIndexer.search(queryString, user, accessMode, maxSectionHitCount, true, cbInterface));

				if(cbUtilityModule != null) {
					XMLUtils.appendNewElement(doc, documentElement, "SectionLogoURI", req.getContextPath() + cbUtilityModule.getFullAlias());
				}

			}else if(type.equals("tags")){

				//Search only for tags
				XMLUtils.append(doc, searchElement, "TagHits", "Tag", tagCache.getMatchingTags(queryString, maxTagsHitCount));

			}else if(type.equals("tag")){

				//Get content by tag
				if(accessMode == SearchAccessMode.RESTRICTED){

					if(sectionIDs != null){

						XMLUtils.append(doc, searchElement, "ContentHits", searchContent(queryString, user, accessMode, maxContentHitCount, getRestrictedSearchFilter(sectionIDs, null), SearchConstants.TAG_FIELD));
					}

				}else if(accessMode == SearchAccessMode.DEFAULT){

					if(sectionIDs == null){

						XMLUtils.append(doc, searchElement, "ContentHits", searchContent(queryString, user, accessMode, maxContentHitCount, OPEN_SECTIONS_FILTER, SearchConstants.TAG_FIELD));

					}else{

						XMLUtils.append(doc, searchElement, "ContentHits", searchContent(queryString, user, accessMode, maxContentHitCount, getDefaultSearchFilter(sectionIDs, null), SearchConstants.TAG_FIELD));
					}

				}else if(accessMode == SearchAccessMode.ADMIN){

					XMLUtils.append(doc, searchElement, "ContentHits", searchContent(queryString, user, accessMode, maxContentHitCount, null, SearchConstants.TAG_FIELD));

				}else{

					throw new RuntimeException("Unsupported access mode " + accessMode);
				}

			}else{

				//Search content restricted to certain type
				if(accessMode == SearchAccessMode.RESTRICTED){

					if(sectionIDs != null){

						XMLUtils.append(doc, searchElement, "ContentHits", searchContent(queryString, user, accessMode, maxContentHitCount, getRestrictedSearchFilter(sectionIDs, type), DEFAULT_FIELDS));
					}

				}else if(accessMode == SearchAccessMode.DEFAULT){

					if(sectionIDs == null){

						XMLUtils.append(doc, searchElement, "ContentHits", searchContent(queryString, user, accessMode, maxContentHitCount, getTypedFilter(OPEN_SECTIONS_QUERY, type), DEFAULT_FIELDS));

					}else{

						XMLUtils.append(doc, searchElement, "ContentHits", searchContent(queryString, user, accessMode, maxContentHitCount, getDefaultSearchFilter(sectionIDs, type), DEFAULT_FIELDS));
					}

				}else if(accessMode == SearchAccessMode.ADMIN){

					XMLUtils.append(doc, searchElement, "ContentHits", searchContent(queryString, user, accessMode, maxContentHitCount, getTypeFilter(type), DEFAULT_FIELDS));

				}else{

					throw new RuntimeException("Unsupported access mode " + accessMode);
				}
			}

			return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());

		}

		log.info("User " + user + " requested search form");

		return new SimpleForegroundModuleResponse(doc, moduleDescriptor.getName(), getDefaultBreadcrumb());
	}


	private void appendUsers(Document doc, Element searchElement, List<User> users) {

		if(users != null){

			if(userProfileProvider != null) {

				XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "ProfileImageAlias", userProfileProvider.getProfileImageAlias());
				XMLUtils.appendNewElement(doc, doc.getDocumentElement(), "ShowProfileAlias", userProfileProvider.getShowProfileAlias());
			}

			Element userHitsElement = XMLUtils.appendNewElement(doc, searchElement, "UserHits");

			for(User user : users){

				Element userElement = user.toXML(doc);
				userHitsElement.appendChild(userElement);

				AttributeHandler attributeHandler = user.getAttributeHandler();

				if(attributeHandler != null){

					userElement.appendChild(attributeHandler.toXML(doc));
				}
			}
		}
	}

	private List<ContentHit> searchContent(String queryString, User user, SearchAccessMode accessMode, int maxHits, Filter filter, String... fields) throws IOException {

		//Check if the index contains any documents
		if (indexReader == null || indexReader.numDocs() == 0) {

			return null;
		}

		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_44, fields, analyzer);

		Query query;

		try {
			query = parser.parse(SearchUtils.rewriteQueryString(queryString));

		} catch (ParseException e) {

			log.warn("Unable to parse query string " + StringUtils.toLogFormat(queryString, 50) + " requsted by user " + user);

			return null;
		}

		TopDocs results;

		if(filter != null){

			results = searcher.search(query, filter, maxHits);

		}else{

			results = searcher.search(query, maxHits);
		}

		if (results.scoreDocs.length == 0) {

			return null;
		}

		QueryScorer scorer = new QueryScorer(query, SearchConstants.CONTENT_FIELD);
		Highlighter highlighter = new Highlighter(scorer);

		List<ContentHit> filteredHits = new ArrayList<ContentHit>(results.scoreDocs.length);

		for (ScoreDoc scoreDoc : results.scoreDocs) {

			org.apache.lucene.document.Document doc = searcher.doc(scoreDoc.doc);

			int sectionID = NumberUtils.toInt(doc.get(SearchConstants.SECTION_ID_FIELD));

			SectionInterface sectionInterface = systemInterface.getSectionInterface(sectionID);

			if (sectionInterface == null) {

				continue;
			}

			String fragment = null;

			//Get fragment
			TokenStream stream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), scoreDoc.doc, SearchConstants.CONTENT_FIELD, doc, analyzer);

			if(stream != null){

				Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);

				highlighter.setTextFragmenter(fragmenter);

				String storedField = doc.get(SearchConstants.CONTENT_FIELD);

				try {
					fragment = highlighter.getBestFragment(stream, storedField);
				} catch (InvalidTokenOffsetsException e) {}

				if(StringUtils.isEmpty(fragment)){

					fragment = StringUtils.substring(storedField, 50, "...");
				}

			}else{

				fragment = StringUtils.substring(doc.get(SearchConstants.CONTENT_FIELD), 50, "...");
			}

			filteredHits.add(new ContentHit(doc.get(SearchConstants.ALIAS_FIELD), doc.get(SearchConstants.TITLE_FIELD), doc.get(SearchConstants.INFO_LINE_FIELD), fragment, doc.get(SearchConstants.ITEM_TYPE_FIELD), sectionInterface.getSectionDescriptor().getName(), sectionInterface.getSectionDescriptor().getFullAlias()));
		}

		if (filteredHits.isEmpty()) {

			return null;
		}

		return filteredHits;
	}

	private Filter getDefaultSearchFilter(List<Integer> sectionIDs, String type) {

		BooleanQuery accessQuery = new BooleanQuery();

		accessQuery.add(new TermQuery(new Term(SearchConstants.SECTION_ACCESS_MODE_FIELD, SectionAccessMode.OPEN.toString())), Occur.SHOULD);

		for(Integer sectionID : sectionIDs){

			accessQuery.add(NumericRangeQuery.newIntRange(SearchConstants.SECTION_ID_FIELD, sectionID, sectionID, true, true), Occur.SHOULD);
		}

		if(type != null){

			return getTypedFilter(accessQuery, type);
		}

		return new QueryWrapperFilter(accessQuery);
	}

	private Filter getRestrictedSearchFilter(List<Integer> sectionIDs, String type) {

		BooleanQuery accessQuery = new BooleanQuery();

		for(Integer sectionID : sectionIDs){

			accessQuery.add(NumericRangeQuery.newIntRange(SearchConstants.SECTION_ID_FIELD, sectionID, sectionID, true, true), Occur.SHOULD);
		}

		if(type != null){

			return getTypedFilter(accessQuery, type);
		}

		return new QueryWrapperFilter(accessQuery);
	}

	private Filter getTypedFilter(BooleanQuery accessQuery, String type) {

		BooleanQuery typeQuery = new BooleanQuery();
		typeQuery.add(new TermQuery(new Term(SearchConstants.ITEM_TYPE_FIELD, type)), Occur.MUST);

		BooleanQuery combinedQuery = new BooleanQuery();
		combinedQuery.add(accessQuery, Occur.MUST);
		combinedQuery.add(typeQuery, Occur.MUST);

		return new QueryWrapperFilter(combinedQuery);
	}

	private Filter getTypeFilter(String type){

		BooleanQuery booleanQuery = new BooleanQuery();

		booleanQuery.add(new TermQuery(new Term(SearchConstants.ITEM_TYPE_FIELD, type)), Occur.MUST);

		return new QueryWrapperFilter(booleanQuery);
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

		String queryString = req.getParameter("q");
		
		if(queryString != null){
			queryString = new String(queryString.getBytes(), "UTF-8");
		}
		
		log.info("User " + user + " searching for: " + StringUtils.toLogFormat(queryString, 50));

		JsonObject jsonObject = new JsonObject();

		if (!StringUtils.isEmpty(queryString)) {

			SearchAccessMode accessMode = getSearchAccessMode(user);

			if(accessMode == SearchAccessMode.RESTRICTED){

				//Users is external, only search for rooms
				JsonObject sectionResult = this.sectionIndexer.jsonSearch(queryString, user, SearchAccessMode.RESTRICTED, maxJsonSectionHitCount);

				jsonObject.putField("sectionResult", sectionResult);

			}else{

				//Do a full search for open/closed sections and users
				JsonObject sectionResult = this.sectionIndexer.jsonSearch(queryString, user, accessMode, maxJsonSectionHitCount);

				jsonObject.putField("sectionResult", sectionResult);

				JsonObject usersResult = this.searchUsers(queryString);

				jsonObject.putField("users", usersResult);
			}

			appendTags(jsonObject, queryString);
		}

		HTTPUtils.sendReponse(jsonObject.toJson(), JsonUtils.getContentType(), res);

		return null;

	}

	private SearchAccessMode getSearchAccessMode(User user) {

		if(CBAccessUtils.isExternalUser(user)){

			return SearchAccessMode.RESTRICTED;

		}else if(this.cbInterface.isGlobalAdmin(user)){

			return SearchAccessMode.ADMIN;

		}else{

			return SearchAccessMode.DEFAULT;
		}
	}

	private void appendTags(JsonObject targetJsonObject, String queryString) {

		TagCache tagCache = this.tagCache;

		if(tagCache != null){

			List<String> tags = tagCache.getMatchingTags(queryString, maxJsonTagsHitCount);

			JsonArray jsonArray = new JsonArray();

			if(tags != null){

				for (String tag : tags) {

					jsonArray.addNode(tag);
				}
			}

			JsonObject jsonObject = new JsonObject(2);
			jsonObject.putField("hitCount", CollectionUtils.getSize(tags));
			jsonObject.putField("hits", jsonArray);

			targetJsonObject.putField("tags", jsonObject);
		}
	}

	private JsonObject searchUsers(String queryString) {

		List<User> users = systemInterface.getUserHandler().searchUsers(queryString, false, true, maxJsonUserHitCount);

		JsonArray jsonArray = new JsonArray();

		if(users != null){

			//Create JSON from hits
			for (User user : users) {

				JsonObject resultJson = new JsonObject();

				resultJson.putField("ID", user.getUserID().toString());

				String organization = getOrganization(user);

				if(organization != null){

					resultJson.putField("Name", user.getFirstname() + " " + user.getLastname() + " (" + organization + ")");

				}else{

					resultJson.putField("Name", user.getFirstname() + " " + user.getLastname());
				}

				resultJson.putField("Email", user.getEmail());
				resultJson.putField("Username", user.getUsername());
				resultJson.putField("FullAlias", userProfileProvider.getShowProfileAlias() + "/" + user.getUserID());

				jsonArray.addNode(resultJson);
			}
		}

		JsonObject jsonObject = new JsonObject(2);
		jsonObject.putField("hitCount", CollectionUtils.getSize(users));
		jsonObject.putField("hits", jsonArray);

		return jsonObject;
	}

	private String getOrganization(User user) {

		AttributeHandler attributeHandler = user.getAttributeHandler();

		if(attributeHandler != null){

			return attributeHandler.getString(CBConstants.USER_ORGANIZATION_NAME_ATTRIBUTE);
		}

		return null;
	}

	@Override
	public void sectionCached(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyAlreadyCachedException {

		if(sectionIndexer != null) {

			Lock lock = this.indexLock;

			if (lock != null) {

				lock.lock();
			}

			try {

				if (CBSectionAttributeHelper.getSectionTypeID(sectionDescriptor) != null) {

					sectionIndexer.addSection(sectionDescriptor);
				}

			} catch(Exception e) {

				log.error("Error adding section " + sectionDescriptor + " to index",e);

			} finally {

				if (lock != null) {

					lock.unlock();
				}
			}

		}

	}

	@Override
	public void sectionUpdated(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyNotCachedException {

		if(sectionIndexer != null) {

			Lock lock = this.indexLock;

			if (lock != null) {

				lock.lock();
			}

			try {

				if (CBSectionAttributeHelper.getSectionTypeID(sectionDescriptor) != null) {

					sectionIndexer.updateSection(sectionDescriptor);

				}

			} catch(Exception e) {

				log.error("Error updating index for section " + sectionDescriptor,e);

			} finally {

				if (lock != null) {

					lock.unlock();
				}
			}

		}

	}

	@Override
	public void sectionUnloaded(SectionDescriptor sectionDescriptor, Section sectionInstance) throws KeyNotCachedException {

		if(sectionIndexer != null) {

			Lock lock = this.indexLock;

			if (lock != null) {

				lock.lock();
			}

			try {

				if (CBSectionAttributeHelper.getSectionTypeID(sectionDescriptor) != null) {

					sectionIndexer.deleteSection(sectionDescriptor);

				}

			} catch(Exception e) {

				log.error("Error removing section " + sectionDescriptor + " from index",e);

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

		if(moduleDescriptor.getModuleID() == null){

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

		if(logItemParsing){

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

			parser.parse(inputStream, contentHandler, metadata);

			org.apache.lucene.document.Document doc = new org.apache.lucene.document.Document();
			doc.add(new TextField(SearchConstants.TITLE_FIELD, item.getTitle(), Field.Store.YES));

			doc.add(new TextField(SearchConstants.CONTENT_FIELD, StringUtils.replaceUTF8Quotes(writer.toString()), Field.Store.YES));
			doc.add(new StringField(SearchConstants.ITEM_ID_FIELD, item.getID(), Field.Store.YES));
			doc.add(new StringField(SearchConstants.ITEM_TYPE_FIELD, item.getType(), Field.Store.YES));
			doc.add(new StringField(SearchConstants.MODULE_ID_FIELD, moduleDescriptor.getModuleID().toString(), Field.Store.YES));
			doc.add(new IntField(SearchConstants.SECTION_ID_FIELD, moduleDescriptor.getSectionID(), Field.Store.YES));
			doc.add(new StringField(SearchConstants.ALIAS_FIELD, item.getAlias(), Field.Store.YES));
			doc.add(new StringField(SearchConstants.SECTION_ACCESS_MODE_FIELD, CBSectionAttributeHelper.getAccessMode(sectionDescriptor).toString(), Field.Store.YES));

			if(item.getInfoLine() != null){

				doc.add(new TextField(SearchConstants.INFO_LINE_FIELD, item.getInfoLine(), Field.Store.YES));
			}

			if(item.getTags() != null){

				for(String tag : item.getTags()){

					doc.add(new StringField(SearchConstants.TAG_FIELD, tag, Field.Store.NO));
				}
			}

			return doc;

		} finally {

			StreamUtils.closeStream(inputStream);
		}
	}

	@EventListener(channel=CBSearchableItem.class)
	public void processEvent(CBSearchableItemAddEvent event, EventSource source) {

		queueAddEvent(event.getModuleDescriptor(), event.getItems());
	}

	@EventListener(channel=CBSearchableItem.class)
	public void processEvent(CBSearchableItemUpdateEvent event, EventSource source) {

		if(event.getModuleDescriptor().getModuleID() == null){

			log.info("Ignoring update event from module " + moduleDescriptor + " containing " + event.getItems().size() + " items since the module has no moduleID set.");
			return;
		}

		log.info("Queued update event from module " + event.getModuleDescriptor() + " containing " + event.getItems().size() + " items.");
		eventQueue.add(new UpdateEvent(event.getModuleDescriptor(), event.getItems()));
		checkQueueState(false);
	}

	@EventListener(channel=CBSearchableItem.class)
	public void processEvent(CBSearchableItemDeleteEvent event, EventSource source) {

		if(event.getModuleDescriptor().getModuleID() == null){

			log.info("Ignored delete event from module " + event.getModuleDescriptor() + " containing " + event.getItemIDs().size() + " item ID's since the module has no moduleID set.");
			return;
		}

		log.info("Queued delete event from module " + event.getModuleDescriptor() + " containing " + event.getItemIDs().size() + " item ID's.");
		eventQueue.add(new DeleteEvent(event.getModuleDescriptor(), event.getItemIDs()));
		checkQueueState(false);
	}

	@EventListener(channel=CBSearchableItem.class)
	public void processEvent(CBSearchableItemClearEvent event, EventSource source) {

		if(event.getModuleDescriptor().getModuleID() == null){

			log.info("Ignored clear event from module " + event.getModuleDescriptor() + " since the module has no moduleID set.");
			return;
		}

		log.info("Queued clear event from module " + event.getModuleDescriptor());
		queueClearEvent(event.getModuleDescriptor());
	}

	public synchronized void checkQueueState(boolean commit) {

		if (systemInterface.getSystemStatus() != SystemStatus.STARTED) {

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

				} catch (IOException e) {
					log.error("Unable to commit index", e);
				}
			}
		}
	}

	public void addDocument(Iterable<? extends IndexableField> doc) throws IOException {

		indexWriter.addDocument(doc);
	}

	public void deleteDocuments(Term... terms) throws IOException {

		BooleanQuery query = new BooleanQuery();

		for (Term term : terms) {

			query.add(new TermQuery(term), Occur.MUST);
		}

		indexWriter.deleteDocuments(query);
	}

	@Override
	public void systemStarted() {

		checkQueueState(false);
	}

	public boolean isValidSystemState() {

		return systemInterface.getSystemStatus() == SystemStatus.STARTED;
	}
}
