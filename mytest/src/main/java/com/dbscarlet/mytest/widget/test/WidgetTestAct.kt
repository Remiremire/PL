package com.dbscarlet.mytest.widget.test

import android.graphics.Color
import android.support.design.widget.TabLayout
import android.support.v4.view.PagerAdapter
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.alibaba.android.arouter.facade.annotation.Route
import com.dbscarlet.applib.ActPath
import com.dbscarlet.applib.base.BaseActivity
import com.dbscarlet.mytest.R
import com.dbscarlet.mytest.databinding.ActWidgetTestBinding
import com.dbscarlet.mytest.widget.CustomTabLayout

/**
 * Created by Daibing Wang on 2018/8/27.
 */
@Route(path = ActPath.Test.WIDGET_TEST)
class WidgetTestAct: BaseActivity<ActWidgetTestBinding>() {
    override fun getContentLayout(): Int {
        return R.layout.act_widget_test
    }

    override fun initView() {
        binding.pager.adapter = PageAdapter()
        binding.tabLayout.bindWithViewPager(binding.pager)
        binding.btnMode.setOnClickListener {
            if (binding.tabLayout.mode == CustomTabLayout.Mode.SCROLLABLE) {
                binding.tabLayout.mode = CustomTabLayout.Mode.FIXED
            } else {
                binding.tabLayout.mode = CustomTabLayout.Mode.SCROLLABLE
            }
        }
        binding.tabSys.tabMode = TabLayout.MODE_SCROLLABLE
        binding.tabSys.setupWithViewPager(binding.pager)
        binding.btnIndicator.setOnClickListener {
            when {
                binding.tabLayout.indicatorWidth == CustomTabLayout.INDICATOR_WRAP_TEXT -> {
                    binding.tabLayout.indicatorWidth = CustomTabLayout.INDICATOR_MATCH_TAB
                }
                binding.tabLayout.indicatorWidth == CustomTabLayout.INDICATOR_MATCH_TAB -> {
                    binding.tabLayout.indicatorWidth = binding.tabLayout.dip2Px(30f)
                }
                else -> {
                    binding.tabLayout.indicatorWidth = CustomTabLayout.INDICATOR_WRAP_TEXT
                }
            }
        }
    }

    inner class PageAdapter: PagerAdapter() {
        override fun isViewFromObject(view: View?, `object`: Any?): Boolean {
            return view === `object`
        }

        override fun getCount(): Int {
            return 10
        }

        override fun instantiateItem(container: ViewGroup?, position: Int): Any {
            val tv = TextView(this@WidgetTestAct)
            tv.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT)
            tv.gravity = Gravity.CENTER
            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 28f)
            tv.text = "content${position + 1}"
            tv.setTextColor(Color.BLACK)
            container?.addView(tv)
            return tv
        }

        override fun destroyItem(container: ViewGroup?, position: Int, `object`: Any?) {
            container?.removeView(`object` as View)
        }

        override fun getPageTitle(position: Int): CharSequence {
            return "PAGE\n${position + 1}"

        }
    }
}