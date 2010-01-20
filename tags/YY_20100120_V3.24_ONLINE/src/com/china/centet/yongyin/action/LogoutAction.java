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

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.struts.action.Action#execute(org.apache.struts.action.ActionMapping,
     *      org.apache.struts.action.ActionForm, javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse)
     */
    public ActionForward execute(ActionMapping actionMapping, ActionForm actionForm,
                                 HttpServletRequest request, HttpServletResponse response)
    {
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

        session.invalidate();

        return actionMapping.findForward("success");

    }
}
