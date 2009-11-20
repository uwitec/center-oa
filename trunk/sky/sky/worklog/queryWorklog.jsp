<%@page contentType="text/html; charset=GBK"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="工作日志管理" link="true" guid="true" cal="false"/>
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/json.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>
<script type="text/javascript">

var allDef = window.top.topFrame.allDef;
var guidMap;
var thisObj;
function load()
{
	 guidMap = {
		 title: '工作日志列表',
		 url: '../worklog/worklog.do?method=queryWorkLog',
		 colModel : [
		     {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} sstatus={status}>', width : 40, align: 'center'},
		     {display: '工作日期', name : 'workDate', width : '15%', sortable : true, align: 'left'},
		     {display: '星期N', name : 'week', width : '10%', sortable : false, align: 'left'},
		     {display: '职员', name : 'stafferName', width : '10%', sortable : false, align: 'left'},
		     {display: '状态', name : 'status', width : '10%', sortable : false, cc: 'workLogStatus'},
		     {display: '是否逾期', name : 'result', width : '10%', sortable : false, cc: 'workLogResult'},
		     {display: '提交时间', name : 'logTime', width : 'auto', sortable : true}
		     ],
		 extAtt: {
		     workDate : {begin : '<a href=../worklog/worklog.do?method=findWorkLog&id={id}>', end : '</a>'}
		 },
		 buttons : [
		     {id: 'add', bclass: 'add', onpress : addBean, auth: '0401'},
		     {id: 'update', bclass: 'update',  onpress : updateBean, auth: '0401'},
		     {id: 'del', bclass: 'del',  onpress : delBean, auth: '0401'},
		     {id: 'pass', bclass: 'pass', caption: '通过', onpress : submitBean, auth: '0401'},
		     {id: 'search', bclass: 'search', onpress : doSearch}
		     ],
		 usepager: true,
		 useRp: true,
		 queryMode: 1,
		 auth: window.top.topFrame.gAuth,
		 cache: 0,
		 height: DEFAULT_HEIGHT,
		 queryCondition: null,
		 showTableToggleBtn: true,
		 def: allDef,
		 callBack: $callBack
	 };
	 
	 $("#mainTable").flexigrid(guidMap, thisObj);
}

function $callBack()
{
    loadForm();
    
    $.highlight($("#mainTable").get(0), '延期', 'red');
}
 
function doSearch()
{
    window.common.qmodal('../admin/query.do?method=popCommonQuery&key=queryWorkLog');
}

function addBean(opr, grid)
{
    $l('../worklog/addWorklog.jsp');
}

function submitBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') && getRadio('checkb').sstatus == 0)
    if (window.confirm('确定提交日志?'))
    $ajax('../worklog/worklog.do?method=processWorkLog&operation=0&id=' + getRadioValue('checkb'), callBackFun);
}

function delBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') && getRadio('checkb').sstatus == 0)
    if (window.confirm('确定删除日志?'))
    $ajax('../worklog/worklog.do?method=processWorkLog&operation=1&id=' + getRadioValue('checkb'), callBackFun);
}

function updateBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') && getRadio('checkb').sstatus == 0)
    {
        $l('../worklog/worklog.do?method=findWorkLog&update=1&id=' + getRadioValue('checkb'));
    }
}

</script>
</head>
<body onload="load()" class="body_class">
<form name="mainForm" action="../examine/city.do" method="post">
<p:cache></p:cache>
</form>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
</body>