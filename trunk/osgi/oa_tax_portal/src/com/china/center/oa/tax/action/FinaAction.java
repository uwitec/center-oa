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
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.DepartmentBean;
import com.china.center.oa.publics.bean.DutyBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.DepartmentDAO;
import com.china.center.oa.publics.dao.DutyDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.tax.bean.CheckViewBean;
import com.china.center.oa.tax.bean.FinanceBean;
import com.china.center.oa.tax.bean.FinanceItemBean;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.bean.UnitBean;
import com.china.center.oa.tax.constanst.TaxConstanst;
import com.china.center.oa.tax.dao.CheckViewDAO;
import com.china.center.oa.tax.dao.FinanceDAO;
import com.china.center.oa.tax.dao.FinanceItemDAO;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.oa.tax.dao.UnitDAO;
import com.china.center.oa.tax.facade.TaxFacade;
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

    private DepartmentDAO departmentDAO = null;

    private StafferDAO stafferDAO = null;

    private FinanceDAO financeDAO = null;

    private CheckViewDAO checkViewDAO = null;

    private FinanceItemDAO financeItemDAO = null;

    private static final String QUERYFINANCE = "queryFinance";

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
            this.financeDAO);

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
            this.checkViewDAO, new HandleResult<CheckViewBean>()
            {
                public void handle(CheckViewBean obj)
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

        String alogTime = request.getParameter("alogTime");

        String blogTime = request.getParameter("blogTime");

        if (StringTools.isNullOrNone(alogTime) && StringTools.isNullOrNone(blogTime))
        {
            changeMap.put("alogTime", TimeTools.now( -90));

            changeMap.put("blogTime", TimeTools.now(1));

            condtion.addCondition("FinanceBean.logTime", ">=", TimeTools.now( -90));

            condtion.addCondition("FinanceBean.logTime", "<=", TimeTools.now(1));
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

            String reason = request.getParameter("reason");

            User user = Helper.getUser(request);

            taxFacade.checks(user.getId(), id, reason);

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

        FinanceVO bean = financeDAO.findVO(id);

        if (bean == null)
        {
            return ActionTools.toError("数据异常,请重新操作", mapping, request);
        }

        List<FinanceItemVO> voList = financeItemDAO.queryEntityVOsByFK(id);

        for (FinanceItemVO item : voList)
        {
            TaxBean tax = taxDAO.find(item.getTaxId());

            item.setForward(tax.getForward());

            item.setTaxName(tax.getCode() + tax.getName());

            if (tax.getDepartment() == TaxConstanst.TAX_CHECK_YES)
            {
                DepartmentBean depart = departmentDAO.find(item.getDepartmentId());

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
        }

        bean.setItemVOList(voList);

        request.setAttribute("bean", bean);

        return mapping.findForward("detailFinance");
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
        String[] departmentIds = request.getParameterValues("departmentId");
        String[] idescriptions = request.getParameterValues("idescription");
        String[] taxIds = request.getParameterValues("taxId2");
        String[] stafferId2s = request.getParameterValues("stafferId2");
        String[] unitId2s = request.getParameterValues("unitId2");
        String[] inmoneys = request.getParameterValues("inmoney");
        String[] outmoneys = request.getParameterValues("outmoney");

        List<FinanceItemBean> itemList = new ArrayList<FinanceItemBean>();

        double inTotal = 0.0d;

        double outTotal = 0.0d;

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

            if (tax.getStaffer() == TaxConstanst.TAX_CHECK_YES)
            {
                if (StringTools.isNullOrNone(stafferId2s[i]))
                {
                    throw new MYException("科目[%s]职员不能为空,请重新操作", tax.getCode() + tax.getName());
                }

                item.setStafferId(stafferId2s[i]);
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

            item.setName(idescriptions[i]);

            item.setType(bean.getType());

            item.setPareId(pareId);

            if (item.getForward() == TaxConstanst.TAX_FORWARD_IN)
            {
                item.setInmoney(MathTools.parseDouble(inmoneys[i]));
            }
            else
            {
                item.setOutmoney(MathTools.parseDouble(outmoneys[i]));
            }

            inTotal += item.getInmoney();

            outTotal += item.getOutmoney();

            if (inTotal == outTotal && outTotal != 0.0)
            {
                inTotal = 0.0d;

                outTotal = 0.0d;

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
        List<TaxBean> taxList = taxDAO.listEntityBeans("order by TaxBean.code asc");

        request.setAttribute("taxList", taxList);

        List<DutyBean> dutyList = dutyDAO.listEntityBeans();

        request.setAttribute("dutyList", dutyList);

        List<DepartmentBean> departmentBeanList = departmentDAO.listEntityBeans();

        request.setAttribute("departmentBeanList", departmentBeanList);

        JSONArray object = new JSONArray(taxList, false);

        request.setAttribute("taxListStr", object.toString());

        return mapping.findForward("addFinance");
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
     * @return the departmentDAO
     */
    public DepartmentDAO getDepartmentDAO()
    {
        return departmentDAO;
    }

    /**
     * @param departmentDAO
     *            the departmentDAO to set
     */
    public void setDepartmentDAO(DepartmentDAO departmentDAO)
    {
        this.departmentDAO = departmentDAO;
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

}
