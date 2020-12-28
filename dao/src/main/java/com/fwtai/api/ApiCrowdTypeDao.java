package com.fwtai.api;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 人群类型访问数据库
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/9 13:43
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class ApiCrowdTypeDao{

    @Resource
    private DaoHandle dao;

    public int add(final PageFormData pageFormData){
        return dao.execute("api_bs_crowd_type.add",pageFormData);
    }

    public String queryExistById(final String kid){
        return dao.queryForString("api_bs_crowd_type.queryExistById",kid);
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("api_bs_crowd_type.edit",pageFormData);
    }

    public HashMap<String, Object> queryById(final String kid){
        return dao.queryForHashMap("api_bs_crowd_type.queryById",kid);
    }

    public int delById(final String kid){
        return dao.execute("api_bs_crowd_type.delById",kid);
    }

    public int delByKeys(final ArrayList<String> list){
        return dao.execute("api_bs_crowd_type.delByKeys",list);
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"api_bs_crowd_type.listData","api_bs_crowd_type.listTotal");
    }

    //获取全部人群类型
    public List<HashMap<String, Object>> getList(final PageFormData formData){
        return dao.queryForListHashMap("api_bs_crowd_type.getList",formData);
    }
}