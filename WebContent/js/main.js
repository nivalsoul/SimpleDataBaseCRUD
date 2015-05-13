$(document).ready(function(){
	$("#listCheckAll").click(function () {
		$("input[name='listChkId']").each(function () {
			$(this).prop("checked",$("#listCheckAll").prop("checked"));
		});
	});
	$("#qCheckAll").click(function () {
		$("input[name='qChkId']").each(function () {
			$(this).prop("checked",$("#qCheckAll").prop("checked"));
		});
	});
});

UEDITOR_HOME_URL = "ueditor-1.4.2/";

var allFields=null;
var fieldsInfo="";

/**
 * 读取数据库表字段信息
 */
function read(){
	var tableName = $("#tableName").val();
	if(tableName==""){
		alert("请输入数据库表名！");
		$("#tableName").focus();
		return;
	}
	$.post("fieldServlet", {"opt":"readTableInfo", "tableName" : tableName},
			function (data, textStatus){
    			$("tr[flag='data']").each(function () {
        			$(this).remove();
        		});
    			allFields = data.fields;
				for(var o in allFields){
					var field = allFields[o];
					var tr = $("#fieldTr");
					tr.find("td").each(function(){
						if($(this).attr("flag")!="chk" && $(this).children().length==0)
						    $(this).html("");
					});
					tr.find("#chkId").val(field.fieldName);
					tr.find("#fieldName").html(field.fieldName);
					tr.find("#comment").val(field.comment);
					tr.find("#dataType").html(field.dataType);
					tr.find("#fieldWidth").val("");
					tr.find("#fieldAlign").val("left");
					tr.show();
					var newTr = tr.clone();
					newTr.attr("id","fieldTr"+field.fieldName);
					newTr.attr("flag","data");
					$("#fields").append(newTr);
					
					fieldsInfo+="<div style='border-bottom: 1px solid #b1cde3;padding:2px 10px;'>"
						+field.fieldName+"（"+field.comment+"）</div>";
				}
				$("#fieldTr").hide();
				$("#editPageSetting").html("<div style='border-bottom: 1px solid #b1cde3;color:red'>当前表中字段信息：</div>"+fieldsInfo);
				$("#viewPageSetting").html("<div style='border-bottom: 1px solid #b1cde3;color:red'>当前表中字段信息：</div>"+fieldsInfo);
				
				var pkFields = data.pkFields;
				nopkId=true;
				for(var o in pkFields){
					var pkf = pkFields[o];
					if(pkf.pkName=="id"){
						nopkId=false;
						break;
					}
				}
				if(nopkId)
					alert("当前表中没有主键为id的字段，请加上该自增主键id后再使用！");
			}, 
	"json");
}

function moveUp(obj)
{
    var current=$(obj).parent().parent();
    var prev=current.prev();
    if(current.index()>1)
    {
        current.insertBefore(prev);
    }
}
function moveDown(obj)
{
    var current=$(obj).parent().parent();
    var next=current.next();
    if(next)
    {
        current.insertAfter(next);
    }
}

/**
 * 显示某个列表、编辑和查看配置页面
 * @param id
 * @param str
 */
function show(id,str){
	$("div.tabpage").each(function(){
		$(this).hide();
	});
	$("#"+str).show();
	
	$("table#tab td").each(function(){
		$(this).css("background-color","#E8E8E8");
	});
	$("#"+id).css("background-color","#7AC5CD");
	
	if(str=="editDiv" && allFields != null){
		if(editTip==null)
		    resetEditRowCol();
		else if(editTable==null){
			$("#editPageTip").dialog();
			$("#editPageTip").parent().css("top","30px").css("left","5%").width("90%").height("75%");
			$("#editPageTip").height("95%");
		}
	}
	if(str=="viewDiv" && allFields != null){
		if(viewTip==null)
		    resetViewRowCol();
		else if(viewTable==null){
			$("#viewPageTip").dialog();
			$("#viewPageTip").parent().css("top","30px").css("left","5%").width("90%").height("75%");
			$("#viewPageTip").height("95%");
		}
	}
}

//重置编辑页面字段行列数
function resetEditRowCol(){
	var tip='<p>编辑页面表单以table形式组织，请根据数据库表中需要用于增加和修改的字段，'
        +'    填写table的行数和最大列数(合并列分开计算)。主键若是自增字段，设为隐藏。</p>'
        +'<strong>行数：</strong><input type="number" id="rowNum" style="width:60px">'
        +'<strong>列数：</strong><input type="number" id="colNum" style="width:60px">'
        +'<input type="button"  value="确定" onclick="setEditPage(\'selectField\')">';
	$("#editPageTip").html(tip);
	$("#editPageTip").dialog();
	$("#editPageTip").parent().css("left","35%").width("30%");
}

//重置查看页面字段行列数
function resetViewRowCol(){
	var tip='<p>查看页面表单以table形式组织，请根据数据库表中需要查看的字段，填写table的行数和最大列数(合并列分开计算)：</p>'
        +'<strong>行数：</strong><input type="number" id="rowNum" style="width:60px">'
        +'<strong>列数：</strong><input type="number" id="colNum" style="width:60px">'
        +'<input type="button"  value="确定" onclick="setViewPage(\'selectField\')">';
	$("#viewPageTip").html(tip);
	$("#viewPageTip").dialog();
	$("#viewPageTip").parent().css("left","35%").width("30%");
}





var editTip=null;
var editTable=null;

//重选选择编辑页面字段
function reselectEditFields(){
	if(allFields==null){
		alert("请先输入数据库表名读取字段信息！");
		$("#tableName").focus();
		return;
	}
	if(editTip==null)
	    resetEditRowCol();
	else{
		$("#editPageTip").dialog();
		$("#editPageTip").parent().css("top","30px").css("left","5%").width("90%").height("75%");
		$("#editPageTip").height("95%");
	}
}

/**
 * 编辑页面字段设置
 */
function setEditPage(str){
	if(str=="selectField"){//选择字段并设置布局
		var rowNum=$("#editPageTip").find("#rowNum").val();
		var colNum=$("#editPageTip").find("#colNum").val();
		editTip='<p>请在下列表格中选择编辑页面上的字段布局位置'
			+'（注意：只需选择每行有哪些字段及其顺序即可，每一行从第一列开始依次选择，'
			+'如有合并列的情况则后面的留空，其他属性下一步再设置）</p>';
		editTip+='<table id="selectedEditFields" width="100%">';
		for(var i=1;i<=rowNum;i++){
			editTip+='<tr>';
			for(var j=1;j<=colNum;j++){
				editTip+='<td><input id="editField_'+i+'_'+j+'" type="text" onclick="selectField(this,\'canSelectEditFields\')"'
				    +'style="width:90%"  onblur="hideFiels(event,\'canSelectEditFields\')"></td>';
			}
			editTip+='</tr>';
		}
		editTip+='</table>';
		editTip+='<div style="margin-top:15px">'
			+'<input type="button"  value="确定" onclick="setEditPage(\'setField\')">&nbsp;&nbsp;&nbsp;&nbsp;'
			+'<input type="button"  value="重置行数和列数" onclick="resetEditRowCol()"></div>'
			+'<div id="canSelectEditFields" style="position:absolute; display:none;background-color:#698B69"></div>';
		$("#editPageTip").html(editTip);
		$("#editPageTip").dialog();
		$("#editPageTip").parent().css("top","30px").css("left","5%").width("90%").height("75%");
		$("#editPageTip").height("95%");
	}else if(str=="setField"){//设置字段相关属性
		editTable='<table id="editFields" style="width:100%; border:0; ">'
	        +'<tr>'
	        +'    <td class="headTd" style="width:8%">行列号</td>'
	        +'    <td class="headTd" style="width:15%">字段名称</td>'
	        +'    <td class="headTd" style="width:15%">字段中文名</td>'
	        +'   <td class="headTd" style="width:8%">名称列宽度</td>'
	        +'   <td class="headTd" style="width:8%">值所在列宽度</td>'
	        +'   <td class="headTd" style="width:10%">值所在列合并列数</td>'
	        +'   <td class="headTd" style="width:10%">页面数据格式</td>'
	        +'    <td class="headTd" style="width:8%">是否必填</td>'
	        +'    <td class="headTd" style="width:18%">默认值/其他属性值</td>'
	        +'</tr>';
		$("#selectedEditFields").find("input[type='text']").each(function(){
			if($(this).val()!=""){
				var xy=$(this).attr("id").split("_");
				var value=$(this).val().split("《");
				editTable+='<tr id="editFieldTr"  flag="data" row="'+xy[1]+'" col="'+xy[2]+'">'
				+'   <td id="rowcolNum" align="center"><b>第<font color="red">'
				        +xy[1]+'</font>行第<font color="red">'+xy[2]+'</font>列</b></td>'
		        +'   <td id="fieldName">'+value[0]+'</td>'
		        +'   <td><input type="text" id="comment" style="width:90%" value="'+value[1].split("》")[0]+'"></td>'
		        +'   <td align="center"><input type="text" id="fieldWidth" style="width:90%" placeholder="单列宽度"></td>'
		        +'   <td align="center"><input type="text" id="valueWidth" style="width:90%"></td>'
		        +'   <td align="center"><input type="text" id="valueColSpanNum" style="width:90%" value="1"></td>'
		        +'   <td align="center">'
		        +'       <select id="dataType" onchange="setDefaultValue(this)">'
		        +'           <option value="text">text</option>'
		        +'           <option value="number">number</option>'
		        +'           <option value="date">date</option>'
		        +'           <option value="select">select</option>'
		        +'            <option value="textarea">textarea</option>'
		        +'           <option value="richText">richText</option>'
		        +'       </select>'
		        +'   </td>'
		        +'   <td align="center">'
		        +'       <select id="required" >'
		        +'           <option value="">否</option>'
		        +'           <option value="required">是</option>'
		        +'       </select>'
		        +'    </td>'
		        +'   <td >'
		        +'        <textarea id="defaultValue" rows="2" style="width:95%"></textarea>'
		        +'   </td>'
		        +'</tr>';
			}
		});
	    editTable+='</table>';
	    var hideTip='主键、隐藏字段或者其他隐藏域，格式为[["id",""],["hideField","defaultValue"]]形式的数组，表示属性名和默认值';
	    editTable+='<table style="width:100%; border:0; margin-top:10px;">'
	        +'<tr>'
	        +'   <td><strong>编辑页面标题：</strong><input type="text" id="editName" value="xx编辑页面" style="width:85%"></td>'
	        +'</tr>'
	        +'<tr>'
	        +'   <td><strong>隐藏字段：</strong><input type="text" id="hideFields" style="width:85%" '
	        +' placeholder=\''+hideTip+'\' title=\''+hideTip+'\'></td>'
	        +' </tr>'
	        +'</table>';
		$("#editPageSetting").html(editTable);
		$("#editPageTip").dialog("close");
	}
}
//选择字段页面数据类型后设置默认值
function setDefaultValue(obj){
	var $dv=$(obj).parent().nextAll().find("#defaultValue");
	if($(obj).val()=="select"){
		$dv.val('[["value1","显示名称1"], ["value2","显示名称2"]]');
		$dv.attr('title','格式为：[["value1","显示名称1"], ["value2","显示名称2"]]，表示select下拉框的值');
	}else if($(obj).val()=="textarea"){
		$dv.val('[5,"100","90%",1000]');
		$dv.attr('title','格式为：[5,"100","90%",1000]，分别表示 rows、 cols、width和最大输入长度，如果有width，则可以没有cols');
	}else if($(obj).val()=="richText"){//富文本
		$dv.val('["95%","300px"]');
		$dv.attr('title','格式为：["95%","300px"]，分别表示编辑框的宽度和高度');
	}else{
		$dv.val('');
		$dv.attr('title','');
	}
}


//弹出选择字段对话框
function selectField(obj,csfid){
	var canSelectFielsList="";
	for(var o in allFields){
		var field = allFields[o];
		var str=field.fieldName+"《"+field.comment+"》";
		canSelectFielsList+="<div style='border-bottom: 1px solid #b1cde3;padding:2px 10px; cursor:pointer; ' " +
				"onclick=\"selectThis('"+$(obj).attr("id")+"', '"+str+"','"+csfid+"')\">"+str+"</div>";
	}
	$("#"+csfid).fadeIn();
	$("#"+csfid).html(canSelectFielsList);
	$("#"+csfid).css("top", $(obj).position().top+20).css("left", $(obj).position().left+10);
	$("#"+csfid).click(function (event) { 
		event.stopPropagation();//阻止事件向上冒泡 
	});
}
//选中某个字段
function selectThis(id,value,csfid){
	$("#"+id).val(value);
	$("#"+csfid).fadeOut();
}
//隐藏对话框
function hideFiels(event,csfid){
	$("#"+csfid).fadeOut(); 
	event.stopPropagation();//阻止事件向上冒泡 
}


var viewTip=null;
var viewTable=null;

//重选选择查看页面字段
function reselectViewFields(){
	if(allFields==null){
		alert("请先输入数据库表名读取字段信息！");
		$("#tableName").focus();
		return;
	}
	if(viewTip==null)
	    resetViewRowCol();
	else{
		$("#viewPageTip").dialog();
		$("#viewPageTip").parent().css("top","30px").css("left","5%").width("90%").height("75%");
		$("#viewPageTip").height("95%");
	}
}

/**
 * 查看页面字段设置
 */
function setViewPage(str){
	if(str=="selectField"){//选择字段并设置布局
		var rowNum=$("#viewPageTip").find("#rowNum").val();
		var colNum=$("#viewPageTip").find("#colNum").val();
		viewTip='<p>请在下列表格中选择查看页面上的字段布局位置'
			+'（注意：只需选择每行有哪些字段及其顺序即可，每一行从第一列开始依次选择，'
			+'如有合并列的情况则后面的留空，其他属性下一步再设置）</p>';
		viewTip+='<table id="selectedViewFields" width="100%">';
		for(var i=1;i<=rowNum;i++){
			viewTip+='<tr>';
			for(var j=1;j<=colNum;j++){
				viewTip+='<td><input id="viewField_'+i+'_'+j+'" type="text" onclick="selectField(this,\'canSelectViewFields\')"'
				    +'style="width:90%"  onblur="hideFiels(event,\'canSelectViewFields\')"></td>';
			}
			viewTip+='</tr>';
		}
		viewTip+='</table>';
		viewTip+='<div style="margin-top:15px">'
			+'<input type="button"  value="确定" onclick="setViewPage(\'setField\')">&nbsp;&nbsp;&nbsp;&nbsp;'
			+'<input type="button"  value="重置行数和列数" onclick="resetViewRowCol()"></div>'
			+'<div id="canSelectViewFields" style="position:absolute; display:none;background-color:#698B69"></div>';
		$("#viewPageTip").html(viewTip);
		$("#viewPageTip").dialog();
		$("#viewPageTip").parent().css("top","30px").css("left","5%").width("90%").height("75%");
		$("#viewPageTip").height("95%");
	}else if(str=="setField"){//设置字段相关属性
		viewTable='<table id="viewFields" style="width:100%; border:0; ">'
	        +'<tr>'
	        +'    <td class="headTd" style="width:15%">行列号</td>'
	        +'    <td class="headTd" style="width:25%">字段名称</td>'
	        +'    <td class="headTd" style="width:25%">字段中文名</td>'
	        +'   <td class="headTd" style="width:11%">名称列宽度</td>'
	        +'   <td class="headTd" style="width:12%">值所在列宽度</td>'
	        +'   <td class="headTd" style="width:12%">值所在列合并列数</td>'
	        +'</tr>';
		$("#selectedViewFields").find("input[type='text']").each(function(){
			if($(this).val()!=""){
				var xy=$(this).attr("id").split("_");
				var value=$(this).val().split("《");
				viewTable+='<tr id="viewFieldTr"  flag="data" row="'+xy[1]+'" col="'+xy[2]+'">'
				+'   <td id="rowcolNum" align="center"><b>第<font color="red">'
				        +xy[1]+'</font>行第<font color="red">'+xy[2]+'</font>列</b></td>'
		        +'   <td id="fieldName">'+value[0]+'</td>'
		        +'   <td><input type="text" id="comment" style="width:90%" value="'+value[1].split("》")[0]+'"></td>'
		        +'   <td align="center"><input type="text" id="fieldWidth" style="width:90%" placeholder="单列宽度"></td>'
		        +'   <td align="center"><input type="text" id="valueWidth" style="width:90%"></td>'
		        +'   <td align="center"><input type="text" id="valueColSpanNum" style="width:90%" value="1"></td>'
		        +'</tr>';
			}
		});
	    viewTable+='</table>';
	    viewTable+='<table style="width:100%; border:0; margin-top:10px;">'
	        +'<tr>'
	        +'   <td><strong>查看页面标题：</strong><input type="text" id="viewName" value="xx查看页面" style="width:85%"></td>'
	        +'</tr>'
	        +'</table>';
		$("#viewPageSetting").html(viewTable);
		$("#viewPageTip").dialog("close");
	}
}



/**
 * 预览相应页面效果
 * @param str
 */
function view(str){
	if(allFields==null){
		alert("请先输入数据库表名读取字段信息！");
		$("#tableName").focus();
		return;
	}
	var ss="";
	if(str=="listPage"){
		getListFieldConfig();
		if(listFields.length==0){
			alert("你还没有选择字段！");
			return;
		}
		ss='<div>'+
	        '<select id="searchType" name="searchType" onChange="changeType()"></select>'+
	        '<span id="key"><input type="text" id="keywords" name="keywords"></span>'+
	        '<input type="button" value="查询" onclick="search();">'+
	    '</div>'+
	    '<div id="pageDiv" class="list"></div>';
		$("#viewDialog").html(ss);
		initPage();
		$("#dataRow").show();
		$("#dataRow").parent().append($("#dataRow").clone());
	}else if(str=="editPage"){
		getEditFieldConfig();
		if(editFields.length==0){
			alert("你还没有选择字段！");
			return;
		}
		ss='<form id="editForm" name="editForm"  method="post" target="_self"><div id="editPageDiv"></div>'+
		  '<div class="boxDiv" align="center" style="margin:10px 0;">'+
		  '    <input type="button" value="保存" >&nbsp;&nbsp;'+
		  '    <input type="button" value="取消" >'+
		  '</div></form>';
		$("#viewDialog").html(ss);
		initEditPage();
	}else if(str=="viewPage"){
		getViewFieldConfig();
		if(viewFields.length==0){
			alert("你还没有选择字段！");
			return;
		}
		ss='<div id="viewPageDiv"></div>';
		$("#viewDialog").html(ss);
		initViewPage();
	}
	if(ss!=""){
		$("#viewDialog").dialog();
		$("#viewDialog").parent().css("top","30px").css("left","5%").css("min-height","50%").width("90%").height("auto");
	}
}
//编辑页面处理字段的方法
function dealField(fields){
	return defaultDealField(fields);
}

//获取列表页面字段配置信息
function getListFieldConfig(){
	servletName=$("#servletName").val();
	editUrl=$("#editUrl").val();
	viewUrl=$("#viewUrl").val();
	listFields = new Array();
	searchFields = new Array();
	$("table#fields tr[flag='data']").each(function () {
		var fieldName=$(this).find("#fieldName").html();
		var comment=$(this).find("#comment").val();
		if($(this).find("#listChkId").prop("checked")){
			var fieldWidth=$(this).find("#fieldWidth").val();
			var fieldAlign=$(this).find("#fieldAlign").val();
			var f=new Array();
			f.push(fieldName);
			f.push(comment);
			f.push(fieldWidth);
			f.push(fieldAlign);
			listFields.push(f);
		}
		if($(this).find("#qChkId").prop("checked")){
			var sf=new Array();
			sf.push(fieldName);
			sf.push(comment);
			searchFields.push(sf);
		}
	});
}

//获取编辑页面字段配置信息
function getEditFieldConfig(){
	editFields = new Array();
	var r="1";
	var rfs =  new Array();
	$("table#editFields tr[flag='data']").each(function () {
		if($(this).attr("row")!=r){
			editFields.push(rfs);
			r=$(this).attr("row");
			rfs =  new Array();
		}
		var fieldName=$(this).find("#fieldName").html();
		var comment=$(this).find("#comment").val();
		var fieldWidth=$(this).find("#fieldWidth").val();
		var valueWidth=$(this).find("#valueWidth").val();
		var valueColSpanNum=$(this).find("#valueColSpanNum").val();
		var dataType=$(this).find("#dataType").val();
		var required=$(this).find("#required").val();
		var defaultValue=$(this).find("#defaultValue").val();
		if(dataType=="select" || dataType=="textarea" || dataType=="richText")
		    eval('defaultValue='+defaultValue+";");
		var f=new Array();
		f.push(fieldName);
		f.push(comment);
		f.push(fieldWidth);
		f.push(valueWidth);
		f.push(valueColSpanNum);
		f.push(dataType);
		f.push(required);
		f.push(defaultValue);
		rfs.push(f);
	});
	if(rfs.length>0)
	    editFields.push(rfs);
	hideFields=[];
	if($("#hideFields").val()!=null && $("#hideFields").val()!="")
	    eval('hideFields = '+$("#hideFields").val()+';');
}

//获取查看页面字段配置信息
function getViewFieldConfig(){
	viewFields = new Array();
	var r="1";
	var rfs =  new Array();
	$("table#viewFields tr[flag='data']").each(function () {
		if($(this).attr("row")!=r){
			viewFields.push(rfs);
			r=$(this).attr("row");
			rfs =  new Array();
		}
		var fieldName=$(this).find("#fieldName").html();
		var comment=$(this).find("#comment").val();
		var fieldWidth=$(this).find("#fieldWidth").val();
		if(fieldWidth=="")
			fieldWidth="15%";
		var valueWidth=$(this).find("#valueWidth").val();
		var valueColSpanNum=$(this).find("#valueColSpanNum").val();
		var f=new Array();
		f.push(fieldName);
		f.push(comment);
		f.push(fieldWidth);
		f.push(valueWidth);
		f.push(valueColSpanNum);
		rfs.push(f);
	});
	if(rfs.length>0)
		viewFields.push(rfs);
}

var canSave=true;
function checkBeforeSave(){
	var ss='<div id="checkInfo"><div style="margin:15px 0">正在检查页面配置，请稍后……</h5></div>';
	$("#saveCheckInfo").html(ss);
	$("#saveCheckInfo").dialog();
	$("#saveCheckInfo").parent().css("top","50px").css("left","25%").css("min-height","40%").width("50%").height(320);
	$("#saveCheckInfo").height(250);
	getListFieldConfig();
	var info="";
	if(listFields.length==0){
		info="<font color='red'>没有选择列表显示字段！</font>";
		canSave=false;
	}
	if(searchFields.length==0){
		info+="<font color='#CDAD00'>没有选择列表查询字段！</font>";
	}
	$("#checkInfo").append("<div style='margin-bottom:15px'><font color='#218868'>列表页面配置检查完毕！</font>"+info+"</div>");
	getEditFieldConfig();
	info="";
	if(editFields.length==0){
		info="<font color='red'>没有选择可编辑字段！</font>";
		canSave=false;
	}
	if(hideFields.length==0){
		info+="<font color='red'>没有设置隐藏字段字段(包括主键id)！</font>";
		canSave=false;
	}
	$("#checkInfo").append("<div style='margin-bottom:15px'><font color='#218868'>编辑页面配置检查完毕！</font>"+info+"</div>");
	getViewFieldConfig();
	info="";
	if(viewFields.length==0){
		info="<font color='red'>没有选择用于查看的字段！</font>";
		canSave=false;
	}
	$("#checkInfo").append("<div style='margin-bottom:15px'><font color='#218868'>查看页面配置检查完毕！</font>"+info+"</div>");
	$("#checkInfo").append("<div style='margin-top:15px' class='boxDiv' align='center'>"
			+"<input type='button' value='好了，保存' onclick='save();'>"
	        +"<input type='button' value='返回修改' onclick='$(\"#saveCheckInfo\").dialog(\"close\");'>");
}

function save(){
	if(!canSave){
		alert("页面设置中还有未完成项，可能导致不能正常生成相应页面，请按提示信息修改后重试！");
		$("#saveCheckInfo").dialog("close");
		return;
	}else{
		var tableName = $("#tableName").val();
		var servletName=$("#servletName").val();
		var listName=$("#listName").val();
		var editUrl=$("#editUrl").val();
		var viewUrl=$("#viewUrl").val();
		var pageSize=$("#pageSize").val();
		var editName=$("#editName").val();
		var viewName=$("#viewName").val();
		$.post("fieldServlet", {"opt":"create", "tableName" : tableName, "servletName" : servletName, "listName" : listName,
			"listFields" : JSON.stringify(listFields), "searchFields" : JSON.stringify(searchFields), 
			"editUrl" : editUrl, "viewUrl" : viewUrl, "pageSize" : pageSize,
			"editName" : editName, "editFields" : JSON.stringify(editFields), "hideFields" : JSON.stringify(hideFields), 
			"viewName" : viewName, "viewFields" : JSON.stringify(viewFields)},
				function (data, textStatus){
	    			if(data.result=="ok"){
	    				var ss="<h1>保存成功！</h1>";
	    				$("#saveCheckInfo").html(ss+data.info);
	    			}
				}, 
		"json");
	}
	
}
