package com.chenggong.modelsearch.bean;

/**
 * Created by chenggong on 18-3-28.
 * 搜索的请求参数
 * json 格式
 * {"type":"0","fileName":"__","encoded":"__","pagesNum":"__","displayNum":"__","hashcode":"__"}
 */

public class SearchReqBean {
    private String type;        // 有四个类型，在configure中配置
    private String fileName;    //搜索名称
    private String encoded;     //编码，图像或者模型base64编码
    private String pagesNum;    //页码
    private String displayNum;  //每页展示数目
    private String hashcode;    //hashcode

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getEncoded() {
        return encoded;
    }

    public void setEncoded(String encoded) {
        this.encoded = encoded;
    }

    public String getPagesNum() {
        return pagesNum;
    }

    public void setPagesNum(String pagesNum) {
        this.pagesNum = pagesNum;
    }

    public String getDisplayNum() {
        return displayNum;
    }

    public void setDisplayNum(String displayNum) {
        this.displayNum = displayNum;
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
    }

    public SearchReqBean() {
    }

    public SearchReqBean(String type, String fileName) {
        this(type, fileName, "1", "", "", "15");
    }
    //名称搜索
    public SearchReqBean(String type, String fileName, String pagesNum) {
        this(type, fileName, pagesNum, "", "", "15");
    }
    //图片搜索
    public SearchReqBean(String type, String fileName, String pagesNum,String encoded) {
        this(type, fileName, pagesNum, encoded, "", "15");
    }
    //用于构造reload
    public SearchReqBean(String type, String fileName, String pagesNum, String encoded, String hashcode ) {
        this(type, fileName, pagesNum, encoded, hashcode, "15");
    }

    public SearchReqBean(String type, String fileName, String pagesNum, String encoded, String hashcode,String displayNum ) {
        this.type = type;
        this.fileName = fileName;
        this.encoded = encoded;
        this.pagesNum = pagesNum;
        this.displayNum = displayNum;
        this.hashcode = hashcode;
    }
}
