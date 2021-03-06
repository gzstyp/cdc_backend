package com.fwtai.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 环境监测访问数据库
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/9 13:43
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class EnvironmentDao{

    @Resource
    private DaoHandle dao;

    public int add(final PageFormData pageFormData){
        return dao.execute("bs_environment.add",pageFormData);
    }

    public String queryExistById(final String kid){
        return dao.queryForString("bs_environment.queryExistById",kid);
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("bs_environment.edit",pageFormData);
    }

    public HashMap<String, Object> queryById(final String kid){
        return dao.queryForHashMap("bs_environment.queryById",kid);
    }

    public int delById(final String kid){
        return dao.execute("bs_environment.delById",kid);
    }

    public int delByKeys(final ArrayList<String> list){
        return dao.execute("bs_environment.delByKeys",list);
    }

    public int editPositive(final ArrayList<String> list){
        return dao.execute("bs_environment.editPositive",list);
    }

    public int editNegative(final PageFormData formData){
        return dao.execute("bs_environment.editNegative",formData);
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"bs_environment.listData","bs_environment.listTotal");
    }

    public List<Map<String, Object>> queryDataExport(final PageFormData formData){
        return dao.queryForListMap("bs_environment.queryDataExport",formData);
    }
}