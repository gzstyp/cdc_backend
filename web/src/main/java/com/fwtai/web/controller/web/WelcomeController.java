package com.fwtai.web.controller.web;

import com.fwtai.service.web.WelcomeService;
import com.fwtai.tool.ToolClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 首页统计
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-12-31 0:23
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@RestController
@RequestMapping("/welcome")
public class WelcomeController{

    @Resource
    private WelcomeService welcomeService;

    @GetMapping("/getData")
    public void getData(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(welcomeService.getData(ToolClient.getFormData(request)),response);
    }
}