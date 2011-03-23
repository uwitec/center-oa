/**
 * File Name: StockPayAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.portal.action;


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

import com.center.china.osgi.publics.User;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.JSONTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.BackPayApplyBean;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.constant.BackPayApplyConstant;
import com.china.center.oa.finance.dao.BackPayApplyDAO;
import com.china.center.oa.finance.dao.BankDAO;
import com.china.center.oa.finance.facade.FinanceFacade;
import com.china.center.oa.finance.vo.BackPayApplyVO;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.RequestTools;


/**
 * BackPayApplyAction
 * 
 * @author ZHUZHU
 * @version 2011-2-18
 * @see BackPayApplyAction
 * @since 3.0
 */
public class BackPayApplyAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private FlowLogDAO flowLogDAO = null;

    private BankDAO bankDAO = null;

    private OutDAO outDAO = null;

    private OutManager outManager = null;

    private UserManager userManager = null;

    private BackPayApplyDAO backPayApplyDAO = null;

    private FinanceFacade financeFacade = null;

    private static final String QUERYBACKPAYAPPLY = "queryBackPayApply";

    /**
     * queryStockPayApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryBackPayApply(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYBACKPAYAPPLY, request, condtion);

        String mode = RequestTools.getValueFromRequest(request, "mode");

        User user = Helper.getUser(request);

        ActionForward checkAuth = checkAuth(mapping, request);

        if (checkAuth != null)
        {
            return checkAuth;
        }

        // 自己的
        if ("0".equals(mode))
        {
            condtion.addCondition("BackPayApplyBean.stafferId", "=", user.getStafferId());
        }
        // 结算中心
        else if ("1".equals(mode))
        {
            condtion.addIntCondition("BackPayApplyBean.status", "=",
                BackPayApplyConstant.STATUS_SUBMIT);
        }
        // 出纳
        else if ("2".equals(mode))
        {
            condtion.addIntCondition("BackPayApplyBean.status", "=",
                BackPayApplyConstant.STATUS_SEC);
        }
        else
        {
            condtion.addFlaseCondition();
        }

        condtion.addCondition("order by BackPayApplyBean.id desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYBACKPAYAPPLY, request, condtion,
            this.backPayApplyDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * checkAuth
     * 
     * @param mapping
     * @param request
     * @return
     */
    private ActionForward checkAuth(ActionMapping mapping, HttpServletRequest request)
    {
        User user = Helper.getUser(request);

        String mode = request.getParameter("mode");

        // 自己的
        if ("0".equals(mode))
        {
            if ( !userManager.containAuth(user, AuthConstant.SAIL_SUBMIT))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

                return mapping.findForward("queryBackPayApply");
            }
        }
        // 结算中心
        else if ("1".equals(mode))
        {
            if ( !userManager.containAuth(user, AuthConstant.SAIL_BACKPAY_CENTER))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

                return mapping.findForward("queryBackPayApply");

            }
        }
        // 出纳
        else if ("2".equals(mode))
        {
            if ( !userManager.containAuth(user, AuthConstant.SAIL_BACKPAY_SEC))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

                return mapping.findForward("queryBackPayApply");
            }
        }
        else
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

            return mapping.findForward("queryBackPayApply");
        }

        return null;
    }

    /**
     * findBackPayApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findBackPayApply(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        BackPayApplyVO bean = backPayApplyDAO.findVO(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据异常,请重新操作");

            return mapping.findForward("error");
        }

        request.setAttribute("bean", bean);

        List<FlowLogBean> loglist = flowLogDAO.queryEntityBeansByFK(id);

        request.setAttribute("loglist", loglist);

        if (bean.getType() == BackPayApplyConstant.TYPE_OUT)
        {
            OutBean out = outDAO.find(bean.getOutId());

            request.setAttribute("out", out);

            double backTotal = outDAO.sumOutBackValue(bean.getOutId());

            request.setAttribute("backTotal", backTotal);
        }

        List<BankBean> bankList = bankDAO.listEntityBeans();

        request.setAttribute("bankList", bankList);

        return mapping.findForward("handleBackPayApply");
    }

    /**
     * addBackPayApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addBackPayApply(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        BackPayApplyBean bean = new BackPayApplyBean();

        BeanUtil.getBean(bean, request);

        try
        {
            User user = Helper.getUser(request);

            bean.setStafferId(user.getStafferId());

            bean.setType(BackPayApplyConstant.TYPE_OUT);

            financeFacade.addBackPayApplyBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("queryType", "8");

        RequestTools.menuInitQuery(request);

        return mapping.findForward("queryOut");
    }

    /**
     * passBackPayApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward passBackPayApply(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String reason = request.getParameter("reason");

        String mode = request.getParameter("mode");

        try
        {
            User user = Helper.getUser(request);

            financeFacade.passBackPayApplyBean(user.getId(), id, reason);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("mode", mode);

        return mapping.findForward("queryBackPayApply");
    }

    /**
     * deleteBackPayApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteBackPayApply(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.deleteBackPayApplyBean(user.getId(), id);

            ajax.setSuccess("成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("操作失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * endBackPayApply(结束)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward endBackPayApply(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String reason = request.getParameter("reason");
        String bankId = request.getParameter("bankId");
        String payType = request.getParameter("payType");
        String backPay = request.getParameter("backPay");

        OutBillBean outBill = new OutBillBean();

        try
        {
            User user = Helper.getUser(request);

            outBill.setBankId(bankId);

            outBill.setPayType(MathTools.parseInt(payType));

            outBill.setMoneys(MathTools.parseDouble(backPay));

            financeFacade.endBackPayApplyBean(user.getId(), id, reason, outBill);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("mode", "2");

        return mapping.findForward("queryBackPayApply");
    }

    /**
     * rejectBackPayApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward rejectBackPayApply(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String reason = request.getParameter("reason");

        String mode = request.getParameter("mode");

        try
        {
            User user = Helper.getUser(request);

            financeFacade.rejectBackPayApplyBean(user.getId(), id, reason);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        request.setAttribute("mode", mode);

        return mapping.findForward("queryBackPayApply");
    }

    /**
     * @return the flowLogDAO
     */
    public FlowLogDAO getFlowLogDAO()
    {
        return flowLogDAO;
    }

    /**
     * @param flowLogDAO
     *            the flowLogDAO to set
     */
    public void setFlowLogDAO(FlowLogDAO flowLogDAO)
    {
        this.flowLogDAO = flowLogDAO;
    }

    /**
     * @return the financeFacade
     */
    public FinanceFacade getFinanceFacade()
    {
        return financeFacade;
    }

    /**
     * @param financeFacade
     *            the financeFacade to set
     */
    public void setFinanceFacade(FinanceFacade financeFacade)
    {
        this.financeFacade = financeFacade;
    }

    /**
     * @return the bankDAO
     */
    public BankDAO getBankDAO()
    {
        return bankDAO;
    }

    /**
     * @param bankDAO
     *            the bankDAO to set
     */
    public void setBankDAO(BankDAO bankDAO)
    {
        this.bankDAO = bankDAO;
    }

    /**
     * @return the backPayApplyDAO
     */
    public BackPayApplyDAO getBackPayApplyDAO()
    {
        return backPayApplyDAO;
    }

    /**
     * @param backPayApplyDAO
     *            the backPayApplyDAO to set
     */
    public void setBackPayApplyDAO(BackPayApplyDAO backPayApplyDAO)
    {
        this.backPayApplyDAO = backPayApplyDAO;
    }

    /**
     * @return the userManager
     */
    public UserManager getUserManager()
    {
        return userManager;
    }

    /**
     * @param userManager
     *            the userManager to set
     */
    public void setUserManager(UserManager userManager)
    {
        this.userManager = userManager;
    }

    /**
     * @return the outDAO
     */
    public OutDAO getOutDAO()
    {
        return outDAO;
    }

    /**
     * @param outDAO
     *            the outDAO to set
     */
    public void setOutDAO(OutDAO outDAO)
    {
        this.outDAO = outDAO;
    }

    /**
     * @return the outManager
     */
    public OutManager getOutManager()
    {
        return outManager;
    }

    /**
     * @param outManager
     *            the outManager to set
     */
    public void setOutManager(OutManager outManager)
    {
        this.outManager = outManager;
    }
}
