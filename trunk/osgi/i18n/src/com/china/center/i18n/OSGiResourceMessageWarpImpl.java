package com.china.center.i18n;

/**
 * OSGiResourceMessageWarp
 * 
 * @author ZHUZHU
 * @version 2010-1-13
 * @see OSGiResourceMessageWarpImpl
 * @since 1.0
 */
public class OSGiResourceMessageWarpImpl implements OSGiResourceMessageWarp
{
    private MessageInterface resourceMessage = null;

    private String[] basenames = null;

    private boolean isLoad = false;

    /**
     * default constructor
     */
    public OSGiResourceMessageWarpImpl()
    {}

    public void setBasenames(String[] basenames)
    {
        this.basenames = basenames;

        if (resourceMessage != null && !isLoad)
        {
            resourceMessage.setBasenames(this.basenames);

            isLoad = true;
        }
    }

    public void destroy()
    {
        resourceMessage.removeBasenames(this.basenames);
    }

    /**
     * set resourceMessage
     * 
     * @param resourceMessage
     *            the value of resourceMessage
     */
    public void setResourceMessage(MessageInterface resourceMessage)
    {
        this.resourceMessage = resourceMessage;

        if (this.basenames != null && !isLoad)
        {
            resourceMessage.setBasenames(this.basenames);

            isLoad = true;
        }
    }
}
