package com.dbscarlet.mediademo

import android.os.Environment
import java.io.File

/**
 * Created by Daibing Wang on 2018/11/6.
 */
internal object Constant {
    val MODULE_ROOT_PATH = "${Environment.getExternalStorageDirectory().absolutePath}${File.separator}mediaDemo"
}