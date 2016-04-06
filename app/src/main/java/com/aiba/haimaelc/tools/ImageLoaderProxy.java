package com.aiba.haimaelc.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.aiba.haimaelc.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

public class ImageLoaderProxy {
    private static final int MB = 1024 * 1024;
    private static final int MAX_DISK_CACHE = MB * 50;
    private static final int MAX_MEMORY_CACHE = MB * 4;
    private static final int MAX_DISK_FILE_COUNT = 100;
    private static final int MAX_WIDHT = 480;
    private static final int MAX_HEIGHT = 800;
    private static final String DISK_CACHE_DIR = "haima/bitmap/";

    private static final int CONNECTIMEOUT = 5 * 1000;
    private static final int READTIMEOUT = 30 * 1000;

    private static final int THREAD_SIZE = Runtime.getRuntime().availableProcessors();

    private static boolean isShowLog = false;

    private static ImageLoader imageLoader;

    public static ImageLoader getInstance() {
        if (imageLoader == null) {
            synchronized (ImageLoaderProxy.class) {
                if (imageLoader == null) {
                    imageLoader = ImageLoader.getInstance();
                }
            }
        }
        return imageLoader;
    }

    public static void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getOwnCacheDirectory(context, DISK_CACHE_DIR);
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context);
        builder.diskCache(new UnlimitedDiskCache(cacheDir));//自定义缓存目录
        builder.diskCacheFileCount(MAX_DISK_FILE_COUNT);//缓存文件最大个数
        builder.diskCacheSize(MAX_DISK_CACHE);//缓存大小
        builder.diskCacheFileNameGenerator(new Md5FileNameGenerator());//保存的时候URL用MD5加密
        //缓存最大的宽度高度
        builder.memoryCacheExtraOptions(MAX_WIDHT, MAX_HEIGHT);//设置每个缓存文件的最大长宽
        builder.threadPoolSize(THREAD_SIZE);//线程池线程个数
        builder.threadPriority(Thread.NORM_PRIORITY - 2);//设置线程的优先级
        builder.memoryCacheSize(MAX_MEMORY_CACHE);//内存缓存的大小
        builder.imageDownloader(new BaseImageDownloader(context, CONNECTIMEOUT, READTIMEOUT));//设置连接和超时时间
        builder.defaultDisplayImageOptions(getDefaultOption());
        if (LogUtils.DEBUG && isShowLog)
            builder.writeDebugLogs();

        getInstance().init(builder.build());
    }

    public static DisplayImageOptions getDisplayOption(int loadResource) {
        return getDisplayOption(loadResource, ImageScaleType.EXACTLY_STRETCHED, 5);
    }

    public static DisplayImageOptions getDisplayOption(int loadResource, ImageScaleType scaleType) {
        return getDisplayOption(loadResource, scaleType, 5);
    }

    public static DisplayImageOptions getDisplayOption(int loadResource, int radian) {
        return getDisplayOption(loadResource, ImageScaleType.EXACTLY_STRETCHED, radian);
    }

    public static DisplayImageOptions getDisplayOption(int loadResource, ImageScaleType scaleType, int radian) {
        DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();

        if (loadResource != 0) {
            //URL＝null时显示的图片
            builder.showImageForEmptyUri(loadResource);
            //加载过程中显示的图片
            builder.showImageOnLoading(loadResource);
            //加载失败后显示的图片
            builder.showImageOnFail(loadResource);
        }
        //支持内存缓存 ，default FALSE
        builder.cacheInMemory(true);
        //支持sd卡缓存，default FALSE
        builder.cacheOnDisk(true);
        /**
         *  设置图片以何种编码方式显示(缩放方式)
         *  EXACTLY :图像将完全按比例缩小的目标大小
         *
         *  EXACTLY_STRETCHED:图片会缩放到目标大小完全
         *
         *  IN_SAMPLE_INT:图像将被二次采样的整数倍
         *
         *  IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
         *
         *  NONE:图片不会调整
         *
         */
        if (scaleType != null)
            builder.imageScaleType(scaleType);
        //设置图片的解码类型
        builder.bitmapConfig(Bitmap.Config.RGB_565);
        //设置图片显示为圆角,弧度
        if (radian != 0)
            builder.displayer(new RoundedBitmapDisplayer(radian));
        return builder.build();
    }

    public static DisplayImageOptions getDefaultOption() {
        return new DisplayImageOptions
                .Builder()
                //URL＝null时显示的图片
                .showImageForEmptyUri(R.mipmap.default_image)
                //加载过程中显示的图片
                .showImageOnLoading(R.mipmap.default_image)
                //加载失败后显示的图片
                .showImageOnFail(R.mipmap.default_image)
                //支持内存缓存 ，default FALSE
                .cacheInMemory(true)
                //支持sd卡缓存，default FALSE
                .cacheOnDisk(true)
                /**
                 *  设置图片以何种编码方式显示(缩放方式)
                 *  EXACTLY :图像将完全按比例缩小的目标大小
                 *
                 *  EXACTLY_STRETCHED:图片会缩放到目标大小完全
                 *
                 *  IN_SAMPLE_INT:图像将被二次采样的整数倍
                 *
                 *  IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
                 *
                 *  NONE:图片不会调整
                 *
                 */
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                //设置图片的解码类型
                .bitmapConfig(Bitmap.Config.RGB_565)
                //设置图片显示为圆角,弧度
                .displayer(new RoundedBitmapDisplayer(5))
                .build();
    }

    /**
     * 个人头像专用
     */
    public static void displayImage4Face(String url, ImageView imageView) {
        imageLoader.displayImage(url, imageView, getDisplayOption(R.mipmap.default_image));
    }

    /**
     * 评论中头像专用
     */
    public static void displayImage4CommentHead(String url, ImageView imageView) {
        imageLoader.displayImage(url, imageView, getDisplayOption(R.mipmap.slide_person_photo, 1000));
    }

    /**
     * 取车员和送车员头像专用
     */
    public static void displayImage4Driver(String url, ImageView imageView) {
        imageLoader.displayImage(url, imageView, getDisplayOption(R.mipmap.slide_person_photo, 1000));
    }

    /**
     * 评论中浏览图片先显示缩略图
     */
    public static void displayImage4Thumbnail(String url, ImageView imageView) {
        DisplayImageOptions options = getDisplayOption(R.mipmap.default_image, null, 0);
        imageLoader.displayImage(url, imageView, options);
    }

    /**
     * 浏览图片的大图
     */
    public static void displayImage4Photo(String url, ImageView imageView, ImageLoadingListener loadingListener) {
        DisplayImageOptions options = getDisplayOption(0, null, 0);
        imageLoader.displayImage(url, imageView, options, loadingListener);
    }

    /**
     * 自定义Options
     */
    public static void displayImage(String url, ImageView imageView, DisplayImageOptions options) {
        imageLoader.displayImage(url, imageView, options);
    }
}
