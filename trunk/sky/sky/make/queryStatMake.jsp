<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="定制产品统计" />
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/tableSort.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/cnchina.js"></script>
<script language="javascript">
function querys()
{
    if (formCheck())
    {
	   formEntry.submit();
	}
}

function resets()
{
	formEntry.reset();
}

function load()
{
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../worklog/worklog.do"><input
	type="hidden" name="method" value="queryStatWorkLog">
<p:navigation
	height="22">
	<td width="550" class="navigation">定制产品 &gt;&gt; 统计分析</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0">
		    <tr align=center class="content0">
                <td align="center">开始时间</td>
                <td align="center" width="35%"><p:plugin name="beginDate" oncheck="notNone" innerString="head=开始时间"
                    value="${beginDate}" /></td>
                <td align="center">结束时间</td>
                <td align="center" width="35%"><p:plugin name="endDate"
                    value="${endDate}" /></td>
            </tr>
            
			<tr align=center class="content1">
				<td align="center" >异常原因</td>
				<td align="center" width="35%">
				<select name="exceptionReason"
                    class="select_class" values="${exceptionReason}">
                    <option value="">--</option>
                    <option value="0">正常</option>
                    <option value="1">创意未过</option>
                    <option value="2">设计未过</option>
                    <option value="3">客户需求变化</option>
                    <option value="4">工艺问题</option>
                    <option value="5">创交货时间不足</option>
                    <option value="6">付款和运输条件不满足</option>
                    <option value="99">其他</option>
                </select>
				</td>
				
				<td align="center" >职员</td>
				<td align="center" width="35%">
				<select name="stafferId"
                    class="select_class" values="${stafferId}" quick="true">
                    <option value="">--</option>
                    <c:forEach items="${stafferList}" var="item">
                    <option value="${item.id}">${item.name}</option>
                    </c:forEach>
                </select>
				</td>
			</tr>
			
			<tr align=center class="content0">
				<td align="right" colspan="4"><input type="button" class="button_class"
				value="&nbsp;&nbsp;查 询&nbsp;&nbsp;" onclick="querys()">&nbsp;&nbsp;
				<input type="button" class="button_class"
				value="&nbsp;&nbsp;重 置&nbsp;&nbsp;" onclick="resets()">
				</td>
			</tr>
		</table>

	</p:subBody>


	<p:title>
		<td class="caption"><strong>分析列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0" id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)" width="25%"><strong>职员</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)" width="25%"><strong>日志总量</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)" width="25%"><strong>延期日志</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)" width="25%"><strong>异常率</strong></td>
			</tr>

			<c:forEach items="${statWorkLogList}" var="item" varStatus="vs">
				<tr class="${s.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)">${item.stafferName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.total}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.exceptionNum}</td>
					<td align="center" onclick="hrefAndSelect(this)"><font color='${item.exceptionNum > 0 ? "red" : ""}'>${item.exRatio}</font></td>
				</tr>
			</c:forEach>
		</table>

	</p:subBody>

	<p:message />

</p:body></form>
</body>
</html>

