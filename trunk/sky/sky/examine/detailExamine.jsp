<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="考评明细" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="javascript">
function load()
{
    setAllReadOnly();
}

function queryItem()
{
    $l('../examine/examine.do?method=configExamineItem&id=${bean.id}&readonly=1');
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry"><input
    type="hidden" name="id" value="${bean.id}"> <input
    type="hidden" name="forward" value=""><p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">考评管理</span> &gt;&gt; 考评信息</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>考评基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.center.oa.examine.bean.ExamineBean" opr="1"/>

		<p:table cells="2">
            <p:pro field="name" cell="2" innerString="size=60" />
            
            <p:pro field="attType" innerString="readonly=true">
                <option value="">--</option>
                <p:option type="attType"/>
            </p:pro>
            
            <p:pro field="locationId" innerString="quick=true readonly=true">
                <option value="">--</option>
                <c:forEach items="${locationList}" var="item">
                    <option value="${item.id}">${item.name}</option>
                </c:forEach>
            </p:pro>

            <p:pro field="stafferId" innerString="readonly=true" value="${bean.stafferName}"></p:pro>

            <p:pro field="year" innerString="quick=true readonly=true">
                <option value="">--</option>
                <c:forEach begin="2008" end="2100" var="item">
                    <option value="${item}">${item}</option>
                </c:forEach>
            </p:pro>
            
            <p:pro field="parentId" cell="1" innerString="readonly=true" value="${bean.parentName}"></p:pro>
            
            <p:cell title="制定人">${bean.createrName}</p:cell>

            <p:pro field="description" cell="0" innerString="rows=4 cols=60" />

        </p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button"
			class="button_class" id="ok_b1" style="cursor: pointer"
			value="&nbsp;&nbsp;查看考核项&nbsp;&nbsp;" onclick="queryItem()"></div>
	</p:button>

	<p:message />
</p:body></form>
</body>
</html>

