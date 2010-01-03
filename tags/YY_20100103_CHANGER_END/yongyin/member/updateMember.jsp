<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="修改会员" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>

<script language="javascript">

var oldCard = '${bean.cardNo}';
function addApplys()
{
	if (oldCard != $$('cardNo'))
	{
		if (!window.confirm('确定修改会员的卡号?只有换卡的时候才有必须修改会员的卡号.'))
		{
			return;
		}
	}
	submit('确定修改会员[' + $$('name') + ']');
}

function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../member/member.do">
	<input type="hidden" name="method" value="updateMember">
	<input type="hidden" name="id" value="${bean.id}">
<p:navigation height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">会员管理</span> &gt;&gt; 修改会员</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>会员信息：【修改会员的卡号就可以实现换卡的功能】</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.centet.yongyin.bean.MemberBean" />

		<p:table cells="2">
			<p:pro field="name" value="${bean.name}" />
			<p:pro field="cardNo" value="${bean.cardNo}" />
			<p:pro field="handphone" value="${bean.handphone}"/>
			<p:pro field="password" value="${bean.password}"/>
			<p:pro field="email" value="${bean.email}"/>
			<p:pro field="connect" value="${bean.connect}"/>

			<c:if test="${user.role == 'SHOP'}">
			<p:pro field="grade" value="${bean.grade}">
				<option value="0">普卡</option>
				<option value="1">银卡</option>
				<option value="2">金卡</option>
				<option value="3">铂金卡</option>
			</p:pro>
			</c:if>

			<c:if test="${user.role != 'SHOP'}">
			<p:pro field="grade" innerString="readonly=true" value="${bean.grade}">
				<option value="0">普卡</option>
				<option value="1">银卡</option>
				<option value="2">金卡</option>
				<option value="3">铂金卡</option>
			</p:pro>
			</c:if>

			<p:pro field="type"  value="${bean.type}">
				<option value="0">普通会员</option>
				<option value="1">永久会员</option>
			</p:pro>
			<p:pro field="company"  value="${bean.company}"/>

			<p:pro field="position" value="${bean.position}"/>

			<p:pro field="area" cell="1" value="${bean.area}"/>

			<p:pro field="sex"  value="${bean.sex}">
				<option value="0">男</option>
				<option value="1">女</option>
			</p:pro>

			<p:pro field="address"   value="${bean.address}" innerString="size='80'" cell="2"/>

			<p:pro field="description" value="${bean.description}" innerString="cols=80 rows=3" cell="2"/>

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

	<p:message/>
</p:body></form>
</body>
</html>

