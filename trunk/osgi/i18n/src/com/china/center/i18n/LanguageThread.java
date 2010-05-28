package com.china.center.i18n;


import java.util.Locale;


/**
 * 获取当前语言的ThreadLocal
 * 
 * @author ZHUZHU
 * @version 2008-7-4
 * @see LanguageThread
 * @since
 */

public class LanguageThread
{
    private static final ThreadLocal THREADLOCAL = new ThreadLocal();

    /**
     * Description: <br>
     * 1、设置当前语言 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     * 
     * @param locale
     *            当前语言locale
     * @return
     * @see
     */
    public static void putLocale(Locale locale)
    {
        THREADLOCAL.set(locale);
    }

    /**
     * Description: <br>
     * 1、获取当前语言locale…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     * 
     * @param
     * @return Locale 当前设置的语言Locale
     * @see
     */
    public static Locale getLocale()
    {
        Locale temp = (Locale)THREADLOCAL.get();
        if (temp == null)
        {
            String languageSet = getLanguageSet();

            if (languageSet.equals("0"))
            {
                temp = new Locale("zh", "CN");
            }
            else
            {
                temp = new Locale("en", "US");
            }
        }
        return temp;
    }

    /**
     * Description: <br>
     * 1、获取config.xml中配置的语言…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     * 
     * @param
     * @return String 当前语言 "zh" "en"
     * @see
     */
    public static String getLanguageSet()
    {
        // return ResourceTools.getProperty(LANGUAGESET, "1");
        return "0";
    }
}
