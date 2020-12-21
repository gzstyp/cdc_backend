package com.fwtai.api;

import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;

/**
 * app版本更新
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-12-21 10:24
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Repository
public class ApiAppVersionDao{

    @Resource
    private DaoHandle dao;

    public HashMap<String,Object> getVersion(){
        return dao.queryForHashMap("api_sync_data.getVersion");
    }
}