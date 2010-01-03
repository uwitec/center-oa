/**
 * File Name: StafferAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.action;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.common.query.QueryConditionBean;
import com.china.center.common.query.QueryConfig;
import com.china.center.common.query.QueryItemBean;
import com.china.center.common.taglib.MapBean;
import com.china.center.common.taglib.PageSelectOption;
import com.china.center.oa.budget.bean.FeeItemBean;
import com.china.center.oa.budget.dao.BudgetDAO;
import com.china.center.oa.budget.dao.BudgetItemDAO;
import com.china.center.oa.budget.dao.FeeItemDAO;
import com.china.center.oa.budget.vo.BudgetVO;
import com.china.center.oa.credit.bean.CreditLevelBean;
import com.china.center.oa.credit.dao.CreditItemSecDAO;
import com.china.center.oa.credit.dao.CreditLevelDAO;
import com.china.center.oa.credit.vo.CreditItemSecVO;
import com.china.center.oa.helper.Helper;
import com.china.center.oa.helper.LocationHelper;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.DepartmentBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.PostBean;
import com.china.center.oa.publics.bean.ProvinceBean;
import com.china.center.oa.publics.bean.RoleBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.dao.DepartmentDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.PostDAO;
import com.china.center.oa.publics.dao.PrincipalshipDAO;
import com.china.center.oa.publics.dao.ProvinceDAO;
import com.china.center.oa.publics.dao.RoleDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.manager.LocationManager;
import com.china.center.oa.publics.manager.OrgManager;
import com.china.center.oa.publics.wrap.StafferOrgWrap;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.JSONTools;
import com.china.center.tools.StringTools;
import com.url.ajax.json.JSONObject;


/**
 * 公共查询 PublicQueryAction
 * 
 * @author ZHUZHU
 * @version 2009-2-17
 * @see PublicQueryAction
 * @since 1.0
 */
public class PublicQueryAction extends DispatchAction
{
    private StafferDAO stafferDAO = null;

    private OrgManager orgManager = null;

    private LocationManager locationManager = null;

    private LocationDAO locationDAO = null;

    private PrincipalshipDAO principalshipDAO = null;

    private DepartmentDAO departmentDAO = null;

    private PostDAO postDAO = null;

    private CreditItemSecDAO creditItemSecDAO = null;

    private BudgetItemDAO budgetItemDAO = null;

    private FeeItemDAO feeItemDAO = null;

    private BudgetDAO budgetDAO = null;

    private RoleDAO roleDAO = null;

    private ProvinceDAO provinceDAO = null;

    private CreditLevelDAO creditLevelDAO = null;

    private QueryConfig queryConfig = null;

    /**
     * default constructor
     */
    public PublicQueryAction()
    {}

    /**
     * popStafferQuery
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward popCommonQuery(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        // 在xml里面定义的
        String key = request.getParameter("key");

        // name是用于如何定义查询是使用同一个，但是在内存里面保存的查询条件不一样
        String name = request.getParameter("name");

        QueryItemBean query = queryConfig.findQueryCondition(key);

        if (query == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有配置查询,请核实");

            return mapping.findForward("error");
        }

        if (StringTools.isNullOrNone(name))
        {
            name = query.getName();
        }

        request.setAttribute("queryName", name);

        Map<String, List> selectMap = new HashMap<String, List>();
        request.setAttribute("selectMap", selectMap);
        request.setAttribute("query", query);

        User user = Helper.getUser(request);

        List<QueryConditionBean> condition = query.getConditions();

        for (QueryConditionBean queryConditionBean : condition)
        {
            if ("select".equals(queryConditionBean.getType()))
            {
                setSelectOption(user, queryConditionBean.getOption(), selectMap);
            }
        }

        return mapping.findForward("commonQuery");
    }

    /**
     * popCommonQuery2
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward popCommonQuery2(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        // 在XML里面定义的
        String key = request.getParameter("key");

        // name是用于如何定义查询是使用同一个，但是在内存里面保存的查询条件不一样
        String name = request.getParameter("name");

        QueryItemBean query = queryConfig.findQueryCondition(key);

        if (query == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有配置查询,请核实");

            return mapping.findForward("error");
        }

        if (StringTools.isNullOrNone(name))
        {
            name = query.getName();
        }

        request.setAttribute("queryName", name);

        Map<String, List> selectMap = new HashMap<String, List>();
        request.setAttribute("selectMap", selectMap);
        request.setAttribute("query", query);

        User user = Helper.getUser(request);

        List<QueryConditionBean> condition = query.getConditions();

        for (QueryConditionBean queryConditionBean : condition)
        {
            if ("select".equals(queryConditionBean.getType()))
            {
                setSelectOption(user, queryConditionBean.getOption(), selectMap);
            }
        }

        StringBuilder sb = new StringBuilder();

        buildQueryHtml(request, name, query, selectMap, sb);

        Map<String, String> sbMap = new HashMap();

        sbMap.put("key", sb.toString());

        JSONObject object = new JSONObject(sbMap);

        return JSONTools.writeResponse(response, object.toString());
    }

    /**
     * buildQueryHtml
     * 
     * @param request
     * @param name
     * @param query
     * @param selectMap
     * @param sb
     */
    private void buildQueryHtml(HttpServletRequest request, String name, QueryItemBean query,
                                Map<String, List> selectMap, StringBuilder sb)
    {
        sb.append("<input type=hidden name=load value=1/>");

        // write JAVA object to string
        List<QueryConditionBean> hidenConditions = query.getConditions();

        for (QueryConditionBean eachItem : hidenConditions)
        {
            sb.append(StringTools.format("<input type=hidden name='hidden_query_%s' value='%s'/>",
                eachItem.getName(), eachItem.getAssistant()));
        }

        String pkey = name + "_pmap";

        Map ppmap = (Map)request.getSession().getAttribute(pkey);

        if (ppmap == null)
        {
            ppmap = new HashMap();
        }

        sb.append("<table align='center' width='98%' cellpadding='0' id='default_table' cellspacing='1' class='table0'>");

        for (int i = 0; i < hidenConditions.size(); i++ )
        {
            QueryConditionBean eachItem = hidenConditions.get(i);

            String value = StringTools.print((String)ppmap.get(eachItem.getName()));

            sb.append("<tr class='content1'>");

            sb.append(StringTools.format("<td width='20%%' align='left'>%s：</td>",
                eachItem.getCaption()));
            sb.append("<td width=80% >");

            if ("text".equals(eachItem.getType()))
            {
                sb.append(StringTools.format(
                    "<input type=text name='%s' id='%s' size=35 onkeypress='enterKeyPress(querySure)' frister=%s %s value='%s'/>",
                    eachItem.getName(), eachItem.getName(), (i == 0 ? "1" : "0"),
                    eachItem.getInner(), value));
            }
            else if ("select".equals(eachItem.getType()))
            {
                sb.append(StringTools.format(
                    "<select name='%s' id='%s' quick=true %s class='select_class' values='%s' style='width:250px'>",
                    eachItem.getName(), eachItem.getName(), eachItem.getInner(),
                    StringTools.print((String)ppmap.get(eachItem.getName()))));

                sb.append("<option value=''>--</option>");

                List optionList = selectMap.get(eachItem.getOption());

                for (Object optionItem : optionList)
                {
                    sb.append(StringTools.format("<option value='%s'>%s</option>",
                        BeanUtil.getProperty(optionItem, "id"), BeanUtil.getProperty(optionItem,
                            "name")));
                }
                sb.append("</select>");
            }
            else if ("date".equals(eachItem.getType()))
            {
                sb.append(StringTools.format(
                    "<input type=text name = '%s' id='%s'  value = '%s' %s readonly=readonly >"
                        + "<img src='%s/images/calendar.gif' style='cursor: pointer' title='请选择时间' align='top' onclick='return calDate(\"%s\");' height='20px' width='20px'/>",
                    eachItem.getName(), eachItem.getName(), value, eachItem.getInner(),
                    request.getContextPath(), eachItem.getName()));
            }
            else if ("datetime".equals(eachItem.getType()))
            {
                sb.append(StringTools.format(
                    "<input type=text name = '%s' id='%s'  value = '%s' %s readonly=readonly >"
                        + "<img src='%s/images/calendar.gif' style='cursor: pointer' title='请选择时间' align='top' onclick='return calDateTime(\"%s\");' height='20px' width='20px'/>",
                    eachItem.getName(), eachItem.getName(), value, eachItem.getInner(),
                    request.getContextPath(), eachItem.getName()));
            }

            sb.append("</td>");
            sb.append("</tr>");
        }

        sb.append("</table>");
    }

    /**
     * 设置set
     * 
     * @param user
     * @param key
     * @param selectMap
     */
    private void setSelectOption(User user, String key, Map<String, List> selectMap)
    {
        if (StringTools.isNullOrNone(key))
        {
            return;
        }

        // $就是全局的
        if (key.startsWith("$"))
        {
            List list = PageSelectOption.optionMap.get(key.substring(1));

            if (list != null)
            {
                selectMap.put(key, list);

                return;
            }
        }

        // $locationList
        if ("$locationlist".equalsIgnoreCase(key))
        {
            List<LocationBean> locationList = locationDAO.listEntityBeans();

            selectMap.put(key, locationList);

            return;
        }

        // $postList
        if ("$postList".equalsIgnoreCase(key))
        {
            List<PostBean> postList = postDAO.listEntityBeans();

            selectMap.put(key, postList);

            return;
        }

        // $examType
        if ("$examType".equalsIgnoreCase(key))
        {
            List<MapBean> el = PageSelectOption.optionMap.get("examType");

            selectMap.put(key, el);

            return;
        }

        // $departmentList
        if ("$departmentList".equalsIgnoreCase(key))
        {
            List<DepartmentBean> departmentList = departmentDAO.listEntityBeans();

            selectMap.put(key, departmentList);

            return;
        }

        // 列举全部的省
        if ("$provinceList".equals(key))
        {
            List<ProvinceBean> list = provinceDAO.listEntityBeans();

            selectMap.put(key, list);

            return;
        }

        // $staffer_location 区域下的职员
        if ("$staffer_location".equals(key))
        {
            List<StafferBean> list = null;
            if (LocationHelper.isVirtualLocation(user.getLocationId()))
            {
                list = stafferDAO.listEntityBeans();
            }
            else
            {
                list = stafferDAO.queryStafferByLocationId(user.getLocationId());
            }

            selectMap.put(key, list);

            return;
        }

        if ("$staffer".equals(key))
        {
            List<StafferBean> list = stafferDAO.listEntityBeans();

            selectMap.put(key, list);

            return;
        }

        if ("$feeItemList".equals(key))
        {
            List<FeeItemBean> list = feeItemDAO.listEntityBeans();

            selectMap.put(key, list);

            return;
        }

        if ("$releaseBudget".equals(key))
        {
            List<BudgetVO> list = budgetDAO.queryCurrentRunBudget();

            selectMap.put(key, list);

            return;
        }

        if ("$creditLevel".equals(key))
        {
            List<CreditLevelBean> list = creditLevelDAO.listEntityBeans();

            selectMap.put(key, list);

            return;
        }

        if ("$creditItemSecList".equals(key))
        {
            List<CreditItemSecVO> list = creditItemSecDAO.listEntityVOs();

            selectMap.put(key, list);

            return;
        }

        // 自己和下属
        if ("$staffer_belong".equals(key))
        {
            StafferBean sb = stafferDAO.find(user.getStafferId());

            if (sb == null)
            {
                return;
            }

            List<StafferOrgWrap> list;
            try
            {
                list = orgManager.queryAllSubStaffer(sb.getPrincipalshipId());
                selectMap.put(key, list);
            }
            catch (MYException e)
            {}

            return;
        }

        // $role_location 区域下的角色
        if ("$role_location".equals(key))
        {
            List<RoleBean> list = null;

            if (LocationHelper.isVirtualLocation(user.getLocationId()))
            {
                list = roleDAO.listEntityBeans();
            }
            else
            {
                list = roleDAO.queryRoleByLocationId(user.getLocationId());
            }

            selectMap.put(key, list);

            return;
        }
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
     * @return the locationManager
     */
    public LocationManager getLocationManager()
    {
        return locationManager;
    }

    /**
     * @param locationManager
     *            the locationManager to set
     */
    public void setLocationManager(LocationManager locationManager)
    {
        this.locationManager = locationManager;
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
     * @return the postDAO
     */
    public PostDAO getPostDAO()
    {
        return postDAO;
    }

    /**
     * @param postDAO
     *            the postDAO to set
     */
    public void setPostDAO(PostDAO postDAO)
    {
        this.postDAO = postDAO;
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

    /**
     * @return the roleDAO
     */
    public RoleDAO getRoleDAO()
    {
        return roleDAO;
    }

    /**
     * @param roleDAO
     *            the roleDAO to set
     */
    public void setRoleDAO(RoleDAO roleDAO)
    {
        this.roleDAO = roleDAO;
    }

    /**
     * @return the provinceDAO
     */
    public ProvinceDAO getProvinceDAO()
    {
        return provinceDAO;
    }

    /**
     * @param provinceDAO
     *            the provinceDAO to set
     */
    public void setProvinceDAO(ProvinceDAO provinceDAO)
    {
        this.provinceDAO = provinceDAO;
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
     * @return the budgetItemDAO
     */
    public BudgetItemDAO getBudgetItemDAO()
    {
        return budgetItemDAO;
    }

    /**
     * @param budgetItemDAO
     *            the budgetItemDAO to set
     */
    public void setBudgetItemDAO(BudgetItemDAO budgetItemDAO)
    {
        this.budgetItemDAO = budgetItemDAO;
    }

    /**
     * @return the budgetDAO
     */
    public BudgetDAO getBudgetDAO()
    {
        return budgetDAO;
    }

    /**
     * @param budgetDAO
     *            the budgetDAO to set
     */
    public void setBudgetDAO(BudgetDAO budgetDAO)
    {
        this.budgetDAO = budgetDAO;
    }

    /**
     * @return the feeItemDAO
     */
    public FeeItemDAO getFeeItemDAO()
    {
        return feeItemDAO;
    }

    /**
     * @param feeItemDAO
     *            the feeItemDAO to set
     */
    public void setFeeItemDAO(FeeItemDAO feeItemDAO)
    {
        this.feeItemDAO = feeItemDAO;
    }

    /**
     * @return the creditItemSecDAO
     */
    public CreditItemSecDAO getCreditItemSecDAO()
    {
        return creditItemSecDAO;
    }

    /**
     * @param creditItemSecDAO
     *            the creditItemSecDAO to set
     */
    public void setCreditItemSecDAO(CreditItemSecDAO creditItemSecDAO)
    {
        this.creditItemSecDAO = creditItemSecDAO;
    }

    /**
     * @return the creditLevelDAO
     */
    public CreditLevelDAO getCreditLevelDAO()
    {
        return creditLevelDAO;
    }

    /**
     * @param creditLevelDAO
     *            the creditLevelDAO to set
     */
    public void setCreditLevelDAO(CreditLevelDAO creditLevelDAO)
    {
        this.creditLevelDAO = creditLevelDAO;
    }
}
