package com.hycan.idn.tsp.message.constant;

/**
 * 常见常数
 *
 * @author liangliang
 * @date 2022/12/26
 */
public interface CommonConstants {

    String SYS = "SYS";

    /**
     * obs资源前缀
     */
    String RESOURCE = "resource/";

    /**
     * 图文渲染接口
     */
    String ARTICLE_URL = "%s/msg-center-svc/v1/resource-configs/detail?resource_type=%s&resource_url=%s";

    /**
     * 车系占位符
     */
    String SERIAL_PLACEHOLDER = "{serial}";
    /**
     * vin占位符
     */
    String VIN_PLACEHOLDER = "{vin}";
    /**
     * AVNT
     */
    String AVNT = "avnt_";

    /**
     * 字符串拼接符
     */
    String UNDERLINE = "_";

    /**
     * 字符串拼接符
     */
    String FILE_JOINT = "/";

    /**
     * 默认分页大小
     */
    Integer DEFAULT_SIZE = 10;

    /**
     * 默认页码
     */
    Integer DEFAULT_CURRENT = 1;

}
