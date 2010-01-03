<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="会员消费明细" />
<script language="JavaScript" src="../js/key.js"></script>

<script language="javascript">

</script>

</head>
<body class="body_class">
<form><p:navigation height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">会员消费管理</span> &gt;&gt; 会员消费明细</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="80%">

	<p:title>
		<td class="caption"><strong>会员消费信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="85%">
		<p:table cells="1">
			<p:cell title="会员">${bean.cardNo}</p:cell>
			<p:cell title="客户">${bean.memberName}</p:cell>
			<p:cell title="产品">${bean.productName}</p:cell>
			<p:cell title="产品数量">${bean.amount}</p:cell>
			<p:cell title="产品单价">${bean.price}</p:cell>
			<p:cell title="原始金额">${bean.precost}</p:cell>
			<p:cell title="折扣">${bean.rebate}</p:cell>
			<p:cell title="实际消费金额">${bean.cost}</p:cell>
			<p:cell title="备注">${bean.description}</p:cell>
		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="87%" rightWidth="13%">
		<div align="right"><input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>
</p:body></form>
</body>
</html>

