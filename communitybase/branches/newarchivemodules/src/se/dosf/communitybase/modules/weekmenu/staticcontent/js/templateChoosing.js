$(document).ready(
		function() {
			
			var $dropdown = $("#offset");

			if ($dropdown.length > 0) {
				window.WeekMenu_Prev = $("#WeekMenu_Prev").text().toNum();
				window.WeekMenu_DefaultOffset = $("#WeekMenu_DefaultOffset").text().toNum();
				window.WeekMenu_Weeks = $("#WeekMenu_Weeks").text().toNum();
				window.WeekMenu_WeekPrefix = $("#WeekMenu_WeekPrefix").text();
				window.WeekMenu_OverwriteChanges = $("#WeekMenu_OverwriteChanges").text();
				
				var $entries = $dropdown.find("option");
				 
				 $entries.each(function(j) {
					var $entry = $(this);
					$entry.text(window.WeekMenu_WeekPrefix + $entry.text())
				}); 
				
				$dropdown.change(function() {
					var $selected = 1 + ((($(this).val() - 1) + window.WeekMenu_DefaultOffset) % window.WeekMenu_Weeks);

					var $monday = $("#monday");
					var $tuesday = $("#tuesday");
					var $wednesday = $("#wednesday");
					var $thursday = $("#thursday");
					var $friday = $("#friday");
					var $saturday = $("#saturday");
					var $sunday = $("#sunday");

					if (!(WeekMenu_isPristine($monday, window.WeekMenu_Prev)
							&& WeekMenu_isPristine($tuesday, window.WeekMenu_Prev)
							&& WeekMenu_isPristine($wednesday, window.WeekMenu_Prev)
							&& WeekMenu_isPristine($thursday, window.WeekMenu_Prev)
							&& WeekMenu_isPristine($friday, window.WeekMenu_Prev)
							&& WeekMenu_isPristine($saturday, window.WeekMenu_Prev)
							&& WeekMenu_isPristine($sunday, window.WeekMenu_Prev))) {
						if(!confirm(window.WeekMenu_OverwriteChanges)){
							window.WeekMenu_Prev = $selected;
							return;
						}
					}
					
					WeekMenu_setDay($monday, $selected);
					WeekMenu_setDay($tuesday, $selected);
					WeekMenu_setDay($wednesday, $selected);
					WeekMenu_setDay($thursday, $selected);
					WeekMenu_setDay($friday, $selected);
					WeekMenu_setDay($saturday, $selected);
					WeekMenu_setDay($sunday, $selected);
					
					window.WeekMenu_Prev = $selected;
				});
			}

		});

function WeekMenu_setDay($dayTextField, $selectedWeek) {
	$dayTextField.val(WeekMenu_getDayFromWeek($selectedWeek, $dayTextField.attr('id')));
}

function WeekMenu_isPristine($dayTextField, $selectedWeek) {
	return WeekMenu_getDayFromWeek($selectedWeek, $dayTextField.attr('id')) === $dayTextField.val();
}

function WeekMenu_getDayFromWeek($week, $day) {
	return $("#" + "WeekMenu" + $week + "-" + $day).text();
}

String.prototype.toNum = function(){
    return parseInt(this, 10);
}