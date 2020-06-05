package com.quick.common.core;

import android.os.Bundle;
import com.alibaba.android.arouter.launcher.ARouter;
import com.quick.common.core.exception.ApiException;
import com.quick.common.core.http.ADResult;
import com.quick.common.core.http.NetworkManager;
import com.quick.common.core.http.Result;
import java.lang.reflect.ParameterizedType;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zhaolong.
 * Description: 每个Activity对应一个viewmodel，又或者多个Fragment同Activity共享一个viewmodel，负责拿到model(请求网络或者封装)，通过Livedata完成页面数据更新
 * Date: 2020/5/22 14:46
 */
public abstract class WDViewModel<R> extends ViewModel implements LifecycleObserver {
    public final static int REQUEST_TYPE_DEFAULT = 0; //默认IRquest
    public final static int REQUEST_TYPE_APP_AD = 1;  //例：这个为广告请求接口，由于接口类中方法太多，所以写了另外一个业务接口

    public final static int RESPONSE_TYPE_DEFAULT = 0;
    public final static int RESPONSE_TYPE_APP_AD = 1;

    //负责每个页面中的dialog弹出框隐藏和展示
    public MutableLiveData<Boolean> dialog = new MutableLiveData<>();
    //关闭页面
    public MutableLiveData<Void> finish = new MutableLiveData<>();
    //如果上个页面使用了startActivityForResult，则本页面回传值需要使用此参数进行回传值的操作
    public MutableLiveData<Void> forResult = new MutableLiveData<>();

    protected R iRequest;

    public WDViewModel(){
        Class<R> tClass = getTClass();
        iRequest = NetworkManager.instance().create(getRequestType(),tClass);
    }
    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    protected void create(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    protected void start(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    protected void resume(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    protected void pause(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    protected void stop(){}

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    protected void destroy(){}

    /**
     * @param path 传送Activity的
     */
    public void intentByRouter(String path) {
        ARouter.getInstance().build(path)
                .navigation();
    }

    /**
     * @param path 传送Activity的
     * @param bundle
     */
    public void intentByRouter(String path, Bundle bundle) {
        ARouter.getInstance().build(path)
                .with(bundle)
                .navigation();
    }

    public void finish(){
        finish.setValue(null);
    }

    /**
     * 获取泛型对相应的Class对象
     * @return
     */
    private Class<R>  getTClass(){
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();
        //返回表示此类型实际类型参数的 Type 对象的数组()，想要获取第二个泛型的Class，所以索引写1
        return (Class)type.getActualTypeArguments()[0];//<T>
    }

    /**
     * 请求数据，所有网络操作请使用本方法
     * @param observable
     * @param dataCall
     * @return
     */
    public Disposable request(Observable observable, final DataCall dataCall) {
        return observable.subscribeOn(Schedulers.io())//将请求调度到子线程上
                .observeOn(AndroidSchedulers.mainThread())//观察响应结果，把响应结果调度到主线程中处理
                .onErrorReturn(new Function<Throwable, Throwable>() {//处理所有异常
                    @Override
                    public Throwable apply(Throwable throwable) throws Exception {
                        return throwable;
                    }
                })
                .subscribe(getConsumer(dataCall), new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable e) throws Exception {
                        dataCall.fail(ApiException.handleException(e));
                    }
                });
    }

    /**
     * 此方法用来确定采用的Rtrofit中baseUrl
     * 由于Retrofit特性，baseUrl不能随意改动，当大项目拥有
     * 多个域名控制不同业务的时候，则需要不同的Retrofit
     * @TODO 可以根据需要在子类中重写
     */
    protected int getRequestType() {
        return REQUEST_TYPE_DEFAULT;
    }

    /**
     * 返回值类型，方便不同接口返回数据结构不同的情况，参见{@link #getConsumer(DataCall)}
     * 应对大项目多数据结构
     * @TODO 可以根据需要在子类中重写
     */
    protected int getResponseType() {
        return RESPONSE_TYPE_DEFAULT;
    }

    /**
     * @description 根据返回值{@link #getResponseType()}灵活改变Consumer或者自己直接重写都可以
     */
    protected Consumer getConsumer(final DataCall dataCall){
        if (getResponseType() == RESPONSE_TYPE_APP_AD) {
            return new Consumer<ADResult>() {
                @Override
                public void accept(ADResult result) throws Exception {
                    if (result.getCode()==0) {
                        dataCall.success(result.getData());
                    }else{
                        dataCall.fail(new ApiException(String.valueOf(result.getCode()), result.getMsg()));
                    }
                }
            };
        }else{
            return new Consumer<Result>() {
                @Override
                public void accept(Result result) throws Exception {
                    /**
                     * errorCode = 0 代表执行成功，不建议依赖任何非0的 errorCode.
                     * errorCode = -1001 代表登录失效，需要重新登录。
                     */
                    if (result.getErrorCode() == 0) {
                        dataCall.success(result.getData());
                    }else{
                        dataCall.fail(new ApiException(String.valueOf(result.getErrorCode()), result.getErrorMsg()));
                    }
                }
            };
        }
    }

    public MutableLiveData<Boolean> getDialog(){
        return dialog;
    }

}
