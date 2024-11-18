/**
 * Copyright (c) 2018-2020 PoleStar Studio. All Rights Reserved. <br>
 * Use is subject to license terms.<br>
 * <p>
 * 在此处填写文件说明
 */
package com.zony.app.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 制度层级枚举类.
 * <p>制度层级枚举，name在前端进行显示.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/15 -10:23 
 */

@AllArgsConstructor
public enum DefineTypeEnum {
    //拓展类型1，需要从log中reminder中获取下一步代办人
    EXPEND("expend","reminder"),
    //拓展类型2，需要查当前办理人的部门负责人
    EXPEND1("expend1","部门负责人"),
    //拓展类型3，需要查当前办理人的处长
    EXPEND2("expend2","处长");


    private String code;
    private String name;
    private static Map<String, DefineTypeEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (DefineTypeEnum item : DefineTypeEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }
    @JsonCreator
    public static DefineTypeEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (DefineTypeEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<DefineTypeEnum, String> {
        @Override
        public String convertToDatabaseColumn(DefineTypeEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public DefineTypeEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
