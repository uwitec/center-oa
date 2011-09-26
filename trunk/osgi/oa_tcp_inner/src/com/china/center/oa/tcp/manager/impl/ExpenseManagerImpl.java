/**
 * File Name: TravelApplyManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-7-17<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.tcp.manager.impl;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.iaop.annotation.IntegrationAOP;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.config.ConfigLoader;
import com.center.china.osgi.publics.AbstractListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.common.taglib.DefinedCommon;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.budget.bean.BudgetItemBean;
import com.china.center.oa.budget.bean.BudgetLogBean;
import com.china.center.oa.budget.constant.BudgetConstant;
import com.china.center.oa.budget.dao.BudgetItemDAO;
import com.china.center.oa.budget.manager.BudgetManager;
import com.china.center.oa.finance.bean.OutBillBean;
import com.china.center.oa.finance.constant.FinanceConstant;
import com.china.center.oa.finance.manager.BillManager;
import com.china.center.oa.group.dao.GroupVSStafferDAO;
import com.china.center.oa.group.vs.GroupVSStafferBean;
import com.china.center.oa.mail.bean.MailBean;
import com.china.center.oa.mail.manager.MailMangaer;
import com.china.center.oa.publics.bean.AttachmentBean;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.bean.PrincipalshipBean;
import com.china.center.oa.publics.constant.IDPrefixConstant;
import com.china.center.oa.publics.constant.PublicConstant;
import com.china.center.oa.publics.constant.StafferConstant;
import com.china.center.oa.publics.dao.AttachmentDAO;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.helper.UserHelper;
import com.china.center.oa.publics.manager.NotifyManager;
import com.china.center.oa.publics.manager.OrgManager;
import com.china.center.oa.tcp.bean.ExpenseApplyBean;
import com.china.center.oa.tcp.bean.TcpApplyBean;
import com.china.center.oa.tcp.bean.TcpApproveBean;
import com.china.center.oa.tcp.bean.TcpFlowBean;
import com.china.center.oa.tcp.bean.TcpHandleHisBean;
import com.china.center.oa.tcp.bean.TcpShareBean;
import com.china.center.oa.tcp.bean.TravelApplyBean;
import com.china.center.oa.tcp.bean.TravelApplyItemBean;
import com.china.center.oa.tcp.bean.TravelApplyPayBean;
import com.china.center.oa.tcp.constanst.TcpConstanst;
import com.china.center.oa.tcp.dao.ExpenseApplyDAO;
import com.china.center.oa.tcp.dao.TcpApplyDAO;
import com.china.center.oa.tcp.dao.TcpApproveDAO;
import com.china.center.oa.tcp.dao.TcpFlowDAO;
import com.china.center.oa.tcp.dao.TcpHandleHisDAO;
import com.china.center.oa.tcp.dao.TcpPrepaymentDAO;
import com.china.center.oa.tcp.dao.TcpShareDAO;
import com.china.center.oa.tcp.dao.TravelApplyDAO;
import com.china.center.oa.tcp.dao.TravelApplyItemDAO;
import com.china.center.oa.tcp.dao.TravelApplyPayDAO;
import com.china.center.oa.tcp.helper.TCPHelper;
import com.china.center.oa.tcp.listener.TcpPayListener;
import com.china.center.oa.tcp.manager.ExpenseManager;
import com.china.center.oa.tcp.vo.ExpenseApplyVO;
import com.china.center.oa.tcp.vo.TcpApproveVO;
import com.china.center.oa.tcp.vo.TcpShareVO;
import com.china.center.oa.tcp.vo.TravelApplyItemVO;
import com.china.center.oa.tcp.wrap.TcpParamWrap;
import com.china.center.tools.FileTools;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.TimeTools;


/**
 * TravelApplyManagerImpl
 * 
 * @author ZHUZHU
 * @version 2011-7-17
 * @see ExpenseManagerImpl
 * @since 3.0
 */
@IntegrationAOP
public class ExpenseManagerImpl extends AbstractListenerManager<TcpPayListener> implements ExpenseManager
{
    private final Log operationLog = LogFactory.getLog("opr");

    private TcpApplyDAO tcpApplyDAO = null;

    private TcpFlowDAO tcpFlowDAO = null;

    private GroupVSStafferDAO groupVSStafferDAO = null;

    private TcpPrepaymentDAO tcpPrepaymentDAO = null;

    private TcpShareDAO tcpShareDAO = null;

    private TravelApplyDAO travelApplyDAO = null;

    private TravelApplyItemDAO travelApplyItemDAO = null;

    private TravelApplyPayDAO travelApplyPayDAO = null;

    private CommonDAO commonDAO = null;

    private ExpenseApplyDAO expenseApplyDAO = null;

    private TcpHandleHisDAO tcpHandleHisDAO = null;

    private MailMangaer mailMangaer = null;

    private NotifyManager notifyManager = null;

    private BudgetManager budgetManager = null;

    private OrgManager orgManager = null;

    private BillManager billManager = null;

    private BudgetItemDAO budgetItemDAO = null;

    private AttachmentDAO attachmentDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private TcpApproveDAO tcpApproveDAO = null;

    /**
     * default constructor
     */
    public ExpenseManagerImpl()
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.tcp.manager.TravelApplyManager#addExpenseApplyBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.tcp.bean.ExpenseApplyBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addExpenseBean(User user, ExpenseApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        bean.setId(commonDAO.getSquenceString20(IDPrefixConstant.ID_TCP_PREFIX));

        bean.setStafferId(user.getStafferId());

        bean.setBorrowStafferId(user.getStafferId());

        bean.setStatus(TcpConstanst.TCP_STATUS_INIT);

        // 获取flowKey
        TCPHelper.setFlowKey(bean);

        // TravelApplyItemBean
        List<TravelApplyItemBean> itemList = bean.getItemList();

        for (TravelApplyItemBean travelApplyItemBean : itemList)
        {
            travelApplyItemBean.setId(commonDAO.getSquenceString20());
            travelApplyItemBean.setParentId(bean.getId());
        }

        travelApplyItemDAO.saveAllEntityBeans(itemList);

        if (bean.getPayType() == TcpConstanst.PAYTYPE_PAY_YES)
        {
            long temp = 0L;

            List<TravelApplyPayBean> payList = bean.getPayList();

            for (TravelApplyPayBean travelApplyPayBean : payList)
            {
                travelApplyPayBean.setId(commonDAO.getSquenceString20());

                travelApplyPayBean.setParentId(bean.getId());

                temp += travelApplyPayBean.getMoneys();
            }

            // 这里的公司支付金额
            bean.setBorrowTotal(temp);

            // 员工还款金额
            bean.setLastMoney(0);

            travelApplyPayDAO.saveAllEntityBeans(payList);
        }
        else if (bean.getPayType() == TcpConstanst.PAYTYPE_PAY_OK)
        {
            bean.setLastMoney(0);

            bean.setBorrowTotal(0);
        }

        checkApply(user, bean);

        // 从父申请里面拷贝分担比例
        List<TcpShareBean> shareList = tcpShareDAO.queryEntityBeansByFK(bean.getRefId());

        for (TcpShareBean tcpShareBean : shareList)
        {
            tcpShareBean.setId(commonDAO.getSquenceString20());
            tcpShareBean.setRefId(bean.getId());
        }

        tcpShareDAO.saveAllEntityBeans(shareList);

        List<AttachmentBean> attachmentList = bean.getAttachmentList();

        for (AttachmentBean attachmentBean : attachmentList)
        {
            attachmentBean.setId(commonDAO.getSquenceString20());
            attachmentBean.setRefId(bean.getId());
        }

        attachmentDAO.saveAllEntityBeans(attachmentList);

        expenseApplyDAO.saveEntityBean(bean);

        saveApply(user, bean);

        saveFlowLog(user, TcpConstanst.TCP_STATUS_INIT, bean, "自动提交保存", PublicConstant.OPRMODE_SAVE);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean submitExpenseBean(User user, String id, String processId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        ExpenseApplyVO bean = findVO(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !bean.getStafferId().equals(user.getStafferId()))
        {
            throw new MYException("只能操作自己的申请");
        }

        // 预算占用
        checkBudget(user, bean, 0);

        // 获得当前的处理环节
        TcpFlowBean token = tcpFlowDAO.findByUnique(bean.getFlowKey(), bean.getStatus());

        // 进入审批状态
        int newStatus = saveApprove(user, processId, bean, token.getNextStatus(), 0);

        int oldStatus = bean.getStatus();

        bean.setStatus(newStatus);

        expenseApplyDAO.updateStatus(bean.getId(), newStatus);

        // 记录操作日志
        saveFlowLog(user, oldStatus, bean, "提交申请", PublicConstant.OPRMODE_SUBMIT);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean passExpenseBean(User user, TcpParamWrap param)
        throws MYException
    {
        String id = param.getId();
        String processId = param.getProcessId();
        String reason = param.getReason();

        JudgeTools.judgeParameterIsNull(user, id);

        ExpenseApplyVO bean = findVO(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 权限
        checkAuth(user, id);

        int oldStatus = bean.getStatus();

        // 分支处理
        logicProcess(user, param, bean, oldStatus);

        // 获得当前的处理环节
        TcpFlowBean token = tcpFlowDAO.findByUnique(bean.getFlowKey(), bean.getStatus());

        // 群组模式
        if (token.getNextPlugin().startsWith("group"))
        {
            int newStatus = saveApprove(user, processId, bean, token.getNextStatus(), 0);

            if (newStatus != oldStatus)
            {
                bean.setStatus(newStatus);

                expenseApplyDAO.updateStatus(bean.getId(), newStatus);
            }

            // 记录操作日志
            saveFlowLog(user, oldStatus, bean, reason, PublicConstant.OPRMODE_PASS);
        }
        // 共享池模式
        if (token.getNextPlugin().startsWith("pool"))
        {
            String groupId = token.getNextPlugin().substring(5);

            List<GroupVSStafferBean> vsList = groupVSStafferDAO.queryEntityBeansByFK(groupId);

            if (ListTools.isEmptyOrNull(vsList))
            {
                throw new MYException("当前群组内没有人员,请确认操作");
            }

            List<String> processList = new ArrayList();

            for (GroupVSStafferBean groupVSStafferBean : vsList)
            {
                processList.add(groupVSStafferBean.getStafferId());
            }

            int newStatus = saveApprove(user, processList, bean, token.getNextStatus(), 1);

            if (newStatus != oldStatus)
            {
                bean.setStatus(newStatus);

                expenseApplyDAO.updateStatus(bean.getId(), newStatus);
            }

            // 记录操作日志
            saveFlowLog(user, oldStatus, bean, reason, PublicConstant.OPRMODE_PASS);
        }
        // 插件模式
        else if (token.getNextPlugin().startsWith("plugin"))
        {
            // plugin:travelSingeAll(意思是权签人会签)
            if (token.getNextPlugin().equalsIgnoreCase("plugin:travelSingeAll"))
            {
                List<String> processList = new ArrayList();

                // 先处理一个
                List<TcpShareVO> shareVOList = bean.getShareVOList();

                if (ListTools.isEmptyOrNull(shareVOList))
                {
                    throw new MYException("下环节里面没有人员,请确认操作");
                }

                for (TcpShareVO tcpShareVO : shareVOList)
                {
                    processList.add(tcpShareVO.getApproverId());
                }

                int newStatus = saveApprove(user, processList, bean, token.getNextStatus(), 0);

                bean.setStatus(newStatus);

                expenseApplyDAO.updateStatus(bean.getId(), newStatus);

                // 记录操作日志
                saveFlowLog(user, oldStatus, bean, reason, PublicConstant.OPRMODE_PASS);
            }
        }
        // 结束模式
        else if (token.getNextPlugin().startsWith("end"))
        {
            // 结束了需要清空
            tcpApproveDAO.deleteEntityBeansByFK(bean.getId());

            bean.setStatus(token.getNextStatus());

            expenseApplyDAO.updateStatus(bean.getId(), bean.getStatus());

            // 记录操作日志
            saveFlowLog(user, oldStatus, bean, reason, PublicConstant.OPRMODE_PASS);
        }

        return true;
    }

    /**
     * logicProcess
     * 
     * @param user
     * @param param
     * @param bean
     * @param oldStatus
     * @throws MYException
     */
    private void logicProcess(User user, TcpParamWrap param, ExpenseApplyVO bean, int oldStatus)
        throws MYException
    {
        // 这里需要特殊处理的(稽核修改金额)
        if (oldStatus == TcpConstanst.TCP_STATUS_WAIT_CHECK)
        {
            // 稽核需要重新整理pay和重新预算
            if (bean.getPayType() == TcpConstanst.PAYTYPE_PAY_YES)
            {
                List<TravelApplyPayBean> newPayList = (List<TravelApplyPayBean>)param.getOther();

                long newBrrow = 0L;

                for (TravelApplyPayBean travelApplyPayBean : newPayList)
                {
                    newBrrow += travelApplyPayBean.getCmoneys();
                }

                // 重新计算付款金额(这里可能存在金额不一致的情况)
                bean.setBorrowTotal(newBrrow);

                bean.setPayList(newPayList);

                checkBudget(user, bean, 1);

                // 成功后更新支付列表
                travelApplyPayDAO.updateAllEntityBeans(newPayList);

                // 允许检查实际报销
                expenseApplyDAO.updateBorrowTotal(param.getId(), bean.getBorrowTotal());
            }
        }

        // TODO 这里注意财务选择不同科目下的费用,费用合计是借款金额+支付金额,同时把之前的借款需要平账
        // TODO 借:个人借款(充抵之前的借款) 贷:费用科目
        if (oldStatus == TcpConstanst.TCP_STATUS_WAIT_PAY
            && bean.getPayType() == TcpConstanst.PAYTYPE_PAY_YES)
        {
            // 财务付款
            List<OutBillBean> outBillList = (List<OutBillBean>)param.getOther();

            double total = 0.0d;
            StringBuffer idBuffer = new StringBuffer();
            for (OutBillBean outBill : outBillList)
            {
                // 生成付款单
                createOutBill(user, outBill, bean);

                total += outBill.getMoneys();

                idBuffer.append(outBill.getId()).append(';');
            }

            if (MathTools.doubleToLong2(total) != bean.getBorrowTotal())
            {
                throw new MYException("付款金额[%.2f]不等于借款金额[%.2f]", total, MathTools
                    .longToDouble2(bean.getBorrowTotal()));
            }

            Collection<TcpPayListener> listenerMapValues = this.listenerMapValues();

            for (TcpPayListener tcpPayListener : listenerMapValues)
            {
                // TODO_OSGI 这里是出差申请的借款生成凭证
                tcpPayListener.onPayExpenseApply(user, bean, outBillList, null);
            }

            // 更新预算使用状态
            budgetManager.updateBudgetLogUserTypeByRefIdWithoutTransactional(user, bean.getId(),
                BudgetConstant.BUDGETLOG_USERTYPE_REAL, idBuffer.toString());
        }

        // 采购货比三家
        if (oldStatus == TcpConstanst.TCP_STATUS_WAIT_BUY
            && bean.getType() == TcpConstanst.TCP_APPLYTYPE_STOCK)
        {
            List<TravelApplyItemBean> newItemList = (List<TravelApplyItemBean>)param.getOther2();

            List<TravelApplyItemVO> itemVOList = bean.getItemVOList();

            long total = 0L;

            for (TravelApplyItemVO travelApplyItemVO : itemVOList)
            {
                for (TravelApplyItemBean travelApplyItemBean : newItemList)
                {
                    if (travelApplyItemVO.getId().equals(travelApplyItemBean.getId()))
                    {
                        travelApplyItemVO.setCheckPrices(travelApplyItemBean.getCheckPrices());
                        travelApplyItemVO.setMoneys(travelApplyItemBean.getMoneys());
                        travelApplyItemVO.setPurpose(travelApplyItemBean.getPurpose());
                    }
                }

                // 更新采购项
                travelApplyItemDAO.updateEntityBean(travelApplyItemVO);

                total += travelApplyItemVO.getMoneys();
            }

            expenseApplyDAO.updateTotal(param.getId(), total);

            expenseApplyDAO.updateBorrowTotal(param.getId(), total);

            // 更新借款人员是审核人
            expenseApplyDAO.updateBorrowStafferId(param.getId(), user.getStafferId());

            // 先删除
            travelApplyPayDAO.deleteEntityBeansByFK(bean.getId());

            List<TravelApplyPayBean> payList = (List<TravelApplyPayBean>)param.getOther();

            for (TravelApplyPayBean travelApplyPayBean : payList)
            {
                travelApplyPayBean.setId(commonDAO.getSquenceString20());
                travelApplyPayBean.setParentId(bean.getId());
            }

            // 重新加入
            travelApplyPayDAO.saveAllEntityBeans(payList);

            // 更新预算(重新加入预占)
            checkBudget(user, bean, 1);
        }
    }

    /**
     * createOutBill
     * 
     * @param user
     * @param outBill
     * @param apply
     * @throws MYException
     */
    private void createOutBill(User user, OutBillBean outBill, ExpenseApplyVO apply)
        throws MYException
    {
        // 自动生成付款单
        outBill.setDescription(DefinedCommon.getValue("tcpType", apply.getType()) + "申请借款的付款:"
                               + apply.getId());

        outBill.setLocationId(user.getLocationId());

        outBill.setLogTime(TimeTools.now());

        outBill.setType(FinanceConstant.OUTBILL_TYPE_BORROW);

        outBill.setOwnerId(apply.getBorrowStafferId());

        outBill.setStafferId(user.getStafferId());

        outBill.setProvideId("");

        // 借款的单据
        outBill.setStockId(apply.getId());

        outBill.setStockItemId("");

        billManager.addOutBillBeanWithoutTransaction(user, outBill);
    }

    /**
     * 权限
     * 
     * @param user
     * @param id
     * @throws MYException
     */
    private void checkAuth(User user, String id)
        throws MYException
    {
        List<TcpApproveBean> approveList = tcpApproveDAO.queryEntityBeansByFK(id);

        boolean hasAuth = false;

        for (TcpApproveBean tcpApproveBean : approveList)
        {
            if (tcpApproveBean.getApproverId().equals(user.getStafferId()))
            {
                hasAuth = true;

                break;
            }
        }

        if ( !hasAuth)
        {
            throw new MYException("没有操作权限,请确认操作");
        }
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean rejectExpenseBean(User user, TcpParamWrap param)
        throws MYException
    {
        String id = param.getId();
        String reason = param.getReason();
        String type = param.getType();

        JudgeTools.judgeParameterIsNull(user, id);

        ExpenseApplyVO bean = findVO(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 权限
        checkAuth(user, id);

        // 获得当前的处理环节
        // TcpFlowBean token = tcpFlowDAO.findByUnique(bean.getFlowKey(), bean.getStatus());

        // 获得上一步
        FlowLogBean lastLog = flowLogDAO.findLastLog(bean.getId());

        if (lastLog == null)
        {
            // 驳回到初始
            type = "1";
        }
        else
        {
            int nextStatus = lastLog.getAfterStatus();

            // 驳回上一步到处是也是type=1
            if (TCPHelper.isTravelApplyInit(nextStatus))
            {
                type = "1";
            }
        }

        // 驳回到初始
        if ("1".equals(type))
        {
            // 结束了需要清空
            tcpApproveDAO.deleteEntityBeansByFK(bean.getId());

            // 清空预占的预算
            budgetManager.deleteBudgetLogListWithoutTransactional(user, bean.getId());

            // 解冻前面的(这样恢复预算)
            budgetManager.updateBudgetLogStatusWithoutTransactional(user, bean.getRefId(),
                BudgetConstant.BUDGETLOG_STATUS_OK);

            int oldStatus = bean.getStatus();

            bean.setStatus(TcpConstanst.TCP_STATUS_REJECT);

            expenseApplyDAO.updateStatus(bean.getId(), bean.getStatus());

            // 记录操作日志
            saveFlowLog(user, oldStatus, bean, reason, PublicConstant.OPRMODE_REJECT);
        }
        // 驳回到上一步
        else
        {
            // 获得上一步的人
            String actorId = lastLog.getActorId();

            int nextStatus = lastLog.getAfterStatus();

            int newStatus = saveApprove(user, actorId, bean, nextStatus, 0);

            int oldStatus = bean.getStatus();

            bean.setStatus(newStatus);

            expenseApplyDAO.updateStatus(bean.getId(), newStatus);

            if (TCPHelper.isTravelApplyInit(newStatus))
            {
                // 清空预占的金额
                budgetManager.deleteBudgetLogListWithoutTransactional(user, bean.getId());
            }

            // 记录操作日志
            saveFlowLog(user, oldStatus, bean, reason, PublicConstant.OPRMODE_REJECT_PRE);
        }

        return true;
    }

    /**
     * saveApprove
     * 
     * @param user
     * @param processList
     * @param bean
     * @param nextStatus
     * @param pool
     * @return
     * @throws MYException
     */
    private int saveApprove(User user, List<String> processList, ExpenseApplyVO bean,
                            int nextStatus, int pool)
        throws MYException
    {
        // 获得当前的处理环节
        TcpFlowBean token = tcpFlowDAO.findByUnique(bean.getFlowKey(), bean.getStatus());

        if (token.getSingeAll() == 0)
        {
            // 清除之前的处理人
            tcpApproveDAO.deleteEntityBeansByFK(bean.getId());
        }
        else
        {
            // 仅仅删除自己的
            List<TcpApproveBean> approveList = tcpApproveDAO.queryEntityBeansByFK(bean.getId());

            for (TcpApproveBean tcpApproveBean : approveList)
            {
                if (tcpApproveBean.getApproverId().equals(user.getStafferId()))
                {
                    tcpApproveDAO.deleteEntityBean(tcpApproveBean.getId());
                }
            }
        }

        List<TcpApproveBean> appList = tcpApproveDAO.queryEntityBeansByFK(bean.getId());

        if (appList.size() == 0 || token.getSingeAll() == 0)
        {
            for (String processId : processList)
            {
                // 进入审批状态
                TcpApproveBean approve = new TcpApproveBean();

                approve.setId(commonDAO.getSquenceString20());
                approve.setApplyerId(bean.getStafferId());
                approve.setApplyId(bean.getId());
                approve.setApproverId(processId);
                approve.setFlowKey(bean.getFlowKey());
                approve.setLogTime(TimeTools.now());
                approve.setName(bean.getName());
                approve.setStatus(nextStatus);
                approve.setTotal(bean.getTotal());
                approve.setType(bean.getType());
                approve.setPool(pool);

                tcpApproveDAO.saveEntityBean(approve);
            }

            // 如果是共享的不发送邮件
            if (pool == TcpConstanst.TCP_POOL_COMMON)
            {
                MailBean mail = new MailBean();

                mail.setTitle(bean.getStafferName() + "的"
                              + DefinedCommon.getValue("tcpType", bean.getType()) + "申请["
                              + bean.getName() + "]等待您的处理.");

                mail.setContent(mail.getContent());

                mail.setSenderId(StafferConstant.SUPER_STAFFER);

                mail.setReveiveIds(listToString(processList));

                mail.setReveiveIds2(bean.getStafferId());

                mail.setHref(TcpConstanst.TCP_TRAVELAPPLY_PROCESS_URL + bean.getId());

                // send mail
                mailMangaer.addMailWithoutTransactional(UserHelper.getSystemUser(), mail);
            }
        }
        else
        {
            // 会签
            nextStatus = bean.getStatus();
        }

        return nextStatus;
    }

    /**
     * @param processers
     * @return
     */
    private String listToString(List<String> processers)
    {
        StringBuilder builder = new StringBuilder();

        for (String string : processers)
        {
            builder.append(string).append(';');
        }

        return builder.toString();
    }

    /**
     * 进入审批状态
     * 
     * @param processId
     * @param bean
     * @param pool
     * @throws MYException
     */
    private int saveApprove(User user, String processId, ExpenseApplyVO bean, int nextStatus,
                            int pool)
        throws MYException
    {
        List<String> processList = new ArrayList();

        processList.add(processId);

        return saveApprove(user, processList, bean, nextStatus, pool);
    }

    /**
     * 校验预算(且占用预算)/报销的时候先冻结申请的预算,然后重新规划处当前的预算
     * 
     * @param user
     * @param bean
     * @param type
     *            0:new add 1:稽核需要重新整理pay和重新预算
     * @throws MYException
     */
    private void checkBudget(User user, ExpenseApplyVO bean, int type)
        throws MYException
    {
        TravelApplyBean apply = travelApplyDAO.find(bean.getRefId());

        checkApplyFeedback(bean, apply);

        if (type == 1)
        {
            // 先删除之前的
            budgetManager.deleteBudgetLogListWithoutTransactional(user, bean.getId());
        }
        else
        {
            // 冻结前面的(这样前面的不占用预算)
            budgetManager.updateBudgetLogStatusWithoutTransactional(user, bean.getRefId(),
                BudgetConstant.BUDGETLOG_STATUS_TEMP);
        }

        double borrowRadio = 1.0;

        long max = 0;

        // 增加的时候
        if (type == 0)
        {
            // 如果报销的金额没有实际申请的大,还是需要占用的(防止总预算不够)
            if (bean.getRefMoney() >= bean.getTotal())
            {
                borrowRadio = bean.getRefMoney() / (bean.getTotal() + 0.0d);
            }

            max = Math.max(bean.getRefMoney(), bean.getTotal());
        }
        else
        {
            // bean.getRefMoney() + bean.getBorrowTotal()（实际使用金额）
            borrowRadio = (bean.getRefMoney() + bean.getBorrowTotal()) / (bean.getTotal() + 0.0d);

            max = bean.getRefMoney() + bean.getBorrowTotal();
        }

        List<TravelApplyItemVO> itemVOList = bean.getItemVOList();

        List<TcpShareVO> shareVOList = bean.getShareVOList();

        List<BudgetLogBean> logList = new ArrayList();

        long hasUse = 0L;

        for (Iterator iterator = shareVOList.iterator(); iterator.hasNext();)
        {
            TcpShareVO tcpShareVO = (TcpShareVO)iterator.next();

            for (Iterator ite = itemVOList.iterator(); ite.hasNext();)
            {
                TravelApplyItemVO travelApplyItemVO = (TravelApplyItemVO)ite.next();

                // 预算鉴权()
                BudgetLogBean log = new BudgetLogBean();

                logList.add(log);

                log.setBudgetId(tcpShareVO.getBudgetId());

                BudgetItemBean item = budgetItemDAO.findByBudgetIdAndFeeItemId(tcpShareVO
                    .getBudgetId(), travelApplyItemVO.getFeeItemId());

                if (item == null)
                {

                    throw new MYException("预算[%s]里面缺少预算项[%s],请确认操作", tcpShareVO.getBudgetName(),
                        travelApplyItemVO.getFeeItemName());
                }

                log.setBudgetItemId(item.getId());

                log.setDepartmentId(tcpShareVO.getDepartmentId());

                log.setFeeItemId(travelApplyItemVO.getFeeItemId());

                log.setFromType(BudgetConstant.BUDGETLOG_FROMTYPE_EXPENSE);

                log.setLocationId(user.getLocationId());

                log.setLog(DefinedCommon.getValue("tcpType", bean.getType()) + "申请[" + bean.getId()
                           + "]占用预算");

                log.setLogTime(TimeTools.now());

                log.setRefId(bean.getId());

                log.setRefSubId(travelApplyItemVO.getId());

                // 使用人
                log.setStafferId(bean.getBorrowStafferId());

                // 预占
                log.setUserType(BudgetConstant.BUDGETLOG_USERTYPE_PRE);

                // 这里肯定有误差的
                long useMoney = Math.round( (tcpShareVO.getRatio() / 100.0) * borrowRadio
                                           * travelApplyItemVO.getMoneys());

                log.setMonery(useMoney);

                hasUse += useMoney;
            }
        }

        // 处理不能的情况
        long chae = hasUse - max;

        // 消除误差
        if (chae != 0)
        {
            for (BudgetLogBean budgetLogBean : logList)
            {
                if (budgetLogBean.getMonery() > chae)
                {
                    budgetLogBean.setMonery(budgetLogBean.getMonery() - chae);

                    break;
                }
            }
        }

        // 进入使用日志,如果超出预算会抛出异常的
        budgetManager.addBudgetLogListWithoutTransactional(user, bean.getId(), logList);
    }

    /**
     * checkApplyFeedback
     * 
     * @param bean
     * @param apply
     * @throws MYException
     */
    private void checkApplyFeedback(ExpenseApplyVO bean, TravelApplyBean apply)
        throws MYException
    {
        if (apply == null)
        {
            throw new MYException("申请不存在,请确认操作");
        }

        if (apply.getFeedback() != TcpConstanst.TCP_APPLY_FEEDBACK_NO)
        {
            throw new MYException("申请[%s]已经被报销过,请确认操作", bean.getRefId());
        }
    }

    /**
     * checkApply
     * 
     * @param bean
     * @throws MYException
     */
    private void checkApply(User user, ExpenseApplyBean bean)
        throws MYException
    {
        TravelApplyBean apply = travelApplyDAO.find(bean.getRefId());

        if (apply == null)
        {
            throw new MYException("申请不存在,请确认操作");
        }

        if (apply.getFeedback() != TcpConstanst.TCP_APPLY_FEEDBACK_NO)
        {
            throw new MYException("申请[%s]已经被报销过,请确认操作", bean.getRefId());
        }

        if ( !apply.getStafferId().equals(user.getStafferId()))
        {
            throw new MYException("只能关联自己的申请,请确认操作");
        }

        // 检查当前是否有单据关联
        List<ExpenseApplyBean> refList = expenseApplyDAO.queryEntityBeansByFK(bean.getRefId());

        if (refList.size() > 1)
        {
            throw new MYException("借款申请已经被其他单据使用,请确认操作");
        }

        if (refList.size() == 1 && !refList.get(0).getId().equals(bean.getId()))
        {
            throw new MYException("借款申请已经被其他单据[%s]使用,请确认操作", refList.get(0).getId());
        }

        // 实现单单清的策略
        bean.setRefMoney(apply.getBorrowTotal());

        if (bean.getPayType() == TcpConstanst.PAYTYPE_PAY_YES)
        {
            // 公司付款金额+借款金额 == 报销金额
            if (bean.getRefMoney() + bean.getBorrowTotal() != bean.getTotal())
            {
                throw new MYException("公司付款给员工下报销总金额[%.2f]必须等于借款金额[%.2f]加公司的付款金额[%.2f]", MathTools
                    .longToDoubleStr2(bean.getTotal()), MathTools.longToDoubleStr2(bean
                    .getRefMoney()), MathTools.longToDoubleStr2(bean.getBorrowTotal()));
            }
        }

        if (bean.getPayType() == TcpConstanst.PAYTYPE_PAY_OK)
        {
            bean.setBorrowTotal(0);

            // 借款金额 == 报销金额
            if (bean.getRefMoney() + bean.getBorrowTotal() != bean.getTotal())
            {
                throw new MYException("收支平衡下报销总金额[%.2f]必须等于借款金额[%.2f]", MathTools
                    .longToDoubleStr2(bean.getTotal()), MathTools.longToDoubleStr2(bean
                    .getRefMoney()));
            }
        }

        if (bean.getPayType() == TcpConstanst.PAYTYPE_PAY_NO)
        {
            bean.setBorrowTotal(0);

            // 借款金额 == 报销金额+员工支付金额
            if (bean.getRefMoney() != bean.getTotal() + bean.getLastMoney())
            {
                throw new MYException("员工付款给公司下报销总金额[%.2f]加员工付款金额[%.2f]必须等于借款金额[%.2f]", MathTools
                    .longToDoubleStr2(bean.getTotal()), MathTools.longToDoubleStr2(bean
                    .getLastMoney()), MathTools.longToDoubleStr2(bean.getRefMoney()));
            }
        }
    }

    /**
     * saveApply
     * 
     * @param user
     * @param bean
     */
    public void saveApply(User user, ExpenseApplyBean bean)
    {
        TcpApplyBean apply = new TcpApplyBean();

        apply.setId(bean.getId());
        apply.setName(bean.getId());
        apply.setFlowKey(bean.getFlowKey());
        apply.setApplyId(bean.getId());
        apply.setApplyId(user.getStafferId());
        apply.setType(bean.getType());
        apply.setStatus(TcpConstanst.TCP_STATUS_INIT);
        apply.setTotal(bean.getTotal());
        apply.setLogTime(bean.getLogTime());
        apply.setDescription(bean.getDescription());

        tcpApplyDAO.saveEntityBean(apply);
    }

    /**
     * saveFlowLog/PublicConstant.OPRMODE_PASS
     * 
     * @param user
     * @param preStatus
     * @param apply
     * @param reason
     * @param oprMode
     */
    private void saveFlowLog(User user, int preStatus, ExpenseApplyBean apply, String reason,
                             int oprMode)
    {
        FlowLogBean log = new FlowLogBean();

        log.setFullId(apply.getId());

        log.setActor(user.getStafferName());

        log.setActorId(user.getStafferId());

        log.setOprMode(oprMode);

        log.setDescription(reason);

        log.setLogTime(TimeTools.now());

        log.setPreStatus(preStatus);

        log.setAfterStatus(apply.getStatus());

        flowLogDAO.saveEntityBean(log);

        // 先删除历史处理
        ConditionParse condition = new ConditionParse();
        condition.addWhereStr();
        condition.addCondition("stafferId", "=", user.getStafferId());
        condition.addCondition("refId", "=", apply.getId());
        tcpHandleHisDAO.deleteEntityBeansByCondition(condition);

        // 记录处理历史
        TcpHandleHisBean his = new TcpHandleHisBean();
        his.setId(commonDAO.getSquenceString20());
        his.setLogTime(TimeTools.now());
        his.setRefId(apply.getId());
        his.setStafferId(user.getStafferId());
        his.setApplyId(apply.getStafferId());
        his.setType(apply.getType());
        his.setName(apply.getName());

        tcpHandleHisDAO.saveEntityBean(his);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.tcp.manager.TravelApplyManager#updateExpenseApplyBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.tcp.bean.ExpenseApplyBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean updateExpenseBean(User user, ExpenseApplyBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        ExpenseApplyBean old = expenseApplyDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !bean.getStafferId().equals(user.getStafferId()))
        {
            throw new MYException("只能修改自己的申请");
        }

        bean.setStatus(TcpConstanst.TCP_STATUS_INIT);

        bean.setBorrowStafferId(user.getStafferId());

        // 获取flowKey
        TCPHelper.setFlowKey(bean);

        // 先清理
        travelApplyItemDAO.deleteEntityBeansByFK(bean.getId());
        travelApplyPayDAO.deleteEntityBeansByFK(bean.getId());
        tcpShareDAO.deleteEntityBeansByFK(bean.getId());
        attachmentDAO.deleteEntityBeansByFK(bean.getId());

        // TravelApplyItemBean
        List<TravelApplyItemBean> itemList = bean.getItemList();

        for (TravelApplyItemBean travelApplyItemBean : itemList)
        {
            travelApplyItemBean.setId(commonDAO.getSquenceString20());
            travelApplyItemBean.setParentId(bean.getId());
        }

        travelApplyItemDAO.saveAllEntityBeans(itemList);

        if (bean.getPayType() == TcpConstanst.PAYTYPE_PAY_YES)
        {
            long temp = 0L;

            List<TravelApplyPayBean> payList = bean.getPayList();

            for (TravelApplyPayBean travelApplyPayBean : payList)
            {
                travelApplyPayBean.setId(commonDAO.getSquenceString20());

                travelApplyPayBean.setParentId(bean.getId());

                temp += travelApplyPayBean.getMoneys();
            }

            // 这里的公司支付金额
            bean.setBorrowTotal(temp);

            // 员工还款金额
            bean.setLastMoney(0);

            travelApplyPayDAO.saveAllEntityBeans(payList);
        }
        else if (bean.getPayType() == TcpConstanst.PAYTYPE_PAY_OK)
        {
            bean.setLastMoney(0);

            bean.setBorrowTotal(0);
        }

        checkApply(user, bean);

        // 从父申请里面拷贝分担比例
        List<TcpShareBean> shareList = tcpShareDAO.queryEntityBeansByFK(bean.getRefId());

        for (TcpShareBean tcpShareBean : shareList)
        {
            tcpShareBean.setId(commonDAO.getSquenceString20());
            tcpShareBean.setRefId(bean.getId());
        }

        tcpShareDAO.saveAllEntityBeans(shareList);

        List<AttachmentBean> attachmentList = bean.getAttachmentList();

        for (AttachmentBean attachmentBean : attachmentList)
        {
            attachmentBean.setId(commonDAO.getSquenceString20());
            attachmentBean.setRefId(bean.getId());
        }

        attachmentDAO.saveAllEntityBeans(attachmentList);

        expenseApplyDAO.updateEntityBean(bean);

        saveFlowLog(user, old.getStatus(), bean, "自动修改保存", PublicConstant.OPRMODE_SAVE);

        return true;
    }

    @Transactional(rollbackFor = MYException.class)
    public boolean deleteExpenseBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        ExpenseApplyBean bean = expenseApplyDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !TCPHelper.canTravelApplyDelete(bean))
        {
            throw new MYException("不是初始态和驳回态,不能删除");
        }

        // 删除
        travelApplyItemDAO.deleteEntityBeansByFK(bean.getId());
        travelApplyPayDAO.deleteEntityBeansByFK(bean.getId());
        tcpShareDAO.deleteEntityBeansByFK(bean.getId());
        flowLogDAO.deleteEntityBeansByFK(bean.getId());

        // 删除预算使用,一般都是空
        budgetManager.deleteBudgetLogListWithoutTransactional(user, bean.getId());

        String rootPath = ConfigLoader.getProperty("tcpAttachmentPath");

        List<AttachmentBean> attachmenList = attachmentDAO.queryEntityBeansByFK(id);

        for (AttachmentBean attachmentBean : attachmenList)
        {
            FileTools.deleteFile(rootPath + attachmentBean.getPath());
        }

        attachmentDAO.deleteEntityBeansByFK(bean.getId());

        expenseApplyDAO.deleteEntityBean(id);

        tcpApplyDAO.deleteEntityBean(id);

        operationLog.info(user + " delete ExpenseApplyBean:" + bean);

        return true;
    }

    public ExpenseApplyVO findVO(String id)
    {
        ExpenseApplyVO bean = expenseApplyDAO.findVO(id);

        if (bean == null)
        {
            return bean;
        }

        List<TravelApplyItemVO> itemVOList = travelApplyItemDAO.queryEntityVOsByFK(id);

        bean.setItemVOList(itemVOList);

        for (TravelApplyItemVO travelApplyItemVO : itemVOList)
        {
            travelApplyItemVO.setShowMoneys(TCPHelper
                .formatNum2(travelApplyItemVO.getMoneys() / 100.0d));
        }

        List<AttachmentBean> attachmentList = attachmentDAO.queryEntityVOsByFK(id);

        bean.setAttachmentList(attachmentList);

        List<TravelApplyPayBean> payList = travelApplyPayDAO.queryEntityVOsByFK(id);

        bean.setPayList(payList);

        List<TcpShareVO> shareList = tcpShareDAO.queryEntityVOsByFK(id);

        for (TcpShareVO tcpShareVO : shareList)
        {
            PrincipalshipBean dep = orgManager.findPrincipalshipById(tcpShareVO.getDepartmentId());

            if (dep != null)
            {
                tcpShareVO.setDepartmentName(dep.getFullName());
            }
        }

        bean.setShareVOList(shareList);

        TCPHelper.chageVO(bean);

        // 当前处理人
        List<TcpApproveVO> approveList = tcpApproveDAO.queryEntityVOsByFK(bean.getId());

        for (TcpApproveVO tcpApproveVO : approveList)
        {
            bean.setProcesser(bean.getProcesser() + tcpApproveVO.getApproverName() + ';');
        }

        // 流程描述
        List<TcpFlowBean> flowList = tcpFlowDAO.queryEntityBeansByFK(bean.getFlowKey());

        Collections.sort(flowList, new Comparator<TcpFlowBean>()
        {
            public int compare(TcpFlowBean o1, TcpFlowBean o2)
            {
                return o1.getCurrentStatus() - o2.getCurrentStatus();
            }
        });

        StringBuffer sb = new StringBuffer();

        for (TcpFlowBean tcpFlowBean : flowList)
        {
            if (bean.getStatus() == tcpFlowBean.getCurrentStatus())
            {
                sb.append("<font color=red>").append(
                    DefinedCommon.getValue("tcpStatus", tcpFlowBean.getCurrentStatus())).append(
                    "</font>").append("->");
            }
            else
            {
                sb
                    .append(DefinedCommon.getValue("tcpStatus", tcpFlowBean.getCurrentStatus()))
                    .append("->");
            }
        }

        if (bean.getStatus() == TcpConstanst.TCP_STATUS_END)
        {
            sb.append("<font color=red>").append(
                DefinedCommon.getValue("tcpStatus", TcpConstanst.TCP_STATUS_END)).append("</font>");
        }
        else
        {
            sb.append(DefinedCommon.getValue("tcpStatus", TcpConstanst.TCP_STATUS_END));
        }

        bean.setFlowDescription(sb.toString());

        return bean;
    }

    /**
     * @return the tcpApplyDAO
     */
    public TcpApplyDAO getTcpApplyDAO()
    {
        return tcpApplyDAO;
    }

    /**
     * @param tcpApplyDAO
     *            the tcpApplyDAO to set
     */
    public void setTcpApplyDAO(TcpApplyDAO tcpApplyDAO)
    {
        this.tcpApplyDAO = tcpApplyDAO;
    }

    /**
     * @return the tcpFlowDAO
     */
    public TcpFlowDAO getTcpFlowDAO()
    {
        return tcpFlowDAO;
    }

    /**
     * @param tcpFlowDAO
     *            the tcpFlowDAO to set
     */
    public void setTcpFlowDAO(TcpFlowDAO tcpFlowDAO)
    {
        this.tcpFlowDAO = tcpFlowDAO;
    }

    /**
     * @return the tcpPrepaymentDAO
     */
    public TcpPrepaymentDAO getTcpPrepaymentDAO()
    {
        return tcpPrepaymentDAO;
    }

    /**
     * @param tcpPrepaymentDAO
     *            the tcpPrepaymentDAO to set
     */
    public void setTcpPrepaymentDAO(TcpPrepaymentDAO tcpPrepaymentDAO)
    {
        this.tcpPrepaymentDAO = tcpPrepaymentDAO;
    }

    /**
     * @return the tcpShareDAO
     */
    public TcpShareDAO getTcpShareDAO()
    {
        return tcpShareDAO;
    }

    /**
     * @param tcpShareDAO
     *            the tcpShareDAO to set
     */
    public void setTcpShareDAO(TcpShareDAO tcpShareDAO)
    {
        this.tcpShareDAO = tcpShareDAO;
    }

    /**
     * @return the travelApplyDAO
     */
    public TravelApplyDAO getTravelApplyDAO()
    {
        return travelApplyDAO;
    }

    /**
     * @param travelApplyDAO
     *            the travelApplyDAO to set
     */
    public void setTravelApplyDAO(TravelApplyDAO travelApplyDAO)
    {
        this.travelApplyDAO = travelApplyDAO;
    }

    /**
     * @return the travelApplyItemDAO
     */
    public TravelApplyItemDAO getTravelApplyItemDAO()
    {
        return travelApplyItemDAO;
    }

    /**
     * @param travelApplyItemDAO
     *            the travelApplyItemDAO to set
     */
    public void setTravelApplyItemDAO(TravelApplyItemDAO travelApplyItemDAO)
    {
        this.travelApplyItemDAO = travelApplyItemDAO;
    }

    /**
     * @return the travelApplyPayDAO
     */
    public TravelApplyPayDAO getTravelApplyPayDAO()
    {
        return travelApplyPayDAO;
    }

    /**
     * @param travelApplyPayDAO
     *            the travelApplyPayDAO to set
     */
    public void setTravelApplyPayDAO(TravelApplyPayDAO travelApplyPayDAO)
    {
        this.travelApplyPayDAO = travelApplyPayDAO;
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
     * @return the flowLogDAO
     */
    public FlowLogDAO getFlowLogDAO()
    {
        return flowLogDAO;
    }

    /**
     * @param flowLogDAO
     *            the flowLogDAO to set
     */
    public void setFlowLogDAO(FlowLogDAO flowLogDAO)
    {
        this.flowLogDAO = flowLogDAO;
    }

    /**
     * @return the tcpApproveDAO
     */
    public TcpApproveDAO getTcpApproveDAO()
    {
        return tcpApproveDAO;
    }

    /**
     * @param tcpApproveDAO
     *            the tcpApproveDAO to set
     */
    public void setTcpApproveDAO(TcpApproveDAO tcpApproveDAO)
    {
        this.tcpApproveDAO = tcpApproveDAO;
    }

    /**
     * @return the attachmentDAO
     */
    public AttachmentDAO getAttachmentDAO()
    {
        return attachmentDAO;
    }

    /**
     * @param attachmentDAO
     *            the attachmentDAO to set
     */
    public void setAttachmentDAO(AttachmentDAO attachmentDAO)
    {
        this.attachmentDAO = attachmentDAO;
    }

    /**
     * @return the orgManager
     */
    public OrgManager getOrgManager()
    {
        return orgManager;
    }

    /**
     * @param orgManager
     *            the orgManager to set
     */
    public void setOrgManager(OrgManager orgManager)
    {
        this.orgManager = orgManager;
    }

    /**
     * @return the budgetManager
     */
    public BudgetManager getBudgetManager()
    {
        return budgetManager;
    }

    /**
     * @param budgetManager
     *            the budgetManager to set
     */
    public void setBudgetManager(BudgetManager budgetManager)
    {
        this.budgetManager = budgetManager;
    }

    /**
     * @return the budgetItemDAO
     */
    public BudgetItemDAO getBudgetItemDAO()
    {
        return budgetItemDAO;
    }

    /**
     * @param budgetItemDAO
     *            the budgetItemDAO to set
     */
    public void setBudgetItemDAO(BudgetItemDAO budgetItemDAO)
    {
        this.budgetItemDAO = budgetItemDAO;
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

    /**
     * @return the mailMangaer
     */
    public MailMangaer getMailMangaer()
    {
        return mailMangaer;
    }

    /**
     * @param mailMangaer
     *            the mailMangaer to set
     */
    public void setMailMangaer(MailMangaer mailMangaer)
    {
        this.mailMangaer = mailMangaer;
    }

    /**
     * @return the groupVSStafferDAO
     */
    public GroupVSStafferDAO getGroupVSStafferDAO()
    {
        return groupVSStafferDAO;
    }

    /**
     * @param groupVSStafferDAO
     *            the groupVSStafferDAO to set
     */
    public void setGroupVSStafferDAO(GroupVSStafferDAO groupVSStafferDAO)
    {
        this.groupVSStafferDAO = groupVSStafferDAO;
    }

    /**
     * @return the billManager
     */
    public BillManager getBillManager()
    {
        return billManager;
    }

    /**
     * @param billManager
     *            the billManager to set
     */
    public void setBillManager(BillManager billManager)
    {
        this.billManager = billManager;
    }

    /**
     * @return the tcpHandleHisDAO
     */
    public TcpHandleHisDAO getTcpHandleHisDAO()
    {
        return tcpHandleHisDAO;
    }

    /**
     * @param tcpHandleHisDAO
     *            the tcpHandleHisDAO to set
     */
    public void setTcpHandleHisDAO(TcpHandleHisDAO tcpHandleHisDAO)
    {
        this.tcpHandleHisDAO = tcpHandleHisDAO;
    }

    /**
     * @return the expenseApplyDAO
     */
    public ExpenseApplyDAO getExpenseApplyDAO()
    {
        return expenseApplyDAO;
    }

    /**
     * @param expenseApplyDAO
     *            the expenseApplyDAO to set
     */
    public void setExpenseApplyDAO(ExpenseApplyDAO expenseApplyDAO)
    {
        this.expenseApplyDAO = expenseApplyDAO;
    }

}
