package com.china.center.i18n;

import java.util.Locale;

/**
 * 
 * MessageS
 * 
 * @author Administrator
 * @version 
 * @see com.china.center.i18n
 * @since
 */
public interface MessageInterface
{
    /**
     * 
     * Description: <br>
     * Implement: <br>
     * @param code String
     * @param locale Locale
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    String getMessageByLocale(String code, Locale locale);
    
    /**
     * 
     * Description: <br>
     * Implement: <br>
     * 1、… <br>
     * 2、… <br>
     * [参数列表，说明每个参数用途]
     * 
     * @param  code String
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    String getMessage(String code);
    
    /**
     * 
     * Description: <br>
     * Implement: <br>
     * 1、… <br>
     * 2、… <br>
     * [参数列表，说明每个参数用途]
     * 
     * @param  code String
     * @param  arg0 []object
     * @return String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    String getMessageByParamter(String code, Object[] arg0);
    
    /** 
     * getMessage
     *
     * @param code
     * @param locale
     * @param arg0
     * @return 
     */
    String getMessageByFull(String code, Locale locale, Object[] arg0);
    
    /** 
     * setLocale
     *
     * @param locale 
     */
    void setLocale(String locale);
    
    /** 
     * setBasenames
     *
     * @param basenames 
     */
    void setBasenames(String[] basenames);
    
    /** 
     * removeBasenames
     *
     * @param basenames 
     */
    void removeBasenames(String[] basenames);
}
