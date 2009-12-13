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

import com.china.center.annosql.constant.AnoConstant;
import com.china.center.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.oa.facade.PublicFacade;
import com.china.center.oa.helper.Helper;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.dao.OrgDAO;
import com.china.center.oa.publics.dao.PrincipalshipDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.manager.OrgManager;
import com.china.center.oa.publics.vo.OrgVO;
import com.china.center.oa.publics.vs.OrgBean;
import com.china.center.oa.publics.wrap.StafferOrgWrap;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.CommonTools;
import com.url.ajax.json.JSONArray;
import com.url.ajax.json.JSONObject;


public class OrgAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private OrgDAO orgDAO = null;

    private PrincipalshipDAO principalshipDAO = null;

    private PublicFacade publicFacade = null;

    private StafferDAO stafferDAO = null;

    private OrgManager orgManager = null;

    /**
     * default constructor
     */
    public OrgAction()
    {}

    /**
     * queryOrg
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryOrg(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        preForListAllOrgTree(request);

        return mapping.findForward("queryOrg");
    }

    /**
     * 查询人员树
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward queryStafferOrg(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        try
        {
            preForListAllStafferOrgTree(request, "0");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "查询失败:" + e.getMessage());

            return mapping.findForward("error");
        }

        return mapping.findForward("queryStafferOrg");
    }

    /**
     * 弹出org
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward popOrg(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        preForListAllOrgTree(request);

        return mapping.findForward("popOrg");
    }

    /**
     * 为准备显示整个组织树准备
     * 
     * @param request
     */
    private void preForListAllOrgTree(HttpServletRequest request)
    {
        List<PrincipalshipBean> plist = principalshipDAO.listEntityBeans("order by level");

        JSONArray jarr = new JSONArray(plist, true);

        request.setAttribute("shipList", jarr.toString());

        Map<String, List<OrgVO>> map = new HashMap<String, List<OrgVO>>();

        for (PrincipalshipBean principalshipBean : plist)
        {
            List<OrgVO> vos = orgDAO.queryEntityVOsByFK(principalshipBean.getId());

            map.put(principalshipBean.getId(), vos);
        }

        JSONObject object = new JSONObject();

        object.createMapList(map, true);

        request.setAttribute("mapJSON", object.toString());
    }

    /**
     * 准备人员树
     * 
     * @param request
     * @throws MYException
     */
    private void preForListAllStafferOrgTree(HttpServletRequest request, String prinRootId)
        throws MYException
    {
        List<PrincipalshipBean> plist = orgManager.querySubPrincipalship(prinRootId);

        Map<String, List<StafferOrgWrap>> map = new HashMap<String, List<StafferOrgWrap>>();

        // 循环组织
        for (PrincipalshipBean principalshipBean : plist)
        {
            // 查询下一级岗位
            List<PrincipalshipBean> vos = principalshipDAO.querySubPrincipalship(principalshipBean.getId());

            List<StafferOrgWrap> wraps = new ArrayList<StafferOrgWrap>();

            for (PrincipalshipBean orgBean : vos)
            {
                // 子职务下的人员
                List<StafferBean> pplist = stafferDAO.queryStafferByPrincipalshipId(orgBean.getId());

                if (pplist.isEmpty())
                {
                    StafferOrgWrap wrap = new StafferOrgWrap();

                    wrap.setStafferId(orgBean.getId());

                    wrap.setStafferName(orgBean.getName());

                    wrap.setPrincipalshipId(orgBean.getId());

                    wrap.setPrincipalshipName(orgBean.getName());

                    wrap.setPersonal(1);

                    wraps.add(wrap);
                }
                else
                {
                    for (StafferBean stafferBean2 : pplist)
                    {
                        StafferOrgWrap wrap = new StafferOrgWrap();

                        wrap.setStafferId(stafferBean2.getId());

                        wrap.setStafferName(stafferBean2.getName());

                        wrap.setPrincipalshipId(orgBean.getId());

                        wrap.setPrincipalshipName(orgBean.getName());

                        wraps.add(wrap);
                    }
                }
            }

            map.put(principalshipBean.getId(), wraps);
        }

        JSONObject object = new JSONObject();

        object.createMapList(map, true);

        request.setAttribute("mapJSON", object.toString());

        PrincipalshipBean root = principalshipDAO.find(prinRootId);

        JSONObject rootObj = new JSONObject(root);

        request.setAttribute("root", rootObj.toString());
    }

    /**
     * preForAddRole
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward preForAddOrg(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        preForListAllOrgTree(request);

        return mapping.findForward("addOrg");
    }

    /**
     * addOrg
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward addOrg(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        PrincipalshipBean bean = new PrincipalshipBean();

        try
        {
            BeanUtil.getBean(bean, request);

            createOrgList(request, bean);

            User user = Helper.getUser(request);

            publicFacade.addOrgBean(user.getId(), bean);

            request.setAttribute(KeyConstant.MESSAGE, "成功增加职务:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加职务失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return queryOrg(mapping, form, request, response);
    }

    /**
     * createOrgList
     * 
     * @param request
     * @param bean
     */
    private void createOrgList(HttpServletRequest request, PrincipalshipBean bean)
    {
        String[] auths = request.getParameterValues("tree_checkbox");

        List<OrgBean> orgList = new ArrayList<OrgBean>();

        if (auths != null && auths.length > 0)
        {
            for (String item : auths)
            {
                OrgBean rab = new OrgBean();

                rab.setParentId(item);

                rab.setSubId(bean.getId());

                orgList.add(rab);
            }
        }

        bean.setParentOrgList(orgList);
    }

    /**
     * updateOrg
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward updateOrg(ActionMapping mapping, ActionForm form,
                                   HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        PrincipalshipBean bean = new PrincipalshipBean();

        String modifyParent = request.getParameter("modifyParent");
        try
        {
            BeanUtil.getBean(bean, request);

            createOrgList(request, bean);

            User user = Helper.getUser(request);

            publicFacade.updateOrgBean(user.getId(), bean, "1".equals(modifyParent));

            request.setAttribute(KeyConstant.MESSAGE, "成功修改职务:" + bean.getName());
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "修改职务失败:" + e.getMessage());
        }

        CommonTools.removeParamers(request);

        return queryOrg(mapping, form, request, response);
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
    public ActionForward delOrg(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        try
        {
            String id = request.getParameter("id");

            User user = Helper.getUser(request);

            publicFacade.delOrgBean(user.getId(), id);

            request.setAttribute(KeyConstant.MESSAGE, "成功删除职务");
        }
        catch (MYException e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "删除职务失败:" + e.getMessage());
        }

        return queryOrg(mapping, form, request, response);
    }

    /**
     * findOrg
     * 
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return
     * @throws ServletException
     */
    public ActionForward findOrg(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse response)
        throws ServletException
    {
        String update = request.getParameter("update");

        try
        {
            String id = request.getParameter("id");

            PrincipalshipBean bean = principalshipDAO.find(id);

            if (bean == null)
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "职务不存在");

                return queryOrg(mapping, form, request, response);
            }

            request.setAttribute("bean", bean);

            List<OrgVO> vos = orgDAO.queryEntityVOsByFK(id, AnoConstant.FK_FIRST);

            String parentName = "";

            for (OrgVO orgVO : vos)
            {
                parentName += orgVO.getParentName() + " ";
            }

            request.setAttribute("parentName", parentName);

        }
        catch (Exception e)
        {
            _logger.warn(e, e);

            request.setAttribute(KeyConstant.ERROR_MESSAGE, "删除职务失败:" + e.getMessage());
        }

        if ("1".equals(update))
        {
            preForListAllOrgTree(request);

            return mapping.findForward("updateOrg");
        }
        else
        {
            return mapping.findForward("findOrg");
        }
    }

    /**
     * @return the orgDAO
     */
    public OrgDAO getOrgDAO()
    {
        return orgDAO;
    }

    /**
     * @param orgDAO
     *            the orgDAO to set
     */
    public void setOrgDAO(OrgDAO orgDAO)
    {
        this.orgDAO = orgDAO;
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
}
