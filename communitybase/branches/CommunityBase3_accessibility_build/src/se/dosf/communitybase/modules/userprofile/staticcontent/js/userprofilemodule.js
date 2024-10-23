$(function() {
	$("#removeprofileimage").on("click", function(e) {
		e.preventDefault();
		
		$("#profileImageWrapper").toggle(true).find("input[type=hidden]").val("true");
		$("#profileImageUploadWrapper").remove();
	});
	
	$("#profileimagelabel").on("click", function(e) {
		e.preventDefault();
		
		$("#profileImageWrapper").toggle(true).find("input[type=hidden]").val("true");
		$("#profileImageUploadWrapper").remove();
		$("#profileImageFile").click();
	});
	
	$profileImage = $("#fullProfileImage");
	
	if (!isTransparent($profileImage[0])) {
		$profileImage.parent().removeClass("profile");
	}
	
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

function isTransparent(image) {
    const canvas = document.createElement("canvas");
    const context = canvas.getContext("2d");
    
    if (!image || image.width === 0 && image.height === 0) {
		return false;
	}
    
    canvas.width = image.width;
    canvas.height = image.width;
    
    context.drawImage(image, 0, 0);
    
    const data = context.getImageData(0, 0, canvas.width, canvas.height).data;
    
    let opacity = 0;
    for (let i = 0; i < data.length; i += 4) {
        opacity += data[i + 3];
    }
    
    const opacityRatio = (opacity / 255) / (data.length / 4); 
    
    return opacityRatio === 0;
}
