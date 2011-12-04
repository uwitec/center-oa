/**
 * File Name: ParentQueryFinaAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-8-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.action;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import com.center.china.osgi.publics.file.writer.WriteFile;
import com.center.china.osgi.publics.file.writer.WriteFileFactory;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.common.OldPageSeparateTools;
import com.china.center.actionhelper.common.PageSeparateTools;
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
import com.china.center.oa.publics.constant.SysConfigConstant;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.dao.PrincipalshipDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.manager.OrgManager;
import com.china.center.oa.sail.dao.UnitViewDAO;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.tax.bean.FinanceMonthBean;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.bean.UnitBean;
import com.china.center.oa.tax.constanst.FinaConstant;
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.oa.tax.dao.CheckViewDAO;
import com.china.center.oa.tax.dao.FinanceDAO;
import com.china.center.oa.tax.dao.FinanceItemDAO;
import com.china.center.oa.tax.dao.FinanceMonthDAO;
import com.china.center.oa.tax.dao.FinanceRepDAO;
import com.china.center.oa.tax.dao.FinanceTurnDAO;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.oa.tax.dao.UnitDAO;
import com.china.center.oa.tax.facade.TaxFacade;
import com.china.center.oa.tax.helper.FinanceHelper;
import com.china.center.oa.tax.helper.TaxHelper;
import com.china.center.oa.tax.manager.FinanceManager;
import com.china.center.oa.tax.vo.FinanceItemVO;
import com.china.center.oa.tax.vo.FinanceRepVO;
import com.china.center.oa.tax.vo.FinanceShowVO;
import com.china.center.oa.tax.vo.TaxVO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.center.tools.WriteFileBuffer;


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

    protected OrgManager orgManager = null;

    protected FinanceTurnDAO financeTurnDAO = null;

    protected FinanceMonthDAO financeMonthDAO = null;

    protected ParameterDAO parameterDAO = null;

    protected FinanceRepDAO financeRepDAO = null;

    protected static final String QUERYFINANCE = "queryFinance";

    protected static final String QUERYFINANCEMONTH = "queryFinanceMonth";

    protected static final String QUERYFINANCETURN = "queryFinanceTurn";

    protected static final String QUERYFINANCEITEM = "queryFinanceItem";

    protected static final String QUERYCHECKVIEW = "queryCheckView";

    protected static final String QUERYTAXFINANCE1 = "queryTaxFinance1";

    protected static final String QUERYTAXFINANCE2 = "queryTaxFinance2";

    protected static final String QUERYTAXFINANCE3 = "queryTaxFinance3";

    protected static final String QUERYTAXREPORT = "queryTaxReport";

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

        FinanceItemVO head = null;
        FinanceItemVO currentTotal = null;
        FinanceItemVO allTotal = null;

        try
        {
            if (PageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = getQueryCondition(request, user, 0);

                int tatol = financeItemDAO.countVOByCondition(condtion.toString());

                PageSeparate page = new PageSeparate(tatol, 50);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, QUERYTAXFINANCE1);

                list = financeItemDAO.queryEntityVOsByCondition(condtion, page);

                // 结转
                head = sumHead(request, user);

                // 当期合计
                currentTotal = sumCurrentTotal(request, user, condtion);

                // 当前累计
                allTotal = sumAllTotal(request, user);
            }
            else
            {
                PageSeparateTools.processSeparate(request, QUERYTAXFINANCE1);

                list = financeItemDAO.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(
                    request, QUERYTAXFINANCE1), OldPageSeparateTools.getPageSeparate(request,
                    QUERYTAXFINANCE1));

                head = (FinanceItemVO)request.getSession().getAttribute("queryTaxFinance1_head");

                currentTotal = (FinanceItemVO)request.getSession().getAttribute(
                    "queryTaxFinance1_currentTotal");

                allTotal = (FinanceItemVO)request.getSession().getAttribute(
                    "queryTaxFinance1_allTotal");
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败");

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        String queryType = request.getParameter("queryType");

        // 明细
        if ("0".equals(queryType))
        {
            // 这里的分类账有个特点就是余额需要递增
            PageSeparate pageSeparate = OldPageSeparateTools.getPageSeparate(request,
                QUERYTAXFINANCE1);

            TaxBean tax = (TaxBean)request.getSession().getAttribute("queryTaxFinance1_tax");

            long ptotal = 0L;

            // 当进入翻页的时候,开始计算上页的期末余额
            if (pageSeparate.getNowPage() > 1)
            {
                PageSeparate newPage = new PageSeparate();
                newPage.setRowCount( (pageSeparate.getNowPage() - 1) * pageSeparate.getPageSize());
                newPage.setPageSize(newPage.getRowCount());

                long[] sumVOn = financeItemDAO.sumVOMoneryByCondition(OldPageSeparateTools
                    .getCondition(request, QUERYTAXFINANCE1), newPage);

                // 余额
                ptotal = TaxHelper.getLastMoney(tax, sumVOn);
            }

            // 合计出本次展现的开始金额
            long lastmoney = head.getLastmoney() + ptotal;

            long oldLastmoney = lastmoney;

            for (FinanceItemVO financeItemVO : list)
            {
                fillItemVO(financeItemVO);

                lastmoney = financeItemVO.getLastmoney() + lastmoney;

                financeItemVO.setLastmoney(lastmoney);

                financeItemVO.setShowLastmoney(FinanceHelper.longToString(lastmoney));
            }

            if (pageSeparate.getNowPage() <= 1)
            {
                // 放入合计统计
                list.add(0, head);
            }
            else
            {
                FinanceItemVO newHead = new FinanceItemVO();

                BeanUtil.copyProperties(newHead, head);

                newHead.setLastmoney(oldLastmoney);

                newHead.setDescription("转上页累计余额");

                newHead.setShowLastmoney(FinanceHelper.longToString(oldLastmoney));

                // 放入合计统计
                list.add(0, newHead);
            }
        }
        else
        {
            // 总帐
            list.clear();

            // 放入合计统计
            list.add(0, head);
        }

        list.add(currentTotal);

        list.add(allTotal);

        request.setAttribute("resultList", list);

        return mapping.findForward(QUERYTAXFINANCE1);
    }

    /**
     * 三大费用
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryTaxFinance3(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = Helper.getUser(request);

        request.getSession().setAttribute("EXPORT_FINANCEITE_KEY", QUERYTAXFINANCE3);

        List<FinanceItemVO> list = null;

        CommonTools.saveParamers(request);

        FinanceItemVO currentTotal = null;

        String parentTax = request.getParameter("parentTax");

        try
        {
            if (PageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = getQueryCondition3(request, user, 0, parentTax);

                int tatol = financeItemDAO.countVOByCondition(condtion.toString());

                PageSeparate page = new PageSeparate(tatol, 50);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, QUERYTAXFINANCE3);

                list = financeItemDAO.queryEntityVOsByCondition(condtion, page);

                // 当期合计
                currentTotal = sumCurrentTotal(request, user, condtion);
            }
            else
            {
                PageSeparateTools.processSeparate(request, QUERYTAXFINANCE3);

                list = financeItemDAO.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(
                    request, QUERYTAXFINANCE3), OldPageSeparateTools.getPageSeparate(request,
                    QUERYTAXFINANCE3));

                currentTotal = (FinanceItemVO)request.getSession().getAttribute(
                    "queryTaxFinance1_currentTotal");
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

        list.add(currentTotal);

        request.setAttribute("resultList", list);

        return mapping.findForward(QUERYTAXFINANCE3);
    }

    /**
     * 报表
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryTaxReport(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        request.getSession().setAttribute("EXPORT_FINANCEITE_KEY", QUERYTAXREPORT);

        CommonTools.saveParamers(request);

        String type = request.getParameter("type");

        String year = request.getParameter("year");

        String month = request.getParameter("month");

        String beginTimeKey = (MathTools.parseInt(year) - 1) + "12";

        String endTimeKey = year + month;

        try
        {
            List<FinanceRepVO> repList = financeRepDAO.queryEntityVOsByFK(type);

            Collections.sort(repList, new Comparator<FinanceRepVO>()
            {
                public int compare(FinanceRepVO o1, FinanceRepVO o2)
                {
                    return o1.getItemIndex() - o2.getItemIndex();
                }
            });

            // 资产负债表
            if ("0".equals(type))
            {
                for (FinanceRepVO financeRepVO : repList)
                {
                    long beginTotal = 0L;

                    long endTotal = 0L;

                    // 允许为空
                    if (StringTools.isNullOrNone(financeRepVO.getExpr()))
                    {
                        financeRepVO.setBeginMoney(beginTotal);

                        financeRepVO.setBeginMoneyStr(FinanceHelper.longToString(beginTotal));

                        financeRepVO.setEndMoney(endTotal);

                        financeRepVO.setEndMoneyStr(FinanceHelper.longToString(endTotal));

                        continue;
                    }

                    // 表达式
                    String expr = financeRepVO.getExpr();

                    List<String>[] parserExpr = FinanceHelper.parserExpr(expr);

                    List<String> taxList = parserExpr[0];

                    List<String> oprList = parserExpr[1];

                    for (int i = 0; i < taxList.size(); i++ )
                    {
                        FinanceMonthBean beginTurn = financeMonthDAO.findByUnique(taxList.get(i),
                            beginTimeKey);

                        FinanceMonthBean endTurn = financeMonthDAO.findByUnique(taxList.get(i),
                            endTimeKey);

                        // 第一个为初始化
                        if (i == 0)
                        {
                            if (beginTurn != null)
                            {
                                beginTotal = beginTurn.getLastAllTotal();
                            }

                            if (endTurn != null)
                            {
                                endTotal = endTurn.getLastAllTotal();
                            }

                            continue;
                        }

                        // 需要操作符号
                        if (i < (taxList.size() - 1))
                        {
                            String opr = oprList.get(i - 1);

                            if ("+".equals(opr))
                            {
                                if (beginTurn != null)
                                {
                                    beginTotal += beginTurn.getLastAllTotal();
                                }

                                if (endTurn != null)
                                {
                                    endTotal += endTurn.getLastAllTotal();
                                }
                            }

                            if ("-".equals(opr))
                            {
                                if (beginTurn != null)
                                {
                                    beginTotal -= beginTurn.getLastAllTotal();
                                }

                                if (endTurn != null)
                                {
                                    endTotal -= endTurn.getLastAllTotal();
                                }
                            }
                        }
                    }

                    financeRepVO.setBeginMoney(beginTotal);

                    financeRepVO.setBeginMoneyStr(FinanceHelper.longToString(beginTotal));

                    financeRepVO.setEndMoney(endTotal);

                    financeRepVO.setEndMoneyStr(FinanceHelper.longToString(endTotal));
                }
            }
            else
            {
                // 损益报表
                for (FinanceRepVO financeRepVO : repList)
                {
                    long beginTotal = 0L;

                    long endTotal = 0L;

                    // 允许为空
                    if (StringTools.isNullOrNone(financeRepVO.getExpr()))
                    {
                        financeRepVO.setBeginMoney(beginTotal);

                        financeRepVO.setBeginMoneyStr(FinanceHelper.longToString(beginTotal));

                        financeRepVO.setEndMoney(endTotal);

                        financeRepVO.setEndMoneyStr(FinanceHelper.longToString(endTotal));

                        continue;
                    }

                    // 表达式
                    String expr = financeRepVO.getExpr();

                    List<String>[] parserExpr = FinanceHelper.parserExpr(expr);

                    List<String> taxList = parserExpr[0];

                    List<String> oprList = parserExpr[1];

                    for (int i = 0; i < taxList.size(); i++ )
                    {
                        FinanceMonthBean curTurn = financeMonthDAO.findByUnique(taxList.get(i),
                            endTimeKey);

                        // 当年的累计数(1月到N月)
                        long sumMonthTurnTotal = financeMonthDAO.sumMonthTurnTotal(taxList.get(i),
                            year + "01", endTimeKey);

                        // 第一个为初始化
                        if (i == 0)
                        {
                            if (curTurn != null)
                            {
                                // 当月发生额
                                beginTotal = curTurn.getInmoneyTotal();
                            }

                            // 累计数
                            endTotal = sumMonthTurnTotal;

                            continue;
                        }

                        // 需要操作符号
                        if (i < (taxList.size() - 1))
                        {
                            String opr = oprList.get(i - 1);

                            if ("+".equals(opr))
                            {
                                if (curTurn != null)
                                {
                                    beginTotal += curTurn.getInmoneyTotal();
                                }

                                endTotal += sumMonthTurnTotal;
                            }

                            if ("-".equals(opr))
                            {
                                if (curTurn != null)
                                {
                                    beginTotal -= curTurn.getInmoneyTotal();
                                }

                                endTotal -= sumMonthTurnTotal;
                            }
                        }
                    }

                    financeRepVO.setBeginMoney(beginTotal);

                    financeRepVO.setBeginMoneyStr(FinanceHelper.longToString(beginTotal));

                    financeRepVO.setEndMoney(endTotal);

                    financeRepVO.setEndMoneyStr(FinanceHelper.longToString(endTotal));
                }

            }

            for (FinanceRepVO financeRepVO : repList)
            {
                if (financeRepVO.getRmethod() == FinaConstant.FINANCEREP_RMETHOD_ALL)
                {
                    financeRepVO.setEndMoneyChineseStr(MathTools.hangeToBig(FinanceHelper
                        .longToDouble(financeRepVO.getEndMoney())));
                }
            }

            request.getSession().setAttribute("g_tax_rep_resultList" + type, repList);

            request.getSession().setAttribute("g_tax_rep_type", type);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败");

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward(QUERYTAXREPORT + (CommonTools.parseInt(type) + 1));
    }

    /**
     * 合计出结转结余(辅助)
     * 
     * @param request
     * @param user
     * @return
     * @throws MYException
     */
    private FinanceItemVO sumHead(HttpServletRequest request, User user)
        throws MYException
    {
        // 计算出开始日期前的结余(开始日期前扫描全表,这里可以优化,如果只有科目可以查询月结表)
        FinanceMonthBean monthTurn = hasMonthTurn(request, user);

        ConditionParse preQueryCondition = null;

        if (monthTurn == null)
        {
            preQueryCondition = getQueryCondition(request, user, 1);
        }
        else
        {
            // 构建条件
            preQueryCondition = new ConditionParse();

            preQueryCondition.addWhereStr();

            // 开始日期前的结余
            String beginDate = request.getParameter("beginDate");

            TaxVO tax = (TaxVO)request.getAttribute("tax");

            // 动态级别的查询
            preQueryCondition.addCondition("FinanceItemBean.taxId" + tax.getLevel(), "=", tax
                .getId());

            preQueryCondition.addCondition("FinanceItemBean.financeDate", ">=", monthTurn
                .getDescription());

            preQueryCondition.addCondition("FinanceItemBean.financeDate", "<", beginDate);
        }

        TaxVO tax = (TaxVO)request.getAttribute("tax");

        FinanceItemVO head = sumHeadInner(monthTurn, preQueryCondition, tax);

        request.getSession().setAttribute("queryTaxFinance1_head", head);

        String beginDate = request.getParameter("beginDate");

        head.setDescription("结余(" + beginDate + "之前)");

        return head;
    }

    /**
     * sumHeadInner
     * 
     * @param preQueryCondition
     * @param tax
     * @return
     */
    private FinanceItemVO sumHeadInner(FinanceMonthBean monthTurn,
                                       ConditionParse preQueryCondition, TaxBean tax)
    {
        // 期初余额
        long last = 0L;

        FinanceItemVO head = new FinanceItemVO();

        // 借方
        long[] sumMoneryByCondition = financeItemDAO.sumMoneryByCondition(preQueryCondition);

        if (monthTurn != null)
        {
            // 通过月结查询和增量查询
            sumMoneryByCondition[0] += monthTurn.getInmoneyAllTotal();

            sumMoneryByCondition[1] += monthTurn.getOutmoneyAllTotal();
        }

        head.setInmoney(sumMoneryByCondition[0]);

        head.setOutmoney(sumMoneryByCondition[1]);

        if (tax.getForward() == TaxConstanst.TAX_FORWARD_IN)
        {
            last = sumMoneryByCondition[0] - sumMoneryByCondition[1];
        }
        else
        {
            last = sumMoneryByCondition[1] - sumMoneryByCondition[0];
        }

        head.setTaxId(tax.getId());

        fillItemVO(head);

        // 开始日期前累计余额
        head.setLastmoney(last);

        head.setShowLastmoney(FinanceHelper.longToString(last));

        return head;
    }

    /**
     * 当期合计(查询条件重复使用)
     * 
     * @param request
     * @param user
     * @param condtion
     * @return
     * @throws MYException
     */
    private FinanceItemVO sumCurrentTotal(HttpServletRequest request, User user,
                                          ConditionParse condtion)
        throws MYException
    {
        TaxVO tax = (TaxVO)request.getAttribute("tax");

        // 借方
        long[] sumMoneryByCondition = financeItemDAO.sumMoneryByCondition(condtion);

        FinanceItemVO currentTotal = new FinanceItemVO();

        currentTotal.setTaxId(tax.getId());

        if (sumMoneryByCondition[0] == sumMoneryByCondition[1])
        {
            currentTotal.setDescription("当期合计:"
                                        + FinanceHelper
                                            .longToChineseString(sumMoneryByCondition[0]));
        }
        else
        {
            currentTotal.setDescription("当期合计");
        }

        currentTotal.setInmoney(sumMoneryByCondition[0]);

        currentTotal.setOutmoney(sumMoneryByCondition[1]);

        fillItemVO(currentTotal);

        request.getSession().setAttribute("queryTaxFinance1_currentTotal", currentTotal);

        return currentTotal;
    }

    /**
     * 当前累计（借方：从当年1月到选择的结束日期的借方和，贷一样，但是余额是从帐册开始到结束时间的余额，支持辅助核算的过滤）
     * 
     * @param request
     * @param user
     * @param condtion
     * @return
     * @throws MYException
     */
    private FinanceItemVO sumAllTotal(HttpServletRequest request, User user)
        throws MYException
    {
        // 从当年1月到选择的结束日期
        ConditionParse condtion = getQueryCondition(request, user, 2);

        TaxVO tax = (TaxVO)request.getAttribute("tax");

        // 借方
        long[] sumMoneryByCondition = financeItemDAO.sumMoneryByCondition(condtion);

        FinanceItemVO allTotal = new FinanceItemVO();

        allTotal.setTaxId(tax.getId());

        allTotal.setDescription("当年累计(余额是截至查询期末余额)");

        allTotal.setInmoney(sumMoneryByCondition[0]);

        allTotal.setOutmoney(sumMoneryByCondition[1]);

        fillItemVO(allTotal);

        FinanceItemVO current = (FinanceItemVO)request.getSession().getAttribute(
            "queryTaxFinance1_currentTotal");

        // 累计的需要叠加
        FinanceItemVO head = (FinanceItemVO)request.getSession().getAttribute(
            "queryTaxFinance1_head");

        // 重新计算(结余即期末余额+当期余额)
        allTotal.setLastmoney(head.getLastmoney() + current.getLastmoney());

        allTotal.setShowLastmoney(FinanceHelper.longToString(allTotal.getLastmoney()));

        request.getSession().setAttribute("queryTaxFinance1_allTotal", allTotal);

        return allTotal;
    }

    /**
     * getQueryCondition
     * 
     * @param request
     * @param user
     * @param type
     * @return
     * @throws MYException
     */
    protected ConditionParse getQueryCondition(HttpServletRequest request, User user, int type)
        throws MYException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        if (type == 0)
        {
            String beginDate = request.getParameter("beginDate");
            condtion.addCondition("FinanceItemBean.financeDate", ">=", beginDate);

            String endDate = request.getParameter("endDate");
            condtion.addCondition("FinanceItemBean.financeDate", "<=", endDate);

            if ( !beginDate.substring(0, 4).equals(endDate.substring(0, 4)))
            {
                throw new MYException("查询不能跨年");
            }
        }

        // 结转 开始日期前的结余(整个表查询哦)
        if (type == 1)
        {
            // 开始日期前的结余
            String beginDate = request.getParameter("beginDate");

            // 这里的时间是默认的
            condtion.addCondition("FinanceItemBean.financeDate", ">", "2011-05-01");

            condtion.addCondition("FinanceItemBean.financeDate", "<", beginDate);
        }

        // 当前累计(从当年1月到选择的结束日期)
        if (type == 2)
        {
            String endDate = request.getParameter("endDate");

            // 从当年1月
            condtion.addCondition("FinanceItemBean.financeDate", ">=", endDate.substring(0, 4)
                                                                       + "-01-01");

            condtion.addCondition("FinanceItemBean.financeDate", "<=", endDate);
        }

        String taxId = request.getParameter("taxId");

        TaxVO tax = taxDAO.findVO(taxId);

        if (tax == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        request.setAttribute("tax", tax);

        request.getSession().setAttribute("queryTaxFinance1_tax", tax);

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

        String productId = request.getParameter("productId");

        if ( !StringTools.isNullOrNone(productId))
        {
            condtion.addCondition("FinanceItemBean.productId", "=", productId);
        }

        String depotId = request.getParameter("depotId");

        if ( !StringTools.isNullOrNone(depotId))
        {
            condtion.addCondition("FinanceItemBean.depotId", "=", depotId);
        }

        String unitId = request.getParameter("unitId");

        if ( !StringTools.isNullOrNone(unitId))
        {
            condtion.addCondition("FinanceItemBean.unitId", "=", unitId);
        }

        return condtion;
    }

    /**
     * 是否可以使用月结加快统计(有就就是FinanceMonthBean)
     * 
     * @param request
     * @param user
     * @return
     * @throws MYException
     */
    private FinanceMonthBean hasMonthTurn(HttpServletRequest request, User user)
        throws MYException
    {
        boolean quickQuery = parameterDAO.getBoolean(SysConfigConstant.TAX_QUICK_QUERY);

        // 不启用
        if ( !quickQuery)
        {
            return null;
        }

        String stafferId = request.getParameter("stafferId");

        String departmentId = request.getParameter("departmentId");

        String beginDate = request.getParameter("beginDate");

        String taxId = request.getParameter("taxId");

        String productId = request.getParameter("productId");

        String depotId = request.getParameter("depotId");

        String unitId = request.getParameter("unitId");

        if (StringTools.isNullOrNone(stafferId) && StringTools.isNullOrNone(departmentId)
            && StringTools.isNullOrNone(productId) && StringTools.isNullOrNone(depotId)
            && StringTools.isNullOrNone(unitId))
        {
            TaxVO tax = taxDAO.findVO(taxId);

            if (tax == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            request.setAttribute("tax", tax);

            request.getSession().setAttribute("queryTaxFinance1_tax", tax);

            Calendar cal = Calendar.getInstance();

            // 本月时间
            cal.setTime(TimeTools.getDateByFormat(beginDate, TimeTools.SHORT_FORMAT));

            // 下个月的1号
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);

            String turnMonth = TimeTools.getStringByFormat(new Date(cal.getTime().getTime()),
                "yyyyMM");

            FinanceMonthBean month = financeMonthDAO.findByUnique(taxId, turnMonth);

            if (month != null)
            {
                // 获取月初
                month.setDescription(beginDate.substring(0, 7) + "-01");
            }

            return month;
        }

        return null;
    }

    /**
     * 科目余额的导出
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward exportTaxQuery(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        String filenName = "Tax_" + TimeTools.now("MMddHHmmss") + ".csv";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WriteFile write = null;

        try
        {
            out = reponse.getOutputStream();

            write = WriteFileFactory.getMyTXTWriter();

            write.openFile(out);

            Object attribute = request.getSession().getAttribute("EXPORT_FINANCEITE_KEY");

            if (attribute == null)
            {
                return ActionTools.toError(mapping, request);
            }

            String type = attribute.toString();

            if (type.equals(QUERYTAXFINANCE2))
            {

                String queryType = request
                    .getSession()
                    .getAttribute("EXPORT_FINANCEITE_QUERYTYPE")
                    .toString();

                List<FinanceShowVO> showList = (List<FinanceShowVO>)request
                    .getSession()
                    .getAttribute("resultList_2");

                if ("0".equals(queryType))
                {
                    write.writeLine("编码,名称,期初余额,本期借方,本期贷方,借方累计,贷方累计,方向,期末余额");

                    for (FinanceShowVO each : showList)
                    {
                        WriteFileBuffer line = new WriteFileBuffer(write);

                        line.writeColumn("[" + each.getTaxId() + "]");
                        line.writeColumn(each.getTaxName());
                        line.writeColumn(changeString(each.getShowBeginAllmoney()));
                        line.writeColumn(changeString(each.getShowCurrInmoney()));
                        line.writeColumn(changeString(each.getShowCurrOutmoney()));
                        line.writeColumn(changeString(each.getShowAllInmoney()));
                        line.writeColumn(changeString(each.getShowAllOutmoney()));
                        line.writeColumn(changeString(each.getForwardName()));
                        line.writeColumn(changeString(each.getShowLastmoney()));

                        line.writeLine();
                    }
                }

                if ("1".equals(queryType))
                {
                    write.writeLine("编码,名称,职员,期初余额,本期借方,本期贷方,借方累计,贷方累计,方向,期末余额");

                    for (FinanceShowVO each : showList)
                    {
                        WriteFileBuffer line = new WriteFileBuffer(write);

                        line.writeColumn("[" + each.getTaxId() + "]");
                        line.writeColumn(each.getTaxName());
                        line.writeColumn(each.getStafferName());
                        line.writeColumn(changeString(each.getShowBeginAllmoney()));
                        line.writeColumn(changeString(each.getShowCurrInmoney()));
                        line.writeColumn(changeString(each.getShowCurrOutmoney()));
                        line.writeColumn(changeString(each.getShowAllInmoney()));
                        line.writeColumn(changeString(each.getShowAllOutmoney()));
                        line.writeColumn(changeString(each.getForwardName()));
                        line.writeColumn(changeString(each.getShowLastmoney()));

                        line.writeLine();
                    }
                }

                if ("2".equals(queryType))
                {
                    write.writeLine("编码,名称,单位,期初余额,本期借方,本期贷方,借方累计,贷方累计,方向,期末余额");

                    for (FinanceShowVO each : showList)
                    {
                        WriteFileBuffer line = new WriteFileBuffer(write);

                        line.writeColumn("[" + each.getTaxId() + "]");
                        line.writeColumn(each.getTaxName());
                        line.writeColumn(each.getUnitName());
                        line.writeColumn(changeString(each.getShowBeginAllmoney()));
                        line.writeColumn(changeString(each.getShowCurrInmoney()));
                        line.writeColumn(changeString(each.getShowCurrOutmoney()));
                        line.writeColumn(changeString(each.getShowAllInmoney()));
                        line.writeColumn(changeString(each.getShowAllOutmoney()));
                        line.writeColumn(changeString(each.getForwardName()));
                        line.writeColumn(changeString(each.getShowLastmoney()));

                        line.writeLine();
                    }
                }
            }

            write.close();

        }
        catch (Throwable e)
        {
            _logger.error(e, e);

            return null;
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                }
            }

            if (write != null)
            {

                try
                {
                    write.close();
                }
                catch (IOException e1)
                {
                }
            }

        }

        return null;
    }

    /**
     * 导出报表
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward exportTaxReport(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        String filenName = "Tax_Report_" + TimeTools.now("MMddHHmmss") + ".csv";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WriteFile write = null;

        try
        {
            out = reponse.getOutputStream();

            write = WriteFileFactory.getMyTXTWriter();

            write.openFile(out);

            // g_tax_rep_type
            Object attribute = request.getSession().getAttribute("g_tax_rep_type");

            if (attribute == null)
            {
                return ActionTools.toError(mapping, request);
            }

            String type = attribute.toString();

            List<FinanceRepVO> list = (List<FinanceRepVO>)request.getSession().getAttribute(
                "g_tax_rep_resultList" + type);

            if ("0".equals(type))
            {
                write.writeLine("行次,分类,名称,年初数,期末数,算法");

                for (FinanceRepVO each : list)
                {
                    WriteFileBuffer line = new WriteFileBuffer(write);

                    line.writeColumn(each.getItemIndex());
                    line.writeColumn(each.getItemPName());
                    line.writeColumn(each.getItemName());
                    line.writeColumn(changeString(each.getBeginMoneyStr()));
                    line.writeColumn(changeString(each.getEndMoneyStr()));
                    line.writeColumn(changeString('[' + each.getExpr() + ']'));

                    line.writeLine();
                }
            }

            if ("1".equals(type))
            {
                write.writeLine("行次,分类,名称,本初数,本年累计数,算法");

                for (FinanceRepVO each : list)
                {
                    WriteFileBuffer line = new WriteFileBuffer(write);

                    line.writeColumn(each.getItemIndex());
                    line.writeColumn(each.getItemPName());
                    line.writeColumn(each.getItemName());
                    line.writeColumn(changeString(each.getBeginMoneyStr()));
                    line.writeColumn(changeString(each.getEndMoneyStr()));
                    line.writeColumn(changeString('[' + each.getExpr() + ']'));

                    line.writeLine();
                }
            }

            write.close();

        }
        catch (Throwable e)
        {
            _logger.error(e, e);

            return null;
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                }
            }

            if (write != null)
            {

                try
                {
                    write.close();
                }
                catch (IOException e1)
                {
                }
            }

        }

        return null;
    }

    private String changeString(String str)
    {
        return str.replaceAll(",", "");
    }

    /**
     * getQueryCondition3
     * 
     * @param request
     * @param user
     * @param type
     * @return
     * @throws MYException
     */
    protected ConditionParse getQueryCondition3(HttpServletRequest request, User user, int type,
                                                String parentTax)
        throws MYException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        if (type == 0)
        {
            String year = request.getParameter("year");
            String month = request.getParameter("month");

            condtion.addCondition("FinanceItemBean.financeDate", ">=", year + "-" + month + "-01");

            condtion.addCondition("FinanceItemBean.financeDate", "<=", year + "-" + month + "-31");
        }

        String taxId = request.getParameter("taxId");

        if (StringTools.isNullOrNone(taxId))
        {
            taxId = parentTax;
        }

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
     * 科目的查询条件
     * 
     * @param request
     * @param user
     * @param type
     * @return
     * @throws MYException
     */
    protected ConditionParse getQueryCondition2(HttpServletRequest request, User user, int type)
        throws MYException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        if (type == 0)
        {
            String beginDate = request.getParameter("beginDate");

            condtion.addCondition("FinanceItemBean.financeDate", ">=", beginDate);

            String endDate = request.getParameter("endDate");

            condtion.addCondition("FinanceItemBean.financeDate", "<=", endDate);

            if ( !beginDate.substring(0, 4).equals(endDate.substring(0, 4)))
            {
                throw new MYException("查询不能跨年");
            }
        }

        // 结转 开始日期前的结余(整个表查询哦)
        if (type == 1)
        {
            // 开始日期前的结余
            String beginDate = request.getParameter("beginDate");

            // 这里的时间是默认的
            condtion.addCondition("FinanceItemBean.financeDate", ">", "2011-05-01");

            condtion.addCondition("FinanceItemBean.financeDate", "<", beginDate);
        }

        // 当前累计(从当年1月到选择的结束日期)
        if (type == 2)
        {
            String endDate = request.getParameter("endDate");

            // 从当年1月
            condtion.addCondition("FinanceItemBean.financeDate", ">=", endDate.substring(0, 4)
                                                                       + "-01-01");

            condtion.addCondition("FinanceItemBean.financeDate", "<=", endDate);
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

        String queryType = request.getParameter("queryType");

        request.getSession().setAttribute("EXPORT_FINANCEITE_KEY", QUERYTAXFINANCE2);

        request.getSession().setAttribute("EXPORT_FINANCEITE_QUERYTYPE", queryType);

        CommonTools.saveParamers(request);

        try
        {
            String taxId = request.getParameter("taxId");

            TaxVO tax = null;

            if ( !StringTools.isNullOrNone(taxId))
            {
                tax = taxDAO.findVO(taxId);
            }

            // 科目查询
            if ("0".equals(queryType))
            {
                processTaxLastQuery(request, user, tax);
            }

            // 职员查询
            if ("1".equals(queryType))
            {
                if (tax == null)
                {
                    throw new MYException("缺少科目");
                }

                processStafferLastQuery(request, user, tax);
            }

            // 单位查询
            if ("2".equals(queryType))
            {
                if (tax == null)
                {
                    throw new MYException("缺少科目");
                }

                processUnitLastQuery(request, user, tax);
            }
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward(QUERYTAXFINANCE2);
    }

    /**
     * 科目余额-科目：只有科目
     * 
     * @param request
     * @param user
     * @param tax
     * @throws MYException
     */
    private void processTaxLastQuery(HttpServletRequest request, User user, TaxVO tax)
        throws MYException
    {
        String stafferId = request.getParameter("stafferId");

        String unitId = request.getParameter("unitId");

        if ( !StringTools.isNullOrNone(stafferId) && !StringTools.isNullOrNone(unitId))
        {
            throw new MYException("科目余额查询不能同时有职员和单位");
        }

        List<TaxBean> taxList = null;

        if (tax == null)
        {
            if ( !StringTools.isNullOrNone(stafferId))
            {
                // 辅助核算
                ConditionParse con = new ConditionParse();
                con.addWhereStr();

                con.addIntCondition("staffer", "=", TaxConstanst.TAX_CHECK_YES);

                taxList = taxDAO.queryEntityBeansByCondition(con.toString());
            }
            else if ( !StringTools.isNullOrNone(unitId))
            {
                // 辅助核算
                ConditionParse con = new ConditionParse();
                con.addWhereStr();

                con.addIntCondition("unit", "=", TaxConstanst.TAX_CHECK_YES);

                taxList = taxDAO.queryEntityBeansByCondition(con.toString());
            }
            else
            {
                taxList = taxDAO.listEntityBeans("order by id");
            }
        }
        else
        {
            if (tax.getBottomFlag() == TaxConstanst.TAX_BOTTOMFLAG_ROOT)
            {
                // 查询所有的
                // 动态级别的查询
                ConditionParse taxCondition = new ConditionParse();
                taxCondition.addWhereStr();
                taxCondition.addCondition("parentId" + tax.getLevel(), "=", tax.getId());

                taxList = taxDAO.queryEntityBeansByCondition(taxCondition.toString());

                taxList.add(0, tax);
            }
            else
            {
                taxList = new ArrayList();

                taxList.add(tax);
            }
        }

        List<FinanceShowVO> showList = new ArrayList();

        request.getSession().setAttribute("resultList_2", showList);

        // 查询
        for (TaxBean taxBean : taxList)
        {
            FinanceShowVO show = new FinanceShowVO(0);

            show.setTaxId(taxBean.getId());

            show.setTaxName(taxBean.getName());

            show.setForwardName(FinanceHelper.getForwardName(taxBean));

            // 本期借方/贷方
            ConditionParse condtion = getQueryCondition2(request, user, 0);

            createTaxQuery(request, taxBean, condtion);

            long[] sumCurrMoneryByCondition = financeItemDAO.sumMoneryByCondition(condtion);

            show.setShowCurrInmoney(FinanceHelper.longToString(sumCurrMoneryByCondition[0]));
            show.setShowCurrOutmoney(FinanceHelper.longToString(sumCurrMoneryByCondition[1]));

            // 期初余额
            condtion = getQueryCondition2(request, user, 1);

            createTaxQuery(request, taxBean, condtion);

            FinanceItemVO head = sumHeadInner(null, condtion, taxBean);

            show.setShowBeginAllmoney(FinanceHelper.longToString(head.getLastmoney()));

            // 当前累计(从当年1月到选择的结束日期)
            condtion = getQueryCondition2(request, user, 2);

            createTaxQuery(request, taxBean, condtion);

            long[] sumAllMoneryByCondition = financeItemDAO.sumMoneryByCondition(condtion);

            show.setShowAllInmoney(FinanceHelper.longToString(sumAllMoneryByCondition[0]));
            show.setShowAllOutmoney(FinanceHelper.longToString(sumAllMoneryByCondition[1]));

            // 期末余额
            long currentLast = 0L;

            if (taxBean.getForward() == TaxConstanst.TAX_FORWARD_IN)
            {
                currentLast = sumCurrMoneryByCondition[0] - sumCurrMoneryByCondition[1];
            }
            else
            {
                currentLast = sumCurrMoneryByCondition[1] - sumCurrMoneryByCondition[0];
            }

            // 期初+当期发生=期末
            show.setShowLastmoney(FinanceHelper.longToString(head.getLastmoney() + currentLast));

            showList.add(show);
        }
    }

    /**
     * createTaxQuery
     * 
     * @param request
     * @param taxBean
     * @param condtion
     */
    private void createTaxQuery(HttpServletRequest request, TaxBean taxBean, ConditionParse condtion)
    {
        condtion.addCondition("FinanceItemBean.taxId" + taxBean.getLevel(), "=", taxBean.getId());

        String stafferId = request.getParameter("stafferId");

        if ( !StringTools.isNullOrNone(stafferId))
        {
            condtion.addCondition("FinanceItemBean.stafferId", "=", stafferId);
        }

        String unitId = request.getParameter("unitId");

        if ( !StringTools.isNullOrNone(unitId))
        {
            condtion.addCondition("FinanceItemBean.unitId", "=", unitId);
        }
    }

    /**
     * 单位么科目余额查询
     * 
     * @param request
     * @param user
     * @param tax
     * @throws MYException
     */
    private void processUnitLastQuery(HttpServletRequest request, User user, TaxVO taxBean)
        throws MYException
    {
        List<FinanceShowVO> showList = new ArrayList();

        request.getSession().setAttribute("resultList_2", showList);

        // 这里的查询分为职员下所有单位的查询,或者查询指定的一个单位

        String stafferId = request.getParameter("stafferId");

        String unitId = request.getParameter("unitId");

        List<String> unitList = new ArrayList();

        String beginDate = request.getParameter("beginDate");

        String endDate = request.getParameter("endDate");

        // 查询名下所有的单位(过滤掉查询范围内没有出现的单位)
        if ( !StringTools.isNullOrNone(stafferId) && StringTools.isNullOrNone(unitId))
        {
            // 先查询出本期发生的单位
            unitList.addAll(financeItemDAO.queryDistinctUnitByStafferId(stafferId, beginDate,
                endDate));
        }
        else if (StringTools.isNullOrNone(stafferId) && StringTools.isNullOrNone(unitId))
        {
            // 先查询出本期发生的单位
            unitList.addAll(financeItemDAO.queryDistinctUnit(beginDate, endDate));
        }
        else
        {
            // 只有一个单位
            unitList.add(unitId);
        }

        // 查询每个单位
        for (String eachUnitId : unitList)
        {
            if (StringTools.isNullOrNone(eachUnitId))
            {
                continue;
            }

            // 查询
            FinanceShowVO show = new FinanceShowVO(2);

            show.setTaxId(taxBean.getId());

            show.setTaxName(taxBean.getName());

            show.setForwardName(FinanceHelper.getForwardName(taxBean));

            UnitBean unitBean = unitDAO.find(eachUnitId);

            if (unitBean != null)
            {
                show.setUnitName(unitBean.getName());
            }

            // 本期借方/贷方
            ConditionParse condtion = getQueryCondition2(request, user, 0);

            createUnitCondition(taxBean, eachUnitId, condtion);

            long[] sumCurrMoneryByCondition = financeItemDAO.sumMoneryByCondition(condtion);

            show.setCurrInmoney(sumCurrMoneryByCondition[0]);
            show.setShowCurrInmoney(FinanceHelper.longToString(sumCurrMoneryByCondition[0]));
            show.setCurrOutmoney(sumCurrMoneryByCondition[1]);
            show.setShowCurrOutmoney(FinanceHelper.longToString(sumCurrMoneryByCondition[1]));

            // 期初余额
            condtion = getQueryCondition2(request, user, 1);
            createUnitCondition(taxBean, eachUnitId, condtion);

            FinanceItemVO head = sumHeadInner(null, condtion, taxBean);

            show.setBeginAllmoney(head.getLastmoney());
            show.setShowBeginAllmoney(FinanceHelper.longToString(head.getLastmoney()));

            // 当前累计(从当年1月到选择的结束日期)
            condtion = getQueryCondition2(request, user, 2);
            createUnitCondition(taxBean, eachUnitId, condtion);

            long[] sumAllMoneryByCondition = financeItemDAO.sumMoneryByCondition(condtion);

            show.setAllInmoney(sumAllMoneryByCondition[0]);
            show.setShowAllInmoney(FinanceHelper.longToString(sumAllMoneryByCondition[0]));
            show.setAllOutmoney(sumAllMoneryByCondition[1]);
            show.setShowAllOutmoney(FinanceHelper.longToString(sumAllMoneryByCondition[1]));

            // 期末余额
            long currentLast = 0L;

            if (taxBean.getForward() == TaxConstanst.TAX_FORWARD_IN)
            {
                currentLast = sumCurrMoneryByCondition[0] - sumCurrMoneryByCondition[1];
            }
            else
            {
                currentLast = sumCurrMoneryByCondition[1] - sumCurrMoneryByCondition[0];
            }

            show.setLastmoney(head.getLastmoney() + currentLast);
            show.setShowLastmoney(FinanceHelper.longToString(head.getLastmoney() + currentLast));

            showList.add(show);
        }

        // 合计
        FinanceShowVO total = new FinanceShowVO();

        total.setTaxId("合计");

        total.setForwardName(FinanceHelper.getForwardName(taxBean));

        for (FinanceShowVO financeShowVO : showList)
        {
            total.setBeginAllmoney(financeShowVO.getBeginAllmoney() + total.getBeginAllmoney());

            total.setCurrInmoney(financeShowVO.getCurrInmoney() + total.getCurrInmoney());
            total.setCurrOutmoney(financeShowVO.getCurrOutmoney() + total.getCurrOutmoney());

            total.setAllInmoney(financeShowVO.getAllInmoney() + total.getAllInmoney());
            total.setAllOutmoney(financeShowVO.getAllOutmoney() + total.getAllOutmoney());

            total.setLastmoney(financeShowVO.getLastmoney() + total.getLastmoney());
        }

        total.setShowBeginAllmoney(FinanceHelper.longToString(total.getBeginAllmoney()));
        total.setShowCurrInmoney(FinanceHelper.longToString(total.getCurrInmoney()));
        total.setShowCurrOutmoney(FinanceHelper.longToString(total.getCurrOutmoney()));

        total.setShowAllInmoney(FinanceHelper.longToString(total.getAllInmoney()));
        total.setShowAllOutmoney(FinanceHelper.longToString(total.getAllOutmoney()));

        total.setShowLastmoney(FinanceHelper.longToString(total.getLastmoney()));

        showList.add(total);
    }

    /**
     * processStafferLastQuery
     * 
     * @param request
     * @param user
     * @param taxBean
     * @throws MYException
     */
    private void processStafferLastQuery(HttpServletRequest request, User user, TaxVO taxBean)
        throws MYException
    {
        List<FinanceShowVO> showList = new ArrayList();

        request.getSession().setAttribute("resultList_2", showList);

        // 这里的查询分为职员下所有单位的查询,或者查询指定的一个单位

        String stafferId = request.getParameter("stafferId");

        List<String> stafferList = new ArrayList();

        // 查询名下所有的单位(过滤掉查询范围内没有出现的单位)
        if (StringTools.isNullOrNone(stafferId))
        {
            String beginDate = request.getParameter("beginDate");

            String endDate = request.getParameter("endDate");

            // 先查询出本期发生的单位
            stafferList.addAll(financeItemDAO.queryDistinctStafferId(beginDate, endDate));
        }
        else
        {
            // 只有一个单位
            stafferList.add(stafferId);
        }

        // 查询每个职员
        for (String eachStafferId : stafferList)
        {
            if (StringTools.isNullOrNone(eachStafferId))
            {
                continue;
            }

            // 查询
            FinanceShowVO show = new FinanceShowVO(1);

            show.setTaxId(taxBean.getId());

            show.setTaxName(taxBean.getName());

            show.setForwardName(FinanceHelper.getForwardName(taxBean));

            StafferBean staffer = stafferDAO.find(eachStafferId);

            if (staffer != null)
            {
                show.setStafferName(staffer.getName());
            }

            // 本期借方/贷方
            ConditionParse condtion = getQueryCondition2(request, user, 0);
            createStafferCondition(taxBean, eachStafferId, condtion);

            long[] sumCurrMoneryByCondition = financeItemDAO.sumMoneryByCondition(condtion);

            show.setCurrInmoney(sumCurrMoneryByCondition[0]);
            show.setShowCurrInmoney(FinanceHelper.longToString(sumCurrMoneryByCondition[0]));

            show.setCurrOutmoney(sumCurrMoneryByCondition[1]);
            show.setShowCurrOutmoney(FinanceHelper.longToString(sumCurrMoneryByCondition[1]));

            // 期初余额
            condtion = getQueryCondition2(request, user, 1);

            createStafferCondition(taxBean, eachStafferId, condtion);

            FinanceItemVO head = sumHeadInner(null, condtion, taxBean);

            show.setBeginAllmoney(head.getLastmoney());
            show.setShowBeginAllmoney(FinanceHelper.longToString(head.getLastmoney()));

            // 当前累计(从当年1月到选择的结束日期)
            condtion = getQueryCondition2(request, user, 2);
            createStafferCondition(taxBean, eachStafferId, condtion);

            long[] sumAllMoneryByCondition = financeItemDAO.sumMoneryByCondition(condtion);

            show.setAllInmoney(sumAllMoneryByCondition[0]);
            show.setShowAllInmoney(FinanceHelper.longToString(sumAllMoneryByCondition[0]));
            show.setAllOutmoney(sumAllMoneryByCondition[1]);
            show.setShowAllOutmoney(FinanceHelper.longToString(sumAllMoneryByCondition[1]));

            // 期末余额
            long currentLast = 0L;

            if (taxBean.getForward() == TaxConstanst.TAX_FORWARD_IN)
            {
                currentLast = sumCurrMoneryByCondition[0] - sumCurrMoneryByCondition[1];
            }
            else
            {
                currentLast = sumCurrMoneryByCondition[1] - sumCurrMoneryByCondition[0];
            }

            show.setLastmoney(head.getLastmoney() + currentLast);
            show.setShowLastmoney(FinanceHelper.longToString(head.getLastmoney() + currentLast));

            showList.add(show);
        }

        // 合计
        FinanceShowVO total = new FinanceShowVO();

        total.setTaxId("合计");

        total.setForwardName(FinanceHelper.getForwardName(taxBean));

        for (FinanceShowVO financeShowVO : showList)
        {
            total.setBeginAllmoney(financeShowVO.getBeginAllmoney() + total.getBeginAllmoney());

            total.setCurrInmoney(financeShowVO.getCurrInmoney() + total.getCurrInmoney());
            total.setCurrOutmoney(financeShowVO.getCurrOutmoney() + total.getCurrOutmoney());

            total.setAllInmoney(financeShowVO.getAllInmoney() + total.getAllInmoney());
            total.setAllOutmoney(financeShowVO.getAllOutmoney() + total.getAllOutmoney());

            total.setLastmoney(financeShowVO.getLastmoney() + total.getLastmoney());
        }

        total.setShowBeginAllmoney(FinanceHelper.longToString(total.getBeginAllmoney()));
        total.setShowCurrInmoney(FinanceHelper.longToString(total.getCurrInmoney()));
        total.setShowCurrOutmoney(FinanceHelper.longToString(total.getCurrOutmoney()));

        total.setShowAllInmoney(FinanceHelper.longToString(total.getAllInmoney()));
        total.setShowAllOutmoney(FinanceHelper.longToString(total.getAllOutmoney()));

        total.setShowLastmoney(FinanceHelper.longToString(total.getLastmoney()));

        showList.add(total);
    }

    /**
     * createUnitCondition
     * 
     * @param taxBean
     * @param eachUnitId
     * @param condtion
     */
    private void createUnitCondition(TaxVO taxBean, String eachUnitId, ConditionParse condtion)
    {
        condtion.addCondition("FinanceItemBean.taxId" + taxBean.getLevel(), "=", taxBean.getId());

        condtion.addCondition("FinanceItemBean.unitId", "=", eachUnitId);
    }

    /**
     * createStafferCondition
     * 
     * @param taxBean
     * @param stafferId
     * @param condtion
     */
    private void createStafferCondition(TaxVO taxBean, String stafferId, ConditionParse condtion)
    {
        condtion.addCondition("FinanceItemBean.taxId" + taxBean.getLevel(), "=", taxBean.getId());

        condtion.addCondition("FinanceItemBean.stafferId", "=", stafferId);
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

        item.setLastmoney(last);

        item.setShowLastmoney(FinanceHelper.longToString(last));

        item.getShowChineseInmoney();
        item.getShowChineseOutmoney();
        item.getShowChineseLastmoney();
    }

    /**
     * @return the orgManager
     */
    public OrgManager getOrgManager()
    {
        return orgManager;
    }

    /**
     * @param orgManager
     *            the orgManager to set
     */
    public void setOrgManager(OrgManager orgManager)
    {
        this.orgManager = orgManager;
    }

    /**
     * @return the parameterDAO
     */
    public ParameterDAO getParameterDAO()
    {
        return parameterDAO;
    }

    /**
     * @param parameterDAO
     *            the parameterDAO to set
     */
    public void setParameterDAO(ParameterDAO parameterDAO)
    {
        this.parameterDAO = parameterDAO;
    }

    /**
     * @return the financeRepDAO
     */
    public FinanceRepDAO getFinanceRepDAO()
    {
        return financeRepDAO;
    }

    /**
     * @param financeRepDAO
     *            the financeRepDAO to set
     */
    public void setFinanceRepDAO(FinanceRepDAO financeRepDAO)
    {
        this.financeRepDAO = financeRepDAO;
    }

}
