/**
 *管理界面左侧目录树
 */

 var setting = {
		view: {
			dblClickExpand: false,
			showLine: true,
			selectedMulti: false
		},
		data: {
			simpleData: {
				enable:true,
				idKey: "id",
				pIdKey: "pId",
				rootPId: ""
			}
		},
		callback: {
			beforeClick: function(treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("tree");
				if (!treeNode.hasLink) {
					zTree.expandNode(treeNode);
					return false;
				} else {
					$("#contentIfr").attr("src",treeNode.linkUrl+"?nodeId="+treeNode.id);
					return true;
				}
			}
		}
	};

	

    $(document).ready(function(){
		$.post("../admin/treeServlet", { action: "post", opt : "getModuleTree", module : "system", type : "system"},
 			function (data, textStatus){
					eval("var zNodes="+data+";");
					$.fn.zTree.init($("#tree"), setting, zNodes);
 			}, 
 	    "text");
	});
    
    function setHeight(obj){
    	document.body.height=obj.contentDocument.body.scrollHeight+200;
    }
