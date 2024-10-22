$(function() {
	"use strict";
	
	if ($.siteSearch) {
		return;
	}
	
	var instances = [];
	
	$.fn.extend({
		siteSearch: function() {
			var $instance = $(this);
			
			if (instances[$instance.prop("id")]) {
				return;
			}
			
			instances[$instance.prop("id")] = 1;
			
			var $searchField = $instance.find(".search-input");
			var noHitsTemplateHTML = $instance.find(".search-no-hits-template");
			
			var $searchContainer = $instance.find(".search-result-container");
			
			var $searchFilters = $instance.find(".search-filters");
			
			var type = "";
			
			if ($searchFilters.is("select")) {
				type = $searchFilters.val();
				
				$searchFilters.on("change", function() {
					type = $searchFilters.val();
					
					$searchField.trigger("keyup");
				});
			}
			else {
				type = $searchFilters.find(".active").data("type");
				
				$searchFilters.on("click", ".filter", function(e) {
					e.preventDefault();
					
					var $filter = $(this);
					
					$searchFilters.find(".active").removeClass("active");
					
					$filter.addClass("active");
					
					type = $filter.data("type");
					
					$searchField.trigger("keyup");
				});
			}
			
			var $containerWrapper = $("#" + $instance.prop("id") + "-results")
			
			if ($containerWrapper.length > 0) {
				$searchContainer.appendTo($containerWrapper);
			}
			
			var $searchResult = $searchContainer.find(".search-result");
			
			var delay = (function(callback, ms) {
				var timer = 0;
				
				return function() {
					var context = this;
					var args = arguments;
					clearTimeout(timer);
					timer = setTimeout(function () {
						callback.apply(context, args);
					}, ms || 0);
				};
			});
			
			$searchField.on("keyup", delay(function(e) {
				var keyCode = e.keyCode || e.which;
				
				if (keyCode == 13 && $searchField.val() != "") {
					window.location.href = $searchField.data("hrefbase") + "?enc=true&t=" + type + "&q=" + encodeURIComponent($searchField.val());
					
					return false;
				}
				
				search($searchField.val());
		    }, 500));
			
			$searchContainer.on("click", ".close-search", function(e) {
				e.preventDefault();
				
				$searchField.val("").trigger("focus");
				
				search("");
			});
			
			$searchContainer.on("click", ".showAllLink", function(e) {
				
				e.preventDefault();
				
				window.location.href = $searchField.data("hrefbase") + "?enc=true&t=" + type + "&q=" + encodeURIComponent($searchField.val());
			});
			
			var search = (function(searchStr) {
				if (searchStr != "") {
					$.ajax({
						cache: false,
						url: $instance.data("searchmoduleuri") + "/search?enc=true&t=" + type + "&q=" + encodeURIComponent(searchStr),
						dataType: "json",
						contentType: "application/x-www-form-urlencoded;charset=UTF-8",
						error: function (xhr, ajaxOptions, thrownError) { },
						success: function(response) {
							var resultBody = "";
							
							if (response.viewFragments) {
								for (var i = 0; i < response.viewFragments.length; i++) {
									resultBody += response.viewFragments[i];
								}
							}
							
							if (resultBody === "") {
								resultBody = noHitsTemplateHTML;
							}
							
							$searchResult.html(resultBody);
					        $searchContainer.show();
						}
					});
				} 
				else {
					$searchContainer.hide();
					$searchResult.html("");
				}
			});
		}
	});
	
	$(function() {
		$(".site-search-container").each(function(i, val) {
			$(this).siteSearch();
		});
	});
}(jQuery));