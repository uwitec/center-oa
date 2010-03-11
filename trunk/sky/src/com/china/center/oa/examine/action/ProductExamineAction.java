/**
 * File Name: ProductExamineAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.action;


import java.util.ArrayList;
import java.util.Iterator;
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

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.common.PageSeparateTools;
import com.china.center.common.json.AjaxResult;
import com.china.center.common.query.CommonQuery;
import com.china.center.oa.constant.AuthConstant;
import com.china.center.oa.constant.ExamineConstant;
import com.china.center.oa.constant.StafferConstant;
import com.china.center.oa.examine.bean.ProductExamineBean;
import com.china.center.oa.examine.bean.ProductExamineItemBean;
import com.china.center.oa.examine.dao.ProductCityExamineItemDAO;
import com.china.center.oa.examine.dao.ProductExamineDAO;
import com.china.center.oa.examine.helper.ExamineHelper;
import com.china.center.oa.examine.vo.ProductCityExamineItemVO;
import com.china.center.oa.examine.vo.ProductExamineVO;
import com.china.center.oa.facade.ExamineFacade;
import com.china.center.oa.helper.Helper;
import com.china.center.oa.product.manager.ProductStatManager;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.tools.ActionTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.JSONTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * ProductExamineAction
 * 
 * @author zhuzhu
 * @version 2009-2-14
 * @see ProductExamineAction
 * @since 1.0
 */
public class ProductExamineAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private ExamineFacade examineFacade = null;

    private ProductExamineDAO productExamineDAO = null;

    private ProductCityExamineItemDAO productCityExamineItemDAO = null;

    private static String QUERYPRODUCTEXAMINE = "queryProductExamine";

    private UserManager userManager = null;

    private StafferDAO stafferDAO = null;

    private LocationDAO locationDAO = null;

    private ProductStatManager productStatManager = null;

    public ProductExamineAction()
    {}

    /**
     * 查询产品考核
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryProductExamine(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
        throws ServletException
    {
        // productStatManager.statProduct();
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        final User user = Helper.getUser(request);

        String jsonstr = "";

        if ( !userManager.containAuth(user, AuthConstant.EXAMINE_OPR))
        {
            condtion.addCondition("ProductExamineBean.locationId", "=", user.getLocationId());

            condtion.addCondition("ProductExamineBean.status", ">",
                ExamineConstant.EXAMINE_STATUS_INIT);

            condtion.addCondition("order by ProductExamineBean.logTime desc");

            // 从条件里面过滤用户
            jsonstr = ActionTools.querySelfBeanByJSONAndToString(QUERYPRODUCTEXAMINE, request,
                condtion, new CommonQuery()
                {
                    public int getCount(String key, HttpServletRequest request,
                                        ConditionParse condition)
                    {
                        return productExamineDAO.countProductExamineByCondition(
                            user.getStafferId(), condition);
                    }

                    public String getOrderPfix(String key, HttpServletRequest request)
                    {
                        return "ProductExamineBean";
                    }

                    public List queryResult(String key, HttpServletRequest request,
                                            ConditionParse queryCondition)
                    {
                        return productExamineDAO.queryProductExamineByConstion(
                            user.getStafferId(), PageSeparateTools.getCondition(request, key),
                            PageSeparateTools.getPageSeparate(request, key));
                    }

                    public String getSortname(HttpServletRequest request)
                    {
                        return request.getParameter(ActionTools.SORTNAME);
                    }
                });
        }
        else
        {
            condtion.addCondition("order by ProductExamineBean.logTime desc");

            ActionTools.processJSONQueryCondition(QUERYPRODUCTEXAMINE, request, condtion);

            jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPRODUCTEXAMINE, request, condtion,
                this.productExamineDAO);
        }

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * preForAddExamine
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddProductExamine(ActionMapping mapping, ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
        throws ServletException
    {
        handleStafferList(request);

        List<LocationBean> locationList = locationDAO.listEntityBeans();

        request.setAttribute("locationList", locationList);

        return mapping.findForward("addProductExamine");
    }

    /**
     * addExamine
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addProductExamine(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ProductExamineBean bean = new ProductExamineBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            bean.setCreaterId(user.getStafferId());

            createProductExamine(request, bean);

            String stafferIds = request.getParameter("stafferId");

            String[] ss = stafferIds.split(",");

            examineFacade.addProductExamine(user.getId(), bean, ss);

            request.setAttribute(KeyConstant.MESSAGE, "成功保存产品考核基本信息");

        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "保存考核基本信息失败:" + e.getMessage());

            return mapping.findForward("queryProductExamine");
        }

        String forward = request.getParameter("forward");

        if ("1".equals(forward))
        {
            CommonTools.removeParamers(request);

            request.setAttribute("pid", bean.getId());

            return queryProductExamineItem(mapping, form, request, response);
        }

        return mapping.findForward("queryProductExamine");
    }

    /**
     * 查询考核项
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryProductExamineItem(ActionMapping mapping, ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("pid");

        if (StringTools.isNullOrNone(id))
        {
            Object tep = request.getAttribute("pid");

            if (tep != null)
            {
                id = tep.toString();
            }
        }

        ProductExamineVO examine = productExamineDAO.findVO(id);

        if (examine == null)
        {
            return ActionTools.toError("考核不存在", mapping, request);
        }

        if (ExamineHelper.isReadonly(examine.getStatus()))
        {
            request.setAttribute("readonly", 1);
        }

        List<ProductCityExamineItemVO> list = productCityExamineItemDAO.queryEntityVOsByFK(id);

        request.setAttribute("newList", list);

        request.setAttribute("examine", examine);

        CommonTools.saveParamers(request);

        String look = request.getParameter("look");

        if ("1".equals(look))
        {
            if ( !ExamineHelper.isReadonly(examine.getStatus()))
            {
                return ActionTools.toError("考核没有提交", mapping, request);
            }

            return mapping.findForward("configProductExamine2");
        }
        else
        {
            return mapping.findForward("configProductExamine2");
        }
    }

    /**
     * 保存新客户的考核配置
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward configProductExamineItem(ActionMapping mapping, ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("pid");

        ProductExamineBean examine = productExamineDAO.find(id);

        if (examine == null)
        {
            return ActionTools.toError("考核不存在", mapping, request);
        }

        User user = Helper.getUser(request);

        List<ProductExamineItemBean> nList = new ArrayList<ProductExamineItemBean>();

        ActionForward tem = createItem(mapping, request, nList);

        if (tem != null)
        {
            return tem;
        }

        try
        {
            examineFacade.configProductExamine(user.getId(), id, nList);

            request.setAttribute(KeyConstant.MESSAGE, "成功保存考核信息");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "保存考核信息失败:" + e.getMessage());
        }

        request.setAttribute("pid", id);

        return queryProductExamineItem(mapping, form, request, response);
    }

    /**
     * 详细显示基本信息
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findProductExamine(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        ProductExamineVO bean = productExamineDAO.findVO(id);

        if (bean == null)
        {
            return ActionTools.toError("考核不存在", mapping, request);
        }

        handleStafferList(request);

        request.setAttribute("bean", bean);

        List<LocationBean> locationList = locationDAO.listEntityBeans();

        request.setAttribute("locationList", locationList);

        return mapping.findForward("detailProductExamine");
    }

    /**
     * 构建考核子项
     * 
     * @param mapping
     * @param request
     * @param nList
     * @return
     */
    private ActionForward createItem(ActionMapping mapping, HttpServletRequest request,
                                     List<ProductExamineItemBean> nList)
    {
        String[] stafferIds = request.getParameterValues("stafferIds");

        String[] realValues = request.getParameterValues("realValue");

        if (stafferIds.length != realValues.length)
        {
            return ActionTools.toError("数据不完备,请重新操作", mapping, request);
        }

        for (int i = 0; i < stafferIds.length; i++ )
        {
            ProductExamineItemBean item = new ProductExamineItemBean();

            item.setPlanValue(CommonTools.parseInt(realValues[i]));

            item.setStafferId(stafferIds[i]);

            nList.add(item);
        }

        return null;
    }

    /**
     * 删除考核
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward delProductExamine(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            examineFacade.delProductExamine(user.getId(), id);

            ajax.setSuccess("成功删除考核");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除考核失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 提交考核
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward submitProductExamine(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            examineFacade.submitProductExamine(user.getId(), id);

            ajax.setSuccess("成功提交考核");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("提交考核失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 构建实体bean
     * 
     * @param request
     * @param bean
     */
    private void createProductExamine(HttpServletRequest request, ProductExamineBean bean)
    {
        bean.setBeginTime(bean.getBeginTime() + " 00:00:00");

        String endTime = TimeTools.getSpecialDateStringByDays(bean.getBeginTime(),
            (bean.getMonth() * 30) + 1);

        bean.setEndTime(endTime);
    }

    /**
     * 处理职员(只有终端和拓展的职员)
     * 
     * @param request
     */
    private void handleStafferList(HttpServletRequest request)
    {
        User user = Helper.getUser(request);

        List<StafferBean> stafferList = stafferDAO.queryStafferByLocationId(user.getLocationId());

        for (Iterator iterator = stafferList.iterator(); iterator.hasNext();)
        {
            StafferBean stafferBean = (StafferBean)iterator.next();

            if (stafferBean.getExamType() != StafferConstant.EXAMTYPE_EXPAND
                && stafferBean.getExamType() != StafferConstant.EXAMTYPE_TERMINAL)
            {
                iterator.remove();
            }
        }

        request.setAttribute("stafferList", stafferList);
    }

    /**
     * @return the examineFacade
     */
    public ExamineFacade getExamineFacade()
    {
        return examineFacade;
    }

    /**
     * @param examineFacade
     *            the examineFacade to set
     */
    public void setExamineFacade(ExamineFacade examineFacade)
    {
        this.examineFacade = examineFacade;
    }

    /**
     * @return the productExamineDAO
     */
    public ProductExamineDAO getProductExamineDAO()
    {
        return productExamineDAO;
    }

    /**
     * @param productExamineDAO
     *            the productExamineDAO to set
     */
    public void setProductExamineDAO(ProductExamineDAO productExamineDAO)
    {
        this.productExamineDAO = productExamineDAO;
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

    public ProductCityExamineItemDAO getProductCityExamineItemDAO()
    {
        return productCityExamineItemDAO;
    }

    public void setProductCityExamineItemDAO(ProductCityExamineItemDAO productCityExamineItemDAO)
    {
        this.productCityExamineItemDAO = productCityExamineItemDAO;
    }

    public ProductStatManager getProductStatManager()
    {
        return productStatManager;
    }

    public void setProductStatManager(ProductStatManager productStatManager)
    {
        this.productStatManager = productStatManager;
    }
}
