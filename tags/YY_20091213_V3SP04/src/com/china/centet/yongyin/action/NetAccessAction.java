/**
 *
 */
package com.china.centet.yongyin.action;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import com.china.center.tools.BeanUtil;
import com.china.center.tools.StringTools;
import com.china.centet.yongyin.bean.NetAccessBean;
import com.china.centet.yongyin.manager.NetAccessManager;


/**
 * @author Administrator
 */
public class NetAccessAction extends DispatchAction
{
    private final Log _logger = LogFactory.getLog(getClass());

    private NetAccessManager netAccessManager = null;

    /**
     *
     */
    public NetAccessAction()
    {}

    public ActionForward findNet(ActionMapping mapping, ActionForm form,
                                 HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        request.setAttribute("bean", netAccessManager.getAccessBean());

        String to = "";

        String[] tos = netAccessManager.getAccessBean().getRecivers();

        for (int i = 0; i < tos.length; i++ )
        {
            to += tos[i] + ",";
        }

        to = to.substring(0, to.length() - 1);

        request.setAttribute("to", to);

        return mapping.findForward("updateNetAccess");
    }

    public ActionForward updateNetAccess(ActionMapping mapping, ActionForm form,
                                         HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        NetAccessBean bean = new NetAccessBean();

        BeanUtil.getBean(bean, request);

        setNetAccess(request, bean);

        netAccessManager.updateNetAccessBean(bean);

        _logger.info("updateNetAccessBean:" + bean);

        return findNet(mapping, form, request, reponse);
    }

    public ActionForward clearUrl(ActionMapping mapping, ActionForm form,
                                  HttpServletRequest request, HttpServletResponse reponse)
        throws ServletException
    {
        netAccessManager.clear();

        return findNet(mapping, form, request, reponse);
    }

    /**
     * @param request
     * @param bean
     */
    private void setNetAccess(HttpServletRequest request, NetAccessBean bean)
    {
        String to = request.getParameter("to");

        String[] tos = to.split(",");
        List<String> ll = new ArrayList<String>();

        for (String string : tos)
        {
            if ( !StringTools.isNullOrNone(string))
            {
                ll.add(string);
            }
        }

        String[] tos1 = new String[ll.size()];

        ll.toArray(tos1);

        bean.setRecivers(tos1);
    }

    /**
     * @return the netAccessManager
     */
    public NetAccessManager getNetAccessManager()
    {
        return netAccessManager;
    }

    /**
     * @param netAccessManager
     *            the netAccessManager to set
     */
    public void setNetAccessManager(NetAccessManager netAccessManager)
    {
        this.netAccessManager = netAccessManager;
    }
}
