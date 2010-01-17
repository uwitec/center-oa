/**
 * File Name: StockPayDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-1-17<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.centet.yongyin.bean.StockPayBean;
import com.china.centet.yongyin.vo.StockPayBeanVO;


/**
 * StockPayDAO
 * 
 * @author ZHUZHU
 * @version 2010-1-17
 * @see StockPayDAO
 * @since 1.0
 */
@Bean(name = "stockPayDAO")
public class StockPayDAO extends BaseDAO2<StockPayBean, StockPayBeanVO>
{

}
