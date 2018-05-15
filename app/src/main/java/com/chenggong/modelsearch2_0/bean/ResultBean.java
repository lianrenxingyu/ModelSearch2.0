package com.chenggong.modelsearch2_0.bean;

import java.util.List;

/**
 * Created by chenggong on 18-5-13.
 *
 * @author chenggong
 */

public class ResultBean {
    private String count;
    private List<String> objs;
    private List<String> pics;

    public ResultBean(String count, List<String> objs, List<String> pics) {

        this.count = count;
        this.objs = objs;
        this.pics = pics;
    }

    public ResultBean() {
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public List<String> getObjs() {
        return objs;
    }

    public void setObjs(List<String> objs) {
        this.objs = objs;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }


}
