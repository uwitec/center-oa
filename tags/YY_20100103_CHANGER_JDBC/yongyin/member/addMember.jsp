<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="增加会员" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<c:if test="${modify}">
<c:set var="dis" value="修改"/>
</c:if>
<c:if test="${!modify}">
<c:set var="dis" value="增加"/>
</c:if>
<script language="javascript">
function addApplys()
{
	submit('确定${dis}会员[' + $$('name') + ']');
}

function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../member/member.do">
	<input type="hidden" name="method" value="addMember">
<p:navigation height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">会员管理</span> &gt;&gt; ${dis}会员</td>
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
			<p:pro field="name" />
			<p:pro field="cardNo"/>
			<p:pro field="handphone"/>
			<p:pro field="password"/>
			<p:pro field="email"/>
			<p:pro field="connect"/>
			
			<p:pro field="grade">
				<option value="0">普卡</option>
			</p:pro>
			
			<p:pro field="type">
				<option value="0">普通会员</option>
				<option value="1">永久会员</option>
			</p:pro>
			<p:pro field="company"/>
			
			<p:pro field="position"/>
			
			<p:pro field="area" cell="1"/>
			
			<p:pro field="sex">
				<option value="0">男</option>
				<option value="1">女</option>
			</p:pro>
			
			<p:pro field="address" innerString="size='80'" cell="2"/>
			
			<p:pro field="description" innerString="cols=80 rows=3" cell="2"/>
			
		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class" name="adds"
			style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="addApplys()">&nbsp;&nbsp; <input type="button"
			class="button_class" onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>
</p:body></form>
</body>
</html>

