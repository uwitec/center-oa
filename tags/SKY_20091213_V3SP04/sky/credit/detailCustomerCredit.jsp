<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="配置客户静态指标" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="javascript">

</script>

</head>
<body class="body_class">
<form name="formEntry" action="../credit/customer.do">
<input type="hidden" name="cid" value="${bean.id}"> 
<p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">客户管理</span> &gt;&gt; 客户静态指标</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>客户静态指标：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<p:table cells="2">
		    
		    <p:cell title="客户名称">
		      ${bean.name}
		    </p:cell>  
		    
		    <p:cell title="客户编码">
              ${bean.code}
            </p:cell> 

			<c:forEach items="${itemList}" var="item">
				<p:cell title="${item.name}">
	            <select id="${item.id}" name="${item.id}" class="select_class" values="${valueMap[item.id]}" oncheck="notNone;" head="${item.name}" readonly=true>
	               <option value="">--</option>
	               <c:forEach items="${map[item.id]}" var="subItem">
	                   <option value="${subItem.id}">${subItem.name}</option>
	               </c:forEach>
	            </select>
	            </p:cell>  
			</c:forEach>
		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class" id="ok_b"
		 value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"
			onclick="javascript:history.go(-1)"></div>
	</p:button>
</p:body></form>
</body>
</html>

