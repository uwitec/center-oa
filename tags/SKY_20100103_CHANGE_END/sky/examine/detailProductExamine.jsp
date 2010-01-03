<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="产品考核" guid="false" cal="true"/>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="javascript">

function addBean()
{
	$l('../examine/product.do?method=queryProductExamineItem&pid=${bean.id}')
}

function load()
{
    setAllReadOnly();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../examine/product.do"><input
	type="hidden" name="method" value="queryProductExamineItem"><input
    type="hidden" name="pid" value="${bean.id}"> 
   <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">考核管理</span> &gt;&gt; 产品考核明细</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>产品考核基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.center.oa.examine.bean.ProductExamineBean" opr="1" />

		<p:table cells="2">
			<p:pro field="name" cell="2" innerString="size=60" />
			
            <p:pro field="locationId" innerString="quick=true onchange=cchange()">
                <option value="">--</option>
                <c:forEach items="${locationList}" var="item">
                    <option value="${item.id}">${item.name}</option>
                </c:forEach>
            </p:pro>
			
			<p:pro field="productId" cell="1" value="${bean.productName}"></p:pro>
                            
            <p:pro field="beginTime"></p:pro>        
                 
            <p:pro field="endTime"></p:pro>             

			<p:pro field="description" cell="0" innerString="rows=4 cols=60" />

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button"
			class="button_class" id="ok_b1" style="cursor: pointer"
			value="&nbsp;&nbsp;查看产品考核项&nbsp;&nbsp;" onclick="addBean()"></div>
	</p:button>

	<p:message />
</p:body></form>
</body>
</html>

