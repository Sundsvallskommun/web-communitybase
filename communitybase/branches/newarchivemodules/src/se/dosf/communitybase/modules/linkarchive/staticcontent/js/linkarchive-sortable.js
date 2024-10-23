$(document).ready(function() {
	
	// Sort by criteria
	$("#sortingcriterias").bind("change", { url: $("#sortingcriterias").attr("ref") }, function(event) {
		window.location = event.data.url + "?orderby=" + event.target.value + ($("#reverseordertrue").is(':checked') ? "&reverse=true" : "&reverse=false");
	});
	
	// Reverse sort order
	$("#reverseordertrue").bind("click", { url: $("#reverseordertrue").attr("ref"), criteria: $("#sortingcriterias").val() }, function(event) {
		window.location = event.data.url + "?orderby=" + event.data.criteria + "&reverse=true";
	});

	$("#reverseorderfalse").bind("click", { url: $("#reverseorderfalse").attr("ref"), criteria: $("#sortingcriterias").val() }, function(event) {
		window.location = event.data.url + "?orderby=" + event.data.criteria + "&reverse=false";
	});
});