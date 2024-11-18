
package com.zony.common.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * 发送邮件时，接收参数的类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailVo {

    /** 收件人，支持多个收件人 */
    @NotEmpty
    private List<String> address;
    /** 标题/流程标题等 */
    @NotBlank
    private String title;
    /** 正文 */
    @NotBlank
    private String content;
    /** 自定义所需通知内容 */
    private Map<String,Object> param;

    public EmailVo(List<String> address,String title,String content){
        this.address = address;
        this.title = title;
        this.content = content;
    }
}
