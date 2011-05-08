/**
 * File Name: ComposeProductListenerTaxImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-5-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.listener.impl;


import com.china.center.common.MYException;
import com.china.center.oa.product.listener.ComposeProductListener;
import com.china.center.oa.product.vo.ComposeFeeDefinedVO;
import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.dao.TaxDAO;
import com.china.center.tools.StringTools;


/**
 * ComposeProductListenerTaxImpl
 * 
 * @author ZHUZHU
 * @version 2011-5-8
 * @see ComposeProductListenerTaxImpl
 * @since 3.0
 */
public class ComposeProductListenerTaxImpl implements ComposeProductListener
{
    private TaxDAO taxDAO = null;

    /**
     * default constructor
     */
    public ComposeProductListenerTaxImpl()
    {
    }

    public void onFindComposeFeeDefinedVO(ComposeFeeDefinedVO vo)
        throws MYException
    {
        if (StringTools.isNullOrNone(vo.getTaxId()))
        {
            return;
        }

        TaxBean tax = taxDAO.find(vo.getTaxId());

        if (tax != null)
        {
            vo.setTaxName(tax.getCode() + tax.getName());
        }
    }

    public String getListenerType()
    {
        return "ComposeProductListener.TaxImpl";
    }

    /**
     * @return the taxDAO
     */
    public TaxDAO getTaxDAO()
    {
        return taxDAO;
    }

    /**
     * @param taxDAO
     *            the taxDAO to set
     */
    public void setTaxDAO(TaxDAO taxDAO)
    {
        this.taxDAO = taxDAO;
    }

}
