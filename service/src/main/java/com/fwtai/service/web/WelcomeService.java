package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.tool.ToolClient;
import com.fwtai.web.WelcomeDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class WelcomeService{

    @Resource
    private WelcomeDao welcomeDao;

    public String getData(final PageFormData formData){
        DataFilter.getAreaLevel(formData);
        final HashMap<String,Object> map = new HashMap<>();
        final HashMap<String,Object> environment = welcomeDao.getEnvironment(formData);
        final HashMap<String,Object> employee = welcomeDao.getEmployee(formData);
        final HashMap<String,Object> crowdTotal = welcomeDao.getCrowdTotal(formData);
        final List<Map<String,Object>> typeSample7Day = welcomeDao.typeSample7Day(formData);
        map.put("environment",environment);
        map.put("employee",employee);
        map.put("crowdTotal",crowdTotal);
        map.put("typeSample7Day",typeSample7Day);
        return ToolClient.queryJson(map);
    }
}