package com.fwtai.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public HashMap<String, Object> getCrowdTotal(final PageFormData formData){
        return dao.queryForHashMap("welcome.getCrowdTotal",formData);
    }

    public List<Map<String, Object>> typeSample7Day(final PageFormData formData){
        return dao.queryForListMap("welcome.typeSample7Day",formData);
    }
}