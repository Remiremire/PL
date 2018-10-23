package com.dbscarlet.common.basic

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v4.app.Fragment
import dagger.android.support.AndroidSupportInjection

/**
 * Created by Daibing Wang on 2018/6/29.
 */
abstract class CommonFragment: Fragment() {

    final override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
        onAttachedContext(context)
    }

    final override fun onAttach(activity: Activity?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachedContext(activity)
        }
    }

    protected open fun onAttachedContext(context: Context?) {

    }

}