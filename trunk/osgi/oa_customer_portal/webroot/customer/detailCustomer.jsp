<%@ page contentType="text/html;charset=UTF-8" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="客户明细" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/cnchina.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/key.js"></script>
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/buffalo.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="javascript">
var END_POINT="${pageContext.request.contextPath}/bfapp";

var buffalo = new Buffalo(END_POINT);

var cmap = window.top.topFrame.cmap;

var pList = window.top.topFrame.pList;

var updateCode = '${updateCode}';

function load()
{
    setOption($O('provinceId'), "", "--");
    for (var i = 0; i < pList.length; i++)
    {
        setOption($O('provinceId'), pList[i].id, pList[i].name);
    }
    
    changes($O('cityId'));
    
    loadForm();
    
    changes($O('cityId'));
    
    loadForm();
    
    changeArea();
    
    setAllReadOnly();
    
    if (updateCode == '1')
    {
        $r('code', false);
        
        $O('code').value = '';
    }
    
    //testclip();
}

function changes(obj)
{
    removeAllItem($O('cityId'));
    setOption($O('cityId'), "", "--");
    if ($$('provinceId') == "")
    {
        return;
    }
    
    var cityList = cmap[$$('provinceId')];
    for (var i = 0; i < cityList.length; i++)
    {
        setOption($O('cityId'), cityList[i].id, cityList[i].name);
    }
}

function changeArea()
{
    var id = $$('cityId');
    
    if (id == "")
    {
        return;
    }
    
    buffalo.remoteCall("commonManager.queryAreaByParentId",[id], function(reply) {
                var result = reply.getResult();
                
                var sList = result;
                
                removeAllItem($O('areaId'));
                
                setOption($O('areaId'), "", "--");
                
                for (var i = 0; i < sList.length; i++)
                {
                    setOption($O('areaId'), sList[i].id,  sList[i].name);
                }
                
                loadForm();
        });
}

function subCode()
{
    submit('确定提交客户编码?');
}

var boocopy = true;

function copy()
{
   return false;
}

function testclip()
{
  if (!boocopy)
  {
      return;
  }
  
  try   
  {   
      if (clipboardData)
      {
          clipboardData.setData("Text","");  
      }
  }  
  catch(e)
  {
  }  
   
  setTimeout("testclip()", 50)   
}
</script>

</head>
<body class="body_class" onload="load()">
<form name="addApply" action="../customer/customer.do"><input
	type="hidden" name="method" value="assignApplyCustomerCode">
<input type="hidden" name="id" value="${bean.id}">
<input type="hidden" name="createTime" value="${bean.createTime}">
	 <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="javascript:history.go(-1)">客户管理</span> &gt;&gt; 客户明细</td>
	<td width="85"></td>
</p:navigation> <br>
<p:body width="100%">

	<p:title>
		<td class="caption"><strong>客户基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
	
		<p:class value="com.china.center.oa.customer.bean.CustomerBean" opr="2"/>

		<p:table cells="2">
			<p:pro field="name" cell="2" innerString="size=60"/>
			
			<p:pro field="code" cell="2" innerString="size=60"/>

            <p:pro field="connector" />

            <p:pro field="sex">
                <option value="0">男</option>
                <option value="1">女</option>
            </p:pro>


            <p:pro field="provinceId"
                innerString="quick=true onchange=changes(this)" />
           <p:pro field="cityId" innerString="quick=true onchange=changeArea(this)" />
            
            <p:pro field="areaId" innerString="quick=true" cell="2"/>

            <p:pro field="selltype" innerString="quick=true">
                <p:option type="101"></p:option>
            </p:pro>
            <p:pro field="protype" innerString="quick=true">
                <p:option type="102"></p:option>
            </p:pro>

            <p:pro field="newtype" innerString="quick=true">
                <p:option type="103"></p:option>
            </p:pro>
            <p:pro field="qqtype" innerString="quick=true">
                <p:option type="104"></p:option>
            </p:pro>

            <p:pro field="rtype" innerString="quick=true">
                <p:option type="105"></p:option>
            </p:pro>
            <p:pro field="formtype" innerString="quick=true">
                <p:option type="106"></p:option>
            </p:pro>

            <p:pro field="mtype" innerString="quick=true">
                <p:option type="107"></p:option>
            </p:pro>
            <p:pro field="htype" innerString="quick=true">
                <p:option type="108"></p:option>
            </p:pro>
            
            <p:pro field="blog" innerString="quick=true">
                <p:option type="blog"></p:option>
            </p:pro>
            <p:pro field="card" innerString="quick=true">
                <p:option type="card"></p:option>
            </p:pro>
            
            <p:pro field="hlocal" cell="2">
                <option value="">--</option>
                <c:forEach items="${sybList}" var="item">
                <option value="${item.id}">${item.name}</option>
                </c:forEach>
            </p:pro>
            
            <p:pro field="beginConnectTime" />
            <p:pro field="post" />


            <p:pro field="company" cell="2" innerString="size=60" />
            <p:pro field="cdepartement" cell="2" innerString="size=60" />
            <p:pro field="address" cell="2" innerString="size=60" />

            <p:pro field="handphone" cell="2" innerString="size=60"/>

            <p:pro field="tel" cell="2" innerString="size=60"/>
            <p:pro field="mail" cell="2" innerString="size=60"/>
            
            <p:pro field="qq" />
            <p:pro field="msn" />
            
            <p:pro field="web" cell="2" innerString="size=60" />

            <p:pro field="fax" />
            <p:pro field="postcode" />

            <p:pro field="bank" />
            <p:pro field="accounts" />


            <p:pro field="dutycode" />
            <p:pro field="flowcom" />
            
            <p:pro field="birthday" cell="2"/>

            <p:pro field="description" cell="0" innerString="rows=4 cols=60" />
            
            <p:pro field="assignPer1" />
            <p:pro field="assignPer2" />
            <p:pro field="assignPer3" />
            <p:pro field="assignPer4" />
            
		</p:table>
	</p:subBody>

	<p:line flag="1" />
	
	<p:button leftWidth="100%" rightWidth="0%">
        <div align="right">
        <input type="button" class="button_class"
            style="cursor: pointer" value="&nbsp;&nbsp;返 回&nbsp;&nbsp;"
            onclick="javascript:history.go(-1)"></div>
    </p:button>
    
</p:body></form>
</body>
</html>

