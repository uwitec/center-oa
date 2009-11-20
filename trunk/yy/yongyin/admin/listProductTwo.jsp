<%@ page contentType="text/html;charset=GBK" language="java"%>
<%@include file="./common.jsp" %>

<html>

<head>
<p:link title="产品查询" />
<script src="../js/prototype.js"></script>
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script language="javascript">
function addywy()
{
	document.location.href = '../admin/addProduct.jsp';
}

function delywy()
{
	if (window.confirm("确定删除产品[" + getRadio("productId").productName + "]?"))
	document.location.href = './product.do?method=delProduct&productId=' + getRadioValue("productId");
}


function query()
{
	adminForm.submit();
}

function press()
{
    window.common.enter(query);
} 

function exports()
{
	if (window.confirm("您确认需要导出产品出入?"))
	document.location.href = '../admin/product.do?method=export';
}

function exportSnapsho()
{
    if (window.confirm("您确认需要导出最近库存快照?"))
    document.location.href = '../admin/product.do?method=exportSnapsho';
}

</script>
<title>管理员列表</title>

</head>
<body class="body_class">
<form action="./product.do" name="adminForm"><input type="hidden"
	value="queryProductTwo" name="method"> <input type="hidden"
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
						<td align="center"><input type="text" name="name" onkeypress="press()"
							value="${name}"></td>
						<td width="15%" align="center">产品编码</td>
						<td align="center"><input type="text" name="code" onkeypress="press()"
							value="${code}"></td>
					</tr>

					<tr class="content2">
						<td colspan="4" align="right"><input type="button"
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
				<table width="100%" border="0" cellspacing="2">
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
						<td align="center" width="8%" class="td_class"><B>选择</B></td>
						<td align="center" onclick="tableSort(this)" class="td_class"><B>产品名称</B></td>
						<td align="center" onclick="tableSort(this)" class="td_class"><B>产品编码</B></td>
						<td align="center" onclick="tableSort(this, true)" class="td_class"><B>产品入库</B></td>
						<td align="center" onclick="tableSort(this, true)" class="td_class"><B>产品出库</B></td>
						<td align="center" onclick="tableSort(this)" class="td_class"><B>入/出(当日)</B></td>
					</tr>

					<c:forEach items="${productList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td align="center"><input type="radio" name="productId"
								productName="${item.name}" value="${item.id}" ${vs.index== 0 ? "checked" : ""}/></td>
							<td align="center">${item.name}</td>
							<td align="center">${item.code}</td>
							<td align="center">${proMap[item.id].add}</td>
							<td align="center">${proMap[item.id].del}</td>
							<td align="center">${proMap[item.id].add}/${proMap[item.id].del}</td>
						</tr>
					</c:forEach>
				</table>

				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					align="center" class="table1">
					<tr>
						<td colspan="8" align="right"><c:if
							test="${!ProductList.firstPage}">
							<a href="./product.do?method=queryProductTwo&page=previous"><font
								color="blue"><img src="../images/preview.gif" border="0"></font></a>
						</c:if> <c:if test="${!ProductList.lastPage}">
							<a href="./product.do?method=queryProductTwo&page=next"><font
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
	
	<tr>
		<td width="99%">
		<div align="right">
		
		<c:if test='${user.role == "MANAGER" && user.locationID == "0"}'>
		<input type="button"
            class="button_class" value="&nbsp;导出最近库存快照&nbsp;"
            onclick="exportSnapsho()"/>
		&nbsp;&nbsp;
		</c:if>
		<input type="button"
			class="button_class" value="&nbsp;导出产品当日出入&nbsp;"
			onclick="exports()">&nbsp;</div>
		</td>
		<td width="1%"></td>
	</tr>

</table>

</form>
</body>
</html>
