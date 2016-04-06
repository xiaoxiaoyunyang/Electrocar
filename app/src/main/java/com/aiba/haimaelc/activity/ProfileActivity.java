package com.aiba.haimaelc.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.aiba.haimaelc.AppConst;
import com.aiba.haimaelc.R;
import com.aiba.haimaelc.model.SortCity;
import com.aiba.haimaelc.tools.AliOss;
import com.aiba.haimaelc.tools.BitmapTools;
import com.aiba.haimaelc.tools.CommonTools;
import com.aiba.haimaelc.tools.LogUtils;
import com.aiba.haimaelc.tools.ProgressManager;
import com.aiba.haimaelc.tools.ToastUtil;
import com.aiba.haimaelc.widget.CustomerChoiceDialog;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;

import java.io.File;
import java.io.IOException;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    private TextView tv_city;
    private ImageView iv_user_photo;
    private String filePath = "";//头像图片的路径
    private String photo_key = "";//头像图片的key

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initView();
    }

    private void initView() {
        setTitleBarBack();
        setTitleText("编辑资料");
        findViewById(R.id.select_city).setOnClickListener(this);
        findViewById(R.id.modify_phone).setOnClickListener(this);
        findViewById(R.id.modify_nickname).setOnClickListener(this);
        tv_city = (TextView) findViewById(R.id.tv_city);
        iv_user_photo = (ImageView) findViewById(R.id.iv_user_photo);
        iv_user_photo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.modify_phone:
                intent = new Intent(ProfileActivity.this, ModifyInformationActivity.class);
                intent.putExtra(AppConst.MODIFY_TYPE, AppConst.MODIFY_PHONE);
                startActivityForResult(intent, MODIFY_PHONE);
                break;
            case R.id.modify_nickname:
                intent = new Intent(ProfileActivity.this, ModifyInformationActivity.class);
                intent.putExtra(AppConst.MODIFY_TYPE, AppConst.MODIFY_NICK_NAME);
                startActivityForResult(intent, MODIFY_NICKNAME);
                break;
            case R.id.select_city:
                intent = new Intent(ProfileActivity.this, SelectCityActivity.class);
                startActivityForResult(intent, SELECT_CITY);
                break;
            case R.id.iv_user_photo:
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
                break;
        }
    }

    private void showPickPhotoFrom(int type) {
        switch (type) {
            case 0:
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, SELECT_FROM_ALBUM);
                break;
            case 1:
                try {
                    Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    filePath = getFilePath();
                    File file = new File(filePath);
                    camIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(camIntent, CAMERA_CAPTURE);
                } catch (Exception e) {
                    ToastUtil.makeText("请开启拍照的权限!");
                }
                break;
        }
    }

    private String getFilePath() {
        File files = CommonTools.getMediaSdCacheDir();
        if (files != null) {
            return files.getPath() + File.separator + System.currentTimeMillis() + ".jpg";
        } else {
            return Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
        }
    }

    //裁剪图片
    private void cropPhoto() {
        Intent intent = new Intent("com.android.camera.action.CROP"); // 剪裁
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "image/*");
        intent.putExtra("scale", true);
        // 设置宽高比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 设置裁剪图片宽高
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        filePath = getFilePath();//不用会将相册选图的原图裁剪了
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(filePath)));
        startActivityForResult(intent, CROP_PHOTO); // 设置裁剪参数显示图片至ImageView
    }

    /**
     * 阿里云
     *
     * @param path 文件路径
     * @param key  上传文件名
     */
    private void upload(final String path, final String key) {
        LogUtils.logE("path:" + path);
        LogUtils.logE("name:" + key);
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(AliOss.BUCKET, key, path);
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
                ProgressManager.closeProgress();
                LogUtils.logE("UploadSuccess");
                LogUtils.logE("ETag:" + result.getETag());
                LogUtils.logE("RequestId:" + result.getRequestId());
                photo_key = key;
                ProfileActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap bitmap = BitmapTools.processImg(filePath, 160, BitmapTools.getImageDegree(filePath));
                        iv_user_photo.setImageBitmap(BitmapTools.getCornerBitmap(bitmap));
                    }
                });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                ProgressManager.closeProgress();
                ToastUtil.makeText("上传失败");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SELECT_CITY:
                    SortCity sortCity = (SortCity) data.getSerializableExtra(AppConst.SELECT_CITY);
                    tv_city.setText(sortCity.getName());
                    break;
                case CAMERA_CAPTURE:
                    cropPhoto();
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
                    } else if (selectedImage != null) {  //在部分定制的android机型上处理方式 htc miui
                        filePath = selectedImage.getPath();
                    }
                    cropPhoto();
                    break;
                case CROP_PHOTO:
                    if (data == null)
                        return;
                    try {
                        String key = CommonTools.getMd5ByFile(new File(filePath));
                        ProgressManager.showProgress(this, "头像上传中");
                        upload(filePath, key);
                    } catch (IOException e) {
                        e.printStackTrace();
                        ToastUtil.makeText("上传失败");
                    }
                    break;
            }
        }
    }
}
