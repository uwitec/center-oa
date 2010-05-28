/*
 * $Id: ForwardConfig.java 55980 2004-10-29 15:34:55Z husted $ 
 *
 * Copyright 1999-2004 The Apache Software Foundation.
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

package com.china.center.struts.wrap;

import java.io.Serializable;

/**
 * <p>A JavaBean representing the configuration information of a
 * <code>&lt;forward&gt;</code> element from a Struts
 * configuration file.</p>
 *
 * @version $Rev: 55980 $ $Date: 2004-10-29 16:34:55 +0100 (Fri, 29 Oct 2004) $
 * @since Struts 1.1
 */

public class ForwardConfigWrap implements Serializable
{
    private String path = "";
    
    private String name = "";
    
    private String module = "";
    
    private boolean redirect = false;
    
    public ForwardConfigWrap()
    {
    }
    
    /**
     * @return ���� path
     */
    public String getPath()
    {
        return path;
    }
    
    /**
     * @param ��path���и�ֵ
     */
    public void setPath(String path)
    {
        this.path = path;
    }
    
    /**
     * @return ���� name
     */
    public String getName()
    {
        return name;
    }
    
    /**
     * @param ��name���и�ֵ
     */
    public void setName(String name)
    {
        this.name = name;
    }
    
    /**
     * @return ���� module
     */
    public String getModule()
    {
        return module;
    }
    
    /**
     * @param ��module���и�ֵ
     */
    public void setModule(String module)
    {
        this.module = module;
    }
    
    /**
     * @return ���� redirect
     */
    public boolean isRedirect()
    {
        return redirect;
    }
    
    /**
     * @param ��redirect���и�ֵ
     */
    public void setRedirect(boolean redirect)
    {
        this.redirect = redirect;
    }
    
    /**
     * Constructs a <code>String</code> with all attributes
     * in name = value format.
     *
     * @return a <code>String</code> representation 
     * of this object.
     */
    public String toString()
    {
        final String TAB = ",";
        
        StringBuilder retValue = new StringBuilder();
        
        retValue.append("ForwardConfigWrap ( ")
                .append(super.toString())
                .append(TAB)
                .append("path = ")
                .append(this.path)
                .append(TAB)
                .append("name = ")
                .append(this.name)
                .append(TAB)
                .append("module = ")
                .append(this.module)
                .append(TAB)
                .append("redirect = ")
                .append(this.redirect)
                .append(TAB)
                .append(" )");
        
        return retValue.toString();
    }
}
