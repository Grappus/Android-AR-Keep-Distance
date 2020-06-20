package android.tesseract.jio.covid19.ar.journal;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dipanshu Harbola on 20/6/20.
 */
public class LineGraph extends View {

    // default height width in case of wrap_content
    private int mWidth = 700;
    private int mHeight = 600;

    private float maxValue, minValue;
    private List<GraphData> mData = new ArrayList<>(5);

    private Paint mLinePaint;
    private Paint mTextPaint;
    private Paint mPointPaint;

    private int mOriginX = 50, mOriginY = 0;
    private int xDistance, yDistance;
    private int yValue;

    private Point[] points;

    public LineGraph(Context context) {
        this(context, null);
    }

    public LineGraph(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    /**
     * init paint for canvas drawing
     */
    private void initPaint() {

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(3);
        mLinePaint.setColor(Color.parseColor("#333333"));

        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(Color.parseColor("#333333"));
        mTextPaint.setTextSize(36);

        mPointPaint = new Paint();
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setStrokeWidth(5);
        mPointPaint.setColor(Color.RED);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, mHeight);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(mWidth, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, mHeight);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        try {
            calculateVaule();

            drawCoordinateSystem(canvas);
            drawText(canvas);
            drawDataPoint(canvas);
            drawDataLine(canvas);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    private void calculateVaule() {

        Log.e("minValue", minValue + "");
        Log.e("mHeight", mHeight + "");
        Log.e("mData", mData + "");
        yValue = 10;
        yDistance = mHeight / (mData.size() + 2);

        if (minValue < 0) {
            mOriginY = (int) (mHeight - 50 + (minValue / yValue * yDistance));
        } else {
            mOriginY = mHeight - 50;
        }

        xDistance = (mWidth - mOriginX) / (mData.size() + 1);

        Log.e("yDistance", yDistance + "");
        Log.e("xDistance", xDistance + "");
        Log.e("yValue", yValue + "");
        Log.e("mOriginY", mOriginY + "");
    }

    private void drawCoordinateSystem(Canvas canvas) {

        Path path = new Path();

        path.moveTo(0, mOriginY);
        path.lineTo(mWidth, mOriginY);

        path.moveTo(mOriginX, mHeight);
        path.lineTo(mOriginX, 0);

        //canvas.drawPath(path, mLinePaint);

        path.reset();

        for (int i = 1; i <= mData.size(); i++) {
            points[i - 1].x = mOriginX + xDistance * i;
            path.moveTo(mOriginX + xDistance * i, mOriginY);
            path.lineTo(mOriginX + xDistance * i, mOriginY - 10);
        }
        canvas.drawPath(path, mLinePaint);


        path.reset();

        for (int i = 0; i <= 4; i++) {

            path.moveTo(mWidth, mOriginY - yDistance * i);
            path.lineTo(mOriginX + 25, mOriginY - yDistance * i);
        }

        mLinePaint.setPathEffect(new DashPathEffect(new float[]{6, 18}, 0));
        canvas.drawPath(path, mLinePaint);

    }

    private void drawText(Canvas canvas) {

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float fontHeight = fontMetrics.descent - fontMetrics.ascent;
        mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);

        for (int i = 0; i < mData.size(); i++) {

            float x = points[i].x - mTextPaint.measureText(mData.get(i).getTitle()) / 2;
            float y = mOriginY + fontHeight;
            canvas.drawText(mData.get(i).getTitle(), x, y, mTextPaint);
        }

        int degree = mOriginY / yDistance;
        for (int i = 0; i < degree; i++) {

            float m = mOriginX - mTextPaint.measureText(maxValue + "");
            float n = mOriginY - yDistance * (i);
            canvas.drawText(yValue * (i) + "", m, n, mTextPaint);
        }
    }

    private void drawDataPoint(Canvas canvas) {

        mPointPaint.setStyle(Paint.Style.FILL);

        for (int i = 0; i < points.length; i++) {

            points[i].y = mOriginY - (mData.get(i).getValue() / yValue) * yDistance;
            canvas.drawCircle(points[i].x, points[i].y, 5, mPointPaint);
        }
    }

    private void drawDataLine(Canvas canvas) {

        mPointPaint.setStyle(Paint.Style.STROKE);

        Path path = new Path();
        path.moveTo(points[0].x, points[0].y);

        for (int i = 1; i < points.length; i++) {
            path.lineTo(points[i].x, points[i].y);
        }

        mPointPaint.setAlpha(187);
        canvas.drawPath(path, mPointPaint);
    }

    public void setGraphData(List<GraphData> mData) {
        Log.e("List<GraphData> mData", mData + "");
        this.mData = mData;
        this.points = new Point[mData.size()];

        for (int i = 0; i < mData.size(); i++) {

            if (mData.get(i).getValue() > maxValue) {
                maxValue = mData.get(i).getValue();
            }

            if (mData.get(i).getValue() < minValue) {
                minValue = mData.get(i).getValue();
            }

            points[i] = new Point();
        }

        postInvalidate();
    }


    public static class GraphData {

        private String title;
        private float value;

        public GraphData(String title, float value) {

            this.title = title;
            this.value = value;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public float getValue() {
            return value;
        }

        public void setValue(float value) {
            this.value = value;
        }
    }

    private class Point {

        public float x;
        public float y;
    }
}
