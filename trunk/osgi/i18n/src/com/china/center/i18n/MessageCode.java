package com.china.center.i18n;

import java.util.Locale;


/**
 * 
 * MessageCode
 * 
 * @author Administrator
 * @version 
 * @see com.china.center.i18n
 * @since
 */
public class MessageCode
{
    /**
     * Description: <br>
     * @brief 动态获取中英文的资源文件的国际化信息
     * @param code
     * 			  国际化信息的KEY
     * 
     * @return 资源文件中代码代表的国际化信息
     */
    public static String getPageMessage(String code)
    {
        if (code == null || "".equals(code.trim()))
        {
            return "";
        }
        
        return OSGiResourceMessage.newInstance().getMessageByLocale(code,
                LanguageThread.getLocale());
    }
    
    /** 
     * getPageMessage
     *
     * @param code
     * @param locale
     * @return 
     */
    public static String getPageMessage(String code, Locale locale)
    {
        if (code == null || "".equals(code.trim()))
        {
            return "";
        }
        
        return OSGiResourceMessage.newInstance().getMessageByLocale(code,
                locale);
    }
    
    /**
     * Description: <br>
     * @brief 动态获取中英文的资源文件的国际化信息
     * @param code
     *            国际化信息的KEY
     * @param args
     *            国际化信息中KEY的参数
     * @return 资源文件中代码代表的国际化信息
     */
    public static String getPageMessage(String code, Object[] args)
    {
        if (code == null || "".equals(code.trim()))
        {
            return "";
        }
        return OSGiResourceMessage.newInstance().getMessageByParamter(code, args);
    }
}
