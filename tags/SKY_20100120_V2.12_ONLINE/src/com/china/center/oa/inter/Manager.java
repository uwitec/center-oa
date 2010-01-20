/**
 * File Name: Manager.java<br>
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
 * Common Manager
 * 
 * @author zhuzhu
 * @version 2009-4-15
 * @see Manager
 * @since 1.0
 */
public interface Manager<Bean extends Serializable>
{
    boolean addBean(User user, Bean bean)
        throws MYException;

    boolean updateBean(User user, Bean bean)
        throws MYException;

    boolean deleteBean(User user, Serializable id)
        throws MYException;
}
