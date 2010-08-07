/**
 * File Name: CommonManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-6-21<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.manager.impl;


import java.util.Iterator;
import java.util.List;

import org.china.center.spring.ex.annotation.Exceptional;

import com.china.center.oa.publics.bean.AreaBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.StafferConstant;
import com.china.center.oa.publics.dao.AreaDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.manager.CommonManager;


/**
 * CommonManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-6-21
 * @see CommonManagerImpl
 * @since 1.0
 */
@Exceptional
public class CommonManagerImpl implements CommonManager
{
    private AreaDAO areaDAO = null;

    private StafferDAO stafferDAO = null;

    private UserDAO userDAO = null;

    public List<AreaBean> queryAreaByParentId(String parentId)
    {
        return areaDAO.queryEntityBeansByFK(parentId);
    }

    public List<StafferBean> queryStafferByLocationId(String locationId)
    {
        return stafferDAO.queryStafferByLocationId(locationId);
    }

    /**
     * 查询区域下的是业务员考核的职员
     * 
     * @param locationId
     * @param type
     * @param attType
     * @return
     */
    public List<StafferBean> queryStafferByLocationIdAndFiter(String locationId, int type, int attType)
    {
        List<StafferBean> stafferList = stafferDAO.queryStafferByLocationId(locationId);

        // 当是分公司考核
        if (attType == 0)
        {
            type = -1;
        }

        for (Iterator iterator = stafferList.iterator(); iterator.hasNext();)
        {
            StafferBean stafferBean = (StafferBean)iterator.next();

            if (type == -1)
            {
                if (stafferBean.getExamType() != StafferConstant.EXAMTYPE_EXPAND
                    && stafferBean.getExamType() != StafferConstant.EXAMTYPE_TERMINAL)
                {
                    iterator.remove();
                }
            }
            else
            {
                if (stafferBean.getExamType() != type)
                {
                    iterator.remove();
                }
            }
        }

        return stafferList;
    }

    /**
     * @return the areaDAO
     */
    public AreaDAO getAreaDAO()
    {
        return areaDAO;
    }

    /**
     * @param areaDAO
     *            the areaDAO to set
     */
    public void setAreaDAO(AreaDAO areaDAO)
    {
        this.areaDAO = areaDAO;
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
}
