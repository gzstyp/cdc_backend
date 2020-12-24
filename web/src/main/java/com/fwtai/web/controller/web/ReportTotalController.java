package com.fwtai.web.controller.web;

import com.fwtai.service.core.UserService;
import com.fwtai.service.web.ReportTotalService;
import com.fwtai.tool.ToolClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 日报表
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/12/23 19:29
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@RequestMapping("/reportTotal")
public class ReportTotalController{

    @Resource
    private UserService userService;

    @Resource
    private ReportTotalService reportTotalService;

    /**获取区域数据*/
    @PreAuthorize("hasAuthority('reportTotal_btn_row_areaSelect')")
    @GetMapping("/queryAreaSelect")
    public void queryArea(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.queryArea(ToolClient.getFormData(request)),response);
    }

    @PreAuthorize("hasAuthority('reportTotal_btn_getView')")
    @GetMapping("/getView")
    public void getView(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(reportTotalService.getView(ToolClient.getFormData(request)),response);
    }
}