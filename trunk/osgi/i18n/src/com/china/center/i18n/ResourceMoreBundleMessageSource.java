/*
 * Copyright 2002-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.china.center.i18n;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.context.support.AbstractMessageSource;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * WarpResourceBundleMessageSource<br>
 * (modify org.springframework.context.support.ResourceBundleMessageSource)
 *  
 * @author  ZHUZHU
 * @version  2010-1-13
 * @see  ResourceMoreBundleMessageSource
 * @since  1.0
 */
public class ResourceMoreBundleMessageSource extends AbstractMessageSource
        implements BeanClassLoaderAware
{
    /**
     * LOGGER
     */
    private static final Log LOGGER = LogFactory.getLog(ResourceMoreBundleMessageSource.class);
    
    protected List<String> basenames = new ArrayList();
    
    protected List<Locale> allLocaleList = new ArrayList();
    
    protected ClassLoader bundleClassLoader;
    
    protected ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
    
    /**
     * Cache to hold loaded ResourceBundles.
     * This Map is keyed with the bundle basename, which holds a Map that is
     * keyed with the Locale and in turn holds the ResourceBundle instances.
     * This allows for very efficient hash lookups, significantly faster
     * than the ResourceBundle class's own cache.
     */
    private final Map cachedResourceBundles = new HashMap();
    
    /**
     * load BASENAME form special bundles
     */
    private final Map cachedBasenamesBundles = new HashMap();
    
    /**
     * Cache to hold already generated MessageFormats.
     * This Map is keyed with the ResourceBundle, which holds a Map that is
     * keyed with the message code, which in turn holds a Map that is keyed
     * with the Locale and holds the MessageFormat values. This allows for
     * very efficient hash lookups without concatenated keys.
     * @see #getMessageFormat
     */
    private final Map cachedBundleMessageFormats = new HashMap();
    
    /**
     * Set a single basename, following {@link java.util.ResourceBundle} conventions:
     * essentially, a fully-qualified classpath location. If it doesn't contain a
     * package qualifier (such as <code>org.mypackage</code>), it will be resolved
     * from the classpath root.
     * <p>Messages will normally be held in the "/lib" or "/classes" directory of
     * a web application's WAR structure. They can also be held in jar files on
     * the class path.
     * <p>Note that ResourceBundle names are effectively classpath locations: As a
     * consequence, the JDK's standard ResourceBundle treats dots as package separators.
     * This means that "test.theme" is effectively equivalent to "test/theme",
     * just like it is for programmatic <code>java.util.ResourceBundle</code> usage.
     * @see #setBasenames
     * @see java.util.ResourceBundle#getBundle(String)
     */
    public synchronized void setBasename(String basename)
    {
        setBasenames(new String[] { basename });
    }
    
    /**
     * Set an array of basenames, each following {@link java.util.ResourceBundle}
     * conventions: essentially, a fully-qualified classpath location. If it
     * doesn't contain a package qualifier (such as <code>org.mypackage</code>),
     * it will be resolved from the classpath root.
     * <p>The associated resource bundles will be checked sequentially
     * when resolving a message code. Note that message definitions in a
     * <i>previous</i> resource bundle will override ones in a later bundle,
     * due to the sequential lookup.
     * <p>Note that ResourceBundle names are effectively classpath locations: As a
     * consequence, the JDK's standard ResourceBundle treats dots as package separators.
     * This means that "test.theme" is effectively equivalent to "test/theme",
     * just like it is for programmatic <code>java.util.ResourceBundle</code> usage.
     * @see #setBasename
     * @see java.util.ResourceBundle#getBundle(String)
     */
    public synchronized void setBasenames(String[] basenames)
    {
        if (basenames != null)
        {
            for (int i = 0; i < basenames.length; i++)
            {
                String basename = basenames[i].trim();
                Assert.hasText(basename, "Basename must not be empty");
                
                //不能重复加载
                if (!this.basenames.contains(basename))
                {
                    LOGGER.info("Add message resource:" + basename);
                    
                    //追加
                    this.basenames.add(basename);
                    
                    //获得当前的类加载器
                    cachedBasenamesBundles.put(basename, Thread.currentThread()
                            .getContextClassLoader());
                }
            }
        }
    }
    
    /** 
     * destroy(全部销毁)
     * 
     */
    public synchronized void destroy()
    {
        Object[] array = this.basenames.toArray();
        
        for (Object object : array)
        {
            removeBasenames(new String[] { object.toString() });
        }
    }
    
    /** 
     * 卸载资源
     *
     * @param basenames 
     */
    public synchronized void removeBasenames(String[] basenames)
    {
        if (basenames != null)
        {
            for (int i = 0; i < basenames.length; i++)
            {
                String basename = basenames[i].trim();
                
                LOGGER.info("Remove message resource:" + basename);
                
                this.basenames.remove(basename);
                
                //销毁类加载的缓存
                this.cachedBasenamesBundles.remove(basename);
                
                //获得当前的类加载器
                cachedBasenamesBundles.remove(basename);
                
                Map localeMap = (Map) this.cachedResourceBundles.get(basename);
                
                if (localeMap != null)
                {
                    for (Locale eachLocale : allLocaleList)
                    {
                        ResourceBundle bundle = (ResourceBundle) localeMap.get(eachLocale);
                        
                        if (bundle != null)
                        {
                            //销毁cachedBundleMessageFormats的缓存
                            this.cachedBundleMessageFormats.remove(bundle);
                        }
                    }
                }
                
                //销毁cachedResourceBundles的缓存
                this.cachedResourceBundles.remove(basename);
            }
        }
    }
    
    /**
     * Set the ClassLoader to load resource bundles with.
     * <p>Default is the containing BeanFactory's
     * {@link org.springframework.beans.factory.BeanClassLoaderAware bean ClassLoader},
     * or the default ClassLoader determined by
     * {@link org.springframework.util.ClassUtils#getDefaultClassLoader()}
     * if not running within a BeanFactory.
     */
    public void setBundleClassLoader(ClassLoader classLoader)
    {
        this.bundleClassLoader = classLoader;
    }
    
    /**
     * Return the ClassLoader to load resource bundles with.
     * <p>Default is the containing BeanFactory's bean ClassLoader.
     * @see #setBundleClassLoader
     */
    protected ClassLoader getBundleClassLoader(String basename)
    {
        Object cacheClassLoader = cachedBasenamesBundles.get(basename);
        
        //获取bundle的ClassLoader
        if (cacheClassLoader != null)
        {
            return (ClassLoader) cacheClassLoader;
        }
        
        return (this.bundleClassLoader != null ? this.bundleClassLoader
                : this.beanClassLoader);
    }
    
    public void setBeanClassLoader(ClassLoader classLoader)
    {
        this.beanClassLoader = (classLoader != null ? classLoader
                : ClassUtils.getDefaultClassLoader());
    }
    
    /**
     * Resolves the given message code as key in the registered resource bundles,
     * returning the value found in the bundle as-is (without MessageFormat parsing).
     */
    protected String resolveCodeWithoutArguments(String code, Locale locale)
    {
        String result = null;
        for (int i = 0; result == null && i < this.basenames.size(); i++)
        {
            ResourceBundle bundle = getResourceBundle(this.basenames.get(i),
                    locale);
            if (bundle != null)
            {
                result = getStringOrNull(bundle, code);
            }
        }
        return result;
    }
    
    /**
     * Resolves the given message code as key in the registered resource bundles,
     * using a cached MessageFormat instance per message code.
     */
    protected MessageFormat resolveCode(String code, Locale locale)
    {
        MessageFormat messageFormat = null;
        for (int i = 0; messageFormat == null && i < this.basenames.size(); i++)
        {
            ResourceBundle bundle = getResourceBundle(this.basenames.get(i),
                    locale);
            if (bundle != null)
            {
                messageFormat = getMessageFormat(bundle, code, locale);
            }
        }
        return messageFormat;
    }
    
    /**
     * Return a ResourceBundle for the given basename and code,
     * fetching already generated MessageFormats from the cache.
     * @param basename the basename of the ResourceBundle
     * @param locale the Locale to find the ResourceBundle for
     * @return the resulting ResourceBundle, or <code>null</code> if none
     * found for the given basename and Locale
     */
    protected ResourceBundle getResourceBundle(String basename, Locale locale)
    {
        synchronized (this.cachedResourceBundles)
        {
            Map localeMap = (Map) this.cachedResourceBundles.get(basename);
            if (localeMap != null)
            {
                ResourceBundle bundle = (ResourceBundle) localeMap.get(locale);
                if (bundle != null)
                {
                    return bundle;
                }
            }
            try
            {
                ResourceBundle bundle = doGetBundle(basename, locale);
                if (localeMap == null)
                {
                    localeMap = new HashMap();
                    this.cachedResourceBundles.put(basename, localeMap);
                }
                localeMap.put(locale, bundle);
                return bundle;
            }
            catch (MissingResourceException ex)
            {
                if (logger.isWarnEnabled())
                {
                    logger.warn("ResourceBundle [" + basename
                            + "] not found for MessageSource: "
                            + ex.getMessage());
                }
                // Assume bundle not found
                // -> do NOT throw the exception to allow for checking parent message source.
                return null;
            }
        }
    }
    
    /**
     * Obtain the resource bundle for the given basename and Locale.
     * @param basename the basename to look for
     * @param locale the Locale to look for
     * @return the corresponding ResourceBundle
     * @throws MissingResourceException if no matching bundle could be found
     * @see java.util.ResourceBundle#getBundle(String, java.util.Locale, ClassLoader)
     * @see #getBundleClassLoader()
     */
    protected ResourceBundle doGetBundle(String basename, Locale locale)
            throws MissingResourceException
    {
        return ResourceBundle.getBundle(basename,
                locale,
                getBundleClassLoader(basename));
    }
    
    /**
     * Return a MessageFormat for the given bundle and code,
     * fetching already generated MessageFormats from the cache.
     * @param bundle the ResourceBundle to work on
     * @param code the message code to retrieve
     * @param locale the Locale to use to build the MessageFormat
     * @return the resulting MessageFormat, or <code>null</code> if no message
     * defined for the given code
     * @throws MissingResourceException if thrown by the ResourceBundle
     */
    protected MessageFormat getMessageFormat(ResourceBundle bundle,
            String code, Locale locale) throws MissingResourceException
    {
        synchronized (this.cachedBundleMessageFormats)
        {
            Map codeMap = (Map) this.cachedBundleMessageFormats.get(bundle);
            Map localeMap = null;
            if (codeMap != null)
            {
                localeMap = (Map) codeMap.get(code);
                if (localeMap != null)
                {
                    MessageFormat result = (MessageFormat) localeMap.get(locale);
                    if (result != null)
                    {
                        return result;
                    }
                }
            }
            
            String msg = getStringOrNull(bundle, code);
            if (msg != null)
            {
                if (codeMap == null)
                {
                    codeMap = new HashMap();
                    this.cachedBundleMessageFormats.put(bundle, codeMap);
                }
                if (localeMap == null)
                {
                    localeMap = new HashMap();
                    codeMap.put(code, localeMap);
                }
                MessageFormat result = createMessageFormat(msg, locale);
                localeMap.put(locale, result);
                return result;
            }
            
            return null;
        }
    }
    
    private String getStringOrNull(ResourceBundle bundle, String key)
    {
        try
        {
            return bundle.getString(key);
        }
        catch (MissingResourceException ex)
        {
            // Assume key not found
            // -> do NOT throw the exception to allow for checking parent message source.
            return null;
        }
    }
    
    /**
     * Show the configuration of this MessageSource.
     */
    public String toString()
    {
        return getClass().getName() + ": basenames=[" + this.basenames + "]";
    }
    
    /**
     * set allLocaleList
     * @param allLocaleList the value of allLocaleList
     */
    public void setAllLocaleList(List<Locale> allLocaleList)
    {
        this.allLocaleList.addAll(allLocaleList);
    }
    
}
