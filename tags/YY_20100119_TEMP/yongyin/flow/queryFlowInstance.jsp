<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="流程实例列表" />
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">
function querys()
{
	$('method').value = 'queryFlowInstance';
	formEntry.submit();
}

function press()
{
	if (window.common.getEvent().keyCode == 13)
	{
		querys();
	}
}

function resets()
{
	formEntry.reset();

	$('title').value = '';

	setSelectIndex($('flowId'), 0);
	setSelectIndex($('status'), 0);
}

function load()
{
	$f('title');
}

function process(id)
{
	$('method').value = 'preForProcessFlowInstance';
	$('id').value = id;
	$('approve').value = "1";
	formEntry.submit();
}

function update(id)
{
	$('method').value = 'preForUpdateFlowInstance';
	$('id').value = id;
	formEntry.submit();
}

function del(id)
{
	if (window.confirm('确定删除此流程实例?'))
	{
		$('method').value = 'delFlowInstance';
		$('id').value = id;
		formEntry.submit();
	}
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../flow/flow.do"><input
	type="hidden" name="method" value="queryFlowInstance"> <input
	type="hidden" value="1" name="firstLoad"> <input type="hidden"
	value="" name="id">
<input type="hidden" value="" name="approve">
 <p:navigation height="22">
	<td width="550" class="navigation">流程实例管理 &gt;&gt; 流程实例列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center">开始时间</td>
				<td align="center" width="35%"><p:plugin name="alogTime"
					value="${alogTime}" /></td>
				<td align="center">结束时间</td>
				<td align="center" width="35%"><p:plugin name="blogTime"
					value="${blogTime}" /></td>
			</tr>

			<tr align=center class="content1">
				<td align="center">查询类型</td>
				<td align="center" width="35%"><select name="queryType"
					class="select_class" values="${queryType}" readonly=true>
					<option value="1">我的申请</option>
					<option value="2">当前处理</option>
					<option value="4">我的查阅</option>
					<option value="3">处理历史</option>
				</select></td>
				<td align="center">流程定义</td>
				<td align="center" width="35%"><select name="flowId"
					class="select_class" values="${flowId}">
					<option value="">--</option>
					<c:forEach items="${defined}" var="item">
					<option value="${item.id}">${item.name}</option>
					</c:forEach>
				</select></td>
			</tr>

			<tr align=center class="content0">
				<td align="center">标题</td>
				<td align="center" width="35%">
				<input type="text" value="${title}" name="title" onkeydown="press()"></td>
				<td align="center">状态</td>
				<td align="center" width="35%"><select name="status"
					class="select_class" values="${status}">
					<option value="">--</option>
					<option value="1">初始</option>
					<option value="2">审批中</option>
					<option value="3">结束</option>
				</select></td>
			</tr>

			<tr align=center class="content1">
				<td align="right" colspan="4"><input type="button"
					class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;"
					onclick="querys()">&nbsp;&nbsp; <input type="button"
					class="button_class" value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"
					onclick="resets()"></td>
			</tr>

		</table>

	</p:subBody>


	<p:title>
		<td class="caption"><strong>流程实例列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="20%"><strong>单号</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="15%"><strong>标题</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="15%"><strong>流程名称</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>当前环节</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="20%"><strong>截止时间</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>提交者</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"
					width="10%"><strong>操作</strong></td>
			</tr>

			<c:forEach items="${list}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)"><a
						href="../flow/flow.do?method=detailFlowInstance&id=${item.id}">${item.id}
						</a></td>
					<td align="center" onclick="hrefAndSelect(this)">
					${item.title} </td>
					<td align="center" onclick="hrefAndSelect(this)"><a
						href="../flow/flow.do?method=detailFlowDefine&id=${item.flowId}">
					${item.flowName} </a></td>
					<td align="center" onclick="hrefAndSelect(this)">${item.currentTokenName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.endTime}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.userName}</td>
					<td align="center" onclick="hrefAndSelect(this)">
					<c:if test="${queryType == '1' && item.status == 0}">
					<a title="修改流程实例"
						href="javascript:update('${item.id}')">
					<img src="../images/edit.gif" border="0" height="15" width="15"></a>
					<a title="删除流程实例"
						href="javascript:del('${item.id}')">
					<img src="../images/del.gif" border="0" height="15" width="15"></a>
					</c:if>
					<c:if test="${queryType == '2'}">
					<a title="处理流程"
						href="javascript:process('${item.id}')">
					<img src="../images/edit.gif" border="0" height="15" width="15"></a>
					</c:if>
					</td>
				</tr>
			</c:forEach>
		</table>

		<p:formTurning form="formEntry" method="queryFlowInstance"></p:formTurning>
	</p:subBody>

	<p:line flag="1" />

	<p:message />

</p:body></form>
</body>
</html>

