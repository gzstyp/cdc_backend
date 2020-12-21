package com.fwtai.service.api;

import com.fwtai.api.ApiAppVersionDao;
import com.fwtai.tool.ToolClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * app版本更新
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-12-21 10:13
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@Service
public class ApiAppVersionService{

    @Resource
    private ApiAppVersionDao apiAppVersionDao;

    /*获取最新的版本号*/
    public String getVersion(){
        return ToolClient.queryJson(apiAppVersionDao.getVersion());
    }
}