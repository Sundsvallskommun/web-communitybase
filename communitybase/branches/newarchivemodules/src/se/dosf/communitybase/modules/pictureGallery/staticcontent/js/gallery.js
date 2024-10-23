var REQUIRED_FLASH_VERSION = "9.0.24";
var noFlashURI;
var swfURI;
var uploadURI;
var imageURI;
var buttonImage;
var sizeLimit;

$(document).ready(function() {
	
	if (noFlashURI != undefined && !swfobject.hasFlashPlayerVersion(REQUIRED_FLASH_VERSION)) {
		window.location = noFlashURI;
	}
	
	$("#file_upload").uploadify({
		"uploader"  : swfURI,
		"script"    : uploadURI,
		"cancelImg" : imageURI + "/cancel.png",
		"queueID" : "custom-queue",
		"sizeLimit" : sizeLimit,
		"fileExt" : "*.jpg;*.jpeg;*.gif;*.bmp;*.png",
		"fileDesc" : language.onlyImages + " (.jpg, .jpeg, .gif, .bmp, .png)",
		"auto"      : false,
		"multi"     : true,
		"buttonImg" : imageURI + "/" + buttonImage,
		"height" : 24,
		"width" : 89,
		"completedText" : language.complete,
		"onInit" : function() {
			$("#queue-message").html(language.queueEmptyMessage);
		},
		"onSelectOnce" : function(event,data) {
			
			var $queueMessage = $("#queue-message");
			
			$queueMessage.removeClass("green red");
			$queueMessage.html(language.queueMessage.replace("%p", data.fileCount));
			
			$("#upload_button, #clear_button").removeAttr("disabled");
			
		},
		"onAllComplete" : function(event,data) {
			
			var $queueMessage = $("#queue-message");
			
			if(data.filesUploaded == 1) {
				$queueMessage.html(language.uploadAllCompleteSingular.replace("%p", data.filesUploaded)).addClass("green");
			} else {
				$queueMessage.html(language.uploadAllCompletePlural.replace("%p", data.filesUploaded)).addClass("green");
			}
			
			window.setTimeout(function() {clearQueue($queueMessage);}, 2000);
			
		},
		"onOpen" : function(event,ID,fileObj) {
		     $('#queue-message').html(language.uploadOpen.replace("%p", fileObj.name) + "...");
	    },
		"onClearQueue" : function(event) {
			
			var $queueMessage = $("#queue-message");
			
			$queueMessage.html(language.queueCleared).addClass("red");
			
			window.setTimeout(function() {clearQueue($queueMessage);}, 2000);
			
		},
		"onCancel" : function(event,ID,fileObj,data) {
			
			if(data.fileCount == 0) {
				clearQueue($("#queue-message"));
			} else {
				$("#queue-message").html(language.queueMessage.replace("%p", data.fileCount));
			}
			
		},
		"onError" : function (event,ID,fileObj,errorObj) {
			
			if(errorObj.type == "File Size") {
				
				var $item = $("#file_upload" + ID);
				
				$item.find(".percentage").text(" - " + language[errorObj.type]);
				$item.find(".uploadifyProgress").hide();
				$item.addClass("uploadifyError");
				
				return false;
			}
			
		}
	});
	
	var dropdown = $("#category-dropdown");
	
	if(dropdown.length > 0){
		var addCategory = $("#new-category");
		
		if($("#auto-select-new-category").length > 0){
			dropdown.val("_new");
			showOrHideAddCategory(dropdown, addCategory, false);
		}
		
		dropdown.change(function(){
			showOrHideAddCategory(dropdown, addCategory, true);
		});
	}
	
});

function showOrHideAddCategory(dropdown, addCategory, clear){
	if(dropdown.val() === "_new"){
		addCategory.toggleClass("hidden", false);
		addCategory.prop("disabled", false);
		dropdown.attr("name", "notCategory")
		addCategory.attr("name", "category")
		
		if(clear){
			addCategory.val("");
		}
	} else {
		addCategory.toggleClass("hidden", true);
		addCategory.prop("disabled", true);
		addCategory.attr("name", "notCategory")
		dropdown.attr("name", "category")
	}
}

function clearQueue($queueMessage) {
	
	$("#cancel_button, #clear_button, #upload_button").attr("disabled","disabled");
	$queueMessage.removeClass("green red");
	$queueMessage.html(language.queueEmptyMessage);
	
} 

function viewComments(showAll){
	document.viewComments.viewComments.value=showAll;
	document.viewComments.submit();
}

function hideShow(id){
	
	if (document.getElementById){
		var element=document.getElementById(id);
		
		if (element) {
			
			if (element.style.display != "none") {
				element.style.display = "none";
			} else {
				element.style.display = "block";
			}
		}
	}
	
}