package com.aiba.haimaelc.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;

import com.aiba.haimaelc.R;

public class SlipButton extends View implements OnTouchListener, OnClickListener {

    private String strName;
    public boolean changeByset = false;
    public boolean NowChoose = false;
    private boolean OnSlip = false;
    public float DownX = 0f, NowX = 0f;

    private OnChangedListener ChgLsn;
    private Bitmap bg_on, bg_off, slip_btn;

    public SlipButton(Context context) {
        super(context);
        init(context);
    }

    public SlipButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void setChecked(boolean fl) {
        changeByset = true;
        if (fl) {
            NowChoose = true;
        } else {
            NowChoose = false;
        }
        invalidate();
    }

    private void init(Context context) {
        bg_on = BitmapFactory.decodeResource(getResources(), R.mipmap.slip_on_btn);
        bg_off = BitmapFactory.decodeResource(getResources(), R.mipmap.slip_off_butn);
        slip_btn = BitmapFactory.decodeResource(getResources(), R.mipmap.slip_butn_normal);
        /*	Btn_On = new Rect(0, 0, slip_btn.getWidth(), slip_btn.getHeight());
            Btn_Off = new Rect(bg_off.getWidth() - slip_btn.getWidth(), 0,
					bg_off.getWidth(), slip_btn.getHeight());*/
        setOnTouchListener(this);
        setOnClickListener(this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float x;
        float halfHeight = getHeight() / 2.0f;
        float halfWidth = getWidth() / 2.0f;
        float bgHalfHeight = bg_on.getHeight() / 2.0f;
        float bgHalfWidth = bg_on.getWidth() / 2.0f;
        if (bgHalfHeight > halfHeight) {
            bgHalfHeight = halfHeight;
        }
        if (bgHalfWidth > halfWidth) {
            bgHalfWidth = halfWidth;
        }
        float bgHalfBallHeight = slip_btn.getHeight() / 2.0f;
        float bgHalfBallWidth = slip_btn.getWidth() / 2.0f;

        if (changeByset) {
            changeByset = false;
            OnSlip = false;
        }
        if (OnSlip) {
            if (NowX - bgHalfBallWidth < halfWidth - bgHalfWidth) {
                x = halfWidth - bgHalfWidth;
            } else if (NowX - bgHalfBallWidth > halfWidth + bgHalfWidth
                    - bgHalfBallWidth * 2) {
                x = halfWidth + bgHalfWidth - bgHalfBallWidth * 2;
            } else {
                x = NowX - bgHalfBallWidth;
            }
            if (x < halfWidth - bgHalfWidth) {
                x = halfWidth - bgHalfWidth;
            } else if (x > halfWidth + bgHalfWidth - bgHalfBallWidth * 2) {
                x = halfWidth + bgHalfWidth - bgHalfBallWidth * 2;
            }
        } else {
            if (NowChoose) {
                x = halfWidth + bgHalfWidth - bgHalfBallWidth * 2;
            } else {
                x = halfWidth - bgHalfWidth;
            }
        }
        if (x + bgHalfBallWidth < halfWidth) {
            canvas.drawBitmap(bg_off, halfWidth - bgHalfWidth, halfHeight
                    - bgHalfHeight, null);
        } else {
            canvas.drawBitmap(bg_on, halfWidth - bgHalfWidth, halfHeight
                    - bgHalfHeight, null);
        }
        canvas.drawBitmap(slip_btn, x, halfHeight - bgHalfBallHeight, null);
    }

    private boolean changeAble = true;

    public void setChangeAble(boolean changeAble) {
        this.changeAble = changeAble;
    }

    public boolean onTouch(View v, MotionEvent event) {
        if (!changeAble) {
            return true;
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                NowX = event.getX();
                break;
            case MotionEvent.ACTION_DOWN:
                if (event.getX() > bg_on.getWidth()
                        || event.getY() > bg_on.getHeight()) {
                    return false;
                }
                OnSlip = true;
                DownX = event.getX();
                NowX = DownX;
                break;
            case MotionEvent.ACTION_UP:
                OnSlip = false;
                boolean LastChoose = NowChoose;
                if (event.getX() >= (getWidth() / 2.0f))
                    NowChoose = true;
                else {
                    NowChoose = false;
                }
                if (ChgLsn != null && (LastChoose != NowChoose)) {
                    ChgLsn.OnChanged(strName, NowChoose);
                }
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }

    public void setOnChangedListener(String name, OnChangedListener l) {
        strName = name;
        ChgLsn = l;
    }

    @Override
    public void onClick(View view) {
        invalidate();
    }

    public interface OnChangedListener {
        abstract void OnChanged(String strName, boolean CheckState);
    }
}
