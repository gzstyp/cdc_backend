package com.fwtai.api;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

/**
 * 基础数据同步
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-12-16 19:00
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
 */
@Repository
public class ApiSyncDataDao{

    @Resource
    private DaoHandle dao;

    /**获取人群分类基础数据*/
    public List<HashMap<String,Object>> getCrowdCategory(final PageFormData formData){
        return dao.queryForListHashMap("api_sync_data.getCrowdCategory",formData);
    }

    /** 获取人群类型 */
    public List<HashMap<String,Object>> getCrowdType(final PageFormData formData){
        return dao.queryForListHashMap("api_sync_data.getCrowdType",formData);
    }

    /**获取经营场所数据*/
    public List<HashMap<String,Object>> getManagerArea(final PageFormData formData){
        return dao.queryForListHashMap("api_sync_data.getManagerArea",formData);
    }
}