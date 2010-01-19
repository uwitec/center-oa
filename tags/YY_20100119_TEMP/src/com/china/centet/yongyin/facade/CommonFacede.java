/**
 *
 */
package com.china.centet.yongyin.facade;


import com.china.centet.yongyin.bean.User;


/**
 * @author Administrator
 */
public interface CommonFacede
{
    /**
     * µÇÂ¼½Ó¿Ú
     * 
     * @param name
     * @param password
     * @return
     */
    User login(String name, String password);
}
