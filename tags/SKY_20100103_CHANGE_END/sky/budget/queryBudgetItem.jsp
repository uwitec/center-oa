<%@page contentType="text/html; charset=GBK"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="预算项" link="true" guid="true" cal="false"/>
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/json.js"></script>
<script type="text/javascript">

var allDef = window.top.topFrame.allDef;
var guidMap;
var thisObj;

function load()
{
	 guidMap = {
		 title: '预算项列表',
		 url: '../budget/budget.do?method=queryFeeItem',
		 colModel : [
		     {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lname={name}>', width : 40, sortable : false, align: 'center'},
		     {display: '名称', name : 'name', width : 'auto', sortable : false, align: 'left'}
		     ],
		 extAtt: {
		     name : {begin : '<a href=../budget/budget.do?method=findFeeItem&id={id}&update=1>', end : '</a>'}
		 },
		 buttons : [
		     {id: 'add', bclass: 'add', onpress : addBean, auth: '0502'},
		     {id: 'update', bclass: 'update', onpress : updateBean, auth: '0502'},
		     {id: 'del', bclass: 'delete', onpress : delBean, auth: '0502'}
		     ],
		 usepager: true,
		 useRp: true,
		 queryMode: 1,
		 cache: 0,
		 auth: window.top.topFrame.gAuth,
		 showTableToggleBtn: true,
		 height: DEFAULT_HEIGHT,
		 def: allDef,
		 callBack: loadForm //for firefox load ext att
	 };
	 
	 $("#mainTable").flexigrid(guidMap, thisObj);
 }

function addBean(opr, grid)
{
    $l('../budget/addOrUpdateFeeItem.jsp');
}

function delBean()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (window.confirm('确定删除--' + getRadio('checkb').lname))
        {
            $ajax('../budget/budget.do?method=delFeeItem&id=' + getRadioValue('checkb'), callBackFun);
        }
    }
}

function updateBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
       $l('../budget/budget.do?method=findFeeItem&update=1&id=' + getRadioValue('checkb'));
    }
}

</script>
</head>
<body onload="load()" class="body_class">
<form>
<p:cache/>
</form>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
</body>