package com.hycan.idn.tsp.message.pojo.basetask;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 批量导入VIN文件请求VO
 *
 * @author liangliang
 * @datetime 2022-08-19 13:40
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportVinReqVO {

    /**
     * vin车架号
     */
    @ExcelProperty(value = "vin", index = 0)
    private String vin;
}
