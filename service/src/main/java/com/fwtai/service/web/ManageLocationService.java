package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.bean.UploadFile;
import com.fwtai.bean.UploadObject;
import com.fwtai.config.ConfigFile;
import com.fwtai.config.LocalUserId;
import com.fwtai.core.UserDao;
import com.fwtai.poi.ToolExcel;
import com.fwtai.tool.ToolChinese;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import com.fwtai.web.ManageLocationDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 经营场所业务层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-17 15:37:21
 * @官网 <url>http://www.yinlz.com</url>
*/
@Service
public class ManageLocationService{

    @Value("${upload_dir_window}")
    private String dir_window;

    @Value("${upload_dir_linux}")
    private String dir_linux;

    @Resource
    private ManageLocationDao managelocationDao;

    @Resource
    private UserDao userDao;

    public String add(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_province_id = "province_id";
        final String p_city_id = "city_id";
        final String p_county_id = "county_id";
        final String p_site_name = "site_name";
        final String p_site_letter = "site_letter";
        final String p_site_type = "site_type";
        final String p_linkman = "linkman";
        final String p_address = "address";
        final String p_freeze = "freeze";
        final String p_entrance = "entrance";
        final String p_risk = "risk";
        final String validate = ToolClient.validateField(formData,p_province_id,p_city_id,p_county_id,p_site_name,p_site_type,p_linkman,p_address,p_freeze,p_entrance,p_risk);
        if(validate != null)return validate;
        String site_letter = formData.getString(p_site_letter);
        if(site_letter == null){
            site_letter = ToolChinese.getPinYinHeadChar(formData.getString(p_site_name));
        }
        final Long provinceId = formData.getLong(p_province_id);
        final Long city_id = formData.getLong(p_city_id);
        final Long county_id = formData.getLong(p_county_id);
        final Long towns_id = formData.getLong("towns_id");
        final Long vallage_id = formData.getLong("vallage_id");
        Long area_id = provinceId;
        if(city_id != null){
            area_id = city_id;
        }
        if(county_id != null){
            area_id = county_id;
        }
        if(towns_id != null){
            area_id = towns_id;
        }
        if(vallage_id != null){
            area_id = vallage_id;
        }
        final String userId = LocalUserId.get();
        formData.put("kid",ToolString.getIdsChar32());
        formData.put("area_id",area_id);
        formData.put("audit_user",userId);
        formData.put("craete_userid",userId);
        formData.put("modify_userid",userId);
        formData.put("site_letter",site_letter);
        final HashMap<String,Object> map = getAreaLevel(area_id);
        formData.put("area_level",map.get("level"));
        return ToolClient.executeRows(managelocationDao.add(formData));
    }

    //获取级别1-5,省市县镇村
    protected HashMap<String,Object> getAreaLevel(final long kid){
        return userDao.getAreaLevel(kid);
    }

    public String edit(final HttpServletRequest request){
        final PageFormData formData = new PageFormData(request);
        final String p_kid = "kid";
        final String p_site_name = "site_name";
        final String p_site_letter = "site_letter";
        final String p_site_type = "site_type";
        final String p_linkman = "linkman";
        final String p_address = "address";
        final String p_freeze = "freeze";
        final String p_entrance = "entrance";
        final String p_risk = "risk";
        final String validate = ToolClient.validateField(formData,p_site_name,p_site_type,p_linkman,p_address,p_freeze,p_entrance,p_risk,p_kid);
        if(validate != null)return validate;
        final String exist_key = managelocationDao.queryExistById(formData.getString(p_kid));
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"数据已不存在,刷新重试");
        }
        final Long provinceId = formData.getLong("province_id");
        final Long city_id = formData.getLong("city_id");
        final Long county_id = formData.getLong("county_id");
        final Long towns_id = formData.getLong("towns_id");
        final Long vallage_id = formData.getLong("vallage_id");
        Long area_id = provinceId;
        if(city_id != null){
            area_id = city_id;
        }
        if(county_id != null){
            area_id = county_id;
        }
        if(towns_id != null){
            area_id = towns_id;
        }
        if(vallage_id != null){
            area_id = vallage_id;
        }
        if(area_id != null){
            formData.put("area_id",area_id);
            final HashMap<String,Object> map = getAreaLevel(area_id);
            formData.put("area_level",map.get("level"));
        }
        String site_letter = formData.getString(p_site_letter);
        if(site_letter == null){
            site_letter = ToolChinese.getPinYinHeadChar(formData.getString(p_site_name));
        }
        formData.put("site_letter",site_letter);
        final String userId = LocalUserId.get();
        formData.put("modify_userid",userId);
        return ToolClient.executeRows(managelocationDao.edit(formData));
    }

    public String queryById(final PageFormData pageFormData){
        final String p_id = "id";
        final String validate = ToolClient.validateField(pageFormData,p_id);
        if(validate != null)return validate;
        return ToolClient.queryJson(managelocationDao.queryById(pageFormData.getString(p_id)));
    }

    public String delById(final PageFormData formData){
        final String p_kid = "id";
        final String validate = ToolClient.validateField(formData,p_kid);
        if(validate != null)return validate;
        final String kid = formData.getString(p_kid);
        final String exist_key = managelocationDao.queryExistById(kid);
        if(exist_key == null){
            return ToolClient.createJson(ConfigFile.code199,"删除失败,数据已不存在");
        }
        return ToolClient.executeRows(managelocationDao.delById(kid));
    }

    public String delByKeys(final PageFormData formData){
        final String p_ids = "ids";
        final String validate = ToolClient.validateField(formData,p_ids);
        if(validate != null)return validate;
        final String ids = formData.getString(p_ids);
        final ArrayList<String> lists = ToolString.keysToList(ids);
        return ToolClient.executeRows(managelocationDao.delByKeys(lists),"操作成功","数据已不存在,刷新重试");
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
            final HashMap<String,Object> map = managelocationDao.listData(formData);
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
        final String validate = ToolClient.validateField(formData,p_excel,p_province_id,p_city_id,p_county_id);
        if(validate != null)return validate;
        final String baseDir = ToolString.isLinuxOS() ? dir_linux : dir_window;
        final UploadObject uploadObject = ToolClient.uploadImage(request,baseDir,"/excel/import",true,1,true);
        final String errorMsg = uploadObject.getErrorMsg();
        if(errorMsg != null) return ToolClient.createJsonFail(errorMsg);
        final ArrayList<UploadFile> listFiles = uploadObject.getListFiles();
        if(listFiles == null || listFiles.size() <= 0) return ToolClient.createJsonFail("请选择Excel文件");
        final String fullPath = listFiles.get(0).getFullPath();
        final HashMap<String,String> mapper = new HashMap<>();
        mapper.put("场所类型（从下拉列表选择）","site_type");
        mapper.put("名称","site_name");
        mapper.put("联系人","linkman");
        mapper.put("联系人手机号","mobile");
        mapper.put("地址","address");
        mapper.put("是否有冷冻冷藏产品","freeze");
        mapper.put("是否含有冷冻进口产品","entrance");
        mapper.put("是否含有高中风险地区产品","risk");
        try {
            final ArrayList<HashMap<String,Object>> list = ToolExcel.parseExcel(mapper,fullPath,1,2);
            ToolClient.delFileByThread(fullPath);
            return ToolClient.queryJson(listFiles.get(0));
        } catch (final Exception e) {
            e.printStackTrace();
            ToolClient.delFileByThread(fullPath);
            return ToolClient.createJsonFail("导入失败,请检查文件表头是否有误");
        }
    }

    /*获取经营场所*/
    public String getManagerLocation(){
        return ToolClient.queryJson(managelocationDao.getManagerLocation());
    }
}