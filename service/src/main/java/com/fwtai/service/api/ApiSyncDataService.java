package com.fwtai.service.api;

import com.fwtai.api.ApiSyncDataDao;
import com.fwtai.bean.PageFormData;
import com.fwtai.tool.ToolClient;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 基础数据同步
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-12-16 18:59
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
 */
@Service
public class ApiSyncDataService{

    @Resource
    private ApiSyncDataDao apiSyncDataDao;

    /** 获取人群分类基础数据 */
    public String getCrowdCategory(final PageFormData formData){
        return ToolClient.queryJson(apiSyncDataDao.getCrowdCategory(formData));
    }

    /** 获取人群类型 */
    public String getCrowdType(final PageFormData formData){
        return ToolClient.queryJson(apiSyncDataDao.getCrowdType(formData));
    }

    /** 获取经营场所数据 */
    public String getManagerArea(final PageFormData formData){
        final String areaId = formData.getString("areaId");
        if(areaId == null) return ToolClient.jsonValidateField();
        return ToolClient.queryJson(apiSyncDataDao.getManagerArea(formData));
    }

    /** 获取经营场所类型数据 */
    public String getManagerLocation(){
        return ToolClient.queryJson(apiSyncDataDao.getManagerLocation());
    }

    //冷库类型,用于环境监测或从业人员监测
    public String getFreezeType(){
        return ToolClient.queryJson(apiSyncDataDao.getFreezeType());
    }

    //标本类型(用于环境监测)
    public String getSpecimenType(){
        return ToolClient.queryJson(apiSyncDataDao.getSpecimenType());
    }

    //样本类型(用于从业人员)
    public String getSampleType(){
        return ToolClient.queryJson(apiSyncDataDao.getSampleType());
    }

    //工种(用于从业人员)
    public String getProfession(){
        return ToolClient.queryJson(apiSyncDataDao.getProfession());
    }
}