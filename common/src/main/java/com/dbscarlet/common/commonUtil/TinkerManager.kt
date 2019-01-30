package com.dbscarlet.common.commonUtil

import android.app.Activity
import android.app.AlarmManager
import android.app.Application
import android.app.PendingIntent
import android.content.Context
import android.support.v7.app.AlertDialog
import com.tencent.tinker.lib.service.PatchResult
import com.tencent.tinker.lib.tinker.TinkerInstaller
import com.tinkerpatch.sdk.TinkerPatch
import com.tinkerpatch.sdk.loader.TinkerPatchApplicationLike
import com.tinkerpatch.sdk.tinker.service.TinkerServerResultService

/**
 * Created by Daibing Wang on 2018/7/2.
 */
object TinkerManager {
    var tinkerInstallCallback: InstallCallback? = null

    fun initTinker(app: Application) {
        val applicationLike = TinkerPatchApplicationLike.getTinkerPatchApplicationLike()
        val builder = TinkerPatch.Builder(applicationLike)
        builder.resultServiceClass(TinkerResultService::class.java)
        TinkerPatch.init(builder.build())
                .apply {
                    reflectPatchLibrary()
                    setPatchRestartOnSrceenOff(true)
                    setPatchRollbackOnScreenOff(true)
                    setFetchPatchIntervalByHours(-1)
                    fetchPatchUpdateAndPollWithInterval()
                    setPatchResultCallback {
                        tinkerInstallCallback?.onInstallResult(InstallResult(it))
                    }
                }
    }

    fun installTinkerPatch(context: Context, patchFile: String) {
        TinkerInstaller.onReceiveUpgradePatch(context, patchFile)
    }

    fun restartApp(context: Context) {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)
        val restartIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        alarmManager?.set(AlarmManager.RTC, System.currentTimeMillis() + 1500, restartIntent)
        System.exit(0)
    }

    fun showRestartDialog(activity: Activity) {
        AlertDialog.Builder(activity)
                .setTitle("补丁加载成功")
                .setMessage("重启应用, 以完成更新？")
                .setPositiveButton("确定") { dialog, i -> restartApp(activity) }
                .setNegativeButton("取消") { dialog, i -> }.show()
    }


}

interface InstallCallback {
    fun onInstallResult(installResult: InstallResult)
}

class InstallResult internal constructor(private val result: PatchResult?) {
    val isSuccess = result?.isSuccess ?: false
    val rawPatchFilePath: String? = result?.rawPatchFilePath
    val costTime = result?.costTime ?: 0L
    val e: Throwable? = result?.e
    val patchVersion: String? = result?.patchVersion

    override fun toString(): String {
        return "InstallResult(result=$result, isSuccess=$isSuccess, " +
                "rawPatchFilePath='$rawPatchFilePath', costTime=$costTime, " +
                "e=$e, patchVersion=$patchVersion)"
    }

}

class TinkerResultService: TinkerServerResultService()

