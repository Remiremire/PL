package com.dbscarlet.mytest.core.curve;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

/**
 * Created by Daibing Wang on 2018/7/20.
 */
public class CurveLine {
    private int lineColor;
    private int gradientTopColor;
    private int gradientBottomColor;
    private List<Float> valueList;

    private Path curveLinePath;
    private boolean readyToDraw;


    public void compute(float top, float left, float bottom, float right,
                        float minValue, float maxValue,float xAxesStep,
                        int pointTextSize, float LINE_SMOOTHNESS) {


    }

    private void computeLine(float top, float left, float bottom, float right,
                             float minValue, float maxValue,float xAxesStep, float LINE_SMOOTHNESS) {
        curveLinePath.reset();
        final int valueSize = valueList.size();
        float[] prePreviousPoint = null;
        float[] previousPoint = null;
        float[] currentPoint = null;
        float[] nextPoint = null;
        int minY = Integer.MAX_VALUE;
        int firstX = 0;
        int lastX = 0;
        for (int valueIndex = 0; valueIndex < valueSize; ++valueIndex) {
            if (currentPoint == null) {
                currentPoint = new float[2];
                setPointLocation(currentPoint, valueIndex, maxValue, minValue,
                        top, left, bottom, xAxesStep);
            }
            if (previousPoint == null) {
                previousPoint = new float[2];
                copyPoint(currentPoint, previousPoint);
            }
            if (prePreviousPoint == null) {
                prePreviousPoint = new float[2];
                copyPoint(previousPoint, prePreviousPoint);
            }
            if (nextPoint == null) {
                nextPoint = new float[2];
                if (valueIndex < valueList.size() - 1) {
                    setPointLocation(nextPoint, valueIndex + 1, maxValue, minValue,
                            top, left, bottom, xAxesStep);
                } else {
                    copyPoint(currentPoint, nextPoint);
                }
            }
            if (valueIndex == 0) {
                // Move to start point.
                path.moveTo(currentPointX, currentPointY);
            } else {
                // Calculate control points.
                final float firstDiffX = (currentPointX - prePreviousPointX);
                final float firstDiffY = (currentPointY - prePreviousPointY);
                final float secondDiffX = (nextPointX - previousPointX);
                final float secondDiffY = (nextPointY - previousPointY);
                float firstControlPointX = previousPointX + (LINE_SMOOTHNESS * firstDiffX);
                float firstControlPointY = previousPointY + (LINE_SMOOTHNESS * firstDiffY);
                float secondControlPointX = currentPointX - (LINE_SMOOTHNESS * secondDiffX);
                float secondControlPointY = currentPointY - (LINE_SMOOTHNESS * secondDiffY);

                /////////////////连续三个值相同认为是直线，重新修正控制点//////////////
                if (currentPointY == previousPointY && currentPointY == prePreviousPointY && valueIndex > 2) {
                    secondControlPointY = currentPointY;
                }
                if (valueIndex > 3 && prePreviousPointY == previousPointY) {
                    float prePrePreviousPointY = mChartFrame.computeY(valueList.get(valueIndex - 3).value);
                    if (prePreviousPointY == prePrePreviousPointY) {
                        firstControlPointX = previousPointX + (currentPointX - previousPointX) * (1 - LINE_SMOOTHNESS) * 0.8f;
                        firstControlPointY = previousPointY;
                    }
                }
                if (currentPointY == previousPointY && valueIndex > 0
                        && currentPointY == nextPointY && valueIndex + 1 < valueSize) {
                    firstControlPointY = currentPointY;
                }
                if (valueIndex + 2 < valueSize && currentPointY == nextPointY) {
                    float nextNextPointY = mChartFrame.computeY(valueList.get(valueIndex + 2).value);
                    if (nextNextPointY == nextPointY) {
                        secondControlPointX = currentPointX - (currentPointX - previousPointX) * (1 - LINE_SMOOTHNESS) * 0.8f;
                        secondControlPointY = currentPointY;
                    }
                }

                path.cubicTo(firstControlPointX, firstControlPointY, secondControlPointX, secondControlPointY,
                        currentPointX, currentPointY);
            }

            if (valueSize == 1) {
                path.rMoveTo(-dip2Px(5), 0);
                path.rLineTo(dip2Px(10), 0);
            }

            prePreviousPointX = previousPointX;
            prePreviousPointY = previousPointY;
            previousPointX = currentPointX;
            previousPointY = currentPointY;
            currentPointX = nextPointX;
            currentPointY = nextPointY;
        }
    }

    private void setPointLocation(float[] pointHolder, int valueIndex, float maxValue, float minValue,
                                  float top, float left, float bottom, float xAxesStep) {
        Float value = valueList.get(valueIndex);
        pointHolder[0] = left + xAxesStep * valueIndex;
        pointHolder[1] = bottom + (top - bottom) * (value / (maxValue - minValue));
    }

    private void copyPoint(float[] original, float[] target) {
        System.arraycopy(original, 0, target, 0, 2);
    }

    public void drawCurveLine(Canvas canvas, Paint paint) {

    }
}
