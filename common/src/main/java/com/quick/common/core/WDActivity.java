package com.quick.common.core;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import com.alibaba.android.arouter.launcher.ARouter;
import com.quick.common.BR;
import com.quick.common.util.LogUtils;
import java.lang.reflect.ParameterizedType;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

/**
 * Created by zhaolong.
 * Description: WDActivity
 * Date: 2020/5/22 0022 15:38
 */
public abstract class WDActivity<VM extends WDViewModel,VDB extends ViewDataBinding> extends AppCompatActivity {
    public Dialog mLoadDialog;// 加载框

    /**
     * 记录处于前台的Activity
     */
    private static WDActivity mForegroundActivity = null;

    protected VM viewModel;
    protected VDB binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //打印堆栈ID
        LogUtils.e("getTaskId = " + getTaskId());
        initLoad();
        setContentView(getLayoutId());
        ARouter.getInstance().inject(this);
        viewModel = new ViewModelProvider(this).get(getTClass());
        viewModel.dialog.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean o) {
                if (o)
                    mLoadDialog.show();
                else mLoadDialog.cancel();
            }
        });
        viewModel.finish.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer o) {
                finish();
            }
        });

        binding = DataBindingUtil.setContentView(this, getLayoutId());
        //所有布局中dababinding对象变量名称都是vm
        binding.setVariable(BR.vm,viewModel);
        binding.executePendingBindings();//立即更新UI
//        binding.setLifecycleOwner(this);
        getLifecycle().addObserver(viewModel);
        initView(savedInstanceState);
    }


    /**
     * 获取泛型对相应的Class对象
     * @return
     */
    private Class<VM>  getTClass(){
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();
        //返回表示此类型实际类型参数的 Type 对象的数组()，想要获取第二个泛型的Class，所以索引写1
        return (Class)type.getActualTypeArguments()[0];//<T>
    }

    /**
     * 设置layoutId
     *
     * @return
     */
    protected abstract int getLayoutId();

    /**
     * 初始化视图
     */
    protected abstract void initView(Bundle savedInstanceState);

    /**
     * 初始化加载框
     */
    private void initLoad() {
        mLoadDialog = new ProgressDialog(this);// 加载框
        mLoadDialog.setCanceledOnTouchOutside(false);
        mLoadDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (mLoadDialog.isShowing() && keyCode == KeyEvent.KEYCODE_BACK) {
                    cancelLoadDialog();//加载消失的同时
                }
                return false;
            }
        });
    }

    //取消操作：请求或者其他
    public void cancelLoadDialog() {
        viewModel.dialog.setValue(false);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mForegroundActivity = this;
    }

    /**
     * 获取当前处于前台的activity
     */
    public static WDActivity getForegroundActivity() {
        return mForegroundActivity;
    }

}
