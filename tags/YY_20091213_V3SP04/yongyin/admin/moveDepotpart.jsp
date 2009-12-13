<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="仓区移动" />
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/buffalo.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script src="../js/public.js"></script>
<script language="javascript">

var END_POINT="${pageContext.request.contextPath}/bfapp";

var buffalo = new Buffalo(END_POINT);

function applyPasswords()
{
	if (formCheck() && window.confirm('确定移动产品?'))
	buffalo.remoteCall("storageManager.moveStorage",[$F('src'), $F('dest'), $F('productId'), parseInt($$('amount')), $F('user')], function(reply) {	
		        var resultobject = reply.getResult();
		        
		        if(typeof(resultobject) == 'string')
				{
					alert('仓区间产品移动成功!流水号是:' + resultobject);
					reset();
					return;
				}
				else
				{
					alert(resultobject.message);
				}
				
		        return;
		});
}



function getProduct(oo)
{
	$('productId').value = $a(oo, 'productId');//oo.productId;
	
	$('productName').value = $a(oo, 'productName');//oo.productName;
}

function reset()
{
	$('productName').value ='';
	$('amount').value ='';
	$('user').value ='';
}

function selectProducts()
{
	var depotpartId = $F('src');
	
	if (depotpartId == null || depotpartId == '')
	{
		alert('请选择仓区');
		return;
	}
	
	window.common.modal('../admin/das.do?method=queryProduct&rpt=1&firstLoad=1&depotpartId='+ depotpartId);
}


function load()
{
}

</script>

</head>
<body class="body_class" onload="load()">
<form><input type="hidden" value="1" name="productId"> <p:navigation
	height="22">
	<td width="550" class="navigation">仓区管理 &gt;&gt; <span
		style="cursor: hand" onclick="javascript:history.go(-1)">仓区列表</span>
	&gt;&gt; 产品移动</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="80%">

	<p:title>
		<td class="caption"><strong>产品移动：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="75%">
		<p:table cells="1">
			<p:cell title="源仓区" width="20">
				<select name="src" oncheck="notNone;noEquals($F('dest'))" message="源仓区和目的仓区不能相同" style="width: 240px">
					<option value="">--</option>
					<c:forEach items="${depotpartList}" var="item">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</p:cell>

			<p:cell title="移动产品">
				<input type="text" name="productName" oncheck="notNone;">&nbsp;&nbsp;
				<input type="button" value="&nbsp;选择产品&nbsp;"
					class="button_class" onclick="selectProducts()">
			</p:cell>


			<p:cell title="目的仓区" width="20">
				<select name="dest" oncheck="notNone;" style="width: 240px">
					<option value="">--</option>
					<c:forEach items="${depotpartList}" var="item">
						<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select>
			</p:cell>

			<p:cell title="移动数量" width="15">
				<input type="text" name="amount" oncheck="notNone;isInt;range(0)">
				<font color="#FF0000">*</font>
			</p:cell>

			<p:cell title="申请人" width="15">
				<input type="text" name="user" oncheck="notNone;">
				<font color="#FF0000">*</font>
			</p:cell>

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="87%" rightWidth="13%">
		<div align="right"><input type="button" class="button_class"
			style="cursor: pointer" value="&nbsp;&nbsp;移 动&nbsp;&nbsp;"
			onclick="applyPasswords()">&nbsp;&nbsp;<input type="button"
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

