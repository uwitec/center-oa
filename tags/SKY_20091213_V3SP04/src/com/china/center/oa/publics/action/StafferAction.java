/**
 * File Name: StafferAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.action;


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

import com.china.center.common.ConditionParse;
import com.china.center.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.common.query.HandleResult;
import com.china.center.common.query.QueryConfig;
import com.china.center.common.query.QueryItemBean;
import com.china.center.common.taglib.MapBean;
import com.china.center.common.taglib.PageSelectOption;
import com.china.center.oa.facade.PublicFacade;
import com.china.center.oa.helper.Helper;
import com.china.center.oa.helper.LocationHelper;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.DepartmentBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.PostBean;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.dao.DepartmentDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.PostDAO;
import com.china.center.oa.publics.dao.PrincipalshipDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.helper.StafferHelper;
import com.china.center.oa.publics.manager.LocationManager;
import com.china.center.oa.publics.vo.StafferVO;
import com.china.center.tools.ActionTools;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.china.center.tools.DecSecurity;
import com.china.center.tools.JSONTools;
import com.china.center.tools.RandomTools;
import com.china.center.tools.Security;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


public class StafferAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private StafferDAO stafferDAO = null;

    private LocationManager locationManager = null;

    private LocationDAO locationDAO = null;

    private PrincipalshipDAO principalshipDAO = null;

    private DepartmentDAO departmentDAO = null;

    private PostDAO postDAO = null;

    private QueryConfig queryConfig = null;

    private PublicFacade publicFacade = null;

    private static String QUERYSTAFFER = "queryStaffer";

    /**
     * default constructor
     */
    public StafferAction()
    {}

    /**
     * queryStaffer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryStaffer(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        ConditionParse condtion = new ConditionParse();

        condtion.addWhereStr();

        User user = Helper.getUser(request);

        if ( !LocationHelper.isVirtualLocation(user.getLocationId())
            && !LocationHelper.isSystemLocation(user.getLocationId()))
        {
            condtion.addCondition("StafferBean.locationId", "=", user.getLocationId());
        }

        // condtion.addIntCondition("StafferBean.status", "=", StafferConstant.STATUS_COMMON);

        ActionTools.processJSONQueryCondition(QUERYSTAFFER, request, condtion);

        String jsonstr = ActionTools.queryVOByJSONAndToString(QUERYSTAFFER, request, condtion,
            this.stafferDAO, new HandleResult<StafferVO>()
            {
                public void handle(StafferVO vo)
                {
                    if (StafferHelper.hasEnc(vo))
                    {
                        vo.setEnc("设置");
                    }
                    else
                    {
                        vo.setEnc("未设置");
                    }
                }
            });

        return JSONTools.writeResponse(response, jsonstr);
    }

    /**
     * preForAddStaffer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddStaffer(ActionMapping mapping, ActionForm form,
                                          HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        User user = Helper.getUser(request);

        if (LocationHelper.isVirtualLocation(user.getLocationId()))
        {
            List<LocationBean> locationList = locationDAO.listEntityBeans();

            request.setAttribute("locationList", locationList);
        }
        else
        {
            List<LocationBean> locationList = new ArrayList<LocationBean>();

            locationList.add(locationDAO.find(user.getLocationId()));

            request.setAttribute("locationList", locationList);
        }

        List<PrincipalshipBean> priList = principalshipDAO.listEntityBeans();
        request.setAttribute("priList", priList);

        List<DepartmentBean> depList = departmentDAO.listEntityBeans();
        request.setAttribute("depList", depList);

        List<PostBean> postList = postDAO.listEntityBeans();
        request.setAttribute("postList", postList);

        return mapping.findForward("addStaffer");
    }

    /**
     * preForSetpwkey
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForSetpwkey(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        StafferVO bean = stafferDAO.findVO(id);

        request.setAttribute("bean", bean);

        if (StafferHelper.hasEnc(bean))
        {
            request.setAttribute("hasSet", true);
        }
        else
        {
            request.setAttribute("hasSet", false);
        }

        return mapping.findForward("updatePwkey");
    }

    /**
     * updatePwkey
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updatePwkey(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String key = request.getParameter("key").toUpperCase();

        String md5key = Security.getSecurity(key);

        String deskeygen = md5key.substring(0, 4)
                           + md5key.substring(md5key.length() - 4, md5key.length());

        StafferBean bean = stafferDAO.find(id);

        if (bean == null)
        {
            return ActionTools.toError("职员不存在", "queryStaffer", mapping, request);
        }

        String enKey = RandomTools.getRandomMumber(32);

        bean.setPwkey(DecSecurity.encrypt(enKey, deskeygen));

        try
        {
            User user = Helper.getUser(request);

            publicFacade.updateStafferPwkey(user, bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功设置加密锁:" + enKey + ".请拷贝32位锁至KeyTool进行设置");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "修改加密锁失败:" + e.getMessage());
        }

        return preForSetpwkey(mapping, form, request, response);
    }

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
    public ActionForward popStafferCommonQuery(ActionMapping mapping, ActionForm form,
                                               HttpServletRequest request,
                                               HttpServletResponse response)
        throws ServletException
    {
        QueryItemBean query = queryConfig.findQueryCondition(QUERYSTAFFER);

        if (query == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "没有配置查询,请核实");

            return mapping.findForward("queryStaffer");
        }

        // postList
        List<PostBean> postList = postDAO.listEntityBeans();

        List<DepartmentBean> departmentList = departmentDAO.listEntityBeans();

        List<LocationBean> locationList = locationDAO.listEntityBeans();

        Map<String, List> selectMap = new HashMap<String, List>();

        selectMap.put("postList", postList);
        selectMap.put("departmentList", departmentList);
        selectMap.put("locationList", locationList);

        List<MapBean> el = PageSelectOption.optionMap.get("examType");

        selectMap.put("$examType", el);

        request.setAttribute("selectMap", selectMap);

        request.setAttribute("query", query);

        return mapping.findForward("commonQuery");
    }

    /**
     * addStaffer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addStaffer(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        StafferBean bean = new StafferBean();

        try
        {
            BeanUtil.getBean(bean, request);

            bean.setLogTime(TimeTools.now());

            User user = Helper.getUser(request);

            publicFacade.addStafferBean(user, bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryStaffer");
    }

    /**
     * updateStaffer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateStaffer(ActionMapping mapping, ActionForm form,
                                       HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        StafferBean bean = new StafferBean();

        try
        {
            BeanUtil.getBean(bean, request);

            bean.setLogTime(TimeTools.now());

            User user = Helper.getUser(request);

            publicFacade.updateStafferBean(user, bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功修改:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "修改失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryStaffer");
    }

    /**
     * delStaffer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward delStaffer(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        try
        {
            String id = request.getParameter("stafferId");

            User user = Helper.getUser(request);

            publicFacade.delStafferBean(user, id);

            request.setAttribute(KeyConstant.MESSAGE, "成功废弃");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "废弃失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return mapping.findForward("queryStaffer");
    }

    /**
     * updateStaffer
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findStaffer(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String id = request.getParameter("id");

        String update = request.getParameter("update");

        StafferVO bean = stafferDAO.findVO(id);

        request.setAttribute("bean", bean);

        if ( !StringTools.isNullOrNone(update))
        {
            List<LocationBean> locationList = locationDAO.listEntityBeans();
            request.setAttribute("locationList", locationList);

            List<PrincipalshipBean> priList = principalshipDAO.listEntityBeans();
            request.setAttribute("priList", priList);

            List<DepartmentBean> depList = departmentDAO.listEntityBeans();
            request.setAttribute("depList", depList);

            List<PostBean> postList = postDAO.listEntityBeans();
            request.setAttribute("postList", postList);

            return mapping.findForward("updateStaffer");
        }

        return mapping.findForward("detailStaffer");
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
     * @return the publicFacade
     */
    public PublicFacade getPublicFacade()
    {
        return publicFacade;
    }

    /**
     * @param publicFacade
     *            the publicFacade to set
     */
    public void setPublicFacade(PublicFacade publicFacade)
    {
        this.publicFacade = publicFacade;
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
}
