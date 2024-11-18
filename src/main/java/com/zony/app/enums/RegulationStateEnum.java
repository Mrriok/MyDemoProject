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
public enum RegulationStateEnum {
    UNBEGIN("unbegin","未启动"),
    PROGRESSING("progressing","进行中"),
    COMPLETED("completed","已完成");

    private String code;
    private String name;
    private static Map<String,RegulationStateEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (RegulationStateEnum item : RegulationStateEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }

    @JsonCreator
    public static RegulationStateEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (RegulationStateEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<RegulationStateEnum, String> {
        @Override
        public String convertToDatabaseColumn(RegulationStateEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public RegulationStateEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
