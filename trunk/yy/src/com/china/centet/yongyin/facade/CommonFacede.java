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

    /**
     * login2
     * 
     * @param name
     * @param password
     * @param key
     * @return
     */
    User login2(String name, String password, String rand, String key, String randKey);
}
