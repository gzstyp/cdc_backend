package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolWord;
import com.fwtai.web.MonitorReportDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class MonitorReportService{

    @Resource
    private MonitorReportDao monitorReportDao;

    /**查询登录者所拥有的权限*/
    public String queryPermissions(){
        final List<String> permissions = monitorReportDao.queryPermissions();
        return ToolClient.queryJson(permissions);
    }

    //日报word导出
    public void queryExportWord(final HttpServletRequest request,final HttpServletResponse response){
        final PageFormData formData = ToolClient.getFormData(request);
        formData.remove("accessToken");
        formData.remove("refreshToken");
        final List<HashMap<String,Object>> list = monitorReportDao.queryEmployeeReport(formData);
        try {
            if(list == null || list.size() <= 0){
                final String json = ToolClient.createJson(ConfigFile.code199,ConfigFile.title +"暂无数据,请换个日期或区县试试");
                ToolClient.responseJson(json,response);
            }else{
                ToolWord.exportWord(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".docx",response);
            }
        } catch (final Exception e){
            e.printStackTrace();
            final String json = ToolClient.createJson(ConfigFile.code199,ConfigFile.title +"导出失败,请换个日期或区县试试");
            ToolClient.responseJson(json,response);
        }
    }

    public String queryDataView(final PageFormData formData){
        return ToolClient.queryJson(formData);
    }
}