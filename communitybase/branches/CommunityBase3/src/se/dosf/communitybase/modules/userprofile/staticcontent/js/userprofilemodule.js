$(function() {
	$("#removeprofileimage").on("click", function(e) {
		e.preventDefault();
		
		$(this).parent().prev().toggle(true).find("input[type=hidden]").val("true");
		$(this).parent().remove();
	});
	
	var $mobilePhone = $("#mobilePhone");
	var $verificationCode = $("#verificationCode");
	var $mobilePhoneError = $("#mobilePhoneError");
	var $sendCode = $("#sendcode");
	
	$("#changeMobilePhone").on("change", function() {
		var $checkbox = $(this);
		var isChecked = $checkbox.is(":checked");
		
		$mobilePhone.prop("disabled", !isChecked);
		$verificationCode.prop("disabled", !isChecked);
		
		if (isChecked) {
			$sendCode.addClass("pointer");
		}
		else {
			$sendCode.removeClass("pointer");
		}
		
	}).trigger("change");
	
	$sendCode.on("click", function(e) {
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