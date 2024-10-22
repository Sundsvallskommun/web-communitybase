$(document).ready(function() {
	
	$("#typeSelector").change(function() {

		$(this).closest("form").submit();
		
	});
});

function show(type){
	
	$("#show-more-" + type).hide();
	
	$("#searchwrapper li.toggle-" + type).fadeIn();
	
	$("#show-less-" + type).show();
	
	return false;
}

function hide(type){

	$("#show-less-" + type).hide();
	
	$("#searchwrapper li.toggle-" + type).fadeOut();
	
	$("#show-more-" + type).show();
	
	return false;
}