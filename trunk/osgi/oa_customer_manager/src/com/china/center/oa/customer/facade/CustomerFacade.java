/**
 * File Name: CustomerFacade.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.facade;


import java.io.Serializable;

import com.china.center.common.MYException;
import com.china.center.oa.credit.bean.CreditItemBean;
import com.china.center.oa.credit.bean.CreditItemSecBean;
import com.china.center.oa.credit.bean.CreditItemThrBean;
import com.china.center.oa.credit.bean.CreditLevelBean;
import com.china.center.oa.customer.bean.AssignApplyBean;
import com.china.center.oa.customer.bean.CustomerApplyBean;
import com.china.center.oa.customer.bean.CustomerCheckBean;
import com.china.center.oa.customer.bean.CustomerCheckItemBean;


/**
 * CustomerFacade
 * 
 * @author ZHUZHU
 * @version 2010-10-6
 * @see CustomerFacade
 * @since 1.0
 */
public interface CustomerFacade
{
    boolean applyAddCustomer(String userId, CustomerApplyBean bean)
        throws MYException;

    boolean delApplyCustomer(String userId, String cid)
        throws MYException;

    boolean applyUpdateCustomer(String userId, CustomerApplyBean bean)
        throws MYException;

    boolean applyDelCustomer(String userId, CustomerApplyBean bean)
        throws MYException;

    boolean rejectApplyCustomer(String userId, String cid, String reson)
        throws MYException;

    boolean passApplyCustomer(String userId, String cid)
        throws MYException;

    boolean assignApplyCustomerCode(String userId, String cid, String code)
        throws MYException;

    boolean addAssignApply(String userId, AssignApplyBean bean)
        throws MYException;

    boolean passAssignApply(String userId, String cid)
        throws MYException;

    boolean checkHisCustomer(String userId, String cid)
        throws MYException;

    boolean rejectAssignApply(String userId, String cid)
        throws MYException;

    boolean reclaimAssignCustomer(String userId, String cid)
        throws MYException;

    boolean reclaimStafferAssignCustomer(String userId, String stafferId, int flag)
        throws MYException;

    boolean addCheckBean(String userId, CustomerCheckBean bean)
        throws MYException;

    boolean goonBean(String userId, CustomerCheckBean bean)
        throws MYException;

    boolean passCheckBean(String userId, String id)
        throws MYException;

    boolean updateCheckItem(String userId, CustomerCheckItemBean bean)
        throws MYException;

    boolean rejectCheckBean(String userId, String id)
        throws MYException;

    boolean delCheckBean(String userId, String id)
        throws MYException;

    boolean addCreditItemThr(String userId, CreditItemThrBean bean)
        throws MYException;

    boolean updateCreditItemThr(String userId, CreditItemThrBean bean)
        throws MYException;

    boolean updateCreditItem(String userId, CreditItemBean bean)
        throws MYException;

    boolean updateCreditItemSec(String userId, CreditItemSecBean bean)
        throws MYException;

    boolean deleteCreditItemThr(String userId, Serializable id)
        throws MYException;

    boolean doPassApplyConfigStaticCustomerCredit(String userId, String cid)
        throws MYException;

    boolean doRejectApplyConfigStaticCustomerCredit(String userId, String cid)
        throws MYException;

    boolean updateCreditLevel(String userId, CreditLevelBean bean)
        throws MYException;

    boolean interposeCredit(String userId, String cid, double newCreditVal)
        throws MYException;
}
