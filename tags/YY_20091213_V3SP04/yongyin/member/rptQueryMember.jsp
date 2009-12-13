<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="./error.jsp"%>
<%@include file="./common.jsp"%>
<html>
<head>
<p:link title="会员列表" />
<base target="_self">
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/fanye.js"></script>
<script language="javascript">
function querys()
{
	formEntry.submit();
}

function sures()
{
	var opener = window.common.opener();
	
	var oo = getCheckBox("beans");
	if (oo.length == 0)
	{
		alert('请选择产品');
		return;
	}
	
    opener.getMember(oo);
    
    closes();
}

function closes()
{
	opener = null;
	window.close();
}

function press()
{
	if (window.common.getEvent().keyCode == 13)
	{
		querys();
	}
}


function resets()
{
	formEntry.reset();
	
	$('cardNo').value = '';
	$('name').value = '';
}

function load()
{
	loadForm();
	$f('cardNo');
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../member/member.do"><input
	type="hidden" name="method" value="rptQueryMember"><input type="hidden" value="1"
	name="firstLoad"> <p:navigation
	height="22">
	<td width="550" class="navigation">会员管理 &gt;&gt; 会员列表</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:subBody width="90%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr align=center class="content0">
				<td align="center" >会员卡号</td>
				<td align="center" width="35%"><input type="text" name="cardNo" value="${cardNo}" onkeypress="press()"></td>
				<td align="center" >会员名称</td>
				<td align="center" width="35%"><input type="text" name="name" value="${name}" onkeypress="press()"></td>
			</tr>
			
			<tr align=center class="content1">
				<td align="center" >会员等级</td>
				<td align="center" width="35%">
				<select name="grade" class="select_class" values="${grade}">
					<option value="">--</option>
					<option value="0">普通卡</option>
					<option value="1">银卡</option>
					<option value="2">金卡</option>
					<option value="3">铂金卡</option>
				</select>
				</td>
				<td align="center" >会员类型</td>
				<td align="center" width="35%">
				<select name="type" class="select_class" values="${type}">
					<option value="">--</option>
					<option value="0">普通会员</option>
					<option value="1">永久会员</option>
				</select>
				</td>
			</tr>
			
			<tr align=center class="content1">
				<td align="right" colspan="4"><input type="button" class="button_class"
				value="&nbsp;&nbsp;查 询&nbsp;&nbsp;" onclick="querys()">&nbsp;&nbsp;
				<input type="button" class="button_class"
				value="&nbsp;&nbsp;重 置&nbsp;&nbsp;" onclick="resets()">
				</td>
			</tr>
		</table>

	</p:subBody>
	

	<p:title>
		<td class="caption"><strong>会员列表：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="90%">
		<table width="100%" align="center" cellspacing='1' class="table0" id="result">
			<tr align=center class="content0">
				<td align="center" class="td_class">选择</td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>卡号</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>名称</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>等级</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>类型</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>积分</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this, true)"><strong>手机</strong></td>
			</tr>

			<c:forEach items="${listMember}" var="item" varStatus="vs">
				<tr class="${s.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center"><input type="radio" name="beans" cardNo="${item.cardNo}" memberName="${item.name}"
					rebate="${item.rebate}"	value="${item.id}" ${vs.index == 0 ? "checked" : ""}/></td>
					<td align="center" onclick="hrefAndSelect(this)"><a href="../member/member.do?method=findMember&id=${item.id}">${item.cardNo}</a></td>
					<td align="center" onclick="hrefAndSelect(this)">${item.name}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:get('memberGrade' ,item.grade)}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:get('memberType' ,item.type)}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.point}</td>
					<td align="center" onclick="hrefAndSelect(this)">${item.handphone}</td>
				</tr>
			</c:forEach>
		</table>
		
		<p:formTurning form="formEntry" method="rptQueryMember"></p:formTurning>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class" name="adds"
			style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="sures()"></div>
	</p:button>

	<p:message />
	
</p:body></form>
</body>
</html>

