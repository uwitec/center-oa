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
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.bean.InBillBean;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.BankDAO;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.OutBillDAO;
import com.china.center.oa.finance.facade.FinanceFacade;
import com.china.center.oa.finance.vo.InBillVO;
import com.china.center.oa.finance.vo.OutBillVO;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.dao.InvoiceDAO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.MathTools;


/**
 * BillAction
 * 
 * @author ZHUZHU
 * @version 2011-1-9
 * @see BillAction
 * @since 3.0
 */
public class BillAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private InBillDAO inBillDAO = null;

    private BankDAO bankDAO = null;

    private OutBillDAO outBillDAO = null;

    private FinanceFacade financeFacade = null;

    private InvoiceDAO invoiceDAO = null;

    private static final String QUERYINBILL = "queryInBill";

    private static final String QUERYOUTBILL = "queryOutBill";

    private static final String QUERYSELFINBILL = "querySelfInBill";

    /**
     * queryInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryInBill(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYINBILL, request, condtion);

        condtion.addCondition("InBillBean.locationId", "=", user.getLocationId());

        condtion.addCondition("order by InBillBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYINBILL, request, condtion,
            this.inBillDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryOutBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryOutBill(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYOUTBILL, request, condtion);

        condtion.addCondition("OutBillBean.locationId", "=", user.getLocationId());

        condtion.addCondition("order by OutBillBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYOUTBILL, request, condtion,
            this.outBillDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * preForAddInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddInBill(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        List<BankBean> banlList = bankDAO.listEntityBeans();

        request.setAttribute("bankList", banlList);

        return mapping.findForward("addInBill");
    }

    /**
     * preForAddOutBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddOutBill(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        List<BankBean> banlList = bankDAO.listEntityBeans();

        request.setAttribute("bankList", banlList);

        List<InvoiceBean> invoiceList = invoiceDAO.listForwardIn();

        request.setAttribute("invoiceList", invoiceList);

        return mapping.findForward("addOutBill");
    }

    /**
     * addInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addInBill(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        InBillBean bean = new InBillBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            bean.setStafferId(user.getStafferId());

            bean.setLocationId(user.getLocationId());

            bean.setStatus(FinanceConstant.INBILL_STATUS_PAYMENTS);

            bean.setLock(FinanceConstant.BILL_LOCK_NO);

            financeFacade.addInBillBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryInBill");
    }

    /**
     * addOutBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addOutBill(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        OutBillBean bean = new OutBillBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            bean.setStafferId(user.getStafferId());

            bean.setLocationId(user.getLocationId());

            bean.setLock(FinanceConstant.BILL_LOCK_NO);

            financeFacade.addOutBillBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryOutBill");
    }

    /**
     * deleteInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteInBill(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.deleteInBillBean(user.getId(), id);

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
     * deleteOutBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteOutBill(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.deleteOutBillBean(user.getId(), id);

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
     * querySelfInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward querySelfInBill(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYSELFINBILL, request, condtion);

        condtion.addCondition("InBillBean.ownerId", "=", user.getStafferId());

        condtion.addIntCondition("InBillBean.status", "=", FinanceConstant.INBILL_STATUS_NOREF);

        condtion.addCondition("order by InBillBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSELFINBILL, request, condtion,
            this.inBillDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * preForBingInBillByCustomerId
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForBingInBillByCustomerId(ActionMapping mapping, ActionForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse response)
        throws ServletException
    {
        String customerId = request.getParameter("customerId");

        User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("InBillBean.ownerId", "=", user.getStafferId());

        condtion.addIntCondition("InBillBean.status", "=", FinanceConstant.INBILL_STATUS_NOREF);

        condtion.addCondition("InBillBean.customerId", "=", customerId);

        condtion.addCondition("order by InBillBean.logTime desc");

        List<InBillVO> billList = inBillDAO.queryEntityVOsByCondition(condtion);

        request.setAttribute("billList", billList);

        return mapping.findForward("bingBill");
    }

    /**
     * findInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findInBill(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        InBillVO bean = inBillDAO.findVO(id);

        if (bean == null)
        {
            return ActionTools.toError("数据异常,请重新操作", mapping, request);
        }

        request.setAttribute("bean", bean);

        return mapping.findForward("detailInBill");
    }

    /**
     * findOutBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findOutBill(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        OutBillVO bean = outBillDAO.findVO(id);

        if (bean == null)
        {
            return ActionTools.toError("数据异常,请重新操作", mapping, request);
        }

        request.setAttribute("bean", bean);

        return mapping.findForward("detailOutBill");
    }

    /**
     * splitInBill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward splitInBill(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            String newMoney = request.getParameter("newMoney");

            User user = Helper.getUser(request);

            financeFacade.splitInBillBean(user.getId(), id, MathTools.parseDouble(newMoney));

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
     * @return the inBillDAO
     */
    public InBillDAO getInBillDAO()
    {
        return inBillDAO;
    }

    /**
     * @param inBillDAO
     *            the inBillDAO to set
     */
    public void setInBillDAO(InBillDAO inBillDAO)
    {
        this.inBillDAO = inBillDAO;
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
     * @return the outBillDAO
     */
    public OutBillDAO getOutBillDAO()
    {
        return outBillDAO;
    }

    /**
     * @param outBillDAO
     *            the outBillDAO to set
     */
    public void setOutBillDAO(OutBillDAO outBillDAO)
    {
        this.outBillDAO = outBillDAO;
    }

    /**
     * @return the invoiceDAO
     */
    public InvoiceDAO getInvoiceDAO()
    {
        return invoiceDAO;
    }

    /**
     * @param invoiceDAO
     *            the invoiceDAO to set
     */
    public void setInvoiceDAO(InvoiceDAO invoiceDAO)
    {
        this.invoiceDAO = invoiceDAO;
    }
}