/**
 * File Name: WorkLogAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-16<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.worklog.action;


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

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.common.PageSeparateTools;
import com.china.center.common.json.AjaxResult;
import com.china.center.common.query.CommonQuery;
import com.china.center.common.query.QueryConfig;
import com.china.center.oa.constant.AuthConstant;
import com.china.center.oa.constant.EnumConstant;
import com.china.center.oa.constant.WorkLogConstant;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.customer.helper.CustomerHelper;
import com.china.center.oa.facade.WorkLogFacade;
import com.china.center.oa.helper.Helper;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.EnumBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.dao.EnumDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.oa.worklog.bean.VisitBean;
import com.china.center.oa.worklog.bean.WorkLogBean;
import com.china.center.oa.worklog.dao.VisitDAO;
import com.china.center.oa.worklog.dao.WorkLogDAO;
import com.china.center.oa.worklog.manager.WorkLogManager;
import com.china.center.oa.worklog.vo.WorkLogVO;
import com.china.center.oa.worklog.wrap.StatWorkLogWrap;
import com.china.center.tools.ActionTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.JSONTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * WorkLogAction
 * 
 * @author zhuzhu
 * @version 2009-2-16
 * @see WorkLogAction
 * @since 1.0
 */
public class WorkLogAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private WorkLogFacade workLogFacade = null;

    private UserManager userManager = null;

    private WorkLogManager workLogManager = null;

    private WorkLogDAO workLogDAO = null;

    private CustomerDAO customerDAO = null;

    private VisitDAO visitDAO = null;

    private StafferDAO stafferDAO = null;

    private LocationDAO locationDAO = null;

    private EnumDAO enumDAO = null;

    private static String QUERYWORKLOG = "queryWorkLog";

    private static String QUERYWORKCUSTOMER = "queryWorkCustomer";

    private QueryConfig queryConfig = null;

    /**
     * 查询日志
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryWorkLog(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        String stafferId = request.getParameter("stafferId");

        if (StringTools.isNullOrNone(stafferId))
        {
            condtion.addCondition("WorkLogBean.stafferId", "=", user.getStafferId());
        }

        ActionTools.processJSONQueryCondition(QUERYWORKLOG, request, condtion);

        condtion.addCondition("order by WorkLogBean.logTime desc");

        List list = ActionTools.selfCommonQueryBeanInnerByJSON(QUERYWORKLOG, request, condtion,
            new CommonQuery()
            {
                public int getCount(String key, HttpServletRequest request,
                                    ConditionParse condition)
                {
                    return workLogDAO.countWorkLogByConstion(condition);
                }

                public String getOrderPfix(String key, HttpServletRequest request)
                {
                    return "WorkLogBean";
                }

                public List queryResult(String key, HttpServletRequest request,
                                        ConditionParse queryCondition)
                {
                    return workLogDAO.queryWorkLogByConstion(PageSeparateTools.getCondition(
                        request, key), PageSeparateTools.getPageSeparate(request, key));
                }

                public String getSortname(HttpServletRequest request)
                {
                    return request.getParameter(ActionTools.SORTNAME);
                }
            });

        // 处理是星期的问题
        proceeWeek(list);

        String jsonstr = JSONTools.getJSONString(list, PageSeparateTools.getPageSeparate(request,
            QUERYWORKLOG));

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * query worklog visit
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryWorkCustomer(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        final ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        String stafferId = request.getParameter("stafferId");

        // if do not choose staffer and do not has WORKLOG_GBOAL_QUERY
        if (StringTools.isNullOrNone(stafferId)
            && !userManager.containAuth(user, AuthConstant.WORKLOG_GBOAL_QUERY))
        {
            condtion.addCondition("WorkLogBean.stafferId", "=", user.getStafferId());
        }
        else if (ActionTools.isNullQuery(request, queryConfig, QUERYWORKCUSTOMER)
                 && userManager.containAuth(user, AuthConstant.WORKLOG_GBOAL_QUERY))
        {
            condtion.addCondition("WorkLogBean.logTime", ">=", TimeTools.now( -30));
        }

        ActionTools.processJSONQueryCondition(QUERYWORKCUSTOMER, request, condtion);

        condtion.addCondition("order by WorkLogBean.logTime desc");

        List list = ActionTools.selfCommonQueryBeanInnerByJSON(QUERYWORKCUSTOMER, request,
            condtion, new CommonQuery()
            {
                public int getCount(String key, HttpServletRequest request,
                                    ConditionParse condition)
                {
                    return visitDAO.countWorkItemByConstion(condition);
                }

                public String getOrderPfix(String key, HttpServletRequest request)
                {
                    return "WorkLogBean";
                }

                public List queryResult(String key, HttpServletRequest request,
                                        ConditionParse queryCondition)
                {
                    return visitDAO.queryWorkItemByConstion(PageSeparateTools.getCondition(
                        request, key), PageSeparateTools.getPageSeparate(request, key));
                }

                public String getSortname(HttpServletRequest request)
                {
                    return request.getParameter(ActionTools.SORTNAME);
                }
            });

        String jsonstr = JSONTools.getJSONString(list, PageSeparateTools.getPageSeparate(request,
            QUERYWORKCUSTOMER));

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * 处理星期
     * 
     * @param list
     */
    private void proceeWeek(List<WorkLogVO> list)
    {
        for (WorkLogVO workLogVO : list)
        {
            workLogVO.setWeek(TimeTools.getWeekDay(workLogVO.getWorkDate()));
        }
    }

    /**
     * preForStatWorkLog
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForStatWorkLog(ActionMapping mapping, ActionForm form,
                                           HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        createRefResource(request);

        String beginDate = TimeTools.getDateShortString( -30);

        request.setAttribute("beginDate", beginDate);

        return mapping.findForward("queryStatWorkLog");
    }

    /**
     * @param request
     */
    private void createRefResource(HttpServletRequest request)
    {
        // get all staffer and location
        List<StafferBean> stafferList = stafferDAO.listCommonEntityBeans();

        List<LocationBean> locationList = locationDAO.listEntityBeans();

        request.setAttribute("stafferList", stafferList);

        request.setAttribute("locationList", locationList);
    }

    /**
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryStatWorkLog(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        // save parameter
        CommonTools.saveParamers(request);

        String beginDate = request.getParameter("beginDate");
        String endDate = request.getParameter("endDate");
        String locationId = request.getParameter("locationId");
        String stafferId = request.getParameter("stafferId");

        List<StatWorkLogWrap> statWorkLogList = workLogManager.queryStatWorkLogWrap(beginDate,
            endDate, locationId, stafferId);

        request.setAttribute("statWorkLogList", statWorkLogList);

        createRefResource(request);

        return mapping.findForward("queryStatWorkLog");
    }

    /**
     * 增加日志
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addOrUpdateWorkLog(ActionMapping mapping, ActionForm form,
                                            HttpServletRequest request,
                                            HttpServletResponse response)
        throws ServletException
    {
        WorkLogBean bean = new WorkLogBean();

        String opr = request.getParameter("opr");

        String update = request.getParameter("update");

        try
        {
            BeanUtil.getBean(bean, request);

            User user = Helper.getUser(request);

            bean.setStafferId(user.getStafferId());

            if ("0".equals(opr))
            {
                bean.setStatus(WorkLogConstant.WORKLOG_STATUS_INIT);
            }
            else
            {
                bean.setStatus(WorkLogConstant.WORKLOG_STATUS_SUBMIT);
            }

            createVisit(request, bean);

            if ("1".equals(update))
            {
                workLogFacade.updateWorkLog(user.getId(), bean);
            }
            else
            {
                workLogFacade.addWorkLog(user.getId(), bean);
            }

            request.setAttribute(KeyConstant.MESSAGE, "成功保存日志");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "保存日志失败:" + e.getMessage());
        }

        return mapping.findForward("queryWorkLog");
    }

    /**
     * 处理
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward processWorkLog(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String operation = request.getParameter("operation");

        String resultMsg = "";

        AjaxResult ajax = new AjaxResult();

        try
        {
            User user = Helper.getUser(request);

            if ("0".equals(operation))
            {
                workLogFacade.submitWorkLog(user.getId(), id);
            }

            if ("1".equals(operation))
            {
                workLogFacade.delWorkLog(user.getId(), id);
            }

            resultMsg = "成功操作";
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            resultMsg = "操作失败:" + e.getMessage();

            ajax.setError();
        }

        ajax.setMsg(resultMsg);

        return JSONTools.writeResponse(response, ajax);
    }

    /**
     * find日志
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findWorkLog(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String update = request.getParameter("update");

        WorkLogVO bean = workLogDAO.findVO(id);

        if (bean == null)
        {
            return ActionTools.toError("日志不存在", mapping, request);
        }

        bean.setVisits(visitDAO.queryEntityBeansByFK(id));

        request.setAttribute("bean", bean);

        if ("1".equals(update))
        {
            User user = Helper.getUser(request);

            if ( !user.getStafferId().equals(bean.getStafferId()))
            {
                return ActionTools.toError("没有权限", mapping, request);
            }

            return mapping.findForward("updateWorkLog");
        }

        return mapping.findForward("detailWorkLog");
    }

    /**
     * 构建Visit
     * 
     * @param request
     * @param bean
     * @throws MYException
     */
    private void createVisit(HttpServletRequest request, WorkLogBean bean)
        throws MYException
    {
        List<VisitBean> visits = new ArrayList<VisitBean>();

        bean.setVisits(visits);

        String[] workTypes = request.getParameterValues("workType");

        String[] targerIds = request.getParameterValues("targerId");

        String[] beginTimes = request.getParameterValues("beginTime");

        String[] endTimes = request.getParameterValues("endTime");

        String[] results = request.getParameterValues("result");

        String[] nextWorks = request.getParameterValues("nextWork");

        String[] nextDates = request.getParameterValues("nextDate");

        String[] descriptions = request.getParameterValues("description");

        if (workTypes.length != targerIds.length || workTypes.length != beginTimes.length
            || workTypes.length != endTimes.length || workTypes.length != results.length)
        {
            throw new MYException("数据不完备");
        }

        for (int i = 0; i < workTypes.length; i++ )
        {
            if (StringTools.isNullOrNone(targerIds[i]))
            {
                throw new MYException("数据不完备,请重新操作");
            }

            VisitBean visit = new VisitBean();

            visit.setWorkType(CommonTools.parseInt(workTypes[i]));

            visit.setResult(CommonTools.parseInt(results[i]));

            EnumBean en = enumDAO.findByTypeAndEnumIndex(EnumConstant.WORKLOG_TYPE, workTypes[i]);

            if (en != null)
            {
                visit.setWorkTypeName(en.getValue());
            }

            visit.setTargerId(targerIds[i]);

            CustomerBean customer = customerDAO.find(targerIds[i]);

            if (customer == null)
            {
                throw new MYException("数据不完备,客户不存在");
            }

            // 解密
            CustomerHelper.decryptCustomer(customer);

            visit.setTargerName(customer.getName());

            visit.setHandPhone(StringTools.truncate2(customer.getHandphone(), 30));

            visit.setTel(customer.getTel());

            // 拜访的是到底是新的还是老的
            visit.setTargerAtt(customer.getNewtype());

            visit.setBeginTime(beginTimes[i]);

            visit.setEndTime(endTimes[i]);

            if (nextWorks.length > i)
            {
                visit.setNextWork(nextWorks[i]);
            }

            if (nextDates.length > i)
            {
                visit.setNextDate(nextDates[i]);
            }

            if (descriptions.length > i)
            {
                visit.setDescription(descriptions[i]);
            }

            visits.add(visit);
        }
    }

    /**
     * default constructor
     */
    public WorkLogAction()
    {}

    public WorkLogFacade getWorkLogFacade()
    {
        return workLogFacade;
    }

    public void setWorkLogFacade(WorkLogFacade workLogFacade)
    {
        this.workLogFacade = workLogFacade;
    }

    public WorkLogDAO getWorkLogDAO()
    {
        return workLogDAO;
    }

    public void setWorkLogDAO(WorkLogDAO workLogDAO)
    {
        this.workLogDAO = workLogDAO;
    }

    public VisitDAO getVisitDAO()
    {
        return visitDAO;
    }

    public void setVisitDAO(VisitDAO visitDAO)
    {
        this.visitDAO = visitDAO;
    }

    public EnumDAO getEnumDAO()
    {
        return enumDAO;
    }

    public void setEnumDAO(EnumDAO enumDAO)
    {
        this.enumDAO = enumDAO;
    }

    public CustomerDAO getCustomerDAO()
    {
        return customerDAO;
    }

    public void setCustomerDAO(CustomerDAO customerDAO)
    {
        this.customerDAO = customerDAO;
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

    /**
     * @return the workLogManager
     */
    public WorkLogManager getWorkLogManager()
    {
        return workLogManager;
    }

    /**
     * @param workLogManager
     *            the workLogManager to set
     */
    public void setWorkLogManager(WorkLogManager workLogManager)
    {
        this.workLogManager = workLogManager;
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
     * @return the queryConfig
     */
    public QueryConfig getQueryConfig()
    {
        return queryConfig;
    }

    /**
     * @param queryConfig
     *            the queryConfig to set
     */
    public void setQueryConfig(QueryConfig queryConfig)
    {
        this.queryConfig = queryConfig;
    }
}
