
//树节点的id，用于设置权限
//var nodeId = getArgsFromHref("nodeId");

/**
 * 列表填充公共方法
 * 页面必须有的标签及id设置：
 * 1、搜索只支持类型+关键字的形式，类型id和name为searchType，关键字id和name为keywords，
 *     类型改变后如果需要做特殊处理请在页面定义相应方法。
 * 2、页面每次查询记录数目前为隐藏的标签pageSize，初始化大小可以在页面中定义，若要实现可输入可以显示出来。
 * 3、列表最外层须有id为pageDiv的DIV，列表table的id为dataList，表头自定义，第一列包含一个id为checkAll的checkbox，
 *     数据行tr的id为dataRow，其中第一列td包含一个id和name为chkId的checkbox。
 * 4、分页部分包含id和name一致的隐藏input：count、pageNum和pageSize，当前页span的id为curPage，总页数span的id为allPage；
 *     翻页button的id分别为：首页（firstBtn），上一页（prevBtn），下一页（nextBtn），末页（lastBtn）
 */
    function fill(searchType,keywords,pageNum){
    	var pageSize = $("#pageSize").val();
    	$.post(servletName, { action: "post", opt : "search", nodeId : nodeId,
    		searchType : searchType, keywords : keywords, pageNum : pageNum, pageSize : pageSize},
    			function (data, textStatus){
	    			$("tr[flag='data']").each(function () {
	        			$(this).remove();
	        		});
	    			var list = data.list;
    				for(var o in list){
    					var row = list[o];
    					var tr = $("#dataRow");
    					tr.find("td").each(function(){
    						if($(this).attr("flag")!="chk")
    						    $(this).html("");
    					});
    					var id = eval("row."+fields[0][0]);
    					for(var i=0; i<fields.length; i++){
    						var field = fields[i][0];
    						var value = eval("row."+field);
    						if(i==0){
    							tr.find("#chkId").val(id);
    						}else{
    							var str = getFieldStr(id,field,value);
    							tr.find("#"+field).html(str);
    						}
    					}
    					
    					tr.show();
    					var newTr = tr.clone();
    					newTr.attr("id","dataRow"+id);
    					newTr.attr("flag","data");
    					$("#dataList").append(newTr);
    				}
    				$("#dataRow").hide();
    				//设置权限
    				if(data.functions!=null && data.functions!="" && $("#functionsDiv").length<1){
    					$(data.functions).insertBefore($("#pageDiv"));
    				}
    				//设置页码
    				setPageInfo(data.count, pageNum);
    			}, 
    	"json");
    }
 
    //设置分页信息
	function setPageInfo(count, pageNum){
		$("#count").val(count);
		$("#allPage").html(count);
		$("#curPage").html(pageNum);
		$("#pageDiv").find("button").each(function() {
			$(this).attr("disabled", false);
		});
		if (pageNum == 1) {
			$("#firstBtn").attr("disabled", "disabled");
		$("#prevBtn").attr("disabled", "disabled");
		}
		if (pageNum == count) {
			$("#nextBtn").attr("disabled", "disabled");
		$("#lastBtn").attr("disabled", "disabled");
		}
	}
	
	//查询
	function search(str){
    	var searchType=$("#searchType").val();
    	var keywords=$("#keywords").val();
    	var pageNum=parseInt($("#pageNum").val());
    	if(str==undefined || str==null || str=="first"){
    		pageNum=1;
    	}else if(str=="prev"){
    		pageNum--;
    		if(pageNum==0)
    			pageNum=1;
    	}else if(str=="next"){
    		pageNum++;
    		if(pageNum>$("#count").val())
    			pageNum=$("#count").val();
    	}else if(str=="last"){
    		pageNum=$("#count").val();
    	}
    	$("#pageNum").val(pageNum);
    		
    	fill(searchType,keywords,pageNum);
    }
	
	function add(){
    	window.location=editUrl+"?nodeId="+nodeId;
    }
	    
    function edit(){
    	var Id;
    	var n=0;
    	$("input[name='chkId']").each(function () {
    		if($(this).prop("checked")){
    			Id = $(this).attr("value");
    			n++;
    		}
		});
    	if(n==0){
    		alert("你还没有选择记录！");
    		return;
    	}
    	if(n>1){
    		alert("只能选择一条记录进行操作！");
    		return;
    	}else{
    		window.location=editUrl+"?id="+Id+"&nodeId="+nodeId;
    	}
    }
    
    function del(){
    	var Ids="";
    	var n=0;
    	$("input[name='chkId']").each(function () {
    		if($(this).prop("checked")){
    			Ids += "," + $(this).attr("value");
    			n++;
    		}
		});
    	if(n==0){
    		alert("你还没有选择记录！");
    		return;
    	}else{
    		if(confirm("确定要删除所选记录吗？")){
    			Ids=Ids.substr(1);
        		$.post(servletName, { action: "post", opt : "delete", Ids : Ids},
            			function (data, textStatus){
            				if(data=="ok"){
            					alert("删除成功！");
            					window.location.reload();
            				}
            			}, 
            	"text");
    		}
    	}
    }
    
    //初始化列表页面
    function initPage(){
    	var searchType='';
    	for(var i=0;i<searchFields.length;i++){
    		searchType += '<option value="'+searchFields[i][0]+'">'+searchFields[i][1]+'</option>';
    	}
    	$("#searchType").append(searchType);
    	var table = '<table id="dataList"  style="width:100%" border="0">';
    	table += '<tr>';
    	table += '<td class="headTd" style="width:'+listFields[0][2]+'"><input type="checkbox"  id="checkAll"></td>';
    	for(var i=1;i<listFields.length;i++){
    		table += '<td class="headTd" style="width:'+listFields[i][2]+'">'+listFields[i][1]+'</td>';
    	}
    	table += '</tr>';
    	table += '<tr id="dataRow"  style="display:none">';
    	table += '<td align="center" flag="chk"><input type="checkbox"  name="chkId"  id="chkId"></td>';
    	for(var i=1;i<listFields.length;i++){
    		table += '<td id="'+listFields[i][0]+'" align="'+listFields[i][3]+'"></td>';
    	}
    	table += '</tr>';
    	table += '</table>';
    	$("#pageDiv").append(table);
    	var div = '<div align="right" style="margin-top:10px;padding-right:20px;">';
    	div += '<input type="hidden"  id="count" name="count" value="1">';
    	div += '<input type="hidden"  id="pageNum" name="pageNum" value="1">';
    	div += '<input type="hidden"  id="pageSize" name="pageSize" value="15">';
    	div += '<span id="curPage">1</span>&nbsp;/&nbsp;<span id="allPage">1</span>&nbsp;&nbsp;';
    	div += '<button id="firstBtn"  onclick="search(\'first\')">首页</button>';
    	div += '<button id="prevBtn"  onclick="search(\'prev\')">上一页</button>';
    	div += '<button id="nextBtn"  onclick="search(\'next\')">下一页</button>';
    	div += '<button id="lastBtn"  onclick="search(\'last\')">末页</button>';
    	div += '</div>';
    	$("#pageDiv").append(div);
    }
    
    //初始化编辑页面
    function initEditPage(){
    	editForm.action=servletName+"?opt=save"
    	for(var i=0;i<hideFields.length;i++){
    		$('<input type="hidden"  id="'+hideFields[i][0]+'" name="'+hideFields[i][0]
    		    +'" value="'+hideFields[i][1]+'" />').insertBefore($("#editPageDiv"));
    	}
    	var table = '<table id="dataTable"  style="width:100%" border="0">';
    	for(var i=0;i<editFields.length;i++){
    		var rowFields = editFields[i];
    		if(rowFields.length>0){
    			table += '<tr>';
        		for(var j=0;j<rowFields.length;j++){
        			//具体设置从调用页面中获取
        			table += dealField(rowFields[j]);
        		}
        		table += '</tr>';
    		}
    	}
    	table += '</table>';
    	$("#editPageDiv").append(table);
    }

    //编辑页面取消
    function editPageCancel(){
    	window.location="list.html?nodeId="+nodeId;
    }
    
    //编辑页面自动默认填充方法
    function defaultDealField(fields){
    	var field = fields[0];
    	var tds="";
    	tds += '<td width="'+fields[2]+'" class="nameTd">'+fields[1]+'</td>';
    	tds += '<td width="'+fields[3]+'" colspan="'+fields[4]+'">';
    	var type = fields[5];
    	var str="";
		if(type=="text"){
			str += '<input type="text" id="'+field+'" name="'+field+'"  style="width:95%" value="'+fields[7]+'" ';
			if(fields[6]!="")
				str += ' required="'+fields[6]+'"';
			str += '/>';
		}else if(type=="number"){
			str += '<input type="number" id="'+field+'" name="'+field+'"  style="width:95%" value="'+fields[7]+'"';
			if(fields[6]!="")
				str += ' required="'+fields[6]+'"';
			str += '/>';
		}else if(type=="date"){
			str += '<input type="text" id="'+field+'" name="'+field+'" style="width:95%"'
			    +' title="点击输入框选择日期" onclick="new Calendar().show(this);" readonly="readonly" ';
			if(fields[6]!="")
				str += ' required="'+fields[6]+'"';
			str += '/>';
		}else if(type=="select"){
			str += '<select id="'+field+'" name="'+field+'">';
			for(var k=0;k<fields[7].length;k++){
				str += '<option value="'+fields[7][k][0]+'">'+fields[7][k][1]+'</option>';
			}
			str += '</select>';
		}else if(type=="textarea"){
			str += '<textarea id="'+field+'" name="'+field+'" rows="'+fields[7][0]
			    +'" cols="'+fields[7][1]+'" style="width:'+fields[7][2]+'" maxlength="'+fields[7][3]+'"></textarea>';
		}else if(type=="richText"){
			str += '<script id="'+field+'" name="'+field+'" type="text/plain"  style="width:'+fields[7][0]+';height:'+fields[7][1]+';"><\/script>';
			str += '<script type="text/javascript">/*首先删除已有编辑器实例*/\n UE.delEditor("'+field+'");\n var ue = UE.getEditor("'+field+'");<\/script>';
		}
		tds += str + '</td>';
		return tds;
    }
    
    //初始化查看页面
    function initViewPage(){
    	var table = '<table id="dataTable"  style="width:100%" border="0">';
    	for(var i=0;i<viewFields.length;i++){
    		var rowFields = viewFields[i];
    		if(rowFields.length>0){
	    		table += '<tr>';
	    		for(var j=0;j<rowFields.length;j++){
	    			table += '<td width="'+rowFields[j][2]+'" height="30px"  align="center"  style="background-color:#EEEEEE; ">'+rowFields[j][1]+'</td>';
	    			table += '<td id="'+rowFields[j][0]+'" width="'+rowFields[j][3]+'" colspan="'+rowFields[j][4]+'"></td>';
	    		}
	    		table += '</tr>';
    		}
    	}
    	table += '</table>';
    	$("#viewPageDiv").append(table);
    }
    