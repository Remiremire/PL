package com.dbscarlet.mediademo.image

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.ActPath
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.mediademo.Constant
import com.dbscarlet.mediademo.R
import com.dbscarlet.mediademo.databinding.ActCameraTestBinding
import com.scarlet.lightpermission.LightPermission
import java.io.File
import kotlin.math.max

/**
 * Created by Daibing Wang on 2018/11/6.
 */
@Route(path = ActPath.Media.CAMERA_TEST)
class CameraTestAct: BaseActivity<ActCameraTestBinding>() {
    private val request_img_capture = 0
    private val imgFilePath = Constant.MODULE_ROOT_PATH + File.separator + "cameraTest.jpg"
    private val defWidth = 200
    private val defHeight = 200

    override fun getContentLayout(): Int {
        return R.layout.act_camera_test
    }

    override fun initView() {
        binding.ivCameraImg.setOnClickListener {
            LightPermission.request(this)
                    .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                    .execute { _, _ ->
                        val file = File(imgFilePath)
                        val uri = createFileUri(file)
                        startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                                .putExtra(MediaStore.EXTRA_OUTPUT, uri)
                                .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION),
                                request_img_capture)
                    }
        }
        binding.tvInsertImg.setOnClickListener {
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
            val file = File(imgFilePath)
            val uri = createFileUri(file)
            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    .putExtra(MediaStore.EXTRA_OUTPUT, uri)
        }
    }

    private fun createFileUri(file: File): Uri {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            Uri.fromFile(file)
        } else {
            FileProvider.getUriForFile(this, application.packageName, file)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            request_img_capture -> {
                if (resultCode == Activity.RESULT_OK) {
                    val options = BitmapFactory.Options()
                    options.inJustDecodeBounds = true
                    BitmapFactory.decodeFile(imgFilePath, options)
                    options.inSampleSize = max(options.outWidth / defWidth, options.outHeight / defHeight)
                    options.inJustDecodeBounds = false
                    val imgBitmap = BitmapFactory.decodeFile(imgFilePath, options)
                    binding.ivCameraImg.setImageBitmap(imgBitmap)
                }
            }
        }
    }
}