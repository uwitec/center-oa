/*
 * File Name: MenuItemManager.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.manager;


import java.util.List;

import com.china.centet.yongyin.bean.MenuItemBean;
import com.china.centet.yongyin.dao.MenuItemDAO;


/**
 * MenuItemManager
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class MenuItemManager
{
    private MenuItemDAO menuItemDAO = null;

    public void init()
    {}

    /**
     * @return the menuItemList
     */
    public List<MenuItemBean> getMenuItemList()
    {
        return menuItemDAO.listMenuItem();
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
}
