package com.fwtai.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

@ApiModel("人群日报|统计|人群各人群类型统计")
public class CrowdTotal implements Serializable{

    @ApiModelProperty(notes = "主键",required = false,value = "人群类型统计主键,添加时有后端生成,编辑时必填项")
    private String kid;
    @ApiModelProperty(notes = "手机端的appid",required = false,value = "手机端的appid,用于标识手机端,若需要添加后返回这appid则传参即可")
    private String appid;
    @ApiModelProperty(notes = "区域级别1-5,省市县镇村",required = true,value = "1-5分别对应省市县镇村")
    private Integer area_level;
    @ApiModelProperty(notes = "省级id[sys_area的kid]",required = true,value = "当前登录人所绑定的省级id[sys_area的kid]")
    private Long province_id;
    @ApiModelProperty(notes = "市（州）[sys_area的kid]",required = true,value = "当前登录人所绑定的市（州）[sys_area的kid]")
    private Long city_id;
    @ApiModelProperty(notes = "县（区）[sys_area的kid]",required = true,value = "当前登录人所绑定的县（区）[sys_area的kid]")
    private Long county_id;
    @ApiModelProperty(notes = "人群分类id",required = true,value = "人群分类id,表bs_crowd的kid主键")
    private String crowd_id;
    @ApiModelProperty(notes = "人群类型id",required = true,value = "人群类型id,表bs_crowd_type的kid主键")
    private String crowd_type_id;
    @ApiModelProperty(notes = "已检测人数",required = true,value = "输入当前已检测人数,数目肯定是少于等于已采样人数")
    private Integer detection_total;
    @ApiModelProperty(notes = "检测阳性人数",required = true,value = "检测阳性人数,数目肯定是少于等于已检测人数")
    private Integer masculine_total;
    @ApiModelProperty(notes = "已采样人数",required = true,value = "已采样人数")
    private Integer sampling_total;
    @ApiModelProperty(notes = "标识是否已审核且发布,返回数据时仅返回flag=0的数据",value = "是否已审核且发布")
    private Integer flag;

    public String getKid(){
        return kid;
    }

    public void setKid(String kid){
        this.kid = kid;
    }

    public String getAppid(){
        return appid;
    }

    public void setAppid(String appid){
        this.appid = appid;
    }

    public Integer getArea_level(){
        return area_level;
    }

    public void setArea_level(Integer area_level){
        this.area_level = area_level;
    }

    public Long getProvince_id(){
        return province_id;
    }

    public void setProvince_id(Long province_id){
        this.province_id = province_id;
    }

    public Long getCity_id(){
        return city_id;
    }

    public void setCity_id(Long city_id){
        this.city_id = city_id;
    }

    public Long getCounty_id(){
        return county_id;
    }

    public void setCounty_id(Long county_id){
        this.county_id = county_id;
    }

    public String getCrowd_id(){
        return crowd_id;
    }

    public void setCrowd_id(String crowd_id){
        this.crowd_id = crowd_id;
    }

    public String getCrowd_type_id(){
        return crowd_type_id;
    }

    public void setCrowd_type_id(String crowd_type_id){
        this.crowd_type_id = crowd_type_id;
    }

    public Integer getDetection_total(){
        return detection_total;
    }

    public void setDetection_total(Integer detection_total){
        this.detection_total = detection_total;
    }

    public Integer getMasculine_total(){
        return masculine_total;
    }

    public void setMasculine_total(Integer masculine_total){
        this.masculine_total = masculine_total;
    }

    public Integer getSampling_total(){
        return sampling_total;
    }

    public void setSampling_total(Integer sampling_total){
        this.sampling_total = sampling_total;
    }

    public Integer getFlag(){
        return flag;
    }

    public void setFlag(Integer flag){
        this.flag = flag;
    }
}