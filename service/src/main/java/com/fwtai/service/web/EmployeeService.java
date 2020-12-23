package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import com.fwtai.web.EmployeeDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 从业人员业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-23 10:37:06
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class EmployeeService{

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private EmployeeDao employeeDao;

    public String add(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_age = "age";
        final String p_appid = "appid";
        final String p_area_id = "area_id";
        final String p_area_level = "area_level";
        final String p_city_id = "city_id";
        final String p_cold_chain = "cold_chain";
        final String p_county_id = "county_id";
        final String p_create_date = "create_date";
        final String p_detection_date = "detection_date";
        final String p_freeze_type = "freeze_type";
        final String p_gender = "gender";
        final String p_modify_date = "modify_date";
        final String p_phone = "phone";
        final String p_province_id = "province_id";
        final String p_profession = "profession";
        final String p_real_name = "real_name";
        final String p_result = "result";
        final String p_sample_code = "sample_code";
        final String p_sampling_date = "sampling_date";
        final String p_sample_type = "sample_type";
        final String p_work_site = "work_site";
        final String p_work_type = "work_type";
        final String validate = ToolClient.validateField(formData,p_age,p_appid,p_area_id,p_area_level,p_city_id,p_cold_chain,p_county_id,p_create_date,p_detection_date,p_freeze_type,p_gender,p_modify_date,p_phone,p_province_id,p_profession,p_real_name,p_result,p_sample_code,p_sampling_date,p_sample_type,p_work_site,p_work_type);
        if(validate != null)return validate;
        formData.put("kid",ToolString.getIdsChar32());
        return ToolClient.executeRows(employeeDao.add(formData));
    }

    public String edit(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_kid = "kid";
        final String p_age = "age";
        final String p_appid = "appid";
        final String p_area_id = "area_id";
        final String p_area_level = "area_level";
        final String p_city_id = "city_id";
        final String p_cold_chain = "cold_chain";
        final String p_county_id = "county_id";
        final String p_create_date = "create_date";
        final String p_detection_date = "detection_date";
        final String p_freeze_type = "freeze_type";
        final String p_gender = "gender";
        final String p_modify_date = "modify_date";
        final String p_phone = "phone";
        final String p_province_id = "province_id";
        final String p_profession = "profession";
        final String p_real_name = "real_name";
        final String p_result = "result";
        final String p_sample_code = "sample_code";
        final String p_sampling_date = "sampling_date";
        final String p_sample_type = "sample_type";
        final String p_work_site = "work_site";
        final String p_work_type = "work_type";
        final String validate = ToolClient.validateField(formData,p_age,p_appid,p_area_id,p_area_level,p_city_id,p_cold_chain,p_county_id,p_create_date,p_detection_date,p_freeze_type,p_gender,p_modify_date,p_phone,p_province_id,p_profession,p_real_name,p_result,p_sample_code,p_sampling_date,p_sample_type,p_work_site,p_work_type,p_kid);
        if(validate != null)return validate;
        final String exist_key = employeeDao.queryExistById(formData.getString(p_kid));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        return ToolClient.executeRows(employeeDao.edit(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(employeeDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = employeeDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(employeeDao.delById(kid));
    }

    public String delByKeys(final PageFormData formData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final ArrayList<String> lists = ToolString.keysToList(ids);
        return ToolClient.executeRows(employeeDao.delByKeys(lists),"操作成功","数据已不存在,刷新重试");
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
            final HashMap<String,Object> map = employeeDao.listData(formData);
            return ToolClient.dataTableOK((List<Object>)map.get(ConfigFile.rows),map.get(ConfigFile.total),formData.get("sEcho"));
        } catch (Exception e){
            return ToolClient.dataTableException(formData.get("sEcho"));
        }
    }
}