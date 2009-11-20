/**
 * File Name: CreditItemDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-27<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.credit.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.credit.bean.CreditItemBean;
import com.china.center.oa.credit.vo.CreditItemVO;


/**
 * CreditItemDAO
 * 
 * @author ZHUZHU
 * @version 2009-10-27
 * @see CreditItemDAO
 * @since 1.0
 */
@Bean(name = "creditItemDAO")
public class CreditItemDAO extends BaseDAO2<CreditItemBean, CreditItemVO>
{

}
