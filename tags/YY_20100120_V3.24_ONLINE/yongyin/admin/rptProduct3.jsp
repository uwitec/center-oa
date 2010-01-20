<%@ page contentType="text/html;charset=GBK" language="java"%>
<%@include file="./common.jsp"%>
<html>

<head>
<base target="_self">
<p:link title="产品查询" />
<script src="../js/prototype.js"></script>
<script src="../js/compatible.js"></script>
<script src="../js/public.js"></script>
<script src="../js/common.js"></script>
<script language="javascript">
 
function sure()
{
	var opener = window.common.opener();

	var oo = getRadio("products");

	if (oo == null)
	{
		alert('请选择产品');
		return;
	}
	
    opener.getProduct(oo);

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

function pop(obj)
{
	window.open(obj.href);
}

function opens(url)
{
	window.common.modal(url);
}

</script>
</head>
<body class="body_class">
<form action="./product.do" name="adminForm"><input type="hidden"
	value="rptInQueryProduct3" name="method"> <input type="hidden"
	value="1" name="firstLoad"><input type="hidden" value="${type}"
	name="type"> <c:set var="ltype"
	value="${type == '1' ? 'checkbox' : 'radio'}" />
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
<table width="95%" border="0" cellpadding="0" cellspacing="0"
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

					<tr class="content1">
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
				<table width="100%" border="0" cellspacing="2">
					<tr>
						<td>
						<table width="100%" border="0" cellpadding="0" cellspacing="10">
							<tr>
								<td width="35">&nbsp;</td>
								<td width="6"><img src="../images/dot_r.gif" width="6"
									height="6"></td>
								<td class="caption"><strong>浏览产品:</strong><b>【双击表格可以直接确定】</b></td>
								<td align="right"><input type="button" class="button_class" id="b_sure"
									value="&nbsp;&nbsp;确 定&nbsp;&nbsp;" onClick="sure()"></td>
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
						<td align="center" onclick="tableSort(this)" class="td_class">产品编码</td>
						<td align="center" onclick="tableSort(this)" class="td_class">产品名称</td>
						<td align="center" onclick="tableSort(this)" class="td_class">产品数量</td>
						<td align="center" onclick="tableSort(this)" class="td_class">图片</td>
					</tr>

					<c:forEach items="${productList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td align="center"><input type="radio" name="products" id="products_${item.id}"
								productname="${item.name}" value="${item.id}" /></td>
							<td align="center" onclick="hrefAndSelect(this)" ondblclick="sure()">
							<a title="点击查看询价历史" target="_blank" onclick="opens('../admin/price.do?method=queryNearlyPrice&productId=${item.id}')"><u style="cursor: pointer;">
							${item.code}</u></a>
							</td>
							<td align="center" onclick="hrefAndSelect(this)" ondblclick="sure()">${item.name}</td>
							<td align="center" onclick="hrefAndSelect(this)" ondblclick="sure()">${item.num}</td>
							<td align="center"><span style="cursor: pointer;"
							 href="${rootUrl}${item.picPath}?${random}" onclick="pop(this)" title="点击查看原图">
							<img src="${rootUrl}${item.picPath}?${random}" width="80" height="80"></span></td>
						</tr>
					</c:forEach>
				</table>

				<p:formTurning form="adminForm" method="rptInQueryProduct3"></p:formTurning>
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
		<div align="right"><b>【双击表格可以直接确定】</b><input type="button" class="button_class"
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
