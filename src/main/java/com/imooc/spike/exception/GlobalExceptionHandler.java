package com.imooc.spike.exception;

import com.imooc.spike.result.CodeMsg;
import com.imooc.spike.result.Result;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午10:41 18-4-17
 * @Modified By:
 **/
@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

        @ExceptionHandler(value = Exception.class)
        public Result<String> exceptionHnadler(HttpServletRequest request, Exception e) {
                //将异常信息打印到控制台便于分析错误
                e.printStackTrace();
                if (e instanceof GlobalException) {
                        GlobalException ge = (GlobalException) e;
                        return Result.error(ge.getCm());
                }
                if (e instanceof BindException) {
                        BindException ex = (BindException) e;
                        List<ObjectError> errors = ex.getAllErrors();
                        ObjectError error = errors.get(0);
                        String msg = error.getDefaultMessage();
                        return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
                }
                return Result.error(CodeMsg.SERVER_ERROR);
        }

}
