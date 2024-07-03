package com.hycan.idn.tsp.message.facade;

import com.hycan.idn.tsp.common.core.constant.CommonConstants;
import com.hycan.idn.tsp.common.core.constant.SecurityConstants;
import com.hycan.idn.tsp.common.core.util.R;
import com.hycan.idn.tsp.message.constant.ProtocolConstants;
import com.hycan.idn.tsp.message.pojo.basetask.VehicleBaseInfoVO;
import com.hycan.idn.tsp.vms.feign.RemoteCarModelService;
import com.hycan.idn.tsp.vms.feign.VehInfoService;
import com.hycan.idn.tsp.vms.feign.VehMsgContentService;
import com.hycan.idn.tsp.vms.feign.VehPartsInfoService;
import com.hycan.idn.tsp.vms.params.inner.bo.veh.VehPartsDetailsBO;
import com.hycan.idn.tsp.vms.params.inner.bo.veh.VinListBo;
import com.hycan.idn.tsp.vms.params.inner.dto.msg.VehCarDTO;
import com.hycan.idn.tsp.vms.params.inner.dto.msg.VehModelDTO;
import com.hycan.idn.tsp.vms.params.inner.dto.veh.CarModelDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * VMS feign接口包装类
 *
 * @author Shadow
 * @datetime 2024-03-06 09:44
 */
@Slf4j
@Component
public class VmsFeignFacade {

    @Resource
    private VehMsgContentService vehMsgContentService;

    @Resource
    private VehInfoService vehInfoService;

    @Resource
    private VehPartsInfoService vehPartsInfoService;

    @Resource
    private RemoteCarModelService remoteCarModelService;

    public List<VehCarDTO> getVehInfo(List<String> vinList) {
        if (CollectionUtils.isEmpty(vinList)) {
            return Collections.emptyList();
        }

        VinListBo vinListBo = new VinListBo();
        vinListBo.setVinList(vinList);
        R<List<VehCarDTO>> result = vehMsgContentService.getVehByVinList(vinListBo, SecurityConstants.FROM_IN);
        if (CommonConstants.SUCCESS != result.getCode() || ObjectUtils.isEmpty(result.getData())) {
            return Collections.emptyList();
        }

        return result.getData();
    }

    /**
     * 根据MT列表获取车辆VIN码列表
     *
     * @param mtList MT列表
     * @return 车辆VIN码列表
     */
    public List<String> getVinsByMts(List<Long> mtList) {
        if (CollectionUtils.isEmpty(mtList)) {
            return Collections.emptyList();
        }

        List<String> mtStrList = mtList.stream().map(String::valueOf).collect(Collectors.toList());
        R<List<VehModelDTO>> result = vehMsgContentService.getVinByModelIds(mtStrList, SecurityConstants.FROM_IN);
        if (CommonConstants.SUCCESS != result.getCode() || ObjectUtils.isEmpty(result.getData())) {
            return Collections.emptyList();
        }
        return result.getData().stream().map(VehModelDTO::getVin).collect(Collectors.toList());
    }

    public String getSerialByVin(String vin) {
        R<String> result = vehInfoService.getSeriesByVin(vin, SecurityConstants.FROM_IN);
        if (CommonConstants.SUCCESS != result.getCode() || ObjectUtils.isEmpty(result.getData())) {
            return null;
        }

        return result.getData();
    }

    public String getCdcSnByVin(String vin) {
        R<VehPartsDetailsBO> result = vehPartsInfoService.getByVinPartType(
                vin, ProtocolConstants.AVNT_VALUE, SecurityConstants.FROM_IN);
        if (CommonConstants.SUCCESS != result.getCode() || ObjectUtils.isEmpty(result.getData())
                || !StringUtils.hasText(result.getData().getSn())) {
            return null;
        }
        return result.getData().getSn();
    }

    public List<VehicleBaseInfoVO> getVehBaselInfo(List<Long> mtList) {
        R<List<CarModelDTO>> result = remoteCarModelService.getCarModelByModelIdList(mtList, SecurityConstants.FROM_IN);
        if (CommonConstants.SUCCESS != result.getCode() || ObjectUtils.isEmpty(result.getData())) {
            return null;
        }

        Map<Long, List<Long>> carModelMap = result.getData().stream()
                .collect(Collectors.groupingBy(CarModelDTO::getCarSeriesId,
                        Collectors.mapping(CarModelDTO::getId, Collectors.toList())));

        return carModelMap.entrySet().stream()
                .map(entry -> {
                    VehicleBaseInfoVO vehicleBaseInfoVO = new VehicleBaseInfoVO();
                    vehicleBaseInfoVO.setSerialId(entry.getKey());
                    vehicleBaseInfoVO.setMtId(entry.getValue());
                    return vehicleBaseInfoVO;
                })
                .collect(Collectors.toList());
    }
}
