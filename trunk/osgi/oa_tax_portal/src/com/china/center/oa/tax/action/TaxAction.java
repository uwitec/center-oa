/**
 * File Name: TaxAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-31<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.action;


import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.actionhelper.query.HandleResult;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.BankBean;
import com.china.center.oa.finance.dao.BankDAO;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.bean.TaxTypeBean;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.oa.tax.dao.TaxTypeDAO;
import com.china.center.oa.tax.facade.TaxFacade;
import com.china.center.oa.tax.vo.TaxVO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;


/**
 * TaxAction
 * 
 * @author ZHUZHU
 * @version 2011-1-31
 * @see TaxAction
 * @since 1.0
 */
public class TaxAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private TaxDAO taxDAO = null;

    private TaxTypeDAO taxTypeDAO = null;

    private BankDAO bankDAO = null;

    private TaxFacade taxFacade = null;

    private static final String QUERYTAX = "queryTax";

    /**
     * default constructor
     */
    public TaxAction()
    {
    }

    /**
     * queryTax
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryTax(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                  HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYTAX, request, condtion);

        condtion.addCondition("order by TaxBean.code asc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYTAX, request, condtion, this.taxDAO,
            new HandleResult<TaxVO>()
            {
                public void handle(TaxVO obj)
                {
                    obj.getOther();
                }
            });

        return JSONTools.writeResponse(response, jsonstr);
    }

    public ActionForward preForAddTax(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                      HttpServletResponse response)
        throws ServletException
    {
        List<TaxTypeBean> taxTypeList = taxTypeDAO.listEntityBeans();

        request.setAttribute("taxTypeList", taxTypeList);

        List<BankBean> bankList = bankDAO.listEntityBeans("order by name");

        request.setAttribute("bankList", bankList);

        return mapping.findForward("addTax");
    }

    /**
     * addTax
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addTax(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse response)
        throws ServletException
    {
        TaxBean bean = new TaxBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            taxFacade.addTaxBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryTax");
    }

    /**
     * deleteTax
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteTax(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                   HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            taxFacade.deleteTaxBean(user.getId(), id);

            ajax.setSuccess("成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("操作失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * @return the taxDAO
     */
    public TaxDAO getTaxDAO()
    {
        return taxDAO;
    }

    /**
     * @param taxDAO
     *            the taxDAO to set
     */
    public void setTaxDAO(TaxDAO taxDAO)
    {
        this.taxDAO = taxDAO;
    }

    /**
     * @return the taxFacade
     */
    public TaxFacade getTaxFacade()
    {
        return taxFacade;
    }

    /**
     * @param taxFacade
     *            the taxFacade to set
     */
    public void setTaxFacade(TaxFacade taxFacade)
    {
        this.taxFacade = taxFacade;
    }

    /**
     * @return the taxTypeDAO
     */
    public TaxTypeDAO getTaxTypeDAO()
    {
        return taxTypeDAO;
    }

    /**
     * @param taxTypeDAO
     *            the taxTypeDAO to set
     */
    public void setTaxTypeDAO(TaxTypeDAO taxTypeDAO)
    {
        this.taxTypeDAO = taxTypeDAO;
    }

    /**
     * @return the bankDAO
     */
    public BankDAO getBankDAO()
    {
        return bankDAO;
    }

    /**
     * @param bankDAO
     *            the bankDAO to set
     */
    public void setBankDAO(BankDAO bankDAO)
    {
        this.bankDAO = bankDAO;
    }
}
