
package com.zony.app.enums;

import com.zony.common.enums.RequestMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 验证码业务场景对应的 Redis 中的 key
 * </p>
 */
@Getter
@AllArgsConstructor
public enum StatusEnum {

    /**
     * 草稿 @AnonymousGetMapping
     */
    DRAFT("0"),

    /**
     * 进行中 @AnonymousPostMapping
     */
    PROGRESS("1"),
    /**
     * 已办结 @AnonymousPostMapping
     */
    COMPLETE("2");

    private final String key;


}
