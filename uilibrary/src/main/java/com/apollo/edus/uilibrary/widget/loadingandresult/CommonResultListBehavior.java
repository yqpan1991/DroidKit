package com.apollo.edus.uilibrary.widget.loadingandresult;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.apollo.edus.uilibrary.R;

public class CommonResultListBehavior implements LoadingAndResultContainer.Behavior {
    @Override
    public View onAttachedView(ViewGroup parentView) {
        View currentView = LayoutInflater.from(parentView.getContext()).inflate(R.layout.edus_layout_home_page_content, parentView, false);
        parentView.addView(currentView, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return currentView;
    }
}
