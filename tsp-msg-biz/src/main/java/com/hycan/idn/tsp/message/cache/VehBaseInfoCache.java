package com.hycan.idn.tsp.message.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.hycan.idn.tsp.message.facade.VmsFeignFacade;
import com.hycan.idn.tsp.message.pojo.vehiclebase.VehicleBaseInfoDTO;
import com.hycan.idn.tsp.vms.params.inner.dto.msg.VehCarDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-25 16:22
 */
@Component
public class VehBaseInfoCache {

    @Resource
    private VmsFeignFacade vmsFeignFacade;

    private final Cache<String, String> snCache = Caffeine.newBuilder()
            .initialCapacity(500)
            .maximumSize(50000)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    private final Cache<String, String> serialCache = Caffeine.newBuilder()
            .initialCapacity(500)
            .maximumSize(50000)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    /**
     * 缓存VIN与车辆基础信息
     */
    private final Cache<String, VehicleBaseInfoDTO> vehInfoCache = Caffeine.newBuilder()
            .initialCapacity(500)
            .maximumSize(50000)
            .expireAfterWrite(24, TimeUnit.HOURS)
            .build();

    public Map<String, VehicleBaseInfoDTO> getVehInfoByVins(List<String> vins) {
        List<VehicleBaseInfoDTO> vehInfoList = new ArrayList<>();
        List<String> syncVins = new ArrayList<>();
        for (String vin : vins) {
            VehicleBaseInfoDTO vehInfo = vehInfoCache.getIfPresent(vin);
            if (Objects.nonNull(vehInfo)) {
                vehInfoList.add(vehInfo);
                continue;
            }
            syncVins.add(vin);
        }

        if (!CollectionUtils.isEmpty(syncVins)) {
            List<VehCarDTO> vehCarDTOList = vmsFeignFacade.getVehInfo(syncVins);
            vehInfoList.addAll(VehicleBaseInfoDTO.of(vehCarDTOList));
        }

        return vehInfoList.stream().collect(Collectors.toMap(VehicleBaseInfoDTO::getVin, vehInfo -> vehInfo));
    }

    public String getSerialByVin(String vin) {
        String serial = serialCache.getIfPresent(vin);
        if (StringUtils.isNotBlank(serial)) {
            return serial;
        }

        return vmsFeignFacade.getSerialByVin(vin);
    }

    public String getCdcSnByVin(String vin) {
        String sn = snCache.getIfPresent(vin);
        if (StringUtils.isNotBlank(sn)) {
            return sn;
        }
        return vmsFeignFacade.getCdcSnByVin(vin);
    }
}
