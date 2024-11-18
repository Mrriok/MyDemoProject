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
 * 填写功能简述.
 * <p>填写详细说明.<br>
 * @version v1.0
 * @author MrriokChen
 * @date 2020/7/16 -16:02 
 */
@AllArgsConstructor
public enum ProgressStatusEnum {

    NO_STARTED("noStarted","未启动"),
    PROGRESS("progress","进行中"),
    END_OF_APPROVAL("end_of_approval","审批完结"),
    DELAY_INCOMPLETE("delay_incomplete","延时未完成"),
    COMPLETE("complete","已完成");

    private String code;
    private String name;
    private static Map<String,ProgressStatusEnum> valueMap;

    @JsonValue
    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getNameByCode(String code){
        for (ProgressStatusEnum item : ProgressStatusEnum.values()) {
            if(code.equals(item.getCode())){
                return item.getName();
            }
        }
        return null;
    }

    @JsonCreator
    public static ProgressStatusEnum getItem(String code) {
        if (valueMap == null) {
            valueMap = new ConcurrentHashMap<>();
            for (ProgressStatusEnum item : values()) {
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
    public static class EnumConverter implements AttributeConverter<ProgressStatusEnum, String> {
        @Override
        public String convertToDatabaseColumn(ProgressStatusEnum attribute) {
            return attribute == null ? null : attribute.getCode();
        }

        @Override
        public ProgressStatusEnum convertToEntityAttribute(String dbData) {
            return getItem(dbData);
        }
    }

    @Override
    public String toString() {
        return code;
    }
}
