<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312">
<p:link title="上传标题图片" />
<script src="../js/CommonScriptMethod.js"></script>
<script language="JavaScript" src="../js/cal.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<style type="text/css">
		<!--
		body,td,th {
			font-size: 12px;
		}
		
		.style1 {color: #000000}
		.width {
		}
		-->
		</style>

<Script Language="JavaScript">
function uploads()
{
	if (formCheck())
	{
		if (window.confirm('确定导出数据?'))
		form1.submit();
	}
}
</Script>

</head>
<body id="bodys" class="body_class">

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
				<td width="550" class="navigation">日常数据导入 &gt;&gt; 数据导入</td>
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


<form action="../admin/exportData.do" method="post" name="form1">

<table width="70%" border="0" cellpadding="0" cellspacing="0"
	align="center" id="tables">
	<tr>
		<td valign="top" colspan='2'>
		<table width="100%" border="0" cellpadding="0" cellspacing="0">
			<!--DWLayoutTable-->
			<tr>
				<td width="784" height="2"></td>
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
								<td class="caption"><strong>选择时间:(导出出入库单和收付款单和临时产品客户)</strong></td>
							</tr>
						</table>
						</td>
					</tr>
				</table>
				</div>
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
		<table border="0" width=70% class="border" align=center>

			<tr class="content1">
				<td class="content2">选择时间:</td>
				<td class="content2"><input type="text" name="date" oncheck="notNone" message="请选择时间"
					onKeypress="calDate(this)" class="time_input"
					onclick="calDate(this)" maxlength="20" size="20"
					readonly="readonly" /></td>
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
		<td width="85%" id="submits">
		<div align="right"><input type="button"
			value="&nbsp;&nbsp;导 出&nbsp;&nbsp;" onclick="uploads()"
			class="button_class" /></div>
		</td>
		<td width="15%"></td>
	</tr>

	<tr>
		<td height="10" colspan='2'></td>
	</tr>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>

</table>
</form>
<br>
<br>

</body>
</html>
