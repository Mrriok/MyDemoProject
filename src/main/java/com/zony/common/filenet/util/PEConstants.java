/**
 * 
 */
package com.zony.common.filenet.util;

public interface PEConstants {

	public static final int MAX_TASK_LIST_SIZE = 5000;
	/**
	 * eventLog中代表流程step记录的常量
	 */
	public static final String EVENT_TYPE_WORKOBJECT_QUEUED = "352";
	public static final String EVENT_TYPE_COMPLETE_MSG = "510";
	public static final String EVENT_TYPE_WF_TERMINATION_MSG = "165";
	public static final String EVENT_TYPE_BEGIN_SERVICE_NORMAL = "350";
	public static final String EVENT_TYPE_END_SERVICE_NORMAL = "360";

	public static final String USER_INBOX_NAME = "Inbox";
	public static final String USER_INBOX0_NAME = "Inbox(0)";
	public static final String DEFAULT_ROSTER_NAME = "DefaultRoster";
	// System Fields defined by PE
	/** The name of the work class. */
	public static final String SYSTEM_FIELD_WORK_CLASS_NAME = "F_Class";
	/**
	 * This field is initialized to null every time that a step starts. It
	 * allows the users participating in a step to share information about the
	 * step. For example, when a user reassigns the step, he can write a comment
	 * for the user receiving the step.
	 */
	public static final String SYSTEM_FIELD_COMMENT = "F_Comment";
	/**
	 * The time the workflow was created. With the exception of the initial work
	 * item of the workflow, it is different than F_CreateTime.
	 */
	public static final String SYSTEM_FIELD_STARTTIME = "F_CreateTime";
	/**
	 * The time at which the work item entered the queue or was updated in the
	 * queue.
	 */
	public static final String SYSTEM_FIELD_ENQUEUETIME = "F_EnqueueTime";
	/**
	 * The work item's lock status:
	 * <p>
	 * <li>0=not locked</li>
	 * <li>1=locked by user</li>
	 * <li>2=locked by system</li>
	 * </p>
	 */
	public static final String SYSTEM_FIELD_LOCKED = "F_Locked";
	/**
	 * Whether the work item is overdue:
	 * <p>
	 * <li>0=work item is not overdue</li>
	 * <li>1=reminder has expired for this step</li>
	 * <li>2=deadline has expired for this step</li>
	 */
	public static final String SYSTEM_FIELD_OVERDUE = "F_Overdue";
	/**
	 * Transient field. It only has a value if the work item has been saved with
	 * a response. Derived from F_Responses.
	 */
	public static final String SYSTEM_FIELD_RESPONSE = "F_Response";
	/**
	 * The name of the step that is either in process or (if no step is
	 * currently in process) is next to be executed for the work item.
	 */
	public static final String SYSTEM_FIELD_STEPNAME = "F_StepName";
	/**
	 * The status of the step. Valid values are Complete, In progress, or
	 * Deleted.
	 */
	public static final String SYSTEM_FIELD_STEPSTATUS = "F_StepStatus";
	/**
	 * The subject entered by the user when a workflow is launched. This field
	 * is used in the out-of-the-box Workplace application to populate the Name
	 * field on the Tasks page.
	 */
	public static final String SYSTEM_FIELD_SUBJECT = "F_Subject";
	/**
	 * A 16-byte binary field that is actually a GUID (global unique
	 * identifier). It uniquely identifies a single work item.
	 */
	public static final String SYSTEM_FIELD_WOBNUM = "F_WobNum";
	/**
	 * A 16-byte binary field that is actually a GUID (global unique
	 * identifier). It uniquely identifies a single work item.
	 */
	public static final String SYSTEM_FIELD_ORIGINATOR = "F_Originator";

	public static final String SYSTEM_FIELD_EVENTTYPE = "F_EventType";

	public static final String SYSTEM_FIELD_WORKFLOWNUMBER = "F_WorkFlowNumber";

	public static final String SYSTEM_FIELD_USERID = "F_UserId";
	public static final String SYSTEM_FIELD_BOUNDUSER = "F_BoundUser";

}
