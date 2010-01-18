/**
 * File Name: ConsumeManager.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-12-15
 * Grant: open source to everybody
 */
package com.china.centet.yongyin.manager;


import java.util.ArrayList;
import java.util.List;

import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.cache.bean.MoreHashMap;
import com.china.center.common.MYException;
import com.china.center.eltools.ElTools;
import com.china.center.oa.note.bean.ShortMessageConstant;
import com.china.center.oa.note.bean.ShortMessageTaskBean;
import com.china.center.oa.note.dao.ShortMessageTaskDAO;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.RandomTools;
import com.china.center.tools.SequenceTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;
import com.china.centet.yongyin.bean.BaseBean;
import com.china.centet.yongyin.bean.FlowLogBean;
import com.china.centet.yongyin.bean.LocationBean;
import com.china.centet.yongyin.bean.OutBean;
import com.china.centet.yongyin.bean.PriceAskProviderBean;
import com.china.centet.yongyin.bean.Role;
import com.china.centet.yongyin.bean.StafferBean2;
import com.china.centet.yongyin.bean.StockBean;
import com.china.centet.yongyin.bean.StockItemBean;
import com.china.centet.yongyin.bean.StockPayBean;
import com.china.centet.yongyin.bean.StockPayItemBean;
import com.china.centet.yongyin.bean.User;
import com.china.centet.yongyin.bean.helper.LocationHelper;
import com.china.centet.yongyin.constant.Constant;
import com.china.centet.yongyin.constant.PriceConstant;
import com.china.centet.yongyin.constant.StockConstant;
import com.china.centet.yongyin.constant.SysConfigConstant;
import com.china.centet.yongyin.dao.BaseBeanDAO;
import com.china.centet.yongyin.dao.CommonDAO;
import com.china.centet.yongyin.dao.CommonDAO2;
import com.china.centet.yongyin.dao.FlowLogDAO;
import com.china.centet.yongyin.dao.LocationDAO;
import com.china.centet.yongyin.dao.ParameterDAO;
import com.china.centet.yongyin.dao.PriceAskProviderDAO;
import com.china.centet.yongyin.dao.PriceDAO;
import com.china.centet.yongyin.dao.StafferDAO2;
import com.china.centet.yongyin.dao.StockDAO;
import com.china.centet.yongyin.dao.StockItemDAO;
import com.china.centet.yongyin.dao.StockPayDAO;
import com.china.centet.yongyin.dao.StockPayItemDAO;
import com.china.centet.yongyin.vo.PriceAskProviderBeanVO;
import com.china.centet.yongyin.vo.StockBeanVO;
import com.china.centet.yongyin.vo.StockItemBeanVO;


/**
 * 会员消费的manager
 * 
 * @author ZHUZHU
 * @version 2007-12-15
 * @see
 * @since
 */
public class StockManager
{
    private PriceDAO priceDAO = null;

    private StockDAO stockDAO = null;

    private CommonDAO commonDAO = null;

    private OutManager outManager = null;

    /**
     * 流程日志
     */
    private FlowLogDAO flowLogDAO = null;

    private StockItemDAO stockItemDAO = null;

    private LocationDAO locationDAO = null;

    private ParameterDAO parameterDAO = null;

    private StafferDAO2 stafferDAO2 = null;

    private CommonDAO2 commonDAO2 = null;

    private ShortMessageTaskDAO shortMessageTaskDAO = null;

    private BaseBeanDAO baseBeanDAO = null;

    private StockPayItemDAO stockPayItemDAO = null;

    private StockPayDAO stockPayDAO = null;

    private String stockLocation = "";

    private PriceAskProviderDAO priceAskProviderDAO = null;

    private static MoreHashMap<Integer, Integer, Role> map = new MoreHashMap<Integer, Integer, Role>();

    private static MoreHashMap<Integer, Integer, Role> rejectMap = new MoreHashMap<Integer, Integer, Role>();

    static
    {
        map.put(StockConstant.STOCK_STATUS_REJECT, StockConstant.STOCK_STATUS_SUBMIT, Role.COMMON);

        map.put(StockConstant.STOCK_ITEM_STATUS_INIT, StockConstant.STOCK_STATUS_SUBMIT,
            Role.COMMON);

        map.put(StockConstant.STOCK_STATUS_SUBMIT, StockConstant.STOCK_STATUS_MANAGERPASS,
            Role.MANAGER);

        map.put(StockConstant.STOCK_STATUS_MANAGERPASS, StockConstant.STOCK_STATUS_PRICEPASS,
            Role.PRICE);

        map.put(StockConstant.STOCK_STATUS_PRICEPASS, StockConstant.STOCK_STATUS_END, Role.STOCK);

        map.put(StockConstant.STOCK_STATUS_STOCKPASS, StockConstant.STOCK_STATUS_END,
            Role.STOCKMANAGER);

        rejectMap.put(StockConstant.STOCK_STATUS_SUBMIT, StockConstant.STOCK_STATUS_REJECT,
            Role.MANAGER);

        rejectMap.put(StockConstant.STOCK_STATUS_MANAGERPASS, StockConstant.STOCK_STATUS_REJECT,
            Role.PRICE);

        rejectMap.put(StockConstant.STOCK_STATUS_PRICEPASS, StockConstant.STOCK_STATUS_REJECT,
            Role.STOCK);

        rejectMap.put(StockConstant.STOCK_STATUS_STOCKPASS, StockConstant.STOCK_STATUS_REJECT,
            Role.STOCKMANAGER);
    }

    /**
     * default constructor
     */
    public StockManager()
    {}

    /**
     * 保存采购单
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean addStockBean(final User user, final StockBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(bean);

        LocationBean lb = locationDAO.findLocation(bean.getLocationId());

        if (lb == null)
        {
            throw new MYException("区域不存在");
        }

        bean.setId(lb.getLocationCode() + "_CG" + TimeTools.now("yyyyMMddHHmm")
                   + commonDAO.getSquenceString());

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

    /**
     * addStockPayBean(归类生成付款单)
     * 
     * @param user
     * @param bean
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean addStockPayBean(final User user, List<String> payItemIdList)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(payItemIdList);

        List<StockPayBean> payList = new ArrayList();

        for (String eachStr : payItemIdList)
        {
            StockPayItemBean sib = stockPayItemDAO.find(eachStr);

            if (sib == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            if (sib.getStatus() != StockConstant.STOCK_ITEM_PAY_STATUS_INIT)
            {
                throw new MYException("单据已经汇总,请确认操作");
            }

            StockPayBean payEach = getPay(sib.getProviderId(), payList);

            if (payEach == null)
            {
                StockPayBean pay = new StockPayBean();

                payList.add(pay);

                pay.setId(SequenceTools.getSequence(10));

                pay.setProviderId(sib.getProviderId());

                pay.setLogTime(TimeTools.now());

                pay.setStafferId(user.getStafferId());

                pay.setTotal(sib.getTotal());

                payEach = pay;
            }
            else
            {
                payEach.setTotal(sib.getTotal() + payEach.getTotal());
            }

            sib.setPayId(payEach.getId());

            sib.setStatus(StockConstant.STOCK_ITEM_PAY_STATUS_USED);

            // 修改成绑定
            stockPayItemDAO.updateEntityBean(sib);
        }

        // 全部保存
        stockPayDAO.saveAllEntityBeans(payList);

        return true;
    }

    private StockPayBean getPay(String providerId, List<StockPayBean> payList)
    {
        for (StockPayBean stockPayBean : payList)
        {
            if (stockPayBean.getProviderId().equals(providerId))
            {
                return stockPayBean;
            }
        }

        return null;
    }

    /**
     * 查询采购单
     * 
     * @param id
     * @return
     * @throws MYException
     */
    public StockBeanVO findStockVO(String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(id);

        StockBeanVO vo = stockDAO.findVO(id);

        List<StockItemBeanVO> itemVO = stockItemDAO.queryEntityVOsByFK(id);

        vo.setItemVO(itemVO);

        return vo;
    }

    /**
     * 采购item的改变
     * 
     * @param itemId
     * @param providerId
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean stockItemAskChange(String itemId, String providerId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(itemId, providerId);

        PriceAskProviderBean bean = priceAskProviderDAO.findBeanByAskIdAndProviderId(itemId,
            providerId);

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

    /**
     * 采购询价
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
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

    /**
     * 更新采购单
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean updateStockBean(final User user, final StockBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean);

        StockBean sb = stockDAO.find(bean.getId());

        if (sb == null)
        {
            throw new MYException("采购单不存在");
        }

        if (sb.getStatus() != StockConstant.STOCK_STATUS_REJECT
            && sb.getStatus() != StockConstant.STOCK_STATUS_INIT)
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
     * 更新采购单
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public String endStock(final User user, final String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        if ( !user.getLocationID().equals(this.stockLocation))
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

        List<OutBean> beanList = autoToOut(user, id, bean);

        for (OutBean outBean : beanList)
        {
            fullId += outManager.coloneOutAndSubmitWithOutAffair(outBean, user) + ";";
        }

        // 更新状态并且记录日志
        updateStockStatus(user, id, StockConstant.STOCK_STATUS_LASTEND, Constant.OPRMODE_PASS, "");

        return fullId;
    }

    /**
     * 根据采购单生成自动入库单，每个采购单体生成一个入库单
     * 
     * @param user
     * @param id
     * @param bean
     * @param out
     */
    private List<OutBean> autoToOut(final User user, final String id, StockBean bean)
    {
        List<OutBean> beanList = new ArrayList<OutBean>();

        List<StockItemBeanVO> items = stockItemDAO.queryEntityVOsByFK(id);

        for (StockItemBeanVO item : items)
        {
            List<BaseBean> baseList = new ArrayList<BaseBean>();

            OutBean out = new OutBean();

            out.setStatus(Constant.STATUS_SAVE);

            out.setStafferName(user.getStafferName());

            out.setType(Constant.OUT_TYPE_INBILL);

            out.setOutTime(TimeTools.now());

            out.setDepartment("采购部");

            out.setCustomerId(item.getProviderId());

            out.setCustomerName(item.getProviderName());

            out.setLocationId(this.stockLocation);

            // 产品所在区域 采购部
            out.setLocation(this.stockLocation);

            out.setTotal(item.getTotal());

            out.setDescription("采购单自动转换成入库单,采购单单号:" + id);

            BaseBean baseBean = new BaseBean();

            baseBean.setValue(item.getTotal());
            baseBean.setLocationId(this.stockLocation);
            baseBean.setAmount(item.getAmount());
            baseBean.setProductId(item.getProductId());
            baseBean.setProductName(item.getProductName());
            baseBean.setUnit("套");
            baseBean.setPrice(item.getPrice());
            baseBean.setValue(item.getTotal());

            baseList.add(baseBean);

            out.setBaseList(baseList);

            beanList.add(out);
        }

        return beanList;
    }

    /**
     * 采购单调出
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public String stockItemChangeToOut(final User user, final String id, String tranNo)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        if ( !user.getLocationID().equals(this.stockLocation))
        {
            throw new MYException("只有采购部的人员才能调出采购单");
        }

        String fullId = null;

        StockItemBeanVO item = stockItemDAO.findVO(id);

        if (item == null)
        {
            throw new MYException("采购单项不存在");
        }

        if (item.getHasRef() != StockConstant.STOCK_ITEM_REF_NO)
        {
            throw new MYException("采购单项不能调出");
        }

        StockBean bean = stockDAO.find(item.getStockId());

        if (bean == null)
        {
            throw new MYException("采购单不存在");
        }

        // 生成入库单 outManager
        OutBean out = new OutBean();

        itemAutoToOut(user, tranNo, item, bean, out);

        fullId = outManager.coloneOutAndSubmitWithOutAffair(out, user);

        if ( !StringTools.isNullOrNone(fullId))
        {
            StockItemBean sb = stockItemDAO.find(id);

            sb.setRefOutId(fullId);

            sb.setHasRef(StockConstant.STOCK_ITEM_REF_YES);

            stockItemDAO.updateEntityBean(sb);
        }

        return fullId;
    }

    /**
     * @param user
     * @param tranNo
     * @param item
     * @param bean
     * @param out
     */
    private void itemAutoToOut(final User user, String tranNo, StockItemBeanVO item,
                               StockBean bean, OutBean out)
    {
        out.setStatus(Constant.STATUS_SAVE);

        out.setStafferName(user.getStafferName());

        out.setType(Constant.OUT_TYPE_INBILL);

        out.setOutType(Constant.INBILL_OUT);

        out.setOutTime(TimeTools.now());

        out.setDepartment("采购部");

        out.setCustomerId(item.getProviderId());

        out.setCustomerName(item.getProviderName());

        out.setLocationId(this.stockLocation);

        // 产品所在区域 采购部
        out.setLocation(this.stockLocation);

        out.setDestinationId(LocationHelper.SYSTEMLOCATION);

        out.setTranNo(tranNo);

        out.setTotal(item.getTotal());

        out.setDescription("采购单自动调出转换成调拨单,采购单单号:" + bean.getId());

        List<BaseBean> baseList = new ArrayList<BaseBean>();

        BaseBean baseBean = new BaseBean();

        baseBean.setValue( -item.getTotal());
        baseBean.setLocationId(this.stockLocation);
        baseBean.setAmount( -item.getAmount());
        baseBean.setProductId(item.getProductId());
        baseBean.setProductName(item.getProductName());
        baseBean.setUnit("套");
        baseBean.setPrice(item.getPrice());
        baseBean.setValue(item.getTotal());

        baseList.add(baseBean);

        out.setBaseList(baseList);
    }

    /**
     * 更新采购单
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean delStockBean(final User user, final String id)
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

        if (sb.getStatus() != StockConstant.STOCK_STATUS_REJECT
            && sb.getStatus() != StockConstant.STOCK_STATUS_INIT)
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

    /**
     * 通过
     * 
     * @param user
     * @param id
     * @param reason
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class, DataAccessException.class})
    @Exceptional
    public boolean passStock(final User user, final String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StockBeanVO sb = stockDAO.findVO(id);

        if (sb == null)
        {
            throw new MYException("采购单不存在");
        }

        if (user.getRole() != getOprRole(sb.getStatus()))
        {
            throw new MYException("不能操作");
        }

        int nextStatus = getNextStatus(sb.getStatus());

        List<StockItemBeanVO> itemList = stockItemDAO.queryEntityVOsByFK(id);

        double total = 0.0d;

        // 如果是提交需要验证是否是外网询价
        checkSubmit(sb, nextStatus, itemList);

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

                // 通知董事长(董事长的ID为5)
                List<StafferBean2> sbList = stafferDAO2.queryByPostId("5");

                for (StafferBean2 stafferBean2 : sbList)
                {
                    sendSMSInner(user, stafferBean2, id, sb, sbStr.toString());
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
        if (nextStatus == StockConstant.STOCK_STATUS_MANAGERPASS
            && sb.getType() == PriceConstant.PRICE_ASK_TYPE_NET)
        {
            // 直接到(采购主管)
            nextStatus = StockConstant.STOCK_STATUS_PRICEPASS;

            // 更新最终价格为询价价格
            for (StockItemBean iitem : itemList)
            {
                PriceAskProviderBean ppbs = priceAskProviderDAO.find(iitem.getPriceAskProviderId());

                if (ppbs == null)
                {
                    throw new MYException("数据错误,请确认操作");
                }

                iitem.setProviderId(ppbs.getProviderId());

                iitem.setPrice(iitem.getPrePrice());

                iitem.setTotal(iitem.getPrice() * iitem.getAmount());

                stockItemDAO.updateEntityBean(iitem);
            }

            reason = "外网询价采购无需询价员询价";
        }

        updateStockStatus(user, id, nextStatus, Constant.OPRMODE_PASS, reason);

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
    private void sendSMSInner(User user, StafferBean2 sb, String id, StockBeanVO sbvo,
                              String message)
    {
        // send short message
        ShortMessageTaskBean sms = new ShortMessageTaskBean();

        sms.setId(commonDAO2.getSquenceString20());

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
     * checkSubmit
     * 
     * @param sb
     * @param nextStatus
     * @param itemList
     * @throws MYException
     */
    private void checkSubmit(StockBean sb, int nextStatus, List<StockItemBeanVO> itemList)
        throws MYException
    {
        if (nextStatus == StockConstant.STOCK_STATUS_SUBMIT
            && sb.getType() == PriceConstant.PRICE_ASK_TYPE_NET)
        {
            // 需要校验数量是
            for (StockItemBean iitem : itemList)
            {
                if (StringTools.isNullOrNone(iitem.getPriceAskProviderId()))
                {
                    throw new MYException("数据错误,请重新操作");
                }

                int sum = stockItemDAO.sumNetProductByPid(iitem.getPriceAskProviderId())
                          + iitem.getAmount();

                PriceAskProviderBeanVO ppb = priceAskProviderDAO.findVO(iitem.getPriceAskProviderId());

                if (ppb == null)
                {
                    throw new MYException("数据错误,请重新操作");
                }

                if (ppb.getSupportAmount() < sum)
                {
                    throw new MYException(
                        "外网采购中供应商[%s]提供的产品[%s]数量只有%d已经使用了%d,你请求的采购数量[%d]已经超出(可能其他业务员已经采购一空)",
                        ppb.getProviderName(), ppb.getProductName(), ppb.getSupportAmount(),
                        (sum - iitem.getAmount()), iitem.getAmount());
                }
            }
        }
    }

    /**
     * 检查采购的是否是最小的价格
     * 
     * @param item
     * @return
     */
    private boolean checkMin(List<StockItemBeanVO> item)
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
    private boolean checkItemMoney(List<StockItemBeanVO> item, StringBuilder sbStr)
    {
        int max = parameterDAO.getInt(SysConfigConstant.STOCK_MAX_SINGLE_MONEY);

        for (StockItemBeanVO stockItemBean : item)
        {
            if (stockItemBean.getTotal() >= max)
            {
                sbStr.append("[" + stockItemBean.getProductName() + "]的采购金额为:"
                             + ElTools.formatNum(stockItemBean.getTotal()) + ",请注意");

                return false;
            }
        }

        return true;
    }

    /**
     * 驳回
     * 
     * @param user
     * @param id
     * @param status
     * @param reason
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean rejectStock(final User user, final String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        StockBean sb = stockDAO.find(id);

        if (sb == null)
        {
            throw new MYException("采购单不存在");
        }

        if (sb.getStatus() == StockConstant.STOCK_STATUS_REJECT
            || sb.getStatus() == StockConstant.STOCK_STATUS_INIT
            || sb.getStatus() == StockConstant.STOCK_STATUS_END
            || sb.getStatus() == StockConstant.STOCK_STATUS_LASTEND)
        {
            throw new MYException("采购单状态错误");
        }

        Role role = rejectMap.getValue2(sb.getStatus());

        if (user.getRole() != role)
        {
            throw new MYException("不能操作");
        }

        int nextStatus = StockConstant.STOCK_STATUS_REJECT;

        recoverStockItemAsk(id);

        return updateStockStatus(user, id, nextStatus, Constant.OPRMODE_REJECT, reason);
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

            stockItemBean.setStatus(StockConstant.STOCK_ITEM_STATUS_INIT);

            stockItemBean.setPrice(0.0d);

            stockItemBean.setTotal(0.0d);

            // 先修改item的一些值
            stockItemDAO.updateEntityBean(stockItemBean);

            // 删除先前的询价
            priceAskProviderDAO.deleteEntityBeansByFK(stockItemBean.getId());
        }
    }

    /**
     * stock驳回到询价
     * 
     * @param user
     * @param id
     * @param status
     * @param reason
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean rejectStockToAsk(final User user, final String id, String reason)
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

        if (user.getRole() != Role.STOCK)
        {
            throw new MYException("不能操作");
        }

        recoverStockItemAsk(id);

        return updateStockStatus(user, id, StockConstant.STOCK_STATUS_MANAGERPASS,
            Constant.OPRMODE_REJECT, reason);
    }

    /**
     * 更新采购单的状态
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    private boolean updateStockStatus(final User user, final String id, int nextStatus,
                                      int oprMode, String reason)
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

        addLog(user, id, nextStatus, sb, oprMode, reason);

        return true;
    }

    /**
     * 更新采购单的状态
     * 
     * @param bean
     * @return
     * @throws MYException
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean updateStockPayStatus(final User user, final String id, int payStatus)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        checkPay(user, id, payStatus);

        // 更新采购主单据
        stockDAO.updatePayStatus(id, payStatus);

        handleStockItemPay(user, id, payStatus);

        return true;
    }

    /**
     * handleStockItemPay
     * 
     * @param user
     * @param id
     * @param payStatus
     */
    private void handleStockItemPay(final User user, final String id, int payStatus)
    {
        if (payStatus == StockConstant.STOCK_PAY_YES)
        {
            // NOTE 自动生成付款单(根据采购的item)
            List<StockItemBean> stockItemList = stockItemDAO.queryEntityBeansByFK(id);

            for (StockItemBean stockItemBean : stockItemList)
            {
                StockPayItemBean spib = new StockPayItemBean();

                spib.setId(SequenceTools.getSequence(10));

                spib.setStockId(id);

                spib.setStockItemId(stockItemBean.getId());

                spib.setStafferId(user.getStafferId());

                spib.setProviderId(stockItemBean.getProviderId());

                spib.setProductId(stockItemBean.getProductId());

                spib.setAmount(stockItemBean.getAmount());

                spib.setLogTime(TimeTools.now());

                spib.setPrice(stockItemBean.getPrice());

                spib.setTotal(stockItemBean.getPrice() * stockItemBean.getAmount());

                spib.setStatus(StockConstant.STOCK_ITEM_PAY_STATUS_INIT);

                stockPayItemDAO.saveEntityBean(spib);
            }
        }
    }

    /**
     * 检查是否可以修改采购单的付款状态
     * 
     * @param id
     * @param payStatus
     * @throws MYException
     */
    private void checkPay(User user, String id, int payStatus)
        throws MYException
    {
        StockBean sb = stockDAO.find(id);

        if (sb == null)
        {
            throw new MYException("采购单不存在");
        }

        if (sb.getPay() == StockConstant.STOCK_PAY_YES)
        {
            throw new MYException("采购单付款不能操作");
        }

        // 申请
        if (payStatus == StockConstant.STOCK_PAY_APPLY)
        {
            if (user.getRole() != Role.STOCK)
            {
                throw new MYException("没有权限");
            }

            if (sb.getPay() != StockConstant.STOCK_PAY_NO
                && sb.getPay() != StockConstant.STOCK_PAY_REJECT)
            {
                throw new MYException("采购单不能申请付款");
            }
        }

        if (payStatus == StockConstant.STOCK_PAY_REJECT)
        {
            if (user.getRole() != Role.MANAGER
                || !LocationHelper.isSystemLocation(user.getLocationID()))
            {
                throw new MYException("没有权限");
            }

            if (sb.getPay() != StockConstant.STOCK_PAY_APPLY)
            {
                throw new MYException("采购单不能驳回申请付款");
            }
        }

        if (payStatus == StockConstant.STOCK_PAY_YES)
        {
            if (user.getRole() != Role.STOCKMANAGER)
            {
                throw new MYException("没有权限");
            }

            if (sb.getPay() != StockConstant.STOCK_PAY_APPLY)
            {
                throw new MYException("采购单不能通过申请付款");
            }
        }
    }

    /**
     * 获得下一个状态
     * 
     * @param current
     * @param isPass
     * @return
     */
    private int getNextStatus(int current)
    {
        return map.getValue1(current);
    }

    private Role getOprRole(int current)
    {
        return map.getValue2(current);
    }

    /**
     * 增加log
     * 
     * @param user
     * @param id
     * @param status
     * @param sb
     */
    private void addLog(final User user, final String id, int status, StockBean sb, int oprMode,
                        String reason)
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

    /**
     * @return the priceDAO
     */
    public PriceDAO getPriceDAO()
    {
        return priceDAO;
    }

    /**
     * @param priceDAO
     *            the priceDAO to set
     */
    public void setPriceDAO(PriceDAO priceDAO)
    {
        this.priceDAO = priceDAO;
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
     * @return the outManager
     */
    public OutManager getOutManager()
    {
        return outManager;
    }

    /**
     * @param outManager
     *            the outManager to set
     */
    public void setOutManager(OutManager outManager)
    {
        this.outManager = outManager;
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
     * @return the baseBeanDAO
     */
    public BaseBeanDAO getBaseBeanDAO()
    {
        return baseBeanDAO;
    }

    /**
     * @param baseBeanDAO
     *            the baseBeanDAO to set
     */
    public void setBaseBeanDAO(BaseBeanDAO baseBeanDAO)
    {
        this.baseBeanDAO = baseBeanDAO;
    }

    /**
     * @return the stockPayItemDAO
     */
    public StockPayItemDAO getStockPayItemDAO()
    {
        return stockPayItemDAO;
    }

    /**
     * @param stockPayItemDAO
     *            the stockPayItemDAO to set
     */
    public void setStockPayItemDAO(StockPayItemDAO stockPayItemDAO)
    {
        this.stockPayItemDAO = stockPayItemDAO;
    }

    /**
     * @return the stockPayDAO
     */
    public StockPayDAO getStockPayDAO()
    {
        return stockPayDAO;
    }

    /**
     * @param stockPayDAO
     *            the stockPayDAO to set
     */
    public void setStockPayDAO(StockPayDAO stockPayDAO)
    {
        this.stockPayDAO = stockPayDAO;
    }

    /**
     * @return the stafferDAO2
     */
    public StafferDAO2 getStafferDAO2()
    {
        return stafferDAO2;
    }

    /**
     * @param stafferDAO2
     *            the stafferDAO2 to set
     */
    public void setStafferDAO2(StafferDAO2 stafferDAO2)
    {
        this.stafferDAO2 = stafferDAO2;
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
}
