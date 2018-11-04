package com.apollo.edus.biz.aop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.v4.app.Fragment;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AopFragmentCallback<T> implements InvocationHandler {
    private final String TAG = UUID.randomUUID().toString();
    private T mOriginalCallback;
    private List<String> mFilterMethodList;
    private Fragment mAttachedFragment;

    public AopFragmentCallback(Fragment fragment, T callback){
        if(fragment == null || fragment.getActivity() == null){
            throw new RuntimeException("fragment cannot be null or fragment attached Activity cannot be null");
        }
        if(callback == null){
            throw new RuntimeException("callback cannot be null");
        }
        mFilterMethodList = new ArrayList<>();
        mFilterMethodList.add("equals");
        mFilterMethodList.add("hashCode");
        mAttachedFragment = fragment;
        mOriginalCallback = callback;
        fragment.getChildFragmentManager().beginTransaction().add(new SubFragment(this), TAG).commit();
    }

    private void handleDestroy(){
        mAttachedFragment = null;
        mOriginalCallback = null;
    }

    @Override
    public Object invoke(Object o, Method method, Object[] args) throws Throwable {
        if(mFilterMethodList.contains(method.getName())){//保留的方法,调用原来的方法
            if(mOriginalCallback != null){
                return method.invoke(mOriginalCallback, args);
            }else{
                return method.invoke(this, args);
            }
        }else{
            if(mAttachedFragment == null || mOriginalCallback == null){//为null,表示已经activity 销毁,因而不再做绑定
                return null;
            }
        }
        return method.invoke(mOriginalCallback, args);
    }

    @SuppressLint("ValidFragment")
    public static class SubFragment extends Fragment{
        private AopFragmentCallback mCallback;

        public SubFragment(AopFragmentCallback callback){
            mCallback = callback;
        }

        @Override
        public void onDestroyView() {
            super.onDestroyView();
            if(mCallback != null){
                mCallback.handleDestroy();
                mCallback = null;
            }
        }
    }
}
