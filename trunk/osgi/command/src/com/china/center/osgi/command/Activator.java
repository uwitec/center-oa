package com.china.center.osgi.command;


import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;


public class Activator implements BundleActivator, CommandProvider
{
    private BundleContext context;

    private String tab = "\t"; //$NON-NLS-1$

    private String newline = "\r\n"; //$NON-NLS-1$

    public void start(BundleContext context)
        throws Exception
    {
        this.context = context;

        Hashtable properties = new Hashtable();

        // 需要注册的
        context.registerService(CommandProvider.class.getName(), this, properties);
    }

    public String getHelp()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\twhatami - returns whether the bundle is a plug-in or not\n");
        buffer.append("\tuname - returns framework information\n");
        buffer.append("\tgrep(f) - returns filtrate bundles form framework.EG:grep osgi\n");
        buffer.append("\tenv - returns env in system,support filtrate keyword.EG:env osgi\n");

        return buffer.toString();
    }

    public void stop(BundleContext context)
        throws Exception
    {
    }

    public void _uname(CommandInterpreter intp)
        throws Exception
    {
        String vendor = context.getProperty(Constants.FRAMEWORK_VENDOR);
        String version = context.getProperty(Constants.FRAMEWORK_VERSION);
        String osName = context.getProperty(Constants.FRAMEWORK_OS_NAME);
        String osVersion = context.getProperty(Constants.FRAMEWORK_OS_VERSION);
        intp.println("\n " + vendor + " " + version + " (" + osName + " " + osVersion + ")");
    }

    /**
     * env
     * 
     * @param intp
     * @throws Exception
     */
    public void _env(CommandInterpreter intp)
        throws Exception
    {
        Properties properties = System.getProperties();

        Set<Entry<Object, Object>> entrySet = properties.entrySet();

        String filtrate = intp.nextArgument();

        List<String> filtrateList = new ArrayList();

        while (filtrate != null)
        {
            filtrateList.add(filtrate);

            filtrate = intp.nextArgument();
        }

        for (Entry<Object, Object> entry : entrySet)
        {
            String msg = entry.getKey() + " = " + entry.getValue();

            if (contain(msg, filtrateList))
            {
                intp.println(msg);
            }
        }
    }

    private boolean contain(String msg, List<String> filtrateList)
    {
        boolean isShow = true;

        for (String string : filtrateList)
        {
            if (msg.toLowerCase().indexOf(string.toLowerCase()) == -1)
            {
                isShow = false;

                break;
            }
        }

        return isShow;
    }

    public void _whatami(CommandInterpreter intp)
        throws Exception
    {
        try
        {
            long id = Long.parseLong(intp.nextArgument());
            Bundle bundle = context.getBundle(id);
            URL url = bundle.getEntry("plugin.xml");
            if (url != null)
            {
                intp.println("\n I'm (" + bundle.getSymbolicName() + ") a plug-in");
            }
            else
            {
                intp.println("\n I'm (" + bundle.getSymbolicName() + ") not a plug-in");
            }
        }
        catch (NumberFormatException nfe)
        {
            intp.println("\n Error processing command");
        }
    }

    public void _grep(CommandInterpreter intp)
        throws Exception
    {
        _f(intp);
    }

    /**
     * filter
     * 
     * @param ci
     * @throws Exception
     */
    public void _f(CommandInterpreter intp)
        throws Exception
    {
        Bundle[] bundles = (Bundle[])context.getBundles();

        if (bundles.length == 0)
        {
            intp.println("NO bundle");
        }
        else
        {
            String filtrate = intp.nextArgument();

            intp.print(newline);
            intp.print("id");
            intp.print(tab);
            intp.print("State");
            intp.print(tab + "        ");
            intp.print("Bundle");

            List<String> filtrateList = new ArrayList();

            while (filtrate != null)
            {
                filtrateList.add(filtrate);

                filtrate = intp.nextArgument();
            }

            int total = 0;
            for (int i = 0; i < bundles.length; i++ )
            {
                Method method = bundles[i].getClass().getMethod("getVersion");

                String version = "";

                if (method != null)
                {
                    version = method.invoke(bundles[i]).toString();
                }

                String allStr = String.valueOf(bundles[i].getBundleId())
                                + bundles[i].getSymbolicName() + version + getStateName(bundles[i]);

                if (contain(allStr, filtrateList))
                {
                    total++ ;
                    intp.print(newline);
                    intp.print(bundles[i].getBundleId());
                    intp.print(tab);
                    intp.print(getStateName(bundles[i]));
                    intp.print(tab);
                    intp.print(bundles[i].getSymbolicName() + "_" + version);
                }
            }

            intp.print(newline);

            intp.print("filter bundles amount: " + total);
        }
    }

    /**
     * filter
     * 
     * @param ci
     * @throws Exception
     */
    public void _exc(CommandInterpreter intp)
        throws Exception
    {
        Bundle[] bundles = (Bundle[])context.getBundles();

        if (bundles.length == 0)
        {
            intp.println("NO bundle");
        }
        else
        {
            String filtrate = intp.nextArgument();

            List<String> filtrateList = new ArrayList();

            while (filtrate != null)
            {
                filtrateList.add(filtrate);

                filtrate = intp.nextArgument();
            }

            if (filtrateList.size() != 2)
            {
                intp.print("参数不正确.exc 接口 方法");

                intp.print(newline);

                return;
            }

            String className = filtrateList.get(0).trim();

            String methodName = filtrateList.get(1).trim();

            // 类名.方法(无参数的)
            ServiceReference serviceReference = context.getServiceReference(className);

            if (serviceReference == null)
            {
                intp.print("接口没有找到注册的服务");

                intp.print(newline);

                return;
            }

            Object service = context.getService(serviceReference);

            if (service == null)
            {
                intp.print("注册的服务不存在");

                intp.print(newline);

                return;
            }

            Method[] declaredMethods = service.getClass().getDeclaredMethods();

            for (Method method : declaredMethods)
            {
                if (method.getName().equalsIgnoreCase(methodName))
                {
                    method.invoke(service);

                    intp.print(methodName + ":执行成功");

                    intp.print(newline);

                    return;
                }
            }

            intp.print(methodName + ":没有找到");

            intp.print(newline);
        }
    }

    public void _rr(CommandInterpreter intp)
        throws Exception
    {
        Bundle[] bundles = (Bundle[])context.getBundles();

        if (bundles.length == 0)
        {
            intp.println("NO bundle");
        }
        else
        {
            intp.print(newline);
            intp.print("id");
            intp.print(tab);
            intp.print("State");
            intp.print(tab + "        ");
            intp.print("Bundle");

            List<String> filtrateList = new ArrayList();

            filtrateList.add("RESOLVED");

            int total = 0;
            for (int i = 0; i < bundles.length; i++ )
            {
                Method method = bundles[i].getClass().getMethod("getVersion");

                String version = "";

                if (method != null)
                {
                    version = method.invoke(bundles[i]).toString();
                }

                String allStr = String.valueOf(bundles[i].getBundleId())
                                + bundles[i].getSymbolicName() + version + getStateName(bundles[i]);

                if (contain(allStr, filtrateList))
                {
                    total++ ;
                    intp.print(newline);
                    intp.print(bundles[i].getBundleId());
                    intp.print(tab);
                    intp.print(getStateName(bundles[i]));
                    intp.print(tab);
                    intp.print(bundles[i].getSymbolicName() + "_" + version);
                }
            }

            intp.print(newline);

            intp.print("filter bundles amount: " + total);
        }
    }

    public void _aa(CommandInterpreter intp)
        throws Exception
    {
        Bundle[] bundles = (Bundle[])context.getBundles();

        if (bundles.length == 0)
        {
            intp.println("NO bundle");
        }
        else
        {
            intp.print(newline);
            intp.print("id");
            intp.print(tab);
            intp.print("State");
            intp.print(tab + "        ");
            intp.print("Bundle");

            List<String> filtrateList = new ArrayList();

            filtrateList.add("ACTIVE");

            int total = 0;
            for (int i = 0; i < bundles.length; i++ )
            {
                Method method = bundles[i].getClass().getMethod("getVersion");

                String version = "";

                if (method != null)
                {
                    version = method.invoke(bundles[i]).toString();
                }

                String allStr = String.valueOf(bundles[i].getBundleId())
                                + bundles[i].getSymbolicName() + version + getStateName(bundles[i]);

                if (contain(allStr, filtrateList))
                {
                    total++ ;
                    intp.print(newline);
                    intp.print(bundles[i].getBundleId());
                    intp.print(tab);
                    intp.print(getStateName(bundles[i]));
                    intp.print(tab);
                    intp.print(bundles[i].getSymbolicName() + "_" + version);
                }
            }

            intp.print(newline);

            intp.print("filter bundles amount: " + total);
        }
    }

    protected String getStateName(Bundle bundle)
    {
        int state = bundle.getState();
        switch (state)
        {
            case Bundle.UNINSTALLED:
                return "UNINSTALLED "; //$NON-NLS-1$

            case Bundle.INSTALLED:
                return "INSTALLED   "; //$NON-NLS-1$

            case Bundle.RESOLVED:
                return "RESOLVED    "; //$NON-NLS-1$

            case Bundle.STARTING:
                return "STARTING    "; //$NON-NLS-1$

            case Bundle.STOPPING:
                return "STOPPING    "; //$NON-NLS-1$

            case Bundle.ACTIVE:
                return "ACTIVE      "; //$NON-NLS-1$

            default:
                return Integer.toHexString(state);
        }
    }

}