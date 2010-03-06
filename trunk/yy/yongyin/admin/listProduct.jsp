<%@ page contentType="text/html;charset=GBK" language="java"%>
<%@include file="./common.jsp"%>

<html>
<head>
<p:link title="产品查询" />
<script src="../js/prototype.js"></script>
<script src="../js/common.js"></script>
<script language="javascript">
function addywy()
{
	document.location.href = '../admin/product.do?method=preForAddProduct';
}

function addstaffer()
{
	document.location.href = '../admin/addCommon.jsp?type=4';
}

function addDepartment()
{
	document.location.href = '../admin/addCommon.jsp?type=3';
}

function addCustmer()
{
	document.location.href = '../admin/common.do?method=preForaddCustmer';
}

function modifyProduct()
{
	if (getRadio("productId"))
	document.location.href = './product.do?method=findProduct&productId=' + getRadioValue("productId");
}

function delywy()
{
	if (getRadio("productId"))
	if (window.confirm("确定删除产品[" + getRadio("productId").productName + "]?"))
	document.location.href = './product.do?method=delProduct&productId=' + getRadioValue("productId");
}


function query()
{
	adminForm.submit();
}

function sysnProduct()
{
	if (window.confirm("您确认需要同步产品到各区域?"))
	document.location.href = './product.do?method=sysnProduct';
}

function exports()
{
	if (window.confirm("您确认需要导出全部的日常数据?"))
	document.location.href = '../admin/export.do';
}
</script>
</head>
<body class="body_class">
<form action="./product.do" name="adminForm"><input type="hidden"
	value="queryProduct" name="method"> <input type="hidden"
	value="1" name="firstLoad">
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
<table width="100%" border="0" cellpadding="0" cellspacing="0"
	align="center">
	<tr>
		<td align='center' colspan='2'>
		<table width="98%" border="0" cellpadding="0" cellspacing="0"
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
						<td colspan="4" align="right"><input type="submit"
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
								<td class="caption"><strong>浏览产品:</strong></td>
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
		<table width="98%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr align="center" class="content0">
						<td align="center" width="5%" class="td_class">选择</td>
						<td align="center" onclick="tableSort(this)" class="td_class">产品名称</td>
						<td align="center" onclick="tableSort(this)" class="td_class">产品编码</td>
						<td align="center" onclick="tableSort(this)" class="td_class">盘点分类</td>
						<td align="center" onclick="tableSort(this)" class="td_class">状态</td>
						<td align="center" onclick="tableSort(this)" class="td_class">归属</td>
						<td align="center" onclick="tableSort(this)" class="td_class">图片</td>
						<td align="center" onclick="tableSort(this)" class="td_class">索引</td>
					</tr>

					<c:forEach items="${productList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td align="center"><input type="radio" name="productId"
								productName="${item.name}" value="${item.id}" ${vs.index== 0 ? "checked" : ""}/></td>
							<td align="center" onclick="hrefAndSelect(this)">${item.name}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.code}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.type == 0 ? "每天盘点" : "其他"}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:get('productStatus', item.status)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.temp == 0 ? "自有" : "非自有"}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.picPath}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.id}</td>
						</tr>
					</c:forEach>
				</table>

				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					align="center" class="table1">
					<tr>
						<td colspan="8" align="right"><c:if
							test="${!ProductList.firstPage}">
							<a href="./product.do?method=queryProduct&page=previous"><font
								color="blue"><img src="../images/preview.gif" border="0"></font></a>
						</c:if> <c:if test="${!ProductList.lastPage}">
							<a href="./product.do?method=queryProduct&page=next"><font
								color="blue"><img src="../images/next.gif" border="0"></font></a>
						</c:if></td>
					</tr>
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
		<td width="100%">
		<div align="right"><input type="button" class="button_class"
			value="&nbsp;增加产品&nbsp;" onClick="addywy()">&nbsp;&nbsp;
			<input type="button" class="button_class"
			value="&nbsp;修改产品&nbsp;" onClick="modifyProduct()">&nbsp;&nbsp;
			
			<c:if test="${user.role == 'TOP'}">
			<input type="button" class="button_class"
			value="&nbsp;同步产品&nbsp;" onClick="sysnProduct()">&nbsp;&nbsp;
			</c:if>
			</div>
		</td>
		<td width="0%"></td>
	</tr>

	<tr height="10">
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT> <c:remove var="MESSAGE_INFO"
			scope="session" /><c:remove var="errorInfo" scope="session" /></td>
	</tr>
</table>

</form>
</body>
</html>
