package com.fwtai.web.controller.web;

import com.fwtai.service.core.UserService;
import com.fwtai.service.web.DailyDetailsService;
import com.fwtai.tool.ToolClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日报情况
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021年2月18日 16:01:51
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@RequestMapping("/dailyDetails")
public class DailyDetailsController{

    @Resource
    private UserService userService;

    @Resource
    private DailyDetailsService dailyDetailsService;

    /**获取区域数据*/
    @PreAuthorize("hasAuthority('dailyDetails_btn_row_areaSelect')")
    @GetMapping("/queryAreaSelect")
    public void queryArea(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.queryArea(ToolClient.getFormData(request)),response);
    }

    @PreAuthorize("hasAuthority('dailyDetails_btn_getView')")
    @GetMapping("/getView")
    public void getView(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(dailyDetailsService.getView(ToolClient.getFormData(request)),response);
    }

    /**按钮-导出*/
    @PreAuthorize("hasAuthority('dailyDetails_btn_export')")
    @GetMapping("/queryExportWord")
    public void queryExportWord(final HttpServletRequest request,final HttpServletResponse response){
        dailyDetailsService.queryDataExport(request,response);
    }

    @PreAuthorize("hasAuthority('dailyDetails_btn_permissions')")
    @GetMapping("/queryPermissions")
    public void queryPermissions(final HttpServletResponse response){
        ToolClient.responseJson(dailyDetailsService.queryPermissions(),response);
    }
}