package com.fwtai.api.controller;

import com.fwtai.config.ConfigFile;
import com.fwtai.service.api.ApiCrowdCategoryService;
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
 * 人群分类控制层|路由层[api]
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-15 13:50:30
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@Api(tags = "人群分类")
@RequestMapping(ConfigFile.api_v10 + "crowdCategory")
public class CrowdCategoryController{

    @Resource
	private ApiCrowdCategoryService apiCrowdCategoryService;

    /**获取分页数据*/
    @PreAuthorize("hasRole('ROLE_APP')")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "current", value = "表示当前页", dataType = "int", paramType = "query", required = true),
        @ApiImplicitParam(name = "pageSize", value = "表示每页大小", dataType = "int", paramType = "query", required = true)
    })
    @GetMapping("/listDataPage")// http://192.168.3.108:801/api/v1.0/crowdCategory/listDataPage
    public void listDataPage(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdCategoryService.listData(request),response);
    }

    @ApiOperation(value = "获取人群分类", notes = "返回值kid表示主键,name是人群分类名称,对应的表 bs_crowd")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/getList")
    public void getList(final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdCategoryService.getList(),response);
    }
}