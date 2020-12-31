package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolString;
import com.fwtai.web.ReportTotalDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportTotalService{

    @Resource
    private ReportTotalDao reportTotalDao;

    public String getView(final PageFormData formData){
        final String p_crowd_date = "crowd_date";
        String crowd_date = formData.getString(p_crowd_date);
        if(crowd_date == null){
            crowd_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            formData.put(p_crowd_date,crowd_date);
        }
        return ToolClient.queryJson(reportTotalDao.getView(formData));
    }

    /**查询登录者所拥有的权限*/
    public String queryPermissions(){
        final List<String> permissions = reportTotalDao.queryPermissions();
        return ToolClient.queryJson(permissions);
    }

    //导出,https://www.yuque.com/easyexcel/doc/api
    public void queryDataExport(final HttpServletRequest request,final HttpServletResponse response){
        final PageFormData formData = ToolClient.getFormData(request);
        formData.remove("accessToken");
        formData.remove("refreshToken");
        final String p_crowd_date = "crowd_date";
        String crowd_date = formData.getString(p_crowd_date);
        if(crowd_date == null){
            crowd_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            formData.put(p_crowd_date,crowd_date);
        }
        final String fileName = new ToolString().getDate()+"_"+ToolString.getIdsChar32();
        final List<HashMap<String,Object>> list = reportTotalDao.queryDataExport(formData);

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
        if(crowd_date != null){
            _date += crowd_date;
        }
        label += "核酸日报表"+_date+" (0:00-24:00)";

        System.out.println(label);

        final String json = ToolClient.createJson(ConfigFile.code200,"导出失败,稍候重试");
        ToolClient.responseJson(json,response);

        /*final boolean b = ToolExcel.writeExcelTemplate(excelFullPath,list,templateFileName);
        if(b){
            ToolClient.download(excelFullPath,response);
        }else{
            final String json = ToolClient.createJson(ConfigFile.code199,"导出失败,稍候重试");
            ToolClient.responseJson(json,response);
        }*/
    }
}