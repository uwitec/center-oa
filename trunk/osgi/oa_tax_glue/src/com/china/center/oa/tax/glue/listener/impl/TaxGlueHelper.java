/**
 * File Name: TaxGlueHelper.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-6-19<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tax.glue.listener.impl;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.china.center.oa.tax.bean.TaxBean;
import com.china.center.oa.tax.dao.TaxDAO;


/**
 * TaxGlueHelper
 * 
 * @author ZHUZHU
 * @version 2011-6-19
 * @see TaxGlueHelper
 * @since 3.0
 */
public abstract class TaxGlueHelper
{
    private static final Log badLog = LogFactory.getLog("bad");

    /**
     * TODO 统一兼容性处理
     * 
     * @param bankId
     * @param taxDAO
     * @return
     */
    public static boolean bankGoon(String bankId, TaxDAO taxDAO)
    {
        // 这里暂时不启用
        TaxBean inTax = taxDAO.findByBankId(bankId);

        if (inTax == null)
        {
            badLog.error("Miss Bakn tax:" + bankId);

            return false;
        }

        TaxBean outTax = taxDAO.findTempByBankId(bankId);

        if (outTax == null)
        {
            badLog.error("Miss Temp tax:" + bankId);

            return false;
        }

        return true;
    }
}
