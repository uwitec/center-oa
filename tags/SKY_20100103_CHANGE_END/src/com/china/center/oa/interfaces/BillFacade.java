/**
 * File Name: BillFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-6-24<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.interfaces;


import com.china.center.common.MYException;
import com.china.centet.yongyin.bean.Bill;


/**
 * BillFacade
 * 
 * @author zhuzhu
 * @version 2009-6-24
 * @see BillFacade
 * @since 1.0
 */
public interface BillFacade
{
    boolean addBill(String stafferId, Bill bill, String budgetItemId, String budgetId)
        throws MYException;
}
