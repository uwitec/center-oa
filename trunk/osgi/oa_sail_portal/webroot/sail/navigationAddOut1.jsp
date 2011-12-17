<%@ page contentType="text/html;charset=UTF-8" language="java"
    errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="经营费用" cal="true" guid="true"/>
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/tableSort.js"></script>
<script language="javascript">

function load()
{
	loadForm();
	
	bingTable("senfe");
}

function query()
{
	submit(null, null, null);
}

function resetAll()
{
   formEntry.reset();
}

</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../sail/out.do" method="post">
<input type="hidden" name="method" value="queryShow"> 
<input type="hidden" value="1" name="firstLoad">
<p:navigation
	height="22">
	<td width="550" class="navigation">增加销售单向导--开票设置</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0">
			<tr class="content1">
				<td width="15%" align="center">是否开票</td>
				<td align="left">
				<select name="finType" values="${ssmap.finType}" class="select_class" oncheck="notNone">
                    <option value="0">不开票</option>
                    <option value="1">开票</option>
                </select>
				<font color="#FF0000">*</font>
				</td>
				<td width="15%" align="center">税点(‰)</td>
                <td align="left">
                <input type="text" name="ratio" style="width: 80%" value="${ssmap.ratio}" oncheck="notNone;isNumber">
                <font color="#FF0000">*</font>
                </td>
			</tr>

			<tr class="content2">
			    <td width="15%" align="center">销售类型:</td>
                <td align="left" colspan="1"><select name="sailType"
                    class="select_class" values="${ssmap.sailType}" >
                    <p:option type="productSailType"/>
                </select></td>
                
               <td width="15%" align="center">销售品类:</td>
                <td align="left" colspan="1"><select name="productType"
                    class="select_class" values="${ssmap.productType}" >
                    <p:option type="productType"/>
                </select></td>
            </tr>
            
            <tr class="content1">
               <td width="15%" align="center">纳税实体</td>
                        <td align="left">
                        <select name="duty" style="width: 80%"
                            class="select_class" values=${ssmap.duty} >
                            <c:forEach items='${dutyList}' var="item">
                                <option value="${item.id}">${item.name}(${my:get('dutyType', item.type)})</option>
                            </c:forEach>
                        </select>
                </td>
                
                <td width="15%" align="center"></td>
                <td align="left" colspan="1"></td>
            </tr>

			<tr class="content2">
				<td colspan="4" align="right"><input type="button" onclick="query()"
					class="button_class" value="&nbsp;&nbsp;查 询&nbsp;&nbsp;">&nbsp;&nbsp;<input
					type="button" class="button_class" onclick="resetAll()"
					value="&nbsp;&nbsp;重 置&nbsp;&nbsp;"></td>
			</tr>
		</table>

	</p:subBody>

	<p:title>
		<td class="caption"><strong>开票品名：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<table width="100%" align="center" cellspacing='1' class="table0" id="senfe">
			<tr align=center class="content0">
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>品名</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>销售类型</strong></td>
				<td align="center" class="td_class" onclick="tableSort(this)"><strong>销售品类</strong></td>
			</tr>

			<c:forEach items="${g_showList}" var="item"
				varStatus="vs">
				<tr class="${vs.index % 2 == 0 ? 'content1' : 'content2'}">
					<td align="center" onclick="hrefAndSelect(this)">${item.showName}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:get('productSailType', item.sailType)}</td>
					<td align="center" onclick="hrefAndSelect(this)">${my:get('productType', item.productType)}</td>
				</tr>
			</c:forEach>
		</table>

	</p:subBody>

	<p:line flag="1" />

    <p:button leftWidth="100%" rightWidth="0%">
        <div align="right"><input type="button" class="button_class" id="ok_b"
            style="cursor: pointer" value="&nbsp;&nbsp;下一步&nbsp;&nbsp;"
            onclick="addBean()"></div>
    </p:button>

    <p:message2/>
	
</p:body>
</form>
</body>
</html>

