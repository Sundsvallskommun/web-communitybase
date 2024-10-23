$(function() {
	$("#SearchModule").on("click", ".show-more", function(e) {
		
		e.preventDefault();
		
		var $link = $(this);
		$link.hide();
		$link.siblings().show();
		
		var $section = $link.closest("section");
		$section.find(".toggle-item").fadeIn();
		$section.find(".show-more-text").hide();
		$section.find(".show-less-text").show();
		
	}).on("click", ".show-less", function(e) {
		
		e.preventDefault();
		
		var $link = $(this);
		$link.hide();
		$link.siblings().show();
		
		var $section = $link.closest("section");
		$section.find(".toggle-item").fadeOut();
		$section.find(".show-less-text").hide();
		$section.find(".show-more-text").show();
	});
});