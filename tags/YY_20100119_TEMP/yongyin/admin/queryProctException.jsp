<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="货物异常列表" />
<script language="JavaScript" src="../js/common.js"></script>
<script src="../js/prototype.js"></script>
<script language="javascript">
function addproException()
{
	document.location.href = '../admin/addProductException.jsp';
}

function updateproException()
{
	if (getRadioValue('proExceptions') == '')
	{
		alert('请选择货物异常');
		return;
	}
	
	if (getRadio('proExceptions').statuss == 0 || getRadio('proExceptions').statuss == 2)
	{
		document.location.href = '../admin/proException.do?method=findProductException&update=1&id=' + getRadioValue('proExceptions');
	}
	else
	{
		alert('不能操作!');
	}
}

function delproException()
{
	if (getRadioValue('proExceptions') == '')
	{
		alert('请选择货物异常');
		return;
	}
	
	if (getRadio('proExceptions').statuss == 0 || getRadio('proExceptions').statuss == 2)
	{
		if (window.confirm('确定删除货物异常?'))
		document.location.href = 
		'../admin/proException.do?method=delProctException&id=' + getRadioValue('proExceptions');
	}
	else
	{
		alert('不能操作!');
	}
}

function pass()
{
	if (getRadioValue('proExceptions') == '')
	{
		alert('请选择货物异常');
		return;
	}
	
	if (getRadio('proExceptions').statuss == 1)
	{
		if (window.confirm('确定通过货物异常?'))
		{
			var sss = window.prompt('请输入通过原因：', '');
			
			if (sss == '' || sss == null)
			{
				return;
			}
		 
			document.location.href = 
				'../admin/proException.do?method=updateProctExceptionStatus&opr=1&id=' 
				+ getRadioValue('proExceptions') + '&apply=' + sss;
		}
	}
	else
	{
		alert('不能操作!');
	}
}

function reject()
{
	if (getRadioValue('proExceptions') == '')
	{
		alert('请选择货物异常');
		return;
	}
	
	if (getRadio('proExceptions').statuss == 1)
	{
		if (window.confirm('确定驳回货物异常?'))
		{
			var sss = window.prompt('请输入驳回原因：', '');
			
			if (sss == '' || sss == null)
			{
				return;
			}
		 
			document.location.href = 
				'../admin/proException.do?method=updateProctExceptionStatus&opr=0&id=' 
				+ getRadioValue('proExceptions') + '&apply=' + sss;
		}
	}
	else
	{
		alert('不能操作!');
	}
}

function load()
{
	loadForm();
	$f('productName');
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="bankList" action="../admin/proException.do"><input
	type="hidden" name="method" value="queryProductException"> <p:navigation
	height="22">
	<td width="550" class="navigation">货物异常管理 &gt;&gt; 货物异常列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="90%">

	<p:subBody width="95%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr class="content1">
				<td width="15%" align="center">开始时间</td>
				<td align="left">
				<p:plugin name="beginDate" size="20" value="${beginDate}"/>
				</td>
				<td width="15%" align="center">结束时间</td>
				<td align="left">
				<p:plugin name="endDate" size="20" value="${endDate}"/>
				</td>
			</tr>

			<tr class="content2">
				<td width="15%" align="center">产品名称</td>
				<td align="left"><input type="text" name="productName"
					value="${productName}"></td>
				<td width="15%" align="center">货单状态</td>
				<td align="left"><select name="status" class="select_class"
					values="${status}">
					<option value="">--</option>
					<option value="0">保存</option>
					<option value="1">提交</option>
					<option value="2">驳回</option>
					<option value="3">通过</option>
				</select></td>
			</tr>

			<tr class="content1">
				<td colspan="4" align="right"><input type="submit"
					class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;">&nbsp;&nbsp;<input
					type="reset" class="button_class"
					value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"></td>
			</tr>
		</table>

	</p:subBody>

	<p:title>
		<td class="caption"><strong>货物异常列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="95%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" class="td_class">选择</td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>产品</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>数量</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>申请人</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>状态</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>时间</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="25%"><strong>原因</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>附件</strong></td>
			</tr>

			<c:forEach items="${requestScope.proctExceptionList}" var="item"
				varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center"><input type="radio" name="proExceptions"
						statuss="${item.status}" value="${item.id}" ${vs.index== 0 ? "checked" : ""}/></td>
					<td align="center" onclick="hrefAndSelect(this)"><a
						href="../admin/proException.do?method=findProductException&id=${item.id}">${item.productName}</a></td>
					<td align="center" onclick="hrefAndSelect(this)">${item.amount}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.applyer}</td>
					<%
					    String[] sss1 = new String[] {"保存", "提交", "驳回", "通过"};
					    request.setAttribute("sss1", sss1);
					%>
					<td align="center" onclick="hrefAndSelect(this)">${my:getValue(item.status,
					sss1)}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.logDate}</td>
					<td align="center" onclick="hrefAndSelect(this)"
						title="${item.description}">${my:truncateString(item.description,
					0, 10)}</td>
					<td align="center" onclick="hrefAndSelect(this)"><a
						title="点击下载附件"
						href="../admin/proException.do?method=downProductException&id=${item.id}">${item.fileName}</a></td>
				</tr>
			</c:forEach>
		</table>

	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="98%" rightWidth="2%">
		<div align="right"><c:if test='${user.role != "MANAGER"}'>
			<input type="button" class="button_class"
				value="&nbsp;&nbsp;增加货物异常&nbsp;&nbsp;" onclick="addproException()">&nbsp;&nbsp;<input
				type="button" class="button_class"
				value="&nbsp;&nbsp;修改货物异常&nbsp;&nbsp;"
				onclick="updateproException()">&nbsp;&nbsp;<input
				type="button" class="button_class"
				value="&nbsp;&nbsp;删除货物异常&nbsp;&nbsp;" onclick="delproException()">&nbsp;&nbsp;
			</c:if> <c:if test='${user.role == "MANAGER"}'>
			<input type="button" class="button_class"
				value="&nbsp;&nbsp;通 过&nbsp;&nbsp;" onclick="pass()">&nbsp;&nbsp;<input
				type="button" class="button_class"
				value="&nbsp;&nbsp;驳 回&nbsp;&nbsp;" onclick="reject()">&nbsp;&nbsp;
			</c:if></div>
	</p:button>

	<p:message />

</p:body></form>
</body>
</html>

