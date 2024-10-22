$(document).ready(function() {
	
	$("table").each(function() {
		var $this = $(this);
		$this.find("tr:not(:first):visible:even").addClass("even");
		$this.find("tr:not(:first):visible:odd").addClass("odd");
	});
	
	$("#wholeDay").change(function() {
		if($(this).is(":checked")) {
			var $startAndEndTime = $("#startTime, #endTime");
			$startAndEndTime.val("");
			$startAndEndTime.attr("disabled", "disabled");
		}
	});
	
	$("#partOfDay").change(function() {
		if($(this).is(":checked")) {
			$("#startTime, #endTime").removeAttr("disabled");
		}
	});
	
	$("#oneDay").change(function() {
		if($(this).is(":checked")) {
			$("#severalDaysForm").hide();
			$("#startDate, #endDate").val("");
			$("#oneDayForm").show();
		}
	});
	
	$("#severalDays").change(function() {
		if($(this).is(":checked")) {
			$("#oneDayForm").hide();
			$("#wholeDay").attr("checked", "checked");
			$("#date, #startTime, #endTime").val("");
			$("#severalDaysForm").show();
		}
	});
	
	$("#startTime, #endTime").timePicker({
		startTime: "06:00",
		endTime: "18:00",
		step: 15
	});

	var oldTime = null;
	
	if($.timePicker("#startTime") != undefined) {
		oldTime = $.timePicker("#startTime").getTime();
	}
	
	// Keep the duration between the two inputs.
	$("#startTime").change(function() {
	  if ($("#endTime").val()) { // Only update when second input has a value.
	    // Calculate duration.
	    var duration = ($.timePicker("#endTime").getTime() - oldTime);
	    var time = $.timePicker("#startTime").getTime();
	    // Calculate and update the time in the second input.
	    $.timePicker("#endTime").setTime(new Date(new Date(time.getTime() + duration)));
	    oldTime = time;
	  }
	});
	// Validate.
	$("#endTime").change(function() {
	  if($.timePicker("#startTime").getTime() > $.timePicker(this).getTime()) {
	    $(this).addClass("error");
	  }
	  else {
	    $(this).removeClass("error");
	  }
	});

	var orderByField = $("#orderByField").val();
	$("#orderBy").val(orderByField != "" ? orderByField : "name");
	var orderField = $("#orderField").val();
	$("#order").val(orderField != "" ? orderField : "ASC");
	
	initForms();
	
});


function initForms() {
	
	$("#wholeDay").trigger("change");
	$("#partOfDay").trigger("change");
	$("#oneDay").trigger("change");
	$("#severalDays").trigger("change");
	
}

function orderBy(column) {

	var $order = $("#order");
	var $orderBy = $("#orderBy");
	
	if($orderBy.val() == column) {
		$order.val($order.val() == "ASC" ? "DESC" : "ASC");
	}
	
	$orderBy.val(column);
	
	$("#searchAbsenceForm").submit();

}
