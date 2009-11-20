<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="增加会员消费" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<c:set var="dis" value="增加" />
<script language="javascript">
function addApplys()
{
	submit('确定${dis}会员消费');
}

function selectMember()
{
     window.common.modal("../member/member.do?method=rptQueryMember&firstLoad=1");
}

function selectProduct()
{
	//单选
	window.common.modal("../admin/product.do?method=rptInQueryProduct2&firstLoad=1&type=2");
}

function getProduct(oo)
{
	var obj = oo[0];
	
	$('productId').value = obj.value;
	$('productName').value = obj.productName;
}

function getMember(oo)
{
	var obj = oo[0];
	
	$('memberId').value = obj.value;
	$('member').value = obj.cardNo;
	$('memberName').value = obj.memberName;
	$('rebate').value = obj.rebate;
}

function load()
{
	loadForm();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../member/member.do" method="post"><input
	type="hidden" name="method" value="addConsume">
	<input type="hidden" name="productId" value="">
	<input type="hidden" name="memberId" value="">
 <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: hand"
		onclick="javascript:history.go(-1)">会员消费管理</span> &gt;&gt; ${dis}会员消费</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>会员消费信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.centet.yongyin.bean.ConsumeBean" />

		<p:table cells="2">
			<p:cell title="会员">
				<input type=text name="member" oncheck="notNone;" head="会员"
					readonly="readonly">
				<font color="#FF0000">*</font>&nbsp;&nbsp;<input type="button"
					value="&nbsp;...&nbsp;" name="qout" class="button_class"
					onclick="selectMember()">
			</p:cell>

			<p:pro field="productName" innerString="readonly=readonly">&nbsp;&nbsp;<input
					type="button" value="&nbsp;...&nbsp;" name="qout"
					class="button_class" onclick="selectProduct()">
			</p:pro>
			
			<p:cell title="客户">
				<input type=text name="memberName" readonly="readonly">
				<font color="#FF0000">*</font>
			</p:cell>

			<p:pro field="amount" value="1" />

			<p:pro field="price"  />


			<p:pro field="precost" />

			<p:pro field="rebate" />

			<p:pro field="cost" />

			<p:pro field="description" innerString="cols=80 rows=3" cell="2" />

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			name="adds" style="cursor: pointer"
			value="&nbsp;&nbsp;确 定&nbsp;&nbsp;" onclick="addApplys()">&nbsp;&nbsp;
		<input type="button" class="button_class"
			onclick="javascript:history.go(-1)"
			value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"></div>
	</p:button>

	<tr>
		<td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
			color="red">${errorInfo}</FONT></td>
	</tr>
</p:body></form>
</body>
</html>

