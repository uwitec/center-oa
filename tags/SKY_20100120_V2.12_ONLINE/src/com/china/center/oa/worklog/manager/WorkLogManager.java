/**
 * File Name: WorkLogManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-2-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.worklog.manager;


import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.constant.CustomerConstant;
import com.china.center.oa.constant.SysConfigConstant;
import com.china.center.oa.constant.WorkLogConstant;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.worklog.bean.VisitBean;
import com.china.center.oa.worklog.bean.WorkLogBean;
import com.china.center.oa.worklog.dao.VisitDAO;
import com.china.center.oa.worklog.dao.WorkLogDAO;
import com.china.center.oa.worklog.wrap.StatWorkLogWrap;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * WorkLogManager
 * 
 * @author zhuzhu
 * @version 2009-2-15
 * @see WorkLogManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "workLogManager")
public class WorkLogManager
{
    private WorkLogDAO workLogDAO = null;

    private VisitDAO visitDAO = null;

    private CustomerDAO customerDAO = null;

    private CommonDAO2 commonDAO2 = null;

    private ParameterDAO parameterDAO = null;

    public WorkLogManager()
    {}

    /**
     * 增加工作日志(已经提交)
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean addBean(User user, WorkLogBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        checkAdd(bean);

        if (bean.getStatus() == WorkLogConstant.WORKLOG_STATUS_SUBMIT)
        {
            process(bean);
        }
        else
        {
            bean.setResult(WorkLogConstant.WORKLOG_RESULT_COMMON);
        }

        bean.setId(commonDAO2.getSquenceString());

        bean.setStafferId(user.getStafferId());

        bean.setLogTime(TimeTools.now());

        workLogDAO.saveEntityBean(bean);

        // 增加默认的考核项
        for (VisitBean item : bean.getVisits())
        {
            item.setParentId(bean.getId());

            visitDAO.saveEntityBean(item);

            processOldCustomer(item, bean.getStafferId());
        }

        return true;
    }

    /**
     * NOTE zhuzhu 处理日志拜访老客户的逻辑
     * 
     * @param item
     */
    private void processOldCustomer(VisitBean item, String stafferId)
    {
        // 如果是拜访的终端客户，超过3次就是变成老客户
        if (item.getWorkType() == WorkLogConstant.WORKTYPE_VISIT)
        {
            CustomerBean cbean = customerDAO.find(item.getTargerId());

            // 只有是终端新客户的时候才处理
            if (cbean != null && cbean.getNewtype() == CustomerConstant.NEWTYPE_NEW
                && cbean.getSelltype() == CustomerConstant.SELLTYPE_TER)
            {
                int count = visitDAO.countByCIdAndType(stafferId, item.getTargerId());

                // 变成老客户
                if (count >= parameterDAO.getInt(SysConfigConstant.MAX_VISITS))
                {
                    customerDAO.updateCustomerNewTypeToOld(item.getTargerId());
                }
            }
        }
    }

    /**
     * 提交
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean submitBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        WorkLogBean bean = checkSubmit(user, id);

        bean.setLogTime(TimeTools.now());

        process(bean);

        bean.setStatus(WorkLogConstant.WORKLOG_STATUS_SUBMIT);

        workLogDAO.updateEntityBean(bean);

        return true;
    }

    /**
     * 删除日志
     * 
     * @param user
     * @param id
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean delBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        checkDel(user, id);

        workLogDAO.deleteEntityBean(id);

        // 删除子项
        visitDAO.deleteEntityBeansByFK(id);

        return true;
    }

    /**
     * 修改工作日志
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateBean(User user, WorkLogBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        WorkLogBean old = checkUpdate(user, bean);

        bean.setWorkDate(old.getWorkDate());

        if (bean.getStatus() == WorkLogConstant.WORKLOG_STATUS_SUBMIT)
        {
            process(bean);
        }
        else
        {
            bean.setResult(WorkLogConstant.WORKLOG_RESULT_COMMON);
        }

        bean.setStafferId(user.getStafferId());

        bean.setLogTime(TimeTools.now());

        workLogDAO.updateEntityBean(bean);

        // 删除原有的,再增加
        visitDAO.deleteEntityBeansByFK(bean.getId());

        // 增加默认的考核项
        for (VisitBean item : bean.getVisits())
        {
            item.setParentId(bean.getId());

            visitDAO.saveEntityBean(item);
        }

        return true;
    }

    /**
     * queryStatWorkLogWrap
     * 
     * @param beginDate
     * @param endDate
     * @param locationId
     * @param stafferId
     * @return
     */
    public List<StatWorkLogWrap> queryStatWorkLogWrap(String beginDate, String endDate,
                                                      String locationId, String stafferId)
    {
        List<StatWorkLogWrap> statWorkLogList = workLogDAO.queryStatWorkLogWrap(beginDate,
            endDate, locationId, stafferId);

        Collections.sort(statWorkLogList, new Comparator<StatWorkLogWrap>()
        {
            public int compare(StatWorkLogWrap o1, StatWorkLogWrap o2)
            {
                return o2.getTotal() - o1.getTotal();
            }
        });

        String end = endDate;

        if (StringTools.isNullOrNone(endDate))
        {
            end = TimeTools.now_short();
        }

        for (StatWorkLogWrap statWorkLogWrap : statWorkLogList)
        {
            int lazy = workLogDAO.countSubmitWorkLogByStafferAndPeriodDate(
                statWorkLogWrap.getStafferId(), beginDate, end,
                WorkLogConstant.WORKLOG_RESULT_LAZY);

            statWorkLogWrap.setExceptionNum(lazy);

            double rd = (lazy / (statWorkLogWrap.getTotal() + 0.0d)) * 100.0d;

            statWorkLogWrap.setExRatio(MathTools.formatNum(rd) + "%");
        }

        return statWorkLogList;
    }

    private WorkLogBean checkSubmit(User user, String id)
        throws MYException
    {
        WorkLogBean old = workLogDAO.find(id);

        if (old == null)
        {
            throw new MYException("请确认工作日志是否存在");
        }

        if (old.getStatus() == WorkLogConstant.WORKLOG_STATUS_SUBMIT)
        {
            throw new MYException("工作日志已经提交");
        }

        if ( !old.getStafferId().equals(user.getStafferId()))
        {
            throw new MYException("只能操作自己的工作日志");
        }

        return old;
    }

    private WorkLogBean checkDel(User user, String id)
        throws MYException
    {
        WorkLogBean old = workLogDAO.find(id);

        if (old == null)
        {
            throw new MYException("请确认工作日志是否存在");
        }

        if (old.getStatus() == WorkLogConstant.WORKLOG_STATUS_SUBMIT)
        {
            throw new MYException("工作日志已经提交,不能删除");
        }

        if ( !old.getStafferId().equals(user.getStafferId()))
        {
            throw new MYException("只能操作自己的工作日志");
        }

        return old;
    }

    /**
     * 增加的业务逻辑检查
     * 
     * @param bean
     * @throws MYException
     */
    private void checkAdd(WorkLogBean bean)
        throws MYException
    {
        if (ListTools.isEmptyOrNull(bean.getVisits()))
        {
            throw new MYException("工作日志里面没有拜访记录");
        }

        if (workLogDAO.countByWorkDate(bean.getStafferId(), bean.getWorkDate()) > 0)
        {
            throw new MYException("%s的工作日志已经存在", bean.getWorkDate());
        }

        if (StringTools.compare(TimeTools.now_short(), bean.getWorkDate()) < 0)
        {
            throw new MYException("工作日志时间不能大于%s", TimeTools.now_short());
        }
    }

    private WorkLogBean checkUpdate(User user, WorkLogBean bean)
        throws MYException
    {
        WorkLogBean old = workLogDAO.find(bean.getId());

        if (old == null)
        {
            throw new MYException("工作日志不存在");
        }

        if (old.getStatus() == WorkLogConstant.WORKLOG_STATUS_SUBMIT)
        {
            throw new MYException("工作日志已经提交");
        }

        if ( !old.getStafferId().equals(user.getStafferId()))
        {
            throw new MYException("只能操作自己的工作日志");
        }

        if (ListTools.isEmptyOrNull(bean.getVisits()))
        {
            throw new MYException("工作日志里面没有拜访记录");
        }

        if (StringTools.compare(TimeTools.now_short(), bean.getWorkDate()) < 0)
        {
            throw new MYException("工作日志时间不能大于%s", TimeTools.now_short());
        }

        if (StringTools.compare(TimeTools.now_short(), bean.getWorkDate()) == 0
            && bean.getStatus() == WorkLogConstant.WORKLOG_STATUS_SUBMIT)
        {
            String begin = parameterDAO.getString(SysConfigConstant.WORKLOG_BEGINTIME);

            String end = parameterDAO.getString(SysConfigConstant.WORKLOG_ENDTIME);

            if (StringTools.compare(TimeTools.now(), TimeTools.now_short() + " " + begin) < 0)
            {
                throw new MYException("工作日志只能在当天的%s到次日的%s之间变动", begin, end);
            }
        }

        return old;
    }

    /**
     * 特殊处理(主要是超时)
     * 
     * @param bean
     * @throws MYException
     */
    private void process(WorkLogBean bean)
        throws MYException
    {
        String begin = parameterDAO.getString(SysConfigConstant.WORKLOG_BEGINTIME);

        String end = parameterDAO.getString(SysConfigConstant.WORKLOG_ENDTIME);

        String max_day = "";

        // 周六 周日不算的
        String workDay = bean.getWorkDate();

        Calendar cal = Calendar.getInstance();

        cal.setTime(TimeTools.getDateByFormat(workDay, TimeTools.SHORT_FORMAT));

        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        // 周五的日志,可以周一提交
        if (dayOfWeek == 6)
        {
            cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 3);

            max_day = TimeTools.getString(cal.getTimeInMillis(), TimeTools.SHORT_FORMAT);
        }
        else
        {
            cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 1);

            max_day = TimeTools.getString(cal.getTimeInMillis(), TimeTools.SHORT_FORMAT);
        }

        String beginDate = workDay + " " + begin;

        String endDate = max_day + " " + end;

        if (StringTools.compare(TimeTools.now(), beginDate) >= 0
            && StringTools.compare(TimeTools.now(), endDate) <= 0)
        {
            bean.setResult(WorkLogConstant.WORKLOG_RESULT_COMMON);
        }
        else if (StringTools.compare(TimeTools.now(), beginDate) < 0)
        {
            throw new MYException("工作日志只能在当天的%s到次日的%s之间提交", begin, end);
        }
        else
        {
            bean.setResult(WorkLogConstant.WORKLOG_RESULT_LAZY);
        }

    }

    /**
     * @return the workLogDAO
     */
    public WorkLogDAO getWorkLogDAO()
    {
        return workLogDAO;
    }

    /**
     * @param workLogDAO
     *            the workLogDAO to set
     */
    public void setWorkLogDAO(WorkLogDAO workLogDAO)
    {
        this.workLogDAO = workLogDAO;
    }

    /**
     * @return the visitDAO
     */
    public VisitDAO getVisitDAO()
    {
        return visitDAO;
    }

    /**
     * @param visitDAO
     *            the visitDAO to set
     */
    public void setVisitDAO(VisitDAO visitDAO)
    {
        this.visitDAO = visitDAO;
    }

    /**
     * @return the commonDAO2
     */
    public CommonDAO2 getCommonDAO2()
    {
        return commonDAO2;
    }

    /**
     * @param commonDAO2
     *            the commonDAO2 to set
     */
    public void setCommonDAO2(CommonDAO2 commonDAO2)
    {
        this.commonDAO2 = commonDAO2;
    }

    /**
     * @return the parameterDAO
     */
    public ParameterDAO getParameterDAO()
    {
        return parameterDAO;
    }

    /**
     * @param parameterDAO
     *            the parameterDAO to set
     */
    public void setParameterDAO(ParameterDAO parameterDAO)
    {
        this.parameterDAO = parameterDAO;
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
}
