package com.china.centet.yongyin.action;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;


public class LogoutAction extends Action
{
    private static Log logger = LogFactory.getLog(LogoutAction.class);

    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
                                 HttpServletRequest request, HttpServletResponse response)
    {
        /** 得到当前的session对象 */
        HttpSession session = request.getSession(false);
        if (logger.isDebugEnabled())
        {
            logger.debug("LogoutAction.execute. session ==" + session);
        }

        if (session == null)
        {
            logger.error("LogoutAction.execute. getSession is null");
            return actionMapping.findForward("success");
        }
        /** 让session失效 */
        session.invalidate();
        return actionMapping.findForward("success");

    }
}
