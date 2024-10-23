
$(document).ready(function() {
	
	$("#resume-toggle").change(function(){
		
		var value = $(this).val();
		
		if(value == 1){
			
			$("#hour").show();
			$("#notification-types").show();
			$("#sections").show();
			
		}else{
			
			$("#hour").hide();
			$("#notification-types").hide();
			$("#sections").hide();
		}
	});
});