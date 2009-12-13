/**
 * File Name: StafferAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.action;


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
import com.china.center.common.json.AjaxResult;
import com.china.center.common.query.QueryConfig;
import com.china.center.oa.constant.CustomerConstant;
import com.china.center.oa.customer.bean.ProviderBean;
import com.china.center.oa.customer.bean.ProviderHisBean;
import com.china.center.oa.customer.dao.ProviderDAO;
import com.china.center.oa.customer.dao.ProviderHisDAO;
import com.china.center.oa.facade.CustomerFacade;
import com.china.center.oa.helper.Helper;
import com.china.center.oa.publics.User;
import com.china.center.tools.ActionTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.JSONTools;
import com.china.center.tools.StringTools;


public class ProviderAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private QueryConfig queryConfig = null;

    private ProviderDAO providerDAO = null;

    private CustomerFacade customerFacade = null;

    private ProviderHisDAO providerHisDAO = null;

    private static String QUERYPROVIDER = "queryProvider";

    private static String QUERYCHECKHISPROVIDER = "queryCheckHisProvider";

    /**
     * default constructor
     */
    public ProviderAction()
    {}

    /**
     * 查询供应商
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryProvider(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYPROVIDER, request, condtion);

        String jsonstr = ActionTools.queryBeanByJSONAndToString(QUERYPROVIDER, request, condtion,
            this.providerDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * 增加供应商
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addProvider(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ProviderBean bean = new ProviderBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            customerFacade.addProvider(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加供应商:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加供应商失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryProvider");
    }

    /**
     * 修改供应商
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateProvider(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ProviderBean bean = new ProviderBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            customerFacade.updateProvider(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "修改供应商成功:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "修改供应商失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryProvider");
    }

    /**
     * 删除供应商
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward delProvider(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            customerFacade.delProvider(user.getId(), id);

            ajax.setSuccess("成功删除供应商");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除供应商失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 查看供应商详细
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findProvider(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String update = request.getParameter("update");

        // User user = Helper.getUser(request);

        ProviderBean vo = providerDAO.find(id);

        if (vo == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "供应商不存在");

            return mapping.findForward("querySelfCustomer");
        }

        request.setAttribute("bean", vo);

        if ("1".equals(update))
        {
            return mapping.findForward("updateProvider");
        }

        // detailProvider
        return mapping.findForward("detailProvider");
    }

    /**
     * findHisProvider
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findHisProvider(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        ProviderHisBean vo = providerHisDAO.find(id);

        if (vo == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "供应商不存在");

            return mapping.findForward("querySelfCustomer");
        }

        request.setAttribute("bean", vo);

        // detailProvider
        return mapping.findForward("detailProvider");
    }

    /**
     * queryCheckHisCustomer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryCheckHisProvider(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("ProviderHisBean.checkStatus", "=", CustomerConstant.HIS_CHECK_NO);

        ActionTools.processJSONQueryCondition(QUERYCHECKHISPROVIDER, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYCHECKHISPROVIDER, request,
            condtion, this.providerHisDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * checkHisProvider
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward checkHisProvider(ActionMapping mapping, ActionForm form,
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
                    customerFacade.checkHisProvider(user.getId(), eachItem);
                }
            }

            ajax.setSuccess("成功核对供应商");
        }
        catch (Exception e)
        {
            _logger.warn(e, e);

            ajax.setError("核对失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
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
     * @return the providerHisDAO
     */
    public ProviderHisDAO getProviderHisDAO()
    {
        return providerHisDAO;
    }

    /**
     * @param providerHisDAO
     *            the providerHisDAO to set
     */
    public void setProviderHisDAO(ProviderHisDAO providerHisDAO)
    {
        this.providerHisDAO = providerHisDAO;
    }
}
