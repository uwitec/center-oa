package com.china.center.tools;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * 资源定位
 *
 * @author zhuzhu
 * @version 2007-7-23
 * @see ResourceLocator
 * @since
 */
public class ResourceLocator
{

    public static final String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     * 取得配置文件的InputStream
     * Description: 配置文件可以用classpath开头，使用类路径加载文件，也可以直接使用file:开头使用相对（绝对）路径
     * 该方法就是区分这两个不同的加载方法，来获取资源的InputStream<br>
     *
     * @param location
     * @return InputStream
     * @throws FileNotFoundException  
     */
    public static InputStream getResource(String location)
        throws FileNotFoundException
    {
        if (location == null)
        {
            throw new FileNotFoundException("Filename could not be NULL");
        }
        //处理classpath:开头的路径
        if (location.startsWith(CLASSPATH_URL_PREFIX))
        {
            String path = location.substring(CLASSPATH_URL_PREFIX.length());
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
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
                //处理以file:开头的路径
                URL url = new URL(location);
                File file = new File(url.getFile());
                if (file.exists())
                {
                    return new FileInputStream(file);
                }
                else
                {
                    throw new FileNotFoundException(
                        location + " cannot be opened because it does not exist");
                }
            }
            catch (MalformedURLException e)
            {
                //进入这个分支是因为文件位置既没有以file:开头，也没有以classpath:开头,直接用文件尝试读取
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
                    throw new FileNotFoundException(
                        location + " cannot be opened because it does not exist");
                }
            }

        }
    }
}
