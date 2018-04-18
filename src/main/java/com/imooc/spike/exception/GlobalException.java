package com.imooc.spike.exception;

import com.imooc.spike.result.CodeMsg;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午10:49 18-4-17
 * @Modified By:
 **/
public class GlobalException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private CodeMsg cm;

    public GlobalException(CodeMsg cm) {
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
