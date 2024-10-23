var croppieUpload = (function($wrapper) {
	var $result = $wrapper.find(".crop-result");
	var $file = $wrapper.find("input[type=file]");
	var $cropCoords = $wrapper.find(".crop-coordinates");
	
	var $uploadCrop;
	
	var readFile = (function(input) {
		if (input.files && input.files[0]) {
            var reader = new FileReader();
            
            reader.onload = function (e) {
            	$result.addClass('ready');
				
            	$uploadCrop.croppie('bind', {
            		url: e.target.result
            	});
            	
            	$result.show();
            }
            
            reader.readAsDataURL(input.files[0]);
        }
        else {
	        alert("Sorry - your browser doesn't support the FileReader API");
	    }
	});
	
	var imageSizeX = $wrapper.data("imagesize-x");
	var imageSizeY = $wrapper.data("imagesize-y");
	
	if (!imageSizeX) {
		imageSizeX = 250;
	}
	
	if (!imageSizeY) {
		imageSizeY = imageSizeX;
	}

	$uploadCrop = $result.croppie({
		viewport: {
	        width: imageSizeX,
	        height: imageSizeY,
	    },
	    boundary: {
	        width: imageSizeX + 50,
	        height: imageSizeY + 50
	    }
	});
	
	$uploadCrop.on('update.croppie', function(e, cropData) {
		$cropCoords.val(cropData.points.toString());
	});

	$file.on('change', function() {
		readFile(this);
	});
});