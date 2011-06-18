/**
 * File Name: FinaAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-2-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.action;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import com.china.center.actionhelper.jsonimpl.JSONArray;
import com.china.center.actionhelper.query.HandleResult;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.bean.ProductBean;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.PrincipalshipDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.sail.dao.UnitViewDAO;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.tax.bean.FinanceBean;
import com.china.center.oa.tax.bean.FinanceItemBean;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.bean.UnitBean;
import com.china.center.oa.tax.constanst.CheckConstant;
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.oa.tax.dao.CheckViewDAO;
import com.china.center.oa.tax.dao.FinanceDAO;
import com.china.center.oa.tax.dao.FinanceItemDAO;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.oa.tax.dao.UnitDAO;
import com.china.center.oa.tax.facade.TaxFacade;
import com.china.center.oa.tax.helper.FinanceHelper;
import com.china.center.oa.tax.manager.FinanceManager;
import com.china.center.oa.tax.vo.CheckViewVO;
import com.china.center.oa.tax.vo.FinanceItemVO;
import com.china.center.oa.tax.vo.FinanceVO;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.SequenceTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * FinaAction
 * 
 * @author ZHUZHU
 * @version 2011-2-7
 * @see FinaAction
 * @since 1.0
 */
public class FinaAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private TaxFacade taxFacade = null;

    private TaxDAO taxDAO = null;

    private DutyDAO dutyDAO = null;

    private UnitDAO unitDAO = null;

    private OutManager outManager = null;

    private FinanceManager financeManager = null;

    private PrincipalshipDAO principalshipDAO = null;

    private StafferDAO stafferDAO = null;

    private FinanceDAO financeDAO = null;

    private UnitViewDAO unitViewDAO = null;

    private CheckViewDAO checkViewDAO = null;

    private FinanceItemDAO financeItemDAO = null;

    private DepotDAO depotDAO = null;

    private ProductDAO productDAO = null;

    private static final String QUERYFINANCE = "queryFinance";

    private static final String QUERYFINANCEITEM = "queryFinanceItem";

    private static final String QUERYCHECKVIEW = "queryCheckView";

    private static String RPTQUERYUNIT = "rptQueryUnit";

    /**
     * default constructor
     */
    public FinaAction()
    {
    }

    /**
     * queryFinance
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryFinance(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        Map<String, String> initMap = initLogTime(request, condtion);

        ActionTools.processJSONQueryCondition(QUERYFINANCE, request, condtion, initMap);

        condtion.addCondition("order by FinanceBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYFINANCE, request, condtion,
            this.financeDAO, new HandleResult<FinanceVO>()
            {
                public void handle(FinanceVO obj)
                {
                    obj.getShowInmoney();
                    obj.getShowOutmoney();
                }
            });

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryFinanceItem
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryFinanceItem(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        Map<String, String> initMap = initItemLogTime(request, condtion);

        ActionTools.processJSONQueryCondition(QUERYFINANCEITEM, request, condtion, initMap);

        condtion.addCondition("order by FinanceItemBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYFINANCEITEM, request, condtion,
            this.financeItemDAO, new HandleResult<FinanceItemVO>()
            {
                public void handle(FinanceItemVO obj)
                {
                    fillItemVO(obj);
                }
            });

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * queryCheck
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryCheckView(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        Map<String, String> initMap = initCheck(request, condtion);

        ActionTools.processJSONQueryCondition(QUERYCHECKVIEW, request, condtion, initMap);

        condtion.addCondition("order by CheckViewBean.logTime desc");

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYCHECKVIEW, request, condtion,
            this.checkViewDAO, new HandleResult<CheckViewVO>()
            {
                public void handle(CheckViewVO obj)
                {
                    StafferBean sb = stafferDAO.find(obj.getStafferId());

                    if (sb != null)
                    {
                        obj.setStafferId(sb.getName());
                    }
                }
            });

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * initLogTime
     * 
     * @param request
     * @param condtion
     * @return
     */
    private Map<String, String> initLogTime(HttpServletRequest request, ConditionParse condtion)
    {
        Map<String, String> changeMap = new HashMap<String, String>();

        String alogTime = request.getParameter("afinanceDate");

        String blogTime = request.getParameter("bfinanceDate");

        if (StringTools.isNullOrNone(alogTime) && StringTools.isNullOrNone(blogTime))
        {
            changeMap.put("afinanceDate", TimeTools.now_short( -30));

            changeMap.put("bfinanceDate", TimeTools.now_short(1));

            condtion.addCondition("FinanceBean.financeDate", ">=", TimeTools.now_short( -30));

            condtion.addCondition("FinanceBean.financeDate", "<=", TimeTools.now_short(1));
        }

        return changeMap;
    }

    private Map<String, String> initItemLogTime(HttpServletRequest request, ConditionParse condtion)
    {
        Map<String, String> changeMap = new HashMap<String, String>();

        String alogTime = request.getParameter("afinanceDate");

        String blogTime = request.getParameter("bfinanceDate");

        if (StringTools.isNullOrNone(alogTime) && StringTools.isNullOrNone(blogTime))
        {
            changeMap.put("afinanceDate", TimeTools.now_short( -30));

            changeMap.put("bfinanceDate", TimeTools.now_short(1));

            condtion.addCondition("FinanceItemBean.financeDate", ">=", TimeTools.now_short( -30));

            condtion.addCondition("FinanceItemBean.financeDate", "<=", TimeTools.now_short(1));
        }

        return changeMap;
    }

    private Map<String, String> initCheck(HttpServletRequest request, ConditionParse condtion)
    {
        Map<String, String> changeMap = new HashMap<String, String>();

        String checkStatus = request.getParameter("checkStatus");

        if (StringTools.isNullOrNone(checkStatus))
        {
            changeMap.put("checkStatus", String.valueOf(PublicConstant.CHECK_STATUS_INIT));

            condtion.addIntCondition("CheckViewBean.checkStatus", "=",
                PublicConstant.CHECK_STATUS_INIT);
        }

        return changeMap;
    }

    /**
     * addFinance
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addFinance(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        FinanceBean bean = new FinanceBean();

        try
        {
            BeanUtil.getBean(bean, request);

            setFinanceBean(bean, request);

            User user = Helper.getUser(request);

            taxFacade.addFinanceBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward(QUERYFINANCE);
    }

    /**
     * updateFinance
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateFinance(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        FinanceBean bean = new FinanceBean();

        try
        {
            BeanUtil.getBean(bean, request);

            setFinanceBean(bean, request);

            User user = Helper.getUser(request);

            taxFacade.updateFinanceBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功操作:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward(QUERYFINANCE);
    }

    /**
     * deleteFinance
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward deleteFinance(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            taxFacade.deleteFinanceBean(user.getId(), id);

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
     * checks
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward checks(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        try
        {
            String id = request.getParameter("id");

            int type = MathTools.parseInt(request.getParameter("type"));

            String reason = request.getParameter("reason");

            User user = Helper.getUser(request);

            if (type != 6 && type != 99)
            {
                taxFacade.checks(user.getId(), id, "[" + user.getStafferName() + "]" + reason);
            }
            // 凭证核对
            else if (type == 99)
            {
                taxFacade.updateFinanceCheck(user.getId(), id, "[" + user.getStafferName() + "]"
                                                               + reason);
            }
            else
            {
                outManager.check(id, user, reason);

                financeManager.deleteChecks(user, id);
            }

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
     * checks2
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward checks2(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        int type = MathTools.parseInt(request.getParameter("type"));

        try
        {
            String id = request.getParameter("id");

            String reason = request.getParameter("reason");

            User user = Helper.getUser(request);

            if (type != 6)
            {
                taxFacade.checks2(user.getId(), id, type, "[" + user.getStafferName() + "]"
                                                          + reason);
            }
            else
            {
                outManager.check(id, user, reason);

                financeManager.deleteChecks(user, id);
            }

            request.setAttribute(KeyConstant.MESSAGE, "成功操作");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "操作失败:" + e.getMessage());
        }

        String forward = "error";

        if (type == CheckConstant.CHECK_TYPE_COMPOSE)
        {
            forward = "queryCompose";
        }
        else if (type == CheckConstant.CHECK_TYPE_CHANGE)
        {
            forward = "queryPriceChange";
        }
        else if (type == CheckConstant.CHECK_TYPE_INBILL)
        {
            forward = "queryInBill";
        }
        else if (type == CheckConstant.CHECK_TYPE_OUTBILL)
        {
            forward = "queryOutBill";
        }
        else if (type == CheckConstant.CHECK_TYPE_STOCK)
        {
            return mapping.findForward(QUERYCHECKVIEW);
        }
        else if (type == CheckConstant.CHECK_TYPE_BUY)
        {
            return mapping.findForward(QUERYCHECKVIEW);
        }
        else if (type == CheckConstant.CHECK_TYPE_CUSTOMER)
        {
            return mapping.findForward(QUERYCHECKVIEW);
        }

        return mapping.findForward(forward);
    }

    /**
     * synCheckView
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward synCheckView(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        AjaxResult ajax = new AjaxResult();

        checkViewDAO.syn();

        unitViewDAO.syn();

        ajax.setSuccess("成功操作");

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * findFinance
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findFinance(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String update = request.getParameter("update");

        FinanceVO bean = financeDAO.findVO(id);

        if (bean == null)
        {
            return ActionTools.toError("数据异常,请重新操作", mapping, request);
        }

        List<FinanceItemVO> voList = financeItemDAO.queryEntityVOsByFK(id);

        for (FinanceItemVO item : voList)
        {
            fillItemVO(item);
        }

        bean.setItemVOList(voList);

        request.setAttribute("bean", bean);

        if ("1".equals(update))
        {
            if (bean.getStatus() == TaxConstanst.FINANCE_STATUS_CHECK)
            {
                return ActionTools.toError("已经被核对(锁定)不能修改,请重新操作", mapping, request);
            }

            preInner(request);

            return mapping.findForward("updateFinance");
        }

        return mapping.findForward("detailFinance");
    }

    /**
     * fillItemVO
     * 
     * @param item
     */
    private void fillItemVO(FinanceItemVO item)
    {
        TaxBean tax = taxDAO.find(item.getTaxId());

        item.setForward(tax.getForward());

        item.setTaxName(tax.getCode() + tax.getName());

        if (tax.getDepartment() == TaxConstanst.TAX_CHECK_YES)
        {
            PrincipalshipBean depart = principalshipDAO.find(item.getDepartmentId());

            if (depart != null)
            {
                item.setDepartmentName(depart.getName());
            }
        }

        if (tax.getStaffer() == TaxConstanst.TAX_CHECK_YES)
        {
            StafferBean sb = stafferDAO.find(item.getStafferId());

            if (sb != null)
            {
                item.setStafferName(sb.getName());
            }
        }

        if (tax.getUnit() == TaxConstanst.TAX_CHECK_YES)
        {
            UnitBean unit = unitDAO.find(item.getUnitId());

            if (unit != null)
            {
                item.setUnitName(unit.getName());
            }
        }

        if (tax.getProduct() == TaxConstanst.TAX_CHECK_YES)
        {
            ProductBean product = productDAO.find(item.getProductId());

            if (product != null)
            {
                item.setProductName(product.getName());
            }
        }

        if (tax.getDepot() == TaxConstanst.TAX_CHECK_YES)
        {
            DepotBean depot = depotDAO.find(item.getDepotId());

            if (depot != null)
            {
                item.setDepotName(depot.getName());
            }
        }

        if (tax.getDuty() == TaxConstanst.TAX_CHECK_YES)
        {
            DutyBean duty2 = dutyDAO.find(item.getDuty2Id());

            if (duty2 != null)
            {
                item.setDuty2Name(duty2.getName());
            }
        }

        item.getShowInmoney();
        item.getShowOutmoney();
    }

    /**
     * setFinanceBean
     * 
     * @param bean
     * @param request
     * @throws MYException
     */
    private void setFinanceBean(FinanceBean bean, HttpServletRequest request)
        throws MYException
    {
        String[] departmentIds = request.getParameterValues("departmentId2");
        String[] idescriptions = request.getParameterValues("idescription");
        String[] taxIds = request.getParameterValues("taxId2");
        String[] stafferId2s = request.getParameterValues("stafferId2");
        String[] unitId2s = request.getParameterValues("unitId2");
        String[] productId2s = request.getParameterValues("productId2");
        String[] depotIds = request.getParameterValues("depotId");
        String[] duty2Ids = request.getParameterValues("duty2Id");
        String[] inmoneys = request.getParameterValues("inmoney");
        String[] outmoneys = request.getParameterValues("outmoney");

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        long inTotal = 0;

        long outTotal = 0;

        String pareId = SequenceTools.getSequence();

        for (int i = 0; i < taxIds.length; i++ )
        {
            if (StringTools.isNullOrNone(taxIds[i]))
            {
                continue;
            }

            FinanceItemBean item = new FinanceItemBean();

            item.setDescription(idescriptions[i]);

            item.setDutyId(bean.getDutyId());

            item.setTaxId(taxIds[i]);

            TaxBean tax = taxDAO.find(item.getTaxId());

            if (tax == null)
            {
                throw new MYException("科目不存在");
            }

            item.setForward(tax.getForward());

            if (tax.getDepartment() == TaxConstanst.TAX_CHECK_YES)
            {
                if (StringTools.isNullOrNone(departmentIds[i]))
                {
                    throw new MYException("科目[%s]部门不能为空,请重新操作", tax.getCode() + tax.getName());
                }

                item.setDepartmentId(departmentIds[i]);
            }
            else
            {
                item.setDepartmentId("");
            }

            if (tax.getStaffer() == TaxConstanst.TAX_CHECK_YES)
            {
                if (StringTools.isNullOrNone(stafferId2s[i]))
                {
                    throw new MYException("科目[%s]职员不能为空,请重新操作", tax.getCode() + tax.getName());
                }

                item.setStafferId(stafferId2s[i]);
            }
            else
            {
                item.setStafferId("");
            }

            if (tax.getUnit() == TaxConstanst.TAX_CHECK_YES)
            {
                if (StringTools.isNullOrNone(unitId2s[i]))
                {
                    throw new MYException("科目[%s]单位不能为空,请重新操作", tax.getCode() + tax.getName());
                }

                item.setUnitId(unitId2s[i]);

                UnitBean unit = unitDAO.find(item.getUnitId());

                if (unit == null)
                {
                    throw new MYException("单位不存在,请确认操作");
                }

                item.setUnitType(unit.getType());
            }
            else
            {
                item.setUnitId("");
            }

            if (tax.getProduct() == TaxConstanst.TAX_CHECK_YES)
            {
                if (StringTools.isNullOrNone(productId2s[i]))
                {
                    throw new MYException("科目[%s]产品不能为空,请重新操作", tax.getCode() + tax.getName());
                }

                item.setProductId(productId2s[i]);

                ProductBean product = productDAO.find(item.getProductId());

                if (product == null)
                {
                    throw new MYException("产品不存在,请确认操作");
                }
            }
            else
            {
                item.setProductId("");
            }

            if (tax.getDepot() == TaxConstanst.TAX_CHECK_YES)
            {
                if (StringTools.isNullOrNone(depotIds[i]))
                {
                    throw new MYException("科目[%s]仓库不能为空,请重新操作", tax.getCode() + tax.getName());
                }

                item.setDepotId(depotIds[i]);

                DepotBean depot = depotDAO.find(item.getDepotId());

                if (depot == null)
                {
                    throw new MYException("仓库不存在,请确认操作");
                }
            }
            else
            {
                item.setDepotId("");
            }

            if (tax.getDuty() == TaxConstanst.TAX_CHECK_YES)
            {
                if (StringTools.isNullOrNone(duty2Ids[i]))
                {
                    throw new MYException("科目[%s]纳税实体不能为空,请重新操作", tax.getCode() + tax.getName());
                }

                item.setDuty2Id(duty2Ids[i]);

                DutyBean duty2 = dutyDAO.find(item.getDuty2Id());

                if (duty2 == null)
                {
                    throw new MYException("纳税实体不存在,请确认操作");
                }
            }
            else
            {
                item.setDuty2Id("");
            }

            item.setName(idescriptions[i]);

            item.setType(bean.getType());

            item.setPareId(pareId);

            item.setInmoney(FinanceHelper.doubleToLong(inmoneys[i]));

            item.setOutmoney(FinanceHelper.doubleToLong(outmoneys[i]));

            inTotal += item.getInmoney();

            outTotal += item.getOutmoney();

            if (inTotal == outTotal && outTotal != 0)
            {
                inTotal = 0;

                outTotal = 0;

                pareId = SequenceTools.getSequence();
            }

            itemList.add(item);
        }

        if (inTotal != outTotal)
        {
            throw new MYException("借贷不相等,请重新操作");
        }

        bean.setItemList(itemList);
    }

    /**
     * preForAddFinance
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     */
    public ActionForward preForAddFinance(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
    {
        preInner(request);

        return mapping.findForward("addFinance");
    }

    private void preInner(HttpServletRequest request)
    {
        List<TaxBean> taxList = taxDAO.listEntityBeans("order by TaxBean.code asc");

        for (Iterator iterator = taxList.iterator(); iterator.hasNext();)
        {
            TaxBean taxBean = (TaxBean)iterator.next();

            if (taxBean.getBottomFlag() == TaxConstanst.TAX_BOTTOMFLAG_ROOT)
            {
                iterator.remove();
            }
        }

        request.setAttribute("taxList", taxList);

        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList", dutyList);

        List<DepotBean> depotList = depotDAO.listEntityBeans();

        request.setAttribute("depotList", depotList);

        JSONArray object = new JSONArray(taxList, false);

        request.setAttribute("taxListStr", object.toString());
    }

    /**
     * rptQueryUnit
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryUnit(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<UnitBean> list = null;

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            setInnerCondition(request, condtion);

            int total = unitDAO.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYUNIT);

            list = unitDAO.queryEntityBeansByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYUNIT);

            list = unitDAO.queryEntityBeansByCondition(PageSeparateTools.getCondition(request,
                RPTQUERYUNIT), PageSeparateTools.getPageSeparate(request, RPTQUERYUNIT));
        }

        request.setAttribute("list", list);

        return mapping.findForward(RPTQUERYUNIT);
    }

    private void setInnerCondition(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        String code = request.getParameter("code");

        String type = request.getParameter("type");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("name", "like", name);
        }

        if ( !StringTools.isNullOrNone(code))
        {
            condtion.addCondition("code", "like", code);
        }

        if ( !StringTools.isNullOrNone(type))
        {
            condtion.addIntCondition("type", "=", type);
        }

        condtion.addCondition("order by id desc");
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
     * @return the financeItemDAO
     */
    public FinanceItemDAO getFinanceItemDAO()
    {
        return financeItemDAO;
    }

    /**
     * @param financeItemDAO
     *            the financeItemDAO to set
     */
    public void setFinanceItemDAO(FinanceItemDAO financeItemDAO)
    {
        this.financeItemDAO = financeItemDAO;
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
     * @return the unitDAO
     */
    public UnitDAO getUnitDAO()
    {
        return unitDAO;
    }

    /**
     * @param unitDAO
     *            the unitDAO to set
     */
    public void setUnitDAO(UnitDAO unitDAO)
    {
        this.unitDAO = unitDAO;
    }

    /**
     * @return the checkViewDAO
     */
    public CheckViewDAO getCheckViewDAO()
    {
        return checkViewDAO;
    }

    /**
     * @param checkViewDAO
     *            the checkViewDAO to set
     */
    public void setCheckViewDAO(CheckViewDAO checkViewDAO)
    {
        this.checkViewDAO = checkViewDAO;
    }

    /**
     * @return the outManager
     */
    public OutManager getOutManager()
    {
        return outManager;
    }

    /**
     * @param outManager
     *            the outManager to set
     */
    public void setOutManager(OutManager outManager)
    {
        this.outManager = outManager;
    }

    /**
     * @return the financeManager
     */
    public FinanceManager getFinanceManager()
    {
        return financeManager;
    }

    /**
     * @param financeManager
     *            the financeManager to set
     */
    public void setFinanceManager(FinanceManager financeManager)
    {
        this.financeManager = financeManager;
    }

    /**
     * @return the unitViewDAO
     */
    public UnitViewDAO getUnitViewDAO()
    {
        return unitViewDAO;
    }

    /**
     * @param unitViewDAO
     *            the unitViewDAO to set
     */
    public void setUnitViewDAO(UnitViewDAO unitViewDAO)
    {
        this.unitViewDAO = unitViewDAO;
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
     * @return the principalshipDAO
     */
    public PrincipalshipDAO getPrincipalshipDAO()
    {
        return principalshipDAO;
    }

    /**
     * @param principalshipDAO
     *            the principalshipDAO to set
     */
    public void setPrincipalshipDAO(PrincipalshipDAO principalshipDAO)
    {
        this.principalshipDAO = principalshipDAO;
    }

}
