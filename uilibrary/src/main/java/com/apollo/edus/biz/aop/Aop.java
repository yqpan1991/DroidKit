package com.apollo.edus.biz.aop;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Description.
 *
 * @author panda
 */
public interface Aop {

    <T> T makeActivityAop(Activity activity, T callback);

    <T> T makeFragmentAop(Fragment fragment, T callback);
}
