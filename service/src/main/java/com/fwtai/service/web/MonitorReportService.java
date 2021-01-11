package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.poi.WordExport;
import com.fwtai.tool.ToolClient;
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
        final String p_sampling_date_start = "sampling_date_start";
        final String p_sampling_date_end = "sampling_date_end";
        final String start = formData.getString(p_sampling_date_start);
        final String end = formData.getString(p_sampling_date_end);
        String selectArea = formData.getString("select_area");
        if(selectArea ==null){
            selectArea = "未选择中高风险地区";
        }
        final List<HashMap<String,Object>> listEmployee = monitorReportDao.queryEmployeeReport(formData);
        final List<HashMap<String,Object>> listSiteType = monitorReportDao.querySiteTypeReport(formData);
        final List<HashMap<String,Object>> listEnvironmentOuterPack = monitorReportDao.queryEnvironmentOuterPack(formData);
        final List<HashMap<String,Object>> listEntranceRisk = monitorReportDao.queryEntranceRisk(formData);
        final HashMap<String, Object> sampleTypeTotal = monitorReportDao.querySampleTypeTotal(formData);
        try {
            if(listEmployee == null || listEmployee.size() <= 0){
                final String json = ToolClient.createJson(ConfigFile.code199,ConfigFile.title +"暂无数据,请换个日期或区县试试");
                ToolClient.responseJson(json,response);
            }else{
                WordExport.exportWord(start,end,selectArea,listEmployee,listSiteType,listEnvironmentOuterPack,listEntranceRisk,sampleTypeTotal,new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+".docx",response);
            }
        } catch (final Exception e){
            e.printStackTrace();
            final String json = ToolClient.createJson(ConfigFile.code199,ConfigFile.title +"导出失败,系统出现错误");
            ToolClient.responseJson(json,response);
        }
    }

    public String queryDataView(final PageFormData formData){
        final List<HashMap<String,Object>> listEmployee = monitorReportDao.queryEmployeeReport(formData);
        final List<HashMap<String,Object>> listSiteType = monitorReportDao.querySiteTypeReport(formData);
        final List<HashMap<String,Object>> listEnvironmentOuterPack = monitorReportDao.queryEnvironmentOuterPack(formData);
        final List<HashMap<String,Object>> listEntranceRisk = monitorReportDao.queryEntranceRisk(formData);
        final HashMap<String, Object> sampleTypeTotal = monitorReportDao.querySampleTypeTotal(formData);
        final HashMap<String,Object> result = new HashMap<String,Object>();
        result.put("listEmployee",listEmployee);//从业人员监测结果,表5
        result.put("listSiteType",listSiteType);//全省不同类型场所监测情况,表2
        result.put("listEnvironmentOuterPack",listEnvironmentOuterPack);//外环境不同样本类型监测情况[区分产品包装和其余外环境样本],表4
        result.put("listEntranceRisk",listEntranceRisk);//表3 不同来源食品监测情况.分_进口_国产_中高风险地区
        result.put("sampleTypeTotal",sampleTypeTotal);//表4上面的文字描述
        return ToolClient.queryJson(result);
    }
}