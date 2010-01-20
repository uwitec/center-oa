/**
 * File Name: MakeViewDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2009-12-29<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customize.make.bean.MakeViewBean;
import com.china.center.oa.customize.make.vo.MakeViewVO;


/**
 * MakeViewDAO
 * 
 * @author ZHUZHU
 * @version 2009-12-29
 * @see MakeViewDAO
 * @since 1.0
 */
@Bean(name = "makeViewDAO")
public class MakeViewDAO extends BaseDAO2<MakeViewBean, MakeViewVO>
{

}
