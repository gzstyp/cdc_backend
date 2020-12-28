package com.fwtai.service.api;

import com.fwtai.api.ApiCrowdTotalDao;
import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.config.LocalUserId;
import com.fwtai.entity.CrowdTotal;
import com.fwtai.tool.ToolClient;
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
 * 人群日报业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-16 11:06:25
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class ApiCrowdTotalService{

    @Resource
    private ApiCrowdTotalDao apiCrowdTotalDao;

    public String add(final CrowdTotal crowdTotal){
        final PageFormData formData = ToolClient.beanToPageFormData(crowdTotal);
        final String p_area_level = "area_level";
        final String p_city_id = "city_id";
        final String p_county_id = "county_id";
        final String p_crowd_id = "crowd_id";
        final String p_crowd_type_id = "crowd_type_id";
        final String p_detection_total = "detection_total";
        final String p_masculine_total = "masculine_total";
        final String p_province_id = "province_id";
        final String p_sampling_total = "sampling_total";
        final String p_appid = "appid";
        final String validate = ToolClient.validateField(formData,p_area_level,p_city_id,p_county_id,p_crowd_id,p_crowd_type_id,p_detection_total,p_masculine_total,p_province_id,p_sampling_total);
        if(validate != null)return validate;
        final String validateInteger = ToolClient.validateInteger(formData,p_area_level,p_detection_total,p_masculine_total,p_sampling_total);
        if(validateInteger != null)return validateInteger;
        final int sampling = formData.getInteger(p_sampling_total);
        final int detection = formData.getInteger(p_detection_total);
        final int masculine = formData.getInteger(p_masculine_total);
        if(detection > sampling){
            return ToolClient.createJsonFail("检测人数大于采样人数");
        }
        if(masculine > sampling){
            return ToolClient.createJsonFail("阳性人数大于采样人数");
        }
        final String kid = ToolString.getIdsChar32();
        formData.put("kid",kid);
        final String userId = LocalUserId.get();
        formData.put("craete_userid",userId);
        formData.put("modify_userid",userId);
        formData.put("audit_user",userId);
        try {
            final int rows = apiCrowdTotalDao.add(formData);
            if(rows > 0){
                final HashMap<String,Object> result = new HashMap<>(3);
                result.put("kid",kid);
                final String appid = formData.getString(p_appid);
                if(appid != null){
                    result.put("appid",appid);
                }
                result.put("rows",rows);
                return ToolClient.queryJson(result);
            }else{
                return ToolClient.createJsonFail("操作失败");
            }
        } catch (final Exception e) {
            return ToolClient.createJson(ConfigFile.code198,"已添加当日的统计,若有误请编辑");
        }
    }

    public String edit(final CrowdTotal crowdTotal){
        final PageFormData formData = ToolClient.beanToPageFormData(crowdTotal);
        final String p_kid = "kid";
        final String p_area_level = "area_level";
        final String p_city_id = "city_id";
        final String p_county_id = "county_id";
        final String p_crowd_id = "crowd_id";
        final String p_crowd_type_id = "crowd_type_id";
        final String p_detection_total = "detection_total";
        final String p_masculine_total = "masculine_total";
        final String p_province_id = "province_id";
        final String p_sampling_total = "sampling_total";
        final String validate = ToolClient.validateField(formData,p_area_level,p_city_id,p_county_id,p_crowd_id,p_crowd_type_id,p_detection_total,p_masculine_total,p_province_id,p_sampling_total,p_kid);
        if(validate != null)return validate;
        final String validateInteger = ToolClient.validateInteger(formData,p_area_level,p_detection_total,p_masculine_total,p_sampling_total);
        if(validateInteger != null)return validateInteger;
        final int sampling = formData.getInteger(p_sampling_total);
        final int detection = formData.getInteger(p_detection_total);
        final int masculine = formData.getInteger(p_masculine_total);
        if(detection > sampling){
            return ToolClient.createJsonFail("检测人数大于采样人数");
        }
        if(masculine > sampling){
            return ToolClient.createJsonFail("阳性人数大于采样人数");
        }
        final String exist_key = apiCrowdTotalDao.queryExistById(formData.getString(p_kid));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        final String userId = LocalUserId.get();
        formData.put("modify_userid",userId);
        return ToolClient.executeRows(apiCrowdTotalDao.edit(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(apiCrowdTotalDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = apiCrowdTotalDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(apiCrowdTotalDao.delById(kid));
    }

    public String delByKeys(final PageFormData formData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final ArrayList<String> lists = ToolString.keysToList(ids);
        return ToolClient.executeRows(apiCrowdTotalDao.delByKeys(lists),"操作成功","数据已不存在,刷新重试");
    }

    //适用于api接口
    public String listDataPage(final HttpServletRequest request){
        final PageFormData formData = ToolClient.pageParamsApi(request);
        if(formData == null) return ToolClient.jsonValidateField();
        final HashMap<String,Object> map = apiCrowdTotalDao.listData(formData);
        return ToolClient.jsonPage((List<?>) map.get(ConfigFile.rows),(Integer) map.get(ConfigFile.total));
    }

    public String getListData(final HttpServletRequest request){
        final PageFormData formData = ToolClient.getFormData(request);
        String crowd_date = formData.getString("crowd_date");
        if(crowd_date == null){
            crowd_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            formData.put("crowd_date",crowd_date);
        }
        return ToolClient.queryJson(apiCrowdTotalDao.getListData(formData));
    }

    /**人群统计审批审核后提交保存*/
    public String editBatchAudit(final PageFormData formData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final String userId = LocalUserId.get();
        final ArrayList<String> lists = ToolString.keysToList(ids);
        final HashMap<String,Object> map = new HashMap<>(2);
        map.put("audit_user",userId);
        map.put("listIds",lists);
        final int rows = apiCrowdTotalDao.editBatchAudit(map);
        final String msg = (rows == lists.size()) ? "操作成功" : "操作成功"+rows+"条数,失败"+(lists.size()-rows)+"条数";
        return ToolClient.executeRows(rows,msg,"数据已不存在,刷新重试");
    }

    public String getListType(HttpServletRequest request){
        final PageFormData formData = ToolClient.getFormData(request);
        return ToolClient.queryJson(apiCrowdTotalDao.getListType(formData));
    }
}