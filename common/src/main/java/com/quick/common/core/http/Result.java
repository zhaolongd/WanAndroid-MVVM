package com.quick.common.core.http;

/**
 * Created by zhaolong.
 * Description: 返回数据结构定义
 * Date: 2020/5/22 0022 14:58
 */
public class Result<T>{
    T data;
    int errorCode;
    String errorMsg;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
