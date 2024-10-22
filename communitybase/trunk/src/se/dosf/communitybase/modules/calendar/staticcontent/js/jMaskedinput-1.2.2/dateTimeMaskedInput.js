jQuery(function($){
	$.mask.definitions['@']='[01]';
	$.mask.definitions['£']='[012]';
	$.mask.definitions['#']='[0123]';
	$("#startdate").mask("9999-@9-#9"); 
	
	$.mask.definitions['£']='[012]';
	$.mask.definitions['%']='[012345]';  
	$("#starttime").mask("£9:%9"); 
	$("#endtime").mask("£9:%9");
});