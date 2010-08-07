/**
 * File Name: EnumManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-21<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager.impl;


import java.util.ArrayList;
import java.util.List;

import com.china.center.common.taglib.MapBean;
import com.china.center.common.taglib.PageSelectOption;
import com.china.center.oa.publics.bean.EnumBean;
import com.china.center.oa.publics.dao.EnumDAO;
import com.china.center.oa.publics.manager.EnumManager;


/**
 * EnumManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-6-21
 * @see EnumManagerImpl
 * @since 1.0
 */
public class EnumManagerImpl implements EnumManager
{
    private EnumDAO enumDAO = null;

    /**
     * default constructor
     */
    public EnumManagerImpl()
    {}

    /**
     * 加载标签资源
     */
    public void init()
    {
        List<EnumBean> list = enumDAO.listEntityBeans();

        for (EnumBean enumBean : list)
        {
            List<MapBean> iList = PageSelectOption.optionMap.get(String.valueOf(enumBean.getType()));

            if (iList == null)
            {
                iList = new ArrayList<MapBean>();

                PageSelectOption.optionMap.put(String.valueOf(enumBean.getType()), iList);
            }

            iList.add(new MapBean(enumBean.getKey(), enumBean.getValue()));
        }
    }

    /**
     * @return the enumDAO
     */
    public EnumDAO getEnumDAO()
    {
        return enumDAO;
    }

    /**
     * @param enumDAO
     *            the enumDAO to set
     */
    public void setEnumDAO(EnumDAO enumDAO)
    {
        this.enumDAO = enumDAO;
    }
}
