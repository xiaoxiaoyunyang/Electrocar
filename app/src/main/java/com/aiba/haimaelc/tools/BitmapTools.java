package com.aiba.haimaelc.tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;

public class BitmapTools {

    public static Bitmap processImg(String filePath, int width, int degree) {
        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
        //解析BitMap的对象不占用内存
        bmpFactoryOptions.inJustDecodeBounds = true;
        //使用options解析Bitmap对象，换回的bitmap对象为空，数据存储在Options中
        BitmapFactory.decodeFile(filePath, bmpFactoryOptions);
        bmpFactoryOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        bmpFactoryOptions.inPurgeable = true;
        bmpFactoryOptions.inInputShareable = true;
        int www = bmpFactoryOptions.outWidth < bmpFactoryOptions.outHeight ?
                bmpFactoryOptions.outWidth : bmpFactoryOptions.outHeight;
        //计算缩放比例
        bmpFactoryOptions.inSampleSize = www / width;
        LogUtils.logE(bmpFactoryOptions.inSampleSize + "");
        bmpFactoryOptions.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, bmpFactoryOptions);
        if (degree != 0) {
            // 旋转图片
            Matrix m = new Matrix();
            m.postRotate(degree);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), m, true);
        }
        return bitmap;
    }

    /**
     * 获得图片角度方向
     *
     * @param filePath 图片路径
     * @return 角度方向
     */
    public static int getImageDegree(String filePath) {
        //根据图片的filepath获取到一个ExifInterface的对象
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);
        } catch (Exception e) {
            e.printStackTrace();
            exif = null;
        }
        //获取图片的方向角度
        int degree = 0;
        if (exif != null) {
            // 读取图片中相机方向信息
            int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);
            // 计算旋转角度
            switch (ori) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
                default:
                    degree = 0;
                    break;
            }
        }
        return degree;
    }

    public static Bitmap getCornerBitmap(Bitmap bitmap) {
        return getCornerBitmap(bitmap, true, true, true, true);
    }

    public static Bitmap getCornerBitmap(Bitmap bitmap, boolean leftTop, boolean leftBottom,
                                         boolean rightTop, boolean rightBottom) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        Rect rect = new Rect(0, 0, w, h);
        paint.setAntiAlias(true);
        Path p = new Path();
        if (leftTop) {
            RectF rectF = new RectF(0, 0, w / 10, h / 10);
            p.arcTo(rectF, 180, 90);
        } else {
            p.moveTo(0, 0);
        }
        p.lineTo(w * 19 / 20, 0);
        if (rightTop) {
            RectF rectF = new RectF(w * 9 / 10, 0, w, h / 10);
            p.arcTo(rectF, 270, 90);
        } else {
            p.lineTo(w, 0);
        }
        p.lineTo(w, h * 19 / 20);
        if (rightBottom) {
            RectF rectF = new RectF(w * 9 / 10, h * 9 / 10, w, h);
            p.arcTo(rectF, 0, 90);
        } else {
            p.lineTo(w, h);
        }
        p.lineTo(w / 20, h);
        if (leftBottom) {
            RectF rectF = new RectF(0, h * 9 / 10, w / 10, h);
            p.arcTo(rectF, 90, 90);
        } else {
            p.lineTo(0, h);
        }
        p.close();
        canvas.drawPath(p, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}
