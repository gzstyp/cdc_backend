package com.fwtai.web.controller.web;

import com.fwtai.service.core.UserService;
import com.fwtai.service.web.DateAreaService;
import com.fwtai.tool.ToolClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日期区域查询
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021年2月25日 17:02:00
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@RequestMapping("/dateArea")
public class DateAreaController{

    @Resource
    private UserService userService;

    @Resource
    private DateAreaService dateAreaService;

    /**获取区域数据*/
    @PreAuthorize("hasAuthority('dateArea_btn_row_areaSelect')")
    @GetMapping("/queryAreaSelect")
    public void queryArea(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.queryArea(ToolClient.getFormData(request)),response);
    }

    @PreAuthorize("hasAuthority('dateArea_btn_getView')")
    @GetMapping("/getView")
    public void getView(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(dateAreaService.getView(ToolClient.getFormData(request)),response);
    }

    /**按钮-导出*/
    @PreAuthorize("hasAuthority('dateArea_btn_export')")
    @GetMapping("/exportExcel")
    public void exportExcel(final HttpServletRequest request,final HttpServletResponse response){
        dateAreaService.queryDataExport(request,response);
    }

    @PreAuthorize("hasAuthority('dateArea_btn_permissions')")
    @GetMapping("/queryPermissions")
    public void queryPermissions(final HttpServletResponse response){
        ToolClient.responseJson(dateAreaService.queryPermissions(),response);
    }
}