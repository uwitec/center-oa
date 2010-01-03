<%@ page contentType="text/html;charset=GBK" language="java"%>
<%@include file="./common.jsp"%>

<html>
<head>
<base target="_self">
<p:link title="产品查询" />
<script language="JavaScript" src="../js/compatible.js"></script>
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">

if(window.HTMLElement)
{
	HTMLElement.prototype.__defineGetter__('productName',function(){ 
	return this.getAttribute('productName'); 
	}); 
	HTMLElement.prototype.__defineSetter__('productName',function(){ 
	this.setAttribute('productName', sText);
	return sText; 
	}); 
	
	HTMLElement.prototype.__defineGetter__('productId',function(){ 
    return this.getAttribute('productId'); 
    }); 
    HTMLElement.prototype.__defineSetter__('productName',function(){ 
    this.setAttribute('productId', sText);
    return sText; 
    }); 
	
	HTMLElement.prototype.__defineGetter__('num',function(){ 
	return this.getAttribute('num'); 
	}); 
	HTMLElement.prototype.__defineSetter__('num',function(){ 
	this.setAttribute('num', sText);
	return sText; 
	}); 
}

function add()
{
	var opener = window.common.opener();
	
	var oo = getRadio("relation");
	if (oo == null)
	{
		alert('请选择产品');
		return;
	}
	
    opener.getProduct(oo);
    
    opener = null;
    window.close();
}

function load()
{
	loadForm();
	$f('name');
}

function res()
{
	$('name').value = '';
	$('code').value = '';
	$('storageName').value = '';
}


</script>
</head>
<body class="body_class" onload="load()">
<form action="../admin/das.do" name="adminForm"><input
	type="hidden" value="queryProduct" name="method"> <input
	type="hidden" value="1" name="firstLoad"> <input type="hidden"
	value="${depotpartBean.id}" name="depotpartId"> <input
	type="hidden" value="1" name="rpt">
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
				<td width="550" class="navigation">仓区管理 &gt;&gt; 仓区列表 &gt;&gt;
				浏览产品</td>
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
						<td width="15%" align="center">产品名称</td>
						<td align="center"><input type="text" name="name"
							value="${name}"></td>
						<td width="15%" align="center">产品编码</td>
						<td align="center"><input type="text" name="code"
							value="${code}"></td>
					</tr>

					<tr class="content2">
						<td width="15%" align="center">储位</td>
						<td align="center"><input type="text" name="storageName"
							value="${storageName}"></td>
						<td colspan="2" align="right"><input type="submit"
							class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;">&nbsp;&nbsp;<input
							type="button" onclick="res()" class="button_class"
							value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"></td>
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
		<table width="85%" border="0" cellpadding="0" cellspacing="0"
			class="border">
			<tr>
				<td>
				<table width="100%" border="0" cellspacing='1'>
					<tr align="center" class="content0">
						<td align="center" width="8%" class="td_class"><B>选择</B></td>
						<td align="center" onclick="tableSort(this, true)"
							class="td_class"><B>仓区</B></td>
						<td align="center" onclick="tableSort(this)" class="td_class"><B>储位</B></td>
						<td align="center" onclick="tableSort(this)" class="td_class"><B>产品名称</B></td>
						<td align="center" onclick="tableSort(this)" class="td_class"><B>产品编码</B></td>
						<td align="center" onclick="tableSort(this)" class="td_class"><B>可发货数量</B></td>
						<td align="center" onclick="tableSort(this, true)"
							class="td_class"><B>实际库存</B></td>
					</tr>

					<c:forEach items="${productList}" var="item" varStatus="vs">
						<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
							<td align="center"><input type="radio" name="relation" productId="${item.productId}"
								index="0" productName="${item.productName}" am="${item.amount}"
								value="${item.id}" /></td>
							<td onclick="hrefAndSelect(this)" align="center">${depotpartBean.name}</td>
							<td onclick="hrefAndSelect(this)" align="center">${item.storageName}</td>
							<td onclick="hrefAndSelect(this)" align="center">${item.productName}</td>
							<td onclick="hrefAndSelect(this)" align="center">${item.productCode}</td>
							<td onclick="hrefAndSelect(this)" align="center">${item.mayAmount}</td>
							<td onclick="hrefAndSelect(this)" align="center">${item.amount}</td>
						</tr>
					</c:forEach>
				</table>

				<table width="100%" border="0" cellpadding="0" cellspacing="0"
					align="center" class="table1">
					<tr>
						<td colspan="8" align="right"><c:if
							test="${!ProductList.firstPage}">
							<a
								href="../admin/das.do?method=queryProduct&rpt=1&page=previous&depotpartId=${depotpartBean.id}"><font
								color="blue"><img src="../images/preview.gif" border="0"></font></a>
						</c:if> <c:if test="${!ProductList.lastPage}">
							<a
								href="../admin/das.do?method=queryProduct&rpt=1&page=next&depotpartId=${depotpartBean.id}"><font
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
		<td width="92%">
		<div align="right"><input type="button" class="button_class"
			value="&nbsp;&nbsp;确 定&nbsp;&nbsp;" onclick="add()"></div>
		</td>
		<td width="8%"></td>
	</tr>

</table>

</form>
</body>
</html>
