package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolExcel;
import com.fwtai.tool.ToolString;
import com.fwtai.web.ReportTotalDao;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ReportTotalService{

    @Value("${excel_dir_window}")
    private String dir_window;

    @Value("${excel_dir_linux}")
    private String dir_linux;

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

    //导出
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
        final String separator = File.separator;
        final String os_dir = ToolString.isLinuxOS() ? dir_linux : dir_window;
        final String templateFileName = os_dir + "daily.xlsx";
        final String fileName = new ToolString().getDate()+"_"+ToolString.getIdsChar32();
        final List<HashMap<String,Object>> list = reportTotalDao.queryDataExport(formData);
        final String excelFullPath = os_dir + "download"+separator+fileName+".xlsx";
        final boolean b = ToolExcel.writeExcelTemplate(excelFullPath,list,templateFileName);
        if(b){
            ToolClient.download(excelFullPath,response);
        }else{
            final String json = ToolClient.createJson(ConfigFile.code199,"导出失败,稍候重试");
            ToolClient.responseJson(json,response);
        }
    }
}