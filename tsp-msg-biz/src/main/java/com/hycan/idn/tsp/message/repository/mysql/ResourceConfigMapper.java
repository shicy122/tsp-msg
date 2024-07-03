package com.hycan.idn.tsp.message.repository.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hycan.idn.tsp.message.pojo.resourceconfig.PageResourceConfigReqVO;
import com.hycan.idn.tsp.message.pojo.resourceconfig.ResourceConfigDTO;
import com.hycan.idn.tsp.message.pojo.resourceconfig.ResourceConfigRspVO;
import com.hycan.idn.tsp.message.entity.mysql.ResourceConfigEntity;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 资源配置Mapper接口
 *
 * @author liangwenqi
 * @date 2022-08-02
 */
public interface ResourceConfigMapper extends BaseMapper<ResourceConfigEntity> {

    /**
     * 根据资源名称查询资源配置是否存在
     *
     * @param resourceName 资源名称
     * @param resourceType 资源类型
     * @return 是否存在重复资源配置
     */
    boolean isExists(@Param("resource_name") String resourceName, @Param("resource_type") String resourceType);

    /**
     * 根据资源配置ID修改资源配置状态
     *
     * @param id     节假日配置ID
     * @param status 节假日配置状态
     */
    int updateStatusById(@Param("id") Long id, @Param("status") String status);

    /**
     * 按条件分页查询资源配置
     *
     * @param page  分页数据
     * @param reqVO 分页查询资源配置请求VO
     * @return 分页返回资源配置响应VO
     */
    IPage<ResourceConfigRspVO> findPage(Page<ResourceConfigRspVO> page, @Param("query") PageResourceConfigReqVO reqVO);

    /**
     * 根据基础任务ID，查询资源配置
     *
     * @param baseTaskId 基础任务ID
     * @return 资源配置对象
     */
    List<ResourceConfigDTO> findResourcesByBaseTaskId(@Param("base_task_id") Long baseTaskId);

    /**
     * 根据基础任务ID，查询资源配置
     *
     * @param baseTaskIds 基础任务ID列表
     * @return 资源配置对象
     */
    List<ResourceConfigDTO> findResourcesByBaseTaskIds(@Param("base_task_ids") List<Long> baseTaskIds);
}
