package com.china.centet.yongyin.action;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
import com.china.center.oa.customer.bean.ProviderUserBean;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.DecSecurity;
import com.china.center.tools.ListTools;
import com.china.center.tools.RandomTools;
import com.china.center.tools.Security;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.BaseBean;
import com.china.centet.yongyin.bean.ConsignBean;
import com.china.centet.yongyin.bean.LocationBean;
import com.china.centet.yongyin.bean.MenuItemBean;
import com.china.centet.yongyin.bean.OutBean;
import com.china.centet.yongyin.bean.ProviderBean;
import com.china.centet.yongyin.bean.Role;
import com.china.centet.yongyin.bean.RoleBean;
import com.china.centet.yongyin.bean.StafferBean;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.bean.helper.LoginHelper;
import com.china.centet.yongyin.bean.helper.OutBeanHelper;
import com.china.centet.yongyin.bean.helper.RoleHelper;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.constant.SysConfigConstant;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.ConsignDAO;
import com.china.centet.yongyin.dao.OutDAO;
import com.china.centet.yongyin.dao.ParameterDAO;
import com.china.centet.yongyin.dao.ProductTypeVSCustomerDAO;
import com.china.centet.yongyin.dao.ProviderDAO;
import com.china.centet.yongyin.dao.ProviderUserDAO;
import com.china.centet.yongyin.dao.StafferDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.manager.CommonMamager;
import com.china.centet.yongyin.manager.EhcacheManager;
import com.china.centet.yongyin.manager.LocationManager;
import com.china.centet.yongyin.manager.MenuItemManager;
import com.china.centet.yongyin.vo.UserVO;
import com.china.centet.yongyin.vs.ProductTypeVSCustomer;


public class LoginAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private final Log _accessLog = LogFactory.getLog("access");

    private UserDAO userDAO = null;

    private StafferDAO stafferDAO = null;

    private ParameterDAO parameterDAO = null;

    private ProviderDAO providerDAO = null;

    private CommonDAO commonDAO = null;

    private LocationManager locationManager = null;

    private MenuItemManager menuItemManager = null;

    private EhcacheManager ehcacheManager = null;

    private ProviderUserDAO providerUserDAO = null;

    private ProductTypeVSCustomerDAO productTypeVSCustomerDAO = null;

    private OutDAO outDAO = null;

    private ConsignDAO consignDAO = null;

    private CommonMamager commonMamager = null;

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

        if ( !commonMamager.isPass()
            && !StringTools.isNullOrNone(DecSecurity.decrypt(commonMamager.dy())))
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "服务没有授权绑定");
            return mapping.findForward("error");
        }

        boolean real = getContorl();

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

        String randVal = rand.toUpperCase();

        User user = userDAO.findUserByLoginName(longinName);

        if (user == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户名或密码错误");
            return mapping.findForward("error");
        }

        StafferBean stafferBean = stafferDAO.find(user.getStafferId());

        if (stafferBean == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户名或密码错误!");

            return mapping.findForward("error");
        }

        if (stafferBean.getStatus() == 99)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户名或密码错误!");

            return mapping.findForward("error");
        }

        // 是否二次认证
        String anhao = parameterDAO.getString(SysConfigConstant.SIGN_YY_CENTER);

        // 是否启用加密锁
        boolean hasEncLock = parameterDAO.getBoolean(SysConfigConstant.NEED_SUPER_ENC_LOCK);

        if (real && anhao != null && !anhao.equals(spassword))
        {
            _accessLog.info(logLogin(request, user, false) + ',' + spassword);

            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "二次密码错误");

            return mapping.findForward("error");
        }

        // 锁定处理
        if (user.getStatus() == Constant.LOGIN_STATUS_LOCK)
        {
            _accessLog.info(logLogin(request, user, false) + ',' + spassword);

            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户被锁定,请联系管理员解锁!");

            return mapping.findForward("error");
        }

        // 验证密码
        boolean enc = handleEncLock(key, randKey, randVal, hasEncLock, stafferBean.getPwkey());

        // 验证密码
        if ( !real || (user.getPassword().equals(Security.getSecurity(password)) && enc))
        {
            user.setPassword("");

            // 记录访问日志
            _accessLog.info(logLogin(request, user, true) + ',' + spassword);

            userDAO.modifyFail(user.getName(), 0);

            userDAO.modifyLogTime(user.getId(), TimeTools.now());

            String flag = user.getLocationID();

            if ( !"-1".equals(flag))
            {
                LocationBean location = locationManager.findLocationById(flag);

                if (location == null)
                {
                    request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "登录的区域不存在");
                    return mapping.findForward("error");
                }

                request.getSession().setAttribute("GLocationName", location.getLocationName());
            }
            else
            {
                request.getSession().setAttribute("GLocationName", "系统");
            }

            request.getSession().setAttribute(Constant.CURRENTLOCATIONID, flag);

            request.getSession().setAttribute("SN", "财务系统");

            request.getSession().setAttribute("flag", flag);

            request.getSession().setAttribute("user", user);

            request.getSession().setAttribute("GTime", TimeTools.now("yyyy-MM-dd"));

            // 得到银行
            List<String> list2 = commonDAO.listAll("t_center_bank");

            request.getSession().setAttribute("bankList", list2);

            Map<String, List<MenuItemBean>> menuItemMap = new HashMap<String, List<MenuItemBean>>();

            List<MenuItemBean> result = filterMenuItem(user, menuItemMap);

            request.getSession().setAttribute("menuRootList", result);

            request.getSession().setAttribute("menuItemMap", menuItemMap);

            setYearMonth(request);
        }
        else
        {
            if ( (user.getFail() + 1) >= Constant.LOGIN_FAIL_MAX)
            {
                userDAO.modifyStatus(user.getName(), Constant.LOGIN_STATUS_LOCK);

                userDAO.modifyFail(user.getName(), 0);
            }
            else
            {
                userDAO.modifyFail(user.getName(), user.getFail() + 1);
            }

            _accessLog.info(logLogin(request, user, false) + ',' + spassword);

            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户名或密码错误");
            return mapping.findForward("error");
        }

        processUser(request, user);

        request.getSession().setAttribute("gkey", key);

        request.getSession().setAttribute("hasEncLock", hasEncLock);

        ActionForward forward = mapping.findForward("success");

        String path = forward.getPath();

        return new ActionForward(path, true);
    }

    /**
     * getContorl
     * 
     * @return
     */
    private boolean getContorl()
    {
        return parameterDAO.getBoolean("REAL_LOGIN");
    }

    /**
     * loginAsk
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward loginAsk(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String longinName = request.getParameter("userName");

        String password = request.getParameter("password");

        String rand = request.getParameter("rand");

        String key = request.getParameter("key");

        String randKey = request.getParameter("jiamiRand");

        boolean real = getContorl();

        if (real && rand == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "验证码错误");
            return mapping.findForward("errorAsk");
        }

        Object oo = request.getSession().getAttribute("rand");

        if (real && oo == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "验证码错误");
            return mapping.findForward("errorAsk");
        }

        if (real && !rand.equalsIgnoreCase(oo.toString()))
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "验证码错误");
            return mapping.findForward("errorAsk");
        }

        String randVal = rand.toUpperCase();

        ProviderUserBean puser = providerUserDAO.findByUnique(longinName);

        if (puser == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户名或密码错误");
            return mapping.findForward("errorAsk");
        }

        User user = LoginHelper.getUser(puser);

        ProviderBean provider = providerDAO.find(puser.getProvideId());

        if (provider == null)
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "供应商不存在");

            return mapping.findForward("errorAsk");
        }

        // 是否启用加密锁
        boolean hasEncLock = (false && parameterDAO.getBoolean(SysConfigConstant.NEED_SUPER_ENC_LOCK));

        // 验证密码
        boolean enc = true || handleEncLock(key, randKey, randVal, hasEncLock, "");

        // 验证密码
        if ( !real || (puser.getPassword().equals(Security.getSecurity(password)) && enc))
        {

            request.getSession().setAttribute("user", user);

            request.getSession().setAttribute("GLocationName", "系统");

            request.getSession().setAttribute("GProvider", provider);

            request.getSession().setAttribute("SNFLAG", "ASK");

            request.getSession().setAttribute("SN", "询价系统");

            request.getSession().setAttribute("GTime", TimeTools.now("yyyy-MM-dd"));

            List<ProductTypeVSCustomer> list = productTypeVSCustomerDAO.queryEntityBeansByFK(
                user.getId(), 1);

            Map<String, List<MenuItemBean>> menuItemMap = new HashMap<String, List<MenuItemBean>>();

            List<MenuItemBean> result = filterMenuItem(user, menuItemMap);

            request.getSession().setAttribute("menuRootList", result);

            request.getSession().setAttribute("typeList", list);

            request.getSession().setAttribute("menuItemMap", menuItemMap);
        }
        else
        {
            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户名或密码错误");

            return mapping.findForward("errorAsk");
        }

        // processUser(request, user);

        request.getSession().setAttribute("gkey", key);

        request.getSession().setAttribute("hasEncLock", hasEncLock);

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
                                  String pwkey)
    {
        return !hasEncLock || LoginHelper.encRadomStr(pwkey, key, randVal).equals(randKey);
    }

    /**
     * @param request
     * @param user
     */
    private void processUser(HttpServletRequest request, User user)
    {
        if (user.getRole() == Role.COMMON)
        {
            processCommon(request, user);
        }

        if (user.getRole() == Role.FLOW)
        {
            processFlow(request, user);
        }
    }

    /**
     * @param request
     * @param user
     */
    private void processCommon(HttpServletRequest request, User user)
    {
        // 进入业务员告警页面
        ConditionParse condtion = new ConditionParse();

        // 获得条件
        getCondition(condtion, user);

        List<OutBean> list = outDAO.queryOutBeanByCondtion(condtion);

        long current = new Date().getTime();
        for (OutBean outBean : list)
        {
            Date temp = TimeTools.getDateByFormat(outBean.getRedate(), "yyyy-MM-dd");

            if (temp != null)
            {
                if (temp.getTime() < current)
                {
                    // 超期的
                    outBean.setPay(2);
                }
            }
        }

        if (list.size() != 0)
        {
            // 提示页面
            getDivs(request, list);

            request.getSession().setAttribute("flagOut", list);
            request.getSession().setAttribute("flagOutString", "0");
        }
    }

    /**
     * 取得今天需要发货的发货单
     * 
     * @param request
     * @param user
     */
    private void processFlow(HttpServletRequest request, User user)
    {
        // 进入业务员告警页面
        ConditionParse condtion = new ConditionParse();

        // 只查询出库单
        condtion.addIntCondition("t2.type", "=", Constant.OUT_TYPE_OUTBILL);

        condtion.addIntCondition("t2.status", ">=", Constant.STATUS_PASS);

        condtion.addIntCondition("t2.status", "<=", Constant.STATUS_SEC_PASS);

        // 未收货的
        condtion.addIntCondition("t1.reprotType", "=", Constant.CONSIGN_REPORT_INIT);

        condtion.addCondition("t2.location", "=", user.getLocationID());

        String now = TimeTools.now_short();

        condtion.addCondition("arriveDate", "=", now);

        List<ConsignBean> list = consignDAO.queryConsignByCondition(condtion);

        if (list.size() != 0)
        {
            // 提示页面
            getDivs(request, list);

            request.getSession().setAttribute("flagOut", list);
            request.getSession().setAttribute("flagOutString", "0");
        }
    }

    private String logLogin(HttpServletRequest request, User user, boolean success)
    {
        return request.getRemoteAddr() + ',' + user.getName() + ',' + user.getStafferName() + ','
               + success;
    }

    /**
     * 过滤菜单
     * 
     * @param user
     * @return 跟节点的菜单
     */
    private List<MenuItemBean> filterMenuItem(User user,
                                              Map<String, List<MenuItemBean>> menuItemMap)
    {
        List<MenuItemBean> list = menuItemManager.getMenuItemList();

        List<MenuItemBean> result = ListTools.newList(MenuItemBean.class);

        List<MenuItemBean> rootMenus = new ArrayList<MenuItemBean>();

        Map<String, MenuItemBean> menuRootMap = new HashMap<String, MenuItemBean>();

        int index = RoleHelper.getIndexFromRole(user);

        // Role role = user.getRole();

        for (int i = 0; i < list.size(); i++ )
        {
            MenuItemBean item = list.get(i);

            if (item.getBottomFlag() != Constant.BOTTOMFLAG_YES)
            {
                menuRootMap.put(item.getId(), item);

                continue;
            }

            String auth = item.getAuth();

            if (StringTools.isNullOrNone(auth))
            {
                continue;
            }

            if ( (auth.length() - 1) < index)
            {
                continue;
            }

            /*
             * if (role == Role.NETASK) { if ( !item.getParentId().equals("08")) { continue; } }
             */

            if (Constant.AUTH_PASS == auth.charAt(index))
            {
                result.add(item);
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

    private void getDivs(HttpServletRequest request, List<? extends OutBean> list)
    {
        Map divMap = new HashMap();

        if (list != null)
        {
            for (OutBean bean : list)
            {
                try
                {
                    List<BaseBean> baseList = outDAO.queryBaseByOutFullId(bean.getFullId());

                    divMap.put(bean.getFullId(), OutBeanHelper.createTable(baseList,
                        bean.getTotal()));
                }
                catch (Exception e)
                {
                    _logger.error("addOut", e);
                }
            }
        }

        request.getSession().setAttribute("divMap", divMap);
    }

    private void getCondition(ConditionParse condtion, User user)
    {
        // 只查询出库单
        condtion.addIntCondition("type", "=", Constant.OUT_TYPE_OUTBILL);

        condtion.addIntCondition("status", "=", Constant.STATUS_PASS);

        condtion.addCondition("STAFFERNAME", "=", user.getStafferName());

        condtion.addIntCondition("pay", "=", Constant.PAY_NOT);

        condtion.addCondition("reday", "<>", "0");
    }

    private void setYearMonth(HttpServletRequest request)
    {
        Calendar cal = Calendar.getInstance();

        List<String> months = new ArrayList();
        int year = cal.get(Calendar.YEAR);

        for (int i = 1; i < 13; i++ )
        {
            if (i < 10)
            {
                months.add(year + "0" + i);
            }
            else
            {
                months.add(year + "" + i);
            }
        }

        year-- ;
        for (int i = 1; i < 13; i++ )
        {
            if (i < 10)
            {
                months.add(year + "0" + i);
            }
            else
            {
                months.add(year + "" + i);
            }
        }

        year-- ;
        for (int i = 1; i < 13; i++ )
        {
            if (i < 10)
            {
                months.add(year + "0" + i);
            }
            else
            {
                months.add(year + "" + i);
            }
        }

        request.getSession().setAttribute("months", months);
    }

    public ActionForward addForAdmin(ActionMapping mapping, ActionForm form,
                                     HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        List<StafferBean> list = stafferDAO.listEntityBeans();

        List<RoleBean> roleList = commonDAO.listVisibleRole();

        request.getSession().setAttribute("staffers", list);

        request.getSession().setAttribute("roleList", roleList);

        return mapping.findForward("addAdmins");
    }

    /**
     * 增加区域
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward addFlag(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        // String flags = request.getParameter("flags");

        // commonDAO.addFlag(flags);

        return mapping.findForward("index");
    }

    public ActionForward modifyPassword(ActionMapping mapping, ActionForm form,
                                        HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String oldPassword = request.getParameter("oldPassword");

        String newPassword = request.getParameter("newPassword");

        User user = (User)request.getSession().getAttribute("user");

        String snFlag = (String)request.getSession().getAttribute("SNFLAG");

        if ( !"ASK".equals(snFlag))
        {
            User user1 = userDAO.findUserByLoginName(user.getName());

            if (user1 == null)
            {
                request.setAttribute(KeyConstant.MESSAGE, "用户不存在");
                return mapping.findForward("password");
            }

            if (user1.getPassword().equals(Security.getSecurity(oldPassword)))
            {
                if (userDAO.modifyPassword(user.getName(), Security.getSecurity(newPassword)))
                {
                    user.setPassword(Security.getSecurity(newPassword));
                    request.setAttribute(KeyConstant.MESSAGE, "密码修改成功");
                }
                else
                {
                    request.setAttribute(KeyConstant.MESSAGE, "密码修改失败");
                }
            }
            else
            {
                request.setAttribute(KeyConstant.MESSAGE, "原密码错误");
            }
        }
        else
        {
            ProviderUserBean puser = providerUserDAO.find(user.getStafferId());

            if (puser == null)
            {
                request.setAttribute(KeyConstant.MESSAGE, "用户不存在");

                return mapping.findForward("password");
            }

            if ( !puser.getPassword().equals(Security.getSecurity(oldPassword)))
            {
                request.setAttribute(KeyConstant.MESSAGE, "原密码错误");

                return mapping.findForward("password");
            }

            // 修改外网询价用户的密码
            String md5 = Security.getSecurity(newPassword);

            providerUserDAO.updatePassword(user.getStafferId(), md5);

            request.setAttribute(KeyConstant.MESSAGE, "密码修改成功");
        }

        return mapping.findForward("password");
    }

    public ActionForward unlock(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String userName = request.getParameter("userName");

        if (userDAO.modifyStatus(userName, Constant.LOGIN_STATUS_COMMON))
        {
            request.setAttribute(KeyConstant.MESSAGE, "强制解锁成功");
        }
        else
        {
            request.setAttribute(KeyConstant.MESSAGE, "强制解锁失败");
        }

        return listCommon(mapping, form, request, reponse);
    }

    /**
     * 初始化密码
     * 
     * @param mapping
     * @param form
     * @param request
     * @param reponse
     * @return
     * @throws ServletException
     */
    public ActionForward initPassword(ActionMapping mapping, ActionForm form,
                                      HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String userName = request.getParameter("userName");

        String password = RandomTools.getRandomString(Constant.PASSWORD_MIN_LENGTH);

        if (userDAO.modifyPassword(userName, Security.getSecurity(password)))
        {
            request.setAttribute(KeyConstant.MESSAGE, "初始化[" + userName
                                                      + "]密码成功,密码：<input type=text value='"
                                                      + password + "' >");
        }
        else
        {
            request.setAttribute(KeyConstant.MESSAGE, "初始化密码失败");
        }

        return listCommon(mapping, form, request, reponse);
    }

    public ActionForward addYWY(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String userName = request.getParameter("ywyName");

        String password = RandomTools.getRandomString(Constant.PASSWORD_MIN_LENGTH);

        String stafferId = request.getParameter("stafferId");

        StafferBean sban = stafferDAO.find(stafferId);

        if (sban == null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "职员不存在");

            return mapping.findForward("addAdmins");
        }

        String locationId = request.getSession().getAttribute(Constant.CURRENTLOCATIONID).toString();

        int type = Integer.parseInt(request.getParameter("role"));

        if (userDAO.findUserByLoginName(userName) != null)
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "人员" + userName + "已经存在");
            return mapping.findForward("addAdmins");
        }

        User user = new User();
        user.setId("0");
        user.setName(userName);
        user.setLocationID(locationId);
        user.setPassword(Security.getSecurity(password));
        user.setType(type);
        user.setStafferId(stafferId);
        user.setStafferName(sban.getName());
        user.setLoginTime(TimeTools.now());

        if (userDAO.addUser(user))
        {
            request.setAttribute(KeyConstant.MESSAGE, "增加人员[" + userName
                                                      + "]成功,密码：<input type=text value='"
                                                      + password + "' >" + ",请及时通知相应人员密码!");
        }
        else
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "增加人员失败");
        }

        return mapping.findForward("addAdmins");
    }

    public ActionForward delYWY(ActionMapping mapping, ActionForm form,
                                HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        String userName = request.getParameter("userName");

        if (userDAO.delYWY(userName))
        {
            request.setAttribute(KeyConstant.MESSAGE, "人员" + userName + "已经成功删除");
        }
        else
        {
            request.setAttribute(KeyConstant.ERROR_MESSAGE, "删除人员失败");
        }

        return listCommon(mapping, form, request, reponse);
    }

    public ActionForward listCommon(ActionMapping mapping, ActionForm form,
                                    HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        User user = (User)request.getSession().getAttribute("user");

        List<User> list = userDAO.queryCommon(user);

        List<UserVO> list1 = new ArrayList<UserVO>();

        LocationBean temp = null;

        for (User user2 : list)
        {
            temp = locationManager.findLocationById(user2.getLocationID());

            UserVO vo = new UserVO();

            BeanUtil.copyProperties(vo, user2);

            if (temp != null)
            {
                vo.setLocationName(temp.getLocationName());
            }

            list1.add(vo);
        }

        request.setAttribute("userList", list1);

        return mapping.findForward("listCommon");
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
     * @return the outDAO
     */
    public OutDAO getOutDAO()
    {
        return outDAO;
    }

    /**
     * @param outDAO
     *            the outDAO to set
     */
    public void setOutDAO(OutDAO outDAO)
    {
        this.outDAO = outDAO;
    }

    /**
     * @return the menuItemManager
     */
    public MenuItemManager getMenuItemManager()
    {
        return menuItemManager;
    }

    /**
     * @param menuItemManager
     *            the menuItemManager to set
     */
    public void setMenuItemManager(MenuItemManager menuItemManager)
    {
        this.menuItemManager = menuItemManager;
    }

    /**
     * @return the ehcacheManager
     */
    public EhcacheManager getEhcacheManager()
    {
        return ehcacheManager;
    }

    /**
     * @param ehcacheManager
     *            the ehcacheManager to set
     */
    public void setEhcacheManager(EhcacheManager ehcacheManager)
    {
        this.ehcacheManager = ehcacheManager;
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
     * @return the consignDAO
     */
    public ConsignDAO getConsignDAO()
    {
        return consignDAO;
    }

    /**
     * @param consignDAO
     *            the consignDAO to set
     */
    public void setConsignDAO(ConsignDAO consignDAO)
    {
        this.consignDAO = consignDAO;
    }

    /**
     * @return the commonMamager
     */
    public CommonMamager getCommonMamager()
    {
        return commonMamager;
    }

    /**
     * @param commonMamager
     *            the commonMamager to set
     */
    public void setCommonMamager(CommonMamager commonMamager)
    {
        this.commonMamager = commonMamager;
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
     * @return the providerUserDAO
     */
    public ProviderUserDAO getProviderUserDAO()
    {
        return providerUserDAO;
    }

    /**
     * @param providerUserDAO
     *            the providerUserDAO to set
     */
    public void setProviderUserDAO(ProviderUserDAO providerUserDAO)
    {
        this.providerUserDAO = providerUserDAO;
    }

    /**
     * @return the productTypeVSCustomerDAO
     */
    public ProductTypeVSCustomerDAO getProductTypeVSCustomerDAO()
    {
        return productTypeVSCustomerDAO;
    }

    /**
     * @param productTypeVSCustomerDAO
     *            the productTypeVSCustomerDAO to set
     */
    public void setProductTypeVSCustomerDAO(ProductTypeVSCustomerDAO productTypeVSCustomerDAO)
    {
        this.productTypeVSCustomerDAO = productTypeVSCustomerDAO;
    }

    /**
     * @return the providerDAO
     */
    public ProviderDAO getProviderDAO()
    {
        return providerDAO;
    }

    /**
     * @param providerDAO
     *            the providerDAO to set
     */
    public void setProviderDAO(ProviderDAO providerDAO)
    {
        this.providerDAO = providerDAO;
    }

}
