/**
 * File Name: MakeAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-11<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.action;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.common.json.AjaxResult;
import com.china.center.oa.constant.MakeConstant;
import com.china.center.oa.customize.make.bean.FileAliasBean;
import com.china.center.oa.customize.make.bean.Make01Bean;
import com.china.center.oa.customize.make.bean.MakeBean;
import com.china.center.oa.customize.make.bean.MakeFileBean;
import com.china.center.oa.customize.make.bean.MakeTokenBean;
import com.china.center.oa.customize.make.bean.MakeTokenItemBean;
import com.china.center.oa.customize.make.dao.FileAliasDAO;
import com.china.center.oa.customize.make.dao.Make01DAO;
import com.china.center.oa.customize.make.dao.MakeDAO;
import com.china.center.oa.customize.make.dao.MakeFileDAO;
import com.china.center.oa.customize.make.dao.MakeTokenItemDAO;
import com.china.center.oa.customize.make.helper.MakeHelper;
import com.china.center.oa.customize.make.manager.MakeManager;
import com.china.center.oa.customize.make.vo.MakeVO;
import com.china.center.oa.customize.make.wrap.MakeFileWrap;
import com.china.center.oa.group.dao.GroupVSStafferDAO;
import com.china.center.oa.group.vs.GroupVSStafferBean;
import com.china.center.oa.helper.Helper;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.bean.SystemTemplateFileBean;
import com.china.center.oa.publics.dao.LogDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.SystemTemplateFileDAO;
import com.china.center.oa.publics.vo.StafferVO;
import com.china.center.tools.ActionTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.FileTools;
import com.china.center.tools.HTTPTools;
import com.china.center.tools.JSONTools;
import com.china.center.tools.TimeTools;
import com.china.center.tools.UtilStream;


/**
 * MakeAction
 * 
 * @author ZHUZHU
 * @version 2009-10-11
 * @see MakeAction
 * @since 1.0
 */
public class MakeAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private MakeManager makeManager = null;

    private MakeTokenItemDAO makeTokenItemDAO = null;

    private SystemTemplateFileDAO systemTemplateFileDAO = null;

    private MakeDAO makeDAO = null;

    private FileAliasDAO fileAliasDAO = null;

    private LogDAO logDAO = null;

    private StafferDAO stafferDAO = null;

    private GroupVSStafferDAO groupVSStafferDAO = null;

    private Make01DAO make01DAO = null;

    private MakeFileDAO makeFileDAO = null;

    /**
     * defined the edit server name
     */
    private String editServerName = "webdav";

    private String systemAtt = "";

    /**
     * 文件跟目录
     */
    private String makeAttachmentRoot = "";

    private static String QUERYSELFMAKE = "querySelfMake";

    private static String QUERYALLMAKE = "queryAllMake";

    private static String QUERYMAKETEMPLATE = "queryMakeTemplate";

    private static String QUERYHANDLERMAKE = "queryHandlerMake";

    /**
     * addMake
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addMake(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        Make01Bean bean = new Make01Bean();

        MakeBean make = new MakeBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            make.setCreaterId(user.getStafferId());

            make.setTitle(request.getParameter("title"));

            make.setType(CommonTools.parseInt(request.getParameter("type")));

            make.setHanderId(request.getParameter("handerId"));

            makeManager.addMake(user, bean, make);

            request.setAttribute(KeyConstant.MESSAGE, "成功申请定制产品");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "申请失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("querySelfMake");
    }

    /**
     * queryLog
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryHistoryToken(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        List<MakeTokenBean> tokens = null;

        try
        {
            tokens = makeManager.queryHistoryToken(id);

            ajax.setSuccess(tokens);
        }
        catch (MYException e)
        {
            ajax.setError(e.getErrorContent());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * updateMake01
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateMake01(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        Make01Bean bean = new Make01Bean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            makeManager.updateMake01AndSubmit(user, bean, request.getParameter("handerId"));

            request.setAttribute(KeyConstant.MESSAGE, "成功申请定制产品");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "申请失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("querySelfMake");
    }

    /**
     * findMakeTemplate
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findMakeTemplate(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        SystemTemplateFileBean template = systemTemplateFileDAO.find(id);

        request.setAttribute("template", template);

        String eurl = HTTPTools.getHTTPURL(request) + this.editServerName + "/"
                      + request.getSession().getId();

        request.setAttribute("eurl", eurl + template.getPath());

        return mapping.findForward("detailMakeTemplate");
    }

    /**
     * findMake
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findMake(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String makeId = request.getParameter("id");

        String update = request.getParameter("update");

        String hanlerMode = request.getParameter("hanlerMode");

        MakeVO make = makeDAO.findVO(makeId);

        if (make == null)
        {
            return ActionTools.toError("请重新操作", "queryHandlerMake", mapping, request);
        }

        request.setAttribute("make", make);

        // handler update
        if ("1".equals(update))
        {
            Make01Bean item = make01DAO.findVO(make.getId());

            MakeTokenItemBean position = makeTokenItemDAO.find(make.getPosition());

            if (position == null)
            {
                return ActionTools.toError("请重新操作", "queryHandlerMake", mapping, request);
            }

            request.setAttribute("position", position);

            request.setAttribute("bean", item);

            return mapping.findForward(MakeHelper.findForwardUpdate(make.getStatus()));
        }

        // handle and find
        Make01Bean item = make01DAO.findVO(make.getId());

        MakeTokenItemBean position = makeTokenItemDAO.find(make.getPosition());

        if (position == null)
        {
            return ActionTools.toError("请重新操作", "queryHandlerMake", mapping, request);
        }

        request.setAttribute("position", position);

        request.setAttribute("bean", item);

        request.setAttribute("rbean", item);

        // query template
        List<SystemTemplateFileBean> templateList = systemTemplateFileDAO.queryEntityBeansByFK(position.getId());

        try
        {
            setUrl(request);

            handleCustomize(request, make, templateList);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getMessage());

            return mapping.findForward("error");
        }
        catch (IOException e)
        {
            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getMessage());

            return mapping.findForward("error");
        }

        hanleAttachment(request, make, templateList);

        if ("1".equals(hanlerMode))
        {
            if (make.getStatus() == MakeConstant.STATUS_END)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "流程已经结束,不能处理");

                return mapping.findForward("queryHandlerMake");
            }

            // handle next process
            handleNext(request, make, position);

            return mapping.findForward(MakeHelper.findForwardHandler(make.getStatus()));
        }

        // query all attachment
        handleAllAttachment(request, make);

        return mapping.findForward("detailMake");
    }

    /**
     * handleNext
     * 
     * @param request
     * @param make
     * @param position
     */
    private void handleNext(HttpServletRequest request, MakeVO make, MakeTokenItemBean position)
    {
        boolean listNext = true;

        List<StafferBean> stafferList = new ArrayList();

        String role = position.getRole();

        if ("1".equals(role) || "0".equals(role))
        {
            stafferList.add(stafferDAO.find(make.getCreaterId()));
        }
        else
        {
            int count = groupVSStafferDAO.countByFK(role);

            // in 30,list all staffer
            if (count <= 30)
            {
                List<GroupVSStafferBean> vs = groupVSStafferDAO.queryEntityBeansByFK(role);

                for (GroupVSStafferBean groupVSStafferBean : vs)
                {
                    stafferList.add(stafferDAO.find(groupVSStafferBean.getStafferId()));
                }
            }
            else
            {
                listNext = false;
            }
        }

        request.setAttribute("stafferList", stafferList);

        request.setAttribute("listNext", listNext);
    }

    /**
     * handleAllAttachment
     * 
     * @param request
     * @param make
     */
    private void handleAllAttachment(HttpServletRequest request, MakeVO make)
    {
        List<MakeFileBean> allAttachment = makeFileDAO.queryEntityBeansByFK(make.getId());

        List<MakeFileWrap> makeFileLWrapList = new ArrayList();

        for (MakeFileBean makeFileBean : allAttachment)
        {
            MakeFileWrap wrap = new MakeFileWrap();

            BeanUtil.copyProperties(wrap, makeFileBean);

            wrap.setEdit(false);

            makeFileLWrapList.add(wrap);
        }

        request.setAttribute("makeFileLWrapList", makeFileLWrapList);
    }

    /**
     * hanleAttachment
     * 
     * @param request
     * @param make
     * @param templateList
     */
    private void hanleAttachment(HttpServletRequest request, MakeVO make,
                                 List<SystemTemplateFileBean> templateList)
    {
        request.setAttribute("templateList", templateList);

        // 定制处理 handleCustomizeAttachment

        List<MakeFileBean> makeFileList = makeFileDAO.queryByPidAndTokenId(make.getId(),
            make.getStatus());

        List<MakeFileWrap> makeFileLWrapList = new ArrayList();

        for (MakeFileBean makeFileBean : makeFileList)
        {
            MakeFileWrap wrap = new MakeFileWrap();

            BeanUtil.copyProperties(wrap, makeFileBean);

            // whether edit
            wrap.setEdit(isEdit(makeFileBean, templateList));

            makeFileLWrapList.add(wrap);
        }

        request.setAttribute("makeFileLWrapList", makeFileLWrapList);
    }

    /**
     * NOTE isEdit(如果有显示编辑定制也在这里面)
     * 
     * @param makeFileBean
     * @param templateList
     * @return
     */
    private boolean isEdit(MakeFileBean makeFileBean, List<SystemTemplateFileBean> templateList)
    {
        for (SystemTemplateFileBean systemTemplateFileBean : templateList)
        {
            if (systemTemplateFileBean.getType() == makeFileBean.getType())
            {
                return true;
            }
        }

        // alias
        FileAliasBean alias = fileAliasDAO.findByUnique(makeFileBean.getTokenItemId());

        if (alias != null)
        {
            if (alias.getType() == makeFileBean.getType())
            {
                return true;
            }
        }

        return false;
    }

    /**
     * handleCustomize
     * 
     * @param request
     * @param status
     */
    private void handleCustomize(HttpServletRequest request, MakeBean make,
                                 List<SystemTemplateFileBean> templates)
        throws MYException, IOException
    {
        // 特殊处理
        if (MakeConstant.MAKE_TOKEN_02 == make.getStatus())
        {
            // find template
            if (make.getPosition() == 22)
            {
                handle22(request, make, templates);
            }

            Make01Bean make01 = make01DAO.find(make.getId());

            request.setAttribute("bean", make01);

            return;
        }

        FileAliasBean alias = fileAliasDAO.findByUnique(make.getPosition());

        if (alias != null)
        {
            // handle alias token
            MakeFileBean aliasMakeFile = makeFileDAO.findByPidAndTokenIdAndType(make.getId(),
                make.getStatus(), alias.getType());

            if (aliasMakeFile != null)
            {
                return;
            }

            // copy alias
            aliasMakeFile = makeFileDAO.findByPidAndTokenIdAndType(make.getId(),
                alias.getAliasTokenId(), alias.getType());

            if (aliasMakeFile == null)
            {
                return;
            }

            MakeFileBean aliasNewMakeFileBean = new MakeFileBean();

            BeanUtil.copyProperties(aliasNewMakeFileBean, aliasMakeFile);

            aliasNewMakeFileBean.setId("");

            aliasNewMakeFileBean.setTokenId(make.getStatus());

            aliasNewMakeFileBean.setTokenItemId(make.getPosition());

            User user = Helper.getUser(request);

            makeManager.addMakeFile(user, aliasNewMakeFileBean);
        }

        // 正常处理
        for (SystemTemplateFileBean systemTemplateFileBean : templates)
        {
            handleCommonAttachment(request, make, systemTemplateFileBean);
        }
    }

    /**
     * handle22
     * 
     * @param id
     * @param position
     * @throws MYException
     * @throws IOException
     */
    private void handle22(HttpServletRequest request, MakeBean make,
                          List<SystemTemplateFileBean> templates)
        throws MYException, IOException
    {
        String id = make.getId();

        Make01Bean find = make01DAO.find(id);

        if (find == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        MakeFileBean makeFile = makeFileDAO.findByPidAndTokenIdAndType(make.getId(),
            make.getStatus(), MakeConstant.MAKE_FILE_TYPE_22);

        String fk = "M" + make.getPosition() + find.getAppType();

        // filter
        for (Iterator iterator = templates.iterator(); iterator.hasNext();)
        {
            SystemTemplateFileBean systemTemplateFileBean = (SystemTemplateFileBean)iterator.next();

            if ( !systemTemplateFileBean.getFk().equals(fk))
            {
                iterator.remove();
            }
        }

        // create attachment
        if (makeFile == null)
        {
            MakeFileBean newMakeFileBean = new MakeFileBean();

            SystemTemplateFileBean template = systemTemplateFileDAO.findByUnique(fk);

            if (template == null)
            {
                throw new MYException("估价模板不存在,请确认操作");
            }

            FileInputStream in = new FileInputStream(this.systemAtt + template.getPath());

            String savePath = mkdir(this.makeAttachmentRoot) + id + template.getFileName();

            OutputStream out = new FileOutputStream(savePath);

            UtilStream ustream = new UtilStream(in, out);

            // copy file to
            ustream.copyAndCloseStream();

            newMakeFileBean.setPid(id);

            newMakeFileBean.setName(template.getName());

            newMakeFileBean.setPath(savePath.substring(this.makeAttachmentRoot.length()));

            newMakeFileBean.setTokenId(make.getStatus());

            newMakeFileBean.setTokenItemId(make.getPosition());

            newMakeFileBean.setType(template.getType());

            User user = Helper.getUser(request);

            makeManager.addMakeFile(user, newMakeFileBean);
        }

    }

    /**
     * handleCommonAttachment
     * 
     * @param request
     * @param make
     * @param template
     * @throws MYException
     * @throws IOException
     */
    private void handleCommonAttachment(HttpServletRequest request, MakeBean make,
                                        SystemTemplateFileBean template)
        throws MYException, IOException
    {
        String id = make.getId();

        // do common attachment
        MakeFileBean makeFile = makeFileDAO.findByPidAndTokenIdAndType(make.getId(),
            make.getStatus(), template.getType());

        if (makeFile != null)
        {
            return;
        }

        // create attachment
        MakeFileBean newMakeFileBean = new MakeFileBean();

        FileInputStream in = new FileInputStream(this.systemAtt + template.getPath());

        String savePath = mkdir(this.makeAttachmentRoot) + id + template.getFileName();

        OutputStream out = new FileOutputStream(savePath);

        UtilStream ustream = new UtilStream(in, out);

        // copy file to
        ustream.copyAndCloseStream();

        newMakeFileBean.setPid(id);

        newMakeFileBean.setName(template.getName());

        newMakeFileBean.setPath(savePath.substring(this.makeAttachmentRoot.length()));

        newMakeFileBean.setTokenId(make.getStatus());

        newMakeFileBean.setTokenItemId(make.getPosition());

        newMakeFileBean.setType(template.getType());

        User user = Helper.getUser(request);

        makeManager.addMakeFile(user, newMakeFileBean);
    }

    /**
     * @param request
     */
    private void setUrl(HttpServletRequest request)
    {
        String eurl = HTTPTools.getHTTPURL(request) + this.editServerName + "/"
                      + request.getSession().getId() + "/make";

        request.setAttribute("eurl", eurl);
    }

    private String mkdir(String root)
    {
        String path = TimeTools.now("yyyy/MM/dd/HH/");

        FileTools.mkdirs(root + '/' + path);

        return root + '/' + path;
    }

    /**
     * 群组内职员查询
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryCreate(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        List<StafferVO> list = new ArrayList<StafferVO>();

        MakeBean find = makeDAO.find(id);

        if (find != null)
        {
            list.add(stafferDAO.findVO(find.getCreaterId()));
        }

        request.setAttribute("beanList", list);

        return mapping.findForward("rptQueryGroupMember");
    }

    /**
     * passMake
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward passMake(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String makeId = request.getParameter("id");

        String reason = request.getParameter("reason");

        String handerId = request.getParameter("handerId");

        try
        {
            User user = Helper.getUser(request);

            makeManager.pass(user, makeId, reason, handerId);

            request.setAttribute(KeyConstant.MESSAGE, "成功审批定制产品");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "审批申请失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryHandlerMake");
    }

    /**
     * rejectMake
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward rejectMake(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String makeId = request.getParameter("id");

        String reason = request.getParameter("reason");

        try
        {
            User user = Helper.getUser(request);

            makeManager.reject(user, makeId, reason);

            request.setAttribute(KeyConstant.MESSAGE, "成功驳回定制产品");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "驳回申请失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryHandlerMake");
    }

    /**
     * rejectTokenMake
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward rejectTokenMake(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String makeId = request.getParameter("id");

        String reason = request.getParameter("reason");

        String rejectTokenId = request.getParameter("rejectTokenId");

        try
        {
            User user = Helper.getUser(request);

            makeManager.rejectTokenMake(user, makeId, CommonTools.parseInt(rejectTokenId), reason);

            request.setAttribute(KeyConstant.MESSAGE, "成功驳回定制产品");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "驳回申请失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryHandlerMake");
    }

    /**
     * delMake
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward delMake(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            makeManager.delMake(user, id);

            ajax.setSuccess("成功删除定制产品申请");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除定制产品失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * querySelfMake
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward querySelfMake(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        condtion.addCondition("MakeBean.createrId", "=", user.getStafferId());

        ActionTools.processJSONQueryCondition(QUERYSELFMAKE, request, condtion);

        condtion.addCondition("order by MakeBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSELFMAKE, request, condtion,
            this.makeDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * querySelfMake
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryAllMake(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYALLMAKE, request, condtion);

        condtion.addCondition("order by MakeBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYALLMAKE, request, condtion,
            this.makeDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryMakeTemplate
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryMakeTemplate(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYMAKETEMPLATE, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYMAKETEMPLATE, request,
            condtion, this.systemTemplateFileDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * querySelfMake
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryHandlerMake(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        condtion.addCondition("MakeBean.handerId", "=", user.getStafferId());

        condtion.addIntCondition("MakeBean.status", "<", MakeConstant.STATUS_END);

        condtion.addIntCondition("MakeBean.position", ">", 11);

        ActionTools.processJSONQueryCondition(QUERYHANDLERMAKE, request, condtion);

        condtion.addCondition("order by MakeBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYHANDLERMAKE, request, condtion,
            this.makeDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * default constructor
     */
    public MakeAction()
    {}

    /**
     * @return the makeManager
     */
    public MakeManager getMakeManager()
    {
        return makeManager;
    }

    /**
     * @param makeManager
     *            the makeManager to set
     */
    public void setMakeManager(MakeManager makeManager)
    {
        this.makeManager = makeManager;
    }

    /**
     * @return the makeDAO
     */
    public MakeDAO getMakeDAO()
    {
        return makeDAO;
    }

    /**
     * @param makeDAO
     *            the makeDAO to set
     */
    public void setMakeDAO(MakeDAO makeDAO)
    {
        this.makeDAO = makeDAO;
    }

    /**
     * @return the makeTokenItemDAO
     */
    public MakeTokenItemDAO getMakeTokenItemDAO()
    {
        return makeTokenItemDAO;
    }

    /**
     * @param makeTokenItemDAO
     *            the makeTokenItemDAO to set
     */
    public void setMakeTokenItemDAO(MakeTokenItemDAO makeTokenItemDAO)
    {
        this.makeTokenItemDAO = makeTokenItemDAO;
    }

    /**
     * @return the logDAO
     */
    public LogDAO getLogDAO()
    {
        return logDAO;
    }

    /**
     * @param logDAO
     *            the logDAO to set
     */
    public void setLogDAO(LogDAO logDAO)
    {
        this.logDAO = logDAO;
    }

    /**
     * @return the systemTemplateFileDAO
     */
    public SystemTemplateFileDAO getSystemTemplateFileDAO()
    {
        return systemTemplateFileDAO;
    }

    /**
     * @param systemTemplateFileDAO
     *            the systemTemplateFileDAO to set
     */
    public void setSystemTemplateFileDAO(SystemTemplateFileDAO systemTemplateFileDAO)
    {
        this.systemTemplateFileDAO = systemTemplateFileDAO;
    }

    /**
     * @return the make01DAO
     */
    public Make01DAO getMake01DAO()
    {
        return make01DAO;
    }

    /**
     * @param make01DAO
     *            the make01DAO to set
     */
    public void setMake01DAO(Make01DAO make01DAO)
    {
        this.make01DAO = make01DAO;
    }

    /**
     * @return the makeAttachmentRoot
     */
    public String getMakeAttachmentRoot()
    {
        return makeAttachmentRoot;
    }

    /**
     * @param makeAttachmentRoot
     *            the makeAttachmentRoot to set
     */
    public void setMakeAttachmentRoot(String makeAttachmentRoot)
    {
        this.makeAttachmentRoot = makeAttachmentRoot;
    }

    /**
     * @return the stafferDAO
     */
    public StafferDAO getStafferDAO()
    {
        return stafferDAO;
    }

    /**
     * @param stafferDAO
     *            the stafferDAO to set
     */
    public void setStafferDAO(StafferDAO stafferDAO)
    {
        this.stafferDAO = stafferDAO;
    }

    /**
     * @return the editServerName
     */
    public String getEditServerName()
    {
        return editServerName;
    }

    /**
     * @param editServerName
     *            the editServerName to set
     */
    public void setEditServerName(String editServerName)
    {
        this.editServerName = editServerName;
    }

    /**
     * @return the makeFileDAO
     */
    public MakeFileDAO getMakeFileDAO()
    {
        return makeFileDAO;
    }

    /**
     * @param makeFileDAO
     *            the makeFileDAO to set
     */
    public void setMakeFileDAO(MakeFileDAO makeFileDAO)
    {
        this.makeFileDAO = makeFileDAO;
    }

    /**
     * @return the fileAliasDAO
     */
    public FileAliasDAO getFileAliasDAO()
    {
        return fileAliasDAO;
    }

    /**
     * @param fileAliasDAO
     *            the fileAliasDAO to set
     */
    public void setFileAliasDAO(FileAliasDAO fileAliasDAO)
    {
        this.fileAliasDAO = fileAliasDAO;
    }

    /**
     * @return the groupVSStafferDAO
     */
    public GroupVSStafferDAO getGroupVSStafferDAO()
    {
        return groupVSStafferDAO;
    }

    /**
     * @param groupVSStafferDAO
     *            the groupVSStafferDAO to set
     */
    public void setGroupVSStafferDAO(GroupVSStafferDAO groupVSStafferDAO)
    {
        this.groupVSStafferDAO = groupVSStafferDAO;
    }

    /**
     * @return the systemAtt
     */
    public String getSystemAtt()
    {
        return systemAtt;
    }

    /**
     * @param systemAtt
     *            the systemAtt to set
     */
    public void setSystemAtt(String systemAtt)
    {
        this.systemAtt = systemAtt;
    }
}
