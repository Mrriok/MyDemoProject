/**
 * 
 */
package com.zony.common.filenet.util;

public interface CEConstants {
	/**
	 * 0 - No inheritance (this object only). 不继承，权限信息只对当前对象生效
	 */
	public static final int INHERITABLE_DEPTH_NO_INHERITANCE = 0;
	/**
	 * 1 - This object and immediate children only. 继承当前的子对象，
	 */
	public static final int INHERITABLE_DEPTH_ONE_LEVEL = 1;
	/**
	 * -1 - This object and all children (infinite levels deep). 继承所有子对象
	 */
	public static final int INHERITABLE_DEPTH_UNLIMITED = -1;
	/**
	 * -2 - All children (infinite levels deep) but not this object.
	 * 继承所有子对象，当前对象不生效
	 */
	public static final int INHERITABLE_DEPTH_UNLIMITED_NO_THISOBJ = -2;
	/**
	 * -3 - Immediate children only; not this object. 继承当前子对象，不对当前对象生效
	 */
	public static final int INHERITABLE_DEPTH_IMMEDIATE_CHILDREN_ONLY_NO_THIS = -3;
}
