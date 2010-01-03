/**
 * File Name: EnumManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-9<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager;


import java.util.ArrayList;
import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.common.taglib.MapBean;
import com.china.center.common.taglib.PageSelectOption;
import com.china.center.oa.publics.bean.EnumBean;
import com.china.center.oa.publics.dao.EnumDAO;


/**
 * EnumManager
 * 
 * @author zhuzhu
 * @version 2008-11-9
 * @see EnumManager
 * @since 1.0
 */
@Bean(name = "enumManager", initMethod = "init")
public class EnumManager
{
    private EnumDAO enumDAO = null;

    /**
     * default constructor
     */
    public EnumManager()
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

        try
        {
            // 加载资源
            Class.forName("com.china.center.oa.constant.DefinedCommon");
        }
        catch (ClassNotFoundException e)
        {}
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
