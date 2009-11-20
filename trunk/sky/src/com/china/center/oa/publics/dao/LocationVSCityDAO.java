/**
 * File Name: DepartmentDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.publics.vo.LocationVSCityVO;
import com.china.center.oa.publics.vs.LocationVSCityBean;


/**
 * DepartmentDAO
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see LocationVSCityDAO
 * @since 1.0
 */
@Bean(name = "locationVSCityDAO")
public class LocationVSCityDAO extends BaseDAO2<LocationVSCityBean, LocationVSCityVO>
{
}
