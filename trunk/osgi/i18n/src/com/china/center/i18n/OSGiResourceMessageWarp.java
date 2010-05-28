package com.china.center.i18n;

/**
 * OSGiResourceMessageWarp
 * 
 * @author ZHUZHU
 * @version 2010-1-13
 * @see OSGiResourceMessageWarp
 * @since 1.0
 */
public interface OSGiResourceMessageWarp
{
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
    void destroy();
}
