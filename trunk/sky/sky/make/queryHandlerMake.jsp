<%@page contentType="text/html; charset=GBK"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="定制产品管理" link="true" guid="true" cal="true" dialog="true" />
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
         title: '定制产品处理列表',
         url: '../make/make.do?method=queryHandlerMake',
         colModel : [
             {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lstatus={status} lposition={position}>', width : 40, align: 'center'},
             {display: '标题', name : 'title', width : '30%'},
             {display: '环数', name : 'token', content: '第{status}环', width : '8%'},
             {display: '环节', name : 'statusName', width : '8%'},
             {display: '位置', name : 'positionName', width : '8%'},
             {display: '状态', name : 'endType', width : '8%', cc: 'exceptionReason'},
             {display: '创建人', name : 'createrName', width : '6%'},
             {display: '处理人', name : 'handerName', width : '8%'},
             {display: '类型', name : 'type', width : '8%', cc: 'makeType'},
             {display: '申请时间', name : 'logTime', sortable : true, width : 'auto'}
             ],
         extAtt: {
             title : {begin : '<a href=../make/make.do?method=findMake&hanlerMode=1&id={id}>', end : '</a>'}
         },
         buttons : [
             {id: 'update', bclass: 'update', caption: '处理申请', onpress : updateBean, auth: 'true'},
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
    
    highlights($("#mainTable").get(0), ['结束', '第9999环'], 'blue');
}

function addBean(opr, grid)
{
    $l('../make/addMake01.jsp');
}

function delBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') 
        && getRadioValue('checkb').lstatus == 1 && getRadioValue('checkb').lposition == 11 && window.confirm('确定删除申请?'))
    $ajax('../make/make.do?method=delMake&id=' + getRadioValue('checkb'), callBackFun);
    else
    $error('不能操作');
}

function updateBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') 
        && getRadioValue('checkb').lposition != 11)
    $l('../make/make.do?method=findMake&hanlerMode=1&id=' + getRadioValue('checkb'));
    else
    $error('不能操作');
}

function doSearch()
{
    $modalQuery('../admin/query.do?method=popCommonQuery2&key=queryHandlerMake');
}

</script>
</head>
<body onload="load()" class="body_class">
<form name="mainForm"  method="post">
<p:cache></p:cache>
</form>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
<p:query/>
</body>