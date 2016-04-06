package com.aiba.haimaelc.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RatingBar;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.tools.AliOss;
import com.aiba.haimaelc.tools.BitmapTools;
import com.aiba.haimaelc.tools.CommonTools;
import com.aiba.haimaelc.tools.LogUtils;
import com.aiba.haimaelc.tools.ToastUtil;
import com.aiba.haimaelc.widget.CustomerChoiceDialog;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class CommentStationActivity extends BaseActivity implements View.OnClickListener,
        RatingBar.OnRatingBarChangeListener {

    private RatingBar comment_star;
    private String stationId;
    private GridLayout gl_comment_photos;
    private String comment_input = "", photo_key = "";
    private List<String> photoPaths = new ArrayList<>();//图片路径
    private HashMap<String, View> photos;//存放图片的view(photoPath-view)
    private HashMap<String, String> photoKeys;//存放上传图片的key(photoPath-key)
    private String filePath;
    private int size;//存放图片的view的数量
    private int uploadFlag = 0;
    private final static int MAX_PHOTO = 5;//最多上传几张图片
    private final static int MAX_TEXT = 100;//描述信息最多字数
    private static final String PHOTO_NAME = "haima_comment_photo";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_station);
        Intent intent = getIntent();
        if (intent == null) {
            finish();
            return;
        }
        stationId = intent.getStringExtra(AppConst.STATION_ID);
        if (TextUtils.isEmpty(stationId)) {
            finish();
            return;
        }
        initView();
        initPhotos();
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("我要评论");
        Button bt_submit = (Button) findViewById(R.id.bt_submit);
        bt_submit.setOnClickListener(this);
        bt_submit.setText("提交评论");
        gl_comment_photos = (GridLayout) findViewById(R.id.gl_comment_photos);
        comment_star = (RatingBar) findViewById(R.id.comment_star);
        comment_star.setOnRatingBarChangeListener(this);
    }

    private void initPhotos() {
        photoKeys = new HashMap<>();
        photos = new HashMap<>();
        addPhotos(null, "");
    }

    private void addPhotos(Bitmap bitmap, String tag) {
        GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
        layoutParams.width = layoutParams.height = (CommonTools.getScreenWidth(this) - CommonTools.dpToPx(this, 20)) / 5;
        View add_photo = LayoutInflater.from(this).inflate(R.layout.view_photo, null);
        add_photo.setLayoutParams(layoutParams);
        ImageView photo_image = (ImageView) add_photo.findViewById(R.id.photo);
        photo_image.setOnClickListener(this);
        View photo_layout = LayoutInflater.from(this).inflate(R.layout.view_photo, null);
        photo_layout.setLayoutParams(layoutParams);
        ImageView photo = (ImageView) photo_layout.findViewById(R.id.photo);
        View delete = photo_layout.findViewById(R.id.photo_delete);
        if (bitmap == null) {
            gl_comment_photos.addView(add_photo, 0);
        } else {
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(this);
            delete.setTag(tag);
            photo_layout.setTag(tag);
            photo.setImageBitmap(BitmapTools.getCornerBitmap(bitmap));
            size = photos.size();
            switch (size) {
                case MAX_PHOTO - 1:
                    gl_comment_photos.removeViewAt(MAX_PHOTO - 1);
                    for (String str : photoPaths) {
                        if (!photos.keySet().contains(str)) {
                            photos.put(tag, photo_layout);
                            gl_comment_photos.addView(photo_layout, MAX_PHOTO - 1);
                            break;
                        }
                    }
                    break;
                default:
                    for (String str : photoPaths) {
                        if (!photos.keySet().contains(str)) {
                            photos.put(tag, photo_layout);
                            gl_comment_photos.addView(photo_layout, size);
                            break;
                        }
                    }
                    break;
            }
        }
    }

    private void showPickPhotoFrom(int type) {
        if (type == 1) {
            try {
                Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File file = CommonTools.getMediaSdCacheDir();
                if (file != null) {
                    filePath = file.getPath() + "/" + PHOTO_NAME + System.currentTimeMillis() + ".jpg";
                } else {
                    filePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                            + "/" + PHOTO_NAME + System.currentTimeMillis() + ".jpg";
                }
                File photo = new File(filePath);
                camIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
                startActivityForResult(camIntent, CAMERA_CAPTURE);
            } catch (Exception e) {
                ToastUtil.makeText("请开启拍照的权限!");
            }
        } else {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, SELECT_FROM_ALBUM);
        }
    }

    /**
     * 阿里云
     *
     * @param path 文件路径
     * @param key  上传文件名
     */
    private void upload(final String path, final String key) {
        uploadFlag++;
        photo_key = key;
        LogUtils.logE("path:" + path);
        LogUtils.logE("name:" + key);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BitmapTools.processImg(path, 240, BitmapTools.getImageDegree(path)).compress(
                Bitmap.CompressFormat.PNG, 100, baos);
        LogUtils.logE("压缩后图片尺寸" + baos.size());
        setPhotoProgress(path, true, false);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(AliOss.BUCKET, key, baos.toByteArray());
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {

            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                LogUtils.logE("currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        AliOss.getOSSClient(getApplicationContext()).asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {

            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
//                ProgressManager.closeProgress();
                LogUtils.logE("UploadSuccess");
                LogUtils.logE("ETag:" + result.getETag());
                LogUtils.logE("RequestId:" + result.getRequestId());
                photoKeys.put(path, key);
                LogUtils.logE("path-" + path + "===key-" + key);
                uploadFlag--;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPhotoProgress(path, false, false);
                    }
                });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
//                ProgressManager.closeProgress();
                ToastUtil.makeText("上传失败");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        setPhotoProgress(path, false, true);
                    }
                });
                uploadFlag--;
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    LogUtils.logE("ErrorCode:" + serviceException.getErrorCode());
                    LogUtils.logE("RequestId:" + serviceException.getRequestId());
                    LogUtils.logE("HostId:" + serviceException.getHostId());
                    LogUtils.logE("RawMessage:" + serviceException.getRawMessage());
                }
            }
        });
    }

    private void setPhotoProgress(String path, boolean isShow, boolean isUploadFalse) {
        View view = photos.get(path);
        if (view != null) {
            view.setOnClickListener(isShow ? null : this);
            view.findViewById(R.id.pb_upload_photo).setVisibility(isShow ? View.VISIBLE : View.GONE);
            view.findViewById(R.id.tv_reupload).setVisibility(!isShow && isUploadFalse ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.bt_submit:
                // TODO: 2016/3/15 评论
                finish();
                break;
            case R.id.photo_delete:
                String tag = (String) v.getTag();
                size = photos.size();
                View view;
                Set<String> set = photos.keySet();
                for (String k : set) {
                    view = photos.get(k);
                    if (view.getTag().equals(tag)) {
                        photos.remove(k);
                        gl_comment_photos.removeView(view);
                        photoPaths.remove(tag);
                        break;
                    }
                }
                if (photos.size() == MAX_PHOTO - 1) {
                    GridLayout.LayoutParams layoutParams = new GridLayout.LayoutParams();
                    layoutParams.width = layoutParams.height = (CommonTools.getScreenWidth(this) - CommonTools.dpToPx(this, 20)) / 5;
                    View add_photo = LayoutInflater.from(this).inflate(R.layout.view_photo, null);
                    add_photo.setLayoutParams(layoutParams);
                    ImageView photo_image = (ImageView) add_photo.findViewById(R.id.photo);
                    photo_image.setOnClickListener(this);
                    gl_comment_photos.addView(add_photo, MAX_PHOTO - 1);
                }
                break;
            case R.id.photo:
                if (photos.size() >= MAX_PHOTO) {
                    ToastUtil.makeText("最多上传" + MAX_PHOTO + "张图片");
                } else {
                    final CustomerChoiceDialog dialog = new CustomerChoiceDialog(this);
                    dialog.setButton1Text("从相册选择", R.color.green_dark);
                    dialog.setButton2Text("拍照", R.color.green_dark);
                    dialog.setButton1OnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            showPickPhotoFrom(0);
                        }
                    });
                    dialog.setButton2OnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            showPickPhotoFrom(1);
                        }
                    });
                    dialog.show();
                }
                break;
            case R.id.photo_view:
                if (v.findViewById(R.id.tv_reupload).getVisibility() == View.VISIBLE) {
                    Set<String> kset = photos.keySet();
                    for (String ks : kset) {
                        if (v.equals(photos.get(ks))) {
                            try {
                                String key = CommonTools.getMd5ByFile(new File(ks));
                                upload(ks, key);
                            } catch (IOException e) {
                                e.printStackTrace();
                                ToastUtil.makeText("上传失败");
                            }
                            return;
                        }
                    }
                } else {
                    try {
                        intent = new Intent(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse("file://" + v.getTag()), "image/*");
                        startActivity(intent);
//                        overridePendingTransition(R.anim.head_in, R.anim.retain);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            Bitmap bitmap;
            switch (requestCode) {
                case CAMERA_CAPTURE:
                    bitmap = BitmapTools.processImg(filePath, 160, BitmapTools.getImageDegree(filePath));
                    if (bitmap == null) {
                        ToastUtil.makeText("拍照失败");
                    } else {
                        photoPaths.add(filePath);
                        addPhotos(bitmap, filePath);
                        try {
                            String key = CommonTools.getMd5ByFile(new File(filePath));
                            upload(filePath, key);
                        } catch (IOException e) {
                            e.printStackTrace();
                            ToastUtil.makeText("上传失败");
                        }
                    }
                    break;
                case SELECT_FROM_ALBUM:
                    if (data == null)
                        return;
                    Uri selectedImage = data.getData();
                    LogUtils.logE(selectedImage.toString());
                    ContentResolver cr = getContentResolver();
                    Cursor cursor = cr.query(selectedImage, null, null, null, null);
                    if (cursor != null) {
                        cursor.moveToFirst();
                        int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                        if (index == -1) {
                            ToastUtil.makeText("选图失败");
                            return;
                        }
                        filePath = cursor.getString(index);
                        cursor.close();
                    } else {
                        filePath = selectedImage.getPath();
                    }

                    bitmap = BitmapTools.processImg(filePath, 160, BitmapTools.getImageDegree(filePath));
                    if (bitmap == null) {
                        ToastUtil.makeText("选图失败");
                    } else {
                        photoPaths.add(filePath);
                        addPhotos(bitmap, filePath);
                        try {
                            String key = CommonTools.getMd5ByFile(new File(filePath));
                            upload(filePath, key);
                        } catch (IOException e) {
                            e.printStackTrace();
                            ToastUtil.makeText("上传失败");
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
        LogUtils.logE("ratingbar");
        if (rating == 0) {
            ratingBar.setRating(1);
        }
    }
}
