/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.enums.WorkbenchEnum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/8/11 -17:15 
 */
@AllArgsConstructor
public enum CurrentNodeEnum {

    RISK_CONTROL_OFFICE_PRELIMINARY_REVIEW("riskControlOfficePreliminaryReview","风控办初审"),
    EDITING_DEPARTMENT_TO_MODIFY_NODE("editingDepartmentToModifyNode","编修部门修改节点"),
    SYSTEM_REVISION_DEPARTMENT_REVISION("systemRevisionDepartmentRevision","制度编修部门修改"),
    FEEDBACK("feedback","意见反馈"),
    COLLECT_OPINIONS("collectOpinions","收集采纳意见"),
    FINAL_APPROVAL_BY_THE_DIRECTOR_OF_RISK_CONTROL_OFFICE("finalApprovalByTheDirectorOfRiskControlOffice","风控办终审主管审批"),
    APPROVAL_BY_THE_DIRECTOR_OD_FINAL_APPROVAL_OF_THE_RISK_CONTROL_OFFICE("approvalByTheDirectorOfFinalApprovalOfTheRiskControlOffice","风控办终审处长审批"),
    FEEDBACK_FROM_VARIOUS_DEPARTMENTS("feedbackFromVariousDepartments","各部门反馈意见");

    private String code;
    private String name;
    private static Map<String,CurrentNodeEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (CurrentNodeEnum item : CurrentNodeEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }

    @JsonCreator
    public static CurrentNodeEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (CurrentNodeEnum item : values()) {
                valueMap.put(item.getCode(), item);
            }
        }
        if (code == null) {
            return null;
        } else {
            return valueMap.get(code);
        }
    }

    @Converter
    public static class EnumConverter implements AttributeConverter<CurrentNodeEnum, String> {
        @Override
        public String convertToDatabaseColumn(CurrentNodeEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public CurrentNodeEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
