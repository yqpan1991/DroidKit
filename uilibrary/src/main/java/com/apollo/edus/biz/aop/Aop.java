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

/**
 * Description.
 *
 * @author panda
 */
public interface Aop {

    <T> T makeActivityAop(Activity activity, T callback);

    <T> T makeFragmentAop(Fragment fragment, T callback);
}
