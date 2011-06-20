<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="客户预收管理" link="true" guid="true" cal="true" dialog="true" />
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>
<script type="text/javascript">

var gurl = '../finance/bill.do?method=';
var addUrl = '../finance/addInBill.jsp';
var ukey = 'InBill';

var allDef = window.top.topFrame.allDef;
var guidMap;
var thisObj;
function load()
{
     preload();
     
     guidMap = {
         title: '收款单列表',
         url: gurl + 'querySelf' + ukey,
         colModel : [
             {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lstatus={status}>', width : 40, align: 'center'},
             {display: '标识', name : 'id', width : '15%'},
             {display: '帐号', name : 'bankName', width : '10%'},
             {display: '类型', name : 'type', cc: 'inbillType', width : '8%'},
             {display: '状态', name : 'status', cc: 'inbillStatus', width : '8%'},
             {display: '金额', name : 'moneys',  toFixed: 2, width : '8%'},
             {display: '客户', name : 'customerName', width : '10%'},
             {display: '职员', name : 'ownerName', width : '8%'},
             {display: '时间', name : 'logTime', sortable : true, width : 'auto'}
             ],
         extAtt: {
             id : {begin : '<a href=' + gurl + 'find' + ukey + '&id={id}>', end : '</a>'}
         },
         buttons : [
             {id: 'update', bclass: 'update', caption: '申请预收退款', onpress : splitInBill},
             {id: 'update1', bclass: 'update', caption: '预收转费用', onpress : splitInBill2},
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

function splitInBill(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {    
        $.messager.prompt('申请预收退款', '请输入预收退款金额(只能是数字)', 0.0,
            function(value, isOk)
            {
                if (isOk)
                if (isFloat(value))
                $ajax('../finance/bill.do?method=splitInBill&id=' + $$('checkb') + '&newMoney=' + value, callBackFun);
                else
                $error('只能输入数字');           
            });
    }
    else
    $error('不能操作');
}

function splitInBill2(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {   
        $.messager.prompt('预收转费用', '请输入备注', '',
            function(value, isOk)
            {
                if (isOk)
                $ajax2('../finance/bank.do?method=drawPayment4&billId=' + $$('checkb'), {'reason' : value},  callBackFun);
            });
    }
    else
    $error('不能操作');
}

function doSearch()
{
    $modalQuery('../admin/query.do?method=popCommonQuery2&key=querySelf' + ukey);
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