<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="资金校验管理" link="true" guid="true" cal="true" dialog="true" />
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/plugin/highlight/jquery.highlight.js"></script>
<script type="text/javascript">

var gurl = '../finance/finance.do?method=';
var addUrl = '../finance/addFinance.jsp';
var ukey = 'CheckView';

var allDef = getAllDef();
var guidMap;
var thisObj;
function load()
{
     preload();
     
     guidMap = {
         title: '资金校验列表',
         url: gurl + 'query' + ukey,
         colModel : [
             {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lstatus={checkStatus} ltype={type}>', width : 40, align: 'center'},
             {display: '标识', name : 'id', width : '15%'},
             {display: '类型', name : 'type', cc: 'checkType', width : '10%'},
             {display: '状态', name : 'checkStatus',  cc: 'pubCheckStatus', width : '10%'},
             {display: '职员', name : 'stafferName', width : '10%'},
             {display: '单位', name : 'unitName', width : '18%'},
             {display: '关联单据', name : 'refId', width : '18%'},
             {display: '时间', name : 'logTime',  sortable : true, width : 'auto'}
             ],
         extAtt: {
             id : {begin : '<a title=点击查看明细 href="javaScript:openDetail(\'{id}\', {type})">', end : '</a>'}
         },
         buttons : [
             {id: 'pass', bclass: 'pass', caption: '总部核对', onpress : checkBean, auth: '1803'},
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

function openDetail(id, type)
{
    if (type == 1)
    {
        $l('../product/product.do?method=findCompose&id=' + id);
    }
    
    if (type == 2)
    {
        $l('../product/product.do?method=findPriceChange&id=' + id);
    }
    
    if (type == 3)
    {
        $l('../finance/bill.do?method=findInBill&id=' + id);
    }
    
    if (type == 4)
    {
        $l('../finance/bill.do?method=findOutBill&id=' + id);
    }
    
    if (type == 5)
    {
        $l('../stock/stock.do?method=findStock&id=' + id);
    }
    
    if (type == 6)
    {
        $l('../sail/out.do?method=findOut&fow=99&outId=' + id);
    }
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

function checkBean()
{
	if (getRadio('checkb') && getRadioValue('checkb') && getRadio('checkb').lstatus == 0)
	{	
		$.messager.prompt('总部核对', '请核对说明', '', function(msg){
                if (msg)
                {
                    $ajax(gurl + 'checks&id=' + getRadioValue('checkb') + '&reason=' + ajaxPararmter(msg) + '&type=' + getRadio('checkb').ltype , 
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
<p:message/>
<table id="mainTable" style="display: none"></table>
<p:query/>
</body>