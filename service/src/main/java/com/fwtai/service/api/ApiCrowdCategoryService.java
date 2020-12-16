package com.fwtai.service.api;

import com.fwtai.api.ApiCrowdCategoryDao;
import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * 人群分类业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-15 13:50:30
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class ApiCrowdCategoryService{

    @Resource
    private ApiCrowdCategoryDao apiCrowdCategoryDao;

    public String listData(final HttpServletRequest request){
        final PageFormData formData = ToolClient.pageParamsApi(request);
        if(formData == null) return ToolClient.jsonValidateField();
        final HashMap<String,Object> map = apiCrowdCategoryDao.listData(formData);
        return ToolClient.jsonPage((List<?>) map.get(ConfigFile.rows),(Integer) map.get(ConfigFile.total));
    }

    public String getList(){
        return ToolClient.queryJson(apiCrowdCategoryDao.getList());
    }
}