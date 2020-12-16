package com.fwtai.web.controller.web;

import com.fwtai.service.web.CrowdCategoryService;
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
 * 人群分类控制层|路由层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-15 13:40:39
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@RequestMapping("/crowdCategory")
public class CrowdCategoryController{

    @Resource
	private CrowdCategoryService crowdcategoryService;

    /**添加*/
    @PreAuthorize("hasAuthority('crowdCategory_btn_add')")
    @PostMapping("/add")
    public void add(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(crowdcategoryService.add(request),response);
    }

    /**编辑*/
    @PreAuthorize("hasAuthority('crowdCategory_row_edit')")
    @PostMapping("/edit")
    public void edit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(crowdcategoryService.edit(request),response);
    }

    /**根据id查询对应的数据*/
    @PreAuthorize("hasAuthority('crowdCategory_row_queryById')")
    @GetMapping("/queryById")
    public void queryById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(crowdcategoryService.queryById(ToolClient.getFormData(request)),response);
    }

    /**删除-单行*/
    @PreAuthorize("hasAuthority('crowdCategory_row_delById')")
    @PostMapping("/delById")
    public void delById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(crowdcategoryService.delById(ToolClient.getFormData(request)),response);
    }

    /**批量删除*/
    @PreAuthorize("hasAuthority('crowdCategory_btn_delByKeys')")
    @PostMapping("/delByKeys")
    public void delByKeys(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(crowdcategoryService.delByKeys(ToolClient.getFormData(request)),response);
    }

    /**获取数据*/
    @PreAuthorize("hasAuthority('crowdCategory_btn_listData')")
    @GetMapping("/listData")
    public void listData(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(crowdcategoryService.listData(ToolClient.getFormData(request)),response);
    }

    @GetMapping("/notAuthorized")
    public void notAuthorized(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.notAuthorized(),response);
    }
}