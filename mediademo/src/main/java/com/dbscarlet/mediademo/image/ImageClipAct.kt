package com.dbscarlet.mediademo.image

import android.databinding.ViewDataBinding
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.ActPath
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.mediademo.R

/**
 * Created by Daibing Wang on 2018/11/30.
 */
@Route(path = ActPath.Media.IMAGE_CLIP)
class ImageClipAct: BaseActivity<ViewDataBinding>() {
    override fun getContentLayout(): Int {
        return R.layout.act_image_clip
    }


}