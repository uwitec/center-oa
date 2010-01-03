<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="新客户考核" cal="true" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/pop.js"></script>
<script language="JavaScript" src="../js/jquery/jquery.js"></script>
<script language="JavaScript" src="../js/plugin/highlight/jquery.highlight.js"></script>
<script language="javascript">

function load()
{
	loadForm();

	init();
}

function init()
{
    $.highlight(document.body, '超过预期', 'blue');
    
    $.highlight(document.body, '未达标', 'red');
}

function refush()
{
	$l('../examine/examine.do?method=queryNewCustomerExamine&pid=${examine.id}');
}

function oopen(id)
{
    window.common.modal('../admin/pop.do?method=popExamineLog&id=' + id);
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../examine/examine.do" method="post"><input
	type="hidden" name="method" value="configNewCustomerExamine"> <input type="hidden"
	name="pid" value="${examine.id}"> <p:navigation height="22">
	<td width="550" class="navigation">新客户考核</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>【${examine.stafferName}】新客户考核信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0" id="tables">
			<tr align="center" class="content0">
				<td width="5%" align="left">次序</td>
				<td width="20%" align="left">开始时间</td>
				<td width="20%" align="left">结束时间</td>
				<td align="left">客户数量(${totalReal}/${totalPlan})</td>
				<td align="left">结果</td>
				<td align="left">状态</td>
			</tr>

			<c:forEach items="${newList}" var="item" varStatus="vs">
				<tr class='${vs.index % 2 == 0 ? "content2" : "content1"}'>
				    <td>${item.step}</td>
					<td>${item.beginTime}</td>
					<td>${item.endTime}</td>
					<td><a title="点击查看详细" href="javaScript:void(0)" onclick="oopen('${item.id}')">${item.realValue}/${item.planValue}</a></td>
					<td>${my:get("examineResult", item.result)}</td>
					<td>${my:get("examineItemStatus", item.status)}</td>
				</tr>
			</c:forEach>
		</table>
	</p:subBody>

	<p:line flag="1" />
</p:body></form>
<p:message></p:message>
</body>
</html>

