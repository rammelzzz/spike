package com.imooc.spike.result;

import com.sun.org.apache.bcel.internal.classfile.Code;

/**
 * @Author: rammelzzz
 * @Description: 后台返回Json规范类
 * @Date: Created in 下午5:23 18-4-17
 * @Modified By:
 **/
public class Result<T> {

        private int code;
        private String msg;
        private T data;


        private Result(T data) {
                this.code = 0;
                this.msg = "success";
                this.data = data;
        }

        private Result(CodeMsg cm) {
                if (cm == null) {
                        return;
                }
                this.code = cm.getCode();
                this.msg = cm.getMsg();
        }

        /**
         * 成功时调用
         *
         * @param data
         * @param <T>
         * @return
         */
        public static <T> Result<T> success(T data) {
                return new Result<T>(data);
        }

        public static <T> Result<T> error(CodeMsg cm) {
                return new Result<T>(cm);
        }

        public int getCode() {
                return code;
        }

        public String getMsg() {
                return msg;
        }


        public T getData() {
                return data;
        }

}
