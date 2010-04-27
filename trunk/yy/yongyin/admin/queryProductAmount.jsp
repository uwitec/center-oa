<%@ page contentType="text/html;charset=GBK" language="java" errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>

<html>

<head>
<p:link title="产品查询" />
<script src="../js/CommonScriptMethod.js"></script>
<script src="../js/prototype.js"></script>
<script src="../js/common.js"></script>
<script language="javascript">
function query()
{
	adminForm.submit();
}

function load()
{
	loadForm();
}

function exprots()
{
	if ($$('locationId') == '')
	{
		alert('请选择区域');
		return false;
	}
	
	document.location.href = '../admin/product.do?method=exportLocationProduct&eLocationId=' + $$('locationId');
}

function exprotProduct()
{
	document.location.href = '../admin/product.do?method=exportProduct';
}
</script>
</head>
<body class="body_class" onload="load()">
<form action="./product.do" name="adminForm"><input type="hidden"
	value="queryProductThree" name="method"> <input type="hidden"
	value="2" name="firstLoad">
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
				<td width="550" class="navigation">日常管理 &gt;&gt; 浏览产品</td>
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
<table width="98%" border="0" cellpadding="0" cellspacing="0"
	align="center">
	<tr>
		<td align='center' colspan='2'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content1">
						<td width="15%" align="center">产品名称</td>
						<td align="center"><input type="text" name="name"
							value="${name}"></td>
						<td width="15%" align="center">产品编码</td>
						<td align="center"><input type="text" name="code"
							value="${code}"></td>
					</tr>

					<tr class="content2">
						<td width="15%" align="center">区域</td>
						<td align="center"><select name="locationId" ${readonly}
							class="select_class" values="${locationId}">
								<option value="">--</option>
							<c:forEach items='${locationList}' var="item">
								<option value="${item.id}">${item.locationName}</option>
							</c:forEach>
						</select></td>
						<td colspan="2" align="right"><input type="submit"
							onclick="query()" class="button_class"
							value="&nbsp;&nbsp;查 询&nbsp;&nbsp;"></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>

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
								<td class="caption"><strong>产品库存列表: </strong></td>
								<td align="right"><b>合计数量：${total}</b></td>
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
		<table width="100%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr align="center" class="content0">
						<td align="center" onclick="tableSort(this)" class="td_class"><B>产品名称</B></td>
						<td align="center" onclick="tableSort(this)" class="td_class"><B>产品编码</B></td>
						<td align="center" onclick="tableSort(this)" class="td_class"><B>产品区域</B></td>
						<td align="center" onclick="tableSort(this, true)" class="td_class"><B>产品数量</B></td>
						<td align="center" onclick="tableSort(this, true)" class="td_class"><B>产品总数量</B></td>
					</tr>

					<c:forEach items="${productList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td align="center">${item.productName}</td>
							<td align="center">${item.productCode}</td>
							<td align="center">${item.locationName}</td>
							<td align="center">${item.num}</td>
							<td align="center">${item.total}</td>
						</tr>
					</c:forEach>
				</table>

				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					align="center" class="table1">
					<tr>
						<td colspan="8" align="right"><c:if
							test="${pre}">
							<a href="./product.do?method=queryProductThree&page=previous&locationId=${locationId}"><font
								color="blue"><img src="../images/preview.gif" border="0"></font></a>
						</c:if> <c:if test="${next}">
							<a href="./product.do?method=queryProductThree&page=next&locationId=${locationId}"><font
								color="blue"><img src="../images/next.gif" border="0"></font></a>
						</c:if></td>
					</tr>
				</table>
				</td>
			</tr>
		</table>
		</td>
	</tr>

	<tr height="10">
		<td height="10" colspan='2'></td>
	</tr>
	
	<c:if test='${user.role == "ADMIN" || user.role == "MANAGER"}'>
	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			style="cursor: pointer" value="&nbsp;&nbsp;导出库存&nbsp;&nbsp;"
			onclick="exprots()">&nbsp;&nbsp;<input type="button" class="button_class"
			style="cursor: pointer" value="&nbsp;&nbsp;导出产品&nbsp;&nbsp;"
			onclick="exprotProduct()"></div>
	</p:button>
	</c:if>

</table>

</form>
</body>
</html>
