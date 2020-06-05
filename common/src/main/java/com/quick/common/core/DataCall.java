package com.quick.common.core;

import com.quick.common.core.exception.ApiException;

/**
 * Created by zhaolong.
 * Date: 2020/5/22 0022 14:32
 */
public interface DataCall<T> {
    void success(T data);
    void fail(ApiException data);
}
