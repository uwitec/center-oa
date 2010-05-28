package com.china.center.i18n;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.NoSuchMessageException;

/**
 * IISS消息类，提供获取国际化资源的方法 <br>
 * 需要通过newInstance()方法取得实例
 * 
 * @author Administrator
 * @version
 * @see com.china.center.i18n
 * @since
 */
public class OSGiResourceMessage extends ResourceMoreBundleMessageSource implements
        MessageInterface
{
    /**
     * LOGGER
     */
    private static final Log LOGGER = LogFactory.getLog(OSGiResourceMessage.class);
    
    private static OSGiResourceMessage message = null;
    
    private Locale locale = new Locale("zh", "CN");
    
    /**
     * 
     * Description: <br>
     * Implement: <br>
     * 1、… <br>
     * 2、… <br>
     * [参数列表，说明每个参数用途]
     * 
     * @param
     * @return MessageS
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public static MessageInterface newInstance()
    {
        if (OSGiResourceMessage.message == null)
        {
            LOGGER.error("IissMessage.message is null,system create a new,but this object may not support enough message resource");
            
            OSGiResourceMessage.message = new OSGiResourceMessage();
        }
        
        return OSGiResourceMessage.message;
    }
    
    /**
     * 
     * Description: <br>
     * Implement: <br>
     * 1、… <br>
     * 2、… <br>
     * [参数列表，说明每个参数用途]
     * 
     * @param locale
     *            String
     * @exception/throws [违例类型] [违例说明]
     * @see [类、类#方法、类#成员]
     */
    public void setLocale(String locale)
    {
        if (locale != null)
        {
            final String[] str = locale.split("_");
            
            // 构建新的本地实体
            this.locale = new Locale(str[0], str[1]);
        }
        
        message = this;
    }
    
    /** 
     * setAllLocale
     *
     * @param locales 
     */
    public synchronized void setAllLocale(final String[] locales)
    {
        if (locales != null)
        {
            for (int i = 0; i < locales.length; i++)
            {
                final String[] str = locales[i].split("_");
                
                setAllLocaleList(new ArrayList<Locale>()
                {
                    {
                        add(new Locale(str[0], str[1]));
                    }
                });
            }
        }
    }
    
    /**
     * Description: <br>
     * 
     * @param code
     *            String
     * @return code
     */
    public String getMessage(String code)
    {
        return this.getMessageByParamter(code, (Object[]) null);
    }
    
    /**
     * Description: <br>
     * 
     * @param code
     *            String
     * @param args
     *            Object[]
     * @return code,args
     */
    public String getMessageByParamter(String code, Object[] args)
    {
       return getMessageByFull(code, this.locale, args);
    }
    
    /**
     * Description: <br>
     * 
     * @param code
     *            String
     * @param locale
     *            Locale
     * @return String
     */
    public String getMessageByLocale(String code, Locale locale)
    {
        try
        {
            return this.getMessage(code, null, locale);
        }
        catch (NoSuchMessageException e)
        {
            LOGGER.error("---------------------MISS message:" + code);
            
            return code;
        }
    }
    
    /** 
     * locaLanguage
     *
     * @return 
     */
    public Locale locaLanguage()
    {
        return LanguageThread.getLocale();
    }

    public String getMessageByFull(String code, Locale locale, Object[] arg0)
    {
        try
        {
            return this.getMessage(code, arg0, locale == null ? locaLanguage()
                    : locale);
        }
        catch (NoSuchMessageException e)
        {
            LOGGER.error("---------------------MISS message:" + code);
            
            return code;
        }
    }
}
