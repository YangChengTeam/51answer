package com.yc.ac.index.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.qq.e.ads.nativ.NativeExpressADView;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

import java.util.ArrayList;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wanglin private String on 2018/3/8 09;//01.
 */
@Entity
public class BookInfo implements Parcelable, MultiItemEntity {

    @Id
    private long id;
    @Index
    @JSONField(name = "id")
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
    private String code;// 唯一码
    private int is_del;// 是否已删除。（0否1是）
    private String sort;// 排序
    private String share_num;// 分享次数
    private String pv_num;// 浏览次数

    private int access;// 用户是否private String有权访问 0;// 无授权；1;// 有授权
    private int favorite;// 用户private String是否收藏 0;// 未收藏；1;// 已收藏
    @Transient
    private List<VersionDetailInfo> flag;// [
    @JSONField(name = "check")
    private int state;//审核状态 1 拒绝 0 通过

    private String share_content;// 分享内容

    private String author;// 作者
    private String time;// 日期 2018-03-07
    @Transient
    private List<String> answer_list;

    private String cover_id;//临时书本id
    private String answer_img;
    private long saveTime;
    @Transient
    public static final int ADV = 1;
    @Transient
    public static final int CONTENT = 2;
    @Transient
    private int itemType = CONTENT;

    @Transient
    private NativeExpressADView view;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIs_del() {
        return is_del;
    }

    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getShare_num() {
        return share_num;
    }

    public void setShare_num(String share_num) {
        this.share_num = share_num;
    }

    public String getPv_num() {
        return pv_num;
    }

    public void setPv_num(String pv_num) {
        this.pv_num = pv_num;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public List<VersionDetailInfo> getFlag() {
        return flag;
    }

    public void setFlag(List<VersionDetailInfo> flag) {
        this.flag = flag;
    }

    public String getShare_content() {
        return share_content;
    }

    public void setShare_content(String share_content) {
        this.share_content = share_content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getAnswer_list() {
        return answer_list;
    }

    public void setAnswer_list(List<String> answer_list) {
        this.answer_list = answer_list;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getCover_id() {
        return cover_id;
    }

    public void setCover_id(String cover_id) {
        this.cover_id = cover_id;
    }

    public String getAnswer_img() {
        return answer_img;
    }

    public void setAnswer_img(String answer_img) {
        this.answer_img = answer_img;
    }


    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bookId);
        dest.writeString(this.name);
        dest.writeString(this.cover_img);
        dest.writeString(this.year);
        dest.writeString(this.version);
        dest.writeString(this.period);
        dest.writeString(this.part_type);
        dest.writeString(this.grade);
        dest.writeString(this.subject);
        dest.writeString(this.press);
        dest.writeString(this.code);
        dest.writeInt(this.is_del);
        dest.writeString(this.sort);
        dest.writeString(this.share_num);
        dest.writeString(this.pv_num);
        dest.writeInt(this.access);
        dest.writeInt(this.favorite);
        dest.writeList(this.flag);
        dest.writeInt(this.state);
        dest.writeString(this.share_content);
        dest.writeString(this.author);
        dest.writeString(this.time);
        dest.writeStringList(this.answer_list);
        dest.writeString(this.cover_id);
        dest.writeString(this.answer_img);
    }

    public BookInfo() {
    }

    protected BookInfo(Parcel in) {
        this.bookId = in.readString();
        this.name = in.readString();
        this.cover_img = in.readString();
        this.year = in.readString();
        this.version = in.readString();
        this.period = in.readString();
        this.part_type = in.readString();
        this.grade = in.readString();
        this.subject = in.readString();
        this.press = in.readString();
        this.code = in.readString();
        this.is_del = in.readInt();
        this.sort = in.readString();
        this.share_num = in.readString();
        this.pv_num = in.readString();
        this.access = in.readInt();
        this.favorite = in.readInt();
        this.flag = new ArrayList<>();
        in.readList(this.flag, VersionDetailInfo.class.getClassLoader());
        this.state = in.readInt();
        this.share_content = in.readString();
        this.author = in.readString();
        this.time = in.readString();
        this.answer_list = in.createStringArrayList();
        this.cover_id = in.readString();
        this.answer_img = in.readString();
    }

    @Generated(hash = 329383499)
    public BookInfo(long id, String bookId, String name, String cover_img, String year, String version,
                    String period, String part_type, String grade, String subject, String press, String code, int is_del,
                    String sort, String share_num, String pv_num, int access, int favorite, int state,
                    String share_content, String author, String time, String cover_id, String answer_img, long saveTime) {
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
        this.code = code;
        this.is_del = is_del;
        this.sort = sort;
        this.share_num = share_num;
        this.pv_num = pv_num;
        this.access = access;
        this.favorite = favorite;
        this.state = state;
        this.share_content = share_content;
        this.author = author;
        this.time = time;
        this.cover_id = cover_id;
        this.answer_img = answer_img;
        this.saveTime = saveTime;
    }

    public static final Parcelable.Creator<BookInfo> CREATOR = new Parcelable.Creator<BookInfo>() {
        @Override
        public BookInfo createFromParcel(Parcel source) {
            return new BookInfo(source);
        }

        @Override
        public BookInfo[] newArray(int size) {
            return new BookInfo[size];
        }
    };

    @Override
    public String toString() {
        return "BookInfo{" +
                "bookId='" + bookId + '\'' +
                ", name='" + name + '\'' +
                ", cover_img='" + cover_img + '\'' +
                ", year='" + year + '\'' +
                ", version='" + version + '\'' +
                ", period='" + period + '\'' +
                ", part_type='" + part_type + '\'' +
                ", grade='" + grade + '\'' +
                ", subject='" + subject + '\'' +
                ", press='" + press + '\'' +
                ", code='" + code + '\'' +
                ", is_del=" + is_del +
                ", sort='" + sort + '\'' +
                ", share_num='" + share_num + '\'' +
                ", pv_num='" + pv_num + '\'' +
                ", access=" + access +
                ", favorite=" + favorite +
                ", flag=" + flag +
                ", state=" + state +
                ", share_content='" + share_content + '\'' +
                ", author='" + author + '\'' +
                ", time='" + time + '\'' +
                ", answer_list=" + answer_list +
                ", cover_id='" + cover_id + '\'' +
                ", answer_img='" + answer_img + '\'' +
                '}';
    }


    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public NativeExpressADView getView() {
        return view;
    }

    public void setView(NativeExpressADView view) {
        this.view = view;
    }

    @Override
    public int getItemType() {
        return itemType;
    }
}
