package com.fwtai.api.controller;

import com.fwtai.config.ConfigFile;
import com.fwtai.entity.ReqPage;
import com.fwtai.service.api.ApiCrowdTypeService;
import com.fwtai.tool.ToolClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 人群类型控制层|路由层[api]
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-16 10:16:07
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@Api(tags = "人群类型")
@RequestMapping(ConfigFile.api_v10 +"crowdType")
public class CrowdTypeController{

    @Resource
	private ApiCrowdTypeService apiCrowdTypeService;

    /**添加*/
    @ApiOperation(value = "请求,添加操作", notes = "添加操作")
    @PreAuthorize("hasRole('ROLE_APP') or hasAnyRole('ROLE_APP_SUPER')")
    @PostMapping("/add")
    public void add(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTypeService.add(request),response);
    }

    /**编辑*/
    @ApiOperation(value = "编辑操作", notes = "通过主键kid编辑|修改数据,字段的参照表结构")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "kid", value = "被修改的主键值,其余的参照表结构", dataType = "String", paramType = "query", required = true)
    })
    @PreAuthorize("hasRole('ROLE_APP') or hasAnyRole('ROLE_APP_SUPER')")
    @PostMapping("/edit")
    public void edit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTypeService.edit(request),response);
    }

    /**根据id查询对应的数据*/
    @ApiOperation(value = "获取详细信息", notes = "通过id获取详细信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "被查看的id", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/queryById")
    public void queryById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTypeService.queryById(ToolClient.getFormData(request)),response);
    }

    /**删除-单行*/
    @ApiOperation(value = "删除数据", notes = "通过id删除对应数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "删除数据的id值", dataType = "String", paramType = "query", required = true),
    })
    @PreAuthorize("hasRole('ROLE_APP') or hasAnyRole('ROLE_APP_SUPER')")
    @PostMapping("/delById")
    public void delById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTypeService.delById(ToolClient.getFormData(request)),response);
    }

    /**批量删除*/
    @ApiOperation(value = "批量删除", notes = "通过ids删除对应数据,ids是字符串,每个值主键id以英文逗号,隔开;如10001,10002,10003")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "主键的集合以英文逗号,隔开。如10001,10002,10003", dataType = "String", paramType = "query", required = true),
    })
    @PreAuthorize("hasRole('ROLE_APP') or hasAnyRole('ROLE_APP_SUPER')")
    @PostMapping("/delByKeys")
    public void delByKeys(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTypeService.delByKeys(ToolClient.getFormData(request)),response);
    }

    @ApiOperation(value = "获取全部人群类型", notes = "返回值kid表示主键,name是人群分类名称,crowd_id是表'人群分类'的id主键,对应的表 bs_crowd_type")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getList")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "crowd_id", value = "人群分类kid", dataType = "String", paramType = "query", required = false)
    })
    public void getList(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTypeService.getList(ToolClient.getFormData(request)),response);
    }

    /**获取分页数据*/
    @GetMapping("/listData")
    public void listData(@RequestBody ReqPage reqPage,final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTypeService.listData(ToolClient.getFormData(request)),response);
    }
}