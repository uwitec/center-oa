<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<base target="_self">
<p:link title="日志列表" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/tableSort.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="JavaScript" src="../js/jquery/jquery.js"></script>
<script language="JavaScript" src="../js/plugin/highlight/jquery.highlight.js"></script>
<script language="javascript">

function closes()
{
	opener = null;
	window.close();
}

function load()
{
	loadForm();
	
	highlights(document.getElementsByTagName('form')[0], ['通过'], 'blue');
	
	highlights(document.getElementsByTagName('form')[0], ['驳回'], 'red');
}

function esc_back()
{
    closesd();
}

function closesd()
{
    var opener = window.common.opener();
    
    opener = null;
    window.close();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry"  method="post"> <p:navigation
	height="22">
	<td width="550" class="navigation">日志管理</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">
	<p:title>
		<td class="caption"><strong>日志列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="90%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="result">
			<tr align=center class="content0">
				<td align="center" onclick="tableSort(this)" class="td_class" title="点击排序">
				<strong>时间</strong></td>
				<td align="center" onclick="tableSort(this)"><strong>操作</strong></td>
				<td align="center" onclick="tableSort(this)"><strong>环节</strong></td>
				<td align="center" onclick="tableSort(this)"><strong>审批人</strong></td>
				<td align="center" onclick="tableSort(this)"><strong>日志</strong></td>
			</tr>

			<c:forEach items="${beanList}" var="item" varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center">${item.logTime}</td>
					<td align="center">${item.operation}</td>
					<td align="center">${item.position}</td>
					<td align="center">${item.stafferName}</td>
					<td align="center">${item.log}</td>
				</tr>
			</c:forEach>
		</table>
			
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="95%" rightWidth="5%">
		<div align="right">
		<input type="button" class="button_class"
            value="&nbsp;&nbsp;关 闭&nbsp;&nbsp;" onClick="closesd()" id="clo">
		</div>
	</p:button>

	<p:message />

</p:body></form>
</body>
</html>

