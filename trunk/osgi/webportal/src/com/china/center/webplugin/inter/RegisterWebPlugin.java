package com.china.center.webplugin.inter;


import java.io.InputStream;


/**
 * RegisterWebPlugin
 * 
 * @author ZHUZHU
 * @version 2009-8-11
 * @see RegisterWebPlugin
 * @since 1.0
 */
public interface RegisterWebPlugin
{

    /**
     * registerWebResource
     * 
     * @param webPluginName
     * @param filePath
     * @param webResource
     * @return
     */
    boolean registerWebResource(String webPluginName, String filePath, InputStream webResource);

    /**
     * removeWebResource
     * 
     * @param webPluginName
     * @return
     */
    boolean removeWebResource(String webPluginName);
}
