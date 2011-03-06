<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="开票管理" link="true" guid="true" cal="true" dialog="true" />
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>
<script type="text/javascript">

var gurl = '../finance/invoiceins.do?method=';
var addUrl = '../invoiceins/addInvoiceins.jsp';
var ukey = 'Invoiceins';

var allDef = getAllDef();
var guidMap;
var thisObj;

var mode = '<p:value key="mode"/>';

function load()
{
     preload();
     
     guidMap = {
         title: '开票列表',
         url: gurl + 'query' + ukey + '&mode=' + mode,
         colModel : [
             {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id}>', width : 40, align: 'center'},
             {display: '开票单位', name : 'unit', width : '12%'},
             {display: '发票类型', name : 'invoiceName', cc: 'bankType', width : '15%'},
             {display: '状态', name : 'status', cc: 'invoiceinsStatus', width : '10%'},
             {display: '纳税实体', name : 'dutyName', width : '10%'},
             {display: '客户', name : 'customerName', width : '15%'},
             {display: '金额', name : 'moneys', width : '10%', toFixed: 2},
             {display: '开票人', name : 'stafferName', width : '8%'},
             {display: '时间', name : 'logTime', width : 'auto', sortable : true}
             ],
         extAtt: {
             unit : {begin : '<a href=' + gurl + 'find' + ukey + '&id={id}&mode=' + mode + '>', end : '</a>'}
         },
         buttons : [
             <c:if test="${mode == 0}">
             {id: 'add', bclass: 'add', caption: '申请开票', onpress : addBean, auth: '1401'},
             </c:if>
             <c:if test="${mode == 1}">
             {id: 'pass', bclass: 'pass', caption: '处理', onpress : doProcess, auth: '1604'},
             </c:if>
             <c:if test="${mode == 2}">
             {id: 'del', bclass: 'del',  onpress : delBean, auth: '1605'},
             </c:if>
             
             {id: 'search', bclass: 'search', onpress : doSearch}
             ],
        <p:conf/>
     };
     
     $("#mainTable").flexigrid(guidMap, thisObj);
}
 
function $callBack()
{
    loadForm();
    
}

function addBean(opr, grid)
{
    $l(gurl + 'preForAdd' + ukey + '&mode=' + mode);
    //$l(addUrl);
}

function delBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {    
        if(window.confirm('确定删除?'))    
        $ajax(gurl + 'delete' + ukey + '&id=' + getRadioValue('checkb'), callBackFun);
    }
    else
    $error('不能操作');
}

function doProcess()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {   
        $l(gurl + 'find' + ukey + '&update=1&id=' + getRadioValue('checkb') + '&mode=' + mode);
    }
    else
    $error('不能操作');
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
<p:query/>
</body>