/**
 * File Name: WorkLogAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-16<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.group.action;


import java.util.ArrayList;
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
import com.china.center.oa.constant.GroupConstant;
import com.china.center.oa.facade.MailGroupFacade;
import com.china.center.oa.group.bean.GroupBean;
import com.china.center.oa.group.dao.GroupDAO;
import com.china.center.oa.group.dao.GroupVSStafferDAO;
import com.china.center.oa.group.vo.GroupVSStafferVO;
import com.china.center.oa.group.vs.GroupVSStafferBean;
import com.china.center.oa.helper.Helper;
import com.china.center.oa.publics.User;
import com.china.center.tools.ActionTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.JSONTools;
import com.china.center.tools.StringTools;


/**
 * WorkLogAction
 * 
 * @author zhuzhu
 * @version 2009-2-16
 * @see GroupAction
 * @since 1.0
 */
public class GroupAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private MailGroupFacade mailGroupFacade = null;

    private GroupDAO groupDAO = null;

    private GroupVSStafferDAO groupVSStafferDAO = null;

    private static String QUERYGROUP = "queryGroup";

    private static String QUERYPUBLICGROUP = "queryPublicGroup";

    private static String QUERYSYSTEMGROUP = "querySystemGroup";

    /**
     * default constructor
     */
    public GroupAction()
    {}

    /**
     * queryGroup
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryGroup(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        String stafferId = request.getParameter("stafferId");

        if (StringTools.isNullOrNone(stafferId))
        {
            condtion.addCondition("GroupBean.stafferId", "=", user.getStafferId());
        }

        // filter system group
        // condtion.addCondition("GroupBean.type", "<>", GroupConstant.GROUP_TYPE_SYSTEM);

        ActionTools.processJSONQueryCondition(QUERYGROUP, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYGROUP, request, condtion,
            this.groupDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryPublicGroup
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryPublicGroup(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("GroupBean.type", "=", GroupConstant.GROUP_TYPE_PUBLIC);

        ActionTools.processJSONQueryCondition(QUERYPUBLICGROUP, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPUBLICGROUP, request, condtion,
            this.groupDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * querySystemGroup
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward querySystemGroup(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("GroupBean.type", "=", GroupConstant.GROUP_TYPE_SYSTEM);

        ActionTools.processJSONQueryCondition(QUERYSYSTEMGROUP, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSYSTEMGROUP, request, condtion,
            this.groupDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * addOrUpdateGroup
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addOrUpdateGroup(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        GroupBean bean = new GroupBean();

        String update = request.getParameter("update");

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            bean.setStafferId(user.getStafferId());

            createItems(request, bean);

            if ("1".equals(update))
            {
                mailGroupFacade.updateGroup(user.getId(), bean);
            }
            else
            {
                mailGroupFacade.addGroup(user.getId(), bean);
            }

            request.setAttribute(KeyConstant.MESSAGE, "成功保存群组");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "保存群组失败:" + e.getMessage());
        }

        if (bean.getType() == GroupConstant.GROUP_TYPE_PRIVATE)
        {
            return mapping.findForward("queryGroup");
        }

        if (bean.getType() == GroupConstant.GROUP_TYPE_SYSTEM)
        {
            return mapping.findForward("querySystemGroup");
        }

        // return mapping.findForward("queryPublicGroup");
        return mapping.findForward("queryGroup");
    }

    /**
     * find群组
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findGroup(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        String update = request.getParameter("update");

        GroupBean bean = groupDAO.find(id);

        if (bean == null)
        {
            return ActionTools.toError("群组不存在", mapping, request);
        }

        List<GroupVSStafferVO> items = groupVSStafferDAO.queryEntityVOsByFK(id);

        request.setAttribute("bean", bean);

        request.setAttribute("items", items);

        if ("1".equals(update))
        {
            return mapping.findForward("updateGroup");
        }

        return mapping.findForward("detailGroup");
    }

    /**
     * 构建Visit
     * 
     * @param request
     * @param bean
     * @throws MYException
     */
    private void createItems(HttpServletRequest request, GroupBean bean)
        throws MYException
    {
        List<GroupVSStafferBean> items = new ArrayList<GroupVSStafferBean>();

        bean.setItems(items);

        String[] vsStafferIds = request.getParameterValues("vsStafferIds");

        if (vsStafferIds == null)
        {
            return;
        }

        for (int i = 0; i < vsStafferIds.length; i++ )
        {
            if (StringTools.isNullOrNone(vsStafferIds[i]))
            {
                continue;
            }

            GroupVSStafferBean vs = new GroupVSStafferBean();

            vs.setStafferId(vsStafferIds[i]);

            items.add(vs);
        }
    }

    /**
     * delGroup
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward delGroup(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            mailGroupFacade.delGroup(user.getId(), id);

            ajax.setSuccess("成功删除群组");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除群组失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * @return the mailGroupFacade
     */
    public MailGroupFacade getMailGroupFacade()
    {
        return mailGroupFacade;
    }

    /**
     * @param mailGroupFacade
     *            the mailGroupFacade to set
     */
    public void setMailGroupFacade(MailGroupFacade mailGroupFacade)
    {
        this.mailGroupFacade = mailGroupFacade;
    }

    /**
     * @return the groupDAO
     */
    public GroupDAO getGroupDAO()
    {
        return groupDAO;
    }

    /**
     * @param groupDAO
     *            the groupDAO to set
     */
    public void setGroupDAO(GroupDAO groupDAO)
    {
        this.groupDAO = groupDAO;
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
}
