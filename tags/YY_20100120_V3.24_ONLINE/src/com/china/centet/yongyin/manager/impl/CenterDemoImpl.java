/**
 * 
 */
package com.china.centet.yongyin.manager.impl;


import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;

import com.china.center.common.MYException;
import com.china.centet.yongyin.bean.BankBean;
import com.china.centet.yongyin.bean.BillDemo;
import com.china.centet.yongyin.bean.ProviderBean;
import com.china.centet.yongyin.manager.CenterDemo;


/**
 * @author Administrator
 */
@Bean(name = "centerDemo")
@Exceptional(sensitiveException = {RuntimeException.class})
public class CenterDemoImpl implements CenterDemo
{
    /**
     * BankBean
     */
    public BankBean getBank(BillDemo bill, ProviderBean[] customer)
        throws MYException
    {
        BankBean bean = new BankBean();

        bean.setId("1001");

        bean.setLocationName("ÄÏ¾©");

        return bean;
    }
}
