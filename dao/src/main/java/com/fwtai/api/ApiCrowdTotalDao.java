package com.fwtai.api;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 人群日报访问数据库
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020/4/9 13:43
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
@Repository
public class ApiCrowdTotalDao{

    @Resource
    private DaoHandle dao;

    public int add(final PageFormData pageFormData){
        return dao.execute("api_bs_crowd_total.add",pageFormData);
    }

    public String queryExistById(final String kid){
        return dao.queryForString("api_bs_crowd_total.queryExistById",kid);
    }

    public int edit(final PageFormData pageFormData){
        return dao.execute("api_bs_crowd_total.edit",pageFormData);
    }

    public HashMap<String, Object> queryById(final String kid){
        return dao.queryForHashMap("api_bs_crowd_total.queryById",kid);
    }

    public int delById(final String kid){
        return dao.execute("api_bs_crowd_total.delById",kid);
    }

    public int delByKeys(final ArrayList<String> list){
        return dao.execute("api_bs_crowd_total.delByKeys",list);
    }

    public int editBatchAudit(final HashMap<String,Object> map){
        return dao.execute("api_bs_crowd_total.editBatchAudit",map);
    }

    public HashMap<String,Object> listData(final PageFormData pageFormData){
        return dao.queryForPage(pageFormData,"api_bs_crowd_total.listData","api_bs_crowd_total.listTotal");
    }

    public List<HashMap<String,Object>> getListData(final PageFormData formData){
        return dao.queryForListHashMap("api_bs_crowd_total.getListData",formData);
    }

    public List<HashMap<String,Object>> getList(final PageFormData formData){
        return dao.queryForListHashMap("api_bs_crowd_total.getList",formData);
    }

    public List<HashMap<String,Object>> getListType(final PageFormData formData){
        return dao.queryForListHashMap("api_bs_crowd_total.getListType",formData);
    }
}