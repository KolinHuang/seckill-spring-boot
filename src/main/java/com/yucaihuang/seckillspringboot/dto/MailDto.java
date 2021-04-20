package com.yucaihuang.seckillspringboot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author kol Huang
 * @date 2020/11/20
 */

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class MailDto {

    //邮件主题
    private String subject;
    //邮件内容
    private String content;
    //接收人
    private String[] tos;

}
