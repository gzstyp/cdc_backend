package com.fwtai.api;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 经营场所访问数据库
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/9 13:43
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class ApiManageLocationDao{

    @Resource
    private DaoHandle dao;

    public int add(final PageFormData pageFormData){
        return dao.execute("api_bs_manage.add",pageFormData);
    }

    public String queryExistById(final String kid){
        return dao.queryForString("api_bs_manage.queryExistById",kid);
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("api_bs_manage.edit",pageFormData);
    }

    public HashMap<String, Object> queryById(final String kid){
        return dao.queryForHashMap("api_bs_manage.queryById",kid);
    }

    public int delById(final String kid){
        return dao.execute("api_bs_manage.delById",kid);
    }

    public int delByKeys(final ArrayList<String> list){
        return dao.execute("api_bs_manage.delByKeys",list);
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"api_bs_manage.listData","api_bs_manage.listTotal");
    }

    public List<HashMap<String,Object>> listDataPage(final PageFormData formData){
        return dao.queryForListHashMap("api_bs_manage.listDataPage",formData);
    }
}