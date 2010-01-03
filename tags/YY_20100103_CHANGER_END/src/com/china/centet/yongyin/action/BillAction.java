package com.china.centet.yongyin.action;


import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.common.OldPageSeparateTools;
import com.china.center.jdbc.inter.PublicSQL;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.ParamterMap;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.Helper;
import com.china.centet.yongyin.bean.BankBean;
import com.china.centet.yongyin.bean.BaseBean;
import com.china.centet.yongyin.bean.Bill;
import com.china.centet.yongyin.bean.LocationBean;
import com.china.centet.yongyin.bean.OutBean;
import com.china.centet.yongyin.bean.helper.LocationHelper;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.dao.BillDAO;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.CustomerDAO;
import com.china.centet.yongyin.dao.OutDAO;
import com.china.centet.yongyin.manager.BankManager;
import com.china.centet.yongyin.manager.LocationManager;
import com.china.centet.yongyin.manager.OutManager;


/**
 * BILL action
 * 
 * @author Administrator
 */
public class BillAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private final Log logger1 = LogFactory.getLog("sec");

    private BillDAO billDAO = null;

    private CommonDAO commonDAO = null;

    private CustomerDAO customerDAO = null;

    private BankManager bankManager = null;

    private LocationManager locationManager = null;

    private PublicSQL publicSQL = null;

    private OutManager outManager = null;

    private OutDAO outDAO = null;

    public ActionForward queryBill(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String firstLoad = request.getParameter("firstLoad");

        String locationId = Helper.getCurrentLocationId(request);

        String forward = (String)request.getAttribute("forward");

        // 从树节点进来和增加界面
        ConditionParse condtion = new ConditionParse();

        if ( !"0".equals(locationId))
        {
            condtion.addCondition("locationId", "=", locationId);
        }

        List<BankBean> list1 = null;
        if (Constant.SYSTEM_LOCATION.equals(Helper.getCurrentLocationId(request)))
        {
            list1 = bankManager.listBank();
        }
        else
        {
            list1 = bankManager.queryBankByLocationId(Helper.getCurrentLocationId(request));
        }

        request.setAttribute("bankList", list1);

        List<Bill> list = null;

        // 分页处理
        if (OldPageSeparateTools.isFirstLoad(request))
        {
            if ("1".equals(firstLoad))
            {
                Calendar cal = Calendar.getInstance();

                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - 1);

                String now = TimeTools.getStringByFormat(new Date(), "yyyy-MM-dd");

                String now1 = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()),
                    "yyyy-MM-dd");

                condtion.addCommonCondition("dates", ">=", publicSQL.to_date(now1, "yyyy-MM-dd"));

                condtion.addCommonCondition("dates", "<=", publicSQL.to_date(now, "yyyy-MM-dd"));

                request.setAttribute("date", now1);

                request.setAttribute("date1", now);
            }

            // 从查询界面和删除界面过来
            if ("2".equals(firstLoad) || "2".equals(forward) || "1".equals(forward))
            {
                setCondition(request, condtion, forward);
            }

            int count = billDAO.countBillByCondition(condtion);

            PageSeparate page = new PageSeparate(count, 15);

            OldPageSeparateTools.initPageSeparate(condtion, page, request, "queryBill");

            list = billDAO.queryBillByCondition(condtion, page);
        }
        else
        {
            OldPageSeparateTools.processSeparate(request, "queryBill");

            list = billDAO.queryBillByCondition(OldPageSeparateTools.getCondition(request,
                "queryBill"), OldPageSeparateTools.getPageSeparate(request, "queryBill"));
        }

        for (Bill bill : list)
        {
            BankBean bb = bankManager.findBankById(bill.getBank());

            if (bb != null)
            {
                bill.setBank(bb.getName());
            }
        }

        request.getSession().setAttribute("billList", list);

        request.setAttribute("now", TimeTools.now("yyyy-MM-dd"));

        CommonTools.saveParamers(request);

        return mapping.findForward("billList");
    }

    /**
     * 模式窗口里面的bill
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryBillForRpt(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        // 查询在途的单据
        ConditionParse condtion = new ConditionParse();

        condtion.addCondition("destLocationId", "=", Helper.getCurrentLocationId(request));

        condtion.addIntCondition("inway", "=", Constant.IN_WAY);

        List<Bill> list1 = billDAO.queryBillByCondition(condtion);

        for (Bill bill : list1)
        {
            BankBean bb = bankManager.findBankById(bill.getBank());

            if (bb != null)
            {
                bill.setBank(bb.getName());
            }

            // 目的银行
            if ( !StringTools.isNullOrNone(bill.getDestBank()))
            {
                // 借用Receipt
                bill.setReceipt(bill.getDestBank());
                bb = bankManager.findBankById(bill.getDestBank());

                if (bb != null)
                {
                    bill.setDestBank(bb.getName());
                }
            }

        }

        request.getSession().setAttribute("billList", list1);

        return mapping.findForward("rptBill");
    }

    public ActionForward export(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {

        OutputStream out = null;

        String flag = (String)request.getSession().getAttribute("flag");

        List<Bill> billList = null;
        // yyyy-MM-dd HH:mm:ss
        String filenName = null;

        billList = (List<Bill>)request.getSession().getAttribute("billList");

        filenName = flag + TimeTools.now("MMddHHmmss") + ".xls";

        if (billList.size() == 0)
        {
            return null;
        }

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WritableWorkbook wwb = null;
        WritableSheet ws = null;

        try
        {
            out = reponse.getOutputStream();

            // create a excel
            wwb = Workbook.createWorkbook(out);
            ws = wwb.createSheet("sheel1", 0);
            int i = 0, j = 0;

            Bill element = null;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            WritableFont font2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD,
                false, jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);

            WritableCellFormat format = new WritableCellFormat(font);

            WritableCellFormat format2 = new WritableCellFormat(font2);

            ws.addCell(new Label(j++ , i, "单据类型", format));
            ws.addCell(new Label(j++ , i, "单据标识", format));
            ws.addCell(new Label(j++ , i, "填写日期", format));
            ws.addCell(new Label(j++ , i, "帐户", format));
            ws.addCell(new Label(j++ , i, "类型", format));
            ws.addCell(new Label(j++ , i, "库单号", format));
            ws.addCell(new Label(j++ , i, "金额", format));
            ws.addCell(new Label(j++ , i, "客户", format));
            ws.addCell(new Label(j++ , i, "经办人", format));

            ws.addCell(new Label(j++ , i, "备注", format));

            // 写outbean
            for (Iterator iter = billList.iterator(); iter.hasNext();)
            {
                element = (Bill)iter.next();

                j = 0;
                i++ ;

                ws.addCell(new Label(j++ , i, element.getType() == 0 ? "收款单" : "付款单"));
                ws.addCell(new Label(j++ , i, element.getId()));

                ws.addCell(new Label(j++ , i, TimeTools.getStringByFormat(element.getDates(),
                    "yyyy-MM-dd")));

                ws.addCell(new Label(j++ , i, element.getBank()));
                ws.addCell(new Label(j++ , i, element.getBillType()));
                ws.addCell(new Label(j++ , i, element.getOutId()));

                ws.addCell(new Label(j++ , i, String.valueOf(element.getMoney()), format2));

                ws.addCell(new Label(j++ , i, element.getCustomerName()));
                ws.addCell(new Label(j++ , i, element.getStafferName()));
                ws.addCell(new Label(j++ , i, element.getDescription()));
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

    public ActionForward findBill(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");
        String mark = request.getParameter("mark");
        Bill bean = null;
        try
        {
            bean = billDAO.findBillById(id);

            BankBean bb = bankManager.findBankById(bean.getBank());

            bean.setTemp(TimeTools.getStringByFormat(bean.getDates(), "yyyy-MM-dd"));

            if (bb != null)
            {
                bean.setBank(bb.getName());
            }

            // 目的银行
            if ( !StringTools.isNullOrNone(bean.getDestBank()))
            {
                bb = bankManager.findBankById(bean.getDestBank());

                if (bb != null)
                {
                    bean.setDestBank(bb.getName());
                }
            }

            request.setAttribute("bill", bean);

            if ( !StringTools.isNullOrNone(mark))
            {
                request.setAttribute("fff", Boolean.TRUE);
            }
            else
            {
                request.setAttribute("fff", Boolean.FALSE);
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询库单失败");
            _logger.error("addOut", e);

            return mapping.findForward("error");
        }

        return mapping.findForward("detailBill");
    }

    public ActionForward mark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                              HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");
        String mark = request.getParameter("mark");
        try
        {
            billDAO.mark(id, mark);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询库单失败");
            _logger.error("addOut", e);

            return mapping.findForward("error");
        }

        request.setAttribute("forward", "1");

        return queryBill(mapping, form, request, reponse);
    }

    public ActionForward queryForAdd(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        request.setAttribute("current", TimeTools.now("yyyy-MM-dd"));

        List<BankBean> list = null;

        if (LocationHelper.isSystemLocation(Helper.getCurrentLocationId(request)))
        {
            list = bankManager.listBank();
        }
        else
        {
            list = bankManager.queryBankByLocationId(Helper.getCurrentLocationId(request));
        }

        ConditionParse condition = new ConditionParse();

        // 获得其他区域的银行
        condition.addCondition("locationId", "<>", Helper.getCurrentLocationId(request));

        List<BankBean> list1 = null;

        if (LocationHelper.isSystemLocation(Helper.getCurrentLocationId(request)))
        {
            list1 = bankManager.listBank();
        }
        else
        {
            list1 = bankManager.queryBankByCondition(condition);
        }

        request.setAttribute("bankList", list);

        request.setAttribute("bankList1", list1);

        return mapping.findForward("addBill");
    }

    private void setCondition(HttpServletRequest re, ConditionParse condtion, String forward)
    {
        ParamterMap request = null;
        if ("1".equals(forward))
        {
            request = new ParamterMap((Map<String, String>)re.getSession().getAttribute(
                "GBillCondition"));

            request.saveParamersToRequest(re);
        }
        else
        {
            CommonTools.saveParamers(re);
            request = new ParamterMap(re);
            re.getSession().setAttribute("GBillCondition", request.getParameterMap());
        }

        String outTime = request.getParameter("date");
        String outTime1 = request.getParameter("date1");

        if ( !StringTools.isNullOrNone(outTime))
        {
            condtion.addCommonCondition("dates", ">=", publicSQL.to_date(outTime, "yyyy-MM-dd"));
        }
        else
        {
            condtion.addCommonCondition("dates", ">=", publicSQL.to_date(TimeTools.now_short(),
                "yyyy-MM-dd"));

            re.setAttribute("date", TimeTools.now_short());
        }

        if ( !StringTools.isNullOrNone(outTime1))
        {
            condtion.addCommonCondition("dates", "<=", publicSQL.to_date(outTime1, "yyyy-MM-dd"));
        }
        else
        {
            re.setAttribute("date1", TimeTools.now_short());
            condtion.addCommonCondition("dates", "<=", publicSQL.to_date(TimeTools.now_short(),
                "yyyy-MM-dd"));
        }

        String id = request.getParameter("receipt");

        if ( !StringTools.isNullOrNone(id))
        {
            condtion.addCondition("id", "like", id);
        }

        String status = request.getParameter("type");

        if ( !StringTools.isNullOrNone(status))
        {
            condtion.addIntCondition("type", "=", status);
        }

        String bank = request.getParameter("bank");

        if ( !StringTools.isNullOrNone(bank))
        {
            condtion.addCondition("bank", "=", bank);
        }

        String outId = request.getParameter("outId");

        if ( !StringTools.isNullOrNone(outId))
        {
            condtion.addCondition("outId", "like", outId);
        }

        String stafferName = request.getParameter("stafferName");

        if ( !StringTools.isNullOrNone(stafferName))
        {
            condtion.addCondition("stafferName", "like", stafferName);
        }

        String mark = request.getParameter("mark");

        if ( !StringTools.isNullOrNone(mark))
        {
            if ("1".equals(mark))
            {
                condtion.addCondition("mark", "<>", " ");
            }
            else
            {
                condtion.addCondition("and mark = ''");
            }
        }

        String inway = request.getParameter("inway");

        if ( !StringTools.isNullOrNone(inway))
        {
            condtion.addCondition("inway", "=", inway);
        }

    }

    public ActionForward addBill(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        Bill bill = new Bill();

        BeanUtil.getBean(bill, request);

        if (StringTools.isNullOrNone(bill.getCustomerId()))
        {
            bill.setMtype(0);
        }

        // 转账
        if (String.valueOf(Constant.BILL_TYPE_CHANGE).equals(request.getParameter("billType")))
        {
            bill.setType(Constant.BILL_PAY);
        }

        // 跨区域付账
        if (String.valueOf(Constant.BILL_TYPE_CHANGE_CITY).equals(request.getParameter("billType")))
        {
            bill.setInway(Constant.IN_WAY);

            bill.setType(Constant.BILL_PAY);

            String destBankId = bill.getDestBank();

            BankBean bb = bankManager.findBankById(destBankId);

            if (bb == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

                return mapping.findForward("error");
            }

            if (bb.getLocationId().equals(Helper.getCurrentLocationId(request)))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "由于是跨区域转账，银行必须是其他区域的银行");

                return mapping.findForward("error");
            }

            bill.setDestLocationId(bb.getLocationId());
        }

        // 跨区域收账
        if (String.valueOf(Constant.BILL_TYPE_CHANGE_IN_CITY).equals(
            request.getParameter("billType")))
        {
            bill.setType(Constant.BILL_RECIVE);

            String refId = bill.getRefBillId();

            Bill tem = billDAO.findBillById(refId);

            if (tem == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "相关的跨区域付款单据不存在");

                return mapping.findForward("error");
            }

            if ( !Helper.getCurrentLocationId(request).equals(tem.getDestLocationId()))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "区域不符合，请重新操作");

                return mapping.findForward("error");
            }

            if (tem.getInway() != Constant.IN_WAY)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "相关的跨区域付款单据不是在途中");

                return mapping.findForward("error");
            }
        }

        bill.setDates(TimeTools.getDateByFormat(request.getParameter("date"), "yyyy-MM-dd"));

        LocationBean lb = locationManager.findLocationById(Helper.getCurrentLocationId(request));

        if (lb == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作");

            return mapping.findForward("error");
        }

        String flag = lb.getLocationCode();

        bill.setId(flag + StringTools.formatString(commonDAO.getSquenceString()));

        String pay = request.getParameter("pay");

        bill.setLocationId(Helper.getCurrentLocationId(request));

        try
        {
            if (billDAO.addBill(bill))
            {
                // bill需要把入库的钱记录到单据里面
                if ( !StringTools.isNullOrNone(bill.getOutId()))
                {
                    OutBean bean = outManager.findOutById(bill.getOutId());

                    if (bean != null)
                    {
                        bean.addPay(bill.getMoneys());

                        // 修改已经的付款
                        outManager.modifyOutHadPay(bean.getFullId(), bean.getHadPay());
                    }
                    else
                    {
                        _logger.error("缺少单据:" + bill.getOutId());
                    }
                }

                // 转账需要生成2个单据
                if (bill.getBillType().equals(String.valueOf(Constant.BILL_TYPE_CHANGE)))
                {
                    Bill bill1 = new Bill();

                    BeanUtils.copyProperties(bill1, bill);

                    bill1.setType(1 - bill.getType());

                    bill1.setId(flag + StringTools.formatString(commonDAO.getSquenceString()));

                    String dirBank = request.getParameter("dirBanks");

                    bill1.setBank(dirBank);

                    billDAO.addBill(bill1);
                }

                // 跨区域收账 需要把相关的在途单据取消
                if (bill.getBillType().equals(String.valueOf(Constant.BILL_TYPE_CHANGE_IN_CITY)))
                {
                    String refId = bill.getRefBillId();

                    billDAO.updateInway(refId, Constant.IN_WAY_OVER);

                    logger1.info("收付款:" + refId + "在途状态已经修改成在途结束");
                }

                // 如果已经付款全部结束
                String other = "";
                if (String.valueOf(Constant.PAY_YES).equals(pay))
                {
                    String fullId = bill.getOutId();

                    if ( !StringTools.isNullOrNone(fullId))
                    {
                        outManager.modifyPay(fullId, Constant.PAY_YES);

                        other = ",且库单:" + fullId + "已经回款";
                    }
                }

                request.setAttribute(KeyConstant.MESSAGE, "增加单据成功:" + bill.getId() + "." + other);
            }
            else
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加单据失败");
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加单据失败:");
            _logger.error("addBill", e);
        }

        request.setAttribute("forward", "1");

        return queryBill(mapping, form, request, reponse);
    }

    public ActionForward addBillFormOut(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        request.setAttribute("current", TimeTools.now("yyyy-MM-dd"));

        StringBuffer description = new StringBuffer();

        try
        {
            String outId = request.getParameter("outId");

            OutBean out = outManager.findOutById(outId);

            request.setAttribute("customerName", out.getCustomerName());

            List<BaseBean> list = outDAO.queryBaseByOutFullId(outId);

            String[] ss = request.getParameterValues("product");

            if (ss != null)
            {
                List<String> temps = Arrays.asList(ss);

                int i = 1;
                for (BaseBean temp : list)
                {
                    if (temps.contains(temp.getId()))
                    {
                        description.append(i++ ).append(':').append(temp.getProductName());
                        description.append("[数量:").append(temp.getAmount()).append(temp.getUnit()).append(
                            ']');
                        description.append("[单价:").append(temp.getPrice()).append(']');
                        description.append("[金额:").append(temp.getValue()).append(']');
                        description.append("[成本:").append(temp.getDescription()).append(']').append(
                            "\r\n");
                    }
                }
            }
        }
        catch (Exception e)
        {
            _logger.error("addBillFormOut", e);
        }

        request.setAttribute("description", description);

        request.setAttribute("rivet", "1");

        List<BankBean> list = null;
        if (LocationHelper.isSystemLocation(Helper.getCurrentLocationId(request)))
        {
            ConditionParse condition = new ConditionParse();

            condition.addWhereStr();

            list = bankManager.queryBankByCondition(condition);
        }
        else
        {
            list = bankManager.queryBankByLocationId(Helper.getCurrentLocationId(request));
        }

        request.setAttribute("bankList", list);

        return mapping.findForward("addBill");
    }

    public ActionForward delBill(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        try
        {
            String id = request.getParameter("id");

            billDAO.delBill(id);

            request.getSession().setAttribute(KeyConstant.MESSAGE, "删除单据成功");
        }
        catch (Exception e)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "删除单据失败");
            _logger.error("delProduct", e);
        }

        request.setAttribute("forward", "2");

        return queryBill(mapping, form, request, reponse);
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
     * @return the _logger
     */
    public Log get_logger()
    {
        return _logger;
    }

    /**
     * @return the bankManager
     */
    public BankManager getBankManager()
    {
        return bankManager;
    }

    /**
     * @param bankManager
     *            the bankManager to set
     */
    public void setBankManager(BankManager bankManager)
    {
        this.bankManager = bankManager;
    }

    /**
     * @return the locationManager
     */
    public LocationManager getLocationManager()
    {
        return locationManager;
    }

    /**
     * @param locationManager
     *            the locationManager to set
     */
    public void setLocationManager(LocationManager locationManager)
    {
        this.locationManager = locationManager;
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

}
