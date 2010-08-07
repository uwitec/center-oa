/**
 * File Name: PopQueryAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.action;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.actionhelper.common.PageSeparateTools;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.constant.StafferConstant;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.LogDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.vo.LogVO;
import com.china.center.oa.publics.vo.StafferVO;
import com.china.center.tools.CommonTools;
import com.china.center.tools.StringTools;


/**
 * PopQueryAction
 * 
 * @author ZHUZHU
 * @version 2010-6-27
 * @see PopQueryAction
 * @since 1.0
 */
public class PopQueryAction extends DispatchAction
{
    private StafferDAO stafferDAO = null;

    private LocationDAO locationDAO = null;

    private LogDAO logDAO = null;

    private static String RPTQUERYSTAFFER = "rptQueryStaffer";

    /**
     * @param request
     * @param condtion
     */
    private void setStafferInnerCondition(HttpServletRequest request, ConditionParse condtion)
    {
        String name = request.getParameter("name");

        String code = request.getParameter("code");

        String locationId = request.getParameter("locationId");

        if ( !StringTools.isNullOrNone(name))
        {
            condtion.addCondition("StafferBean.name", "like", name);
        }

        if ( !StringTools.isNullOrNone(code))
        {
            condtion.addCondition("StafferBean.code", "like", code);
        }

        if ( !StringTools.isNullOrNone(locationId))
        {
            condtion.addCondition("StafferBean.locationId", "=", locationId);
        }
    }

    /**
     * 职员的查询
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryStaffer(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                         HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        List<StafferVO> list = null;

        List<LocationBean> locationList = locationDAO.listEntityBeans();

        if (PageSeparateTools.isFirstLoad(request))
        {
            ConditionParse condtion = new ConditionParse();

            condtion.addWhereStr();

            // 过滤废弃的
            condtion.addIntCondition("StafferBean.status", "=", StafferConstant.STATUS_COMMON);

            setStafferInnerCondition(request, condtion);

            int total = stafferDAO.countByCondition(condtion.toString());

            PageSeparate page = new PageSeparate(total, PublicConstant.PAGE_COMMON_SIZE);

            PageSeparateTools.initPageSeparate(condtion, page, request, RPTQUERYSTAFFER);

            list = stafferDAO.queryEntityVOsByCondition(condtion, page);
        }
        else
        {
            PageSeparateTools.processSeparate(request, RPTQUERYSTAFFER);

            list = stafferDAO.queryEntityVOsByCondition(PageSeparateTools.getCondition(request, RPTQUERYSTAFFER),
                PageSeparateTools.getPageSeparate(request, RPTQUERYSTAFFER));
        }

        request.setAttribute("beanList", list);

        request.setAttribute("locationList", locationList);

        return mapping.findForward("rptQueryStaffer");
    }

    /**
     * query log
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward rptQueryLog(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                     HttpServletResponse reponse)
        throws ServletException
    {
        CommonTools.saveParamers(request);

        String fk = request.getParameter("fk");

        List<LogVO> list = logDAO.queryEntityVOsByFK(fk);

        Collections.sort(list, new Comparator<LogVO>()
        {

            public int compare(LogVO o1, LogVO o2)
            {
                return o1.getLogTime().compareTo(o2.getLogTime());
            }
        });

        request.setAttribute("beanList", list);

        return mapping.findForward("rptQueryLog");
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
     * @return the logDAO
     */
    public LogDAO getLogDAO()
    {
        return logDAO;
    }

    /**
     * @param logDAO
     *            the logDAO to set
     */
    public void setLogDAO(LogDAO logDAO)
    {
        this.logDAO = logDAO;
    }
}
