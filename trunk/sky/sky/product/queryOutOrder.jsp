<%@page contentType="text/html; charset=GBK"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="订货管理" link="true" guid="true" cal="true" dialog="true" />
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>
<script type="text/javascript">

var allDef = window.top.topFrame.allDef;
var guidMap;
var thisObj;
function load()
{
     preload();
     
     guidMap = {
         title: '订货列表',
         url: '../product/product.do?method=queryOutOrder',
         colModel : [
             {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lstatus={status}>', width : 40, align: 'center'},
             {display: '产品', name : 'productName', width : '15%'},
             {display: '编码', name : 'productCode', width : '8%'},
             {display: '状态', name : 'status', width : '8%', cc: 'productOrderStatus'},
             {display: '订货量', name : 'orderAmount', width : '8%'},
             {display: '订货职员', name : 'stafferName', width : '8%'},
             {display: '备注', name : 'description', width : '20%'},
             {display: '失效', name : 'endTime', width : '15%'},
             {display: '时间', name : 'logTime', sortable : true, cname: 'logTime',  width : 'auto'}
             ],
         extAtt: {
             //title : {begin : '<a href=../product/product.do?method=findMake&id={id}>', end : '</a>'}
         },
         buttons : [
             {id: 'add', bclass: 'add', caption: '增加预定', onpress : addBean, auth: '1001'},
             {id: 'update', bclass: 'update', caption: '更新预定', onpress : updateBean, auth: '1001'},
             {id: 'delete', bclass: 'delete', caption: '取消预定', onpress : deldBean, auth: '1001'},
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
    
    highlights($("#mainTable").get(0), ['结束'], 'red');
    highlights($("#mainTable").get(0), ['预定'], 'blue');
}


function addBean(opr, grid)
{
    $l('../product/addOutOrder.jsp');
}

function updateBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') 
        && getRadio('checkb').lstatus == 0)
    $l('../product/product.do?method=findOutOrder&update=1&id=' + $$('checkb'));
    else
    $error('不能操作');
}

function deldBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') 
        && getRadio('checkb').lstatus == 0 && window.confirm('确定订购正常结束(即已经开单)?'))
    $ajax('../product/product.do?method=cancelOutOrder&id=' + getRadioValue('checkb'), callBackFun);
    else
    $error('不能操作');
}

function doSearch()
{
    $modalQuery('../admin/query.do?method=popCommonQuery2&key=queryOutOrder');
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