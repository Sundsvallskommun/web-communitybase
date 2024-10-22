$(function() {
	$("#registrationForm").on("submit", function(e) {
		$(this).find('input:submit').prop('disabled', 'true');	
	});
	
	var $mobilePhone = $("#mobilePhone");
	var $mobilePhoneError = $("#mobilePhoneError");
	
	$("#sendcode").on("click", function(e) {
		e.preventDefault();
		
		if ($mobilePhone.val().length > 0) {
			$mobilePhoneError.html("");
			
			var $button = $(this);
			
			if ($button.hasClass("pointer")) {
				$button.toggleClass("pointer text-secondary");
				$button.find(".icons").toggleClass("icon-send icon-refresh pulse spin");
				
				$.ajax({
					type: "GET",
					cache: false,
					data: { mobilePhone: $mobilePhone.val() },
					url: $button.data("url"),
					dataType: "json",
					contentType: "application/x-www-form-urlencoded;charset=UTF-8",
					error: function (xhr, ajaxOptions, thrownError) {
						
						$mobilePhoneError.html($button.data("unknown"));
						
						$button.find(".icons").toggleClass("icon-send icon-refresh pulse spin");
						
						setTimeout(function() {
							$button.toggleClass("pointer text-secondary");
						}, 3000);
					},
					success: function(response) {
						if (response.UnknownError) {
							$mobilePhoneError.html($button.data("unknown"));
						}
						else if (response.InvalidPhoneNumber) {
							$mobilePhoneError.html($button.data("invalidnumber"));
						}
						
						$button.find(".icons").toggleClass("icon-send icon-refresh pulse spin");
						
						setTimeout(function() {
							$button.toggleClass("pointer text-secondary");
						}, 3000);
					}
				});
			}
		}
	});
});