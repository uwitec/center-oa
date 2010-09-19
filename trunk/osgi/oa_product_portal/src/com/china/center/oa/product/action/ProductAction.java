/**
 * File Name: FlowAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-26<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.action;


import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.center.china.osgi.config.ConfigLoader;
import com.center.china.osgi.publics.User;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.JSONTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.common.PageSeparateTools;
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.bean.ProviderBean;
import com.china.center.oa.product.constant.ProductConstant;
import com.china.center.oa.product.dao.ProductCombinationDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.ProductVSLocationDAO;
import com.china.center.oa.product.dao.ProviderDAO;
import com.china.center.oa.product.facade.ProductFacade;
import com.china.center.oa.product.manager.ProductManager;
import com.china.center.oa.product.vo.ProductCombinationVO;
import com.china.center.oa.product.vo.ProductVO;
import com.china.center.oa.product.vo.ProductVSLocationVO;
import com.china.center.oa.product.vs.ProductCombinationBean;
import com.china.center.oa.product.vs.ProductVSLocationBean;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.FileTools;
import com.china.center.tools.RequestDataStream;
import com.china.center.tools.RequestTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.center.tools.UtilStream;


/**
 * FlowAction
 * 
 * @author ZHUZHU
 * @version 2009-4-26
 * @see ProductAction
 * @since 1.0
 */
public class ProductAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private ProductFacade productFacade = null;

    private ProductManager productManager = null;

    private ProductCombinationDAO productCombinationDAO = null;

    private ProductDAO productDAO = null;

    private CommonDAO commonDAO = null;

    private LocationDAO locationDAO = null;

    private ProviderDAO providerDAO = null;

    private ProductVSLocationDAO productVSLocationDAO = null;

    private static String QUERYPRODUCT = "queryProduct";

    private static String QUERYAPPLYPRODUCT = "queryApplyProduct";

    private static String QUERYCHECKPRODUCT = "queryCheckProduct";

    private static String RPTQUERYPRODUCT = "rptQueryProduct";

    /**
     * default constructor
     */
    public ProductAction()
    {
    }

    /**
     * queryProduct
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                      HttpServletResponse response)
        throws ServletException
    {
        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        // 过滤非稳态的产品
        condtion.addIntCondition("ProductBean.status", "<>", ProductConstant.STATUS_APPLY);

        ActionTools.processJSONQueryCondition(QUERYPRODUCT, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPRODUCT, request, condtion, this.productDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryApplyProduct(查询申请的产品,主要是虚拟产品)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryApplyProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                           HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("ProductBean.status", "=", ProductConstant.STATUS_APPLY);

        condtion.addCondition("ProductBean.createrId", "=", user.getStafferId());

        ActionTools.processJSONQueryCondition(QUERYAPPLYPRODUCT, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYAPPLYPRODUCT, request, condtion, this.productDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * 查询产品申请
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryCheckProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                           HttpServletResponse response)
        throws ServletException
    {
        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("ProductBean.status", "=", ProductConstant.STATUS_APPLY);

        ActionTools.processJSONQueryCondition(QUERYCHECKPRODUCT, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYCHECKPRODUCT, request, condtion, this.productDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * 产品的选择
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<ProductVO> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setProductInnerCondition(request, condtion);

            int total = productDAO.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYPRODUCT);

            list = productDAO.queryEntityVOsByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYPRODUCT);

            list = productDAO.queryEntityVOsByCondition(PageSeparateTools.getCondition(request, RPTQUERYPRODUCT),
                PageSeparateTools.getPageSeparate(request, RPTQUERYPRODUCT));
        }

        request.setAttribute("beanList", list);

        request.setAttribute("random", new Random().nextInt());

        String rootUrl = RequestTools.getRootUrl(request);

        request.setAttribute("rootUrl", rootUrl);

        return mapping.findForward("rptQueryProduct");
    }

    /**
     * @param request
     * @param condtion
     */
    private void setProductInnerCondition(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        String code = request.getParameter("code");

        String abstractType = request.getParameter("abstractType");

        String status = request.getParameter("status");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("ProductBean.name", "like", name);
        }

        if ( !StringTools.isNullOrNone(code))
        {
            condtion.addCondition("ProductBean.code", "like", code);
        }

        if ( !StringTools.isNullOrNone(abstractType))
        {
            condtion.addIntCondition("ProductBean.abstractType", "=", abstractType);
        }

        if ( !StringTools.isNullOrNone(status))
        {
            condtion.addIntCondition("ProductBean.status", "=", ProductConstant.STATUS_COMMON);
        }

    }

    /**
     * 管理员增加产品(非虚拟产品)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                    HttpServletResponse response)
        throws ServletException
    {
        ProductBean bean = new ProductBean();

        // 模板最多10M
        RequestDataStream rds = new RequestDataStream(request, 1024 * 1024 * 10L);

        try
        {
            rds.parser();
        }
        catch (FileUploadBase.SizeLimitExceededException e)
        {
            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加失败:附件超过10M");

            return mapping.findForward("queryProduct");
        }
        catch (Exception e)
        {
            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加失败");

            return mapping.findForward("queryProduct");
        }

        BeanUtil.getBean(bean, rds.getParmterMap());

        bean.setId(commonDAO.getSquenceString());

        ActionForward afor = parserAttachment(mapping, request, rds, bean);

        if (afor != null)
        {
            return afor;
        }

        rds.close();

        try
        {
            User user = Helper.getUser(request);

            bean.setCreaterId(user.getStafferId());

            bean.setLogTime(TimeTools.now());

            bean.setStatus(ProductConstant.STATUS_COMMON);

            productFacade.addProductBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功保存产品");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "保存产品失败:" + e.getMessage());
        }

        return mapping.findForward("queryProduct");
    }

    /**
     * 管理员增加产品(虚拟产品)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addAbstractProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                            HttpServletResponse response)
        throws ServletException
    {
        ProductBean bean = new ProductBean();

        BeanUtil.getBean(bean, request);

        bean.setId(commonDAO.getSquenceString());

        setCombination(request, bean);

        try
        {
            User user = Helper.getUser(request);

            bean.setCreaterId(user.getStafferId());

            bean.setLogTime(TimeTools.now());

            bean.setStatus(ProductConstant.STATUS_APPLY);

            bean.setAbstractType(ProductConstant.ABSTRACT_TYPE_YES);

            productFacade.addProductBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功保存产品");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "保存产品失败:" + e.getMessage());
        }

        return mapping.findForward("queryApplyProduct");
    }

    private void setCombination(HttpServletRequest request, ProductBean bean)
    {
        List<ProductCombinationBean> vsList = new ArrayList();

        User user = Helper.getUser(request);

        bean.setVsList(vsList);

        // 获取组合方式 ProductCombinationBean
        String[] srcProductIds = request.getParameterValues("srcProductId");

        String[] srcAmounts = request.getParameterValues("srcAmount");

        for (int i = 0; i < srcProductIds.length; i++ )
        {
            ProductCombinationBean com = new ProductCombinationBean();

            com.setAmount(CommonTools.parseInt(srcAmounts[i]));
            com.setVproductId(bean.getId());
            com.setSproductId(srcProductIds[i]);
            com.setCreaterId(user.getStafferId());

            vsList.add(com);
        }
    }

    /**
     * 管理员修改产品(非虚拟产品)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response)
        throws ServletException
    {
        // 模板最多10M
        RequestDataStream rds = new RequestDataStream(request, 1024 * 1024 * 10L);

        try
        {
            rds.parser();
        }
        catch (FileUploadBase.SizeLimitExceededException e)
        {
            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加失败:附件超过10M");

            return mapping.findForward("queryProduct");
        }
        catch (Exception e)
        {
            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加失败");

            return mapping.findForward("queryProduct");
        }

        String id = rds.getParmterMap().get("id");

        ProductBean bean = productDAO.find(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据错误,请确认操作");

            return mapping.findForward("queryProduct");
        }

        BeanUtil.getBean(bean, rds.getParmterMap());

        ActionForward afor = parserAttachment(mapping, request, rds, bean);

        if (afor != null)
        {
            return afor;
        }

        rds.close();

        try
        {
            User user = Helper.getUser(request);

            productFacade.updateProductBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功修改产品");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "修改产品失败:" + e.getMessage());
        }

        return mapping.findForward("queryProduct");
    }

    private ActionForward parserAttachment(ActionMapping mapping, HttpServletRequest request, RequestDataStream rds,
                                           ProductBean bean)
    {
        if ( !rds.haveStream())
        {
            return null;
        }

        FileOutputStream out = null;

        UtilStream ustream = null;

        try
        {
            String rabsPath = '/' + bean.getId() + "."
                              + FileTools.getFilePostfix(FileTools.getFileName(rds.getUniqueFileName())).toLowerCase();

            String filePath = this.getPicPath() + '/' + rabsPath;

            bean.setPicPath(rabsPath);

            out = new FileOutputStream(filePath);

            ustream = new UtilStream(rds.getUniqueInputStream(), out);

            ustream.copyStream();
        }
        catch (IOException e)
        {
            _logger.error(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "保存失败");

            return mapping.findForward("queryProduct");
        }
        finally
        {
            if (ustream != null)
            {
                try
                {
                    ustream.close();
                }
                catch (IOException e)
                {
                    _logger.error(e, e);
                }
            }
        }

        return null;
    }

    /**
     * findProduct
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        ProductVO bean = productDAO.findVO(id);

        if (bean == null)
        {
            return ActionTools.toError("数据异常,请重新操作", "queryProduct", mapping, request);
        }

        request.setAttribute("bean", bean);

        String rootUrl = RequestTools.getRootUrl(request);

        request.setAttribute("rootUrl", rootUrl);

        setProviderName(bean);

        String update = request.getParameter("update");

        if ("1".equals(update))
        {
            if (bean.getAbstractType() == ProductConstant.ABSTRACT_TYPE_YES)
            {
                return ActionTools.toError("虚拟产品不能修改", "queryProduct", mapping, request);
            }

            return mapping.findForward("updateProduct");
        }

        // 销售范围
        List<ProductVSLocationVO> voList = productVSLocationDAO.queryEntityVOsByFK(id);

        StringBuilder builder = new StringBuilder();
        for (ProductVSLocationVO productVSLocationVO : voList)
        {
            builder.append(productVSLocationVO.getLocationName()).append(" ");
        }

        // 虚拟产品的处理
        if (bean.getAbstractType() == ProductConstant.ABSTRACT_TYPE_YES)
        {
            // 获取组合方式
            List<ProductCombinationVO> comVOList = productCombinationDAO.queryEntityVOsByFK(id);

            request.setAttribute("comVOList", comVOList);
        }

        request.setAttribute("locationNames", builder);

        return mapping.findForward("detailProduct");
    }

    private void setProviderName(ProductVO bean)
    {
        // 查询4个供应商
        if ( !StringTools.isNullOrNone(bean.getMainProvider()))
        {
            ProviderBean pro = providerDAO.find(bean.getMainProvider());

            if (pro != null)
            {
                bean.setMainProviderName(pro.getName());
            }
        }

        if ( !StringTools.isNullOrNone(bean.getAssistantProvider1()))
        {
            ProviderBean pro = providerDAO.find(bean.getAssistantProvider1());

            if (pro != null)
            {
                bean.setAssistantProviderName1(pro.getName());
            }
        }

        if ( !StringTools.isNullOrNone(bean.getAssistantProvider2()))
        {
            ProviderBean pro = providerDAO.find(bean.getAssistantProvider2());

            if (pro != null)
            {
                bean.setAssistantProviderName2(pro.getName());
            }
        }

        if ( !StringTools.isNullOrNone(bean.getAssistantProvider3()))
        {
            ProviderBean pro = providerDAO.find(bean.getAssistantProvider3());

            if (pro != null)
            {
                bean.setAssistantProviderName3(pro.getName());
            }
        }
    }

    /**
     * configProductVSLocation
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward configProductVSLocation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                 HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String newLocationIds = request.getParameter("newLocationIds");

        String[] split = newLocationIds.split("~");

        List<ProductVSLocationBean> vsList = new ArrayList();

        for (String each : split)
        {
            if ( !StringTools.isNullOrNone(each))
            {
                ProductVSLocationBean vs = new ProductVSLocationBean();

                vs.setLocationId(each);
                vs.setProductId(id);

                vsList.add(vs);
            }
        }

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            productFacade.configProductVSLocation(user.getId(), id, vsList);

            ajax.setSuccess("成功配置产品的销售范围");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("配置产品的销售范围失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * configProductVSLocation
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward configPrice(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String batchPrice = request.getParameter("batchPrice");

        String sailPrice = request.getParameter("sailPrice");

        ProductBean bean = productDAO.find(id);

        AjaxResult ajax = new AjaxResult();

        if (bean == null)
        {
            ajax.setError("数据不存在,请重新操作");

            return JSONTools.writeResponse(response, ajax);
        }

        double oldBatchPrice = bean.getBatchPrice();
        double oldSailPrice = bean.getSailPrice();

        double newBatchPrice = CommonTools.parseFloat(batchPrice);
        double newSailPrice = CommonTools.parseFloat(sailPrice);

        if (oldBatchPrice > newBatchPrice || oldSailPrice > newSailPrice)
        {
            ajax.setError("只能提高批发价和零售价,请重新操作");

            return JSONTools.writeResponse(response, ajax);
        }

        bean.setBatchPrice(newBatchPrice);
        bean.setSailPrice(newSailPrice);

        try
        {
            User user = Helper.getUser(request);

            productFacade.updateProductBean(user.getId(), bean);

            ajax.setSuccess("成功配置产品");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("配置产品的失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 准备配置产品和分公司的关系
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForConfigProductVSLocation(ActionMapping mapping, ActionForm form,
                                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        String id = request.getParameter("id");

        List<ProductVSLocationBean> beanList = productVSLocationDAO.queryEntityBeansByFK(id);

        request.setAttribute("beanList", beanList);

        List<LocationBean> locationList = locationDAO.listEntityBeans();

        for (LocationBean locationBean : locationList)
        {
            locationBean.setCode("0");

            for (ProductVSLocationBean vs : beanList)
            {
                if (vs.getLocationId().equals(locationBean.getId()))
                {
                    locationBean.setCode("1");
                }
            }
        }

        ajax.setSuccess(locationList);

        return JSONTools.writeResponse(response, ajax);

    }

    /**
     * deleteProduct
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            productFacade.deleteProductBean(user.getId(), id);

            ajax.setSuccess("成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除产品失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * deleteProduct
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward passApplyProduct(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            productFacade.changeProductStatus(user.getId(), id, ProductConstant.STATUS_APPLY,
                ProductConstant.STATUS_COMMON);

            ajax.setSuccess("成功通过此申请");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("操作失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    public String getPicPath()
    {
        return ConfigLoader.getProperty("picPath");
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
     * @return the productManager
     */
    public ProductManager getProductManager()
    {
        return productManager;
    }

    /**
     * @param productManager
     *            the productManager to set
     */
    public void setProductManager(ProductManager productManager)
    {
        this.productManager = productManager;
    }

    /**
     * @return the productCombinationDAO
     */
    public ProductCombinationDAO getProductCombinationDAO()
    {
        return productCombinationDAO;
    }

    /**
     * @param productCombinationDAO
     *            the productCombinationDAO to set
     */
    public void setProductCombinationDAO(ProductCombinationDAO productCombinationDAO)
    {
        this.productCombinationDAO = productCombinationDAO;
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
     * @return the productVSLocationDAO
     */
    public ProductVSLocationDAO getProductVSLocationDAO()
    {
        return productVSLocationDAO;
    }

    /**
     * @param productVSLocationDAO
     *            the productVSLocationDAO to set
     */
    public void setProductVSLocationDAO(ProductVSLocationDAO productVSLocationDAO)
    {
        this.productVSLocationDAO = productVSLocationDAO;
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
