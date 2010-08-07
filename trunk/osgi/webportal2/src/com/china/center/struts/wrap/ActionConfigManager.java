package com.china.center.struts.wrap;

/**
 * RegisterConfig
 * 
 * @author ZHUZHU
 * @version [版本号, 2009-7-25]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public interface ActionConfigManager
{
    void addActionConfig(ActionConfigWrap config, Object server);

    void removeActionConfig(String path);
}
