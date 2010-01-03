/**
 * File Name: PageConstant.java<br>
 * CopyRight: Copyright by www.center.china<br>
 * Description:<br>
 * Creater: zhuAchen<br>
 * CreateTime: 2008-11-3<br>
 * Grant: open source to everybody
 */
package com.china.center.common;

/**
 * PageConstant
 * 
 * @author zhuzhu
 * @version 2008-11-3
 * @see PageConstant
 * @since 1.0
 */
public interface PageConstant
{
    String LOAD = "load";

    String PAREXT = "parExt_";

    String JUST = "just";

    /**
     * 0:memery 1:init
     */
    String JSON_QUERYMODE = "queryMode";

    /**
     * 0:init(memery) 1:chage page size to init 2:turning page(pagesize无效) 3:flush init(pagesize无效)
     */
    String JSON_OPRACTION = "oprAction";

    String FORWARD = "forward";

    String MEMORY = "memory";

    String PAGE = "page";

    String PARAMETER_MAP = "pmap";

    String TURN = "turn";

    String PAGE_ATTRIBUTE_NAME = "A_page";

    String CONDITION_ATTRIBUTE_NAME = "A_condtion";
}
