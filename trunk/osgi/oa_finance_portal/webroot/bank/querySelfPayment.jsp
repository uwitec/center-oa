<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<style type="text/css">
.flexigrid div.fbutton .draw
{
    background: url(../css/flexigrid/images/get.png) no-repeat center left;
}

.flexigrid div.fbutton .odraw
{
    background: url(../css/flexigrid/images/oget.png) no-repeat center left;
} 
</style>
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
         title: '回款列表(回款认领被驳回后想重新认领请先退领再认领)',
         url: gurl + 'querySelfPayment',
         colModel : [
             {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lstatus={status} lstafferId={stafferId} luseall={useall}>', width : 40, align: 'center'},
             {display: '帐户', name : 'bankName', width : '18%'},
             {display: '类型', name : 'type', cc: 'paymentType', width : '5%'},
             {display: '状态', name : 'status', cc: 'paymentStatus', width : '5%'},
             //{display: '金额使用', name : 'useall', cc: 'paymentUseall', width : '8%'},
             {display: '认领人', name : 'stafferName',  width : '8%'},
             {display: '客户', name : 'customerName',  width : '8%'},
             {display: '回款来源', name : 'fromer', width : '8%'},
             {display: '回款金额', name : 'money', width : '8%', toFixed: 2},
             {display: '回款日期', name : 'receiveTime', width : '8%', sortable : true},
             {display: '标识', name : 'refId', sortable : true, width : '12%'},
             {display: '备注', name : 'description', width : 'auto'}
             ],
         extAtt: {
             //name : {begin : '<a href=' + gurl + 'find' + ukey + '&id={id}>', end : '</a>'}
         },
         buttons : [
             {id: 'get', bclass: 'draw', caption: '认领', onpress : drawBean},
             {id: 'oget1', bclass: 'odraw', caption: '退领', onpress : odrawBean},
             //{id: 'oget2', bclass: 'update', caption: '关联销售', onpress : refBean},
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
    
    highlights($("#mainTable").get(0), ['全部使用'], 'blue');
}

var currId = '${user.stafferId}';

function drawBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') && getRadio('checkb').lstatus == 0)
    {    
        $l(gurl + 'find' + ukey + '&id=' + getRadioValue('checkb'));
    }
    else
    $error('不能操作');
}

function refBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') 
        && getRadio('checkb').lstatus == 1 
        && getRadio('checkb').luseall == 0)
    {    
        $l(gurl + 'find' + ukey + '&mode=1&id=' + getRadioValue('checkb'));
    }
    else
    $error('不能操作');
}

function odrawBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') && getRadio('checkb').lstatus == 1 && getRadio('checkb').lstafferId == currId)
    {    
        if(window.confirm('确定退领此回款?'))    
        $ajax(gurl + 'drop' + ukey + '&id=' + getRadioValue('checkb'), callBackFun);
    }
    else
    $error('不能操作');
}



function doSearch()
{
    $modalQuery('../admin/query.do?method=popCommonQuery2&key=querySelfPayment');
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