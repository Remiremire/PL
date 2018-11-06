package com.dbscarlet.mediademo.di

import com.dbscarlet.mediademo.MediaMainAct
import com.dbscarlet.mediademo.image.CameraTestAct
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * Created by Daibing Wang on 2018/11/6.
 */
@Module
abstract class MediaInjectors{
    @ContributesAndroidInjector
    abstract fun mediaTest(): MediaMainAct

    @ContributesAndroidInjector
    abstract fun cameraTest(): CameraTestAct
}