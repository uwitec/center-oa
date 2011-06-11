/**
 * File Name: OutListenerTaxGlueImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.publics.wrap.ResultBean;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.listener.OutListener;


/**
 * TODO_OSGI 入库单/销售单/结算单
 * 
 * @author ZHUZHU
 * @version 2011-6-8
 * @see OutListenerTaxGlueImpl
 * @since 3.0
 */
public class OutListenerTaxGlueImpl implements OutListener
{

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onCancleBadDebts(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public void onCancleBadDebts(User user, OutBean bean)
        throws MYException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onCheck(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public void onCheck(User user, OutBean bean)
        throws MYException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onConfirmOutOrBuy(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public void onConfirmOutOrBuy(User user, OutBean bean)
        throws MYException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onDelete(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public void onDelete(User user, OutBean bean)
        throws MYException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onHadPay(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public ResultBean onHadPay(User user, OutBean bean)
    {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onOutBalancePass(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBalanceBean)
     */
    public void onOutBalancePass(User user, OutBalanceBean bean)
        throws MYException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onPass(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public void onPass(User user, OutBean bean)
        throws MYException
    {
        // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#onReject(com.center.china.osgi.publics.User,
     *      com.china.center.oa.sail.bean.OutBean)
     */
    public void onReject(User user, OutBean bean)
        throws MYException
    {

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.listener.OutListener#outNeedPayMoney(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    public double outNeedPayMoney(User user, String fullId)
    {
        return 0;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.center.china.osgi.publics.ParentListener#getListenerType()
     */
    public String getListenerType()
    {
        return "OutListener.TaxGlueImpl";
    }

}
