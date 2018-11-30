package com.dbscarlet.mediademo.image

import android.Manifest
import android.hardware.Camera
import android.os.Handler
import android.os.Looper
import android.view.SurfaceHolder
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.ActPath
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.mediademo.R
import com.dbscarlet.mediademo.databinding.ActCameraSurfaceBinding
import com.scarlet.lightpermission.LightPermission

/**
 * Created by Daibing Wang on 2018/11/26.
 */
@Route(path = ActPath.Media.CAMERA_SURFACE)
class CameraSurfaceAct: BaseActivity<ActCameraSurfaceBinding>()
        , SurfaceHolder.Callback, Camera.PictureCallback {
    var surfaceHolder: SurfaceHolder? = null
    var mCamera: Camera? = null

    override fun getContentLayout(): Int {
        return R.layout.act_camera_surface
    }

    override fun initView() {
        surfaceHolder = binding.camera.holder
        surfaceHolder?.addCallback(this)
        binding.camera.setOnLongClickListener {
            mCamera?.takePicture(null, null, null)
            true
        }
    }

    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
        mCamera?.stopPreview()
        mCamera?.release()
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
        LightPermission.request(this)
                .permissions(Manifest.permission.CAMERA)
                .execute { _, _ ->
                    bindCamera()
                }
    }

    override fun onPictureTaken(data: ByteArray?, camera: Camera?) {
        Handler(Looper.getMainLooper())
                .postDelayed({
                    mCamera?.reconnect()
                    mCamera?.startPreview()
                }, 3000)
    }

    private fun bindCamera() {
        val camera = Camera.open()
        mCamera = camera
        try {
            val parameters = camera.parameters
            camera.setDisplayOrientation(90)
//            parameters.setRotation(90)
            camera.parameters = parameters
            camera.setPreviewDisplay(surfaceHolder)
            camera.startPreview()
        } catch (e: Exception) {
            camera.release()
            mCamera = null
        }

    }
}