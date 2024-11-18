package com.zony.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 流程类型
 *
 * @Author jinweiwei
 * @Date 2020-07-17
 **/
@AllArgsConstructor
public enum AddedReasonEnum {
    LAW_CHANGED("0","政府法律法规变更"),
    REGULATION_CHANGED("1","上级制度或相关制度更新"),
    FUNCTION_ADJUST("2","组织机构或管理职能调整"),
    OPTIMALIZATION("3","业务发展需要优化或调整制度/流程"),
    LACK("4","业务制度有缺失的"),
    FIND_QUESTION("5","制度复审、监督检查及执行过程中发现制度有问题"),
    IMPROVE_EFFIENCY("6","提升管理效率"),
    OTHER_SITUATION("7","其他需要修订的情形");

    private final String code;
    private final String name;
    private static Map<String, AddedReasonEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (AddedReasonEnum item : AddedReasonEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }

    @JsonCreator
    public static AddedReasonEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (AddedReasonEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<AddedReasonEnum, String> {
        @Override
        public String convertToDatabaseColumn(AddedReasonEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public AddedReasonEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
