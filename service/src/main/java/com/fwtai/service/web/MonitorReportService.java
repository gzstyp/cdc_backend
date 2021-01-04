package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import com.fwtai.web.MonitorReportDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    //日报导出
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
                final String json = ToolClient.createJson(ConfigFile.code200,ConfigFile.title +"操作成功");
                ToolClient.responseJson(json,response);
            }
        } catch (final Exception e){
            final String json = ToolClient.createJson(ConfigFile.code199,ConfigFile.title +"导出失败,请换个日期或区县试试");
            ToolClient.responseJson(json,response);
        }
    }

    public String queryDataView(final PageFormData formData){
        return ToolClient.queryJson(formData);
    }
}