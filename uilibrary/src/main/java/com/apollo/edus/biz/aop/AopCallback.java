/*
 * Project: DingTalk
 * 
 * File Created at 2018-11-02
 * 
 * Copyright 2015 Alibaba.com Corporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.apollo.edus.biz.aop;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Description.
 *
 * @author panda
 */
public class AopCallback<T> implements InvocationHandler, Application.ActivityLifecycleCallbacks {

    private T mOriginalCallback;
    private List<String> mFilterMethodList;
    private Activity mActivity;

    public AopCallback(Activity activity, T callback){
        if(activity == null || callback == null){
            throw new RuntimeException("activity or callback cannot be null");
        }
        mFilterMethodList = new ArrayList<>();
        mFilterMethodList.add("equals");
        mFilterMethodList.add("hashCode");
        mActivity = activity;
        mOriginalCallback = callback;
        activity.getApplication().registerActivityLifecycleCallbacks(this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if(mFilterMethodList.contains(method.getName())){//保留的方法,调用原来的方法
            if(mOriginalCallback != null){
                return method.invoke(mOriginalCallback, args);
            }else{
                return method.invoke(this, args);
            }
        }else{
            if(mActivity == null || mOriginalCallback == null){//为null,表示已经activity 销毁,因而不再做绑定
                return null;
            }
        }
        return method.invoke(mOriginalCallback, args);
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if(activity == mActivity){
            activity.getApplication().unregisterActivityLifecycleCallbacks(this);
            mActivity = null;
            mOriginalCallback = null;
        }
    }
}
