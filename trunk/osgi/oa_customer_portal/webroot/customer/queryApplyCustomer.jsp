<%@page contentType="text/html; charset=UTF-8"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<p:link title="客户管理" link="true" guid="true" cal="false"/>
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/pop.js"></script>
<script src="../js/json.js"></script>
<script type="text/javascript">

var allDef = window.top.topFrame.allDef;
var guidMap;
var thisObj;
var updateCode = window.top.topFrame.containAuth('0214') ? '1' : '0';
function load()
{
	 guidMap = {
		 title: '申请客户列表',
		 url: '../customer/customer.do?method=queryApplyCustomer',
		 colModel : [
		     {display: '选择', name : 'check', content : '<input type=radio name=checkb value={id} lname={name}>', width : 40, sortable : false, align: 'center'},
		     {display: '客户', name : 'name', width : '20%', sortable : false, align: 'left'},
		     {display: '操作', name : 'opr', width : '10%', sortable : false, align: 'left', cc: 'customerOpr'},
		     {display: '申请人', name : 'stafferName', width : '10%', sortable : false, align: 'left'},
		     {display: '类型', name : 'selltype', width : '10%', sortable : false, align: 'left', cc: 101},
		     {display: '级别', name : 'qqtype', width : '10%', sortable : false, align: 'left', cc: 104},
		     {display: '分类', name : 'rtype', width : '10%', sortable : false, align: 'left', cc: 105},
		     {display: '状态', name : 'status', width : '11%', sortable : false, align: 'left', cc: 'customerStatus'},
		     {display: '时间', name : 'loginTime', width : 'auto', sortable : true, align: 'left'}
		     ],
		 extAtt: {
		     name : {begin : '<a href=../customer/customer.do?method=findApplyCustomer&id={id}&updateCode='+ updateCode + '>', end : '</a>'}
		 },
		 buttons : [
		     {id: 'add', caption: '申请增加', bclass: 'add', auth: '0202', onpress : addBean},
		     {id: 'del',  caption: '删除申请', bclass: 'delete', auth: '0202', onpress : delBean},
		     {id: 'pass', caption: '通过',bclass: 'pass', auth: '0203', onpress : doPass},
		     {id: 'reject', caption: '驳回',bclass: 'reject', auth: '0203', onpress : doReject}
		     ],
		 usepager: true,
		 useRp: true,
		 auth: window.top.topFrame.gAuth,
		 queryMode: 0,
		 cache: 0,
		 queryCondition: null,
		 showTableToggleBtn: true,
		 height: 'page',
		 def: allDef,
		 callBack: loadForm
	 };
	 
	 $("#mainTable").flexigrid(guidMap, thisObj);
 }
 
function doPass()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (window.confirm('确定通过--' + getRadio('checkb').lname))
        {
            $ajax('../customer/customer.do?method=processApply&operation=0&id=' + getRadioValue('checkb'), callBackFun);
        }
    }
}

function callBackFun(data)
{
    reloadTip(data.msg, data.ret == 0);
    
    if (data.ret == 0)
    commonQuery();
}

function doReject()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (window.confirm('确定驳回--' + getRadio('checkb').lname))
        {
            $ajax('../customer/customer.do?method=processApply&operation=1&id=' + getRadioValue('checkb'), callBackFun);
        }
    }
}

function doAssignCode()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        $l('../customer/customer.do?method=findApplyCustomer&updateCode=1&id=' + getRadioValue('checkb'));
    }
}

function delBean()
{
    if (getRadio('checkb') && getRadioValue('checkb'))
    {
        if (window.confirm('确定删除申请--' + getRadio('checkb').lname))
        {
            $ajax('../customer/customer.do?method=delApplyCustomer&id=' + getRadioValue('checkb'), callBackFun);
        }
    }
}

function addBean()
{
    $l('../customer/customer.do?method=preForAddApplyCustomer');
}


function commonQuery(par)
{
    gobal_guid.p.queryCondition = par;
    
    gobal_guid.grid.populate(true);
}
</script>
</head>
<body onload="load()" class="body_class">
<form>
</form>
<p:cache/>
<p:message></p:message>
<table id="mainTable" style="display: none"></table>
</body>