package com.fwtai.service.api;

import com.fwtai.api.ApiEnvironmentDao;
import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.config.LocalUserId;
import com.fwtai.entity.EnvironmentBean;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
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
 * @创建日期 2020-12-18 17:32:36
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class ApiEnvironmentService{

    @Resource
    private ApiEnvironmentDao apiEnvironmentDao;

    public String add(final EnvironmentBean environmentBean){
        final PageFormData formData = ToolClient.beanToPageFormData(environmentBean);
        final String p_sample_code = "sample_code";
        final String p_province_id = "province_id";
        final String p_city_id = "city_id";
        final String p_county_id = "county_id";
        final String p_area_id = "area_id";
        final String p_area_level = "area_level";
        final String p_site_type = "site_type";
        final String p_market_name = "market_name";
        final String p_vendor_name = "vendor_name";
        final String p_phone = "phone";
        final String p_entrance = "entrance";
        final String p_sample_name = "sample_name";
        final String p_freeze_related = "freeze_related";
        final String p_sample_type = "sample_type";
        final String p_sampling_date = "sampling_date";
        final String p_result = "result";
        final String validate = ToolClient.validateField(formData,p_sample_code,p_province_id,p_city_id,p_county_id,p_area_id,p_area_level,p_site_type,p_market_name,p_vendor_name,p_phone,p_entrance,p_sample_name,p_freeze_related,p_sample_type,p_sampling_date,p_result);
        if(validate != null)return validate;
        final String validateInteger = ToolClient.validateInteger(formData,p_entrance,p_freeze_related,p_result);
        if(validateInteger != null)return validateInteger;
        final boolean b = ToolString.checkDate(formData.getString(p_sampling_date));
        if(!b){
            return ToolClient.createJsonFail("日期格式不对");
        }
        final String userId = LocalUserId.get();
        final String kid = ToolString.getIdsChar32();
        formData.put("kid",kid);
        formData.put("audit_user",userId);
        formData.put("craete_userid",userId);
        formData.put("modify_userid",userId);
        final int rows = apiEnvironmentDao.add(formData);
        if(rows > 0){
            final HashMap<String,Object> result = new HashMap<>();
            result.put("kid",kid);
            result.put("rows",rows);
            return ToolClient.queryJson(result);
        }else{
            return ToolClient.createJsonFail("操作失败");
        }
    }

    public String edit(final EnvironmentBean environmentBean){
        final PageFormData formData = ToolClient.beanToPageFormData(environmentBean);
        final String p_kid = "kid";
        final String p_sample_code = "sample_code";
        final String p_site_type = "site_type";
        final String p_market_name = "market_name";
        final String p_vendor_name = "vendor_name";
        final String p_phone = "phone";
        final String p_entrance = "entrance";
        final String p_sample_name = "sample_name";
        final String p_freeze_related = "freeze_related";
        final String p_sample_type = "sample_type";
        final String p_sampling_date = "sampling_date";
        final String p_result = "result";
        final String validate = ToolClient.validateField(formData,p_sample_code,p_site_type,p_market_name,p_vendor_name,p_phone,p_entrance,p_sample_name,p_freeze_related,p_sample_type,p_sampling_date,p_result,p_kid);
        if(validate != null)return validate;
        final String validateInteger = ToolClient.validateInteger(formData,p_entrance,p_freeze_related,p_result);
        if(validateInteger != null)return validateInteger;
        final boolean b = ToolString.checkDate(formData.getString(p_sampling_date));
        if(!b){
            return ToolClient.createJsonFail("日期格式不对");
        }
        final String exist_key = apiEnvironmentDao.queryExistById(formData.getString(p_kid));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        final String userId = LocalUserId.get();
        formData.put("audit_user",userId);
        return ToolClient.executeRows(apiEnvironmentDao.edit(formData));
    }

    public String updateAudit(final String ids){
        return null;
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(apiEnvironmentDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = apiEnvironmentDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(apiEnvironmentDao.delById(kid));
    }

    public String delByKeys(final PageFormData formData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final ArrayList<String> lists = ToolString.keysToList(ids);
        return ToolClient.executeRows(apiEnvironmentDao.delByKeys(lists),"操作成功","数据已不存在,刷新重试");
    }

    //适用于api接口
    public String listDataPage(final HttpServletRequest request){
        final PageFormData formData = ToolClient.pageParamsApi(request);
        if(formData == null) return ToolClient.jsonValidateField();
        final HashMap<String,Object> map = apiEnvironmentDao.listData(formData);
        return ToolClient.jsonPage((List<?>) map.get(ConfigFile.rows),(Integer) map.get(ConfigFile.total));
    }

    public String updateBatchAudit(final PageFormData formData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final String userId = LocalUserId.get();
        final ArrayList<String> lists = ToolString.keysToList(ids);
        final HashMap<String,Object> map = new HashMap<>(2);
        map.put("audit_user",userId);
        map.put("listIds",lists);
        final int rows = apiEnvironmentDao.updateBatchAudit(map);
        final String msg = (rows == lists.size()) ? "操作成功" : "操作成功"+rows+"条数,失败"+(lists.size()-rows)+"条数";
        return ToolClient.executeRows(rows,msg,"数据已不存在,刷新重试");
    }
}