package com.china.centet.yongyin.bean;


import org.apache.struts.action.ActionForm;
import org.apache.struts.upload.FormFile;


public class UploadFileForm extends ActionForm
{
    private FormFile myFile;

    public UploadFileForm()
    {

    }

    /**
     * @return Returns the myFile.
     */
    public FormFile getMyFile()
    {
        return myFile;
    }

    /**
     * @param myFile
     *            The myFile to set.
     */
    public void setMyFile(FormFile myFile)
    {
        this.myFile = myFile;
    }

}