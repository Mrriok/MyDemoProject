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
public enum HierarchyEnum {

    REGULATION("regulation","制度"),
    WAY("way","办法"),
    RULE("rule","细则");

    private String code;
    private String name;
    private static Map<String,HierarchyEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (HierarchyEnum item : HierarchyEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }

    @JsonCreator
    public static HierarchyEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (HierarchyEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<HierarchyEnum, String> {
        @Override
        public String convertToDatabaseColumn(HierarchyEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public HierarchyEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
