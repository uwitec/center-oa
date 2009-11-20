<%@ page contentType="text/html;charset=GBK" language="java"%>
<%@include file="./common.jsp"%>

<html>
<head>
<p:link title="供应商管理" />
<script src="../js/prototype.js"></script>
<script src="../js/common.js"></script>
<script src="../js/public.js"></script>
<script src="../js/autosuggest.js"></script>
<script src="../js/cnchina.js"></script>
<script language="JavaScript" src="../js/buffalo.js"></script>
<script language="javascript">

function addCustmer()
{
	document.location.href = '../admin/common.do?method=preForaddCustmer';
}

function addCustmer()
{
	document.location.href = '../admin/common.do?method=preForaddCustmer';
}

function updateCustmer()
{
	if ($$('coustmers') == '')
	{
		alert('请选择供应商');
		return false;
	}

	document.location.href = '../admin/common.do?method=preForUpdateCustmer&id=' + $$('coustmers');
}

function query()
{
	adminForm.submit();
}

var staffer = [];
<c:forEach items="${staffer}" var="item">
staffer.push('${item}');
</c:forEach>

var phint;
function load()
{
	phint = new hint('stafferName', [], HINT_MODE.SERVER, true, callbacks);

	$f('name');

}

var END_POINT="${pageContext.request.contextPath}/bfapp";

var buffalo = new Buffalo(END_POINT);

function callbacks()
{
	
}

function bingType()
{
	document.location.href = '../admin/common.do?method=preForBing&customerId=' + $$('coustmers');
}
</script>
<title>供应商列表</title>
</head>
<body class="body_class" onload="load()">
<form action="./common.do" name="adminForm"><input type="hidden"
	value="queryProvider" name="method"> <input type="hidden"
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
				<td width="550" class="navigation">日常管理 &gt;&gt; 浏览供应商</td>
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
		<td align='center' colspan='2'>
		<table width="85%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content1">
						<td width="15%" align="center">供应商名称</td>
						<td align="center"><input type="text" name="name"
							value="${name}"></td>
						<td width="15%" align="center">供应商编码</td>
						<td align="center"><input type="text" name="code"
							value="${code}"></td>
					</tr>

					<tr class="content2">
						<td width="15%" align="center">所属职员</td>
						<td align="center"><input type="text" name="stafferName" id="stafferName" readonly="readonly"
							value="${stafferName}"></td>
						<td colspan="2" align="right"><input type="button"
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
								<td class="caption"><strong>浏览供应商:【总数:${TATOL}】</strong></td>
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
		<table width="85%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr align="center" class="content0">
						<td align="center" width="8%" class="td_class">选择</td>
						<td align="center" onclick="tableSort(this)" class="td_class">供应商名称</td>
						<td align="center" onclick="tableSort(this)" class="td_class">供应商编码</td>
					</tr>

					<c:forEach items="${customerList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td align="center"><input type="radio" name="coustmers"
								 value="${item.id}" ${vs.index== 0 ? "checked" : ""}/></td>
							<td align="center">${item.name}</td>
							<td align="center">${item.code}</td>
						</tr>
					</c:forEach>
				</table>

				<p:turning url="../admin/common.do?method=queryProvider" />

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
		<td width="93%">
		<div align="right">
			<input type="button" class="button_class"
			value="&nbsp;绑定分类&nbsp;" onClick="bingType()">&nbsp;&nbsp;
			</div>
		</td>
		<td width="7%"></td>
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
