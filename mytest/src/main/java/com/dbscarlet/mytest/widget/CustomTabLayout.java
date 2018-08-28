package com.dbscarlet.mytest.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Daibing Wang on 2018/8/6.
 */
public class CustomTabLayout extends HorizontalScrollView {
    public static int INDICATOR_WRAP_TEXT = -1;
    public static int INDECATOR_MATCH_TAB = -2;

    private Mode mode;
    private int indicatorHeight;
    private int indicatorWidth;
    private int indicatorColor = 0xff079dfe;
    private int defaultTextSize = 16;
    private int selectTextSize = 18;
    private int defaultTextColor = 0xff101d34;
    private int selectTextColor = 0xff101d34;
    private Typeface defaultTypeFace = Typeface.defaultFromStyle(Typeface.NORMAL);
    private Typeface selectTypeFace = Typeface.defaultFromStyle(Typeface.BOLD);

    private TabContainerLayout mTabContainer;


    public CustomTabLayout(Context context) {
        super(context);
        initView();
    }

    public CustomTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CustomTabLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        setHorizontalScrollBarEnabled(false);
        mTabContainer = new TabContainerLayout(getContext());
        addView(mTabContainer);
        setMode(Mode.SCROLLABLE);
        setIndicatorHeight(dip2Px(4));
        setIndicatorWidth(dip2Px(30));
    }

    public void setMode(Mode mode) {
        this.mode = mode;
        if (mode == Mode.SCROLLABLE) {
            mTabContainer.setLayoutParams(new HorizontalScrollView.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    mTabContainer.setLayoutParams(new HorizontalScrollView.LayoutParams(
                            getWidth() - getPaddingLeft() - getPaddingRight(),
                            ViewGroup.LayoutParams.WRAP_CONTENT));
                }
            });
        }
        mTabContainer.layoutTabs();
    }

    /**
     * 设置下划线宽度
     * @param indicatorWidth 适应文字宽度{@link #INDICATOR_WRAP_TEXT},
     *                       或者填满tab的宽度{@link #INDECATOR_MATCH_TAB},
     *                       或者是一个单位为pixel的固定宽度
     */
    public void setIndicatorWidth(int indicatorWidth) {
        this.indicatorWidth = indicatorWidth;
        mTabContainer.invalidate();
    }

    public void setIndicatorHeight(int indicatorHeight) {
        this.indicatorHeight = indicatorHeight;
        mTabContainer.setPadding(0, 0, 0, indicatorHeight);
    }

    public Mode getMode() {
        return mode;
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public int getIndicatorWidth() {
        return indicatorWidth;
    }

    public void bindWithViewPager(ViewPager viewPager) {
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                int len = mTabContainer.getChildCount();
                float left = 0;
                float curWidth = 0;
                float nextWidth = 0;
                for (int i = 0; i < len; i++) {
                    int width = mTabContainer.getChildAt(i).getWidth();
                    if (i < position) {
                        left += width;
                    } else if (i == position){
                        curWidth = width;
                    } else {
                        nextWidth = width;
                        break;
                    }
                }
                int layoutWidth = getWidth() - getPaddingLeft() - getPaddingRight();
                int scrollX = getScrollX();
                Log.i("customTabs", "onScrolled: left=" + left
                        + "\tcurWidth=" + curWidth
                        + "\tnextWidth=" + nextWidth
                        + "\tlayoutWidth=" + layoutWidth
                        + "\tscrollX=" + scrollX);
                if (scrollX + layoutWidth < left + curWidth + nextWidth * positionOffset) {
                    scrollBy((int) (left + curWidth + nextWidth * positionOffset - scrollX - layoutWidth), 0);
                } else if (scrollX > left + curWidth * positionOffset) {
                    scrollBy((int) (left + curWidth * positionOffset - scrollX), 0);
                }
            }
        });
        mTabContainer.bindViewPager(viewPager);
    }

    private void setTabTextStyle(TextView tab, boolean isSelected) {
        tab.setGravity(Gravity.CENTER);
        if (isSelected) {
            tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectTextSize);
            tab.setTextColor(selectTextColor);
            tab.setTypeface(selectTypeFace);
        } else {
            tab.setTextSize(TypedValue.COMPLEX_UNIT_SP, defaultTextSize);
            tab.setTextColor(defaultTextColor);
            tab.setTypeface(defaultTypeFace);
        }
        int dp12 = dip2Px(12);
        int dp20 = dip2Px(20);
        tab.setPadding(dp20, dp12, dp20, dp12);
    }

    public int dip2Px(float px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, getResources().getDisplayMetrics());
    }

    public enum Mode {
        FIXED, SCROLLABLE
    }

    private class TabContainerLayout extends LinearLayout {
        private Paint paint;
        private TextView mSelectedTab;
        private int indicatorLocation;
        private float offset;

        public TabContainerLayout(Context context) {
            super(context);
            setGravity(Gravity.CENTER_VERTICAL);
            setOrientation(HORIZONTAL);
            setWillNotDraw(false);
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeCap(Paint.Cap.ROUND);
        }

        private void bindViewPager(final ViewPager viewPager) {
            removeAllViews();
            PagerAdapter pagerAdapter = viewPager.getAdapter();
            int len = pagerAdapter.getCount();
            int currentItem = viewPager.getCurrentItem();
            indicatorLocation = currentItem;
            for (int i = 0; i < len; i++) {
                final int index = i;
                TextView tab = new TextView(getContext());
                tab.setText(pagerAdapter.getPageTitle(index));
                tab.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index, true);
                    }
                });
                if (index == currentItem) {
                    setTabTextStyle(tab, true);
                    mSelectedTab = tab;
                } else {
                    setTabTextStyle(tab, false);
                }

                addView(tab);
            }

            viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    if (positionOffset >= 1 || positionOffset <= 0) return;
                    offset = positionOffset;
                    indicatorLocation = position;
                    invalidate();
                }

                @Override
                public void onPageSelected(int position) {
                    if (mSelectedTab != null) {
                        setTabTextStyle(mSelectedTab, false);
                    }
                    TextView tab = (TextView) mTabContainer.getChildAt(position);
                    setTabTextStyle(tab, true);
                    mSelectedTab = tab;
                }
            });

            layoutTabs();
        }

        private void layoutTabs() {
            int len = getChildCount();
            if (len < 1) return;
            LinearLayout.LayoutParams lp;
            if (mode == Mode.SCROLLABLE) {
                lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
            } else {
                lp = new LinearLayout.LayoutParams(0,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        1);
            }

            for (int i = 0; i < len; i++) {
                getChildAt(i).setLayoutParams(lp);
            }
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            drawIndicator(canvas);
        }

        private void drawIndicator(Canvas canvas) {
            int childCount = getChildCount();
            if (childCount == 0 ) {
                return;
            }
            float currentIndicatorW = currentIndicatorWidth();
            float indicatorCenter = currentIndicatorCenter();
            float start = indicatorCenter - currentIndicatorW / 2;
            paint.setColor(indicatorColor);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(indicatorHeight);
            int indicatorY = getHeight() - indicatorHeight / 2 - getPaddingBottom();
            canvas.drawLine(start, indicatorY, start + currentIndicatorW, indicatorY, paint);
        }

        private float currentIndicatorWidth() {
            float result;
            if (indicatorWidth == INDICATOR_WRAP_TEXT) {
                TextView tvL = (TextView) getChildAt(indicatorLocation);
                float lw = tvL.getPaint().measureText(tvL.getText().toString());
                if (indicatorLocation < getChildCount() - 1) {
                    TextView tvR = (TextView) getChildAt(indicatorLocation + 1);
                    float rw = tvR.getPaint().measureText(tvR.getText().toString());
                    if (offset <= 0.5) {
                        result = lw + rw * offset / 0.5f;
                    } else {
                        result = rw + lw * (1 - offset) / 0.5f;
                    }
                } else {
                    result = lw;
                }
            } else if (indicatorWidth == INDECATOR_MATCH_TAB) {
                int lw = getChildAt(indicatorLocation).getWidth();
                if (indicatorLocation < getChildCount() - 1) {
                    int rw = getChildAt(indicatorLocation + 1).getWidth();
                    result = lw + (rw - lw) * offset;
                } else {
                    result = lw;
                }
            } else {
                result = indicatorWidth * (2 - 4 * (offset - 0.5f) * (offset - 0.5f));
            }
            return result;
        }

        private float currentIndicatorCenter() {
            int len = getChildCount();
            float result = 0;
            for (int i = 0; i < len; i++) {
                if (i < indicatorLocation) {
                    result += getChildAt(i).getWidth();
                } else if (i == indicatorLocation) {
                    result += getChildAt(i).getWidth() / 2;
                } else {
                    result += (getChildAt(i - 1).getWidth() + getChildAt(i).getWidth()) / 2 * offset;
                    break;
                }
            }
            return result;
        }
    }
}
