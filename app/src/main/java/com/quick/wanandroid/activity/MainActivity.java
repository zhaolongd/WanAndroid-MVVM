package com.quick.wanandroid.activity;

import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.quick.common.core.WDActivity;
import com.quick.wanandroid.R;
import com.quick.wanandroid.databinding.ActivityMainBinding;
import com.quick.wanandroid.fragment.HomeFragment;
import com.quick.wanandroid.vm.MainViewModel;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends WDActivity<MainViewModel, ActivityMainBinding> {
    private HomeFragment mHomeFragment;
    private Fragment[] fragments;
    private int lastfragment = 0;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        mHomeFragment = new HomeFragment();
        fragments = new Fragment[]{mHomeFragment, mHomeFragment, mHomeFragment, mHomeFragment, mHomeFragment};
        getSupportFragmentManager().beginTransaction().replace(R.id.container, mHomeFragment).show(mHomeFragment).commit();
        binding.bottomNavView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void switchFragment(int lastfragment, int index) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //隐藏上个Fragment
        transaction.hide(fragments[lastfragment]);
        if (fragments[index].isAdded() == false) {
            transaction.add(R.id.container, fragments[index]);
        }
        transaction.show(fragments[index]).commitAllowingStateLoss();
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.navigation_home:
                    if (lastfragment != 0) {
                        switchFragment(lastfragment, 0);
                        lastfragment = 0;
                    }
                    return true;
                case R.id.navigation_wenda:
                    if (lastfragment != 1) {
                        switchFragment(lastfragment, 1);
                        lastfragment = 1;
                    }
                    return true;
                case R.id.navigation_public:
                    if (lastfragment != 2) {
                        switchFragment(lastfragment, 2);
                        lastfragment = 2;
                    }
                    return true;
                case R.id.navigation_structure:
                    if (lastfragment != 3) {
                        switchFragment(lastfragment, 3);
                        lastfragment = 3;
                    }
                    return true;
                case R.id.navigation_project:
                    if (lastfragment != 4) {
                        switchFragment(lastfragment, 4);
                        lastfragment = 4;
                    }
                    return true;
                default:
                    break;
            }
            return false;
        }
    };
}
