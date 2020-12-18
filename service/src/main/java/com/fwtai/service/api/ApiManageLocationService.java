package com.fwtai.service.api;

import com.fwtai.api.ApiManageLocationDao;
import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.config.LocalUserId;
import com.fwtai.entity.ManagerLocation;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;

/**
 * 经营场所业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-16 17:51:01
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class ApiManageLocationService{

    @Resource
    private ApiManageLocationDao apiManageLocationDao;

    public String add(final ManagerLocation manager){
        final PageFormData formData = ToolClient.beanToPageFormData(manager);
        final String p_province_id = "province_id";
        final String p_city_id = "city_id";
        final String p_county_id = "county_id";
        final String p_area_level = "area_level";
        final String p_area_id = "area_id";
        final String p_site_letter = "site_letter";
        final String p_site_name = "site_name";
        final String p_site_type = "site_type";
        final String p_linkman = "linkman";
        final String p_address = "address";
        final String p_freeze = "freeze";
        final String p_entrance = "entrance";
        final String p_risk = "risk";
        final String validate = ToolClient.validateField(formData,p_address,p_area_level,p_area_id,p_city_id,p_county_id,p_entrance,p_freeze,p_linkman,p_province_id,p_risk,p_site_letter,p_site_name,p_site_type);
        if(validate != null)return validate;
        final String userId = LocalUserId.get();
        formData.put("kid",ToolString.getIdsChar32());
        formData.put("craete_userid",userId);
        formData.put("modify_userid",userId);
        formData.put("audit_user",userId);
        return ToolClient.executeRows(apiManageLocationDao.add(formData));
    }

    public String edit(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_kid = "kid";
        final String p_address = "address";
        final String p_area_level = "area_level";
        final String p_audit_user = "audit_user";
        final String p_city_id = "city_id";
        final String p_county_id = "county_id";
        final String p_entrance = "entrance";
        final String p_freeze = "freeze";
        final String p_linkman = "linkman";
        final String p_mobile = "mobile";
        final String p_province_id = "province_id";
        final String p_remark = "remark";
        final String p_risk = "risk";
        final String p_site_letter = "site_letter";
        final String p_site_name = "site_name";
        final String p_site_type = "site_type";
        final String validate = ToolClient.validateField(formData,p_address,p_area_level,p_audit_user,p_city_id,p_county_id,p_entrance,p_freeze,p_linkman,p_mobile,p_province_id,p_remark,p_risk,p_site_letter,p_site_name,p_site_type,p_kid);
        if(validate != null)return validate;
        final String exist_key = apiManageLocationDao.queryExistById(formData.getString(p_kid));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        return ToolClient.executeRows(apiManageLocationDao.edit(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(apiManageLocationDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = apiManageLocationDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(apiManageLocationDao.delById(kid));
    }

    public String delByKeys(final PageFormData formData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final ArrayList<String> lists = ToolString.keysToList(ids);
        return ToolClient.executeRows(apiManageLocationDao.delByKeys(lists),"操作成功","数据已不存在,刷新重试");
    }

    //适用于api接口
    public String listDataPage(final HttpServletRequest request){
        final PageFormData formData = ToolClient.pageParamsApi(request);
        return ToolClient.queryJson(apiManageLocationDao.listDataPage(formData));
    }
}