/**
 * File Name: ComposeProductListenerTaxGlueImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.product.bean.ComposeProductBean;
import com.china.center.oa.product.listener.ComposeProductListener;
import com.china.center.oa.product.vo.ComposeFeeDefinedVO;


/**
 * TODO_OSGI 产品合成-运营总监通过(合成和分解)
 * 
 * @author ZHUZHU
 * @version 2011-6-8
 * @see ComposeProductListenerTaxGlueImpl
 * @since 3.0
 */
public class ComposeProductListenerTaxGlueImpl implements ComposeProductListener
{

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.listener.ComposeProductListener#onConfirmCompose(com.center.china.osgi.publics.User,
     *      com.china.center.oa.product.bean.ComposeProductBean)
     */
    public void onConfirmCompose(User user, ComposeProductBean bean)
        throws MYException
    {
        // 

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.product.listener.ComposeProductListener#onFindComposeFeeDefinedVO(com.china.center.oa.product.vo.ComposeFeeDefinedVO)
     */
    public void onFindComposeFeeDefinedVO(ComposeFeeDefinedVO vo)
        throws MYException
    {
        // 

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        // 
        return "ComposeProductListener.TaxGlueImpl";
    }

}
