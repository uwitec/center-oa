/**
 * File Name: ProductExamineItemDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-14<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.examine.dao;


import net.sourceforge.sannotations.annotation.Bean;

import com.china.center.jdbc.inter.impl.BaseDAO2;
import com.china.center.oa.examine.bean.ProductExamineItemBean;
import com.china.center.oa.examine.vo.ProductExamineItemVO;


/**
 * ProductExamineItemDAO
 * 
 * @author zhuzhu
 * @version 2009-2-14
 * @see ProductExamineItemDAO
 * @since 1.0
 */
@Bean(name = "productExamineItemDAO")
public class ProductExamineItemDAO extends BaseDAO2<ProductExamineItemBean, ProductExamineItemVO>
{

}
