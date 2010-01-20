package com.china.centet.yongyin.action;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.common.OldPageSeparateTools;
import com.china.center.eltools.ElTools;
import com.china.center.jdbc.inter.PublicSQL;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.CommonTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.Helper;
import com.china.centet.yongyin.bean.BankBean;
import com.china.centet.yongyin.bean.Bill;
import com.china.centet.yongyin.bean.ProviderBean;
import com.china.centet.yongyin.bean.Role;
import com.china.centet.yongyin.bean.StatBean;
import com.china.centet.yongyin.bean.helper.LocationHelper;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.constant.StockConstant;
import com.china.centet.yongyin.dao.BankDAO;
import com.china.centet.yongyin.dao.BaseBeanDAO;
import com.china.centet.yongyin.dao.BillDAO;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.CustomerDAO;
import com.china.centet.yongyin.dao.OutDAO;
import com.china.centet.yongyin.dao.ProviderDAO;
import com.china.centet.yongyin.dao.StatDAO;
import com.china.centet.yongyin.dao.StockItemDAO;
import com.china.centet.yongyin.trigger.Trigger;
import com.china.centet.yongyin.vo.StockItemBeanVO;


public class StatAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private BillDAO billDAO = null;

    private CommonDAO commonDAO = null;

    private CustomerDAO customerDAO = null;

    private ProviderDAO providerDAO = null;

    private OutDAO outDAO = null;

    private BankDAO bankDAO = null;

    private StatDAO statDAO = null;

    private Trigger trigger = null;

    private PublicSQL publicSQL = null;

    private BaseBeanDAO baseBeanDAO = null;

    private StockItemDAO stockItemDAO = null;

    /**
     * 本月的日结
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryCurrentStat(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String frist = request.getParameter("load");

        String statId = "";
        ConditionParse condtion = new ConditionParse();
        if ("1".equals(frist))
        {
            statId = TimeTools.now("yyyyMM");

            condtion.addCondition("statId", "=", statId);
            request.setAttribute("statId", statId);

            // 统计当月的存入session
            statCurrent(request);
        }
        else
        {
            String bank = request.getParameter("bank");

            if ( !StringTools.isNullOrNone(bank))
            {
                Map<String, StatBean> map = new HashMap();

                Map<String, StatBean> maps = (Map)request.getSession().getAttribute("statBanMap");

                map.put(bank, maps.get(bank));

                request.setAttribute("statBanMap", map);
            }
        }

        return mapping.findForward("statList");
    }

    /**
     * 查询历史统计
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryHistoryStat(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        setCondition(request, condtion);

        try
        {
            List<StatBean> statList = statDAO.queryStatByCondition(condtion);

            request.setAttribute("statList", statList);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败");

            _logger.error("queryStat", e);

            return mapping.findForward("error");
        }

        return mapping.findForward("statHistoryList");
    }

    /**
     * 统计供应商
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward stacProvider(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        String beginTime = request.getParameter("alogTime");

        String endTime = request.getParameter("blogTime");

        List<StockItemBeanVO> list = null;

        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                if (StringTools.isNullOrNone(beginTime))
                {
                    beginTime = TimeTools.getMonthBegin();

                    endTime = TimeTools.getDateShortString(1);

                    request.setAttribute("alogTime", beginTime);

                    request.setAttribute("blogTime", endTime);

                }
                request.getSession().setAttribute("StockItemBeanVO_beginTime", beginTime);
                request.getSession().setAttribute("StockItemBeanVO_endTime", endTime);

                int total = stockItemDAO.countStatStockItem(beginTime, endTime);

                PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, "stacProvider");

                list = stockItemDAO.queryStatStockItemVO(beginTime, endTime, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "stacProvider");

                beginTime = (String)request.getSession().getAttribute("StockItemBeanVO_beginTime");

                endTime = (String)request.getSession().getAttribute("StockItemBeanVO_endTime");

                list = stockItemDAO.queryStatStockItemVO(beginTime, endTime,
                    OldPageSeparateTools.getPageSeparate(request, "stacProvider"));
            }

            for (StockItemBeanVO stockItemBeanVO : list)
            {
                float total = baseBeanDAO.queryTotalBaseByCondition(
                    stockItemBeanVO.getProviderId(), beginTime, endTime);

                stockItemBeanVO.setTotal(stockItemBeanVO.getTotal() + total);
            }

            request.setAttribute("list", list);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "统计失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("statProvider");
    }

    public ActionForward exportAll(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        CommonTools.saveParamers(request);

        String beginTime = request.getParameter("alogTime");

        String endTime = request.getParameter("blogTime");

        int total = stockItemDAO.countStatStockItem(beginTime, endTime);

        // 只有总部的总经理才能导出
        if ( ! (Helper.getUser(request).getRole() == Role.MANAGER && LocationHelper.isSystemLocation(Helper.getCurrentLocationId(request))))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "敏感数据,只有总部的总经理才能导出");

            return mapping.findForward("error");
        }

        if (total > 150)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "导出的供应商数不能超过150");

            return mapping.findForward("error");
        }

        PageSeparate page = new PageSeparate(total, Constant.PAGE_EXPORT);

        String filenName = "StockItem_" + TimeTools.now("MMddHHmmss") + ".xls";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WritableWorkbook wwb = null;

        WritableSheet ws = null;

        try
        {
            out = reponse.getOutputStream();

            wwb = Workbook.createWorkbook(out);
            ws = wwb.createSheet(beginTime + "到" + endTime, 0);
            int i = 0, j = 0;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            WritableCellFormat format = new WritableCellFormat(font);

            ws.addCell(new Label(j++ , i, "时间", format));
            ws.addCell(new Label(j++ , i, "供应商", format));
            ws.addCell(new Label(j++ , i, "产品", format));
            ws.addCell(new Label(j++ , i, "产品编码", format));
            ws.addCell(new Label(j++ , i, "单价", format));
            ws.addCell(new Label(j++ , i, "数量", format));
            ws.addCell(new Label(j++ , i, "金额", format));

            boolean fr = true;

            while (fr || page.nextPage())
            {
                List<StockItemBeanVO> statList = stockItemDAO.queryStatStockItemVO(beginTime,
                    endTime, page);

                for (StockItemBeanVO item : statList)
                {
                    List<StockItemBeanVO> voList = queryInner(request, beginTime, endTime,
                        item.getProviderId());

                    for (StockItemBeanVO stockItemBeanVO : voList)
                    {
                        j = 0;

                        i++ ;

                        ws.addCell(new Label(j++ , i, stockItemBeanVO.getLogTime()));
                        ws.addCell(new Label(j++ , i, item.getProviderName()));
                        ws.addCell(new Label(j++ , i, stockItemBeanVO.getProductName()));
                        ws.addCell(new Label(j++ , i, stockItemBeanVO.getProductCode()));
                        ws.addCell(new Label(j++ , i,
                            String.valueOf(ElTools.formatNum(stockItemBeanVO.getPrice()))));
                        ws.addCell(new Label(j++ , i, String.valueOf(stockItemBeanVO.getAmount())));
                        ws.addCell(new Label(j++ , i,
                            String.valueOf(ElTools.formatNum(stockItemBeanVO.getTotal()))));
                    }
                }

                fr = false;
            }

            wwb.write();

        }
        catch (Exception e)
        {
            return null;
        }
        finally
        {
            if (wwb != null)
            {
                try
                {
                    wwb.close();
                }
                catch (Exception e1)
                {}
            }
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {}
            }
        }

        return null;
    }

    private List<StockItemBeanVO> queryInner(HttpServletRequest request, String beginTime,
                                             String endTime, String providerId)
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        setQueryStockItemCondition(beginTime, endTime, providerId, condtion);

        int total = stockItemDAO.countVOBycondition(condtion.toString());

        return stockItemDAO.queryEntityVOsByLimit(condtion, total);
    }

    /**
     * @param beginTime
     * @param endTime
     * @param providerId
     * @param condtion
     */
    private void setQueryStockItemCondition(String beginTime, String endTime, String providerId,
                                            ConditionParse condtion)
    {
        condtion.addCondition("StockItemBean.logTime", ">=", beginTime);

        condtion.addCondition("StockItemBean.logTime", "<=", endTime);

        condtion.addIntCondition("StockItemBean.status", "=", StockConstant.STOCK_ITEM_STATUS_END);

        condtion.addCondition("StockItemBean.providerId", "=", providerId);
    }

    /**
     * 供应商统计明细
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryStockItemForStacProvider(ActionMapping mapping, ActionForm form,
                                                       HttpServletRequest request,
                                                       HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        List<StockItemBeanVO> list = null;

        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                setConditionForStacProvider(condtion, request);

                int total = stockItemDAO.countVOBycondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, Constant.PAGE_SIZE * 2);

                OldPageSeparateTools.initPageSeparate(condtion, page, request,
                    "queryStockItemForStacProvider");

                list = stockItemDAO.queryEntityVOsBycondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryStockItemForStacProvider");

                list = stockItemDAO.queryEntityVOsBycondition(OldPageSeparateTools.getCondition(
                    request, "queryStockItemForStacProvider"), OldPageSeparateTools.getPageSeparate(
                    request, "queryStockItemForStacProvider"));
            }

            String providerId = request.getParameter("providerId");

            // 供应商
            ProviderBean customer = providerDAO.find(providerId);

            request.setAttribute("list", list);

            request.setAttribute("customer", customer);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("listStockItemForStat");
    }

    /**
     * 供应商统计明细(采购退货的)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryStockItemForStacBase(ActionMapping mapping, ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        List<StockItemBeanVO> list = null;

        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                setConditionForStacProvider(condtion, request);

                int total = stockItemDAO.countVOBycondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, Constant.PAGE_SIZE
                                                            + Constant.PAGE_SIZE);

                OldPageSeparateTools.initPageSeparate(condtion, page, request,
                    "queryStockItemForStacBase");

                list = stockItemDAO.queryEntityVOsBycondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "queryStockItemForStacBase");

                list = stockItemDAO.queryEntityVOsBycondition(OldPageSeparateTools.getCondition(
                    request, "queryStockItemForStacBase"), OldPageSeparateTools.getPageSeparate(
                    request, "queryStockItemForStacBase"));
            }

            request.setAttribute("list", list);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("queryStockItemForStat");
    }

    /**
     * 设置供应商统计的过滤item的条件
     * 
     * @param condtion
     * @param request
     */
    private void setConditionForStacProvider(ConditionParse condtion, HttpServletRequest request)
    {
        String beginTime = request.getParameter("alogTime");

        String endTime = request.getParameter("blogTime");

        if (StringTools.isNullOrNone(beginTime))
        {
            beginTime = TimeTools.getMonthBegin();

            endTime = TimeTools.now_short();
        }

        String providerId = request.getParameter("providerId");

        setQueryStockItemCondition(beginTime, endTime, providerId, condtion);
    }

    /**
     * 银行资金的统计
     * 
     * @param request
     */
    private void statCurrent(HttpServletRequest request)
    {
        // 进行统计，根据银行统计
        List<BankBean> bankList = null;

        // 对于本区域的内部银行过滤
        if (LocationHelper.isSystemLocation(Helper.getCurrentLocationId(request)))
        {
            bankList = bankDAO.listEntityBeans();
        }
        else
        {
            ConditionParse condition = new ConditionParse();
            condition.addCondition("locationId", "=", Helper.getCurrentLocationId(request));
            bankList = bankDAO.queryEntityBeansBycondition(condition);
        }
        ConditionParse condtion = new ConditionParse();

        String begin = TimeTools.now("yyyy-MM") + "-01";
        String end = TimeTools.now("yyyy-MM-dd");

        // 获得上个月是否统计
        Calendar cal = Calendar.getInstance();

        String lastId = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()), "yyyyMM");

        cal.add(Calendar.MONTH, -1);

        String llastId = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()), "yyyyMM");

        List<Bill> billList = null;

        Map<String, StatBean> map = new HashMap();
        for (BankBean temp : bankList)
        {
            condtion.addWhereStr();
            condtion.addCommonCondition("dates", ">=", publicSQL.to_date(begin, "yyyy-MM-dd"));

            condtion.addCommonCondition("dates", "<=", publicSQL.to_date(end, "yyyy-MM-dd"));

            condtion.addCondition("bank", "=", temp.getId());

            billList = billDAO.queryBillByCondition(condtion);

            // 调用trigger的统计
            map.put(temp.getName(), trigger.statBank(billList, lastId, llastId, temp.getName(),
                false));

            // 清除查询条件
            condtion.clear();
        }

        request.getSession().setAttribute("statBanMap", map);

        request.getSession().setAttribute("bankList", bankList);
    }

    public void setCondition(HttpServletRequest request, ConditionParse condtion)
    {
        String frist = request.getParameter("frist");

        if ("1".equals(frist))
        {
            // 获得上个月是否统计
            Calendar cal = Calendar.getInstance();

            cal.add(Calendar.MONTH, -1);

            int month = cal.get(Calendar.MONTH) + 1;

            String statId = null;
            if (month >= 10)
            {
                statId = String.valueOf(cal.get(Calendar.YEAR)) + String.valueOf(month);
            }
            else
            {
                statId = String.valueOf(cal.get(Calendar.YEAR)) + "0" + String.valueOf(month);
            }

            if ( !StringTools.isNullOrNone(statId))
            {
                condtion.addCondition("statId", "=", statId);
            }

            request.setAttribute("statId", statId);
        }
        else
        {
            String statId = request.getParameter("statId");

            if ( !StringTools.isNullOrNone(statId))
            {
                condtion.addCondition("statId", "=", statId);
            }

            String bank = request.getParameter("bank");

            if ( !StringTools.isNullOrNone(bank))
            {
                condtion.addCondition("bank", "=", bank);
            }
        }
    }

    /**
     * @return the billDAO
     */
    public BillDAO getBillDAO()
    {
        return billDAO;
    }

    /**
     * @param billDAO
     *            the billDAO to set
     */
    public void setBillDAO(BillDAO billDAO)
    {
        this.billDAO = billDAO;
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
     * @return the customerDAO
     */
    public CustomerDAO getCustomerDAO()
    {
        return customerDAO;
    }

    /**
     * @param customerDAO
     *            the customerDAO to set
     */
    public void setCustomerDAO(CustomerDAO customerDAO)
    {
        this.customerDAO = customerDAO;
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
     * @return the statDAO
     */
    public StatDAO getStatDAO()
    {
        return statDAO;
    }

    /**
     * @param statDAO
     *            the statDAO to set
     */
    public void setStatDAO(StatDAO statDAO)
    {
        this.statDAO = statDAO;
    }

    /**
     * @return the trigger
     */
    public Trigger getTrigger()
    {
        return trigger;
    }

    /**
     * @param trigger
     *            the trigger to set
     */
    public void setTrigger(Trigger trigger)
    {
        this.trigger = trigger;
    }

    /**
     * @return the publicSQL
     */
    public PublicSQL getPublicSQL()
    {
        return publicSQL;
    }

    /**
     * @param publicSQL
     *            the publicSQL to set
     */
    public void setPublicSQL(PublicSQL publicSQL)
    {
        this.publicSQL = publicSQL;
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
     * @return the baseBeanDAO
     */
    public BaseBeanDAO getBaseBeanDAO()
    {
        return baseBeanDAO;
    }

    /**
     * @param baseBeanDAO
     *            the baseBeanDAO to set
     */
    public void setBaseBeanDAO(BaseBeanDAO baseBeanDAO)
    {
        this.baseBeanDAO = baseBeanDAO;
    }

    /**
     * @return the stockItemDAO
     */
    public StockItemDAO getStockItemDAO()
    {
        return stockItemDAO;
    }

    /**
     * @param stockItemDAO
     *            the stockItemDAO to set
     */
    public void setStockItemDAO(StockItemDAO stockItemDAO)
    {
        this.stockItemDAO = stockItemDAO;
    }

    /**
     * @return the providerDAO
     */
    public ProviderDAO getProviderDAO()
    {
        return providerDAO;
    }

    /**
     * @param providerDAO
     *            the providerDAO to set
     */
    public void setProviderDAO(ProviderDAO providerDAO)
    {
        this.providerDAO = providerDAO;
    }

}
