package com.chenggong.modelsearch.utils;

/**
 * Created by chenggong on 18-3-28.
 */

public final class Configure {

    /**
     * 四种type数据，分别是名字，图片，三维模型，图片重新加载，模型重新加载
     */
    public final static String NAME_TYPE = "0";
    public final static String IMAGE_TYPE = "1";
    public final static String OBJECT_TYPE= "2";
    public final static String IMAGE_Reload_TYPE = "3";
    public final static String OBJECT_Reload_TYPE = "4";

    public final static String NAME_UPLOAD_URL = "http://39.106.1.76:8080/hawkeyeweb/nameUpload.model";

    public final static String IAMGE_UPLOAD_URL = "http://39.106.1.76:8080/hawkeyeweb/imgUpload.model";
    public final static String IAMGE_RELOAD_URL = "http://39.106.1.76:8080/hawkeyeweb/imgReload.model";

    public final static String OBJECT_UPLOAD_URL = "http://39.106.1.76:8080/hawkeyeweb/objUpload.model";
    public final static String ObJECT_RELOAD_URL = "http://39.106.1.76:8080/hawkeyeweb/objReload.model";

}
