package com.fwtai.config;

/**
 * 保存高并发环境下的用户token信息
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-04-09 16:36
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class AreaLevel{

    private final static ThreadLocal<Integer> localToken = new ThreadLocal<Integer>();

    //在拦截器获取并保存token,在需要service或dao层或其他地方调用解析即可
    public static Integer get(){
        return localToken.get();
    }

    public static void set(final Integer value){
        localToken.set(value);
    }

    public static void remove(){
        localToken.remove();
    }
}