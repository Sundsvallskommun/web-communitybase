
function checkOrUnCheck(global, parent, name){
	
	var checkboxes = document.getElementsByName(name);
	
	if(checkboxes != null){
		
		if(parent.checked == true){
			
			check(checkboxes, true);
			
		}else{
			
			check(checkboxes, false);
			document.getElementById(global).checked = false;
			
		}
		
	}
	
}

function checkOrUnCheckAll(parent){
	
	var checkboxes = document.getElementsByTagName('input');
	
	if(checkboxes != null){
		
		for(var i = 0; i < checkboxes.length; i++){
			
			if(checkboxes[i].type == 'checkbox'){
				
				if(parent.checked == true){
					
					checkboxes[i].checked = true;
					
				}else{
					
					checkboxes[i].checked = false;
					
				}
				
			}
			
		}
		
	}
	
}

function check(checkboxes, checked){

	for(var i=0; i < checkboxes.length; i++){
		
		checkboxes[i].checked = checked;
		
	}
		
}

function uncheck(global, parentid, checkbox){
	
	var parent = document.getElementById(parentid);
	
	if(parent != null){
	
		if(checkbox.checked == false){
			
			parent.checked = false;
			document.getElementById("global").checked = false;
	
		}else{
			
			var checkboxes = document.getElementsByName(checkbox.name);
			
			if(checkboxes != null){
			
				var allChecked = true;
				
				for(var i=0; i < checkboxes.length; i++){
					
					if(checkboxes[i].checked != true){
						allChecked = false;
						break;
					}
					
				}
				
				if(allChecked == true){
					
					parent.checked = true;
					
				}
			
			}
			
		}
	
	}
	
}