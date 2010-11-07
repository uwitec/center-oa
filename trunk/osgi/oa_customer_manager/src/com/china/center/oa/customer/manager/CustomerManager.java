/**
 * File Name: CustomerManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.manager;


import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.customer.bean.AssignApplyBean;
import com.china.center.oa.customer.bean.CustomerApplyBean;
import com.china.center.oa.customer.bean.CustomerBean;


/**
 * CustomerManager
 * 
 * @author ZHUZHU
 * @version 2010-10-6
 * @see CustomerManager
 * @since 1.0
 */
public interface CustomerManager
{
    boolean applyAddCustomer(User user, CustomerApplyBean bean)
        throws MYException;

    boolean addAssignApply(User user, AssignApplyBean bean)
        throws MYException;

    boolean applyUpdateCustomer(User user, CustomerApplyBean bean)
        throws MYException;

    /**
     * 申请利润分配
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    boolean applyUpdateCustomeAssignPer(User user, CustomerApplyBean bean)
        throws MYException;

    boolean applyDelCustomer(User user, CustomerApplyBean bean)
        throws MYException;

    /**
     * rejectApplyCustomer(增加和修改)
     * 
     * @param user
     * @param cid
     * @param reson
     * @return
     * @throws MYException
     */
    boolean rejectApplyCustomer(User user, String cid, String reson)
        throws MYException;

    /**
     * rejectApplyCustomer(利润分配)
     * 
     * @param user
     * @param cid
     * @param reson
     * @return
     * @throws MYException
     */
    boolean rejectApplyCustomerAssignPer(User user, String cid)
        throws MYException;

    /**
     * passApplyCustomer(修改和删除客户的)
     * 
     * @param user
     * @param cid
     * @return
     * @throws MYException
     */
    boolean passApplyCustomerAssignPer(User user, String cid)
        throws MYException;

    /**
     * passApplyCustomer(修改和删除客户的)
     * 
     * @param user
     * @param cid
     * @return
     * @throws MYException
     */
    boolean passApplyCustomer(User user, String cid)
        throws MYException;

    @Deprecated
    boolean assignApplyCustomerCode(User user, String cid, String code)
        throws MYException;

    boolean passAssignApply(User user, String cid)
        throws MYException;

    boolean reclaimAssignCustomer(User user, String cid)
        throws MYException;

    boolean reclaimStafferAssignCustomer(User user, String stafferId, int flag)
        throws MYException;

    boolean rejectAssignApply(User user, String cid)
        throws MYException;

    void addCustomer(User user, CustomerBean bean, String stafferId)
        throws MYException;

    boolean updateCustomerLever(User user, String id, int lever)
        throws MYException;

    boolean delApply(User user, String cid)
        throws MYException;

    boolean checkHisCustomer(User user, String cid)
        throws MYException;

    boolean hasCustomerAuth(String stafferId, String customerId)
        throws MYException;

    void synchronizationAllCustomerLocation();
}
