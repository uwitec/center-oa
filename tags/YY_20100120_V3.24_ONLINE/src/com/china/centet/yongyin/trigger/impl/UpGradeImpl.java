/*
 * File Name: UpGradeImpl.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-8-16
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.trigger.impl;


import com.china.centet.yongyin.dao.UpGradeDAO;
import com.china.centet.yongyin.trigger.UpGrade;


/**
 * @author zhuzhu
 * @version 2007-8-16
 * @see
 * @since
 */
public class UpGradeImpl implements UpGrade
{
    private UpGradeDAO upGradeDAO = null;

    /**
     * default constructor
     */
    public UpGradeImpl()
    {}

    /**
     * initDB
     */
    public void initDB()
    {
        // 初始化菜单
        upGradeDAO.initMenuItem();
        System.out.println("更新菜单成功...");
    }

    /**
     * @return the upGradeDAO
     */
    public UpGradeDAO getUpGradeDAO()
    {
        return upGradeDAO;
    }

    /**
     * @param upGradeDAO
     *            the upGradeDAO to set
     */
    public void setUpGradeDAO(UpGradeDAO upGradeDAO)
    {
        this.upGradeDAO = upGradeDAO;
    }

}
