package com.dbscarlet.tinkertest.core

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
import com.dbscarlet.applib.Path
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.common.basic.IPresenter
import com.dbscarlet.common.util.FileUtil
import com.dbscarlet.tinkertest.R
import kotlinx.android.synthetic.main.act_find_patch.*
import kotlinx.android.synthetic.main.item_find_patch.view.*
import java.io.File
import java.util.*

/**
 * Created by Daibing Wang on 2018/7/3.
 */
@Route(path = Path.FIND_PATCH_FILE)
class FindPatchAct: BaseActivity() {
    private val fileList: MutableList<File> = mutableListOf()
    private var fileStack: Stack<File> = Stack()
    private val adapter = Adapter()
    private var patchFile: File? = null

    override fun getPresenters(): Array<IPresenter<*>>? {
        return null
    }

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
        requestPermissions(102)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplain {
                    Toast.makeText(this, "需要存储权限", Toast.LENGTH_SHORT).show()
                    it.askPermission()
                }.onGoAppSetting {
                    AlertDialog.Builder(this)
                            .setMessage("需要跳转设置界面申请权限")
                            .setNegativeButton("取消") { dialog, which -> it.cancel() }
                            .setPositiveButton("确定"){dialog, which -> it.goSetting()}
                            .show()
                }.onAllowed {
                    fileStack.push(FileUtil.getSdRootDir())
                    updateFileList()
                }.onRefused {
                    finish()
                }.request()

    }

    private fun updateFileList() {
        requestPermissions(103)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onAllowed {
                    fileList.clear()
                    fileList.addAll(fileStack.peek().listFiles())
                    fileList.sortWith(Comparator<File> {
                        f1, f2 ->
                        if (f1.isDirectory == f2.isDirectory) {
                            f1.name.compareTo(f2.name)
                        } else {
                            val c1 = if (f1.isDirectory) 1 else 0
                            val c2 = if (f1.isDirectory) 1 else 0
                            c1 - c2
                        }
                    })
                    adapter.notifyDataSetChanged()
                }.request()
    }

    override fun onBackPressed() {
        fileStack.pop()
        if (fileStack.empty()) {
            super.onBackPressed()
        } else {
            updateFileList()
        }
    }

    private inner class Adapter: RecyclerView.Adapter<Adapter.Holder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): Holder {
            return Holder(layoutInflater.inflate(R.layout.item_find_patch, parent, false))
        }

        override fun getItemCount(): Int {
            return fileList.size
        }

        override fun onBindViewHolder(holder: Holder?, position: Int) {
            val file = fileList[position]
            holder?.itemView?.tv_file_name?.text = file.name
            holder?.itemView?.tv_flag?.text = if (file.isDirectory) ">"
            else if (file.equals(patchFile)) "√"
            else ""
            holder?.itemView?.setOnClickListener{
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