package com.dbscarlet.applib

/**
 * Created by Daibing Wang on 2018/6/28.
 */
object Path {
    private object Group {
        const val APP = "/app/"
        const val TEST = "/test/"
    }
    const val HOME = Group.APP + "home"
    const val TINKER_TEST = Group.TEST + "versionInfo"
    const val FIND_PATCH_FILE = Group.TEST + "findPatchFile"
    const val API_TEST = Group.TEST + "apiTest"
    const val AUTHENTICATION = Group.TEST + "authentication";
}