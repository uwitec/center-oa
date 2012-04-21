<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="回款管理" link="true" guid="true" cal="true" dialog="true" />
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>
<script type="text/javascript">

var gurl = '../finance/bank.do?method=';
var addUrl = '../finance/addBank.jsp';
var ukey = 'Payment';

var allDef = window.top.topFrame.allDef;
var guidMap;
var thisObj;
function load()
{
     preload();
     
     guidMap = {
         title: '回款列表',
         url: gurl + 'query' + ukey,
         colModel : [
             {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lstatus={status} lstafferId={stafferId}>', width : 40, align: 'center'},
             {display: '系统标识', name : 'id', width : '14%'},
             {display: '帐户', name : 'bankName', width : '12%'},
             {display: '类型', name : 'type', cc: 'paymentType', width : '5%'},
             {display: '状态', name : 'status', cc: 'paymentStatus', width : '5%'},
             {display: '认领人', name : 'stafferName', cc: 'paymentStatus', width : '5%'},
             {display: '回款来源', name : 'fromer', width : '8%'},
             {display: '客户', name : 'customerName', width : '10%'},
             {display: '回款/手续费', name : 'money', content: '{money}/{handling}',  width : '10%', toFixed: 2},
             {display: '回款时间', name : 'receiveTime', width : '8%', sortable : true},
             {display: '标识', name : 'refId', sortable : true, width : '12%'},
             {display: '备注', name : 'description', width : 'auto'}
             ],
         extAtt: {
             id : {begin : '<a href=' + gurl + 'find' + ukey + '&mode=2&id={id}>', end : '</a>'}
         },
         buttons : [
             {id: 'add', bclass: 'add',  onpress : addBean, auth: '1602'},
             {id: 'del', bclass: 'del',  onpress : delBean, auth: '1602'},
             {id: 'del1', bclass: 'del',  caption: '删除批次', onpress : delBean1, auth: '1602'},
             {id: 'export', bclass: 'replied',  caption: '导出查询结果', onpress : exports},
             {id: 'search', bclass: 'search', onpress : doSearch}
             ],
        <p:conf/>
     };
     
     $("#mainTable").flexigrid(guidMap, thisObj);
}
 
function $callBack()
{
    loadForm();
    
    highlights($("#mainTable").get(0), ['对私', '未认领'], 'red');
}

function addBean(opr, grid)
{
    $l(gurl + 'preForAdd' + ukey);
    //$l(addUrl);
}

function delBean1(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') && getRadio('checkb').lstatus == 0)
    {    
        if(window.confirm('确定删除此批次的回款单?'))    
        $ajax(gurl + 'batchDelete' + ukey + '&id=' + getRadioValue('checkb'), callBackFun);
    }
    else
    $error('不能操作');
}

function delBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') && getRadio('checkb').lstatus == 0)
    {    
        if(window.confirm('确定删除?'))    
        $ajax(gurl + 'delete' + ukey + '&id=' + getRadioValue('checkb'), callBackFun);
    }
    else
    $error('不能操作');
}

function updateBean()
{
	if (getRadio('checkb') && getRadioValue('checkb'))
	{	
		$l(gurl + 'find' + ukey + '&update=1&id=' + getRadioValue('checkb'));
	}
	else
	$error('不能操作');
}

function exports()
{
    document.location.href = gurl + 'exportPayment';
}

function doSearch()
{
    $modalQuery('../admin/query.do?method=popCommonQuery2&key=query' + ukey);
}

</script>
</head>
<body onload="load()" class="body_class">
<form name="mainForm" method="post">
<p:cache></p:cache>
</form>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
<p:query height="300"/>
</body>