package com.imooc.spike.result;

/**
 * @Author: rammelzzz
 * @Description:
 * @Date: Created in 下午5:49 18-4-17
 * @Modified By:
 **/
public class CodeMsg {

    private int code;
    private String msg;

    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //通用code begin
    public static CodeMsg REQUEST_ILLEGAL = new CodeMsg(500102, "請求非法!");
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "server error");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验错误, %s");
    public static CodeMsg ACCESS_LIMIT = new CodeMsg(500103, "請求過於頻繁!");
    public static CodeMsg SESSION_ERROR = new CodeMsg(500104, "需要登录!");
    //通用code end

    //用户模块code begin
    public static CodeMsg PASSWORD_EMPTY = new CodeMsg(300100, "password is empty");
    public static CodeMsg MOBILE_EMPTY = new CodeMsg(300101, "mobile is empty");
    public static CodeMsg PASSWORD_ERROR = new CodeMsg(300102, "password is erorr");
    public static CodeMsg MOBILE_ERROR = new CodeMsg(300103, "mobile is erorr");
    public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(300104, "mobile not exist");
    //用户模块code end

    //商品模块code begin
    public static CodeMsg SPIKE_OVER = new CodeMsg(500100, "商品秒杀结束!");
    public static CodeMsg REPEAT_SPIKE = new CodeMsg(500101, "重复秒杀!");
    public static CodeMsg ORDER_NOT_EXIST = new CodeMsg(500102, "订单不存在!");
    //商品模块code end

    public int getCode() {
        return code;
    }


    public String getMsg() {
        return msg;
    }

    public CodeMsg fillArgs(Object... args) {
        int code = this.code;
        String message = String.format(this.msg, args);
        return new CodeMsg(code, message);
    }

}
