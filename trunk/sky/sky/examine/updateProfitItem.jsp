<%@page contentType="text/html; charset=GBK"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="修改利润" link="true" guid="true" cal="false"/>
<script src="../js/common.js"></script>
<script src="../js/JCheck.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/json.js"></script>
<script src="../js/jquery.blockUI.js"></script>
<script type="text/javascript">
var guidMap;
var thisObj;

var allDef = window.top.topFrame.allDef;

function load()
{
	 guidMap = {
		 title: '变更利润',
		 url: '../examine/examine.do?method=queryProfitExamineForUpdate&id=${param.id}',
		 colModel : [
		     {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lstatus={status} lprofit={planValue}>', 
		                                                  width : 40, sortable : false, align: 'center'},
		     {display: '开始时间', name : 'beginTime', width : '20%', sortable : false, align: 'left'},
		     {display: '结束时间', name : 'endTime', width : '20%', sortable : false, align: 'left'},
		     {display: '计划利润', name : 'planValue', width : '15%', toFixed : 2, sortable : false, align: 'left'},
		     {display: '状态', name : 'status', width : 'auto', sortable : false, cc : 'examineItemStatus'}
		     ],
		 extAtt: {
		     //cityName : {begin : '<a href=../examine/city.do?method=findCityConfig&id={cityId}&update=1 title=查看明细>', end : '</a>'}
		 },
		 buttons : [
		     {id: 'update', bclass: 'update', onpress : updateBean, auth: '0305'},
		     {id: 'back', bclass: 'back', caption : '返回上一页', onpress : goBack}
		     ],
		 usepager: false,
		 useRp: true,
		 queryMode: 1,
		 auth: window.top.topFrame.gAuth,
		 cache: 0,
		 def: allDef,
		 queryCondition: null,
		 showTableToggleBtn: true,
		 callBack: loadForm
	 };
	 
	 $("#mainTable").flexigrid(guidMap, thisObj);
 }

function updateBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (getRadio('checkb').lstatus == 0)
        { 
    	   $O('profit').value = getRadio('checkb').lprofit;
    	
           $.blockUI({ message: $('#dataDiv'),css:{width: '40%'}});
        }
        else
        {
            alert('此考评项已经结束,不能修改');
        }
    }
}

function goBack()
{
    if (!window.opener)
    window.history.go(-1);
    else
    window.close();
}

function $close()
{
    $.unblockUI();
}

function $process(flag)
{
	if (eCheck([$O('profit'), $O('reason')]))
	{
    	var sid = $$('profit');
	
	    if (window.confirm('确定变更利润?'))
	    {
		    $ajax('../examine/examine.do?method=updateProfitExamine&id=' 
		    		+ getRadioValue('checkb') + '&reason=' + $$('reason') + '&newProfit=' + sid, callBackFun);
		    
		    $.unblockUI();
	    }
	}
}

function callBackFun(data)
{
    reloadTip(data.msg, data.ret == 0);
    
    if (data.ret == 0)
    commonQuery();
}
</script>
</head>
<body onload="load()" class="body_class">
<form name="mainForm" action="../examine/examine.do" method="post">
<input type="hidden" name="method" value=""/>
<p:cache></p:cache>
</form>
<div id="dataDiv" style="display:none">
<p align='left'><label><font color=""><b>请输入利润</b></font></label></p>
<p><label>&nbsp;</label></p>
更新利润：<input type="text" name="profit" value="" oncheck="isFloat" style="width: 80%"/>
<p><label>&nbsp;</label></p>
<p><label>&nbsp;</label></p>
更新原因：<textarea name="reason" value="" rows="4" oncheck="notNone;maxLength(100)" style="width: 80%"></textarea>
<p><label>&nbsp;</label></p>
<p>
<input type='button' value='&nbsp;&nbsp;确 定&nbsp;&nbsp;' id='div_b_ok1' class='button_class' onclick='$process(0)'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input type='button' id='div_b_cancle' value='&nbsp;&nbsp;关 闭&nbsp;&nbsp;' class='button_class' onclick='$close()'/>
</p>
<p><label>&nbsp;</label></p>
</div>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
</body>
</html>