/**
 * File Name: ConsumeManager.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.manager;


import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.china.center.common.ConditionParse;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.PageSeparate;
import com.china.center.tools.JudgeTools;
import com.china.centet.yongyin.bean.ConsumeBean;
import com.china.centet.yongyin.bean.ExchangeBean;
import com.china.centet.yongyin.bean.GradeBean;
import com.china.centet.yongyin.bean.MemberBean;
import com.china.centet.yongyin.dao.ConsumeDAO;
import com.china.centet.yongyin.dao.ExchangeDAO;
import com.china.centet.yongyin.dao.GradeDAO;
import com.china.centet.yongyin.dao.MemberDAO;
import com.china.centet.yongyin.vo.ConsumeBeanVO;
import com.china.centet.yongyin.vo.ExchangeBeanVO;


/**
 * 会员消费的manager
 * 
 * @author zhuzhu
 * @version 2007-12-15
 * @see
 * @since
 */
public class ConsumeManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private ConsumeDAO consumeDAO = null;

    private MemberDAO memberDAO = null;

    private GradeDAO gradeDAO = null;

    private ExchangeDAO exchangeDAO = null;

    private DataSourceTransactionManager transactionManager = null;

    /**
     * default constructor
     */
    public ConsumeManager()
    {}

    /**
     * 增加会员消费
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    public boolean addConsume(final ConsumeBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        // 设置积分
        bean.setPoint((int)Math.round(bean.getCost()));

        // 更新会员积分
        final MemberBean mbean = memberDAO.find(bean.getMemberId());

        if (mbean == null)
        {
            throw new MYException("会员不存在");
        }

        bean.setMemberpoint(bean.getPoint() + mbean.getPoint());

        try
        {
            // 增加管理员操作在数据库事务中完成
            TransactionTemplate tran = new TransactionTemplate(transactionManager);

            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    consumeDAO.saveEntityBean(bean);

                    // 更新会员积分
                    memberDAO.updatePoint(mbean.getId(), bean.getMemberpoint());

                    // 更新会员可用积分
                    memberDAO.updateUserPoint(mbean.getId(), bean.getPoint() + mbean.getUsepoint());

                    // 是否升级
                    List<GradeBean> list = gradeDAO.listEntityBeans();

                    for (GradeBean gradeBean : list)
                    {
                        // 会员积分 大于基本积分
                        if (bean.getMemberpoint() >= gradeBean.getBasepoint())
                        {
                            // 且当前会员级别 小于此级别
                            if (mbean.getGrade() < gradeBean.getId())
                            {
                                memberDAO.updateGrate(mbean.getId(), gradeBean.getId());

                                memberDAO.updateRebate(mbean.getId(), gradeBean.getRebate());
                            }
                        }
                    }

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error("增加会员消费错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("增加会员消费错误：", e);
            throw new MYException(e.getCause().toString());
        }

        return true;
    }

    public List<ConsumeBeanVO> queryConsumesByCondtion(ConditionParse condtion, PageSeparate page)
    {
        return consumeDAO.queryEntityVOsBycondition(condtion, page);
    }

    public List<ExchangeBeanVO> queryExchangeBeanVOsByCondtion(ConditionParse condtion,
                                                               PageSeparate page)
    {
        return exchangeDAO.queryEntityVOsBycondition(condtion, page);
    }

    public int countExchangeBeanVOByCondtion(ConditionParse condtion)
    {
        condtion.addWhereStr();

        return exchangeDAO.countVOBycondition(condtion.toString());
    }

    public ConsumeBeanVO findConsumeVO(String id)
    {
        return consumeDAO.findVO(id);
    }

    public int countByCondtion(ConditionParse condtion)
    {
        condtion.addWhereStr();

        return consumeDAO.countVOBycondition(condtion.toString());
    }

    public boolean addExchange(final ExchangeBean bean)
        throws MYException
    {
        final MemberBean member = memberDAO.find(bean.getMemberId());

        if (member == null)
        {
            throw new MYException("会员不存在");
        }

        if (member.getUsepoint() < bean.getCostpoint())
        {
            throw new MYException("会员积分不足不能兑换");
        }

        try
        {
            // 增加管理员操作在数据库事务中完成
            TransactionTemplate tran = new TransactionTemplate(transactionManager);

            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    bean.setBeforepoint(member.getUsepoint() - bean.getCostpoint());

                    exchangeDAO.saveEntityBean(bean);

                    if (member.getUsepoint() - bean.getCostpoint() < 0)
                    {
                        throw new RuntimeException("会员积分不足不能兑换");
                    }

                    memberDAO.updateUserPoint(bean.getMemberId(), member.getUsepoint()
                                                                  - bean.getCostpoint());

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error("增加会员兑换错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("增加会员兑换错误：", e);
            throw new MYException(e.getCause().toString());
        }

        return true;
    }

    /**
     * @return the transactionManager
     */
    public DataSourceTransactionManager getTransactionManager()
    {
        return transactionManager;
    }

    /**
     * @param transactionManager
     *            the transactionManager to set
     */
    public void setTransactionManager(DataSourceTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    /**
     * @return the consumeDAO
     */
    public ConsumeDAO getConsumeDAO()
    {
        return consumeDAO;
    }

    /**
     * @param consumeDAO
     *            the consumeDAO to set
     */
    public void setConsumeDAO(ConsumeDAO consumeDAO)
    {
        this.consumeDAO = consumeDAO;
    }

    /**
     * @return the memberDAO
     */
    public MemberDAO getMemberDAO()
    {
        return memberDAO;
    }

    /**
     * @param memberDAO
     *            the memberDAO to set
     */
    public void setMemberDAO(MemberDAO memberDAO)
    {
        this.memberDAO = memberDAO;
    }

    /**
     * @return the gradeDAO
     */
    public GradeDAO getGradeDAO()
    {
        return gradeDAO;
    }

    /**
     * @param gradeDAO
     *            the gradeDAO to set
     */
    public void setGradeDAO(GradeDAO gradeDAO)
    {
        this.gradeDAO = gradeDAO;
    }

    /**
     * @return the exchangeDAO
     */
    public ExchangeDAO getExchangeDAO()
    {
        return exchangeDAO;
    }

    /**
     * @param exchangeDAO
     *            the exchangeDAO to set
     */
    public void setExchangeDAO(ExchangeDAO exchangeDAO)
    {
        this.exchangeDAO = exchangeDAO;
    }
}
