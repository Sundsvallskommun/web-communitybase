var searchModuleURI;
var sectionLogoURI;
var contextPath;
var i18nSearchModule = {
	"SHOW_ALL_RESULT": "Show all search result",
	"EXTENDED_SEARCH": "Make complete search"
};

$(document).ready(function() {
	
	$("#search-object").keyup(function (e) {
		
		var keyCode = e.which;
		
		if(keyCode == 13 && $(this).val() != "") {

			window.location = $("#showAllLink").find("a").data("hrefbase") + "?q=" + $(this).val();
			
			return;
		}
		
		search();
		
    }).parent().removeClass("focus");

	$('.close-search').on('click', function(e) {
        
		e.preventDefault();
		
        var parent = $('.samarbetsrum-search'),
            search = parent.find('.of-search');

        if (typeof search.attr('style') !== 'undefined') {
            search.removeAttr('style').removeClass('active');

            return;
        }
        
    });
	
	$('.of-search-results .of-close, .of-searchfield + .of-close').on('click', function(e) {
		
		e.preventDefault();
		
		var $searchField = $("#search-object");
		$searchField.parent().removeClass('focus');
		$searchField.val("").focus();
		$searchField.removeClass("loading");
	});
	
});

function search() {
	
	var $searchField = $('#search-object');
	
	var searchStr = $searchField.val();
	
	var $showAllLink = $("#showAllLink");
	var $searchResultWrapper = $(".of-search-results article");
	
	if(searchStr != "") {
		
		$searchField.parent().addClass("focus");
		
		$.ajax({
			
			cache: false,
			url: searchModuleURI + "/search?q=" + encodeURIComponent(searchStr),
			dataType: "json",
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			error: function (xhr, ajaxOptions, thrownError) { },
			success: function(response) {

				var result = eval(response);
				
				$searchResultWrapper.html("");
				
				var $searchResult = $("<ul/>");
				
				if(result.sectionResult.hitCount > 0) {
					
					var $resultWrapper = $("<li><div>" + $("#search-sections-header").html() + "</div><ul/></li>");
					
					$.each(result.sectionResult.hits, function(index, hit) {
					
						var $ul = $resultWrapper.find("ul").last();
						
						var $section = $("<li>" + $("#search-section-template").html() + "</li>");
						
						$section.find("a").attr("href", contextPath + hit.fullAlias);
						$section.find("img").attr("src", $section.find("img").data("srcbase") + "/" + hit.sectionID);
						$section.find("span").text(hit.name);
						
						$ul.append($section);
					
					});
					
					$searchResult.append($resultWrapper);
					
				}
				
				if(result.users && result.users.hitCount > 0) {
					
					var $resultWrapper = $("<li><div>" + $("#search-users-header").html() + "</div><ul/></li>");
					
					$.each(result.users.hits, function(index, hit) {
					
						var $ul = $resultWrapper.find("ul").last();
						
						var $user = $("<li>" + $("#search-user-template").html() + "</li>");
						
						$user.find("a").attr("href", contextPath + hit.FullAlias);
						$user.find("img").attr("src", $user.find("img").data("srcbase") + "/" + hit.ID);
						$user.find("span").text(hit.Name);
						
						$ul.append($user);
					
					});
					
					$searchResult.append($resultWrapper);
					
				}
				
				if(result.tags.hitCount > 0) {
					
					var $resultWrapper = $("<li><div>" + $("#search-tags-header").html() + "</div><ul/></li>");
					
					$.each(result.tags.hits, function(index, hit) {
					
						var $ul = $resultWrapper.find("ul").last();
						
						var $tag = $("<li>" + $("#search-tags-template").html() + "</li>");
						
						$tag.find("a").attr("href", $tag.find("a").data("srcbase") + "?t=tag&q=" + hit);
						$tag.find("span").text("#" + hit);
						
						$ul.append($tag);
					
					});
					
					$searchResult.append($resultWrapper);
					
				}
				
				$searchResultWrapper.append($searchResult);
				
				if($searchResult.find("li").length > 0) {
					$showAllLink.find("a").text(i18nSearchModule.SHOW_ALL_RESULT).attr("href", $showAllLink.find("a").data("hrefbase") + "?q=" + searchStr);
					$showAllLink.removeClass("of-hidden");
				} else {
					$showAllLink.find("a").text(i18nSearchModule.EXTENDED_SEARCH).attr("href", $showAllLink.find("a").data("hrefbase") + "?q=" + searchStr);
				}
				
		        $searchResultWrapper.show().parent().show();
				
			}
			
		});
		
        
    } else {
    	
    	$searchField.parent().removeClass("focus");
    	$showAllLink.addClass("of-hidden").attr("href", "");
    	$searchResultWrapper.html("");
    	$searchResultWrapper.hide();
    	
    }
	
}