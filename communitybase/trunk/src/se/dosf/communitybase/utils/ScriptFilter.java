package se.dosf.communitybase.utils;

public class ScriptFilter {

	public static boolean validateNoScriptField(String text){
		
		if(text.contains("&lt;link") || text.contains("&lt;script")){
			return false;
		}
		
		return true;
		
	}
	
}
