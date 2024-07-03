package com.hycan.idn.tsp.message.validator;

import com.hycan.idn.tsp.message.annotation.SceneCardTaskExclusiveFields;
import com.hycan.idn.tsp.message.pojo.scenecardtask.CreateSceneCardTaskReqVO;
import com.hycan.idn.tsp.message.pojo.scenecardtask.UpdateSceneCardTaskReqVO;
import groovy.lang.Tuple2;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 场景卡片字段校验
 *
 * @author shichongying
 * @datetime 2024-02-29 10:35
 */
public class SceneCardTaskFieldsValidator implements ConstraintValidator<SceneCardTaskExclusiveFields, Object> {

    private Class<?> targetClass;

    @Override
    public void initialize(SceneCardTaskExclusiveFields constraintAnnotation) {
        targetClass = constraintAnnotation.targetClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (targetClass == CreateSceneCardTaskReqVO.class) {
            CreateSceneCardTaskReqVO reqVO = (CreateSceneCardTaskReqVO) value;

            Tuple2<Boolean, String> result = validResource(reqVO.getResourceId(), reqVO.getCustomUrl(), reqVO.getResourceType());
            if (Boolean.FALSE.equals(result.getV1())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(result.getV2()).addConstraintViolation();
            }
            return result.getV1();
        } else if (targetClass == UpdateSceneCardTaskReqVO.class) {
            UpdateSceneCardTaskReqVO reqVO = (UpdateSceneCardTaskReqVO) value;
            Tuple2<Boolean, String> result = validResource(reqVO.getResourceId(), reqVO.getCustomUrl(), reqVO.getResourceType());
            if (Boolean.FALSE.equals(result.getV1())) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(result.getV2()).addConstraintViolation();
            }
            return result.getV1();
        }
        return false;
    }

    /**
     * 资源校验
     *
     * @param resourceId   资源ID
     * @param customUrl    自定义链接
     * @param resourceType 资源类型
     */
    private Tuple2<Boolean, String> validResource(Long resourceId, String customUrl, String resourceType) {

        if (StringUtils.isNotBlank(customUrl) && null != resourceId) {
            return new Tuple2<>(Boolean.FALSE, "系统资源和自定义资源只能二选一!");
        }

        if ((StringUtils.isNotBlank(customUrl) || null != resourceId) && StringUtils.isBlank(resourceType)) {
            return new Tuple2<>(Boolean.FALSE, "资源类型不能为空!");
        }

        return new Tuple2<>(Boolean.TRUE, null);
    }
}