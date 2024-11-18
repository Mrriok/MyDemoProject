/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.enums.WorkbenchEnum;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.zony.app.enums.WorkflowClassEnum;
import com.zony.app.enums.WorkflowDisposeObjEnum;
import com.zony.app.enums.WorkflowDisposeWayEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Converter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/8/14 -10:16 
 */
@AllArgsConstructor
public enum WorkflowDefinePropertyEnum {

    WORKFLOW_TYPE("workflowType","流程类型"),
    STEP_NAME("stepName","步骤名称"),
    STEP_SEQ_NAME("stepSeqNum","步骤序号"),
    DISPOSE_OBJ_ID("disposeObjId","处理者"),
    DISPOSE_OBJECT("disposeObjType","处理类型"),
    DISPOSE_WAY_TYPE("disposeWayType","步骤处理方式类型");

    private String code;
    private String name;
    private static Map<String,WorkflowDefinePropertyEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (WorkflowDefinePropertyEnum item : WorkflowDefinePropertyEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }

    @JsonCreator
    public static WorkflowDefinePropertyEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (WorkflowDefinePropertyEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<WorkflowDefinePropertyEnum, String> {
        @Override
        public String convertToDatabaseColumn(WorkflowDefinePropertyEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public WorkflowDefinePropertyEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }

}
