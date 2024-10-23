package se.dosf.communitybase.modules.search;


public class SearchUtils {

	private static final String[] WILDCARD_EXCLUSIONS = new String[] { "?", "\"", "+", " ", "~","*", "-", "&&", "||", "^", ":"};

	public static String rewriteQueryString(String queryString) {

		for(String exclusion : WILDCARD_EXCLUSIONS){

			if(queryString.contains(exclusion)){

				return queryString;
			}
		}

		return queryString + "*";
	}
}
