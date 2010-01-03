<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>

<p:link title="增加收付款" />
<script language="JavaScript" src="../js/CommonScriptMethod.js"></script>
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="javascript">
function marks()
{
	if (formCheck())
	{
		if (window.confirm('确定核对?'))
		{
			addBill.submit();
		}
	}
}

function load()
{
	var ff = '${bill.billType}';
	var type = '${bill.type}';
	
	var text = ({0:({0:'单据收款', 1:'预收款', 2:'区域内转账', 3: '跨区域收账'}), 1:({0:'普通付款', 1:'报销', 2:'转账', 4: '跨区域付账'})})[type][ff];
	
	if (text)
	{
		$('spec').innerHTML = text;
	}
	else
	{
		$('spec').innerHTML = type;
	}
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="addBill" action="../admin/bill.do"><input
	type="hidden" name="method" value="mark"> <input type="hidden"
	name="id" value="${bill.id}">
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
				<td width="550" class="navigation">款单明细</td>
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
<table width="85%" border="0" cellpadding="0" cellspacing="0"
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
								<td width="15">&nbsp;</td>
								<td width="6"><img src="../images/dot_r.gif" width="6"
									height="6"></td>
								<td class="caption"><strong>单据信息:</strong></td>
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
		<td colspan='2' align='center'>
		<table width="80%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content2">
						<td width="15%">单据:</td>
						<td width="35%">${bill.id}</td>
						<td width="15%">收款帐户:</td>
						<td width="35%">${bill.bank}</td>
					</tr>

					<tr class="content1">
						<td width="15%">单据类型:</td>
						<td width="35%">${bill.type == 0 ? "收款单" : "付款单"}</td>

						<td width="15%">类型:</td>
						<td width="35%" id='spec'></td>
					</tr>

					<tr class="content2">
						<td width="15%">出库单号:</td>
						<td width="35%">${bill.outId}</td>

						<td width="15%">目的银行</td>
						<td width="35%">${bill.destBank}</td>
					</tr>

					<tr class="content1">
						<td width="15%" id="inout">收款金额:</td>
						<td width="35%">${my:formatNum(bill.moneys)}</td>

						<td width="15%" id="cust">客户:</td>
						<td width="35%">${bill.customerName}</td>
					</tr>

					<tr class="content2">
						<td width="15%" id="inout">经办人:</td>
						<td width="35%">${bill.stafferName}</td>
						<td width="15%" id="mm">收款日期:</td>
						<td width="35%">${bill.temp}</td>
					</tr>

					<tr class="content1">

						<td width="15%" id="cust">备注:</td>
						<td width="35%" colspan="3">${bill.description}</td>
					</tr>

					<tr class="content2">

						<td width="15%" id="cust">在途方式:</td>
						<%
						    String[] sss1 = new String[] {"", "<font color=red>在途</font>", "在途结束"};
						    request.setAttribute("sss1", sss1);
						%>
						<td width="35%">${my:getValue(bill.inway, sss1)}</td>
						<td width="15%" id="cust">相关单据:</td>
						<td width="35%"><a
							href="../admin/bill.do?method=findBill&id=${bill.refBillId}">${bill.refBillId}</a></td>
					</tr>


					<c:if test="${fff}">
						<tr class="content2">
							<td width="15%" id="cust">核对:</td>
							<td width="35%" colspan="3"><textarea rows="3" cols="55"
								oncheck="notNone;maxLength(254)" name="mark">${bill.mark}</textarea></td>
						</tr>
					</c:if>

					<c:if test="${!fff}">
						<tr class="content2">
							<td width="15%" id="cust">核对:</td>
							<td width="35%" colspan="3">${bill.mark}</td>
						</tr>
					</c:if>
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
		<td width="90%">
		<div align="right"><c:if test="${fff}">
			<input type="button" class="button_class" onclick="marks()"
				value="&nbsp;&nbsp;核 对&nbsp;&nbsp;">
		</c:if> <input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
		</td>
		<td width="10%"></td>
	</tr>

</table>


</form>
</body>
</html>

