$(document).ready(function() {

	var $treeView = $(".communitybase-treeview");
	
	$treeView.hide();
	
	$treeView.bind("loaded.jstree", function (event, data) {
			$(".jstree-loading").removeClass("jstree-loading");
			$treeView.show();
		}).jstree({ 
		"core" : { animation : 0, initially_open : [ "global_global" ] },
		"themes" : {
			"theme" : "communitybase",
			"icons" : $treeView.hasClass("jstree-show-icons") ? true : false
		},
		"types" : {
			"valid_children" : [ "global" ],
			"types" : {
				"global" : {
					"valid_children" : [ "school" ],
					"max_depth" : 3,
					"close_node" : function() { if($("#global_global").hasClass("jstree-force-open")) return false; },
					"hover_node" : $("#global_global").hasClass("jstree-force-open") ? false : true
				},
				"school" : {
					"valid_children" : [ "group" ],
					"hover_node" : $("ul[rel='school']").hasClass("jstree-clickable-nodes") ? true : false,
					"select_node" : $("ul[rel='school']").hasClass("jstree-clickable-nodes") ? true : false
				},
				"group" : {
					"valid_children" : [ "user" ],
					"hover_node" : $("ul[rel='group']").hasClass("jstree-clickable-nodes") ? true : false,
					"select_node" : $("ul[rel='group']").hasClass("jstree-clickable-nodes") ? true : false
				},
				"user" : {
					"valid_children" : [ "user" ],
					"hover_node" : $("ul[rel='user']").hasClass("jstree-clickable-nodes") ? true : false,
					"select_node" : $("ul[rel='user']").hasClass("jstree-clickable-nodes") ? true : false
				}
			}
		},
		"checkbox" : {
			"real_checkboxes" : true,
			"real_checkboxes_names" : function(n)  {
				var id = n[0].id;
				var name_value = id.split("_");
				return [name_value[0], name_value[1]];
			},
			"use_native_checkboxes" : true,
			"set_parent_checked": false
		},
		"cookies" : {
			"save_opened" : $treeView.attr("id"),
			"save_selected" : false 
		},
		"search" : {
			"show_only_matches" : true
		},
		"plugins" : [ "themes", "html_data", "ui", "types", "cookies", $treeView.hasClass("jstree-create-checkboxes") ? "checkbox" : "", $treeView.hasClass("jstree-search") ? "search" : "" ]
	 }).bind("select_node.jstree", function (event, data) { 
		 if(data.rslt.obj.children("a:first").attr("href") == "javascript:void(0);") {
			 $treeView.jstree("toggle_node", data.rslt.obj);
		 }
	 });
	 
	 $("#jstree-expand-all").click(function() {
		 $treeView.jstree("open_all");
	 });
	 
	 $("#jstree-collapse-all").click(function() {
		 $treeView.jstree("close_all");
	 });
	 
	 $("#search-tree-field").keyup(function(event) {
		 var $this = $(this);
		 if ( event.which == 13 ) {
		     event.preventDefault();
		  }
		  if($this.val() == "") {
			  $treeView.jstree("close_all", $("ul[rel='school']"));
		  }
		  $treeView.jstree("search", $this.val(), true);
	 });
	 
	 $("#search-tree-field-restore").click(function() {
		 $("#search-tree-field").val("");
		 $treeView.jstree("clear_search");
		 $treeView.jstree("close_all", $("ul[rel='school']"));
	 });
	 
});