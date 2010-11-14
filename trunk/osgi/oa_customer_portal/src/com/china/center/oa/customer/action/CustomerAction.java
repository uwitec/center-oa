/**
 * File Name: StafferAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.action;


import java.io.FileOutputStream;
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
import org.jfree.data.category.CategoryDataset;

import com.center.china.osgi.publics.User;
import com.center.china.osgi.publics.file.read.ReadeFileFactory;
import com.center.china.osgi.publics.file.read.ReaderFile;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.JSONTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.common.PageSeparateTools;
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.actionhelper.jsonimpl.OprMap;
import com.china.center.actionhelper.query.CommonQuery;
import com.china.center.actionhelper.query.QueryConfig;
import com.china.center.actionhelper.query.QueryItemBean;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.customer.bean.AssignApplyBean;
import com.china.center.oa.customer.bean.CustomerApplyBean;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.bean.CustomerCheckBean;
import com.china.center.oa.customer.bean.CustomerCheckItemBean;
import com.china.center.oa.customer.constant.CustomerConstant;
import com.china.center.oa.customer.dao.AssignApplyDAO;
import com.china.center.oa.customer.dao.ChangeLogDAO;
import com.china.center.oa.customer.dao.CustomerApplyDAO;
import com.china.center.oa.customer.dao.CustomerCheckDAO;
import com.china.center.oa.customer.dao.CustomerCheckItemDAO;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.customer.dao.CustomerHisDAO;
import com.china.center.oa.customer.dao.StafferVSCustomerDAO;
import com.china.center.oa.customer.facade.CustomerFacade;
import com.china.center.oa.customer.helper.CustomerHelper;
import com.china.center.oa.customer.manager.CustomerManager;
import com.china.center.oa.customer.vo.CustomerApplyVO;
import com.china.center.oa.customer.vo.CustomerHisVO;
import com.china.center.oa.customer.vo.CustomerVO;
import com.china.center.oa.customer.wrap.NotPayWrap;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.CityBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CityDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.PrincipalshipDAO;
import com.china.center.oa.publics.dao.ProvinceDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.facade.PublicFacade;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.CreateChartServiceImpl;
import com.china.center.tools.FileTools;
import com.china.center.tools.RequestDataStream;
import com.china.center.tools.SequenceTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


public class CustomerAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private CustomerApplyDAO customerApplyDAO = null;

    private CustomerDAO customerDAO = null;

    private AssignApplyDAO assignApplyDAO = null;

    private CustomerHisDAO customerHisDAO = null;

    private CustomerCheckDAO customerCheckDAO = null;

    private CustomerCheckItemDAO customerCheckItemDAO = null;

    private StafferVSCustomerDAO stafferVSCustomerDAO = null;

    private QueryConfig queryConfig = null;

    private LocationDAO locationDAO = null;

    private ChangeLogDAO changeLogDAO = null;

    private ProvinceDAO provinceDAO = null;

    private StafferDAO stafferDAO = null;

    private CityDAO cityDAO = null;

    private CustomerFacade customerFacade = null;

    private PublicFacade publicFacade = null;

    private CustomerManager customerManager = null;

    private UserManager userManager = null;

    private PrincipalshipDAO principalshipDAO = null;

    private static String QUERYCUSTOMER = "queryCustomer";

    private static String QUERYAPPLYCUSTOMER = "queryApplyCustomer";

    private static String QUERYHISCUSTOMER = "queryHisCustomer";

    private static String QUERYCANASSIGNCUSTOMER = "queryCanAssignCustomer";

    private static String QUERYASSIGNAPPLY = "queryAssignApply";

    private static String QUERYCUSTOMERASSIGN = "queryCustomerAssign";

    private static String QUERYCHECKHISCUSTOMER = "queryCheckHisCustomer";

    private static String QUERYCHANGELOG = "queryChangeLog";

    private static String QUERYAPPLYCUSTOMERFORCODE = "queryApplyCustomerForCode";

    private static String QUERYAPPLYCUSTOMERFORCREDIT = "queryApplyCustomerForCredit";

    private static String QUERYAPPLYCUSTOMERFORLEVER = "queryApplyCustomerForLever";

    private static String RPTQUERYALLCUSTOMER = "rptQueryAllCustomer";

    private static String RPTQUERYSELFCUSTOMER = "rptQuerySelfCustomer";

    /**
     * default constructor
     */
    public CustomerAction()
    {
    }

    /**
     * querySelfCustomer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward querySelfCustomer(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        String jsonstr = "";

        if (userManager.containAuth(user, AuthConstant.CUSTOMER_QUERY_LOCATION))
        {
            // 看到区域下所有的客户
            condtion.addCondition("CustomerBean.locationId", "=", user.getLocationId());

            ActionTools.processJSONQueryCondition(QUERYCUSTOMER, request, condtion);

            jsonstr = ActionTools.queryVOByJSONAndToString(QUERYCUSTOMER, request, condtion,
                this.customerDAO);
        }
        else
        {
            final String stafferId = user.getStafferId();

            ActionTools.processJSONQueryCondition(QUERYCUSTOMER, request, condtion);

            condtion.addCondition("order by CustomerBean.loginTime desc");

            jsonstr = ActionTools.querySelfBeanByJSONAndToString(QUERYCUSTOMER, request, condtion,
                new CommonQuery()
                {
                    public int getCount(String key, HttpServletRequest request,
                                        ConditionParse condition)
                    {
                        return customerDAO.countSelfCustomerByConstion(stafferId, condition);
                    }

                    public String getOrderPfix(String key, HttpServletRequest request)
                    {
                        return "CustomerBean";
                    }

                    public List queryResult(String key, HttpServletRequest request,
                                            ConditionParse queryCondition)
                    {
                        return customerDAO.querySelfCustomerByConstion(stafferId, PageSeparateTools
                            .getCondition(request, key), PageSeparateTools.getPageSeparate(request,
                            key));
                    }

                    public String getSortname(HttpServletRequest request)
                    {
                        return request.getParameter(ActionTools.SORTNAME);
                    }
                });
        }

        return JSONTools.writeResponse(response, jsonstr);
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
    public ActionForward rptQueryAllCustomer(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<CustomerBean> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setInnerCondition(request, condtion);

            int total = customerDAO.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYALLCUSTOMER);

            list = customerDAO.queryEntityBeansByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYALLCUSTOMER);

            list = customerDAO.queryEntityBeansByCondition(PageSeparateTools.getCondition(request,
                RPTQUERYALLCUSTOMER), PageSeparateTools.getPageSeparate(request,
                RPTQUERYALLCUSTOMER));
        }

        request.setAttribute("list", list);

        return mapping.findForward("rptQueryAllCustomer");
    }

    /**
     * 查询自己名下的客户
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQuerySelfCustomer(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String stafferId = request.getParameter("stafferId");

        List<CustomerBean> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setInnerCondition2(request, condtion);

            int total = customerDAO.countSelfCustomerByConstion(stafferId, condtion);

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYSELFCUSTOMER);

            list = customerDAO.querySelfCustomerByConstion(stafferId, condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYSELFCUSTOMER);

            list = customerDAO.queryEntityBeansByCondition(PageSeparateTools.getCondition(request,
                RPTQUERYSELFCUSTOMER), PageSeparateTools.getPageSeparate(request,
                RPTQUERYSELFCUSTOMER));
        }

        // 自动解密
        for (CustomerBean customerBean : list)
        {
            CustomerHelper.decryptCustomer(customerBean);
        }

        request.setAttribute("list", list);

        return mapping.findForward("rptQuerySelfCustomer");
    }

    /**
     * 总裁配置信用等级
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward interposeLever(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("cid");

        String newLever = request.getParameter("newcval");

        StafferBean bean = stafferDAO.find(id);

        if (bean == null)
        {
            return ActionTools.toError("职员不存在", "interposeLever", mapping, request);
        }

        bean.setLever(CommonTools.parseInt(newLever));

        try
        {
            User user = Helper.getUser(request);

            publicFacade.updateStafferLever(user, bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功设置职员的信用杠杆");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "修改失败:" + e.getMessage());
        }

        return mapping.findForward("interposeLever");
    }

    /**
     * @param request
     * @param condtion
     */
    private void setInnerCondition(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        String code = request.getParameter("code");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("name", "like", name);
        }

        if ( !StringTools.isNullOrNone(code))
        {
            condtion.addCondition("code", "like", code);
        }

        condtion.addCondition("order by creditVal desc");
    }

    /**
     * @param request
     * @param condtion
     */
    private void setInnerCondition2(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        String code = request.getParameter("code");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("CustomerBean.name", "like", name);
        }

        if ( !StringTools.isNullOrNone(code))
        {
            condtion.addCondition("CustomerBean.code", "like", code);
        }

        condtion.addCondition("order by CustomerBean.creditVal desc");
    }

    /**
     * 查询区域下的客户分布(用来回收客户的)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryCustomerAssign(ActionMapping mapping, ActionForm form,
                                             final HttpServletRequest request,
                                             HttpServletResponse response)
        throws ServletException
    {
        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        condtion.addCondition("t3.locationId", "=", user.getLocationId());

        ActionTools.processJSONQueryCondition(QUERYCUSTOMERASSIGN, request, condtion);

        String jsonstr = ActionTools.querySelfBeanByJSONAndToString(QUERYCUSTOMERASSIGN, request,
            condtion, new CommonQuery()
            {
                public int getCount(String key, HttpServletRequest request, ConditionParse condition)
                {
                    return customerDAO.countCustomerAssignByConstion(condition);
                }

                public String getOrderPfix(String key, HttpServletRequest request)
                {
                    return "t1";
                }

                public List queryResult(String key, HttpServletRequest request,
                                        ConditionParse queryCondition)
                {
                    return customerDAO.queryCustomerAssignByConstion(PageSeparateTools
                        .getCondition(request, key), PageSeparateTools
                        .getPageSeparate(request, key));
                }

                public String getSortname(HttpServletRequest request)
                {
                    return request.getParameter(ActionTools.SORTNAME);
                }
            });

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * 生成客户分布图
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryCustomerDistribute(ActionMapping mapping, ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        if ( !userManager.containAuth(user, AuthConstant.CUSTOMER_DISTRIBUTE))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

            return mapping.findForward("error");
        }

        String path = servlet.getServletContext().getRealPath("/") + "temp/";

        String ttemp = TimeTools.now("yyyy/MM/dd/HH/");

        path = path + ttemp;

        FileTools.mkdirs(path);

        String fileName = SequenceTools.getSequence() + ".png";

        List<LocationBean> locationList = locationDAO.listEntityBeans();

        double[] data = new double[locationList.size()];

        String[] columnKeys = new String[locationList.size()];

        int total = 0;

        for (int i = 0; i < locationList.size(); i++ )
        {
            data[i] = customerDAO.countByLocationId(locationList.get(i).getId());

            total += data[i];

            columnKeys[i] = locationList.get(i).getName();
        }

        CreateChartServiceImpl pm = new CreateChartServiceImpl(path);

        double[][] datas = new double[][] {data};

        String[] rowKeys = {"Customers:" + total};

        CategoryDataset dataset = pm.getBarData(datas, rowKeys, columnKeys);

        pm.createBarChart(dataset, "各分公司", "客户数量", "客户分布", fileName);

        List<String> urlList = new ArrayList<String>();

        urlList.add("../temp/" + ttemp + fileName);

        request.setAttribute("urlList", urlList);

        return mapping.findForward("queryCustomerDistribute");
    }

    /**
     * 查询日志
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryChangeLog(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYCHANGELOG, request, condtion);

        condtion.addCondition("order by logTime desc");

        String jsonstr = ActionTools.queryBeanByJSONAndToString(QUERYCHANGELOG, request, condtion,
            this.changeLogDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * 生成职员的客户分布图
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryStafferCustomerDistribute(ActionMapping mapping, ActionForm form,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        if ( !userManager.containAuth(user, AuthConstant.CUSTOMER_RECLAIM))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

            return mapping.findForward("error");
        }

        LocationBean lbean = locationDAO.find(user.getLocationId());

        if (lbean == null)
        {
            return ActionTools.toError("区域为空", mapping, request);
        }

        String url = createCustmoerDistribute(user.getLocationId(), lbean.getName());

        List<String> urlList = new ArrayList<String>();

        urlList.add(url);

        request.setAttribute("urlList", urlList);

        return mapping.findForward("queryCustomerDistribute");
    }

    /**
     * 生成所有区域的职员分布
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryAllStafferCustomerDistribute(ActionMapping mapping, ActionForm form,
                                                           HttpServletRequest request,
                                                           HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        if ( !userManager.containAuth(user, AuthConstant.CUSTOMER_DISTRIBUTE))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

            return mapping.findForward("error");
        }

        List<LocationBean> locationlist = locationDAO.listEntityBeans();

        List<String> urlList = new ArrayList<String>();

        for (LocationBean locationBean : locationlist)
        {
            urlList.add(createCustmoerDistribute(locationBean.getId(), locationBean.getName()));
        }

        request.setAttribute("urlList", urlList);

        return mapping.findForward("queryCustomerDistribute");
    }

    /**
     * 构建区域分布
     * 
     * @param locationId
     *            区域
     * @return 构建的路径
     */
    private String createCustmoerDistribute(String locationId, String locationName)
    {
        String path = servlet.getServletContext().getRealPath("/") + "temp/";

        String ttemp = TimeTools.now("yyyy/MM/dd/HH/");

        path = path + ttemp;

        FileTools.mkdirs(path);

        String fileName = SequenceTools.getSequence() + ".png";

        List<StafferBean> stafferList = stafferDAO.queryStafferByLocationId(locationId);

        List<OprMap> lop = new ArrayList<OprMap>();

        int total = 0;
        for (StafferBean stafferBean : stafferList)
        {
            int count = stafferVSCustomerDAO.countByStafferId(stafferBean.getId());

            if (count > 0)
            {
                OprMap mms = new OprMap();

                mms.setKey(stafferBean.getName());

                mms.setValue(count);

                lop.add(mms);
            }

            total += count;
        }

        double[] data = new double[lop.size()];

        String[] columnKeys = new String[lop.size()];

        for (int i = 0; i < lop.size(); i++ )
        {
            data[i] = (Integer)lop.get(i).getValue();

            columnKeys[i] = lop.get(i).getKey().toString();
        }

        CreateChartServiceImpl pm = new CreateChartServiceImpl(path);

        double[][] datas = new double[][] {data};

        String[] rowKeys = {"Customers:" + total};

        CategoryDataset dataset = pm.getBarData(datas, rowKeys, columnKeys);

        // 一个职员75
        int width = lop.size() * 60 > 900 ? lop.size() * 60 : 900;

        pm.createBarChart(dataset, locationName + "公司职员", "客户数量", locationName + "客户分布", fileName,
            width);

        return "../temp/" + ttemp + fileName;
    }

    /**
     * 查询历史修改
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryHisCustomer(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        if ( !userManager.containAuth(user, AuthConstant.CUSTOMER_QUERY_HIS))
        {
            return null;
        }

        // 只能看到自己的客户
        condtion.addCondition("CustomerHisBean.CUSTOMERID", "=", id);

        ActionTools.processJSONQueryCondition(QUERYHISCUSTOMER, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYHISCUSTOMER, request, condtion,
            this.customerHisDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * 查询需要核对的客户
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryCheckHisCustomer(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        // 只能看到自己的客户
        condtion.addIntCondition("CustomerHisBean.checkStatus", "=", CustomerConstant.HIS_CHECK_NO);

        ActionTools.processJSONQueryCondition(QUERYCHECKHISCUSTOMER, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYCHECKHISCUSTOMER, request,
            condtion, this.customerHisDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryApplyCustomer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryApplyCustomer(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        condtion.addIntCondition("CustomerApplyBean.opr", "<>", CustomerConstant.OPR_UPATE_CREDIT);

        condtion.addIntCondition("CustomerApplyBean.opr", "<>",
            CustomerConstant.OPR_UPATE_ASSIGNPER);

        if (userManager.containAuth(user, AuthConstant.CUSTOMER_CHECK))
        {
            condtion.addCondition("CustomerApplyBean.locationId", "=", user.getLocationId());

            condtion
                .addIntCondition("CustomerApplyBean.status", "=", CustomerConstant.STATUS_APPLY);
        }
        else
        {
            // 只能看到自己的客户
            condtion.addCondition("CustomerApplyBean.updaterId", "=", user.getStafferId());
        }

        ActionTools.processJSONQueryCondition(QUERYAPPLYCUSTOMER, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYAPPLYCUSTOMER, request,
            condtion, this.customerApplyDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryApplyCustomerForCredit
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryApplyCustomerForCredit(ActionMapping mapping, ActionForm form,
                                                     HttpServletRequest request,
                                                     HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        condtion.addIntCondition("CustomerApplyBean.opr", "=", CustomerConstant.OPR_UPATE_CREDIT);

        condtion.addCondition("CustomerApplyBean.locationId", "=", user.getLocationId());

        condtion.addIntCondition("CustomerApplyBean.status", "=", CustomerConstant.STATUS_APPLY);

        ActionTools.processJSONQueryCondition(QUERYAPPLYCUSTOMERFORCREDIT, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYAPPLYCUSTOMERFORCREDIT, request,
            condtion, this.customerApplyDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryApplyCustomerForAssignPer(利润分配申请查询,事业部填写--总裁审批)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryApplyCustomerForAssignPer(ActionMapping mapping, ActionForm form,
                                                        HttpServletRequest request,
                                                        HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion
            .addIntCondition("CustomerApplyBean.opr", "=", CustomerConstant.OPR_UPATE_ASSIGNPER);

        condtion.addIntCondition("CustomerApplyBean.status", "=", CustomerConstant.STATUS_APPLY);

        ActionTools.processJSONQueryCondition(QUERYAPPLYCUSTOMERFORLEVER, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYAPPLYCUSTOMERFORLEVER, request,
            condtion, this.customerApplyDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryApplyCustomerForCode
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryApplyCustomerForCode(ActionMapping mapping, ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("CustomerApplyBean.opr", "<>", CustomerConstant.OPR_UPATE_CREDIT);

        condtion.addIntCondition("CustomerApplyBean.opr", "<>",
            CustomerConstant.OPR_UPATE_ASSIGNPER);

        condtion
            .addIntCondition("CustomerApplyBean.status", "=", CustomerConstant.STATUS_WAIT_CODE);

        ActionTools.processJSONQueryCondition(QUERYAPPLYCUSTOMERFORCODE, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYAPPLYCUSTOMERFORCODE, request,
            condtion, this.customerApplyDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryCanAssignCustomer(可分配的)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryCanAssignCustomer(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        String selltype = request.getParameter("selltype");

        if (StringTools.isNullOrNone(selltype))
        {
            // 默认是拓展(终端全国共享,拓展是分公司共享)
            selltype = "1";

            condtion.addIntCondition("CustomerBean.selltype", "=", selltype);
        }

        if ("0".equals(selltype))
        {
            condtion.addIntCondition("CustomerBean.status", "=", CustomerConstant.REAL_STATUS_IDLE);
        }
        else
        {
            condtion.addCondition("CustomerBean.locationId", "=", user.getLocationId());

            condtion.addIntCondition("CustomerBean.status", "=", CustomerConstant.REAL_STATUS_IDLE);
        }

        // exportAssign(request);

        ActionTools.processJSONQueryCondition(QUERYCANASSIGNCUSTOMER, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYCANASSIGNCUSTOMER, request,
            condtion, this.customerDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * exportAssign
     * 
     * @param condtion
     */
    public ActionForward exportC(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
    {
        // User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        // condtion.addCondition("CustomerBean.locationId", "=", user.getLocationId());

        // condtion.addIntCondition("CustomerBean.status", "=", CustomerConstant.REAL_STATUS_IDLE);

        OutputStream out = null;

        String filenName = null;

        filenName = "c:/exportAssign_" + TimeTools.now("MMddHHmmss") + ".xls";

        WritableWorkbook wwb = null;

        WritableSheet ws = null;

        List<CustomerBean> list = this.customerDAO.queryEntityBeansByCondition(condtion);

        try
        {
            out = new FileOutputStream(filenName);

            // create a excel
            wwb = Workbook.createWorkbook(out);

            ws = wwb.createSheet("exportC", 0);

            int i = 0, j = 0;

            CustomerBean element = null;

            WritableFont font = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLUE);

            WritableFont font2 = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false,
                jxl.format.UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK);

            WritableCellFormat format = new WritableCellFormat(font);

            WritableCellFormat format2 = new WritableCellFormat(font2);

            ws.addCell(new Label(j++ , i, "客户名称", format2));
            ws.addCell(new Label(j++ , i, "客户编码", format));
            ws.addCell(new Label(j++ , i, "公司", format));
            ws.addCell(new Label(j++ , i, "联系人", format));
            ws.addCell(new Label(j++ , i, "手机", format));
            ws.addCell(new Label(j++ , i, "固话", format));
            ws.addCell(new Label(j++ , i, "地址", format));
            ws.addCell(new Label(j++ , i, "QQ", format));
            ws.addCell(new Label(j++ , i, "E-Mail", format));
            ws.addCell(new Label(j++ , i, "类型(0:终端 1:拓展)", format));

            for (Iterator iter = list.iterator(); iter.hasNext();)
            {
                element = (CustomerBean)iter.next();

                CustomerHelper.decryptCustomer(element);

                j = 0;
                i++ ;

                ws.addCell(new Label(j++ , i, element.getName()));
                ws.addCell(new Label(j++ , i, element.getCode()));

                ws.addCell(new Label(j++ , i, element.getCompany()));
                ws.addCell(new Label(j++ , i, element.getConnector()));
                ws.addCell(new Label(j++ , i, element.getHandphone()));
                ws.addCell(new Label(j++ , i, element.getTel()));
                ws.addCell(new Label(j++ , i, element.getAddress()));
                ws.addCell(new Label(j++ , i, element.getQq()));
                ws.addCell(new Label(j++ , i, element.getMail()));
                ws.addCell(new Label(j++ , i, String.valueOf(element.getSelltype())));

            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
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
     * preForAddApplyCustomer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddApplyCustomer(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
        throws ServletException
    {
        setSYB(request);

        return mapping.findForward("addCustomer");
    }

    private void setSYB(HttpServletRequest request)
    {
        // 查询事业部
        List<PrincipalshipBean> sybList = principalshipDAO.listSYBSubPrincipalship();

        request.setAttribute("sybList", sybList);
    }

    /**
     * addApplyCustomer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addApplyCustomer(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        CustomerApplyBean bean = new CustomerApplyBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            bean.setCreaterId(user.getStafferId());

            customerFacade.applyAddCustomer(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功申请客户:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "申请失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryApplyCustomer");
    }

    /**
     * 分配客户编码
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    @Deprecated
    public ActionForward assignApplyCustomerCode(ActionMapping mapping, ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);
        try
        {
            String id = request.getParameter("id");

            String code = request.getParameter("code");

            customerFacade.assignApplyCustomerCode(user.getId(), id, code.trim());

            request.setAttribute(KeyConstant.MESSAGE, "分配客户编码成功,客户审批结束");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "分配失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryApplyCustomerForCode");
    }

    /**
     * addUpdateApplyCustomer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addUpdateApplyCustomer(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        // 防止某些隐藏值被修改
        CustomerBean bean = customerDAO.find(id);

        if (bean == null)
        {
            ActionTools.toError("客户不存在", "queryApplyCustomer", mapping, request);
        }

        CustomerApplyBean apply = new CustomerApplyBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            BeanUtil.copyProperties(apply, bean);

            customerFacade.applyUpdateCustomer(user.getId(), apply);

            request.setAttribute(KeyConstant.MESSAGE, "成功申请修改客户:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "申请修改失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryApplyCustomer");
    }

    /**
     * addAssignPerApplyCustomer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addAssignPerApplyCustomer(ActionMapping mapping, ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("cid");

        // 防止某些隐藏值被修改
        CustomerBean bean = customerDAO.find(id);

        if (bean == null)
        {
            ActionTools.toError("客户不存在", "applyAssignPer", mapping, request);
        }

        CustomerApplyBean apply = new CustomerApplyBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            BeanUtil.copyProperties(apply, bean);

            customerFacade.applyUpdateCustomeAssignPer(user.getId(), apply);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作客户:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        return mapping.findForward("applyAssignPer");
    }

    /**
     * addUpdateApplyCustomer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addDelApplyCustomer(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        CustomerApplyBean apply = new CustomerApplyBean();

        AjaxResult ajax = new AjaxResult();

        try
        {
            CustomerBean bean = customerDAO.find(id);

            if (bean == null)
            {
                ajax.setError("客户不存在");

                return JSONTools.writeResponse(response, ajax);
            }

            BeanUtil.copyProperties(apply, bean);

            User user = Helper.getUser(request);

            customerFacade.applyDelCustomer(user.getId(), apply);

            ajax.setSuccess("成功申请删除客户:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("申请删除失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 手工全量同步客户分公司属性
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward synchronizationAllCustomerLocation(ActionMapping mapping, ActionForm form,
                                                            HttpServletRequest request,
                                                            HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        User user = Helper.getUser(request);

        try
        {
            if (userManager.containAuth(user, AuthConstant.CUSTOMER_SYNCHRONIZATION))
            {
                customerFacade.synchronizationAllCustomerLocation(user.getId());

                ajax.setSuccess("同步成功");
            }
            else
            {
                ajax.setError("没有权限");
            }
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("同步失败");
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * delApplyCustomer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward delApplyCustomer(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            customerFacade.delApplyCustomer(user.getId(), id);

            ajax.setSuccess("成功删除申请");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除申请失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * addUpdateApplyCustomer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findCustomer(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String update = request.getParameter("update");

        String linkId = request.getParameter("linkId");

        CommonTools.saveParamers(request);

        User user = Helper.getUser(request);

        CustomerVO vo = customerDAO.findVO(id);

        if (vo == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "客户不存在");

            return mapping.findForward("querySelfCustomer");
        }

        request.setAttribute("bean", vo);

        setSYB(request);

        try
        {
            // 修改，需要验证权限
            if ("1".equals(update))
            {
                boolean hasAuth = customerManager.hasCustomerAuth(user.getStafferId(), id);

                if ( !hasAuth)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有此客户的修改权限");

                    return mapping.findForward("querySelfCustomer");
                }

                CustomerHelper.decryptCustomer(vo);
            }
            else if ("0".equals(update))
            {
                if (userManager.containAuth(user, AuthConstant.CUSTOMER_OQUERY))
                {
                    CustomerHelper.decryptCustomer(vo);
                }
                else
                {
                    CustomerHelper.handleCustomer(vo);
                }
            }
            else if ("2".equals(update))
            {
                boolean hasAuth = customerManager.hasCustomerAuth(user.getStafferId(), id);

                if ( !hasAuth && !userManager.containAuth(user, AuthConstant.CUSTOMER_CHECK))
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

                    return mapping.findForward("queryApplyCustomer");
                }

                CustomerHelper.decryptCustomer(vo);
            }
            // process linkid
            else if ("3".equals(update))
            {
                if (StringTools.isNullOrNone(linkId))
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据不完备");

                    return mapping.findForward("error");
                }

                CustomerCheckItemBean item = customerCheckItemDAO.find(linkId);

                if (item == null)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据不完备");

                    return mapping.findForward("error");
                }

                if ( !item.getCustomerId().equals(id))
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据不完备");

                    return mapping.findForward("error");
                }

                CustomerCheckBean pbean = customerCheckDAO.find(item.getParentId());

                if (pbean == null)
                {
                    request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据不完备");

                    return mapping.findForward("error");
                }

                if ( !pbean.getApplyerId().equals(user.getStafferId()))
                {
                    boolean hasAuth = customerManager.hasCustomerAuth(user.getStafferId(), id);

                    if ( !hasAuth)
                    {
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "权限不足");

                        return mapping.findForward("error");
                    }
                }

                if (pbean.getBeginTime().compareTo(TimeTools.now()) > 0
                    || pbean.getEndTime().compareTo(TimeTools.now()) < 0)
                {
                    boolean hasAuth = customerManager.hasCustomerAuth(user.getStafferId(), id);

                    if ( !hasAuth)
                    {
                        request.setAttribute(KeyConstant.ERROR_MESSAGE, "超过指定的时间,无法操作");

                        return mapping.findForward("error");
                    }
                }

                CustomerHelper.decryptCustomer(vo);
            }
            else
            {
                if (userManager.containAuth(user, AuthConstant.CUSTOMER_OQUERY))
                {
                    CustomerHelper.decryptCustomer(vo);
                }
                else
                {
                    CustomerHelper.handleCustomer(vo);
                }
            }
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getMessage());

            return mapping.findForward("querySelfCustomer");
        }

        if ("1".equals(update))
        {
            return mapping.findForward("updateCustomer");
        }
        else
        {
            return mapping.findForward("detailCustomer");
        }
    }

    /**
     * addUpdateApplyCustomer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findApplyCustomer(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String updateCode = request.getParameter("updateCode");

        User user = Helper.getUser(request);

        setSYB(request);

        try
        {
            CustomerApplyVO vo = customerApplyDAO.findVO(id);

            if (vo == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "客户不存在");

                return mapping.findForward("querySelfCustomer");
            }

            request.setAttribute("bean", vo);

            // 修改客户编码
            if (userManager.containAuth(user, AuthConstant.CUSTOMER_ASSIGN_CODE)
                && "1".equals(updateCode))
            {
                CustomerHelper.handleCustomer(vo);

                request.setAttribute("updateCode", "1");

                return mapping.findForward("detailCustomer");
            }

            boolean isSelfApply = vo.getUpdaterId().equals(user.getStafferId());

            boolean hasAuth = customerManager.hasCustomerAuth(user.getStafferId(), id);

            if ( !isSelfApply && !hasAuth
                && !userManager.containAuth(user, AuthConstant.CUSTOMER_CHECK))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

                return mapping.findForward("queryApplyCustomer");
            }

            CustomerHelper.decryptCustomer(vo);
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getMessage());

            return mapping.findForward("queryApplyCustomer");
        }

        return mapping.findForward("detailCustomer");
    }

    /**
     * addUpdateApplyCustomer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findHisCustomer(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        setSYB(request);

        CustomerHisVO vo = customerHisDAO.findVO(id);

        if (vo == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "客户不存在");

            return mapping.findForward("querySelfCustomer");
        }

        CustomerHelper.decryptCustomer(vo);

        request.setAttribute("bean", vo);

        return mapping.findForward("detailCustomer");
    }

    /**
     * processApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward processApply(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String operation = request.getParameter("operation");

        String reson = request.getParameter("reson");

        String resultMsg = "";

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            if ("0".equals(operation))
            {
                customerFacade.passApplyCustomer(user.getId(), id);
            }

            if ("1".equals(operation))
            {
                customerFacade.rejectApplyCustomer(user.getId(), id, reson);
            }

            resultMsg = "成功处理申请";

            if ("2".equals(operation))
            {
                customerFacade.delApplyCustomer(user.getId(), id);

                resultMsg = "成功删除申请";
            }
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            resultMsg = "处理申请失败:" + e.getMessage();

            ajax.setError();
        }

        CommonTools.removeParamers(request);

        ajax.setMsg(resultMsg);

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * processApplyAssignPer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward processApplyAssignPer(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String operation = request.getParameter("operation");

        String resultMsg = "";

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            if ("0".equals(operation))
            {
                customerFacade.passApplyCustomerAssignPer(user.getId(), id);
            }

            if ("1".equals(operation))
            {
                customerFacade.rejectApplyCustomerAssignPer(user.getId(), id);
            }

            resultMsg = "成功处理申请";
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            resultMsg = "处理申请失败:" + e.getMessage();

            ajax.setError();
        }

        CommonTools.removeParamers(request);

        ajax.setMsg(resultMsg);

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * addAssignApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addAssignApply(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            String cids = request.getParameter("cids");

            String[] customerIds = cids.split("~");

            AssignApplyBean bean = new AssignApplyBean();

            bean.setUserId(user.getStafferId());

            bean.setStafferId(user.getStafferId());

            bean.setLocationid(user.getLocationId());

            for (String eachItem : customerIds)
            {
                if ( !StringTools.isNullOrNone(eachItem))
                {
                    bean.setCustomerId(eachItem.trim());

                    customerFacade.addAssignApply(user.getId(), bean);
                }
            }

            ajax.setSuccess("成功增加申请");
        }
        catch (Exception e)
        {
            _logger.warn(e, e);

            ajax.setError("申请失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 核对客户修改
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward checkHisCustomer(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            String cids = request.getParameter("cids");

            String[] customerIds = cids.split("~");

            for (String eachItem : customerIds)
            {
                if ( !StringTools.isNullOrNone(eachItem))
                {
                    customerFacade.checkHisCustomer(user.getId(), eachItem);
                }
            }

            ajax.setSuccess("成功核对客户");
        }
        catch (Exception e)
        {
            _logger.warn(e, e);

            ajax.setError("核对失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * queryAssignApply(查询可分配申请)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryAssignApply(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        condtion.addCondition("AssignApplyBean.locationId", "=", user.getLocationId());

        ActionTools.processJSONQueryCondition(QUERYASSIGNAPPLY, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYASSIGNAPPLY, request, condtion,
            this.assignApplyDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * 处理分配客户
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward processAssignApply(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            String cids = request.getParameter("cids");

            String opr = request.getParameter("opr");

            String[] customerIds = cids.split("~");

            for (String eachItem : customerIds)
            {
                if ( !StringTools.isNullOrNone(eachItem))
                {
                    if ("0".equals(opr))
                    {
                        customerFacade.passAssignApply(user.getId(), eachItem);
                    }

                    if ("1".equals(opr))
                    {
                        customerFacade.rejectAssignApply(user.getId(), eachItem);
                    }
                }
            }

            ajax.setSuccess("成功处理申请");
        }
        catch (Exception e)
        {
            _logger.warn(e, e);

            ajax.setError("处理申请失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 回收分配客户
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward reclaimAssignCustomer(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            String cids = request.getParameter("customerIds");

            String[] customerIds = cids.split("~");

            for (String eachItem : customerIds)
            {
                if ( !StringTools.isNullOrNone(eachItem))
                {
                    customerFacade.reclaimAssignCustomer(user.getId(), eachItem);
                }
            }

            ajax.setSuccess("成功回收客户");
        }
        catch (Exception e)
        {
            _logger.warn(e, e);

            ajax.setError("回收客户失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 回收职员的客户
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward reclaimStafferCustomer(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            String stafferId = request.getParameter("stafferId");

            String flag = request.getParameter("flag");

            customerFacade.reclaimStafferAssignCustomer(user.getId(), stafferId, CommonTools
                .parseInt(flag));

            ajax.setSuccess("成功回收职员客户");
        }
        catch (Exception e)
        {
            _logger.warn(e, e);

            ajax.setError("回收职员客户失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * popStafferQuery
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward popStafferCommonQuery(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        QueryItemBean query = queryConfig.findQueryCondition(QUERYCUSTOMER);

        if (query == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有配置查询,请核实");

            return mapping.findForward("queryStaffer");
        }

        request.setAttribute("query", query);

        return mapping.findForward("commonQuery");
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
        // TODO 应收客户导出在OutQueryDAOImpl里面实现了,转移到sail里面导出
        List<NotPayWrap> beanList = null;// outStatDAO.listNotPayWrap();

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
     * 导入
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward uploadCustomer(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        if ( !userManager.containAuth(user, AuthConstant.CUSTOMER_UPLOAD))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有权限");

            return mapping.findForward("uploadCustomer");
        }

        RequestDataStream rds = new RequestDataStream(request);

        try
        {
            rds.parser();
        }
        catch (Exception e1)
        {
            _logger.error(e1, e1);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "解析失败");

            return mapping.findForward("uploadCustomer");
        }

        int success = 0;

        int fault = 0;

        StringBuilder builder = new StringBuilder();

        if (rds.haveStream())
        {
            try
            {
                ReaderFile reader = ReadeFileFactory.getXLSReader();

                reader.readFile(rds.getUniqueInputStream());

                while (reader.hasNext())
                {
                    String[] obj = (String[])reader.next();

                    // 第一行忽略
                    if (reader.getCurrentLineNumber() == 1)
                    {
                        continue;
                    }

                    String stafferId = getStafferId(obj);

                    int currentNumber = reader.getCurrentLineNumber();

                    boolean addSucess = false;

                    if (obj.length >= 35)
                    {
                        addSucess = innerAdd(user, builder, obj, stafferId, currentNumber);
                    }
                    else
                    {
                        builder.append("第[" + currentNumber + "]错误:").append(
                            "数据长度不足34格,备注可以为空,但信息更新时间不可以为空").append("<br>");
                    }

                    if (addSucess)
                    {
                        success++ ;
                    }
                    else
                    {
                        fault++ ;
                    }
                }
            }
            catch (Exception e)
            {
                _logger.error(e, e);

                request.setAttribute(KeyConstant.ERROR_MESSAGE, "导入失败");

                return mapping.findForward("uploadCustomer");
            }
        }

        rds.close();

        StringBuilder result = new StringBuilder();

        result.append("导入成功:").append(success).append("条,失败:").append(fault).append("条<br>");

        result.append(builder.toString());

        request.setAttribute(KeyConstant.MESSAGE, result.toString());

        return mapping.findForward("uploadCustomer");
    }

    /**
     * handleStafferId
     * 
     * @param obj
     * @param stafferId
     * @return
     */
    private String getStafferId(String[] obj)
    {
        String stafferId = "";

        if ( !StringTools.isNullOrNone(obj[1]))
        {
            StafferBean sb = stafferDAO.findyStafferByName(obj[1]);

            if (sb != null)
            {
                stafferId = sb.getId();
            }
        }

        return stafferId;
    }

    /**
     * @param user
     * @param builder
     * @param obj
     * @param stafferId
     * @param currentNumber
     * @return
     */
    private boolean innerAdd(User user, StringBuilder builder, String[] obj, String stafferId,
                             int currentNumber)
    {
        boolean addSucess = false;

        try
        {
            CustomerBean bean = createCustomerBean(obj);

            // 设置是否被使用
            if ( !StringTools.isNullOrNone(stafferId))
            {
                bean.setStatus(CustomerConstant.REAL_STATUS_USED);
            }

            customerManager.addCustomer(user, bean, stafferId);

            addSucess = true;
        }
        catch (Exception e)
        {
            addSucess = false;

            builder
                .append("<font color=red>第[" + currentNumber + "]行错误:")
                .append(e.getMessage())
                .append("</font><br>");
        }

        return addSucess;
    }

    private CustomerBean createCustomerBean(String[] obj)
        throws MYException
    {
        CustomerBean bean = new CustomerBean();

        int i = 2;

        bean.setFormtype(CommonTools.parseInt(obj[i++ ]));// 客户来源
        bean.setBeginConnectTime(obj[i++ ]);// 开始联系客户时间
        bean.setCompany(obj[i++ ]);// 客户公司

        String name = obj[i++ ];

        if (StringTools.isNullOrNone(name))
        {
            throw new MYException("客户名称为空");
        }

        bean.setName(name.trim());// 客户名称

        String code = obj[i++ ];

        if (StringTools.isNullOrNone(code))
        {
            throw new MYException("客户编码为空");
        }

        bean.setCode(code.trim());// 客户编码

        bean.setSelltype(CommonTools.parseInt(obj[i++ ]));// 客户类型
        bean.setProtype(CommonTools.parseInt(obj[i++ ]));// 客户分类1
        bean.setNewtype(CommonTools.parseInt(obj[i++ ]));// 客户分类2
        bean.setMtype(CommonTools.parseInt(obj[i++ ]));// 客户性质
        bean.setHtype(CommonTools.parseInt(obj[i++ ]));// 行业划分
        bean.setQqtype(CommonTools.parseInt(obj[i++ ]));// 客户等级
        bean.setRtype(CommonTools.parseInt(obj[i++ ]));// 开发进程
        bean.setBlog(CommonTools.parseInt(obj[i++ ]));// 有无历史成交
        bean.setCard(CommonTools.parseInt(obj[i++ ]));// 有无名片
        i++ ;// 省

        String cityName = obj[i++ ];

        CityBean city = cityDAO.findCityByName(cityName);

        if (city == null)
        {
            throw new MYException("地市[%s]不存在", cityName);
        }

        bean.setProvinceId(city.getParentId());
        bean.setCityId(city.getId());

        bean.setConnector(obj[i++ ]);// 联系人
        bean.setPost(obj[i++ ]);// 职务
        bean.setHandphone(obj[i++ ]);// 手机
        bean.setTel(obj[i++ ]);// 座机
        bean.setFax(obj[i++ ]);// 传真
        bean.setQq(obj[i++ ]);// QQ
        bean.setMsn(obj[i++ ]);// MSN
        bean.setMail(obj[i++ ]);// E-mail
        bean.setWeb(obj[i++ ]);// 网址
        bean.setAddress(obj[i++ ]);// 地址
        bean.setPostcode(obj[i++ ]);// 邮编
        bean.setBirthday(obj[i++ ]);// 生日
        bean.setBank(obj[i++ ]);// 开户银行
        bean.setAccounts(obj[i++ ]);// 帐号
        bean.setDutycode(obj[i++ ]);// 税号
        bean.setFlowcom(obj[i++ ]);// 指定的物流公司
        bean.setLoginTime(obj[i++ ]);// 信息更新时间

        if (obj.length >= 36)
        {
            bean.setDescription(obj[i++ ]);
        }

        return bean;
    }

    /**
     * @return the customerApplyDAO
     */
    public CustomerApplyDAO getCustomerApplyDAO()
    {
        return customerApplyDAO;
    }

    /**
     * @param customerApplyDAO
     *            the customerApplyDAO to set
     */
    public void setCustomerApplyDAO(CustomerApplyDAO customerApplyDAO)
    {
        this.customerApplyDAO = customerApplyDAO;
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
     * @return the customerHisDAO
     */
    public CustomerHisDAO getCustomerHisDAO()
    {
        return customerHisDAO;
    }

    /**
     * @param customerHisDAO
     *            the customerHisDAO to set
     */
    public void setCustomerHisDAO(CustomerHisDAO customerHisDAO)
    {
        this.customerHisDAO = customerHisDAO;
    }

    /**
     * @return the queryConfig
     */
    public QueryConfig getQueryConfig()
    {
        return queryConfig;
    }

    /**
     * @param queryConfig
     *            the queryConfig to set
     */
    public void setQueryConfig(QueryConfig queryConfig)
    {
        this.queryConfig = queryConfig;
    }

    /**
     * @return the customerFacade
     */
    public CustomerFacade getCustomerFacade()
    {
        return customerFacade;
    }

    /**
     * @param customerFacade
     *            the customerFacade to set
     */
    public void setCustomerFacade(CustomerFacade customerFacade)
    {
        this.customerFacade = customerFacade;
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
     * @return the stafferVSCustomerDAO
     */
    public StafferVSCustomerDAO getStafferVSCustomerDAO()
    {
        return stafferVSCustomerDAO;
    }

    /**
     * @param stafferVSCustomerDAO
     *            the stafferVSCustomerDAO to set
     */
    public void setStafferVSCustomerDAO(StafferVSCustomerDAO stafferVSCustomerDAO)
    {
        this.stafferVSCustomerDAO = stafferVSCustomerDAO;
    }

    /**
     * @return the provinceDAO
     */
    public ProvinceDAO getProvinceDAO()
    {
        return provinceDAO;
    }

    /**
     * @param provinceDAO
     *            the provinceDAO to set
     */
    public void setProvinceDAO(ProvinceDAO provinceDAO)
    {
        this.provinceDAO = provinceDAO;
    }

    /**
     * @return the cityDAO
     */
    public CityDAO getCityDAO()
    {
        return cityDAO;
    }

    /**
     * @param cityDAO
     *            the cityDAO to set
     */
    public void setCityDAO(CityDAO cityDAO)
    {
        this.cityDAO = cityDAO;
    }

    /**
     * @return the assignApplyDAO
     */
    public AssignApplyDAO getAssignApplyDAO()
    {
        return assignApplyDAO;
    }

    /**
     * @param assignApplyDAO
     *            the assignApplyDAO to set
     */
    public void setAssignApplyDAO(AssignApplyDAO assignApplyDAO)
    {
        this.assignApplyDAO = assignApplyDAO;
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
     * @return the changeLogDAO
     */
    public ChangeLogDAO getChangeLogDAO()
    {
        return changeLogDAO;
    }

    /**
     * @param changeLogDAO
     *            the changeLogDAO to set
     */
    public void setChangeLogDAO(ChangeLogDAO changeLogDAO)
    {
        this.changeLogDAO = changeLogDAO;
    }

    /**
     * @return the customerCheckDAO
     */
    public CustomerCheckDAO getCustomerCheckDAO()
    {
        return customerCheckDAO;
    }

    /**
     * @param customerCheckDAO
     *            the customerCheckDAO to set
     */
    public void setCustomerCheckDAO(CustomerCheckDAO customerCheckDAO)
    {
        this.customerCheckDAO = customerCheckDAO;
    }

    /**
     * @return the customerCheckItemDAO
     */
    public CustomerCheckItemDAO getCustomerCheckItemDAO()
    {
        return customerCheckItemDAO;
    }

    /**
     * @param customerCheckItemDAO
     *            the customerCheckItemDAO to set
     */
    public void setCustomerCheckItemDAO(CustomerCheckItemDAO customerCheckItemDAO)
    {
        this.customerCheckItemDAO = customerCheckItemDAO;
    }

    /**
     * @return the principalshipDAO
     */
    public PrincipalshipDAO getPrincipalshipDAO()
    {
        return principalshipDAO;
    }

    /**
     * @param principalshipDAO
     *            the principalshipDAO to set
     */
    public void setPrincipalshipDAO(PrincipalshipDAO principalshipDAO)
    {
        this.principalshipDAO = principalshipDAO;
    }

    /**
     * @return the publicFacade
     */
    public PublicFacade getPublicFacade()
    {
        return publicFacade;
    }

    /**
     * @param publicFacade
     *            the publicFacade to set
     */
    public void setPublicFacade(PublicFacade publicFacade)
    {
        this.publicFacade = publicFacade;
    }

}
