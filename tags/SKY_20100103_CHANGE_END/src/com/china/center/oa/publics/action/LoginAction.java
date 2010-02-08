package com.china.center.oa.publics.action;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import com.china.center.common.KeyConstant;
import com.china.center.common.MYException;
import com.china.center.oa.constant.AuthConstant;
import com.china.center.oa.constant.OAConstant;
import com.china.center.oa.constant.PublicConstant;
import com.china.center.oa.constant.StafferConstant;
import com.china.center.oa.constant.SysConfigConstant;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.CityBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.MenuItemBean;
import com.china.center.oa.publics.bean.ProvinceBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.dao.CityDAO;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.MenuItemDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.dao.ProvinceDAO;
import com.china.center.oa.publics.dao.RoleAuthDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.helper.LoginHelper;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.oa.publics.vo.UserVO;
import com.china.center.oa.publics.vs.RoleAuthBean;
import com.china.center.oa.util.MySessionListener;
import com.china.center.tools.ListTools;
import com.china.center.tools.Security;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.url.ajax.json.JSONArray;
import com.url.ajax.json.JSONObject;


/**
 * LoginAction
 * 
 * @author ZHUZHU
 * @version 2009-7-14
 * @see LoginAction
 * @since 1.0
 */
public class LoginAction extends DispatchAction
{
    private final Log _accessLog = LogFactory.getLog("access");

    private UserDAO userDAO = null;

    private UserManager userManager = null;

    private LocationDAO locationDAO = null;

    private MenuItemDAO menuItemDAO = null;

    private RoleAuthDAO roleAuthDAO = null;

    private ParameterDAO parameterDAO = null;

    private StafferDAO stafferDAO = null;

    private CommonDAO commonDAO = null;

    private ProvinceDAO provinceDAO = null;

    private CityDAO cityDAO = null;

    /**
     * login
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward login(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                               HttpServletResponse reponse)
        throws ServletException
    {
        String longinName = request.getParameter("userName");

        String password = request.getParameter("password");

        String rand = request.getParameter("rand");

        String key = request.getParameter("key");

        String randKey = request.getParameter("jiamiRand");

        String spassword = request.getParameter("spassword");

        // �Ƿ������֤
        String anhao = parameterDAO.getString(SysConfigConstant.SIGN_YY_CENTER);

        // �Ƿ����ü�����
        boolean hasEncLock = parameterDAO.getBoolean(SysConfigConstant.NEED_SUPER_ENC_LOCK);

        boolean real = false;

        ActionForward checkCommonResult = checkCommon(mapping, request, rand, real);

        if (checkCommonResult != null)
        {
            return checkCommonResult;
        }

        String randVal = rand.toUpperCase();

        UserVO user = userDAO.findUserByName(longinName);

        if (user == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "�û������������");
            return mapping.findForward("error");
        }

        if (real && anhao != null && !anhao.equals(spassword))
        {
            _accessLog.info(logLogin(request, user, false) + ',' + spassword);

            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "�����������");

            return mapping.findForward("error");
        }

        // ��������
        if (user.getStatus() == PublicConstant.LOGIN_STATUS_LOCK)
        {
            _accessLog.info(logLogin(request, user, false) + ',' + spassword);

            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "�û�������,����ϵ����Ա����!");

            return mapping.findForward("error");
        }

        StafferBean stafferBean = stafferDAO.find(user.getStafferId());

        if (stafferBean == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "�û������������!");

            return mapping.findForward("error");
        }

        if (stafferBean.getStatus() == StafferConstant.STATUS_DROP)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "�û������������!");

            return mapping.findForward("error");
        }

        // ��֤����
        boolean enc = handleEncLock(key, randKey, randVal, hasEncLock, stafferBean);

        if ( !real || (user.getPassword().equals(Security.getSecurity(password)) && enc))
        {
            try
            {
                checkDataValidity(mapping, request, user);
            }
            catch (MYException e)
            {
                return mapping.findForward(e.getErrorContent());
            }

            handleLoginSucess(request, spassword, user);
        }
        else
        {
            handleFail(user);

            _accessLog.info(logLogin(request, user, false) + ',' + spassword);

            if ( !enc)
            {
                request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "��������ƥ��");
            }
            else
            {
                request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "�û������������");
            }

            return mapping.findForward("error");
        }

        // processCity(user, request);

        JSONArray auths = new JSONArray(user.getAuth(), true);

        request.getSession().setAttribute("authJSON", auths.toString());

        request.getSession().setAttribute("gkey", key);

        request.getSession().setAttribute("hasEncLock", hasEncLock);

        request.getSession().setAttribute("g_stafferBean", stafferBean);

        ActionForward forward = mapping.findForward("success");

        String path = forward.getPath();

        MySessionListener.sessionSet.add(request.getSession().getId());

        return new ActionForward(path, true);
    }

    /**
     * handleEncLock
     * 
     * @param key
     * @param randKey
     * @param randVal
     * @param hasEncLock
     * @param stafferBean
     * @return
     */
    private boolean handleEncLock(String key, String randKey, String randVal, boolean hasEncLock,
                                  StafferBean stafferBean)
    {
        return !hasEncLock
               || LoginHelper.encRadomStr(stafferBean.getPwkey(), key, randVal).equals(randKey);
    }

    /**
     * handleLoginSucess
     * 
     * @param request
     * @param spassword
     * @param user
     */
    private void handleLoginSucess(HttpServletRequest request, String spassword, UserVO user)
    {
        user.setPassword("");

        // ��¼������־
        _accessLog.info(logLogin(request, user, true) + ',' + spassword);

        userManager.updateFail(user.getId(), 0);

        userManager.updateLogTime(user.getId(), TimeTools.now());

        request.getSession().setAttribute(OAConstant.CURRENTLOCATIONID, user.getLocationId());

        request.getSession().setAttribute("user", user);

        request.getSession().setAttribute("GTime", TimeTools.now("yyyy-MM-dd"));

        Map<String, List<MenuItemBean>> menuItemMap = new HashMap<String, List<MenuItemBean>>();

        // get auth by role
        List<RoleAuthBean> auth = roleAuthDAO.queryEntityBeansByFK(user.getRoleId());

        RoleAuthBean publicAuth = new RoleAuthBean();

        publicAuth.setAuthId(AuthConstant.PUNLIC_AUTH);

        auth.add(publicAuth);

        user.setAuth(auth);

        List<MenuItemBean> result = filterMenuItem(user, menuItemMap);

        request.getSession().setAttribute("menuRootList", result);

        request.getSession().setAttribute("menuItemMap", menuItemMap);
    }

    /**
     * handle login fail
     * 
     * @param user
     */
    private void handleFail(User user)
    {
        if ( (user.getFail() + 1) >= PublicConstant.LOGIN_FAIL_MAX)
        {
            userManager.updateStatus(user.getId(), PublicConstant.LOGIN_STATUS_LOCK);

            userManager.updateFail(user.getId(), 0);
        }
        else
        {
            userManager.updateFail(user.getId(), user.getFail() + 1);
        }
    }

    /**
     * @param mapping
     * @param request
     * @param user
     * @return
     */
    private void checkDataValidity(ActionMapping mapping, HttpServletRequest request, User user)
        throws MYException
    {
        String locationId = user.getLocationId();

        if ( !PublicConstant.VIRTUAL_LOCATION.equals(locationId))
        {
            LocationBean location = locationDAO.find(locationId);

            if (location == null)
            {
                request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "��¼�����򲻴���");

                throw new MYException("error");
            }

            request.getSession().setAttribute("GLocationName", location.getName());
        }
        else
        {
            request.getSession().setAttribute("GLocationName", "ϵͳ");
        }

        StafferBean sb = stafferDAO.find(user.getStafferId());

        if (sb == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "��¼��ְԱ������");

            throw new MYException("error");
        }

        request.getSession().setAttribute("g_staffer", sb);
    }

    /**
     * checkCommon
     * 
     * @param mapping
     * @param request
     * @param rand
     * @param real
     */
    private ActionForward checkCommon(ActionMapping mapping, HttpServletRequest request,
                                      String rand, boolean real)
    {
        if (real && rand == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "��֤�����");
            return mapping.findForward("error");
        }

        Object oo = request.getSession().getAttribute("rand");

        if (real && oo == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "��֤�����");
            return mapping.findForward("error");
        }

        if (real && !rand.equalsIgnoreCase(oo.toString()))
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "��֤�����");
            return mapping.findForward("error");
        }

        return null;
    }

    /**
     * @param request
     */
    public void processCity(User user, HttpServletRequest request)
    {
        List<ProvinceBean> plist = provinceDAO.listEntityBeans();

        Map<String, List<CityBean>> map = new HashMap<String, List<CityBean>>();

        for (ProvinceBean provinceBean : plist)
        {
            List<CityBean> clist = cityDAO.queryEntityBeansByFK(provinceBean.getId());

            map.put(provinceBean.getId(), clist);
        }

        JSONObject object = new JSONObject();

        JSONArray jarr = new JSONArray(plist, true);

        object.createMapList(map, false);

        request.getSession().setAttribute("jsStrJSON", object.toString());

        request.getSession().setAttribute("pStrJSON", jarr.toString());
    }

    private String logLogin(HttpServletRequest request, User user, boolean success)
    {
        return request.getRemoteAddr() + ',' + user.getName() + ',' + user.getStafferId() + ','
               + success;
    }

    /**
     * ���˲˵�
     * 
     * @param user
     * @return ���ڵ�Ĳ˵�
     */
    private List<MenuItemBean> filterMenuItem(User user,
                                              Map<String, List<MenuItemBean>> menuItemMap)
    {
        List<MenuItemBean> list = menuItemDAO.listEntityBeans();

        List<MenuItemBean> result = ListTools.newList(MenuItemBean.class);

        List<MenuItemBean> rootMenus = new ArrayList<MenuItemBean>();

        Map<String, MenuItemBean> menuRootMap = new HashMap<String, MenuItemBean>();

        for (int i = 0; i < list.size(); i++ )
        {
            MenuItemBean item = list.get(i);

            if (item.getBottomFlag() != PublicConstant.BOTTOMFLAG_YES)
            {
                menuRootMap.put(item.getId(), item);

                continue;
            }

            String auth = item.getAuth();

            if (StringTools.isNullOrNone(auth))
            {
                continue;
            }

            if (containAuth(user, auth))
            {
                result.add(item);

                continue;
            }
        }

        for (int i = 0; i < result.size(); i++ )
        {
            MenuItemBean menuItemBean = result.get(i);

            if ( !menuItemMap.containsKey(menuItemBean.getParentId()))
            {
                rootMenus.add(menuRootMap.get(menuItemBean.getParentId()));

                menuItemMap.put(menuItemBean.getParentId(), new ArrayList<MenuItemBean>());
            }

            menuItemMap.get(menuItemBean.getParentId()).add(menuItemBean);

            Collections.sort(menuItemMap.get(menuItemBean.getParentId()),
                new Comparator<MenuItemBean>()
                {
                    public int compare(MenuItemBean o1, MenuItemBean o2)
                    {
                        return o1.getIndexPos() - o2.getIndexPos();
                    }
                });
        }

        Collections.sort(rootMenus, new Comparator<MenuItemBean>()
        {
            public int compare(MenuItemBean o1, MenuItemBean o2)
            {
                return o1.getIndexPos() - o2.getIndexPos();
            }

        });

        return rootMenus;
    }

    private boolean containAuth(User user, String auth)
    {
        List<RoleAuthBean> auths = user.getAuth();

        for (RoleAuthBean roleAuthBean : auths)
        {
            if (roleAuthBean.getAuthId().equals(auth))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * modifyPassword
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward modifyPassword(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String oldPassword = request.getParameter("oldPassword");

        String newPassword = request.getParameter("newPassword");

        UserVO user = (UserVO)request.getSession().getAttribute("user");

        User user1 = userDAO.findUserByName(user.getName());

        if (user1 == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "�û�������");
            return mapping.findForward("password");
        }

        if (user1.getPassword().equals(Security.getSecurity(oldPassword)))
        {
            if (userManager.updatePassword(user.getId(), Security.getSecurity(newPassword)))
            {
                user.setPassword(Security.getSecurity(newPassword));
                request.setAttribute(KeyConstant.MESSAGE, "�����޸ĳɹ�");
            }
            else
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "�����޸�ʧ��");
            }
        }
        else
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "ԭ�������");
        }

        return mapping.findForward("password");
    }

    /**
     * @return the userDAO
     */
    public UserDAO getUserDAO()
    {
        return userDAO;
    }

    /**
     * @param userDAO
     *            the userDAO to set
     */
    public void setUserDAO(UserDAO userDAO)
    {
        this.userDAO = userDAO;
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
     * @return the menuItemDAO
     */
    public MenuItemDAO getMenuItemDAO()
    {
        return menuItemDAO;
    }

    /**
     * @param menuItemDAO
     *            the menuItemDAO to set
     */
    public void setMenuItemDAO(MenuItemDAO menuItemDAO)
    {
        this.menuItemDAO = menuItemDAO;
    }

    /**
     * @return the roleAuthDAO
     */
    public RoleAuthDAO getRoleAuthDAO()
    {
        return roleAuthDAO;
    }

    /**
     * @param roleAuthDAO
     *            the roleAuthDAO to set
     */
    public void setRoleAuthDAO(RoleAuthDAO roleAuthDAO)
    {
        this.roleAuthDAO = roleAuthDAO;
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
     * @return the cityDAO
     */
    public CityDAO getCityDAO()
    {
        return cityDAO;
    }

    /**
     * @param cityDAO
     *            the cityDAO to set
     */
    public void setCityDAO(CityDAO cityDAO)
    {
        this.cityDAO = cityDAO;
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
     * @return the parameterDAO
     */
    public ParameterDAO getParameterDAO()
    {
        return parameterDAO;
    }

    /**
     * @param parameterDAO
     *            the parameterDAO to set
     */
    public void setParameterDAO(ParameterDAO parameterDAO)
    {
        this.parameterDAO = parameterDAO;
    }
}