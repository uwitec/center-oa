/**
 * File Name: Activator.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * CREATER: ZHUACHEN<br>
 * CreateTime: 2011-3-26<br>
 * Grant: open source to everybody
 */
package com.china.center.webplugin.init;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import com.china.center.tools.StringTools;
import com.china.center.webplugin.inter.RegisterWebPlugin;


/**
 * Activator
 * 
 * @author ZHUZHU
 * @version 2011-3-26
 * @see Activator
 * @since 3.0
 */
public class Activator implements BundleActivator
{
    private static ScanResource scanResource = null;

    public void start(final BundleContext context)
        throws Exception
    {
        // 获取工作目录下的所有的资源文件
        final String devEnv = System.getProperty("osgi.devEnv");

        if ( !"true".equalsIgnoreCase(devEnv))
        {
            System.out.println("Exit scan resource 1");

            return;
        }

        final String workspace = System.getProperty("osgi.workspace");

        if (StringTools.isNullOrNone(workspace))
        {
            System.out.println("Exit scan resource 2");

            return;
        }

        new Thread()
        {
            public void run()
            {
                RegisterWebPlugin server = null;

                ServiceReference reference = null;

                while (server == null)
                {
                    reference = context.getServiceReference(RegisterWebPlugin.class.getName());

                    if (reference == null)
                    {
                        try
                        {
                            Thread.sleep(500);
                        }
                        catch (Exception e)
                        {
                        }
                        continue;
                    }

                    server = (RegisterWebPlugin)context.getService(reference);

                    if (server == null)
                    {
                        try
                        {
                            Thread.sleep(500);
                        }
                        catch (Exception e)
                        {
                        }
                    }
                }

                System.out.println("开始监视扫描目录Wait");

                try
                {
                    Thread.sleep(60000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                System.out.println("开始监视扫描目录Begin");

                scanResource = new ScanResource(workspace, server);

                scanResource.start();
            }
        }.start();
    }

    public void stop(BundleContext arg0)
        throws Exception
    {
        if (scanResource != null)
        {
            scanResource.setCarry(false);
        }
    }

    class ScanResource extends Thread
    {
        private String workspace = "";

        private boolean carry = true;

        private RegisterWebPlugin registerWebPlugin = null;

        private Map<String, Long> map = new HashMap<String, Long>();

        public ScanResource(String workspace, RegisterWebPlugin registerWebPlugin)
        {
            this.workspace = workspace;

            this.registerWebPlugin = registerWebPlugin;
        }

        public void run()
        {
            File file = new File(workspace);

            while (true)
            {
                if ( !carry)
                {
                    this.interrupt();

                    break;
                }

                try
                {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }

                // 获得各个bundle的工程目录
                File[] listFiles = file.listFiles();

                for (File eachBundle : listFiles)
                {
                    File[] subBundle = eachBundle.listFiles();

                    for (File eachDir : subBundle)
                    {
                        if (eachDir.getAbsolutePath().endsWith("webroot"))
                        {
                            scan(eachDir);
                        }
                    }
                }
            }
        }

        /**
         * webroot
         * 
         * @param file
         */
        private void scan(File file)
        {
            File[] listFiles = file.listFiles();

            for (File subDir : listFiles)
            {
                // /webroot/ask
                if (subDir.isDirectory())
                {
                    File[] fileList = subDir.listFiles();

                    for (File last : fileList)
                    {
                        if (last.isDirectory())
                        {
                            continue;
                        }

                        String pathKey = last.getAbsolutePath();

                        Long mtime = map.get(pathKey);

                        long lastModified = last.lastModified();

                        if (mtime == null)
                        {
                            map.put(pathKey, lastModified);

                            System.out.println("初始加载:" + pathKey + "<-->" + lastModified);
                        }
                        else
                        {
                            // 时间戳不一致
                            if (lastModified != mtime.longValue())
                            {
                                // 重新加载
                                try
                                {
                                    System.out.println("重新加载:" + pathKey);

                                    registerWebPlugin.registerWebResource("", pathKey
                                        .substring(pathKey.indexOf("webroot") + 8),
                                        new FileInputStream(last));

                                    map.put(pathKey, lastModified);
                                }
                                catch (FileNotFoundException e)
                                {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            }
        }

        /**
         * @return the workspace
         */
        public String getWorkspace()
        {
            return workspace;
        }

        /**
         * @param workspace
         *            the workspace to set
         */
        public void setWorkspace(String workspace)
        {
            this.workspace = workspace;
        }

        /**
         * @return the registerWebPlugin
         */
        public RegisterWebPlugin getRegisterWebPlugin()
        {
            return registerWebPlugin;
        }

        /**
         * @param registerWebPlugin
         *            the registerWebPlugin to set
         */
        public void setRegisterWebPlugin(RegisterWebPlugin registerWebPlugin)
        {
            this.registerWebPlugin = registerWebPlugin;
        }

        /**
         * @return the carry
         */
        public boolean isCarry()
        {
            return carry;
        }

        /**
         * @param carry
         *            the carry to set
         */
        public void setCarry(boolean carry)
        {
            this.carry = carry;
        }
    }
}
