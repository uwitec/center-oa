<%@ page contentType="text/html;charset=GBK"%>

<%
    request.setCharacterEncoding("GBK");

	String content = request.getParameter("Content");
	
    String exNumber = request.getParameter("ExNumber");
    
    String phone = request.getParameter("MobilePhones");
    
    content = java.net.URLEncoder.encode(content, "GBK");
    
    response.sendRedirect("../sms/sms.do?method=receiveSMS&MobilePhones=" 
        + phone + "&ExNumber=" + exNumber + "&Content=" + content);
%>
