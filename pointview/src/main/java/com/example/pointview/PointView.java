package com.example.pointview;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.FloatRange;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luozhanming on 2017/12/14.
 */

public class PointView extends View {

    private int mCurrentPointTextColor;
    private int mOtherTextColor;
    private int mScaleTextColor;
    private int mRulerColor;  //进度尺颜色
    private int mTopAndBottomTextColor;

    private float mRadius;   //半径
    private float mRulerWidth;  //刻度尺宽度
    private float mProgressWidth;
    private float mPointTextSize;
    private float mTopAndBottomTextSize;

    private List<Float> mScores;   //目标
    private Bitmap mScoreBitmap;  //目标达成显示图标

    private Paint mTextPaint;
    private Paint mProgressPaint;
    private Paint mRulerPaint;

    private Path mRulerPath;

    private int mMaxPoint; //最大分值
    private int mCurrentPoint = -1;   //当前分值

    private String title = "";
    private String bottomText = "";


    public PointView(Context context) {
        super(context);
    }

    public PointView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttrs(context, attrs);
        init();
    }

    private void init() {
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);

        mRulerPaint = new Paint();
        mRulerPaint.setColor(mRulerColor);
        mRulerPaint.setStrokeCap(Paint.Cap.ROUND);
        mRulerPaint.setAntiAlias(true);
        mRulerPaint.setStrokeWidth(mRulerWidth);

        mProgressPaint = new Paint();
        mProgressPaint.setStyle(Paint.Style.STROKE);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mRulerPaint.setAntiAlias(true);
        mProgressPaint.setStrokeWidth(mProgressWidth);

        mRulerPath = new Path();

        mScores = new ArrayList<>();
        mScores.add(1f);
    }

    private void parseAttrs(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PointView);
        mCurrentPointTextColor = typedArray.getColor(R.styleable.PointView_currentPointTextColor, Utils.getThemeColor(getContext(), Utils.COLOR_ACCENT));
        mScaleTextColor = typedArray.getColor(R.styleable.PointView_scaleTextColor, 0);
        mRulerColor = typedArray.getColor(R.styleable.PointView_scaleTextColor, Utils.getThemeColor(getContext(), Utils.COLOR_PRIMARY));
        mTopAndBottomTextColor = typedArray.getColor(R.styleable.PointView_topAndBottomTextColor, Utils.getThemeColor(getContext(), Utils.COLOR_PRIMARY_DARK));
        mRadius = typedArray.getDimension(R.styleable.PointView_radius, 200);
        mRulerWidth = typedArray.getDimension(R.styleable.PointView_rulerWidth, 20);
        mTopAndBottomTextSize = typedArray.getDimension(R.styleable.PointView_topAndBottomTextSize, 16);
        mPointTextSize = typedArray.getDimension(R.styleable.PointView_pointTextSize, 36);
        mCurrentPoint = typedArray.getInteger(R.styleable.PointView_currentPoint, -1);
        title = typedArray.getString(R.styleable.PointView_title);
        bottomText = typedArray.getString(R.styleable.PointView_bottomText);

        mProgressWidth = mRulerWidth + 20;
        mMaxPoint = typedArray.getInteger(R.styleable.PointView_maxPoint, 100);
        int scoreDrawableId = typedArray.getResourceId(R.styleable.PointView_scoreDrawable, R.mipmap.home_find_icon_cup);
        if (scoreDrawableId != 0) {
            mScoreBitmap = BitmapFactory.decodeResource(getResources(), scoreDrawableId);
        }
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (9 / 3f * mRadius);
        int height = (int) (7 / 3f * mRadius);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.translate(getMeasuredWidth() / 2, 4 / 3f * mRadius);
        drawRuler(canvas);
        drawProgress(canvas);
        drawScaleText(canvas);
        drawInnerText(canvas);
    }

    private void drawInnerText(Canvas canvas) {
        canvas.save();
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(Utils.sp2px(getContext(), mTopAndBottomTextSize));
        mTextPaint.setStrokeWidth(3);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setColor(mTopAndBottomTextColor);
        float[] pos = new float[2];
        float[] tan = new float[2];

        PathMeasure pathMeasure = new PathMeasure(mRulerPath, false);
        pathMeasure.getPosTan(0, pos, tan);
        if (!TextUtils.isEmpty(title)) {
            canvas.drawText(title, 0, -1f / 2 * mRadius, mTextPaint);
        }

        if (!TextUtils.isEmpty(bottomText)) {
            canvas.drawText(bottomText, 0, pos[1] + mTextPaint.getTextSize() / 2, mTextPaint);
        }
        mTextPaint.setColor(mCurrentPointTextColor);
        mTextPaint.setTextSize(Utils.sp2px(getContext(), mPointTextSize));
        mTextPaint.setStrokeWidth(20);
        if (mCurrentPoint < 0) {
            canvas.drawText("——", 0, mTextPaint.getTextSize() / 2, mTextPaint);
        } else {
            canvas.drawText(mCurrentPoint + "", 0, mTextPaint.getTextSize() / 2, mTextPaint);
        }
        canvas.restore();
    }

    private void drawScaleText(Canvas canvas) {
        canvas.save();
        float[] pos = new float[2];
        float[] tan = new float[2];
        PathMeasure pathMeasure = new PathMeasure(mRulerPath, false);
        if (mScaleTextColor == 0) {
            mTextPaint.setColor(mRulerColor);
            mTextPaint.setAlpha(128);
        } else {
            mTextPaint.setColor(mScaleTextColor);
        }
        mTextPaint.setTextSize(mRulerWidth + 20);
        mTextPaint.setStrokeWidth(5);
        for (float score : mScores) {
            pathMeasure.getPosTan(pathMeasure.getLength() * score, pos, tan);
            String text = String.valueOf((int) (score * mMaxPoint));
            if (score < 0.5) {
                mTextPaint.setTextAlign(Paint.Align.RIGHT);
                canvas.drawText(text, pos[0] - 60, pos[1] + mTextPaint.getTextSize() / 2, mTextPaint);
            } else if (score == 0.5) {
                mTextPaint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(text, pos[0], pos[1] - 60, mTextPaint);
            } else {
                mTextPaint.setTextAlign(Paint.Align.LEFT);
                canvas.drawText(text, pos[0] + 60, pos[1] + mTextPaint.getTextSize() / 2, mTextPaint);
            }
        }
        canvas.restore();
    }

    private void drawProgress(Canvas canvas) {
        float[] pos = new float[2];
        float[] tan = new float[2];
        canvas.save();
        RectF rect = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        Path progressPath = new Path();
        progressPath.addArc(rect, -225f, (float) mCurrentPoint / mMaxPoint * 270f);
        PathMeasure pathMeasure = new PathMeasure(progressPath, false);
        float x0, y0, x1, y1;
        pathMeasure.getPosTan(0, pos, tan);
        x0 = pos[0];
        y0 = pos[1];
        pathMeasure.getPosTan((float) mCurrentPoint / mMaxPoint * pathMeasure.getLength(), pos, tan);
        x1 = pos[0];
        y1 = pos[1];
        Shader shader = new LinearGradient(x0, y0, x1, y1, Color.GREEN, Color.YELLOW, Shader.TileMode.CLAMP);
        mProgressPaint.setShader(shader);
        canvas.drawPath(progressPath, mProgressPaint);

        for (float scores : mScores) {
            if (mCurrentPoint >= scores * mMaxPoint) {
                PathMeasure rulerMessure = new PathMeasure(mRulerPath, false);
                Paint bitmapPaint = new Paint();
                rulerMessure.getPosTan(rulerMessure.getLength() * scores, pos, tan);
                int width = mScoreBitmap.getWidth();
                int height = mScoreBitmap.getHeight();
                canvas.drawBitmap(mScoreBitmap, pos[0] - width / 2, pos[1] - height / 2, bitmapPaint);
            }
        }
        canvas.restore();
    }

    private void drawRuler(Canvas canvas) {
        canvas.save();
        RectF rect = new RectF(-mRadius, -mRadius, mRadius, mRadius);
        mRulerPaint.setStyle(Paint.Style.STROKE);
        mRulerPath.addArc(rect, -225f, 270f);
        canvas.drawPath(mRulerPath, mRulerPaint);

        PathMeasure pathMeasure = new PathMeasure(mRulerPath, false);
        float[] pos = new float[2];
        float[] tan = new float[2];
        float length = pathMeasure.getLength();
        pathMeasure.getPosTan(0, pos, tan);
        mRulerPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(pos[0], pos[1], mProgressWidth / 2, mRulerPaint);
        pathMeasure.getPosTan(length, pos, tan);
        canvas.drawCircle(pos[0], pos[1], mProgressWidth / 2, mRulerPaint);
        for (Float score : mScores) {
            pathMeasure.getPosTan(length * score, pos, tan);
            canvas.drawCircle(pos[0], pos[1], mProgressWidth / 2, mRulerPaint);
        }
        canvas.restore();
    }

    public int getCurrentPointTextColor() {
        return mCurrentPointTextColor;
    }

    public void setCurrentPointTextColor(int mCurrentPointTextColor) {
        this.mCurrentPointTextColor = mCurrentPointTextColor;
    }

    public int getOtherTextColor() {
        return mOtherTextColor;
    }

    public void setOtherTextColor(int mOtherTextColor) {
        this.mOtherTextColor = mOtherTextColor;
        invalidate();
    }

    public int getmScaleTextColor() {
        return mScaleTextColor;
    }

    public void setmScaleTextColor(int mScaleTextColor) {
        this.mScaleTextColor = mScaleTextColor;
        invalidate();
    }

    public int getRulerColor() {
        return mRulerColor;
    }

    public void setRulerColor(int mRulerColor) {
        this.mRulerColor = mRulerColor;
        invalidate();
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
        invalidate();
    }

    public List<Float> getScores() {
        return mScores;
    }

    public void setScores(@FloatRange(from = 0, to = 1.0) float... scores) {
        for (float score : scores) {
            mScores.add(score);
        }
        invalidate();
    }

    public Bitmap getScoreBitmap() {
        return mScoreBitmap;
    }

    public void setScoreBitmap(int resId) {
        this.mScoreBitmap = BitmapFactory.decodeResource(getResources(), resId);
        invalidate();
    }


    public int getMaxPoint() {
        return mMaxPoint;
    }

    public void setMaxPoint(int mMaxPoint) {
        this.mMaxPoint = mMaxPoint;
        invalidate();
    }

    public int getCurrentPoint() {
        return mCurrentPoint;
    }

    public void setCurrentPoint(int point) {
        if (point > mMaxPoint) {
            throw new IllegalArgumentException("Point set must less than max point.");
        }
        this.mCurrentPoint = point;
        invalidate();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        invalidate();
    }

    public String getBottomText() {
        return bottomText;
    }

    public void setBottomText(String bottomText) {
        this.bottomText = bottomText;
        invalidate();
    }

    public void setCurrentPointWithAnimation(final int point, int duration) {
        if (point > mMaxPoint) {
            throw new IllegalArgumentException("Point set must less than max point.");
        }
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "currentPoint", mCurrentPoint, point)
                .setDuration(duration);
        animator.start();

    }
}