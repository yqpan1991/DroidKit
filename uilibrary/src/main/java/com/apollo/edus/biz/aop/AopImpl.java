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
