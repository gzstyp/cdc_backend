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

    /*常规日报查询*/
    public List<HashMap<String,Object>> getView(final PageFormData formData){
        return dao.queryForListHashMap("reportTotal.getView",formData);
    }

    public List<HashMap<String,Object>> queryDataExport(final PageFormData formData){
        return dao.queryForListHashMap("reportTotal.queryDataExport",formData);
    }

    /**查询登录者所拥有的权限*/
    public List<String> queryPermissions(){
        return dao.queryPermissions("reportTotal/queryAreaSelect");//指定当前子菜单url[当然也可以指定controller层的自己url]
    }
}