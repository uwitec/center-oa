package com.center.china.osgi.config.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * ResourceLocator
 *  
 * @author  ZHUZHU
 * @version  2009-7-23
 * @see  ResourceLocator
 * @since  1.0
 */
public class ResourceLocator
{
    
    /**
     * CLASSPATH_URL_PREFIX
     */
    public static final String CLASSPATH_URL_PREFIX = "classpath:";
    
    /**
     * getResource
     * @param location location
     * @return InputStream
     * @throws FileNotFoundException InputStream 
     */
    public static InputStream getResource(String location)
            throws FileNotFoundException
    {
        if (location == null)
        {
            throw new FileNotFoundException("Filename could not be NULL");
        }
        if (location.startsWith(CLASSPATH_URL_PREFIX))
        {
            String path = location.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader classLoader = Thread.currentThread()
                    .getContextClassLoader();
            InputStream is = null;
            is = classLoader.getResourceAsStream(path);
            
            if (is == null)
            {
                throw new FileNotFoundException(path
                        + " cannot be opened because it does not exist");
            }
            return is;
        }
        else
        {
            
            try
            {
                URL url = new URL(location);
                File file = new File(url.getFile());
                if (file.exists())
                {
                    return new FileInputStream(file);
                }
                else
                {
                    throw new FileNotFoundException(location
                            + " cannot be opened because it does not exist");
                }
            }
            catch (MalformedURLException e)
            {
                if (location.startsWith("/"))
                {
                    location = location.substring(1);
                }
                File file = new File(location);
                if (file.exists())
                {
                    return new FileInputStream(file);
                }
                else
                {
                    throw new FileNotFoundException(location
                            + " cannot be opened because it does not exist");
                }
            }
            
        }
    }
}
