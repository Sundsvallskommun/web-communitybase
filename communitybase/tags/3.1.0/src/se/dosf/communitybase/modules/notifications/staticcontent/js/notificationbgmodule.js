$(document).ready(function() {

	$(document).on('click', function(e) {

	    $('a[data-toggle="of-notification-menu"]').removeClass('active');
	    $('.of-notification-menu').removeClass('active');

	});
	
	$("#notification-symbol").click(function(){
		
		if(!$(this).hasClass("active")){
			
			$("#notifications-list li:not(.notification-template)").remove();
			$("#no-notifications").hide();
			$("#notifications-loading").show();
			$("#notifications-loading").ofLoading();
		}
		
		$.ajax({
			cache: false,
			url: notificationHandlerUrl + "/latest",
			dataType: "html",
			contentType: "application/x-www-form-urlencoded;charset=UTF-8",
			error: function (xhr, ajaxOptions, thrownError) {  },
			success: function(response) {
				
				$("#notifications-loading").ofLoading(false);
				$("#notifications-loading").hide();
				
				response = $.trim(response)
				
				if(response != "") { 
					
					$("#notifications-list").append(response);
					
				} else {
					
					$("#no-notifications").show();
					
				}
			}
		});
	});
});
