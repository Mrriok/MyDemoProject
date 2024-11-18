package com.zony.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
public enum SystemDocAttachEnum {

    MAIN_BODY("1","正文"),
    FLOW_DIAGRAM("2","流程图"),
    CONTROL_MATRIX("3","控制矩阵"),
    PERMISSIONS("4","权限清单"),
    OTHER("5","其他附件");

    private String code;
    private String name;
    private static Map<String,SystemDocAttachEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (SystemNameEnum item : SystemNameEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }

    @JsonCreator
    public static SystemDocAttachEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (SystemDocAttachEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<SystemDocAttachEnum, String> {
        @Override
        public String convertToDatabaseColumn(SystemDocAttachEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public SystemDocAttachEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
