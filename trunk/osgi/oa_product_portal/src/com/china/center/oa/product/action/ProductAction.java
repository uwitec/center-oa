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
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.constant.ProductConstant;
import com.china.center.oa.product.dao.ProductCombinationDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.ProductVSLocationDAO;
import com.china.center.oa.product.facade.ProductFacade;
import com.china.center.oa.product.manager.ProductManager;
import com.china.center.oa.product.vs.ProductVSLocationBean;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.tools.BeanUtil;
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

    private ProductVSLocationDAO productVSLocationDAO = null;

    private static String QUERYPRODUCT = "queryProduct";

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

        ActionTools.processJSONQueryCondition(QUERYPRODUCT, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPRODUCT, request, condtion, this.productDAO);

        return JSONTools.writeResponse(response, jsonstr);
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

        ProductBean bean = productDAO.findVO(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据异常,请重新操作");

            return mapping.findForward("queryProduct");
        }

        request.setAttribute("bean", bean);

        String rootUrl = RequestTools.getRootUrl(request);

        request.setAttribute("rootUrl", rootUrl);

        String update = request.getParameter("update");

        if ("1".equals(update))
        {
            return mapping.findForward("updateProduct");
        }

        return mapping.findForward("detailProduct");
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

            ajax.setSuccess("成功删除产品");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除产品失败:" + e.getMessage());
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
}
