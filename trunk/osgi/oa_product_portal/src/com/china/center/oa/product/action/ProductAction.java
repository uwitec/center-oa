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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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
import com.china.center.actionhelper.jsonimpl.JSONArray;
import com.china.center.actionhelper.query.HandleResult;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.product.bean.ComposeFeeBean;
import com.china.center.oa.product.bean.ComposeFeeDefinedBean;
import com.china.center.oa.product.bean.ComposeItemBean;
import com.china.center.oa.product.bean.ComposeProductBean;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.bean.PriceChangeBean;
import com.china.center.oa.product.bean.PriceChangeNewItemBean;
import com.china.center.oa.product.bean.PriceChangeSrcItemBean;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.bean.ProviderBean;
import com.china.center.oa.product.constant.ComposeConstant;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.constant.ProductConstant;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.dao.ComposeFeeDefinedDAO;
import com.china.center.oa.product.dao.ComposeProductDAO;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.dao.PriceChangeDAO;
import com.china.center.oa.product.dao.ProductCombinationDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.ProductVSLocationDAO;
import com.china.center.oa.product.dao.ProviderDAO;
import com.china.center.oa.product.dao.StorageRelationDAO;
import com.china.center.oa.product.facade.ProductFacade;
import com.china.center.oa.product.manager.ComposeProductManager;
import com.china.center.oa.product.manager.ProductManager;
import com.china.center.oa.product.vo.ComposeFeeDefinedVO;
import com.china.center.oa.product.vo.ComposeProductVO;
import com.china.center.oa.product.vo.PriceChangeNewItemVO;
import com.china.center.oa.product.vo.PriceChangeSrcItemVO;
import com.china.center.oa.product.vo.PriceChangeVO;
import com.china.center.oa.product.vo.ProductCombinationVO;
import com.china.center.oa.product.vo.ProductVO;
import com.china.center.oa.product.vo.ProductVSLocationVO;
import com.china.center.oa.product.vo.StorageRelationVO;
import com.china.center.oa.product.vs.ProductCombinationBean;
import com.china.center.oa.product.vs.ProductVSLocationBean;
import com.china.center.oa.product.vs.StorageRelationBean;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.EnumDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.helper.OATools;
import com.china.center.oa.publics.manager.OrgManager;
import com.china.center.oa.sail.bean.SailConfBean;
import com.china.center.oa.sail.manager.SailConfigManager;
import com.china.center.oa.tax.bean.FinanceBean;
import com.china.center.oa.tax.dao.FinanceDAO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.FileTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.MathTools;
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

    private ComposeFeeDefinedDAO composeFeeDefinedDAO = null;

    private ComposeProductDAO composeProductDAO = null;

    private CommonDAO commonDAO = null;

    private LocationDAO locationDAO = null;

    private ProviderDAO providerDAO = null;

    private DepotpartDAO depotpartDAO = null;

    private DepotDAO depotDAO = null;

    private PriceChangeDAO priceChangeDAO = null;

    private StorageRelationDAO storageRelationDAO = null;

    private ComposeProductManager composeProductManager = null;

    private EnumDAO enumDAO = null;

    private OrgManager orgManager = null;

    private FinanceDAO financeDAO = null;

    private SailConfigManager sailConfigManager = null;

    private ProductVSLocationDAO productVSLocationDAO = null;

    private static String QUERYPRODUCT = "queryProduct";

    private static String QUERYAPPLYPRODUCT = "queryApplyProduct";

    private static String QUERYCHECKPRODUCT = "queryCheckProduct";

    private static String QUERYCOMPOSE = "queryCompose";

    private static String RPTQUERYPRODUCT = "rptQueryProduct";

    private static String RPTQUERYABSPRODUCT = "rptQueryAbsProduct";

    private static String QUERYPRICECHANGE = "queryPriceChange";

    private static String QUERYCOMPOSEFEEDEFINED = "queryComposeFeeDefined";

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
    public ActionForward queryProduct(ActionMapping mapping, ActionForm form,
                                      final HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        // 产品管理进来的
        final String src = request.getParameter("src");

        // 过滤非稳态的产品
        condtion.addIntCondition("ProductBean.status", "<>", ProductConstant.STATUS_APPLY);

        ActionTools.processJSONQueryCondition(QUERYPRODUCT, request, condtion);

        // 默认虚拟产品在前面
        condtion.addCondition("order by ProductBean.abstractType desc, ProductBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPRODUCT, request, condtion,
            this.productDAO, new HandleResult<ProductVO>()
            {
                public void handle(ProductVO obj)
                {
                    if ( !"0".equals(src))
                    {
                        SailConfBean sailConf = sailConfigManager.findProductConf(Helper
                            .getStaffer(request), obj);

                        obj.setSailPrice(obj.getSailPrice()
                                         * (1 + sailConf.getPratio() / 1000.0d + sailConf
                                             .getIratio() / 1000.0d));
                    }
                }
            });

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryPriceChange
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryPriceChange(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYPRICECHANGE, request, condtion);

        condtion.addCondition("order by PriceChangeBean.id desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPRICECHANGE, request, condtion,
            this.priceChangeDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * lockStorageRelation
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward lockStorageRelation(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            productFacade.lockStorageRelation(user.getId());

            ajax.setSuccess("成功锁定库存");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("锁定库存失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * lockStorageRelation
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findStorageRelationStatus(ActionMapping mapping, ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        if (productFacade.isStorageRelationLock())
        {
            ajax.setSuccess("<font color=blue><b>锁定</b></font>");
        }
        else
        {
            ajax.setSuccess("<font color=red><b>未锁定</b></font>");
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * unlockStorageRelation
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward unlockStorageRelation(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            productFacade.unlockStorageRelation(user.getId());

            ajax.setSuccess("成功解锁库存");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("解锁库存失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * queryCompose
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryCompose(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String foward = request.getParameter("foward");

        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        if ("1".equals(foward))
        {
            condtion.addIntCondition("ComposeProductBean.status", "=",
                ComposeConstant.STATUS_SUBMIT);
        }

        if ("2".equals(foward))
        {
            condtion.addIntCondition("ComposeProductBean.status", "=",
                ComposeConstant.STATUS_MANAGER_PASS);
        }

        if ("3".equals(foward))
        {
            condtion.addIntCondition("ComposeProductBean.status", "=",
                ComposeConstant.STATUS_CRO_PASS);
        }

        ActionTools.processJSONQueryCondition(QUERYCOMPOSE, request, condtion);

        condtion.addCondition("order by ComposeProductBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYCOMPOSE, request, condtion,
            this.composeProductDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryApplyProduct(查询申请的产品)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryApplyProduct(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("ProductBean.status", "=", ProductConstant.STATUS_APPLY);

        condtion.addCondition("ProductBean.createrId", "=", user.getStafferId());

        ActionTools.processJSONQueryCondition(QUERYAPPLYPRODUCT, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYAPPLYPRODUCT, request, condtion,
            this.productDAO);

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
    public ActionForward queryCheckProduct(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addIntCondition("ProductBean.status", "=", ProductConstant.STATUS_APPLY);

        ActionTools.processJSONQueryCondition(QUERYCHECKPRODUCT, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYCHECKPRODUCT, request, condtion,
            this.productDAO);

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
    public ActionForward rptQueryProduct(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
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

            list = productDAO.queryEntityVOsByCondition(PageSeparateTools.getCondition(request,
                RPTQUERYPRODUCT), PageSeparateTools.getPageSeparate(request, RPTQUERYPRODUCT));
        }

        request.setAttribute("beanList", list);

        request.setAttribute("random", new Random().nextInt());

        String rootUrl = RequestTools.getRootUrl(request);

        request.setAttribute("rootUrl", rootUrl);

        return mapping.findForward("rptQueryProduct");
    }

    /**
     * 虚拟产品的选择
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryAbsProduct(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<ProductVO> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setAbsProductInnerCondition(request, condtion);

            int total = productDAO.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYABSPRODUCT);

            list = productDAO.queryEntityVOsByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYPRODUCT);

            list = productDAO
                .queryEntityVOsByCondition(PageSeparateTools.getCondition(request,
                    RPTQUERYABSPRODUCT), PageSeparateTools.getPageSeparate(request,
                    RPTQUERYABSPRODUCT));
        }

        Map<String, List<ProductBean>> map = new HashMap();

        for (ProductVO productVO : list)
        {
            // 获取组合方式
            List<ProductCombinationVO> comVOList = productCombinationDAO
                .queryEntityVOsByFK(productVO.getId());

            List<ProductBean> eachList = new ArrayList<ProductBean>();

            String lastName = "&nbsp;<br>";

            for (ProductCombinationVO productCombinationVO : comVOList)
            {
                ProductBean product = productDAO.find(productCombinationVO.getSproductId());

                eachList.add(product);

                lastName += product.getName() + "(" + product.getCode() + ")<br>&nbsp;";
            }

            productVO.setReserve1(lastName);

            map.put(productVO.getId(), eachList);
        }

        request.setAttribute("beanList", list);

        // var uAuth = JSON.parse('${authJSON}');
        request.setAttribute("mapStr", JSONTools.getMapListJSON(map));

        request.setAttribute("random", new Random().nextInt());

        String rootUrl = RequestTools.getRootUrl(request);

        request.setAttribute("rootUrl", rootUrl);

        return mapping.findForward("rptQueryAbsProduct");
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

        String ctype = request.getParameter("ctype");

        String mtype = request.getParameter("mtype");

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

        if ( !StringTools.isNullOrNone(ctype))
        {
            condtion.addIntCondition("ProductBean.ctype", "=", ProductConstant.CTYPE_YES);
        }

        if (OATools.getManagerFlag() && !StringTools.isNullOrNone(mtype))
        {
            condtion.addCondition("ProductBean.reserve4", "=", mtype);
        }
    }

    /**
     * @param request
     * @param condtion
     */
    private void setAbsProductInnerCondition(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        String code = request.getParameter("code");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("ProductBean.name", "like", name);
        }

        if ( !StringTools.isNullOrNone(code))
        {
            condtion.addCondition("ProductBean.code", "like", code);
        }

        condtion
            .addIntCondition("ProductBean.abstractType", "=", ProductConstant.ABSTRACT_TYPE_YES);

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
    public ActionForward addProduct(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
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

            bean.setStatus(ProductConstant.STATUS_APPLY);

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
    public ActionForward addAbstractProduct(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response)
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

    /**
     * 合成产品
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward composeProduct(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ComposeProductBean bean = new ComposeProductBean();

        BeanUtil.getBean(bean, request);

        try
        {
            setCompose(request, bean);

            User user = Helper.getUser(request);

            bean.setStafferId(user.getStafferId());

            bean.setType(ComposeConstant.COMPOSE_TYPE_COMPOSE);

            productFacade.addComposeProduct(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功合成产品,合成后均价:"
                                                      + MathTools.formatNum(bean.getPrice()));
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "合成产品失败:" + e.getMessage());
        }

        return preForCompose(mapping, form, request, response);
    }

    /**
     * queryComposeFeeDefined
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryComposeFeeDefined(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
        throws ServletException
    {
        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYCOMPOSEFEEDEFINED, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYCOMPOSEFEEDEFINED, request,
            condtion, this.composeFeeDefinedDAO, new HandleResult<ComposeFeeDefinedVO>()
            {
                public void handle(ComposeFeeDefinedVO obj)
                {
                    try
                    {
                        ComposeFeeDefinedVO vo = composeProductManager.findComposeFeeDefinedVO(obj
                            .getId());

                        obj.setTaxName(vo.getTaxName());
                    }
                    catch (MYException e)
                    {
                        _logger.warn(e, e);
                    }
                }
            });

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * addComposeFeeDefinedBean
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addComposeFeeDefined(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
        throws ServletException
    {
        ComposeFeeDefinedBean bean = new ComposeFeeDefinedBean();

        BeanUtil.getBean(bean, request);

        try
        {
            User user = Helper.getUser(request);

            productFacade.addComposeFeeDefinedBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "操作成功");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        return mapping.findForward(QUERYCOMPOSEFEEDEFINED);
    }

    /**
     * findComposeFeeDefined
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findComposeFeeDefined(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        ComposeFeeDefinedVO bean = null;

        try
        {
            bean = composeProductManager.findComposeFeeDefinedVO(id);
        }
        catch (MYException e)
        {
            return ActionTools.toError(e.getErrorContent(), QUERYCOMPOSEFEEDEFINED, mapping,
                request);
        }

        if (bean == null)
        {
            return ActionTools.toError("数据异常,请重新操作", QUERYCOMPOSEFEEDEFINED, mapping, request);
        }

        request.setAttribute("bean", bean);

        return mapping.findForward("updateComposeFeeDefined");
    }

    /**
     * updateComposeFeeDefinedBean
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateComposeFeeDefined(ActionMapping mapping, ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
        throws ServletException
    {
        ComposeFeeDefinedBean bean = new ComposeFeeDefinedBean();

        BeanUtil.getBean(bean, request);

        try
        {
            User user = Helper.getUser(request);

            productFacade.updateComposeFeeDefinedBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "操作成功");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        return mapping.findForward(QUERYCOMPOSEFEEDEFINED);
    }

    /**
     * deleteComposeFeeDefinedBean
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteComposeFeeDefined(ActionMapping mapping, ActionForm form,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            productFacade.deleteComposeFeeDefinedBean(user.getId(), id);

            ajax.setSuccess("成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * 产品调价
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward priceChange(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        PriceChangeBean bean = new PriceChangeBean();

        try
        {
            User user = Helper.getUser(request);

            setForPriceChange(request, bean);

            bean.setDescription(request.getParameter("description"));

            bean.setStafferId(user.getStafferId());

            bean.setLogTime(TimeTools.now());

            productFacade.addPriceChange(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功产品调价");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "产品调价失败:" + e.getMessage());
        }

        return mapping.findForward("queryPriceChange");
    }

    /**
     * setCompose
     * 
     * @param request
     * @param bean
     */
    private void setCompose(HttpServletRequest request, ComposeProductBean bean)
        throws MYException
    {
        String dirDepotpart = request.getParameter("dirDepotpart");
        String dirProductId = request.getParameter("dirProductId");
        String dirAmount = request.getParameter("dirAmount");
        String srcDepot = request.getParameter("srcDepot");

        bean.setDepotpartId(dirDepotpart);
        bean.setDeportId(srcDepot);
        bean.setProductId(dirProductId);
        bean.setAmount(CommonTools.parseInt(dirAmount));
        bean.setLogTime(TimeTools.now());
        bean.setType(StorageConstant.OPR_STORAGE_COMPOSE);

        // 获取费用
        String[] feeItemIds = request.getParameterValues("feeItemId");
        String[] feeItems = request.getParameterValues("feeItem");
        String[] idescriptions = request.getParameterValues("idescription");

        List<ComposeFeeBean> feeList = new ArrayList<ComposeFeeBean>();

        double total = 0.0d;

        for (int i = 0; i < feeItems.length; i++ )
        {
            if ( !MathTools.equal(0.0, CommonTools.parseFloat(feeItems[i])))
            {
                ComposeFeeBean each = new ComposeFeeBean();
                each.setFeeItemId(feeItemIds[i]);
                each.setPrice(CommonTools.parseFloat(feeItems[i]));
                each.setLogTime(bean.getLogTime());
                each.setDescription(idescriptions[i]);
                feeList.add(each);

                total += each.getPrice();
            }
        }

        bean.setFeeList(feeList);

        String[] srcDepotparts = request.getParameterValues("srcDepotpart");
        String[] srcProductIds = request.getParameterValues("srcProductId");
        String[] srcAmounts = request.getParameterValues("useAmount");
        String[] srcPrices = request.getParameterValues("srcPrice");
        String[] srcRelations = request.getParameterValues("srcRelation");

        List<ComposeItemBean> itemList = new ArrayList<ComposeItemBean>();

        for (int i = 0; i < srcRelations.length; i++ )
        {
            if (StringTools.isNullOrNone(srcDepotparts[i]))
            {
                continue;
            }

            if (bean.getProductId().equals(srcProductIds[i]))
            {
                throw new MYException("产品不能自己合成自己");
            }

            ComposeItemBean each = new ComposeItemBean();
            each.setAmount(CommonTools.parseInt(srcAmounts[i]));
            each.setDeportId(srcDepot);
            each.setDepotpartId(srcDepotparts[i]);
            each.setLogTime(bean.getLogTime());
            each.setPrice(CommonTools.parseFloat(srcPrices[i]));
            each.setProductId(srcProductIds[i]);
            each.setRelationId(srcRelations[i]);

            itemList.add(each);

            total += each.getPrice() * each.getAmount();
        }

        bean.setItemList(itemList);

        // 计算新产品的成本价
        double price = total / bean.getAmount();

        bean.setPrice(price);
    }

    /**
     * setForPriceChange
     * 
     * @param request
     * @param bean
     */
    private void setForPriceChange(HttpServletRequest request, PriceChangeBean bean)
        throws MYException
    {
        // 获取费用
        String[] relations = request.getParameterValues("relation_id");
        String[] oprices = request.getParameterValues("old_price");

        String[] changeAmounts = request.getParameterValues("changeAmount");
        String[] changePrices = request.getParameterValues("changePrice");

        List<PriceChangeSrcItemBean> srcList = new ArrayList<PriceChangeSrcItemBean>();
        bean.setSrcList(srcList);

        List<PriceChangeNewItemBean> newList = new ArrayList<PriceChangeNewItemBean>();
        bean.setNewList(newList);

        if (relations == null)
        {
            throw new MYException("没有调价的产品,请重新操作");
        }

        for (int i = 0; i < relations.length; i++ )
        {
            // 没有修改价格
            if (CommonTools.parseFloat(oprices[i]) == CommonTools.parseFloat(changePrices[i]))
            {
                continue;
            }

            String id = relations[i];

            StorageRelationBean relation = storageRelationDAO.find(id);

            if (relation == null)
            {
                throw new MYException("库存不存在,请重新操作");
            }

            if (CommonTools.parseInt(changeAmounts[i]) > relation.getAmount())
            {
                throw new MYException("调价数量不能大于总数量,请重新操作");
            }

            String refId = commonDAO.getSquenceString();

            // 修改价格
            PriceChangeSrcItemBean src = new PriceChangeSrcItemBean();
            src.setAmount(relation.getAmount());
            src.setDeportId(relation.getLocationId());
            src.setDepotpartId(relation.getDepotpartId());
            src.setPrice(relation.getPrice());
            src.setProductId(relation.getProductId());
            src.setRefId(refId);

            // 使用RelationId更新
            src.setRelationId(relation.getId());
            src.setStafferId(relation.getStafferId());
            src.setStorageId(relation.getStorageId());
            srcList.add(src);

            // 调价后的库存
            PriceChangeNewItemBean item = new PriceChangeNewItemBean();
            item.setAmount(CommonTools.parseInt(changeAmounts[i]));
            item.setDeportId(relation.getLocationId());
            item.setDepotpartId(relation.getDepotpartId());
            // 改变价格
            item.setPrice(CommonTools.parseFloat(changePrices[i]));
            item.setProductId(relation.getProductId());
            item.setRefId(refId);
            item.setStafferId(relation.getStafferId());
            item.setStorageId(relation.getStorageId());

            newList.add(item);

            // 保持原价的
            if (CommonTools.parseInt(changeAmounts[i]) != relation.getAmount())
            {
                PriceChangeNewItemBean item_old = new PriceChangeNewItemBean();
                // 剩余的数量
                item_old.setAmount(relation.getAmount() - CommonTools.parseInt(changeAmounts[i]));
                item_old.setDeportId(relation.getLocationId());
                item_old.setDepotpartId(relation.getDepotpartId());
                // 原价
                item_old.setPrice(relation.getPrice());
                item_old.setProductId(relation.getProductId());
                item_old.setRefId(refId);
                item_old.setStafferId(relation.getStafferId());
                item_old.setStorageId(relation.getStorageId());

                newList.add(item_old);
            }
        }

        if (ListTools.isEmptyOrNull(bean.getSrcList()))
        {
            throw new MYException("没有调价操作,请重新操作");
        }
    }

    private void setCombination(HttpServletRequest request, ProductBean bean)
    {
        List<ProductCombinationBean> vsList = new ArrayList();

        User user = Helper.getUser(request);

        bean.setVsList(vsList);

        // 获取组合方式 ProductCombinationBean
        String[] srcProductIds = request.getParameterValues("srcProductId");

        // String[] srcAmounts = request.getParameterValues("srcAmount");

        for (int i = 0; i < srcProductIds.length; i++ )
        {
            ProductCombinationBean com = new ProductCombinationBean();

            com.setAmount(1);
            com.setVproductId(bean.getId());
            com.setSproductId(srcProductIds[i]);
            com.setCreaterId(user.getStafferId());

            vsList.add(com);
        }
    }

    /**
     * query depotpart for compose
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForCompose(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        List<DepotBean> list = depotDAO.listEntityBeans();

        List<DepotpartBean> depotpartList = new ArrayList<DepotpartBean>();

        for (DepotBean depotBean : list)
        {
            // 只查询OK仓区的
            List<DepotpartBean> depotList = depotpartDAO.queryOkDepotpartInDepot(depotBean.getId());

            for (DepotpartBean depotpartBean : depotList)
            {
                depotpartBean.setName(depotBean.getName() + " --> " + depotpartBean.getName());
            }

            depotpartList.addAll(depotList);
        }

        request.setAttribute("depotList", list);

        request.setAttribute("depotpartList", depotpartList);

        JSONArray object = new JSONArray(depotpartList, false);

        request.setAttribute("depotpartListStr", object.toString());

        List<ComposeFeeDefinedBean> feeList = composeFeeDefinedDAO.listEntityBeans();

        request.setAttribute("feeList", feeList);

        return mapping.findForward("composeProduct");
    }

    /**
     * preForPriceChange
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddPriceChange(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        if ( !productFacade.isStorageRelationLock())
        {
            return ActionTools.toError("库存未锁定,请先锁定库存再调价", "queryPriceChange", mapping, request);
        }

        String products = request.getParameter("products");

        String[] split = products.split("\r\n");

        if (split.length > 100)
        {
            return ActionTools.toError("不能超过100个产品", "queryPriceChange", mapping, request);
        }

        Set<String> set = new HashSet<String>();

        for (String string : split)
        {
            if ( !StringTools.isNullOrNone(string))
            {
                set.add(string.trim());
            }
        }

        StringBuffer error = new StringBuffer();

        List<StorageRelationVO> relationList = new LinkedList<StorageRelationVO>();

        int mtype = -1;

        for (String each : set)
        {
            if (StringTools.isNullOrNone(each))
            {
                continue;
            }

            ProductBean product = productDAO.findByUnique(each.trim());

            // 忽略
            if (product == null)
            {
                error.append("产品[" + each.trim() + "]不存在").append("<br>");
                continue;
            }

            // 可以调价
            if (product.getAdjustPrice() != ProductConstant.ADJUSTPRICE_YES)
            {
                error.append("产品[" + product.getCode() + "]不支持调价").append("<br>");
                continue;
            }

            if (OATools.getManagerFlag())
            {
                if (mtype == -1)
                {
                    mtype = CommonTools.parseInt(product.getReserve4());
                }
                else
                {
                    if (mtype != CommonTools.parseInt(product.getReserve4()))
                    {
                        error.append("产品[" + product.getCode() + "]管理属性不匹配").append("<br>");
                        continue;
                    }
                }
            }

            ConditionParse condition = new ConditionParse();

            // 数量大于0的库存
            condition.addIntCondition("StorageRelationBean.amount", ">", 0);

            // 良品仓的
            condition.addIntCondition("DepotpartBean.type", "=", DepotConstant.DEPOTPART_TYPE_OK);

            // 公共的库存
            condition.addCondition("StorageRelationBean.stafferId", "=", "0");

            condition.addCondition("StorageRelationBean.productId", "=", product.getId());

            List<StorageRelationVO> eachList = storageRelationDAO
                .queryEntityVOsByCondition(condition);

            for (Iterator iterator = eachList.iterator(); iterator.hasNext();)
            {
                StorageRelationVO vo = (StorageRelationVO)iterator.next();

                vo.setStafferName("公共");

                // 过滤预占的
                int inWay = Math.abs(productFacade.onPriceChange2(user.getId(), vo));

                if (vo.getAmount() > inWay)
                {
                    vo.setAmount(vo.getAmount() - inWay);

                    vo.setErrorAmount(inWay);
                }
                else
                {
                    iterator.remove();
                }
            }

            relationList.addAll(eachList);
        }

        Collections.sort(relationList, new Comparator<StorageRelationVO>()
        {
            public int compare(StorageRelationVO o1, StorageRelationVO o2)
            {
                int a = o1.getProductId().compareTo(o2.getProductId());

                if (a != 0)
                {
                    return a;
                }

                return o1.getLocationId().compareTo(o2.getLocationId());
            }
        });

        request.setAttribute("relationList", relationList);

        request.setAttribute(KeyConstant.ERROR_MESSAGE, error.toString());

        return mapping.findForward("priceChange");
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
    public ActionForward updateProduct(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
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

    private ActionForward parserAttachment(ActionMapping mapping, HttpServletRequest request,
                                           RequestDataStream rds, ProductBean bean)
    {
        if ( !rds.haveStream())
        {
            return null;
        }

        FileOutputStream out = null;

        UtilStream ustream = null;

        try
        {
            String rabsPath = '/'
                              + bean.getId()
                              + "."
                              + FileTools.getFilePostfix(
                                  FileTools.getFileName(rds.getUniqueFileName())).toLowerCase();

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
    public ActionForward findProduct(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
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

    /**
     * findCompose
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findCompose(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        ComposeProductVO bean = productFacade.findComposeById(id);

        if (bean == null)
        {
            return ActionTools.toError("数据异常,请重新操作", "queryCompose", mapping, request);
        }

        List<FinanceBean> financeBeanList = financeDAO.queryRefFinanceItemByRefId(id);

        if (financeBeanList.size() > 0)
        {
            bean.setOtherId(financeBeanList.get(0).getId());
        }

        request.setAttribute("bean", bean);

        return mapping.findForward("detailCompose");
    }

    /**
     * findPriceChange
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findPriceChange(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        PriceChangeVO bean = productFacade.findPriceChangeById(id);

        if (bean == null)
        {
            return ActionTools.toError("数据异常,请重新操作", "queryPriceChange", mapping, request);
        }

        List<PriceChangeSrcItemVO> srcVOList = bean.getSrcVOList();
        List<PriceChangeNewItemVO> newVOList = bean.getNewVOList();

        Map<String, List<PriceChangeNewItemVO>> map = new HashMap<String, List<PriceChangeNewItemVO>>();

        for (PriceChangeSrcItemVO each : srcVOList)
        {
            if (StringTools.isNullOrNone(each.getStafferName()))
            {
                each.setStafferName("公共");
            }

            map.put(each.getRefId(), new ArrayList<PriceChangeNewItemVO>());

            for (PriceChangeNewItemVO newEach : newVOList)
            {
                if (StringTools.isNullOrNone(newEach.getStafferName()))
                {
                    newEach.setStafferName("公共");
                }

                if (newEach.getRefId().equals(each.getRefId()))
                {
                    map.get(each.getRefId()).add(newEach);
                }
            }
        }

        List<FinanceBean> financeBeanList = financeDAO.queryRefFinanceItemByRefId(id);

        if (financeBeanList.size() > 0)
        {
            bean.setOtherId(financeBeanList.get(0).getId());
        }

        request.setAttribute("map", map);
        request.setAttribute("bean", bean);

        return mapping.findForward("detailPriceChange");
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

        if ( !StringTools.isNullOrNone(bean.getAssistantProvider4()))
        {
            ProviderBean pro = providerDAO.find(bean.getAssistantProvider4());

            if (pro != null)
            {
                bean.setAssistantProviderName4(pro.getName());
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
    public ActionForward configProductVSLocation(ActionMapping mapping, ActionForm form,
                                                 HttpServletRequest request,
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
    public ActionForward configPrice(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
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

        double newBatchPrice = CommonTools.parseFloat(batchPrice);
        double newSailPrice = CommonTools.parseFloat(sailPrice);

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
                                                       HttpServletRequest request,
                                                       HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        String id = request.getParameter("id");

        List<ProductVSLocationBean> beanList = productVSLocationDAO.queryEntityBeansByFK(id);

        request.setAttribute("beanList", beanList);

        List<PrincipalshipBean> locationList = orgManager.listAllIndustry();

        for (PrincipalshipBean locationBean : locationList)
        {
            locationBean.setLevel(0);

            for (ProductVSLocationBean vs : beanList)
            {
                if (vs.getLocationId().equals(locationBean.getId()))
                {
                    locationBean.setLevel(1);
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
    public ActionForward rollbackPriceChange(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        if ( !productFacade.isStorageRelationLock())
        {
            ajax.setError("库存未锁定,请先锁定库存再回滚调价");

            return JSONTools.writeResponse(response, ajax);
        }

        try
        {
            User user = Helper.getUser(request);

            productFacade.rollbackPriceChange(user.getId(), id);

            ajax.setSuccess("成功回滚调价");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("回滚调价失败:" + e.getMessage());
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
    public ActionForward deleteProduct(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
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
     * passComposeBean
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward passCompose(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            productFacade.passComposeProduct(user.getId(), id);

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
     * lastPassComposeBean
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward lastPassCompose(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            productFacade.lastPassComposeProduct(user.getId(), id);

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
     * deleteComposeBean
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteCompose(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            productFacade.rejectComposeProduct(user.getId(), id);

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
     * rollbackComposeProduct
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward rollbackComposeProduct(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            productFacade.rollbackComposeProduct(user.getId(), id);

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
     * deleteProduct
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward passApplyProduct(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
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

    /**
     * @return the depotpartDAO
     */
    public DepotpartDAO getDepotpartDAO()
    {
        return depotpartDAO;
    }

    /**
     * @param depotpartDAO
     *            the depotpartDAO to set
     */
    public void setDepotpartDAO(DepotpartDAO depotpartDAO)
    {
        this.depotpartDAO = depotpartDAO;
    }

    /**
     * @return the depotDAO
     */
    public DepotDAO getDepotDAO()
    {
        return depotDAO;
    }

    /**
     * @param depotDAO
     *            the depotDAO to set
     */
    public void setDepotDAO(DepotDAO depotDAO)
    {
        this.depotDAO = depotDAO;
    }

    /**
     * @return the enumDAO
     */
    public EnumDAO getEnumDAO()
    {
        return enumDAO;
    }

    /**
     * @param enumDAO
     *            the enumDAO to set
     */
    public void setEnumDAO(EnumDAO enumDAO)
    {
        this.enumDAO = enumDAO;
    }

    /**
     * @return the composeProductDAO
     */
    public ComposeProductDAO getComposeProductDAO()
    {
        return composeProductDAO;
    }

    /**
     * @param composeProductDAO
     *            the composeProductDAO to set
     */
    public void setComposeProductDAO(ComposeProductDAO composeProductDAO)
    {
        this.composeProductDAO = composeProductDAO;
    }

    /**
     * @return the priceChangeDAO
     */
    public PriceChangeDAO getPriceChangeDAO()
    {
        return priceChangeDAO;
    }

    /**
     * @param priceChangeDAO
     *            the priceChangeDAO to set
     */
    public void setPriceChangeDAO(PriceChangeDAO priceChangeDAO)
    {
        this.priceChangeDAO = priceChangeDAO;
    }

    /**
     * @return the storageRelationDAO
     */
    public StorageRelationDAO getStorageRelationDAO()
    {
        return storageRelationDAO;
    }

    /**
     * @param storageRelationDAO
     *            the storageRelationDAO to set
     */
    public void setStorageRelationDAO(StorageRelationDAO storageRelationDAO)
    {
        this.storageRelationDAO = storageRelationDAO;
    }

    /**
     * @return the orgManager
     */
    public OrgManager getOrgManager()
    {
        return orgManager;
    }

    /**
     * @param orgManager
     *            the orgManager to set
     */
    public void setOrgManager(OrgManager orgManager)
    {
        this.orgManager = orgManager;
    }

    /**
     * @return the composeFeeDefinedDAO
     */
    public ComposeFeeDefinedDAO getComposeFeeDefinedDAO()
    {
        return composeFeeDefinedDAO;
    }

    /**
     * @param composeFeeDefinedDAO
     *            the composeFeeDefinedDAO to set
     */
    public void setComposeFeeDefinedDAO(ComposeFeeDefinedDAO composeFeeDefinedDAO)
    {
        this.composeFeeDefinedDAO = composeFeeDefinedDAO;
    }

    /**
     * @return the composeProductManager
     */
    public ComposeProductManager getComposeProductManager()
    {
        return composeProductManager;
    }

    /**
     * @param composeProductManager
     *            the composeProductManager to set
     */
    public void setComposeProductManager(ComposeProductManager composeProductManager)
    {
        this.composeProductManager = composeProductManager;
    }

    /**
     * @return the financeDAO
     */
    public FinanceDAO getFinanceDAO()
    {
        return financeDAO;
    }

    /**
     * @param financeDAO
     *            the financeDAO to set
     */
    public void setFinanceDAO(FinanceDAO financeDAO)
    {
        this.financeDAO = financeDAO;
    }

    /**
     * @return the sailConfigManager
     */
    public SailConfigManager getSailConfigManager()
    {
        return sailConfigManager;
    }

    /**
     * @param sailConfigManager
     *            the sailConfigManager to set
     */
    public void setSailConfigManager(SailConfigManager sailConfigManager)
    {
        this.sailConfigManager = sailConfigManager;
    }
}
