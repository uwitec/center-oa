/**
 * File Name: OutManagerImpl.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2010-11-7<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.sail.manager.impl;


import java.util.ArrayList;
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

import com.center.china.osgi.publics.User;
import com.china.center.common.MYException;
import com.china.center.jdbc.util.ConditionParse;
import com.china.center.oa.credit.bean.CreditLevelBean;
import com.china.center.oa.credit.dao.CreditCoreDAO;
import com.china.center.oa.credit.dao.CreditLevelDAO;
import com.china.center.oa.customer.bean.CustomerBean;
import com.china.center.oa.customer.dao.CustomerDAO;
import com.china.center.oa.note.bean.ShortMessageTaskBean;
import com.china.center.oa.note.constant.ShortMessageConstant;
import com.china.center.oa.note.dao.ShortMessageTaskDAO;
import com.china.center.oa.note.manager.HandleMessage;
import com.china.center.oa.product.bean.DepotBean;
import com.china.center.oa.product.constant.DepotConstant;
import com.china.center.oa.product.constant.StorageConstant;
import com.china.center.oa.product.dao.DepotDAO;
import com.china.center.oa.product.dao.DepotpartDAO;
import com.china.center.oa.product.dao.ProductDAO;
import com.china.center.oa.product.dao.StorageDAO;
import com.china.center.oa.product.manager.StorageRelationManager;
import com.china.center.oa.product.wrap.ProductChangeWrap;
import com.china.center.oa.publics.bean.FlowLogBean;
import com.china.center.oa.publics.bean.LocationBean;
import com.china.center.oa.publics.bean.StafferBean;
import com.china.center.oa.publics.bean.UserBean;
import com.china.center.oa.publics.constant.SysConfigConstant;
import com.china.center.oa.publics.dao.CommonDAO;
import com.china.center.oa.publics.dao.FlowLogDAO;
import com.china.center.oa.publics.dao.LocationDAO;
import com.china.center.oa.publics.dao.ParameterDAO;
import com.china.center.oa.publics.dao.StafferDAO;
import com.china.center.oa.publics.dao.UserDAO;
import com.china.center.oa.sail.bean.BaseBean;
import com.china.center.oa.sail.bean.ConsignBean;
import com.china.center.oa.sail.bean.LogBean;
import com.china.center.oa.sail.bean.OutBean;
import com.china.center.oa.sail.bean.OutUniqueBean;
import com.china.center.oa.sail.constanst.OutConstant;
import com.china.center.oa.sail.constanst.SailConstant;
import com.china.center.oa.sail.dao.BaseDAO;
import com.china.center.oa.sail.dao.ConsignDAO;
import com.china.center.oa.sail.dao.OutDAO;
import com.china.center.oa.sail.dao.OutUniqueDAO;
import com.china.center.oa.sail.helper.OutHelper;
import com.china.center.oa.sail.helper.YYTools;
import com.china.center.oa.sail.manager.OutManager;
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
public class OutManagerImpl implements OutManager
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

    private StorageDAO storageDAO = null;

    private CustomerDAO customerDAO = null;

    private ParameterDAO parameterDAO = null;

    private CreditLevelDAO creditLevelDAO = null;

    private DepotpartDAO depotpartDAO = null;

    private CreditCoreDAO creditCoreDAO = null;

    private FlowLogDAO flowLogDAO = null;

    private OutUniqueDAO outUniqueDAO = null;

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

        // 保存库单
        outBean.setStatus(OutConstant.STATUS_SAVE);

        outBean.setInway(OutConstant.IN_WAY_NO);

        // 获得baseList
        final String[] nameList = request.getParameter("nameList").split("~");
        final String[] idsList = request.getParameter("idsList").split("~");
        final String[] showIdList = request.getParameter("showIdList").split("~");
        final String[] showNameList = request.getParameter("showNameList").split("~");
        final String[] ownerList = request.getParameter("ownerList").split("~");
        final String[] unitList = request.getParameter("unitList").split("~");
        final String[] amontList = request.getParameter("amontList").split("~");
        final String[] priceList = request.getParameter("priceList").split("~");
        final String[] costPriceList = request.getParameter("costPriceList").split("~");
        final String[] totalList = request.getParameter("totalList").split("~");
        final String[] desList = (" " + request.getParameter("desList") + " ").split("~");

        // 组织BaseBean
        double ttatol = 0.0d;
        for (int i = 0; i < nameList.length; i++ )
        {
            ttatol += (Double.parseDouble(priceList[i]) * Integer.parseInt(amontList[i]));
        }

        outBean.setTotal(ttatol);

        outBean.setCurcredit(0.0d);

        outBean.setStaffcredit(0.0d);

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
                        base.setOutId(outBean.getFullId());
                        base.setProductId(idsList[i]);

                        if (StringTools.isNullOrNone(base.getProductId()))
                        {
                            throw new RuntimeException("产品ID为空,数据不完备");
                        }

                        base.setProductName(nameList[i]);
                        base.setUnit(unitList[i]);
                        base.setPrice(MathTools.parseDouble(priceList[i]));
                        base.setAmount(MathTools.parseInt(amontList[i]));
                        base.setValue(MathTools.parseDouble(totalList[i]));
                        base.setShowId(showIdList[i]);
                        base.setShowName(showNameList[i]);
                        base.setOwner(ownerList[i]);
                        base.setCostPrice(MathTools.parseDouble(costPriceList[i]));
                        base.setLocationId(outBean.getLocation());

                        base.setDescription(desList[i].trim());

                        // 增加单个产品到base表
                        baseDAO.saveEntityBean(base);
                    }

                    sendSMS(outBean, user);

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
    public String coloneOutAndSubmitAffair(final OutBean outBean, final User user)
        throws MYException
    {
        return coloneOutAndSubmitWithOutAffair(outBean, user, StorageConstant.OPR_STORAGE_REDEPLOY);
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
    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean submit(final String fullId, final User user)
        throws MYException
    {
        return submitWithOutAffair(fullId, user, StorageConstant.OPR_STORAGE_OUTBILLIN);
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.sail.manager.OutManager#submitWithOutAffair(java.lang.String,
     *      com.center.china.osgi.publics.User)
     */
    public synchronized boolean submitWithOutAffair(final String fullId, final User user, int type)
        throws MYException
    {
        final OutBean outBean = outDAO.find(fullId);

        if (outBean == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        int preStatus = outBean.getStatus();

        if (preStatus != OutConstant.STATUS_SAVE && preStatus != OutConstant.STATUS_REJECT)
        {
            throw new MYException("单据已经提交或者驳回,请重新操作");
        }

        final List<BaseBean> baseList = checkSubmit(fullId, outBean);

        final List<LogBean> logList = new ArrayList<LogBean>();

        // 这里是入库单的直接库存变动
        processBaseList(user, outBean, baseList, logList, type);

        // CORE 修改库单的状态(信用额度处理)
        int status = processOutStutus(fullId, user, outBean);

        // 处理在途
        processOutInWay(fullId, outBean);

        // 增加数据库日志
        addOutLog(fullId, user, outBean, "提交", SailConstant.OPR_OUT_PASS, OutConstant.STATUS_SUBMIT);

        outBean.setStatus(status);

        sendSMS(outBean, user);

        return true;
    }

    /**
     * @param fullId
     * @param outBean
     */
    private void processOutInWay(final String fullId, final OutBean outBean)
    {
        // 如果是入库的调入，需要把在途的入库单取消掉
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL && outBean.getOutType() == OutConstant.INBILL_IN)
        {
            String ofullid = outBean.getRefOutFullId();

            outDAO.updataInWay(ofullid, OutConstant.IN_WAY_OVER);

            importLog.info(ofullid + "的在途状态改变成在途结束");
        }

        // 如果是入库的调出
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL && outBean.getOutType() == OutConstant.INBILL_OUT)
        {
            outDAO.updataInWay(fullId, OutConstant.IN_WAY);

            importLog.info(fullId + "的在途状态改变成在途");
        }
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

        if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
        {
            importLog.info(fullId + ":" + user.getStafferName() + ":" + 1 + ":redrectFrom:" + outBean.getStatus());

            // 出库单
            try
            {
                outDAO.modifyOutStatus2(fullId, OutConstant.STATUS_SUBMIT);

                result = 1;

                // 只有销售单才有信用，剔除个人领样的
                if (outBean.getOutType() == OutConstant.OUTTYPE_OUT_COMMON)
                {
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
                    if (outCredit && cbean != null && !StringTools.isNullOrNone(cbean.getCreditLevelId())
                        && outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_COMMON)
                    {
                        throw new MYException("不支持此类型,请重新操作");
                    }

                    // 使用业务员的信用额度
                    if (outCredit && outBean.getReserve3() == OutConstant.OUT_SAIL_TYPE_CREDIT_AND_CUR)
                    {
                        StafferBean sb2 = stafferDAO.find(outBean.getStafferId());

                        if (sb2 == null)
                        {
                            throw new MYException("数据不完备,请重新操作");
                        }

                        // 先清空两个预占金额,主要是统计的时候方便
                        outDAO.updateCurcredit(fullId, 0.0d);

                        outDAO.updateStaffcredit(fullId, 0.0d);

                        double noPayBusinessInCur = outDAO.sumNoPayBusiness(outBean.getCustomerId(), YYTools
                            .getFinanceBeginDate(), YYTools.getFinanceEndDate());

                        double noPayBusiness = outDAO.sumNoPayAndAvouchBusinessByStafferId(outBean.getStafferId(),
                            YYTools.getFinanceBeginDate(), YYTools.getFinanceEndDate());

                        double remainInCur = clevel.getMoney() - noPayBusinessInCur;

                        if (remainInCur < 0)
                        {
                            remainInCur = 0.0;
                        }

                        // 全部使用客户的信用等级
                        if (remainInCur >= outBean.getTotal())
                        {
                            outDAO.updateCurcredit(fullId, outBean.getTotal());

                            outDAO.updateStaffcredit(fullId, 0.0d);
                        }

                        // 一半使用客户,一半使用职员的
                        if (remainInCur < outBean.getTotal())
                        {
                            // 全部使用客户的信用等级
                            outDAO.updateCurcredit(fullId, remainInCur);

                            // 当前单据需要使用的职员信用额度
                            double remainInStaff = outBean.getTotal() - remainInCur;

                            // 防止职员信用等级超支
                            if ( (noPayBusiness + remainInStaff) > sb2.getCredit())
                            {
                                outBean.setReserve6("客户信用最大额度是:"
                                                    + MathTools.formatNum(clevel.getMoney())
                                                    + ".当前客户未付款金额(不包括此单):"
                                                    + MathTools.formatNum(noPayBusinessInCur)
                                                    + ".职员信用额度是:"
                                                    + MathTools.formatNum(sb2.getCredit())
                                                    + ".职员信用已经使用额度是:"
                                                    + MathTools.formatNum(noPayBusiness)
                                                    + ".职员信用超支(包括此单):"
                                                    + (MathTools.formatNum( (noPayBusiness + remainInStaff - sb2
                                                        .getCredit()))));

                                isCreditOutOf = true;

                                outDAO.updateOutReserve2(fullId, OutConstant.OUT_CREDIT_OVER, outBean.getReserve6());
                            }

                            outDAO.updateStaffcredit(fullId, remainInStaff);
                        }
                    }

                    // 信用没有受限检查产品价格是否为0
                    if ( !isCreditOutOf)
                    {
                        boolean isZero = false;

                        List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(outBean.getFullId());

                        for (BaseBean baseBean : baseList)
                        {
                            if (baseBean.getPrice() == 0.0d)
                            {
                                isZero = true;

                                outBean.setReserve6("存在价格为0的产品");

                                outDAO.updateOutReserve2(fullId, OutConstant.OUT_CREDIT_MIN, outBean.getReserve6());

                                break;
                            }
                        }

                        if ( !isZero)
                        {
                            outBean.setReserve6("");

                            outDAO.updateOutReserve2(fullId, OutConstant.OUT_CREDIT_COMMON, outBean.getReserve6());
                        }
                    }

                    // 修改人工干预,重新置为0
                    String pid = "90000000000000009999";

                    creditCoreDAO.updateCurCreToInit(pid, outBean.getCustomerId());
                }
            }
            catch (Exception e)
            {
                _logger.error(e, e);

                throw new MYException(e);
            }
        }
        else
        {
            // 入库直接通过
            importLog.info(fullId + ":" + user.getStafferName() + ":" + 3 + ":redrectFrom:" + outBean.getStatus());

            try
            {
                outDAO.modifyOutStatus2(fullId, OutConstant.STATUS_PASS);

                result = 3;
            }
            catch (Exception e)
            {
                throw new MYException(e.toString());
            }

            outDAO.modifyData(fullId, TimeTools.now("yyyy-MM-dd"));
        }

        return result;
    }

    /**
     * 处理base入库
     * 
     * @param user
     * @param outBean
     * @param baseList
     * @param logList
     * @throws MYException
     */
    private void processBaseList(final User user, final OutBean outBean, final List<BaseBean> baseList,
                                 final List<LogBean> logList, int type)
        throws MYException
    {
        // 入库单提交后就直接移动库存了,销售需要在库管通过后生成发货单前才会变动库存
        if (outBean.getType() != OutConstant.OUT_TYPE_INBILL)
        {
            return;
        }

        String sequence = commonDAO.getSquenceString();

        for (BaseBean element : baseList)
        {
            ProductChangeWrap wrap = new ProductChangeWrap();

            wrap.setDepotpartId(outBean.getLocation());
            wrap.setPrice(element.getCostPrice());
            wrap.setProductId(element.getProductId());
            wrap.setStafferId(element.getOwner());
            wrap.setChange(element.getAmount());
            wrap.setDescription("库单[" + outBean.getFullId() + "]操作");
            wrap.setSerializeId(sequence);
            wrap.setType(type);

            storageRelationManager.changeStorageRelationWithoutTransaction(user, wrap, true);
        }

        saveUnique(user, outBean);
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

        if (outBean.getStatus() != OutConstant.STATUS_SAVE && outBean.getStatus() != OutConstant.STATUS_REJECT)
        {
            throw new MYException(fullId + " 状态错误,不能提交");
        }

        final List<BaseBean> baseList = baseDAO.queryEntityBeansByFK(fullId);

        // 先检查入库
        for (BaseBean element : baseList)
        {
            ProductChangeWrap wrap = new ProductChangeWrap();

            wrap.setDepotpartId(outBean.getLocation());
            wrap.setPrice(element.getCostPrice());
            wrap.setProductId(element.getProductId());
            wrap.setStafferId(element.getOwner());

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

        // 如果是入库的调入，需要有REF的单据
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL && outBean.getOutType() == OutConstant.INBILL_IN)
        {
            String ofullid = outBean.getRefOutFullId();

            if (StringTools.isNullOrNone(ofullid))
            {
                throw new MYException("由于是调入的库单需要调出的库单对应，请重新操作选择调出的库单");
            }

            OutBean temp = outDAO.find(ofullid);

            if (temp == null)
            {
                throw new MYException("选择调出的库单不存在，请重新操作选择调出的库单");
            }

            if (temp.getInway() != OutConstant.IN_WAY)
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
    public synchronized boolean reject(final String fullId, final User user, final String reason)
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

        return true;

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
    private void doReject(final String fullId, final User user, final String reason, final OutBean outBean,
                          final List<BaseBean> baseList, final String deportId)
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
                    // 如果销售单，需要删除发货单(当库管驳回的时候才触发)
                    if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
                    {
                        ConsignBean beans = consignDAO.findConsignById(fullId);

                        if (beans != null)
                        {
                            consignDAO.delConsign(fullId);
                        }
                    }

                    // 修改状态
                    if (outBean.getType() == OutConstant.OUT_TYPE_OUTBILL)
                    {
                        importLog.info(fullId + ":" + user.getStafferName() + ":" + OutConstant.STATUS_REJECT
                                       + ":redrectFrom:" + outBean.getStatus());
                        outDAO.modifyOutStatus2(outBean.getFullId(), OutConstant.STATUS_REJECT);
                    }
                    else
                    {
                        importLog.info(fullId + ":" + user.getStafferName() + ":" + OutConstant.STATUS_SAVE
                                       + ":redrectFrom:" + outBean.getStatus());

                        outDAO.modifyOutStatus2(outBean.getFullId(), OutConstant.STATUS_SAVE);
                    }

                    // 驳回修改在途方式
                    outDAO.updataInWay(fullId, OutConstant.IN_WAY_NO);

                    // 操作日志
                    addOutLog(fullId, user, outBean, reason, SailConstant.OPR_OUT_REJECT, OutConstant.STATUS_REJECT);

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
    private void checkReject(final OutBean outBean, final List<BaseBean> baseList, final String deportId)
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
     * CORE 审核通过(这里只有销售单才有此操作)结算中心/物流审批/库管发货
     * 
     * @param outBean
     * @param user
     * @param depotpartId
     *            废弃
     * @return
     * @throws Exception
     */
    public synchronized boolean pass(final String fullId, final User user, final int nextStatus, final String reason,
                                     final String depotpartId)
        throws MYException
    {
        final OutBean outBean = outDAO.find(fullId);

        final int oldStatus = outBean.getStatus();

        if (outBean == null)
        {
            throw new MYException("销售单不存在，请重新操作");
        }

        // 检查pass的条件
        if (outBean.getType() == OutConstant.OUT_TYPE_INBILL)
        {
            throw new MYException("入库单没有此操作!");
        }

        // 检查pass的条件
        if (outBean.getStatus() == OutConstant.STATUS_SAVE || outBean.getStatus() == OutConstant.STATUS_REJECT)
        {
            throw new MYException("状态不可以通过!");
        }

        final DepotBean depot = depotDAO.find(outBean.getLocation());

        if (depot == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // 需要发货单先通过(只有中心仓库才有物流哦)
        if (nextStatus == OutConstant.STATUS_FLOW_PASS && depot.getType() == DepotConstant.DEPOT_TYPE_CENTER)
        {
            ConsignBean consignBean = consignDAO.findConsignById(fullId);

            if (consignBean == null)
            {
                throw new MYException("没有发货单,请重新操作!");
            }

            if (consignBean.getCurrentStatus() == SailConstant.CONSIGN_INIT)
            {
                throw new MYException("发货单没有审批通过，请先处理发货单");
            }
        }

        // 入库操作在数据库事务中完成
        TransactionTemplate tran = new TransactionTemplate(transactionManager);
        try
        {
            tran.execute(new TransactionCallback()
            {
                public Object doInTransaction(TransactionStatus arg0)
                {
                    int newNextStatus = nextStatus;

                    // 直接把物流通过到库管通过
                    if (newNextStatus == OutConstant.STATUS_FLOW_PASS
                        && depot.getType() == DepotConstant.DEPOT_TYPE_LOCATION)
                    {
                        newNextStatus = OutConstant.STATUS_PASS;
                    }

                    importLog.info(outBean.getFullId() + ":" + user.getStafferName() + ":" + newNextStatus
                                   + ":redrectFrom:" + oldStatus);

                    // 修改状态
                    outDAO.modifyOutStatus2(outBean.getFullId(), newNextStatus);

                    // 修改manager的入库时间
                    if (newNextStatus == OutConstant.STATUS_MANAGER_PASS)
                    {
                        outDAO.modifyManagerTime(outBean.getFullId(), TimeTools.now());
                    }

                    // 需要把回款日志敲定且变动库存
                    if (newNextStatus == OutConstant.STATUS_PASS)
                    {
                        outDAO.modifyData(fullId, TimeTools.now("yyyy-MM-dd"));

                        long add = outBean.getReday() * 24 * 3600 * 1000L;

                        // 这里需要把出库单的回款日期修改
                        outDAO.modifyReDate2(fullId, TimeTools.getStringByFormat(new Date(new Date().getTime() + add),
                            "yyyy-MM-dd"));

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
                    }

                    // 结算中心审核通过后，中心仓库的销售单转到物流管理员，同时自动生成发货单
                    if (newNextStatus == OutConstant.STATUS_MANAGER_PASS
                        && depot.getType() == DepotConstant.DEPOT_TYPE_CENTER)
                    {
                        ConsignBean bean = new ConsignBean();

                        bean.setCurrentStatus(SailConstant.CONSIGN_INIT);

                        bean.setFullId(outBean.getFullId());

                        consignDAO.addConsign(bean);
                    }

                    addOutLog(fullId, user, outBean, reason, SailConstant.OPR_OUT_PASS, newNextStatus);

                    outBean.setStatus(newNextStatus);

                    sendSMS(outBean, user);

                    return Boolean.TRUE;
                }
            });
        }
        catch (TransactionException e)
        {
            _logger.error("通过库单错误：", e);
            throw new MYException("数据库内部错误");
        }
        catch (DataAccessException e)
        {
            _logger.error("通过库单错误：", e);
            throw new MYException(e.getCause().toString());
        }
        catch (Exception e)
        {
            _logger.error("通过库单错误：", e);
            throw new MYException("处理异常:" + e.getMessage());
        }

        return true;

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
    private void processPass(final User user, final OutBean outBean, final List<BaseBean> baseList, int type)
        throws MYException
    {
        String sequence = commonDAO.getSquenceString();

        for (BaseBean element : baseList)
        {
            ProductChangeWrap wrap = new ProductChangeWrap();

            wrap.setDepotpartId(outBean.getLocation());
            wrap.setPrice(element.getCostPrice());
            wrap.setProductId(element.getProductId());
            wrap.setStafferId(element.getOwner());

            // 这里是销售单,所以是负数
            wrap.setChange( -element.getAmount());
            wrap.setDescription("库单[" + outBean.getFullId() + "]库管通过操作");
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

                    outDAO.modifyOutStatus2(fullId, OutConstant.STATUS_SEC_PASS);

                    // TODO_OSGI 核对此销售单的应收,看看是否正确结余(然后写到应收字段里面)

                    addOutLog(fullId, user, outBean, "核对", SailConstant.OPR_OUT_PASS, OutConstant.STATUS_SEC_PASS);

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

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean modifyPay(String fullId, int pay)
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

        outDAO.modifyReDate2(fullId, TimeTools.now_short());

        return outDAO.modifyPay2(fullId, pay);
    }

    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean mark(String fullId, boolean status)
    {
        return outDAO.mark2(fullId, status);
    }

    @Transactional(rollbackFor = {MYException.class})
    @Exceptional
    public boolean modifyReDate(String fullId, String reDate)
    {
        return outDAO.modifyReDate2(fullId, reDate);
    }

    @Exceptional
    @Transactional(rollbackFor = {MYException.class})
    public boolean modifyOutHadPay(String fullId, String hadPay)
    {
        return outDAO.modifyOutHadPay2(fullId, hadPay);
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
    private void sendSMS(OutBean out, User user)
    {
        if (out.getType() == 1 || true)
        {
            return;
        }

        // 0:保存 1:提交 2:驳回 3:发货 4:会计审核通过 6:总经理审核通过
        if (out.getStatus() == OutConstant.STATUS_SAVE || out.getStatus() == OutConstant.STATUS_REJECT
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
        if (out.getStatus() == OutConstant.STATUS_MANAGER_PASS && !"0".equals(out.getLocation()))
        {
            ConditionParse condtition = new ConditionParse();

            condtition.addCondition("locationId", "=", out.getLocationId());

            condtition.addIntCondition("status", "=", 0);

            condtition.addIntCondition("role", "=", 1);

            queryUserToSendSMS(out, user, condtition, "库管员审批");
        }

    }

    /**
     * queryUserToSendSMS
     * 
     * @param out
     * @param user
     * @param condtition
     */
    private void queryUserToSendSMS(OutBean out, User user, ConditionParse condtition, String tokenName)
    {
        List<UserBean> userList = ListTools.distinct(userDAO.queryEntityBeansByCondition(condtition));

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

        ShortMessageTaskDAO shortMessageTaskDAO = DynamicBundleTools.getService(ShortMessageTaskDAO.class);

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

        sms.setMessage(realStaffer.getName() + "发起销售单[" + out.getDescription() + "(回款天数:" + out.getReday() + ";总金额:"
                       + MathTools.formatNum(out.getTotal()) + ")]" + "需您审批(" + tokenName + ").0通过,1驳回.回复格式["
                       + sms.getHandId() + ":0]或[" + sms.getHandId() + ":1:理由]");

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

    /**
     * @return the storageDAO
     */
    public StorageDAO getStorageDAO()
    {
        return storageDAO;
    }

    /**
     * @param storageDAO
     *            the storageDAO to set
     */
    public void setStorageDAO(StorageDAO storageDAO)
    {
        this.storageDAO = storageDAO;
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
     * 增加日志
     * 
     * @param fullId
     * @param user
     * @param outBean
     */
    private void addOutLog(final String fullId, final User user, final OutBean outBean, String des, int mode,
                           int astatus)
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
}
