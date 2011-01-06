/**
 * File Name: InvoiceinsAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.portal.action;


import java.util.ArrayList;
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
import com.china.center.actionhelper.jsonimpl.JSONArray;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.finance.bean.InvoiceinsBean;
import com.china.center.oa.finance.bean.InvoiceinsItemBean;
import com.china.center.oa.finance.dao.InsVSOutDAO;
import com.china.center.oa.finance.dao.InvoiceinsDAO;
import com.china.center.oa.finance.dao.InvoiceinsItemDAO;
import com.china.center.oa.finance.facade.FinanceFacade;
import com.china.center.oa.finance.manager.InvoiceinsManager;
import com.china.center.oa.finance.vo.InvoiceinsVO;
import com.china.center.oa.finance.vs.InsVSOutBean;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.InvoiceBean;
import com.china.center.oa.publics.bean.ShowBean;
import com.china.center.oa.publics.constant.InvoiceConstant;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.InvoiceDAO;
import com.china.center.oa.publics.dao.ShowDAO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;


/**
 * InvoiceinsAction
 * 
 * @author ZHUZHU
 * @version 2011-1-2
 * @see InvoiceinsAction
 * @since 3.0
 */
public class InvoiceinsAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private FinanceFacade financeFacade = null;

    private InvoiceinsDAO invoiceinsDAO = null;

    private InvoiceinsItemDAO invoiceinsItemDAO = null;

    private InsVSOutDAO insVSOutDAO = null;

    private DutyDAO dutyDAO = null;

    private ShowDAO showDAO = null;

    private InvoiceDAO invoiceDAO = null;

    private InvoiceinsManager invoiceinsManager = null;

    private static final String QUERYINVOICEINS = "queryInvoiceins";

    /**
     * default constructor
     */
    public InvoiceinsAction()
    {
    }

    /**
     * preForAddInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddInvoiceins(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
        throws ServletException
    {
        prepare(request);

        // 查询开单品名
        List<ShowBean> showList = showDAO.listEntityBeans();

        JSONArray shows = new JSONArray(showList, true);

        request.setAttribute("showJSON", shows.toString());

        return mapping.findForward("addInvoiceins");
    }

    private void prepare(HttpServletRequest request)
    {
        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList", dutyList);

        ConditionParse condition = new ConditionParse();

        condition.addWhereStr();

        condition.addIntCondition("InvoiceBean.forward", "=", InvoiceConstant.INVOICE_FORWARD_OUT);

        List<InvoiceBean> invoiceList = invoiceDAO.queryEntityBeansByCondition(condition);

        request.setAttribute("invoiceList", invoiceList);
    }

    /**
     * findInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findInvoiceins(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        InvoiceinsVO bean = invoiceinsManager.findVO(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据异常,请重新操作");

            return mapping.findForward("queryBank");
        }

        prepare(request);

        request.setAttribute("bean", bean);

        return mapping.findForward("detailInvoiceins");
    }

    /**
     * queryInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryInvoiceins(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYINVOICEINS, request, condtion);

        User user = Helper.getUser(request);

        condtion.addCommonCondition("InvoiceinsBean.locationId", "=", user.getLocationId());

        condtion.addCondition("order by InvoiceinsBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYINVOICEINS, request, condtion,
            this.invoiceinsDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * addInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addInvoiceins(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        try
        {
            InvoiceinsBean bean = createIns(request);

            User user = Helper.getUser(request);

            financeFacade.addInvoiceinsBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryInvoiceins");
    }

    /**
     * createIns
     * 
     * @param request
     * @return
     */
    private InvoiceinsBean createIns(HttpServletRequest request)
        throws MYException
    {
        InvoiceinsBean bean = new InvoiceinsBean();

        BeanUtil.getBean(bean, request);

        String[] showIds = request.getParameterValues("showId");
        String[] amounts = request.getParameterValues("amount");
        String[] prices = request.getParameterValues("price");

        List<InvoiceinsItemBean> itemList = new ArrayList<InvoiceinsItemBean>();

        for (int i = 0; i < showIds.length; i++ )
        {
            if (StringTools.isNullOrNone(showIds[i]))
            {
                break;
            }

            InvoiceinsItemBean item = new InvoiceinsItemBean();

            item.setShowId(showIds[i]);
            item.setShowName(showDAO.find(showIds[i]).getName());
            item.setAmount(MathTools.parseInt(amounts[i]));
            item.setPrice(MathTools.parseDouble(prices[i]));

            itemList.add(item);
        }

        if (ListTools.isEmptyOrNull(itemList))
        {
            throw new MYException("没有开票项");
        }

        double total = 0.0d;
        for (InvoiceinsItemBean invoiceinsItemBean : itemList)
        {
            total += invoiceinsItemBean.getAmount() * invoiceinsItemBean.getPrice();
        }

        bean.setMoneys(total);

        bean.setItemList(itemList);

        User user = Helper.getUser(request);

        bean.setLocationId(user.getLocationId());

        bean.setStafferId(user.getStafferId());

        String outId = request.getParameter("outId");

        if ( !StringTools.isNullOrNone(outId))
        {
            List<InsVSOutBean> vsList = new ArrayList<InsVSOutBean>();

            String[] split = outId.split(";");

            for (int i = 0; i < split.length; i++ )
            {
                if ( !StringTools.isNullOrNone(split[i]))
                {
                    InsVSOutBean vs = new InsVSOutBean();

                    vs.setOutId(split[i]);

                    vsList.add(vs);
                }
            }

            bean.setVsList(vsList);
        }

        return bean;
    }

    /**
     * deleteInvoiceins
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteInvoiceins(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            financeFacade.deleteInvoiceinsBean(user.getId(), id);

            ajax.setSuccess("成功删除");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * @return the financeFacade
     */
    public FinanceFacade getFinanceFacade()
    {
        return financeFacade;
    }

    /**
     * @param financeFacade
     *            the financeFacade to set
     */
    public void setFinanceFacade(FinanceFacade financeFacade)
    {
        this.financeFacade = financeFacade;
    }

    /**
     * @return the invoiceinsDAO
     */
    public InvoiceinsDAO getInvoiceinsDAO()
    {
        return invoiceinsDAO;
    }

    /**
     * @param invoiceinsDAO
     *            the invoiceinsDAO to set
     */
    public void setInvoiceinsDAO(InvoiceinsDAO invoiceinsDAO)
    {
        this.invoiceinsDAO = invoiceinsDAO;
    }

    /**
     * @return the invoiceinsItemDAO
     */
    public InvoiceinsItemDAO getInvoiceinsItemDAO()
    {
        return invoiceinsItemDAO;
    }

    /**
     * @param invoiceinsItemDAO
     *            the invoiceinsItemDAO to set
     */
    public void setInvoiceinsItemDAO(InvoiceinsItemDAO invoiceinsItemDAO)
    {
        this.invoiceinsItemDAO = invoiceinsItemDAO;
    }

    /**
     * @return the insVSOutDAO
     */
    public InsVSOutDAO getInsVSOutDAO()
    {
        return insVSOutDAO;
    }

    /**
     * @param insVSOutDAO
     *            the insVSOutDAO to set
     */
    public void setInsVSOutDAO(InsVSOutDAO insVSOutDAO)
    {
        this.insVSOutDAO = insVSOutDAO;
    }

    /**
     * @return the invoiceinsManager
     */
    public InvoiceinsManager getInvoiceinsManager()
    {
        return invoiceinsManager;
    }

    /**
     * @param invoiceinsManager
     *            the invoiceinsManager to set
     */
    public void setInvoiceinsManager(InvoiceinsManager invoiceinsManager)
    {
        this.invoiceinsManager = invoiceinsManager;
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
}
