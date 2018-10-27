package com.apollo.edus.uilibrary.widget.loadingandresult;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.apollo.edus.uilibrary.R;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 页面从加载中,到加载的结果(正常数据结果页,空页面,错误页面)
 */
public class LoadingAndResultContainer extends FrameLayout {
    static final ThreadLocal<Map<String, Constructor<Behavior>>> sConstructors =
            new ThreadLocal<>();

    private final String TAG = this.getClass().getSimpleName();

    public enum DISPLAY_STATUS {
        LOADING(0),
        COMMON_RESULT(1),
        EMPTY(2),
        ERROR(3);

        private int status;

        private DISPLAY_STATUS(int status){
            this.status = status;
        }

        public int getStatus(){
            return status;
        }

        public static DISPLAY_STATUS fromValue(int status){
            if(status == LOADING.getStatus()){
                return LOADING;
            }else if(status == COMMON_RESULT.getStatus()){
                return COMMON_RESULT;
            }else if(status == EMPTY.getStatus()){
                return EMPTY;
            }else if(status == ERROR.getStatus()){
                return ERROR;
            }else{
                return null;
            }
        }

    };

    private View mLoadingView;
    private View mEmptyView;
    private View mErrorView;
    private View mContentView;

    private DISPLAY_STATUS mDisplayStatus;

    public LoadingAndResultContainer(@NonNull Context context) {
        this(context, null);
    }

    public LoadingAndResultContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingAndResultContainer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }


    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        if(attrs == null || context == null){
            return;
        }
        TypedArray ta = context.getTheme().obtainStyledAttributes(attrs, R.styleable.LoadingAndResultContainer, defStyleAttr, 0);
        int loadingStringResId = ta.getResourceId(R.styleable.LoadingAndResultContainer_loading_behavior, 0);
        int contentStringResId = ta.getResourceId(R.styleable.LoadingAndResultContainer_content_behavior, 0);
        int emptyStringResId = ta.getResourceId(R.styleable.LoadingAndResultContainer_empty_behavior, 0);
        int errorStringResId = ta.getResourceId(R.styleable.LoadingAndResultContainer_error_behavior, 0);
        Integer defaultStatus = ta.getInteger(R.styleable.LoadingAndResultContainer_default_status_v2, DISPLAY_STATUS.LOADING.getStatus());
        if(loadingStringResId > 0){
            Behavior behavior = parseBehavior(getContext(), getResources().getString(loadingStringResId));
            if(behavior != null){
                mLoadingView = behavior.onAttachedView(this);
            }
        }
        if(contentStringResId > 0){
            Behavior behavior = parseBehavior(getContext(), getResources().getString(contentStringResId));
            if(behavior != null){
                mContentView = behavior.onAttachedView(this);
            }
        }
        if(emptyStringResId > 0){
            Behavior behavior = parseBehavior(getContext(), getResources().getString(emptyStringResId));
            if(behavior != null){
                mEmptyView = behavior.onAttachedView(this);
            }
        }
        if(errorStringResId > 0){
            Behavior behavior = parseBehavior(getContext(), getResources().getString(errorStringResId));
            if(behavior != null){
                mErrorView = behavior.onAttachedView(this);
            }
        }
        DISPLAY_STATUS displayStatus = DISPLAY_STATUS.LOADING;
        if(defaultStatus != null){
            displayStatus = DISPLAY_STATUS.fromValue(defaultStatus.intValue());
        }
        ta.recycle();
        refreshStatus(displayStatus, false);
    }

    private void refreshStatus(DISPLAY_STATUS displayStatus, boolean isForce) {
        if(isSameStatus(displayStatus) && !isForce){
            return;
        }
        if(displayStatus == DISPLAY_STATUS.LOADING){
            showLoading(isForce);
        }else if(displayStatus == DISPLAY_STATUS.COMMON_RESULT){
            showCommonResult(isForce);
        }else if(displayStatus == DISPLAY_STATUS.EMPTY){
            showEmpty(isForce);
        }else if(displayStatus == DISPLAY_STATUS.ERROR){
            showError(isForce);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        int count = getChildCount();
        if(count <= 0){
            return;
        }
        List<Integer> validIdList = new ArrayList<>();
        validIdList.add(R.id.dg_larc_loading_id);
        validIdList.add(R.id.dg_larc_empty_id);
        validIdList.add(R.id.dg_larc_error_id);
        validIdList.add(R.id.dg_larc_content_id);
        Set<Integer> hittedSet = new HashSet<>();
        //找到所有的childView,如果id不属于正常的id的列表,那么就抛出异常
        for(int index = 0; index < count ; index ++){
            if(!validIdList.contains(getChildAt(index).getId())){
                throw new RuntimeException("view id is invalid, please check");
            }
            View childView = getChildAt(index);
            if(hittedSet.contains(childView.getId())){
                throw new RuntimeException("child view id cannot be same in container");
            }
            hittedSet.add(childView.getId());
            if(R.id.dg_larc_loading_id == childView.getId()){
                if(mLoadingView == null){
                    mLoadingView = findViewById(R.id.dg_larc_loading_id);
                }
            }else if(R.id.dg_larc_empty_id == childView.getId()){
                if(mEmptyView == null){
                    mEmptyView = findViewById(R.id.dg_larc_empty_id);
                }
            }else if(R.id.dg_larc_error_id == childView.getId()){
                if(mErrorView == null){
                    mErrorView = findViewById(R.id.dg_larc_error_id);
                }
            }else if(R.id.dg_larc_content_id == childView.getId()){
                if(mContentView == null){
                    mContentView = findViewById(R.id.dg_larc_content_id);
                }
            }
        }
        refreshStatus(mDisplayStatus, true);
    }

    public View getLoadingView() {
        return mLoadingView;
    }

    public void setLoadingView(View loadingView) {
        if(loadingView == null){
            return;
        }
        if(loadingView.getParent() != null){
            throw new RuntimeException("setLoadingView view cannot have parent");
        }
        if(mLoadingView != null){
            removeView(mLoadingView);
        }
        this.mLoadingView = loadingView;
        addView(mLoadingView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

    }

    public View getEmptyView() {
        return mEmptyView;
    }

    public void setEmptyView(View emptyView) {
        if(emptyView == null){
            return;
        }
        if(emptyView.getParent() != null){
            throw new RuntimeException("setEmptyView view cannot have parent");
        }
        if(mEmptyView != null){
            removeView(mEmptyView);
        }
        this.mEmptyView = emptyView;
        addView(mEmptyView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public View getErrorView() {
        return mErrorView;
    }

    public void setErrorView(View errorView) {
        if(errorView == null){
            return;
        }
        if(errorView.getParent() != null){
            throw new RuntimeException("setEmptyView view cannot have parent");
        }
        if(mErrorView != null){
            removeView(mErrorView);
        }
        this.mErrorView = errorView;
        addView(mErrorView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public View getContentView() {
        return mContentView;
    }

    public void setContentView(View contentView) {
        if(contentView == null){
            return;
        }
        if(contentView.getParent() != null){
            throw new RuntimeException("setEmptyView view cannot have parent");
        }
        if(mContentView != null){
            removeView(mContentView);
        }
        this.mContentView = contentView;
        addView(mContentView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public DISPLAY_STATUS getDisplayStatus() {
        return mDisplayStatus;
    }

    public void showLoading(){
        showLoading(false);
    }

    private void showLoading(boolean forceUpdate){
        if(isSameStatus(DISPLAY_STATUS.LOADING) && !forceUpdate){
            return;
        }
        mDisplayStatus = DISPLAY_STATUS.LOADING;
        setViewVisibility(mLoadingView, View.VISIBLE);
        setViewVisibility(mContentView, View.GONE);
        setViewVisibility(mEmptyView, View.GONE);
        setViewVisibility(mErrorView, View.GONE);
    }

    private boolean isSameStatus(DISPLAY_STATUS status) {
        return mDisplayStatus == status;
    }

    public void showCommonResult(){
        showCommonResult(false);
    }

    private void showCommonResult(boolean forceUpdate){
        if(isSameStatus(DISPLAY_STATUS.COMMON_RESULT) && !forceUpdate){
            return;
        }
        mDisplayStatus = DISPLAY_STATUS.COMMON_RESULT;
        setViewVisibility(mLoadingView, View.GONE);
        setViewVisibility(mContentView, View.VISIBLE);
        setViewVisibility(mEmptyView, View.GONE);
        setViewVisibility(mErrorView, View.GONE);
    }

    public void showEmpty(){
        showEmpty(false);
    }

    private void showEmpty(boolean forceUpdate){
        if(isSameStatus(DISPLAY_STATUS.EMPTY) && !forceUpdate){
            return;
        }
        mDisplayStatus = DISPLAY_STATUS.EMPTY;
        setViewVisibility(mLoadingView, View.GONE);
        setViewVisibility(mContentView, View.GONE);
        setViewVisibility(mEmptyView, View.VISIBLE);
        setViewVisibility(mErrorView, View.GONE);
    }

    public void showError(){
        showError(false);
    }

    private void showError(boolean forceUpdate){
        if(isSameStatus(DISPLAY_STATUS.ERROR) && !forceUpdate){
            return;
        }
        mDisplayStatus = DISPLAY_STATUS.ERROR;
        setViewVisibility(mLoadingView, View.GONE);
        setViewVisibility(mContentView, View.GONE);
        setViewVisibility(mEmptyView, View.GONE);
        setViewVisibility(mErrorView, View.VISIBLE);
    }

    private void setViewVisibility(View view , int visibility){
        if(view != null){
            view.setVisibility(visibility);
        }
    }

    public interface Behavior {
        View onAttachedView(ViewGroup parentView);
    }

    static LoadingAndResultContainer.Behavior parseBehavior(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            return null;
        }

        final String fullName = name;
        try {
            Map<String, Constructor<Behavior>> constructors = sConstructors.get();
            if (constructors == null) {
                constructors = new HashMap<>();
                sConstructors.set(constructors);
            }
            Constructor<Behavior> c = constructors.get(fullName);
            if (c == null) {
                final Class<Behavior> clazz = (Class<Behavior>) Class.forName(fullName, true,
                        context.getClassLoader());
                c = clazz.getConstructor(null);
                c.setAccessible(true);
                constructors.put(fullName, c);
            }
            return c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Could not inflate Behavior subclass " + fullName, e);
        }
    }
}
