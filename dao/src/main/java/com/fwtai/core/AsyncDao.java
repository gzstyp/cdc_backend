package com.fwtai.core;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

@Repository
public class AsyncDao{

    @Resource
    private DaoHandle dao;

    public void updateSucceed(final String username){
        dao.execute("sys_user.updateSucceedTime",username);/*最后登录时间*/
        dao.execute("sys_user.updateTimes",username);/*更新登录次数*/
        dao.execute("sys_user.updateErrorTime",username);/*登录成功把时间设置为当前默认时间*/
        dao.execute("sys_user.updateErrorCount",username);/*登录成功把登录错误次数更改为0*/
    }

    //更新用户区域绑定
    public void updateUserArea(final PageFormData formData){
        dao.execute("sys_user.updateOrEditUserArea",formData);
    }
}