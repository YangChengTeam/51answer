package com.yc.ac.utils;

import android.content.Context;
import android.os.Looper;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;

import java.io.File;

/**
 * Created by wanglin  on 2018/2/7 10:34.
 */

public class GlideCacheHelper {

    private static GlideCacheHelper instance;
    private Context context;
    private String cache_path;

    public GlideCacheHelper(Context context) {
        this.context = context;
        cache_path = context.getCacheDir() + "/" + InternalCacheDiskCacheFactory.DEFAULT_DISK_CACHE_DIR;
//                DiskCache.Factory.DEFAULT_DISK_CACHE_DIR;
    }

    public static GlideCacheHelper getInstance(Context context) {
        if (instance == null)
            synchronized (GlideCacheHelper.class) {
                if (instance == null) {
                    instance = new GlideCacheHelper(context);
                }
            }
        return instance;
    }


    public String getCacheSize() {
        try {
            return ACache.getFormatSize(ACache.getFolderSize(new File(cache_path)));
        } catch (Exception e) {
            e.printStackTrace();
            return "获取失败";
        }

    }

    public boolean clearCache() {

        clearMemoryCache();
//        clearDiskCache();
        return deleteFolderFile(cache_path, true);

    }


    private boolean clearMemoryCache() {

        //运行在主线程
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                Glide.get(context).clearMemory();
                return true;
            }
        } catch (Exception e) {
        }
        return false;
    }

    private boolean clearDiskCache() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
            return true;

        } catch (Exception e) {

        }
        return false;
    }

    /**
     * 删除指定目录下的文件，这里用于缓存的删除
     *
     * @param filePath       filePath
     * @param deleteThisPath deleteThisPath
     */
    private boolean deleteFolderFile(String filePath, boolean deleteThisPath) {
        if (!TextUtils.isEmpty(filePath)) {
            try {
                File file = new File(filePath);
                if (file.isDirectory()) {
                    File files[] = file.listFiles();
                    for (File file1 : files) {
                        deleteFolderFile(file1.getAbsolutePath(), true);
                    }
                }
                if (deleteThisPath) {
                    if (!file.isDirectory()) {
                        file.delete();
                    } else {
                        if (file.listFiles().length == 0) {
                            file.delete();
                        }
                    }
                }
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

}
