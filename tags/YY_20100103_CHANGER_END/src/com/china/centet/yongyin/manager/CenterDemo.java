/**
 * 
 */
package com.china.centet.yongyin.manager;


import com.china.center.common.MYException;
import com.china.centet.yongyin.bean.BankBean;
import com.china.centet.yongyin.bean.BillDemo;
import com.china.centet.yongyin.bean.ProviderBean;


/**
 * @author Administrator
 */
public interface CenterDemo
{
    BankBean getBank(BillDemo bill, ProviderBean[] customer)
        throws MYException;
}
