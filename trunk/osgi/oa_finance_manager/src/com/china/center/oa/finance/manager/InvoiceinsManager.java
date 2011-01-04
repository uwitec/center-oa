/**
 * File Name: InvoiceinsManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-1-1<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.finance.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.finance.bean.InvoiceinsBean;
import com.china.center.oa.finance.vo.InvoiceinsVO;


/**
 * InvoiceinsManager
 * 
 * @author ZHUZHU
 * @version 2011-1-1
 * @see InvoiceinsManager
 * @since 3.0
 */
public interface InvoiceinsManager
{
    boolean addInvoiceinsBean(User user, InvoiceinsBean bean)
        throws MYException;

    boolean deleteInvoiceinsBean(User user, String id)
        throws MYException;

    InvoiceinsVO findVO(String id);
}
