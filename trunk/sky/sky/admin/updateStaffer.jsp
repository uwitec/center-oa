<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="修改人员" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/cnchina.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="javascript">
function updateBean()
{
	submit('确定修改人员?');
}

//选择职位
function selectPrin()
{
    window.common.modal('../admin/org.do?method=popOrg');
}

function setOrgFromPop(id, name)
{
    $O('principalshipId').value = id;
    
    $O('principalshipName').value = name;
}


</script>

</head>
<body class="body_class">
<form name="addApply" action="../admin/staffer.do"><input
	type="hidden" name="method" value="updateStaffer"><input
	type="hidden" name="principalshipId" value="${bean.principalshipId}"> <input
	type="hidden" name="id" value="${bean.id}"> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">人员管理</span> &gt;&gt; 修改人员</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="98%">

	<p:title>
		<td class="caption"><strong>人员基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="100%">
		<p:class value="com.china.center.oa.publics.bean.StafferBean" />

		<p:table cells="2">
			<p:pro field="name" value="${bean.name}" innerString="readonly=true" />

			<p:pro field="sex" value="${bean.sex}">
				<option value="0">男</option>
				<option value="1">女</option>
			</p:pro>
			
			<p:pro field="code" value="${bean.code}" innerString="readonly=true"/>
            <p:pro field="examType" value="${bean.examType}">
                <p:option type="examType"></p:option>
            </p:pro>


			<p:pro field="locationId" innerString="quick=true"
				value="${bean.locationId}">
				<c:forEach items="${locationList}" var="item">
					<option value="${item.id}">${item.name}</option>
				</c:forEach>
			</p:pro>
			<p:pro field="departmentId" innerString="quick=true"
				value="${bean.departmentId}">
				<option value="">--</option>
				<c:forEach items="${depList}" var="item">
					<option value="${item.id}">${item.name}</option>
				</c:forEach>
			</p:pro>

			<p:pro field="postId" innerString="quick=true" value="${bean.postId}">
				<option value="">--</option>
				<c:forEach items="${postList}" var="item">
					<option value="${item.id}">${item.name}</option>
				</c:forEach>
			</p:pro>
			<p:pro field="principalshipId" value="${bean.principalshipName}"
				innerString="readonly=true">
				<input type="button" value="&nbsp;...&nbsp;" name="qout" id="qout"
					class="button_class" onclick="selectPrin()">&nbsp;&nbsp;
			</p:pro>

			<p:pro field="graduateSchool" value="${bean.graduateSchool}" />
			<p:pro field="graduateDate" value="${bean.graduateDate}" />

			<p:pro field="specialty" value="${bean.specialty}" />
			<p:pro field="graduate" value="${bean.graduate}" />

			<p:pro field="nation" value="${bean.nation}" />
			<p:pro field="city" value="${bean.city}" />

			<p:pro field="visage" value="${bean.visage}" />
			<p:pro field="idCard" value="${bean.idCard}" />


			<p:pro field="birthday" value="${bean.birthday}" />
			<p:pro field="handphone" value="${bean.handphone}" />

			<p:pro field="subphone" value="${bean.subphone}" />
			<p:pro field="credit" value="${bean.credit}" />

			<p:pro field="address" cell="0" innerString="size=80"
				value="${bean.address}" />

			<p:pro field="description" cell="0" innerString="rows=4 cols=60"
				value="${bean.description}" />

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class"
			style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="updateBean()"></div>
	</p:button>

	<p:message />
</p:body></form>
</body>
</html>

