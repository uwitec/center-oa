/**
 * File Name: AbstractManager.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2009-4-15<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.inter;


import java.io.Serializable;

import com.china.center.common.MYException;
import com.china.center.oa.publics.User;


/**
 * AbstractManager
 * 
 * @author zhuzhu
 * @version 2009-4-15
 * @see AbstractManager
 * @since 1.0
 */
public abstract class AbstractManager<Bean extends Serializable> implements Manager<Bean>
{
    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.inter.Manager#addBean(com.china.center.oa.publics.bean.User, java.io.Serializable)
     */
    public abstract boolean addBean(User user, Bean bean)
        throws MYException;

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.inter.Manager#deleteBean(com.china.center.oa.publics.bean.User, java.io.Serializable)
     */
    public abstract boolean deleteBean(User user, Serializable id)
        throws MYException;

    /*
     * (non-Javadoc)
     * 
     * @see com.china.center.oa.inter.Manager#updateBean(com.china.center.oa.publics.bean.User, java.io.Serializable)
     */
    public abstract boolean updateBean(User user, Bean bean)
        throws MYException;
}
