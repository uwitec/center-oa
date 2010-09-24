<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<base target="_self">
<p:link title="外网询价查询" />
<script src="../js/prototype.js"></script>
<script src="../js/public.js"></script>
<script src="../js/common.js"></script>
<script language="javascript">

function sure()
{
	var opener = window.common.opener();

	var oo = getRadio("processers");

	if (oo == null)
	{
		alert('请选择外网询价单');
		return;
	}

    opener.getPriceAskProvider(oo);

    closes();
}

function closes()
{
	opener = null;
	window.close();
}

function query()
{
	adminForm.submit();

}

</script>
<title>外网询价列表</title>

<link href="../css/center.css" type=text/css rel=stylesheet>
<script src="../js/CommonScriptMethod.js"></script>
</head>
<body class="body_class" onload="loadForm(adminForm)">
<form action="./common.do" name="adminForm"><input type="hidden"
	value="rptUser" name="method">
<input type="hidden" value="l" name="load">
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
				<td width="550" class="navigation">日常管理 &gt;&gt; 外网询价列表</td>
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
								<td class="caption"><strong>浏览外网询价:</strong></td>
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
						<td align="center" width="8%" align="center">选择</td>
						<td align="center" onclick="tableSort(this)" class="td_class">时间</td>
						<td align="center" onclick="tableSort(this)" class="td_class">供应商</td>
						<td align="center" onclick="tableSort(this)" class="td_class">产品编码</td>
						<td align="center" onclick="tableSort(this)" class="td_class">产品名称</td>
						<td align="center" onclick="tableSort(this)" class="td_class">可提供数量</td>
						<td align="center" onclick="tableSort(this)" class="td_class">剩余数量</td>
						<td align="center" onclick="tableSort(this)" class="td_class">价格</td>
						<td align="center" onclick="tableSort(this)" class="td_class">描述</td>
					</tr>

					<c:forEach items="${beanList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td align="center"><input type="radio" name="processers" ppid="${item.pid}" pprovideriId="${item.providerId}"
							     pproviderName="${item.providerName}" pamount="${item.remainmount}"
								pn="${item.productName}" pp="${my:formatNum(item.price)}" paskid="${item.askId}" value="${item.productId}" /></td>
							<td align="center" onclick="hrefAndSelect(this)">${item.logTime}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.providerName}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.productName}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.productCode}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.supportAmount}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.remainmount}</td>
							<td align="center" onclick="hrefAndSelect(this)">${my:formatNum(item.price)}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.description}</td>
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
		<td width="100%">
		<div align="right"><input type="button" class="button_class"
			value="&nbsp;&nbsp;确 定&nbsp;&nbsp;" onClick="sure()">&nbsp;&nbsp;<input
			type="button" class="button_class"
			value="&nbsp;&nbsp;关 闭&nbsp;&nbsp;" onClick="closes()">&nbsp;&nbsp;</div>
		</td>
		<td width="0%"></td>
	</tr>
</table>

</form>
</body>
</html>
