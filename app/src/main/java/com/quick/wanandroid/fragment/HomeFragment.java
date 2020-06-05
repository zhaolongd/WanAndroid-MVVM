package com.quick.wanandroid.fragment;

import android.os.Bundle;
import com.quick.common.core.WDFragment;
import com.quick.wanandroid.R;
import com.quick.wanandroid.databinding.FragmentHomeBinding;
import com.quick.wanandroid.vm.HomeViewModel;

/**
 * Created by zhaolong.
 * Description: 首页
 * Date: 2020/5/26 0026 14:03
 */
public class HomeFragment extends WDFragment<HomeViewModel, FragmentHomeBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

}
