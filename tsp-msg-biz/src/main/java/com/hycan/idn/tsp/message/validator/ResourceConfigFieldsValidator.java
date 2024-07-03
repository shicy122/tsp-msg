package com.hycan.idn.tsp.message.validator;

import com.hycan.idn.tsp.message.annotation.ResourceConfigExclusiveFields;
import com.hycan.idn.tsp.message.pojo.resourceconfig.CreateResourceConfigReqVO;
import com.hycan.idn.tsp.message.pojo.resourceconfig.UpdateResourceConfigReqVO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 资源配置字段校验
 *
 * @author shichongying
 * @datetime 2024-02-29 10:35
 */
public class ResourceConfigFieldsValidator implements ConstraintValidator<ResourceConfigExclusiveFields, Object> {

    private Class<?> targetClass;

    @Override
    public void initialize(ResourceConfigExclusiveFields constraintAnnotation) {
        targetClass = constraintAnnotation.targetClass();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (targetClass == CreateResourceConfigReqVO.class) {
            CreateResourceConfigReqVO reqVO = (CreateResourceConfigReqVO) value;
            return (reqVO.getResourceUrl() == null) != (reqVO.getArticleContent() == null);
        } else if (targetClass == UpdateResourceConfigReqVO.class) {
            UpdateResourceConfigReqVO reqVO = (UpdateResourceConfigReqVO) value;
            return (reqVO.getResourceUrl() == null) != (reqVO.getArticleContent() == null);
        }
        return false;
    }
}