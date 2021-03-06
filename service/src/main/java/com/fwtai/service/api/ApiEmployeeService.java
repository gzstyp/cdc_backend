package com.fwtai.service.api;

import com.fwtai.api.ApiEmployeeDao;
import com.fwtai.bean.IdentityCard;
import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.config.LocalUserId;
import com.fwtai.entity.EmployeeBean;
import com.fwtai.entity.PublishBean;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolIdCard;
import com.fwtai.tool.ToolString;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 从业人员业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-19 14:48:44
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class ApiEmployeeService{

    @Resource
    private ApiEmployeeDao apiEmployeeDao;

    public String add(final EmployeeBean employeeBean){
        final PageFormData formData = ToolClient.beanToPageFormData(employeeBean);
        final String p_sample_code = "sample_code";
        final String p_area_id = "area_id";
        final String p_appid = "appid";
        final String p_province_id = "province_id";
        final String p_city_id = "city_id";
        final String p_county_id = "county_id";
        final String p_area_level = "area_level";
        final String p_real_name = "real_name";
        final String p_id_card = "id_card";
        final String p_work_site = "work_site";
        final String p_work_type = "work_type";
        final String p_profession = "profession";
        final String p_cold_chain = "cold_chain";
        final String p_sample_type = "sample_type";
        final String p_detection_date = "detection_date";
        final String p_result = "result";
        final String validate = ToolClient.validateField(formData,p_sample_code,p_area_id,p_appid,p_province_id,p_city_id,p_county_id,p_area_level,p_real_name,p_work_site,p_work_type,p_profession,p_cold_chain,p_sample_type,p_result);
        if(validate != null)return validate;
        final String validateInteger = ToolClient.validateInteger(formData,p_area_level,p_cold_chain,p_result);
        if(validateInteger != null)return validateInteger;
        String detection_date = formData.getString(p_detection_date);
        if(detection_date == null){
            detection_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            formData.put("detection_date",detection_date);
        }else{
            final boolean bl = ToolString.checkDate(detection_date);
            if(!bl){
                return ToolClient.createJsonFail("检测日期格式不对");
            }
        }
        final String idCard = formData.getString(p_id_card);
        if(idCard == null){
            return ToolClient.createJsonFail("请输入身份证号");
        }
        final IdentityCard identityCard = ToolIdCard.extractIdInfo(idCard);
        formData.put("gender",identityCard.getSex().getCode());
        formData.put("age",identityCard.getAge());
        final String userId = LocalUserId.get();
        final String kid = ToolString.getIdsChar32();
        formData.put("kid",kid);
        formData.put("craete_userid",userId);
        formData.put("audit_user",userId);
        formData.put("modify_userid",userId);
        final int rows = apiEmployeeDao.add(formData);
        if(rows > 0){
            final HashMap<String,Object> result = new HashMap<>(3);
            result.put("kid",kid);
            result.put("appid",formData.get(p_appid));
            result.put("rows",rows);
            return ToolClient.queryJson(result);
        }else{
            return ToolClient.createJsonFail("操作失败");
        }
    }

    public String edit(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_kid = "kid";
        final String p_cold_chain = "cold_chain";
        final String p_detection_date = "detection_date";
        final String p_id_card = "id_card";
        final String p_result = "result";
        final String validate = ToolClient.validateField(formData,p_cold_chain,p_result,p_kid);
        if(validate != null)return validate;
        final String validateInteger = ToolClient.validateInteger(formData,p_cold_chain,p_result);
        if(validateInteger != null)return validateInteger;
        final String exist_key = apiEmployeeDao.queryExistById(formData.getString(p_kid));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        String detection_date = formData.getString(p_detection_date);
        if(detection_date == null){
            detection_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            formData.put("detection_date",detection_date);
        }else{
            final int length = detection_date.length();
            if(length != 10){
                return ToolClient.createJsonFail("检测日期格式不对");
            }
            final boolean bl = ToolString.checkDate(detection_date);
            if(!bl){
                return ToolClient.createJsonFail("检测日期格式不对");
            }
        }
        final String idCard = formData.getString(p_id_card);
        if(idCard !=null){
            final IdentityCard identityCard = ToolIdCard.extractIdInfo(idCard);
            formData.put("gender",identityCard.getSex().getCode());
            formData.put("age",identityCard.getAge());
        }
        final String userId = LocalUserId.get();
        formData.put("modify_userid",userId);
        return ToolClient.executeRows(apiEmployeeDao.edit(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(apiEmployeeDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String p_userId = "userId";
        final String validate = ToolClient.validateField(formData,p_kid,p_userId);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = apiEmployeeDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(apiEmployeeDao.delById(formData));
    }

    public String delByKeys(final PageFormData formData){
        final String p_ids = "ids";
        final String p_userId = "userId";
        final String validate = ToolClient.validateField(formData,p_ids,p_userId);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final ArrayList<String> lists = ToolString.keysToList(ids);
        final HashMap<String,Object> map = new HashMap<>(2);
        map.put("lists",lists);
        map.put("userId",formData.get(p_userId));
        final int rows = apiEmployeeDao.delByKeys(map);
        final String msg = (rows == lists.size()) ? "操作成功" : "成功"+rows+"条数,失败"+(lists.size()-rows)+"条数";
        return ToolClient.executeRows(rows,msg,"操作失败,刷新重试");
    }

    public String editPublish(final ArrayList<PublishBean> lists){
        final int rows = apiEmployeeDao.editPublish(lists);
        if(rows > 0){
            return ToolClient.createJsonSuccess("操作成功");
        }else{
            return ToolClient.createJsonFail("操作失败");
        }
    }

    //适用于api接口
    public String listDataPage(final HttpServletRequest request){
        final PageFormData formData = ToolClient.pageParamsApi(request);
        if(formData == null) return ToolClient.jsonValidateField();
        final HashMap<String,Object> map = apiEmployeeDao.listData(formData);
        return ToolClient.jsonPage((List<?>) map.get(ConfigFile.rows),(Integer) map.get(ConfigFile.total));
    }

    //获取指定采样日期获取全部数据
    public String listAllData(final HttpServletRequest request){
        final PageFormData formData = ToolClient.getFormData(request);
        final String p_sampling_date = "sampling_date";
        final String validate = ToolClient.validateField(formData,p_sampling_date);
        if(validate != null)return validate;
        formData.put("craete_userid",LocalUserId.get());
        return ToolClient.queryJson(apiEmployeeDao.listAllData(formData));
    }

    public String updateEmployeeAudit(final PageFormData formData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final String userId = LocalUserId.get();
        final ArrayList<String> lists = ToolString.keysToList(ids);
        final HashMap<String,Object> map = new HashMap<>(2);
        map.put("audit_user",userId);
        map.put("listIds",lists);
        final int rows = apiEmployeeDao.updateEmployeeAudit(map);
        final String msg = (rows == lists.size()) ? "操作成功" : "成功"+rows+"条数,重复"+(lists.size()-rows)+"条数";
        return ToolClient.executeRows(rows,msg,"操作失败,已重复上报");
    }
}