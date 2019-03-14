package com.yc.answer.index.presenter;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.LogUtil;
import com.yc.answer.index.contract.UploadContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.model.bean.OssInfo;
import com.yc.answer.index.model.engine.UploadBookEngine;
import com.yc.answer.utils.ToastUtils;
import com.yc.answer.utils.UserInfoHelper;

import java.util.HashMap;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.base.UIUtils;

/**
 * Created by wanglin  on 2018/4/23 16:09.
 */

public class UploadPresenter extends BasePresenter<UploadBookEngine, UploadContract.View> implements UploadContract.Presenter {
    public UploadPresenter(Context context, UploadContract.View view) {
        super(context, view);
        mEngine = new UploadBookEngine(context);
    }

    @Override
    public void loadData(boolean isForceUI, boolean isLoadingUI) {

    }

    public void getOssInfo(final String path, final String type, final String bookId) {
        mView.showLoadingDialog("正在上传，请稍候...");
        Subscription subscription = mEngine.getOssInfo().subscribe(new Subscriber<OssInfo>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(OssInfo ossInfo) {
                if (ossInfo.getStatus() == 200) {
                    uploadBook(ossInfo, path, type, bookId);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

    public void uploadBook(OssInfo ossInfo, String path, final String type, final String bookId) {
        String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider(ossInfo.getAccessKeyId(), ossInfo.getAccessKeySecret(), ossInfo.getSecurityToken());

        //该配置类如果不设置，会有默认配置，具体可看该类
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求数，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次

        OSSLog.enableLog(); //这个开启会支持写入手机sd卡中的一份日志文件位置在SDCard_path\OSSLog\logs.csv


        OSS oss = new OSSClient(mContext, endpoint, credentialProvider, conf);

        LogUtil.msg(path);
        // 构造上传请求
        final PutObjectRequest put = new PutObjectRequest("answer-bshu", "userImg/" + path.substring(path.lastIndexOf("/") + 1), path);
        put.setCallbackParam(new HashMap<String, String>() {
            {
                put("callbackUrl", "http://answer.bshu.com/api/ali_oss/osscallback");
                put("callbackHost", "answer.bshu.com");
                put("callbackBodyType", "application/json");
                put("callbackBody", "{\"object\":${object},\"size\":${size} ,\"type\":${x:type},\"user_id\":${x:user_id},\"book_id\":${x:book_id}}");

            }
        });
        put.setCallbackVars(new HashMap<String, String>() {
            {
                put("x:type", type);
                put("x:user_id", UserInfoHelper.getUId());
                put("x:book_id", bookId);
            }
        });
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Log.d("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);
            }
        });
        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                mView.dismissDialog();
                Log.d("PutObject", "UploadSuccess");
                String body = result.getServerCallbackReturnBody();
                LogUtil.msg("success:" + body);
                try {
                    final ResultInfo<BookInfo> resultInfo = JSON.parseObject(body, new TypeReference<ResultInfo<BookInfo>>() {
                    }.getType());
                    if (resultInfo != null && resultInfo.code == HttpConfig.STATUS_OK && resultInfo.data != null) {
//                        LogUtil.msg("success: " + resultInfo.data.toString());
                        UIUtils.post(new Runnable() {
                            @Override
                            public void run() {
                                mView.showUploadResult(resultInfo.data);
                            }
                        });
                    } else {
                        ToastUtils.showCenterToast(mContext, "书本上传不正确，请重新上传");
                    }

                } catch (Exception e) {
                    ToastUtils.showCenterToast(mContext, "书本上传不正确，请重新上传");
                }


            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                mView.dismissDialog();
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });


    }


}
