<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="凭证明细"/>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="javascript">

function load()
{
}
</script>

</head>

<body class="body_class">
<form name="formEntry">
<input type="hidden" name="id" value="${bean.id}">
<p:navigation
	height="22">
	<td width="550" class="navigation">凭证明细</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>凭证信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">

		<p:table cells="2">
			<p:cell title="凭证时间">${bean.financeDate}</p:cell>
			<p:cell title="标识">${bean.id}</p:cell>
			<p:cell title="纳税实体">${bean.dutyName}</p:cell>
			<p:cell title="类型">${my:get('financeType', bean.type)}</p:cell>
			<p:cell title="创建类型">${my:get('financeCreateType', bean.createType)}</p:cell>
			<p:cell title="状态">${my:get('financeStatus', bean.status)}</p:cell>
			<p:cell title="总金额">${bean.showInmoney}</p:cell>
			<p:cell title="录入人">${bean.createrName}</p:cell>
			<p:cell title="录入时间">${bean.logTime}</p:cell>
			<p:cell title="描述" end="true">${bean.description}</p:cell>
		</p:table>
	</p:subBody>
	
	<p:tr/>
	
	<p:subBody width="100%">
		<p:table cells="2">
			<tr align="center" class="content0">
				 <td width="10%" align="center">索引</td>
				 <td width="15%" align="center">摘要</td>
                 <td width="40%" align="center">科目</td>
                 <td width="10%" align="center">关联单据</td>
                 <td width="8%" align="center">借方金额</td>
                 <td width="8%" align="center">贷方金额</td>
			</tr>
			
			<c:forEach items="${bean.itemVOList}" var="item">
			<tr align="center" class="content1">
				<td align="center">${item.pareId}</td>
				<td align="center">${item.description}</td>
				<td align="center">${item.taxName}(${item.departmentName}/${item.stafferName}/${item.unitName})</td>
				<td align="center">${item.refId}</td>
				<td align="center">${item.showInmoney}</td>
				<td align="center">${item.showOutmoney}</td>
			</tr>
			</c:forEach>
		</p:table>
	</p:subBody>
	
	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right">
		<input type="button" class="button_class" id="return_b"
			style="cursor: pointer" value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"
			onclick="javascript:history.go(-1)">
		</div>
	</p:button>

	<p:message2/>
</p:body></form>
</body>
</html>

