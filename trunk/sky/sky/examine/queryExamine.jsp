<%@page contentType="text/html; charset=GBK"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="业务考评管理" link="true" guid="true" cal="false" dialog="true" />
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/json.js"></script>
<script src="../js/JCheck.js"></script>
<script src="../js/jquery.blockUI.js"></script>
<script src="../js/oa/queryExamine.js"></script>
<script type="text/javascript">

var allDef = window.top.topFrame.allDef;
var guidMap;
var thisObj;

var updatFlag = window.top.topFrame.containAuth('0304') ? '1' : '0';
function load()
{
     preload();
     
	 guidMap = {
		 title: '业务考核列表',
		 url: '../examine/examine.do?method=queryExamine',
		 colModel : [
		     {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lname={name} sstatus={status} atttype={attType} abs={abs}>', width : 40, sortable : false, align: 'center'},
		     {display: '名称', name : 'name', width : '20%', sortable : false, align: 'left'},
		     {display: '被考核人', name : 'stafferName', width : '8%', sortable : false, align: 'left'},
		     {display: '年度', name : 'year', width : '5%', sortable : true, align: 'left'},
		     {display: '类型', name : 'attType', width : '10%', sortable : false, align: 'left', cc: 'attType'},
		     {display: '状态', name : 'status', width : '10%', sortable : false, align: 'left', cc: 'examineStatus'},
		     {display: '业绩', name : 'totalProfit', width : '8%', sortable : false},
		     {display: '父考核', name : 'parentName', width : '10%', sortable : false},
		     {display: '分类', name : 'type', width : '8%', sortable : false, cc: 'examineType'},
		     {display: '时间', name : 'logTime', width : 'auto', sortable : true, align: 'left'}
		     ],
		 extAtt: {
		     name : {begin : '<a href=../examine/examine.do?method=findExamine&id={id}>', end : '</a>'}
		 },
		 buttons : [
		     {id: 'add', bclass: 'add', onpress : addBean, auth: '0304'},
		     {id: 'update', bclass: 'update', onpress : updateBean, auth: '0304'},
		     {id: 'submits', bclass: 'pass', caption: '提交', onpress : submitBean, auth: '0304'},
		     {id: 'del', bclass: 'delete', onpress : delBean, auth: '0304'},
		     {id: 'update1', caption: '配置考核项', bclass: 'edit', onpress : configYear, auth: '0304'},
		     {id: 'update3', caption: '配置全部子考核', bclass: 'edit', onpress : configAllSubItem, auth: '0305'},
		     {id: 'update4', caption: '变更考核', bclass: 'edit', onpress : changeItem, auth: '0304'},
		     {id: 'update5', caption: '废弃考核', bclass: 'delete', onpress : delBean2, auth: '0304'},
		     {id: 'current',  caption: '查看进度', bclass: 'search', onpress : queryCurrent},
		     {id: 'current1',  caption: '查看子考核进度', bclass: 'search', onpress : queryAllSubCurrent},
		     {id: 'log',  caption: '考评日志', bclass: 'search', onpress : logBean},
		     {id: 'search', bclass: 'search', onpress : doSearch}
		     ],
		 usepager: true,
		 useRp: true,
		 queryMode: 0,
		 cache: 0,
		 auth: window.top.topFrame.gAuth,
		 showTableToggleBtn: true,
		 height: DEFAULT_HEIGHT,
		 def: allDef,
		 callBack: loadForm //for firefox load ext att
	 };
	 
	 $("#mainTable").flexigrid(guidMap, thisObj);
 }
 


</script>
</head>
<body onload="load()" class="body_class">
<form>
<p:cache></p:cache>
<div id="dataDiv" style="display:none">
<p align='left'><label><font color=""><b>请输入退回原因</b></font></label></p>
<p><label>&nbsp;</label></p>
<textarea name="reason" value="" rows="4" oncheck="notNone;maxLength(100)" style="width: 85%"></textarea>
<p><label>&nbsp;</label></p>
<p>
<input type='button' value='&nbsp;&nbsp;确 定&nbsp;&nbsp;' id='div_b_ok1' class='button_class' onclick='$process()'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input type='button' id='div_b_cancle' value='&nbsp;&nbsp;关 闭&nbsp;&nbsp;' class='button_class' onclick='$close()'/>
</p>
<p><label>&nbsp;</label></p>
</div>

<div id="logDiv" style="display:none">
<p align='left'><label><font color=""><b>考评日志:</b></font></label></p>
<p><label>&nbsp;</label></p>
<div id="logD" align='left'>
</div>
<p><label>&nbsp;</label></p>
<p>
<input type='button' id='div_b_cancle' value='&nbsp;&nbsp;关 闭&nbsp;&nbsp;' class='button_class' onclick='$close()'/>
</p>
<p><label>&nbsp;</label></p>
</div>
</form>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
<p:query/>
</body>