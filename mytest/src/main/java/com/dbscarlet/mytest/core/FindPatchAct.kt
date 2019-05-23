package com.dbscarlet.mytest.core

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.ActPath
import com.dbscarlet.common.basic.CommonActivity
import com.dbscarlet.common.commonUtil.FileUtil
import com.dbscarlet.mytest.R
import com.scarlet.lightpermission.LightPermission
import kotlinx.android.synthetic.main.act_find_patch.*
import kotlinx.android.synthetic.main.item_find_patch.view.*
import java.io.File
import java.util.*

/**
 * Created by Daibing Wang on 2018/7/3.
 */
@Route(path = ActPath.Test.FIND_PATCH_FILE)
class FindPatchAct: CommonActivity() {
    private val fileList: MutableList<File> = mutableListOf()
    private var fileStack: Stack<File> = Stack()
    private val adapter = Adapter()
    private var patchFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.act_find_patch)
        rv_files.layoutManager = LinearLayoutManager(this)
        rv_files.adapter = adapter
        btn_install.setOnClickListener {
            setResult(Activity.RESULT_OK, Intent().putExtra("patchFile", patchFile))
            finish()
        }
        btn_delete.setOnClickListener{
            patchFile?.delete()
            updateFileList()
        }
        LightPermission.request(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onNeedExplain { activity, permRequest, requestPermission ->
                    Toast.makeText(activity, "需要存储权限", Toast.LENGTH_SHORT).show()
                    requestPermission.requestPermission()
                }.onDisable { activity, permRequest, setAppPermission ->
                    AlertDialog.Builder(activity)
                            .setMessage("需要跳转设置界面申请权限")
                            .setNegativeButton("取消") { _, _ -> setAppPermission.cancel() }
                            .setPositiveButton("确定"){ _, _ -> setAppPermission.goSetting()}
                            .show()
                }.onRefused { activity, permRequest, list ->
                    Toast.makeText(this, "缺少存储权限", Toast.LENGTH_LONG).show()
                    finish()
                }.execute { activity, permRequest ->
                    Toast.makeText(this, "加载文件列表", Toast.LENGTH_SHORT).show()
                    fileStack.push(FileUtil.getSdRootDir())
                    updateFileList()
                }
        }

    private fun updateFileList() {
        LightPermission.request(this)
                .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .execute { activity, permRequest ->
                    fileList.clear()
                    if (!fileStack.empty()) {
                        val files = fileStack.peek().listFiles()
                        if (files != null && files.isNotEmpty()) {
                            fileList.addAll(files)
                        }
                    }
                    adapter.notifyDataSetChanged()
                }
    }

    override fun onBackPressed() {
        if (fileStack.size <= 1) {
            super.onBackPressed()
        } else {
            fileStack.pop()
            updateFileList()
        }
    }

    private inner class Adapter: RecyclerView.Adapter<Adapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            return Holder(layoutInflater.inflate(R.layout.item_find_patch, parent, false))
        }

        override fun getItemCount(): Int {
            return fileList.size
        }

        override fun onBindViewHolder(holder: Holder, position: Int) {
            val file = fileList[position]
            holder.itemView?.tv_file_name?.text = file.name
            holder.itemView?.tv_flag?.text = if (file.isDirectory) ">"
            else if (file.equals(patchFile)) "√"
            else ""
            holder.itemView?.setOnClickListener{
                if (file.isDirectory) {
                    fileStack.push(file)
                    updateFileList()
                } else if(file == patchFile){
                    patchFile = null
                    it?.tv_flag?.text = ""
                    ll_btns.visibility = View.INVISIBLE
                } else {
                    patchFile = file
                    it?.tv_flag?.text = "√"
                    ll_btns.visibility = View.VISIBLE
                }
            }
        }

        inner class Holder(itemView: View): RecyclerView.ViewHolder(itemView)
    }
}