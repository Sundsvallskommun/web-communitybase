package se.dosf.communitybase.modules.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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

import se.dosf.communitybase.enums.SectionAccessMode;
import se.dosf.communitybase.interfaces.CBInterface;
import se.dosf.communitybase.utils.CBAccessUtils;
import se.dosf.communitybase.utils.CBSectionAttributeHelper;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.SectionDescriptor;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.string.StringUtils;

public class SectionIndexer {

	private static final String ID_FIELD = "sectionID";
	private static final String NAME_FIELD = "name";
	private static final String FULLALIAS_FIELD = "fullAlias";
	private static final String DESCRIPTION_FIELD = "description";
	private static final String ACCESS_MODE_FIELD = "accessMode";

	private static final String[] SEARCH_FIELDS = new String[] { ID_FIELD, NAME_FIELD, DESCRIPTION_FIELD };

	private static final Filter DEFAULT_FILTER;

	static{

		BooleanQuery booleanQuery = new BooleanQuery();

		booleanQuery.add(new TermQuery(new Term(ACCESS_MODE_FIELD, SectionAccessMode.OPEN.toString())), Occur.SHOULD);
		booleanQuery.add(new TermQuery(new Term(ACCESS_MODE_FIELD, SectionAccessMode.CLOSED.toString())), Occur.SHOULD);

		DEFAULT_FILTER = new QueryWrapperFilter(booleanQuery);
	}

	protected Logger log = Logger.getLogger(this.getClass());

	private StandardAnalyzer analyzer = new StandardAnalyzer(Version.LUCENE_44);
	private Directory index = new RAMDirectory();
	private IndexWriter indexWriter;
	private IndexReader indexReader;
	private IndexSearcher searcher;

	public SectionIndexer() throws IOException {

		indexWriter = new IndexWriter(index, new IndexWriterConfig(Version.LUCENE_44, analyzer));
	}

	public void addSection(SectionDescriptor sectionDescriptor) {

		try {

			log.debug("Adding search index for section: " + sectionDescriptor);

			Document doc = new Document();
			doc.add(new IntField(ID_FIELD, sectionDescriptor.getSectionID(), Field.Store.YES));
			doc.add(new TextField(NAME_FIELD, sectionDescriptor.getName(), Field.Store.YES));
			doc.add(new TextField(FULLALIAS_FIELD, sectionDescriptor.getFullAlias(), Field.Store.YES));
			doc.add(new StringField(ACCESS_MODE_FIELD, CBSectionAttributeHelper.getAccessMode(sectionDescriptor).toString(), Field.Store.YES));

			String description = CBSectionAttributeHelper.getDescription(sectionDescriptor);

			if (description != null) {

				doc.add(new TextField(DESCRIPTION_FIELD, description, Field.Store.YES));
			}

			indexWriter.addDocument(doc);

			commit();

		} catch (Exception e) {

			log.error("Error indexing section " + sectionDescriptor, e);
		}

	}

	public void updateSection(SectionDescriptor sectionDescriptor) {

		log.debug("Updating search index for section: " + sectionDescriptor);

		deleteSection(sectionDescriptor);

		addSection(sectionDescriptor);

	}

	public void deleteSection(SectionDescriptor sectionDescriptor) {

		try {

			log.debug("Deleting search index for section: " + sectionDescriptor);

			indexWriter.deleteDocuments(NumericRangeQuery.newIntRange(ID_FIELD, sectionDescriptor.getSectionID(), sectionDescriptor.getSectionID(), true, true));
			
			commit();

		} catch (Exception e) {

			log.error("Error deleting indexing section " + sectionDescriptor, e);
		}

	}

	public List<SectionHit> search(String queryString, User user, SearchAccessMode accessMode, int maxHitCount, boolean fragments, CBInterface cbInterface) throws IOException {

		if(searcher == null){

			return null;
		}

		MultiFieldQueryParser parser = new MultiFieldQueryParser(Version.LUCENE_44, SEARCH_FIELDS, analyzer);

		Query query;

		try {

			query = parser.parse(SearchUtils.rewriteQueryString(queryString));
			//query = parser.parse(queryString);

		} catch (ParseException e) {

			log.warn("Unable to parse query string " + StringUtils.toLogFormat(queryString, 50) + " when searching for section instances requsted by user " + user);

			return null;
		}

		TopDocs results;

		if(accessMode == SearchAccessMode.RESTRICTED){

			List<Integer> sectionIDs = CBAccessUtils.getUserSections(user);

			if(sectionIDs == null){

				return null;
			}

			Filter filter = getRestrictedSearchFilter(sectionIDs);

			results = searcher.search(query, filter, maxHitCount);

		}else if(accessMode == SearchAccessMode.DEFAULT){

			List<Integer> sectionIDs = CBAccessUtils.getUserSections(user);

			if(sectionIDs == null){

				results = searcher.search(query, DEFAULT_FILTER, maxHitCount);

			}else{

				Filter filter = getSearchFilter(sectionIDs);

				results = searcher.search(query, filter, maxHitCount);
			}

		}else if(accessMode == SearchAccessMode.ADMIN){

			results = searcher.search(query, maxHitCount);

		}else{

			throw new RuntimeException("Unsupported search access mode " + accessMode);
		}

		if (results.scoreDocs.length == 0) {

			return null;
		}

		ArrayList<SectionHit> sectionHits = new ArrayList<SectionHit>(results.scoreDocs.length);

		if(fragments){

			QueryScorer scorer = new QueryScorer(query, DESCRIPTION_FIELD);
			Highlighter highlighter = new Highlighter(scorer);

			for (ScoreDoc scoreDoc : results.scoreDocs) {

				Document doc = searcher.doc(scoreDoc.doc);

				String fragment = null;

				String storedField = doc.get(DESCRIPTION_FIELD);

				if(!StringUtils.isEmpty(storedField)) {
				
					//Get fragment
					TokenStream stream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), scoreDoc.doc, DESCRIPTION_FIELD, doc, analyzer);
	
					if(stream != null){
	
						Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
	
						highlighter.setTextFragmenter(fragmenter);
	
	
						try {
							fragment = highlighter.getBestFragment(stream, storedField);
						} catch (InvalidTokenOffsetsException e) {
							fragment = null;
						}
					}
				
				}

				Integer sectionID = Integer.parseInt(doc.get(ID_FIELD));

				Integer memberCount;

				if(cbInterface != null){

					memberCount = CollectionUtils.getSize(cbInterface.getSectionMembers(sectionID));

				}else{

					memberCount = null;
				}

				sectionHits.add(new SectionHit(sectionID, doc.get(FULLALIAS_FIELD), doc.get(NAME_FIELD), fragment, memberCount, doc.get(ACCESS_MODE_FIELD)));
			}

		}else{

			for (ScoreDoc scoreDoc : results.scoreDocs) {

				Document doc = searcher.doc(scoreDoc.doc);

				sectionHits.add(new SectionHit(Integer.parseInt(doc.get(ID_FIELD)), doc.get(FULLALIAS_FIELD), doc.get(NAME_FIELD), null, null, doc.get(ACCESS_MODE_FIELD)));
			}
		}

		return sectionHits;
	}

	public JsonObject jsonSearch(String queryString, User user, SearchAccessMode accessMode, int maxHitCount) throws IOException {

		List<SectionHit> hits = search(queryString, user, accessMode, maxHitCount, false, null);

		if(hits == null){

			return returnEmptyResponse();
		}

		JsonArray jsonArray = new JsonArray();

		//Create JSON from hits
		for (SectionHit sectionHit : hits) {

			JsonObject resultJson = new JsonObject();

			resultJson.putField(ID_FIELD, sectionHit.getSectionID());
			resultJson.putField(NAME_FIELD, sectionHit.getName());
			resultJson.putField(FULLALIAS_FIELD, sectionHit.getFullAlias());

			jsonArray.addNode(resultJson);
		}

		JsonObject jsonObject = new JsonObject(2);
		jsonObject.putField("hitCount", hits.size());
		jsonObject.putField("hits", jsonArray);

		return jsonObject;
	}

	private Filter getSearchFilter(List<Integer> sectionIDs) {

		BooleanQuery booleanQuery = new BooleanQuery();

		booleanQuery.add(new TermQuery(new Term(ACCESS_MODE_FIELD, SectionAccessMode.OPEN.toString())), Occur.SHOULD);
		booleanQuery.add(new TermQuery(new Term(ACCESS_MODE_FIELD, SectionAccessMode.CLOSED.toString())), Occur.SHOULD);

		for(Integer sectionID : sectionIDs){

			booleanQuery.add(NumericRangeQuery.newIntRange(ID_FIELD, sectionID, sectionID, true, true), Occur.SHOULD);
		}

		return new QueryWrapperFilter(booleanQuery);
	}

	protected Filter getRestrictedSearchFilter(List<Integer> sectionIDs) {

		BooleanQuery booleanQuery = new BooleanQuery();

		for(Integer sectionID : sectionIDs){

			booleanQuery.add(NumericRangeQuery.newIntRange(ID_FIELD, sectionID, sectionID, true, true), Occur.SHOULD);
		}

		return new QueryWrapperFilter(booleanQuery);
	}

	public static JsonObject returnEmptyResponse() {

		JsonObject jsonObject = new JsonObject(1);
		jsonObject.putField("hitCount", "0");

		return jsonObject;
	}

	private void commit() throws IOException {

		indexWriter.commit();
		this.indexReader = DirectoryReader.open(index);
		this.searcher = new IndexSearcher(indexReader);

	}

	public void close() {

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

	}
}
