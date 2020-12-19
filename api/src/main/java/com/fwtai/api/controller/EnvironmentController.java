package com.fwtai.api.controller;

import com.fwtai.config.ConfigFile;
import com.fwtai.entity.EnvironmentBean;
import com.fwtai.entity.ReqPage;
import com.fwtai.service.api.ApiEnvironmentService;
import com.fwtai.tool.ToolClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 环境监测控制层|路由层[api]
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-18 17:32:36
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@Api(tags = "环境监测")
@RequestMapping(ConfigFile.api_v10 +"environment")
public class EnvironmentController{

    @Resource
	private ApiEnvironmentService apiEnvironmentService;

    /**添加*/
    @ApiOperation(value = "添加操作", notes = "新建|新增|添加操作,bean实体form表单方式非json格式提交")
    @PreAuthorize("hasRole('ROLE_APP')")
    @PostMapping("/add")
    public void add(final EnvironmentBean environmentBean,final HttpServletResponse response){
        ToolClient.responseJson(apiEnvironmentService.add(environmentBean),response);
    }

    /**编辑*/
    @ApiOperation(value = "编辑操作", notes = "通过主键kid编辑|修改数据,字段的参照表结构，主键的字段可能不是id或kid,请参考表结构")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "kid", value = "被修改的主键值,其余的参照表结构", dataType = "String", paramType = "query", required = true),
        @ApiImplicitParam(name = "flag", value = "是否已审核(0未审核;1已审核)", dataType = "int", paramType = "query", required = true)
    })
    @PreAuthorize("hasRole('ROLE_APP')")
    @PostMapping("/edit")
    public void edit(final EnvironmentBean environmentBean,final HttpServletResponse response){
        ToolClient.responseJson(apiEnvironmentService.edit(environmentBean),response);
    }

    /**根据id查询对应的数据*/
    @ApiOperation(value = "获取详细信息", notes = "通过id获取详细信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "被查看的id", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/queryById")
    public void queryById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiEnvironmentService.queryById(ToolClient.getFormData(request)),response);
    }

    /**删除-单行*/
    @ApiOperation(value = "删除数据", notes = "通过id删除对应数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "删除数据的id", dataType = "String", paramType = "query", required = true)
    })
    @PreAuthorize("hasRole('ROLE_APP')")
    @PostMapping("/delById")
    public void delById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiEnvironmentService.delById(ToolClient.getFormData(request)),response);
    }

    /**批量删除*/
    @ApiOperation(value = "批量删除", notes = "通过ids删除对应数据,ids是字符串,每个值主键id以英文逗号,隔开;如10001,10002,10003")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "主键的集合以英文逗号,隔开。如10001,10002,10003", dataType = "String", paramType = "query", required = true)
    })
    @PreAuthorize("hasRole('ROLE_APP')")
    @PostMapping("/delByKeys")
    public void delByKeys(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiEnvironmentService.delByKeys(ToolClient.getFormData(request)),response);
    }

    /**获取分页数据,todo 参数reqPage用不到请删除,否则swagger看不到请求的参数*/
    @ApiOperation(value = "获取分页数据", notes = "如需带条件搜索的自行添加对应的字段和值即可,支持多个字段和对应的值")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/listDataPage")
    public void listDataPage(final ReqPage reqPage,final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiEnvironmentService.listDataPage(request),response);
    }

    @ApiOperation(value = "审批审核且提交更新为已审核状态", notes = "ids是字符串,每个值主键kid以英文逗号,隔开;如10001,10002,10003")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "主键的集合以英文逗号,隔开。如10001,10002,10003", dataType = "String", paramType = "query", required = true),
    })
    @PreAuthorize("hasRole('ROLE_APP_SUPER')")
    @PostMapping("/updateBatchAudit")
    public void updateBatchAudit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiEnvironmentService.updateBatchAudit(ToolClient.getFormData(request)),response);
    }
}