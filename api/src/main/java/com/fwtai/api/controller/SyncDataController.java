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
 * 基础数据同步,要求当天的数据必须上传完
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

    @ApiOperation(value = "获取经营场所数据", notes = "获取经营场所数据,根据当前登录人的areaData的区域主键area_id所绑定区域来同步基础数据")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getManagerArea")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "areaId", value = "当前登录人的区域字段area_id的值获取,即登录成功后返回的areaData里的kid主键值", dataType = "String", paramType = "query", required = true)
    })
    public void getManagerArea(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiSyncDataService.getManagerArea(ToolClient.getFormData(request)),response);
    }

    @ApiOperation(value = "获取经营场所|从业场所|监测场所类型数据", notes = "经营场所|从业场所|监测场所都属于场所类型数据,供下拉列表选择")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getManagerLocation")
    public void getManagerLocation(final HttpServletResponse response){
        ToolClient.responseJson(apiSyncDataService.getManagerLocation(),response);
    }

    @ApiOperation(value = "获取冷库类型数据", notes = "冷库类型,用于环境监测或从业人员监测")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getFreezeType")
    public void getFreezeType(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiSyncDataService.getFreezeType(),response);
    }

    @ApiOperation(value = "标本类型(用于环境监测)", notes = "标本类型(用于环境监测)")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getSpecimenType")
    public void getSpecimenType(final HttpServletResponse response){
        ToolClient.responseJson(apiSyncDataService.getSpecimenType(),response);
    }

    @ApiOperation(value = "样本类型(用于从业人员)", notes = "样本类型(用于从业人员)")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getSampleType")
    public void getSampleType(final HttpServletResponse response){
        ToolClient.responseJson(apiSyncDataService.getSampleType(),response);
    }

    @ApiOperation(value = "工种", notes = "工种(用于从业人员)")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getProfession")
    public void getProfession(final HttpServletResponse response){
        ToolClient.responseJson(apiSyncDataService.getProfession(),response);
    }

    @ApiOperation(value = "根据父级id获取字典数据(基础数据)", notes = "根据父级id获取字典数据(基础数据)")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getDictByPid")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "pid", value = "父级字典id", dataType = "String", paramType = "query", required = true)
    })
    public void getDictByPid(final String pid,final HttpServletResponse response){
        ToolClient.responseJson(apiSyncDataService.getDictByPid(pid),response);
    }
}