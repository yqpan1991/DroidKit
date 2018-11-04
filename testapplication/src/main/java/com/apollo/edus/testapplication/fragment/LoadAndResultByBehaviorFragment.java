package com.apollo.edus.testapplication.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apollo.edus.biz.aop.AopImpl;
import com.apollo.edus.biz.aop.Demo;
import com.apollo.edus.biz.aop.Listener;
import com.apollo.edus.testapplication.R;
import com.apollo.edus.uilibrary.widget.loadingandresult.LoadingAndResultContainer;

public class LoadAndResultByBehaviorFragment extends Fragment {

    private View mVLoading;
    private View mVEmptyView;
    private View mVErrorView;
    private View mVResultView;
    private LoadingAndResultContainer mLarcView;

    private int index = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_loading_and_result_by_behavior, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLarcView = view.findViewById(R.id.larc_content);
        mVLoading = mLarcView.getLoadingView();
        mVEmptyView = mLarcView.getEmptyView();
        mVErrorView = mLarcView.getErrorView();
        mVErrorView.findViewById(R.id.tv_retry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLoadData();
            }
        });
        mVResultView = mLarcView.getContentView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handleLoadData();
        Demo.getInstance().registerListener(AopImpl.getInstance().makeFragmentAop(this, new Listener() {
            @Override
            public void onSuccess() {

            }
        }));
        /*Demo.getInstance().registerListener( new Listener() {
            @Override
            public void onSuccess() {

            }
        });*/
    }

    private void handleLoadData(){
        mLarcView.showLoading();
        mLarcView.postDelayed(new Runnable() {
            @Override
            public void run() {
                index++;
                if(index % 2 == 1){//show error
                    mLarcView.showError();
                }else{//show result
                    mLarcView.showCommonResult();
                }
            }
        }, 400);
    }
}
