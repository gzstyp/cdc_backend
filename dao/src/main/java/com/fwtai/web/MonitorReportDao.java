package com.fwtai.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.datasource.DaoHandle;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Repository
public class MonitorReportDao{

    @Resource
    private DaoHandle dao;

    /**从业人员监测结果*/
    public List<HashMap<String,Object>> queryEmployeeReport(final PageFormData formData){
        return dao.queryForListHashMap("reportTotal.queryEmployeeReport",formData);
    }

    /**查询登录者所拥有的权限*/
    public List<String> queryPermissions(){
        return dao.queryPermissions("monitorReport/queryPermissions");//指定当前子菜单url[当然也可以指定controller层的自己url]
    }
}