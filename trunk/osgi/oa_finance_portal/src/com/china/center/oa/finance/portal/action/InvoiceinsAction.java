/**
 * File Name: InvoiceinsAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.portal.action;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import com.center.china.osgi.publics.User;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.JSONTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.common.PageSeparateTools;
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.actionhelper.jsonimpl.JSONArray;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.finance.bean.InvoiceinsBean;
import com.china.center.oa.finance.bean.InvoiceinsItemBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.dao.InsVSOutDAO;
import com.china.center.oa.finance.dao.InvoiceinsDAO;
import com.china.center.oa.finance.dao.InvoiceinsItemDAO;
import com.china.center.oa.finance.facade.FinanceFacade;
import com.china.center.oa.finance.manager.InvoiceinsManager;
import com.china.center.oa.finance.vo.InvoiceinsVO;
import com.china.center.oa.finance.vs.InsVSOutBean;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.bean.ShowBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.InvoiceConstant;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.DutyVSInvoiceDAO;
import com.china.center.oa.publics.dao.InvoiceDAO;
import com.china.center.oa.publics.dao.ShowDAO;
import com.china.center.oa.publics.manager.StafferManager;
import com.china.center.oa.publics.vs.DutyVSInvoiceBean;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.dao.OutBalanceDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.RequestTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * InvoiceinsAction
 * 
 * @author ZHUZHU
 * @version 2011-1-2
 * @see InvoiceinsAction
 * @since 3.0
 */
public class InvoiceinsAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private FinanceFacade financeFacade = null;

    private InvoiceinsDAO invoiceinsDAO = null;

    private InvoiceinsItemDAO invoiceinsItemDAO = null;

    private InsVSOutDAO insVSOutDAO = null;

    private DutyDAO dutyDAO = null;

    private ShowDAO showDAO = null;

    private StafferManager stafferManager = null;

    private OutDAO outDAO = null;

    private InvoiceDAO invoiceDAO = null;

    private OutBalanceDAO outBalanceDAO = null;

    private InvoiceinsManager invoiceinsManager = null;

    private DutyVSInvoiceDAO dutyVSInvoiceDAO = null;

    private static final String QUERYINVOICEINS = "queryInvoiceins";

    /**
     * default constructor
     */
    public InvoiceinsAction()
    {
    }

    /**
     * preForAddInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddInvoiceins(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        prepare(request);

        // 查询开单品名
        List<ShowBean> showList = showDAO.listEntityBeans();

        JSONArray shows = new JSONArray(showList, true);

        request.setAttribute("showJSON", shows.toString());

        // 获得财务审批的权限人(1604)
        List<StafferBean> stafferList = stafferManager
            .queryStafferByAuthId(AuthConstant.INVOICEINS_OPR);

        request.setAttribute("stafferList", stafferList);

        return mapping.findForward("addInvoiceins");
    }

    /**
     * preForAddInvoiceins2
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddInvoiceins2(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        prepare(request);

        // 查询开单品名
        List<ShowBean> showList = showDAO.listEntityBeans();

        JSONArray shows = new JSONArray(showList, true);

        request.setAttribute("showJSON", shows.toString());

        return mapping.findForward("addInvoiceins2");
    }

    private void prepare(HttpServletRequest request)
    {
        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList", dutyList);

        ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        condition.addIntCondition("InvoiceBean.forward", "=", InvoiceConstant.INVOICE_FORWARD_OUT);

        List<InvoiceBean> invoiceList = invoiceDAO.queryEntityBeansByCondition(condition);

        request.setAttribute("invoiceList", invoiceList);

        List<DutyVSInvoiceBean> vsList = dutyVSInvoiceDAO.listEntityBeans();

        // 过滤
        fiterVS(invoiceList, vsList);

        JSONArray vsJSON = new JSONArray(vsList, true);

        request.setAttribute("vsJSON", vsJSON.toString());

        JSONArray invoices = new JSONArray(invoiceList, true);

        request.setAttribute("invoicesJSON", invoices.toString());
    }

    private void fiterVS(List<InvoiceBean> invoiceList, List<DutyVSInvoiceBean> vsList)
    {
        for (Iterator iterator = vsList.iterator(); iterator.hasNext();)
        {
            DutyVSInvoiceBean dutyVSInvoiceBean = (DutyVSInvoiceBean)iterator.next();

            boolean delete = true;

            for (InvoiceBean invoiceBean : invoiceList)
            {
                if (dutyVSInvoiceBean.getInvoiceId().equals(invoiceBean.getId()))
                {
                    delete = false;
                    break;
                }
            }

            if (delete)
            {
                iterator.remove();
            }
        }
    }

    /**
     * findInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findInvoiceins(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        InvoiceinsVO bean = invoiceinsManager.findVO(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据异常,请重新操作");

            return mapping.findForward("queryBank");
        }

        prepare(request);

        request.setAttribute("bean", bean);

        return mapping.findForward("detailInvoiceins");
    }

    /**
     * queryInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryInvoiceins(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYINVOICEINS, request, condtion);

        User user = Helper.getUser(request);

        String mode = RequestTools.getValueFromRequest(request, "mode");

        // 自己的
        if ("0".equals(mode))
        {
            condtion.addCondition("InvoiceinsBean.stafferId", "=", user.getStafferId());
        }
        // 出纳审核
        else if ("1".equals(mode))
        {
            condtion.addCondition("InvoiceinsBean.processer", "=", user.getStafferId());

            condtion.addIntCondition("InvoiceinsBean.status", "=",
                FinanceConstant.INVOICEINS_STATUS_SUBMIT);
        }
        // 出纳查询
        else if ("2".equals(mode))
        {
            // condtion.addCondition("InvoiceinsBean.locationId", "=", user.getLocationId());
        }
        else
        {
            condtion.addFlaseCondition();
        }

        condtion.addCondition("order by InvoiceinsBean.id desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYINVOICEINS, request, condtion,
            this.invoiceinsDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * exportInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward exportInvoiceins(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        PageSeparate pageSeparate = PageSeparateTools.getPageSeparate(request, QUERYINVOICEINS);

        ConditionParse condition = PageSeparateTools.getCondition(request, QUERYINVOICEINS);

        if (pageSeparate.getRowCount() > 1500)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "导出的记录数不能超过1500");

            return mapping.findForward("error");
        }

        if (pageSeparate.getRowCount() == 0)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "导出的记录数为0");

            return mapping.findForward("error");
        }

        // 查询的单据
        List<InvoiceinsVO> beanList = invoiceinsDAO.queryEntityVOsByCondition(condition);

        OutputStream out = null;

        String filenName = null;

        filenName = "Invoiceins_" + TimeTools.now("MMddHHmmss") + ".xls";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WritableWorkbook wwb = null;

        WritableSheet ws = null;

        try
        {
            out = reponse.getOutputStream();

            // create a excel
            wwb = Workbook.createWorkbook(out);

            ws = wwb.createSheet("Invoiceins", 0);

            int i = 0, j = 0;

            InvoiceinsVO element = null;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            WritableFont font2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);

            WritableCellFormat format = new WritableCellFormat(font);

            WritableCellFormat format2 = new WritableCellFormat(font2);

            ws.addCell(new Label(j++ , i, "时间", format));
            ws.addCell(new Label(j++ , i, "发票标识", format));
            ws.addCell(new Label(j++ , i, "纳税实体", format));
            ws.addCell(new Label(j++ , i, "客户", format));
            ws.addCell(new Label(j++ , i, "发票类型", format));
            ws.addCell(new Label(j++ , i, "开票品名", format));
            ws.addCell(new Label(j++ , i, "规格", format));
            ws.addCell(new Label(j++ , i, "单位", format));
            ws.addCell(new Label(j++ , i, "数量", format));
            ws.addCell(new Label(j++ , i, "单价", format));
            ws.addCell(new Label(j++ , i, "合计", format));

            for (Iterator iter = beanList.iterator(); iter.hasNext();)
            {
                element = (InvoiceinsVO)iter.next();

                List<InvoiceinsItemBean> itemList = invoiceinsItemDAO.queryEntityBeansByFK(element
                    .getId());

                for (InvoiceinsItemBean invoiceinsItemBean : itemList)
                {
                    j = 0;
                    i++ ;

                    ws.addCell(new Label(j++ , i, element.getLogTime()));
                    ws.addCell(new Label(j++ , i, element.getId()));
                    ws.addCell(new Label(j++ , i, element.getDutyName()));
                    ws.addCell(new Label(j++ , i, element.getCustomerName()));
                    ws.addCell(new Label(j++ , i, element.getInvoiceName()));

                    // 子项
                    ws.addCell(new Label(j++ , i, invoiceinsItemBean.getShowName()));
                    ws.addCell(new Label(j++ , i, invoiceinsItemBean.getSpecial()));
                    ws.addCell(new Label(j++ , i, invoiceinsItemBean.getUnit()));
                    ws.addCell(new jxl.write.Number(j++ , i, invoiceinsItemBean.getAmount(),
                        format2));
                    ws
                        .addCell(new jxl.write.Number(j++ , i, invoiceinsItemBean.getPrice(),
                            format2));
                    ws.addCell(new jxl.write.Number(j++ , i,
                        (invoiceinsItemBean.getAmount() * invoiceinsItemBean.getPrice()), format2));

                }

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
        finally
        {
            if (wwb != null)
            {
                try
                {
                    wwb.write();
                    wwb.close();
                }
                catch (Exception e1)
                {
                }
            }
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
        }

        return null;
    }

    /**
     * addInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addInvoiceins(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String mode = request.getParameter("mode");

        try
        {
            InvoiceinsBean bean = createIns(request);

            User user = Helper.getUser(request);

            financeFacade.addInvoiceinsBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("mode", mode);

        RequestTools.menuInitQuery(request);

        return mapping.findForward("queryInvoiceins");
    }

    /**
     * createIns
     * 
     * @param request
     * @return
     */
    private InvoiceinsBean createIns(HttpServletRequest request)
        throws MYException
    {
        InvoiceinsBean bean = new InvoiceinsBean();

        BeanUtil.getBean(bean, request);

        String[] showIds = request.getParameterValues("showId");
        String[] amounts = request.getParameterValues("amount");
        String[] prices = request.getParameterValues("price");
        String[] specials = request.getParameterValues("special");
        String[] units = request.getParameterValues("sunit");

        List<InvoiceinsItemBean> itemList = new ArrayList<InvoiceinsItemBean>();

        for (int i = 0; i < showIds.length; i++ )
        {
            if (StringTools.isNullOrNone(showIds[i]))
            {
                break;
            }

            InvoiceinsItemBean item = new InvoiceinsItemBean();

            item.setShowId(showIds[i]);
            item.setShowName(showDAO.find(showIds[i]).getName());
            item.setAmount(MathTools.parseInt(amounts[i]));
            item.setPrice(MathTools.parseDouble(prices[i]));
            item.setSpecial(specials[i].trim());
            item.setUnit(units[i].trim());

            itemList.add(item);
        }

        if (ListTools.isEmptyOrNull(itemList))
        {
            throw new MYException("没有开票项");
        }

        double total = 0.0d;

        for (InvoiceinsItemBean invoiceinsItemBean : itemList)
        {
            total += invoiceinsItemBean.getAmount() * invoiceinsItemBean.getPrice();
        }

        bean.setMoneys(total);

        bean.setItemList(itemList);

        User user = Helper.getUser(request);

        bean.setLocationId(user.getLocationId());

        bean.setStafferId(user.getStafferId());

        String outId = request.getParameter("outId");

        if ( !StringTools.isNullOrNone(outId))
        {
            double canUse = bean.getMoneys();

            List<InsVSOutBean> vsList = new ArrayList<InsVSOutBean>();

            String[] split = outId.split(";");

            for (int i = 0; i < split.length; i++ )
            {
                if ( !StringTools.isNullOrNone(split[i]))
                {
                    if (canUse == 0.0)
                    {
                        break;
                    }

                    InsVSOutBean vs = new InsVSOutBean();

                    vs.setOutId(split[i]);

                    vs.setType(FinanceConstant.INSVSOUT_TYPE_OUT);

                    OutBean out = outDAO.find(vs.getOutId());

                    OutBalanceBean balance = null;

                    if (out == null)
                    {
                        balance = outBalanceDAO.find(vs.getOutId());

                        if (balance == null)
                        {
                            throw new MYException("数据错误,请确认操作");
                        }
                        else
                        {
                            vs.setType(FinanceConstant.INSVSOUT_TYPE_BALANCE);
                        }
                    }

                    if (vs.getType() == FinanceConstant.INSVSOUT_TYPE_OUT)
                    {
                        // 剩余需要开票的金额
                        double imoney = out.getTotal() - out.getInvoiceMoney();

                        if (canUse >= imoney)
                        {
                            vs.setMoneys(imoney);

                            canUse = canUse - imoney;
                        }
                        else
                        {
                            vs.setMoneys(canUse);

                            canUse = 0.0d;
                        }
                    }
                    else
                    {
                        // 剩余需要开票的金额
                        double imoney = balance.getTotal() - balance.getInvoiceMoney();

                        if (canUse >= imoney)
                        {
                            vs.setMoneys(imoney);

                            canUse = canUse - imoney;
                        }
                        else
                        {
                            vs.setMoneys(canUse);

                            canUse = 0.0d;
                        }
                    }

                    vsList.add(vs);
                }
            }

            if (canUse > 0)
            {
                throw new MYException("开票金额过多,请确认操作");
            }

            bean.setVsList(vsList);

            StringBuffer buffer = new StringBuffer();

            for (InsVSOutBean insVSOutBean : vsList)
            {
                buffer.append(insVSOutBean.getOutId()).append(';');
            }

            bean.setRefIds(buffer.toString());
        }

        return bean;
    }

    /**
     * deleteInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteInvoiceins(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.deleteInvoiceinsBean(user.getId(), id);

            ajax.setSuccess("成功删除");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * passInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward passInvoiceins(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String mode = request.getParameter("mode");

        try
        {
            User user = Helper.getUser(request);

            financeFacade.passInvoiceinsBean(user.getId(), id);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("mode", mode);

        return mapping.findForward("queryInvoiceins");
    }

    /**
     * rejectInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward rejectInvoiceins(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String mode = request.getParameter("mode");

        try
        {
            User user = Helper.getUser(request);

            financeFacade.rejectInvoiceinsBean(user.getId(), id);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        request.setAttribute("mode", mode);

        return mapping.findForward("queryInvoiceins");
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
     * @return the invoiceinsDAO
     */
    public InvoiceinsDAO getInvoiceinsDAO()
    {
        return invoiceinsDAO;
    }

    /**
     * @param invoiceinsDAO
     *            the invoiceinsDAO to set
     */
    public void setInvoiceinsDAO(InvoiceinsDAO invoiceinsDAO)
    {
        this.invoiceinsDAO = invoiceinsDAO;
    }

    /**
     * @return the invoiceinsItemDAO
     */
    public InvoiceinsItemDAO getInvoiceinsItemDAO()
    {
        return invoiceinsItemDAO;
    }

    /**
     * @param invoiceinsItemDAO
     *            the invoiceinsItemDAO to set
     */
    public void setInvoiceinsItemDAO(InvoiceinsItemDAO invoiceinsItemDAO)
    {
        this.invoiceinsItemDAO = invoiceinsItemDAO;
    }

    /**
     * @return the insVSOutDAO
     */
    public InsVSOutDAO getInsVSOutDAO()
    {
        return insVSOutDAO;
    }

    /**
     * @param insVSOutDAO
     *            the insVSOutDAO to set
     */
    public void setInsVSOutDAO(InsVSOutDAO insVSOutDAO)
    {
        this.insVSOutDAO = insVSOutDAO;
    }

    /**
     * @return the invoiceinsManager
     */
    public InvoiceinsManager getInvoiceinsManager()
    {
        return invoiceinsManager;
    }

    /**
     * @param invoiceinsManager
     *            the invoiceinsManager to set
     */
    public void setInvoiceinsManager(InvoiceinsManager invoiceinsManager)
    {
        this.invoiceinsManager = invoiceinsManager;
    }

    /**
     * @return the dutyDAO
     */
    public DutyDAO getDutyDAO()
    {
        return dutyDAO;
    }

    /**
     * @param dutyDAO
     *            the dutyDAO to set
     */
    public void setDutyDAO(DutyDAO dutyDAO)
    {
        this.dutyDAO = dutyDAO;
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

    /**
     * @return the showDAO
     */
    public ShowDAO getShowDAO()
    {
        return showDAO;
    }

    /**
     * @param showDAO
     *            the showDAO to set
     */
    public void setShowDAO(ShowDAO showDAO)
    {
        this.showDAO = showDAO;
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
     * @return the outBalanceDAO
     */
    public OutBalanceDAO getOutBalanceDAO()
    {
        return outBalanceDAO;
    }

    /**
     * @param outBalanceDAO
     *            the outBalanceDAO to set
     */
    public void setOutBalanceDAO(OutBalanceDAO outBalanceDAO)
    {
        this.outBalanceDAO = outBalanceDAO;
    }

    /**
     * @return the dutyVSInvoiceDAO
     */
    public DutyVSInvoiceDAO getDutyVSInvoiceDAO()
    {
        return dutyVSInvoiceDAO;
    }

    /**
     * @param dutyVSInvoiceDAO
     *            the dutyVSInvoiceDAO to set
     */
    public void setDutyVSInvoiceDAO(DutyVSInvoiceDAO dutyVSInvoiceDAO)
    {
        this.dutyVSInvoiceDAO = dutyVSInvoiceDAO;
    }

    /**
     * @return the stafferManager
     */
    public StafferManager getStafferManager()
    {
        return stafferManager;
    }

    /**
     * @param stafferManager
     *            the stafferManager to set
     */
    public void setStafferManager(StafferManager stafferManager)
    {
        this.stafferManager = stafferManager;
    }
}
