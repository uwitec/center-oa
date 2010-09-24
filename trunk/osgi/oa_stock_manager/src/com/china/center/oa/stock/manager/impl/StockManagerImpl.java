/**
 * File Name: StockManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-9-20<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.stock.manager.impl;


import java.util.List;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.oa.note.bean.ShortMessageTaskBean;
import com.china.center.oa.note.constant.ShortMessageConstant;
import com.china.center.oa.note.dao.ShortMessageTaskDAO;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.constant.AuthConstant;
import com.china.center.oa.publics.constant.SysConfigConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.helper.AuthHelper;
import com.china.center.oa.stock.bean.PriceAskProviderBean;
import com.china.center.oa.stock.bean.StockBean;
import com.china.center.oa.stock.bean.StockItemBean;
import com.china.center.oa.stock.constant.PriceConstant;
import com.china.center.oa.stock.constant.StockConstant;
import com.china.center.oa.stock.dao.PriceAskProviderDAO;
import com.china.center.oa.stock.dao.StockDAO;
import com.china.center.oa.stock.dao.StockItemDAO;
import com.china.center.oa.stock.manager.StockManager;
import com.china.center.oa.stock.vo.PriceAskProviderBeanVO;
import com.china.center.oa.stock.vo.StockItemVO;
import com.china.center.oa.stock.vo.StockVO;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.RandomTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * StockManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-9-20
 * @see StockManagerImpl
 * @since 1.0
 */
@Exceptional
public class StockManagerImpl implements StockManager
{
    private StockDAO stockDAO = null;

    private CommonDAO commonDAO = null;

    /**
     * 流程日志
     */
    private FlowLogDAO flowLogDAO = null;

    private StockItemDAO stockItemDAO = null;

    private LocationDAO locationDAO = null;

    private ParameterDAO parameterDAO = null;

    private StafferDAO stafferDAO = null;

    private ShortMessageTaskDAO shortMessageTaskDAO = null;

    private String stockLocation = "";

    private PriceAskProviderDAO priceAskProviderDAO = null;

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.stock.manager.StockManager#addBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.stock.bean.StockBean)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addStockBean(User user, StockBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        LocationBean lb = locationDAO.find(bean.getLocationId());

        if (lb == null)
        {
            throw new MYException("区域不存在");
        }

        bean.setId(lb.getCode() + "_CG" + TimeTools.now("yyyyMMddHHmm") + commonDAO.getSquenceString());

        bean.setStatus(StockConstant.STOCK_STATUS_INIT);

        // 保存采购主单据
        stockDAO.saveEntityBean(bean);

        // 保存采购的产品
        List<StockItemBean> items = bean.getItem();

        for (StockItemBean stockItemBean : items)
        {
            stockItemBean.setStockId(bean.getId());

            stockItemDAO.saveEntityBean(stockItemBean);
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.stock.manager.StockManager#delStockBean(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean delStockBean(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StockBean sb = stockDAO.find(id);

        if (sb == null)
        {
            throw new MYException("采购单不存在");
        }

        if ( !user.getId().equals(sb.getUserId()))
        {
            throw new MYException("没有权限");
        }

        if (sb.getStatus() != StockConstant.STOCK_STATUS_REJECT && sb.getStatus() != StockConstant.STOCK_STATUS_INIT)
        {
            throw new MYException("采购单不存在初始或者驳回状态不能删除");
        }

        // 更新采购主单据
        stockDAO.deleteEntityBean(id);

        // 先删除
        stockItemDAO.deleteEntityBeansByFK(id);

        flowLogDAO.deleteEntityBeansByFK(id);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.stock.manager.StockManager#endStock(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = MYException.class)
    public String endStock(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        if ( !user.getLocationId().equals(this.stockLocation))
        {
            throw new MYException("只有采购部的人员才能结束采购单");
        }

        String fullId = "";

        StockBean bean = stockDAO.find(id);

        if (bean == null)
        {
            throw new MYException("采购单不存在");
        }

        if (bean.getStatus() != StockConstant.STOCK_STATUS_END)
        {
            throw new MYException("采购单不存在采购中,不能结束采购");
        }

        // TODO_OSGI 采购入库

        // 更新状态并且记录日志
        updateStockStatus(user, id, StockConstant.STOCK_STATUS_LASTEND, 0, "");

        return fullId;
    }

    /**
     * 更新采购单的状态
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    private boolean updateStockStatus(final User user, final String id, int nextStatus, int oprMode, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StockBean sb = stockDAO.find(id);

        if (sb == null)
        {
            throw new MYException("采购单不存在");
        }

        // 更新采购主单据
        stockDAO.updateStatus(id, nextStatus);

        stockDAO.updateExceptStatus(id, StockConstant.EXCEPTSTATUS_COMMON);

        addLog(user, id, nextStatus, sb, oprMode, reason);

        return true;
    }

    /**
     * 增加log
     * 
     * @param user
     * @param id
     * @param status
     * @param sb
     */
    private void addLog(final User user, final String id, int status, StockBean sb, int oprMode, String reason)
    {
        FlowLogBean log = new FlowLogBean();

        log.setActor(user.getStafferName());

        log.setLogTime(TimeTools.now());

        log.setFullId(id);

        // 操作类型
        log.setOprMode(oprMode);

        log.setPreStatus(sb.getStatus());

        log.setAfterStatus(status);

        log.setDescription(reason);

        flowLogDAO.saveEntityBean(log);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.stock.manager.StockManager#findStockVO(java.lang.String)
     */
    public StockVO findStockVO(String id)
    {
        StockVO vo = stockDAO.findVO(id);

        List<StockItemVO> itemVO = stockItemDAO.queryEntityVOsByFK(id);

        vo.setItemVO(itemVO);

        return vo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.stock.manager.StockManager#passStock(com.center.china.osgi.publics.User,
     *      java.lang.String)
     */
    @Transactional(rollbackFor = {MYException.class, DataAccessException.class})
    public boolean passStock(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StockVO sb = stockDAO.findVO(id);

        if (sb == null)
        {
            throw new MYException("采购单不存在");
        }

        int nextStatus = getNextStatus(user, sb.getStatus());

        List<StockItemVO> itemList = stockItemDAO.queryEntityVOsByFK(id);

        double total = 0.0d;

        // 如果是结束需要验证是否是外网询价
        checkEndStock(sb, nextStatus, itemList);

        // 询价处理 需要全部的item都询价结束
        if (nextStatus == StockConstant.STOCK_STATUS_PRICEPASS)
        {
            for (StockItemBean stockItemBean : itemList)
            {
                if (stockItemBean.getStatus() != StockConstant.STOCK_ITEM_STATUS_ASK)
                {
                    throw new MYException("采购单下存在没有询价的产品,不能通过");
                }

                total += stockItemBean.getTotal();
            }

            stockDAO.updateTotal(id, total);
        }

        // 采购主管通过后到 单比金额大于5万，或者是选择价格不是最低，需要采购经理审核
        if (sb.getStatus() == StockConstant.STOCK_STATUS_PRICEPASS)
        {
            if ( !checkMin(itemList))
            {
                stockDAO.updateExceptStatus(id, StockConstant.EXCEPTSTATUS_EXCEPTION_MIN);

                nextStatus = StockConstant.STOCK_STATUS_STOCKPASS;
            }

            StringBuilder sbStr = new StringBuilder();

            if ( !checkItemMoney(itemList, sbStr))
            {
                stockDAO.updateExceptStatus(id, StockConstant.EXCEPTSTATUS_EXCEPTION_MONEY);

                // 获得董事长的人员
                List<StafferBean> sbList = stafferDAO.queryStafferByAuthId(AuthConstant.SPECIAL_AUTH_CHAIRMAN);

                for (StafferBean stafferBean : sbList)
                {
                    sendSMSInner(user, stafferBean, id, sb, sbStr.toString());
                }

                nextStatus = StockConstant.STOCK_STATUS_STOCKPASS;
            }
        }

        // 当结束的时候 把各个item的状态标志成结束(给询价作为参考)
        if (nextStatus == StockConstant.STOCK_STATUS_END)
        {
            for (StockItemBean stockItemBean2 : itemList)
            {
                // 更新状态
                stockItemBean2.setStatus(StockConstant.STOCK_ITEM_STATUS_END);

                // 更新记录时间
                stockItemBean2.setLogTime(TimeTools.now());

                stockItemDAO.updateEntityBean(stockItemBean2);
            }
        }

        String reason = "";

        // 处理外网询价的特殊流程
        if (nextStatus == StockConstant.STOCK_STATUS_MANAGERPASS && sb.getType() == PriceConstant.PRICE_ASK_TYPE_NET)
        {
            // 直接到(采购主管)
            nextStatus = StockConstant.STOCK_STATUS_PRICEPASS;

            reason = "外网询价采购无需询价员询价";
        }

        updateStockStatus(user, id, nextStatus, 0, reason);

        return true;
    }

    /**
     * 检查采购的是否是最小的价格
     * 
     * @param item
     * @return
     */
    private boolean checkMin(List<StockItemVO> item)
    {
        List<PriceAskProviderBean> ppbs = null;

        for (StockItemBean stockItemBean : item)
        {
            ppbs = priceAskProviderDAO.queryEntityBeansByFK(stockItemBean.getId());

            double min = Integer.MAX_VALUE;
            for (PriceAskProviderBean priceAskProviderBean : ppbs)
            {
                min = Math.min(priceAskProviderBean.getPrice(), min);
            }

            if (stockItemBean.getPrice() > min)
            {
                return false;
            }
        }

        return true;
    }

    /**
     * 检查采购的单比价格有没有超过5万的
     * 
     * @param item
     * @return
     */
    private boolean checkItemMoney(List<StockItemVO> item, StringBuilder sbStr)
    {
        int max = parameterDAO.getInt(SysConfigConstant.STOCK_MAX_SINGLE_MONEY);

        for (StockItemVO stockItemBean : item)
        {
            if (stockItemBean.getTotal() >= max)
            {
                sbStr.append("[" + stockItemBean.getProductName() + "]的采购金额为:"
                             + MathTools.formatNum(stockItemBean.getTotal()) + ",请注意");

                return false;
            }
        }

        return true;
    }

    /**
     * checkSubmit
     * 
     * @param sb
     * @param nextStatus
     * @param itemList
     * @throws MYException
     */
    private void checkEndStock(StockBean sb, int nextStatus, List<StockItemVO> itemList)
        throws MYException
    {
        if (nextStatus == StockConstant.STOCK_STATUS_END && sb.getType() == PriceConstant.PRICE_ASK_TYPE_NET)
        {
            // 需要校验数量是
            for (StockItemBean iitem : itemList)
            {
                if (StringTools.isNullOrNone(iitem.getPriceAskProviderId()))
                {
                    throw new MYException("数据错误,请重新操作");
                }

                // 事务内已经被使用的
                int sum = stockItemDAO.sumNetProductByPid(iitem.getPriceAskProviderId());

                PriceAskProviderBeanVO ppb = priceAskProviderDAO.findVO(iitem.getPriceAskProviderId());

                if (ppb == null)
                {
                    throw new MYException("数据错误,请重新操作");
                }

                if (ppb.getSupportAmount() < sum)
                {
                    throw new MYException("外网采购中供应商[%s]提供的产品[%s]数量只有%d已经使用了%d,你请求的采购数量[%d]已经超出(可能其他业务员已经采购一空)", ppb
                        .getProviderName(), ppb.getProductName(), ppb.getSupportAmount(), (sum - iitem.getAmount()),
                        iitem.getAmount());
                }
            }
        }
    }

    /**
     * 获得下一个状态
     * 
     * @param current
     * @param isPass
     * @return
     * @throws MYException
     */
    private int getNextStatus(User user, int current)
        throws MYException
    {
        if (current == StockConstant.STOCK_STATUS_INIT)
        {
            return StockConstant.STOCK_STATUS_SUBMIT;
        }

        if (current == StockConstant.STOCK_STATUS_REJECT)
        {
            return StockConstant.STOCK_STATUS_SUBMIT;
        }

        if (current == StockConstant.STOCK_STATUS_SUBMIT)
        {
            return StockConstant.STOCK_STATUS_MANAGERPASS;
        }

        if (current == StockConstant.STOCK_STATUS_MANAGERPASS)
        {
            if (AuthHelper.containAuth(user, AuthConstant.STOCK_MANAGER_PASS))
            {
                return StockConstant.STOCK_STATUS_PRICEPASS;
            }
            else
            {
                throw new MYException("没有权限");
            }
        }

        // 这里是采购主管的操作(如果没有异常忽略采购经理)
        if (current == StockConstant.STOCK_STATUS_PRICEPASS)
        {
            return StockConstant.STOCK_STATUS_STOCKMANAGERPASS;
        }

        if (current == StockConstant.STOCK_STATUS_STOCKPASS)
        {
            if (AuthHelper.containAuth(user, AuthConstant.STOCK_NET_STOCK_PASS, AuthConstant.STOCK_INNER_STOCK_PASS))
            {
                return StockConstant.STOCK_STATUS_STOCKMANAGERPASS;
            }
            else
            {
                throw new MYException("没有权限");
            }
        }

        if (current == StockConstant.STOCK_STATUS_STOCKMANAGERPASS)
        {
            return StockConstant.STOCK_STATUS_END;
        }

        return StockConstant.STOCK_STATUS_INIT;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.stock.manager.StockManager#rejectStock(com.center.china.osgi.publics.User,
     *      java.lang.String, java.lang.String)
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean rejectStock(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StockBean sb = stockDAO.find(id);

        if (sb == null)
        {
            throw new MYException("采购单不存在");
        }

        if (sb.getStatus() == StockConstant.STOCK_STATUS_REJECT || sb.getStatus() == StockConstant.STOCK_STATUS_INIT
            || sb.getStatus() == StockConstant.STOCK_STATUS_END || sb.getStatus() == StockConstant.STOCK_STATUS_LASTEND)
        {
            throw new MYException("采购单状态错误");
        }

        int nextStatus = StockConstant.STOCK_STATUS_REJECT;

        recoverStockItemAsk(id);

        return updateStockStatus(user, id, nextStatus, 1, reason);
    }

    /**
     * 恢复采购询价的初始状态
     * 
     * @param id
     */
    private void recoverStockItemAsk(final String id)
    {
        List<StockItemBean> item = stockItemDAO.queryEntityBeansByFK(id);

        // 删除询价
        for (StockItemBean stockItemBean : item)
        {
            stockItemBean.setProviderId("");

            stockItemBean.setPriceAskProviderId("");

            stockItemBean.setStatus(StockConstant.STOCK_ITEM_STATUS_INIT);

            stockItemBean.setPrice(0.0d);

            stockItemBean.setTotal(0.0d);

            // 先修改item的一些值
            stockItemDAO.updateEntityBean(stockItemBean);

            // 删除先前的询价
            priceAskProviderDAO.deleteEntityBeansByFK(stockItemBean.getId());
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.stock.manager.StockManager#rejectStockToAsk(com.center.china.osgi.publics.User,
     *      java.lang.String, java.lang.String)
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean rejectStockToAsk(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StockBean sb = stockDAO.find(id);

        if (sb == null)
        {
            throw new MYException("采购单不存在");
        }

        // 只有在询价员通过下才能驳回到询价员
        if (sb.getStatus() != StockConstant.STOCK_STATUS_PRICEPASS)
        {
            throw new MYException("采购单状态错误");
        }

        if ( !AuthHelper.containAuth(user, AuthConstant.STOCK_INNER_STOCK_PASS))
        {
            throw new MYException("不能操作");
        }

        recoverStockItemAsk(id);

        return updateStockStatus(user, id, StockConstant.STOCK_STATUS_MANAGERPASS, 1, reason);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.stock.manager.StockManager#stockItemAsk(com.china.center.oa.stock.bean.StockItemBean)
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean stockItemAsk(StockItemBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        bean.setStatus(StockConstant.STOCK_ITEM_STATUS_ASK);

        bean.setTotal(bean.getPrice() * bean.getAmount());

        // 先修改item的一些值
        stockItemDAO.updateEntityBean(bean);

        // 删除先前的询价
        priceAskProviderDAO.deleteEntityBeansByFK(bean.getId());

        for (PriceAskProviderBean priceAskProviderBean : bean.getAsks())
        {
            priceAskProviderDAO.saveEntityBean(priceAskProviderBean);
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.stock.manager.StockManager#stockItemAskChange(java.lang.String, java.lang.String)
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean stockItemAskChange(String itemId, String providerId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(itemId, providerId);

        PriceAskProviderBean bean = priceAskProviderDAO.findBeanByAskIdAndProviderId(itemId, providerId,
            PriceConstant.PRICE_ASK_TYPE_INNER);

        if (bean == null)
        {
            throw new MYException("系统错误");
        }

        StockItemBean item = stockItemDAO.find(itemId);

        if (item == null)
        {
            throw new MYException("系统错误");
        }

        item.setProviderId(providerId);

        item.setPrice(bean.getPrice());

        item.setTotal(bean.getPrice() * item.getAmount());

        // 更新item
        stockItemDAO.updateEntityBean(item);

        List<StockItemBean> items = stockItemDAO.queryEntityBeansByFK(item.getStockId());

        double total = 0.0d;

        for (StockItemBean stockItemBean : items)
        {
            if (stockItemBean.getStatus() != StockConstant.STOCK_ITEM_STATUS_ASK)
            {
                throw new MYException("采购单下存在没有询价的产品,不能通过");
            }

            total += stockItemBean.getTotal();
        }

        // 更新采购单据的总金额
        stockDAO.updateTotal(item.getStockId(), total);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.stock.manager.StockManager#stockItemAskForNet(com.china.center.oa.stock.bean.StockItemBean,
     *      java.util.List)
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean stockItemAskForNet(StockItemBean oldItem, List<StockItemBean> newItemList)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(oldItem, newItemList);

        stockItemDAO.deleteEntityBean(oldItem.getId());

        for (StockItemBean stockItemBean : newItemList)
        {
            stockItemBean.setStatus(StockConstant.STOCK_ITEM_STATUS_ASK);
            stockItemBean.setTotal(stockItemBean.getAmount() * stockItemBean.getPrice());
        }

        stockItemDAO.saveAllEntityBeans(newItemList);

        return true;
    }

    @Transactional(rollbackFor = {MYException.class})
    public boolean updateStockNearlyPayDate(User user, String id, String nearlyPayDate)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StockBean sb = stockDAO.find(id);

        if (sb == null)
        {
            throw new MYException("采购单不存在");
        }

        sb.setNearlyPayDate(nearlyPayDate);

        // 更新采购主单据
        stockDAO.updateEntityBean(sb);

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.stock.manager.StockManager#updateStockBean(com.center.china.osgi.publics.User,
     *      com.china.center.oa.stock.bean.StockBean)
     */
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateStockBean(User user, StockBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        StockBean sb = stockDAO.find(bean.getId());

        if (sb == null)
        {
            throw new MYException("采购单不存在");
        }

        if (sb.getStatus() != StockConstant.STOCK_STATUS_REJECT && sb.getStatus() != StockConstant.STOCK_STATUS_INIT)
        {
            throw new MYException("采购单不存在驳回状态不能修改");
        }

        // 更新采购主单据
        stockDAO.updateEntityBean(bean);

        // 先删除
        stockItemDAO.deleteEntityBeansByFK(bean.getId());

        List<StockItemBean> items = bean.getItem();

        for (StockItemBean stockItemBean : items)
        {
            stockItemBean.setStockId(bean.getId());

            stockItemDAO.saveEntityBean(stockItemBean);
        }

        return true;
    }

    /**
     * sendSMSInner
     * 
     * @param user
     * @param sb
     * @param id
     * @param sbvo
     * @param message
     */
    private void sendSMSInner(User user, StafferBean sb, String id, StockVO sbvo, String message)
    {
        // send short message
        ShortMessageTaskBean sms = new ShortMessageTaskBean();

        sms.setId(commonDAO.getSquenceString20());

        sms.setFk(id);

        sms.setType(104);

        sms.setHandId(RandomTools.getRandomMumber(4));

        sms.setStatus(ShortMessageConstant.STATUS_INIT);

        sms.setMtype(ShortMessageConstant.MTYPE_ONLY_SEND);

        sms.setFktoken("0");

        sms.setMessage(sbvo.getUserName() + "发起的采购单" + message);

        sms.setReceiver(sb.getHandphone());

        sms.setStafferId(sb.getId());

        sms.setLogTime(TimeTools.now());

        // 24 hour
        sms.setEndTime(TimeTools.now(1));

        // internal
        sms.setSendTime(TimeTools.now());

        // add sms
        shortMessageTaskDAO.saveEntityBean(sms);
    }

    /**
     * @return the stockDAO
     */
    public StockDAO getStockDAO()
    {
        return stockDAO;
    }

    /**
     * @param stockDAO
     *            the stockDAO to set
     */
    public void setStockDAO(StockDAO stockDAO)
    {
        this.stockDAO = stockDAO;
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
     * @return the stockItemDAO
     */
    public StockItemDAO getStockItemDAO()
    {
        return stockItemDAO;
    }

    /**
     * @param stockItemDAO
     *            the stockItemDAO to set
     */
    public void setStockItemDAO(StockItemDAO stockItemDAO)
    {
        this.stockItemDAO = stockItemDAO;
    }

    /**
     * @return the locationDAO
     */
    public LocationDAO getLocationDAO()
    {
        return locationDAO;
    }

    /**
     * @param locationDAO
     *            the locationDAO to set
     */
    public void setLocationDAO(LocationDAO locationDAO)
    {
        this.locationDAO = locationDAO;
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
     * @return the shortMessageTaskDAO
     */
    public ShortMessageTaskDAO getShortMessageTaskDAO()
    {
        return shortMessageTaskDAO;
    }

    /**
     * @param shortMessageTaskDAO
     *            the shortMessageTaskDAO to set
     */
    public void setShortMessageTaskDAO(ShortMessageTaskDAO shortMessageTaskDAO)
    {
        this.shortMessageTaskDAO = shortMessageTaskDAO;
    }

    /**
     * @return the stockLocation
     */
    public String getStockLocation()
    {
        return stockLocation;
    }

    /**
     * @param stockLocation
     *            the stockLocation to set
     */
    public void setStockLocation(String stockLocation)
    {
        this.stockLocation = stockLocation;
    }

    /**
     * @return the priceAskProviderDAO
     */
    public PriceAskProviderDAO getPriceAskProviderDAO()
    {
        return priceAskProviderDAO;
    }

    /**
     * @param priceAskProviderDAO
     *            the priceAskProviderDAO to set
     */
    public void setPriceAskProviderDAO(PriceAskProviderDAO priceAskProviderDAO)
    {
        this.priceAskProviderDAO = priceAskProviderDAO;
    }
}
