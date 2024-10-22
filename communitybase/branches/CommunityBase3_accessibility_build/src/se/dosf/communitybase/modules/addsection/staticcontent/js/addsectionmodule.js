var SectionTypeDeleteModes = undefined;

$(function() {
	var sectionIDDropdown = $("#sectionTypeID");
	var accessModesCheckboxes = $("#accessModes");
	
	var sectionIDChanged = (function(sectionIDDropdown, accessModesCheckboxes) {
		var sectionID = sectionIDDropdown.val();
		
		accessModesCheckboxes.find("input").each(function(){
			var input = $(this);
			var allowedAccessMode = input.val() in SectionAccess[sectionID];
			
			input.prop('disabled', !allowedAccessMode);
			
			if (!allowedAccessMode){
				input.prop('checked', false);
			}
		});
	});
	
	sectionIDChanged(sectionIDDropdown, accessModesCheckboxes);
	
	sectionIDDropdown.on("change", function() {
		sectionIDChanged(sectionIDDropdown, accessModesCheckboxes);
	});
	
	sectionIDDropdown.on("change", function() {
		var value = SectionTypeDeleteModes[$(this).val()];
		
		$("#deleteDateWrapper").toggle(value !== 'DISABLED');
		$("#deleteDateOptionalText").toggle(value === 'OPTIONAL')
	}).trigger("change");
});