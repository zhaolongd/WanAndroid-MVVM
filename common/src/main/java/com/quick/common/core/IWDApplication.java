package com.quick.common.core;

import android.app.Application;

/**
 * Created by zhaolong.
 * Description: 组件化只能有一个Application，而且为了解耦各个模块不能互相引用，
 * Date: 2020/5/22 0022 15:29
 */
public interface IWDApplication {
    public static final String[] MODULE_APP= new String[]{};
    void onCreate(Application application);
}
