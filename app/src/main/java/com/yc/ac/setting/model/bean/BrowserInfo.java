package com.yc.ac.setting.model.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by wanglin  on 2019/3/15 10:26.
 */
@Entity
public class BrowserInfo {
    @Id(autoincrement = true)
    private Long id;
    @Index
    private String bookId;// 书本ID
    private String name;// 书本名称
    private String cover_img;// 封面图片

    private String year;// 年份
    private String version;// 书本版本（如人教本）
    private String period;// 学校阶段
    private String part_type;// 上下册
    private String grade;// 年级
    private String subject;// 科目
    private String press;// 出版社

    private String browserTime;//浏览日期
    private long saveTime;//保存时间
    private int lastPage;//上次浏览的页数
    @Transient
    private boolean isShow = true;
    @Transient
    private boolean isSelect;
    @Transient
    private boolean isShowIcon;//是否显示选择图标


    @Generated(hash = 1630058935)
    public BrowserInfo(Long id, String bookId, String name, String cover_img, String year,
                       String version, String period, String part_type, String grade, String subject,
                       String press, String browserTime, long saveTime, int lastPage) {
        this.id = id;
        this.bookId = bookId;
        this.name = name;
        this.cover_img = cover_img;
        this.year = year;
        this.version = version;
        this.period = period;
        this.part_type = part_type;
        this.grade = grade;
        this.subject = subject;
        this.press = press;
        this.browserTime = browserTime;
        this.saveTime = saveTime;
        this.lastPage = lastPage;
    }

    @Generated(hash = 972459462)
    public BrowserInfo() {
    }


    public Long getId() {
        return id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPart_type() {
        return part_type;
    }

    public void setPart_type(String part_type) {
        this.part_type = part_type;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getBrowserTime() {
        return browserTime;
    }

    public void setBrowserTime(String browserTime) {
        this.browserTime = browserTime;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isShowIcon() {
        return isShowIcon;
    }

    public void setShowIcon(boolean showIcon) {
        isShowIcon = showIcon;
    }
}
