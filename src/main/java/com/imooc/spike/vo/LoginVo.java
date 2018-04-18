package com.imooc.spike.vo;

import com.imooc.spike.validate.IsMobile;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午9:48 18-4-17
 * @Modified By:
 **/
public class LoginVo {

        @NotNull
        @IsMobile
        private String mobile;

        @NotNull
        @Length(min = 32)
        private String password;

        public String getMobile() {
                return mobile;
        }

        public void setMobile(String mobile) {
                this.mobile = mobile;
        }

        public String getPassword() {
                return password;
        }

        public void setPassword(String password) {
                this.password = password;
        }

        @Override
        public String toString() {
                return "LoginVo{" +
                        "mobile=" + mobile +
                        ", password='" + password + '\'' +
                        '}';
        }
}
