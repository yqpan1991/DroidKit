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
import android.support.v4.app.Fragment;

import com.apollo.edus.biz.utils.Singleton;

import java.lang.reflect.Proxy;

/**
 * Callback with Aop
 *
 * @author panda
 */
public class AopImpl implements Aop{
    private static Singleton<Aop> mAopSingleton = new Singleton<Aop>() {
        @Override
        protected Aop create() {
            return new AopImpl();
        }
    };

    private AopImpl(){

    }

    @Override
    public <T> T makeActivityAop(Activity activity, T callback) {
        if(activity == null){
            return callback;
        }
        return (T) Proxy.newProxyInstance(callback.getClass().getClassLoader(), callback.getClass().getInterfaces()
                , new AopCallback<>(activity, callback));
    }

    @Override
    public <T> T makeFragmentAop(Fragment fragment, T callback) {
        if(fragment == null){
            return callback;
        }

        return (T) Proxy.newProxyInstance(callback.getClass().getClassLoader(), callback.getClass().getInterfaces()
                , new AopFragmentCallback<>(fragment, callback));
    }


    public static Aop getInstance(){
        return mAopSingleton.get();
    }

}
