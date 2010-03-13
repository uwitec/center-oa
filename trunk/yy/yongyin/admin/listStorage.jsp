<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="储位列表" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="javascript">
function addStorage()
{
	$l('../admin/addStorage.jsp?add=1&depotpartId=' + $$('depotpartId'));
}

function modifyStorage()
{
	if (getRadioValue('storages') == '')
	{
		alert('请选择储位');
		return;
	}

	$l('../admin/das.do?method=findStorage&modfiy=1&depotpartId=${depotpartId}&id=' + getRadioValue('storages'));
}

function defaultStorage()
{
	var svalue = getRadioValue('storages');
	$l('../admin/das.do?method=findStorage&modfiy=2&id=' + svalue + '&depotpartId=${depotpartId}');
}

function backs()
{
	$l('../admin/das.do?method=queryDepotpart');
}

function del()
{
	if (getRadioValue('storages') == '')
	{
		alert('请选择储位');
		return;
	}

	if (window.confirm('确定删除储位-' + getRadio('storages').storageName))
	{
		$l('../admin/das.do?method=delStorage&depotpartId=${depotpartId}&id=' + getRadioValue('storages'));
	}
}

function load()
{
	loadForm();
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="bankList" action="../admin/das.do"><input
	type="hidden" name="depotpartId" value="${depotpartId}"> <p:navigation
	height="22">
	<td width="550" class="navigation">储位管理 &gt;&gt; 浏览储位</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>储位列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" class="td_class" width="5%">选择</td>
				<td align="center" width="20%" class="td_class" onclick="tableSort(this)"><strong>仓区</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="15%"><strong>储位名称</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>储存产品</strong></td>
			</tr>

			<c:forEach items="${requestScope.listStorage}" var="item"
				varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center"><input type="radio" name="storages" storageName="${item.name}"
						value="${item.id}" index="0"/></td>
					<td align="center" onclick="hrefAndSelect(this)">${item.depotpartName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.name}</td>
					<td align="center" onclick="hrefAndSelect(this)" title="${item.productName}">
					${my:truncateString(item.productName, 0, 100)}</td>
				</tr>
			</c:forEach>
		</table>

	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			style="cursor: pointer" value="&nbsp;&nbsp;增加储位&nbsp;&nbsp;"
			onclick="addStorage()">&nbsp;&nbsp;<input type="button"
			class="button_class" value="&nbsp;&nbsp;修改储位&nbsp;&nbsp;"
			onclick="modifyStorage()">&nbsp;&nbsp;<input type="button"
			class="button_class" value="&nbsp;&nbsp;删除储位&nbsp;&nbsp;" onclick="del()">
		&nbsp;&nbsp;<input type="button"
			class="button_class" value="&nbsp;储位产品转移&nbsp;"
			onclick="defaultStorage()">
			&nbsp;&nbsp;<input type="button" class="button_class"
			value="&nbsp;&nbsp;返回仓区&nbsp;&nbsp;" onclick="backs()"></div>
	</p:button>

	<p:message />

</p:body></form>
</body>
</html>

