package com.zony.common.filenet.util;

/**
 * @fileName LogEventTypeCon.java
 * @package com.zony.filenet.util
 * @function PE 日志事件类型常量
 * @version 1.0.0
 * @date 2014年10月28日
 * @author Jeffrey
 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
 */
public class LogEventTypeConstants {

	/**
	 * 系统消息
	 * VW_VWUserLoginMsg: 当用户登录时。 240
	 * VW_VWUserLogoutMsg: 当用户注销时。 250
	 * VW_VWStartTransferMsg:
	 * 启动将配置或工作流程属性信息保存到工作流程数据库的流程。当您在流程配置控制台中落实更改时，或者当您将工作流程链接至文档或文档类时，就会进行此流程。
	 * 260
	 * VW_VWNewConfigMsg:
	 * 将新的或者已修改的工作流程登记簿、工作队列或者事件日志信息保存到工作流程数据库时。当您在流程配置控制台中落实更改时，就会进行此流程。 265
	 * VW_VWNewWorkClassMsg: 将新的或已修改的工作流程定义保存到工作流程数据库时。当您从“流程设计器”启动工作流程时进行此流程。
	 * 266
	 * VW_VWEndTransferMsg: 传输过程完成时。当您从流程设计器启动工作流程时或当您从流程配置控制台提交配置更改时，将进行此传输。
	 * 270
	 * VW_VWStartInitRegionMsg: 启动用于初始化所选择的隔离式区域的流程。 300
	 * VW_VWEndInitRegionMsg: 结束用于初始化所选择的隔离式区域的流程。 310
	 * VW_VWStartFullInitializeMsg: 启动用于初始化所选择的工作流程数据库中所有隔离式区域的流程。 320
	 * VW_VWEndFullInitializeMsg: 结束用于初始化所选择的工作流程数据库中所有隔离式区域的流程。 330
	 * VW_VWRemoveDatabaseMsg: 启动用于除去所选择的工作流程数据库的流程。 340
	 **/
	/**
	 * 工作项终端系统操作。
	 */
	public static final int VW_WOSystemOperationMsg = 100;
	/**
	 * 在正常处理期间，当工作项从一台服务器移至另一台服务器时记录。
	 */
	public static final int VW_WOMovedToServerMsg = 110;
	/**
	 * 当更改工作项的名称时记录。
	 */
	public static final int VW_WONameChangedMsg = 120;
	/**
	 * 评估规则之后记录。[规则]
	 */
	public static final int VW_WOEvaluateRuleSetMsg = 115;
	/**
	 * 当工作项已到达里程碑时记录。[里程碑]
	 */
	public static final int VW_WOMilestoneMsg = 125;
	/**
	 * 记录“子代”工作项的创建。
	 */
	public static final int VW_WOChildCreationMsg = 130;
	/**
	 * 记录“父代”工作项的创建。
	 */
	public static final int VW_WOParentCreationMsg = 140;
	/**
	 * 当在实际创建工作项之前为潜在工作项保留唯一的工作对象编号时记录。在 IBM Case Manager 中用于 REST API。
	 */
	public static final int VW_CreateWobNumMsg = 550;
	/**
	 * 记录“子代”工作项的终止。
	 */
	public static final int VW_WOChildTerminationMsg = 150;
	/**
	 * 记录“父代”工作项的终止。
	 */
	public static final int VW_WOParentTerminationMsg = 160;
	/**
	 * 记录工作流程中所有工作项的完成。,<br/>
	 * <b>要点：VW_WFTermination （事件编号 165）流程跟踪器正确处理带 -T 参数的 vwlog 命令所必需的，并且用于清理
	 * workflow 事件日志中已终止的工作流程的事件。
	 */
	public static final int VW_WFTermination = 165;
	/**
	 * 记录异常处理工作流程图的执行。
	 */
	public static final int VW_WOExceptionMsg = 170;
	/**
	 * 当截止期限已到期时记录。
	 */
	public static final int VW_WODeadlineMsg = 172;
	/**
	 * 用来在截止期限已到期时发送电子邮件通知。如果已禁用此事件类别，那么不会发出事件通知。
	 */
	public static final int VW_WOReminderMsg = 174;
	/**
	 * 当强制工作项跳过指令时记录。
	 */
	public static final int VW_WOForcedToSkipInstructionMsg = 180;
	/**
	 * 当强制终止工作项时记录。
	 */
	public static final int VW_WOForcedToTerminateMsg = 190;
	/**
	 * 当强制删除工作项时记录。
	 */
	public static final int VW_WOForcedToDeleteMsg = 200;
	/**
	 * 当步骤处理器或者用户锁定工作项时记录。
	 */
	public static final int VW_WPBeginServiceMsg = 350;
	/**
	 * 当工作项排队时记录。
	 */
	public static final int VW_WPWorkObjectQueuedMsg = 352;
	/**
	 * 当工作项已更新并且分派到下一个队列时记录。
	 */
	public static final int VW_WPEndServiceNormalMsg = 360;
	/**
	 * 当保存工作项同时保留现有锁定时记录。
	 */
	public static final int VW_WPWOBSaveWithLockMsg = 365;
	/**
	 * 当工作项处理异常结束时记录。
	 */
	public static final int VW_WPEndServiceAbnormalMsg = 370;
	/**
	 * 当工作项已更新并且在同一队列中已解锁时记录。
	 */
	public static final int VW_WPEndServiceReleaseMsg = 380;
	/**
	 * 将工作项委托给另一个用户时记录。
	 */
	public static final int VW_WPEndServiceReleaseDelegateMsg = 382;
	/**
	 * 将工作项重新分配给另一个用户时记录。
	 */
	public static final int VW_WPEndServiceReleaseReassignMsg = 384;
	/**
	 * 在委托之后，将工作项返回给用户时记录。
	 */
	public static final int VW_WPEndServiceReleaseReturnMsg = 386;
	/**
	 * 工作项最终用户操作异常终止
	 */
	public static final int VW_WPEndServiceAbortMsg = 390;
	/**
	 * 在处理工作项期间遇到检查点时记录。
	 */
	public static final int VW_WPCheckPointMsg = 400;
	/**
	 * 当计时器或者 API doCall 发生时记录。
	 */
	public static final int VW_WPExitingInstrSheetMsg = 405;
	/**
	 * 当子图步骤已完成并且处理返回到调用者步骤时记录。
	 */
	public static final int VW_WPReturnInstrSheetMsg = 407;

	/**
	 * VW_User11Msg: 用户定义的消息 11。 410
	 * VW_User12Msg: 用户定义的消息 12。 420
	 * VW_User13Msg: 用户定义的消息 13。 430
	 * VW_User14Msg: 用户定义的消息 14。 440
	 * VW_User21Msg: 用户定义的消息 21。 450
	 * VW_User22Msg: 用户定义的消息 22。 460
	 * VW_User23Msg: 用户定义的消息 23。 470
	 * VW_User24Msg: 用户定义的消息 24。 480
	 */

	/**
	 * 在未指定队列的情况下执行步骤时记录。
	 */
	public static final int VVW_WOEmptyStepMsg = 500;
	/**
	 * 完成复合步骤时记录。
	 */
	public static final int VW_WOCompleteSysStepMsg = 510;
}
