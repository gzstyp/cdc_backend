package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.bean.UploadFile;
import com.fwtai.bean.UploadObject;
import com.fwtai.config.ConfigFile;
import com.fwtai.poi.ToolExcel;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import com.fwtai.web.EnvironmentDao;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${upload_dir_window}")
    private String dir_window;

    @Value("${upload_dir_linux}")
    private String dir_linux;

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
        return ToolClient.executeRows(environmentDao.editPositive(lists),"操作成功","数据已不存在,刷新重试");
    }

    public String editNegative(final PageFormData formData){
        if(formData.size() == 0){
            return ToolClient.createJson(ConfigFile.code199,"请选择搜索条件再发布");
        }
        return ToolClient.executeRows(environmentDao.editNegative(formData));
    }

    public String listData(PageFormData formData){
        DataFilter.getAreaLevel(formData);
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

    //需要手动删除序号、市（州）、县（区）的列、把所有的单元格‘设置单元格格式->文本’,需要把下拉的文字和PC后端的文字要一致,否则导入进去对不上!!!
    public String addImportExcel(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_excel = "excel";
        final String p_province_id = "province_id";
        final String p_city_id = "city_id";
        final String p_county_id = "county_id";
        final String p_area_level = "area_level";
        final String validate = ToolClient.validateField(formData,p_excel,p_province_id,p_city_id,p_county_id,p_area_level);
        if(validate != null)return validate;
        final String baseDir = ToolString.isLinuxOS() ? dir_linux : dir_window;
        final UploadObject uploadObject = ToolClient.uploadImage(request,baseDir,"/excel/import",true,1,true);
        final String errorMsg = uploadObject.getErrorMsg();
        if(errorMsg != null) return ToolClient.createJsonFail(errorMsg);
        final ArrayList<UploadFile> listFiles = uploadObject.getListFiles();
        if(listFiles == null || listFiles.size() <= 0) return ToolClient.createJsonFail("请选择Excel文件");
        final String fullPath = listFiles.get(0).getFullPath();
        final HashMap<String,String> mapper = new HashMap<>();
        /* 序号	标本实验编号*/
        mapper.put("标本实验编号","sample_code");
        //mapper.put("市（州）","site_type");
        //mapper.put("县（区）","freeze_type");
        mapper.put("监测场所类型（从下拉列表选择）","site_type");
        mapper.put("若为冷库请选择类型","freeze_type");
        mapper.put("市场名称","market_name");
        mapper.put("摊主姓名","vendor_name");
        mapper.put("摊位编号","vendor_code");
        mapper.put("产品来源地（填写至国家、省、市）","source");
        mapper.put("是否为进口产品","entrance");
        mapper.put("若为进口产品请填写批号","entrance_serial");
        mapper.put("标本名称（写明具体标本名称，如三文鱼涂抹物）","sample_name");
        mapper.put("是否冷链相关","freeze_related");
        mapper.put("标本类型（从下拉列表选择）","sample_type");
        mapper.put("采样日期","sampling_date");
        mapper.put("检测日期","detection_date");
        mapper.put("新冠核酸检测结果","result");
        mapper.put("备注","remark");
        try {
            final ArrayList<HashMap<String,Object>> list = ToolExcel.parseExcel(mapper,fullPath,1,2);
            ToolClient.delFileByThread(fullPath);
            return ToolClient.queryJson(listFiles.get(0));
        } catch (final Exception e) {
            e.printStackTrace();
            ToolClient.delFileByThread(fullPath);
            return ToolClient.createJsonFail("导入失败<br/>1.请检查表头是否有误<br/>2.是否已设置单元格格式为‘文本’?");
        }
    }

    public void queryDataExport(final HttpServletRequest request,final HttpServletResponse response){
        final PageFormData formData = ToolClient.getFormData(request);
        formData.remove("accessToken");
        formData.remove("refreshToken");
        DataFilter.getAreaLevel(formData);
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