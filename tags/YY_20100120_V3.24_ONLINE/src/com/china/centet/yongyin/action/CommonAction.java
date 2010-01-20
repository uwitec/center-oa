/*
 * File Name: CommonAction.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-8-24
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.action;


import java.util.Calendar;
import java.util.Date;
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

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.common.OldPageSeparateTools;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.RandomTools;
import com.china.center.tools.Security;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.Helper;
import com.china.centet.yongyin.bean.BankBean;
import com.china.centet.yongyin.bean.BaseUser;
import com.china.centet.yongyin.bean.LocationBean;
import com.china.centet.yongyin.bean.ProductTypeBean;
import com.china.centet.yongyin.bean.ProviderBean;
import com.china.centet.yongyin.bean.Role;
import com.china.centet.yongyin.bean.RoleBean;
import com.china.centet.yongyin.bean.StafferBean;
import com.china.centet.yongyin.bean.StatBean;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.bean.helper.RoleHelper;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.CustomerDAO;
import com.china.centet.yongyin.dao.ProductTypeDAO;
import com.china.centet.yongyin.dao.ProductTypeVSCustomerDAO;
import com.china.centet.yongyin.dao.ProviderDAO;
import com.china.centet.yongyin.dao.StafferDAO;
import com.china.centet.yongyin.dao.StatDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.manager.BankManager;
import com.china.centet.yongyin.manager.CenterDemo;
import com.china.centet.yongyin.manager.CustomerManager;
import com.china.centet.yongyin.manager.LocationManager;
import com.china.centet.yongyin.manager.MemeberManager;
import com.china.centet.yongyin.vo.BaseUserVO;
import com.china.centet.yongyin.vs.ProductTypeVSCustomer;


/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2007-8-24
 * @see
 * @since
 */
public class CommonAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private CommonDAO commonDAO = null;

    private StatDAO statDAO = null;

    private StafferDAO stafferDAO = null;

    private BankManager bankManager = null;

    private LocationManager locationManager = null;

    private MemeberManager memeberManager = null;

    private CustomerDAO customerDAO = null;

    private ProviderDAO providerDAO = null;

    private CustomerManager customerManager = null;

    private UserDAO userDAO = null;

    private CenterDemo centerDemo = null;

    private ProductTypeDAO productTypeDAO = null;

    private ProductTypeVSCustomerDAO productTypeVSCustomerDAO = null;

    public ActionForward addCommon(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String type = request.getParameter("type");

        String name = request.getParameter("name").trim();

        CommonTools.saveParamers(request);
        try
        {
            String tablename = null;

            if ("3".equals(type))
            {
                tablename = "t_center_departement";
            }

            if ("4".equals(type))
            {
                StafferBean bean = new StafferBean();

                bean.setName(name);

                stafferDAO.saveEntityBean(bean);
            }

            if ("5".equals(type))
            {
                tablename = "t_center_bank";
            }

            if (tablename != null)
            {
                commonDAO.add(name, tablename);
            }
        }
        catch (Exception e)
        {
            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加日常信息错误");
            return mapping.findForward("error");
        }

        request.setAttribute(KeyConstant.MESSAGE, "增加日常信息成功");

        return mapping.findForward("readd");
    }

    public ActionForward preForaddCustmer(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<String> lists = commonDAO.listAll("t_center_oastaffer");

        request.setAttribute("staffers", lists);

        return mapping.findForward("recustomer");
    }

    public ActionForward preForUpdateCustmer(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse reponse)
        throws ServletException
    {
        request.setAttribute(KeyConstant.ERROR_MESSAGE, "此功能已经废弃");

        return mapping.findForward("error");
    }

    public ActionForward preForaddLocation(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<String> lists = commonDAO.listAll("t_center_oastaffer");

        request.setAttribute("staffers", lists);

        return mapping.findForward("addLocation");
    }

    public ActionForward queryLocation(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<LocationBean> lists = locationManager.listLocation();

        request.setAttribute("locationList", lists);

        return mapping.findForward("listLocation");
    }

    /**
     * 增加区域
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addLocation(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User oprUser = Helper.getUser(request);

        if (oprUser.getRole() != Role.TOP)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限增加区域.");

            return preForaddLocation(mapping, form, request, reponse);
        }

        LocationBean lbean = new LocationBean();

        BeanUtil.getBean(lbean, request);

        User user = new User();

        BeanUtil.getBean(user, request);

        user.setType(1);

        String password = RandomTools.getRandomString(Constant.PASSWORD_MIN_LENGTH);

        user.setPassword(Security.getSecurity(password));

        try
        {
            locationManager.addLocation(lbean, user);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            return preForaddLocation(mapping, form, request, reponse);
        }

        request.setAttribute(KeyConstant.MESSAGE, "增加区域成功!增加人员[" + user.getName()
                                                  + "]成功,密码[<font color=red>" + password
                                                  + "</font>],请及时通知相应人员密码!");

        return preForaddLocation(mapping, form, request, reponse);
    }

    /**
     * 增加银行
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addBank(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        // User oprUser = Helper.getUser(request);
        BankBean bank = new BankBean();

        BeanUtil.getBean(bank, request);

        try
        {
            bankManager.addBank(bank);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            return queryBank(mapping, form, request, reponse);
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功增加银行:" + bank.getName());

        return queryBank(mapping, form, request, reponse);
    }

    /**
     * 增加银行
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward modfiyBank(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        // User oprUser = Helper.getUser(request);
        BankBean bank = new BankBean();

        BeanUtil.getBean(bank, request);

        try
        {
            bankManager.modifyBank(bank);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            return queryBank(mapping, form, request, reponse);
        }

        request.setAttribute(KeyConstant.MESSAGE, "成功修改银行:" + bank.getName());

        return queryBank(mapping, form, request, reponse);
    }

    /**
     * 修改余额
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward modifyMoney(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        String tatolMoney = request.getParameter("tatolMoney");

        StatBean sb = statDAO.findStatBeanById(id);

        if (sb == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "上月银行统计不存在,请联系管理员手工增加.");
            return mapping.findForward("error");
        }

        sb.setTatolMoney(Double.parseDouble(tatolMoney));

        statDAO.updateStat(sb);

        request.setAttribute(KeyConstant.MESSAGE, "成功修改银行" + sb.getBank() + "的余额");

        return queryBank(mapping, form, request, reponse);
    }

    /**
     * 修改余额
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return ActionForward
     * @throws ServletException
     */
    public ActionForward findMoney(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        BankBean bank = bankManager.findBankById(id);

        if (bank == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "银行不存在");
            return mapping.findForward("error");
        }

        // 获得上个月是否统计
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -1);

        String lastId = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()), "yyyyMM");

        cal.add(Calendar.MONTH, -1);

        String llastId = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()), "yyyyMM");

        StatBean sb = statDAO.findStatBeanByBank(lastId, bank.getName());

        if (sb == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "上月银行统计不存在,请联系管理员手工增加.");
            return mapping.findForward("error");
        }

        request.setAttribute("satatBean", sb);
        request.setAttribute("lastId", lastId);
        request.setAttribute("llastId", llastId);

        return mapping.findForward("modifyMoney");
    }

    public ActionForward preForaddBank(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String id = request.getParameter("id");

        if ( !StringTools.isNullOrNone(id))
        {
            BankBean bank = bankManager.findBankById(id);

            if (bank != null)
            {
                request.setAttribute("bank", bank);

                request.setAttribute("modify", true);
            }
        }
        else
        {
            request.setAttribute("modify", false);
        }

        List<LocationBean> lists = locationManager.listLocation();

        request.setAttribute("locationList", lists);

        return mapping.findForward("addBank");
    }

    public ActionForward queryBank(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<BankBean> lists = bankManager.listBank();

        LocationBean lb = null;

        // 获得上个月是否统计
        Calendar cal = Calendar.getInstance();

        cal.add(Calendar.MONTH, -1);

        String lastId = TimeTools.getStringByFormat(new Date(cal.getTimeInMillis()), "yyyyMM");

        for (BankBean bankBean : lists)
        {
            if ( !StringTools.isNullOrNone(bankBean.getLocationId()))
            {
                lb = locationManager.findLocationById(bankBean.getLocationId());

                if (lb != null)
                {
                    bankBean.setLocationName(lb.getLocationName());
                }

                StatBean sb = statDAO.findStatBeanByBank(lastId, bankBean.getName());

                if (sb != null)
                {
                    bankBean.setLmoney(sb.getTatolMoney());
                }
                else
                {
                    bankBean.setLmoney(0.0d);
                }
            }
        }

        request.setAttribute("listBank", lists);
        request.setAttribute("lastId", lastId);

        return mapping.findForward("listBank");
    }

    /**
     * 客户绑定产品类型(前置)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForBing(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String customerId = request.getParameter("customerId");

        List<ProductTypeBean> ptype = productTypeDAO.listEntityBeans();

        request.setAttribute("list", ptype);

        // 供应商
        ProviderBean bean = providerDAO.find(customerId);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "客户不存在");

            return mapping.findForward("error");
        }

        request.setAttribute("bean", bean);

        List<ProductTypeVSCustomer> list = productTypeVSCustomerDAO.queryVSByCustomerId(customerId);

        Map<String, String> ps = new HashMap<String, String>();

        for (ProductTypeVSCustomer productTypeVSCustomer : list)
        {
            ps.put(productTypeVSCustomer.getProductTypeId(), productTypeVSCustomer.getCustomerId());
        }

        request.setAttribute("mapVS", ps);

        return mapping.findForward("bingProductType");
    }

    /**
     * 客户绑定产品类型
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward bingProductTypeToCustmer(ActionMapping mapping, ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse reponse)
        throws ServletException
    {
        String customerId = request.getParameter("customerId");

        String[] productTypeIds = request.getParameterValues("productTypeId");

        if (productTypeIds == null)
        {
            productTypeIds = new String[0];
        }

        try
        {
            memeberManager.bingProductTypeToCustmer(customerId, productTypeIds);

            request.setAttribute(KeyConstant.MESSAGE, "绑定类型成功");
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "绑定类型失败:" + e.getMessage());
        }

        request.setAttribute("forward", "1");

        return queryProvider(mapping, form, request, reponse);
    }

    /**
     * 查询客户
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryCustmer(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        request.setAttribute(KeyConstant.ERROR_MESSAGE, "此功能已经废弃");

        return mapping.findForward("error");
    }

    /**
     * 查询供应商
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
        CommonTools.saveParamers(request);

        if ( !Constant.SYSTEM_LOCATION.equals(Helper.getCurrentLocationId(request))
            && Helper.getUser(request).getRole() != Role.TOP)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "只有总部人员才能操作");

            return mapping.findForward("error");
        }

        String firstLoad = request.getParameter("firstLoad");

        String forward = (String)request.getAttribute("forward");

        String afirstLoad = (String)request.getAttribute("AfirstLoad");

        List<ProviderBean> customerList = null;

        if ("1".equals(firstLoad) || "1".equals(afirstLoad) || "1".equals(forward))
        {
            ConditionParse condition = new ConditionParse();

            condition.addWhereStr();

            setProviderCondition(request, condition);

            int tatol = providerDAO.countBycondition(condition.toString());

            PageSeparate page = new PageSeparate(tatol, Constant.PAGE_SIZE);

            OldPageSeparateTools.initPageSeparate(condition, page, request, "queryProvider");

            customerList = providerDAO.queryEntityBeansBycondition(condition, page);
        }
        else
        {
            OldPageSeparateTools.processSeparate(request, "queryProvider");

            // 处理下上一页
            customerList = providerDAO.queryEntityBeansBycondition(OldPageSeparateTools.getCondition(
                request, "queryProvider"), OldPageSeparateTools.getPageSeparate(request,
                "queryProvider"));
        }

        request.setAttribute("customerList", customerList);

        // updateCustomer
        return mapping.findForward("queryCustomer");
    }

    private void setCustomerCondition(HttpServletRequest request, ConditionParse condition)
    {
        String name = request.getParameter("name");

        if ( !StringTools.isNullOrNone(name))
        {
            condition.addCondition("name", "like", name.trim());
        }

        // stafferName
        String code = request.getParameter("code");

        if ( !StringTools.isNullOrNone(code))
        {
            condition.addCondition("code", "like", code.trim());
        }

        String stafferName = request.getParameter("stafferName");
        if ( !StringTools.isNullOrNone(stafferName))
        {
            condition.addCondition("stafferName", "like", stafferName.trim());
        }
    }

    private void setProviderCondition(HttpServletRequest request, ConditionParse condition)
    {
        String name = request.getParameter("name");

        if ( !StringTools.isNullOrNone(name))
        {
            condition.addCondition("name", "like", name.trim());
        }

        // stafferName
        String code = request.getParameter("code");

        if ( !StringTools.isNullOrNone(code))
        {
            condition.addCondition("code", "like", code.trim());
        }
    }

    /**
     * 增加客户
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addCustmer(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        request.setAttribute(KeyConstant.ERROR_MESSAGE, "此功能已经废弃");

        return mapping.findForward("error");
    }

    /**
     * 修改客户
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward updateCustmer(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        request.setAttribute(KeyConstant.ERROR_MESSAGE, "此功能已经废弃");

        return mapping.findForward("error");
    }

    /**
     * 查询客户(在产品分类下)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryCustmerInVS(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String productTypeId = request.getParameter("productTypeId");

        ConditionParse condition = new ConditionParse();

        List<ProviderBean> customerList = null;

        if (OldPageSeparateTools.isFirstLoad(request))
        {
            setCustomerCondition(request, condition);

            condition.addIntCondition("ProductTypeVSCustomer.productTypeId", "=", productTypeId);

            condition.addCondition("and ProductTypeVSCustomer.customerId = Customer.id");

            int tatol = customerDAO.countCustomerByCondtionInProductTypeVSCustomer(condition);

            PageSeparate page = new PageSeparate(tatol, Constant.PAGE_SIZE);

            OldPageSeparateTools.initPageSeparate(condition, page, request, "rptQueryCustmerInVS");

            customerList = customerDAO.queryCustomerByCondtionInProductTypeVSCustomer(condition,
                page);
        }
        else
        {
            OldPageSeparateTools.processSeparate(request, "rptQueryCustmerInVS");

            // 处理下上一页
            customerList = customerDAO.queryCustomerByCondtionInProductTypeVSCustomer(
                OldPageSeparateTools.getCondition(request, "rptQueryCustmerInVS"),
                OldPageSeparateTools.getPageSeparate(request, "rptQueryCustmerInVS"));
        }

        request.setAttribute("list", customerList);

        return mapping.findForward("rptCustomerInVS");
    }

    /**
     * 流程选择者
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptUser(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<BaseUserVO> list = null;
        try
        {
            if (OldPageSeparateTools.isFirstLoad(request))
            {
                ConditionParse condtion = new ConditionParse();

                setCondition(request, condtion);

                int total = userDAO.countVOBycondition(condtion.toString());

                PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

                OldPageSeparateTools.initPageSeparate(condtion, page, request, "rptUser");

                list = userDAO.queryEntityVOsBycondition(condtion, page);
            }
            else
            {
                OldPageSeparateTools.processSeparate(request, "rptUser");

                list = userDAO.queryEntityVOsBycondition(OldPageSeparateTools.getCondition(request,
                    "rptUser"), OldPageSeparateTools.getPageSeparate(request, "rptUser"));
            }

            for (BaseUser user : list)
            {
                RoleHelper.setBaseRole(user, user.getRole());
            }

            request.setAttribute("list", list);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("rptUser");
    }

    /**
     * 流程选择职员
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptStaffer(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<StafferBean> list = null;
        try
        {
            ConditionParse condtion = new ConditionParse();

            setConditionForStaffer(request, condtion);

            list = stafferDAO.queryEntityBeansBycondition(condtion);

            request.setAttribute("list", list);
        }
        catch (Exception e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            _logger.error(e, e);

            return mapping.findForward("error");
        }

        return mapping.findForward("rptStaffer");
    }

    /**
     * 处理采购的分页
     * 
     * @param request
     * @param condtion
     */
    private void setConditionForStaffer(HttpServletRequest request, ConditionParse condtion)
    {
        condtion.addWhereStr();

        // stafferName
        String name = request.getParameter("name");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("name", "like", name);
        }
    }

    /**
     * 流程选择者
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptRole(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<RoleBean> list = commonDAO.listAllRole();

        request.setAttribute("list", list);

        return mapping.findForward("rptRole");
    }

    /**
     * 处理采购的分页
     * 
     * @param request
     * @param condtion
     */
    private void setCondition(HttpServletRequest request, ConditionParse condtion)
    {
        condtion.addWhereStr();

        // stafferName
        String name = request.getParameter("name");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("BaseUser.name", "like", name);
        }

        String stafferName = request.getParameter("stafferName");

        if ( !StringTools.isNullOrNone(stafferName))
        {
            condtion.addCondition("BaseUser.stafferName", "like", stafferName);
        }
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
     * @return the memeberManager
     */
    public MemeberManager getMemeberManager()
    {
        return memeberManager;
    }

    /**
     * @param memeberManager
     *            the memeberManager to set
     */
    public void setMemeberManager(MemeberManager memeberManager)
    {
        this.memeberManager = memeberManager;
    }

    /**
     * @return the productTypeDAO
     */
    public ProductTypeDAO getProductTypeDAO()
    {
        return productTypeDAO;
    }

    /**
     * @param productTypeDAO
     *            the productTypeDAO to set
     */
    public void setProductTypeDAO(ProductTypeDAO productTypeDAO)
    {
        this.productTypeDAO = productTypeDAO;
    }

    /**
     * @return the productTypeVSCustomerDAO
     */
    public ProductTypeVSCustomerDAO getProductTypeVSCustomerDAO()
    {
        return productTypeVSCustomerDAO;
    }

    /**
     * @param productTypeVSCustomerDAO
     *            the productTypeVSCustomerDAO to set
     */
    public void setProductTypeVSCustomerDAO(ProductTypeVSCustomerDAO productTypeVSCustomerDAO)
    {
        this.productTypeVSCustomerDAO = productTypeVSCustomerDAO;
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
     * @return the centerDemo
     */
    public CenterDemo getCenterDemo()
    {
        return centerDemo;
    }

    /**
     * @param centerDemo
     *            the centerDemo to set
     */
    public void setCenterDemo(CenterDemo centerDemo)
    {
        this.centerDemo = centerDemo;
    }

    /**
     * @return the customerManager
     */
    public CustomerManager getCustomerManager()
    {
        return customerManager;
    }

    /**
     * @param customerManager
     *            the customerManager to set
     */
    public void setCustomerManager(CustomerManager customerManager)
    {
        this.customerManager = customerManager;
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
