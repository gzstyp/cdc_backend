package com.fwtai.web;

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
public class ManageLocationDao{

    @Resource
    private DaoHandle dao;

    public int add(final PageFormData pageFormData){
        return dao.execute("bs_manage.add",pageFormData);
    }

    public String queryExistById(final String kid){
        return dao.queryForString("bs_manage.queryExistById",kid);
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("bs_manage.edit",pageFormData);
    }

    public HashMap<String, Object> queryById(final String kid){
        return dao.queryForHashMap("bs_manage.queryById",kid);
    }

    public int delById(final String kid){
        return dao.execute("bs_manage.delById",kid);
    }

    public int delByKeys(final ArrayList<String> list){
        return dao.execute("bs_manage.delByKeys",list);
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"bs_manage.listData","bs_manage.listTotal");
    }

    //获取经营场所
    public List<HashMap<String, Object>> getManagerLocation(){
        return dao.queryForListHashMap("bs_manage.getManagerLocation");
    }

    public List<HashMap<String,String>> queryIdsByName(final ArrayList<String> names){
        return dao.queryForListString("sys_dict.queryIdsByName",names);
    }

    public int addExcel(final ArrayList<HashMap<String,Object>> list){
        return dao.execute("bs_manage.addExcel",list);
    }
}