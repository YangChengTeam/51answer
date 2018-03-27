package com.yc.answer.index.model.bean;

import java.util.List;

/**
 * Created by wanglin  on 2018/3/8 09:04.
 */

public class BookInfoWrapper {
    private int count;
    private List<BookInfo> lists;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<BookInfo> getLists() {
        return lists;
    }

    public void setLists(List<BookInfo> lists) {
        this.lists = lists;
    }
}
