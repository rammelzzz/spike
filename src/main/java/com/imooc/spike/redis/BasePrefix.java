package com.imooc.spike.redis;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午7:19 18-4-17
 * @Modified By:
 **/
public abstract class BasePrefix implements KeyPrefix {

        private int expireSeconds;

        private String prefix;


        public BasePrefix(String prefix) {
                this(0, prefix);
        }

        public BasePrefix(int expireSeconds, String prefix) {
                this.expireSeconds = expireSeconds;
                this.prefix = prefix;
        }

        @Override
        public int expireSeconds() { //0代表永不过期
                return expireSeconds;
        }

        @Override
        public String getPrefix() {
                String className = getClass().getSimpleName();
                return className + ":" + prefix;
        }
}
