<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="发票" />
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="javascript">


</script>

</head>
<body class="body_class">
<form name="formEntry" action="../finance/invoiceins.do" method="post">
<input type="hidden" name="method" value=""> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">发票管理</span> &gt;&gt; 发票明细</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>发票基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">

		<p:class value="com.china.center.oa.finance.bean.InvoiceinsBean" opr="2"/>

		<p:table cells="1">

			<p:pro field="invoiceDate"/>
			
			<p:pro field="unit" innerString="size=60" />

			<p:pro field="invoiceId" innerString="style='WIDTH: 340px;'">
			    <c:forEach items="${invoiceList}" var="item">
			    <option value="${item.id}">${item.fullName}</option>
			    </c:forEach>
			</p:pro>

			<p:pro field="dutyId" innerString="onchange=loadShow()">
				<p:option type="dutyList" />
			</p:pro>
			
			 <p:cell title="开票客户">
               ${bean.customerName}
            </p:cell>
			
			<p:cell title="关联单据">
			    <c:forEach items="${bean.vsList}" var="item">
			    ${item.outId};
			    </c:forEach>
            </p:cell>
            
            <p:cell title="开票人">
               ${bean.stafferName}
            </p:cell>
            
            <p:cell title="总金额">
               ${my:formatNum(bean.moneys)}
            </p:cell>
            
            <p:cell title="开票时间">
               ${bean.logTime}
            </p:cell>

			<p:pro field="description" cell="0" innerString="rows=3 cols=55" />

		</p:table>

	</p:subBody>

	<p:tr />


	<p:subBody width="100%">

		<p:table cells="1">

			<tr align="center" class="content0">
				<td width="40%" align="center">品名</td>
				<td width="30%" align="center">数量</td>
				<td width="30%" align="center">单价</td>
			</tr>
			
			<c:forEach items="${bean.itemList}" var="item">
			<tr align="center" class="content0">
                <td align="center">${item.showName}</td>
                <td align="center">${item.amount}</td>
                <td align="center">${my:formatNum(item.price)}</td>
            </tr>
            </c:forEach>

		</p:table>

	</p:subBody>
	
	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input
            type="button" name="ba" class="button_class"
            onclick="javascript:history.go(-1)"
            value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<p:message2 />
</p:body></form>
</body>
</html>

