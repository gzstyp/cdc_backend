package com.fwtai.api.controller;

import com.fwtai.config.ConfigFile;
import com.fwtai.entity.EmployeeBean;
import com.fwtai.entity.PublishBean;
import com.fwtai.entity.ReqPage;
import com.fwtai.service.api.ApiEmployeeService;
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
import java.util.ArrayList;

/**
 * 从业人员控制层|路由层[api]
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-19 14:48:44
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@Api(tags = "从业人员")
@RequestMapping(ConfigFile.api_v10 +"employee")
public class EmployeeController{

    @Resource
	private ApiEmployeeService apiEmployeeService;

    /**添加*/
    @ApiOperation(value = "添加操作", notes = "新建|新增|添加操作,bean实体form表单方式非json格式提交")
    @PreAuthorize("hasRole('ROLE_APP')")
    @PostMapping("/add")
    public void add(final EmployeeBean employeeBean,final HttpServletResponse response){
        ToolClient.responseJson(apiEmployeeService.add(employeeBean),response);
    }

    /**编辑*/
    @ApiOperation(value = "编辑操作", notes = "通过主键kid编辑|修改数据,字段的参照表结构，主键的字段可能不是id或kid,请参考表结构")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "kid", value = "被修改的主键值,其余的参照表结构", dataType = "String", paramType = "query", required = true)
    })
    @PreAuthorize("hasRole('ROLE_APP')")
    @PostMapping("/edit")
    public void edit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiEmployeeService.edit(request),response);
    }

    /**根据主键kid查询对应的数据*/
    @ApiOperation(value = "获取详细信息", notes = "通过kid获取详细信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "被查看的kid", dataType = "String", paramType = "query", required = true)
    })
    @GetMapping("/queryById")
    public void queryById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiEmployeeService.queryById(ToolClient.getFormData(request)),response);
    }

    /**删除-单行*/
    @ApiOperation(value = "删除数据", notes = "通过id删除对应数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "删除数据的id", dataType = "String", paramType = "query", required = true)
    })
    @PreAuthorize("hasRole('ROLE_APP')")
    @PostMapping("/delById")
    public void delById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiEmployeeService.delById(ToolClient.getFormData(request)),response);
    }

    /**批量删除*/
    @ApiOperation(value = "批量删除", notes = "通过ids删除对应数据,ids是字符串,每个值主键kid以英文逗号,隔开;如10001,10002,10003")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "主键的集合以英文逗号,隔开。如10001,10002,10003", dataType = "String", paramType = "query", required = true)
    })
    @PreAuthorize("hasRole('ROLE_APP')")
    @PostMapping("/delByKeys")
    public void delByKeys(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiEmployeeService.delByKeys(ToolClient.getFormData(request)),response);
    }

    @ApiOperation(value = "发布接口,仅修改检测结果result的字段", notes = "发布接口,仅修改检测结果result的字段,数据格式:[{\"kid\":\"1024\",\"result\":2},{\"kid\":\"1024\",\"result\":3}]")
    @PreAuthorize("hasRole('ROLE_APP') or hasAnyRole('ROLE_APP_SUPER')")
    @PostMapping("/editPublish")
    public void editPublish(@RequestBody final ArrayList<PublishBean> lists,final HttpServletResponse response){
        ToolClient.responseJson(apiEmployeeService.editPublish(lists),response);
    }

    /**获取分页数据,请勿删除ReqPage否则swagger不显示参数*/
    @ApiOperation(value = "获取分页数据", notes = "如需带条件搜索的自行添加对应的字段和值即可,支持多个字段和对应的值")
    @PreAuthorize("hasRole('ROLE_APP')")
    @GetMapping("/listDataPage")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "market_name", value = "从业场所名称[经营场所名称]", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "site_letter", value = "从业场所名称的首写字母[经营场所名称的首写字母]", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "sampling_date_start", value = "采样日期开始日期,格式为:2020-12-01", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "sampling_date_end", value = "采样日期结束日期,格式为:2020-12-01", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "sample_code", value = "样本编号", dataType = "String", paramType = "query", required = false)
    })
    public void listDataPage(final ReqPage reqPage,final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiEmployeeService.listDataPage(request),response);
    }

    @ApiOperation(value = "审批审核且提交更新为已审核状态", notes = "ids是字符串,每个值主键kid以英文逗号,隔开;如10001,10002,10003")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "主键的集合以英文逗号,隔开。如10001,10002,10003", dataType = "String", paramType = "query", required = true),
    })
    @PreAuthorize("hasRole('ROLE_APP_SUPER')")
    @PostMapping("/updateEmployeeAudit")
    public void updateEmployeeAudit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiEmployeeService.updateEmployeeAudit(ToolClient.getFormData(request)),response);
    }
}