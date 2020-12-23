package com.fwtai.web.controller.web;

import com.fwtai.service.core.DictionaryService;
import com.fwtai.service.core.UserService;
import com.fwtai.service.web.EmployeeService;
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
 * 从业人员控制层|路由层
 * @作者 田应平
 * @版本 v1.0
 * @QQ号码 444141300
 * @创建日期 2020-12-23 10:37:06
 * @官网 <url>http://www.yinlz.com</url>
*/
@RestController
@RequestMapping("/employee")
public class EmployeeController{

    @Resource
	private EmployeeService employeeService;

    @Resource
    private DictionaryService dictionaryService;

    @Resource
    private UserService userService;

    /**添加*/
    @PreAuthorize("hasAuthority('employee_btn_add')")
    @PostMapping("/add")
    public void add(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(employeeService.add(request),response);
    }

    /**编辑*/
    @PreAuthorize("hasAuthority('employee_row_edit')")
    @PostMapping("/edit")
    public void edit(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(employeeService.edit(request),response);
    }

    /**根据id查询对应的数据*/
    @PreAuthorize("hasAuthority('employee_row_queryById')")
    @GetMapping("/queryById")
    public void queryById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(employeeService.queryById(ToolClient.getFormData(request)),response);
    }

    /**删除-单行*/
    @PreAuthorize("hasAuthority('employee_row_delById')")
    @PostMapping("/delById")
    public void delById(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(employeeService.delById(ToolClient.getFormData(request)),response);
    }

    /**批量删除*/
    @PreAuthorize("hasAuthority('employee_btn_delByKeys')")
    @PostMapping("/delByKeys")
    public void delByKeys(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(employeeService.delByKeys(ToolClient.getFormData(request)),response);
    }

    /**获取数据*/
    @PreAuthorize("hasAuthority('employee_btn_listData')")
    @GetMapping("/listData")
    public void listData(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(employeeService.listData(ToolClient.getFormData(request)),response);
    }

    @GetMapping("/notAuthorized")
    public void notAuthorized(final HttpServletResponse response){
        ToolClient.responseJson(ToolClient.notAuthorized(),response);
    }

    /**根据字典父级id获取数据*/
    @PreAuthorize("hasAuthority('employee_btn_getDicts')")
    @GetMapping("/getDicts")
    public void getDicts(final String pid,final HttpServletResponse response){
        ToolClient.responseJson(dictionaryService.queryDictData(pid),response);
    }

    /**获取区域数据*/
    @PreAuthorize("hasAuthority('employee_btn_row_areaSelect')")
    @GetMapping("/queryAreaSelect")
    public void queryArea(final HttpServletRequest request,final HttpServletResponse response){
        ToolClient.responseJson(userService.queryArea(ToolClient.getFormData(request)),response);
    }
}