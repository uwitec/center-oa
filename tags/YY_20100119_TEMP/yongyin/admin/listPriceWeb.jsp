<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="网站列表" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/fanye.js"></script>
<script language="javascript">
function addBean()
{
	document.location.href = '../admin/addPriceWeb.jsp';
}


function load()
{
	loadForm();
}

function dels(id, question)
{
	if (window.confirm('确定删除此网站?'))
	{
		document.location.href = '../admin/price.do?method=delPriceWeb&id=' + id;
	}
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../admin/price.do"><p:navigation
	height="22">
	<td width="550" class="navigation">询价管理 &gt;&gt; 网站列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>网站列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="15%"><strong>网站名称</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>网站地址</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>操作</strong></td>
			</tr>

			<c:forEach items="${list}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)">${item.name}</td>

					<td align="center" onclick="hrefAndSelect(this)">${item.url}</td>

					<td align="center" onclick="hrefAndSelect(this)"><a
						title="删除试卷" href="javascript:dels('${item.id}', '${item.id}')">
					<img src="../images/del.gif" border="0" height="15" width="15"></a>
					</td>
				</tr>
			</c:forEach>
		</table>

	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			name="adds" style="cursor: pointer"
			value="&nbsp;&nbsp;增加网站&nbsp;&nbsp;" onclick="addBean()"></div>
	</p:button>

	<p:message />

</p:body></form>
</body>
</html>

