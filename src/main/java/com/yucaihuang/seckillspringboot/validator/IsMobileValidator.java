package com.yucaihuang.seckillspringboot.validator;

import com.yucaihuang.seckillspringboot.utils.ValidatorUtils;
import org.thymeleaf.util.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

    private boolean required = false;

    @Override
    public void initialize(IsMobile constraintAnnotation) {
        required = constraintAnnotation.required();
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(required){
            return ValidatorUtils.isMobile(s);
        }else {
            if(StringUtils.isEmpty(s)){
                return true;
            }else {
                return ValidatorUtils.isMobile(s);
            }
        }
    }
}
