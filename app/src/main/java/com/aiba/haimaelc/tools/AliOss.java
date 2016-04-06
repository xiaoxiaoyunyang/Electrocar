package com.aiba.haimaelc.tools;

import android.content.Context;

import com.aiba.haimaelc.model.AliOssCredential;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class AliOss {

    private static OSS oss;
    //    private static final String accessKeyId = "8D3pNpYyDoNpkKOZ";
//    private static final String accessKeySecret = "OyOufMLgpVAN1ZLhIkmgi62q6w8VS6";
//    private static final String endpoint = "oss-cn-shanghai.aliyuncs.com";
//    public static final String BUCKET = "moofun";
    private static final String accessKeyId = "bUCRG2kBQdtkEbnW";
    private static final String accessKeySecret = "trLNnqsuS9hNQNFZKZwHdTaUNJ8Pdl";
    private static final String endpoint = "oss-cn-beijing.aliyuncs.com";
    public static final String BUCKET = "haimamoofun";
    private static long expiration;//令牌有效期

    public static OSS getOSSClient(Context context) {
        if (oss == null || expiration - System.currentTimeMillis() < 10 * 60 * 1000) {//离有效期小于10分钟重新获取令牌
//            OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
            ClientConfiguration conf = new ClientConfiguration();
            conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
            conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
            conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
            conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
            OSSLog.enableLog();

            // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
            OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(accessKeyId, accessKeySecret);
            oss = new OSSClient(context, endpoint, credentialProvider);

//            OSSCredentialProvider credentialProvider = new OSSFederationCredentialProvider() {
//
//                @Override
//                public OSSFederationToken getFederationToken() {
//                    try {
//                        // TODO: 2016/3/18 后台获取 credentials
//                        AliOssCredential credentials = null;
//                        if (credentials == null) {
//                            return null;
//                        }
//                        LogUtils.logE("获取有效期:" + credentials.expiration);
//                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
//                        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
//                        expiration = sdf.parse(credentials.expiration).getTime();
//                        return new OSSFederationToken(credentials.accessKeyId,
//                                credentials.accessKeySecret, credentials.securityToken, credentials.expiration);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                    return null;
//                }
//            };
//            oss = new OSSClient(context, endpoint, credentialProvider, conf);
        }
        return oss;
    }
}
