package com.quick.common.core.http;

/**
 * Created by zhaolong.
 * Description: 返回数据结构定义 例如：广告接口返回类型不一样
 * Date: 2020/5/22 15:09
 */
public class ADResult<T> {
    int code;
    String msg;
    T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
