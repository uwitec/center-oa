<%@page contentType="text/html; charset=GBK"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="地市考核配置管理" link="true" guid="true" cal="false" dialog="true" />
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/json.js"></script>
<script type="text/javascript">
var guidMap;
var thisObj;
function load()
{
     preload();
     
	 guidMap = {
		 title: '地市考核配置列表',
		 url: '../examine/city.do?method=queryCityConfig',
		 colModel : [
		     {display: '选择', name : 'check', content : '<input type=radio name=checkb value={cityId} lname={cityName} lsail={bespread}>', width : 40, sortable : false, align: 'center'},
		     {display: '地市', name : 'cityName', width : '30%', sortable : false, align: 'left'},
		     {display: '产品铺样', name : 'bespreadName', width : 'auto', align: 'left'}
		     ],
		 extAtt: {
		     //cityName : {begin : '<a href=../examine/city.do?method=findCityConfig&id={cityId}&update=1 title=查看明细>', end : '</a>'}
		 },
		 buttons : [
		     {id: 'update1', bclass: 'update', caption: '配置区域利润', onpress : updateProfit, auth: '0302'},
		     {id: 'update2', bclass: 'update', caption: '配置铺样指标', onpress : preForUpdateCitySail, auth: '0302'},
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
		 callBack: loadForm
	 };
	 
	 $("#mainTable").flexigrid(guidMap, thisObj);
	 
	 $('#dlg').dialog({
                //iconCls: 'icon-save',
                modal:true,
                closed:true,
                buttons:{
                    '确 定':function(){
                        configBean();
                    },
                    '取 消':function(){
                        $('#dlg').dialog({closed:true});
                    }
                }
     });
	 
	 $ESC('dlg');
 }
 
function doSearch()
{
    $modalQuery('../admin/query.do?method=popCommonQuery2&key=queryCityConfig');
}


function updateBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
       $l('../examine/city.do?method=findStaffer&id=' + getRadioValue('checkb') + '&update=1');
    }
}

function updateProfit(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
       $l('../examine/queryCityProfit.jsp?cityId=' + getRadioValue('checkb'));
    }
    else
    {
    	$error();
    }
}

function configBean()
{
	var newSail = $$('citySail');
	if (getRadio('checkb') && getRadioValue('checkb') && newSail != '')
	{
		$ajax('../examine/city.do?method=updateCityConfigSail&id=' + getRadioValue('checkb') + '&sailId=' + newSail, callSucess);
	}
}

function callSucess(data)
{
	$('#dlg').dialog({closed:true});
	callBackFun(data);
}

function preForUpdateCitySail()
{
	if (getRadio('checkb') && getRadioValue('checkb'))
    {
       $ajax('../examine/city.do?method=listCitySail', callBackFunSail);
    }
    else
    {
    	$error();
    }
}

function callBackFunSail(data)
{
	if ($O('dia_inner').innerHTML != '')
	{
		$Set2('citySail', getRadio('checkb').lsail);
    
        $('#dlg').dialog({closed:false});
		return;
	}
	
	$O('dia_inner').innerHTML = '';
    
    var logs = data.msg;
    
    var htm = '';
    
    for(var i = 0; i < logs.length; i++)
    {
        var item = logs[i];
        
        var llog = '<input type=radio name=citySail value=' + item.id + '>' + item.name + '<br>';
        
        htm += llog;
    }
    
    $O('dia_inner').innerHTML = htm;
    
    $Set2('citySail', getRadio('checkb').lsail);
    
    $('#dlg').dialog({closed:false});
}

function commonQuery(par)
{
    gobal_guid.p.queryCondition = par;
    
    gobal_guid.grid.populate(true);
}
</script>
</head>
<body onload="load()" class="body_class">
<form name="mainForm" method="post">
<input type="hidden" name="method" value=""/>
<input type="hidden" name="stafferId" value=""/>

<p:cache></p:cache>
</form>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
<div id="dlg" title="选择铺样" style="width:320px;">
    <div style="padding:20px;height:200px;" id="dia_inner" title="">
   </div>
</div>
<p:query/>
</body>