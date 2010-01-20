/**
 * File Name: ProctExceptionAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-2-16<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.action;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.FileTools;
import com.china.center.tools.SequenceTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.center.tools.UtilStream;
import com.china.centet.yongyin.Helper;
import com.china.centet.yongyin.bean.ProctExceptionBean;
import com.china.centet.yongyin.bean.Role;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.dao.ProctExceptionDAO;


/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2008-2-16
 * @see
 * @since
 */
public class ProctExceptionAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private String attchmentPath = "";

    private ProctExceptionDAO proctExceptionDAO = null;

    /**
     * 增加货物异常
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addProctException(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws Exception
    {
        FileItemFactory factory = new DiskFileItemFactory();
        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        // upload.setSizeMax(limitSize);
        upload.setSizeMax( -1);

        List<FileItem> fileItems = upload.parseRequest(request);

        Map<String, String> map = new HashMap<String, String>();

        InputStream in = null;

        String fileName = "";

        long size = 0;
        for (FileItem fileItem : fileItems)
        {
            if (fileItem.isFormField())
            {
                map.put(fileItem.getFieldName(), StringTools.getStringBySet(fileItem.getString(),
                    "ISo8859-1", "GBK"));
            }
            else
            {
                in = fileItem.getInputStream();

                size = fileItem.getSize();

                fileName = fileItem.getName();
            }
        }

        ProctExceptionBean bean = new ProctExceptionBean();

        BeanUtil.getBean(bean, map);

        bean.setLocationId(Helper.getCurrentLocationId(request));

        bean.setLogDate(TimeTools.now());

        if (size != 0)
        {
            // 建立路径
            FileTools.mkdirs(this.attchmentPath);

            fileName = FileTools.getFileName(fileName);
            String attachment = mkdir();
            bean.setAttachment(attachment + '/' + fileName);

            bean.setFileName(fileName);

            File file = new File(this.attchmentPath + '/' + attachment + '/' + fileName);

            FileOutputStream out = new FileOutputStream(file);

            UtilStream us = new UtilStream(in, out);

            us.copyAndCloseStream();
        }

        bean.setId(SequenceTools.getSequence("PE", 5));

        // 提交
        bean.setStatus(Constant.PRO_EXCEPTION_SUBMIT);

        bean.setCreater(Helper.getUser(request).getId());

        proctExceptionDAO.addProctException(bean);

        request.setAttribute(KeyConstant.MESSAGE, "成功增加货物异常单据:" + bean.getId());

        request.setAttribute("init", "1");

        return queryProductException(mapping, form, request, reponse);
    }

    /**
     * 查询货物异常
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryProductException(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condition = new ConditionParse();

        setCondition(request, condition);

        List<ProctExceptionBean> list = proctExceptionDAO.queryProctExceptionByCondition(condition);

        request.setAttribute("proctExceptionList", list);

        return mapping.findForward("queryProctException");
    }

    /**
     * 修改货物异常
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward updateProctException(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        ProctExceptionBean bean = proctExceptionDAO.findProctExceptionById(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "货物异常单不存在");

            return mapping.findForward("error");
        }

        BeanUtil.getBean(bean, request);

        // 提交
        bean.setStatus(Constant.PRO_EXCEPTION_SUBMIT);

        proctExceptionDAO.updateProctException(bean);

        request.setAttribute(KeyConstant.MESSAGE, "成功修改货物异常单据:" + bean.getId());

        request.setAttribute("init", "1");

        return queryProductException(mapping, form, request, reponse);
    }

    /**
     * 修改货物异常的状态
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward updateProctExceptionStatus(ActionMapping mapping, ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        String opr = request.getParameter("opr");

        String apply = request.getParameter("apply");

        ProctExceptionBean bean = proctExceptionDAO.findProctExceptionById(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "货物异常单不存在");

            return mapping.findForward("error");
        }

        // 驳回
        if ("0".equals(opr))
        {
            if (bean.getStatus() != Constant.PRO_EXCEPTION_SUBMIT)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "货物异常单状态错误");

                return mapping.findForward("error");
            }

            proctExceptionDAO.updateProctExceptionStatus(id, Constant.PRO_EXCEPTION_REJECT, apply);
        }

        // 通过
        if ("1".equals(opr))
        {
            if (bean.getStatus() != Constant.PRO_EXCEPTION_SUBMIT)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "货物异常单状态错误");

                return mapping.findForward("error");
            }

            proctExceptionDAO.updateProctExceptionStatus(id, Constant.PRO_EXCEPTION_PASS, apply);
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功修改货物异常状态.单据:" + bean.getId());

        request.setAttribute("init", "1");

        return queryProductException(mapping, form, request, reponse);
    }

    /**
     * 删除货物异常
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward delProctException(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        ProctExceptionBean bean = proctExceptionDAO.findProctExceptionById(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "货物异常单不存在");

            return mapping.findForward("error");
        }

        proctExceptionDAO.delProctException(id);

        // 删除文件
        File file = new File(this.attchmentPath + '/' + bean.getAttachment());

        file.delete();

        request.setAttribute(KeyConstant.MESSAGE, "成功删除货物异常单据:" + bean.getId());

        request.setAttribute("init", "1");

        return queryProductException(mapping, form, request, reponse);
    }

    /**
     * 查询货物异常
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward findProductException(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        String update = request.getParameter("update");

        ProctExceptionBean pe = proctExceptionDAO.findProctExceptionById(id);

        if (pe == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "货物异常单不存在");

            return mapping.findForward("error");
        }

        request.setAttribute("bean", pe);

        if ("1".equals(update))
        {
            return mapping.findForward("updateProductException");
        }

        return mapping.findForward("detailProductException");
    }

    private void setCondition(HttpServletRequest request, ConditionParse condition)
    {
        condition.addCondition("locationId", "=", Helper.getCurrentLocationId(request));

        String init = request.getParameter("init");

        if (StringTools.isNullOrNone(init))
        {
            init = (String)request.getAttribute("init");
        }

        // 如果不是manager，知能看到自己的异常单据
        if (Helper.getUser(request).getRole() != Role.MANAGER)
        {
            condition.addCondition("creater", "=", Helper.getUser(request).getId());
        }

        // 从菜单的入口
        if ("1".equals(init))
        {
            Calendar cal = Calendar.getInstance();

            cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 7);

            String now = TimeTools.getStringByFormat(new Date(), "yyyy-MM-dd");

            String old = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()), "yyyy-MM-dd");

            condition.addCondition("logDate", ">=", old + " 00:00:00");
            request.setAttribute("beginDate", old);

            condition.addCondition("logDate", "<=", now + " 23:59:59");
            request.setAttribute("endDate", now);

            // 如果是manager，默认查询提交的
            if (Helper.getUser(request).getRole() == Role.MANAGER)
            {
                condition.addIntCondition("status", "=", Constant.PRO_EXCEPTION_SUBMIT);
                request.setAttribute("status", Constant.PRO_EXCEPTION_SUBMIT);
            }
        }
        else
        {
            String beginDate = request.getParameter("beginDate");

            if ( !StringTools.isNullOrNone(beginDate))
            {
                condition.addCondition("logDate", ">=", beginDate + " 00:00:00");
            }

            String endDate = request.getParameter("endDate");

            if ( !StringTools.isNullOrNone(endDate))
            {
                condition.addCondition("logDate", "<=", endDate + " 23:59:59");
            }
        }

        String productName = request.getParameter("productName");

        if ( !StringTools.isNullOrNone(productName))
        {
            condition.addCondition("productName", "like", productName);
        }

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(status))
        {
            condition.addIntCondition("status", "=", status);
        }
    }

    /**
     * 查询货物异常
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward downProductException(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse reponse)
        throws Exception
    {
        OutputStream out = reponse.getOutputStream();

        String id = request.getParameter("id");

        ProctExceptionBean pe = proctExceptionDAO.findProctExceptionById(id);

        if (pe == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "货物异常单不存在");

            return mapping.findForward("error");
        }

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename="
                                                 + StringTools.getStringBySet(pe.getFileName(),
                                                     "GBK", "ISO8859-1"));

        FileInputStream in = new FileInputStream(new File(this.attchmentPath + '/'
                                                          + pe.getAttachment()));

        UtilStream us = new UtilStream(in, out);

        us.copyAndCloseStream();

        return null;
    }

    private String mkdir()
    {
        String path = TimeTools.now("yyyy/MM/dd/HH/mm/") + SequenceTools.getSequence();

        FileTools.mkdirs(this.attchmentPath + '/' + path);

        return path;
    }

    /**
     * @return the _logger
     */
    public Log get_logger()
    {
        return _logger;
    }

    /**
     * @return the attchmentPath
     */
    public String getAttchmentPath()
    {
        return attchmentPath;
    }

    /**
     * @param attchmentPath
     *            the attchmentPath to set
     */
    public void setAttchmentPath(String attchmentPath)
    {
        this.attchmentPath = attchmentPath;
    }

    /**
     * @return the proctExceptionDAO
     */
    public ProctExceptionDAO getProctExceptionDAO()
    {
        return proctExceptionDAO;
    }

    /**
     * @param proctExceptionDAO
     *            the proctExceptionDAO to set
     */
    public void setProctExceptionDAO(ProctExceptionDAO proctExceptionDAO)
    {
        this.proctExceptionDAO = proctExceptionDAO;
    }
}
