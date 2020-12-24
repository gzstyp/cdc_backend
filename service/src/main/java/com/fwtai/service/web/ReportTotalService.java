package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.tool.ToolClient;
import com.fwtai.web.ReportTotalDao;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ReportTotalService{

    @Resource
    private ReportTotalDao reportTotalDao;

    public String getView(final PageFormData formData){
        final String p_crowd_date = "crowd_date";
        String crowd_date = formData.getString(p_crowd_date);
        if(crowd_date == null){
            crowd_date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
            formData.put(p_crowd_date,crowd_date);
        }
        return ToolClient.queryJson(reportTotalDao.getView(formData));
    }
}