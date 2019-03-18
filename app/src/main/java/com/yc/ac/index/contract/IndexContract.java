package com.yc.ac.index.contract;


import com.yc.ac.index.model.bean.BookInfo;
import com.yc.ac.index.model.bean.TagInfo;
import com.yc.ac.index.model.bean.VersionDetailInfo;

import java.util.List;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;

/**
 * Created by wanglin  on 2018/2/27 14:46.
 */

public interface IndexContract {

    interface View extends IView, ILoading, INoData, INoNet, IHide {
        void showVersionList(List<VersionDetailInfo> data);


        void showImgList(List<String> imgList);

        void showHotBooks(List<BookInfo> lists);

        void showConditionList(List<String> data);

        void showTagInfos(List<TagInfo> data);

        void showZtInfos(List<TagInfo> list);
    }

    interface Presenter extends IPresenter {


        void getSlideInfo(String group);

        void getVersionList();
    }
}
