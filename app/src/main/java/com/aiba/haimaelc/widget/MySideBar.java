package com.aiba.haimaelc.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.aiba.haimaelc.R;
import com.aiba.haimaelc.tools.CommonTools;

public class MySideBar extends View {
    // 触摸事件  
    private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
    // 26个字母  
    public static String[] b = {"热", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};
    private int choose = -1;// 选中  
    private Paint paint = new Paint();
    private TextView mTextDialog;
    private int pHeight;

    /**
     * 为SideBar设置显示字母的TextView
     *
     * @param mTextDialog
     */
    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }


    public MySideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MySideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MySideBar(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        pHeight = MeasureSpec.getSize(heightMeasureSpec);
    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 获取焦点改变背景颜色.
//		int height = AppConfig.isYIQI ? MainActivity.screenHeight * 3 / 4
//                : AiBaMainActivity.screenHeight * 3 / 4;// 获取对应高度
        int height = pHeight * 7 / 8;
        int width = getWidth(); // 获取对应宽度  
        int singleHeight = height / b.length;// 获取每一个字母的高度  
        int initHeight = height / 16;
        int pxSize;
        for (int i = 0; i < b.length; i++) {
            paint.setColor(getContext().getResources().getColor(R.color.green_dark));
            // paint.setColor(Color.WHITE);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            pxSize = CommonTools.dpToPx(getContext(), 14);
            paint.setTextSize(pxSize);
            // 选中的状态  
            if (i == choose) {
                paint.setColor(getContext().getResources().getColor(R.color.green_dark));
                paint.setFakeBoldText(true);
                pxSize = CommonTools.dpToPx(getContext(), 16);
                paint.setTextSize(pxSize);
            }
            // x坐标等于中间-字符串宽度的一半.  
            float xPos = width / 2 - paint.measureText(b[i]) / 2;
            float yPos = initHeight + singleHeight * i + singleHeight
                    - (singleHeight - pxSize) / 2;
            canvas.drawText(b[i], xPos, yPos, paint);
            paint.reset();// 重置画笔  
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        final float y = event.getY();// 点击y坐标  
        final int oldChoose = choose;
        final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
//        int height = AppConfig.isYIQI ? MainActivity.screenHeight * 3 / 4
//                : AiBaMainActivity.screenHeight * 3 / 4;
        int height = pHeight * 7 / 8;
//        int initHeight = height /8 - AiBaHelp.dpToPx(50, getContext())/2;
        int initHeight = height / 16;
        if (y < initHeight || y > height + initHeight) {
            setBackgroundColor(0x00000000);
            choose = -1;
            invalidate();
            return true;
        }
        final int c = (int) ((y - initHeight) * b.length / height);// 点击y坐标所占总高度的比例*b数组的长度就等于点击b中的个数.
        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(0x00000000);
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;

            default:
//            setBackgroundResource(R.drawable.aiba_sidebar_background);  
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onTouchingLetterChanged(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }

                        choose = c;
                        invalidate();
                    }
                }

                break;
        }
        return true;
    }

    /**
     * 向外公开的方法
     *
     * @param onTouchingLetterChangedListener
     */
    public void setOnTouchingLetterChangedListener(
            OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
        this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
    }

    /**
     * 接口
     *
     * @author coder
     */
    public interface OnTouchingLetterChangedListener {
        public void onTouchingLetterChanged(String s);
    }

}  