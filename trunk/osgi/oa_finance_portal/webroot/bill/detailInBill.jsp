<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="收款单详细" guid="true" dialog="true"/>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="javascript">
function checkBean()
{
    $.messager.prompt('总部核对', '请核对说明', '', function(msg){
        if (msg)
        {
            $l('../finance/finance.do?method=checks2&type=3&id=${bean.id}&reason=' + ajaxPararmter(msg) + '&type=${ltype}');
        }
    }, 2);
}

function pagePrint()
{
    var old = $O('b_div').style.display;
    
    $O('b_div').style.display = 'none';
    window.print();

    $O('b_div').style.display = old;
}
</script>

</head>
<body class="body_class">
<form name="formEntry" action="../finance/invoiceins.do" method="post">
<input type="hidden" name="method" value=""> <p:navigation
	height="22">
	<td width="550" class="navigation"><span>收款单详细</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>收款单信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">

		<p:class value="com.china.center.oa.finance.bean.InBillBean" opr="2"/>

		<p:table cells="2">
		
		    <p:cell title="标识">
               ${bean.id}
            </p:cell>

			<p:pro field="type" innerString="style='WIDTH: 340px;'">
			   <p:option type="inbillType" />
			</p:pro>

			<p:pro field="status" innerString="style='WIDTH: 340px;'">
               <p:option type="inbillStatus" />
            </p:pro>
            
            <p:pro field="lock" innerString="style='WIDTH: 340px;'">
               <p:option type="billLock" />
            </p:pro>
            
            <p:cell title="帐号">
               ${bean.bankName}
            </p:cell>
			
			<p:cell title="客户">
               ${bean.customerName}
            </p:cell>
			
			<p:cell title="销售/委托">
			    <a href="../sail/out.do?method=findOut&fow=99&outId=${bean.outId}">${bean.outId}</a>/${bean.outBalanceId}
            </p:cell>
            
            <p:cell title="关联回款">
                ${bean.paymentId}
            </p:cell>
            
            <p:cell title="会计">
               ${bean.stafferName}
            </p:cell>
            
            <p:cell title="职员">
               ${bean.ownerName}
            </p:cell>
            
            <p:cell title="总金额">
               ${my:formatNum(bean.moneys)}
            </p:cell>
            
            <p:cell title="原始金额">
               ${my:formatNum(bean.srcMoneys)}
            </p:cell>
            
            <p:cell title="时间" end="true">
               ${bean.logTime}
            </p:cell>
            
            <p:cell title="回款备注" end="true">
               ${bean.paymentDes}
            </p:cell>

			<p:cell title="备注" end="true">
               ${bean.description}
            </p:cell>
			
			<p:cell title="核对状态" end="true">
               ${my:get('pubCheckStatus', bean.checkStatus)}
            </p:cell>
            
            <p:cell title="核对信息" end="true">
               ${bean.checks}
            </p:cell>

		</p:table>

	</p:subBody>

	
	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right" id="b_div">
		<c:if test="${check == 1}">
		<input
            type="button" name="ba" class="button_class"
            onclick="checkBean()"
            value="&nbsp;&nbsp;总部核对&nbsp;&nbsp;">&nbsp;&nbsp;
		</c:if>
		<input type="button" name="pr"
            class="button_class" onclick="pagePrint()"
            value="&nbsp;&nbsp;打 印&nbsp;&nbsp;">&nbsp;&nbsp;
		<input
            type="button" name="ba" class="button_class"
            onclick="javascript:history.go(-1)"
            value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<p:message2 />
</p:body></form>
</body>
</html>

