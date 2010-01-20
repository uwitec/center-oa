/**
 * File Name: LocationDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.vo.LocationVO;


/**
 * LocationDAO
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see LocationDAO
 * @since 1.0
 */
@Bean(name = "locationDAO")
public class LocationDAO extends BaseDAO2<LocationBean, LocationVO>
{
    public int countCode(String code)
    {
        return this.jdbcOperation.queryForInt("where code = ?", this.claz, code);
    }
}
