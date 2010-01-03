/**
 * File Name: ChangeLogDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-3-12<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customer.bean.ChangeLogBean;


/**
 * ChangeLogDAO
 * 
 * @author zhuzhu
 * @version 2009-3-12
 * @see ChangeLogDAO
 * @since 1.0
 */
@Bean(name = "changeLogDAO")
public class ChangeLogDAO extends BaseDAO2<ChangeLogBean, ChangeLogBean>
{

}
