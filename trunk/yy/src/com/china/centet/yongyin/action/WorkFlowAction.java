/**
 *
 */
package com.china.centet.yongyin.action;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.china.center.common.OldPageSeparateTools;
import com.china.center.common.query.QueryTools;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.FileTools;
import com.china.center.tools.RequestDataStream;
import com.china.center.tools.SequenceTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.center.tools.UtilStream;
import com.china.centet.yongyin.Helper;
import com.china.centet.yongyin.bean.FlowDefineBean;
import com.china.centet.yongyin.bean.FlowInstanceBean;
import com.china.centet.yongyin.bean.FlowTokenBean;
import com.china.centet.yongyin.bean.FlowViewerBean;
import com.china.centet.yongyin.bean.Role;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.constant.FlowConstant;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.FlowBelongDAO;
import com.china.centet.yongyin.dao.FlowConsignDAO;
import com.china.centet.yongyin.dao.FlowDefineDAO;
import com.china.centet.yongyin.dao.FlowInstanceDAO;
import com.china.centet.yongyin.dao.FlowInstanceLogDAO;
import com.china.centet.yongyin.dao.FlowInstanceViewDAO;
import com.china.centet.yongyin.dao.FlowTokenDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.manager.WorkFlowManager;
import com.china.centet.yongyin.vo.FlowBelongBeanVO;
import com.china.centet.yongyin.vo.FlowDefineBeanVO;
import com.china.centet.yongyin.vo.FlowInstanceBeanVO;
import com.china.centet.yongyin.vo.FlowInstanceLogBeanVO;
import com.china.centet.yongyin.vo.FlowInstanceViewBeanVO;
import com.china.centet.yongyin.vo.FlowViewerBeanVO;


/**
 * 采购的的action
 * 
 * @author Administrator
 */
public class WorkFlowAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private WorkFlowManager workFlowManager = null;

    private FlowDefineDAO flowDefineDAO = null;

    private FlowInstanceDAO flowInstanceDAO = null;

    private FlowInstanceLogDAO flowInstanceLogDAO = null;

    private FlowTokenDAO flowTokenDAO = null;

    private FlowConsignDAO flowConsignDAO = null;

    private FlowBelongDAO flowBelongDAO = null;

    private UserDAO userDAO = null;

    private CommonDAO commonDAO = null;

    private FlowInstanceViewDAO flowInstanceViewDAO = null;

    private String attchmentPath = "";

    /**
     * 查询流程定义
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryFlowDefine(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        // User user = Helper.getUser(request);

        List<FlowDefineBeanVO> list = null;
        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                setCondition(request, condtion);

                int total = flowDefineDAO.countVOBycondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, "queryFlowDefine");

                list = flowDefineDAO.queryEntityVOsBycondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryFlowDefine");

                list = flowDefineDAO.queryEntityVOsBycondition(OldPageSeparateTools.getCondition(
                    request, "queryFlowDefine"), OldPageSeparateTools.getPageSeparate(request,
                    "queryFlowDefine"));
            }

            request.setAttribute("list", list);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("queryFlowDefine");
    }

    /**
     * 查询流程实例
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryFlowInstance(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String forward = (String)request.getAttribute("forward");

        String queryType = request.getParameter("queryType");

        if ( !StringTools.isNullOrNone(forward))
        {
            queryType = forward;
        }

        List<FlowInstanceBeanVO> list = new ArrayList<FlowInstanceBeanVO>();

        List<FlowDefineBean> defined = null;

        // 自己的任务
        if ("1".equals(queryType))
        {
            ActionForward act = querySelfInstanceInner(mapping, request, list);

            if (act != null)
            {
                return act;
            }

            defined = flowDefineDAO.queryUseFlow();
        }

        // 需要处理的任务
        if ("2".equals(queryType))
        {
            ActionForward act = queryProcessInstanceInner(mapping, request, list);

            if (act != null)
            {
                return act;
            }

            defined = flowDefineDAO.queryUseFlow();
        }

        // 处理历史
        if ("3".equals(queryType))
        {
            ActionForward act = queryProcessHisInstanceInner(mapping, request, list);

            if (act != null)
            {
                return act;
            }

            defined = flowDefineDAO.listEntityBeans();
        }

        // 我的查阅
        if ("4".equals(queryType))
        {
            ActionForward act = queryProcessViewerInstanceInner(mapping, request, list);

            if (act != null)
            {
                return act;
            }

            defined = flowDefineDAO.listEntityBeans();
        }

        for (FlowInstanceBeanVO flowInstanceBeanVO : list)
        {
            FlowTokenBean token = flowTokenDAO.findToken(flowInstanceBeanVO.getFlowId(),
                flowInstanceBeanVO.getStatus());

            if (token != null)
            {
                flowInstanceBeanVO.setCurrentTokenName(token.getName());
            }
        }

        request.setAttribute("list", list);

        request.setAttribute("queryType", queryType);

        request.setAttribute("defined", defined);

        return mapping.findForward("queryFlowInstance");
    }

    /**
     * @param mapping
     * @param request
     * @param condtion
     */
    private ActionForward querySelfInstanceInner(ActionMapping mapping,
                                                 HttpServletRequest request,
                                                 List<FlowInstanceBeanVO> list)
    {
        ConditionParse condtion = new ConditionParse();
        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                setConditionForInstance(request, condtion);

                int total = flowInstanceDAO.countVOBycondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

                OldPageSeparateTools.initPageSeparate(condtion, page, request,
                    "querySelfInstanceInner");

                list.addAll(flowInstanceDAO.queryEntityVOsBycondition(condtion, page));
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "querySelfInstanceInner");

                list.addAll(flowInstanceDAO.queryEntityVOsBycondition(
                    OldPageSeparateTools.getCondition(request, "querySelfInstanceInner"),
                    OldPageSeparateTools.getPageSeparate(request, "querySelfInstanceInner")));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return null;
    }

    /**
     * 需要处理的任务
     * 
     * @param mapping
     * @param request
     * @param condtion
     */
    private ActionForward queryProcessInstanceInner(ActionMapping mapping,
                                                    HttpServletRequest request,
                                                    List<FlowInstanceBeanVO> list)
    {
        List<FlowBelongBeanVO> belong = new ArrayList<FlowBelongBeanVO>();
        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = new ConditionParse();

                setConditionForProcessInstance(request, condtion);

                int total = flowBelongDAO.countVOBycondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

                OldPageSeparateTools.initPageSeparate(condtion, page, request,
                    "queryProcessInstanceInner");

                belong = flowBelongDAO.queryEntityVOsBycondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryProcessInstanceInner");

                belong = flowBelongDAO.queryEntityVOsBycondition(OldPageSeparateTools.getCondition(
                    request, "queryProcessInstanceInner"), OldPageSeparateTools.getPageSeparate(
                    request, "queryProcessInstanceInner"));
            }

            for (FlowBelongBeanVO flowBelongBean : belong)
            {
                list.add(flowInstanceDAO.findVO(flowBelongBean.getInstanceId()));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return null;
    }

    /**
     * 历史审批的任务
     * 
     * @param mapping
     * @param request
     * @param condtion
     */
    private ActionForward queryProcessHisInstanceInner(ActionMapping mapping,
                                                       HttpServletRequest request,
                                                       List<FlowInstanceBeanVO> list)
    {
        List<FlowInstanceLogBeanVO> belong = new ArrayList<FlowInstanceLogBeanVO>();
        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = new ConditionParse();

                setConditionForProcessHisInstance(request, condtion);

                int total = flowInstanceLogDAO.countVOBycondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

                OldPageSeparateTools.initPageSeparate(condtion, page, request,
                    "queryProcessHisInstanceInner");

                belong = flowInstanceLogDAO.queryEntityVOsBycondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryProcessHisInstanceInner");

                belong = flowInstanceLogDAO.queryEntityVOsBycondition(
                    OldPageSeparateTools.getCondition(request, "queryProcessHisInstanceInner"),
                    OldPageSeparateTools.getPageSeparate(request, "queryProcessHisInstanceInner"));
            }

            for (FlowInstanceLogBeanVO flowBelongBean : belong)
            {
                list.add(flowInstanceDAO.findVO(flowBelongBean.getInstanceId()));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return null;
    }

    /**
     * 我的查阅流程
     * 
     * @param mapping
     * @param request
     * @param condtion
     */
    private ActionForward queryProcessViewerInstanceInner(ActionMapping mapping,
                                                          HttpServletRequest request,
                                                          List<FlowInstanceBeanVO> list)
    {
        List<FlowInstanceViewBeanVO> belong = new ArrayList<FlowInstanceViewBeanVO>();
        try
        {
            ConditionParse condtion = null;

            if (OldPageSeparateTools.isFirstLoad(request))
            {
                condtion = new ConditionParse();

                setConditionForProcessViewerInstanceInner(request, condtion);
            }

            QueryTools.commonQueryVO("queryProcessViewerInstanceInner", request, belong, condtion,
                this.flowInstanceViewDAO);

            for (FlowInstanceViewBeanVO flowBelongBean : belong)
            {
                list.add(flowInstanceDAO.findVO(flowBelongBean.getInstanceId()));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return null;
    }

    /**
     * 增加流程定义
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addFlowDefine(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        FlowDefineBean bean = new FlowDefineBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            bean.setUserId(user.getId());

            bean.setLogTime(TimeTools.now());

            setFlowDefineBean(bean, request);

            bean.setUserId(user.getId());

            workFlowManager.addFlowDefine(bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加流程:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        return queryFlowDefine(mapping, form, request, reponse);
    }

    /**
     * 处理流程的查阅
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward processFlowViewer(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String flowId = request.getParameter("flowId");
        try
        {
            List<FlowViewerBean> viewers = new ArrayList<FlowViewerBean>();

            setFlowViewers(viewers, request);

            User user = Helper.getUser(request);

            workFlowManager.processFlowViewer(user, flowId, viewers);

            request.setAttribute(KeyConstant.MESSAGE, "成功处理流程查阅");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理流程查阅失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        return queryFlowDefine(mapping, form, request, reponse);
    }

    /**
     * 准备增加流程实例
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForApllyFlowInstance(ActionMapping mapping, ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        User user = Helper.getUser(request);

        if ( !workFlowManager.hasApplyFlow(id, user))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限使用此流程");

            request.setAttribute("forward", "1");

            return queryFlowDefine(mapping, form, request, reponse);
        }

        FlowDefineBeanVO vo = workFlowManager.findFlowDefineVO(id);

        request.setAttribute("defineBean", vo);

        CommonTools.removeParamers(request);

        return mapping.findForward("addFlowInstance");
    }

    /**
     * 准备修改流程
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForUpdateFlowInstance(ActionMapping mapping, ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        FlowInstanceBeanVO vo = workFlowManager.findFlowInstanceVO(id);

        if (vo == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + "流程实例不存在");

            return mapping.findForward("error");
        }

        if (vo.getStatus() != FlowConstant.FLOW_INSTANCE_BEGIN)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + "流程实例不能修改");

            return mapping.findForward("error");
        }

        FlowDefineBeanVO defin = workFlowManager.findFlowDefineVO(vo.getFlowId());

        request.setAttribute("defineBean", defin);

        request.setAttribute("bean", vo);

        return mapping.findForward("updateFlowInstance");
    }

    /**
     * 准备修改流程的查阅
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForProcessView(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        FlowDefineBeanVO vo = workFlowManager.findFlowDefineVO(id);

        if (vo == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + "流程实例不存在");

            return mapping.findForward("error");
        }

        List<FlowViewerBeanVO> view = workFlowManager.queryFlowVieweVOByFlowId(id);

        request.setAttribute("view", view);

        request.setAttribute("bean", vo);

        return mapping.findForward("processFlowInstanceView");
    }

    /**
     * 增加流程实例
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addFlowInstance(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = Helper.getUser(request);

        FlowInstanceBean bean = new FlowInstanceBean();

        try
        {
            ActionForward forward = parserFlowInstance(mapping, form, request, reponse, user,
                bean, null, true);

            if (forward != null)
            {
                return forward;
            }

            workFlowManager.addFlowInstance(user, bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加流程实例:" + bean.getTitle());
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        return queryFlowInstance(mapping, form, request, reponse);
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @param user
     * @param bean
     * @throws ServletException
     */
    private ActionForward parserFlowInstance(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse reponse, User user,
                                             FlowInstanceBean bean, Map<String, String> paraMap,
                                             boolean isFrist)
        throws ServletException
    {
        // 解析流
        RequestDataStream rds = new RequestDataStream(request);

        try
        {
            rds.parser();
        }
        catch (Exception e)
        {
            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加流程实例失败");

            request.setAttribute("forward", "1");

            return queryFlowInstance(mapping, form, request, reponse);
        }

        if (paraMap != null)
        {
            paraMap.put("delAccachment", rds.getParameter("delAccachment"));

            paraMap.putAll(rds.getParmterMap());
        }

        // 过程中更新附件不需要处理
        if (isFrist)
        {
            String id = rds.getParameter("flowId");

            if ( !workFlowManager.hasApplyFlow(id, user))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限使用此流程");

                request.setAttribute("forward", "1");

                return queryFlowDefine(mapping, form, request, reponse);
            }
        }

        BeanUtil.getBean(bean, rds.getParmterMap());

        bean.setUserId(user.getId());

        bean.setLogTime(TimeTools.now());

        bean.setLocationId(user.getLocationID());

        if (rds.haveStream())
        {
            FileOutputStream out = null;
            try
            {
                String[] allPath = getRootPath();

                String rootPath = allPath[0];

                String fileAlias = SequenceTools.getSequence() + "."
                                   + FileTools.getFilePostfix(rds.getUniqueFileName());

                String filePath = "/" + allPath[1] + "/" + fileAlias;

                out = new FileOutputStream(rootPath + "/" + fileAlias);

                bean.setAttachment(filePath);

                bean.setFileName(FileTools.getAbsoluteFileName(rds.getUniqueFileName()));

                UtilStream ustream = new UtilStream(rds.getUniqueInputStream(), out);

                ustream.copyAndCloseStream();
            }
            catch (IOException e1)
            {
                _logger.error(e1, e1);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加流程实例失败");

                request.setAttribute("forward", "1");

                return queryFlowDefine(mapping, form, request, reponse);
            }
        }

        rds.close();

        return null;
    }

    /**
     * @return
     */
    private String[] getRootPath()
    {
        String[] allPath = new String[2];

        String tempPath = "flow/" + TimeTools.now("yyyy/MM/dd/HH");

        String rootPath = FileTools.formatPath(this.attchmentPath) + tempPath;

        allPath[0] = rootPath;
        allPath[1] = tempPath;

        FileTools.mkdirs(rootPath);

        return allPath;
    }

    /**
     * 修改流程实例
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward updateFlowInstance(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = Helper.getUser(request);

        FlowInstanceBean bean = new FlowInstanceBean();

        Map<String, String> paraMap = new HashMap<String, String>();

        try
        {
            ActionForward forward = parserFlowInstance(mapping, form, request, reponse, user,
                bean, paraMap, true);

            if (forward != null)
            {
                return forward;
            }

            boolean delAccachment = "1".equals(paraMap.get("delAccachment"));

            FlowInstanceBean oldBean = flowInstanceDAO.find(bean.getId());

            if (oldBean == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "流程实例不存在");

                request.setAttribute("forward", "1");

                return queryFlowInstance(mapping, form, request, reponse);
            }

            // 继承原有的附件
            if (StringTools.isNullOrNone(bean.getAttachment()) && !delAccachment)
            {
                bean.setAttachment(oldBean.getAttachment());

                bean.setFileName(oldBean.getFileName());
            }

            workFlowManager.updateFlowInstance(user, bean, delAccachment);

            request.setAttribute(KeyConstant.MESSAGE, "成功修改流程实例:" + bean.getTitle());
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "修改失败:" + e.getMessage());
        }

        request.setAttribute("forward", "1");

        return queryFlowInstance(mapping, form, request, reponse);
    }

    /**
     * 下载附件
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward downFlowInstanceAttchment(ActionMapping mapping, ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse reponse)
        throws Exception
    {
        OutputStream out = reponse.getOutputStream();

        String id = request.getParameter("id");

        FlowInstanceBean bean = flowInstanceDAO.find(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "流程实例不存在");

            return mapping.findForward("error");
        }

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename="
                                                 + StringTools.getStringBySet(bean.getFileName(),
                                                     "GBK", "ISO8859-1"));

        FileInputStream in = new FileInputStream(new File(
            FileTools.formatPath2(this.attchmentPath) + bean.getAttachment()));

        UtilStream us = new UtilStream(in, out);

        us.copyAndCloseStream();

        return null;
    }

    /**
     * 删除流程定义
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward delFlowDefine(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        try
        {
            workFlowManager.delFlowDefine(id);

            request.setAttribute(KeyConstant.MESSAGE, "成功删除流程");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "删除失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        return queryFlowDefine(mapping, form, request, reponse);
    }

    /**
     * 删除流程实例
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward delFlowInstance(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        try
        {
            workFlowManager.delFlowInstance(id);

            request.setAttribute(KeyConstant.MESSAGE, "成功删除流程实例");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "删除失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        return queryFlowInstance(mapping, form, request, reponse);
    }

    /**
     * 准备处理流程
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForProcessFlowInstance(ActionMapping mapping, ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse reponse)
        throws ServletException
    {
        return detailFlowInstance(mapping, form, request, reponse);
    }

    /**
     * 处理流程
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward processFlowInstance(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse reponse)
        throws ServletException
    {
        User user = Helper.getUser(request);

        Map<String, String> paraMap = new HashMap<String, String>();

        FlowInstanceBean bean = new FlowInstanceBean();

        try
        {
            ActionForward forward = parserFlowInstance(mapping, form, request, reponse, user,
                bean, paraMap, false);

            if (forward != null)
            {
                return forward;
            }

            boolean delAccachment = "1".equals(paraMap.get("delAccachment"));

            // 流程实例的ID
            String instanceId = paraMap.get("id");

            // 更新附件
            if (delAccachment)
            {
                workFlowManager.updateFlowInstanceAttachment(user, instanceId, bean.getFileName(),
                    bean.getAttachment(), delAccachment);
            }

            String oprMode = paraMap.get("oprMode");

            String opinion = paraMap.get("opinion");

            if (String.valueOf(Constant.OPRMODE_PASS).equals(oprMode))
            {
                workFlowManager.passFlowInstance(user, instanceId, opinion);
            }
            else
            {
                workFlowManager.rejectFlowInstance(user, instanceId, opinion);
            }

            request.setAttribute(KeyConstant.MESSAGE, "成功处理流程");
        }
        catch (Exception e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "2");

        return queryFlowInstance(mapping, form, request, reponse);
    }

    /**
     * 放弃流程定义使用
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward dropFlowDefine(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        try
        {
            workFlowManager.dropFlowDefine(id);

            request.setAttribute(KeyConstant.MESSAGE, "成功放弃流程定义");
        }
        catch (MYException e)
        {
            _logger.warn(e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "放弃流程失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        return queryFlowDefine(mapping, form, request, reponse);
    }

    /**
     * 流程的详细
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward detailFlowDefine(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        FlowDefineBeanVO vo = workFlowManager.findFlowDefineVO(id);

        request.setAttribute("bean", vo);

        return mapping.findForward("detailFlowDefine");
    }

    /**
     * 流程实例的详细
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward detailFlowInstance(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        FlowInstanceBeanVO vo = workFlowManager.findFlowInstanceVO(id);

        FlowTokenBean token = flowTokenDAO.findToken(vo.getFlowId(), vo.getStatus());

        if (token != null)
        {
            vo.setCurrentTokenName(token.getName());
        }

        request.setAttribute("bean", vo);

        List<FlowBelongBeanVO> belong = flowBelongDAO.queryEntityVOsByFK(id);

        request.setAttribute("belong", belong);

        return mapping.findForward("detailFlowInstance");
    }

    /**
     * 收集数据
     * 
     * @param pbean
     * @param item
     * @param request
     */
    private void setFlowDefineBean(FlowDefineBean pbean, HttpServletRequest request)
    {
        String[] providers = request.getParameterValues("check_init");

        List<FlowTokenBean> item = new ArrayList<FlowTokenBean>();

        int orders = FlowConstant.FLOW_INSTANCE_BEGIN;

        for (int i = 0; i < providers.length; i++ )
        {
            if ( !StringTools.isNullOrNone(providers[i]))
            {
                FlowTokenBean bean = new FlowTokenBean();

                bean.setType(CommonTools.parseInt(request.getParameter("tokenType_" + providers[i])));

                bean.setProcesser(request.getParameter("processerId_" + providers[i]));

                bean.setName(request.getParameter("tokenName_" + providers[i]));

                bean.setPreOrders(orders - 1);

                bean.setOrders(orders);

                bean.setNextOrders(orders + 1);

                if (orders == FlowConstant.FLOW_INSTANCE_BEGIN)
                {
                    bean.setBegining(true);

                    bean.setEnding(false);
                }
                else
                {
                    bean.setBegining(false);

                    bean.setEnding(false);
                }

                orders++ ;

                item.add(bean);
            }
        }

        // 最后一个环节的下一个指向结束
        item.get(item.size() - 1).setNextOrders(FlowConstant.FLOW_INSTANCE_END);

        FlowTokenBean bean = new FlowTokenBean();

        bean.setName("结束");

        bean.setPreOrders(orders - 1);

        bean.setOrders(FlowConstant.FLOW_INSTANCE_END);

        bean.setNextOrders(FlowConstant.FLOW_INSTANCE_END + 1);

        bean.setBegining(false);

        bean.setEnding(true);

        bean.setType(FlowConstant.FLOW_TYPE_NONE);

        orders++ ;

        item.add(bean);

        pbean.setTokens(item);
    }

    /**
     * 收集数据viewers
     * 
     * @param pbean
     * @param item
     * @param request
     */
    private void setFlowViewers(List<FlowViewerBean> item, HttpServletRequest request)
    {
        String[] providers = request.getParameterValues("check_init");

        for (int i = 0; i < providers.length; i++ )
        {
            if ( !StringTools.isNullOrNone(providers[i]))
            {
                FlowViewerBean bean = new FlowViewerBean();

                bean.setType(CommonTools.parseInt(request.getParameter("tokenType_" + providers[i])));

                bean.setProcesser(request.getParameter("processerId_" + providers[i]));

                item.add(bean);
            }
        }
    }

    /**
     * @param request
     * @param condtion
     */
    private void setCondition(HttpServletRequest request, ConditionParse condtion)
    {
        condtion.addWhereStr();

        String name = request.getParameter("name");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("FlowDefineBean.name", "like", name);
        }

        User user = Helper.getUser(request);

        if (user.getRole() != Role.WORKFLOW)
        {
            condtion.addIntCondition("FlowDefineBean.status", "=", FlowConstant.FLOW_STATUS_INIT);
        }

        condtion.addCondition("order by FlowDefineBean.id desc");
    }

    /**
     * @param request
     * @param condtion
     */
    private void setConditionForInstance(HttpServletRequest request, ConditionParse condtion)
    {
        condtion.addWhereStr();

        setQueryInstancePulic(request, condtion);

        User user = Helper.getUser(request);

        condtion.addCondition("FlowInstanceBean.userId", "=", user.getId());

        condtion.addCondition("order by FlowInstanceBean.id desc");
    }

    /**
     * 设置公共查询条件
     * 
     * @param request
     * @param condtion
     */
    private void setQueryInstancePulic(HttpServletRequest request, ConditionParse condtion)
    {
        String flowId = request.getParameter("flowId");

        if ( !StringTools.isNullOrNone(flowId))
        {
            condtion.addCondition("FlowInstanceBean.flowId", "=", flowId);
        }

        String alogTime = request.getParameter("alogTime");

        if ( !StringTools.isNullOrNone(alogTime))
        {
            condtion.addCondition("FlowInstanceBean.logTime", ">=", alogTime + " 00:00:00");
        }
        else
        {
            condtion.addCondition("FlowInstanceBean.logTime", ">=",
                TimeTools.getDateShortString( -5) + " 00:00:00");

            request.setAttribute("alogTime", TimeTools.getDateShortString( -5));
        }

        String blogTime = request.getParameter("blogTime");

        if ( !StringTools.isNullOrNone(blogTime))
        {
            condtion.addCondition("FlowInstanceBean.logTime", "<=", blogTime + " 23:59:59");
        }
        else
        {
            condtion.addCondition("FlowInstanceBean.logTime", "<=",
                TimeTools.getDateShortString(0) + " 23:59:59");

            request.setAttribute("blogTime", TimeTools.getDateShortString(0));
        }

        String title = request.getParameter("title");

        if ( !StringTools.isNullOrNone(title))
        {
            condtion.addCondition("FlowInstanceBean.title", "like", title);
        }

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(status))
        {
            if ("1".equals(status))
            {
                condtion.addIntCondition("FlowInstanceBean.status", "=",
                    FlowConstant.FLOW_INSTANCE_BEGIN);
            }

            // 处理中
            if ("2".equals(status))
            {
                condtion.addIntCondition("FlowInstanceBean.status", "<>",
                    FlowConstant.FLOW_INSTANCE_BEGIN);
                condtion.addIntCondition("FlowInstanceBean.status", "<>",
                    FlowConstant.FLOW_INSTANCE_END);
            }

            // 结束
            if ("3".equals(status))
            {
                condtion.addIntCondition("FlowInstanceBean.status", "=",
                    FlowConstant.FLOW_INSTANCE_END);
            }
        }
    }

    /**
     * @param request
     * @param condtion
     */
    private void setConditionForProcessInstance(HttpServletRequest request, ConditionParse condtion)
    {
        condtion.addWhereStr();

        setQueryInstancePulic(request, condtion);

        User user = Helper.getUser(request);

        condtion.addCondition("FlowBelongBean.userId", "=", user.getId());

        condtion.addIntCondition("FlowInstanceBean.status", "<>", FlowConstant.FLOW_INSTANCE_END);

        condtion.addCondition("order by FlowInstanceBean.id desc");
    }

    /**
     * @param request
     * @param condtion
     */
    private void setConditionForProcessHisInstance(HttpServletRequest request,
                                                   ConditionParse condtion)
    {
        condtion.addWhereStr();

        setQueryInstancePulic(request, condtion);

        User user = Helper.getUser(request);

        condtion.addCondition("FlowInstanceLogBean.userId", "=", user.getId());

        // 把自己剔除
        condtion.addCondition("FlowInstanceBean.userId", "<>", user.getId());

        condtion.addCondition("group by FlowInstanceBean.id order by FlowInstanceLogBean.id desc");
    }

    /**
     * 设置我的查阅的条件
     * 
     * @param request
     * @param condtion
     */
    private void setConditionForProcessViewerInstanceInner(HttpServletRequest request,
                                                           ConditionParse condtion)
    {
        condtion.addWhereStr();

        setQueryInstancePulic(request, condtion);

        User user = Helper.getUser(request);

        condtion.addCondition("FlowInstanceViewBean.userId", "=", user.getId());

        condtion.addCondition("order by FlowInstanceBean.id desc");
    }

    /**
     * @return the workFlowManager
     */
    public WorkFlowManager getWorkFlowManager()
    {
        return workFlowManager;
    }

    /**
     * @param workFlowManager
     *            the workFlowManager to set
     */
    public void setWorkFlowManager(WorkFlowManager workFlowManager)
    {
        this.workFlowManager = workFlowManager;
    }

    /**
     * @return the flowDefineDAO
     */
    public FlowDefineDAO getFlowDefineDAO()
    {
        return flowDefineDAO;
    }

    /**
     * @param flowDefineDAO
     *            the flowDefineDAO to set
     */
    public void setFlowDefineDAO(FlowDefineDAO flowDefineDAO)
    {
        this.flowDefineDAO = flowDefineDAO;
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
     * @return the flowInstanceLogDAO
     */
    public FlowInstanceLogDAO getFlowInstanceLogDAO()
    {
        return flowInstanceLogDAO;
    }

    /**
     * @param flowInstanceLogDAO
     *            the flowInstanceLogDAO to set
     */
    public void setFlowInstanceLogDAO(FlowInstanceLogDAO flowInstanceLogDAO)
    {
        this.flowInstanceLogDAO = flowInstanceLogDAO;
    }

    /**
     * @return the flowTokenDAO
     */
    public FlowTokenDAO getFlowTokenDAO()
    {
        return flowTokenDAO;
    }

    /**
     * @param flowTokenDAO
     *            the flowTokenDAO to set
     */
    public void setFlowTokenDAO(FlowTokenDAO flowTokenDAO)
    {
        this.flowTokenDAO = flowTokenDAO;
    }

    /**
     * @return the flowConsignDAO
     */
    public FlowConsignDAO getFlowConsignDAO()
    {
        return flowConsignDAO;
    }

    /**
     * @param flowConsignDAO
     *            the flowConsignDAO to set
     */
    public void setFlowConsignDAO(FlowConsignDAO flowConsignDAO)
    {
        this.flowConsignDAO = flowConsignDAO;
    }

    /**
     * @return the userDAO
     */
    public UserDAO getUserDAO()
    {
        return userDAO;
    }

    /**
     * @param userDAO
     *            the userDAO to set
     */
    public void setUserDAO(UserDAO userDAO)
    {
        this.userDAO = userDAO;
    }

    /**
     * @return the commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @param commonDAO
     *            the commonDAO to set
     */
    public void setCommonDAO(CommonDAO commonDAO)
    {
        this.commonDAO = commonDAO;
    }

    /**
     * @return the flowBelongDAO
     */
    public FlowBelongDAO getFlowBelongDAO()
    {
        return flowBelongDAO;
    }

    /**
     * @param flowBelongDAO
     *            the flowBelongDAO to set
     */
    public void setFlowBelongDAO(FlowBelongDAO flowBelongDAO)
    {
        this.flowBelongDAO = flowBelongDAO;
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
     * @return the flowInstanceViewDAO
     */
    public FlowInstanceViewDAO getFlowInstanceViewDAO()
    {
        return flowInstanceViewDAO;
    }

    /**
     * @param flowInstanceViewDAO
     *            the flowInstanceViewDAO to set
     */
    public void setFlowInstanceViewDAO(FlowInstanceViewDAO flowInstanceViewDAO)
    {
        this.flowInstanceViewDAO = flowInstanceViewDAO;
    }
}
