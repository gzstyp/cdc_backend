package com.fwtai.exception;

import com.alibaba.fastjson.JSONObject;
import com.fwtai.config.ConfigFile;

/**
 * 自定义通用的异常信息
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021-02-25 10:23
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
public final class HandleException extends RuntimeException{

    public HandleException(){
        super();
    }

    /**
     * 自定义通用的异常信息
     * @param msg
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/3/1 16:32
    */
    public HandleException(final String msg){
        super(buildJson(msg));
    }

    protected static String buildJson(final String msg){
        final JSONObject json = new JSONObject(2);
        json.put(ConfigFile.code,ConfigFile.code199);
        json.put(ConfigFile.msg,msg);
        return json.toJSONString();
    }
}