
$(document).ready(function() {
	
	if(!(typeof fckSettings === "undefined")) {
	
		var settings = {
				
			customConfig : fckSettings.customConfig != undefined ? fckSettings.customConfig : null, 
			
			height : fckSettings.editorHeight,
			
			filebrowserBrowseUrl : fckSettings.filebrowserBrowseUri != undefined ? fckSettings.basePath + fckSettings.filebrowserBrowseUri : null,
	        
	        filebrowserImageBrowseUrl : fckSettings.filebrowserImageBrowseUri != undefined ? fckSettings.basePath + fckSettings.filebrowserImageBrowseUri : null,
	       
	        filebrowserFlashBrowseUrl : fckSettings.filebrowserFlashBrowseUri != undefined ? fckSettings.basePath + fckSettings.filebrowserFlashBrowseUri : null
				
		};
		
		if(fckSettings.contentsCss != undefined) {
			settings = $.extend(settings, { contentsCss : fckSettings.contentsCss });
		}
		
		if(fckSettings.toolbar != undefined) {
			settings = $.extend(settings, { toolbar : fckSettings.toolbar });
		}

		$('.' + fckSettings.editorContainerClass).ckeditor( function() { },  settings);
		
	}
	
} );