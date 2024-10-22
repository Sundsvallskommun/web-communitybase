package se.dosf.communitybase.modules.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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

@SuppressWarnings("deprecation")
public class SectionIndexer {

	private static final String ID_FIELD = "sectionID";
	private static final String NAME_FIELD = "name";
	private static final String FULLALIAS_FIELD = "fullAlias";
	private static final String DESCRIPTION_FIELD = "description";
	private static final String ACCESS_MODE_FIELD = "accessMode";

	private static final String[] SEARCH_FIELDS = new String[] { ID_FIELD, NAME_FIELD, DESCRIPTION_FIELD };

	protected Logger log = Logger.getLogger(this.getClass());

	private StandardAnalyzer analyzer = new StandardAnalyzer();
	private Directory index = new RAMDirectory();
	private IndexWriter indexWriter;
	private IndexReader indexReader;
	private IndexSearcher searcher;

	public SectionIndexer() throws IOException {

		indexWriter = new IndexWriter(index, new IndexWriterConfig(analyzer));
	}

	public void addSection(SectionDescriptor sectionDescriptor) {

		try {

			log.debug("Adding search index for section: " + sectionDescriptor);

			Document doc = new Document();
			doc.add(new StoredField(ID_FIELD, sectionDescriptor.getSectionID()));
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

			indexWriter.deleteDocuments(IntPoint.newRangeQuery(ID_FIELD, sectionDescriptor.getSectionID(), sectionDescriptor.getSectionID()));

			commit();

		} catch (Exception e) {

			log.error("Error deleting indexing section " + sectionDescriptor, e);
		}

	}

	public List<SectionHit> search(String queryString, User user, SearchAccessMode accessMode, int maxHitCount, boolean fragments, CBInterface cbInterface) throws IOException {

		if (searcher == null) {

			return null;
		}

		MultiFieldQueryParser parser = new MultiFieldQueryParser(SEARCH_FIELDS, analyzer);
		parser.setDefaultOperator(Operator.AND);

		Query query;

		try {

			queryString = SearchUtils.rewriteQueryString(QueryParser.escape(queryString));

			query = parser.parse(queryString);

		} catch (ParseException e) {

			log.warn("Unable to parse query string " + StringUtils.toLogFormat(queryString, 50) + " when searching for section instances requsted by user " + user);

			return null;
		}

		TopDocs prioResults = null;
		TopDocs results;

		BooleanQuery.Builder builder = new BooleanQuery.Builder();
		builder.add(query, Occur.MUST);

		if (accessMode == SearchAccessMode.RESTRICTED) {

			List<Integer> sectionIDs = CBAccessUtils.getUserSections(user, cbInterface);

			if (sectionIDs == null) {

				return null;
			}

			appendRestrictedSearchFilter(builder, sectionIDs);

			results = searcher.search(builder.build(), maxHitCount);

		} else if (accessMode == SearchAccessMode.DEFAULT) {

			List<Integer> sectionIDs = CBAccessUtils.getUserSections(user, cbInterface);

			if (sectionIDs == null) {

				BooleanQuery.Builder filter = new BooleanQuery.Builder();
				filter.add(new TermQuery(new Term(ACCESS_MODE_FIELD, SectionAccessMode.OPEN.toString())), Occur.SHOULD);
				filter.add(new TermQuery(new Term(ACCESS_MODE_FIELD, SectionAccessMode.CLOSED.toString())), Occur.SHOULD);

				builder.add(filter.build(), Occur.FILTER);

				results = searcher.search(query, maxHitCount);

			} else {

				appendRestrictedSearchFilter(builder, sectionIDs);

				results = searcher.search(builder.build(), maxHitCount);

				if (results.totalHits.value < maxHitCount) {
					prioResults = results;

					BooleanQuery.Builder openBuilder = new BooleanQuery.Builder();
					openBuilder.add(query, Occur.MUST);

					appendSearchFilter(openBuilder, sectionIDs);

					results = searcher.search(openBuilder.build(), maxHitCount - (int) prioResults.totalHits.value);
				}

			}

		} else if (accessMode == SearchAccessMode.ADMIN) {

			results = searcher.search(query, maxHitCount);

		} else {

			throw new RuntimeException("Unsupported search access mode " + accessMode);
		}

		if (results.scoreDocs.length == 0) {

			return null;
		}

		List<SectionHit> hits = getSectionHits(results, fragments, query, cbInterface);

		if (prioResults != null) {
			LinkedHashSet<SectionHit> prioHits = new LinkedHashSet<SectionHit>(hits);

			prioHits.addAll(getSectionHits(prioResults, fragments, query, cbInterface));

			return new ArrayList<SectionHit>(prioHits);
		}

		return hits;
	}

	private List<SectionHit> getSectionHits(TopDocs results, boolean fragments, Query query, CBInterface cbInterface) throws IOException {

		ArrayList<SectionHit> sectionHits = new ArrayList<SectionHit>(results.scoreDocs.length);

		if (fragments) {

			QueryScorer scorer = new QueryScorer(query, DESCRIPTION_FIELD);
			Highlighter highlighter = new Highlighter(scorer);

			for (ScoreDoc scoreDoc : results.scoreDocs) {

				Document doc = searcher.doc(scoreDoc.doc);

				String fragment = null;

				String storedField = doc.get(DESCRIPTION_FIELD);

				if (!StringUtils.isEmpty(storedField)) {

					//Get fragment
					TokenStream stream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), scoreDoc.doc, DESCRIPTION_FIELD, doc, analyzer);

					if (stream != null) {

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

				if (cbInterface != null) {

					memberCount = CollectionUtils.getSize(cbInterface.getSectionMembers(sectionID, true));

				} else {

					memberCount = null;
				}

				sectionHits.add(new SectionHit(sectionID, doc.get(FULLALIAS_FIELD), doc.get(NAME_FIELD), fragment, memberCount, doc.get(ACCESS_MODE_FIELD)));
			}

		} else {

			for (ScoreDoc scoreDoc : results.scoreDocs) {

				Document doc = searcher.doc(scoreDoc.doc);

				sectionHits.add(new SectionHit(Integer.parseInt(doc.get(ID_FIELD)), doc.get(FULLALIAS_FIELD), doc.get(NAME_FIELD), null, null, doc.get(ACCESS_MODE_FIELD)));
			}
		}

		return sectionHits;
	}

	public JsonObject jsonSearch(String queryString, User user, SearchAccessMode accessMode, int maxHitCount, CBInterface cbInterface) throws IOException {

		List<SectionHit> hits = search(queryString, user, accessMode, maxHitCount, false, cbInterface);

		if (hits == null) {

			return returnEmptyResponse();
		}

		JsonArray jsonArray = new JsonArray();

		//Create JSON from hits
		for (SectionHit sectionHit : hits) {

			JsonObject resultJson = new JsonObject();

			resultJson.putField(ID_FIELD, sectionHit.getSectionID());
			resultJson.putField(NAME_FIELD, sectionHit.getName());
			resultJson.putField(FULLALIAS_FIELD, sectionHit.getFullAlias());
			resultJson.putField(ACCESS_MODE_FIELD, sectionHit.getAccessMode());

			jsonArray.addNode(resultJson);
		}

		JsonObject jsonObject = new JsonObject(2);
		jsonObject.putField("hitCount", hits.size());
		jsonObject.putField("hits", jsonArray);

		return jsonObject;
	}

	private void appendSearchFilter(BooleanQuery.Builder builder, List<Integer> sectionIDs) {

		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

		booleanQuery.add(new TermQuery(new Term(ACCESS_MODE_FIELD, SectionAccessMode.OPEN.toString())), Occur.SHOULD);
		booleanQuery.add(new TermQuery(new Term(ACCESS_MODE_FIELD, SectionAccessMode.CLOSED.toString())), Occur.SHOULD);

		for (Integer sectionID : sectionIDs) {

			booleanQuery.add(IntPoint.newRangeQuery(ID_FIELD, sectionID, sectionID), Occur.SHOULD);
		}

		builder.add(booleanQuery.build(), Occur.FILTER);
	}

	protected void appendRestrictedSearchFilter(BooleanQuery.Builder builder, List<Integer> sectionIDs) {

		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();

		for (Integer sectionID : sectionIDs) {

			booleanQuery.add(IntPoint.newRangeQuery(ID_FIELD, sectionID, sectionID), Occur.SHOULD);
		}

		builder.add(booleanQuery.build(), Occur.FILTER);
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
