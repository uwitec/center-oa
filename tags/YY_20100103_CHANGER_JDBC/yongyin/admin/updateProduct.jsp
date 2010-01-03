<%@ page contentType="text/html;charset=GBK" language="java"%>
<%@include file="./common.jsp"%>
<html>
<head>

<p:link title="修改产品" cal="false"/>

<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="javascript">
function callbacks()
{
   submit("确定修改此产品");
}

function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formBean" action="./product.do?method=updateProduct" enctype="multipart/form-data" method="post">
<input
	type="hidden" name="productId" value="${bean.id}">
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
				<td width="550" class="navigation">日常维护 &gt;&gt; <span
					style="cursor: hand" onclick="javascript:history.go(-1)">浏览产品</span>
				&gt;&gt; 修改产品</td>
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
								<td width="15">&nbsp;</td>
								<td width="6"><img src="../images/dot_r.gif" width="6"
									height="6"></td>
								<td class="caption"><strong>产品信息:</strong></td>
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
		<table width="85%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr class="content2">
						<td width="30%">产品名称:</td>
						<td><input type="text" name="name" maxlength="50" size="40"
							value="${bean.name}" oncheck="notNone"><font
							color="#FF0000">*</font></td>
					</tr>

					<tr class="content1">
						<td width="30%">产品编码:</td>
						<td><input type="text" name="code" maxlength="16" readonly="readonly"
							value="${bean.code}" oncheck="notNone;isNumberOrLetter"><font
							color="#FF0000">*</font></td>
					</tr>

					<tr class="content2">
						<td width="30%">产品类型:</td>
						<td><select name="genre" oncheck="notNone;" values="${bean.genre}">
							<option value="">--</option>
							<c:forEach items="${list}" var="item">
							<option value="${item.id}">${item.name}</option>
							</c:forEach>
						</select><font color="#FF0000">*</font></td>
					</tr>

					<tr class="content1">
						<td width="30%">盘点分类:</td>
						<td><select name="type" oncheck="notNone;" values="${bean.type}">
							<option value="">--</option>
							<option value="0">每天盘点</option>
							<option value="1">其他</option>
						</select><font color="#FF0000">*</font></td>
					</tr>

					<c:if test="${my:length(bean.picPath) > 0}">
					<tr class="content2">
						<td width="30%">图片:</td>
						<td><img src="${rootUrl}${bean.picPath}?${random}"></td>
					</tr>
					</c:if>

					<tr class="content1">
						<td width="30%">产品图片:</td>
						<td><input type="file" name="picPath" class="button_class" size="40" contenteditable="false"></td>
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
		<td width="82%">
		<div align="right"><input name="add" type="button"
			class="button_class" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;" onclick="callbacks()">&nbsp;&nbsp;
		<input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
		</td>
		<td width="18%"></td>
	</tr>

	<tr height="10">
		<td height="10" colspan='2'></td>
	</tr>


	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>


</table>


</form>
</body>
</html>

