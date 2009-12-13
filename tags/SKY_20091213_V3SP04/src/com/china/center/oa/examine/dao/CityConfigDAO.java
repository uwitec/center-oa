/**
 * File Name: CityConfigDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-1-3<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.examine.bean.CityConfigBean;
import com.china.center.oa.examine.vo.CityConfigVO;


/**
 * CityConfigDAO
 * 
 * @author zhuzhu
 * @version 2009-1-3
 * @see CityConfigDAO
 * @since 1.0
 */
@Bean(name = "cityConfigDAO")
public class CityConfigDAO extends BaseDAO2<CityConfigBean, CityConfigVO>
{

}
