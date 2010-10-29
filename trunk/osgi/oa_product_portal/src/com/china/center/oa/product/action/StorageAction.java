/**
 * File Name: LocationAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.action;


import java.util.Collections;
import java.util.Comparator;
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

import com.center.china.osgi.publics.User;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.JSONTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.common.PageSeparateTools;
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.actionhelper.query.HandleResult;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.bean.StorageBean;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.StorageDAO;
import com.china.center.oa.product.dao.StorageLogDAO;
import com.china.center.oa.product.dao.StorageRelationDAO;
import com.china.center.oa.product.facade.ProductFacade;
import com.china.center.oa.product.vo.StorageLogVO;
import com.china.center.oa.product.vo.StorageRelationVO;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.osgi.jsp.ElTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.StringTools;


/**
 * DepotAction
 * 
 * @author ZHUZHU
 * @version 2010-6-27
 * @see StorageAction
 * @since 1.0
 */
public class StorageAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private ProductFacade productFacade = null;

    private StorageDAO storageDAO = null;

    private ProductDAO productDAO = null;

    private DepotpartDAO depotpartDAO = null;

    private DepotDAO depotDAO = null;

    private StorageLogDAO storageLogDAO = null;

    private StorageRelationDAO storageRelationDAO = null;

    private static final String QUERYSTORAGE = "queryStorage";

    private static final String QUERYSTORAGERELATION = "queryStorageRelation";

    private static final String QUERYSELFSTORAGERELATION = "querySelfStorageRelation";

    private static final String RPTQUERYPRODUCTINDEPOTPART = "rptQueryProductInDepotpart";

    /**
     * default constructor
     */
    public StorageAction()
    {
    }

    public ActionForward queryStorage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                      HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYSTORAGE, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSTORAGE, request, condtion, this.storageDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryStorageRelation
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryStorageRelation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                              HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYSTORAGERELATION, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSTORAGERELATION, request, condtion,
            this.storageRelationDAO, new HandleResult<StorageRelationVO>()
            {
                public void handle(StorageRelationVO vo)
                {
                    if (StringTools.isNullOrNone(vo.getStafferName()))
                    {
                        vo.setStafferName("公共");
                    }

                }
            });

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * querySelfStorageRelation
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward querySelfStorageRelation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                  HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("StorageRelationBean.stafferId", "=", user.getStafferId());

        ActionTools.processJSONQueryCondition(QUERYSELFSTORAGERELATION, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSELFSTORAGERELATION, request, condtion,
            this.storageRelationDAO);

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryStorageLog
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward queryStorageLog(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse reponse)
        throws ServletException
    {
        ConditionParse condition = new ConditionParse();

        setCondition(request, condition);

        // 获取指定时间内的仓区的异动(仅仅查询最近的1000个)
        List<StorageLogVO> list = storageLogDAO.queryEntityVOsByLimit(condition, 1000);

        for (StorageLogVO storageLogBean : list)
        {
            storageLogBean.setTypeName(ElTools.get("storageType", storageLogBean.getType()));
        }

        Collections.sort(list, new Comparator<StorageLogVO>()
        {
            public int compare(StorageLogVO o1, StorageLogVO o2)
            {
                return Integer.parseInt(o1.getId()) - Integer.parseInt(o2.getId());
            }
        });

        request.setAttribute("listStorageLog", list);

        String queryType = request.getParameter("queryType");

        // 仓区下
        if ("1".equals(queryType))
        {
            return mapping.findForward("listStorageLog");
        }

        // 仓库下
        return mapping.findForward("listStorageLog1");
    }

    private void setCondition(HttpServletRequest request, ConditionParse condition)
    {
        String productId = request.getParameter("productId");

        String depotpartId = request.getParameter("depotpartId");

        String depotId = request.getParameter("depotId");

        String priceKey = request.getParameter("priceKey");

        condition.addCondition("StorageLogBean.productId", "=", productId);

        if ( !StringTools.isNullOrNone(depotpartId))
        {
            condition.addCondition("StorageLogBean.depotpartId", "=", depotpartId);
        }

        if ( !StringTools.isNullOrNone(depotId))
        {
            condition.addCondition("StorageLogBean.depotId", "=", depotId);
        }

        if ( !StringTools.isNullOrNone(priceKey))
        {
            condition.addCondition("StorageLogBean.priceKey", "=", priceKey);

            request.setAttribute("priceKey", 1);
        }
        else
        {
            request.setAttribute("priceKey", 0);
        }

        condition.addCondition("order by StorageLogBean.id desc");
    }

    /**
     * addStorage
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addStorage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                    HttpServletResponse response)
        throws ServletException
    {
        StorageBean bean = new StorageBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            productFacade.addStorageBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加储位:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryStorage");
    }

    /**
     * updateDepot
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateStorage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response)
        throws ServletException
    {
        StorageBean bean = new StorageBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            productFacade.updateStorageBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作储位:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryStorage");
    }

    /**
     * preForAddStorage
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForMoveDepotpart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                             HttpServletResponse response)
        throws ServletException
    {
        String depotId = request.getParameter("id");

        List<DepotpartBean> depotpartList = depotpartDAO.queryEntityBeansByFK(depotId);

        request.setAttribute("depotpartList", depotpartList);

        return mapping.findForward("moveDepotpart");
    }

    /**
     * preForMoveDepotpart
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddStorage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                          HttpServletResponse response)
        throws ServletException
    {
        List<DepotBean> depotList = depotDAO.listEntityBeans();

        request.setAttribute("depotList", depotList);

        return mapping.findForward("addStorage");
    }

    /**
     * deleteStorage
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteStorage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            productFacade.deleteStorageBean(user.getId(), id);

            ajax.setSuccess("成功删除储位");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * deleteStorageRelation
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteStorageRelation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            productFacade.deleteStorageRelation(user.getId(), id);

            ajax.setSuccess("成功删除库存");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            ajax.setError("删除失败:" + e.getMessage());
        }

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * findDepot
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findStorage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        StorageBean bean = storageDAO.findVO(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据异常,请重新操作");

            return mapping.findForward("queryDepot");
        }

        List<DepotBean> depotList = depotDAO.listEntityBeans();

        request.setAttribute("depotList", depotList);

        request.setAttribute("bean", bean);

        String update = request.getParameter("update");

        if ("1".equals(update))
        {
            return mapping.findForward("updateStorage");
        }

        return mapping.findForward("detailStorage");
    }

    /**
     * 产品转移查询
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForFindStorageToTransfer(ActionMapping mapping, ActionForm form,
                                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String id = request.getParameter("id");

        String pname = request.getParameter("pname");

        StorageBean bean = storageDAO.findVO(id);

        if (bean == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "数据异常,请重新操作");

            return mapping.findForward("queryDepot");
        }

        List<StorageBean> list = storageDAO.queryEntityBeansByFK(bean.getDepotpartId());

        request.setAttribute("list", list);

        request.setAttribute("bean", bean);

        List<StorageRelationVO> relations = storageRelationDAO.queryEntityVOsByFK(id);

        for (Iterator iterator = relations.iterator(); iterator.hasNext();)
        {
            StorageRelationVO storageRelationBean = (StorageRelationVO)iterator.next();

            ProductBean product = productDAO.find(storageRelationBean.getProductId());

            if (product != null)
            {
                storageRelationBean.setProductName(product.getName() + "     数量【" + storageRelationBean.getAmount()
                                                   + "】 价格【" + ElTools.formatNum(storageRelationBean.getPrice()) + "】");
            }

            if ( !StringTools.isNullOrNone(pname))
            {
                if (product.getName().indexOf(pname) == -1)
                {
                    iterator.remove();
                }
            }

        }

        request.setAttribute("relations", relations);

        return mapping.findForward("transferStorageRelation");
    }

    /**
     * 默认储位的产品的转移
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward transferStorageRelation(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                 HttpServletResponse reponse)
        throws ServletException
    {
        String sourceStorage = request.getParameter("id");

        String dirStorage = request.getParameter("dirStorage");

        String productId = request.getParameter("productId");

        String[] relations = productId.split("#");

        User user = Helper.getUser(request);

        try
        {
            productFacade.transferStorageRelation(user.getId(), sourceStorage, dirStorage, relations);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            return mapping.findForward("queryStorage");
        }

        request.setAttribute(KeyConstant.MESSAGE, "产品储位间转移成功");

        return mapping.findForward("queryStorage");
    }

    /**
     * 仓区里面的产品转移
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward moveDepotpart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                       HttpServletResponse reponse)
        throws ServletException
    {
        String destDepotpartId = request.getParameter("dest");

        String sourceRelationId = request.getParameter("sourceRelationId");

        String amount = request.getParameter("amount");

        User user = Helper.getUser(request);

        try
        {
            productFacade.transferStorageRelationInDepotpart(user.getId(), sourceRelationId, destDepotpartId,
                CommonTools.parseInt(amount));
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            return mapping.findForward("queryStorageRelation");
        }

        request.setAttribute(KeyConstant.MESSAGE, "产品仓区间转移成功");

        return mapping.findForward("queryStorageRelation");
    }

    /**
     * queryProduct
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryProductInDepotpart(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                                    HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<StorageRelationVO> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = setRptQueryProductCondition(request);

            int total = storageRelationDAO.countVOByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYPRODUCTINDEPOTPART);

            list = storageRelationDAO.queryEntityVOsByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYPRODUCTINDEPOTPART);

            list = storageRelationDAO.queryEntityVOsByCondition(PageSeparateTools.getCondition(request,
                RPTQUERYPRODUCTINDEPOTPART), PageSeparateTools.getPageSeparate(request, RPTQUERYPRODUCTINDEPOTPART));
        }

        for (StorageRelationVO vo : list)
        {
            if (StringTools.isNullOrNone(vo.getStafferName()))
            {
                vo.setStafferName("公共");
            }
        }

        request.setAttribute("beanList", list);

        return mapping.findForward("rptQueryProductInDepotpart");
    }

    private ConditionParse setRptQueryProductCondition(HttpServletRequest request)
    {
        String name = request.getParameter("name");

        String code = request.getParameter("code");

        String stafferId = request.getParameter("stafferId");

        String storageName = request.getParameter("storageName");

        String depotpartId = request.getParameter("depotpartId");

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("ProductBean.name", "like", name);
        }

        if ( !StringTools.isNullOrNone(code))
        {
            condtion.addCondition("ProductBean.code", "like", code);
        }

        // storageName
        if ( !StringTools.isNullOrNone(storageName))
        {
            condtion.addCondition("StorageBean.name", "like", storageName);
        }

        if ( !StringTools.isNullOrNone(depotpartId))
        {
            condtion.addCondition("StorageRelationBean.depotpartId", "=", depotpartId);
        }

        if ( !StringTools.isNullOrNone(stafferId))
        {
            condtion.addCondition("StorageRelationBean.stafferId", "=", stafferId);
        }

        return condtion;
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
     * @return the storageDAO
     */
    public StorageDAO getStorageDAO()
    {
        return storageDAO;
    }

    /**
     * @param storageDAO
     *            the storageDAO to set
     */
    public void setStorageDAO(StorageDAO storageDAO)
    {
        this.storageDAO = storageDAO;
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
     * @return the storageLogDAO
     */
    public StorageLogDAO getStorageLogDAO()
    {
        return storageLogDAO;
    }

    /**
     * @param storageLogDAO
     *            the storageLogDAO to set
     */
    public void setStorageLogDAO(StorageLogDAO storageLogDAO)
    {
        this.storageLogDAO = storageLogDAO;
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
}
