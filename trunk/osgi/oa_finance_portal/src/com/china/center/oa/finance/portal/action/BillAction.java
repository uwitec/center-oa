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
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.facade.FinanceFacade;
import com.china.center.oa.finance.vo.InBillVO;
import com.china.center.oa.publics.Helper;
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

    private FinanceFacade financeFacade = null;

    private static final String QUERYINBILL = "queryInBill";

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
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据异常,请重新操作");

            return mapping.findForward("queryBank");
        }

        request.setAttribute("bean", bean);

        return mapping.findForward("detailInBill");
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
}
