package com.zony.common.filenet.util;

/**
 * @fileName FnEnumList.java
 * @package com.zony.filenet.util
 * @function FileNet 中关于所有的枚举对象常量的集中类
 * @version 1.0.0
 * @date 2014-8-5
 * @author Jeffrey
 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
 */
public class FnEnumList {
	/**
	 * @fileName FnEnumList.java
	 * @package com.zony.filenet.util
	 * @function 文档属性的字段类型枚举对象
	 * @version 1.0.0
	 * @date 2014-8-5
	 * @author Jeffrey
	 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
	 */
	public enum PropertyEnum {
		BINARY(1), BOOLEAN(2), DATE(3), DOUBLE(4), GUID(5), LONG(6), OBJECT(7), STRING(8);
		private int intValue;

		private PropertyEnum(int intValue) {
			this.intValue = intValue;
		}

		public int valueOf() {
			return this.intValue;
		}

		@Override
		public String toString() {
			return String.valueOf(this.intValue);
		}
	}

	/**
	 * @fileName FnEnumList.java
	 * @package com.zony.filenet.util
	 * @function ChioceList 对象类型 Group 和 Item 类型 枚举对象
	 * @version 1.0.0
	 * @date 2014-8-5
	 * @author Jeffrey
	 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
	 */
	public enum ChoiceListTypeEnum {
		GROUP, ITEM;
	}

	/**
	 * @fileName FnEnumList.java
	 * @package com.zony.filenet.util
	 * @function 字段类型 简单类型还是List 类型枚举对象
	 * @version 1.0.0
	 * @date 2014-8-5
	 * @author Jeffrey
	 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
	 */
	public enum CardinalityTypeEnum {
		SINGLE(0), ENUM(1), LIST(2);
		private int intValue;

		private CardinalityTypeEnum(int intValue) {
			this.intValue = intValue;
		}

		public int valueOf() {
			return this.intValue;
		}

		@Override
		public String toString() {
			return String.valueOf(this.intValue);
		}
	}
}
