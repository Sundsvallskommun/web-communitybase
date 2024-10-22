package se.dosf.communitybase.modules.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.QueryParser.Operator;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import se.dosf.communitybase.CBConstants;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.collections.CollectionUtils;
import se.unlogic.standardutils.string.StringUtils;

@SuppressWarnings("deprecation")
public class UserIndexer {

	private static final String ID_FIELD = "userID";
	private static final String FULLNAME_FIELD = "fullname";
	private static final String EXTERNAL_USER_FIELD = "externalUser";
	private static final String DESCRIPTION_FIELD = "description";
	private static final String EMAIL_FIELD = "email";
	private static final String ORGANIZATION_FIELD = "organization";
	private static final String ATTRIBUTEVALUES_FIELD = "attributeValues";
	private static final String ATTRIBUTE_FIELD_PREFIX = "attribute-";

	private static final String[] SEARCH_FIELDS = new String[] { FULLNAME_FIELD, EMAIL_FIELD, DESCRIPTION_FIELD, ORGANIZATION_FIELD, ATTRIBUTEVALUES_FIELD };

	protected Logger log = Logger.getLogger(this.getClass());

	private StandardAnalyzer analyzer = new StandardAnalyzer();
	private Directory index = new RAMDirectory();
	private IndexWriter indexWriter;
	private IndexReader indexReader;
	private IndexSearcher searcher;
	private List<String> attributeNames;

	public UserIndexer(List<String> attributeNames) throws IOException {

		this.attributeNames = attributeNames;
		indexWriter = new IndexWriter(index, new IndexWriterConfig(analyzer));
	}

	public List<UserHit> search(String queryString, User user, int maxHitCount) throws IOException {

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

		TopDocs results = searcher.search(query, maxHitCount);

		if (results.scoreDocs.length == 0) {

			return null;
		}

		return getUserHits(results, query);
	}

	private List<UserHit> getUserHits(TopDocs results, Query query) throws IOException {

		ArrayList<UserHit> userHits = new ArrayList<UserHit>(results.scoreDocs.length);

		for (ScoreDoc scoreDoc : results.scoreDocs) {

			Document doc = searcher.doc(scoreDoc.doc);

			userHits.add(new UserHit(Integer.parseInt(doc.get(ID_FIELD)), doc.get(FULLNAME_FIELD), doc.get(EMAIL_FIELD), doc.get(ORGANIZATION_FIELD), doc.get(EXTERNAL_USER_FIELD) != null, getAttributeHandler(doc)));
		}

		return userHits;
	}

	private List<String> getAttributeHandler(Document doc) {

		List<String> attributes = null;

		if (!CollectionUtils.isEmpty(attributeNames)) {
			for (String attributeName : attributeNames) {
				String attributeValue = doc.get(ATTRIBUTE_FIELD_PREFIX + attributeName);

				if (!StringUtils.isEmpty(attributeValue)) {
					attributes = CollectionUtils.addAndInstantiateIfNeeded(attributes, attributeValue);
				}
			}
		}

		return attributes;
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

	public void addUsers(ArrayList<User> users) throws IOException {

		clearIndex();

		if (!CollectionUtils.isEmpty(users)) {
			for (User user : users) {
				if (!user.isEnabled()) {
					continue;
				}

				try {
					log.debug("Adding user to index: " + user);

					Document doc = new Document();
					doc.add(new StoredField(ID_FIELD, user.getUserID()));
					doc.add(new TextField(FULLNAME_FIELD, user.getFirstname() + " " + user.getLastname(), Field.Store.YES));

					if (user.getEmail() != null) {
						doc.add(new TextField(EMAIL_FIELD, user.getEmail(), Field.Store.YES));
					}

					if (user.getAttributeHandler() != null) {
						String description = user.getAttributeHandler().getString(CBConstants.USER_DESCRIPTION_ATTRIBUTE);

						if (!StringUtils.isEmpty(description)) {
							doc.add(new TextField(DESCRIPTION_FIELD, description, Field.Store.YES));
						}

						String organization = user.getAttributeHandler().getString(CBConstants.USER_ORGANIZATION_NAME_ATTRIBUTE);

						if (!StringUtils.isEmpty(organization)) {
							doc.add(new TextField(ORGANIZATION_FIELD, organization, Field.Store.YES));
						}

						if (user.getAttributeHandler().isSet(CBConstants.EXTERNAL_USER_ATTRIBUTE)) {
							doc.add(new TextField(EXTERNAL_USER_FIELD, "true", Field.Store.YES));
						}

						if (!CollectionUtils.isEmpty(attributeNames)) {
							StringBuilder sb = new StringBuilder();

							for (String attributeName : attributeNames) {
								String attributeValue = user.getAttributeHandler().getString(attributeName);

								if (!StringUtils.isEmpty(attributeValue)) {
									doc.add(new TextField(ATTRIBUTE_FIELD_PREFIX + attributeName, attributeValue, Field.Store.YES));
									sb.append(attributeValue);
								}
							}

							if (sb.length() > 0) {
								doc.add(new TextField(ATTRIBUTEVALUES_FIELD, sb.toString(), Field.Store.YES));
							}
						}
					}

					indexWriter.addDocument(doc);

					commit();

				} catch (Exception e) {

					log.error("Error indexing user " + user, e);
				}
			}
		}
	}

	public void clearIndex() throws IOException {

		indexWriter.deleteAll();

		commit();
	}

	public void setAttributeNames(List<String> attributeNames) {

		this.attributeNames = attributeNames;
	}
}