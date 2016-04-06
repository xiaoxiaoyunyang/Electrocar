package com.aiba.haimaelc.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.tools.LogUtils;

/**
 * Created by zhu on 16/3/10.
 */
public class HoloCircleRoundProgressView extends View {

    private float mPercent = 0;
    private float mCircleStrokeWidth = 10;
    private int mStartAngle = 90;
    private int startColor = 0x43F1FF;
    private int endColor = 0x5AF929;
    private int errorStartColor = 0xFFA5A8;
    private int errorEndColor = 0xFF0A11;

    private Context mContext;
    private LinearGradient mShader;
    private LinearGradient mErrorShader;
    private Paint mThumbPaint = new Paint();
    private Paint mCirclePaint = new Paint();
    private Paint mErrorPaint = new Paint();
    private RectF mCircleRectF;


    private float mCircleX;
    private float mCircleY;
    private float mThumbPosX;
    private float mThumbPosY;
    private float mThumbRadius;
    private float mRadius;

    private boolean mIsNormal = true;
    private Status status = Status.START;

    public HoloCircleRoundProgressView(Context context) {
        super(context);
        this.mContext = context;
    }

    public HoloCircleRoundProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.HoloCircleRoundProgressView);
        try {
            mCircleStrokeWidth = array.getDimension(R.styleable.HoloCircleRoundProgressView_holoStrokeWidth, 10);
            startColor = array.getColor(R.styleable.HoloCircleRoundProgressView_holoStartColor, 0x43F1FF);
            endColor = array.getColor(R.styleable.HoloCircleRoundProgressView_holoEndColor, 0x5AF929);
            mPercent = array.getFloat(R.styleable.HoloCircleRoundProgressView_holoPercent, 0);
            errorEndColor = array.getColor(R.styleable.HoloCircleRoundProgressView_holoLowEndColor, 0xFF0A11);
            errorStartColor = array.getColor(R.styleable.HoloCircleRoundProgressView_holoLowStartColor, 0xFFA5A8);
            mStartAngle = array.getInt(R.styleable.HoloCircleRoundProgressView_holoStartAngle,90);
        } finally {
            array.recycle();
        }
        mThumbRadius = mCircleStrokeWidth * 1.5f;
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(status == Status.END) {
            if (!mIsNormal) {
                mErrorPaint.setShader(mErrorShader);
                canvas.drawArc(mCircleRectF, mStartAngle, 100 * 3.6f, false, mErrorPaint);
            } else {
                mCirclePaint.setShader(mShader);
                canvas.drawArc(mCircleRectF, mStartAngle, 100 * 3.6f, false, mCirclePaint);
            }
        }else if(status == Status.START || status == Status.RUNING){
            mCirclePaint.setShader(mShader);
            canvas.drawArc(mCircleRectF, mStartAngle, 100 * 3.6f, false, mCirclePaint);
        }
        canvas.translate(mCircleX, mCircleY);
        canvas.save();
        canvas.rotate(mPercent * 3.6f + 90);
        // rotate the square by 45 degrees
        canvas.rotate(45, mThumbPosX, mThumbPosY);
        canvas.drawCircle(mThumbPosX, mThumbPosY, mThumbRadius, mThumbPaint);

        canvas.restore();


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        updateRectF();
        getCenterXY();
        mShader = new LinearGradient(mCircleRectF.left, mCircleRectF.bottom, mCircleRectF.right, mCircleRectF.bottom,
                new int[]{startColor, endColor
                }, null
                , Shader.TileMode.MIRROR);
        mErrorShader = new LinearGradient(mCircleRectF.left, mCircleRectF.bottom, mCircleRectF.right, mCircleRectF.bottom,
                new int[]{errorStartColor, errorEndColor
                }, null
                , Shader.TileMode.MIRROR);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int width = getDefaultSize(getSuggestedMinimumWidth() + getPaddingRight() + getPaddingLeft(), widthMeasureSpec);
        final int height = getDefaultSize(getSuggestedMinimumHeight() + getPaddingTop() + getPaddingBottom(), heightMeasureSpec);
        final int diameter = Math.min(width, height);
        final float halfWidth = diameter * 0.5f;
        mRadius = halfWidth - mCircleStrokeWidth  - Math.min(getPaddingRight() + getPaddingLeft(), getPaddingTop() + getPaddingBottom()) / 2f;
        mThumbPosX = (float) (mRadius * Math.cos(0));
        mThumbPosY = (float) (mRadius * Math.sin(0));
//        mThumbPosX = mCircleX - mCircleStrokeWidth / 2f - 0.5f  ;
//        mThumbPosY = mCircleY-  halfWidth;

    }

    private void init() {
        mThumbPaint.setShader(null);
        mThumbPaint.setColor(Color.TRANSPARENT);
        mThumbPaint.setAntiAlias(true);//是否抗锯齿
        mThumbPaint.setStyle(Paint.Style.FILL);
        mThumbPaint.setStrokeWidth(mCircleStrokeWidth);

        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeCap(Paint.Cap.ROUND);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);

        mErrorPaint.setAntiAlias(true);
        mErrorPaint.setStyle(Paint.Style.STROKE);
        mErrorPaint.setStrokeCap(Paint.Cap.ROUND);
        mErrorPaint.setStrokeWidth(mCircleStrokeWidth);
    }

    private void updateRectF() {
        int xp = getPaddingLeft() + getPaddingRight();
        int yp = getPaddingBottom() + getPaddingTop();
        mCircleRectF = new RectF(getPaddingLeft() + mCircleStrokeWidth, getPaddingTop() + mCircleStrokeWidth,
                getPaddingLeft() + (getWidth() - xp) - mCircleStrokeWidth,
                getPaddingTop() + (getHeight() - yp) - mCircleStrokeWidth);
    }

    private void getCenterXY() {
        mCircleX = Math.abs(mCircleRectF.left + (mCircleRectF.right - mCircleRectF.left) / 2);
        mCircleY = Math.abs(mCircleRectF.top + (mCircleRectF.bottom - mCircleRectF.top) / 2);
    }

    public float getPercent() {
        return mPercent;
    }

    public void setPercent(float percent) {
        mPercent = percent;
        mIsNormal = true;
        if (mPercent > 40) {
            mThumbPaint.setColor(endColor);
        } else {
            mThumbPaint.setColor(startColor);
        }
//        if (Float.compare(percent, 0.0f) == 0 || percent >= 100.0f) {
        if (Float.compare(percent, 0.0f) == 0) {
            mThumbPaint.setColor(Color.TRANSPARENT);
            refreshTheLayout();
            return;
        }
        refreshTheLayout();
    }

    public void refreshTheLayout() {
        invalidate();
        requestLayout();
    }


    public void setIsNormal(boolean isNormal) {
        mIsNormal = isNormal;
        mThumbPaint.setColor(Color.TRANSPARENT);
        refreshTheLayout();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public enum Status{
        START,RUNING,END
    }
}
