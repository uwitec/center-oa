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
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.common.OldPageSeparateTools;
import com.china.center.actionhelper.common.PageSeparateTools;
import com.china.center.actionhelper.jsonimpl.JSONArray;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.customer.constant.CustomerConstant;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.customer.wrap.NotPayWrap;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.vo.InBillVO;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.bean.ProviderBean;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.ProviderDAO;
import com.china.center.oa.product.dao.StorageDAO;
import com.china.center.oa.product.helper.StorageRelationHelper;
import com.china.center.oa.product.manager.StorageRelationManager;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.AuthBean;
import com.china.center.oa.publics.bean.DepartmentBean;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.bean.ShowBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.InvoiceConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.constant.PublicLock;
import com.china.center.oa.publics.constant.SysConfigConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.DepartmentDAO;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.dao.InvoiceDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.dao.ShowDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.manager.AuthManager;
import com.china.center.oa.publics.manager.FatalNotify;
import com.china.center.oa.publics.manager.StafferManager;
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

    private InBillDAO inBillDAO = null;

    private ShowDAO showDAO = null;

    private UserManager userManager = null;

    private StafferManager stafferManager = null;

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

    private static String QUERYBUY = "queryBuy";

    private static String RPTQUERYOUT = "rptQueryOut";

    private static String RPTQUERYOUTBALANCE = "rptQueryOutBalance";

    private static Object S_LOCK = new Object();

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

        OutBean out = outDAO.find(outId);

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

        // 选择仓库
        List<DepotBean> depotList = depotDAO.listEntityBeans();

        request.setAttribute("depotList", depotList);

        request.setAttribute("out", out);

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

        OutBalanceBean bean = outBalanceDAO.findVO(id);

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
        String flag = RequestTools.getValueFromRequest(request, "flag");

        // 得到部门
        List<DepartmentBean> list2 = departmentDAO.listEntityBeans();

        User user = Helper.getUser(request);

        // 仓库是自己选择的
        request.setAttribute("departementList", list2);

        request.setAttribute("current", TimeTools.now("yyyy-MM-dd"));

        List<DepotBean> locationList = depotDAO.listEntityBeans();

        request.setAttribute("locationList", locationList);

        // 只能看到自己的仓库
        if ("1".equals(flag))
        {
            List<AuthBean> depotAuthList = userManager.queryExpandAuthById(user.getId(),
                AuthConstant.EXPAND_AUTH_DEPOT);

            for (Iterator iterator = locationList.iterator(); iterator.hasNext();)
            {
                DepotBean depotBean = (DepotBean)iterator.next();

                boolean had = false;

                for (AuthBean authBean : depotAuthList)
                {
                    if (authBean.getId().equals(depotBean.getId()))
                    {
                        had = true;

                        break;
                    }
                }

                if ( !had)
                {
                    iterator.remove();
                }

            }

            List<DepotBean> dirLocationList = depotDAO.listEntityBeans();

            request.setAttribute("dirLocationList", dirLocationList);
        }

        int goDays = parameterDAO.getInt(SysConfigConstant.OUT_PERSONAL_REDAY);

        request.setAttribute("goDays", goDays);

        ConditionParse condition = new ConditionParse();

        User oprUser = Helper.getUser(request);

        condition.addCondition("locationId", "=", oprUser.getLocationId());

        showLastCredit(request, user, flag);

        List<InvoiceBean> invoiceList = invoiceDAO.queryEntityBeansByCondition("where forward = ?",
            InvoiceConstant.INVOICE_FORWARD_OUT);

        request.setAttribute("invoiceList", invoiceList);

        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList", dutyList);

        // 查询开单品名
        List<ShowBean> showList = showDAO.listEntityBeans();

        JSONArray shows = new JSONArray(showList, true);

        request.setAttribute("showJSON", shows.toString());

        StafferBean staffer = stafferDAO.find(user.getStafferId());

        request.setAttribute("staffer", staffer);
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
     * TEMPLATE 导出XLS文件
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

        try
        {
            setOutBalanceBean(bean, request);

            User user = (User)request.getSession().getAttribute("user");

            RequestTools.actionInitQuery(request);

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
     * 个人领样退库
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward outBack(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = Helper.getUser(request);

        String outId = request.getParameter("outId");

        String adescription = request.getParameter("adescription");

        // 查询是否被关联
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        con.addIntCondition("OutBean.status", "=", OutConstant.STATUS_SAVE);

        con.addIntCondition("OutBean.outType", "=", OutConstant.OUTTYPE_IN_SWATCH);

        int count = outDAO.countByCondition(con.toString());

        if (count > 0)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "此领样已经申请退货请处理结束后再申请");

            return mapping.findForward("error");
        }

        String[] baseItems = request.getParameterValues("baseItem");

        String[] backUnms = request.getParameterValues("backUnm");

        OutBean oldOut = outDAO.find(outId);

        if (oldOut == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (oldOut.getType() != OutConstant.OUT_TYPE_OUTBILL
            && oldOut.getOutType() != OutConstant.OUTTYPE_OUT_SWATCH)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        OutBean out = new OutBean();

        out.setStatus(OutConstant.STATUS_SAVE);

        out.setStafferName(user.getStafferName());

        out.setStafferId(user.getStafferId());

        out.setType(OutConstant.OUT_TYPE_INBILL);

        out.setOutTime(TimeTools.now_short());

        out.setDepartment("采购部");

        out.setCustomerId("99");

        out.setCustomerName("系统内置供应商");

        // 所在区域
        out.setLocationId(user.getLocationId());

        // 目的仓库
        out.setLocation(oldOut.getLocation());

        out.setInway(OutConstant.IN_WAY_NO);

        out.setOutType(OutConstant.OUTTYPE_IN_SWATCH);

        out.setRefOutFullId(outId);

        out.setDescription("个人领样退库,领样单号:" + outId + ". " + adescription);

        List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outId);

        List<OutBean> refBuyList = queryRefOut(request, outId);

        // 计算出已经退货的数量
        for (BaseBean baseBean : baseList)
        {
            int hasBack = 0;

            for (OutBean ref : refBuyList)
            {
                List<BaseBean> refBaseList = ref.getBaseList();

                for (BaseBean refBase : refBaseList)
                {
                    if (refBase.equals(baseBean))
                    {
                        hasBack += refBase.getAmount();

                        break;
                    }
                }
            }

            baseBean.setInway(hasBack);
        }

        double total = 0.0d;

        List<BaseBean> newBaseList = new ArrayList<BaseBean>();

        for (int i = 0; i < baseItems.length; i++ )
        {
            for (BaseBean each : baseList)
            {
                if (each.getId().equals(baseItems[i]))
                {
                    int back = CommonTools.parseInt(backUnms[i]);

                    if (each.getInway() + back > each.getAmount())
                    {
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, each.getProductName()
                                                                        + "的退货数量超过:"
                                                                        + each.getAmount());

                        return mapping.findForward("error");

                    }

                    if (back > 0)
                    {
                        // 增加base
                        BaseBean baseBean = new BaseBean();

                        baseBean.setValue(each.getCostPrice() * back);
                        baseBean.setLocationId(out.getLocation());
                        baseBean.setAmount(back);
                        baseBean.setProductName(each.getProductName());
                        baseBean.setUnit("套");
                        baseBean.setPrice(each.getCostPrice());
                        baseBean.setShowId(each.getShowId());
                        baseBean.setCostPrice(each.getCostPrice());
                        baseBean.setProductId(each.getProductId());
                        baseBean.setCostPriceKey(StorageRelationHelper.getPriceKey(each
                            .getCostPrice()));
                        baseBean.setOwner(each.getOwner());
                        baseBean.setDepotpartId(each.getDepotpartId());

                        baseBean.setDepotpartName(each.getDepotpartName());
                        // 成本
                        baseBean.setDescription(String.valueOf(each.getPrice()));

                        newBaseList.add(baseBean);

                        total += baseBean.getValue();
                    }
                }
            }
        }

        out.setTotal(total);

        out.setBaseList(newBaseList);

        try
        {
            if (newBaseList.size() > 0)
            {
                // CORE 个人领样退库
                String fullId = outManager.coloneOutWithAffair(out, user,
                    StorageConstant.OPR_STORAGE_SWATH);

                request.setAttribute(KeyConstant.MESSAGE, "成功申请退库:" + fullId);
            }
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e);
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        request.setAttribute("queryType", "9");

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 收集数据
     * 
     * @param pbean
     * @param item
     * @param request
     * @throws MYException
     */
    private void setOutBalanceBean(OutBalanceBean bean, HttpServletRequest request)
        throws MYException
    {
        User user = (User)request.getSession().getAttribute("user");

        String description = request.getParameter("description");

        String type = request.getParameter("type");

        String outId = request.getParameter("outId");

        OutBean out = outDAO.find(outId);

        if (out == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        bean.setCustomerId(out.getCustomerId());

        String dirDepot = request.getParameter("dirDepot");

        bean.setDescription(description);

        bean.setLogTime(TimeTools.now());

        bean.setStafferId(user.getStafferId());

        bean.setOutId(outId);

        bean.setDirDepot(dirDepot);

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
     * 增加(保存修改)修改库单(包括销售单和入库单)
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
            && outBean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT)
        {
            if (StringTools.isNullOrNone(outBean.getDestinationId()))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "调拨没有目的仓库属性,请重新操作");

                return mapping.findForward("error");
            }

            outBean.setReserve1(OutConstant.MOVEOUT_OUT);
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
            // 默认很多属性
            outBean.setStafferId(user.getStafferId());
            outBean.setStafferName(user.getStafferName());
            outBean.setCustomerId(CustomerConstant.PUBLIC_CUSTOMER_ID);
            outBean.setCustomerName(CustomerConstant.PUBLIC_CUSTOMER_NAME);
            outBean.setDepartment("公共部门");
            outBean.setArriveDate(TimeTools.now_short(10));

            // 入库单的处理
            try
            {
                outManager.addOut(outBean, map.getParameterMap(), user);

                if ("提交".equals(saves))
                {
                    outManager.submit(outBean.getFullId(), user,
                        StorageConstant.OPR_STORAGE_INOTHER);
                }
            }
            catch (MYException e)
            {
                _logger.warn(e, e);

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

        CommonTools.removeParamers(request);

        OutBean checkOut = outDAO.find(outBean.getFullId());

        request
            .getSession()
            .setAttribute(
                KeyConstant.MESSAGE,
                "此库单的单号是:" + outBean.getFullId() + ".下一步是:"
                    + OutHelper.getStatus(checkOut.getStatus()));

        CommonTools.removeParamers(request);

        RequestTools.actionInitQuery(request);

        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            return querySelfOut(mapping, form, request, reponse);
        }
        else
        {
            return querySelfBuy(mapping, form, request, reponse);
        }
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
     * 入库单通过
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
        synchronized (S_LOCK)
        {
            String fullId = request.getParameter("outId");

            OutBean out = outDAO.find(fullId);

            User user = (User)request.getSession().getAttribute("user");

            if (out == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

                return mapping.findForward("error");
            }

            if (out.getStatus() != OutConstant.STATUS_SAVE)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

                return mapping.findForward("error");
            }

            try
            {
                outManager.submit(fullId, user, StorageConstant.OPR_STORAGE_SWATH);
            }
            catch (MYException e)
            {
                _logger.warn(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

                return mapping.findForward("error");
            }

            CommonTools.saveParamers(request);

            RequestTools.menuInitQuery(request);

            request.setAttribute("queryType", "5");

            request.setAttribute(KeyConstant.MESSAGE, "成功确认单据:" + fullId);

            return queryBuy(mapping, form, request, reponse);
        }
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
                outBean.setConsign(temp.getCurrentStatus());
            }
        }

        List<InvoiceBean> invoiceList = invoiceDAO.queryEntityBeansByCondition("where forward = ?",
            InvoiceConstant.INVOICE_FORWARD_OUT);

        request.setAttribute("invoiceList", invoiceList);

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

        for (OutVO outVO : list)
        {
            if (outVO.getStatus() == OutConstant.STATUS_PASS)
            {
                // 标识为结束
                outVO.setStatus(OutConstant.STATUS_SEC_PASS);
            }
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

        if ("8".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.BUY_SUBMIT))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("9".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.SAIL_SUBMIT))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("10".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.SAIL_QUERY_SUB))
        {
            throw new MYException("用户没有此操作的权限");
        }
    }

    /**
     * checkQueryBuyAuth
     * 
     * @param request
     * @throws MYException
     */
    private void checkQueryBuyAuth(HttpServletRequest request)
        throws MYException
    {
        // 权限校验
        String queryType = RequestTools.getValueFromRequest(request, "queryType");

        User user = (User)request.getSession().getAttribute("user");

        if ("1".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.BUY_LOCATION_MANAGER))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("2".equals(queryType) && !userManager.containAuth(user.getId(), AuthConstant.BUY_CEO))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("3".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.BUY_CHAIRMA))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("4".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.BUY_SUBMIT))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("5".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.BUY_SUBMIT))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("6".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.BUY_QUERYALL))
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

        List<InvoiceBean> invoiceList = invoiceDAO.queryEntityBeansByCondition("where forward = ?",
            InvoiceConstant.INVOICE_FORWARD_OUT);

        request.setAttribute("invoiceList", invoiceList);

        request.getSession().setAttribute("listOut1", list);

        List<DepartmentBean> departementList = departmentDAO.listEntityBeans();

        request.setAttribute("departementList", departementList);

        request.setAttribute("now", TimeTools.now("yyyy-MM-dd"));

        handlerFlow(request, list, true);

        showLastCredit(request, user, "0");

        getDivs(request, list);

        return mapping.findForward("queryOut");
    }

    /**
     * 入库单审批过程的查询
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryBuy(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = (User)request.getSession().getAttribute("user");

        try
        {
            checkQueryBuyAuth(request);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        request.getSession().setAttribute("exportKey", QUERYBUY);

        List<OutVO> list = null;

        CommonTools.saveParamers(request);

        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = getQueryBuyCondition(request, user);

                int tatol = outDAO.countVOByCondition(condtion.toString());

                PageSeparate page = new PageSeparate(tatol, PublicConstant.PAGE_SIZE - 5);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, QUERYBUY);

                list = outDAO.queryEntityVOsByCondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, QUERYBUY);

                list = outDAO.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(request,
                    QUERYBUY), OldPageSeparateTools.getPageSeparate(request, QUERYBUY));
            }
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询单据失败");

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        for (OutVO outVO : list)
        {
            if (outVO.getStatus() == OutConstant.STATUS_PASS)
            {
                // 标识为结束
                outVO.setStatus(OutConstant.STATUS_SEC_PASS);
            }
        }

        request.setAttribute("listOut1", list);

        List<DepotBean> depotList = depotDAO.listEntityBeans();

        request.setAttribute("depotList", depotList);

        int radioIndex = CommonTools.parseInt(request.getParameter("radioIndex"));

        if (list.size() > 0 && radioIndex >= list.size())
        {
            request.setAttribute("radioIndex", list.size() - 1);
        }

        request.getSession().setAttribute("listOut1", list);

        request.setAttribute("now", TimeTools.now("yyyy-MM-dd"));

        getDivs(request, list);

        return mapping.findForward("queryBuy");
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
     * @param alert
     */
    private void handlerFlow(HttpServletRequest request, List<OutVO> list, boolean alert)
    {
        ConsignBean temp = null;

        // 物流的需要知道是否有发货单
        double total = 0.0d;

        Map<String, String> hasMap = new HashMap<String, String>();
        Map<String, String> overDayMap = new HashMap<String, String>();

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

                if (overDays < 0)
                {
                    overDayMap.put(outBean.getFullId(), String.valueOf(overDays));
                }
                else
                {
                    overDayMap.put(outBean.getFullId(), "<font color=red><b>" + overDays
                                                        + "</b></font>");
                }
            }

            // 款到发货
            if (outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_MONEY
                && outBean.getPay() == OutConstant.PAY_YES)
            {
                overDayMap.put(outBean.getFullId(), String.valueOf(0));
            }

            if (alert)
            {
                if (hasOver(outBean.getStafferName()))
                {
                    hasMap.put(outBean.getFullId(), "true");
                }
                else
                {
                    hasMap.put(outBean.getFullId(), "false");
                }
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

        String department = request.getParameter("department");

        if ( !StringTools.isNullOrNone(department))
        {
            condtion.addCondition("OutBean.department", "=", department);
        }

        String customerName = request.getParameter("customerName");

        if ( !StringTools.isNullOrNone(customerId))
        {
            condtion.addCondition("OutBean.customerName", "like", customerName);
        }

        String stafferName = request.getParameter("stafferName");

        if ( !StringTools.isNullOrNone(stafferName))
        {
            condtion.addCondition("OutBean.stafferName", "like", stafferName);
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

        StafferBean staffer = Helper.getStaffer(request);

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

            condtion.addCondition("OutBean.industryId", "=", staffer.getIndustryId());
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

            setDepotCondotionInOut(user, condtion);

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

            setDepotCondotionInOut(user, condtion);

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
        // 查询没有结束的个人领样
        else if ("8".equals(queryType))
        {
            condtion.addIntCondition("OutBean.outType", "=", OutConstant.OUTTYPE_OUT_SWATCH);

            condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_PASS);

            request.setAttribute("outType", OutConstant.OUTTYPE_OUT_SWATCH);

            request.setAttribute("status", OutConstant.STATUS_PASS);

            setDepotCondotionInOut(user, condtion);
        }
        // 查询没有结束的个人领样
        else if ("9".equals(queryType))
        {
            condtion.addIntCondition("OutBean.outType", "=", OutConstant.OUTTYPE_OUT_SWATCH);

            condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_PASS);

            condtion.addCondition("OutBean.stafferId", "=", user.getStafferId());

            request.setAttribute("outType", OutConstant.OUTTYPE_OUT_SWATCH);

            request.setAttribute("status", OutConstant.STATUS_PASS);

            setDepotCondotionInOut(user, condtion);
        }
        // 查询下属的销售单
        else if ("10".equals(queryType))
        {
            condtion.addCondition("OutBean.industryId2", "=", staffer.getIndustryId2());
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
     * 入库单审批过程的查询(条件的设置)
     * 
     * @param request
     * @param user
     * @return
     */
    private ConditionParse getQueryBuyCondition(HttpServletRequest request, User user)
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

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

        String inway = request.getParameter("inway");

        if ( !StringTools.isNullOrNone(inway))
        {
            condtion.addIntCondition("OutBean.inway", "=", inway);
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
        // 总裁审核
        else if ("2".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_CEO_CHECK);

                request.setAttribute("status", OutConstant.STATUS_CEO_CHECK);
            }
        }
        // 董事长审核
        else if ("3".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_CHAIRMA_CHECK);

                request.setAttribute("status", OutConstant.STATUS_CHAIRMA_CHECK);
            }
        }
        // 库管调拨
        else if ("4".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_SUBMIT);

                request.setAttribute("status", OutConstant.STATUS_SUBMIT);

                condtion.addIntCondition("OutBean.inway", "=", OutConstant.IN_WAY);

                request.setAttribute("inway", OutConstant.IN_WAY);
            }

            setDepotCondotionInBuy(user, condtion);
        }
        // 领样退库
        else if ("5".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_SAVE);

                request.setAttribute("status", OutConstant.STATUS_SAVE);

                condtion.addIntCondition("OutBean.outType", "=", OutConstant.OUTTYPE_IN_SWATCH);

                request.setAttribute("outType", OutConstant.OUTTYPE_IN_SWATCH);

            }

            setLocalDepotConditionInBuy(user, condtion);
        }
        else if ("6".equals(queryType))
        {
            // 可以查询所有
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
    private void setDepotCondotionInBuy(User user, ConditionParse condtion)
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

                // 接受仓库是自己管辖的
                if (iterator.hasNext())
                {
                    sb.append("OutBean.destinationId = '" + authBean.getId() + "' or ");
                }
                else
                {
                    sb.append("OutBean.destinationId = '" + authBean.getId() + "'");
                }

            }

            sb.append(") ");

            condtion.addCondition(sb.toString());
        }
    }

    /**
     * 设置发货仓库的过滤条件
     * 
     * @param user
     * @param condtion
     */
    private void setLocalDepotConditionInBuy(User user, ConditionParse condtion)
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

                // 接受仓库是自己管辖的
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
     * 设置仓库的过滤条件
     * 
     * @param user
     * @param condtion
     */
    private void setDepotCondotionInOut(User user, ConditionParse condtion)
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

                // 接受仓库是自己管辖的
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

        List<OutVO> list = outDAO.queryEntityVOsByCondition(condtion);

        long current = new Date().getTime();

        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            OutVO outBean = (OutVO)iterator.next();

            Date temp = TimeTools.getDateByFormat(outBean.getRedate(), "yyyy-MM-dd");

            if (temp != null)
            {
                if (temp.getTime() > current)
                {
                    iterator.remove();
                }
            }
        }

        handlerFlow(request, list, false);

        // 提示页面
        getDivs(request, list);

        request.setAttribute("listOut1", list);

        return mapping.findForward("queryWarnOut");
    }

    /**
     * getCondition
     * 
     * @param condtion
     * @param stafferName
     */
    private void getCondition(ConditionParse condtion, String stafferName)
    {
        // 只查询销售单
        condtion.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

        condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_PASS);

        condtion.addCondition("OutBean.STAFFERNAME", "=", stafferName);

        condtion.addIntCondition("OutBean.pay", "=", OutConstant.PAY_NOT);

        condtion.addCondition("OutBean.reday", "<>", "0");
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
                outManager.delOut(user, fullId);

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

        CommonTools.removeParamers(request);

        RequestTools.actionInitQuery(request);

        if (bean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            return querySelfOut(mapping, form, request, reponse);
        }
        else
        {
            return querySelfBuy(mapping, form, request, reponse);
        }
    }

    /**
     * rejectBack
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rejectBack(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse reponse)
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
                outManager.delOut(user, fullId);

                request.setAttribute(KeyConstant.MESSAGE, "成功操作:" + fullId);
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
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有保存态的库单才可以驳回");

            return mapping.findForward("error");
        }

        CommonTools.removeParamers(request);

        RequestTools.menuInitQuery(request);

        request.setAttribute("queryType", "5");

        return queryBuy(mapping, form, request, reponse);
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

        if ( ! (bean.getType() == OutConstant.OUT_TYPE_INBILL && bean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT))
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
            OutBean newOut = new OutBean(bean);

            newOut.setStatus(0);

            newOut.setLocationId(user.getLocationId());

            // 仓库就是调出的目的仓区
            newOut.setLocation(bean.getDestinationId());

            newOut.setOutType(OutConstant.OUTTYPE_IN_MOVEOUT);

            newOut.setFullId("");

            newOut.setRefOutFullId(fullId);

            newOut.setDestinationId(bean.getLocation());

            newOut.setDescription("自动接收调拨单:" + fullId + ".生成的调入单据");

            newOut.setInway(OutConstant.IN_WAY_NO);

            newOut.setChecks("");

            // 调入的单据
            newOut.setReserve1(OutConstant.MOVEOUT_IN);

            newOut.setPay(OutConstant.PAY_NOT);

            newOut.setTotal( -newOut.getTotal());

            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

            DepotpartBean defaultOKDepotpart = depotpartDAO.findDefaultOKDepotpart(bean
                .getDestinationId());

            if (defaultOKDepotpart == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "仓库下没有良品仓，请核实");

                return mapping.findForward("error");
            }

            for (BaseBean baseBean : baseList)
            {
                // 获得仓库默认的仓区
                baseBean.setDepotpartId(defaultOKDepotpart.getId());
                baseBean.setValue( -baseBean.getValue());
                baseBean.setLocationId(bean.getDestinationId());
                baseBean.setAmount( -baseBean.getAmount());
            }

            newOut.setBaseList(baseList);

            try
            {
                String ful = outManager.coloneOutAndSubmitAffair(newOut, user,
                    StorageConstant.OPR_STORAGE_REDEPLOY);

                request.setAttribute(KeyConstant.MESSAGE, fullId + "成功自动接收:" + ful);
            }
            catch (MYException e)
            {
                _logger.warn(e, e);

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

            bean.setDestinationId(changeLocationId);

            try
            {
                outManager.updateOut(bean);
            }
            catch (MYException e)
            {
                _logger.warn(e, e);

                request
                    .setAttribute(KeyConstant.ERROR_MESSAGE, "库单不能转调，请核实:" + e.getErrorContent());

                return mapping.findForward("error");
            }

            request.setAttribute(KeyConstant.MESSAGE, fullId + "成功转调");
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
                _logger.warn(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                return mapping.findForward("error");
            }
        }

        request.setAttribute("forward", "10");

        request.setAttribute("queryType", "4");

        return queryBuy(mapping, form, request, reponse);
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

        // LOCK 委托代销结算单通过(退库)
        synchronized (PublicLock.PRODUCT_CORE)
        {
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
            _logger.warn(e, e);

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

        try
        {
            outManager.payOut(user, fullId, "结算中心确认收款");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

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

        try
        {
            outManager.payOut(user, fullId, "财务确认收款");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功核对单据:" + fullId);

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 产生坏账
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward payOut3(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        String baddebts = request.getParameter("baddebts");

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

        try
        {
            outManager.payBaddebts(user, fullId, MathTools.parseDouble(baddebts));
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功记录坏账单据:" + fullId);

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * updateInvoice
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward updateInvoice(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fullId = request.getParameter("outId");

        String invoices = request.getParameter("invoices");

        OutBean out = outDAO.find(fullId);

        User user = (User)request.getSession().getAttribute("user");

        if (out == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getInvoiceMoney() > 0)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        try
        {
            outManager.updateInvoice(user, fullId, invoices);
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功更新发票类型:" + fullId);

        CommonTools.saveParamers(request);

        RequestTools.actionInitQuery(request);

        return querySelfOut(mapping, form, request, reponse);
    }

    /**
     * 坏账取消
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward payOut4(ActionMapping mapping, ActionForm form,
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

        if (out.getStatus() != OutConstant.STATUS_SEC_PASS)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (out.getPay() != OutConstant.PAY_YES)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "还没确认付款");

            return mapping.findForward("error");
        }

        try
        {
            outManager.initPayOut(user, fullId);
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功取消坏账单据:" + fullId);

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

        // 入库单的提交(调拨)
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
                _logger.warn(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理异常：" + e.getErrorContent());

                return mapping.findForward("error");
            }
        }
        // 进入库单 库管--分经理--总裁--董事长
        else if (out.getType() == OutConstant.OUT_TYPE_INBILL
                 && statuss != OutConstant.STATUS_SUBMIT)
        {
            if (out.getOutType() == OutConstant.OUTTYPE_IN_COMMON
                || out.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "采购入库和调拨没有此操作");

                return mapping.findForward("error");
            }

            // 进入待总裁审批
            if (statuss == OutConstant.STATUS_CEO_CHECK)
            {
                try
                {
                    resultStatus = outManager.pass(fullId, user, OutConstant.STATUS_CEO_CHECK,
                        reason, depotpartId);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 进入待董事长审批
            if (statuss == OutConstant.STATUS_CHAIRMA_CHECK)
            {
                try
                {
                    resultStatus = outManager.pass(fullId, user, OutConstant.STATUS_CHAIRMA_CHECK,
                        reason, depotpartId);
                }
                catch (MYException e)
                {
                    _logger.warn(e, e);

                    request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                    return mapping.findForward("error");
                }
            }

            // 进入库管发货(结束了)
            if (statuss == OutConstant.STATUS_PASS)
            {
                try
                {
                    resultStatus = outManager.pass(fullId, user, OutConstant.STATUS_PASS, reason,
                        depotpartId);
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
        else
        {
            // 业务员提交销售单
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

        request.setAttribute(KeyConstant.MESSAGE, "单据[" + fullId + "]操作成功,下一步是:"
                                                  + OutHelper.getStatus(realOut.getStatus()));

        if (realOut.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            if (StringTools.isNullOrNone(request.getParameter("queryType")))
            {
                return querySelfOut(mapping, form, request, reponse);
            }

            return queryOut(mapping, form, request, reponse);
        }
        else
        {
            if (StringTools.isNullOrNone(request.getParameter("queryType")))
            {
                return querySelfBuy(mapping, form, request, reponse);
            }

            return queryBuy(mapping, form, request, reponse);
        }
    }

    /**
     * 领样转销售
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward swatchToSail(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        synchronized (S_LOCK)
        {
            String outId = request.getParameter("outId");

            OutBean bean = outDAO.find(outId);

            if (bean == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "库单不存在,请重新操作");

                return mapping.findForward("error");
            }

            List<BaseBean> list = baseDAO.queryEntityBeansByFK(outId);

            bean.setBaseList(list);

            // 验证ref
            ConditionParse con = new ConditionParse();

            con.addWhereStr();

            con.addCondition("OutBean.refOutFullId", "=", outId);

            con.addCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

            List<OutBean> refList = outDAO.queryEntityBeansByCondition(con);

            for (OutBean outBean : refList)
            {
                if (outManager.isSwatchToSail(outBean.getFullId()) && !OutHelper.isSailEnd(outBean))
                {
                    // 异常不能增加,只能有一个当前的
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "领样转销售只能存在一个未审批结束的,请重新操作");

                    return mapping.findForward("error");
                }
            }

            List<OutBean> refBuyList = queryRefOut(request, outId);

            List<BaseBean> baseList = bean.getBaseList();

            // 计算出已经退货的数量
            for (Iterator iterator = baseList.iterator(); iterator.hasNext();)
            {
                BaseBean baseBean = (BaseBean)iterator.next();

                int hasBack = 0;

                // 退库
                for (OutBean ref : refBuyList)
                {
                    List<BaseBean> refBaseList = ref.getBaseList();

                    for (BaseBean refBase : refBaseList)
                    {
                        if (refBase.equals(baseBean))
                        {
                            hasBack += refBase.getAmount();

                            break;
                        }
                    }
                }

                // 转销售的
                for (OutBean ref : refList)
                {
                    List<BaseBean> refBaseList = baseDAO.queryEntityBeansByFK(ref.getFullId());

                    for (BaseBean refBase : refBaseList)
                    {
                        if (refBase.equals(baseBean))
                        {
                            hasBack += refBase.getAmount();

                            break;
                        }
                    }
                }

                baseBean.setAmount(baseBean.getAmount() - hasBack);

                if (baseBean.getAmount() <= 0)
                {
                    iterator.remove();
                }
            }

            if (baseList.size() == 0)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "领样已经全部退库或者销售,请重新操作");

                return mapping.findForward("error");
            }

            // 生成销售单,然后保存
            OutBean newOut = new OutBean();

            newOut.setOutTime(TimeTools.now_short());
            newOut.setType(OutConstant.OUT_TYPE_OUTBILL);
            newOut.setOutType(OutConstant.OUTTYPE_OUT_COMMON);
            newOut.setRefOutFullId(outId);
            newOut.setDescription("领样转销售,领样单据:" + outId);
            newOut.setDepartment(bean.getDepartment());
            newOut.setLocation(bean.getLocation());
            newOut.setLocationId(bean.getLocationId());
            newOut.setDepotpartId(bean.getDepotpartId());
            newOut.setStafferId(bean.getStafferId());
            newOut.setStafferName(bean.getStafferName());

            newOut.setBaseList(baseList);

            try
            {
                String newFullId = outManager.addSwatchToSail(Helper.getUser(request), newOut);

                CommonTools.removeParamers(request);

                request.setAttribute("fow", "1");

                request.setAttribute("outId", newFullId);

                request.setAttribute("lock_sw", true);

                return this.findOut(mapping, form, request, reponse);
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                return mapping.findForward("error");
            }
        }
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
        String outId = RequestTools.getValueFromRequest(request, "outId");

        String fow = RequestTools.getValueFromRequest(request, "fow");

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

        if (bean.getType() == OutConstant.OUT_TYPE_INBILL)
        {
            request.setAttribute("flag", "1");
        }

        innerForPrepare(request);

        if ("1".equals(fow))
        {
            if (bean.getType() == OutConstant.OUT_TYPE_OUTBILL)
            {
                if (outManager.isSwatchToSail(outId))
                {
                    request.setAttribute("lock_sw", true);
                }
                // 处理修改
                return mapping.findForward("updateOut");
            }
            else
            {
                // 处理修改
                return mapping.findForward("updateBuy");
            }
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

            return mapping.findForward("handerInvokeBuy");
        }

        // 修改发票类型
        if ("6".equals(fow))
        {
            List<InvoiceBean> invoiceList = invoiceDAO.queryEntityBeansByCondition(
                "where forward = ?", InvoiceConstant.INVOICE_FORWARD_OUT);

            request.setAttribute("invoiceList", invoiceList);

            return mapping.findForward("handerInvokeBuy");
        }

        // 处理个人领样退库
        if ("91".equals(fow))
        {
            synchronized (S_LOCK)
            {
                ConditionParse con = new ConditionParse();

                con.addWhereStr();

                con.addCondition("OutBean.refOutFullId", "=", outId);

                con.addCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

                // 领样转销售
                List<OutBean> refList = outDAO.queryEntityBeansByCondition(con);

                for (OutBean outBean : refList)
                {
                    if (outManager.isSwatchToSail(outBean.getFullId())
                        && !OutHelper.isSailEnd(outBean))
                    {
                        // 异常不能增加,只能有一个当前的
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "领样转销售只能存在一个未审批结束的,请重新操作");

                        return mapping.findForward("error");
                    }
                }

                // 领样退库未审批的
                con.clear();

                con.addWhereStr();

                con.addCondition("OutBean.refOutFullId", "=", outId);

                con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

                con.addIntCondition("OutBean.status", "=", OutConstant.STATUS_SAVE);

                con.addIntCondition("OutBean.outType", "=", OutConstant.OUTTYPE_IN_SWATCH);

                int count = outDAO.countByCondition(con.toString());

                if (count > 0)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "此领样已经申请退货请处理结束后再申请");

                    return mapping.findForward("error");
                }

                List<OutBean> refBuyList = queryRefOut(request, outId);

                List<BaseBean> baseList = bean.getBaseList();

                // 计算出已经退货的数量
                for (BaseBean baseBean : baseList)
                {
                    int hasBack = 0;

                    // 退库
                    for (OutBean ref : refBuyList)
                    {
                        List<BaseBean> refBaseList = ref.getBaseList();

                        for (BaseBean refBase : refBaseList)
                        {
                            if (refBase.equals(baseBean))
                            {
                                hasBack += refBase.getAmount();

                                break;
                            }
                        }
                    }

                    // 转销售的
                    for (OutBean ref : refList)
                    {
                        List<BaseBean> refBaseList = baseDAO.queryEntityBeansByFK(ref.getFullId());

                        for (BaseBean refBase : refBaseList)
                        {
                            if (refBase.equals(baseBean))
                            {
                                hasBack += refBase.getAmount();

                                break;
                            }
                        }
                    }

                    baseBean.setInway(hasBack);
                }

                return mapping.findForward("handerOutBack");
            }
        }

        if (bean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            List<InBillVO> billList = inBillDAO.queryEntityVOsByFK(outId);

            request.setAttribute("billList", billList);

            if (bean.getOutType() == OutConstant.OUTTYPE_OUT_CONSIGN)
            {
                // 委托代销的结算单
                List<OutBalanceBean> balanceList = outBalanceDAO.queryEntityBeansByFK(outId);

                for (Iterator iterator = balanceList.iterator(); iterator.hasNext();)
                {
                    OutBalanceBean outBalanceBean = (OutBalanceBean)iterator.next();

                    if (outBalanceBean.getStatus() != OutConstant.OUTBALANCE_STATUS_PASS)
                    {
                        iterator.remove();
                    }
                }

                request.setAttribute("balanceList", balanceList);
            }

            if (bean.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH)
            {
                queryRefOut(request, outId);

                queryRefOut2(request, outId);
            }

            return mapping.findForward("detailOut");
        }

        if (bean.getStatus() == OutConstant.STATUS_PASS)
        {
            // 标识为结束
            bean.setStatus(OutConstant.STATUS_SEC_PASS);
        }

        return mapping.findForward("detailBuy");
    }

    /**
     * 查询REF
     * 
     * @param request
     * @param outId
     * @return
     */
    private List<OutBean> queryRefOut(HttpServletRequest request, String outId)
    {
        // 查询当前已经有多少个人领样
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addCondition(" and OutBean.status in (3, 4)");

        con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        List<OutBean> refBuyList = outDAO.queryEntityBeansByCondition(con);

        for (OutBean outBean : refBuyList)
        {
            List<BaseBean> list = baseDAO.queryEntityBeansByFK(outBean.getFullId());

            outBean.setBaseList(list);
        }

        request.setAttribute("refBuyList", refBuyList);

        return refBuyList;
    }

    private void queryRefOut2(HttpServletRequest request, String outId)
    {
        // 验证ref
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

        List<OutBean> refList = outDAO.queryEntityBeansByCondition(con);

        request.setAttribute("refOutList", refList);
    }

    /**
     * 查询销售单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryOut(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<OutBean> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setInnerCondition2(request, condtion);

            int total = outDAO.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYOUT);

            list = outDAO.queryEntityBeansByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYOUT);

            list = outDAO.queryEntityBeansByCondition(PageSeparateTools.getCondition(request,
                RPTQUERYOUT), PageSeparateTools.getPageSeparate(request, RPTQUERYOUT));
        }

        request.setAttribute("list", list);

        return mapping.findForward("rptQueryOut");
    }

    /**
     * 查询委托代销的清单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryOutBalance(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<OutBalanceVO> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setInnerCondition3(request, condtion);

            int total = outBalanceDAO.countVOByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYOUTBALANCE);

            list = outBalanceDAO.queryEntityVOsByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYOUTBALANCE);

            list = outBalanceDAO
                .queryEntityVOsByCondition(PageSeparateTools.getCondition(request,
                    RPTQUERYOUTBALANCE), PageSeparateTools.getPageSeparate(request,
                    RPTQUERYOUTBALANCE));
        }

        request.setAttribute("list", list);

        return mapping.findForward("rptQueryOutBalance");
    }

    /**
     * @param request
     * @param condtion
     */
    private void setInnerCondition2(HttpServletRequest request, ConditionParse condtion)
    {
        // 条件查询
        String outTime = request.getParameter("outTime");

        String outTime1 = request.getParameter("outTime1");

        String customerName = request.getParameter("customerName");

        String fullId = request.getParameter("fullId");

        if ( !StringTools.isNullOrNone(outTime))
        {
            condtion.addCondition("OutBean.outTime", ">=", outTime);
        }
        else
        {
            condtion.addCondition("OutBean.outTime", ">=", TimeTools.now_short( -180));

            request.setAttribute("outTime", TimeTools.now_short( -180));
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

        if ( !StringTools.isNullOrNone(fullId))
        {
            condtion.addCondition("OutBean.fullId", "like", fullId);
        }

        if ( !StringTools.isNullOrNone(customerName))
        {
            condtion.addCondition("OutBean.customerName", "like", customerName);
        }

        // (url)固定查询
        String stafferId = request.getParameter("stafferId");

        String mode = request.getParameter("mode");

        String invoiceId = request.getParameter("invoiceId");

        String dutyId = request.getParameter("dutyId");

        String customerId = request.getParameter("customerId");

        String invoiceStatus = request.getParameter("invoiceStatus");

        if ( !StringTools.isNullOrNone(invoiceId))
        {
            condtion.addCondition("OutBean.invoiceId", "=", invoiceId);
        }

        if ( !StringTools.isNullOrNone(dutyId))
        {
            condtion.addCondition("OutBean.dutyId", "=", dutyId);
        }

        if ( !StringTools.isNullOrNone(customerId))
        {
            condtion.addCondition("OutBean.customerId", "=", customerId);
        }

        if ( !StringTools.isNullOrNone(stafferId))
        {
            condtion.addCondition("OutBean.stafferId", "=", stafferId);
        }

        if ( !StringTools.isNullOrNone(invoiceStatus))
        {
            condtion.addIntCondition("OutBean.invoiceStatus", "=", invoiceStatus);
        }

        // 查询需要勾款的销售单
        if ("0".equals(mode))
        {
            condtion.addCondition("and OutBean.status in (1, 3, 6, 7, 8, 9)");

            // 过滤委托代销
            condtion.addIntCondition("OutBean.outType", "<>", OutConstant.OUTTYPE_OUT_CONSIGN);
        }

        // 需要开票的销售单
        if ("1".equals(mode))
        {
            condtion.addCondition("and OutBean.status in (3, 4)");

            // 过滤委托代销
            condtion.addIntCondition("OutBean.outType", "<>", OutConstant.OUTTYPE_OUT_CONSIGN);
        }

        condtion.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

        condtion.addCondition("order by OutBean.fullId desc");
    }

    /**
     * 委托代销清单过滤
     * 
     * @param request
     * @param condtion
     */
    private void setInnerCondition3(HttpServletRequest request, ConditionParse condtion)
    {
        // 条件查询
        String alogTime = request.getParameter("alogTime");

        String blogTime = request.getParameter("blogTime");

        String fullId = request.getParameter("outId");

        if ( !StringTools.isNullOrNone(alogTime))
        {
            condtion.addCondition("OutBalanceBean.logTime", ">=", alogTime);
        }
        else
        {
            condtion.addCondition("OutBalanceBean.logTime", ">=", TimeTools.now( -180));

            request.setAttribute("alogTime", TimeTools.now( -180));
        }

        if ( !StringTools.isNullOrNone(blogTime))
        {
            condtion.addCondition("OutBalanceBean.logTime", "<=", blogTime);
        }
        else
        {
            condtion.addCondition("OutBalanceBean.logTime", "<=", TimeTools.now());

            request.setAttribute("blogTime", TimeTools.now());
        }

        if ( !StringTools.isNullOrNone(fullId))
        {
            condtion.addCondition("OutBalanceBean.outId", "like", fullId);
        }

        // (url)固定查询
        String stafferId = request.getParameter("stafferId");

        String invoiceId = request.getParameter("invoiceId");

        String dutyId = request.getParameter("dutyId");

        String type = request.getParameter("type");

        String pay = request.getParameter("pay");

        String customerId = request.getParameter("customerId");

        String invoiceStatus = request.getParameter("invoiceStatus");

        if ( !StringTools.isNullOrNone(invoiceId))
        {
            condtion.addCondition("OutBean.invoiceId", "=", invoiceId);
        }

        if ( !StringTools.isNullOrNone(dutyId))
        {
            condtion.addCondition("OutBean.dutyId", "=", dutyId);
        }

        if ( !StringTools.isNullOrNone(stafferId))
        {
            condtion.addCondition("OutBean.stafferId", "=", stafferId);
        }

        if ( !StringTools.isNullOrNone(invoiceStatus))
        {
            condtion.addIntCondition("OutBalanceBean.invoiceStatus", "=", invoiceStatus);
        }

        if ( !StringTools.isNullOrNone(type))
        {
            condtion.addIntCondition("OutBalanceBean.type", "=", type);
        }

        if ( !StringTools.isNullOrNone(pay))
        {
            condtion.addIntCondition("OutBalanceBean.pay", "=", pay);
        }

        if ( !StringTools.isNullOrNone(customerId))
        {
            condtion.addCondition("OutBalanceBean.customerId", "=", customerId);
        }

        condtion.addIntCondition("OutBalanceBean.status", "=", OutConstant.OUTBALANCE_STATUS_PASS);

        condtion.addCondition("order by OutBalanceBean.logTime desc");
    }

    /**
     * showLastCredit(剩余的信用)
     * 
     * @param request
     * @param user
     */
    private void showLastCredit(HttpServletRequest request, User user, String flag)
    {
        double noPayBusiness = outDAO.sumAllNoPayAndAvouchBusinessByStafferId(user.getStafferId(),
            YYTools.getStatBeginDate(), YYTools.getStatEndDate());

        StafferBean sb2 = stafferDAO.find(user.getStafferId());

        if (sb2 != null)
        {
            // 设置其剩余的信用额度
            request.setAttribute("credit", ElTools.formatNum(sb2.getCredit() * sb2.getLever()
                                                             - noPayBusiness));
        }

        if ( !"1".equals(flag))
        {
            // 分公司经理的额度
            List<StafferBean> managerList = stafferManager.queryStafferByAuthIdAndIndustryId(
                AuthConstant.SAIL_LOCATION_MANAGER, sb2.getIndustryId());

            List<String> mList = new ArrayList<String>();

            for (StafferBean stafferBean : managerList)
            {
                double noPayBusinessInM = outDAO.sumAllNoPayAndAvouchBusinessByStafferId(
                    stafferBean.getId(), YYTools.getStatBeginDate(), YYTools.getStatEndDate());

                StafferBean manager = stafferDAO.find(user.getStafferId());

                mList.add(stafferBean.getName()
                          + "的信用额度剩余:"
                          + ElTools.formatNum(manager.getCredit() * manager.getLever()
                                              - noPayBusinessInM));

                request.setAttribute("mList", mList);
            }
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
}
