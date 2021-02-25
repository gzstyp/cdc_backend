package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.poi.CategoryGeneral;
import com.fwtai.tool.ToolClient;
import com.fwtai.web.DateAreaDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

@Service
public class DateAreaService{

    @Resource
    private DateAreaDao dateAreaDao;

    //日期区域查询-页面
    public String getView(final PageFormData formData){
        DataFilter.getAreaLevel(formData);
        return ToolClient.queryJson(dateAreaDao.getCategoryGeneral(formData));
    }

    /**查询登录者所拥有的权限*/
    public String queryPermissions(){
        final List<String> permissions = dateAreaDao.queryPermissions();
        return ToolClient.queryJson(permissions);
    }

    //常规日报-导出按钮
    public void queryDataExport(final HttpServletRequest request,final HttpServletResponse response){
        final PageFormData formData = ToolClient.getFormData(request);
        formData.remove("accessToken");
        formData.remove("refreshToken");
        final String p_date_start = "sampling_date_start";
        final String p_date_end = "sampling_date_end";
        String date_start = formData.getString(p_date_start);
        String date_end = formData.getString(p_date_end);
        DataFilter.getAreaLevel(formData);

        final List<HashMap<String,Object>> listType = dateAreaDao.getAllType(formData);
        final List<HashMap<String,Object>> list = dateAreaDao.getCategoryGeneral(formData);

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
        String _date = "";
        if(date_start != null){
            _date += date_start;
        }
        if(date_end != null){
            _date = _date + "至" +date_end;
        }
        final String fileName = label + "中高风险地区入黔返黔人员核酸检测统计汇总表.xlsx";
        if(date_end != null)
        label += "日期分类统计("+date_start+"至"+date_end+")";
        try {
            if(list == null || list.size() <= 0){
                final String json = ToolClient.createJson(ConfigFile.code199,ConfigFile.title +"暂无数据,请换个日期或区域试试");
                ToolClient.responseJson(json,response);
            }else{
                CategoryGeneral.exportExcel(label,list,listType,fileName,response);
            }
        } catch (final Exception e){
            e.printStackTrace();
            final String json = ToolClient.createJson(ConfigFile.code199,ConfigFile.title +"导出失败,请换个日期或区域试试");
            ToolClient.responseJson(json,response);
        }
    }
}