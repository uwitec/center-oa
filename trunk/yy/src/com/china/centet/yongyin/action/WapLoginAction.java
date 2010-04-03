/**
 * File Name: WapLoginAction.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-4-3<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.action;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.common.KeyConstant;
import com.china.center.tools.Security;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.LocationBean;
import com.china.centet.yongyin.bean.StafferBean;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.constant.Constant;
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


/**
 * WapLoginAction
 * 
 * @author ZHUZHU
 * @version 2010-4-3
 * @see WapLoginAction
 * @since 1.0
 */
public class WapLoginAction extends DispatchAction
{
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

        String spassword = request.getParameter("spassword");

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

        // 锁定处理
        if (user.getStatus() == Constant.LOGIN_STATUS_LOCK)
        {
            _accessLog.info(logLogin(request, user, false) + ',' + spassword);

            request.getSession().setAttribute(KeyConstant.ERROR_MESSAGE, "用户被锁定,请联系管理员解锁!");

            return mapping.findForward("error");
        }

        // 验证密码
        if ( !real || (user.getPassword().equals(Security.getSecurity(password))))
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

            // wap登录的标识
            request.getSession().setAttribute("gwap", user);

            request.getSession().setAttribute("flag", flag);

            request.getSession().setAttribute("user", user);

            request.getSession().setAttribute("GTime", TimeTools.now("yyyy-MM-dd"));

            List<LocationBean> allOutLocation = new ArrayList();

            LocationBean localLocation = locationManager.findLocationById(user.getLocationID());

            allOutLocation.add(localLocation);

            if ( !"0".equals(user.getLocationID()))
            {
                LocationBean center = locationManager.findLocationById("0");

                allOutLocation.add(center);
            }

            request.getSession().setAttribute("allOutLocation", allOutLocation);
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

        ActionForward forward = mapping.findForward("success");

        String path = forward.getPath();

        return new ActionForward(path, true);
    }

    public ActionForward logout(ActionMapping actionMapping, ActionForm actionForm,
                                HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession(false);

        if (session == null)
        {
            return actionMapping.findForward("toIndex");
        }

        session.invalidate();

        return actionMapping.findForward("toIndex");
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

    private String logLogin(HttpServletRequest request, User user, boolean success)
    {
        return request.getRemoteAddr() + ',' + user.getName() + ',' + user.getStafferName() + ','
               + success;
    }
}
