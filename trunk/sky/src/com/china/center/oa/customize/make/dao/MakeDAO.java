/**
 * File Name: MakeDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.customize.make.bean.MakeBean;
import com.china.center.oa.customize.make.vo.MakeVO;


/**
 * MakeDAO
 * 
 * @author ZHUZHU
 * @version 2009-10-8
 * @see MakeDAO
 * @since 1.0
 */
@Bean(name = "makeDAO")
public class MakeDAO extends BaseDAO2<MakeBean, MakeVO>
{

}
