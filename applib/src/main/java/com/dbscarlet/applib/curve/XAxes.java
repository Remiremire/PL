package com.dbscarlet.applib.curve;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.util.List;

/**
 * Created by Daibing Wang on 2018/7/26.
 */
public class XAxes {
    private final DisplayMetrics displayMetrics;
    private List<String> labels;
    private Paint paint;
    private Path path = new Path();
    private int lineColor = 0xffedeff3;
    private int textColor = 0xff82879b;
    private boolean drawLine = true;
    private float paddingToCanve;
    private float[][] textDrawLocation;
    private float xAxesStep;

    public XAxes(Context context) {
        displayMetrics = context.getResources().getDisplayMetrics();
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, displayMetrics));
        paddingToCanve = dip2Px(7);
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    float getHeight() {
        Paint.FontMetrics fm = paint.getFontMetrics();
        return paddingToCanve + fm.bottom - fm.top;
    }

    void compute(float xAxesLLimit, float xAxesRLimit, RectF curveRectF) {
        if (labels == null || labels.size() == 0) return;
        int size = labels.size();
        xAxesStep = size == 1 ? (curveRectF.width()) : (curveRectF.width()) / (size - 1);
        path.reset();
        textDrawLocation = new float[size][2];
        Paint.FontMetrics fm = paint.getFontMetrics();
        for (int i = 0; i < size; i++) {
            float x = curveRectF.left + xAxesStep * i;
            if (drawLine) {
                path.moveTo(x, curveRectF.top);
                path.lineTo(x, curveRectF.bottom);
            }
            float textWidth = paint.measureText(labels.get(i));
            if (x - textWidth / 2 < xAxesLLimit) {
                textDrawLocation[i][0] = xAxesLLimit;
            } else if (x + textWidth / 2 > xAxesRLimit) {
                textDrawLocation[i][0] = xAxesRLimit - textWidth;
            } else {
                textDrawLocation[i][0] = x - textWidth / 2;
            }
            textDrawLocation[i][1] = curveRectF.bottom + paddingToCanve - fm.top;
        }
    }

    float getXAexStep() {
        return xAxesStep;
    }

    void draw(Canvas canvas) {
        if (labels == null || labels.size() == 0) return;
        float textStrokeWidth = paint.getStrokeWidth();
        paint.setStrokeWidth(dip2Px(1));
        paint.setColor(lineColor);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawPath(path, paint);
        paint.setStrokeWidth(textStrokeWidth);
        paint.setColor(textColor);
        paint.setStyle(Paint.Style.FILL);
        int size = labels.size();
        try {
            for (int i = 0; i < size; i++) {
                canvas.drawText(labels.get(i), textDrawLocation[i][0], textDrawLocation[i][1], paint);
            }
        } catch (Exception e) {

        }

    }

    private float dip2Px(float dip) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, displayMetrics);
    }
}
