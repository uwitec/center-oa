<%@page contentType="text/html; charset=GBK" errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="客户日志" link="true" guid="true" cal="false"/>
<script src="../js/json.js"></script>
<script src="../js/common.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/public.js"></script>
<script type="text/javascript">

var allDef = window.top.topFrame.allDef;
var guidMap;
var thisObj;
function load()
{
	 guidMap = {
		 title: '客户职员关系变动日志',
		 url: '../customer/customer.do?method=queryChangeLog',
		 colModel : [
		     {display: '客户', name : 'customerName', width : '20%', align: 'left'},
             {display: '编码', name : 'customerCode', width : '10%', align: 'left'},
             {display: '职员', name : 'stafferName', width : '20%', align: 'left'},
             {display: '类型', name : 'operation', width : '10%', align: 'left', cc: 'changeLogOpr'},
             {display: '时间', name : 'logTime', width : 'auto', sortable : true, align: 'left'}
		     ],
		 extAtt: {
		     //name : {begin : '<a href=../customer/provider.do?method=findHisProvider&id={id}>', end : '</a>'}
		 },
		 buttons : [
		     {id: 'search', bclass: 'search', onpress : doSearch}
		     ],
		 usepager: true,
		 useRp: true,
		 queryMode: 1,
		 cache: 0,
		 showTableToggleBtn: true,
		 height: DEFAULT_HEIGHT,
		 def: allDef,
		 callBack: loadForm
	 };
	 
	 $("#mainTable").flexigrid(guidMap, thisObj);
}

function doSearch()
{
    window.common.qmodal('../admin/query.do?method=popCommonQuery&key=queryChangeLog');
}

function callBackFun(data)
{
    reloadTip(data.msg, data.ret == 0);
    
    if (data.ret == 0)
    commonQuery();
}
</script>
</head>
<body class="body_class" onload="load()">
<form>
<p:cache></p:cache>
</form>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
</body>