package com.dbscarlet.common.commonUtil

import android.os.Environment
import java.io.File

/**
 * Created by Daibing Wang on 2018/7/3.
 */
class FileUtil private constructor() {
    companion object {
        fun getSdRootDir(): File {
            return Environment.getExternalStorageDirectory()
        }
    }
}