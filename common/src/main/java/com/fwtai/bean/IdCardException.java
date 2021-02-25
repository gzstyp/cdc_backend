package com.fwtai.bean;

/**
 * 身份证号码验证异常
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021-02-25 10:23
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
 */
public final class IdCardException extends RuntimeException{

    public IdCardException(){
        super();
    }

    public IdCardException(String message){
        super(message);
    }
}