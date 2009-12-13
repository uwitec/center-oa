/**
 * File Name: DownFileAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-5<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.action;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.oa.flow.bean.FlowInstanceBean;
import com.china.center.oa.flow.dao.FlowInstanceDAO;
import com.china.center.oa.mail.bean.AttachmentBean;
import com.china.center.oa.mail.dao.AttachmentDAO;
import com.china.center.tools.FileTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.UtilStream;


/**
 * DownFileAction
 * 
 * @author zhuzhu
 * @version 2009-2-5
 * @see DownFileAction
 * @since 1.0
 */
public class DownFileAction extends DispatchAction
{
    private String mailAttchmentPath = "";

    private String flowAtt = "";

    private AttachmentDAO attachmentDAO = null;

    private FlowInstanceDAO flowInstanceDAO = null;

    /**
     * default constructor
     */
    public DownFileAction()
    {}

    /**
     * 下载模板
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public ActionForward downTemplateFile(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException
    {
        String path = servlet.getServletContext().getRealPath("/");

        path = FileTools.formatPath(path) + "template/template.xls";

        File file = new File(path);

        OutputStream out = response.getOutputStream();

        response.setContentType("application/x-dbf");

        response.setHeader("Content-Disposition", "attachment; filename=template.xls");

        UtilStream us = new UtilStream(new FileInputStream(file), out);

        us.copyAndCloseStream();

        return null;
    }

    /**
     * 获得下载的文件
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public ActionForward downProfitTemplateFile(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
        throws ServletException, IOException
    {
        String path = servlet.getServletContext().getRealPath("/");

        path = FileTools.formatPath(path) + "template/profitTemplate.xls";

        File file = new File(path);

        OutputStream out = response.getOutputStream();

        response.setContentType("application/x-dbf");

        response.setHeader("Content-Disposition", "attachment; filename=template.xls");

        UtilStream us = new UtilStream(new FileInputStream(file), out);

        us.copyAndCloseStream();

        return null;
    }

    /**
     * 获得下载的文件
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public ActionForward downTemplateFileByName(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
        throws ServletException, IOException
    {
        String fileName = request.getParameter("fileName");

        String path = servlet.getServletContext().getRealPath("/");

        path = FileTools.formatPath(path) + "template/" + fileName;

        File file = new File(path);

        OutputStream out = response.getOutputStream();

        response.setContentType("application/x-dbf");

        response.setHeader("Content-Disposition", "attachment; filename="
                                                  + StringTools.getStringBySet(fileName, "GBK",
                                                      "ISO8859-1"));

        UtilStream us = new UtilStream(new FileInputStream(file), out);

        us.copyAndCloseStream();

        return null;
    }

    /**
     * down mail attachment
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public ActionForward downMailAttachment(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response)
        throws ServletException, IOException
    {
        String id = request.getParameter("id");

        AttachmentBean attachment = attachmentDAO.find(id);

        if (attachment == null)
        {
            return null;
        }

        String path = FileTools.formatPath2(this.mailAttchmentPath) + attachment.getPath();

        File file = new File(path);

        OutputStream out = response.getOutputStream();

        response.setContentType("application/x-dbf");

        response.setHeader("Content-Disposition", "attachment; filename="
                                                  + StringTools.getStringBySet(
                                                      attachment.getName(), "GBK", "ISO8859-1"));

        UtilStream us = new UtilStream(new FileInputStream(file), out);

        us.copyAndCloseStream();

        return null;
    }

    /**
     * down mail attachment
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     * @throws IOException
     */
    public ActionForward downFlowInstanceAttachment(ActionMapping mapping, ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response)
        throws ServletException, IOException
    {
        String id = request.getParameter("id");

        FlowInstanceBean attachment = flowInstanceDAO.find(id);

        if (attachment == null)
        {
            return null;
        }

        String path = FileTools.formatPath2(this.flowAtt) + attachment.getAttachment();

        File file = new File(path);

        OutputStream out = response.getOutputStream();

        response.setContentType("application/x-dbf");

        response.setHeader("Content-Disposition",
            "attachment; filename="
                + StringTools.getStringBySet(attachment.getFileName(), "GBK", "ISO8859-1"));

        UtilStream us = new UtilStream(new FileInputStream(file), out);

        us.copyAndCloseStream();

        return null;
    }

    /**
     * @return the mailAttchmentPath
     */
    public String getMailAttchmentPath()
    {
        return mailAttchmentPath;
    }

    /**
     * @param mailAttchmentPath
     *            the mailAttchmentPath to set
     */
    public void setMailAttchmentPath(String mailAttchmentPath)
    {
        this.mailAttchmentPath = mailAttchmentPath;
    }

    /**
     * @return the attachmentDAO
     */
    public AttachmentDAO getAttachmentDAO()
    {
        return attachmentDAO;
    }

    /**
     * @param attachmentDAO
     *            the attachmentDAO to set
     */
    public void setAttachmentDAO(AttachmentDAO attachmentDAO)
    {
        this.attachmentDAO = attachmentDAO;
    }

    /**
     * @return the flowInstanceDAO
     */
    public FlowInstanceDAO getFlowInstanceDAO()
    {
        return flowInstanceDAO;
    }

    /**
     * @param flowInstanceDAO
     *            the flowInstanceDAO to set
     */
    public void setFlowInstanceDAO(FlowInstanceDAO flowInstanceDAO)
    {
        this.flowInstanceDAO = flowInstanceDAO;
    }

    /**
     * @return the flowAtt
     */
    public String getFlowAtt()
    {
        return flowAtt;
    }

    /**
     * @param flowAtt
     *            the flowAtt to set
     */
    public void setFlowAtt(String flowAtt)
    {
        this.flowAtt = flowAtt;
    }
}
