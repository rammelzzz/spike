package com.imooc.spike.util;

import com.alibaba.fastjson.JSON;
import com.imooc.spike.result.CodeMsg;
import com.imooc.spike.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午9:52 18-4-21
 * @Modified By:
 **/
public class WebUtil {

    public static void render(HttpServletResponse response, CodeMsg cm) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        String str = JSON.toJSONString(Result.error(cm));
        out.write(str.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

}
