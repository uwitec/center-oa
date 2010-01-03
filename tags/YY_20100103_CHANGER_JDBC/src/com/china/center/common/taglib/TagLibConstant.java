/*
 * 文件名：TagLibConstant.java
 * 版权：Copyright 2002-2007 centerchina Tech. Co. Ltd. All Rights Reserved.
 * 描述：
 * 修改人：zhuzhu
 * 修改时间：2007-7-13
 * 跟踪单号：
 * 修改单号：
 * 修改内容：新增
 */
package com.china.center.common.taglib;

/**
 * 常量
 * 
 * @author zhuzhu
 * @version 2007-7-13
 * @see TagLibConstant
 * @since
 */

public interface TagLibConstant
{
    String CENTER_CELL_INDEX = "CENTER_CELL_INDEX_ISP_CELL_INDEX";

    String CENTER_CELLS_INIT = "CENTER_CELLS_INIT_ISP_CELLS_INIT";

    String CENTER_TRS_INDEX = "CENTER_TRS_INDEX_ISP_TRS_INDEX";

    String CENTER_BEAN_CLASS = "CENTER_BEAN_CLASS_ISP_BEAN_CLASS";

    String CENTER_GOBAL_LANG_SETTING = "CENTER_GOBAL_LANG_SETTING";

    int ALL_CELLS = 0;

    /**
     * 资源文件的目录
     */
    String DEST_FOLDER_NAME = "/images/";

    /**
     * 样式目录
     */
    String CSS_FOLDER_NAME = "/css/";

    /**
     * 脚本存放
     */
    String JS_FOLDER_NAME = "/js/";

    String JS_LANG_FOLDER_NAME = "/js/lang/";

    String CAL_JS_FOLDER_NAME = "center_cal/";

    String CSS_FILE_NAME = "center.css";

    String JS_RESOURCES_NAME = "public_resources.js";

    String CSS_STYLE_LOADING = "CSS_STYLE_LOADING";
}
