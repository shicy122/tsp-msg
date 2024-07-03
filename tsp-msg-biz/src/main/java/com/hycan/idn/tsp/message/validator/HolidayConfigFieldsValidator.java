package com.hycan.idn.tsp.message.validator;

import com.hycan.idn.tsp.message.annotation.HolidayConfigExclusiveFields;
import com.hycan.idn.tsp.message.constant.HolidayTypeConstants;
import com.hycan.idn.tsp.message.pojo.holidayconfig.CreateHolidayConfigReqVO;
import com.hycan.idn.tsp.message.pojo.holidayconfig.UpdateHolidayConfigReqVO;
import groovy.lang.Tuple2;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

/**
 * 资源配置字段校验
 *
 * @author shichongying
 * @datetime 2024-02-29 10:35
 */
public class HolidayConfigFieldsValidator implements ConstraintValidator<HolidayConfigExclusiveFields, Object> {

    private Class<?> targetClass;

    @Override
    public void initialize(HolidayConfigExclusiveFields constraintAnnotation) {
        targetClass = constraintAnnotation.targetClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (targetClass == CreateHolidayConfigReqVO.class) {
            CreateHolidayConfigReqVO reqVO = (CreateHolidayConfigReqVO) value;
            Tuple2<Boolean, String> result = validHolidayDate(reqVO.getStartDate(), reqVO.getEndDate(), reqVO.getHolidayType());
            if (Boolean.FALSE.equals(result.getV1())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(result.getV2()).addConstraintViolation();
            }
            return result.getV1();
        } else if (targetClass == UpdateHolidayConfigReqVO.class) {
            UpdateHolidayConfigReqVO reqVO = (UpdateHolidayConfigReqVO) value;
            Tuple2<Boolean, String> result = validHolidayDate(reqVO.getStartDate(), reqVO.getEndDate(), reqVO.getHolidayType());
            if (Boolean.FALSE.equals(result.getV1())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(result.getV2()).addConstraintViolation();
            }
            return result.getV1();
        }
        return false;
    }

    /**
     * 节假日配置开始结束日期校验
     *
     * @param startDate   节假日开始日期
     * @param endDate     节假日结束日期
     * @param holidayType 节假日类型
     */
    private Tuple2<Boolean, String> validHolidayDate(LocalDate startDate, LocalDate endDate, String holidayType) {
        if (startDate.isAfter(endDate)) {
            return new Tuple2<>(Boolean.FALSE, "开始日期不能大于结束日期!");
        }

        int startYear = startDate.getYear();
        if (startYear < LocalDateTime.now().getYear()) {
            return new Tuple2<>(Boolean.FALSE, "开始日期年份不能小于当前年份!");
        }

        if (HolidayTypeConstants.BIRTHDAY.equals(holidayType)) {
            int endYear = endDate.getYear();
            if (startYear != endYear) {
                return new Tuple2<>(Boolean.FALSE, "生日开始结束日期必须为同一年度!");
            }

            LocalDate firstDayOfYear = startDate.with(TemporalAdjusters.firstDayOfYear());
            if (startDate.isAfter(firstDayOfYear)) {
                return new Tuple2<>(Boolean.FALSE, "生日开始日期必须大于或等于本年度第一天!");
            }

            LocalDate lastDayOfYear = endDate.with(TemporalAdjusters.lastDayOfYear());
            if (endDate.isBefore(lastDayOfYear)) {
                return new Tuple2<>(Boolean.FALSE, "生日结束日期必须小于或等于本年度最后一天!");
            }
        }

        return new Tuple2<>(Boolean.TRUE, null);
    }
}