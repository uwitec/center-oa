/**
 * File Name: MenuItemDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.dao;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.publics.bean.MenuItemBean;

/**
 * MenuItemDAO
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see MenuItemDAO
 * @since 1.0
 */
@Bean(name = "menuItemDAO")
public class MenuItemDAO extends BaseDAO2<MenuItemBean, MenuItemBean>
{

}
