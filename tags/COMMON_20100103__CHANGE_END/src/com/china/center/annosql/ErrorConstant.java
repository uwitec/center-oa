/*
 * File Name: ErrorConstant.java
 * CopyRight: Copyright by www.center.china
 * Description:
 * Creater: zhuAchen
 * CreateTime: 2007-10-7
 * Grant: open source to everybody
 */
package com.china.center.annosql;

import com.china.center.annotation.Note;

/**
 * 公共错误码集合(16进制) <br>
 * 共8位 xxxxxxxx <br>
 * 前0-2位错误类型(00：公共 01：sql 02：逻辑) <br>
 * 前3-4位模块类型(00：公共) <br>
 * 前5-8位具体的错误类型<br>
 *
 * @author zhuzhu
 * @version 2007-10-7
 * @see
 * @since
 */
public interface ErrorConstant
{
	@Note(value = "parameter is null")
	String PARAMETER_NULL = "00000001";

	@Note(value = "object is not a Entity")
	String OBJECT_NOT_ENTITY = "01000001";

	@Note(value = "missing id in object")
	String MISSING_ID = "01000002";

	@Note(value = "columns is empty")
	String COLUMN_EMPTY = "01000003";

	@Note(value = "miss join is null")
	String JOIN_NULL = "01000004";

	@Note(value = "object is not Inherit")
	String OBJECT_NOT_INHERIT = "02000001";
}
