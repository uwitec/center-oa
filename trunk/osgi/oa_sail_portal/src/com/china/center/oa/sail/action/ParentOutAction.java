/**
 * File Name: ParentOutAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-3-21<br>
 * Grant: open source to everybody
 */
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
import com.china.center.actionhelper.jsonimpl.JSONArray;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.customer.constant.CustomerConstant;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.customer.wrap.NotPayWrap;
import com.china.center.oa.customervssail.dao.OutQueryDAO;
import com.china.center.oa.finance.dao.InBillDAO;
import com.china.center.oa.finance.dao.InsVSOutDAO;
import com.china.center.oa.finance.dao.OutBillDAO;
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
import com.china.center.oa.product.helper.StorageRelationHelper;
import com.china.center.oa.product.manager.StorageRelationManager;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.AuthBean;
import com.china.center.oa.publics.bean.DepartmentBean;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.bean.InvoiceCreditBean;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.bean.ShowBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.InvoiceConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.constant.StafferConstant;
import com.china.center.oa.publics.constant.SysConfigConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.DepartmentDAO;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.DutyVSInvoiceDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.dao.InvoiceCreditDAO;
import com.china.center.oa.publics.dao.InvoiceDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.dao.PrincipalshipDAO;
import com.china.center.oa.publics.dao.ShowDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.helper.OATools;
import com.china.center.oa.publics.manager.AuthManager;
import com.china.center.oa.publics.manager.FatalNotify;
import com.china.center.oa.publics.manager.OrgManager;
import com.china.center.oa.publics.manager.StafferManager;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.oa.publics.vo.DutyVO;
import com.china.center.oa.publics.vs.DutyVSInvoiceBean;
import com.china.center.oa.publics.vs.RoleAuthBean;
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
import com.china.center.oa.sail.dao.SailConfigDAO;
import com.china.center.oa.sail.helper.OutHelper;
import com.china.center.oa.sail.helper.YYTools;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.sail.manager.SailManager;
import com.china.center.oa.sail.vo.OutBalanceVO;
import com.china.center.oa.sail.vo.OutVO;
import com.china.center.oa.sail.vo.SailConfigVO;
import com.china.center.oa.tax.dao.FinanceDAO;
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
 * ParentOutAction
 * 
 * @author ZHUZHU
 * @version 2011-3-21
 * @see ParentOutAction
 * @since 3.0
 */
public class ParentOutAction extends DispatchAction
{
    protected final Log _logger = LogFactory.getLog(getClass());

    protected final Log importLog = LogFactory.getLog("sec");

    protected UserDAO userDAO = null;

    protected SailManager sailManager = null;

    protected OutQueryDAO outQueryDAO = null;

    protected FatalNotify fatalNotify = null;

    protected OutManager outManager = null;

    protected ProductDAO productDAO = null;

    protected InvoiceCreditDAO invoiceCreditDAO = null;

    protected CustomerDAO customerDAO = null;

    protected ProviderDAO providerDAO = null;

    protected PrincipalshipDAO principalshipDAO = null;

    protected StafferDAO stafferDAO = null;

    protected ParameterDAO parameterDAO = null;

    protected LocationDAO locationDAO = null;

    protected CommonDAO commonDAO = null;

    protected DepartmentDAO departmentDAO = null;

    protected StorageDAO storageDAO = null;

    /**
     * 发票
     */
    protected InsVSOutDAO insVSOutDAO = null;

    protected DepotDAO depotDAO = null;

    protected InBillDAO inBillDAO = null;

    protected OutBillDAO outBillDAO = null;

    protected ShowDAO showDAO = null;

    protected OrgManager orgManager = null;

    protected UserManager userManager = null;

    protected StafferManager stafferManager = null;

    protected FlowLogDAO flowLogDAO = null;

    protected OutDAO outDAO = null;

    protected FinanceDAO financeDAO = null;

    protected DutyDAO dutyDAO = null;

    protected AuthManager authManager = null;

    protected InvoiceDAO invoiceDAO = null;

    protected BaseDAO baseDAO = null;

    protected ConsignDAO consignDAO = null;

    protected DepotpartDAO depotpartDAO = null;

    protected StorageRelationManager storageRelationManager = null;

    protected BaseBalanceDAO baseBalanceDAO = null;

    protected OutBalanceDAO outBalanceDAO = null;

    protected DutyVSInvoiceDAO dutyVSInvoiceDAO = null;

    protected SailConfigDAO sailConfigDAO = null;

    protected static String QUERYSELFOUT = "querySelfOut";

    protected static String QUERYSELFOUTBALANCE = "querySelfOutBalance";

    protected static String QUERYOUT = "queryOut";

    protected static String QUERYSELFBUY = "querySelfBuy";

    protected static String QUERYBUY = "queryBuy";

    protected static String RPTQUERYOUT = "rptQueryOut";

    protected static String RPTQUERYOUTBALANCE = "rptQueryOutBalance";

    /**
     * 入库单操作锁
     */
    protected static Object S_LOCK = new Object();

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

        // 入库单
        if ("1".equals(flag))
        {
            try
            {
                innerForPrepare(request, true, true);
            }
            catch (MYException e)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

                return mapping.findForward("error");
            }

            List<DutyBean> dutyList = dutyDAO.listEntityBeans();

            for (Iterator iterator = dutyList.iterator(); iterator.hasNext();)
            {
                DutyBean dutyBean = (DutyBean)iterator.next();

                if ( ! (dutyBean.getId().equals(PublicConstant.DEFAULR_DUTY_ID) || dutyBean
                    .getId()
                    .equals(PublicConstant.MANAGER_DUTY_ID)))
                {
                    iterator.remove();
                }
            }

            request.setAttribute("dutyList", dutyList);

            return mapping.findForward("addBuy");
        }

        try
        {
            innerForPrepare(request, true, true);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            return mapping.findForward("error");
        }

        if (OATools.isChangeToV5())
        {
            // 销售单
            return mapping.findForward("addOut4");
        }
        else
        {
            return mapping.findForward("addOut4_bak");
        }
    }

    /**
     * navigationAddOut2
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward navigationAddOut2(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        // 是否锁定库存
        if (storageRelationManager.isStorageRelationLock())
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "库存被锁定,不能开单");

            return mapping.findForward("error");
        }

        try
        {
            innerForPrepare(request, true, true);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            return mapping.findForward("error");
        }

        Map ssmap = (Map)request.getSession().getAttribute("ssmap");

        List<String> showIdList = (List<String>)request.getSession().getAttribute("showIds");

        // 查询开单品名(是过滤出来的)
        List<ShowBean> showList = new ArrayList();

        for (String each : showIdList)
        {
            ShowBean show = showDAO.find(each);

            show.setDutyId(ssmap.get("duty").toString());

            showList.add(show);
        }

        DutyBean sailDuty = dutyDAO.find(ssmap.get("duty").toString());

        request.setAttribute("sailDuty", sailDuty);

        String ratio = request.getParameter("sailId");

        ssmap.put("ratio", ratio);

        request.setAttribute("invoiceDes", "销货发票,税点:" + ratio + "‰(千分值)");

        // 替换之前的
        JSONArray shows = new JSONArray(showList, true);

        // 替换过滤的show
        request.setAttribute("showJSON", shows.toString());

        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList2", dutyList);

        // 销售单
        return mapping.findForward("addOut2");
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
        createDepotList(request);

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
        CommonTools.saveParamers(request);

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

        List<FlowLogBean> logs = flowLogDAO.queryEntityBeansByFK(id);

        request.setAttribute("logList", logs);

        request.setAttribute("baseList", baseList);

        request.setAttribute("bean", bean);

        // 销售单
        return mapping.findForward("detailOutBalance");
    }

    /**
     * innerForPrepare(准备库单的维护)
     * 
     * @param request
     * @param check
     *            是否检查事业部
     * @param detailQuery
     *            TODO
     * @throws MYException
     */
    protected void innerForPrepare(HttpServletRequest request, boolean check, boolean needDeepQuery)
        throws MYException
    {
        String flag = RequestTools.getValueFromRequest(request, "flag");

        // 得到部门
        List<DepartmentBean> list2 = departmentDAO.listEntityBeans();

        User user = Helper.getUser(request);

        // 仓库是自己选择的
        request.setAttribute("departementList", list2);

        request.setAttribute("current", TimeTools.now("yyyy-MM-dd"));

        List<DepotBean> locationList = depotDAO.listEntityBeans();

        // 销售单对仓库的过滤
        if ("0".equals(flag))
        {
            StafferBean staffer = Helper.getStaffer(request);

            for (Iterator iterator = locationList.iterator(); iterator.hasNext();)
            {
                DepotBean depotBean = (DepotBean)iterator.next();

                if (depotBean.getId().equals(DepotConstant.MAKE_DEPOT_ID))
                {
                    iterator.remove();

                    continue;
                }

                if (depotBean.getId().equals(DepotConstant.STOCK_DEPOT_ID))
                {
                    iterator.remove();

                    continue;
                }

                // 人为规定
                if (depotBean.getName().indexOf("不可发") != -1)
                {
                    iterator.remove();

                    continue;
                }

                if (OATools.isChangeToV5())
                {
                    // 兼容没有配置事业部的
                    if (StringTools.isNullOrNone(depotBean.getIndustryId()))
                    {
                        continue;
                    }

                    // 如果事业部不匹配删除
                    if ( !depotBean.getIndustryId().equals(staffer.getIndustryId()))
                    {
                        iterator.remove();
                        continue;
                    }
                }
            }
        }

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

        if (needDeepQuery)
        {
            showLastCredit(request, user, flag);
        }

        List<InvoiceBean> invoiceList = invoiceDAO.queryEntityBeansByCondition("where forward = ?",
            InvoiceConstant.INVOICE_FORWARD_OUT);

        request.setAttribute("invoiceList", invoiceList);

        List<InvoiceBean> inInvoiceList = invoiceDAO.queryEntityBeansByCondition(
            "where forward = ?", InvoiceConstant.INVOICE_FORWARD_IN);

        request.setAttribute("inInvoiceList", inInvoiceList);

        List<DutyVSInvoiceBean> vsList = dutyVSInvoiceDAO.listEntityBeans();

        // 过滤
        fiterVS(invoiceList, vsList);

        JSONArray vsJSON = new JSONArray(vsList, true);

        request.setAttribute("vsJSON", vsJSON.toString());

        JSONArray invoices = new JSONArray(invoiceList, true);

        request.setAttribute("invoicesJSON", invoices.toString());

        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList", dutyList);

        // 查询开单品名
        List<ShowBean> showList = showDAO.listEntityBeans();

        JSONArray shows = new JSONArray(showList, true);

        request.setAttribute("showJSON", shows.toString());

        StafferBean staffer = stafferDAO.find(user.getStafferId());

        if (check)
        {
            if (StringTools.isNullOrNone(staffer.getIndustryId()))
            {
                throw new MYException("您没有事业部属性,不能开单");
            }
        }

        request.setAttribute("staffer", staffer);
    }

    /**
     * showLastCredit(剩余的信用)
     * 
     * @param request
     * @param user
     */
    protected void showLastCredit(HttpServletRequest request, User user, String flag)
    {
        StafferBean sb2 = stafferDAO.find(user.getStafferId());

        double noPayBusiness = outDAO.sumAllNoPayAndAvouchBusinessByStafferId(user.getStafferId(),
            sb2.getIndustryId(), YYTools.getStatBeginDate(), YYTools.getStatEndDate());

        if (sb2 != null && sb2.getBlack() == StafferConstant.BLACK_NO)
        {
            // 设置其剩余的信用额度
            request.setAttribute("credit", ElTools.formatNum(sb2.getCredit() * sb2.getLever()
                                                             - noPayBusiness));
        }
        else
        {
            request.setAttribute("credit", "您是黑名单");
        }

        if ( !"1".equals(flag))
        {
            // 事业部经理的额度
            List<StafferBean> managerList = stafferManager.queryStafferByAuthIdAndIndustryId(
                AuthConstant.SAIL_LOCATION_MANAGER, sb2.getIndustryId());

            PrincipalshipBean pri = principalshipDAO.find(sb2.getIndustryId());

            List<String> mList = new ArrayList<String>();

            for (StafferBean stafferBean : managerList)
            {
                // 查询经理担保的金额
                double noPayBusinessInM = outDAO.sumNoPayAndAvouchBusinessByManagerId(stafferBean
                    .getId(), sb2.getIndustryId(), YYTools.getStatBeginDate(), YYTools
                    .getStatEndDate());

                mList.add(stafferBean.getName()
                          + "的信用额度("
                          + pri.getName()
                          + ")剩余:"
                          + ElTools.formatNum(getIndustryIdCredit(sb2.getIndustryId(), stafferBean
                              .getId())
                                              * stafferBean.getLever() - noPayBusinessInM));

            }

            request.setAttribute("mList", mList);
        }
    }

    protected void fiterVS(List<InvoiceBean> invoiceList, List<DutyVSInvoiceBean> vsList)
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

    protected boolean hasOver(String stafferName)
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

    protected boolean containAuth(User user, String authId)
    {
        if (StringTools.isNullOrNone(authId))
        {
            return true;
        }

        if (authId.equals(AuthConstant.PUNLIC_AUTH))
        {
            return true;
        }

        List<RoleAuthBean> authList = user.getAuth();

        for (RoleAuthBean roleAuthBean : authList)
        {
            if (roleAuthBean.getAuthId().equals(authId))
            {
                return true;
            }
        }

        return false;
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

        User user = (User)request.getSession().getAttribute("user");

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

        OutVO outVO = outList.get(0);

        if (outVO.getType() == 0)
        {
            if ( !containAuth(user, AuthConstant.SAIL_QUERY_EXPORT))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

                return mapping.findForward("error");
            }

        }
        else
        {
            if ( !containAuth(user, AuthConstant.BUY_EXPORT))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

                return mapping.findForward("error");
            }
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
            ws.addCell(new Label(j++ , i, "事业部", format));
            ws.addCell(new Label(j++ , i, "联系人", format));
            ws.addCell(new Label(j++ , i, "联系电话", format));
            ws.addCell(new Label(j++ , i, "单据号码", format));
            ws.addCell(new Label(j++ , i, "类型", format));
            ws.addCell(new Label(j++ , i, "回款日期", format));
            ws.addCell(new Label(j++ , i, "库管通过日期", format));
            ws.addCell(new Label(j++ , i, "状态", format));
            ws.addCell(new Label(j++ , i, "经办人", format));
            ws.addCell(new Label(j++ , i, "仓库", format));
            ws.addCell(new Label(j++ , i, "目的库", format));
            ws.addCell(new Label(j++ , i, "关联单据", format));
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

                    consignBean = consignDAO.findDefaultConsignByFullId(element.getFullId());

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

                    ws.addCell(new Label(j++ , i, element.getIndustryName()));

                    ws.addCell(new Label(j++ , i, element.getConnector()));

                    ws.addCell(new Label(j++ , i, element.getPhone()));

                    ws.addCell(new Label(j++ , i, element.getFullId()));

                    ws.addCell(new Label(j++ , i, OutHelper.getOutType(element)));

                    ws.addCell(new Label(j++ , i, element.getRedate()));

                    String changeTime = element.getChangeTime();

                    if (changeTime.length() > 10)
                    {
                        changeTime = changeTime.substring(0, 10);
                    }

                    ws.addCell(new Label(j++ , i, changeTime));

                    ws.addCell(new Label(j++ , i, OutHelper.getStatus(element.getStatus(), false)));

                    ws.addCell(new Label(j++ , i, element.getStafferName()));

                    ws.addCell(new Label(j++ , i, element.getDepotName()));

                    ws.addCell(new Label(j++ , i, element.getDestinationName()));

                    ws.addCell(new Label(j++ , i, element.getRefOutFullId()));

                    ws.addCell(new Label(j++ , i, element.getDescription()));

                    // 下面是base里面的数据
                    base = (BaseBean)iterator.next();

                    ws.addCell(new Label(j++ , i, base.getProductName()));
                    ws.addCell(new Label(j++ , i, base.getUnit()));
                    ws.addCell(new Label(j++ , i, String.valueOf(base.getAmount())));
                    ws.addCell(new Label(j++ , i, String.valueOf(base.getPrice())));
                    ws.addCell(new Label(j++ , i, String.valueOf(base.getValue())));
                    ws.addCell(new Label(j++ , i, MathTools.formatNum(base.getCostPrice())));

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
     * exportOutBalance
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward exportOutBalance(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        String exportKey = QUERYSELFOUTBALANCE;

        List<OutBalanceVO> outList = null;

        String filenName = null;

        if (OldPageSeparateTools.getPageSeparate(request, exportKey).getRowCount() > 1500)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "导出的记录数不能超过1500");

            return mapping.findForward("error");
        }

        outList = outBalanceDAO.queryEntityVOsByCondition(OldPageSeparateTools.getCondition(
            request, exportKey));

        filenName = "Balance_Export_" + TimeTools.now("MMddHHmmss") + ".xls";

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

            OutBalanceVO element = null;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            WritableCellFormat format = new WritableCellFormat(font);

            element = (OutBalanceVO)outList.get(0);

            ws.addCell(new Label(j++ , i, "日期", format));
            ws.addCell(new Label(j++ , i, "客户", format));
            ws.addCell(new Label(j++ , i, "标识", format));
            ws.addCell(new Label(j++ , i, "销售单号", format));
            ws.addCell(new Label(j++ , i, "总金额", format));
            ws.addCell(new Label(j++ , i, "已付金额", format));
            ws.addCell(new Label(j++ , i, "类型", format));
            ws.addCell(new Label(j++ , i, "职员", format));
            ws.addCell(new Label(j++ , i, "状态", format));
            ws.addCell(new Label(j++ , i, "仓库", format));
            ws.addCell(new Label(j++ , i, "描述", format));

            ws.addCell(new Label(j++ , i, "品名", format));
            ws.addCell(new Label(j++ , i, "结算数量", format));
            ws.addCell(new Label(j++ , i, "销售价", format));
            ws.addCell(new Label(j++ , i, "成本", format));
            ws.addCell(new Label(j++ , i, "合计", format));

            // 写outbean
            for (Iterator iter = outList.iterator(); iter.hasNext();)
            {
                element = (OutBalanceVO)iter.next();

                // 写baseBean
                List<BaseBalanceBean> baseList = null;

                BaseBalanceBean base = null;

                try
                {
                    baseList = baseBalanceDAO.queryEntityBeansByFK(element.getId());
                }
                catch (Exception e)
                {
                    _logger.error(e, e);
                }

                for (Iterator iterator = baseList.iterator(); iterator.hasNext();)
                {
                    j = 0;
                    i++ ;

                    ws.addCell(new Label(j++ , i, element.getLogTime()));

                    ws.addCell(new Label(j++ , i, element.getCustomerName()));

                    ws.addCell(new Label(j++ , i, element.getId()));

                    ws.addCell(new Label(j++ , i, element.getOutId()));

                    ws.addCell(new Label(j++ , i, MathTools.formatNum(element.getTotal())));

                    ws.addCell(new Label(j++ , i, MathTools.formatNum(element.getPayMoney())));

                    ws
                        .addCell(new Label(j++ , i, ElTools
                            .get("outBalanceType", element.getType())));

                    ws.addCell(new Label(j++ , i, element.getStafferName()));

                    ws.addCell(new Label(j++ , i, ElTools.get("outBalanceStatus", element
                        .getStatus())));

                    ws.addCell(new Label(j++ , i, element.getDirDepotName()));

                    ws.addCell(new Label(j++ , i, element.getDescription()));

                    // 下面是base里面的数据
                    base = (BaseBalanceBean)iterator.next();

                    BaseBean baseBean = baseDAO.find(base.getBaseId());

                    ws.addCell(new Label(j++ , i, baseBean.getProductName()));
                    ws.addCell(new Label(j++ , i, String.valueOf(base.getAmount())));
                    ws.addCell(new Label(j++ , i, MathTools.formatNum(baseBean.getPrice())));
                    ws.addCell(new Label(j++ , i, MathTools.formatNum(baseBean.getCostPrice())));
                    ws.addCell(new Label(j++ , i, MathTools.formatNum(base.getAmount()
                                                                      * baseBean.getPrice())));
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
     * 个人领样退库--申请
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

        out.setDutyId(oldOut.getDutyId());

        out.setInvoiceId(oldOut.getInvoiceId());

        out.setDescription("个人领样退库,领样单号:" + outId + ". " + adescription);

        List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outId);

        // 校验库存
        List<OutBean> makeLingYang = makeLingYang(outId, request, baseList);

        if ( !ListTools.isEmptyOrNull(makeLingYang) && false)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "此单存在转销售单据,所以只能全部转销售,不能退库处理");

            return mapping.findForward("error");
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

                        baseBean.setLocationId(out.getLocation());
                        baseBean.setAmount(back);
                        baseBean.setProductName(each.getProductName());
                        baseBean.setUnit("套");
                        baseBean.setShowId(each.getShowId());
                        baseBean.setProductId(each.getProductId());

                        // 领样退库的金额是销售的金额,否则无法回款
                        baseBean.setPrice(each.getPrice());
                        baseBean.setCostPrice(each.getCostPrice());

                        baseBean.setCostPriceKey(StorageRelationHelper.getPriceKey(each
                            .getCostPrice()));
                        baseBean.setOwner(each.getOwner());
                        baseBean.setOwnerName(each.getOwnerName());

                        baseBean.setDepotpartId(each.getDepotpartId());

                        baseBean.setDepotpartName(each.getDepotpartName());

                        baseBean.setDescription(String.valueOf(each.getCostPrice()));

                        baseBean.setValue(each.getPrice() * back);

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
     * 个人领样退库的预处理
     * 
     * @param outId
     * @param request
     * @param bean
     */
    private List<OutBean> makeLingYang(String outId, HttpServletRequest request,
                                       List<BaseBean> baseList)
    {
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

        // 领样转销售
        List<OutBean> refList = outDAO.queryEntityBeansByCondition(con);

        // 领样退库未审批的
        con.clear();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        con.addIntCondition("OutBean.status", "=", OutConstant.STATUS_SAVE);

        con.addIntCondition("OutBean.outType", "=", OutConstant.OUTTYPE_IN_SWATCH);

        List<OutBean> refBuyList = queryRefOut(request, outId);

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

        return refList;
    }

    /**
     * 销售退单
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward outBack2(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = Helper.getUser(request);

        String outId = request.getParameter("outId");

        // 目的仓库
        String dirDeport = request.getParameter("dirDeport");

        String adescription = request.getParameter("adescription");

        ActionForward error = checkAddOutBack(mapping, request, outId);

        if (error != null)
        {
            return error;
        }

        String[] baseItems = request.getParameterValues("baseItem");

        String[] backUnms = request.getParameterValues("backUnm");

        OutBean oldOut = outDAO.find(outId);

        if (oldOut == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误");

            return mapping.findForward("error");
        }

        if (StringTools.isNullOrNone(dirDeport))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请选择目的仓库");

            return mapping.findForward("error");
        }

        if (oldOut.getType() != OutConstant.OUT_TYPE_OUTBILL
            && ! (oldOut.getOutType() == OutConstant.OUTTYPE_OUT_COMMON
                  || oldOut.getOutType() == OutConstant.OUTTYPE_OUT_RETAIL || oldOut.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT))
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

        out.setDepartment(oldOut.getDepartment());

        out.setCustomerId(oldOut.getCustomerId());

        out.setCustomerName(oldOut.getCustomerName());

        out.setDutyId(oldOut.getDutyId());

        out.setInvoiceId(oldOut.getInvoiceId());

        // 所在区域
        out.setLocationId(user.getLocationId());

        // 目的仓库
        out.setLocation(dirDeport);

        out.setInway(OutConstant.IN_WAY_NO);

        out.setOutType(OutConstant.OUTTYPE_IN_OUTBACK);

        out.setRefOutFullId(outId);

        out.setDescription("销售退库,销售单号:" + outId + ". " + adescription);

        DepotpartBean okDepotpart = depotpartDAO.findDefaultOKDepotpart(dirDeport);

        if (okDepotpart == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "仓库下没有良品仓");

            return mapping.findForward("error");
        }

        List<BaseBean> baseList = OutHelper.trimBaseList2(baseDAO.queryEntityBeansByFK(outId));

        List<OutBean> refBuyList = queryRefOut(request, outId);

        // 计算出已经退货的数量(这里是根据产品的总量进行统计的哦)
        for (BaseBean baseBean : baseList)
        {
            int hasBack = 0;

            for (OutBean ref : refBuyList)
            {
                List<BaseBean> refBaseList = OutHelper.trimBaseList2(ref.getBaseList());

                for (BaseBean refBase : refBaseList)
                {
                    if (refBase.equals2(baseBean))
                    {
                        hasBack += refBase.getAmount();

                        break;
                    }
                }
            }

            baseBean.setInway(hasBack);

            baseBean.setId(OutHelper.getKey2(baseBean));
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

                        // 卖出价 * 数量
                        baseBean.setLocationId(out.getLocation());
                        baseBean.setAmount(back);
                        baseBean.setProductName(each.getProductName());
                        baseBean.setUnit("套");
                        baseBean.setShowId(each.getShowId());
                        baseBean.setProductId(each.getProductId());

                        baseBean.setPrice(each.getPrice());
                        baseBean.setCostPrice(each.getCostPrice());
                        baseBean.setCostPriceKey(StorageRelationHelper.getPriceKey(each
                            .getCostPrice()));

                        baseBean.setOwner(each.getOwner());
                        baseBean.setOwnerName(each.getOwnerName());

                        if (oldOut.getLocation().equals(out.getLocation()))
                        {
                            baseBean.setDepotpartId(each.getDepotpartId());
                        }
                        else
                        {
                            baseBean.setDepotpartId(okDepotpart.getId());
                        }

                        if (oldOut.getLocation().equals(out.getLocation()))
                        {
                            baseBean.setDepotpartName(each.getDepotpartName());
                        }
                        else
                        {
                            baseBean.setDepotpartName(okDepotpart.getName());
                        }

                        baseBean.setValue(each.getPrice() * back);

                        if (StringTools.isNullOrNone(each.getDepotpartId()))
                        {
                            request
                                .setAttribute(KeyConstant.ERROR_MESSAGE, "可能是四月之前的单据,没有仓区所以不能退库");

                            return mapping.findForward("error");
                        }

                        // 成本
                        baseBean.setDescription(String.valueOf(each.getCostPrice()));

                        newBaseList.add(baseBean);

                        total += baseBean.getValue();
                    }
                }
            }
        }

        // 此次总金额
        out.setTotal(total);

        out.setBaseList(newBaseList);

        try
        {
            if (newBaseList.size() > 0)
            {
                // CORE 退库
                String fullId = outManager.coloneOutWithAffair(out, user,
                    StorageConstant.OPR_STORAGE_OUTBACK);

                request.setAttribute(KeyConstant.MESSAGE, "成功申请退库:" + fullId);
            }
            else
            {

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "退库数量为0");

                return mapping.findForward("error");

            }
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e);
        }

        CommonTools.removeParamers(request);

        request.setAttribute("forward", "1");

        request.setAttribute("queryType", "8");

        return queryOut(mapping, form, request, reponse);
    }

    /**
     * 检查是否可以增加销售退单
     * 
     * @param mapping
     * @param request
     * @param outId
     * @return
     */
    protected ActionForward checkAddOutBack(ActionMapping mapping, HttpServletRequest request,
                                            String outId)
    {
        // 查询是否被关联
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        con.addCondition("and OutBean.status in (0, 1)");

        con.addIntCondition("OutBean.outType", "=", OutConstant.OUTTYPE_IN_OUTBACK);

        int count = outDAO.countByCondition(con.toString());

        if (count > 0)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "此销售单存在申请退货,请处理结束后再申请");

            return mapping.findForward("error");
        }

        return null;
    }

    /**
     * 收集数据
     * 
     * @param pbean
     * @param item
     * @param request
     * @throws MYException
     */
    protected void setOutBalanceBean(OutBalanceBean bean, HttpServletRequest request)
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

        // 销售单
        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            // 强制设置成OUT_SAIL_TYPE_MONEY
            if (CustomerConstant.BLACK_LEVEL.equals(customercreditlevel))
            {
                outBean.setReserve3(OutConstant.OUT_SAIL_TYPE_MONEY);
            }

            // 业务员黑名单
            StafferBean sb = stafferDAO.find(user.getStafferId());

            if (sb != null && sb.getBlack() == StafferConstant.BLACK_YES)
            {
                outBean.setReserve3(OutConstant.OUT_SAIL_TYPE_MONEY);
            }

            // 销售属性的设置
            if (OATools.getManagerFlag())
            {
                InvoiceBean invoiceBean = invoiceDAO.find(outBean.getInvoiceId());

                int ratio = (int) (invoiceBean.getVal() * 10);

                outBean.setRatio(String.valueOf(ratio));
            }

            action = processCommonOut(mapping, request, user, saves, fullId, outBean, map);
        }
        else
        {
            // 默认很多属性
            outBean.setStafferId(user.getStafferId());
            outBean.setStafferName(user.getStafferName());

            if (StringTools.isNullOrNone(outBean.getCustomerId()))
            {
                outBean.setCustomerId(CustomerConstant.PUBLIC_CUSTOMER_ID);
                outBean.setCustomerName(CustomerConstant.PUBLIC_CUSTOMER_NAME);
            }

            outBean.setDepartment("公共部门");
            outBean.setArriveDate(TimeTools.now_short(10));

            // 入库单的处理
            try
            {
                outManager.addOut(outBean, map.getParameterMap(), user);

                if ("提交".equals(saves))
                {
                    int ttype = StorageConstant.OPR_STORAGE_INOTHER;

                    if (outBean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT)
                    {
                        ttype = StorageConstant.OPR_STORAGE_REDEPLOY;
                    }

                    outManager.submit(outBean.getFullId(), user, ttype);
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
    protected ActionForward processCommonOut(ActionMapping mapping, HttpServletRequest request,
                                             User user, String saves, String fullId,
                                             OutBean outBean, ParamterMap map)
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
    protected void checkSubmit(User user, String id)
    {
        OutBean newOut = outDAO.findRealOut(id);

        if (newOut == null)
        {
            loggerError(id + " is errro in checkSubmit");

            return;
        }

        if (newOut.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
        {
            importLog.info(id + ":" + user.getStafferName() + "(after):" + newOut.getStatus()
                           + "(预计:" + OutConstant.STATUS_CEO_CHECK + ")");

            if (newOut.getStatus() != OutConstant.STATUS_CEO_CHECK)
            {
                loggerError(id + ":" + user.getStafferName() + "(after):" + newOut.getStatus()
                            + "(预计:" + OutConstant.STATUS_CEO_CHECK + ")");
            }
        }
        else
        {
            importLog.info(id + ":" + user.getStafferName() + "(after):" + newOut.getStatus()
                           + "(预计:" + OutConstant.STATUS_SUBMIT + ")");

            if (newOut.getReserve3() != OutConstant.OUT_SAIL_TYPE_LOCATION_MANAGER
                && newOut.getStatus() != OutConstant.STATUS_SUBMIT)
            {
                loggerError(id + ":" + user.getStafferName() + "(after):" + newOut.getStatus()
                            + "(预计:" + OutConstant.STATUS_SUBMIT + ")");
            }

            if (newOut.getReserve3() == OutConstant.OUT_SAIL_TYPE_LOCATION_MANAGER
                && newOut.getStatus() != OutConstant.STATUS_LOCATION_MANAGER_CHECK)
            {
                loggerError(id + ":" + user.getStafferName() + "(after):" + newOut.getStatus()
                            + "(预计:" + OutConstant.STATUS_LOCATION_MANAGER_CHECK + ")");
            }
        }
    }

    /**
     * loggerError(严重错误的日志哦)
     * 
     * @param msg
     */
    protected void loggerError(String msg)
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

            // 退库-事业部经理审批
            if (out.getType() == OutConstant.OUT_TYPE_INBILL
                && (out.getOutType() == OutConstant.OUTTYPE_IN_SWATCH || out.getOutType() == OutConstant.OUTTYPE_IN_OUTBACK))
            {
                if (out.getStatus() != OutConstant.BUY_STATUS_SUBMIT)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "状态错误");

                    return mapping.findForward("error");
                }
            }
            else
            {
                if (out.getStatus() != OutConstant.STATUS_SAVE)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "状态错误");

                    return mapping.findForward("error");
                }
            }

            try
            {
                int type = OutConstant.OUTTYPE_IN_SWATCH;

                if (out.getOutType() == OutConstant.OUTTYPE_IN_SWATCH)
                {
                    type = StorageConstant.OPR_STORAGE_SWATH;
                }

                if (out.getOutType() == OutConstant.OUTTYPE_IN_OUTBACK)
                {
                    type = StorageConstant.OPR_STORAGE_OUTBACK;
                }

                outManager.submit(fullId, user, type);
            }
            catch (MYException e)
            {
                _logger.warn(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "处理错误:" + e.getErrorContent());

                return mapping.findForward("error");
            }

            if ( (out.getOutType() == OutConstant.OUTTYPE_IN_OUTBACK || out.getOutType() == OutConstant.OUTTYPE_IN_SWATCH)
                && !StringTools.isNullOrNone(out.getRefOutFullId()))
            {
                // 验证(销售单)是否可以全部回款
                try
                {
                    outManager.payOut(user, out.getRefOutFullId(), "自动核对付款");
                }
                catch (MYException e)
                {
                    _logger.info(e, e);
                }
            }

            CommonTools.saveParamers(request);

            RequestTools.menuInitQuery(request);

            request.setAttribute("queryType", "5");

            request.setAttribute("holdCondition", "1");

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
            temp = consignDAO.findDefaultConsignByFullId(outBean.getFullId());

            if (temp != null)
            {
                outBean.setConsign(temp.getCurrentStatus());
            }
        }

        List<InvoiceBean> invoiceList = invoiceDAO.queryEntityBeansByCondition("where forward = ?",
            InvoiceConstant.INVOICE_FORWARD_OUT);

        request.setAttribute("invoiceList", invoiceList);

        List<DutyVO> dutyList = dutyDAO.listEntityVOs();

        for (DutyVO vo : dutyList)
        {
            List<InvoiceBean> queryForwardOutByDutyId = invoiceDAO.queryForwardOutByDutyId(vo
                .getId());

            vo.setOutInvoiceBeanList(queryForwardOutByDutyId);
        }

        request.setAttribute("dutyList", dutyList);

        List<DepotBean> depotList = depotDAO.listEntityBeans();

        request.setAttribute("depotList", depotList);

        request.getSession().setAttribute("listOut1", list);

        request.setAttribute("now", TimeTools.now("yyyy-MM-dd"));

        getDivs(request, list);

        return mapping.findForward("querySelfOut");
    }

    protected void createDepotList(HttpServletRequest request)
    {
        List<DepotBean> depotList = depotDAO.listEntityBeans();

        StafferBean staffer = Helper.getStaffer(request);

        for (Iterator iterator = depotList.iterator(); iterator.hasNext();)
        {
            DepotBean depotBean = (DepotBean)iterator.next();

            if (StringTools.isNullOrNone(depotBean.getIndustryId()))
            {
                continue;
            }

            if ( !depotBean.getIndustryId().equals(staffer.getIndustryId()))
            {
                iterator.remove();
                continue;
            }
        }

        request.setAttribute("depotList", depotList);
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
    protected void checkQueryOutAuth(HttpServletRequest request)
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
            && !userManager.containAuth(user, AuthConstant.BUY_SUBMIT, AuthConstant.SAIL_SUBMIT))
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

        if ("11".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.SAIL_QUERY_INDUSTY))
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
    protected void checkQueryBuyAuth(HttpServletRequest request)
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

        // 业务员查询自己的退货单
        if ("7".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.SAIL_SUBMIT))
        {
            throw new MYException("用户没有此操作的权限");
        }

        // 总部会计
        if ("8".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.BILL_QUERY_ALL))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("9".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.SAIL_LOCATION_MANAGER))
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
    protected void checkQueryOutBalanceAuth(HttpServletRequest request)
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

        if ("3".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.BUY_SUBMIT))
        {
            throw new MYException("用户没有此操作的权限");
        }

        if ("4".equals(queryType)
            && !userManager.containAuth(user.getId(), AuthConstant.FINANCE_CHECK))
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
                Object attribute = request.getAttribute("holdCondition");

                ConditionParse condtion = null;

                if (attribute == null)
                {
                    condtion = getQueryCondition(request, user);
                }
                else
                {
                    condtion = OldPageSeparateTools.getCondition(request, QUERYOUT);
                }

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
            temp = consignDAO.findDefaultConsignByFullId(outBean.getFullId());

            if (temp != null)
            {
                outBean.setConsign(temp.getReprotType());
            }
        }

        // 处理仓库
        List<DepotBean> depotList = handerDepot(request, user);

        request.setAttribute("depotList", depotList);

        int radioIndex = CommonTools.parseInt(request.getParameter("radioIndex"));

        Map map = (Map)request.getSession().getAttribute("ppmap");

        if (list.size() > 0 && radioIndex >= list.size())
        {
            request.setAttribute("radioIndex", list.size() - 1);

            map.put("radioIndex", list.size() - 1);
        }

        List<InvoiceBean> invoiceList = invoiceDAO.queryEntityBeansByCondition("where forward = ?",
            InvoiceConstant.INVOICE_FORWARD_OUT);

        request.setAttribute("invoiceList", invoiceList);

        request.getSession().setAttribute("listOut1", list);

        List<DepartmentBean> departementList = departmentDAO.listEntityBeans();

        request.setAttribute("departementList", departementList);

        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList", dutyList);

        request.setAttribute("now", TimeTools.now("yyyy-MM-dd"));

        handlerFlow(request, list, true);

        // 这里是过滤
        String queryType = RequestTools.getValueFromRequest(request, "queryType");

        if ("1".equals(queryType))
        {
            showLastCredit(request, user, "0");
        }

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

        saveQueryType(request);

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
                Object attribute = request.getAttribute("holdCondition");

                ConditionParse condtion = null;

                if (attribute == null)
                {
                    condtion = getQueryBuyCondition(request, user);
                }
                else
                {
                    condtion = OldPageSeparateTools.getCondition(request, QUERYBUY);
                }

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

        request.setAttribute("listOut1", list);

        List<DepotBean> depotList = depotDAO.listEntityBeans();

        request.setAttribute("depotList", depotList);

        List<PrincipalshipBean> locationList = orgManager.listAllIndustry();

        request.setAttribute("locationList", locationList);

        int radioIndex = CommonTools.parseInt(request.getParameter("radioIndex"));

        Map map = (Map)request.getSession().getAttribute("ppmap");

        if (list.size() > 0 && radioIndex >= list.size())
        {
            request.setAttribute("radioIndex", list.size() - 1);

            map.put("radioIndex", list.size() - 1);
        }

        request.getSession().setAttribute("listOut1", list);

        request.setAttribute("now", TimeTools.now("yyyy-MM-dd"));

        getDivs(request, list);

        return mapping.findForward("queryBuy");
    }

    /**
     * saveQueryType
     * 
     * @param request
     */
    private void saveQueryType(HttpServletRequest request)
    {
        String queryType = request.getParameter("queryType");

        if ( !StringTools.isNullOrNone(queryType))
        {
            request.getSession().setAttribute("queryType", queryType);

            return;
        }

        Object attribute = request.getAttribute("queryType");

        if (attribute != null)
        {
            request.getSession().setAttribute("queryType", attribute);
        }
    }

    /**
     * 处理仓库
     * 
     * @param request
     * @param user
     * @return
     */
    protected List<DepotBean> handerDepot(HttpServletRequest request, User user)
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
    protected void handlerFlow(HttpServletRequest request, List<OutVO> list, boolean alert)
    {
        ConsignBean temp = null;

        // 物流的需要知道是否有发货单
        double total = 0.0d;

        Map<String, String> hasMap = new HashMap<String, String>();
        Map<String, String> overDayMap = new HashMap<String, String>();

        for (OutBean outBean : list)
        {
            temp = consignDAO.findDefaultConsignByFullId(outBean.getFullId());

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

    protected ConditionParse getQuerySelfCondition(HttpServletRequest request, User user)
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

        String vtype = request.getParameter("vtype");

        if ( !StringTools.isNullOrNone(vtype))
        {
            condtion.addIntCondition("OutBean.vtype", "=", vtype);
        }

        String customerId = request.getParameter("customerId");

        if ( !StringTools.isNullOrNone(customerId))
        {
            // 客户查询的时候可以查看所有的客户历史
            condtion.addCondition("OutBean.customerId", "=", customerId);
        }
        else
        {
            // 如果不是默认查询整个vtype
            if ( !String.valueOf(OutConstant.VTYPE_SPECIAL).equals(vtype))
            {
                // 只能查询自己的
                condtion.addCondition("OutBean.STAFFERID", "=", user.getStafferId());
            }
        }

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
            if ("99".equals(status))
            {
                condtion.addCondition(" and OutBean.status in (3, 4)");
            }
            else
            {
                condtion.addIntCondition("OutBean.status", "=", status);
            }
        }
        else
        {
            request.setAttribute("status", null);
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

        condtion.addCondition("order by OutBean.id desc");

        return condtion;
    }

    /**
     * getQuerySelfBuyCondition
     * 
     * @param request
     * @param user
     * @return
     */
    protected ConditionParse getQuerySelfBuyCondition(HttpServletRequest request, User user)
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        String vtype = request.getParameter("vtype");

        if ( !StringTools.isNullOrNone(vtype))
        {
            condtion.addIntCondition("OutBean.vtype", "=", vtype);
        }

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
            if ("99".equals(status))
            {
                condtion.addCondition(" and OutBean.status in (3, 4)");
            }
            else
            {
                condtion.addIntCondition("OutBean.status", "=", status);
            }
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

        String customerName = request.getParameter("customerName");

        if ( !StringTools.isNullOrNone(customerName))
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
            condtion.addIntCondition("inway", "=", inway);
        }

        condtion.addCondition("order by OutBean.id desc");

        return condtion;
    }

    /**
     * getQuerySelfBalanceCondition
     * 
     * @param request
     * @param user
     * @return
     */
    protected ConditionParse getQueryBalanceCondition(HttpServletRequest request, User user)
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
            condtion.addCondition("OutBalanceBean.logTime", ">=", TimeTools.now_short( -30));

            request.setAttribute("outTime", TimeTools.now_short( -30));
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

        String id = request.getParameter("qid");

        if ( !StringTools.isNullOrNone(id))
        {
            condtion.addCondition("OutBalanceBean.id", "like", id.trim());
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
        // 查询退货的
        else if ("3".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBalanceBean.status", "=",
                    OutConstant.OUTBALANCE_STATUS_PASS);

                request.setAttribute("status", OutConstant.OUTBALANCE_STATUS_PASS);
            }

            condtion.addIntCondition("OutBalanceBean.type", "=", OutConstant.OUTBALANCE_TYPE_BACK);

            request.setAttribute("type", OutConstant.OUTBALANCE_TYPE_BACK);

            // 库存所管辖的
            setDepotCondotionInOutBlance(user, condtion);
        }
        // 退货总部核对
        else if ("4".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBalanceBean.status", "=",
                    OutConstant.OUTBALANCE_STATUS_END);

                request.setAttribute("status", OutConstant.OUTBALANCE_STATUS_END);

                condtion.addIntCondition("OutBalanceBean.checkStatus", "=",
                    PublicConstant.CHECK_STATUS_INIT);
            }

            condtion.addIntCondition("OutBalanceBean.type", "=", OutConstant.OUTBALANCE_TYPE_BACK);

            request.setAttribute("type", OutConstant.OUTBALANCE_TYPE_BACK);
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
     * @throws MYException
     */
    protected ConditionParse getQueryCondition(HttpServletRequest request, User user)
        throws MYException
    {
        Map<String, String> queryOutCondtionMap = CommonTools.saveParamersToMap(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

        String vtype = request.getParameter("vtype");

        if ( !StringTools.isNullOrNone(vtype))
        {
            condtion.addIntCondition("OutBean.vtype", "=", vtype);
        }

        // 这里是过滤
        String queryType = RequestTools.getValueFromRequest(request, "queryType");

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

            queryOutCondtionMap.put("outTime", TimeTools.now_short( -7));
        }

        if ( !StringTools.isNullOrNone(outTime1))
        {
            condtion.addCondition("OutBean.outTime", "<=", outTime1);
        }
        else
        {
            condtion.addCondition("OutBean.outTime", "<=", TimeTools.now_short());

            request.setAttribute("outTime1", TimeTools.now_short());

            queryOutCondtionMap.put("outTime1", TimeTools.now_short());
        }

        String changeTime = request.getParameter("changeTime");

        String changeTime1 = request.getParameter("changeTime1");

        if ( !StringTools.isNullOrNone(changeTime))
        {
            condtion.addCondition("OutBean.changeTime", ">=", changeTime + " 00:00:00");
        }

        if ( !StringTools.isNullOrNone(changeTime1))
        {
            condtion.addCondition("OutBean.changeTime", "<=", changeTime1 + " 23:59:59");
        }

        String id = request.getParameter("id");

        if ( !StringTools.isNullOrNone(id))
        {
            condtion.addCondition("OutBean.fullid", "like", id.trim());
        }

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(status))
        {
            if ("99".equals(status))
            {
                condtion.addCondition(" and OutBean.status in (3, 4)");
            }
            else
            {
                condtion.addIntCondition("OutBean.status", "=", status);
            }
        }

        String customerId = request.getParameter("customerId");

        if ( !StringTools.isNullOrNone(customerId))
        {
            condtion.addCondition("OutBean.customerId", "=", customerId);
        }

        String duty = request.getParameter("duty");

        if ( !StringTools.isNullOrNone(duty))
        {
            condtion.addCondition("OutBean.dutyId", "=", duty);
        }

        String invoiceStatus = request.getParameter("invoiceStatus");

        if ( !StringTools.isNullOrNone(invoiceStatus))
        {
            condtion.addIntCondition("OutBean.invoiceStatus", "=", invoiceStatus);
        }

        String customerName = request.getParameter("customerName");

        if ( !StringTools.isNullOrNone(customerName))
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

        // 事业部经理查询
        if ("1".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=",
                    OutConstant.STATUS_LOCATION_MANAGER_CHECK);

                request.setAttribute("status", OutConstant.STATUS_LOCATION_MANAGER_CHECK);

                queryOutCondtionMap.put("status", String
                    .valueOf(OutConstant.STATUS_LOCATION_MANAGER_CHECK));
            }

            condtion.addCondition("and OutBean.industryId in " + getAllIndustryId(staffer));
        }
        // 结算中心查询
        else if ("2".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_SUBMIT);

                request.setAttribute("status", OutConstant.STATUS_SUBMIT);

                queryOutCondtionMap.put("status", String.valueOf(OutConstant.STATUS_SUBMIT));
            }
        }
        // 处理发货单 物流审批(只有中心仓库才有物流的)
        else if ("3".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_MANAGER_PASS);

                request.setAttribute("status", OutConstant.STATUS_MANAGER_PASS);

                queryOutCondtionMap.put("status", String.valueOf(OutConstant.STATUS_MANAGER_PASS));
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

                queryOutCondtionMap.put("status", String.valueOf(OutConstant.STATUS_FLOW_PASS));
            }

            setDepotCondotionInOut(user, condtion);

            condtion.addCondition("order by managerTime desc");
        }
        // 会计往来核对
        else if ("5".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.pay", "=", OutConstant.PAY_NOT);

                condtion.addCondition("and OutBean.status in (3, 4)");

                request.setAttribute("pay", OutConstant.PAY_NOT);

                queryOutCondtionMap.put("pay", String.valueOf(OutConstant.PAY_NOT));
            }

            if ( !userManager.containAuth(user.getId(), AuthConstant.BILL_QUERY_ALL))
            {
                condtion.addCondition("OutBean.locationId", "=", user.getLocationId());
            }

            condtion.addCondition("order by changeTime desc");
        }
        // 总部核对(已经付款的销售单)
        else if ("6".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_PASS);

                request.setAttribute("status", OutConstant.STATUS_PASS);

                queryOutCondtionMap.put("status", String.valueOf(OutConstant.STATUS_PASS));
            }

            condtion.addCondition("order by changeTime desc");
        }
        // 总裁审批赠送
        else if ("7".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_CEO_CHECK);

                request.setAttribute("status", OutConstant.STATUS_CEO_CHECK);

                queryOutCondtionMap.put("status", String.valueOf(OutConstant.STATUS_CEO_CHECK));
            }
        }
        // 查询销售退库
        else if ("8".equals(queryType))
        {
            condtion.addCondition("and OutBean.status in (3, 4)");

            condtion.addIntCondition("OutBean.outType", "<>", OutConstant.OUTTYPE_OUT_SWATCH);

            condtion.addIntCondition("OutBean.outType", "<>", OutConstant.OUTTYPE_OUT_CONSIGN);

            condtion.addCondition("OutBean.stafferId", "=", user.getStafferId());
        }
        // 查询没有结束的个人领样
        else if ("9".equals(queryType))
        {
            condtion.addIntCondition("OutBean.outType", "=", OutConstant.OUTTYPE_OUT_SWATCH);

            condtion.addCondition("and OutBean.status in (3, 4)");

            condtion.addCondition("OutBean.stafferId", "=", user.getStafferId());

            request.setAttribute("outType", OutConstant.OUTTYPE_OUT_SWATCH);

            queryOutCondtionMap.put("outType", String.valueOf(OutConstant.OUTTYPE_OUT_SWATCH));

            request.setAttribute("status", OutConstant.STATUS_PASS);

            queryOutCondtionMap.put("status", String.valueOf(OutConstant.STATUS_PASS));
        }
        // 查询下属的销售单
        else if ("10".equals(queryType))
        {
            if (StringTools.isNullOrNone(staffer.getIndustryId2()))
            {
                condtion.addCondition("OutBean.stafferId", "=", user.getStafferId());
            }
            else
            {
                condtion.addCondition("OutBean.industryId2", "=", staffer.getIndustryId2());
            }
        }
        // 查询事业部的销售单
        else if ("11".equals(queryType))
        {
            if (StringTools.isNullOrNone(staffer.getIndustryId()))
            {
                condtion.addCondition("OutBean.stafferId", "=", user.getStafferId());
            }
            else
            {
                condtion.addCondition("OutBean.industryId", "=", staffer.getIndustryId());
            }
        }
        // 未知的则什么都没有
        else
        {
            condtion.addFlaseCondition();
        }

        if ( !condtion.containOrder())
        {
            condtion.addCondition("order by OutBean.id desc");
        }

        request.getSession().setAttribute("ppmap", queryOutCondtionMap);

        return condtion;
    }

    /**
     * getIndustryIdCredit
     * 
     * @param industryId
     * @param managerStafferId
     * @return double
     */
    protected double getIndustryIdCredit(String industryId, String managerStafferId)
    {
        List<InvoiceCreditBean> inList = invoiceCreditDAO.queryEntityBeansByFK(managerStafferId);

        for (InvoiceCreditBean invoiceCreditBean : inList)
        {
            if (invoiceCreditBean.getInvoiceId().equals(industryId))
            {
                return invoiceCreditBean.getCredit();
            }
        }

        return 0.0d;
    }

    protected String getAllIndustryId(StafferBean sb)
        throws MYException
    {
        List<InvoiceCreditBean> inList = invoiceCreditDAO.queryEntityBeansByFK(sb.getId());

        if (inList.size() == 1)
        {
            return "('" + sb.getIndustryId() + "')";
        }

        if (inList.size() == 0)
        {
            throw new MYException("职员[%s]没有事业部属性", sb.getName());
        }

        StringBuffer buffer = new StringBuffer();

        buffer.append("(");

        for (InvoiceCreditBean invoiceCreditBean : inList)
        {
            buffer.append("'").append(invoiceCreditBean.getInvoiceId()).append("'").append(",");
        }

        buffer.deleteCharAt(buffer.length() - 1);

        buffer.append(")");

        return buffer.toString();
    }

    /**
     * 入库单审批过程的查询(条件的设置)
     * 
     * @param request
     * @param user
     * @return
     * @throws MYException
     */
    protected ConditionParse getQueryBuyCondition(HttpServletRequest request, User user)
        throws MYException
    {
        Map<String, String> queryOutCondtionMap = CommonTools.saveParamersToMap(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        String vtype = request.getParameter("vtype");

        if ( !StringTools.isNullOrNone(vtype))
        {
            condtion.addIntCondition("OutBean.vtype", "=", vtype);
        }

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

            queryOutCondtionMap.put("outTime", TimeTools.now_short( -7));
        }

        if ( !StringTools.isNullOrNone(outTime1))
        {
            condtion.addCondition("OutBean.outTime", "<=", outTime1);
        }
        else
        {
            condtion.addCondition("OutBean.outTime", "<=", TimeTools.now_short());

            request.setAttribute("outTime1", TimeTools.now_short());

            queryOutCondtionMap.put("outTime1", TimeTools.now_short());
        }

        String changeTime = request.getParameter("changeTime");

        String changeTime1 = request.getParameter("changeTime1");

        if ( !StringTools.isNullOrNone(changeTime))
        {
            condtion.addCondition("OutBean.changeTime", ">=", changeTime + " 00:00:00");
        }

        if ( !StringTools.isNullOrNone(changeTime1))
        {
            condtion.addCondition("OutBean.changeTime", "<=", changeTime1 + " 23:59:59");
        }

        String id = request.getParameter("id");

        if ( !StringTools.isNullOrNone(id))
        {
            condtion.addCondition("OutBean.fullid", "like", id);
        }

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(status))
        {
            if ("99".equals(status))
            {
                condtion.addCondition(" and OutBean.status in (3, 4)");
            }
            else
            {
                condtion.addIntCondition("OutBean.status", "=", status);
            }
        }

        String customerId = request.getParameter("customerId");

        if ( !StringTools.isNullOrNone(customerId))
        {
            condtion.addCondition("OutBean.customerId", "=", customerId);
        }

        String industryId = request.getParameter("industryId");

        if ( !StringTools.isNullOrNone(industryId))
        {
            condtion.addCondition("OutBean.industryId", "=", industryId);
        }

        String customerName = request.getParameter("customerName");

        if ( !StringTools.isNullOrNone(customerName))
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

        String destinationId = request.getParameter("destinationId");

        if ( !StringTools.isNullOrNone(destinationId))
        {
            condtion.addCondition("OutBean.destinationId", "=", destinationId);
        }

        String inway = request.getParameter("inway");

        if ( !StringTools.isNullOrNone(inway))
        {
            condtion.addIntCondition("OutBean.inway", "=", inway);
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

                queryOutCondtionMap.put("status", String
                    .valueOf(OutConstant.STATUS_LOCATION_MANAGER_CHECK));
            }

            condtion.addCondition("and OutBean.industryId in " + getAllIndustryId(staffer));
        }
        // 总裁审核
        else if ("2".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_CEO_CHECK);

                request.setAttribute("status", OutConstant.STATUS_CEO_CHECK);

                queryOutCondtionMap.put("status", String.valueOf(OutConstant.STATUS_CEO_CHECK));
            }
        }
        // 董事长审核
        else if ("3".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_CHAIRMA_CHECK);

                request.setAttribute("status", OutConstant.STATUS_CHAIRMA_CHECK);

                queryOutCondtionMap.put("status", String.valueOf(OutConstant.STATUS_CHAIRMA_CHECK));
            }
        }
        // 库管调拨
        else if ("4".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addCondition("and OutBean.status in (3, 4)");

                condtion.addIntCondition("OutBean.inway", "=", OutConstant.IN_WAY);

                request.setAttribute("inway", OutConstant.IN_WAY);

                queryOutCondtionMap.put("inway", String.valueOf(OutConstant.IN_WAY));
            }

            setDepotCondotionInBuy(user, condtion);
        }
        // 领样退库/销售退库
        else if ("5".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.BUY_STATUS_SUBMIT);

                request.setAttribute("status", OutConstant.BUY_STATUS_SUBMIT);

                queryOutCondtionMap.put("status", String.valueOf(OutConstant.BUY_STATUS_SUBMIT));

                // 领样退库/销售退库
                condtion.addCondition("and OutBean.outType in (4, 5)");
            }

            setLocalDepotConditionInBuy(user, condtion);
        }
        else if ("6".equals(queryType))
        {
            // 可以查询所有
        }
        // 业务员查询自己的销售退库
        else if ("7".equals(queryType))
        {
            condtion.addCondition("OutBean.stafferId", "=", user.getStafferId());

            condtion.addCondition("and OutBean.outType in (4, 5)");
        }
        // 会计总部核对
        else if ("8".equals(queryType))
        {
            if (OldPageSeparateTools.isMenuLoad(request))
            {
                condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_PASS);

                request.setAttribute("status", OutConstant.STATUS_PASS);

                queryOutCondtionMap.put("status", String.valueOf(OutConstant.STATUS_PASS));
            }
        }
        // 事业部经理领样退库/销售退库
        else if ("9".equals(queryType))
        {
            condtion.addIntCondition("OutBean.status", "=", OutConstant.STATUS_SAVE);

            request.setAttribute("status", OutConstant.STATUS_SAVE);

            queryOutCondtionMap.put("status", String.valueOf(OutConstant.STATUS_SAVE));

            // 领样退库/销售退库
            condtion.addCondition("and OutBean.outType in (4, 5)");

            condtion.addCondition("and OutBean.industryId in " + getAllIndustryId(staffer));
        }
        // 未知的则什么都没有
        else
        {
            condtion.addFlaseCondition();
        }

        if ( !condtion.containOrder())
        {
            condtion.addCondition("order by OutBean.id desc");
        }

        request.getSession().setAttribute("ppmap", queryOutCondtionMap);

        return condtion;
    }

    /**
     * 设置仓库的过滤条件
     * 
     * @param user
     * @param condtion
     */
    protected void setDepotCondotionInBuy(User user, ConditionParse condtion)
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
     * setDepotCondotionInOutBlance
     * 
     * @param user
     * @param condtion
     */
    protected void setDepotCondotionInOutBlance(User user, ConditionParse condtion)
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
                    sb.append("OutBalanceBean.dirDepot = '" + authBean.getId() + "' or ");
                }
                else
                {
                    sb.append("OutBalanceBean.dirDepot = '" + authBean.getId() + "'");
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
    protected void setLocalDepotConditionInBuy(User user, ConditionParse condtion)
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
    protected void setDepotCondotionInOut(User user, ConditionParse condtion)
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
     * queryShow
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryShow(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        Map<String, String> ssmap = CommonTools.saveParamersToMap(request);

        String[] showIds = request.getParameterValues("showId");

        // showIds
        List<String> showIdList = new ArrayList();

        if (showIds != null && showIds.length > 0)
        {
            for (String string : showIds)
            {
                showIdList.add(string);
            }
        }

        request.getSession().setAttribute("showIds", showIdList);

        request.getSession().setAttribute("ssmap", ssmap);

        List<SailConfigVO> resultList = new ArrayList();

        String dutyId = ssmap.get("duty");

        DutyBean duty = dutyDAO.find(dutyId);

        for (String showId : showIdList)
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            condtion.addIntCondition("finType" + duty.getType(), "=", ssmap.get("finType"));

            condtion.addIntCondition("ratio" + duty.getType(), ">", 0);

            condtion.addIntCondition("sailType", "=", ssmap.get("sailType"));

            condtion.addIntCondition("productType", "=", ssmap.get("productType"));

            condtion.addCondition("showId", "=", showId);

            List<SailConfigVO> eachtList = sailConfigDAO.queryEntityVOsByCondition(condtion);

            if (resultList.isEmpty())
            {
                resultList.addAll(eachtList);
            }
            else
            {
                for (Iterator iterator = resultList.iterator(); iterator.hasNext();)
                {
                    SailConfigVO sailConfigVO = (SailConfigVO)iterator.next();

                    boolean hasIn = false;

                    for (SailConfigVO each : eachtList)
                    {
                        String property1 = BeanUtil.getProperty(sailConfigVO, "ratio"
                                                                              + duty.getType());
                        String property2 = BeanUtil.getProperty(each, "ratio" + duty.getType());

                        if ( !"".equals(property1) && property1.equals(property2))
                        {
                            hasIn = true;
                            break;
                        }
                    }

                    if ( !hasIn)
                    {
                        iterator.remove();
                    }
                }
            }
        }

        for (SailConfigVO sailConfigVO : resultList)
        {
            String property1 = BeanUtil.getProperty(sailConfigVO, "ratio" + duty.getType());

            sailConfigVO.setRatio0(CommonTools.parseInt(property1));
        }

        List<ShowBean> showList = (List<ShowBean>)request.getSession().getAttribute("g_showList");

        if (showIdList != null)
        {
            for (ShowBean showBean : showList)
            {
                if (showIdList.contains(showBean.getId()))
                {
                    showBean.setDescription("1");
                }
                else
                {
                    showBean.setDescription("0");
                }
            }
        }

        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList", dutyList);

        request.getSession().setAttribute("navigationList", resultList);

        // 进入导航页面
        return mapping.findForward("navigationAddOut1");
    }

    /**
     * getCondition
     * 
     * @param condtion
     * @param stafferName
     */
    protected void getCondition(ConditionParse condtion, String stafferName)
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
     * 查询REF的入库单(已经通过的,退库的)
     * 
     * @param request
     * @param outId
     * @return
     */
    protected List<OutBean> queryRefOut(HttpServletRequest request, String outId)
    {
        // 查询当前已经有多少个人领样
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addCondition(" and OutBean.status in (3, 4)");

        con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        // 排除其他入库(对冲单据)
        con.addIntCondition("OutBean.outType", "<>", OutConstant.OUTTYPE_IN_OTHER);

        List<OutBean> refBuyList = outDAO.queryEntityBeansByCondition(con);

        for (OutBean outBean : refBuyList)
        {
            List<BaseBean> list = baseDAO.queryEntityBeansByFK(outBean.getFullId());

            outBean.setBaseList(list);
        }

        request.setAttribute("refBuyList", refBuyList);

        return refBuyList;
    }

    protected void queryRefOut2(HttpServletRequest request, String outId)
    {
        // 验证ref
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addCondition("OutBean.type", "=", OutConstant.OUT_TYPE_OUTBILL);

        List<OutBean> refList = outDAO.queryEntityBeansByCondition(con);

        request.setAttribute("refOutList", refList);
    }

    protected void queryRefOut3(HttpServletRequest request, String outId)
    {
        // 验证ref
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        con.addIntCondition("OutBean.outType", "=", OutConstant.OUTTYPE_IN_OTHER);

        List<OutBean> refList = outDAO.queryEntityBeansByCondition(con);

        request.setAttribute("refOutToBuyList", refList);
    }

    /**
     * getDivs
     * 
     * @param request
     * @param list
     */
    protected void getDivs(HttpServletRequest request, List list)
    {
        Map divMap = new HashMap();

        String queryType = RequestTools.getValueFromRequest(request, "queryType");

        // 是否可以看到真实的成本
        boolean containAuth = userManager.containAuth(Helper.getUser(request).getId(),
            AuthConstant.SAIL_QUERY_COST);

        if (list != null)
        {
            for (Object each : list)
            {
                OutBean bean = (OutBean)each;

                try
                {
                    List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(bean.getFullId());

                    for (BaseBean baseBean : baseList)
                    {
                        // 销售价低于成本
                        if ("2".equals(queryType))
                        {
                            if (bean.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
                            {
                                break;
                            }

                            if (baseBean.getPrice() < baseBean.getCostPrice())
                            {
                                bean.setReserve9("1");

                                break;
                            }
                        }
                    }

                    if (OATools.isChangeToV5())
                    {
                        if ( !containAuth)
                        {
                            for (BaseBean baseBean : baseList)
                            {
                                // 显示成本
                                baseBean.setCostPrice(baseBean.getInputPrice());
                            }
                        }
                    }

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
}
