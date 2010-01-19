<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="增加流程实例" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="javascript">

function addBean()
{
	submit('确定增加流程实例?');
}

function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../flow/flow.do?method=addFlowInstance" 
method="post" enctype="multipart/form-data">
	<input type="hidden" name="flowId" value="${defineBean.id}">
	<input
	type="hidden" name="id" value="${bean.id}"> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">流程管理</span> &gt;&gt; 增加流程实例</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>流程实例信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.centet.yongyin.bean.FlowInstanceBean" />

		<p:table cells="1">
			<p:cells celspan="2" title="流程定义">
			${defineBean.name}
			</p:cells>

			<p:pro field="title" innerString="size=40" />

			<p:pro field="endTime"/>
			
			<p:cell title="附件"><input type="file" name="attchment" size="40" class="button_class" contenteditable="false">&nbsp;
			<font color="blue">建议使用压缩文件,减少网络流量</font></p:cell>

			<p:pro field="description"  innerString="cols=80 rows=3"/>

		</p:table>
	</p:subBody>

	<p:tr />

	<p:subBody width="100%">
		<table width="100%" border="0" cellspacing='1' id="tables">
			<tr align="center" class="content0">
				<td width="10%" align="center">顺序</td>
				<td width="30%" align="center">环节名称</td>
				<td width="30%" align="center">处理者</td>
				<td width="30%" align="center">处理方式</td>
			</tr>

			<c:forEach items="${defineBean.tokensVO}" var="item" varStatus="vs">
				<tr class='${vs.index % 2 == 0 ? "content1" : "content2"}'>
					<td align="center">${item.orders + 1}</td>

					<td align="center">${item.name}</td>

					<td align="center">${item.processerName}</td>

					<td align="center">${my:get('tokenType', item.type)}</td>
				</tr>
			</c:forEach>
		</table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right">
		<input type="button" class="button_class"
			name="adds" style="cursor: pointer"
			value="&nbsp;&nbsp;提 交&nbsp;&nbsp;" onclick="addBean()">&nbsp;&nbsp;
		<input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<p:message/>
</p:body></form>
</body>
</html>

