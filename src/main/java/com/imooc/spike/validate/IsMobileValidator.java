package com.imooc.spike.validate;

import com.imooc.spike.util.ValidateUtil;
import org.apache.commons.lang3.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午10:33 18-4-17
 * @Modified By:
 **/
public class IsMobileValidator implements ConstraintValidator<IsMobile, String> {

        private boolean required = false;

        @Override
        public void initialize(IsMobile isMobile) {
                required = isMobile.required();
        }

        @Override
        public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
                if (required) {
                        return ValidateUtil.isMobile(s);
                } else {
                        if (StringUtils.isEmpty(s)) {
                                return true;
                        } else {
                                return ValidateUtil.isMobile(s);
                        }
                }
        }
}
