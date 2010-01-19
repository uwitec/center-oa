<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>

<head>
<p:link title="资金日报" />
<script src="../js/prototype.js"></script>
<script src="../js/fanye.js"></script>
<script src="../js/common.js"></script>
<script language="javascript">
function load()
{
	loadForm();
	//initPage(100, $("result"), "tr", 1, 0);
}

function callbacks()
{
	document.location.href = '../admin/stat.do?method=queryHistoryStat&frist=1';
}
</script>
<title>资金日报</title>

</head>
<body class="body_class" onload="load()">
<form action="./stat.do" name="adminForm"><input type="hidden"
	value="queryCurrentStat" name="method"> <input type="hidden"
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
				<td width="550" class="navigation">资金日报  &gt;&gt; 本月记录</td>
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
					<tr class="content2">
						<td width="15%">帐户:</td>
						<td width="35%" colspan="3"><select name="bank" values="${bank}"
							onchange="submit()" class="select_class2">
							<option value="">--</option>
							<c:forEach items="${bankList}" var="item">
								<option value="${item.name}">${item.name}</option>
							</c:forEach>
						</select></td>
					</tr>

					<tr class="content1">
						<td colspan="4" align="right"><input type="submit"
							class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;"></td>
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
								<td class="caption"><strong>资金日报:</strong></td>
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
				<table width="100%" border="0" cellspacing='1' id="result">
					<tr align="center" class="content0">
						<td align="center" onclick="tableSort(this)" class="td_class"><strong>选择</strong></td>
						<td align="center" onclick="tableSort(this)" class="td_class"><strong>帐户</strong></td>
						<td align="center" onclick="tableSort(this, true)" class="td_class"><strong>上月结余</strong></td>
						<td align="center" onclick="tableSort(this, true)" class="td_class"><strong>本月入账</strong></td>
						<td align="center" onclick="tableSort(this, true)" class="td_class"><strong>本月出账</strong></td>
						<td align="center" onclick="tableSort(this, true)" class="td_class"><strong>本月结余</strong></td>
					</tr>

					<c:forEach items="${statBanMap}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td align="center"><input type="radio" name="stat" value=""
								${vs.index== 0 ? "checked" : ""}/></td>
							<td align="center" onclick="hrefAndSelect(this)">${item.value.bank}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.value.lastMoney}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.value.inMoney}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.value.outMoney}</td>
							<td align="center" onclick="hrefAndSelect(this)">${item.value.tatolMoney}</td>
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
		<div align="right"><input name="add" type="button"
			class="button_class" value="&nbsp;历史月结&nbsp;" onclick="callbacks()"></div>
		</td>
		<td width="8%"></td>
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
