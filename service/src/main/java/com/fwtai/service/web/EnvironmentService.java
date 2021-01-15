package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.poi.ToolExcel;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import com.fwtai.web.EnvironmentDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String editPositive(final PageFormData formData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final ArrayList<String> lists = ToolString.keysToList(ids);
        return ToolClient.executeRows(1);
    }

    public String editNegative(final PageFormData formData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final ArrayList<String> lists = ToolString.keysToList(ids);
        return ToolClient.executeRows(1);
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

    public void queryDataExport(final HttpServletRequest request,final HttpServletResponse response){
        final PageFormData formData = ToolClient.getFormData(request);
        formData.remove("accessToken");
        formData.remove("refreshToken");
        final String fileName = new ToolString().getDate()+"_外环境监测.xlsx";
        final List<Map<String,Object>> list = environmentDao.queryDataExport(formData);
        final ArrayList<String> fields = new ArrayList<>();
        fields.add("sample_code");
        fields.add("city_id");
        fields.add("county_id");
        fields.add("site_type");
        fields.add("freeze_type");
        fields.add("market_name");
        fields.add("vendor_name");
        fields.add("phone");
        fields.add("vendor_code");
        fields.add("source");
        fields.add("entrance");
        fields.add("entrance_serial");
        fields.add("sample_name");
        fields.add("freeze_related");
        fields.add("sample_type");
        fields.add("sampling_date");
        fields.add("detection_date");
        fields.add("result");
        fields.add("remark");

        final ArrayList<String> titles = new ArrayList<>();
        titles.add("标本实验编号");
        titles.add("市（州）");
        titles.add("县（区）");
        titles.add("监测场所类型");
        titles.add("冷库类型");
        titles.add("市场名称");
        titles.add("摊主姓名");
        titles.add("联系电话");
        titles.add("摊位编号");
        titles.add("产品来源地");
        titles.add("是否为进口产品");
        titles.add("进口产品批号");
        titles.add("标本名称");
        titles.add("是否冷链相关");
        titles.add("标本类型");
        titles.add("采样日期");
        titles.add("检测日期");
        titles.add("核酸检测结果");
        titles.add("备注");

        try {
            if(list.isEmpty()){
                ToolClient.responseJson(ToolClient.createJson(ConfigFile.code199,ConfigFile.title + "暂无数据,换个搜索条件试试"),response);
            }else{
                ToolExcel.exportExcel(list,fields,titles,"外环境监测",fileName,response);
            }
        } catch (final Exception e) {
            final String json = ToolClient.createJson(ConfigFile.code199,e.getMessage());
            ToolClient.responseJson(json,response);
        }
    }
}