var http_request;
var getEventsUrl;
var addPostUrl;
var startMonth;
var isAdmin;
var imagePath;
var allSchools;

$().ready(function() {
    
	var options = {
        containerId: "#jMonthCalendar",
	    headerHeight: 50,
	    height:650,
	    width:720,
	    firstDayOfWeek: 1,
	    dateLabelTitle: language.addpost,
	    dateLabelLinkUrl: '#',
	    calendarStartDate: new Date(),
	    dragableEvents: false,
	    activeDroppableClass: false,
	    hoverDroppableClass: false,
	    navLinks: {
		enableToday: false,
		enableNextYear: false,
		enablePrevYear: false,
		p:'<![CDATA[&lsaquo;]]> ' + language.prev, 
		n:language.next + ' <![CDATA[&rsaquo;]]>', 
		t:language.today,
		showMore: language.showMore
	    },
	    onMonthChanging: function(dateIn) { 
	    	
	    	getEvents(dateIn);
	    	$("#addPostLink").attr("href", addPostUrl + "/" + dateIn.getFullYear() + "/" + (dateIn.getMonth()+1) + "/" + dateIn.getDate());
	    	return true; 
	    	
	    },
	    onMonthChanged: function(dateIn) { 
	    	
	    	// fix for z-index problem in IE7
	    	var zIndex = 9000;
	    	$(".Event").each(function() {
	    		zIndex -= 10;
				$(this).css( { "z-index": zIndex } );
	    	});
	    	return true;
	    	
	    },
	    onEventLinkClick: function(event) { return true; },
	    onEventBlockClick: function(event) { return true; },
	    onEventBlockOver: function(event) { return true; },
	    onEventBlockOut: function(event) { return true; },
	    onDayLinkClick: function(date) { 
	    	
	    	if(isAdmin == true){
	    		redirectToAddEvent(date);
	    	}
	    	return false;
	    
	    },
	    onDayCellClick: function(date) { return true; },
	    onDayCellDblClick: function(dateIn) { 
	    	
	    	if(isAdmin == true){
	    		redirectToAddEvent(dateIn);
	    	}
	    	return false; 
	    
	    },
	    onEventDropped: function(event, newDate) { return true; },
	    onShowMoreClick: function(eventArray) { return true; },
	    locale: {
		days: language.days,
		daysShort: language.daysShort,
		daysMin: language.daysMin,
		months: language.months,
		monthsShort: language.monthsShort,
		weekMin: language.weekMin
	    },
	    onAddMoreEventInfo: function(eventInfo, eventElement) { addMoreEventInfo(eventInfo, eventElement); }
	};

    var events = getEvents(new Date(startMonth));
    $.jMonthCalendar.Initialize(options, events);
    $.jMonthCalendar.ChangeMonth(new Date(startMonth));
    
});

function getEvents(dateIn) {
	
	var reqUrl = getEventsUrl + "/" + dateIn.getFullYear() + "/" + (dateIn.getMonth()+1);
	
	$.ajax({
		
		type: "GET",
		url: reqUrl,			
		dataType: "json",
		async: false,
		error: function (XMLHttpRequest, textStatus, errorThrown) { handleError(XMLHttpRequest, textStatus, errorThrown); },
		success: function(jsonObject) {
			
			var events = new Array(jsonObject.posts.length);
			
			for(var i = 0; i < events.length; i++){
				
				var post = jsonObject.posts[i];
				
				var startDate = new Date(post.startTime);
				var endDate = new Date(post.endTime);
				
				events[i] = {"EventID": post.calendarID, "StartDateTime": new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate()), 
						"EndDateTime": new Date(endDate.getFullYear(), endDate.getMonth(), endDate.getDate()), 
						"Title" :  validateTime(startDate, endDate) + post.description, 
						"URL": post.url,
						"Groups": post.groups,
						"Schools": post.schools,
						"SendReminder": post.sendReminder};

				$.jMonthCalendar.ReplaceEventCollection(events);
				
			}
			
		}
		
	});
	
}

function addMoreEventInfo(eventInfo, $eventElement) {
	
	var $showMoreTrigger = $('<a class="tooltip-trigger" href="javascript:void(0);"><img src="' + imagePath + '/information.png" /></a>');

	var $showMorePopup = $('<div class="tooltip" style="display: none">');
	
	$showMorePopup.append('<p class="tiny">' + language.postPublishedTo + ':</p>');
	
	if(eventInfo.Groups) {
		var $groupElement = $('<div class="floatleft" />');
		$groupElement.append('<img class="alignmiddle marginright" src="' + imagePath + '/group.png" />');
		appendPublishingList($groupElement, eventInfo.Groups);
		$showMorePopup.append($groupElement);
		isGlobal = false;
	}
	
	if(eventInfo.Schools) {
		var $schoolElement = $('<div class="floatleft clearboth" />');
		$schoolElement.append('<img class="alignmiddle marginright" src="' + imagePath + '/school.png" />');
		appendPublishingList($schoolElement, eventInfo.Schools);
		$showMorePopup.append($schoolElement);
	}
	
	if(!eventInfo.Groups && !eventInfo.Schools) {
		var $globalElement = $('<div class="floatleft clearboth" />');
		$globalElement.append('<img class="alignmiddle marginright" src="' + imagePath + '/global.png" />');
		$globalElement.append(allSchools);
		$showMorePopup.append($globalElement);		
	}
	
	$eventElement.append($showMoreTrigger);
	$eventElement.append($showMorePopup);
	
	if(eventInfo.SendReminder) {
		$eventElement.append($('<img class="reminder-icon" src="' + imagePath + '/bell.png" title="' + language.reminderIsSent + '" alt="" />'));
	}
	
	$showMoreTrigger.hover(function(event) {
		var position = $(this).position();
		$showMorePopup.css( { "position": "absolute", "left": (position.left + 15) + "px", "top": (position.top + 15) + "px" } );
		$showMorePopup.show();
	}, function(event) {
		$showMorePopup.hide();
	});
	
}

function appendPublishingList($element, $list) {
	
	var length = $list.length;
	
	$.each($list, function(i, item) {
		var text = item.name;
		if(i != length-1) {
			text += ", ";
		}
		$element.append(text);
	});
	
}

function redirectToAddEvent(date){
		
	window.location = addPostUrl + "/" + date.getFullYear() + "/" + (date.getMonth()+1) + "/" + date.getDate();
	
}

function validateTime(startDate, endDate){
	
	var startTime = startDate.getSeconds() == 0 ? toHoursAndMinutes(startDate.getHours(),startDate.getMinutes()) : "";
	var endTime = endDate.getSeconds() == 0 ? toHoursAndMinutes(endDate.getHours(),endDate.getMinutes()) : "";
	
	return startTime != "" ? startTime + ((endTime != "") ? "-" + endTime + " " : "") : "";

}

function toHoursAndMinutes(hours, minutes){
	
	var minutesStr = (minutes < 10 ? "0" : "") + minutes;
    var hoursStr = (hours < 10 ? "0" : "") + hours;
	
	return (hoursStr + ":" + minutesStr);
	
}

function handleError(XMLHttpRequest, textStatus, errorThrown) {
	
	//alert("Ett fel inträffade vid hämtning av posterna: " + textStatus);
	
}

function uncheckAll(name){
	var checkboxes = document.getElementsByName(name);
	
	if(checkboxes != null){
		for(var i=0;i<checkboxes.length;i++){
			checkboxes[i].checked = false;
		}
	}	
}

function checkAll(name){
	var checkboxes = document.getElementsByName(name);
	
	if(checkboxes != null){
		for(var i=0;i<checkboxes.length;i++){
			checkboxes[i].checked = true;
		}
	}	
}
