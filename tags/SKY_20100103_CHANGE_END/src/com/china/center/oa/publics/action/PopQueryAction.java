/**
 * File Name: DownFileAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-5<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.action;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.common.ConditionParse;
import com.china.center.common.PageSeparateTools;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.constant.FlowConstant;
import com.china.center.oa.constant.StafferConstant;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.examine.bean.CustomerExamineLogBean;
import com.china.center.oa.examine.dao.CustomerExamineLogDAO;
import com.china.center.oa.flow.bean.FlowDefineBean;
import com.china.center.oa.flow.bean.TemplateFileBean;
import com.china.center.oa.flow.dao.FlowDefineDAO;
import com.china.center.oa.flow.dao.TemplateFileDAO;
import com.china.center.oa.group.bean.GroupBean;
import com.china.center.oa.group.dao.GroupDAO;
import com.china.center.oa.group.dao.GroupVSStafferDAO;
import com.china.center.oa.group.vs.GroupVSStafferBean;
import com.china.center.oa.helper.Helper;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.LogDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.vo.LogVO;
import com.china.center.oa.publics.vo.StafferVO;
import com.china.center.tools.CommonTools;
import com.china.center.tools.HTTPTools;
import com.china.center.tools.StringTools;
import com.china.centet.yongyin.constant.Constant;


/**
 * DownFileAction
 * 
 * @author zhuzhu
 * @version 2009-2-5
 * @see PopQueryAction
 * @since 1.0
 */
public class PopQueryAction extends DispatchAction
{
    private ProductDAO productDAO = null;

    private CustomerDAO customerDAO = null;

    private StafferDAO stafferDAO = null;

    private LocationDAO locationDAO = null;

    private GroupDAO groupDAO = null;

    private LogDAO logDAO = null;

    private FlowDefineDAO flowDefineDAO = null;

    private GroupVSStafferDAO groupVSStafferDAO = null;

    private TemplateFileDAO templateFileDAO = null;

    private CustomerExamineLogDAO customerExamineLogDAO = null;

    /**
     * defined the edit server name
     */
    private String editServerName = "webdav";

    private static String RPTQUERYPRODCUT = "rptQueryProdcut";

    private static String RPTQUERYCUSTOMER = "rptQueryCustomer";

    private static String RPTQUERYALLCUSTOMER = "rptQueryAllCustomer";

    private static String RPTQUERYSUBFLOW = "rptQuerySubFlow";

    private static String RPTQUERYSTAFFER = "rptQueryStaffer";

    private static String RPTQUERYTEMPLATEFILE = "rptQueryTemplateFile";

    /**
     * /** 查询产品
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryProdcut(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<ProductBean> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setInnerCondition(request, condtion);

            int total = productDAO.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYPRODCUT);

            list = productDAO.queryEntityBeansByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYPRODCUT);

            list = productDAO.queryEntityBeansByCondition(PageSeparateTools.getCondition(request,
                RPTQUERYPRODCUT), PageSeparateTools.getPageSeparate(request, RPTQUERYPRODCUT));
        }

        request.setAttribute("productList", list);

        return mapping.findForward("rptQueryProdcut");
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
    }

    /**
     * @param request
     * @param condtion
     */
    private void setSubFlowCondition(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("name", "like", name);
        }

        condtion.addIntCondition("parentType", "=", FlowConstant.FLOW_PARENTTYPE_SUB);

        condtion.addIntCondition("status", "=", FlowConstant.FLOW_STATUS_REALSE);

    }

    /**
     * @param request
     * @param condtion
     */
    private void setStafferInnerCondition(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        String code = request.getParameter("code");

        String locationId = request.getParameter("locationId");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("StafferBean.name", "like", name);
        }

        if ( !StringTools.isNullOrNone(code))
        {
            condtion.addCondition("StafferBean.code", "like", code);
        }

        if ( !StringTools.isNullOrNone(locationId))
        {
            condtion.addCondition("StafferBean.locationId", "=", locationId);
        }
    }

    /**
     * setTemplateFileInnerCondition
     * 
     * @param request
     * @param condtion
     */
    private void setTemplateFileInnerCondition(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("name", "like", name);
        }
    }

    /**
     * 职员的查询
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryStaffer(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<StafferVO> list = null;

        List<LocationBean> locationList = locationDAO.listEntityBeans();

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            // 过滤废弃的
            condtion.addIntCondition("StafferBean.status", "=", StafferConstant.STATUS_COMMON);

            setStafferInnerCondition(request, condtion);

            int total = stafferDAO.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYSTAFFER);

            list = stafferDAO.queryEntityVOsByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYSTAFFER);

            list = stafferDAO.queryEntityVOsByCondition(PageSeparateTools.getCondition(request,
                RPTQUERYSTAFFER), PageSeparateTools.getPageSeparate(request, RPTQUERYSTAFFER));
        }

        request.setAttribute("beanList", list);

        request.setAttribute("locationList", locationList);

        return mapping.findForward("rptQueryStaffer");
    }

    /**
     * 职员的查询(根据群组或者个人或者其他，不分页)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryStaffer2(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        int mode = CommonTools.parseInt(request.getParameter("mode"));

        String pid = request.getParameter("pid");

        List<StafferVO> list = new ArrayList<StafferVO>();

        if (mode == FlowConstant.FLOW_PLUGIN_STAFFER)
        {
            list.add(stafferDAO.findVO(pid));
        }

        if (mode == FlowConstant.FLOW_PLUGIN_GROUP)
        {
            List<GroupVSStafferBean> vs = groupVSStafferDAO.queryEntityBeansByFK(pid);

            for (GroupVSStafferBean groupVSStafferBean : vs)
            {
                list.add(stafferDAO.findVO(groupVSStafferBean.getStafferId()));
            }
        }

        request.setAttribute("beanList", list);

        return mapping.findForward("rptQueryStaffer2");
    }

    /**
     * 群组内职员查询
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryGroupMember(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String pid = request.getParameter("pid");

        GroupBean group = groupDAO.find(pid);

        List<StafferVO> list = new ArrayList<StafferVO>();

        List<GroupVSStafferBean> vs = groupVSStafferDAO.queryEntityBeansByFK(pid);

        for (GroupVSStafferBean groupVSStafferBean : vs)
        {
            list.add(stafferDAO.findVO(groupVSStafferBean.getStafferId()));
        }

        request.setAttribute("beanList", list);

        request.setAttribute("group", group);

        return mapping.findForward("rptQueryGroupMember");
    }

    /**
     * query log
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryLog(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String fk = request.getParameter("fk");

        List<LogVO> list = logDAO.queryEntityVOsByFK(fk);

        Collections.sort(list, new Comparator<LogVO>()
        {

            public int compare(LogVO o1, LogVO o2)
            {
                return o1.getLogTime().compareTo(o2.getLogTime());
            }
        });

        request.setAttribute("beanList", list);

        return mapping.findForward("rptQueryLog");
    }

    /**
     * 流程模板的查询
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryTemplateFile(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String urlPfix = HTTPTools.getHTTPURL(request) + this.editServerName;

        List<TemplateFileBean> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setTemplateFileInnerCondition(request, condtion);

            int total = templateFileDAO.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYTEMPLATEFILE);

            list = templateFileDAO.queryEntityVOsByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYTEMPLATEFILE);

            list = templateFileDAO.queryEntityVOsByCondition(PageSeparateTools.getCondition(
                request, RPTQUERYTEMPLATEFILE), PageSeparateTools.getPageSeparate(request,
                RPTQUERYTEMPLATEFILE));
        }

        request.setAttribute("beanList", list);

        request.setAttribute("urlPfix", urlPfix);

        return mapping.findForward("rptQueryTemplateFile");
    }

    /**
     * query group
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryGroup(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        Set<GroupBean> result = new HashSet<GroupBean>();

        User user = Helper.getUser(request);

        result.addAll(groupDAO.listPublicGroup());

        result.addAll(groupDAO.queryEntityBeansByFK(user.getStafferId()));

        request.setAttribute("beanList", result);

        return mapping.findForward("rptQueryGroup");
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
    public ActionForward rptQueryCustomer(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<CustomerBean> list = null;

        User user = Helper.getUser(request);

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setInnerCondition(request, condtion);

            int total = customerDAO.countSelfCustomerByConstion(user.getStafferId(), condtion);

            PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYCUSTOMER);

            list = customerDAO.querySelfCustomerByConstion(user.getStafferId(), condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYCUSTOMER);

            list = customerDAO.querySelfCustomerByConstion(user.getStafferId(),
                PageSeparateTools.getCondition(request, RPTQUERYCUSTOMER),
                PageSeparateTools.getPageSeparate(request, RPTQUERYCUSTOMER));
        }

        request.setAttribute("list", list);

        return mapping.findForward("rptQueryCustomer");
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
                                             HttpServletRequest request,
                                             HttpServletResponse reponse)
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

            PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

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
     * rptQuerySubFlow
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQuerySubFlow(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<FlowDefineBean> list = null;

        final String queryKey = RPTQUERYSUBFLOW;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setSubFlowCondition(request, condtion);

            int total = flowDefineDAO.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, Constant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, queryKey);

            list = flowDefineDAO.queryEntityBeansByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, queryKey);

            list = flowDefineDAO.queryEntityBeansByCondition(PageSeparateTools.getCondition(
                request, queryKey), PageSeparateTools.getPageSeparate(request, queryKey));
        }

        request.setAttribute("list", list);

        return mapping.findForward("rptQuerySubFlow");
    }

    /**
     * 日志
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward popExamineLog(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String fk = request.getParameter("id");

        List<CustomerExamineLogBean> list = customerExamineLogDAO.queryEntityBeansByFK(fk);

        request.setAttribute("list", list);

        return mapping.findForward("popExamineLog");
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
     * @return the customerExamineLogDAO
     */
    public CustomerExamineLogDAO getCustomerExamineLogDAO()
    {
        return customerExamineLogDAO;
    }

    /**
     * @param customerExamineLogDAO
     *            the customerExamineLogDAO to set
     */
    public void setCustomerExamineLogDAO(CustomerExamineLogDAO customerExamineLogDAO)
    {
        this.customerExamineLogDAO = customerExamineLogDAO;
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
     * @return the groupDAO
     */
    public GroupDAO getGroupDAO()
    {
        return groupDAO;
    }

    /**
     * @param groupDAO
     *            the groupDAO to set
     */
    public void setGroupDAO(GroupDAO groupDAO)
    {
        this.groupDAO = groupDAO;
    }

    /**
     * @return the templateFileDAO
     */
    public TemplateFileDAO getTemplateFileDAO()
    {
        return templateFileDAO;
    }

    /**
     * @param templateFileDAO
     *            the templateFileDAO to set
     */
    public void setTemplateFileDAO(TemplateFileDAO templateFileDAO)
    {
        this.templateFileDAO = templateFileDAO;
    }

    /**
     * @return the editServerName
     */
    public String getEditServerName()
    {
        return editServerName;
    }

    /**
     * @param editServerName
     *            the editServerName to set
     */
    public void setEditServerName(String editServerName)
    {
        this.editServerName = editServerName;
    }

    /**
     * @return the groupVSStafferDAO
     */
    public GroupVSStafferDAO getGroupVSStafferDAO()
    {
        return groupVSStafferDAO;
    }

    /**
     * @param groupVSStafferDAO
     *            the groupVSStafferDAO to set
     */
    public void setGroupVSStafferDAO(GroupVSStafferDAO groupVSStafferDAO)
    {
        this.groupVSStafferDAO = groupVSStafferDAO;
    }

    /**
     * @return the flowDefineDAO
     */
    public FlowDefineDAO getFlowDefineDAO()
    {
        return flowDefineDAO;
    }

    /**
     * @param flowDefineDAO
     *            the flowDefineDAO to set
     */
    public void setFlowDefineDAO(FlowDefineDAO flowDefineDAO)
    {
        this.flowDefineDAO = flowDefineDAO;
    }

    /**
     * @return the logDAO
     */
    public LogDAO getLogDAO()
    {
        return logDAO;
    }

    /**
     * @param logDAO
     *            the logDAO to set
     */
    public void setLogDAO(LogDAO logDAO)
    {
        this.logDAO = logDAO;
    }
}
