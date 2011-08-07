/**
 * File Name: LoginAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-27<br>
 * Grant: open source to everybody
 */
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

import net.buffalo.request.RequestContext;

import org.apache.catalina.session.StandardSessionFacade;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.center.china.osgi.config.ConfigLoader;
import com.center.china.osgi.publics.User;
import com.china.center.actionhelper.common.KeyConstant;
import com.china.center.actionhelper.jsonimpl.JSONArray;
import com.china.center.actionhelper.jsonimpl.JSONObject;
import com.china.center.common.MYException;
import com.china.center.oa.publics.Helper;
import com.china.center.oa.publics.bean.CityBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.MenuItemBean;
import com.china.center.oa.publics.bean.ProvinceBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.constant.StafferConstant;
import com.china.center.oa.publics.constant.SysConfigConstant;
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
import com.china.center.oa.publics.manager.MenuManager;
import com.china.center.oa.publics.manager.UserManager;
import com.china.center.oa.publics.vo.UserVO;
import com.china.center.oa.publics.vs.RoleAuthBean;
import com.china.center.tools.ListTools;
import com.china.center.tools.Security;
import com.china.center.tools.SessionTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.center.webplugin.inter.WebPathListener;
import com.china.center.webportal.listener.MySessionListener;


/**
 * LoginAction
 * 
 * @author ZHUZHU
 * @version 2010-6-27
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

    private MenuManager menuManager = null;

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

        // 是否二次认证
        String anhao = parameterDAO.getString(SysConfigConstant.SIGN_YY_CENTER);

        boolean login = parameterDAO.getBoolean("REAL_LOGIN");

        // 是否启用加密锁
        boolean hasEncLock = parameterDAO.getBoolean(SysConfigConstant.NEED_SUPER_ENC_LOCK);

        boolean real = login;

        ActionForward checkCommonResult = checkCommon(mapping, request, rand, real);

        if (checkCommonResult != null)
        {
            return checkCommonResult;
        }

        String randVal = rand.toUpperCase();

        UserVO user = userDAO.findUserByName(longinName);

        if (user == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户名或密码错误");
            return mapping.findForward("error");
        }

        if (real && anhao != null && !anhao.equals(spassword))
        {
            _accessLog.info(logLogin(request, user, false) + ',' + spassword);

            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "二次密码错误");

            return mapping.findForward("error");
        }

        // 锁定处理
        if (user.getStatus() == PublicConstant.LOGIN_STATUS_LOCK)
        {
            _accessLog.info(logLogin(request, user, false) + ',' + spassword);

            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户被锁定,请联系管理员解锁!");

            return mapping.findForward("error");
        }

        StafferBean stafferBean = stafferDAO.findVO(user.getStafferId());

        if (stafferBean == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户名或密码错误!");

            return mapping.findForward("error");
        }

        if (stafferBean.getStatus() == StafferConstant.STATUS_DROP)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户名或密码错误!");

            return mapping.findForward("error");
        }

        // 验证密码
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
                request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "密码锁不匹配");
            }
            else
            {
                request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户名或密码错误");
            }

            return mapping.findForward("error");
        }

        // processCity(user, request);

        JSONArray auths = new JSONArray(user.getAuth(), true);

        request.getSession().setAttribute("authJSON", auths.toString());

        request.getSession().setAttribute("gkey", key);

        // 默认是20
        request.getSession().setAttribute("g_page",
            parameterDAO.getInt(SysConfigConstant.GLOBAL_PAGE_SIZE));

        request.getSession().setAttribute("hasEncLock", hasEncLock);

        request.getSession().setAttribute("g_stafferBean", stafferBean);

        request.getSession().setAttribute("g_logout", "../admin/logout.do");

        request.getSession().setAttribute("g_modifyPassword", "../admin/modifyPassword.jsp");

        // OA系统/SKY软件【V2.14.20100509】
        request.getSession().setAttribute("SN",
            "OA系统/SKY软件【" + ConfigLoader.getProperty("version") + "】");

        ActionForward forward = mapping.findForward("success");

        String path = forward.getPath();

        MySessionListener.getSessionSet().add(request.getSession().getId());

        WebPathListener.getGMap().put("SA", MySessionListener.getSessionSet().size());

        return new ActionForward(path, true);
    }

    /**
     * 解锁
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward unlock(ActionMapping mapping, ActionForm form, HttpServletRequest request,
                                HttpServletResponse reponse)
        throws ServletException
    {
        String password = request.getParameter("password");

        boolean real = parameterDAO.getBoolean("REAL_LOGIN");

        User suser = Helper.getUser(request);

        if (suser == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "请重新登录");

            return mapping.findForward("error");
        }

        UserVO user = userDAO.findUserByName(Helper.getUser(request).getName());

        if (user == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "密码错误");

            return mapping.findForward("lock");
        }

        if ( !real || (user.getPassword().equals(Security.getSecurity(password))))
        {
            _accessLog.info(logLogin(request, user, false) + ',' + "LOCK");
        }
        else
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户名或密码错误");

            return mapping.findForward("lock");
        }

        // 去除session里面的锁标识
        request.getSession().removeAttribute("SLOCK");

        ActionForward forward = mapping.findForward("success");

        String path = forward.getPath();

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

        // 记录访问日志
        _accessLog.info(logLogin(request, user, true) + ',' + spassword);

        userManager.updateFail(user.getId(), 0);

        userManager.updateLogTime(user.getId(), TimeTools.now());

        request.getSession().setAttribute(PublicConstant.CURRENTLOCATIONID, user.getLocationId());

        request.getSession().setAttribute("user", user);

        request.getSession().setAttribute("GTime", TimeTools.now("yyyy-MM-dd"));

        Map<String, List<MenuItemBean>> menuItemMap = new HashMap<String, List<MenuItemBean>>();

        // get auth by role
        List<RoleAuthBean> authList = roleAuthDAO.queryEntityBeansByFK(user.getRoleId());

        RoleAuthBean publicAuth = new RoleAuthBean();

        publicAuth.setAuthId(AuthConstant.PUNLIC_AUTH);

        authList.add(publicAuth);

        user.setAuth(authList);

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
                request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "登录的区域不存在");

                throw new MYException("error");
            }

            request.getSession().setAttribute("GLocationName", location.getName());
        }
        else
        {
            request.getSession().setAttribute("GLocationName", "系统");
        }

        StafferBean sb = stafferDAO.find(user.getStafferId());

        if (sb == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "登录的职员不存在");

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
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "验证码错误");
            return mapping.findForward("error");
        }

        Object oo = request.getSession().getAttribute("rand");

        if (real && oo == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "验证码错误");
            return mapping.findForward("error");
        }

        if (real && !rand.equalsIgnoreCase(oo.toString()))
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "验证码错误");
            return mapping.findForward("error");
        }

        return null;
    }

    /**
     * 检查session多长时间没操作
     * 
     * @return
     * @throws ServletException
     */
    public boolean checkSession()
        throws ServletException
    {
        RequestContext context = net.buffalo.request.RequestContext.getContext();

        StandardSessionFacade session = (StandardSessionFacade)context
            .getHttpRequest()
            .getSession();

        // 获取上次访问的时间
        long lastAccessedTime = session.getLastAccessedTime();

        long current = System.currentTimeMillis();

        int lockTime = parameterDAO.getInt(SysConfigConstant.MAX_LOCK_TIME);

        if (current - lastAccessedTime > (1000 * 60 * lockTime))
        {
            session.setAttribute("SLOCK", true);

            return true;
        }

        SessionTools.keepSessionLastAccessTime(session);

        return false;
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
     * 过滤菜单
     * 
     * @param user
     * @return 跟节点的菜单
     */
    private List<MenuItemBean> filterMenuItem(User user, Map<String, List<MenuItemBean>> menuItemMap)
    {
        List<MenuItemBean> list = menuItemDAO.listEntityBeans();

        List<MenuItemBean> result = ListTools.newList(MenuItemBean.class);

        List<MenuItemBean> rootMenus = new ArrayList<MenuItemBean>();

        Map<String, MenuItemBean> menuRootMap = new HashMap<String, MenuItemBean>();

        for (int i = 0; i < list.size(); i++ )
        {
            MenuItemBean item = list.get(i);

            // 根节点
            if (item.getBottomFlag() == PublicConstant.BOTTOMFLAG_NO)
            {
                menuRootMap.put(item.getId(), item);

                continue;
            }

            // 这里支持1101,1102
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

            // 把二级子菜单放入list
            menuItemMap.get(menuItemBean.getParentId()).add(menuItemBean);
        }

        // 二级子菜单排序
        for (Map.Entry<String, List<MenuItemBean>> each : menuItemMap.entrySet())
        {
            // 放入扩展菜单
            List<MenuItemBean> expandMenuList = menuManager.onFindExpandMenu(user, each.getKey());

            if ( !ListTools.isEmptyOrNull(expandMenuList))
            {
                each.getValue().addAll(expandMenuList);
            }

            Collections.sort(each.getValue(), new Comparator<MenuItemBean>()
            {
                public int compare(MenuItemBean o1, MenuItemBean o2)
                {
                    return o1.getIndexPos() - o2.getIndexPos();
                }
            });

            // 统一加menu
            List<MenuItemBean> twoList = each.getValue();

            for (MenuItemBean menuItemBean : twoList)
            {
                if (StringTools.isNullOrNone(menuItemBean.getUrl()))
                {
                    continue;
                }

                if (menuItemBean.getUrl().indexOf("menu=1") != -1)
                {
                    continue;
                }

                if (menuItemBean.getUrl().indexOf("?") != -1)
                {
                    menuItemBean.setUrl(menuItemBean.getUrl() + "&menu=1");
                }
                else
                {
                    menuItemBean.setUrl(menuItemBean.getUrl() + "?menu=1");
                }
            }
        }

        // 一级菜单排序
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
        String[] authArr = auth.split(",");

        List<RoleAuthBean> auths = user.getAuth();

        for (RoleAuthBean roleAuthBean : auths)
        {
            for (String eachAuth : authArr)
            {
                if (StringTools.isNullOrNone(eachAuth))
                {
                    continue;
                }

                if (roleAuthBean.getAuthId().equals(eachAuth.trim()))
                {
                    return true;
                }
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
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "用户不存在");
            return mapping.findForward("password");
        }

        if (user1.getPassword().equals(Security.getSecurity(oldPassword)))
        {
            if (userManager.updatePassword(user.getId(), Security.getSecurity(newPassword)))
            {
                user.setPassword(Security.getSecurity(newPassword));
                request.setAttribute(KeyConstant.MESSAGE, "密码修改成功");
            }
            else
            {
                request.setAttribute(KeyConstant.ERROR_MESSAGE, "密码修改失败");
            }
        }
        else
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "原密码错误");
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
     * @param userDAO
     *            the userDAO to set
     */
    public void setUserDAO2(UserDAO service, Map properties)
    {
        this.userDAO = service;
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

    /**
     * @return the menuManager
     */
    public MenuManager getMenuManager()
    {
        return menuManager;
    }

    /**
     * @param menuManager
     *            the menuManager to set
     */
    public void setMenuManager(MenuManager menuManager)
    {
        this.menuManager = menuManager;
    }
}
