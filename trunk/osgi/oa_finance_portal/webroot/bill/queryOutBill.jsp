<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="付款单管理" link="true" guid="true" cal="true" dialog="true" />
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>
<script type="text/javascript">

var gurl = '../finance/bill.do?method=';
var addUrl = '../finance/addOutBill.jsp';
var ukey = 'OutBill';

var allDef = window.top.topFrame.allDef;
var guidMap;
var thisObj;
function load()
{
     preload();
     
     guidMap = {
         title: '付款单列表',
         url: gurl + 'query' + ukey,
         colModel : [
             {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lstatus={status} llock={lock}>', width : 40, align: 'center'},
             {display: '标识', name : 'id', width : '15%'},
             {display: '帐号', name : 'bankName', sortable : true, cname: 'bankId', width : '20%'},
             {display: '类型', name : 'type', cc: 'outbillType', width : '8%'},
             {display: '付款方式', name : 'payType', cc: 'outbillPayType', width : '5%'},
             //{display: '锁定', name : 'lock', cc: 'billLock', width : '8%'},
             {display: '金额', name : 'moneys',  toFixed: 2, width : '8%'},
             {display: '单位', name : 'provideName', width : '10%'},
             {display: '职员', name : 'ownerName', width : '8%'},
             {display: '状态', name : 'status', cc: 'outbillStatus', width : '8%'},
             {display: '时间', name : 'logTime', sortable : true, width : 'auto'}
             ],
         extAtt: {
             id : {begin : '<a href=' + gurl + 'find' + ukey + '&id={id}>', end : '</a>'}
         },
         buttons : [
             {id: 'add', bclass: 'add', onpress : addBean, auth: '1607'},
             {id: 'update', bclass: 'update', caption: '更新核对', auth: '1607', onpress : updateOutBillBeanChecks},
             {id: 'del', bclass: 'delete', auth: '1607', onpress : delBean},
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
    $l(gurl + 'preForAdd' + ukey);
    //$l(addUrl);
}

function delBean(opr, grid)
{
    if (getRadio('checkb') && getRadioValue('checkb') && getRadio('checkb').llock == 0)
    {    
        if(window.confirm('确定删除?'))    
        $ajax(gurl + 'delete' + ukey + '&id=' + getRadioValue('checkb'), callBackFun);
    }
    else
    $error('不能操作');
}

function updateOutBillBeanChecks()
{
    if (getRadio('checkb') && getRadioValue('checkb') && getRadio('checkb').lstatus == 0)
    {   
        $.messager.prompt('总部核对', '请输入核对说明', '', function(msg){
                if (msg)
                {
                    $ajax('../finance/bill.do?method=updateOutBillBeanChecks&id=' 
                        + getRadioValue('checkb') + '&checks=' + ajaxPararmter(msg) , 
                        callBackFun);
                }
               
            }, 2);
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