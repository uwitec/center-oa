/**
 * File Name: DepartmentDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.publics.dao;


import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.publics.bean.CityBean;


/**
 * DepartmentDAO
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see CityDAO
 * @since 1.0
 */
@Bean(name = "cityDAO")
public class CityDAO extends BaseDAO2<CityBean, CityBean>
{
    public List<CityBean> queryCanAssignCity(String provinceId, String locationId)
    {
        String sql = "where parentId = ? and id not in (select cityID from t_center_vs_locationcity where locationId <> ?)";

        return this.jdbcOperation.queryObjects(sql, this.claz, provinceId, locationId).list(
            this.claz);
    }

    /**
     * 根据名称模糊查询地市
     * 
     * @param name
     * @return
     */
    public CityBean findCityByName(String name)
    {
        List<CityBean> list = this.jdbcOperation.queryForList("where name like ?", claz, name);

        if (list.size() != 1)
        {
            return null;
        }

        return list.get(0);
    }
}
