/**
 * File Name: MakeExtDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.dao;

import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customize.make.bean.MakeExtBean;

/**
 * MakeExtDAO
 * 
 * @author ZHUZHU
 * @version 2009-10-18
 * @see MakeExtDAO
 * @since 1.0
 */
@Bean(name = "makeExtDAO")
public class MakeExtDAO extends BaseDAO2<MakeExtBean, MakeExtBean>
{

}
