package com.hycan.idn.tsp.message.repository.mysql;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hycan.idn.tsp.message.entity.mysql.VehicleBaseInfoEntity;
import com.hycan.idn.tsp.vms.params.inner.dto.msg.VehCarDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * vin基础信息映射器
 *
 * @author liangliang
 * @Entity entity.mysql.domain.TspVinBaseInfo
 * @date 2022/10/13
 */
public interface VehicleBaseInfoMapper extends BaseMapper<VehicleBaseInfoEntity> {
    /**
     * 获取 vin基础信息
     *
     * @param vins 批量vin
     * @return {@link List}<{@link VehCarDTO}>
     */
    List<VehCarDTO> getVinBaseInfoByVin(@Param("vins") List<String> vins);

    /**
     * 批量保存vin基础信息列表
     *
     * @param entities vin基础信息
     */
    void batchInsertOrUpdate(@Param("list") List<VehicleBaseInfoEntity> entities);
}
