package com.yc.ac.utils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;

import com.hwangjr.rxbus.RxBus;
import com.kk.utils.LogUtil;

import com.kk.utils.security.Md5;
import com.yc.ac.R;
import com.yc.ac.constant.BusAction;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import yc.com.base.CacheUtils;

/**
 * Created by wanglin  on 2018/3/13 17:19.
 */

public class RxDownloadManager {

    private static RxDownloadManager manager;

    private Context mContext;
    private ExecutorService executorService;
    public List<String> currentUrlList;


    private RxDownloadManager() {

        throw new UnsupportedOperationException("不能直接构造...");
    }

    private RxDownloadManager(Context context) {
        this.mContext = context;
        executorService = Executors.newFixedThreadPool(1);
        currentUrlList = new ArrayList<>();
    }

    public static RxDownloadManager getInstance(Context context) {
        if (manager == null) {
            synchronized (RxDownloadManager.class) {
                if (manager == null) {
                    manager = new RxDownloadManager(context);
                }
            }
        }
        return manager;
    }


    public void downLoad(List<String> list, String fileName) {

        if (currentUrlList.containsAll(list)) {
            ToastUtils.showCenterToast(mContext, "课本正在下载中，请稍候...");
            return;
        }
        currentUrlList.addAll(list);
        for (String s : list) {
            executorService.submit(new DownloadTask(s, fileName));
        }

    }

    private class DownloadTask implements Runnable {

        private String mPath;
        private String mFileName;

        public DownloadTask(String mPath, String fileName) {
            this.mPath = mPath;
            this.mFileName = fileName;
        }

        @Override
        public void run() {
            downLoadImg(mPath, mFileName);
//            LogUtil.msg("TAG: " + Thread.currentThread().getName());
        }


    }

    private void downLoadImg(String path, String fileName) {
        FileOutputStream fileOutputStream = null;
        try {
            URL url = new URL(path);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            String name = getFileName(path);

            File file = new File(CacheUtils.makeBaseDir(mContext, fileName), name);
            fileOutputStream = new FileOutputStream(file);
            if (file.exists() && file.length() == urlConnection.getContentLength()) {
                return;
            }
            InputStream inputStream = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bufferLength);
            }

            MediaStore.Images.Media.insertImage(mContext.getContentResolver(), file.getAbsolutePath(), name, mContext.getString(R.string.app_name));
            // 最后通知图库更新
            mContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));

            RxBus.get().post(BusAction.DOWNLOAD, path);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String getFileName(String path) {
        String name = Md5.md5(path);
        if (path.lastIndexOf("/") != -1) {
            name = path.substring(path.lastIndexOf("/") + 1);
        }
        return name;
    }


    public boolean isFileExisted(String fileName, String path) {
        String name = getFileName(path);
        return new File(CacheUtils.makeBaseDir(mContext, fileName), name).exists();
    }

    public void destroy() {
        if (!executorService.isShutdown()) {
            executorService.shutdownNow();
            currentUrlList = null;
            executorService = null;
        }
    }


}
