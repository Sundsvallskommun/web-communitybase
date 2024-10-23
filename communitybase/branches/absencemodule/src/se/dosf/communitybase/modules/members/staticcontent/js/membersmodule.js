$(document).ready(function() {
	
	$("input[type='checkbox'][name='emailselection']").change(function(){
		
		var selectedEmails = $("input[type='checkbox'][name='emailselection']:checked").length;
		
		if(selectedEmails > 0) {
			$(".send-email-link").show();
		} else {
			$(".send-email-link").hide();
		}
		
	});
	
});

function sendEmail() {
	
	var emailStr = "";
	
	var selectedEmail = $("input[type='checkbox'][name='emailselection']:checked");
	
	selectedEmail.each(function() {
		
		var email = $("#member_" + $(this).attr("id")).html();
		
		emailStr += email + "; ";
		
	});
	
	document.location.href = "mailto:" + emailStr.substring(0, emailStr.length-2);
	
}

function checkAll() {
	var checkBoxes = $("input[type='checkbox'][name='emailselection']");
	checkBoxes.attr("checked","checked");
	checkBoxes.change();
	
}

function unCheckAll() {
	var checkBoxes = $("input[type='checkbox'][name='emailselection']");
	checkBoxes.removeAttr("checked");
	checkBoxes.change();
}