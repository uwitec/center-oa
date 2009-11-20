<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="考评列表" />
<base target="_self">
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">
function querys()
{
	formEntry.submit();
}

function sures()
{
	var opener = window.common.opener();
	
	var oo = getRadio("beans");
	
	if (oo && oo.length == 0)
	{
		alert('请选择考评');
		return;
	}
	
	if (oo)
    opener.getExamine(oo);
    
    closes();
}

function closes()
{
	opener = null;
	window.close();
}

function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../member/member.do"><input
	type="hidden" name="method" value="rptQueryMember"><input type="hidden" value="1"
	name="firstLoad"> <p:navigation
	height="22">
	<td width="550" class="navigation">考评类型</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>考评类型：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="90%">
		<table width="100%" align="center" cellspacing='1' class="table0" id="result">
			<tr align=center class="content0">
				<td align="center" >选择</td>
				<td align="center" ><strong>名称</strong></td>
				<td align="center" ><strong>年份</strong></td>
				<td align="center" ><strong>利润指标</strong></td>
				<td align="center" ><strong>类型</strong></td>
				<td align="center" ><strong>考核人</strong></td>
				<td align="center" ><strong>父考核</strong></td>
				<td align="center" ><strong>考核区域</strong></td>
			</tr>

			<c:forEach items="${list}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center"><input type="radio" name="beans"  pname="${item.name}"
					value="${item.id}" ${vs.index == 0 ? "checked" : ""}/></td>
					<td align="center">${item.name}</td>
					<td align="center">${item.year}</td>
					<td align="center">${my:formatNum(item.totalProfit)}</td>
					<td align="center">${my:get('attType' ,item.attType)}</td>
					<td align="center">${item.stafferName}</td>
					<td align="center">${item.parentName}</td>
					<td align="center">${item.locationName}</td>
				</tr>
			</c:forEach>
		</table>
		
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class" id="adds"
			style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="sures()"></div>
	</p:button>

	<p:message />
	
</p:body></form>
</body>
</html>

