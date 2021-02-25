package com.fwtai.bean;

import java.time.LocalDate;

/**
 * 身份信息
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2019/12/10 10:28
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class IdentityCard{

    private String ID;
    private Sex sex;
    private LocalDate birthday;
    private long age;
    private String areaCode;

    public String getID(){
        return ID;
    }

    public void setID(String ID){
        this.ID = ID;
    }

    public Sex getSex(){
        return sex;
    }

    public void setSex(Sex sex){
        this.sex = sex;
    }

    public LocalDate getBirthday(){
        return birthday;
    }

    public void setBirthday(LocalDate birthday){
        this.birthday = birthday;
    }

    public long getAge(){
        return age;
    }

    public void setAge(long age){
        this.age = age;
    }

    public String getAreaCode(){
        return areaCode;
    }

    public void setAreaCode(String areaCode){
        this.areaCode = areaCode;
    }

    public enum Sex{
        MALE(1, "男"), FEMALE(2, "女");
        private int code;
        private String value;
        Sex(int code, String value) {
            this.code = code;
            this.value = value;
        }

        public int getCode(){
            return code;
        }

        public void setCode(int code){
            this.code = code;
        }

        public String getValue(){
            return value;
        }

        public void setValue(String value){
            this.value = value;
        }
    }
}