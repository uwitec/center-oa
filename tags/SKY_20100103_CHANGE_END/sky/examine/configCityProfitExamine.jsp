<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="配置区域成交量考核" cal="true" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/pop.js"></script>
<script language="JavaScript" src="../js/json.js"></script>
<script language="javascript">

var listj = JSON.parse('${listJSON}');

var llistj = JSON.parse('${llistJSON}');

function addBean(opr)
{
	submit('确定绑定选择的区域到考核的职员?', null, null);
}

function load()
{
	loadForm();

	init();
}

function init()
{
	for (var i = 0 ; i < llistj.length; i++)
	{
	   var obj = $O('i' + llistj[i].cityId);
	   
	   if (containInList(llistj[i].cityId))
	   {
	       obj.checked = true;
	   }
	}
	
	<c:if test="${readonly == 1}">
    setAllReadOnly();
    </c:if>
}

function containInList(id)
{
    for(var i = 0; i < listj.length; i++)
    {
        if (listj[i] == id)
        {
            return true;
        }
    }
    
    return false;
}

function refush()
{
	$l('../examine/examine.do?method=queryCityProfitExamine&pid=${examine.id}');
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../examine/examine.do" method="post"><input
	type="hidden" name="method" value="configCityProfitExamine"> <input type="hidden"
	name="pid" value="${examine.id}"> <p:navigation height="22">
	<td width="550" class="navigation">配置区域成交量考核</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>【${examine.stafferName}】区域信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0"
			id="tables">
			<tr align="center" class="content0">
			    <td width="10%">区域</td> 
			    <td align="left">
				<c:forEach items="${llits}" var="item" varStatus="va">
				<input id="i${item.cityId}" type="checkbox" name="city" value="${item.cityId}">
				<a target="_blank" href='../examine/queryCityProfit.jsp?cityId=${item.cityId}' title="点击查询区域月利润">
				${item.cityName}</a>&nbsp;
				<c:if test="${(va.index + 1) % 5 == 0}"><br></c:if>
				</c:forEach>
				</td>
			</tr>
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

