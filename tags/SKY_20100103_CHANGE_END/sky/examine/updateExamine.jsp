<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="增加考核" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/cnchina.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="javascript">
function addBean(flag)
{
    $O('forward').value = flag;
    
	submit('确定保存考核?');
}

</script>

</head>
<body class="body_class">
<form name="addApply" action="../examine/examine.do"><input
	type="hidden" name="method" value="updateExamine"> <input
    type="hidden" name="id" value="${bean.id}"><input
    type="hidden" name="parentId" value="${bean.parentId}"><input
    type="hidden" name="stafferId" value="${bean.stafferId}"> <input
    type="hidden" name="forward" value=""><p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">考核管理</span> &gt;&gt; 修改考核</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>考核基本信息：</strong></td>
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
            
            <p:pro field="parentId" cell="2" innerString="readonly=true" value="${bean.parentName}"></p:pro>

            <p:pro field="description" cell="0" innerString="rows=4 cols=60" />

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			id="ok_b" style="cursor: pointer" value="&nbsp;&nbsp;保 存&nbsp;&nbsp;"
			onclick="addBean(0)">&nbsp;&nbsp; <input type="button"
			class="button_class" id="ok_b1" style="cursor: pointer"
			value="&nbsp;&nbsp;保存并制定考核项&nbsp;&nbsp;" onclick="addBean(1)"></div>
	</p:button>

	<p:message />
</p:body></form>
</body>
</html>

