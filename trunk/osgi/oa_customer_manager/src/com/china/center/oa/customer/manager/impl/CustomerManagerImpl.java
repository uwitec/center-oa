/**
 * File Name: CustomerManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-10-6<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customer.manager.impl;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.annosql.constant.AnoConstant;
import com.china.center.oa.customer.bean.AssignApplyBean;
import com.china.center.oa.customer.bean.ChangeLogBean;
import com.china.center.oa.customer.bean.CustomerApplyBean;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.bean.CustomerHisBean;
import com.china.center.oa.customer.constant.CustomerConstant;
import com.china.center.oa.customer.dao.AssignApplyDAO;
import com.china.center.oa.customer.dao.ChangeLogDAO;
import com.china.center.oa.customer.dao.CustomerApplyDAO;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.customer.dao.CustomerHisDAO;
import com.china.center.oa.customer.dao.StafferVSCustomerDAO;
import com.china.center.oa.customer.helper.CustomerHelper;
import com.china.center.oa.customer.manager.CustomerManager;
import com.china.center.oa.customer.vs.StafferVSCustomerBean;
import com.china.center.oa.publics.bean.CityBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.OperationConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.dao.CityDAO;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.LocationVSCityDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.helper.NotifyHelper;
import com.china.center.oa.publics.manager.NotifyManager;
import com.china.center.oa.publics.vs.LocationVSCityBean;
import com.china.center.tools.BeanUtil;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * CustomerManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-10-6
 * @see CustomerManagerImpl
 * @since 1.0
 */
@Exceptional
public class CustomerManagerImpl implements CustomerManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private CustomerApplyDAO customerApplyDAO = null;

    private CustomerDAO customerDAO = null;

    private CustomerHisDAO customerHisDAO = null;

    private LocationVSCityDAO locationVSCityDAO = null;

    private StafferVSCustomerDAO stafferVSCustomerDAO = null;

    private CityDAO cityDAO = null;

    private ChangeLogDAO changeLogDAO = null;

    private CommonDAO commonDAO = null;

    private NotifyManager notifyManager = null;

    private AssignApplyDAO assignApplyDAO = null;

    private StafferDAO stafferDAO = null;

    public CustomerManagerImpl()
    {
    }

    /**
     * applyAddCustomer(申请增加客户的code自动生成,且自动提交)(本质就是增加客户)
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public boolean applyAddCustomer(User user, CustomerApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getName());

        bean.setName(bean.getName().trim());

        checkAddBean(bean);

        // 自动生成code
        String code = commonDAO.getSquenceString20();

        CustomerHelper.encryptCustomer(bean);

        bean.setStatus(CustomerConstant.STATUS_APPLY);

        bean.setUpdaterId(user.getStafferId());

        bean.setOpr(CustomerConstant.OPR_ADD);

        bean.setLocationId(user.getLocationId());

        bean.setId(commonDAO.getSquenceString());

        bean.setCode(code);

        bean.setLoginTime(TimeTools.now());

        bean.setCreateTime(TimeTools.now());

        customerApplyDAO.saveEntityBean(bean);

        // 移植分配code的逻辑 -----------------------------------

        checkAssignCode(bean, code);

        handleAdd(user, bean.getId(), bean);

        return true;
    }

    /**
     * addAssignApply(增加客户和职员的对应关系)
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean addAssignApply(User user, AssignApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        CustomerBean cus = checkAssign(bean);

        cus.setStatus(CustomerConstant.REAL_STATUS_APPLY);

        assignApplyDAO.saveEntityBean(bean);

        customerDAO.updateEntityBean(cus);

        return true;
    }

    /**
     * @param bean
     * @throws MYException
     */
    private CustomerBean checkAssign(AssignApplyBean bean)
        throws MYException
    {
        if (assignApplyDAO.countByUnique(bean.getCustomerId()) > 0)
        {
            throw new MYException("客户已经被申请");
        }

        CustomerBean cus = customerDAO.find(bean.getCustomerId());

        if (cus == null)
        {
            throw new MYException("申请分配的客户不存在");
        }

        if (cus.getStatus() != CustomerConstant.REAL_STATUS_IDLE)
        {
            throw new MYException("申请分配的客户[%s]不在空闲态,请核实", cus.getName());
        }

        checkAtt(bean, cus);

        return cus;
    }

    /**
     * applyUpdateCustomer
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean applyUpdateCustomer(User user, CustomerApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getId());

        checkUpdateBean(bean);

        // 删除存在的(直接覆盖)
        customerApplyDAO.deleteEntityBean(bean.getId());

        bean.setUpdaterId(user.getStafferId());

        CustomerHelper.encryptCustomer(bean);

        bean.setStatus(CustomerConstant.STATUS_APPLY);

        bean.setOpr(CustomerConstant.OPR_UPDATE);

        bean.setLocationId(user.getLocationId());

        bean.setLoginTime(TimeTools.now());

        customerApplyDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * applyUpdateCustomer
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public boolean passApplyCustomerAssignPer(User user, CustomerApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getId());

        // 删除存在的(直接覆盖)
        customerApplyDAO.deleteEntityBean(bean.getId());

        bean.setUpdaterId(user.getStafferId());

        CustomerHelper.encryptCustomer(bean);

        bean.setStatus(CustomerConstant.STATUS_APPLY);

        bean.setOpr(CustomerConstant.OPR_UPATE_ASSIGNPER);

        bean.setLocationId(user.getLocationId());

        bean.setLoginTime(TimeTools.now());

        customerApplyDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * applyDelCustomer
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public boolean applyDelCustomer(User user, CustomerApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getId());

        checkDelCustomer(bean);

        // 删除存在的(直接覆盖)
        customerApplyDAO.deleteEntityBean(bean.getId());

        CustomerHelper.encryptCustomer(bean);

        bean.setUpdaterId(user.getStafferId());

        bean.setStatus(CustomerConstant.STATUS_APPLY);

        bean.setOpr(CustomerConstant.OPR_DEL);

        bean.setLocationId(user.getLocationId());

        bean.setLoginTime(TimeTools.now());

        customerApplyDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * 检查删除
     * 
     * @param bean
     * @throws MYException
     */
    private void checkDelCustomer(CustomerApplyBean bean)
        throws MYException
    {
        // NOTIFY 这里应该是OSGi实现的,当前暂时不动
        if (customerDAO.countCustomerInOut(bean.getId()) > 0
            || customerDAO.countCustomerInBill(bean.getId()) > 0)
        {
            throw new MYException("客户[%s]已经被使用,不能删除", bean.getName());
        }
    }

    /**
     * updateBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public boolean rejectApplyCustomer(User user, String cid, String reson)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        CustomerApplyBean bean = customerApplyDAO.find(cid);

        checkPass(bean);

        bean.setStatus(CustomerConstant.STATUS_REJECT);

        bean.setReson(reson);

        customerApplyDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * rejectApplyCustomerAssignPer
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean rejectApplyCustomerAssignPer(User user, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        CustomerApplyBean bean = customerApplyDAO.find(cid);

        if (bean.getOpr() != CustomerConstant.OPR_UPATE_ASSIGNPER)
        {
            throw new MYException("数据错误,请重新操作");
        }

        customerApplyDAO.deleteEntityBean(cid);

        notifyManager.notifyWithoutTransaction(bean.getUpdaterId(), NotifyHelper.simpleNotify(
            "您的客户[%s]分配比例申请已经被[%s]废弃", bean.getName(), user.getStafferName()));

        return true;
    }

    /**
     * updateBean
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean passApplyCustomer(User user, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        CustomerApplyBean bean = customerApplyDAO.find(cid);

        checkPass(bean);

        if (bean.getOpr() == CustomerConstant.OPR_ADD)
        {
            // 先审批改变状态
            customerApplyDAO.updateStatus(cid, CustomerConstant.STATUS_WAIT_CODE);
            // handleAdd(user, cid, bean);
        }

        if (bean.getOpr() == CustomerConstant.OPR_UPDATE)
        {
            handleUpdate(user, cid, bean);
        }

        if (bean.getOpr() == CustomerConstant.OPR_DEL)
        {
            checkDelCustomer(bean);

            handleDelete(cid, bean);
        }

        return true;
    }

    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean passApplyCustomerAssignPer(User user, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        CustomerApplyBean bean = customerApplyDAO.find(cid);

        checkPass(bean);

        handleUpdateAssignPer(user, cid, bean);

        notifyManager.notifyWithoutTransaction(bean.getUpdaterId(), NotifyHelper.simpleNotify(
            "您的客户[%s]分配比例申请已经被[%s]通过", bean.getName(), user.getStafferName()));

        return true;
    }

    /**
     * 分配编码(就是客户的增加)
     * 
     * @param user
     * @param cid
     * @return
     * @throws MYException
     */

    @Deprecated
    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean assignApplyCustomerCode(User user, String cid, String code)
        throws MYException
    {
        throw new MYException("can not find assignApplyCustomerCode");
    }

    /**
     * 通过分配申请
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean passAssignApply(User user, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        AssignApplyBean apply = assignApplyDAO.find(cid);

        CustomerBean cus = checkOprAssign(apply, true);

        checkAtt(apply, cus);

        // 删除申请
        assignApplyDAO.deleteEntityBean(cid);

        StafferVSCustomerBean vs = new StafferVSCustomerBean();

        vs.setStafferId(apply.getStafferId());

        vs.setCustomerId(apply.getCustomerId());

        // 保存对应关系
        addStafferVSCustomer(vs);

        // 修改客户状态
        cus.setStatus(CustomerConstant.REAL_STATUS_USED);

        customerDAO.updateEntityBean(cus);

        return true;
    }

    /**
     * 保存关系,顺便记录日志
     * 
     * @param vs
     */
    private void addStafferVSCustomer(StafferVSCustomerBean vs)
    {
        stafferVSCustomerDAO.saveEntityBean(vs);

        CustomerBean cb = customerDAO.find(vs.getCustomerId());

        if (cb == null)
        {
            return;
        }

        StafferBean sb = stafferDAO.find(vs.getStafferId());

        if (sb == null)
        {
            return;
        }

        ChangeLogBean log = new ChangeLogBean();

        log.setCustomerCode(cb.getId());
        log.setCustomerName(cb.getName());
        log.setCustomerCode(cb.getCode());

        log.setStafferId(sb.getId());

        log.setStafferName(sb.getName());

        log.setLogTime(TimeTools.now());

        log.setOperation(OperationConstant.OPERATION_CHANGELOG_ADD);

        changeLogDAO.saveEntityBean(log);
    }

    /**
     * 检查属性
     * 
     * @param apply
     * @param cus
     * @throws MYException
     */
    private void checkAtt(AssignApplyBean apply, CustomerBean cus)
        throws MYException
    {
        StafferBean sb = stafferDAO.find(apply.getStafferId());

        if (sb == null)
        {
            throw new MYException("申请的职员不存在");
        }

        // NOTE zhuzhu 判断客户的属性和业务的是否吻合
        if (cus.getSelltype() != sb.getExamType())
        {
            throw new MYException("职员[%s]的终端拓展属性和客户[%s]的终端拓展属性不一致", sb.getName(), cus.getName());
        }
    }

    /**
     * 回收分配客户
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean reclaimAssignCustomer(User user, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        // 删除对应关系
        stafferVSCustomerDAO.deleteEntityBeansByFK(cid, AnoConstant.FK_FIRST);

        // 恢复空闲状态
        customerDAO.updateCustomerstatus(cid, CustomerConstant.REAL_STATUS_IDLE);

        return true;
    }

    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean reclaimStafferAssignCustomer(User user, String stafferId, int flag)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, stafferId);

        // 先更新状态
        customerDAO.updateCustomerByStafferId(stafferId, CustomerConstant.REAL_STATUS_IDLE, flag);

        // 再删除对应关系(不能颠倒，否则无法更新客户状态)
        customerDAO.delCustomerByStafferId(stafferId, flag);

        return true;
    }

    /**
     * 驳回分配申请(就是删除)
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public synchronized boolean rejectAssignApply(User user, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        AssignApplyBean apply = assignApplyDAO.find(cid);

        CustomerBean cus = checkOprAssign(apply, false);

        // 删除申请
        assignApplyDAO.deleteEntityBean(cid);

        // 只有申请态中才更新
        if (cus.getStatus() == CustomerConstant.REAL_STATUS_APPLY)
        {
            // 修改客户状态
            cus.setStatus(CustomerConstant.REAL_STATUS_IDLE);

            customerDAO.updateEntityBean(cus);
        }

        return true;
    }

    /**
     * 检查分配客户操作的逻辑校验
     * 
     * @param apply
     * @return
     * @throws MYException
     */
    private CustomerBean checkOprAssign(AssignApplyBean apply, boolean pass)
        throws MYException
    {
        if (apply == null)
        {
            throw new MYException("申请不存在,请刷新数据");
        }

        CustomerBean cus = customerDAO.find(apply.getCustomerId());

        if (cus == null)
        {
            throw new MYException("申请分配的客户不存在");
        }

        if (pass && cus.getStatus() == CustomerConstant.REAL_STATUS_USED)
        {
            throw new MYException("申请分配的客户[%s]已经被使用,请核实", cus.getName());
        }

        return cus;
    }

    /**
     * @param cid
     * @param bean
     */
    private void handleDelete(String cid, CustomerApplyBean bean)
    {
        CustomerBean cbean = new CustomerBean();

        bean.setLoginTime(TimeTools.now());

        BeanUtil.copyProperties(cbean, bean);

        // 从apply里面删除
        customerApplyDAO.deleteEntityBean(cid);

        // 操作正表里面
        customerDAO.deleteEntityBean(cid);

        // 需要删除职员和客户的对应关系
        stafferVSCustomerDAO.deleteEntityBeansByFK(bean.getId(), AnoConstant.FK_FIRST);
    }

    /**
     * @param user
     * @param cid
     * @param bean
     * @throws MYException
     */
    private void handleUpdate(User user, String cid, CustomerApplyBean bean)
        throws MYException
    {
        CustomerBean cbean = new CustomerBean();

        CustomerHisBean hisbean = new CustomerHisBean();

        bean.setLoginTime(TimeTools.now());

        BeanUtil.copyProperties(cbean, bean);

        BeanUtil.copyProperties(hisbean, bean);

        // 从apply里面删除
        customerApplyDAO.deleteEntityBean(cid);

        checkRealAdd(cbean);

        CustomerBean oldBean = customerDAO.find(cid);

        if (oldBean == null)
        {
            throw new MYException("修改的客户不存在");
        }

        cbean.setCreditUpdateTime(oldBean.getCreditUpdateTime());
        cbean.setStatus(oldBean.getStatus());
        cbean.setAssignPer1(oldBean.getAssignPer1());
        cbean.setAssignPer2(oldBean.getAssignPer2());
        cbean.setAssignPer3(oldBean.getAssignPer3());
        cbean.setAssignPer4(oldBean.getAssignPer4());

        // 加入到正表里面
        customerDAO.updateEntityBean(cbean);

        hisbean.setCustomerId(cid);

        // 记录到his
        customerHisDAO.saveEntityBean(hisbean);
    }

    /**
     * handleUpdateAssignPer
     * 
     * @param user
     * @param cid
     * @param bean
     * @throws MYException
     */
    private void handleUpdateAssignPer(User user, String cid, CustomerApplyBean bean)
        throws MYException
    {
        CustomerBean cbean = customerDAO.find(cid);

        if (cbean == null)
        {
            throw new MYException("修改的客户不存在");
        }

        cbean.setLoginTime(TimeTools.now());

        // 从apply里面删除
        customerApplyDAO.deleteEntityBean(cid);

        cbean.setAssignPer1(bean.getAssignPer1());
        cbean.setAssignPer2(bean.getAssignPer2());
        cbean.setAssignPer3(bean.getAssignPer3());
        cbean.setAssignPer4(bean.getAssignPer4());

        // 加入到正表里面
        customerDAO.updateEntityBean(cbean);
    }

    /**
     * 处理增加从apply到正式表
     * 
     * @param user
     * @param cid
     * @param bean
     * @throws MYException
     */
    private void handleAdd(User user, String cid, CustomerApplyBean bean)
        throws MYException
    {
        String createrId = bean.getCreaterId();

        CustomerBean cbean = new CustomerBean();

        CustomerHisBean hisbean = new CustomerHisBean();

        bean.setLoginTime(TimeTools.now());

        BeanUtil.copyProperties(cbean, bean);

        BeanUtil.copyProperties(hisbean, bean);

        // 从apply里面删除
        customerApplyDAO.deleteEntityBean(cid);

        checkRealAdd(cbean);

        cbean.setStatus(CustomerConstant.REAL_STATUS_IDLE);

        // 如果客户所在的分公司和用户所在分公司相同,则默认给这个申请人
        if (stafferVSCustomerDAO.countByUnique(bean.getId()) == 0
            && user.getLocationId().equals(bean.getLocationId()))
        {
            StafferVSCustomerBean vs = new StafferVSCustomerBean();

            vs.setStafferId(createrId);

            vs.setCustomerId(bean.getId());

            addStafferVSCustomer(vs);

            cbean.setStatus(CustomerConstant.REAL_STATUS_USED);
        }

        // 加入到正表里面
        customerDAO.saveEntityBean(cbean);

        hisbean.setCustomerId(cid);

        hisbean.setUpdaterId(user.getStafferId());

        hisbean.setCheckStatus(CustomerConstant.HIS_CHECK_YES);

        // 记录到his
        customerHisDAO.saveEntityBean(hisbean);
    }

    /**
     * 基本上是导入的
     * 
     * @param user
     * @param bean
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public void addCustomer(User user, CustomerBean bean, String stafferId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        // 生成ID
        bean.setId(commonDAO.getSquenceString());

        bean.setStatus(CustomerConstant.REAL_STATUS_IDLE);

        bean.setLoginTime(TimeTools.now());

        bean.setCreateTime(TimeTools.now());

        // 加密
        CustomerHelper.encryptCustomer(bean);

        CustomerHisBean hisbean = new CustomerHisBean();

        bean.setLoginTime(TimeTools.now());

        BeanUtil.copyProperties(hisbean, bean);

        if (customerDAO.countByUnique(bean.getName().trim()) > 0)
        {
            throw new MYException("客户名称[%s]已经存在", bean.getName());
        }

        checkRealAdd(bean);

        // 加入到正表里面
        customerDAO.saveEntityBean(bean);

        hisbean.setCustomerId(bean.getId());

        hisbean.setUpdaterId(user.getStafferId());

        // 记录到his
        customerHisDAO.saveEntityBean(hisbean);

        // 增加职员和客户的对应关系
        if ( !StringTools.isNullOrNone(stafferId))
        {
            StafferVSCustomerBean vs = new StafferVSCustomerBean();

            vs.setStafferId(stafferId);

            vs.setCustomerId(bean.getId());

            addStafferVSCustomer(vs);
        }
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkPass(CustomerApplyBean bean)
        throws MYException
    {
        if (bean == null)
        {
            throw new MYException("审批的客户不存在");
        }

        if (bean.getStatus() != CustomerConstant.STATUS_APPLY)
        {
            throw new MYException("审批的客户不在申请态");
        }
    }

    private void checkAssignCode(CustomerApplyBean bean, String newCode)
        throws MYException
    {
        if (bean == null)
        {
            throw new MYException("审批的客户不存在");
        }

        // 检查code是否重复
        if ( !bean.getCode().equals(newCode))
        {
            if (customerApplyDAO.countByCode(newCode) > 0 || customerDAO.countByCode(newCode) > 0)
            {
                throw new MYException("分配的客户编码重复:" + newCode);
            }
        }
    }

    /**
     * 增加到正式表的检验
     * 
     * @param cbean
     * @throws MYException
     */
    private void checkRealAdd(CustomerBean cbean)
        throws MYException
    {
        // 修改区域属性
        String cityId = cbean.getCityId();

        if (StringTools.isNullOrNone(cityId))
        {
            throw new MYException("地市属性不存在,请确认数据的准确性");
        }

        LocationVSCityBean lvc = locationVSCityDAO.findByUnique(cityId);

        if (lvc == null)
        {
            CityBean cb = cityDAO.find(cityId);

            if (cb == null)
            {
                throw new MYException("地市属性不正确,请确认数据的准确性");
            }

            // throw new MYException("地市[%s]没有归属到任何分公司,请确认");
            // 默认分配到总部
            cbean.setLocationId(PublicConstant.CENTER_LOCATION);
        }
        else
        {
            cbean.setLocationId(lvc.getLocationId());
        }
    }

    /**
     * delApply
     * 
     * @param user
     * @param stafferId
     * @return
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public boolean delApply(User user, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        CustomerApplyBean bean = customerApplyDAO.find(cid);

        if (bean == null)
        {
            throw new MYException("申请的客户不存在");
        }

        if ( !user.getStafferId().equals(bean.getUpdaterId()))
        {
            throw new MYException("只有申请人才可以删除");
        }

        // 从apply里面删除
        customerApplyDAO.deleteEntityBean(cid);

        return true;
    }

    /**
     * checkHisCustomer
     * 
     * @param user
     * @param cid
     * @return
     * @throws MYException
     */

    @Transactional(rollbackFor = {MYException.class})
    public boolean checkHisCustomer(User user, String cid)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, cid);

        customerHisDAO.updateCheckStatus(cid, CustomerConstant.HIS_CHECK_YES);

        return true;
    }

    public boolean hasCustomerAuth(String stafferId, String customerId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(stafferId, customerId);

        return stafferVSCustomerDAO.countByStafferIdAndCustomerId(stafferId, customerId) > 0;
    }

    @Transactional(rollbackFor = MYException.class)
    public void synchronizationAllCustomerLocation()
    {
        try
        {
            List<LocationVSCityBean> list = locationVSCityDAO.listEntityBeans();

            customerDAO.initCustomerLocation();

            for (LocationVSCityBean locationVSCityBean : list)
            {
                customerDAO.updateCustomerLocationByCity(locationVSCityBean.getCityId(),
                    locationVSCityBean.getLocationId());
            }
        }
        catch (Exception e)
        {
            _logger.error(e, e);
        }
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean updateCustomerLever(User user, String id, int lever)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        // 最小是1
        if (lever < 1)
        {
            lever = 1;
        }

        customerDAO.updateCustomerLever(id, lever);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.customer.manager.CustomerManager#applyUpdateCustomeAssignPer(com.center.china.osgi.publics.User,
     *      com.china.center.oa.customer.bean.CustomerApplyBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean applyUpdateCustomeAssignPer(User user, CustomerApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getId());

        // 删除存在的(直接覆盖)
        customerApplyDAO.deleteEntityBean(bean.getId());

        bean.setUpdaterId(user.getStafferId());

        CustomerHelper.encryptCustomer(bean);

        bean.setStatus(CustomerConstant.STATUS_APPLY);

        bean.setOpr(CustomerConstant.OPR_UPATE_ASSIGNPER);

        bean.setLocationId(user.getLocationId());

        bean.setLoginTime(TimeTools.now());

        customerApplyDAO.saveEntityBean(bean);

        return true;
    }

    /**
     * @param bean
     * @throws MYException
     */
    private void checkAddBean(CustomerApplyBean bean)
        throws MYException
    {
        if (customerApplyDAO.countByUnique(bean.getName().trim()) > 0)
        {
            throw new MYException("客户[%s]已经存在申请", bean.getName());
        }

        if (customerDAO.countByUnique(bean.getName().trim()) > 0)
        {
            throw new MYException("客户名称[%s]已经存在", bean.getName());
        }

        // 验证申请中是否重复
        if (customerApplyDAO.countByCode(bean.getCode().trim()) > 0)
        {
            throw new MYException("客户编码[%s]已经存在申请", bean.getName());
        }

        // 验证已经ok的客户中是否code重复
        if (customerDAO.countByCode(bean.getCode().trim()) > 0)
        {
            throw new MYException("客户编码[%s]已经存在", bean.getName());
        }
    }

    /**
     * 检查update的是否符合条件
     * 
     * @param bean
     * @throws MYException
     */
    private CustomerBean checkUpdateBean(CustomerApplyBean bean)
        throws MYException
    {
        CustomerBean old = customerDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("客户不存在,请重新操作");
        }

        if ( !old.getName().trim().equals(bean.getName().trim()))
        {
            if (customerApplyDAO.countByUnique(bean.getName().trim()) > 0)
            {
                throw new MYException("客户名称[%s]已经存在", bean.getName());
            }

            if (customerDAO.countByUnique(bean.getName().trim()) > 0)
            {
                throw new MYException("客户名称[%s]已经存在", bean.getName());
            }
        }

        if ( !old.getCode().trim().equals(bean.getCode().trim()))
        {
            // 验证申请中是否重复
            if (customerApplyDAO.countByCode(bean.getCode().trim()) > 0)
            {
                throw new MYException("客户编码[%s]已经存在", bean.getCode());
            }

            // 验证已经ok的客户中是否code重复
            if (customerDAO.countByCode(bean.getCode().trim()) > 0)
            {
                throw new MYException("客户编码[%s]已经存在", bean.getCode());
            }
        }

        CustomerApplyBean cbean = customerApplyDAO.find(bean.getId());

        if (cbean != null)
        {
            if (cbean.getOpr() == CustomerConstant.OPR_UPATE_CREDIT
                || cbean.getOpr() == CustomerConstant.OPR_UPATE_ASSIGNPER)
            {
                throw new MYException("客户[%s]存在信用或者利润分配修改,请先结束此审批", bean.getName());
            }
        }
        return old;
    }

    /**
     * @return the commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @param commonDAO
     *            the commonDAO to set
     */
    public void setCommonDAO(CommonDAO commonDAO)
    {
        this.commonDAO = commonDAO;
    }

    /**
     * @return the customerApplyDAO
     */
    public CustomerApplyDAO getCustomerApplyDAO()
    {
        return customerApplyDAO;
    }

    /**
     * @param customerApplyDAO
     *            the customerApplyDAO to set
     */
    public void setCustomerApplyDAO(CustomerApplyDAO customerApplyDAO)
    {
        this.customerApplyDAO = customerApplyDAO;
    }

    /**
     * @return the customerDAO
     */
    public CustomerDAO getCustomerDAO()
    {
        return customerDAO;
    }

    /**
     * @param customerDAO
     *            the customerDAO to set
     */
    public void setCustomerDAO(CustomerDAO customerDAO)
    {
        this.customerDAO = customerDAO;
    }

    /**
     * @return the customerHisDAO
     */
    public CustomerHisDAO getCustomerHisDAO()
    {
        return customerHisDAO;
    }

    /**
     * @param customerHisDAO
     *            the customerHisDAO to set
     */
    public void setCustomerHisDAO(CustomerHisDAO customerHisDAO)
    {
        this.customerHisDAO = customerHisDAO;
    }

    /**
     * @return the locationVSCityDAO
     */
    public LocationVSCityDAO getLocationVSCityDAO()
    {
        return locationVSCityDAO;
    }

    /**
     * @param locationVSCityDAO
     *            the locationVSCityDAO to set
     */
    public void setLocationVSCityDAO(LocationVSCityDAO locationVSCityDAO)
    {
        this.locationVSCityDAO = locationVSCityDAO;
    }

    /**
     * @return the cityDAO
     */
    public CityDAO getCityDAO()
    {
        return cityDAO;
    }

    /**
     * @param cityDAO
     *            the cityDAO to set
     */
    public void setCityDAO(CityDAO cityDAO)
    {
        this.cityDAO = cityDAO;
    }

    /**
     * @return the assignApplyDAO
     */
    public AssignApplyDAO getAssignApplyDAO()
    {
        return assignApplyDAO;
    }

    /**
     * @param assignApplyDAO
     *            the assignApplyDAO to set
     */
    public void setAssignApplyDAO(AssignApplyDAO assignApplyDAO)
    {
        this.assignApplyDAO = assignApplyDAO;
    }

    /**
     * @return the stafferVSCustomerDAO
     */
    public StafferVSCustomerDAO getStafferVSCustomerDAO()
    {
        return stafferVSCustomerDAO;
    }

    /**
     * @param stafferVSCustomerDAO
     *            the stafferVSCustomerDAO to set
     */
    public void setStafferVSCustomerDAO(StafferVSCustomerDAO stafferVSCustomerDAO)
    {
        this.stafferVSCustomerDAO = stafferVSCustomerDAO;
    }

    /**
     * @return the stafferDAO
     */
    public StafferDAO getStafferDAO()
    {
        return stafferDAO;
    }

    /**
     * @param stafferDAO
     *            the stafferDAO to set
     */
    public void setStafferDAO(StafferDAO stafferDAO)
    {
        this.stafferDAO = stafferDAO;
    }

    /**
     * @return the changeLogDAO
     */
    public ChangeLogDAO getChangeLogDAO()
    {
        return changeLogDAO;
    }

    /**
     * @param changeLogDAO
     *            the changeLogDAO to set
     */
    public void setChangeLogDAO(ChangeLogDAO changeLogDAO)
    {
        this.changeLogDAO = changeLogDAO;
    }

    /**
     * @return the notifyManager
     */
    public NotifyManager getNotifyManager()
    {
        return notifyManager;
    }

    /**
     * @param notifyManager
     *            the notifyManager to set
     */
    public void setNotifyManager(NotifyManager notifyManager)
    {
        this.notifyManager = notifyManager;
    }

}
