package com.quick.common.core;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import com.alibaba.android.arouter.launcher.ARouter;
import com.quick.common.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

/**
 * Created by zhaolong.
 * Description: WDApplication
 * Date: 2020/5/22 15:23
 */
public class WDApplication extends Application {
    /**
     * 主线程ID
     */
    private static int mMainThreadId = -1;
    /**
     * 主线程ID
     */
    private static Thread mMainThread;
    /**
     * 主线程Handler
     */
    private static Handler mMainThreadHandler;
    /**
     * 主线程Looper
     */
    private static Looper mMainLooper;

    /**
     * context 全局唯一的上下文
     */
    private static Context context;

    private static SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mMainThreadHandler = new Handler();
        mMainLooper = getMainLooper();
        sharedPreferences = getSharedPreferences("share.xml", MODE_PRIVATE);

        ARouter.openLog();     // Print log
        ARouter.openDebug();
        ARouter.init(this);//阿里路由初始化

        //IM或者推送等服务的appliation初始化
        loadModuleApp();
    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    /**
     * 加载各个模块的Application，例如：推送和IM等模块都需要有Application，
     * 但组件化只能有一个Application，而且为了解耦各个模块不能互相引用，
     * 所以只能通过反射方式，把这些module_appliation进行初始化
     */
    private void loadModuleApp(){
        for (String moduleImpl : IWDApplication.MODULE_APP){
            try {
                Class<?> clazz = Class.forName(moduleImpl);
                Object obj = clazz.newInstance();
                if (obj instanceof IWDApplication){
                    ((IWDApplication) obj).onCreate(this);
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        ARouter.getInstance().destroy();
    }

    public static SharedPreferences getShare() {
        return sharedPreferences;
    }

    /**
     * @return 全局唯一的上下文
     * @author: 康海涛 QQ2541849981
     * @describe: 获取全局Application的上下文
     */
    public static Context getContext() {
        return context;
    }

    /**
     * 获取主线程ID
     */
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    /**
     * 获取主线程
     */
    public static Thread getMainThread() {
        return mMainThread;
    }

    /**
     * 获取主线程的handler
     */
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    /**
     * 获取主线程的looper
     */
    public static Looper getMainThreadLooper() {
        return mMainLooper;
    }
}
