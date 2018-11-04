package com.apollo.edus.biz.utils;

public abstract class Singleton<T> {
    private T mData;

    protected abstract T create();

    public T get(){
        if(mData == null){
            synchronized (this){
                if(mData == null){
                    mData = create();
                }
            }
        }
        return mData;
    }
}
