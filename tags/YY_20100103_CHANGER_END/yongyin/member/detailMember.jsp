<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="会员详细" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/key.js"></script>

<script language="javascript">

function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../member/member.do"><p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">会员管理</span> &gt;&gt; 会员详细</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>会员信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.centet.yongyin.bean.MemberBean" />

		<p:table cells="2">
			<p:pro field="name" value="${bean.name}" />
			<p:pro field="cardNo" innerString="readonly=true"
				value="${bean.cardNo}" />
			<p:pro field="handphone" value="${bean.handphone}" />
			<p:pro field="password" value="${bean.password}" />
			<p:pro field="email" value="${bean.email}" />
			<p:pro field="connect" value="${bean.connect}" />

			<c:if test="${user.role == 'SHOP'}">
				<p:pro field="grade" value="${bean.grade}">
					<option value="0">普卡</option>
					<option value="1">银卡</option>
					<option value="2">金卡</option>
					<option value="3">铂金卡</option>
				</p:pro>
			</c:if>

			<c:if test="${user.role != 'SHOP'}">
				<p:pro field="grade" innerString="readonly=true"
					value="${bean.grade}">
					<option value="0">普卡</option>
					<option value="1">银卡</option>
					<option value="2">金卡</option>
					<option value="3">铂金卡</option>
				</p:pro>
			</c:if>

			<p:pro field="type" value="${bean.type}">
				<option value="0">普通会员</option>
				<option value="1">永久会员</option>
			</p:pro>
			<p:pro field="company" value="${bean.company}" />

			<p:pro field="position" value="${bean.position}" />

			<p:pro field="area" cell="1" value="${bean.area}" />

			<p:cell title="积分" align="1">${bean.point}</p:cell>
			
			<p:pro field="sex" value="${bean.sex}" cell="2">
				<option value="0">男</option>
				<option value="1">女</option>
			</p:pro>

			<p:pro field="address" value="${bean.address}"
				innerString="size='80'" cell="2" />

			<p:pro field="description" value="${bean.description}"
				innerString="cols=80 rows=3" cell="2" />

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>
</p:body></form>
</body>
</html>

