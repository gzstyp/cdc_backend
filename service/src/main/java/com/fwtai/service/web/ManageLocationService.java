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
import java.util.Map;

import static java.util.stream.Collectors.groupingBy;

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

        final String province_id = formData.getString(p_province_id);
        final String city_id = formData.getString(p_city_id);
        final String county_id = formData.getString(p_county_id);

        try {
            final ArrayList<HashMap<String,Object>> list = ToolExcel.parseExcel(mapper,fullPath,1,2);
            ToolClient.delFileByThread(fullPath);
            //分组
            final Map<String,List<Map<String, Object>>> group = list.stream().collect(groupingBy(map -> map.get("site_type").toString()));
            final ArrayList<String> names = new ArrayList<>(group.keySet());
            final List<HashMap<String,String>> ids = managelocationDao.queryIdsByName(names);

            final ArrayList<HashMap<String,String>> duplicates = new ArrayList<>();

            for(int i = 0; i < list.size(); i++){
                final HashMap<String,Object> map = list.get(i);
                String site_name = (String)map.get("site_name");
                if(site_name == null || site_name.length() <=0){
                    return ToolClient.createJsonFail("导入失败,其中‘名称’列的行没有数据,将其删除再导入!");
                }
                site_name = ToolString.wipeStrBlank(site_name);
                map.put("site_name",site_name);

                String address = (String)map.get("address");
                if(address != null && address.length()>0){
                    address = ToolString.wipeStrBlank(address);
                    map.put("address",address);
                }else{
                    map.put("address","");
                }
                String linkman = (String)map.get("linkman");
                if(linkman != null && linkman.length()>0){
                    linkman = ToolString.wipeStrBlank(linkman);
                    map.put("linkman",linkman);
                }else{
                    map.put("linkman","");
                }
                String mobile = (String)map.get("mobile");
                if(mobile != null && mobile.length()>0){
                    if(mobile.contains(".00"))
                        mobile = mobile.substring(0,mobile.lastIndexOf("."));
                    map.put("mobile",mobile);
                }else{
                    map.put("mobile","");
                }
                final HashMap<String,String> temp = new HashMap<>();
                temp.put("kv","名称'"+site_name+",联系人'"+linkman+"',地址'"+address+"'");
                duplicates.add(temp);
            }

            //分组
            final Map<String,List<Map<String, String>>> all = duplicates.stream().collect(groupingBy(map -> map.get("kv")));

            for(final String key : all.keySet()){
                final int size = all.get(key).size();
                if(size > 1){
                    return ToolClient.createJsonFail("导入失败,其中"+key+"数据重复,将其删除再导入!");
                }
            }
            if(all.size() > 10000){
                return ToolClient.createJsonFail("导入失败,数据量已超过10000条");
            }
            list.forEach(map->{
                final String freeze = "freeze";
                final String entrance = "entrance";
                final String risk = "risk";
                final String _freeze = String.valueOf(map.get(freeze));
                if(_freeze.equals("是")){
                    map.put(freeze,1);
                }else{
                    map.put(freeze,0);
                }
                final String _entrance = String.valueOf(map.get(entrance));
                if(_entrance.equals("是")){
                    map.put(entrance,1);
                }else{
                    map.put(entrance,0);
                }
                final String _risk = String.valueOf(map.get(risk));
                if(_risk.equals("是")){
                    map.put(risk,1);
                }else{
                    map.put(risk,0);
                }
                final String userId = LocalUserId.get();
                map.put("kid",ToolString.getIdsChar32());
                map.put("flag",1);
                map.put("area_level",3);
                map.put("area_id",county_id);
                map.put("province_id",province_id);
                map.put("city_id",city_id);
                map.put("county_id",county_id);
                map.put("audit_user",userId);
                map.put("craete_userid",userId);
                map.put("modify_userid",userId);
                String site_name = (String)map.get("site_name");
                map.put("site_letter",ToolChinese.getPinYinHeadChar(site_name));
                final String site_type = "site_type";
                final String type = String.valueOf(map.get(site_type));
                for(int i = 0; i < ids.size(); i++){
                    final HashMap<String,String> _map = ids.get(i);
                    if(_map.get("name").equals(type)){
                        map.put(site_type,_map.get("kid"));
                        break;
                    }
                }
            });
            final int rows = managelocationDao.addExcel(list);
            final String msg = (rows == list.size()) ? "成功导入"+rows+"条" : "成功"+rows+"条数,失败"+(list.size()-rows)+"条数";
            return ToolClient.executeRows(rows,msg,"导入数据失败");
        } catch (final Exception e){
            e.printStackTrace();
            ToolClient.delFileByThread(fullPath);
            return ToolClient.createJsonFail("导入数据失败<br/>1.请检查表头数据是否有误<br/>2.全部单元格格式均已设置为‘文本’<br/>3.检查表头'名称'列数据是否有空<br/>4.检查列'联系人'、'地址'是否有重复<br/>5.若'联系人'和'地址'对应列没有数据将其删除再重新导入!");
        }
    }

    /*获取经营场所*/
    public String getManagerLocation(){
        return ToolClient.queryJson(managelocationDao.getManagerLocation());
    }
}