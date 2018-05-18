package com.yc.answer.index.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.vondear.rxtools.RxSPTool;
import com.yc.answer.constant.SpConstant;
import com.yc.answer.index.contract.IndexContract;
import com.yc.answer.index.model.bean.BookInfo;
import com.yc.answer.index.model.bean.BookInfoWrapper;
import com.yc.answer.index.model.bean.SlideInfo;
import com.yc.answer.index.model.bean.VersionDetailInfo;
import com.yc.answer.index.model.bean.VersionInfo;
import com.yc.answer.index.model.engine.IndexEngine;
import com.yc.answer.utils.EngineUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.base.BasePresenter;
import yc.com.base.CommonInfoHelper;

/**
 * Created by wanglin  on 2018/2/27 14:46.
 */

public class IndexPresenter extends BasePresenter<IndexEngine, IndexContract.View> implements IndexContract.Presenter {
    public IndexPresenter(Context context, IndexContract.View mView) {
        super(context, mView);
        mEngine = new IndexEngine(context);
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
//        getSlideInfo("home");
//        getVersionList();
//        getConditionList();
//        getHotBooks("2");

    }


    @Override
    public void getSlideInfo(String group) {
        CommonInfoHelper.getO(mContext, SpConstant.SLIDE_INFO, new TypeReference<List<SlideInfo>>() {
        }.getType(), new CommonInfoHelper.onParseListener<List<SlideInfo>>() {
            @Override
            public void onParse(List<SlideInfo> o) {
                if (o != null) {
                    showImagList(o);
                }
            }

            @Override
            public void onFail(String json) {

            }
        });

        Subscription subscription = mEngine.getSlideInfos(group).subscribe(new Subscriber<ResultInfo<List<SlideInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<SlideInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    CommonInfoHelper.setO(mContext, listResultInfo.data, SpConstant.SLIDE_INFO);
                    showImagList(listResultInfo.data);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public SlideInfo getSlideInfo(int postion) {
        if (mSlideInfos != null) {
            return mSlideInfos.get(postion);
        }
        return null;

    }

    private List<SlideInfo> mSlideInfos;

    private void showImagList(List<SlideInfo> slideInfos) {
        List<String> imgList = new ArrayList<>();
        if (slideInfos.size() > 0) {
            mSlideInfos = slideInfos;
            for (SlideInfo slideInfo : slideInfos) {
                imgList.add(slideInfo.getImg());
            }
            mView.showImgList(imgList);
        }
    }


    @Override
    public void getVersionList() {

        Subscription subscription = mEngine.getVersionList().subscribe(new Subscriber<ResultInfo<VersionInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getCacheVersion();
            }

            @Override
            public void onNext(ResultInfo<VersionInfo> resultInfoResultInfo) {
                if (resultInfoResultInfo != null && resultInfoResultInfo.code == HttpConfig.STATUS_OK && resultInfoResultInfo.data != null) {
                    showNewData(resultInfoResultInfo.data);
                    CommonInfoHelper.setO(mContext, resultInfoResultInfo.data, SpConstant.INDEX_VERSION);

                } else {
                    getCacheVersion();
                }
            }
        });
        mSubscriptions.add(subscription);


    }


    private void getCacheVersion() {
        CommonInfoHelper.getO(mContext, SpConstant.INDEX_VERSION, new TypeReference<VersionInfo>() {
        }.getType(), new CommonInfoHelper.onParseListener<VersionInfo>() {
            @Override
            public void onParse(VersionInfo versionInfo) {
                if (versionInfo != null) {
                    showNewData(versionInfo);
                }
            }

            @Override
            public void onFail(String json) {

            }
        });
    }


    private void showNewData(VersionInfo versionInfo) {
        if (versionInfo.getSubject() != null) {
            List<VersionDetailInfo> dataSubject = versionInfo.getSubject();

            dataSubject.add(0, new VersionDetailInfo("", "全部"));

            if (dataSubject.size() > 5) {
                dataSubject = dataSubject.subList(0, 5);
            }
            mView.showVersionList(dataSubject);

        }

    }


    public void getHotBooks() {//1: 推荐；2: 热门
        String grade = RxSPTool.getString(mContext, SpConstant.SELECT_GRADE);
//        if (TextUtils.isEmpty(grage)) {
//            grage = "全部";
//        }

        CommonInfoHelper.getO(mContext, SpConstant.HOT_BOOK, new TypeReference<List<BookInfo>>() {
        }.getType(), new CommonInfoHelper.onParseListener<List<BookInfo>>() {
            @Override
            public void onParse(List<BookInfo> bookInfoList) {
                if (bookInfoList != null) {
                    mView.showHotBooks(bookInfoList);
                }
            }

            @Override
            public void onFail(String json) {

            }
        });

        Subscription subscription = EngineUtils.getBookInfoList(mContext, 1, 4, "", "", "", grade, "", "", "", "", "", "", "", "", "", "").subscribe(new Subscriber<ResultInfo<BookInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<BookInfoWrapper> bookInfoWrapperResultInfo) {
                if (bookInfoWrapperResultInfo != null && bookInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && bookInfoWrapperResultInfo.data != null) {
                    mView.showHotBooks(bookInfoWrapperResultInfo.data.getLists());
                    CommonInfoHelper.setO(mContext, bookInfoWrapperResultInfo.data.getLists(), SpConstant.HOT_BOOK);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public void getConditionList() {

        CommonInfoHelper.getO(mContext, SpConstant.HOT_RECOMMOND, new TypeReference<List<String>>() {
        }.getType(), new CommonInfoHelper.onParseListener<List<String>>() {
            @Override
            public void onParse(List<String> o) {
                if (o != null) {
                    mView.showConditionList(o);
                }
            }

            @Override
            public void onFail(String json) {

            }
        });
        Subscription subscription = EngineUtils.getConditionList(mContext).subscribe(new Subscriber<ResultInfo<List<String>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<String>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {

                    mView.showConditionList(listResultInfo.data);
                    CommonInfoHelper.setO(mContext, listResultInfo.data, SpConstant.HOT_RECOMMOND);
                }
            }
        });
        mSubscriptions.add(subscription);
    }

}
