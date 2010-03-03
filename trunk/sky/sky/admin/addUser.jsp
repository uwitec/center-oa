<%@ page contentType="text/html;charset=GBK" language="java"
	errorPage="../common/error.jsp"%>
<%@include file="../common/common.jsp"%>
<html>
<head>
<p:link title="增加人员" />
<script language="JavaScript" src="../js/common.js"></script>
<script language="JavaScript" src="../js/cnchina.js"></script>
<script language="JavaScript" src="../js/public.js"></script>
<script language="JavaScript" src="../js/JCheck.js"></script>
<script language="JavaScript" src="../js/tree.js"></script>
<script language="JavaScript" src="../js/prototype.js"></script>
<script language="JavaScript" src="../js/buffalo.js"></script>
<script language="JavaScript" src="../js/json.js"></script>
<script language="javascript">
var END_POINT="${pageContext.request.contextPath}/bfapp";

var buffalo = new Buffalo(END_POINT);

function addBean()
{
	submit('确定增加人员?');
}

function goqueryUser()
{
    $l('../admin/queryUser.jsp');
}

function changes()
{
    var id = $$('locationId');
    
    buffalo.remoteCall("userManager.queryStafferAndRoleByLocationId",[id], function(reply) {
                var result = reply.getResult();
                
                var sList = result['stafferList'];
                
                removeAllItem($O('stafferId'));
                
                setOption($O('stafferId'), "", "--");
                
                for (var i = 0; i < sList.length; i++)
                {
                    setOption($O('stafferId'), sList[i].id,  sList[i].name);
                }
                
        });
}
var authList = JSON.parse('${authListJSON}');

var tv = new treeview("treeview","../js/tree",true,false);

var nmap = {};

function load()
{
    
    for (var i = 0; i < authList.length; i++)
    {
        var ele = authList[i];
        
        if (ele.bottomFlag == 0 && ele.level == 0)
        {
            var itemNode = snode(ele.name, true, ele.id);
            tv.add(itemNode);
            
            nmap[ele.id] = itemNode;
        }
        else
        {
            var parentNode = nmap[ele.parentId];
            
            if (parentNode == null)
            {
                continue;
            }
            
            var itemNode;
            if (ele.bottomFlag == 0)
            {
                itemNode = snode(ele.name, true, ele.id);
                
                nmap[ele.id] = itemNode;
                
                parentNode.add(itemNode);
            }
            else
            {
                itemNode = snode(ele.name, false, ele.id);
                
                parentNode.add(itemNode);
                
                nmap[ele.id] = itemNode;
            }
        }
    }
    
    tv.create(document.getElementById("tree"));
}

treeview.prototype.onnodecheck = function(sender)
{
    if (sender.checked)
    {
        diguiCheck(sender.parent.id);
    }
    
}

function diguiCheck(sid)
{
    var parentNode = nmap[sid];
    
    if (!parentNode)
    {
        return;
    }
    
    parentNode.checkNode.checked = true;
    
    if (!parentNode.parent || parentNode.parent.id == sid)
    {
        return;
    }
    
    return diguiCheck(parentNode.parent.id);
}

function allSelect(check)
{
    if (check)
    {
        tv.expandAll();
    }
    else
    {
        tv.collapseAll();
    }
}

</script>

</head>
<body class="body_class" onload="changes();load();">
<form name="addApply" action="../admin/user.do"><input
	type="hidden" name="method" value="addUser"> <p:navigation
	height="22">
	<td width="550" class="navigation"><span style="cursor: pointer;"
		onclick="goqueryUser()">人员管理</span> &gt;&gt; 增加人员</td>
	<td width="85"></td>
</p:navigation> <br>

<p:body width="100%">

	<p:title>
		<td class="caption"><strong>人员基本信息：</strong></td>
	</p:title>

	<p:line flag="0" />

	<p:subBody width="98%">
		<p:class value="com.china.center.oa.publics.bean.UserBean" />

		<p:table cells="1">
			<p:pro field="name" />

			<p:pro field="locationId" innerString="quick=true onchange=changes()">
				<c:forEach items="${locationList}" var="item">
					<option value="${item.id}">${item.name}</option>
				</c:forEach>
			</p:pro>

			<p:pro field="stafferId" innerString="quick=true">
				<option value="">--</option>
				<c:forEach items="${stafferList}" var="item">
					<option value="${item.id}">${item.name}</option>
				</c:forEach>
			</p:pro>
			
			<p:cell title="权限">
                <br>
                <span style="cursor: pointer;" onclick="allSelect(true)">全部展开</span> | <span
                    style="cursor: pointer;" onclick="allSelect(false)">全部收起</span>
                <br>
                <br>
                <div id=tree></div>
            </p:cell>

		</p:table>
	</p:subBody>

	<p:line flag="1" />

	<p:button leftWidth="100%" rightWidth="0%">
		<div align="right"><input type="button" class="button_class" id="ok_b"
			style="cursor: pointer" value="&nbsp;&nbsp;确 定&nbsp;&nbsp;"
			onclick="addBean()"></div>
	</p:button>

	<tr>
        <td colspan='2' align="center"><FONT color="blue">${MESSAGE_INFO}</FONT><FONT
            color="red">${errorInfo}</FONT></td>
    </tr>
</p:body></form>
</body>
</html>

