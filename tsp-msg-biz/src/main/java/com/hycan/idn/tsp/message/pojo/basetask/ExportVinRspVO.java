package com.hycan.idn.tsp.message.pojo.basetask;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量到处VIN文件响应VO
 *
 * @author liangliang
 * @datetime 2022-08-19 13:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExportVinRspVO {

    /**
     * vin车架号
     */
    @ExcelProperty(value = "vin", index = 0)
    private String vin;
}
