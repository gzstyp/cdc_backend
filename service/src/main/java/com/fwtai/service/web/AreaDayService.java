package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.poi.AreaDay;
import com.fwtai.tool.ToolClient;
import com.fwtai.web.AreaDayDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class AreaDayService{

    @Resource
    private AreaDayDao areaDayDao;

    //常规日报-页面
    public String getView(final PageFormData formData){
        DataFilter.getAreaLevel(formData);
        return ToolClient.queryJson(areaDayDao.getCategoryGeneral(formData));
    }

    /**查询登录者所拥有的权限*/
    public String queryPermissions(){
        final List<String> permissions = areaDayDao.queryPermissions();
        return ToolClient.queryJson(permissions);
    }

    //常规日报-导出按钮
    public void queryDataExport(final HttpServletRequest request,final HttpServletResponse response){
        final PageFormData formData = ToolClient.getFormData(request);
        formData.remove("accessToken");
        formData.remove("refreshToken");
        final String p_sampling_date = "sampling_date";
        String sampling_date = formData.getString(p_sampling_date);
        DataFilter.getAreaLevel(formData);
        if(sampling_date == null){
            sampling_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            formData.put("sampling_date",sampling_date);
        }

        final List<HashMap<String,Object>> listType = areaDayDao.getAllType(formData);
        final List<HashMap<String,Object>> list = areaDayDao.getCategoryGeneral(formData);

        final String province_id = formData.getString("province_id");
        final String city_id = formData.getString("city_id");
        final String county_id = formData.getString("county_id");

        if(province_id == null){
            formData.remove("province_text");
        }
        if(city_id == null){
            formData.remove("city_text");
        }
        if(county_id == null){
            formData.remove("county_text");
        }
        final String province_text = formData.getString("province_text");
        final String city_text = formData.getString("city_text");
        final String county_text = formData.getString("county_text");
        String label = "";
        if(province_text !=null){
            label += province_text;
        }
        if(city_text != null){
            label += city_text;
        }
        if(county_text != null){
            label += county_text;
        }
        final String fileName = label + "区域日报"+sampling_date+".xlsx";
        label += "区域日报"+sampling_date+"(00:00-24:00)";
        final String sheetName = (city_text != null) ? city_text : "核酸日报表";
        try {
            if(list == null || list.size() <= 0){
                final String json = ToolClient.createJson(ConfigFile.code199,ConfigFile.title +"暂无数据,请换个日期或区域试试");
                ToolClient.responseJson(json,response);
            }else{
                AreaDay.exportExcel(label,sheetName,list,listType,fileName,response);
            }
        } catch (final Exception e){
            e.printStackTrace();
            final String json = ToolClient.createJson(ConfigFile.code199,ConfigFile.title +"导出失败,请换个日期或区域试试");
            ToolClient.responseJson(json,response);
        }
    }
}