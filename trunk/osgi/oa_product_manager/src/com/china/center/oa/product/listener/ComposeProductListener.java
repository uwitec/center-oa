/**
 * File Name: ComposeProductListener.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-5-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.product.listener;


import com.center.china.osgi.publics.ParentListener;
import com.china.center.common.MYException;
import com.china.center.oa.product.vo.ComposeFeeDefinedVO;


/**
 * ComposeProductListener
 * 
 * @author ZHUZHU
 * @version 2011-5-8
 * @see ComposeProductListener
 * @since 3.0
 */
public interface ComposeProductListener extends ParentListener
{
    /**
     * 查询合成定义
     * 
     * @param stafferId
     */
    void onFindComposeFeeDefinedVO(ComposeFeeDefinedVO vo)
        throws MYException;
}
