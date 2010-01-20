/**
 *
 */
package com.china.centet.yongyin.vo;


import java.util.List;

import com.china.center.annotation.Entity;
import com.china.center.annotation.Ignore;
import com.china.center.annotation.Relationship;
import com.china.centet.yongyin.bean.FlowDefineBean;


/**
 * @author Administrator
 */
@Entity(inherit = true, cache = true)
public class FlowDefineBeanVO extends FlowDefineBean
{
    @Relationship(relationField = "userId", tagField = "stafferName")
    private String userName = "";

    @Ignore
    private List<FlowTokenBeanVO> tokensVO = null;

    @Ignore
    private List<FlowViewerBeanVO> viewsVO = null;

    /**
     *
     */
    public FlowDefineBeanVO()
    {}

    /**
     * @return the userName
     */
    public String getUserName()
    {
        return userName;
    }

    /**
     * @param userName
     *            the userName to set
     */
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    /**
     * @return the tokensVO
     */
    public List<FlowTokenBeanVO> getTokensVO()
    {
        return tokensVO;
    }

    /**
     * @param tokensVO
     *            the tokensVO to set
     */
    public void setTokensVO(List<FlowTokenBeanVO> tokensVO)
    {
        this.tokensVO = tokensVO;
    }

    /**
     * @return the viewsVO
     */
    public List<FlowViewerBeanVO> getViewsVO()
    {
        return viewsVO;
    }

    /**
     * @param viewsVO
     *            the viewsVO to set
     */
    public void setViewsVO(List<FlowViewerBeanVO> viewsVO)
    {
        this.viewsVO = viewsVO;
    }
}
