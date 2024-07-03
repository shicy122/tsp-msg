package com.hycan.idn.tsp.message.facade;

import com.alibaba.fastjson.JSONObject;
import com.hycan.idn.common.core.constant.CommonConstants;
import com.hycan.idn.tsp.common.core.util.ExceptionUtil;
import com.hycan.idn.tsp.message.constant.HolidayTypeConstants;
import com.hycan.idn.tsp.message.pojo.OkHttpRspDTO;
import com.hycan.idn.tsp.message.pojo.holidayconfig.SyncLegalHolidayRspVO;
import com.hycan.idn.tsp.message.entity.mysql.HolidayConfigEntity;
import com.hycan.idn.tsp.message.utils.OkHttpUtil;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Request;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 同步节假日信息
 *
 * @author Shadow
 * @datetime 2024-02-27 19:15
 */
@Slf4j
@Component
public class HolidayRemoteFacade {

    public static final String DECEMBER_28TH = "1228";
    public static final Integer MIN_YEAR = 2000;
    public static final Integer MAX_YEAR = 2099;

    private static final String HOLIDAY_URL = "https://api.apihubs.cn/holiday/get?api_key=0b62bd1a0f9ad04af41944517e2133e8798b&field=date,holiday&year=%s&holiday_recess=1&order_by=1&cn=1&size=31";

    @Resource
    private OkHttpUtil okHttpUtil;

    /**
     * 按年度同步法定节假日的假期
     * 由于元旦的假期，可能会出现在上一年，所以查询两年的数据，再截取上一年12月28日到查询年份的12月28日，这样得到的是当前年度的法定假期
     *
     * @param year 年份
     * @return 节假日配置
     */
    public List<HolidayConfigEntity> syncLegalHoliday(int year) {
        if (year <= MIN_YEAR || year >= MAX_YEAR) {
            log.warn("入参 year 为无效值, 同步节假日失败! ");
            return Collections.emptyList();
        }

        int lastYear = year - 1;
        int startDate = Integer.parseInt(lastYear + DECEMBER_28TH);
        int endDate = Integer.parseInt(year + DECEMBER_28TH);

        List<SyncLegalHolidayRspVO> holidays = new ArrayList<>();
        holidays.addAll(sendHolidayRequest(year));
        holidays.addAll(sendHolidayRequest(lastYear));

        return convertHolidayDate(holidays, startDate, endDate);
    }

    private List<SyncLegalHolidayRspVO> sendHolidayRequest(Integer year) {
        try {
            String url = String.format(HOLIDAY_URL, year);
            Request request = new Request.Builder()
                    .get()
                    .url(url)
                    .build();
            OkHttpRspDTO response = okHttpUtil.request(request);
            if (Objects.isNull(response) || !response.isSuccess()) {
                return Collections.emptyList();
            }
            JSONObject jsonObject = JSONObject.parseObject(response.getBody());
            Integer resultCode = jsonObject.getInteger("code");
            JSONObject resultData = jsonObject.getJSONObject("data");
            if (Objects.isNull(resultCode) || Objects.isNull(resultData) || !CommonConstants.SUCCESS.equals(resultCode)) {
                return Collections.emptyList();
            }

            return resultData.getJSONArray("list").toJavaList(SyncLegalHolidayRspVO.class);
        } catch (Exception e) {
            log.error("同步节假日失败, 原因: {}", ExceptionUtil.getBriefStackTrace(e));
        }
        return Collections.emptyList();
    }

    private List<HolidayConfigEntity> convertHolidayDate(List<SyncLegalHolidayRspVO> holidays, int validStartDate, int validEndDate) {
        Map<String, List<SyncLegalHolidayRspVO>> groupedByHoliday = holidays.stream()
                .filter(dto -> dto.getDate() >= validStartDate && dto.getDate() <= validEndDate)
                .sorted(Comparator.comparingInt(SyncLegalHolidayRspVO::getDate))
                .collect(Collectors.groupingBy(SyncLegalHolidayRspVO::getHolidayCn));

        // 获取每个分组中 date 的最大值和最小值，并添加到新的 List 中
        return groupedByHoliday.entrySet().stream()
                .map(entry -> {
                    List<SyncLegalHolidayRspVO> holidayList = entry.getValue();
                    int minDate = holidayList.stream().mapToInt(SyncLegalHolidayRspVO::getDate).min().orElse(0);
                    int maxDate = holidayList.stream().mapToInt(SyncLegalHolidayRspVO::getDate).max().orElse(0);
                    LocalDate startDate = LocalDate.parse(String.valueOf(minDate), DateTimeFormatter.ofPattern("yyyyMMdd"));
                    LocalDate endDate = LocalDate.parse(String.valueOf(maxDate), DateTimeFormatter.ofPattern("yyyyMMdd"));
                    return HolidayConfigEntity.of(entry.getKey(), HolidayTypeConstants.LEGAL, startDate, endDate);
                })
                .collect(Collectors.toList());
    }
}
