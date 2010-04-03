/**
 * File Name: WapAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-3-31<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.tools.CommonTools;
import com.china.center.tools.RegularExpress;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.Helper;
import com.china.centet.yongyin.bean.BaseBean;
import com.china.centet.yongyin.bean.OutBean;
import com.china.centet.yongyin.bean.Product;
import com.china.centet.yongyin.bean.ProductAmount;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.dao.CustomerDAO;
import com.china.centet.yongyin.dao.OutDAO;
import com.china.centet.yongyin.dao.ProductDAO;


/**
 * WapAction
 * 
 * @author ZHUZHU
 * @version 2010-3-31
 * @see WapOutAction
 * @since 1.0
 */
public class WapOutAction extends DispatchAction
{
    private OutDAO outDAO = null;

    private ProductDAO productDAO = null;

    private CustomerDAO customerDAO = null;

    /**
     * toSec
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
    public ActionForward step2(ActionMapping mapping, ActionForm actionForm,
                               HttpServletRequest request, HttpServletResponse response)
    {
        Map gmap = (Map)request.getSession().getAttribute("gwapmap");

        if (gmap == null)
        {
            gmap = new HashMap();

            request.getSession().setAttribute("gwapmap", gmap);
        }

        String location = request.getParameter("location");
        String outType = request.getParameter("outType");
        String reday = request.getParameter("reday");
        String arriveDate = request.getParameter("arriveDate");
        String description = request.getParameter("description");

        if ("1".equals(outType))
        {
            reday = "90";
        }

        gmap.put("location", location);
        gmap.put("outType", outType);
        gmap.put("reday", reday);
        gmap.put("arriveDate", arriveDate);
        gmap.put("description", description);

        if (StringTools.isNullOrNone(location) || StringTools.isNullOrNone(outType)
            || StringTools.isNullOrNone(reday) || StringTools.isNullOrNone(arriveDate))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请完整填写数据");

            return mapping.findForward("step1Error");
        }

        // 格式校验
        if ( !RegularExpress.isNumber(reday))
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "回款天数必须是数字");

            return mapping.findForward("step1Error");
        }

        if ( !RegularExpress.isNumber(arriveDate) || arriveDate.length() != 8
            || TimeTools.getDateByFormat(arriveDate, "yyyyMMdd") == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "回款日期格式[年月日]:20100101");

            return mapping.findForward("step1Error");
        }

        return queryNearestCustmoer(mapping, actionForm, request, response);
    }

    /**
     * 最近交易的客户(获取前5个)
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
    public ActionForward queryNearestCustmoer(ActionMapping mapping, ActionForm actionForm,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
    {
        User user = Helper.getUser(request);

        List<OutBean> nearestOut = getNearestOut(request, user);

        List<CustomerBean> resultCustomerList = new ArrayList();

        Map<String, String> distinct = new HashMap();

        for (OutBean outBean : nearestOut)
        {
            if (customerDAO.isCustomerOwner(user.getStafferId(), outBean.getCustomerId())
                && !distinct.containsKey(outBean.getCustomerId()))
            {
                CustomerBean each = new CustomerBean();
                each.setId(outBean.getCustomerId());
                each.setName(outBean.getCustomerName());
                resultCustomerList.add(each);

                distinct.put(outBean.getCustomerId(), outBean.getCustomerId());
            }
        }

        request.setAttribute("resultCustomerList", resultCustomerList);

        return mapping.findForward("step2");
    }

    /**
     * 最近买的5个产品
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
    public ActionForward queryNearestProduct(ActionMapping mapping, ActionForm actionForm,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
    {
        User user = Helper.getUser(request);

        Map gmap = (Map)request.getSession().getAttribute("gwapmap");

        String locationId = gmap.get("location").toString();

        List<OutBean> nearestOut = getNearestOut(request, user);

        List<Product> resultProductList = new ArrayList();

        Map<String, String> distinct = new HashMap();

        for (OutBean outBean : nearestOut)
        {
            List<BaseBean> baseList = outDAO.queryBaseByOutFullId(outBean.getFullId());

            for (BaseBean baseBean : baseList)
            {
                Product each = productDAO.findProductById(baseBean.getProductId());

                ProductAmount amount = productDAO.findProductAmount(each.getId(), locationId);

                if (each != null && amount != null
                    && !distinct.containsKey(baseBean.getProductId()) && distinct.size() < 10)
                {
                    each.setNum(amount.getNum());

                    resultProductList.add(each);

                    distinct.put(baseBean.getProductId(), baseBean.getProductId());
                }
            }
        }

        request.setAttribute("resultProductList", resultProductList);

        return mapping.findForward("step3");
    }

    /**
     * getNearestOut
     * 
     * @param request
     * @param user
     * @return
     */
    private List<OutBean> getNearestOut(HttpServletRequest request, User user)
    {
        List<OutBean> nearestOut = (List<OutBean>)request.getSession().getAttribute("nearestOut");

        if (nearestOut == null)
        {
            PageSeparate page = new PageSeparate(5, 5);

            ConditionParse condtion = new ConditionParse();
            condtion.addWhereStr();
            condtion.addCondition("stafferId", "=", user.getStafferId());
            condtion.addIntCondition("type", "=", 0);
            condtion.addIntCondition("outType", "=", 0);

            condtion.addCondition("order by outTime desc");

            nearestOut = outDAO.queryOutBeanByCondtion(condtion, page);
        }

        request.getSession().setAttribute("nearestOut", nearestOut);

        return nearestOut;
    }

    /**
     * queryCustomer
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
    public ActionForward queryCustomer(ActionMapping mapping, ActionForm actionForm,
                                       HttpServletRequest request, HttpServletResponse response)
    {
        CommonTools.saveParamers(request);

        ConditionParse condtion = setCondition(request);

        List<CustomerBean> resultCustomerList = customerDAO.queryCustomerByCondtion(condtion, 10);

        request.setAttribute("resultCustomerList", resultCustomerList);

        return mapping.findForward("step2");
    }

    /**
     * setCondition
     * 
     * @param request
     * @return
     */
    private ConditionParse setCondition(HttpServletRequest request)
    {
        String cname = request.getParameter("cname");
        String ccode = request.getParameter("ccode");

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        if ( !StringTools.isNullOrNone(cname))
        {
            condtion.addCondition("t1.name", "like", cname);
        }

        if ( !StringTools.isNullOrNone(ccode))
        {
            condtion.addCondition("t1.code", "like", ccode);
        }

        User user = Helper.getUser(request);

        // 只能看到自己的客户
        condtion.addCondition("t2.stafferId", "=", user.getStafferId());

        return condtion;
    }

    /**
     * setConditionFroProduct
     * 
     * @param request
     * @return
     */
    private ConditionParse setConditionFroProduct(HttpServletRequest request)
    {
        String pname = request.getParameter("pname");
        String pcode = request.getParameter("pcode");

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        if ( !StringTools.isNullOrNone(pname))
        {
            condtion.addCondition("t1.name", "like", pname);
        }

        if ( !StringTools.isNullOrNone(pcode))
        {
            condtion.addCondition("t1.code", "like", pcode);
        }

        Map gmap = (Map)request.getSession().getAttribute("gwapmap");

        String locationId = gmap.get("location").toString();

        condtion.addCondition("t2.LOCATIONID", "=", locationId);

        return condtion;
    }

    /**
     * queryProduct
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
    public ActionForward queryProduct(ActionMapping mapping, ActionForm actionForm,
                                      HttpServletRequest request, HttpServletResponse response)
    {
        CommonTools.saveParamers(request);

        Set<String> productSet = getProductSet(request);

        String[] parameterValues = request.getParameterValues("productNames");

        String[] hasSelectProductNames = request.getParameterValues("hasSelectProductNames");

        productSet.clear();

        if (hasSelectProductNames != null)
        {
            for (String each : hasSelectProductNames)
            {
                productSet.add(each);
            }
        }

        if (parameterValues != null)
        {
            for (String string : parameterValues)
            {
                productSet.add(string);
            }
        }

        ConditionParse condtion = setConditionFroProduct(request);

        PageSeparate page = new PageSeparate(10, 10);

        // 只查询前10个
        List<ProductAmount> resultProductAmountList = productDAO.queryProductAmountByCondtionAndSeparate(
            condtion, page);

        List<Product> resultProductList = new ArrayList();

        for (ProductAmount productAmount : resultProductAmountList)
        {
            Product each = new Product();
            each.setId(productAmount.getProductId());
            each.setName(productAmount.getProductName());
            each.setNum(productAmount.getNum());
            each.setCode(productAmount.getProductCode());

            resultProductList.add(each);
        }

        request.setAttribute("resultProductList", resultProductList);

        return mapping.findForward("step3");
    }

    /**
     * getProductSet
     * 
     * @param request
     * @return
     */
    private Set<String> getProductSet(HttpServletRequest request)
    {
        Map gmap = (Map)request.getSession().getAttribute("gwapmap");

        Set<String> productSet = (Set<String>)gmap.get("productSet");

        if (productSet == null)
        {
            productSet = new HashSet();

            gmap.put("productSet", productSet);
        }

        return productSet;
    }

    /**
     * 产品选择页面
     * 
     * @param mapping
     * @param actionForm
     * @param request
     * @param response
     * @return
     */
    public ActionForward step3(ActionMapping mapping, ActionForm actionForm,
                               HttpServletRequest request, HttpServletResponse response)
    {
        // 这里可以不选择的
        String customerName = request.getParameter("customerName");

        Map gmap = (Map)request.getSession().getAttribute("gwapmap");

        if (gmap == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "请完整填写数据");

            return mapping.findForward("step1Error");
        }

        String outType = gmap.get("outType").toString();

        if ("0".equals(outType))
        {
            if (StringTools.isNullOrNone(customerName))
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "请选择客户");

                return mapping.findForward("step2Error");
            }
        }
        else
        {
            // 个人领样
            gmap.put("customerName", "");
        }

        gmap.put("customerName", customerName);

        return queryNearestProduct(mapping, actionForm, request, response);
    }

    public ActionForward toFor(ActionMapping mapping, ActionForm actionForm,
                               HttpServletRequest request, HttpServletResponse response)
    {
        String[] pros = request.getParameterValues("pros");

        Map gmap = (Map)request.getSession().getAttribute("gmap");

        if (gmap == null)
        {
            gmap = new HashMap();

            request.getSession().setAttribute("gmap", gmap);
        }

        gmap.put("pros", pros);

        return mapping.findForward("total");
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
}
