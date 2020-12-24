package com.fwtai.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Repository
public class ReportTotalDao{

    @Resource
    private DaoHandle dao;

    public List<HashMap<String,Object>> getView(final PageFormData formData){
        return dao.queryForListHashMap("reportTotal.getView",formData);
    }
}