package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import com.fwtai.web.EnvironmentDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 环境监测业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-19 19:52:11
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class EnvironmentService{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private EnvironmentDao environmentDao;

    public String add(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_sample_code = "sample_code";
        final String p_site_type = "site_type";
        final String p_market_name = "market_name";
        final String p_vendor_name = "vendor_name";
        final String p_vendor_code = "vendor_code";
        final String p_source = "source";
        final String p_entrance = "entrance";
        final String p_sample_name = "sample_name";
        final String p_freeze_related = "freeze_related";
        final String p_sample_type = "sample_type";
        final String p_sampling_date = "sampling_date";
        final String p_detection_date = "detection_date";
        final String p_result = "result";
        final String validate = ToolClient.validateField(formData,p_sample_code,p_site_type,p_market_name,p_vendor_name,p_vendor_code,p_source,p_entrance,p_sample_name,p_freeze_related,p_sample_type,p_sampling_date,p_detection_date,p_result);
        if(validate != null)return validate;
        final String fieldInteger = ToolClient.validateInteger(formData,p_entrance,p_freeze_related,p_result);
        if(fieldInteger != null)return fieldInteger;
        formData.put("kid",ToolString.getIdsChar32());
        return ToolClient.executeRows(environmentDao.add(formData));
    }

    public String edit(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_kid = "kid";
        final String p_sample_code = "sample_code";
        final String p_site_type = "site_type";
        final String p_market_name = "market_name";
        final String p_vendor_name = "vendor_name";
        final String p_vendor_code = "vendor_code";
        final String p_source = "source";
        final String p_entrance = "entrance";
        final String p_sample_name = "sample_name";
        final String p_freeze_related = "freeze_related";
        final String p_sample_type = "sample_type";
        final String p_sampling_date = "sampling_date";
        final String p_detection_date = "detection_date";
        final String p_result = "result";
        final String validate = ToolClient.validateField(formData,p_sample_code,p_site_type,p_market_name,p_vendor_name,p_vendor_code,p_source,p_entrance,p_sample_name,p_freeze_related,p_sample_type,p_sampling_date,p_detection_date,p_result,p_kid);
        if(validate != null)return validate;
        final String fieldInteger = ToolClient.validateInteger(formData,p_entrance,p_freeze_related,p_result);
        if(fieldInteger != null)return fieldInteger;
        final String exist_key = environmentDao.queryExistById(formData.getString(p_kid));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        return ToolClient.executeRows(environmentDao.edit(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(environmentDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = environmentDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(environmentDao.delById(kid));
    }

    public String delByKeys(final PageFormData formData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final ArrayList<String> lists = ToolString.keysToList(ids);
        return ToolClient.executeRows(environmentDao.delByKeys(lists),"操作成功","数据已不存在,刷新重试");
    }

    public String listData(PageFormData formData){
        final String p_iColumns = "iColumns";
        final String validate = ToolClient.validateField(formData,p_iColumns);
        if(validate != null)return validate;
        final String fieldInteger = ToolClient.validateInteger(formData,p_iColumns);
        if(fieldInteger != null)return fieldInteger;
        try {
            formData = ToolClient.dataTableMysql(formData);
            if(formData == null)return ToolClient.jsonValidateField();
            final HashMap<String,Object> map = environmentDao.listData(formData);
            return ToolClient.dataTableOK((List<Object>)map.get(ConfigFile.rows),map.get(ConfigFile.total),formData.get("sEcho"));
        } catch (Exception e){
            return ToolClient.dataTableException(formData.get("sEcho"));
        }
    }
}