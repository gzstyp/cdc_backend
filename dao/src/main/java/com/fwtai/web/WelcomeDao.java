package com.fwtai.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;

@Repository
public class WelcomeDao{

    @Resource
    private DaoHandle dao;

    public HashMap<String, Object> getEnvironment(final PageFormData formData){
        return dao.queryForHashMap("welcome.getEnvironment",formData);
    }

    public HashMap<String, Object> getEmployee(final PageFormData formData){
        return dao.queryForHashMap("welcome.getEmployee",formData);
    }
}