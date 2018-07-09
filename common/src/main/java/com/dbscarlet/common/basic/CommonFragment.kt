package com.dbscarlet.common.basic

import android.app.Activity
import android.content.Context
import android.os.Build
import android.support.v4.app.Fragment
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import com.dbscarlet.common.permission.PermissionRequest
import dagger.android.support.AndroidSupportInjection

/**
 * Created by Daibing Wang on 2018/6/29.
 */
abstract class CommonFragment: Fragment(), BaseView {
    private var progressList: MutableList<ProgressHandler>? = null

    override fun toast(msgRes: Int) {
        toast(resources.getString(msgRes))
    }

    override fun toast(msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

    override fun handleProgress(): ProgressHandler {
        val progress = object: ProgressHandler {
            override fun dismiss() {
                progressList?.remove(this)
            }

            val progressBar : ProgressBar = ProgressBar(context)

            override fun show(msg: String?, title: String?) {
                progressBar.visibility = View.VISIBLE
                progressBar.isIndeterminate = false
            }

            override fun update(progress: Int, total: Int) {
                if (!progressBar.isIndeterminate) {
                    progressBar.isIndeterminate = true
                }
                progressBar.max = total
                progressBar.progress = progress
            }

            override fun hide() {
                progressBar.visibility = View.GONE
            }
        }
        progressList = progressList ?: mutableListOf()
        progressList?.add(progress)
        return progress
    }

    abstract fun getPresenters() : Array<IPresenter<*>>?

    final override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        getPresenters()?.forEach { lifecycle.addObserver(it) }
        super.onAttach(context)
        onAttachedContext(context)
    }

    final override fun onAttach(activity: Activity?) {
        AndroidSupportInjection.inject(this)
        getPresenters()?.forEach { lifecycle.addObserver(it) }
        super.onAttach(activity)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            onAttachedContext(activity)
        }
    }

    open protected fun onAttachedContext(context: Context?) {

    }

    override fun onDetach() {
        super.onDetach()
        getPresenters()?.forEach { lifecycle.removeObserver(it) }
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