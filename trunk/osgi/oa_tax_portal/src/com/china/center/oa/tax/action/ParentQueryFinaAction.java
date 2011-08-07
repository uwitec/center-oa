/**
 * File Name: ParentQueryFinaAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-8-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.action;


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
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.common.OldPageSeparateTools;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.PrincipalshipDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.sail.dao.UnitViewDAO;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.bean.UnitBean;
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.oa.tax.dao.CheckViewDAO;
import com.china.center.oa.tax.dao.FinanceDAO;
import com.china.center.oa.tax.dao.FinanceItemDAO;
import com.china.center.oa.tax.dao.FinanceMonthDAO;
import com.china.center.oa.tax.dao.FinanceTurnDAO;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.oa.tax.dao.UnitDAO;
import com.china.center.oa.tax.facade.TaxFacade;
import com.china.center.oa.tax.helper.FinanceHelper;
import com.china.center.oa.tax.manager.FinanceManager;
import com.china.center.oa.tax.vo.FinanceItemVO;
import com.china.center.oa.tax.vo.TaxVO;
import com.china.center.tools.CommonTools;
import com.china.center.tools.StringTools;


/**
 * ParentQueryFinaAction
 * 
 * @author ZHUZHU
 * @version 2011-8-7
 * @see ParentQueryFinaAction
 * @since 3.0
 */
public class ParentQueryFinaAction extends DispatchAction
{
    protected final Log _logger = LogFactory.getLog(getClass());

    protected TaxFacade taxFacade = null;

    protected TaxDAO taxDAO = null;

    protected DutyDAO dutyDAO = null;

    protected UnitDAO unitDAO = null;

    protected OutManager outManager = null;

    protected FinanceManager financeManager = null;

    protected PrincipalshipDAO principalshipDAO = null;

    protected StafferDAO stafferDAO = null;

    protected FinanceDAO financeDAO = null;

    protected UnitViewDAO unitViewDAO = null;

    protected CheckViewDAO checkViewDAO = null;

    protected FinanceItemDAO financeItemDAO = null;

    protected DepotDAO depotDAO = null;

    protected ProductDAO productDAO = null;

    protected FinanceTurnDAO financeTurnDAO = null;

    protected FinanceMonthDAO financeMonthDAO = null;

    protected static final String QUERYFINANCE = "queryFinance";

    protected static final String QUERYFINANCEMONTH = "queryFinanceMonth";

    protected static final String QUERYFINANCETURN = "queryFinanceTurn";

    protected static final String QUERYFINANCEITEM = "queryFinanceItem";

    protected static final String QUERYCHECKVIEW = "queryCheckView";

    protected static final String QUERYTAXFINANCE1 = "queryTaxFinance1";

    protected static String RPTQUERYUNIT = "rptQueryUnit";

    /**
     * 分类账查询
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryTaxFinance1(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = Helper.getUser(request);

        request.getSession().setAttribute("EXPORT_FINANCEITE_KEY", QUERYTAXFINANCE1);

        List<FinanceItemVO> list = null;

        CommonTools.saveParamers(request);

        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = getQueryCondition(request, user);

                int tatol = financeItemDAO.countVOByCondition(condtion.toString());

                PageSeparate page = new PageSeparate(tatol, 100);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, QUERYTAXFINANCE1);

                list = financeItemDAO.queryEntityVOsByCondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, QUERYTAXFINANCE1);

                list = financeItemDAO.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(
                    request, QUERYTAXFINANCE1), OldPageSeparateTools.getPageSeparate(request,
                    QUERYTAXFINANCE1));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败");

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        for (FinanceItemVO financeItemVO : list)
        {
            fillItemVO(financeItemVO);
        }

        request.setAttribute("resultList", list);

        return mapping.findForward(QUERYTAXFINANCE1);
    }

    protected ConditionParse getQueryCondition(HttpServletRequest request, User user)
        throws MYException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        String beginDate = request.getParameter("beginDate");
        condtion.addCondition("FinanceItemBean.financeDate", ">=", beginDate);

        String endDate = request.getParameter("endDate");
        condtion.addCondition("FinanceItemBean.financeDate", "<=", endDate);

        String taxId = request.getParameter("taxId");

        TaxVO tax = taxDAO.findVO(taxId);

        if (tax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        request.setAttribute("tax", tax);

        // 动态级别的查询
        condtion.addCondition("FinanceItemBean.taxId" + tax.getLevel(), "=", taxId);

        String stafferId = request.getParameter("stafferId");

        if ( !StringTools.isNullOrNone(stafferId))
        {
            condtion.addCondition("FinanceItemBean.stafferId", "=", stafferId);
        }

        String departmentId = request.getParameter("departmentId");

        if ( !StringTools.isNullOrNone(departmentId))
        {
            condtion.addCondition("FinanceItemBean.departmentId", "=", departmentId);
        }

        return condtion;
    }

    /**
     * 科目余额查询(先查询上月的结余,然后是时间内的统计)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryTaxFinance2(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = Helper.getUser(request);

        request.getSession().setAttribute("EXPORT_FINANCEITE_KEY", QUERYTAXFINANCE1);

        List<FinanceItemVO> list = null;

        CommonTools.saveParamers(request);

        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = getQueryCondition(request, user);

                int tatol = financeItemDAO.countVOByCondition(condtion.toString());

                PageSeparate page = new PageSeparate(tatol, 100);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, QUERYTAXFINANCE1);

                list = financeItemDAO.queryEntityVOsByCondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, QUERYTAXFINANCE1);

                list = financeItemDAO.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(
                    request, QUERYTAXFINANCE1), OldPageSeparateTools.getPageSeparate(request,
                    QUERYTAXFINANCE1));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败");

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        for (FinanceItemVO financeItemVO : list)
        {
            fillItemVO(financeItemVO);
        }

        request.setAttribute("resultList", list);

        return mapping.findForward(QUERYTAXFINANCE1);
    }

    /**
     * fillItemVO
     * 
     * @param item
     */
    protected void fillItemVO(FinanceItemVO item)
    {
        TaxBean tax = taxDAO.find(item.getTaxId());

        item.setForward(tax.getForward());

        if (tax.getDepartment() == TaxConstanst.TAX_CHECK_YES)
        {
            PrincipalshipBean depart = principalshipDAO.find(item.getDepartmentId());

            if (depart != null)
            {
                item.setDepartmentName(depart.getName());
            }
        }

        if (tax.getStaffer() == TaxConstanst.TAX_CHECK_YES)
        {
            StafferBean sb = stafferDAO.find(item.getStafferId());

            if (sb != null)
            {
                item.setStafferName(sb.getName());
            }
        }

        if (tax.getUnit() == TaxConstanst.TAX_CHECK_YES)
        {
            UnitBean unit = unitDAO.find(item.getUnitId());

            if (unit != null)
            {
                item.setUnitName(unit.getName());
            }
        }

        if (tax.getProduct() == TaxConstanst.TAX_CHECK_YES)
        {
            ProductBean product = productDAO.find(item.getProductId());

            if (product != null)
            {
                item.setProductName(product.getName());
                item.setProductCode(product.getCode());
            }
        }

        if (tax.getDepot() == TaxConstanst.TAX_CHECK_YES)
        {
            DepotBean depot = depotDAO.find(item.getDepotId());

            if (depot != null)
            {
                item.setDepotName(depot.getName());
            }
        }

        if (tax.getDuty() == TaxConstanst.TAX_CHECK_YES)
        {
            DutyBean duty2 = dutyDAO.find(item.getDuty2Id());

            if (duty2 != null)
            {
                item.setDuty2Name(duty2.getName());
            }
        }

        item.getShowInmoney();
        item.getShowOutmoney();

        long last = 0;

        if (tax.getForward() == TaxConstanst.TAX_FORWARD_IN)
        {
            last = item.getInmoney() - item.getOutmoney();
            item.setForwardName("借");
        }
        else
        {
            last = item.getOutmoney() - item.getInmoney();
            item.setForwardName("贷");
        }

        item.setShowLastmoney(FinanceHelper.longToString(last));
    }

}
