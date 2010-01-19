<%@ page contentType="text/html;charset=GBK" language="java"%>
<%@include file="./common.jsp"%>

<html>

<head>
<p:link title="选择收付款" />
<script src="../js/common.js"></script>
<script language="javascript">
function chec()
{
	var opener = window.dialogArguments[0]
	
	if (getRadioValue('bills') == '')
	{
		alert('请选择收付款单');
		return;
	}
	
	opener.getBill(getRadio('bills'));
    opener = null;
    window.close();
}

function load()
{
	loadForm();
}
</script>
</head>
<body class="body_class" onload="load()">
<form action="./bill.do" name="adminForm" onsubmit="return comp()"><input
	type="hidden" value="queryBill" name="method"> <input
	type="hidden" value="2" name="firstLoad">
<table width="100%" border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td height="22" valign="bottom">
		<table width="100%" height="22" border="0" cellpadding="0"
			cellspacing="0">
			<tr valign="middle">
				<td width="8"></td>
				<td width="30">
				<div align="center"><img src="../images/dot_a.gif" width="9"
					height="9"></div>
				</td>
				<td width="550" class="navigation">收付款管理 &gt;&gt; 查询收付款单</td>
				<td width="85"></td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td height="6" valign="top">
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<!--DWLayoutTable-->
			<tr>
				<td width="8" height="6"
					background="../images/index_sp_welcome_center_10.gif"><img
					src="../images/index_sp_welcome_center_07.gif" width="8" height="6"></td>
				<td width="190"
					background="../images/index_sp_welcome_center_08.gif"></td>
				<td width="486"
					background="../images/index_sp_welcome_center_10.gif"></td>
				<td align="right"
					background="../images/index_sp_welcome_center_10.gif">
				<div align="right"><img
					src="../images/index_sp_welcome_center_12.gif" width="23"
					height="6"></div>
				</td>
			</tr>
		</table>
		</td>
	</tr>
</table>

<br>
<table width="90%" border="0" cellpadding="0" cellspacing="0"
	align="center">

	<tr>
		<td valign="top" colspan='2'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<!--DWLayoutTable-->
			<tr>
				<td width="784" height="6"></td>
			</tr>
			<tr>
				<td align="center" valign="top">
				<div align="left">
				<table width="90%" border="0" cellspacing="2">
					<tr>
						<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="10">
							<tr>
								<td width="35">&nbsp;</td>
								<td width="6"><img src="../images/dot_r.gif" width="6"
									height="6"></td>
								<td class="caption"><strong>收付款单:</strong><font color=blue>[当前查询数量:${my:length(billList)}]</font></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>


	<tr>
		<td background="../images/dot_line.gif" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td align='center' colspan='2'>
		<table width="90%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr align="center" class="content0">
						<td align="center" onclick="tableSort(this)" class="td_class">选择</td>
						<td align="center" onclick="tableSort(this)" class="td_class">编号</td>
						<td align="center" onclick="tableSort(this)" class="td_class">客户</td>
						<td align="center" onclick="tableSort(this)" class="td_class">帐户</td>
						<td align="center" onclick="tableSort(this)" class="td_class">库单编号</td>
						<td align="center" onclick="tableSort(this)" class="td_class">单据类型</td>
						<td align="center" onclick="tableSort(this)" class="td_class">金额</td>
						<td align="center" onclick="tableSort(this)" class="td_class">经办人</td>
						<td align="center" onclick="tableSort(this)" class="td_class">在途</td>
						<td align="center" onclick="tableSort(this)" class="td_class">目的银行</td>
					</tr>

					<c:forEach items="${billList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td align="center"><input type="radio" name="bills" money="${my:formatNum(item.money)}" 
							destBank="${item.receipt}"
								value="${item.id}" ${vs.index== 0 ? "checked" : ""}/></td>
							<td align="center"><a onclick="hrefAndSelect(this)"
								href="../admin/bill.do?method=findBill&id=${item.id}"><u>${item.id}</u></a></td>
							<td align="center" onclick="hrefAndSelect(this)">${item.customerName}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.bank}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.outId}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.type == 0 ? "收款单" : "付款单"}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:formatNum(item.money)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.stafferName}</td>
							<%
								String[] sss1 = new String[]{"" , "<font color=red>在途</font>", "在途结束"};
								request.setAttribute("sss1", sss1);
							%>
							<td align="center" onclick="hrefAndSelect(this)">${my:getValue(item.inway, sss1)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.destBank}</td>
						</tr>
					</c:forEach>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>


	<tr>
		<td background="../images/dot_line.gif" colspan='2'></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td width="92%">
		<div align="right"><input type="button" class="button_class"
			value="&nbsp;&nbsp;确 认&nbsp;&nbsp;" onClick="chec()">&nbsp;&nbsp;</div>
		</td>
		<td width="8%"></td>
	</tr>

	<tr height="10">
		<td height="10" colspan='2'></td>
	</tr>
</table>

</form>
</body>
</html>
