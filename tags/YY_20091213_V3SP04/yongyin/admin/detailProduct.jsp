<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="产品详细" />
<script language="JavaScript" src="../js/key.js"></script>
<script language="JavaScript" src="../js/common.js"></script>

<script language="javascript">

</script>

</head>
<body class="body_class">
<form><p:navigation height="22">
	<td width="550" class="navigation">产品详细</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>产品信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:table cells="1">
			<p:cell title="产品名称">
			${bean.name}
			</p:cell>

			<p:cell title="产品编码">
			${bean.code}
			</p:cell>

			<p:cell title="产品类型">
				<select name="genre" values="${bean.genre}" readonly="true">
					<option value="">--</option>
					<c:forEach items="${list}" var="item">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</p:cell>

			<p:cell title="盘点分类">
				<select name="type" oncheck="notNone;" values="${bean.type}"
					readonly="true">
					<option value="">--</option>
					<option value="0">每天盘点</option>
					<option value="1">其他</option>
				</select>
			</p:cell>

			<p:cell title="产品图片">
				<img src="${rootUrl}${bean.picPath}?${random}">
			</p:cell>

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>
</p:body></form>
</body>
</html>

