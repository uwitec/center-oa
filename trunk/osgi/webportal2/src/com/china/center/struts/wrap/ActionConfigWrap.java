/*
 * $Id: ActionConfig.java 377805 2006-02-14 19:26:15Z niallp $ 
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
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * A JavaBean representing the configuration information of an <code>&lt;action&gt;</code> element from a Struts
 * module configuration file.
 * </p>
 * 
 * @version $Rev: 377805 $ $Date: 2006-02-14 19:26:15 +0000 (Tue, 14 Feb 2006) $
 * @since Struts 1.1
 */
public class ActionConfigWrap implements Serializable
{
    private String path = "";

    private String parameter = "";

    private String type = "";

    private String folder = "";

    private String bean = "";

    private boolean validate = false;

    private List<ForwardConfigWrap> forwards = new ArrayList();

    public ActionConfigWrap()
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
     * @return ���� parameter
     */
    public String getParameter()
    {
        return parameter;
    }

    /**
     * @param ��parameter���и�ֵ
     */
    public void setParameter(String parameter)
    {
        this.parameter = parameter;
    }

    /**
     * @return ���� type
     */
    public String getType()
    {
        return type;
    }

    /**
     * @param ��type���и�ֵ
     */
    public void setType(String type)
    {
        this.type = type;
    }

    /**
     * @return ���� validate
     */
    public boolean isValidate()
    {
        return validate;
    }

    /**
     * @param ��validate���и�ֵ
     */
    public void setValidate(boolean validate)
    {
        this.validate = validate;
    }

    /**
     * @return ���� forwards
     */
    public List<ForwardConfigWrap> getForwards()
    {
        return forwards;
    }

    public void addForwardConfig(ForwardConfigWrap forward)
    {
        forwards.add(forward);
    }

    /**
     * @return the folder
     */
    public String getFolder()
    {
        return folder;
    }

    /**
     * @param folder
     *            the folder to set
     */
    public void setFolder(String folder)
    {
        this.folder = folder;
    }

    /**
     * Constructs a <code>String</code> with all attributes in name = value format.
     * 
     * @return a <code>String</code> representation of this object.
     */
    public String toString()
    {
        final String TAB = ",";

        StringBuilder retValue = new StringBuilder();

        retValue
            .append("ActionConfigWrap ( ")
            .append(super.toString())
            .append(TAB)
            .append("path = ")
            .append(this.path)
            .append(TAB)
            .append("parameter = ")
            .append(this.parameter)
            .append(TAB)
            .append("type = ")
            .append(this.type)
            .append(TAB)
            .append("folder = ")
            .append(this.folder)
            .append(TAB)
            .append("validate = ")
            .append(this.validate)
            .append(TAB)
            .append("forwards = ")
            .append(this.forwards)
            .append(TAB)
            .append(" )");

        return retValue.toString();
    }

    /**
     * @return the bean
     */
    public String getBean()
    {
        return bean;
    }

    /**
     * @param bean
     *            the bean to set
     */
    public void setBean(String bean)
    {
        this.bean = bean;
    }

}
