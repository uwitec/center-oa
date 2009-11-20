/**
 * File Name: BillDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-6-24<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.ext.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.Bill;


/**
 * BillDAO
 * 
 * @author zhuzhu
 * @version 2009-6-24
 * @see BillDAO
 * @since 1.0
 */
@Bean(name = "billDAO")
public class BillDAO extends BaseDAO2<Bill, Bill>
{

}
