package com.fwtai.web.controller.web;

import com.fwtai.service.web.MonitorReportService;
import com.fwtai.tool.ToolClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 监测结果报告
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021-01-04 13:30
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@RestController
@RequestMapping("/monitorReport")
public class MonitorReportController{

    @Resource
    private MonitorReportService monitorReportService;

    @PreAuthorize("hasAuthority('monitorReport_btn_permissions')")
    @GetMapping("/queryPermissions")
    public void queryPermissions(final HttpServletResponse response){
        ToolClient.responseJson(monitorReportService.queryPermissions(),response);
    }

    /**导出到word*/
    @PreAuthorize("hasAuthority('monitorReport_export_word')")
    @GetMapping("/queryExportWord")
    public void queryExportWord(final HttpServletRequest request,final HttpServletResponse response){
        monitorReportService.queryExportWord(request,response);
    }

    /**显示监测结果*/
    @PreAuthorize("hasAuthority('monitorReport_export_page')")
    @GetMapping("/queryDataView")
    public void queryDataView(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(monitorReportService.queryDataView(ToolClient.getFormData(request)),response);
    }
}