package com.apollo.edus.biz.aop;

import com.apollo.edus.biz.utils.Singleton;

import java.util.ArrayList;
import java.util.List;

public class Demo {
    private static Singleton<Demo> mSingleTon = new Singleton<Demo>() {
        @Override
        public Demo create() {
            return new Demo();
        }
    };

    private List<Listener> mListenerList;

    private Demo(){
        mListenerList = new ArrayList<>();
    }

    public static Demo getInstance(){
        return mSingleTon.get();
    }

    public void registerListener(Listener listener){
        if(listener != null){
            mListenerList.add(listener);
        }
    }

    public void unregisterListener(Listener listener){
        if(listener != null){
            mListenerList.remove(listener);
        }
    }


}
