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
public enum RegulationAttributeEnum {
    ADD("add","新增"),
    EDIT("edit","修订");

    private String code;
    private String name;
    private static Map<String,RegulationAttributeEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (RegulationAttributeEnum item : RegulationAttributeEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }

    @JsonCreator
    public static RegulationAttributeEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (RegulationAttributeEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<RegulationAttributeEnum, String> {
        @Override
        public String convertToDatabaseColumn(RegulationAttributeEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public RegulationAttributeEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}

