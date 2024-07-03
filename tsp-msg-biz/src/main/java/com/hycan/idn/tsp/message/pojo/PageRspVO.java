package com.hycan.idn.tsp.message.pojo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 分页实体类
 *
 * @author Liuyingjie
 * @datetime 2022/9/1 12:00
 */
@Data
@ApiModel(description = "分页实体类")
public class PageRspVO<T> {

    @ApiModelProperty("分页数据")
    private List<T> records;

    @ApiModelProperty("总数")
    private long total;

    @ApiModelProperty("每页显示记录数")
    private long size;

    @ApiModelProperty("当前页码值")
    private long current;

    public static <T> PageRspVO<T> of(IPage<T> page) {
        PageRspVO<T> pageVO = new PageRspVO<>();
        pageVO.setRecords(page.getRecords());
        pageVO.setCurrent(page.getCurrent());
        pageVO.setSize(page.getSize());
        pageVO.setTotal(page.getTotal());
        return pageVO;
    }

    public static <T> PageRspVO<T> of(List<T> records, Integer current, Integer size, Long total) {
        PageRspVO<T> pageVO = new PageRspVO<>();
        pageVO.setRecords(records);
        pageVO.setCurrent(current);
        pageVO.setSize(size);
        pageVO.setTotal(total);
        return pageVO;
    }
}
