package com.china.center.oa.product.action;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.common.ConditionParse;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.ProductStatDAO;
import com.china.center.tools.ActionTools;
import com.china.center.tools.JSONTools;


/**
 * @author Administrator
 */
public class ProductAction extends DispatchAction
{
    private ProductDAO productDAO = null;

    private ProductStatDAO productStatDAO = null;

    private static String QUERYPRODUCT = "queryProduct";

    private static String QUERYPRODUCTSTAT = "queryProductStat";

    /**
     * default constructor
     */
    public ProductAction()
    {}

    /**
     * ²éÑ¯²úÆ·
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
}
