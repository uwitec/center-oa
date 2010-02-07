/**
 * File Name: MakeManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-8<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.manager;


import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.sannotations.annotation.Bean;
import net.sourceforge.sannotations.annotation.Property;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.china.center.spring.ex.annotation.Exceptional;
import org.springframework.transaction.annotation.Transactional;

import com.china.center.common.MYException;
import com.china.center.oa.constant.MakeConstant;
import com.china.center.oa.constant.ModuleConstant;
import com.china.center.oa.constant.PublicConstant;
import com.china.center.oa.customize.make.bean.Make01Bean;
import com.china.center.oa.customize.make.bean.MakeBean;
import com.china.center.oa.customize.make.bean.MakeExtBean;
import com.china.center.oa.customize.make.bean.MakeFileBean;
import com.china.center.oa.customize.make.bean.MakeTokenBean;
import com.china.center.oa.customize.make.bean.MakeTokenItemBean;
import com.china.center.oa.customize.make.bean.MakeViewBean;
import com.china.center.oa.customize.make.dao.Make01DAO;
import com.china.center.oa.customize.make.dao.MakeDAO;
import com.china.center.oa.customize.make.dao.MakeExtDAO;
import com.china.center.oa.customize.make.dao.MakeFileDAO;
import com.china.center.oa.customize.make.dao.MakeTokenDAO;
import com.china.center.oa.customize.make.dao.MakeTokenItemDAO;
import com.china.center.oa.customize.make.dao.MakeViewDAO;
import com.china.center.oa.publics.User;
import com.china.center.oa.publics.bean.LogBean;
import com.china.center.oa.publics.dao.CommonDAO2;
import com.china.center.oa.publics.dao.LogDAO;
import com.china.center.tools.CommonTools;
import com.china.center.tools.FileTools;
import com.china.center.tools.JudgeTools;
import com.china.center.tools.TimeTools;


/**
 * MakeManager
 * 
 * @author ZHUZHU
 * @version 2009-10-8
 * @see MakeManager
 * @since 1.0
 */
@Exceptional
@Bean(name = "makeManager")
public class MakeManager
{
    private final Log _logger = LogFactory.getLog(getClass());

    private MakeDAO makeDAO = null;

    private Make01DAO make01DAO = null;

    private MakeExtDAO makeExtDAO = null;

    private CommonDAO2 commonDAO2 = null;

    private MakeTokenDAO makeTokenDAO = null;

    private MakeTokenItemDAO makeTokenItemDAO = null;

    private MakeFileDAO makeFileDAO = null;

    private MakeViewDAO makeViewDAO = null;

    /**
     * 文件跟目录
     */
    @Property(value = "${makeAttachmentRoot}")
    private String makeAttachmentRoot = "";

    private LogDAO logDAO = null;

    /**
     * default constructor
     */
    public MakeManager()
    {}

    /**
     * addMake
     * 
     * @param user
     * @param mak01
     * @param make
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addMake(User user, Make01Bean mak01, MakeBean make)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, mak01, make);

        if (make.getType() == MakeConstant.MAKE_TYPE_CUSTOMIZE)
        {
            make.setId("C" + commonDAO2.getSquenceString20());
        }
        else
        {
            make.setId("S" + commonDAO2.getSquenceString20());
        }

        make.setCreaterId(user.getStafferId());

        mak01.setId(make.getId());

        make.setStatus(MakeConstant.MAKE_TOKEN_01);

        // 进入下一环节
        make.setPosition(12);

        make.setLogTime(TimeTools.now());

        makeDAO.saveEntityBean(make);

        make01DAO.saveEntityBean(mak01);

        MakeExtBean ext = new MakeExtBean();

        ext.setId(make.getId());

        // save empty MakeExtBean
        makeExtDAO.saveEntityBean(ext);

        String fid = make.getId();

        saveLog(user, fid, PublicConstant.OPERATION_SUBMIT, "11", "第1环产-业务员提交", "第一环产中职员提交到产中经理");

        return true;
    }

    /**
     * queryHistoryToken
     * 
     * @param user
     * @param mak02
     * @return
     * @throws MYException
     */
    public List<MakeTokenBean> queryHistoryToken(String makeId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(makeId);

        MakeBean make = makeDAO.find(makeId);

        if (make == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        Make01Bean make01 = make01DAO.find(makeId);

        if (make01 == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // TODO (定制产品和自有产品的路线不一样的)current status
        int status = make.getStatus();

        List<MakeTokenBean> result = null;

        if (make.getType() == MakeConstant.MAKE_TYPE_CUSTOMIZE)
        {
            result = makeTokenDAO.queryHistoryToken(status);
        }
        else
        {
            result = makeTokenDAO.querySelfHistoryToken(status);
        }

        // if APPRAISALTYPE_NO delete this token
        if (make01.getAppraisalType() == MakeConstant.APPRAISALTYPE_NO)
        {
            for (Iterator<MakeTokenBean> iterator = result.iterator(); iterator.hasNext();)
            {
                MakeTokenBean makeTokenBean = iterator.next();

                if (makeTokenBean.getId().equals(String.valueOf(MakeConstant.MAKE_TOKEN_02)))
                {
                    iterator.remove();
                }
            }
        }

        return result;
    }

    /**
     * addMake02
     * 
     * @param user
     * @param mak02
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean addMakeFile(User user, MakeFileBean makeFileBean)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, makeFileBean);

        MakeFileBean makeFile = makeFileDAO.findByPidAndTokenIdAndType(makeFileBean.getPid(),
            makeFileBean.getTokenId(), makeFileBean.getType());

        if (makeFile != null)
        {
            throw new MYException("附件已经存在,请确认操作");
        }

        return makeFileDAO.saveEntityBean(makeFileBean);
    }

    /**
     * updateMake01
     * 
     * @param user
     * @param mak01
     * @param make
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean updateMake01AndSubmit(User user, Make01Bean mak01, String handleId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, mak01, handleId);

        MakeBean make = checkUpdate(user, mak01.getId());

        make.setStatus(MakeConstant.MAKE_TOKEN_01);

        make.setHanderId(handleId);

        // 进入下一环节
        make.setPosition(12);

        makeDAO.updateEntityBean(make);

        make01DAO.updateEntityBean(mak01);

        String fid = make.getId();

        saveLog(user, fid, PublicConstant.OPERATION_SUBMIT, "11", "第1环产-业务员提交", "第一环产中职员提交到产中经理");

        return true;
    }

    /**
     * delMake
     * 
     * @param user
     * @param mak01
     * @param make
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean delMake(User user, String makeId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, makeId);

        MakeBean make = checkDel(user, makeId);

        makeDAO.deleteEntityBean(make.getId());

        make01DAO.deleteEntityBean(make.getId());

        makeExtDAO.deleteEntityBean(make.getId());

        makeFileDAO.deleteEntityBeansByFK(make.getId());

        logDAO.deleteEntityBeansByFK(make.getId());

        makeViewDAO.deleteEntityBeansByFK(make.getId());

        return true;
    }

    /**
     * checkDel
     * 
     * @param user
     * @param makeId
     * @return
     * @throws MYException
     */
    private MakeBean checkUpdate(User user, String makeId)
        throws MYException
    {
        return checkDel(user, makeId);
    }

    /**
     * checkDel
     * 
     * @param user
     * @param makeId
     * @return
     * @throws MYException
     */
    private MakeBean checkDel(User user, String makeId)
        throws MYException
    {
        MakeBean make = makeDAO.find(makeId);

        if (make == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !make.getCreaterId().equals(user.getStafferId()))
        {
            throw new MYException("不能删除他人的申请");
        }

        if ( ! (make.getStatus() == 1 && make.getPosition() == 11))
        {
            throw new MYException("只有初始的申请才能删除");
        }

        return make;
    }

    /**
     * pass
     * 
     * @param user
     * @param mak01
     * @param make
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean pass(User user, String makeId, String reason, String handerId)
        throws MYException
    {
        return passInner(user, makeId, reason, handerId);
    }

    /**
     * passInner
     * 
     * @param user
     * @param makeId
     * @param handerId
     * @return
     * @throws MYException
     */
    private boolean passInner(User user, String makeId, String reason, String handerId)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, makeId, handerId);

        MakeBean make = makeDAO.find(makeId);

        if (make == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        Make01Bean make01 = make01DAO.find(makeId);

        if (make01 == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !make.getHanderId().equals(user.getStafferId()))
        {
            throw new MYException("不能处理申请,请确认操作");
        }

        MakeTokenBean makeToken = makeTokenDAO.find(make.getStatus());

        if (makeToken == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        MakeTokenItemBean makeTokenItem = makeTokenItemDAO.find(make.getPosition());

        if (makeTokenItem == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        make.setHanderId(handerId);

        // handler end
        if (makeTokenItem.getEnds() == MakeConstant.END_TOKEN_YES)
        {
            int currentStatus = make.getStatus();
            // flow is end
            if (makeToken.getEnds() == MakeConstant.END_TOKEN_YES)
            {
                make.setStatus(MakeConstant.STATUS_END);

                make.setPosition(MakeConstant.STATUS_END);
            }
            // handler whether make does not need appraisal
            else if (currentStatus == MakeConstant.MAKE_TOKEN_01
                     && make01.getAppraisalType() == MakeConstant.APPRAISALTYPE_NO)
            {
                if (make.getType() == MakeConstant.MAKE_TYPE_SELF)
                {
                    make.setStatus(MakeConstant.MAKE_TOKEN_13);

                    make.setPosition(make.getStatus() * 10 + MakeConstant.TOKEN_BEGIN);
                }
                else
                {
                    make.setStatus(MakeConstant.MAKE_TOKEN_03);

                    make.setPosition(make.getStatus() * 10 + MakeConstant.TOKEN_BEGIN);
                }
            }
            // self flow to token 13
            else if (currentStatus == MakeConstant.MAKE_TOKEN_02
                     && make.getType() == MakeConstant.MAKE_TYPE_SELF)
            {
                make.setStatus(MakeConstant.MAKE_TOKEN_13);

                make.setPosition(make.getStatus() * 10 + MakeConstant.TOKEN_BEGIN);
            }
            else
            {
                // token is end
                make.setStatus(make.getStatus() + 1);

                make.setPosition(make.getStatus() * 10 + MakeConstant.TOKEN_BEGIN);
            }
        }
        else
        {
            make.setPosition(make.getPosition() + 1);
        }

        makeDAO.updateEntityBean(make);

        // log
        saveLog(user, make.getId(), PublicConstant.OPERATION_PASS, makeTokenItem.getId(),
            "第" + makeToken.getId() + "环产-" + makeTokenItem.getName(), reason);

        return true;
    }

    /**
     * reject(to token begin)
     * 
     * @param user
     * @param makeId
     * @param reason
     * @param handerId
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean reject(User user, String makeId, String reason)
        throws MYException
    {
        return rejectInner(user, makeId, reason);
    }

    /**
     * 异常结束
     * 
     * @param user
     * @param makeId
     * @param reason
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean exceptionEnd(User user, String makeId, int etype, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, makeId);

        MakeBean make = makeDAO.find(makeId);

        if (make == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !make.getHanderId().equals(user.getStafferId()))
        {
            throw new MYException("不能处理申请,请确认操作");
        }

        MakeTokenBean makeToken = makeTokenDAO.find(make.getStatus());

        if (makeToken == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        MakeTokenItemBean makeTokenItem = makeTokenItemDAO.find(make.getPosition());

        if (makeTokenItem == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if (makeTokenItem.getEnds() != MakeConstant.END_TOKEN_YES)
        {
            throw new MYException("不在定制客服的结束环,请确认操作");
        }

        // 设置结束
        make.setStatus(MakeConstant.STATUS_END);

        // 异常结束
        make.setEndType(etype);

        makeDAO.updateEntityBean(make);

        // log
        saveLog(user, make.getId(), PublicConstant.OPERATION_EXCEPTIONEND, makeTokenItem.getId(),
            "第" + makeToken.getId() + "环产-" + makeTokenItem.getName(), reason);

        return true;
    }

    /**
     * rejectTokenMake
     * 
     * @param user
     * @param makeId
     * @param tokenId
     * @param reason
     * @return
     * @throws MYException
     */
    @Transactional(rollbackFor = MYException.class)
    public boolean rejectTokenMake(User user, String makeId, int tokenId, String reason)
        throws MYException
    {
        return rejectTokenInner(user, makeId, tokenId, reason);
    }

    /**
     * rejectTokenInner(驳回的指定的环节的时候需要注意需要删除过程中的节点)
     * 
     * @param user
     * @param makeId
     * @param tokenId
     * @param reason
     * @return
     * @throws MYException
     */
    private boolean rejectTokenInner(User user, String makeId, int tokenId, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, makeId);

        MakeBean make = makeDAO.find(makeId);

        if (make == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !make.getHanderId().equals(user.getStafferId()))
        {
            throw new MYException("不能处理申请,请确认操作");
        }

        MakeTokenBean makeToken = makeTokenDAO.find(make.getStatus());

        if (makeToken == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        MakeTokenItemBean makeTokenItem = makeTokenItemDAO.find(make.getPosition());

        if (makeTokenItem == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        MakeTokenItemBean endToken = makeTokenItemDAO.findEndTokenByParentId(tokenId);

        if (endToken == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        LogBean nearLog = logDAO.findLogBeanByFKAndPosid(make.getId(), endToken.getId());

        if (nearLog == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        make.setHanderId(nearLog.getStafferId());

        make.setStatus(tokenId);

        // reject to token begin
        make.setPosition(CommonTools.parseInt(endToken.getId()));

        makeDAO.updateEntityBean(make);

        // delete token attachment
        List<MakeFileBean> makeFileList = makeFileDAO.queryByBetweenToken(make.getId(),
            (tokenId + 1), CommonTools.parseInt(makeToken.getId()));

        for (MakeFileBean makeFileBean : makeFileList)
        {
            makeFileDAO.deleteEntityBean(makeFileBean.getId());

            try
            {
                FileTools.delete(this.makeAttachmentRoot + makeFileBean.getPath());

                _logger.info("delete file success:" + this.makeAttachmentRoot
                             + makeFileBean.getPath());
            }
            catch (IOException e)
            {
                _logger.error(e, e);
            }
        }

        // log
        saveLog(user, make.getId(), PublicConstant.OPERATION_REJECT, makeTokenItem.getId(),
            "第" + makeToken.getId() + "环产-" + makeTokenItem.getName(), reason);

        return true;
    }

    /**
     * rejectInner
     * 
     * @param user
     * @param makeId
     * @param reason
     * @param handerId
     * @return
     * @throws MYException
     */
    private boolean rejectInner(User user, String makeId, String reason)
        throws MYException
    {
        JudgeTools.judgeParameterIsNull(user, makeId);

        MakeBean make = makeDAO.find(makeId);

        if (make == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        if ( !make.getHanderId().equals(user.getStafferId()))
        {
            throw new MYException("不能处理申请,请确认操作");
        }

        MakeTokenBean makeToken = makeTokenDAO.find(make.getStatus());

        if (makeToken == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        MakeTokenItemBean makeTokenItem = makeTokenItemDAO.find(make.getPosition());

        if (makeTokenItem == null)
        {
            throw new MYException("数据错误,请确认操作");
        }

        // if current token is one,reject means return to init
        if (make.getStatus() == MakeConstant.MAKE_TOKEN_01)
        {
            LogBean nearLog = logDAO.findLogBeanByFKAndPosid(make.getId(),
                String.valueOf(make.getStatus() * 10 + 1));

            if (nearLog == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            make.setHanderId(nearLog.getStafferId());

            make.setStatus(make.getStatus());

            // reject to token begin
            make.setPosition(make.getStatus() * 10 + 1);
        }
        else
        {
            // if current token is not one,reject means return to upper token
            List<MakeTokenBean> historyTokenList = queryHistoryToken(make.getId());

            // find nearest token
            MakeTokenBean nearestToken = historyTokenList.get(historyTokenList.size() - 1);

            int nearestStatus = CommonTools.parseInt(nearestToken.getId());

            // find token end item
            MakeTokenItemBean nearestEnd = makeTokenItemDAO.findEndTokenByParentId(nearestStatus);

            LogBean nearLog = logDAO.findLogBeanByFKAndPosid(make.getId(), nearestEnd.getId());

            if (nearLog == null)
            {
                throw new MYException("数据错误,请确认操作");
            }

            make.setHanderId(nearLog.getStafferId());

            make.setStatus(nearestStatus);

            make.setPosition(CommonTools.parseInt(nearestEnd.getId()));

            // delete current token attachment
            List<MakeFileBean> makeFileList = makeFileDAO.queryByPidAndTokenId(make.getId(),
                CommonTools.parseInt(makeToken.getId()));

            for (MakeFileBean makeFileBean : makeFileList)
            {
                makeFileDAO.deleteEntityBean(makeFileBean.getId());

                try
                {
                    FileTools.delete(this.makeAttachmentRoot + makeFileBean.getPath());

                    _logger.info("delete file success:" + this.makeAttachmentRoot
                                 + makeFileBean.getPath());
                }
                catch (IOException e)
                {
                    _logger.error(e, e);
                }
            }
        }

        makeDAO.updateEntityBean(make);

        // log
        saveLog(user, make.getId(), PublicConstant.OPERATION_REJECT, makeTokenItem.getId(),
            "第" + makeToken.getId() + "环产-" + makeTokenItem.getName(), reason);

        return true;
    }

    /**
     * saveLog
     * 
     * @param user
     * @param fid
     * @param operation
     * @param position
     * @param logs
     */
    private void saveLog(User user, String fid, String operation, String posid, String position,
                         String logs)
    {
        LogBean log = new LogBean();

        log.setStafferId(user.getStafferId());

        log.setFkId(fid);

        log.setLocationId(user.getLocationId());

        log.setLogTime(TimeTools.now());

        log.setModule(ModuleConstant.MODULE_MAKE);

        log.setOperation(operation);

        log.setPosid(posid);

        log.setPosition(position);

        log.setLog(logs);

        logDAO.saveEntityBean(log);

        saveViewLog(user, fid);
    }

    /**
     * saveViewLog
     * 
     * @param user
     * @param fid
     */
    private void saveViewLog(User user, String fid)
    {
        // save view
        MakeViewBean viewLog = new MakeViewBean();

        viewLog.setMakeId(fid);

        viewLog.setStafferId(user.getStafferId());

        viewLog.setLogTime(TimeTools.now());

        MakeBean make = makeDAO.find(fid);

        if (make != null)
        {
            viewLog.setCreaterId(make.getCreaterId());
        }

        MakeViewBean old = makeViewDAO.findByUnique(viewLog.getStafferId(), viewLog.getMakeId());

        if (old == null)
        {
            makeViewDAO.saveEntityBean(viewLog);
        }
        else
        {
            viewLog.setId(old.getId());

            makeViewDAO.updateEntityBean(viewLog);
        }
    }

    /**
     * @return the makeDAO
     */
    public MakeDAO getMakeDAO()
    {
        return makeDAO;
    }

    /**
     * @param makeDAO
     *            the makeDAO to set
     */
    public void setMakeDAO(MakeDAO makeDAO)
    {
        this.makeDAO = makeDAO;
    }

    /**
     * @return the make01DAO
     */
    public Make01DAO getMake01DAO()
    {
        return make01DAO;
    }

    /**
     * @param make01DAO
     *            the make01DAO to set
     */
    public void setMake01DAO(Make01DAO make01DAO)
    {
        this.make01DAO = make01DAO;
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
     * @return the logDAO
     */
    public LogDAO getLogDAO()
    {
        return logDAO;
    }

    /**
     * @param logDAO
     *            the logDAO to set
     */
    public void setLogDAO(LogDAO logDAO)
    {
        this.logDAO = logDAO;
    }

    /**
     * @return the makeTokenDAO
     */
    public MakeTokenDAO getMakeTokenDAO()
    {
        return makeTokenDAO;
    }

    /**
     * @param makeTokenDAO
     *            the makeTokenDAO to set
     */
    public void setMakeTokenDAO(MakeTokenDAO makeTokenDAO)
    {
        this.makeTokenDAO = makeTokenDAO;
    }

    /**
     * @return the makeTokenItemDAO
     */
    public MakeTokenItemDAO getMakeTokenItemDAO()
    {
        return makeTokenItemDAO;
    }

    /**
     * @param makeTokenItemDAO
     *            the makeTokenItemDAO to set
     */
    public void setMakeTokenItemDAO(MakeTokenItemDAO makeTokenItemDAO)
    {
        this.makeTokenItemDAO = makeTokenItemDAO;
    }

    /**
     * @return the makeAttachmentRoot
     */
    public String getMakeAttachmentRoot()
    {
        return makeAttachmentRoot;
    }

    /**
     * @param makeAttachmentRoot
     *            the makeAttachmentRoot to set
     */
    public void setMakeAttachmentRoot(String makeAttachmentRoot)
    {
        this.makeAttachmentRoot = makeAttachmentRoot;
    }

    /**
     * @return the makeExtDAO
     */
    public MakeExtDAO getMakeExtDAO()
    {
        return makeExtDAO;
    }

    /**
     * @param makeExtDAO
     *            the makeExtDAO to set
     */
    public void setMakeExtDAO(MakeExtDAO makeExtDAO)
    {
        this.makeExtDAO = makeExtDAO;
    }

    /**
     * @return the makeFileDAO
     */
    public MakeFileDAO getMakeFileDAO()
    {
        return makeFileDAO;
    }

    /**
     * @param makeFileDAO
     *            the makeFileDAO to set
     */
    public void setMakeFileDAO(MakeFileDAO makeFileDAO)
    {
        this.makeFileDAO = makeFileDAO;
    }

    /**
     * @return the makeViewDAO
     */
    public MakeViewDAO getMakeViewDAO()
    {
        return makeViewDAO;
    }

    /**
     * @param makeViewDAO
     *            the makeViewDAO to set
     */
    public void setMakeViewDAO(MakeViewDAO makeViewDAO)
    {
        this.makeViewDAO = makeViewDAO;
    }
}
