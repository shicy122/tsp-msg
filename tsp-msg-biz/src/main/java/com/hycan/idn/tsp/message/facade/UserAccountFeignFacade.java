package com.hycan.idn.tsp.message.facade;

import com.hycan.idn.tsp.account.feign.RemoteTspUserService;
import com.hycan.idn.tsp.common.core.constant.CommonConstants;
import com.hycan.idn.tsp.common.core.constant.SecurityConstants;
import com.hycan.idn.tsp.common.core.util.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

/**
 * TODO 文件描述
 *
 * @author Shadow
 * @datetime 2024-03-21 13:35
 */
@Slf4j
@Component
public class UserAccountFeignFacade {

    @Resource
    private RemoteTspUserService remoteTspUserService;

    public List<String> getBirthdayVins() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("MMdd"));

        R<List<String>> result = remoteTspUserService.getBirthdayListVin(date, SecurityConstants.FROM_IN);
        if (CommonConstants.SUCCESS != result.getCode() || ObjectUtils.isEmpty(result.getData())) {
            return Collections.emptyList();
        }

        return result.getData();
    }
}
