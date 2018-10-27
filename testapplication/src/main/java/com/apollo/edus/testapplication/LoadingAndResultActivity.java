package com.apollo.edus.testapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;

import com.apollo.edus.testapplication.fragment.LoadAndResultByBehaviorFragment;
import com.apollo.edus.testapplication.fragment.LoadAndResultByMixFragment;
import com.apollo.edus.testapplication.fragment.LoadAndResultByViewFragment;

public class LoadingAndResultActivity extends AppCompatActivity {
    private RadioGroup mRgSelector;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_and_result);
        initView();
        initListener();
        initData();
        //1. 采用behavior的方式 load
        //2. 采用 view的方式 load
        //3. 采用混合的方式 load
    }

    private void initView() {
        mRgSelector = findViewById(R.id.rg_selector);
    }

    private void initListener() {
        mRgSelector.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(i == R.id.bt_behavior){
                    showFragment(LoadAndResultByBehaviorFragment.class);
                }else if(i == R.id.bt_subview){
                    showFragment(LoadAndResultByViewFragment.class);
                }else if(i == R.id.bt_complex){
                    showFragment(LoadAndResultByMixFragment.class);
                }
            }
        });
    }


    private void initData() {
        mRgSelector.check(R.id.bt_behavior);
    }

    private void showFragment(Class<? extends Fragment> fragmentClass){
        if(fragmentClass == null){
            return;
        }
        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_fragment_container, fragmentClass.newInstance()).commit();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


}
