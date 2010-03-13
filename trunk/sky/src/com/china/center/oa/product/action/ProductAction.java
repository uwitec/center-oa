package com.china.center.oa.product.action;


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
import com.china.center.oa.constant.ProductConstant;
import com.china.center.oa.facade.ProductFacade;
import com.china.center.oa.helper.Helper;
import com.china.center.oa.product.bean.OutOrderBean;
import com.china.center.oa.product.dao.OutOrderDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.ProductStatDAO;
import com.china.center.oa.product.manager.ProductStatManager;
import com.china.center.oa.publics.User;
import com.china.center.tools.ActionTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.JSONTools;
import com.china.center.tools.TimeTools;


/**
 * @author Administrator
 */
public class ProductAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private ProductDAO productDAO = null;

    private ProductStatDAO productStatDAO = null;

    private OutOrderDAO outOrderDAO = null;

    private ProductStatManager productStatManager = null;

    private ProductFacade productFacade = null;

    private static String QUERYPRODUCT = "queryProduct";

    private static String QUERYOUTORDER = "queryOutOrder";

    private static String QUERYPRODUCTSTAT = "queryProductStat";

    /**
     * default constructor
     */
    public ProductAction()
    {}

    /**
     * 查询产品
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryProduct(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYPRODUCT, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPRODUCT, request, condtion,
            this.productDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryProductStat
     * 
     * @param mapping
     * @param forms
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryProductStat(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYPRODUCTSTAT, request, condtion);

        condtion.addCondition("order by ProductStatBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPRODUCTSTAT, request, condtion,
            this.productStatDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryOutOrder
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryOutOrder(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYOUTORDER, request, condtion);

        condtion.addCondition("order by OutOrderBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYOUTORDER, request, condtion,
            this.outOrderDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * addOutOrder
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addOutOrder(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        OutOrderBean bean = new OutOrderBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            bean.setStatus(ProductConstant.ORDER_STATUS_COMMON);

            bean.setLogTime(TimeTools.now());

            bean.setStafferId(user.getStafferId());

            productFacade.addOutOrder(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加订货");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加订货失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryOutOrder");
    }

    /**
     * cancelOutOrder
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward cancelOutOrder(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            productFacade.cancleOutOrder(user.getId(), id);

            ajax.setSuccess("成功结束订货");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("结束订货失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
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
     * @return the productStatDAO
     */
    public ProductStatDAO getProductStatDAO()
    {
        return productStatDAO;
    }

    /**
     * @param productStatDAO
     *            the productStatDAO to set
     */
    public void setProductStatDAO(ProductStatDAO productStatDAO)
    {
        this.productStatDAO = productStatDAO;
    }

    /**
     * @return the outOrderDAO
     */
    public OutOrderDAO getOutOrderDAO()
    {
        return outOrderDAO;
    }

    /**
     * @param outOrderDAO
     *            the outOrderDAO to set
     */
    public void setOutOrderDAO(OutOrderDAO outOrderDAO)
    {
        this.outOrderDAO = outOrderDAO;
    }

    /**
     * @return the productFacade
     */
    public ProductFacade getProductFacade()
    {
        return productFacade;
    }

    /**
     * @param productFacade
     *            the productFacade to set
     */
    public void setProductFacade(ProductFacade productFacade)
    {
        this.productFacade = productFacade;
    }

    /**
     * @return the productStatManager
     */
    public ProductStatManager getProductStatManager()
    {
        return productStatManager;
    }

    /**
     * @param productStatManager
     *            the productStatManager to set
     */
    public void setProductStatManager(ProductStatManager productStatManager)
    {
        this.productStatManager = productStatManager;
    }
}
