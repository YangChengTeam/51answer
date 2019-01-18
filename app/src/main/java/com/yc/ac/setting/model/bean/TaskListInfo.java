package com.yc.ac.setting.model.bean;

/**
 * Created by wanglin  on 2018/9/13 17:43.
 */
public class TaskListInfo {

    /**
     * is_done : 0
     * task_id : 2
     * name : 上传任务
     * desp : 上传新书得Q币
     * src : newusertask_1.jpg
     */

    private int is_done;
    private int task_id;
    private String name;
    private String desp;
    private String src;

    public int getIs_done() {
        return is_done;
    }

    public void setIs_done(int is_done) {
        this.is_done = is_done;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

}
