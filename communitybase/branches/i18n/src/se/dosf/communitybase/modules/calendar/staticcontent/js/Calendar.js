var http_request;
var getEventsUrl;
var addPostUrl;
var startMonth;
var isAdmin;



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
	    onMonthChanged: function(dateIn) { return true; },
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
	    }

        
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
						"URL": post.url };

				$.jMonthCalendar.ReplaceEventCollection(events);
				
			}
			
		}
		
	});
	
}

function redirectToAddEvent(date){
		
	window.location = addPostUrl + "/" + date.getFullYear() + "/" + (date.getMonth()+1) + "/" + date.getDate();
	
}

function validateTime(startDate, endDate){
	
	var startTime = startDate.getSeconds() == 0 ? toHoursAndMinutes(startDate.getHours(),startDate.getMinutes()) : "";
	var endTime = endDate.getSeconds() == 0 ? toHoursAndMinutes(endDate.getHours(),endDate.getMinutes()) : "";
	
	return startTime != "" ? startTime + ((endTime != "") ? "-" + endTime : "") + " - " : "";

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
