/**
 *
 */
package com.china.centet.yongyin.facade.impl;


import com.china.center.tools.Security;
import com.china.centet.yongyin.bean.StafferBean;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.bean.helper.LoginHelper;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.dao.StafferDAO;
import com.china.centet.yongyin.dao.UserDAO;
import com.china.centet.yongyin.facade.CommonFacede;


/**
 * @author Administrator
 */
public class CommonFacedeImpl implements CommonFacede
{
    private UserDAO userDAO = null;

    private StafferDAO stafferDAO = null;

    /**
     * 实现
     */
    public User login(String name, String password)
    {
        User user = userDAO.findUserByLoginName(name);

        if (user == null)
        {
            return null;
        }

        // 锁定处理
        if (user.getStatus() == Constant.LOGIN_STATUS_LOCK)
        {
            return null;
        }

        // 验证密码
        if (user.getPassword().equals(Security.getSecurity(password)))
        {
            return user;
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

            return null;
        }
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

    public User login2(String name, String password, String rand, String key, String randKey)
    {
        User user = userDAO.findUserByLoginName(name);

        String randVal = rand.toUpperCase();

        if (user == null)
        {
            return null;
        }

        // 锁定处理
        if (user.getStatus() == Constant.LOGIN_STATUS_LOCK)
        {
            return null;
        }

        StafferBean stafferBean = stafferDAO.find(user.getStafferId());

        if (stafferBean == null)
        {
            return null;
        }

        // 验证密码
        if (user.getPassword().equals(Security.getSecurity(password))
            && handleEncLock(key, randKey, randVal, true, stafferBean.getPwkey()))
        {
            return user;
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

            return null;
        }
    }

    private boolean handleEncLock(String key, String randKey, String randVal, boolean hasEncLock,
                                  String pwkey)
    {
        return !hasEncLock || LoginHelper.encRadomStr(pwkey, key, randVal).equals(randKey);
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

}
