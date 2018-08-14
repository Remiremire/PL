package com.dbscarlet.common.basic

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v4.app.Fragment
import com.dbscarlet.common.permission.PermissionRequest
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

    /**
     * 动态权限申请，默认直接调用CommonActivity的实现
     */
    open fun requestPermissions(requestCode: Int): PermissionRequest {
        when(activity) {
            is CommonActivity-> return (activity as CommonActivity).requestPermissions(requestCode)
        }
        throw IllegalStateException("the activity of ${this::class.simpleName} is not extends of CommonActivity, can't use requestPermission")
    }
}