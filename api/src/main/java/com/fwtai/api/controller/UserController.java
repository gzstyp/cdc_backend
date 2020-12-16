package com.fwtai.api.controller;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.ConfigFile;
import com.fwtai.service.core.UserService;
import com.fwtai.tool.ToolClient;
import com.fwtai.tool.ToolJWT;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

/**
 * app端用户中心,list或查询详细信息时，不需要token的,而添加、编辑、删除时需要登录
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-03-24 12:36
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Api(tags = "用户api接口")
@RestController
@RequestMapping(ConfigFile.api_v10 + "user") // http://api.fwt.cloud/api/v1.0/user/register
public class UserController{

    @Resource
    private UserService userService;
    
    @PostMapping("/refreshToken")
    public void refreshToken(final HttpServletRequest request,final HttpServletResponse response){
        final PageFormData formData = ToolClient.getFormData(request);
        final String access_token = formData.getString("accessToken");
        try {
            final String userId = ToolJWT.extractUserId(access_token);
            final HashMap<String,String> result = userService.refreshToken(userId);
            ToolClient.responseJson(ToolClient.queryJson(result),response);
        } catch (final JwtException exception){
            ToolClient.responseJson(ToolClient.tokenInvalid(),response);
        }
    }

    //放行对外提供注册接口url
    @PostMapping(value = "/register")
    public void register(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJsonSuccess("不携带token使用post访问register成功"),response);
    }

    /**编辑*/
    @PreAuthorize("hasAuthority('role_row_edit')")
    @PostMapping("/edit")
    public void edit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJsonSuccess("需要token才能访问edit成功"),response);
    }

    @GetMapping("/rest")
    public void rest(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJsonSuccess("操作成功"),response);
    }

    //实际上是不走这个方法!!!
    /*@ApiOperation(value = "小程序登录功能", notes = "要求输入账号(用户名)、登录密码、type类型为3")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "username", value = "登录账号(用户名|手机号)", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "password", value = "登录密码", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "type", value = "登录密码", dataType = "int", example = "3",paramType = "query", required = true)
    })
    @PostMapping(value = "/get")
    public void get(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.createJsonSuccess("需要token才能访问edit成功"),response);
    }*/
}