$(document).ready(function() {
	
	$(".tooltip_trigger").hover(function(event) {
		var id = $(this).attr("id");
		var position = $(this).position();
		$("#" + id + "_container").css( { "position": "absolute", "left": (position.left + 50) + "px", "top": (position.top + 15) + "px" } );
		$("#" + id + "_container").show();
	},function(event) {
		var id = $(this).attr("id");
		$("#" + id + "_container").hide();
	});
	
});