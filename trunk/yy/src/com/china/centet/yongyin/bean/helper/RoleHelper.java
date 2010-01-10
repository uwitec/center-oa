/*
 * File Name: RoleHelper.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.bean.helper;


import com.china.centet.yongyin.bean.BaseUser;
import com.china.centet.yongyin.bean.Role;
import com.china.centet.yongyin.bean.User;


/**
 * <描述>
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public abstract class RoleHelper
{
    public static int getIndexFromRole(User user)
    {
        Role role = user.getRole();

        if (role == Role.COMMON)
        {
            return 0;
        }

        if (role == Role.ADMIN)
        {
            return 1;
        }

        if (role == Role.SEC)
        {
            return 2;
        }

        if (role == Role.TOP)
        {
            return 3;
        }

        if (role == Role.MANAGER)
        {
            return 4;
        }

        if (role == Role.FLOW)
        {
            return 5;
        }

        if (role == Role.THR)
        {
            return 6;
        }

        if (role == Role.SHOP)
        {
            return 7;
        }

        if (role == Role.SHOPMEMBER)
        {
            return 8;
        }

        if (role == Role.PRICE)
        {
            return 9;
        }

        if (role == Role.REPRICE)
        {
            return 10;
        }

        if (role == Role.STOCK)
        {
            return 11;
        }

        if (role == Role.STOCKMANAGER)
        {
            return 12;
        }

        if (role == Role.WORKFLOW)
        {
            return 13;
        }

        if (role == Role.NETASK)
        {
            return 14;
        }

        return -1;
    }

    /**
     * @param bean
     * @param i
     */
    public static void setRole(User bean, int i)
    {
        if (i == 0)
        {
            bean.setRole(Role.COMMON);
        }

        if (i == 1)
        {
            bean.setRole(Role.ADMIN);
        }

        if (i == 2)
        {
            bean.setRole(Role.SEC);
        }

        if (i == 3)
        {
            bean.setRole(Role.TOP);
        }

        if (i == 4)
        {
            bean.setRole(Role.MANAGER);
        }

        if (i == 5)
        {
            bean.setRole(Role.FLOW);
        }

        if (i == 6)
        {
            bean.setRole(Role.THR);
        }

        if (i == 7)
        {
            bean.setRole(Role.SHOP);
        }

        if (i == 8)
        {
            bean.setRole(Role.SHOPMEMBER);
        }

        // 询价员
        if (i == 9)
        {
            bean.setRole(Role.PRICE);
        }

        // 核价员
        if (i == 10)
        {
            bean.setRole(Role.REPRICE);
        }

        // 核价员
        if (i == 11)
        {
            bean.setRole(Role.STOCK);
        }

        // 核价员
        if (i == 12)
        {
            bean.setRole(Role.STOCKMANAGER);
        }

        // 核价员
        if (i == 13)
        {
            bean.setRole(Role.WORKFLOW);
        }

        if (i == 14)
        {
            bean.setRole(Role.NETASK);
        }
    }

    /**
     * @param bean
     * @param i
     */
    public static void setBaseRole(BaseUser bean, int i)
    {
        if (i == 0)
        {
            bean.setBaseRole(Role.COMMON);
        }

        if (i == 1)
        {
            bean.setBaseRole(Role.ADMIN);
        }

        if (i == 2)
        {
            bean.setBaseRole(Role.SEC);
        }

        if (i == 3)
        {
            bean.setBaseRole(Role.TOP);
        }

        if (i == 4)
        {
            bean.setBaseRole(Role.MANAGER);
        }

        if (i == 5)
        {
            bean.setBaseRole(Role.FLOW);
        }

        if (i == 6)
        {
            bean.setBaseRole(Role.THR);
        }

        if (i == 7)
        {
            bean.setBaseRole(Role.SHOP);
        }

        if (i == 8)
        {
            bean.setBaseRole(Role.SHOPMEMBER);
        }

        // 询价员
        if (i == 9)
        {
            bean.setBaseRole(Role.PRICE);
        }

        // 核价员
        if (i == 10)
        {
            bean.setBaseRole(Role.REPRICE);
        }

        // 核价员
        if (i == 11)
        {
            bean.setBaseRole(Role.STOCK);
        }

        // 核价员
        if (i == 12)
        {
            bean.setBaseRole(Role.STOCKMANAGER);
        }

        // 核价员
        if (i == 13)
        {
            bean.setBaseRole(Role.WORKFLOW);
        }

        if (i == 14)
        {
            bean.setBaseRole(Role.NETASK);
        }
    }
}
