package com.dbscarlet.applib

/**
 * Created by Daibing Wang on 2018/6/28.
 */
object RoutePath {
    private object Group {
        const val APP = "/app/"
        const val TINKER_TEST = "/tinkerTest/"
    }
    const val HOME = Group.APP + "home"
    const val TINKER_TEST = Group.TINKER_TEST + "versionInfo"
    const val FIND_PATCH_FILE = Group.TINKER_TEST + "findPatchFile"
}