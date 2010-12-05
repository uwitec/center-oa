package com.china.center.oa.sail.action;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.center.china.osgi.publics.User;
import com.center.china.osgi.publics.tools.ObjectTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.common.OldPageSeparateTools;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.customer.constant.CustomerConstant;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.customer.wrap.NotPayWrap;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.bean.ProviderBean;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.ProviderDAO;
import com.china.center.oa.product.dao.StorageDAO;
import com.china.center.oa.product.manager.StorageRelationManager;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.AuthBean;
import com.china.center.oa.publics.bean.DepartmentBean;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.InvoiceConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.constant.SysConfigConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.DepartmentDAO;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.dao.InvoiceDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.manager.AuthManager;
import com.china.center.oa.publics.manager.FatalNotify;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.oa.publics.vo.FlowLogVO;
import com.china.center.oa.sail.bean.BaseBalanceBean;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.ConsignBean;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.bean.TransportBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.dao.BaseBalanceDAO;
import com.china.center.oa.sail.dao.BaseDAO;
import com.china.center.oa.sail.dao.ConsignDAO;
import com.china.center.oa.sail.dao.OutBalanceDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.dao.OutQueryDAO;
import com.china.center.oa.sail.helper.FlowLogHelper;
import com.china.center.oa.sail.helper.OutHelper;
import com.china.center.oa.sail.helper.YYTools;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.sail.vo.OutBalanceVO;
import com.china.center.oa.sail.vo.OutVO;
import com.china.center.osgi.jsp.ElTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.ParamterMap;
import com.china.center.tools.RequestTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * 增加出库单
 * 
 * @author ZHUZHU
 * @version 2007-4-1
 * @see
 * @since
 */
public class OutAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private final Log importLog = LogFactory.getLog("sec");

    private UserDAO userDAO = null;

    private OutQueryDAO outQueryDAO = null;

    private FatalNotify fatalNotify = null;

    private OutManager outManager = null;

    private ProductDAO productDAO = null;

    private CustomerDAO customerDAO = null;

    private ProviderDAO providerDAO = null;

    private StafferDAO stafferDAO = null;

    private ParameterDAO parameterDAO = null;

    private LocationDAO locationDAO = null;

    private CommonDAO commonDAO = null;

    private DepartmentDAO departmentDAO = null;

    private StorageDAO storageDAO = null;

    private DepotDAO depotDAO = null;

    private UserManager userManager = null;

    private FlowLogDAO flowLogDAO = null;

    private OutDAO outDAO = null;

    private DutyDAO dutyDAO = null;

    private AuthManager authManager = null;

    private InvoiceDAO invoiceDAO = null;

    private BaseDAO baseDAO = null;

    private ConsignDAO consignDAO = null;

    private DepotpartDAO depotpartDAO = null;

    private StorageRelationManager storageRelationManager = null;

    private BaseBalanceDAO baseBalanceDAO = null;

    private OutBalanceDAO outBalanceDAO = null;

    private static String QUERYSELFOUT = "querySelfOut";

    private static String QUERYSELFOUTBALANCE = "querySelfOutBalance";

    private static String QUERYOUT = "queryOut";

    private static String QUERYSELFBUY = "querySelfBuy";

    /**
     * queryForAdd
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryForAdd(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        // 是否锁定库存
        if (storageRelationManager.isStorageRelationLock())
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库存被锁定,不能开单");

            return mapping.findForward("error");
        }

        String flag = request.getParameter("flag");

        innerForPrepare(request);

        // 增加入库单
        if ("1".equals(flag))
        {
            return mapping.findForward("addBuy");
        }

        // 销售单
        return mapping.findForward("addOut");
    }

    /**
     * 准备增加委托结算清单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddOutBalance(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String outId = request.getParameter("outId");

        List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outId);

        int totalLast = 0;

        for (BaseBean baseBean : baseList)
        {
            int hasPass = baseBalanceDAO.sumPassBaseBalance(baseBean.getId());

            baseBean.setInway(hasPass);

            int last = baseBean.getAmount() - baseBean.getInway();

            baseBean.setUnit(String.valueOf(last));

            totalLast += last;
        }

        if (totalLast == 0)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "销售单委托代销已经结束");

            return mapping.findForward("error");

        }

        request.setAttribute("baseList", baseList);

        request.setAttribute("outId", outId);

        // 销售单
        return mapping.findForward("addOutBalance");
    }

    /**
     * 查询单体
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward findOutBalance(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        OutBalanceBean bean = outBalanceDAO.find(id);

        List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(bean.getOutId());

        for (BaseBean baseBean : baseList)
        {
            BaseBalanceBean bbb = baseBalanceDAO.findByUnique(baseBean.getId(), id);

            int hasPass = baseBalanceDAO.sumPassBaseBalance(baseBean.getId());

            baseBean.setInway(hasPass);

            baseBean.setUnit(String.valueOf(bbb.getAmount()));

            baseBean.setShowName(MathTools.formatNum(bbb.getSailPrice()));
        }

        request.setAttribute("baseList", baseList);

        request.setAttribute("bean", bean);

        // 销售单
        return mapping.findForward("detailOutBalance");
    }

    /**
     * innerForPrepare(准备库单的维护)
     * 
     * @param request
     */
    private void innerForPrepare(HttpServletRequest request)
    {
        // 得到部门
        List<DepartmentBean> list2 = departmentDAO.listEntityBeans();

        User user = Helper.getUser(request);

        // 仓库是自己选择的
        request.setAttribute("departementList", list2);

        request.setAttribute("current", TimeTools.now("yyyy-MM-dd"));

        List<DepotBean> locationList = depotDAO.listEntityBeans();

        request.setAttribute("locationList", locationList);

        int goDays = parameterDAO.getInt(SysConfigConstant.OUT_PERSONAL_REDAY);

        request.setAttribute("goDays", goDays);

        ConditionParse condition = new ConditionParse();

        User oprUser = Helper.getUser(request);

        condition.addCondition("locationId", "=", oprUser.getLocationId());

        // 仓区,但是这里的仓区没有意义了
        List<DepotpartBean> list = depotpartDAO.queryEntityBeansByCondition(condition);

        request.setAttribute("depotpartList", list);

        showLastCredit(request, user);

        List<InvoiceBean> invoiceList = invoiceDAO.queryEntityBeansByCondition("where forward = ?",
            InvoiceConstant.INVOICE_FORWARD_OUT);

        request.setAttribute("invoiceList", invoiceList);

        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList", dutyList);
    }

    private boolean hasOver(String stafferName)
    {
        ConditionParse condtion = new ConditionParse();

        // 获得条件
        getCondition(condtion, stafferName);

        List<OutBean> list = outDAO.queryEntityBeansByCondition(condtion);

        long current = new Date().getTime();

        for (OutBean outBean : list)
        {
            Date temp = TimeTools.getDateByFormat(outBean.getRedate(), "yyyy-MM-dd");

            if (temp != null)
            {
                if (temp.getTime() < current)
                {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 增加库单时查询供应商
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryProvider(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<ProviderBean> list = null;

        ConditionParse condition = new ConditionParse();

        String flag = request.getParameter("flagg");

        String name = request.getParameter("name");

        if ( !StringTools.isNullOrNone(name))
        {
            condition.addCondition("name", "like", name);
            request.setAttribute("name", name);
        }

        String code = request.getParameter("code");

        if ( !StringTools.isNullOrNone(code))
        {
            condition.addCondition("code", "like", code);
            request.setAttribute("code", code);
        }

        list = providerDAO.queryEntityBeansByLimit(condition, 100);

        request.setAttribute("customerList", list);

        request.setAttribute("flagg", flag);

        return mapping.findForward("rptProvider");
    }

    /**
     * export销售单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward export(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        String exportKey = (String)request.getSession().getAttribute("exportKey");

        List<OutVO> outList = null;

        String filenName = null;

        if (OldPageSeparateTools.getPageSeparate(request, exportKey).getRowCount() > 1500)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "导出的记录数不能超过1500");

            return mapping.findForward("error");
        }

        outList = outDAO.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(request,
            exportKey));

        filenName = "Export_" + TimeTools.now("MMddHHmmss") + ".xls";

        if (outList.size() == 0)
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

            OutVO element = null;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            WritableFont font2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.RED);

            WritableCellFormat format = new WritableCellFormat(font);

            WritableCellFormat format2 = new WritableCellFormat(font2);

            element = (OutVO)outList.get(0);

            String ffs = null;
            if (element.getType() == 0)
            {
                ffs = "出";
            }
            else
            {
                ffs = "入";
            }

            ws.addCell(new Label(j++ , i, ffs + "库日期", format));
            ws.addCell(new Label(j++ , i, "调" + ffs + "部门", format));
            ws.addCell(new Label(j++ , i, element.getType() == 0 ? "客户" : "供应商(调出部门)", format));
            ws.addCell(new Label(j++ , i, "联系人", format));
            ws.addCell(new Label(j++ , i, "联系电话", format));
            ws.addCell(new Label(j++ , i, "单据号码", format));
            ws.addCell(new Label(j++ , i, "回款日期", format));
            ws.addCell(new Label(j++ , i, "状态", format));
            ws.addCell(new Label(j++ , i, "经办人", format));
            ws.addCell(new Label(j++ , i, "仓库", format));
            ws.addCell(new Label(j++ , i, "描述", format));

            ws.addCell(new Label(j++ , i, "品名", format));
            ws.addCell(new Label(j++ , i, "单位", format));
            ws.addCell(new Label(j++ , i, "数量", format));
            ws.addCell(new Label(j++ , i, "单价", format));
            ws.addCell(new Label(j++ , i, "金额", format));
            ws.addCell(new Label(j++ , i, "成本", format));
            ws.addCell(new Label(j++ , i, "发货单号", format));
            ws.addCell(new Label(j++ , i, "发货方式", format));
            ws.addCell(new Label(j++ , i, "总金额", format));

            // 写outbean
            for (Iterator iter = outList.iterator(); iter.hasNext();)
            {
                element = (OutVO)iter.next();

                // 写baseBean
                List<BaseBean> baseList = null;
                BaseBean base = null;

                ConsignBean consignBean = null;

                TransportBean transportBean = null;

                try
                {
                    baseList = baseDAO.queryEntityBeansByFK(element.getFullId());

                    consignBean = consignDAO.findConsignById(element.getFullId());

                    if (consignBean != null)
                    {
                        transportBean = consignDAO.findTransportById(consignBean.getTransport());
                    }
                }
                catch (Exception e)
                {
                    _logger.error(e, e);
                }

                for (Iterator iterator = baseList.iterator(); iterator.hasNext();)
                {
                    j = 0;
                    i++ ;

                    ws.addCell(new Label(j++ , i, element.getOutTime()));

                    ws.addCell(new Label(j++ , i, element.getDepartment()));

                    ws.addCell(new Label(j++ , i, element.getCustomerName()));

                    ws.addCell(new Label(j++ , i, element.getConnector()));

                    ws.addCell(new Label(j++ , i, element.getPhone()));

                    ws.addCell(new Label(j++ , i, element.getFullId()));

                    ws.addCell(new Label(j++ , i, element.getRedate()));

                    ws.addCell(new Label(j++ , i, OutHelper.getStatus(element.getStatus(), false)));

                    ws.addCell(new Label(j++ , i, element.getStafferName()));

                    ws.addCell(new Label(j++ , i, element.getDepotName()));

                    ws.addCell(new Label(j++ , i, element.getDescription()));

                    // 下面是base里面的数据
                    base = (BaseBean)iterator.next();

                    ws.addCell(new Label(j++ , i, base.getProductName()));
                    ws.addCell(new Label(j++ , i, base.getUnit()));
                    ws.addCell(new Label(j++ , i, String.valueOf(base.getAmount())));
                    ws.addCell(new Label(j++ , i, String.valueOf(base.getPrice())));
                    ws.addCell(new Label(j++ , i, String.valueOf(base.getValue())));
                    ws.addCell(new Label(j++ , i, base.getDescription()));

                    if ( !iterator.hasNext())
                    {
                        // 到出发货单和发货方式
                        if (consignBean != null)
                        {
                            ws.addCell(new Label(j++ , i, consignBean.getTransportNo()));

                            if (transportBean != null)
                            {
                                ws.addCell(new Label(j++ , i, transportBean.getName()));
                            }
                            else
                            {
                                ws.addCell(new Label(j++ , i, ""));
                            }
                        }
                        else
                        {
                            ws.addCell(new Label(j++ , i, ""));
                            ws.addCell(new Label(j++ , i, ""));
                        }

                        ws.addCell(new Label(j++ , i, String.valueOf(element.getTotal()), format2));
                    }
                }

            }
        }
        catch (Exception e)
        {
            _logger.error(e, e);
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
     * export
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward exportNotPay(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        // 应收客户导出
        List<NotPayWrap> beanList = outQueryDAO.listNotPayWrap();

        OutputStream out = null;

        String filenName = null;

        filenName = "NotPay_" + TimeTools.now("MMddHHmmss") + ".xls";

        if (beanList.size() == 0)
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

            ws = wwb.createSheet("NOTPAY", 0);

            int i = 0, j = 0;

            NotPayWrap element = null;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            WritableFont font2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);

            WritableCellFormat format = new WritableCellFormat(font);

            WritableCellFormat format2 = new WritableCellFormat(font2);

            ws.addCell(new Label(j++ , i, "客户名称", format));
            ws.addCell(new Label(j++ , i, "客户编码", format));
            ws.addCell(new Label(j++ , i, "信用等级", format));
            ws.addCell(new Label(j++ , i, "信用分数", format));
            ws.addCell(new Label(j++ , i, "应收账款", format));

            for (Iterator iter = beanList.iterator(); iter.hasNext();)
            {
                element = (NotPayWrap)iter.next();

                j = 0;
                i++ ;

                ws.addCell(new Label(j++ , i, element.getCname()));
                ws.addCell(new Label(j++ , i, element.getCcode()));

                ws.addCell(new Label(j++ , i, element.getCreditName()));

                ws.addCell(new jxl.write.Number(j++ , i, element.getCreditVal()));

                ws.addCell(new jxl.write.Number(j++ , i, element.getNotPay(), format2));

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
     * addOutBalance
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addOutBalance(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutBalanceBean bean = new OutBalanceBean();

        setOutBalanceBean(bean, request);

        User user = (User)request.getSession().getAttribute("user");

        RequestTools.actionInitQuery(request);

        try
        {
            outManager.addOutBalance(user, bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加结算清单");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e);
        }

        CommonTools.removeParamers(request);

        request.setAttribute("queryType", "1");

        return queryOutBalance(mapping, form, request, reponse);
    }

    /**
     * 收集数据
     * 
     * @param pbean
     * @param item
     * @param request
     */
    private void setOutBalanceBean(OutBalanceBean bean, HttpServletRequest request)
    {
        User user = (User)request.getSession().getAttribute("user");

        String description = request.getParameter("description");

        String type = request.getParameter("type");

        String outId = request.getParameter("outId");

        bean.setDescription(description);

        bean.setLogTime(TimeTools.now());

        bean.setStafferId(user.getStafferId());

        bean.setOutId(outId);

        bean.setStatus(OutConstant.OUTBALANCE_STATUS_SUBMIT);

        bean.setType(MathTools.parseInt(type));

        String[] baseIds = request.getParameterValues("baseId");

        String[] amounts = request.getParameterValues("amount");

        String[] prices = request.getParameterValues("price");

        List<BaseBalanceBean> baseBalanceList = new ArrayList<BaseBalanceBean>();

        double total = 0.0d;
        for (int i = 0; i < baseIds.length; i++ )
        {
            String baseId = baseIds[i];

            BaseBalanceBean each = new BaseBalanceBean();

            each.setBaseId(baseId);
            each.setAmount(MathTools.parseInt(amounts[i]));
            each.setOutId(outId);
            each.setSailPrice(MathTools.parseDouble(prices[i]));

            total += each.getAmount() * each.getSailPrice();

            baseBalanceList.add(each);
        }

        bean.setTotal(total);

        bean.setBaseBalanceList(baseBalanceList);
    }

    /**
     * 增加(保存修改)修改库单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addOut(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse reponse)
        throws ServletException
    {
        // 是否锁定库存
        if (storageRelationManager.isStorageRelationLock())
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库存被锁定,不能开单");

            return mapping.findForward("error");
        }

        User user = (User)request.getSession().getAttribute("user");

        String locationId = Helper.getCurrentLocationId(request);

        String saves = request.getParameter("saves");

        // 客户信用级别
        String customercreditlevel = request.getParameter("customercreditlevel");

        String fullId = request.getParameter("fullId");

        if ("save".equals(saves))
        {
            saves = "保存";
        }
        else
        {
            saves = "提交";
        }

        OutBean outBean = new OutBean();

        outBean.setLocationId(locationId);

        // 增加职员的ID
        outBean.setStafferId(user.getStafferId());

        BeanUtil.getBean(outBean, request);

        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && outBean.getOutType() == OutConstant.INBILL_SELF_IN)
        {
            // 设置成调出
            outBean.setOutType(OutConstant.INBILL_OUT);

            String productLocationId = request.getParameter("sdestinationId");

            if (StringTools.isNullOrNone(productLocationId))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据不合法,请重新操作");

                return mapping.findForward("error");
            }

            outBean.setLocationId(productLocationId);

            outBean.setLocation(productLocationId);
        }

        if (StringTools.isNullOrNone(outBean.getLocation()))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有库存属性,请重新操作");

            return mapping.findForward("error");
        }

        ParamterMap map = new ParamterMap(request);

        ActionForward action = null;

        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            // 强制设置成OUT_SAIL_TYPE_MONEY
            if (CustomerConstant.BLACK_LEVEL.equals(customercreditlevel))
            {
                outBean.setReserve3(OutConstant.OUT_SAIL_TYPE_MONEY);
            }

            action = processCommonOut(mapping, request, user, saves, fullId, outBean, map);
        }
        else
        {
            // 入库单的处理
            try
            {
                outManager.addOut(outBean, map.getParameterMap(), user);

                if ("提交".equals(saves))
                {
                    outManager.submit(outBean.getFullId(), user,
                        StorageConstant.OPR_STORAGE_OUTBILLIN);
                }
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

                return mapping.findForward("error");
            }
            catch (Exception e)
            {
                _logger.error(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getMessage());

                return mapping.findForward("error");
            }
        }

        if (action != null)
        {
            return action;
        }

        request.getSession().setAttribute(KeyConstant.MESSAGE, "此库单的单号是:" + outBean.getFullId());

        CommonTools.removeParamers(request);

        RequestTools.actionInitQuery(request);

        return querySelfOut(mapping, form, request, reponse);
    }

    /**
     * 处理销售单的逻辑
     * 
     * @param mapping
     * @param request
     * @param user
     * @param saves
     * @param fullId
     * @param outBean
     * @param map
     */
    private ActionForward processCommonOut(ActionMapping mapping, HttpServletRequest request,
                                           User user, String saves, String fullId, OutBean outBean,
                                           ParamterMap map)
    {
        // 增加库单
        if ( !StringTools.isNullOrNone(fullId))
        {
            // 修改
            OutBean out = outDAO.find(fullId);

            if (out == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在");

                return mapping.findForward("error");
            }
        }

        try
        {
            String id = outManager.addOut(outBean, map.getParameterMap(), user);

            // 提交
            if (OutConstant.FLOW_DECISION_SUBMIT.equals(saves))
            {
                outManager.submit(id, user, StorageConstant.OPR_STORAGE_OUTBILL);

                checkSubmit(user, id);
            }
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            return mapping.findForward("error");
        }

        return null;
    }

    /**
     * checkSubmit
     * 
     * @param user
     * @param id
     */
    private void checkSubmit(User user, String id)
    {
        OutBean newOut = outDAO.findRealOut(id);

        if (newOut == null)
        {
            loggerError(id + " is errro in checkSubmit");

            return;
        }

        importLog.info(id + ":" + user.getStafferName() + "(after):" + newOut.getStatus() + "(预计:"
                       + OutConstant.STATUS_SUBMIT + ")");

        if (newOut.getReserve3() != OutConstant.OUT_SAIL_TYPE_LOCATION_MANAGER
            && newOut.getStatus() != OutConstant.STATUS_SUBMIT)
        {
            loggerError(id + ":" + user.getStafferName() + "(after):" + newOut.getStatus() + "(预计:"
                        + OutConstant.STATUS_SUBMIT + ")");
        }

        if (newOut.getReserve3() == OutConstant.OUT_SAIL_TYPE_LOCATION_MANAGER
            && newOut.getStatus() != OutConstant.STATUS_LOCATION_MANAGER_CHECK)
        {
            loggerError(id + ":" + user.getStafferName() + "(after):" + newOut.getStatus() + "(预计:"
                        + OutConstant.STATUS_LOCATION_MANAGER_CHECK + ")");
        }
    }

    /**
     * loggerError(严重错误的日志哦)
     * 
     * @param msg
     */
    private void loggerError(String msg)
    {
        importLog.error(msg);

        fatalNotify.notify(msg);
    }

    /**
     * 提交库单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward submitOut(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        return querySelfOut(mapping, form, request, reponse);
    }

    /**
     * 业务员查询销售单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward querySelfOut(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = (User)request.getSession().getAttribute("user");

        request.getSession().setAttribute("exportKey", QUERYSELFOUT);

        List<OutVO> list = null;

        CommonTools.saveParamers(request);

        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = getQuerySelfCondition(request, user);

                int tatol = outDAO.countVOByCondition(condtion.toString());

                PageSeparate page = new PageSeparate(tatol, PublicConstant.PAGE_SIZE - 5);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, QUERYSELFOUT);

                list = outDAO.queryEntityVOsByCondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, QUERYSELFOUT);

                list = outDAO.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(request,
                    QUERYSELFOUT), OldPageSeparateTools.getPageSeparate(request, QUERYSELFOUT));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询单据失败");

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        request.setAttribute("listOut1", list);

        // 发货单
        ConsignBean temp = null;

        for (OutBean outBean : list)
        {
            temp = consignDAO.findConsignById(outBean.getFullId());

            if (temp != null)
            {
                outBean.setConsign(temp.getReprotType());
            }
        }

        List<DepotBean> depotList = depotDAO.listEntityBeans();

        request.setAttribute("depotList", depotList);

        request.getSession().setAttribute("listOut1", list);

        request.setAttribute("now", TimeTools.now("yyyy-MM-dd"));

        getDivs(request, list);

        return mapping.findForward("querySelfOut");
    }

    /**
     * 查询自我的入库单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward querySelfBuy(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = (User)request.getSession().getAttribute("user");

        request.getSession().setAttribute("exportKey", QUERYSELFBUY);

        List<OutVO> list = null;

        CommonTools.saveParamers(request);

        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = getQuerySelfBuyCondition(request, user);

                int tatol = outDAO.countVOByCondition(condtion.toString());

                PageSeparate page = new PageSeparate(tatol, PublicConstant.PAGE_SIZE - 5);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, QUERYSELFBUY);

                list = outDAO.queryEntityVOsByCondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, QUERYSELFBUY);

                list = outDAO.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(request,
                    QUERYSELFBUY), OldPageSeparateTools.getPageSeparate(request, QUERYSELFBUY));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询单据失败");

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        request.setAttribute("listOut1", list);

        List<DepotBean> depotList = depotDAO.listEntityBeans();

        request.setAttribute("depotList", depotList);

        request.setAttribute("now", TimeTools.now("yyyy-MM-dd"));

        getDivs(request, list);

        return mapping.findForward("querySelfBuy");
    }

    /**
     * 查询委托结算清单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryOutBalance(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = (User)request.getSession().getAttribute("user");

        try
        {
            checkQueryOutBalanceAuth(request);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        List<OutBalanceVO> list = null;

        CommonTools.saveParamers(request);

        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = getQueryBalanceCondition(request, user);

                int tatol = outBalanceDAO.countVOByCondition(condtion.toString());

                PageSeparate page = new PageSeparate(tatol, PublicConstant.PAGE_SIZE - 5);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, QUERYSELFOUTBALANCE);

                list = outBalanceDAO.queryEntityVOsByCondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, QUERYSELFOUTBALANCE);

                list = outBalanceDAO.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(
                    request, QUERYSELFOUTBALANCE), OldPageSeparateTools.getPageSeparate(request,
                    QUERYSELFOUTBALANCE));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询单据失败");

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        request.setAttribute("resultList", list);

        return mapping.findForward("queryOutBalance");
    }

    /**
     * checkQueryOutAuth
     * 
     * @param request
     * @throws MYException
     */
    private void checkQueryOutAuth(HttpServletRequest request)
        throws MYException
    {
        // 权限校验
        String queryType = RequestTools.getValueFromRequest(request, "queryType");

        User user = (User)request.getSession().getAttribute("user");

        if ("1".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.SAIL_LOCATION_MANAGER))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("2".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.SAIL_MONEY_CENTER))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("3".equals(queryType) && !userManager.containAuth(user.getId(), AuthConstant.SAIL_FLOW))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("4".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.SAIL_ADMIN))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("5".equals(queryType) && !userManager.containAuth(user.getId(), AuthConstant.SAIL_SEC))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("6".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.SAIL_CENTER_CHECK))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("7".equals(queryType) && !userManager.containAuth(user.getId(), AuthConstant.SAIL_CEO))
        {
            throw new MYException("用户没有此操作的权限");
        }
    }

    /**
     * checkQueryOutAuth
     * 
     * @param request
     * @throws MYException
     */
    private void checkQueryOutBalanceAuth(HttpServletRequest request)
        throws MYException
    {
        // 权限校验
        String queryType = RequestTools.getValueFromRequest(request, "queryType");

        User user = (User)request.getSession().getAttribute("user");

        if ("2".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.SAIL_MONEY_CENTER))
        {
            throw new MYException("用户没有此操作的权限");
        }
    }

    /**
     * 销售单审批过程的查询
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryOut(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = (User)request.getSession().getAttribute("user");

        try
        {
            checkQueryOutAuth(request);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        request.getSession().setAttribute("exportKey", QUERYOUT);

        List<OutVO> list = null;

        CommonTools.saveParamers(request);

        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = getQueryCondition(request, user);

                int tatol = outDAO.countVOByCondition(condtion.toString());

                PageSeparate page = new PageSeparate(tatol, PublicConstant.PAGE_SIZE - 5);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, QUERYOUT);

                list = outDAO.queryEntityVOsByCondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, QUERYOUT);

                list = outDAO.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(request,
                    QUERYOUT), OldPageSeparateTools.getPageSeparate(request, QUERYOUT));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询单据失败");

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        request.setAttribute("listOut1", list);

        // 发货单
        ConsignBean temp = null;

        for (OutBean outBean : list)
        {
            temp = consignDAO.findConsignById(outBean.getFullId());

            if (temp != null)
            {
                outBean.setConsign(temp.getReprotType());
            }
        }

        // 处理仓库
        List<DepotBean> depotList = handerDepot(request, user);

        request.setAttribute("depotList", depotList);

        int radioIndex = CommonTools.parseInt(request.getParameter("radioIndex"));

        if (list.size() > 0 && radioIndex >= list.size())
        {
            request.setAttribute("radioIndex", list.size() - 1);
        }

        request.getSession().setAttribute("listOut1", list);

        request.setAttribute("now", TimeTools.now("yyyy-MM-dd"));

        handlerFlow(request, list);

        showLastCredit(request, user);

        getDivs(request, list);

        return mapping.findForward("queryOut");
    }

    /**
     * 处理仓库
     * 
     * @param request
     * @param user
     * @return
     */
    private List<DepotBean> handerDepot(HttpServletRequest request, User user)
    {
        // 这里是过滤
        String queryType = RequestTools.getValueFromRequest(request, "queryType");

        List<DepotBean> depotList = depotDAO.listEntityBeans();

        if ("3".equals(queryType) || "4".equals(queryType))
        {
            // 只能看到自己的仓库
            List<AuthBean> depotAuthList = userManager.queryExpandAuthById(user.getId(),
                AuthConstant.EXPAND_AUTH_DEPOT);

            for (Iterator iterator = depotList.iterator(); iterator.hasNext();)
            {
                DepotBean depotBean = (DepotBean)iterator.next();

                boolean delete = true;

                for (AuthBean authBean : depotAuthList)
                {
                    if (authBean.getId().equals(depotBean.getId()))
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
        return depotList;
    }

    /**
     * 处理部分物流的逻辑
     * 
     * @param request
     * @param list
     */
    private void handlerFlow(HttpServletRequest request, List<OutVO> list)
    {
        ConsignBean temp = null;

        // 物流的需要知道是否有发货单
        double total = 0.0d;

        Map<String, String> hasMap = new HashMap<String, String>();
        Map<String, Integer> overDayMap = new HashMap<String, Integer>();

        for (OutBean outBean : list)
        {
            temp = consignDAO.findConsignById(outBean.getFullId());

            if (temp != null)
            {
                outBean.setConsign(temp.getCurrentStatus());
            }

            total += outBean.getTotal();

            // 是否超期 超期几天
            if ( !StringTools.isNullOrNone(outBean.getRedate())
                && outBean.getPay() == OutConstant.PAY_NOT)
            {
                int overDays = TimeTools.cdate(TimeTools.now_short(), outBean.getRedate());

                overDayMap.put(outBean.getFullId(), overDays);
            }

            // 款到发货
            if (outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_MONEY
                && outBean.getPay() == OutConstant.PAY_YES)
            {
                overDayMap.put(outBean.getFullId(), 0);
            }

            if (hasOver(outBean.getStafferName()))
            {
                hasMap.put(outBean.getFullId(), "true");
            }
            else
            {
                hasMap.put(outBean.getFullId(), "false");
            }
        }

        request.setAttribute("hasMap", hasMap);

        request.setAttribute("overDayMap", overDayMap);
    }

    private ConditionParse getQuerySelfCondition(HttpServletRequest request, User user)
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

        // 只能查询自己的
        condtion.addCondition("OutBean.STAFFERID", "=", user.getStafferId());

        String outTime = request.getParameter("outTime");

        String outTime1 = request.getParameter("outTime1");

        if ( !StringTools.isNullOrNone(outTime))
        {
            condtion.addCondition("OutBean.outTime", ">=", outTime);
        }
        else
        {
            condtion.addCondition("OutBean.outTime", ">=", TimeTools.now_short( -7));

            request.setAttribute("outTime", TimeTools.now_short( -7));
        }

        if ( !StringTools.isNullOrNone(outTime1))
        {
            condtion.addCondition("OutBean.outTime", "<=", outTime1);
        }
        else
        {
            condtion.addCondition("OutBean.outTime", "<=", TimeTools.now_short());

            request.setAttribute("outTime1", TimeTools.now_short());
        }

        String id = request.getParameter("id");

        if ( !StringTools.isNullOrNone(id))
        {
            condtion.addCondition("OutBean.fullid", "like", id);
        }

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(status))
        {
            condtion.addIntCondition("OutBean.status", "=", status);
        }
        else
        {
            request.setAttribute("status", null);
        }

        String customerId = request.getParameter("customerId");

        if ( !StringTools.isNullOrNone(customerId))
        {
            condtion.addCondition("OutBean.customerId", "=", customerId);
        }

        String outType = request.getParameter("outType");

        if ( !StringTools.isNullOrNone(outType))
        {
            condtion.addIntCondition("OutBean.outType", "=", outType);
        }

        String location = request.getParameter("location");

        if ( !StringTools.isNullOrNone(location))
        {
            condtion.addCondition("OutBean.location", "=", location);
        }

        String redate = request.getParameter("redate");

        String reCom = request.getParameter("reCom");

        if ( !StringTools.isNullOrNone(redate) && !StringTools.isNullOrNone(reCom))
        {
            condtion.addCondition("OutBean.redate", reCom, redate);

            condtion.addCondition("and OutBean.redate <> ''");
        }

        String pay = request.getParameter("pay");

        if ( !StringTools.isNullOrNone(pay))
        {
            if ( !pay.equals(String.valueOf(OutConstant.PAY_OVER)))
            {
                condtion.addIntCondition("OutBean.pay", "=", pay);
            }
            else
            {
                condtion.addCondition(" and OutBean.status in (3, 4)");

                condtion.addCondition(" and OutBean.redate < '" + TimeTools.now_short() + "'");

                condtion.addIntCondition("OutBean.pay", "=", 0);
            }
        }

        String tempType = request.getParameter("tempType");

        if ( !StringTools.isNullOrNone(tempType))
        {
            condtion.addIntCondition("OutBean.tempType", "=", tempType);
        }

        condtion.addCondition("order by OutBean.fullid desc");

        return condtion;
    }

    /**
     * getQuerySelfBuyCondition
     * 
     * @param request
     * @param user
     * @return
     */
    private ConditionParse getQuerySelfBuyCondition(HttpServletRequest request, User user)
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        // 只能查询自己的
        condtion.addCondition("OutBean.STAFFERID", "=", user.getStafferId());

        String outTime = request.getParameter("outTime");

        String outTime1 = request.getParameter("outTime1");

        if ( !StringTools.isNullOrNone(outTime))
        {
            condtion.addCondition("OutBean.outTime", ">=", outTime);
        }
        else
        {
            condtion.addCondition("OutBean.outTime", ">=", TimeTools.now_short( -7));

            request.setAttribute("outTime", TimeTools.now_short( -7));
        }

        if ( !StringTools.isNullOrNone(outTime1))
        {
            condtion.addCondition("OutBean.outTime", "<=", outTime1);
        }
        else
        {
            condtion.addCondition("OutBean.outTime", "<=", TimeTools.now_short());

            request.setAttribute("outTime1", TimeTools.now_short());
        }

        String id = request.getParameter("id");

        if ( !StringTools.isNullOrNone(id))
        {
            condtion.addCondition("OutBean.fullid", "like", id);
        }

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(status))
        {
            condtion.addIntCondition("OutBean.status", "=", status);
        }
        else
        {
            request.setAttribute("status", null);
        }

        String customerId = request.getParameter("customerId");

        if ( !StringTools.isNullOrNone(customerId))
        {
            condtion.addCondition("OutBean.customerId", "=", customerId);
        }

        String outType = request.getParameter("outType");

        if ( !StringTools.isNullOrNone(outType))
        {
            condtion.addIntCondition("OutBean.outType", "=", outType);
        }

        String location = request.getParameter("location");

        if ( !StringTools.isNullOrNone(location))
        {
            condtion.addCondition("OutBean.location", "=", location);
        }

        String inway = request.getParameter("inway");

        if ( !StringTools.isNullOrNone(inway))
        {
            condtion.addIntCondition("inway", "=", inway);
        }

        condtion.addCondition("order by OutBean.fullid desc");

        return condtion;
    }

    /**
     * getQuerySelfBalanceCondition
     * 
     * @param request
     * @param user
     * @return
     */
    private ConditionParse getQueryBalanceCondition(HttpServletRequest request, User user)
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        String outTime = request.getParameter("outTime");

        String outTime1 = request.getParameter("outTime1");

        if ( !StringTools.isNullOrNone(outTime))
        {
            condtion.addCondition("OutBalanceBean.logTime", ">=", outTime);
        }
        else
        {
            condtion.addCondition("OutBalanceBean.logTime", ">=", TimeTools.now_short( -7));

            request.setAttribute("outTime", TimeTools.now_short( -7));
        }

        if ( !StringTools.isNullOrNone(outTime1))
        {
            condtion.addCondition("OutBalanceBean.logTime", "<=", outTime1);
        }
        else
        {
            condtion.addCondition("OutBalanceBean.logTime", "<=", TimeTools.now_short(1));

            request.setAttribute("outTime1", TimeTools.now_short(1));
        }

        String outId = request.getParameter("outId");

        if ( !StringTools.isNullOrNone(outId))
        {
            condtion.addCondition("OutBalanceBean.outId", "like", outId.trim());
        }

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(status))
        {
            condtion.addIntCondition("OutBalanceBean.status", "=", status);
        }

        String type = request.getParameter("type");

        if ( !StringTools.isNullOrNone(type))
        {
            condtion.addIntCondition("OutBalanceBean.type", "=", type);
        }

        // 权限校验
        String queryType = RequestTools.getValueFromRequest(request, "queryType");

        if ("1".equals(queryType))
        {
            // 只能查询自己的
            condtion.addCondition("OutBalanceBean.STAFFERID", "=", user.getStafferId());
        }
        // 查询审批的
        else if ("2".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBalanceBean.status", "=",
                    OutConstant.OUTBALANCE_STATUS_SUBMIT);

                request.setAttribute("status", OutConstant.OUTBALANCE_STATUS_SUBMIT);
            }
        }
        else
        {
            condtion.addFlaseCondition();
        }

        condtion.addCondition("order by OutBalanceBean.logTime desc");

        return condtion;
    }

    /**
     * 销售单审批过程的查询(条件的设置)
     * 
     * @param request
     * @param user
     * @return
     */
    private ConditionParse getQueryCondition(HttpServletRequest request, User user)
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

        String outTime = request.getParameter("outTime");

        String outTime1 = request.getParameter("outTime1");

        if ( !StringTools.isNullOrNone(outTime))
        {
            condtion.addCondition("OutBean.outTime", ">=", outTime);
        }
        else
        {
            condtion.addCondition("OutBean.outTime", ">=", TimeTools.now_short( -7));

            request.setAttribute("outTime", TimeTools.now_short( -7));
        }

        if ( !StringTools.isNullOrNone(outTime1))
        {
            condtion.addCondition("OutBean.outTime", "<=", outTime1);
        }
        else
        {
            condtion.addCondition("OutBean.outTime", "<=", TimeTools.now_short());

            request.setAttribute("outTime1", TimeTools.now_short());
        }

        String id = request.getParameter("id");

        if ( !StringTools.isNullOrNone(id))
        {
            condtion.addCondition("OutBean.fullid", "like", id);
        }

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(status))
        {
            condtion.addIntCondition("OutBean.status", "=", status);
        }

        String customerId = request.getParameter("customerId");

        if ( !StringTools.isNullOrNone(customerId))
        {
            condtion.addCondition("OutBean.customerId", "=", customerId);
        }

        String customerName = request.getParameter("customerName");

        if ( !StringTools.isNullOrNone(customerId))
        {
            condtion.addCondition("OutBean.customerName", "like", customerName);
        }

        String outType = request.getParameter("outType");

        if ( !StringTools.isNullOrNone(outType))
        {
            condtion.addIntCondition("OutBean.outType", "=", outType);
        }

        String location = request.getParameter("location");

        if ( !StringTools.isNullOrNone(location))
        {
            condtion.addCondition("OutBean.location", "=", location);
        }

        String redate = request.getParameter("redate");

        String reCom = request.getParameter("reCom");

        if ( !StringTools.isNullOrNone(redate) && !StringTools.isNullOrNone(reCom))
        {
            condtion.addCondition("OutBean.redate", reCom, redate);

            condtion.addCondition("and OutBean.redate <> ''");
        }

        String pay = request.getParameter("pay");

        if ( !StringTools.isNullOrNone(pay))
        {
            if ( !pay.equals(String.valueOf(OutConstant.PAY_OVER)))
            {
                condtion.addIntCondition("OutBean.pay", "=", pay);
            }
            else
            {
                condtion.addCondition(" and OutBean.status in (3, 4)");

                condtion.addCondition(" and OutBean.redate < '" + TimeTools.now_short() + "'");

                condtion.addIntCondition("OutBean.pay", "=", 0);
            }
        }

        String tempType = request.getParameter("tempType");

        if ( !StringTools.isNullOrNone(tempType))
        {
            condtion.addIntCondition("OutBean.tempType", "=", tempType);
        }

        // 这里是过滤
        String queryType = RequestTools.getValueFromRequest(request, "queryType");

        // 分公司经理查询
        if ("1".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=",
                    OutConstant.STATUS_LOCATION_MANAGER_CHECK);

                request.setAttribute("status", OutConstant.STATUS_LOCATION_MANAGER_CHECK);
            }

            condtion.addCondition("OutBean.locationId", "=", user.getLocationId());
        }
        // 结算中心查询
        else if ("2".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_SUBMIT);

                request.setAttribute("status", OutConstant.STATUS_SUBMIT);
            }
        }
        // 处理发货单 物流审批(只有中心仓库才有物流的)
        else if ("3".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_MANAGER_PASS);

                request.setAttribute("status", OutConstant.STATUS_MANAGER_PASS);
            }

            setDepotCondotion(user, condtion);

            condtion.addCondition("order by managerTime desc");
        }
        // 库管
        else if ("4".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_FLOW_PASS);

                request.setAttribute("status", OutConstant.STATUS_FLOW_PASS);
            }

            setDepotCondotion(user, condtion);

            condtion.addCondition("order by managerTime desc");
        }
        // 会计往来核对
        else if ("5".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_PASS);

                request.setAttribute("status", OutConstant.STATUS_PASS);

                condtion.addIntCondition("OutBean.pay", "=", OutConstant.PAY_NOT);

                request.setAttribute("pay", OutConstant.PAY_NOT);
            }

            condtion.addCondition("OutBean.locationId", "=", user.getLocationId());
        }
        // 总部核对(已经付款的销售单)
        else if ("6".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_PASS);

                request.setAttribute("status", OutConstant.STATUS_PASS);

                condtion.addIntCondition("OutBean.pay", "=", OutConstant.PAY_YES);

                request.setAttribute("pay", OutConstant.PAY_YES);
            }

        }
        // 总裁审批赠送
        else if ("7".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_CEO_CHECK);

                request.setAttribute("status", OutConstant.STATUS_CEO_CHECK);
            }
        }
        // 未知的则什么都没有
        else
        {
            condtion.addFlaseCondition();
        }

        if ( !condtion.containOrder())
        {
            condtion.addCondition("order by OutBean.fullid desc");
        }

        return condtion;
    }

    /**
     * 设置仓库的过滤条件
     * 
     * @param user
     * @param condtion
     */
    private void setDepotCondotion(User user, ConditionParse condtion)
    {
        // 只能看到自己的仓库
        List<AuthBean> depotAuthList = userManager.queryExpandAuthById(user.getId(),
            AuthConstant.EXPAND_AUTH_DEPOT);

        if (ListTools.isEmptyOrNull(depotAuthList))
        {
            // 永远也没有结果
            condtion.addFlaseCondition();
        }
        else
        {
            StringBuffer sb = new StringBuffer();

            sb.append("and (");
            for (Iterator iterator = depotAuthList.iterator(); iterator.hasNext();)
            {
                AuthBean authBean = (AuthBean)iterator.next();

                if (iterator.hasNext())
                {
                    sb.append("OutBean.location = '" + authBean.getId() + "' or ");
                }
                else
                {
                    sb.append("OutBean.location = '" + authBean.getId() + "'");
                }

            }

            sb.append(") ");

            condtion.addCondition(sb.toString());
        }
    }

    /**
     * 业务员预警
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryWarnOut(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = (User)request.getSession().getAttribute("user");

        ConditionParse condtion = new ConditionParse();

        // 获得条件
        getCondition(condtion, user.getStafferName());

        List<OutBean> list = outDAO.queryEntityBeansByCondition(condtion);

        long current = new Date().getTime();
        for (OutBean outBean : list)
        {
            Date temp = TimeTools.getDateByFormat(outBean.getRedate(), "yyyy-MM-dd");

            if (temp != null)
            {
                if (temp.getTime() < current)
                {
                    // 超期的
                    outBean.setPay(2);
                }
            }
        }

        // 提示页面
        getDivs(request, list);

        request.setAttribute("flagOut", list);

        return mapping.findForward("warnOutList");
    }

    private void getCondition(ConditionParse condtion, String stafferName)
    {
        // 只查询出库单
        condtion.addIntCondition("type", "=", OutConstant.OUT_TYPE_OUTBILL);

        condtion.addIntCondition("status", "=", OutConstant.STATUS_PASS);

        condtion.addCondition("STAFFERNAME", "=", stafferName);

        condtion.addIntCondition("pay", "=", OutConstant.PAY_NOT);

        condtion.addCondition("reday", "<>", "0");
    }

    /**
     * 删除库单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward delOut(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        User user = (User)request.getSession().getAttribute("user");

        if (StringTools.isNullOrNone(fullId))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作");

            return mapping.findForward("error");
        }

        OutBean bean = outDAO.find(fullId);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作");

            return mapping.findForward("error");
        }

        if (bean.getStatus() == OutConstant.STATUS_SAVE
            || bean.getStatus() == OutConstant.STATUS_REJECT)
        {
            try
            {
                outManager.delOut(fullId);

                importLog.info(user.getName() + "删除了库单:" + fullId);

                request.setAttribute(KeyConstant.MESSAGE, "库单删除成功:" + fullId);
            }
            catch (MYException e)
            {
                _logger.warn(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "流程异常，请重新操作:" + e.toString());

                return mapping.findForward("error");
            }
        }
        else
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有保存态,驳回态的库单才可以删除");

            return mapping.findForward("error");
        }

        request.setAttribute("forward", "10");

        return querySelfOut(mapping, form, request, reponse);
    }

    /**
     * 处理调出的库单(入库单的处理)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward processInvoke(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");
        String flag = request.getParameter("flag");

        User user = (User)request.getSession().getAttribute("user");

        if (StringTools.isNullOrNone(fullId))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作");

            return mapping.findForward("error");
        }

        OutBean bean = outDAO.find(fullId);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作");

            return mapping.findForward("error");
        }

        // 管理员通过和会计通过的都可以处理
        if ( ! ( (bean.getStatus() == OutConstant.STATUS_PASS || bean.getStatus() == OutConstant.STATUS_SEC_PASS) && bean
            .getOutType() == OutConstant.INBILL_OUT))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不能转调，请核实");

            return mapping.findForward("error");
        }

        if (bean.getInway() != OutConstant.IN_WAY)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不在在途中，不能处理");

            return mapping.findForward("error");
        }

        // 直接接受自动生成一个调入的库单
        if ("1".equals(flag))
        {
            OutBean newOut = (OutBean)ObjectTools.deepCopy(bean);

            newOut.setStatus(0);

            newOut.setLocationId(Helper.getCurrentLocationId(request));
            newOut.setLocation(Helper.getCurrentLocationId(request));

            newOut.setOutType(OutConstant.INBILL_IN);

            newOut.setFullId("");

            newOut.setRefOutFullId(fullId);

            newOut.setDestinationId("");

            newOut.setDescription("自动接收:" + fullId + ".生成的调入单据");

            newOut.setInway(0);

            newOut.setChecks("");

            newOut.setPay(OutConstant.PAY_NOT);

            newOut.setTotal( -newOut.getTotal());

            String depotpartId = request.getParameter("depotpartId");

            // TODO 选择接受的仓区
            // newOut.setLocation(OutConstant.SYSTEM_LOCATION);

            if ( !StringTools.isNullOrNone(depotpartId))
            {
                // 设置仓区
                newOut.setDepotpartId(depotpartId);
            }

            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

            for (BaseBean baseBean : baseList)
            {
                baseBean.setValue( -baseBean.getValue());
                baseBean.setLocationId(Helper.getCurrentLocationId(request));
                baseBean.setAmount( -baseBean.getAmount());
            }

            newOut.setBaseList(baseList);

            try
            {
                String ful = outManager.coloneOutAndSubmitAffair(newOut, user);

                request.setAttribute(KeyConstant.MESSAGE, fullId + "成功自动接收:" + ful);
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不能自动接收，请核实:"
                                                                + e.getErrorContent());

                return mapping.findForward("error");
            }

        }

        // 转调处理
        if ("2".equals(flag))
        {
            String changeLocationId = request.getParameter("changeLocationId");

            if (bean.getLocation().equals(changeLocationId))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "转调区域不能是产品调出区域，请核实");

                return mapping.findForward("error");
            }

            LocationBean lb = locationDAO.find(changeLocationId);

            if (lb == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "转调区域不存在，库单不能转调，请核实");

                return mapping.findForward("error");
            }
            bean.setDestinationId(changeLocationId);

            try
            {
                outManager.updateOut(bean);
            }
            catch (MYException e)
            {
                request
                    .setAttribute(KeyConstant.ERROR_MESSAGE, "库单不能转调，请核实:" + e.getErrorContent());

                return mapping.findForward("error");
            }

            request.setAttribute(KeyConstant.MESSAGE, fullId + "成功转调至:" + lb.getName());
        }

        // 直接驳回
        if ("3".equals(flag))
        {
            try
            {
                outManager.reject(fullId, user, "调出驳回");

                request.setAttribute(KeyConstant.MESSAGE, fullId + "成功驳回:" + fullId);
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                return mapping.findForward("error");
            }
        }

        request.setAttribute("forward", "10");

        // TODO
        request.setAttribute("Bflagg", "1");

        return querySelfOut(mapping, form, request, reponse);
    }

    /**
     * mark(标记单据)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward mark(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                              HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        OutBean bean = outDAO.find(fullId);

        outManager.mark(fullId, !bean.isMark());

        request.setAttribute("forward", "1");

        return querySelfOut(mapping, form, request, reponse);
    }

    /**
     * 通过委托结算
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward passOutBalance(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        User user = (User)request.getSession().getAttribute("user");

        try
        {
            outManager.passOutBalance(user, id);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());
        }

        RequestTools.actionInitQuery(request);

        request.setAttribute("queryType", "2");

        return queryOutBalance(mapping, form, request, reponse);
    }

    /**
     * 驳回委托结算
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rejectOutBalance(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        String reason = request.getParameter("reason");

        User user = (User)request.getSession().getAttribute("user");

        try
        {
            outManager.rejectOutBalance(user, id, reason);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());
        }

        RequestTools.actionInitQuery(request);

        return queryOutBalance(mapping, form, request, reponse);
    }

    /**
     * 删除委托结算
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward deleteOutBalance(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        User user = (User)request.getSession().getAttribute("user");

        try
        {
            outManager.deleteOutBalance(user, id);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());
        }

        RequestTools.actionInitQuery(request);

        return queryOutBalance(mapping, form, request, reponse);
    }

    /**
     * 总部核对(结束销售单)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward checks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        String checks = request.getParameter("reason");

        User user = (User)request.getSession().getAttribute("user");

        try
        {
            outManager.check(fullId, user, checks);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功核对单据:" + fullId);

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 付款(结算中心)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward payOut(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        OutBean out = outDAO.find(fullId);

        User user = (User)request.getSession().getAttribute("user");

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getReserve3() != OutConstant.OUT_SAIL_TYPE_MONEY)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "只能是款到发货的单据可以操作");

            return mapping.findForward("error");
        }

        if (out.getStatus() != OutConstant.STATUS_SUBMIT)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getPay() == OutConstant.PAY_YES)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "已经付款");

            return mapping.findForward("error");
        }

        outManager.modifyPay(user, fullId, OutConstant.PAY_YES, "结算中心确认收款");

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        request.setAttribute(KeyConstant.MESSAGE, "成功确认单据:" + fullId);

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 付款(财务收款心--往来核对)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward payOut2(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        OutBean out = outDAO.find(fullId);

        User user = (User)request.getSession().getAttribute("user");

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getStatus() != OutConstant.STATUS_PASS)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getPay() == OutConstant.PAY_YES)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "已经付款");

            return mapping.findForward("error");
        }

        outManager.modifyPay(user, fullId, OutConstant.PAY_YES, "财务确认收款");

        request.setAttribute(KeyConstant.MESSAGE, "成功核对单据:" + fullId);

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 修改库单的状态
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public synchronized ActionForward modifyOutStatus(ActionMapping mapping, ActionForm form,
                                                      HttpServletRequest request,
                                                      HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        User user = (User)request.getSession().getAttribute("user");

        int statuss = Integer.parseInt(request.getParameter("statuss"));

        String oldStatus = request.getParameter("oldStatus");

        if (StringTools.isNullOrNone(oldStatus))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有历史状态,请重新操作!");

            return mapping.findForward("error");
        }

        int ioldStatus = Integer.parseInt(oldStatus);

        String reason = request.getParameter("reason");

        String depotpartId = request.getParameter("depotpartId");

        importLog.info(fullId + ":" + user.getStafferName() + ";oldStatus:" + oldStatus);

        importLog.info(fullId + ":" + user.getStafferName() + ";nextStatus:" + statuss);

        CommonTools.saveParamers(request);

        OutBean out = null;

        out = outDAO.find(fullId);

        if (out.getStatus() == statuss)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请重新操作!");

            return mapping.findForward("error");
        }

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在，请重新操作!");

            return mapping.findForward("error");
        }

        if (out.getStatus() != ioldStatus)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "单据已经被审批,请重新操作!");

            return mapping.findForward("error");
        }

        int resultStatus = -1;

        // 入库单的提交
        if (out.getType() == OutConstant.OUT_TYPE_INBILL && statuss == OutConstant.STATUS_SUBMIT)
        {
            try
            {
                resultStatus = outManager.submit(out.getFullId(), user,
                    StorageConstant.OPR_STORAGE_OUTBILLIN);

                request.setAttribute(KeyConstant.MESSAGE, out.getFullId() + "库单成功提交!");
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理异常：" + e.getErrorContent());

                return mapping.findForward("error");
            }
        }
        else
        {
            // 业务员提交
            if (statuss == OutConstant.STATUS_SUBMIT)
            {
                try
                {
                    resultStatus = outManager.submit(fullId, user,
                        StorageConstant.OPR_STORAGE_OUTBILL);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 区域总经理信用审核通过
            if (statuss == OutConstant.STATUS_TEMP)
            {
                try
                {
                    resultStatus = outManager.pass(fullId, user, OutConstant.STATUS_SUBMIT, reason,
                        depotpartId);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 结算中心通过 物流管理员 库管通过 总裁通过
            if (statuss == OutConstant.STATUS_MANAGER_PASS
                || statuss == OutConstant.STATUS_FLOW_PASS || statuss == OutConstant.STATUS_PASS)
            {
                // 这里需要计算客户的信用金额-是否报送物流中心经理审批
                boolean outCredit = parameterDAO.getBoolean(SysConfigConstant.OUT_CREDIT);

                // 如果是黑名单的客户(且没有付款)
                if (outCredit && out.getReserve3() == OutConstant.OUT_SAIL_TYPE_MONEY
                    && out.getType() == OutConstant.OUT_TYPE_OUTBILL
                    && out.getPay() == OutConstant.PAY_NOT)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有结算中心确定已经回款后才可以审批此销售单");

                    return mapping.findForward("error");
                }

                try
                {
                    resultStatus = outManager.pass(fullId, user, statuss, reason, depotpartId);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 驳回
            if (statuss == OutConstant.STATUS_REJECT)
            {
                try
                {
                    resultStatus = outManager.reject(fullId, user, reason);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }
        }

        // 核对状态方式发生异常
        OutBean realOut = outDAO.findRealOut(fullId);

        if (realOut != null && realOut.getStatus() != resultStatus)
        {
            String msg = "严重错误,当前单据的状态应该是:" + OutHelper.getStatus(resultStatus) + ",而不是"
                         + OutHelper.getStatus(realOut.getStatus()) + ".请联系管理员确认此单的正确状态!";

            request.setAttribute(KeyConstant.ERROR_MESSAGE, msg);

            loggerError(fullId + ":" + msg);

            return mapping.findForward("error");

        }

        importLog.info(fullId + ":" + user.getStafferName() + ";form:" + oldStatus + ";to"
                       + resultStatus + "(SUCCESS)");

        RequestTools.actionInitQuery(request);

        request.setAttribute(KeyConstant.MESSAGE, "单据[" + fullId + "]操作成功");

        if (StringTools.isNullOrNone(request.getParameter("queryType")))
        {
            return querySelfOut(mapping, form, request, reponse);
        }

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 查询库单（或者修改）
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward findOut(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String outId = request.getParameter("outId");

        String fow = request.getParameter("fow");

        CommonTools.saveParamers(request);

        OutVO bean = null;
        try
        {
            bean = outDAO.findVO(outId);

            if (bean == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在,请重新操作");

                return mapping.findForward("error");
            }

            List<BaseBean> list = baseDAO.queryEntityBeansByFK(outId);

            bean.setBaseList(list);

            List<FlowLogBean> logs = flowLogDAO.queryEntityBeansByFK(outId);

            List<FlowLogVO> voList = ListTools.changeList(logs, FlowLogVO.class,
                FlowLogHelper.class, "getOutLogVO");

            request.setAttribute("bean", bean);

            request.setAttribute("fristBase", list.get(0));

            if (list.size() > 1)
            {
                request.setAttribute("lastBaseList", list.subList(1, list.size()));
            }

            request.setAttribute("logList", voList);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询库单失败");

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        innerForPrepare(request);

        if ("1".equals(fow))
        {
            // 处理修改
            return mapping.findForward("updateOut");
        }

        if ("4".equals(fow))
        {
            request.setAttribute("year", TimeTools.now("yyyy"));
            request.setAttribute("month", TimeTools.now("MM"));
            request.setAttribute("day", TimeTools.now("dd"));
            return mapping.findForward("print");
        }

        // 调出的处理
        if ("5".equals(fow))
        {
            if (bean.getInway() != OutConstant.IN_WAY)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不在在途中，不能处理");

                return mapping.findForward("error");
            }

            if (bean.getLocationId().equals(Helper.getCurrentLocationId(request)))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "不能操作自身区域的调拨");

                return mapping.findForward("error");
            }

            if ( !bean.getDestinationId().equals(Helper.getCurrentLocationId(request)))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "调出已经转到其他区域,没有权限处理");

                return mapping.findForward("error");
            }

            List<LocationBean> locationList = locationDAO.listEntityBeans();

            request.setAttribute("locationList", locationList);

            ConditionParse condition = new ConditionParse();

            condition.addWhereStr();

            condition.addIntCondition("type", "=", DepotConstant.DEPOTPART_TYPE_OK);

            List<DepotpartBean> depotpartList = depotpartDAO.queryEntityBeansByCondition(condition);

            request.setAttribute("depotpartList", depotpartList);

            // TODO

            return mapping.findForward("processOut");
        }

        if (bean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            return mapping.findForward("detailOut");
        }

        return mapping.findForward("detailBuy");
    }

    /**
     * showLastCredit(剩余的信用)
     * 
     * @param request
     * @param user
     */
    private void showLastCredit(HttpServletRequest request, User user)
    {
        double noPayBusiness = outDAO.sumAllNoPayAndAvouchBusinessByStafferId(user.getStafferId(),
            YYTools.getFinanceBeginDate(), YYTools.getFinanceEndDate());

        StafferBean sb2 = stafferDAO.find(user.getStafferId());

        if (sb2 != null)
        {
            // 设置其剩余的信用额度
            request.setAttribute("credit", ElTools.formatNum(sb2.getCredit() * sb2.getLever()
                                                             - noPayBusiness));
        }
    }

    /**
     * getDivs
     * 
     * @param request
     * @param list
     */
    private void getDivs(HttpServletRequest request, List list)
    {
        Map divMap = new HashMap();

        if (list != null)
        {
            for (Object each : list)
            {
                OutBean bean = (OutBean)each;

                try
                {
                    List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(bean.getFullId());

                    divMap.put(bean.getFullId(), OutHelper.createTable(baseList, bean.getTotal()));
                }
                catch (Exception e)
                {
                    _logger.error("addOut", e);
                }
            }
        }

        request.setAttribute("divMap", divMap);
    }

    /**
     * @return the userDAO
     */
    public UserDAO getUserDAO()
    {
        return userDAO;
    }

    /**
     * @param userDAO
     *            the userDAO to set
     */
    public void setUserDAO(UserDAO userDAO)
    {
        this.userDAO = userDAO;
    }

    /**
     * @return the productDAO
     */
    public ProductDAO getProductDAO()
    {
        return productDAO;
    }

    /**
     * @param productDAO
     *            the productDAO to set
     */
    public void setProductDAO(ProductDAO productDAO)
    {
        this.productDAO = productDAO;
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
     * @return the consignDAO
     */
    public ConsignDAO getConsignDAO()
    {
        return consignDAO;
    }

    /**
     * @param consignDAO
     *            the consignDAO to set
     */
    public void setConsignDAO(ConsignDAO consignDAO)
    {
        this.consignDAO = consignDAO;
    }

    /**
     * @return the depotpartDAO
     */
    public DepotpartDAO getDepotpartDAO()
    {
        return depotpartDAO;
    }

    /**
     * @param depotpartDAO
     *            the depotpartDAO to set
     */
    public void setDepotpartDAO(DepotpartDAO depotpartDAO)
    {
        this.depotpartDAO = depotpartDAO;
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
     * @return the storageDAO
     */
    public StorageDAO getStorageDAO()
    {
        return storageDAO;
    }

    /**
     * @param storageDAO
     *            the storageDAO to set
     */
    public void setStorageDAO(StorageDAO storageDAO)
    {
        this.storageDAO = storageDAO;
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
     * @return the stafferDAO
     */
    public StafferDAO getStafferDAO()
    {
        return stafferDAO;
    }

    /**
     * @param stafferDAO
     *            the stafferDAO to set
     */
    public void setStafferDAO(StafferDAO stafferDAO)
    {
        this.stafferDAO = stafferDAO;
    }

    /**
     * @return the departmentDAO
     */
    public DepartmentDAO getDepartmentDAO()
    {
        return departmentDAO;
    }

    /**
     * @param departmentDAO
     *            the departmentDAO to set
     */
    public void setDepartmentDAO(DepartmentDAO departmentDAO)
    {
        this.departmentDAO = departmentDAO;
    }

    /**
     * @return the baseDAO
     */
    public BaseDAO getBaseDAO()
    {
        return baseDAO;
    }

    /**
     * @param baseDAO
     *            the baseDAO to set
     */
    public void setBaseDAO(BaseDAO baseDAO)
    {
        this.baseDAO = baseDAO;
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
     * @return the locationDAO
     */
    public LocationDAO getLocationDAO()
    {
        return locationDAO;
    }

    /**
     * @param locationDAO
     *            the locationDAO to set
     */
    public void setLocationDAO(LocationDAO locationDAO)
    {
        this.locationDAO = locationDAO;
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
     * @return the depotDAO
     */
    public DepotDAO getDepotDAO()
    {
        return depotDAO;
    }

    /**
     * @param depotDAO
     *            the depotDAO to set
     */
    public void setDepotDAO(DepotDAO depotDAO)
    {
        this.depotDAO = depotDAO;
    }

    /**
     * @return the storageRelationManager
     */
    public StorageRelationManager getStorageRelationManager()
    {
        return storageRelationManager;
    }

    /**
     * @param storageRelationManager
     *            the storageRelationManager to set
     */
    public void setStorageRelationManager(StorageRelationManager storageRelationManager)
    {
        this.storageRelationManager = storageRelationManager;
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
     * @return the fatalNotify
     */
    public FatalNotify getFatalNotify()
    {
        return fatalNotify;
    }

    /**
     * @param fatalNotify
     *            the fatalNotify to set
     */
    public void setFatalNotify(FatalNotify fatalNotify)
    {
        this.fatalNotify = fatalNotify;
    }

    /**
     * @return the authManager
     */
    public AuthManager getAuthManager()
    {
        return authManager;
    }

    /**
     * @param authManager
     *            the authManager to set
     */
    public void setAuthManager(AuthManager authManager)
    {
        this.authManager = authManager;
    }

    /**
     * @return the baseBalanceDAO
     */
    public BaseBalanceDAO getBaseBalanceDAO()
    {
        return baseBalanceDAO;
    }

    /**
     * @param baseBalanceDAO
     *            the baseBalanceDAO to set
     */
    public void setBaseBalanceDAO(BaseBalanceDAO baseBalanceDAO)
    {
        this.baseBalanceDAO = baseBalanceDAO;
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
     * @return the outQueryDAO
     */
    public OutQueryDAO getOutQueryDAO()
    {
        return outQueryDAO;
    }

    /**
     * @param outQueryDAO
     *            the outQueryDAO to set
     */
    public void setOutQueryDAO(OutQueryDAO outQueryDAO)
    {
        this.outQueryDAO = outQueryDAO;
    }
}
