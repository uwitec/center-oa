/**
 * File Name: ComposeProductManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-2<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.product.bean.ComposeProductBean;
import com.china.center.oa.product.vo.ComposeProductVO;


/**
 * ComposeProductManager
 * 
 * @author ZHUZHU
 * @version 2010-10-2
 * @see ComposeProductManager
 * @since 1.0
 */
public interface ComposeProductManager
{
    /**
     * 合成产品
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    boolean addComposeProduct(User user, ComposeProductBean bean)
        throws MYException;

    ComposeProductVO findById(String id);
}
