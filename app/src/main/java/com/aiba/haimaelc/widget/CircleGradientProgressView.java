package com.aiba.haimaelc.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.aiba.haimaelc.R;

/**
 * Created by zhu on 16/3/10.
 * 自定义带有渐变的进度条
 */
public class CircleGradientProgressView extends View {

    private float mPercent;
    private float mStrokeWidth;
    private float mBaseWidth;
    private int mBgColor = 0xDEDEDE;
    private float mStartAngle = 90;
    private int mStartColor = 0x43F1FF;
    private int mEndColor = 0x5AF929;
    private int mLowStartColor = 0xFFA5A8;
    private int mLowEndColor = 0xFF0A11;

    private LinearGradient mShader;
    private Context mContext;
    private RectF mBaseR;
    private RectF mOval;
    private Paint mBasePaint;
    private Paint mPaint;
    public CircleGradientProgressView(Context context) {
        super(context);
        this.mContext = context;
    }

    public CircleGradientProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleGradientProgressView);
        mPercent = array.getFloat(R.styleable.CircleGradientProgressView_percent,0);
        mBaseWidth = array.getDimensionPixelSize(R.styleable.CircleGradientProgressView_baseStrokeWidth, dp2Px(4));
        mStrokeWidth = array.getDimensionPixelSize(R.styleable.CircleGradientProgressView_strokeWidth, dp2Px(10));
        mStartAngle = array.getFloat(R.styleable.CircleGradientProgressView_startAngle, 90);
        mBgColor = array.getColor(R.styleable.CircleGradientProgressView_bgColor, 0xDEDEDE);
        mStartColor = array.getColor(R.styleable.CircleGradientProgressView_startColor, 0x43F1FF);
        mEndColor = array.getColor(R.styleable.CircleGradientProgressView_endColor,0x5AF929);
        mLowEndColor = array.getColor(R.styleable.CircleGradientProgressView_lowEndColor, 0xFF0A11);
        mLowStartColor = array.getColor(R.styleable.CircleGradientProgressView_lowStartColor,0xFFA5A8);

        array.recycle();

        initPaint();
    }

    private void initPaint(){
        mBasePaint = new Paint();
        mBasePaint.setAntiAlias(true);
        mBasePaint.setStyle(Paint.Style.STROKE);
        mBasePaint.setStrokeCap(Paint.Cap.ROUND);
        mBasePaint.setStrokeWidth(mBaseWidth);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    private void initBaseRectF(){
        int xp = getPaddingLeft() + getPaddingRight();
        int yp = getPaddingBottom() + getPaddingTop();
        mBaseR = new RectF(getPaddingLeft() + mBaseWidth,getPaddingTop() + mBaseWidth,
                    getPaddingLeft() + (getWidth() - xp) - mBaseWidth,
                    getPaddingTop() + (getHeight() - yp) - mBaseWidth);
    }

    private int dp2Px(float dp){
        return (int) (mContext.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBasePaint.setShader(null);
        mBasePaint.setColor(mBgColor);
        mBasePaint.setStrokeWidth(mBaseWidth);
        canvas.drawArc(mBaseR, 0, 360, false, mBasePaint);

        mPaint.setColor(mBgColor);
        mPaint.setShader(mShader);
        mPaint.setStrokeWidth(mStrokeWidth);
        canvas.drawArc(mOval,mStartAngle,mPercent * 3.6f, false, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        initBaseRectF();
        updateOval();
        updateShader();
    }

    private void updateOval(){
        int xp = getPaddingLeft() + getPaddingRight();
        int yp = getPaddingBottom() + getPaddingTop();
        mOval = new RectF(getPaddingLeft()+ mStrokeWidth/2  , getPaddingTop() + mStrokeWidth/2,
                    getPaddingLeft() + (getWidth() - xp) - mStrokeWidth/2,
                    getPaddingTop() + (getHeight() - yp) - mStrokeWidth/2
        );
    }


    private void updateShader(){
        if(mOval != null)
        mShader = new LinearGradient(mOval.left,mOval.top,mOval.left,
                mOval.bottom, new int[]{mStartColor,mEndColor},null, Shader.TileMode.MIRROR);
    }
    public void setPercent(float percent){
        mPercent = percent;
        if(mPercent <= 20){
            changeStartAndEndColor();
        }else{
            updateShader();
        }
        refreshTheLayout();
    }

    public float getPercent(){
        return mPercent;
    }

    public void setStrokeWidth(float strokeWidth ){
        this.mStrokeWidth = dp2Px(strokeWidth);
        mPaint.setStrokeWidth(mStrokeWidth);
        updateOval();
        refreshTheLayout();

    }

    public float getStrokeWidth(){
        return mStrokeWidth;
    }

    public void setStartColor(int startColor){
        this.mStartColor = startColor;
        updateShader();
        refreshTheLayout();
    }

    public int getStartColor(){
        return mStartColor;
    }

    public void setEndColor(int endColor){
        this.mEndColor = endColor;
        updateShader();
        refreshTheLayout();
    }

    public int getEndColor(){
        return mEndColor;
    }

    public void changeStartAndEndColor(){
        if(mOval != null)
        mShader = new LinearGradient(mOval.left,mOval.top,mOval.left,
                mOval.bottom, new int[]{mLowEndColor,mLowStartColor},new float[]{0.2f,1.0f}, Shader.TileMode.MIRROR);
    }
    public void refreshTheLayout(){
        invalidate();
        requestLayout();
    }
}
