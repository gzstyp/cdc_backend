package com.fwtai.api.controller;

import com.fwtai.config.ConfigFile;
import com.fwtai.entity.CrowdTotal;
import com.fwtai.entity.ReqPage;
import com.fwtai.service.api.ApiCrowdTotalService;
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
 * 人群日报控制层|路由层[api]
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-16 11:06:25
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@Api(tags = "人群日报|统计|人群各人群类型统计")
@RequestMapping(ConfigFile.api_v10 +"crowdTotal")
public class CrowdTotalController{

    @Resource
	private ApiCrowdTotalService apiCrowdTotalService;

    /**添加*/
    @ApiOperation(value = "post请求,添加操作", notes = "添加操作")
    @PreAuthorize("hasRole('ROLE_APP') or hasAnyRole('ROLE_APP_SUPER')")
    @PostMapping("/add")
    //public void add(@RequestBody final CrowdTotal crowdTotal,todo 添加@RequestBody注解时请求体就是json格式数据,否则就是表单的提交方式 final HttpServletRequest request,final HttpServletResponse response){
    public void add(final CrowdTotal crowdTotal, final HttpServletRequest request,final HttpServletResponse response){
        //final PageFormData build = new PageFormData().build(request);//todo json格式表体
        /*
        //todo 参考,勿删,此方式接收的是json格式数据
        try { // getReader() has already been called for this request
            final ServletInputStream input = request.getInputStream();
            int length = request.getContentLength();
            final byte[] buffer = new byte[length];
            input.read(buffer, 0, length);
            System.out.println(new String(buffer));//输出的是json格式数据
        } catch (final Exception e) {
            e.printStackTrace();
        }*/
        ToolClient.responseJson(apiCrowdTotalService.add(crowdTotal),response);
    }

    /**编辑*/
    @ApiOperation(value = "编辑操作", notes = "通过主键kid编辑|修改数据,字段的参照表结构，主键的字段可能不是id或kid,请参考表结构")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "kid", value = "被修改的主键值,其余的参照表结构", dataType = "String", paramType = "query", required = true)
    })
    @PreAuthorize("hasRole('ROLE_APP') or hasAnyRole('ROLE_APP_SUPER')")
    @PostMapping("/edit")
    public void edit(final CrowdTotal crowdTotal,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTotalService.edit(crowdTotal),response);
    }

    /*@ApiOperation(value = "审批审核后提交更新", notes = "人群统计审批审核后提交保存,提交后每条记录的flag已标识为1")
    @PreAuthorize("hasRole('ROLE_APP_SUPER')")
    @PostMapping("/editAudit")
    public void editAudit(@RequestBody final List<CrowdTotal> crowdTotals,final HttpServletResponse response){//todo 接收的是json数据
        ToolClient.responseJson(apiCrowdTotalService.editAudit(crowdTotals),response);
    }*/

    /**根据id查询对应的数据*/
    //@ApiOperation(value = "获取详细信息", notes = "通过id获取详细信息")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "被查看的id", dataType = "String", paramType = "query", required = true),
    })
    @GetMapping("/queryById")
    public void queryById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTotalService.queryById(ToolClient.getFormData(request)),response);
    }

    /**删除-单行*/
    //@ApiOperation(value = "删除数据", notes = "通过id删除对应数据")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "id", value = "删除数据的id", dataType = "String", paramType = "query", required = true),
    })
    @PreAuthorize("hasRole('ROLE_APP') or hasAnyRole('ROLE_APP_SUPER')")
    @PostMapping("/delById")
    public void delById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTotalService.delById(ToolClient.getFormData(request)),response);
    }

    /**批量删除*/
    //@ApiOperation(value = "批量删除", notes = "ids是字符串,每个值主键id以英文逗号,隔开;如10001,10002,10003")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "主键的集合以英文逗号,隔开。如10001,10002,10003", dataType = "String", paramType = "query", required = true),
    })
    @PreAuthorize("hasRole('ROLE_APP') or hasAnyRole('ROLE_APP_SUPER')")
    @PostMapping("/delByKeys")
    public void delByKeys(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTotalService.delByKeys(ToolClient.getFormData(request)),response);
    }

    @ApiOperation(value = "审批审核后提交后且更新为已审核", notes = "ids是字符串,每个值主键kid以英文逗号,隔开;如10001,10002,10003")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "ids", value = "主键的集合以英文逗号,隔开。如10001,10002,10003", dataType = "String", paramType = "query", required = true),
    })
    @PreAuthorize("hasRole('ROLE_APP_SUPER')")
    @PostMapping("/editAudit")
    public void editAudit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTotalService.editBatchAudit(ToolClient.getFormData(request)),response);
    }

    /**获取分页数据,todo 参数reqPage用不到请删除,否则swagger看不到请求的参数*/
    @ApiOperation(value = "获取人群分类带分页", notes = "如需带条件搜索的自行添加对应的字段和值即可,支持多个字段和对应的值")
    @GetMapping("/listDataPage")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "crowd_date", value = "登记日期,格式为:2020-12-28", dataType = "String", paramType = "query", required = false)
    })
    public void listDataPage(final ReqPage reqPage,final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTotalService.listDataPage(request),response);
    }

    //当日统计
    @ApiOperation(value = "获取统计日报数据", notes = "获取统计日报数据,可以通过指定人群类型的kid获取对应的人员类型的详细信息,若不传crowd_id、crowd_type_id则获取全部的统计信息")
    @GetMapping("/getListData")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "crowd_id", value = "人群分类的kid,如‘应检尽检’的对应的kid", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "crowd_type_id", value = "人群类型的kid,如‘企业人员’的对应的kid", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "province_id", value = "当前登录者的返回areaData里的province_id", dataType = "long", paramType = "query", required = false),
        @ApiImplicitParam(name = "city_id", value = "当前登录者的返回areaData里的city_id", dataType = "long", paramType = "query", required = false),
        @ApiImplicitParam(name = "county_id", value = "当前登录者的返回areaData里的county_id", dataType = "long", paramType = "query", required = false),
        @ApiImplicitParam(name = "crowd_date", value = "登记日期,格式为:2020-12-28", dataType = "String", paramType = "query", required = false)
    })
    public void getListData(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTotalService.getListData(request),response);
    }

    @ApiOperation(value = "获取统计日报明细", notes = "获取统计日报明细,可以通过指定‘人群分类的kid’和人群类型的kid获取对应的人员类型的详细信息,若不传crowd_id、crowd_type_id则获取全部的统计信息")
    @GetMapping("/getListType")
    @ApiImplicitParams({
        @ApiImplicitParam(name = "crowd_type_id", value = "人群类型的kid,如‘企业人员’的对应的kid", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "province_id", value = "当前登录者的返回areaData里的province_id", dataType = "long", paramType = "query", required = false),
        @ApiImplicitParam(name = "city_id", value = "当前登录者的返回areaData里的city_id", dataType = "long", paramType = "query", required = false),
        @ApiImplicitParam(name = "county_id", value = "当前登录者的返回areaData里的county_id", dataType = "long", paramType = "query", required = false),
        @ApiImplicitParam(name = "crowd_date_start", value = "开始的登记日期,格式为:2020-12-27", dataType = "String", paramType = "query", required = false),
        @ApiImplicitParam(name = "crowd_date_end", value = "结束的登记日期,格式为:2020-12-28", dataType = "String", paramType = "query", required = false)
    })
    public void getListType(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(apiCrowdTotalService.getListType(request),response);
    }
}