/*
 * File Name: UpGradeDAO.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-8-16
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import com.china.centet.yongyin.bean.Version;


/**
 * 升级的DAO
 * 
 * @author zhuzhu
 * @version 2007-8-16
 * @see
 * @since
 */
public interface UpGradeDAO
{
    /**
     * 获得数据库里面的版本
     */
    Version getVersion();

    /**
     * 更新版本
     * 
     * @param version
     * @return
     */
    boolean updateVersion(Version version);

    void initMenuItem();
}
