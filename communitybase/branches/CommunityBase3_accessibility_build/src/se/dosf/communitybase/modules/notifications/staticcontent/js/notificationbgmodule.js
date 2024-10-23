$(function() {
	
	var $notificationmenu = $("#notificationmenu");
	var notificationHandlerUrl = $notificationmenu.data("url");
	
	if ($notificationmenu.data("autorefresh")) {
		document.addEventListener("visibilitychange", function() {
	
			if (document.visibilityState === "visible") {
				getUnreadNotifications();
			}
		});
	
		setInterval(function(){
			
			if (!document.hidden) {
				getUnreadNotifications();
			}
			
		}, $notificationmenu.data("interval") * 1000);
	}
	
	$notificationmenu.on("click", function(){
		$("#notificationbadge").hide();
		
		if (!$(this).hasClass("active")){
			$("#notifications-list").children().remove();
			$("#no-notifications").hide();
			$("#notifications-loading").show();
		}
		
		$.ajax({
			cache: false,
			url: notificationHandlerUrl + "/latest",
			dataType: "html",
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			error: function (xhr, ajaxOptions, thrownError) {  },
			success: function(response, status, request) {
				
				$("#notifications-loading").hide();
				
				response = $.trim(response)
				
				if (response != "" && request.getResponseHeader("ajaxCallback")) { 
					$("#notifications-list").append(response);
				}
				else {
					$("#no-notifications").show();
				}
			}
		});
	});
	
	var getUnreadNotifications = (function() {
		
		$.ajax({
			cache: false,
			url: notificationHandlerUrl + "/unread",
			dataType: "html",
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			error: function (xhr, ajaxOptions, thrownError) {  },
			success: function(response, status, request) {
				
				if (request.getResponseHeader("ajaxCallback")) {
					var unreadNotifications = $(response).find('.dropdown-item.new');
					
					updateBadge(unreadNotifications.length);
					
					// Non-accessible
//					unreadNotifications.each(function(){
//						
//						if (this.hasAttribute('data-show')) {
//							
//							showToast(this);
//						}
//					});
				}
			}
		});
	});
	
	var updateBadge = (function(count) {
		
		if (count > 0) {
			
			$('#notificationbadge').html(count);
			$('#notificationbadge').show();
			
		} else {
			
			$('#notificationbadge').hide();
		}
	});
	
	var showToast = (function(notification) {
		
		notification.removeAttribute('class');
		
		notification.innerHTML = notification.firstElementChild.innerText;

		var notificationID = notification.id.split("-")[1];
		
		if (notificationID) {
			
			notification.onclick = function() {
				
				$.post(notificationHandlerUrl + "/markasread/" + notificationID);
			};
		}
		
		toastr.info(notification);
	});
});