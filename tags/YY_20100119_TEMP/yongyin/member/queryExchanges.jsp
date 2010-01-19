<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="会员兑换列表" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/fanye.js"></script>
<script language="javascript">

function detail(id)
{
	document.location.href = '../member/member.do?method=findConsume&id=' + id;
}

function load()
{
	loadForm();
}

function back()
{
	document.location.href = '../member/member.do?method=queryMembers&firstLoad=1';
}

function addBean()
{
	document.location.href = '../member/member.do?method=preForAddExchange&memberId=' + '${memberId}';
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../member/member.do"><input
	type="hidden" name="method" value="queryExchanges"><input type="hidden" value="1"
	name="firstLoad"> <p:navigation
	height="22">
	<td width="550" class="navigation">会员兑换管理 &gt;&gt; 会员兑换列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>兑换物品列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0" id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>卡号</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>客户</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>兑换物品</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"><strong>使用积分</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"><strong>剩余积分</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>受理人</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>时间</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>操作</strong></td>
			</tr>

			<c:forEach items="${list}" var="item" varStatus="vs">
				<tr class="${s.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)">
					<a title="查看会员明细" href="../member/member.do?method=findMember&id=${item.memberId}">${item.cardNo}</a>
					</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.memberName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.entity}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.costpoint}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.beforepoint}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.userName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.logTime}</td>
					<td align="center" onclick="hrefAndSelect(this)">

					</td>
				</tr>
			</c:forEach>
		</table>

		<p:formTurning form="formEntry" method="queryExchanges"></p:formTurning>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class" name="adds"
			style="cursor: pointer" value="&nbsp;&nbsp;积分兑换&nbsp;&nbsp;"
			onclick="addBean()">&nbsp;&nbsp;<input type="button" class="button_class"
			onclick="back()"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;">
			</div>
	</p:button>

	<p:message />

</p:body></form>
</body>
</html>

