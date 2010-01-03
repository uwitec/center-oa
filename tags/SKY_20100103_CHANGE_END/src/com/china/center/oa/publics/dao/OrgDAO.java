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
import com.china.center.oa.publics.vo.OrgVO;
import com.china.center.oa.publics.vs.OrgBean;

/**
 * DepartmentDAO
 * 
 * @author zhuzhu
 * @version 2008-11-2
 * @see OrgDAO
 * @since 1.0
 */
@Bean(name = "orgDAO")
public class OrgDAO extends BaseDAO2<OrgBean, OrgVO>
{

}
