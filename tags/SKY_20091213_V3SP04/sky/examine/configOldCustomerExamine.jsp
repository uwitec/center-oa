<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="配置老客户考核" cal="true" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/pop.js"></script>
<script language="javascript">

function addBean(opr)
{
	submit('确定配置老客户考核?', null, null);
}

function load()
{
	loadForm();

	init();
}

function init()
{
	<c:if test="${readonly == 1}">
    setAllReadOnly();
    </c:if>
}

function refush()
{
	$l('../examine/examine.do?method=queryOldCustomerExamine&pid=${examine.id}');
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../examine/examine.do" method="post"><input
	type="hidden" name="method" value="configOldCustomerExamine"> <input type="hidden"
	name="pid" value="${examine.id}"> <p:navigation height="22">
	<td width="550" class="navigation">配置老客户考核</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>【${examine.stafferName}】老客户考核信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
        <table width="100%" align="center" cellspacing='1' class="table0"
            id="tables">
            <tr align="center" class="content0">
                <td width="5%" align="center">次序</td>
                <td width="20%" align="center">开始时间</td>
                <td width="20%" align="center">结束时间</td>
                <td align="center">客户数量</td>
            </tr>

            <c:forEach items="${oldList}" var="item" varStatus="vs">
                <tr class='${vs.index % 2 == 0 ? "content2" : "content1"}'>
                    <td width="5%" align="center">${item.step}</td>
                    <td width="20%">${item.beginTime}</td>
                    <td width="20%">${item.endTime}</td>
                    <td align="center"><input type="text"
                        style="width: 100%" value="${item.planValue}"
                        oncheck="isNumber" name="realValue"></td>
                </tr>
            </c:forEach>
        </table>
    </p:subBody>

	<p:line flag="1" />

    <c:if test="${readonly != 1}">
	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			name="adds" style="cursor: pointer"
			value="&nbsp;&nbsp;保 存&nbsp;&nbsp;" onclick="addBean(1)">&nbsp;&nbsp;
			<input type="button" class="button_class"
			name="adds" style="cursor: pointer"
			value="&nbsp;&nbsp;刷 新&nbsp;&nbsp;" onclick="refush()">
		</div>
	</p:button>
	</c:if>
</p:body></form>
<p:message></p:message>
</body>
</html>

