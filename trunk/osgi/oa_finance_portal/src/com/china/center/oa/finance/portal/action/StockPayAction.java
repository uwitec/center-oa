/**
 * File Name: StockPayAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.portal.action;


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

import com.center.china.osgi.publics.User;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.JSONTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.constant.StockPayApplyConstant;
import com.china.center.oa.finance.dao.StockPayApplyDAO;
import com.china.center.oa.finance.facade.FinanceFacade;
import com.china.center.oa.finance.vo.StockPayApplyVO;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.tools.CommonTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * StockPayAction
 * 
 * @author ZHUZHU
 * @version 2011-2-18
 * @see StockPayAction
 * @since 3.0
 */
public class StockPayAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private StockPayApplyDAO stockPayApplyDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private FinanceFacade financeFacade = null;

    private static final String QUERYSTOCKPAYAPPLY = "queryStockPayApply";

    private static final String QUERYCEOSTOCKPAYAPPLY = "queryCEOStockPayApply";

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
    public ActionForward queryStockPayApply(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        Map<String, String> initMap = initLogTime(request, condtion);

        ActionTools.processJSONQueryCondition(QUERYSTOCKPAYAPPLY, request, condtion, initMap);

        condtion.addCondition("order by StockPayApplyBean.payDate desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSTOCKPAYAPPLY, request,
            condtion, this.stockPayApplyDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    private Map<String, String> initLogTime(HttpServletRequest request, ConditionParse condtion)
    {
        Map<String, String> changeMap = new HashMap<String, String>();

        String alogTime = request.getParameter("alogTime");

        String blogTime = request.getParameter("blogTime");

        if (StringTools.isNullOrNone(alogTime) && StringTools.isNullOrNone(blogTime))
        {
            changeMap.put("alogTime", TimeTools.now_short( -3));

            changeMap.put("blogTime", TimeTools.now_short());

            condtion.addCondition("StockPayApplyBean.payDate", ">=", TimeTools.now_short( -3));

            condtion.addCondition("StockPayApplyBean.payDate", "<=", TimeTools.now_short());
        }

        return changeMap;
    }

    /**
     * queryCEOStockPayApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryCEOStockPayApply(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYCEOSTOCKPAYAPPLY, request, condtion);

        condtion.addIntCondition("StockPayApplyBean.status", "=",
            StockPayApplyConstant.APPLY_STATUS_CEO);

        condtion.addCondition("order by StockPayApplyBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYCEOSTOCKPAYAPPLY, request,
            condtion, this.stockPayApplyDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * findStockPayApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findStockPayApply(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String update = request.getParameter("update");

        StockPayApplyVO bean = stockPayApplyDAO.findVO(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据异常,请重新操作");

            return mapping.findForward("error");
        }

        request.setAttribute("bean", bean);

        List<FlowLogBean> loglist = flowLogDAO.queryEntityBeansByFK(id);

        request.setAttribute("loglist", loglist);

        if ("1".equals(update) && bean.getStatus() != StockPayApplyConstant.APPLY_STATUS_END)
        {
            if (TimeTools.now_short().compareTo(bean.getPayDate()) >= 0)
            {
                return mapping.findForward("handleStockPayApply");
            }
            else
            {
                return ActionTools.toError("付款的最早时间还没有到", "queryStockPayApply", mapping, request);
            }
        }

        return mapping.findForward("detailStockPayApply");
    }

    /**
     * submitStockPay
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward submitStockPay(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String reason = request.getParameter("reason");

        String payMoney = request.getParameter("payMoney");

        try
        {
            User user = Helper.getUser(request);

            financeFacade.submitStockPayApply(user.getId(), id, MathTools.parseDouble(payMoney),
                reason);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryStockPayApply");
    }

    /**
     * @return the stockPayApplyDAO
     */
    public StockPayApplyDAO getStockPayApplyDAO()
    {
        return stockPayApplyDAO;
    }

    /**
     * @param stockPayApplyDAO
     *            the stockPayApplyDAO to set
     */
    public void setStockPayApplyDAO(StockPayApplyDAO stockPayApplyDAO)
    {
        this.stockPayApplyDAO = stockPayApplyDAO;
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
}
