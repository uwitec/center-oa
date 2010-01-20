<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="配置利润考核" cal="false" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="javascript">

function addBean(opr)
{
	submit('确定配置所有子考核的利润考核?', null, null);
}

function load()
{
	loadForm();

	init();
}

function init()
{
	
}

function refush()
{
	$l('../examine/examine.do?method=queryAllSubProfitExamine&pid=${parentBean.id}');
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../examine/examine.do" method="post"><input
	type="hidden" name="method" value="configAllProfitExamine"> <input type="hidden"
	name="pid" value="${parentBean.id}"> 
	<c:forEach items="${subs}" var="item">
        <input type="hidden" name="subItemId" value="${item.id}"> 
    </c:forEach>
	<p:navigation height="22">
	<td width="550" class="navigation">配置利润考核</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>【${parentBean.stafferName}的所有子考核】利润考核信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="tables">
			<tr align="center" class="content0">
				<td width="5%" align="left">次序</td>
                <td width="15%" align="left">开始时间</td>
                <td width="15%" align=left>结束时间</td>
                <c:forEach items="${subs}" var="item">
                <td align="left">${item.stafferName}</td>
                </c:forEach>
                <td align="left">状态</td>
			</tr>

			<c:forEach items="${defaultList}" var="item" varStatus="vs">
				<tr class='${vs.index % 2 == 0 ? "content2" : "content1"}'>
                    <td width="5%">${item.step}</td>
                    <td width="15%">${item.beginTime}</td>
                    <td width="15%">${item.endTime}</td>
                    <c:forEach items="${subs}" var="subItem">
                    <c:set var="tte" value="${subItem.id}" />
                    <td align="left">
                    
                    <c:if test="${subMap[tte][vs.index].status != 0}">
                    <font color="${subMap[subItem.id][vs.index].realValue >= subMap[subItem.id][vs.index].planValue ? 'blue' : 'red'}">
                    ${my:formatNum(subMap[subItem.id][vs.index].realValue)}/${my:formatNum(subMap[subItem.id][vs.index].planValue)}
                    </font>
                    </c:if>
                    
                    <c:if test="${subMap[tte][vs.index].status == 0}">
                    ${my:formatNum(subMap[subItem.id][vs.index].realValue)}/${my:formatNum(subMap[subItem.id][vs.index].planValue)}
                    </c:if>
                    
                    </td>
                    
                    </c:forEach>
                    <td>${my:get("examineItemStatus", subMap[tte][vs.index].status)}</td>
                </tr>
			</c:forEach>
		</table>
	</p:subBody>

	<p:line flag="1" />

</p:body></form>
<p:message></p:message>
</body>
</html>

