<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html>
<head>
<p:link title="定制产品" cal="true"/>
<link rel="stylesheet" href="../js/plugin/accordion/accordion.css" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/jquery/jquery.js"></script>
<script src="../js/plugin/accordion/jquery.accordion.js"></script>
<script language="JavaScript" src="../js/plugin/dialog/jquery.dialog.js"></script>
<link rel="stylesheet" type="text/css" href="../js/plugin/dialog/css/dialog.css"/>
<script language="JavaScript" src="../js/oa/make/02.js"></script>
<script language="javascript">

jQuery().ready(function(){
    
    jQuery('#flowDiv').accordion({
        autoheight: false
    });
    
    var accordions = jQuery('#flowDiv');
    
    accordions.accordion("activate", 3);
    
});

var makePosition = ${make.position};

var role = '${position.role}';

var makeid = '${make.id}';

var baseURL = '${eurl}';
</script>

</head>
<body class="body_class" onload="load()">
<form name="formEntry" action="../make/make.do" method="post"><input
	type="hidden" name="method" value="passMake">
<input type="hidden" name="cid" value=""> 
<input type="hidden" name="rejectTokenId" value=""> 
<input type="hidden" name="id" value="${make.id}"> 

<div class="basic" id="flowDiv">

<div id="title_div">
<a><font color=blue>【第${make.status}环${token.name}】--${position.name}(${position.ends == 1 ? "<font color=red>结束环</font>" : "中间环"})</font></a>
</div>

        <p:class value="com.china.center.oa.customize.make.bean.Make01Bean" opr="1"/>

        
        <div id="base_div">
        <a><font>定制产品信息</font></a>
        <table width='100% border=' 0' cellpadding='0' cellspacing='0'
        class='table1'>
        <tr>
        <td>
        <p:table cells="1">
            
            <p:cell title="标题">
             ${make.title}
            </p:cell>  
            
            <p:cell title="类型">
              <select class="select_class" name="type" values="${make.type}">
                  <p:option type="makeType"></p:option> 
              </select>
            </p:cell> 

            <p:pro field="cname" innerString="size=40">
                <input type="button" value="&nbsp;...&nbsp;" name="qout" id="qout"
                    class="button_class" onclick="selectCus()">&nbsp;&nbsp; 
            </p:pro>

            <p:pro field="description" cell="0" innerString="rows=3 cols=55" />
            
            <p:pro field="endTime"/>
            <p:pro field="flowTypeName"/>
            
            <p:pro field="endTime2"/>
            <p:pro field="flowTypeName2"/>
            
            <p:pro field="amount"/>
            
            <p:pro field="charact"/>
            
            <p:pro field="cdes" innerString="rows=5 cols=55 oncheck='minLength(100)'" outString="内容不能少于50字" />
            
            <p:pro field="sampleType">
                <p:option type="sampleType"></p:option>
            </p:pro>
            
            <p:pro field="billType">
                <p:option type="mbillType"></p:option>
            </p:pro>
            
            <p:pro field="customerType">
                <p:option type="mcustomerType"></p:option>
            </p:pro>
            
            <p:pro field="certificate" innerString="size=60"/>
            
            <p:pro field="request">
                <p:option type="requestType"></p:option> 
            </p:pro>
            
            <p:pro field="appraisalType">
                <p:option type="appraisalType"></p:option>
            </p:pro>
            
            <p:pro field="gujiaType" innerString="style='width:240px'">
                <p:option type="gujiaType"></p:option>
            </p:pro>
            
            <p:pro field="appType">
                <option value="">--</option>
                <p:option type="mappType"></p:option>
            </p:pro>

        </p:table>
        </td>
        </tr>
        </table>
        
        </div>
        
        <div id="attchment_div">
            <a><font color=red>附件查看</font></a>
            <table width='100%' border='0' cellpadding='0' cellspacing='0'
            class='table1'>
            <tr>
            <td>
            <p:table cells="1">
                <p:cell title="附件查看">
            <c:forEach items="${makeFileLWrapList_All}" var="item" varStatus="vs">
            【第${item.tokenId}环节】${item.name}：<input type="button" value="&nbsp;在线查看&nbsp;" name="log_g${vs.index}" id="log_g${vs.index}"
                   class="button_class" onclick="viewTemplate('${item.path}')"><br> 
               </c:forEach>
            </p:cell> 
    
            </p:table>
            </td>
            </tr>
            </table>
        </div>
        
        <div id="apply_div">
            <a><font color=red>我的处理</font></a>
            <table width='100%' border='0' cellpadding='0' cellspacing='0'
            class='table1'>
            <tr>
            <td>
            <p:table cells="1">
                <p:cell title="附件处理">
            <c:forEach items="${makeFileLWrapList}" var="item" varStatus="vs">
            <c:if test="${item.edit == true}">
               ${item.name}：<input type="button" value="&nbsp;在线编辑&nbsp;" name="log_g${vs.index}" id="log_g${vs.index}"
                   class="button_class" onclick="editTemplate('${item.path}')">&nbsp;&nbsp; 
            </c:if>
            
            <c:if test="${item.edit ==  false}">
                  ${item.name}：<input type="button" value="&nbsp;在线查看&nbsp;" name="log_g${vs.index}" id="log_g${vs.index}"
                   class="button_class" onclick="viewTemplate('${item.path}')">&nbsp;&nbsp; 
               </c:if>

               </c:forEach>
            </p:cell> 
            
                <p:cell title="意见">
                  <textarea name="reason" rows=3 cols=55 oncheck="notNone;" head="意见"></textarea>  
                  <font color="red">*</font>
                </p:cell> 
                
               <c:if test="${listNext ==  false}">
                <p:cell title="提交到">
                  <input name="handerName" id="handerName" readonly="readonly">&nbsp;&nbsp; 
                  <input type="hidden" name="handerId" value=""> 
                  <input type="button" value="&nbsp;...&nbsp;" name="qout1" id="qout1"
                        class="button_class" onclick="selectNext()">&nbsp;&nbsp; 
                  <font color="red">*</font>
                  <input type="button" value="&nbsp;审批日志&nbsp;" name="log_b" id="log_b"
                        class="button_class" onclick="queryLog()">
                </p:cell> 
            </c:if>
            
            <c:if test="${listNext ==  true}">
                <p:cell title="提交到">
                  <select class="select_class" name="handerId">
                  <option value="">--</option>
                  <c:forEach items="${stafferList}" var="item">
                  <option value="${item.id}">${item.name}</option>
                  </c:forEach>
                  </select>
                        &nbsp;&nbsp; 
                  <font color="red">*</font>
                  <input type="button" value="&nbsp;审批日志&nbsp;" name="log_b" id="log_b"
                        class="button_class" onclick="queryLog()">
                </p:cell> 
            </c:if>
                 
                 <p:tr>
                <input type="button" class="button_class" id="ok_b" name="ok_b"
         value="&nbsp;&nbsp;通 过&nbsp;&nbsp;" accesskey="O"
            onclick="addBean()">&nbsp;&nbsp;
        <c:if test="${position.ends != 1}">
        <input type="button" class="button_class" id="reject_b" name="reject_b"
         value="&nbsp;&nbsp;驳 回&nbsp;&nbsp;" accesskey="R"
            onclick="rejectMake()">&nbsp;&nbsp;
        </c:if>
        <c:if test="${position.ends == 1}">
        <input type="button" class="button_class" id="reject_b" name="reject_b"
         value="&nbsp;&nbsp;环节驳回&nbsp;&nbsp;" accesskey="E"
            onclick="rejectTokenMake()">
        </c:if>
                 </p:tr>
    
            </p:table>
            </td>
            </tr>
            </table>
        </div>
        
</div>

<div id="dlg1" title="选择驳回到的历史环节" style="width:320px;">
    <div style="padding:20px;height:200px;" id="dialog_inner" title="">
       
    </div>
</div>
</form>

</body>
</html>

