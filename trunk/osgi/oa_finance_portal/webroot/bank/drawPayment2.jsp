<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="认领回款" />
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">

function addBean()
{
	submit('确定关联销售或者预收?', null, check);
}

function check()
{
    //计算是否回款溢出
    var total = ${my:formatNum(bean.money - hasUsed)};
    
    var pu = 0.0;
    
    for (var i = 1; i <= 5; i++)
    {
	    if (!isNone($$('outMoney' + i)))
	    {
	        pu += parseFloat($$('outMoney' + i));
	    }
    }
    
    if (total == pu)
    {
        return true;
    }
    
    if (total > pu)
    {
        return window.confirm('回款没有全部使用,确认是否先领取回款?');
    }
    
    alert('回款金额使用溢出,请核对');
    
    return false;
}

var g_index = 1;

function opens(index)
{
    g_index = index;
    
    window.common.modal('../sail/out.do?method=rptQueryOut&selectMode=0&mode=0&load=1&stafferId=${user.stafferId}&customerId=' + $$('customerId'));
}

function getOut(oos)
{
    $O('outId' + g_index).value = oos[0].value;
    $O('outMoney' + g_index).value = oos[0].ptotal;
}

function clears()
{
    $O('outId' + g_index).value = '客户预收';
    $O('outMoney' + g_index).value = '';
}

</script>

</head>
<body class="body_class">
<form name="formEntry" action="../finance/bank.do" method="post">
<input type="hidden" name="method" value="drawPayment2"> 
<input type="hidden" name="customerId" value="${bean.customerId}"> 
<input type="hidden" name="id" value="${bean.id}"> 
<p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">回款管理</span> &gt;&gt; 销售关联</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>回款基本信息：(如果金额过多回款可以分为多个预收的收款单)</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">

		<p:class value="com.china.center.oa.finance.bean.PaymentBean" />

		<p:table cells="1">

			<p:cell title="帐户">
               ${bean.bankName}
            </p:cell>
            
            <p:cell title="类型">
               ${my:get('paymentType', bean.type)}
            </p:cell>
            
            <p:cell title="总金额">
               ${my:formatNum(bean.money)}
            </p:cell>
            
            <p:cell title="剩余金额(可使用)">
               ${my:formatNum(bean.money - hasUsed)}
            </p:cell>
            
            <p:cell title="回款来源">
               ${bean.fromer}
            </p:cell>
            
            <p:cell title="回款日期">
               ${bean.receiveTime}
            </p:cell>
			
            <c:forEach begin="1" end="5" var="item">
	            <p:cell title="关联单据${item}" end="true">
	                <input type="text" size="40" value="客户预收" readonly="readonly" name="outId${item}"> 
	                &nbsp;回款金额:<input type="text" name="outMoney${item}" oncheck="isFloat2">
	                <input type="button" value="&nbsp;选 择&nbsp;" name="qout" id="qout"
	                    class="button_class" onclick="opens(${item})">&nbsp;
	                <input type="button" value="&nbsp;清 空&nbsp;" name="qout" id="qout"
	                    class="button_class" onclick="clears(${item})">&nbsp;&nbsp;
	            </p:cell>
            </c:forEach>
            
             <p:cell title="备注">
               <textarea rows=3 cols=55  oncheck="maxLength(200);" name="description"></textarea>
            </p:cell>

		</p:table>

	</p:subBody>
	
	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			id="ok_b" style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="addBean()"></div>
	</p:button>

	<p:message2 />
</p:body></form>
</body>
</html>

