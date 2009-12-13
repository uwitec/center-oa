/**
 * File Name: MakeFileWrap.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: ZHUACHEN<br>
 * CreateTime: 2009-10-18<br>
 * Grant: open source to everybody
 */
package com.china.center.oa.customize.make.wrap;


import com.china.center.oa.customize.make.bean.MakeFileBean;


/**
 * MakeFileWrap
 * 
 * @author ZHUZHU
 * @version 2009-10-18
 * @see MakeFileWrap
 * @since 1.0
 */
public class MakeFileWrap extends MakeFileBean
{
    /**
     * 0:NO 1:YES
     */
    private boolean edit = false;

    /**
     * default constructor
     */
    public MakeFileWrap()
    {}

    /**
     * @return the edit
     */
    public boolean isEdit()
    {
        return edit;
    }

    /**
     * @param edit
     *            the edit to set
     */
    public void setEdit(boolean edit)
    {
        this.edit = edit;
    }
}
