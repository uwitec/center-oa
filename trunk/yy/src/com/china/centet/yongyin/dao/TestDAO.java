/**
 * File Name: TestDAO.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-3-10<br>
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.dao;


import com.china.center.jdbc.inter.impl.BaseDAO;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.BankBean;


/**
 * <ÃèÊö>
 * 
 * @author zhuzhu
 * @version 2008-3-10
 * @see
 * @since
 */
public class TestDAO extends BaseDAO<BankBean, BankBean>
{
    public void setClaz()
    {}

    public static void main(String[] args)
    {
        String endTime = TimeTools.getSpecialDateStringByDays("2010-03-01 00:00:00",
                21);
        
        System.out.println(endTime);
    }
}
