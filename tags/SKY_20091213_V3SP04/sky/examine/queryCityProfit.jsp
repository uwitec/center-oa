<%@page contentType="text/html; charset=GBK"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="地市考核利润管理" link="true" guid="true" cal="false"/>
<script src="../js/common.js"></script>
<script src="../js/JCheck.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/json.js"></script>
<script src="../js/jquery.blockUI.js"></script>
<script type="text/javascript">
var guidMap;
var thisObj;

function load()
{
	 guidMap = {
		 title: '地市利润配置列表',
		 url: '../examine/city.do?method=queryCityProfit&cityId=${param.cityId}',
		 colModel : [
		     {display: '选择', name : 'check', content : '<input type=radio name=checkb value={cityId} lid={id} lname={cityName} lprofit={profit}>', width : 40, sortable : false, align: 'center'},
		     {display: '地市', name : 'cityName', width : '20%', sortable : false, align: 'left'},
		     {display: '月份', name : 'month', width : '15%', sortable : false, align: 'left'},
		     {display: '利润', name : 'profit', width : 'auto', sortable : false, align: 'left'}
		     ],
		 extAtt: {
		     //cityName : {begin : '<a href=../examine/city.do?method=findCityConfig&id={cityId}&update=1 title=查看明细>', end : '</a>'}
		 },
		 buttons : [
		     {id: 'update', bclass: 'update', onpress : updateBean, auth: '0302'},
		     {id: 'update1', bclass: 'update', caption: '更新总额',onpress : updateBean1, auth: '0302'},
		     {id: 'back', bclass: 'back', caption: window.opener ? '关闭' : '返回上一页', onpress : goBack}
		     ],
		 usepager: false,
		 useRp: true,
		 queryMode: 1,
		 auth: (window.opener && window.opener != window) ? window.opener.top.topFrame.gAuth : window.top.topFrame.gAuth,
		 cache: 0,
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
    	$O('profit').value = getRadio('checkb').lprofit;
    	
        $.blockUI({ message: $('#dataDiv'),css:{width: '40%'}});
    }
}

function updateBean1(opr, grid)
{
    $.blockUI({ message: $('#dataDiv1'),css:{width: '40%'}});
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
	if (eCheck([$O('profit')]))
	{
    	var sid = $$('profit');
	
	    if (window.confirm('确定修改区域的利润?'))
	    {
		    $ajax('../examine/city.do?method=updateCityProfit&cityId=' 
		    		+ getRadioValue('checkb') + '&id=' + getRadio('checkb').lid + '&profit=' + sid, callBackFun);
		    
		    $.unblockUI();
	    }
	}
}

function $process1()
{
    if (eCheck([$O('total')]))
    {
        var sid = $$('total');
    
        if (window.confirm('确定修改区域的利润?'))
        {
            $ajax('../examine/city.do?method=updateCityProfitByToatl&cityId=${param.cityId}' 
                    + '&total=' + sid, callBackFun);
            
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

function commonQuery(par)
{
    gobal_guid.p.queryCondition = par;
    
    gobal_guid.grid.populate(true);
}
</script>
</head>
<body onload="load()" class="body_class">
<form name="mainForm" action="../examine/city.do" method="post">
<input type="hidden" name="method" value=""/>
<p:cache></p:cache>
</form>
<div id="dataDiv" style="display:none">
<p align='left'><label><font color=""><b>请输入利润</b></font></label></p>
<p><label>&nbsp;</label></p>
<input type="text" name="profit" value="" oncheck="isFloat" style="width: 85%"/>
<p><label>&nbsp;</label></p>
<p>
<input type='button' value='&nbsp;&nbsp;确 定&nbsp;&nbsp;' id='div_b_ok1' class='button_class' onclick='$process(0)'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input type='button' id='div_b_cancle' value='&nbsp;&nbsp;关 闭&nbsp;&nbsp;' class='button_class' onclick='$close()'/>
</p>
<p><label>&nbsp;</label></p>
</div>
<div id="dataDiv1" style="display:none">
<p align='left'><label><font color=""><b>请输入总利润</b></font></label></p>
<p><label>&nbsp;</label></p>
<input type="text" name="total" value="0" oncheck="isInt" style="width: 85%"/>
<p><label>&nbsp;</label></p>
<p>
<input type='button' value='&nbsp;&nbsp;确 定&nbsp;&nbsp;' id='div_b_ok2' class='button_class' onclick='$process1()'/>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
<input type='button' id='div_b_cancle2' value='&nbsp;&nbsp;取 消&nbsp;&nbsp;' class='button_class' onclick='$close()'/>
</p>
<p><label>&nbsp;</label></p>
</div>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
</body>