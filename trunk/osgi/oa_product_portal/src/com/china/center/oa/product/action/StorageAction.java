/**
 * File Name: LocationAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.action;


import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
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
import com.center.china.osgi.publics.file.writer.WriteFile;
import com.center.china.osgi.publics.file.writer.WriteFileFactory;
import com.china.center.actionhelper.common.ActionTools;
import com.china.center.actionhelper.common.JSONTools;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.common.PageSeparateTools;
import com.china.center.actionhelper.json.AjaxResult;
import com.china.center.actionhelper.query.HandleResult;
import com.china.center.common.MYException;
import com.china.center.common.taglib.DefinedCommon;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.bean.StorageApplyBean;
import com.china.center.oa.product.bean.StorageBean;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.ProductVSLocationDAO;
import com.china.center.oa.product.dao.StorageApplyDAO;
import com.china.center.oa.product.dao.StorageDAO;
import com.china.center.oa.product.dao.StorageLogDAO;
import com.china.center.oa.product.dao.StorageRelationDAO;
import com.china.center.oa.product.facade.ProductFacade;
import com.china.center.oa.product.manager.StorageRelationManager;
import com.china.center.oa.product.vo.StorageLogVO;
import com.china.center.oa.product.vo.StorageRelationVO;
import com.china.center.oa.product.vs.ProductVSLocationBean;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.StafferVSIndustryDAO;
import com.china.center.osgi.jsp.ElTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.RequestTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


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

    private StafferDAO stafferDAO = null;

    private StorageLogDAO storageLogDAO = null;

    private ProductVSLocationDAO productVSLocationDAO = null;

    private StorageRelationManager storageRelationManager = null;

    private StorageRelationDAO storageRelationDAO = null;

    private StorageApplyDAO storageApplyDAO = null;

    private StafferVSIndustryDAO stafferVSIndustryDAO = null;

    private static final String QUERYSTORAGE = "queryStorage";

    private static final String QUERYSTORAGERELATION = "queryStorageRelation";

    private static final String QUERYDEPOTSTORAGERELATION = "queryDepotStorageRelation";

    private static final String QUERYSELFSTORAGERELATION = "querySelfStorageRelation";

    private static final String RPTQUERYPRODUCTINDEPOTPART = "rptQueryProductInDepotpart";

    private static final String RPTQUERYPRODUCTINDEPOT = "rptQueryProductInDepot";

    private static final String QUERYSTORAGEAPPLY = "queryStorageApply";

    private static final String QUERYPUBLICSTORAGERELATION = "queryPublicStorageRelation";

    private static final String RPTQUERYSTORAGERELATIONINDEPOT = "rptQueryStorageRelationInDepot";

    /**
     * default constructor
     */
    public StorageAction()
    {
    }

    public ActionForward queryStorage(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYSTORAGE, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSTORAGE, request, condtion,
            this.storageDAO);

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
    public ActionForward queryStorageRelation(ActionMapping mapping, ActionForm form,
                                              HttpServletRequest request,
                                              HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        ActionTools.processJSONQueryCondition(QUERYSTORAGERELATION, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSTORAGERELATION, request,
            condtion, this.storageRelationDAO, new HandleResult<StorageRelationVO>()
            {
                public void handle(StorageRelationVO vo)
                {
                    if (StringTools.isNullOrNone(vo.getStafferName()))
                    {
                        vo.setStafferName("公共");
                    }

                    int preassign = storageRelationManager.sumPreassignByStorageRelation(vo);

                    vo.setPreassignAmount(preassign);

                    int inway = storageRelationManager.sumInwayByStorageRelation(vo);

                    vo.setInwayAmount(inway);
                }
            });

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryPublicStorageRelation
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryPublicStorageRelation(ActionMapping mapping, ActionForm form,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("StorageRelationBean.stafferId", "=", StorageConstant.PUBLIC_STAFFER);

        ActionTools.processJSONQueryCondition(QUERYPUBLICSTORAGERELATION, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYPUBLICSTORAGERELATION, request,
            condtion, this.storageRelationDAO, new HandleResult<StorageRelationVO>()
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
     * 查询仓库下的产品库存
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryDepotStorageRelation(ActionMapping mapping, ActionForm form,
                                                   HttpServletRequest request,
                                                   HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("StorageRelationBean.locationId", "=", request
            .getParameter("depotId"));

        ActionTools.processJSONQueryCondition(QUERYDEPOTSTORAGERELATION, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYDEPOTSTORAGERELATION, request,
            condtion, this.storageRelationDAO, new HandleResult<StorageRelationVO>()
            {
                public void handle(StorageRelationVO vo)
                {
                    if (StringTools.isNullOrNone(vo.getStafferName()))
                    {
                        vo.setStafferName("公共");
                    }

                    int preassign = storageRelationManager.sumPreassignByStorageRelation(vo);

                    vo.setPreassignAmount(preassign);

                    int inway = storageRelationManager.sumInwayByStorageRelation(vo);

                    vo.setInwayAmount(inway);
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
    public ActionForward querySelfStorageRelation(ActionMapping mapping, ActionForm form,
                                                  HttpServletRequest request,
                                                  HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("StorageRelationBean.stafferId", "=", user.getStafferId());

        ActionTools.processJSONQueryCondition(QUERYSELFSTORAGERELATION, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSELFSTORAGERELATION, request,
            condtion, this.storageRelationDAO);

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
    public ActionForward queryStorageApply(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("StorageApplyBean.reveiver", "=", user.getStafferId());

        condtion.addIntCondition("StorageApplyBean.status", "=",
            StorageConstant.STORAGEAPPLY_STATUS_SUBMIT);

        ActionTools.processJSONQueryCondition(QUERYSTORAGEAPPLY, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSTORAGEAPPLY, request, condtion,
            this.storageApplyDAO);

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
    public ActionForward queryStorageLog(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
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

    /**
     * setCondition
     * 
     * @param request
     * @param condition
     */
    private void setCondition(HttpServletRequest request, ConditionParse condition)
    {
        String productId = request.getParameter("productId");

        String depotpartId = request.getParameter("depotpartId");

        String locationId = request.getParameter("locationId");

        String priceKey = request.getParameter("priceKey");

        condition.addCondition("StorageLogBean.productId", "=", productId);

        if ( !StringTools.isNullOrNone(depotpartId))
        {
            condition.addCondition("StorageLogBean.depotpartId", "=", depotpartId);
        }

        if ( !StringTools.isNullOrNone(locationId))
        {
            condition.addCondition("StorageLogBean.locationId", "=", locationId);
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
    public ActionForward addStorage(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
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
    public ActionForward updateStorage(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
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
    public ActionForward preForMoveDepotpart(ActionMapping mapping, ActionForm form,
                                             HttpServletRequest request,
                                             HttpServletResponse response)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String depotId = RequestTools.getValueFromRequest(request, "id");

        List<DepotpartBean> depotpartList = depotpartDAO.queryEntityBeansByFK(depotId);

        request.setAttribute("depotpartList", depotpartList);

        return mapping.findForward("moveDepotpart");
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
    public ActionForward preForAddStorage(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        List<DepotBean> depotList = depotDAO.listEntityBeans();

        request.setAttribute("depotList", depotList);

        return mapping.findForward("addStorage");
    }

    /**
     * preForApplyStorage
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddStorageApply(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        StorageRelationVO bean = storageRelationDAO.findVO(id);

        request.setAttribute("bean", bean);

        return mapping.findForward("addStorageApply");
    }

    /**
     * addStorageApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addStorageApply(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        StorageApplyBean bean = new StorageApplyBean();

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            StorageRelationVO relation = storageRelationDAO.findVO(bean.getStorageRelationId());

            if (relation == null)
            {
                return ActionTools.toError("数据错误,请重新操作", mapping, request);
            }

            bean.setProductName(relation.getProductName());
            bean.setApplyer(user.getStafferId());
            bean.setLogTime(TimeTools.now());

            if (bean.getAmount() > relation.getAmount() || bean.getAmount() == 0)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "转移的数量非法");

                return mapping.findForward("querySelfStorageRelation");
            }

            productFacade.addStorageApply(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("querySelfStorageRelation");
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
    public ActionForward deleteStorage(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
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
     * passStorageApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward passStorageApply(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            productFacade.passStorageApply(user.getId(), id);

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
     * passStorageApply
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward rejectStorageApply(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            productFacade.rejectStorageApply(user.getId(), id);

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
     * deleteStorageRelation
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteStorageRelation(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
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
     * initPriceKey
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward initPriceKey(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        int initPriceKey = storageRelationManager.initPriceKey();

        ajax.setSuccess("成功初始化库存KEY:" + initPriceKey);

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
    public ActionForward findStorage(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
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
                                                     HttpServletRequest request,
                                                     HttpServletResponse response)
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

            if ( !StorageConstant.PUBLIC_STAFFER.equals(storageRelationBean.getStafferId()))
            {
                iterator.remove();
            }

            ProductBean product = productDAO.find(storageRelationBean.getProductId());

            if (product != null)
            {
                storageRelationBean.setProductName(product.getName()
                                                   + "     数量【"
                                                   + storageRelationBean.getAmount()
                                                   + "】 价格【"
                                                   + ElTools.formatNum(storageRelationBean
                                                       .getPrice()) + "】");
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
    public ActionForward transferStorageRelation(ActionMapping mapping, ActionForm form,
                                                 HttpServletRequest request,
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
            productFacade.transferStorageRelation(user.getId(), sourceStorage, dirStorage,
                relations);
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
    public ActionForward moveDepotpart(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String destDepotpartId = request.getParameter("dest");

        String sourceRelationId = request.getParameter("sourceRelationId");

        String amount = request.getParameter("amount");

        User user = Helper.getUser(request);

        try
        {
            String id = productFacade.transferStorageRelationInDepotpart(user.getId(),
                sourceRelationId, destDepotpartId, CommonTools.parseInt(amount));

            request.setAttribute(KeyConstant.MESSAGE, "产品仓区间转移成功,流水号:" + id);
        }
        catch (MYException e)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, e.getErrorContent());

            return mapping.findForward("queryStorageRelation");
        }

        return preForMoveDepotpart(mapping, form, request, response);
    }

    /**
     * rptQueryProductInDepotpart(仓区内)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryProductInDepotpart(ActionMapping mapping, ActionForm form,
                                                    HttpServletRequest request,
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

            list = storageRelationDAO.queryEntityVOsByCondition(PageSeparateTools.getCondition(
                request, RPTQUERYPRODUCTINDEPOTPART), PageSeparateTools.getPageSeparate(request,
                RPTQUERYPRODUCTINDEPOTPART));
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

    /**
     * 仓库内查询(合成产品,只能是良品仓区)
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryProductInDepot(ActionMapping mapping, ActionForm form,
                                                HttpServletRequest request,
                                                HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<StorageRelationVO> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = setRptQueryProductCondition3(request);

            int total = storageRelationDAO.countVOByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYPRODUCTINDEPOT);

            list = storageRelationDAO.queryEntityVOsByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYPRODUCTINDEPOT);

            list = storageRelationDAO.queryEntityVOsByCondition(PageSeparateTools.getCondition(
                request, RPTQUERYPRODUCTINDEPOT), PageSeparateTools.getPageSeparate(request,
                RPTQUERYPRODUCTINDEPOT));
        }

        for (StorageRelationVO vo : list)
        {
            if (StringTools.isNullOrNone(vo.getStafferName()))
            {
                vo.setStafferName("公共");
            }
        }

        request.setAttribute("beanList", list);

        return mapping.findForward("rptQueryProductInDepot");
    }

    /**
     * CORE 在仓库里面查询可销售的库存(这里可能根据销售区域进行过滤产品)不实现翻页
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryStorageRelationInDepot(ActionMapping mapping, ActionForm form,
                                                        HttpServletRequest request,
                                                        HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String depotId = request.getParameter("depotId");

        List<StorageRelationVO> list = new ArrayList<StorageRelationVO>();

        ConditionParse condtion = setRptQueryProductCondition2(request);

        // CORE 控制出售区域(某些产品只能在一定的区域下销售)
        String sailLocation = request.getParameter("sailLocation");

        int total = storageRelationDAO.countVOByCondition(condtion.toString());

        PageSeparate page = new PageSeparate(total, 50);

        PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYSTORAGERELATIONINDEPOT);

        List<StorageRelationVO> queryList = storageRelationDAO.queryEntityVOsByCondition(condtion,
            page);

        // 没有过滤直接查询前50个
        if (StringTools.isNullOrNone(sailLocation))
        {
            list.addAll(queryList);
        }
        else
        {
            createList(list, condtion, sailLocation, page, queryList, request);
        }

        for (Iterator iterator = list.iterator(); iterator.hasNext();)
        {
            StorageRelationVO vo = (StorageRelationVO)iterator.next();

            if (StringTools.isNullOrNone(vo.getStafferName()))
            {
                vo.setStafferName("公共");
            }

            int preassign = storageRelationManager.sumPreassignByStorageRelation(vo);

            // 可发数量
            vo.setMayAmount(vo.getAmount() - preassign);

            // 不显示低于0的数量
            if (vo.getMayAmount() <= 0)
            {
                iterator.remove();

                continue;
            }

            // 预支数量
            vo.setPreassignAmount(preassign);

            ProductBean product = productDAO.find(vo.getProductId());

            if (product != null)
            {
                // 设置批发价
                vo.setBatchPrice(product.getBatchPrice());
            }
        }

        // 查询仓库下的良品仓
        List<DepotpartBean> depotparList = depotpartDAO.queryOkDepotpartInDepot(depotId);

        request.setAttribute("beanList", list);

        request.setAttribute("depotparList", depotparList);

        return mapping.findForward("rptQueryStorageRelationInDepot");
    }

    /**
     * exportStorageRelation
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward exportStorageRelation(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse reponse)
        throws ServletException
    {
        OutputStream out = null;

        String filenName = "Product_" + TimeTools.now("MMddHHmmss") + ".csv";

        reponse.setContentType("application/x-dbf");

        reponse.setHeader("Content-Disposition", "attachment; filename=" + filenName);

        WriteFile write = null;

        try
        {
            out = reponse.getOutputStream();

            ConditionParse condtion = new ConditionParse();

            List<DepotBean> lList = depotDAO.listEntityBeans();

            write = WriteFileFactory.getMyTXTWriter();

            write.openFile(out);

            write.writeLine("日期,仓库,仓区,仓区属性,储位,产品名称,产品编码,产品数量,产品价格");

            String now = TimeTools.now("yyyy-MM-dd");

            for (DepotBean locationBean : lList)
            {
                condtion.clear();

                condtion.addCondition("StorageRelationBean.locationId", "=", locationBean.getId());

                condtion.addIntCondition("StorageRelationBean.amount", ">", 0);

                List<StorageRelationVO> list = storageRelationDAO
                    .queryEntityVOsByCondition(condtion);

                for (StorageRelationVO each : list)
                {
                    if (each.getAmount() > 0)
                    {
                        String typeName = DefinedCommon.getValue("depotpartType", each
                            .getDepotpartType());

                        write.writeLine(now
                                        + ','
                                        + locationBean.getName()
                                        + ','
                                        + each.getDepotpartName()
                                        + ','
                                        + typeName
                                        + ','
                                        + each.getStorageName()
                                        + ','
                                        + each.getProductName().replaceAll(",", " ").replaceAll(
                                            "\r\n", "") + ',' + each.getProductCode() + ','
                                        + String.valueOf(each.getAmount()) + ','
                                        + MathTools.formatNum(each.getPrice()));
                    }
                }

            }

            write.close();

        }
        catch (Throwable e)
        {
            _logger.error(e, e);

            return null;
        }
        finally
        {
            if (out != null)
            {
                try
                {
                    out.close();
                }
                catch (IOException e1)
                {
                }
            }

            if (write != null)
            {

                try
                {
                    write.close();
                }
                catch (IOException e1)
                {
                }
            }
        }

        return null;
    }

    /**
     * createList
     * 
     * @param list
     * @param condtion
     * @param sailLocation
     * @param page
     * @param queryList
     */
    private void createList(List<StorageRelationVO> list, ConditionParse condtion,
                            String sailLocation, PageSeparate page,
                            List<StorageRelationVO> queryList, HttpServletRequest request)
    {
        StafferBean user = Helper.getStaffer(request);

        // 如果没有行业直接退出
        if (StringTools.isNullOrNone(user.getIndustryId()))
        {
            return;
        }

        for (StorageRelationVO each : queryList)
        {
            if (hasInSailLocation(each.getProductId(), sailLocation, user))
            {
                list.add(each);

                if (list.size() >= 50)
                {
                    break;
                }
            }
        }

        int index = 0;

        while (page.hasNextPage())
        {
            page.nextPage();

            index++ ;

            if (index > 30)
            {
                break;
            }

            queryList = storageRelationDAO.queryEntityVOsByCondition(condtion, page);

            for (StorageRelationVO each : queryList)
            {
                if (hasInSailLocation(each.getProductId(), sailLocation, user))
                {
                    list.add(each);

                    if (list.size() >= 50)
                    {
                        return;
                    }
                }
            }
        }
    }

    /**
     * hasInSailLocation
     * 
     * @param productId
     * @param sailLocation
     * @param user
     * @return
     */
    private boolean hasInSailLocation(String productId, String sailLocation, StafferBean user)
    {
        List<ProductVSLocationBean> list = productVSLocationDAO.queryEntityBeansByFK(productId);

        // 如果没有设置就是全部可以销售
        if (list.size() == 0)
        {
            return true;
        }

        for (ProductVSLocationBean productVSLocationBean : list)
        {
            if (productVSLocationBean.getLocationId().equals(user.getIndustryId()))
            {
                return true;
            }
        }

        return false;
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
     * setRptQueryProductCondition3
     * 
     * @param request
     * @return
     */
    private ConditionParse setRptQueryProductCondition3(HttpServletRequest request)
    {
        String name = request.getParameter("name");

        String code = request.getParameter("code");

        String stafferId = request.getParameter("stafferId");

        String storageName = request.getParameter("storageName");

        String depotpartName = request.getParameter("depotpartName");

        String depotpartId = request.getParameter("depotpartId");

        String locationId = request.getParameter("locationId");

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

        if ( !StringTools.isNullOrNone(depotpartName))
        {
            condtion.addCondition("DepotpartBean.name", "like", depotpartName);
        }

        if ( !StringTools.isNullOrNone(depotpartId))
        {
            condtion.addCondition("StorageRelationBean.depotpartId", "=", depotpartId);
        }

        if ( !StringTools.isNullOrNone(stafferId))
        {
            condtion.addCondition("StorageRelationBean.stafferId", "=", stafferId);
        }

        // 只能是OK仓区
        condtion.addIntCondition("DepotpartBean.type", "=", DepotConstant.DEPOTPART_TYPE_OK);

        condtion.addCondition("StorageRelationBean.locationId", "=", locationId);

        return condtion;
    }

    /**
     * 查询仓库下的可用库存
     * 
     * @param request
     * @return
     */
    private ConditionParse setRptQueryProductCondition2(HttpServletRequest request)
    {
        String name = request.getParameter("name");

        String code = request.getParameter("code");

        User user = Helper.getUser(request);

        // 公共的就是0,私有的就是自己的ID
        String stafferId = request.getParameter("stafferId");

        String depotpartId = request.getParameter("depotpartId");

        String depotId = request.getParameter("depotId");

        // 0/null:销售单 1:入库单
        String queryType = request.getParameter("queryType");

        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        condtion.addCondition("StorageRelationBean.locationId", "=", depotId);

        // 只能是OK仓区
        condtion.addIntCondition("DepotpartBean.type", "=", DepotConstant.DEPOTPART_TYPE_OK);

        condtion.addIntCondition("StorageRelationBean.amount", ">", 0);

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("ProductBean.name", "like", name);
        }

        if ( !StringTools.isNullOrNone(code))
        {
            condtion.addCondition("ProductBean.code", "like", code);
        }

        if ( !StringTools.isNullOrNone(depotpartId))
        {
            condtion.addCondition("StorageRelationBean.depotpartId", "=", depotpartId);
        }

        if ( !"1".equals(queryType))
        {
            if (StringTools.isNullOrNone(stafferId))
            {
                condtion
                    .addCondition("and (StorageRelationBean.stafferId = '0' or StorageRelationBean.stafferId = '"
                                  + user.getStafferId() + "')");
            }
            else
            {
                condtion.addCondition("StorageRelationBean.stafferId", "=", stafferId);
            }
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

    /**
     * @return the storageApplyDAO
     */
    public StorageApplyDAO getStorageApplyDAO()
    {
        return storageApplyDAO;
    }

    /**
     * @param storageApplyDAO
     *            the storageApplyDAO to set
     */
    public void setStorageApplyDAO(StorageApplyDAO storageApplyDAO)
    {
        this.storageApplyDAO = storageApplyDAO;
    }

    /**
     * @return the storageRelationManager
     */
    public StorageRelationManager getStorageRelationManager()
    {
        return storageRelationManager;
    }

    /**
     * @param storageRelationManager
     *            the storageRelationManager to set
     */
    public void setStorageRelationManager(StorageRelationManager storageRelationManager)
    {
        this.storageRelationManager = storageRelationManager;
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
     * @return the stafferVSIndustryDAO
     */
    public StafferVSIndustryDAO getStafferVSIndustryDAO()
    {
        return stafferVSIndustryDAO;
    }

    /**
     * @param stafferVSIndustryDAO
     *            the stafferVSIndustryDAO to set
     */
    public void setStafferVSIndustryDAO(StafferVSIndustryDAO stafferVSIndustryDAO)
    {
        this.stafferVSIndustryDAO = stafferVSIndustryDAO;
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
}
