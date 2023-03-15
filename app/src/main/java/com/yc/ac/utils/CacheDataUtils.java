package com.yc.ac.utils;


import com.tencent.mmkv.MMKV;

import java.util.ArrayList;
import java.util.List;


/**
 * Version: 1.0
 */
public class CacheDataUtils {
    private static CacheDataUtils instance;
    public static CacheDataUtils getInstance() {
        if (instance == null) {
            synchronized (CacheDataUtils.class) {
                if (instance == null) {
                    instance = new CacheDataUtils();
                }
            }
        }
        return instance;
    }



    public void setWithDraw() {
        MMKV.defaultMMKV().putString("withdrawsssss", "1");
    }

    public String getWithDraw() {
        String prizes = MMKV.defaultMMKV().getString("withdrawsssss", "");
        return prizes;
    }


    //提现qq
    public void setShareBook(String bookID) {
        MMKV.defaultMMKV().putString("sharkbook"+bookID, bookID);
    }
    //提现qq
    public String getShareBook(String bookID) {
        String bookId = MMKV.defaultMMKV().getString("sharkbook"+bookID, "");
        return bookId;
    }
}
