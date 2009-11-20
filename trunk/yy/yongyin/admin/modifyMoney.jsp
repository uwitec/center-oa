<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="修改余额" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>

<script language="javascript">
function addApplys()
{
	submit('确定修改余额?');
}

function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../admin/common.do"><input
	type="hidden" name="method" value="modifyMoney"> <input
	type="hidden" name="id" value="${satatBean.id}"> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">银行管理</span> &gt;&gt; 修改余额</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="80%">

	<p:title>
		<td class="caption"><strong>银行余额信息：${lastId}</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="75%">
		<p:class value="com.china.centet.yongyin.bean.StatBean" />

		<p:table cells="1">

			<p:cell title="银行名称">${satatBean.bank}</p:cell>

			<p:cell title="[${llastId}]余额">${satatBean.lastMoney}</p:cell>

			<p:cell title="[${llastId}]支出">${satatBean.outMoney}</p:cell>

			<p:cell title="[${llastId}]收入">${satatBean.inMoney}
			<input type="text" style="visibility: hidden" disabled="disabled">
			</p:cell>

			<p:pro field="tatolMoney" value="${satatBean.tatolMoney}" />

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="87%" rightWidth="13%">
		<div align="right"><input type="button" class="button_class"
			value="&nbsp;&nbsp;确 定&nbsp;&nbsp;" onclick="addApplys()">&nbsp;&nbsp;
		<input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>
</p:body></form>
</body>
</html>

