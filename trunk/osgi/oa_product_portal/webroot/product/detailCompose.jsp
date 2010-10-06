<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="产品合成"/>
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
	<td width="550" class="navigation">产品合成明细</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>产品合成基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">

		<p:table cells="2">
			<p:cell title="合成产品">${bean.productName}</p:cell>
			<p:cell title="产品编码">${bean.productCode}</p:cell>
			<p:cell title="数量">${bean.amount}</p:cell>
			<p:cell title="最终价格">${my:formatNum(bean.price)}</p:cell>
			<p:cell title="合成人" end="true">${bean.stafferName}</p:cell>
			<p:cell title="仓库">${bean.deportName}</p:cell>
			<p:cell title="目的仓区">${bean.depotpartName}</p:cell>
			<p:cell title="目的储位">${bean.storageName}</p:cell>
			<p:cell title="类型">${my:get('composeType', bean.type)}</p:cell>
			<p:cell title="时间" end="true">${bean.logTime}</p:cell>
			
		</p:table>
	</p:subBody>
	
	<p:tr/>
	
	<p:subBody width="100%">
		<p:table cells="2">
			<tr align="center" class="content0">
				<td width="50%" align="center">合成费用项</td>
				<td width="50%" align="center">费用</td>
			</tr>
			
			<c:forEach items="${bean.feeVOList}" var="item">
			<tr align="center" class="content1">
				<td width="50%" align="center">${item.feeItemName}</td>
				<td width="50%" align="center">${my:formatNum(item.price)}</td>
			</tr>
			</c:forEach>
		</p:table>
	</p:subBody>
	
	<p:tr/>
	
	<p:subBody width="100%">
		<p:table cells="2">
			<tr align="center" class="content0">
				<td width="15%" align="center">源仓区</td>
				<td width="45%" align="center">源产品</td>
				<td width="15%" align="center">数量</td>
				<td width="15%" align="center">价格</td>
			</tr>
			
			<c:forEach items="${bean.itemVOList}" var="item">
			<tr align="center" class="content1">
				<td align="center">${item.depotpartName}</td>
				<td align="center">${item.productName}(${item.productCode})</td>
				<td align="center">${item.amount}</td>
				<td align="center">${my:formatNum(item.price)}</td>
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

