/**
 * File Name: OutManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.manager.impl;


import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.center.china.osgi.publics.AbstractListenerManager;
import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.credit.bean.CreditLevelBean;
import com.china.center.oa.credit.dao.CreditCoreDAO;
import com.china.center.oa.credit.dao.CreditLevelDAO;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.constant.CustomerConstant;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.note.bean.ShortMessageTaskBean;
import com.china.center.oa.note.constant.ShortMessageConstant;
import com.china.center.oa.note.dao.ShortMessageTaskDAO;
import com.china.center.oa.note.manager.HandleMessage;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.bean.DepotpartBean;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.helper.StorageRelationHelper;
import com.china.center.oa.product.manager.StorageRelationManager;
import com.china.center.oa.product.wrap.ProductChangeWrap;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.NotifyBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.bean.UserBean;
import com.china.center.oa.publics.constant.SysConfigConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.publics.manager.FatalNotify;
import com.china.center.oa.publics.manager.NotifyManager;
import com.china.center.oa.sail.bean.BaseBalanceBean;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.ConsignBean;
import com.china.center.oa.sail.bean.OutBalanceBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.bean.OutUniqueBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.constanst.SailConstant;
import com.china.center.oa.sail.dao.BaseBalanceDAO;
import com.china.center.oa.sail.dao.BaseDAO;
import com.china.center.oa.sail.dao.ConsignDAO;
import com.china.center.oa.sail.dao.OutBalanceDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.dao.OutUniqueDAO;
import com.china.center.oa.sail.helper.OutHelper;
import com.china.center.oa.sail.helper.YYTools;
import com.china.center.oa.sail.listener.OutListener;
import com.china.center.oa.sail.manager.OutManager;
import com.china.center.oa.sail.vo.BaseBalanceVO;
import com.china.center.oa.sail.vo.OutVO;
import com.china.center.osgi.dym.DynamicBundleTools;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.ListTools;
import com.china.center.tools.MathTools;
import com.china.center.tools.ParamterMap;
import com.china.center.tools.RandomTools;
import com.china.center.tools.StringTools;
import com.china.center.tools.TimeTools;


/**
 * OutManagerImpl
 * 
 * @author ZHUZHU
 * @version 2010-11-7
 * @see OutManagerImpl
 * @since 1.0
 */
public class OutManagerImpl extends AbstractListenerManager<OutListener> implements OutManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private final Log importLog = LogFactory.getLog("sec");

    private LocationDAO locationDAO = null;

    private CommonDAO commonDAO = null;

    private ProductDAO productDAO = null;

    private UserDAO userDAO = null;

    private StafferDAO stafferDAO = null;

    private OutDAO outDAO = null;

    private DepotDAO depotDAO = null;

    private BaseDAO baseDAO = null;

    private ConsignDAO consignDAO = null;

    private CustomerDAO customerDAO = null;

    private ParameterDAO parameterDAO = null;

    private CreditLevelDAO creditLevelDAO = null;

    private DepotpartDAO depotpartDAO = null;

    private CreditCoreDAO creditCoreDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private OutUniqueDAO outUniqueDAO = null;

    private NotifyManager notifyManager = null;

    private FatalNotify fatalNotify = null;

    private BaseBalanceDAO baseBalanceDAO = null;

    private OutBalanceDAO outBalanceDAO = null;

    private StorageRelationManager storageRelationManager = null;

    /**
     * 短信最大停留时间
     */
    private int internal = 300000;

    private PlatformTransactionManager transactionManager = null;

    /**
     * default constructor
     */
    public OutManagerImpl()
    {
    }

    /**
     * 增加(修改)
     * 
     * @param locationBean
     * @return String 销售单的ID
     * @throws Exception
     */
    public String addOut(final OutBean outBean, final Map dataMap, final User user)
        throws MYException
    {
        ParamterMap request = new ParamterMap(dataMap);

        String fullId = request.getParameter("fullId");

        if (StringTools.isNullOrNone(fullId))
        {
            // 先保存
            String id = getAll(commonDAO.getSquence());

            LocationBean location = locationDAO.find(outBean.getLocationId());

            if (location == null)
            {
                _logger.error("区域不存在:" + outBean.getLocationId());

                throw new MYException("区域不存在:" + outBean.getLocationId());
            }

            String flag = location.getCode();

            String time = TimeTools.getStringByFormat(new Date(), "yyMMddHHmm");

            fullId = flag + time + id;

            outBean.setId(id);

            outBean.setFullId(fullId);

            dataMap.put("modify", false);
        }
        else
        {
            dataMap.put("modify", true);
        }

        final String totalss = request.getParameter("totalss");

        outBean.setTotal(MathTools.parseDouble(totalss));

        outBean.setStatus(OutConstant.STATUS_SAVE);

        outBean.setInway(OutConstant.IN_WAY_NO);

        // 获得baseList
        final String[] nameList = request.getParameter("nameList").split("~");
        final String[] idsList = request.getParameter("idsList").split("~");
        final String[] showIdList = request.getParameter("showIdList").split("~");
        final String[] showNameList = request.getParameter("showNameList").split("~");
        final String[] unitList = request.getParameter("unitList").split("~");
        final String[] amontList = request.getParameter("amontList").split("~");
        final String[] priceList = request.getParameter("priceList").split("~");
        final String[] totalList = request.getParameter("totalList").split("~");
        final String[] desList = (" " + request.getParameter("desList") + " ").split("~");
        final String[] otherList = request.getParameter("otherList").split("~");

        // 组织BaseBean
        double ttatol = 0.0d;
        for (int i = 0; i < nameList.length; i++ )
        {
            ttatol += (Double.parseDouble(priceList[i]) * Integer.parseInt(amontList[i]));
        }

        outBean.setTotal(ttatol);

        outBean.setCurcredit(0.0d);

        outBean.setStaffcredit(0.0d);

        if (StringTools.isNullOrNone(outBean.getCustomerId())
            || CustomerConstant.PUBLIC_CUSTOMER_ID.equals(outBean.getCustomerId()))
        {
            outBean.setCustomerId(CustomerConstant.PUBLIC_CUSTOMER_ID);

            outBean.setCustomerName(CustomerConstant.PUBLIC_CUSTOMER_NAME);
        }

        if (StringTools.isNullOrNone(outBean.getInvoiceId()))
        {
            outBean.setHasInvoice(OutConstant.HASINVOICE_NO);

            // 不要发票的时候默认已经开票
            outBean.setInvoiceStatus(OutConstant.INVOICESTATUS_END);
        }
        else
        {
            outBean.setHasInvoice(OutConstant.HASINVOICE_YES);
        }

        // 赠送的价格为0
        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
            && outBean.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
        {
            outBean.setTotal(0.0d);
        }

        // 增加管理员操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    if ((Boolean)dataMap.get("modify"))
                    {
                        outDAO.deleteEntityBean(outBean.getFullId());

                        baseDAO.deleteEntityBeansByFK(outBean.getFullId());
                    }

                    // 保存入库单
                    outDAO.saveEntityBean(outBean);

                    // 组织BaseBean
                    for (int i = 0; i < nameList.length; i++ )
                    {
                        BaseBean base = new BaseBean();

                        base.setId(commonDAO.getSquenceString());

                        base.setOutId(outBean.getFullId());

                        base.setProductId(idsList[i]);

                        if (StringTools.isNullOrNone(base.getProductId()))
                        {
                            throw new RuntimeException("产品ID为空,数据不完备");
                        }

                        // ele.productid + '-' + ele.price + '-' + ele.stafferid + '-' + ele.depotpartid
                        String[] coreList = otherList[i].split("-");

                        if (coreList.length != 4)
                        {
                            throw new RuntimeException("数据不完备");
                        }

                        base.setProductName(nameList[i]);
                        base.setUnit(unitList[i]);

                        // 赠送的价格为0
                        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL
                            && outBean.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
                        {
                            base.setPrice(0.0d);
                        }
                        else
                        {
                            base.setPrice(MathTools.parseDouble(priceList[i]));
                        }
                        base.setAmount(MathTools.parseInt(amontList[i]));
                        base.setValue(MathTools.parseDouble(totalList[i]));

                        // 入库单是没有showId的
                        if (showNameList != null && showNameList.length >= (i + 1))
                        {
                            base.setShowId(showIdList[i]);
                            base.setShowName(showNameList[i]);
                        }

                        // 寻找具体的产品价格位置
                        base.setCostPrice(MathTools.parseDouble(coreList[1]));
                        base
                            .setCostPriceKey(StorageRelationHelper.getPriceKey(base.getCostPrice()));
                        base.setOwner(coreList[2]);
                        base.setDepotpartId(coreList[3]);

                        DepotpartBean deport = depotpartDAO.find(base.getDepotpartId());

                        if (deport != null)
                        {
                            base.setDepotpartName(deport.getName());
                        }

                        base.setLocationId(outBean.getLocation());

                        base.setDescription(desList[i].trim());

                        // 增加单个产品到base表
                        baseDAO.saveEntityBean(base);
                    }

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException("系统错误，请联系管理员");
        }

        return fullId;
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public String coloneOutAndSubmitAffair(final OutBean outBean, final User user, int type)
        throws MYException
    {
        return coloneOutAndSubmitWithOutAffair(outBean, user, type);
    }

    public String coloneOutAndSubmitWithOutAffair(OutBean outBean, User user, int type)
        throws MYException
    {
        // 先保存
        String id = getAll(commonDAO.getSquence());

        LocationBean location = locationDAO.find(outBean.getLocationId());

        if (location == null)
        {
            _logger.error("区域不存在:" + outBean.getLocationId());

            throw new MYException("区域不存在:" + outBean.getLocationId());
        }

        String flag = location.getCode();

        String time = TimeTools.getStringByFormat(new Date(), "yyMMddHHmm");

        final String fullId = flag + time + id;

        outBean.setId(id);

        outBean.setFullId(fullId);

        // 保存库单
        outBean.setStatus(OutConstant.STATUS_SAVE);

        // 保存入库单
        outDAO.saveEntityBean(outBean);

        List<BaseBean> list = outBean.getBaseList();

        for (BaseBean baseBean : list)
        {
            baseBean.setId(commonDAO.getSquenceString());

            baseBean.setOutId(fullId);

            // 增加单个产品到base表
            baseDAO.saveEntityBean(baseBean);
        }

        // 提交
        this.submitWithOutAffair(fullId, user, type);

        return fullId;
    }

    /**
     * 提交
     * 
     * @param outBean
     * @param user
     * @return
     * @throws Exception
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public int submit(final String fullId, final User user, int storageType)
        throws MYException
    {
        return submitWithOutAffair(fullId, user, storageType);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.manager.OutManager#submitWithOutAffair(java.lang.String,
     *      com.center.china.osgi.publics.User)
     */
    public synchronized int submitWithOutAffair(final String fullId, final User user, int type)
        throws MYException
    {
        final OutBean outBean = outDAO.find(fullId);

        if (outBean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 检查日志核对
        int outStatusInLog = this.findOutStatusInLog(outBean.getFullId());

        if (outStatusInLog != -1 && outStatusInLog != OutConstant.STATUS_REJECT
            && outStatusInLog != outBean.getStatus())
        {
            String msg = "严重错误,当前单据的状态应该是:" + OutHelper.getStatus(outStatusInLog) + ",而不是"
                         + OutHelper.getStatus(outBean.getStatus()) + ".请联系管理员确认此单的正确状态!";

            loggerError(outBean.getFullId() + ":" + msg);

            throw new MYException(msg);
        }

        final List<BaseBean> baseList = checkSubmit(fullId, outBean);

        // 这里是入库单的直接库存变动
        processBaseList(user, outBean, baseList, type);

        // CORE 修改库单的状态(信用额度处理)
        int status = processOutStutus(fullId, user, outBean);

        // 处理在途
        int result = processOutInWay(user, fullId, outBean);

        // 在途改变状态
        if (result != -1)
        {
            status = result;
        }

        // 增加数据库日志
        addOutLog(fullId, user, outBean, "提交", SailConstant.OPR_OUT_PASS, status);

        outBean.setStatus(status);

        notifyOut(outBean, user, 0);

        return status;
    }

    /**
     * @param fullId
     * @param outBean
     * @throws MYException
     */
    private int processOutInWay(final User user, final String fullId, final OutBean outBean)
        throws MYException
    {
        int result = -1;

        // 如果是调入提交
        if (OutHelper.isMoveIn(outBean))
        {
            // 调入的库存(正数增加,负数减少)
            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

            String sequence = commonDAO.getSquenceString();

            // CORE 调入直接变动库存
            for (BaseBean element : baseList)
            {
                ProductChangeWrap wrap = new ProductChangeWrap();

                wrap.setDepotpartId(element.getDepotpartId());
                wrap.setPrice(element.getCostPrice());
                wrap.setProductId(element.getProductId());
                wrap.setStafferId(element.getOwner());
                wrap.setChange(element.getAmount());
                wrap.setDescription("库单[" + outBean.getFullId() + "]调入操作");
                wrap.setSerializeId(sequence);
                wrap.setType(StorageConstant.OPR_STORAGE_REDEPLOY);
                wrap.setRefId(outBean.getFullId());

                storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, true);
            }

            // 调入的存入唯一
            saveUnique(user, outBean);

            // 在途结束
            outDAO.updataInWay(fullId, OutConstant.IN_WAY_OVER);

            outDAO.modifyOutStatus(fullId, OutConstant.STATUS_PASS);

            result = OutConstant.STATUS_PASS;

            // -----------------------------------------------------------------//

            // 处理调出变动库存
            String moveOutFullId = outBean.getRefOutFullId();

            OutBean moveOut = outDAO.find(moveOutFullId);

            baseList = baseDAO.queryEntityBeansByFK(moveOutFullId);

            // CORE 调出直接变动库存
            for (BaseBean element : baseList)
            {
                ProductChangeWrap wrap = new ProductChangeWrap();

                wrap.setDepotpartId(element.getDepotpartId());
                wrap.setPrice(element.getCostPrice());
                wrap.setProductId(element.getProductId());
                wrap.setStafferId(element.getOwner());
                wrap.setChange(element.getAmount());
                wrap.setDescription("库单[" + moveOut.getFullId() + "]调出操作");
                wrap.setSerializeId(sequence);
                wrap.setType(StorageConstant.OPR_STORAGE_REDEPLOY);
                wrap.setRefId(moveOut.getFullId());

                storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, true);
            }

            // 调入的存入唯一
            saveUnique(user, moveOut);

            // 结束调出的单据
            changeMoveOutToEnd(user, moveOut);
        }

        // 如果是调入
        if (OutHelper.isMoveOut(outBean))
        {
            outDAO.updataInWay(fullId, OutConstant.IN_WAY);

            importLog.info(fullId + "的在途状态改变成在途");
        }

        return result;
    }

    /**
     * 结束调出的单据
     * 
     * @param user
     * @param moveOut
     */
    private void changeMoveOutToEnd(final User user, OutBean moveOut)
    {
        outDAO.updataInWay(moveOut.getFullId(), OutConstant.IN_WAY_OVER);

        outDAO.modifyOutStatus(moveOut.getFullId(), OutConstant.STATUS_PASS);

        // 操作日志
        addOutLog(moveOut.getFullId(), user, moveOut, "对方调入后,自动发货结束", SailConstant.OPR_OUT_PASS,
            OutConstant.STATUS_PASS);

        importLog.info(moveOut.getFullId() + "的在途状态改变成在途结束");
    }

    /**
     * @param fullId
     * @param user
     * @param outBean
     * @throws MYException
     */
    private int processOutStutus(final String fullId, final User user, final OutBean outBean)
        throws MYException
    {
        int result = 0;

        int nextStatus = OutConstant.STATUS_SUBMIT;

        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            importLog.info(fullId + ":" + user.getStafferName() + ":" + 1 + ":redrectFrom:"
                           + outBean.getStatus());

            // 销售单处理
            try
            {
                // 分公司经理担保
                if (outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_LOCATION_MANAGER)
                {
                    nextStatus = OutConstant.STATUS_LOCATION_MANAGER_CHECK;
                }

                // 赠送流程
                if (outBean.getOutType() == OutConstant.OUTTYPE_OUT_PRESENT)
                {
                    nextStatus = OutConstant.STATUS_CEO_CHECK;
                }

                outDAO.modifyOutStatus(fullId, nextStatus);

                result = nextStatus;

                // 只有销售单才有信用(但是个人领样没有客户,就是公共客户)
                boolean isCreditOutOf = false;

                // 这里需要计算客户的信用金额-是否报送物流中心经理审批
                boolean outCredit = parameterDAO.getBoolean(SysConfigConstant.OUT_CREDIT);

                CustomerBean cbean = customerDAO.find(outBean.getCustomerId());

                if (cbean == null)
                {
                    throw new MYException("客户不存在,请确认操作");
                }

                // query customer credit
                CreditLevelBean clevel = creditLevelDAO.find(cbean.getCreditLevelId());

                if (clevel == null)
                {
                    throw new MYException("客户信用等级不存在");
                }

                // 进行逻辑处理(必须是货到收款才能有此逻辑) 此逻辑已经废除
                if (outCredit && cbean != null
                    && !StringTools.isNullOrNone(cbean.getCreditLevelId())
                    && outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_COMMON)
                {
                    throw new MYException("不支持此类型,请重新操作");
                }

                // 使用业务员的信用额度(或者是分公司经理的)
                if (outCredit
                    && (outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_CREDIT_AND_CUR || outBean
                        .getReserve3() == OutConstant.OUT_SAIL_TYPE_LOCATION_MANAGER))
                {
                    StafferBean sb2 = stafferDAO.find(outBean.getStafferId());

                    if (sb2 == null)
                    {
                        throw new MYException("数据不完备,请重新操作");
                    }

                    // 先清空预占金额,主要是统计的时候方便
                    outDAO.updateCurcredit(fullId, 0.0d);

                    outDAO.updateStaffcredit(fullId, 0.0d);

                    outDAO.updateManagercredit(fullId, "", 0.0d);

                    double noPayBusinessInCur = outDAO.sumNoPayBusiness(outBean.getCustomerId(),
                        YYTools.getFinanceBeginDate(), YYTools.getFinanceEndDate());

                    // 自己担保的+替人担保的
                    double noPayBusiness = outDAO.sumAllNoPayAndAvouchBusinessByStafferId(outBean
                        .getStafferId(), YYTools.getStatBeginDate(), YYTools.getStatEndDate());

                    double remainInCur = clevel.getMoney() - noPayBusinessInCur;

                    // 不是公共客户
                    if ( !cbean.getId().equals(CustomerConstant.PUBLIC_CUSTOMER_ID))
                    {
                        if (remainInCur < 0)
                        {
                            remainInCur = 0.0;
                        }

                        // 先客户信用 然后职员信用(信用*杠杆) 最后分公司经理
                        if (remainInCur >= outBean.getTotal())
                        {
                            outDAO.updateCurcredit(fullId, outBean.getTotal());

                            outDAO.updateStaffcredit(fullId, 0.0d);

                            outBean.setReserve6("客户信用最大额度是:"
                                                + MathTools.formatNum(clevel.getMoney())
                                                + ".当前客户未付款金额(不包括此单):"
                                                + MathTools.formatNum(noPayBusinessInCur)
                                                + ".职员信用额度是:"
                                                + MathTools.formatNum(sb2.getCredit()
                                                                      * sb2.getLever())
                                                + ".职员信用已经使用额度是:"
                                                + MathTools.formatNum(noPayBusiness)
                                                + ".信用未超支,不需要分公司经理担保");
                        }
                    }

                    // 职员杠杆后的信用
                    double staffCredit = sb2.getCredit() * sb2.getLever();

                    // 一半使用客户,一半使用职员的(且不是公共客户的)
                    if (remainInCur < outBean.getTotal()
                        && !cbean.getId().equals(CustomerConstant.PUBLIC_CUSTOMER_ID))
                    {
                        // 全部使用客户的信用等级
                        outDAO.updateCurcredit(fullId, remainInCur);

                        // 当前单据需要使用的职员信用额度
                        double remainInStaff = outBean.getTotal() - remainInCur;

                        // 防止职员信用等级超支
                        if ( (noPayBusiness + remainInStaff) > staffCredit)
                        {
                            double lastNeed = (noPayBusiness + remainInStaff) - staffCredit;

                            outBean.setReserve6("客户信用最大额度是:"
                                                + MathTools.formatNum(clevel.getMoney())
                                                + ".当前客户未付款金额(不包括此单):"
                                                + MathTools.formatNum(noPayBusinessInCur)
                                                + ".职员信用额度是:" + MathTools.formatNum(staffCredit)
                                                + ".职员信用已经使用额度是:"
                                                + MathTools.formatNum(noPayBusiness)
                                                + ".信用超支(包括此单):" + (MathTools.formatNum(lastNeed)));

                            // 这里如果不使用分公司经理直接不允许提交此单据
                            if (outBean.getReserve3() != OutConstant.OUT_SAIL_TYPE_LOCATION_MANAGER)
                            {
                                throw new MYException(outBean.getReserve6());
                            }

                            isCreditOutOf = true;

                            outDAO.updateOutReserve(fullId, OutConstant.OUT_CREDIT_OVER, outBean
                                .getReserve6());

                            // 把剩余的信用全部给此单据
                            outDAO.updateStaffcredit(fullId, (staffCredit - noPayBusiness));
                        }
                        else
                        {
                            // 这里完全使用职员的信用
                            outDAO.updateStaffcredit(fullId, remainInStaff);
                        }
                    }

                    // 公共客户信用处理
                    if (cbean.getId().equals(CustomerConstant.PUBLIC_CUSTOMER_ID))
                    {
                        // 当前单据需要使用的职员信用额度
                        double remainInStaff = outBean.getTotal();

                        // 防止职员信用等级超支
                        if ( (noPayBusiness + remainInStaff) > staffCredit)
                        {
                            double lastNeed = (noPayBusiness + remainInStaff) - staffCredit;

                            outBean.setReserve6("职员信用额度是:" + MathTools.formatNum(staffCredit)
                                                + ".职员信用已经使用额度是:"
                                                + MathTools.formatNum(noPayBusiness)
                                                + ".信用超支(包括此单):" + (MathTools.formatNum(lastNeed)));

                            // 这里如果不使用分公司经理直接不允许提交此单据
                            if (outBean.getReserve3() != OutConstant.OUT_SAIL_TYPE_LOCATION_MANAGER)
                            {
                                throw new MYException(outBean.getReserve6());
                            }

                            isCreditOutOf = true;

                            outDAO.updateOutReserve(fullId, OutConstant.OUT_CREDIT_OVER, outBean
                                .getReserve6());

                            // 把剩余的信用全部给此单据
                            outDAO.updateStaffcredit(fullId, (staffCredit - noPayBusiness));
                        }
                        else
                        {
                            // 这里完全使用职员的信用
                            outDAO.updateStaffcredit(fullId, remainInStaff);
                        }
                    }
                }

                // 信用没有受限检查产品价格是否为0
                if ( !isCreditOutOf)
                {
                    outDAO.updateOutReserve(fullId, OutConstant.OUT_CREDIT_COMMON, outBean
                        .getReserve6());
                }

                // 修改人工干预,重新置人工干预信用为0
                String pid = "90000000000000009999";

                creditCoreDAO.updateCurCreToInit(pid, outBean.getCustomerId());
            }
            catch (Exception e)
            {
                _logger.error(e, e);

                throw new MYException(e);
            }
        }
        else
        {
            nextStatus = OutConstant.STATUS_PASS;

            // 采购入库直接就是库管通过结束
            if (outBean.getOutType() == OutConstant.OUTTYPE_IN_COMMON)
            {
                nextStatus = OutConstant.STATUS_PASS;
            }
            // 领样直接通过
            else if (outBean.getOutType() == OutConstant.OUTTYPE_IN_SWATCH)
            {
                nextStatus = OutConstant.STATUS_PASS;
            }
            // 调拨直接是submit
            else if (outBean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT)
            {
                nextStatus = OutConstant.STATUS_SUBMIT;
            }
            // 其他直接是待分公司经理审核
            else
            {
                nextStatus = OutConstant.STATUS_LOCATION_MANAGER_CHECK;
            }

            try
            {
                outDAO.modifyOutStatus(fullId, nextStatus);

                result = nextStatus;
            }
            catch (Exception e)
            {
                throw new MYException(e.toString());
            }

            // 入库直接通过
            importLog.info(fullId + ":" + user.getStafferName() + ":" + nextStatus
                           + ":redrectFrom:" + outBean.getStatus());
        }

        return result;
    }

    /**
     * 处理base入库
     * 
     * @param user
     * @param outBean
     * @param baseList
     * @throws MYException
     */
    private void processBaseList(final User user, final OutBean outBean,
                                 final List<BaseBean> baseList, int type)
        throws MYException
    {
        // 入库单提交后就直接移动库存了,销售需要在库管通过后生成发货单前才会变动库存
        if (outBean.getType() != OutConstant.OUT_TYPE_INBILL)
        {
            return;
        }

        // 处理入库单的库存变动 采购入库/领样退货
        if (outBean.getOutType() == OutConstant.OUTTYPE_IN_COMMON
            || outBean.getOutType() == OutConstant.OUTTYPE_IN_SWATCH)
        {
            String sequence = commonDAO.getSquenceString();

            for (BaseBean element : baseList)
            {
                ProductChangeWrap wrap = new ProductChangeWrap();

                wrap.setDepotpartId(element.getDepotpartId());
                wrap.setPrice(element.getCostPrice());
                wrap.setProductId(element.getProductId());
                wrap.setStafferId(element.getOwner());
                wrap.setChange(element.getAmount());
                wrap.setDescription("库单[" + outBean.getFullId() + "]操作");
                wrap.setSerializeId(sequence);
                wrap.setType(type);
                wrap.setRefId(outBean.getFullId());

                storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, true);
            }

            saveUnique(user, outBean);
        }
    }

    /**
     * 变动库存的时候插入唯一的值,保证库存只变动一次
     * 
     * @param user
     * @param outBean
     */
    private void saveUnique(final User user, final OutBean outBean)
    {
        OutUniqueBean unique = new OutUniqueBean();

        unique.setId(outBean.getFullId());

        unique.setRef(user.getStafferName());

        unique.setLogTime(TimeTools.now());

        outUniqueDAO.saveEntityBean(unique);
    }

    /**
     * 检查submit的准备
     * 
     * @param fullId
     * @param outBean
     * @return
     * @throws MYException
     */
    private List<BaseBean> checkSubmit(final String fullId, final OutBean outBean)
        throws MYException
    {
        if (outBean == null)
        {
            throw new MYException(fullId + " 不存在");
        }

        if ( !OutHelper.canSubmit(outBean))
        {
            throw new MYException(fullId + " 状态错误,不能提交");
        }

        final List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

        // 先检查入库
        for (BaseBean element : baseList)
        {
            ProductChangeWrap wrap = new ProductChangeWrap();

            wrap.setDepotpartId(element.getDepotpartId());
            wrap.setPrice(element.getCostPrice());
            wrap.setProductId(element.getProductId());
            wrap.setStafferId(element.getOwner());
            wrap.setRefId(fullId);

            // 销售单
            if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
            {
                wrap.setChange( -element.getAmount());

                storageRelationManager.checkStorageRelation(wrap);
            }
            else
            {
                // 入库单
                wrap.setChange(element.getAmount());

                storageRelationManager.checkStorageRelation(wrap);
            }
        }

        // 如果是入库的调入，验证是否在途
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && outBean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT
            && outBean.getReserve1() == OutConstant.MOVEOUT_OUT)
        {
            OutBean out = outDAO.find(fullId);

            if (out == null)
            {
                throw new MYException("选择调出的库单不存在，请重新操作选择调出的库单");
            }
        }

        // 如果是入库的调入，验证是否在途
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL
            && outBean.getOutType() == OutConstant.OUTTYPE_IN_MOVEOUT
            && outBean.getReserve1() == OutConstant.MOVEOUT_IN)
        {
            String ofullid = outBean.getRefOutFullId();

            if (StringTools.isNullOrNone(ofullid))
            {
                throw new MYException("由于是调入的库单需要调出的库单对应，请重新操作选择调出的库单");
            }

            OutBean moveOut = outDAO.find(ofullid);

            if (moveOut == null)
            {
                throw new MYException("选择调出的库单不存在，请重新操作选择调出的库单");
            }

            if (moveOut.getInway() != OutConstant.IN_WAY)
            {
                throw new MYException("选择调出的库单不是在途中，请确认");
            }
        }

        return baseList;
    }

    /**
     * 驳回(只有销售单和入库单)
     * 
     * @param outBean
     * @param user
     * @return
     * @throws Exception
     */
    public synchronized int reject(final String fullId, final User user, final String reason)
        throws MYException
    {
        final OutBean outBean = outDAO.find(fullId);

        if (outBean == null)
        {
            throw new MYException("销售单不存在，请重新操作");
        }

        final List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

        // 仓库
        final String locationId = outBean.getLocation();

        doReject(fullId, user, reason, outBean, baseList, locationId);

        return OutConstant.STATUS_REJECT;

    }

    /**
     * doReject
     * 
     * @param fullId
     * @param user
     * @param reason
     * @param outBean
     * @param baseList
     * @param deportId
     * @throws MYException
     */
    private void doReject(final String fullId, final User user, final String reason,
                          final OutBean outBean, final List<BaseBean> baseList,
                          final String deportId)
        throws MYException
    {
        checkReject(outBean, baseList, deportId);

        // 入库操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    // OSGI 驳回监听实现
                    Collection<OutListener> listenerMapValues = listenerMapValues();

                    for (OutListener listener : listenerMapValues)
                    {
                        try
                        {
                            listener.onReject(user, outBean);
                        }
                        catch (MYException e)
                        {
                            throw new RuntimeException(e.getErrorContent());
                        }
                    }

                    // 如果销售单，需要删除发货单(当库管驳回的时候才触发)
                    if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
                    {
                        ConsignBean beans = consignDAO.findConsignById(fullId);

                        if (beans != null)
                        {
                            consignDAO.delConsign(fullId);
                        }
                    }

                    importLog.info(fullId + ":" + user.getStafferName() + ":"
                                   + OutConstant.STATUS_REJECT + ":redrectFrom:"
                                   + outBean.getStatus());

                    outDAO.modifyOutStatus(outBean.getFullId(), OutConstant.STATUS_REJECT);

                    // 驳回修改在途方式
                    outDAO.updataInWay(fullId, OutConstant.IN_WAY_NO);

                    // 变成没有付款
                    outDAO.modifyPay(fullId, OutConstant.PAY_NOT);

                    // 操作日志
                    addOutLog(fullId, user, outBean, reason, SailConstant.OPR_OUT_REJECT,
                        OutConstant.STATUS_REJECT);

                    notifyOut(outBean, user, 1);

                    return Boolean.TRUE;
                }

            });
        }
        catch (TransactionException e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
            _logger.error("增加库单错误：", e);
            throw new MYException("系统错误,请重新操作");
        }
    }

    /**
     * 处理驳回的验证(驳回不涉及库存的移动)
     * 
     * @param outBean
     * @param baseList
     * @param locationId
     * @throws MYException
     */
    private void checkReject(final OutBean outBean, final List<BaseBean> baseList,
                             final String deportId)
        throws MYException
    {
        if (outBean == null)
        {
            throw new MYException("销售单不存在，请重新操作");
        }

        if ( !OutHelper.canReject(outBean))
        {
            throw new MYException("状态不可以驳回!");
        }
    }

    /**
     * CORE 审核通过(这里只有销售单才有此操作)分公司经理审核/结算中心/物流审批/库管发货
     * 
     * @param outBean
     * @param user
     * @param depotpartId
     *            废弃
     * @return
     * @throws Exception
     */
    public synchronized int pass(final String fullId, final User user, final int nextStatus,
                                 final String reason, final String depotpartId)
        throws MYException
    {
        final OutBean outBean = outDAO.find(fullId);

        checkPass(outBean);

        final int oldStatus = outBean.getStatus();

        final DepotBean depot = checkDepotInPass(nextStatus, outBean);

        // 入库操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    int newNextStatus = nextStatus;

                    if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
                    {
                        // 直接把结算中心通过的设置成物流管理员通过(跳过物流)
                        if (newNextStatus == OutConstant.STATUS_MANAGER_PASS
                            && depot.getType() == DepotConstant.DEPOT_TYPE_LOCATION)
                        {
                            newNextStatus = OutConstant.STATUS_FLOW_PASS;
                        }
                    }

                    importLog.info(outBean.getFullId() + ":" + user.getStafferName() + ":"
                                   + newNextStatus + ":redrectFrom:" + oldStatus);

                    // 修改状态
                    outDAO.modifyOutStatus(outBean.getFullId(), newNextStatus);

                    if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
                    {
                        handerPassOut(fullId, user, outBean, depot, newNextStatus);
                    }
                    else
                    {
                        handerPassBuy(fullId, user, outBean, newNextStatus);
                    }

                    addOutLog(fullId, user, outBean, reason, SailConstant.OPR_OUT_PASS,
                        newNextStatus);

                    // 把状态放到最新的out里面
                    outBean.setStatus(newNextStatus);

                    // OSGI 驳回监听实现
                    Collection<OutListener> listenerMapValues = listenerMapValues();

                    for (OutListener listener : listenerMapValues)
                    {
                        try
                        {
                            listener.onPass(user, outBean);
                        }
                        catch (MYException e)
                        {
                            throw new RuntimeException(e.getErrorContent());
                        }
                    }

                    notifyOut(outBean, user, 0);

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error(e, e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error(e, e);
            throw new MYException("数据库内部错误");
        }
        catch (Exception e)
        {
            _logger.error(e, e);
            throw new MYException("处理异常:" + e.getMessage());
        }

        // 更新后的状态
        return outBean.getStatus();

    }

    /**
     * checkDepotInPass
     * 
     * @param nextStatus
     * @param outBean
     * @return
     * @throws MYException
     */
    private DepotBean checkDepotInPass(final int nextStatus, final OutBean outBean)
        throws MYException
    {
        final DepotBean depot = depotDAO.find(outBean.getLocation());

        if (depot == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 需要发货单先通过(只有中心仓库才有物流哦)
        if (nextStatus == OutConstant.STATUS_FLOW_PASS
            && depot.getType() == DepotConstant.DEPOT_TYPE_CENTER)
        {
            ConsignBean consignBean = consignDAO.findConsignById(outBean.getFullId());

            if (consignBean == null)
            {
                throw new MYException("没有发货单,请重新操作!");
            }

            if (consignBean.getCurrentStatus() == SailConstant.CONSIGN_INIT)
            {
                throw new MYException("发货单没有审批通过，请先处理发货单");
            }
        }
        return depot;
    }

    /**
     * checkPass
     * 
     * @param outBean
     * @throws MYException
     */
    private void checkPass(final OutBean outBean)
        throws MYException
    {
        final int oldStatus = outBean.getStatus();

        if (outBean == null)
        {
            throw new MYException("销售单不存在，请重新操作");
        }

        // 检查pass的条件
        if (outBean.getStatus() == OutConstant.STATUS_SAVE
            || outBean.getStatus() == OutConstant.STATUS_REJECT)
        {
            throw new MYException("状态不可以通过!");
        }

        // 检查日志核对
        int outStatusInLog = this.findOutStatusInLog(outBean.getFullId());

        if (outStatusInLog != -1 && outStatusInLog != oldStatus)
        {
            String msg = "严重错误,当前单据的状态应该是:" + OutHelper.getStatus(outStatusInLog) + ",而不是"
                         + OutHelper.getStatus(oldStatus) + ".请联系管理员确认此单的正确状态!";

            loggerError(outBean.getFullId() + ":" + msg);

            throw new MYException(msg);
        }
    }

    /**
     * CORE 销售单变动库存
     * 
     * @param user
     * @param outBean
     * @param baseList
     * @param logList
     * @throws MYException
     */
    private void processPass(final User user, final OutBean outBean, final List<BaseBean> baseList,
                             int type)
        throws MYException
    {
        String sequence = commonDAO.getSquenceString();

        for (BaseBean element : baseList)
        {
            ProductChangeWrap wrap = new ProductChangeWrap();

            wrap.setDepotpartId(element.getDepotpartId());
            wrap.setPrice(element.getCostPrice());
            wrap.setProductId(element.getProductId());
            wrap.setStafferId(element.getOwner());
            wrap.setRefId(outBean.getFullId());

            if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
            {
                // 这里是销售单,所以是负数
                wrap.setChange( -element.getAmount());

                wrap.setDescription("销售单[" + outBean.getFullId() + "]库管通过操作");
            }
            else
            {
                // 这里是入库单
                wrap.setChange(element.getAmount());

                wrap.setDescription("入库单[" + outBean.getFullId() + "]库管通过操作");
            }

            wrap.setSerializeId(sequence);

            wrap.setType(type);

            storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, true);
        }

        saveUnique(user, outBean);
    }

    /**
     * CORE (销售单的终结)财务收款
     * 
     * @param outBean
     * @param user
     * @return
     * @throws Exception
     */
    public boolean check(final String fullId, final User user, final String checks)
        throws MYException
    {
        final OutBean outBean = outDAO.find(fullId);

        if (outBean == null)
        {
            throw new MYException("销售单不存在，请重新操作");
        }

        if (outBean.getStatus() != OutConstant.STATUS_PASS)
        {
            throw new MYException("销售单不在库管通过状态，不能核对");
        }

        // 入库操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    outDAO.modifyChecks(fullId, checks);

                    outDAO.modifyOutStatus(fullId, OutConstant.STATUS_SEC_PASS);

                    // TODO_OSGI 核对此销售单的应收,看看是否正确结余(然后写到应收字段里面)

                    addOutLog(fullId, user, outBean, "核对", SailConstant.OPR_OUT_PASS,
                        OutConstant.STATUS_SEC_PASS);

                    notifyOut(outBean, user, 3);

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error("核对库单错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("核对库单错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
            _logger.error("核对库单错误：", e);
            throw new MYException("系统错误,请重新操作");
        }

        return true;

    }

    public OutBean findOutById(final String fullId)
    {
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            return null;
        }

        out.setBaseList(baseDAO.queryEntityBeansByFK(fullId));

        return out;
    }

    public OutVO findOutVOById(final String fullId)
    {
        OutVO out = outDAO.findVO(fullId);

        if (out == null)
        {
            return null;
        }

        out.setBaseList(baseDAO.queryEntityBeansByFK(fullId));

        return out;
    }

    /**
     * 删除库单
     * 
     * @param fullId
     * @return
     */
    public boolean delOut(final String fullId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(fullId);

        OutBean outBean = outDAO.find(fullId);

        if (outBean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !OutHelper.canDelete(outBean))
        {
            throw new MYException("单据不能被删除,请确认操作");
        }

        // 入库操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    outDAO.deleteEntityBean(fullId);

                    baseDAO.deleteEntityBeansByFK(fullId);

                    // 删除审批记录
                    flowLogDAO.deleteEntityBeansByFK(fullId);

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error("删除库单错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("删除库单错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
            _logger.error("删除库单错误：", e);
            throw new MYException("系统错误,请重新操作");
        }

        return true;
    }

    /**
     * 更新库单(但是不包括状态)
     * 
     * @param fullId
     * @return
     */
    public boolean updateOut(final OutBean out)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(out);

        JudgeTools.judgeParameterIsNull(out.getFullId());

        final OutBean oldBean = outDAO.find(out.getFullId());

        out.setStatus(oldBean.getStatus());

        // 入库操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    outDAO.updateEntityBean(out);

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error("修改库单错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("修改库单错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
            _logger.error("修改库单错误：", e);
            throw new MYException("系统错误,请重新操作");
        }

        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.manager.OutManager#modifyPay(com.center.china.osgi.publics.User, java.lang.String,
     *      int, java.lang.String)
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean payOut(final User user, String fullId, String reason)
        throws MYException
    {
        // 需要增加是否超期 flowId
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            return false;
        }

        // 如果getRedate为空说明已经超前回款了
        if ( !StringTools.isNullOrNone(out.getRedate()))
        {
            int delay = TimeTools.cdate(TimeTools.now(), out.getRedate());

            if (delay > 0)
            {
                outDAO.modifyTempType(fullId, delay);
            }
            else
            {
                outDAO.modifyTempType(fullId, 0);
            }
        }

        if (out.getType() == OutConstant.OUT_TYPE_OUTBILL
            && out.getOutType() == OutConstant.OUTTYPE_OUT_SWATCH)
        {
            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

            List<OutBean> refBuyList = queryRefOut(fullId);

            // 计算出已经退货的数量
            for (BaseBean baseBean : baseList)
            {
                int hasBack = 0;

                for (OutBean ref : refBuyList)
                {
                    List<BaseBean> refBaseList = ref.getBaseList();

                    for (BaseBean refBase : refBaseList)
                    {
                        if (refBase.equals(baseBean))
                        {
                            hasBack += refBase.getAmount();

                            break;
                        }
                    }
                }

                baseBean.setInway(hasBack);
            }

            for (BaseBean baseBean : baseList)
            {
                if (baseBean.getInway() != baseBean.getAmount())
                {
                    throw new MYException(baseBean.getProductName() + "没有全部退库");
                }
            }
        }
        else
        {
            Collection<OutListener> listenerMapValues = this.listenerMapValues();

            // 从监听获得是否回款
            for (OutListener outListener : listenerMapValues)
            {
                outListener.onHadPay(user, out);
            }
        }

        // 付款的金额
        // outDAO.modifyOutHadPay(fullId, out.getTotal() - out.getBadDebts());

        addOutLog(fullId, user, out, reason, SailConstant.OPR_OUT_PASS, out.getStatus());

        notifyOut(out, user, 2);

        // 修改付款标识
        return outDAO.modifyPay(fullId, OutConstant.PAY_YES);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.manager.OutManager#payBaddebts(com.center.china.osgi.publics.User,
     *      java.lang.String, double)
     */
    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean payBaddebts(final User user, String fullId, double bad)
        throws MYException
    {
        // 需要增加是否超期 flowId
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            return false;
        }

        if (out.getStatus() != OutConstant.STATUS_PASS)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (out.getHadPay() + bad > out.getTotal())
        {
            throw new MYException("坏账金额过多,请确认操作");
        }

        // 付款的金额
        outDAO.modifyBadDebts(fullId, bad);

        return true;
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean initPayOut(final User user, String fullId)
        throws MYException
    {
        // 需要增加是否超期 flowId
        OutBean out = outDAO.find(fullId);

        if (out == null)
        {
            return false;
        }

        if (out.getStatus() != OutConstant.STATUS_SEC_PASS)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (out.getPay() != OutConstant.PAY_YES)
        {
            throw new MYException("数据错误,请确认操作");
        }

        outDAO.modifyOutStatus(fullId, OutConstant.STATUS_PASS);

        addOutLog(fullId, user, out, "坏账消除", SailConstant.OPR_OUT_REJECT, OutConstant.STATUS_PASS);

        outDAO.modifyBadDebts(fullId, 0.0d);

        // 修改付款标识
        return outDAO.modifyPay(fullId, OutConstant.PAY_NOT);
    }

    private List<OutBean> queryRefOut(String outId)
    {
        // 查询当前已经有多少个人领样
        ConditionParse con = new ConditionParse();

        con.addWhereStr();

        con.addCondition("OutBean.refOutFullId", "=", outId);

        con.addCondition(" and OutBean.status in (3, 4)");

        con.addIntCondition("OutBean.type", "=", OutConstant.OUT_TYPE_INBILL);

        List<OutBean> refBuyList = outDAO.queryEntityBeansByCondition(con);

        for (OutBean outBean : refBuyList)
        {
            List<BaseBean> list = baseDAO.queryEntityBeansByFK(outBean.getFullId());

            outBean.setBaseList(list);
        }

        return refBuyList;
    }

    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean mark(String fullId, boolean status)
    {
        return outDAO.mark(fullId, status);
    }

    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean modifyReDate(String fullId, String reDate)
    {
        return outDAO.modifyReDate(fullId, reDate);
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean addOutBalance(final User user, OutBalanceBean bean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, bean, bean.getBaseBalanceList());

        OutBean out = outDAO.find(bean.getOutId());

        if (out == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !user.getStafferId().equals(out.getStafferId()))
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (out.getType() != OutConstant.OUT_TYPE_OUTBILL
            || out.getOutType() != OutConstant.OUTTYPE_OUT_CONSIGN)
        {
            throw new MYException("不是委托代销的销售单,请确认操作");
        }

        if (out.getStatus() != OutConstant.STATUS_PASS)
        {
            throw new MYException("数据错误,请确认操作");
        }

        bean.setId(commonDAO.getSquenceString20());

        outBalanceDAO.saveEntityBean(bean);

        List<BaseBalanceBean> baseBalanceList = bean.getBaseBalanceList();

        for (BaseBalanceBean baseBalanceBean : baseBalanceList)
        {
            baseBalanceBean.setId(commonDAO.getSquenceString20());

            baseBalanceBean.setParentId(bean.getId());

            baseBalanceBean.setOutId(bean.getOutId());
        }

        baseBalanceDAO.saveAllEntityBeans(baseBalanceList);

        return true;
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean passOutBalance(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBalanceBean bean = checkBalancePass(id);

        bean.setStatus(OutConstant.OUTBALANCE_STATUS_PASS);

        outBalanceDAO.updateEntityBean(bean);

        OutBean out = outDAO.find(bean.getOutId());

        if (out == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        boolean useDefaultDepotpart = true;

        DepotpartBean defaultOKDepotpart = null;

        if (bean.getType() == OutConstant.OUTBALANCE_TYPE_BACK
            && !out.getLocation().equals(bean.getDirDepot()))
        {
            useDefaultDepotpart = false;

            defaultOKDepotpart = depotpartDAO.findDefaultOKDepotpart(bean.getDirDepot());

            if (defaultOKDepotpart == null)
            {
                throw new MYException("默认仓区不存在,请确认操作");
            }
        }

        List<BaseBalanceBean> baseList = baseBalanceDAO.queryEntityBeansByFK(id);

        String sequence = commonDAO.getSquenceString();

        // 退货单是要变动库存的
        if (bean.getType() == OutConstant.OUTBALANCE_TYPE_BACK)
        {
            // 这里需要变动库存(增加库存)
            for (BaseBalanceBean each : baseList)
            {
                ProductChangeWrap wrap = new ProductChangeWrap();

                BaseBean element = baseDAO.find(each.getBaseId());

                if (useDefaultDepotpart)
                {
                    // 使用默认仓区
                    wrap.setDepotpartId(element.getDepotpartId());
                }
                else
                {
                    // 使用用户选择的仓库
                    wrap.setDepotpartId(defaultOKDepotpart.getId());
                }
                wrap.setPrice(element.getCostPrice());
                wrap.setProductId(element.getProductId());
                wrap.setStafferId(element.getOwner());

                // 增加的数量来自退货的数量
                wrap.setChange(each.getAmount());

                wrap.setDescription("库单[" + bean.getOutId() + "]代销退货操作");
                wrap.setSerializeId(sequence);
                wrap.setType(StorageConstant.OPR_STORAGE_BALANCE);
                wrap.setRefId(id);

                storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, true);
            }
        }

        notifyManager.notifyMessage(bean.getStafferId(), bean.getOutId() + "的结算清单已经被["
                                                         + user.getStafferName() + "]通过");

        return true;
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean deleteOutBalance(User user, String id)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBalanceBean bean = outBalanceDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getStatus() != OutConstant.OUTBALANCE_STATUS_REJECT)
        {
            throw new MYException("数据错误,请确认操作");
        }

        outBalanceDAO.deleteEntityBean(id);

        return true;
    }

    /**
     * checkBalancePass
     * 
     * @param id
     * @return
     * @throws MYException
     */
    private OutBalanceBean checkBalancePass(String id)
        throws MYException
    {
        OutBalanceBean bean = outBalanceDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getStatus() != OutConstant.OUTBALANCE_STATUS_SUBMIT)
        {
            throw new MYException("数据错误,请确认操作");
        }

        checkBalanceOver(id);

        return bean;
    }

    private void checkBalanceOver(String id)
        throws MYException
    {
        // 检查是否结算溢出
        List<BaseBalanceBean> currentBaseList = baseBalanceDAO.queryEntityBeansByFK(id);

        // 看看是否溢出了
        for (BaseBalanceBean baseBalanceBean : currentBaseList)
        {
            BaseBean baseBean = baseDAO.find(baseBalanceBean.getBaseId());

            int total = baseBalanceBean.getAmount();
            List<BaseBalanceVO> hasPassBaseList = baseBalanceDAO
                .queryPassBaseBalance(baseBalanceBean.getBaseId());

            for (BaseBalanceBean pss : hasPassBaseList)
            {
                total += pss.getAmount();
            }

            if (total > baseBean.getAmount())
            {
                throw new MYException(baseBean.getProductName() + "的数量溢出");
            }
        }
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean rejectOutBalance(User user, String id, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, id);

        OutBalanceBean bean = outBalanceDAO.find(id);

        if (bean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (bean.getStatus() != OutConstant.OUTBALANCE_STATUS_SUBMIT)
        {
            throw new MYException("数据错误,请确认操作");
        }

        bean.setStatus(OutConstant.OUTBALANCE_STATUS_REJECT);

        bean.setReason(reason);

        outBalanceDAO.updateEntityBean(bean);

        notifyManager.notifyMessage(bean.getStafferId(), bean.getOutId() + "的结算清单已经被["
                                                         + user.getStafferName() + "]驳回");

        return true;
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
     * @return the commonDAO
     */
    public CommonDAO getCommonDAO()
    {
        return commonDAO;
    }

    /**
     * @return the productDAO
     */
    public ProductDAO getProductDAO()
    {
        return productDAO;
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
     * @param productDAO
     *            the productDAO to set
     */
    public void setProductDAO(ProductDAO productDAO)
    {
        this.productDAO = productDAO;
    }

    /**
     * @return the userDAO
     */
    public UserDAO getUserDAO()
    {
        return userDAO;
    }

    /**
     * @param userDAO
     *            the userDAO to set
     */
    public void setUserDAO(UserDAO userDAO)
    {
        this.userDAO = userDAO;
    }

    /**
     * @return the outDAO
     */
    public OutDAO getOutDAO()
    {
        return outDAO;
    }

    /**
     * @param outDAO
     *            the outDAO to set
     */
    public void setOutDAO(OutDAO outDAO)
    {
        this.outDAO = outDAO;
    }

    private String getAll(int i)
    {
        String s = "00000000" + i;

        return s.substring(s.length() - 9);
    }

    /**
     * @return the consignDAO
     */
    public ConsignDAO getConsignDAO()
    {
        return consignDAO;
    }

    /**
     * @param consignDAO
     *            the consignDAO to set
     */
    public void setConsignDAO(ConsignDAO consignDAO)
    {
        this.consignDAO = consignDAO;
    }

    /**
     * TODO 发送短信(只有销售单发送短信)(从短信接收模块来说需要修复回复短信处理结果)
     * 
     * @param out
     * @param user
     */
    private void notifyOut(OutBean out, User user, int type)
    {
        if (out.getType() == 1)
        {
            return;
        }

        NotifyBean notify = new NotifyBean();

        if (type == 0)
        {
            notify.setMessage(out.getFullId() + "已经被[" + user.getStafferName() + "]审批通过");
        }
        else if (type == 1)
        {
            notify.setMessage(out.getFullId() + "已经被[" + user.getStafferName() + "]驳回");
        }
        else if (type == 2)
        {
            notify.setMessage(out.getFullId() + "已经被[" + user.getStafferName() + "]确认付款");
        }
        else if (type == 3)
        {
            notify.setMessage(out.getFullId() + "已经被[" + user.getStafferName() + "]总部核对,单据流程结束");
        }
        else
        {
            notify.setMessage(out.getFullId() + "已经被[" + user.getStafferName() + "]处理");
        }

        notify.setUrl("../sail/out.do?method=findOut&fow=99&outId=" + out.getFullId());

        notifyManager.notifyWithoutTransaction(out.getStafferId(), notify);

        if (false)
        {
            // 0:保存 1:提交 2:驳回 3:发货 4:会计审核通过 6:总经理审核通过
            if (out.getStatus() == OutConstant.STATUS_SAVE
                || out.getStatus() == OutConstant.STATUS_REJECT
                || out.getStatus() == OutConstant.STATUS_SEC_PASS)
            {
                return;
            }

            // 发送短信给区域总经理审核
            if (out.getStatus() == OutConstant.STATUS_SUBMIT)
            {
                ConditionParse condtition = new ConditionParse();

                condtition.addCondition("locationId", "=", out.getLocationId());

                condtition.addIntCondition("status", "=", 0);

                condtition.addIntCondition("role", "=", 4);

                queryUserToSendSMS(out, user, condtition, "总经理审批");
            }

            // 发送短信给库管审核(非总部)
            if (out.getStatus() == OutConstant.STATUS_MANAGER_PASS
                && !"0".equals(out.getLocation()))
            {
                ConditionParse condtition = new ConditionParse();

                condtition.addCondition("locationId", "=", out.getLocationId());

                condtition.addIntCondition("status", "=", 0);

                condtition.addIntCondition("role", "=", 1);

                queryUserToSendSMS(out, user, condtition, "库管员审批");
            }
        }

    }

    /**
     * queryUserToSendSMS
     * 
     * @param out
     * @param user
     * @param condtition
     */
    private void queryUserToSendSMS(OutBean out, User user, ConditionParse condtition,
                                    String tokenName)
    {
        List<UserBean> userList = ListTools.distinct(userDAO
            .queryEntityBeansByCondition(condtition));

        for (UserBean baseUser : userList)
        {
            StafferBean sb = stafferDAO.find(baseUser.getStafferId());

            if (sb != null && !StringTools.isNullOrNone(sb.getHandphone()))
            {
                sendSMSInner(out, user, sb, tokenName);
            }
        }
    }

    /**
     * sendSMS
     * 
     * @param out
     * @param user
     * @param sb
     */
    private void sendSMSInner(OutBean out, User user, StafferBean sb, String tokenName)
    {
        StafferBean realStaffer = stafferDAO.find(out.getStafferId());

        if (realStaffer == null)
        {
            return;
        }

        // DBT 动态引入的判断
        if ( !DynamicBundleTools.isServiceExist("com.china.center.oa.note.dao.ShortMessageTaskDAO"))
        {
            return;
        }

        ShortMessageTaskDAO shortMessageTaskDAO = DynamicBundleTools
            .getService(ShortMessageTaskDAO.class);

        if (shortMessageTaskDAO == null)
        {
            return;
        }

        // TODO 恢复短信回复的处理 send short message
        ShortMessageTaskBean sms = new ShortMessageTaskBean();

        sms.setId(commonDAO.getSquenceString20());

        sms.setFk(out.getFullId());

        sms.setType(HandleMessage.TYPE_OUT);

        sms.setHandId(RandomTools.getRandomMumber(4));

        sms.setStatus(ShortMessageConstant.STATUS_INIT);

        sms.setMtype(ShortMessageConstant.MTYPE_ONLY_SEND_RECEIVE);

        sms.setFktoken(String.valueOf(out.getStatus()));

        sms.setMessage(realStaffer.getName() + "发起销售单[" + out.getDescription() + "(回款天数:"
                       + out.getReday() + ";总金额:" + MathTools.formatNum(out.getTotal()) + ")]"
                       + "需您审批(" + tokenName + ").0通过,1驳回.回复格式[" + sms.getHandId() + ":0]或["
                       + sms.getHandId() + ":1:理由]");

        sms.setReceiver(sb.getHandphone());

        sms.setStafferId(sb.getId());

        sms.setLogTime(TimeTools.now());

        // 24 hour
        sms.setEndTime(TimeTools.now(1));

        // internal
        sms.setSendTime(TimeTools.getDateTimeString(this.internal));

        // add sms
        shortMessageTaskDAO.saveEntityBean(sms);
    }

    public int findOutStatusInLog(String fullId)
    {
        // 获取日志，正排序
        List<FlowLogBean> logList = flowLogDAO.queryEntityBeansByFK(fullId);

        if (ListTools.isEmptyOrNull(logList))
        {
            return -1;
        }

        return logList.get(logList.size() - 1).getAfterStatus();
    }

    /**
     * 增加日志
     * 
     * @param fullId
     * @param user
     * @param outBean
     */
    private void addOutLog(final String fullId, final User user, final OutBean outBean, String des,
                           int mode, int astatus)
    {
        FlowLogBean log = new FlowLogBean();

        log.setActor(user.getStafferName());

        log.setDescription(des);
        log.setFullId(fullId);
        log.setOprMode(mode);
        log.setLogTime(TimeTools.now());

        log.setPreStatus(outBean.getStatus());

        log.setAfterStatus(astatus);

        flowLogDAO.saveEntityBean(log);
    }

    /**
     * loggerError(严重错误的日志哦)
     * 
     * @param msg
     */
    private void loggerError(String msg)
    {
        importLog.error(msg);

        fatalNotify.notify(msg);
    }

    /**
     * @return the depotpartDAO
     */
    public DepotpartDAO getDepotpartDAO()
    {
        return depotpartDAO;
    }

    /**
     * @param depotpartDAO
     *            the depotpartDAO to set
     */
    public void setDepotpartDAO(DepotpartDAO depotpartDAO)
    {
        this.depotpartDAO = depotpartDAO;
    }

    /**
     * @return the internal
     */
    public int getInternal()
    {
        return internal;
    }

    /**
     * @param internal
     *            the internal to set
     */
    public void setInternal(int internal)
    {
        this.internal = internal;
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
     * @return the creditLevelDAO
     */
    public CreditLevelDAO getCreditLevelDAO()
    {
        return creditLevelDAO;
    }

    /**
     * @param creditLevelDAO
     *            the creditLevelDAO to set
     */
    public void setCreditLevelDAO(CreditLevelDAO creditLevelDAO)
    {
        this.creditLevelDAO = creditLevelDAO;
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
     * @return the baseDAO
     */
    public BaseDAO getBaseDAO()
    {
        return baseDAO;
    }

    /**
     * @param baseDAO
     *            the baseDAO to set
     */
    public void setBaseDAO(BaseDAO baseDAO)
    {
        this.baseDAO = baseDAO;
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
     * @return the creditCoreDAO
     */
    public CreditCoreDAO getCreditCoreDAO()
    {
        return creditCoreDAO;
    }

    /**
     * @param creditCoreDAO
     *            the creditCoreDAO to set
     */
    public void setCreditCoreDAO(CreditCoreDAO creditCoreDAO)
    {
        this.creditCoreDAO = creditCoreDAO;
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
     * @return the transactionManager
     */
    public PlatformTransactionManager getTransactionManager()
    {
        return transactionManager;
    }

    /**
     * @param transactionManager
     *            the transactionManager to set
     */
    public void setTransactionManager(PlatformTransactionManager transactionManager)
    {
        this.transactionManager = transactionManager;
    }

    /**
     * @return the storageRelationManager
     */
    public StorageRelationManager getStorageRelationManager()
    {
        return storageRelationManager;
    }

    /**
     * @param storageRelationManager
     *            the storageRelationManager to set
     */
    public void setStorageRelationManager(StorageRelationManager storageRelationManager)
    {
        this.storageRelationManager = storageRelationManager;
    }

    /**
     * @return the outUniqueDAO
     */
    public OutUniqueDAO getOutUniqueDAO()
    {
        return outUniqueDAO;
    }

    /**
     * @param outUniqueDAO
     *            the outUniqueDAO to set
     */
    public void setOutUniqueDAO(OutUniqueDAO outUniqueDAO)
    {
        this.outUniqueDAO = outUniqueDAO;
    }

    /**
     * @return the depotDAO
     */
    public DepotDAO getDepotDAO()
    {
        return depotDAO;
    }

    /**
     * @param depotDAO
     *            the depotDAO to set
     */
    public void setDepotDAO(DepotDAO depotDAO)
    {
        this.depotDAO = depotDAO;
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
     * @return the fatalNotify
     */
    public FatalNotify getFatalNotify()
    {
        return fatalNotify;
    }

    /**
     * @param fatalNotify
     *            the fatalNotify to set
     */
    public void setFatalNotify(FatalNotify fatalNotify)
    {
        this.fatalNotify = fatalNotify;
    }

    /**
     * @return the baseBalanceDAO
     */
    public BaseBalanceDAO getBaseBalanceDAO()
    {
        return baseBalanceDAO;
    }

    /**
     * @param baseBalanceDAO
     *            the baseBalanceDAO to set
     */
    public void setBaseBalanceDAO(BaseBalanceDAO baseBalanceDAO)
    {
        this.baseBalanceDAO = baseBalanceDAO;
    }

    /**
     * @return the outBalanceDAO
     */
    public OutBalanceDAO getOutBalanceDAO()
    {
        return outBalanceDAO;
    }

    /**
     * @param outBalanceDAO
     *            the outBalanceDAO to set
     */
    public void setOutBalanceDAO(OutBalanceDAO outBalanceDAO)
    {
        this.outBalanceDAO = outBalanceDAO;
    }

    /**
     * 处理销售单的通过
     * 
     * @param fullId
     * @param user
     * @param outBean
     * @param depot
     * @param newNextStatus
     */
    private void handerPassOut(final String fullId, final User user, final OutBean outBean,
                               final DepotBean depot, int newNextStatus)
    {
        // 从分公司经理审核通过到提交
        if (newNextStatus == OutConstant.STATUS_SUBMIT)
        {
            // 只有信用已经超支的情况下才启用分公司经理的信用
            if (outBean.getReserve2() == OutConstant.OUT_CREDIT_OVER)
            {
                // 加入审批人的信用(是自己使用的信用+担保的信用)
                double noPayBusinessByManager = outDAO.sumAllNoPayAndAvouchBusinessByStafferId(user
                    .getStafferId(), YYTools.getStatBeginDate(), YYTools.getStatEndDate());

                StafferBean staffer = stafferDAO.find(user.getStafferId());

                // 这里分公司总经理的信用已经使用结束了,此时直接抛出异常
                if (noPayBusinessByManager > staffer.getCredit())
                {
                    throw new RuntimeException("您的信用额度已经全部占用[使用了"
                                               + MathTools.formatNum(noPayBusinessByManager)
                                               + "],不能再担保业务员的销售");
                }

                // 本次需要担保的信用
                double lastCredit = outBean.getTotal() - outBean.getStaffcredit()
                                    - outBean.getCurcredit();

                // 职员杠杆后的信用
                double staffCredit = staffer.getCredit() * staffer.getLever();

                if ( (lastCredit + noPayBusinessByManager) > staffCredit)
                {
                    throw new RuntimeException("您杠杆后的信用额度是[" + MathTools.formatNum(staffCredit)
                                               + "],已经使用了["
                                               + MathTools.formatNum(noPayBusinessByManager)
                                               + "],本单需要您担保的额度是[" + MathTools.formatNum(lastCredit)
                                               + "],加上本单已经超出您的最大额度,不能再担保业务员的销售");
                }

                // 这里使用分公司经理信用担保
                outDAO.updateManagercredit(outBean.getFullId(), user.getStafferId(), lastCredit);

                // 此时信用不超支了
                outDAO.updateOutReserve(fullId, OutConstant.OUT_CREDIT_COMMON, outBean
                    .getReserve6());
            }
        }

        // 结算中心通过/总裁通过 修改manager的入库时间
        if (newNextStatus == OutConstant.STATUS_MANAGER_PASS)
        {
            outDAO.modifyManagerTime(outBean.getFullId(), TimeTools.now());

            // 验证是否是款到发货
            if (outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_MONEY
                && outBean.getPay() != OutConstant.PAY_YES)
            {
                throw new RuntimeException("此单据是款到发货,当前此单未付款,不能通过");
            }
        }

        // CORE 需要把回款日志敲定且变动销售库存
        if (newNextStatus == OutConstant.STATUS_PASS)
        {
            long add = outBean.getReday() * 24 * 3600 * 1000L;

            // 这里需要把出库单的回款日期修改
            outDAO.modifyReDate(fullId, TimeTools.getStringByFormat(new Date(new Date().getTime()
                                                                             + add), "yyyy-MM-dd"));

            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outBean.getFullId());

            try
            {
                // 变动库存
                processPass(user, outBean, baseList, StorageConstant.OPR_STORAGE_OUTBILL);
            }
            catch (MYException e)
            {
                throw new RuntimeException(e.getErrorContent());
            }

            // TODO 销售单是发货后产生管理凭证
        }

        // 结算中心审核通过后/总裁通过，中心仓库的销售单转到物流管理员，同时自动生成发货单
        if (newNextStatus == OutConstant.STATUS_MANAGER_PASS
            && depot.getType() == DepotConstant.DEPOT_TYPE_CENTER)
        {
            ConsignBean bean = new ConsignBean();

            bean.setCurrentStatus(SailConstant.CONSIGN_INIT);

            bean.setFullId(outBean.getFullId());

            bean.setArriveDate(outBean.getArriveDate());

            consignDAO.addConsign(bean);
        }
    }

    /**
     * 处理入库单的流程
     * 
     * @param fullId
     * @param user
     * @param outBean
     * @param newNextStatus
     */
    private void handerPassBuy(final String fullId, final User user, final OutBean outBean,
                               int newNextStatus)
    {
        // 分公司总经理审批-->待总裁审批
        if (newNextStatus == OutConstant.STATUS_CEO_CHECK)
        {
            // 暂时没有任何操作
        }

        // 待总裁审批-->待董事长审批
        if (newNextStatus == OutConstant.STATUS_CHAIRMA_CHECK)
        {
            // 暂时没有任何操作
        }

        // CORE 待董事长审批-->发货
        if (newNextStatus == OutConstant.STATUS_PASS)
        {
            long add = outBean.getReday() * 24 * 3600 * 1000L;

            // 这里需要把出库单的回款日期修改
            outDAO.modifyReDate(fullId, TimeTools.getStringByFormat(new Date(new Date().getTime()
                                                                             + add), "yyyy-MM-dd"));

            List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outBean.getFullId());

            try
            {
                // 变动库存
                processPass(user, outBean, baseList, StorageConstant.OPR_STORAGE_INOTHER);
            }
            catch (MYException e)
            {
                throw new RuntimeException(e.getErrorContent());
            }

            if (outBean.getOutType() == OutConstant.OUTTYPE_IN_DROP)
            {
                // TODO 入库且是报废后是发货后产生管理凭证
            }
        }
    }
}
