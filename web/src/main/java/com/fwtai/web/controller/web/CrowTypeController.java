package com.fwtai.web.controller.web;

import com.fwtai.service.web.CrowTypeService;
import com.fwtai.tool.ToolClient;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 人群类型控制层|路由层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-15 14:05:18
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@RequestMapping("/crowType")
public class CrowTypeController{

    @Resource
	private CrowTypeService crowtypeService;

    /**添加*/
    @PreAuthorize("hasAuthority('crowType_btn_add')")
    @PostMapping("/add")
    public void add(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(crowtypeService.add(request),response);
    }

    /**编辑*/
    @PreAuthorize("hasAuthority('crowType_row_edit')")
    @PostMapping("/edit")
    public void edit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(crowtypeService.edit(request),response);
    }

    /**根据id查询对应的数据*/
    @PreAuthorize("hasAuthority('crowType_row_queryById')")
    @GetMapping("/queryById")
    public void queryById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(crowtypeService.queryById(ToolClient.getFormData(request)),response);
    }

    /**删除-单行*/
    @PreAuthorize("hasAuthority('crowType_row_delById')")
    @PostMapping("/delById")
    public void delById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(crowtypeService.delById(ToolClient.getFormData(request)),response);
    }

    /**批量删除*/
    @PreAuthorize("hasAuthority('crowType_btn_delByKeys')")
    @PostMapping("/delByKeys")
    public void delByKeys(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(crowtypeService.delByKeys(ToolClient.getFormData(request)),response);
    }

    /**获取数据*/
    @PreAuthorize("hasAuthority('crowType_btn_listData')")
    @GetMapping("/listData")
    public void listData(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(crowtypeService.listData(ToolClient.getFormData(request)),response);
    }

    /**获取人群分类*/
    @PreAuthorize("hasAuthority('crowType_btn_getCrowdCategory')")
    @GetMapping("/getCrowdCategory")
    public void getCrowdCategory(final HttpServletResponse response){
        ToolClient.responseJson(crowtypeService.getCrowdCategory(),response);
    }

    @GetMapping("/notAuthorized")
    public void notAuthorized(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.notAuthorized(),response);
    }
}