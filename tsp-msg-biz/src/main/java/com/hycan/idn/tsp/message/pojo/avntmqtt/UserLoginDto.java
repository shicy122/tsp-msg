package com.hycan.idn.tsp.message.pojo.avntmqtt;

import lombok.Data;

/**
 * @author Liuyingjie
 * @datetime 2022/10/19 13:54
 */
@Data
public class UserLoginDto {

    /**
     * 登录
     */
    private Integer login;

    /**
     * 车架号
     */
    private String vin;

}
