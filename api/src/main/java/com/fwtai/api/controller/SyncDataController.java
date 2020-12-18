package com.fwtai.api.controller;

import com.fwtai.config.ConfigFile;
import com.fwtai.service.api.ApiSyncDataService;
import com.fwtai.tool.ToolClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基础数据同步
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-12-16 18:42
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
@RestController
@Api(tags = "基础数据同步")
@RequestMapping(ConfigFile.api_v10 +"syncData")
public class SyncDataController{

    @Resource
    private ApiSyncDataService apiSyncDataService;

    @ApiOperation(value = "获取人群分类基础数据", notes = "不需携带区域信息数据")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getCrowdCategory")
    public void getCrowdCategory(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiSyncDataService.getCrowdCategory(ToolClient.getFormData(request)),response);
    }

    @ApiOperation(value = "获取人群类型", notes = "不需携带区域信息数据")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getCrowdType")
    public void getCrowdType(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiSyncDataService.getCrowdType(ToolClient.getFormData(request)),response);
    }

    @ApiOperation(value = "获取经营场所数据", notes = "根据当前登录人的的区域主键area_id所绑定区域来同步基础数据")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getManagerArea")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "areaId", value = "当前登录人的区域字段area_id的值获取,即登录成功后返回的areaData里的kid主键值", dataType = "String", paramType = "query", required = true)
    })
    public void getManagerArea(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiSyncDataService.getManagerArea(ToolClient.getFormData(request)),response);
    }

    @ApiOperation(value = "获取经营场所类型数据", notes = "经营场所类型数据,数据来自于数据字典")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getManagerLocation")
    public void getManagerLocation(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiSyncDataService.getManagerLocation(ToolClient.getFormData(request)),response);
    }
}