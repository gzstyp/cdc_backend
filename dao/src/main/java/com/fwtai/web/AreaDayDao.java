package com.fwtai.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Repository
public class AreaDayDao{

    @Resource
    private DaoHandle dao;

    public List<String> getAreaData(final PageFormData formData){
        return dao.queryListString("daily_manager.getAreaData",formData);
    }

    public List<HashMap<String,Object>> getAllType(final PageFormData formData){
        return dao.queryForListHashMap("reportTotal.getAllType",formData);
    }

    /*常规日报查询-页面显示|导出按钮*/
    public List<HashMap<String,Object>> getAreaDaily(final PageFormData formData){
        return dao.queryForListHashMap("daily_manager.getAreaDaily",formData);
    }


    /**查询登录者所拥有的权限*/
    public List<String> queryPermissions(){
        return dao.queryPermissions("areaDay/queryAreaSelect");
    }
}