package com.fwtai.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 经营场所
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-12-16 19:43
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
 */
public class ManagerLocation implements Serializable{

    @ApiModelProperty(notes = "主键",required = false,value = "kid主键,添加时有后端生成,编辑时必填项")
    private String kid;
    @ApiModelProperty(notes = "登记人id,创建人",required = false,value = "无论在添加或编辑时无需添加,其值从token获取")
    private String craete_userid;
    @ApiModelProperty(notes = "省级id[sys_area的kid]",required = true,value = "当前登录用户绑定的省级id值")
    private Long province_id;
    @ApiModelProperty(notes = "市级id[sys_area的kid]",required = true,value = "当前登录用户绑定的市级id值")
    private Long city_id;
    @ApiModelProperty(notes = "县（区）[sys_area的kid]",required = true,value = "当前登录用户绑定的县（区）id值")
    private Long county_id;
    @ApiModelProperty(notes = "级别1-5,省市县镇村",required = false,value = "级别1-5,分别对应的省市县镇村")
    private Integer area_level;
    @ApiModelProperty(notes = "区域的id",required = true,value = "所选的区域id")
    private Long area_id;
    @ApiModelProperty(notes = "经营场所首写字母",required = false,value = "经营场所首写字母")
    private String site_letter;
    @ApiModelProperty(notes = "经营场所名称",required = true,value = "经营场所名称")
    private String site_name;
    @ApiModelProperty(notes = "经营场所类型",required = true,value = "经营场所类型")
    private String site_type;
    @ApiModelProperty(notes = "联系人",required = true,value = "联系人姓名")
    private String linkman;
    @ApiModelProperty(notes = "联系电话",required = true,value = "联系人联系电话")
    private String mobile;
    @ApiModelProperty(notes = "经营场所的地址",required = true,value = "经营场所的位置地址")
    private String address;
    @ApiModelProperty(notes = "是否有冷冻冷藏产品",required = true,value = "是否有冷冻冷藏产品,1是0否")
    private String freeze;
    @ApiModelProperty(notes = "是否含有冷冻进口产品",required = true,value = "是否含有冷冻进口产品,1是0否")
    private String entrance;
    @ApiModelProperty(notes = "是否含有高中风险地区产品",required = true,value = "是否含有高中风险地区产品,1是0否")
    private String risk;
    @ApiModelProperty(notes = "备注",required = false,value = "经营场所备注")
    private String remark;

    public String getKid(){
        return kid;
    }

    public void setKid(String kid){
        this.kid = kid;
    }

    public String getCraete_userid(){
        return craete_userid;
    }

    public void setCraete_userid(String craete_userid){
        this.craete_userid = craete_userid;
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

    public Integer getArea_level(){
        return area_level;
    }

    public void setArea_level(Integer area_level){
        this.area_level = area_level;
    }

    public Long getArea_id(){
        return area_id;
    }

    public void setArea_id(Long area_id){
        this.area_id = area_id;
    }

    public String getSite_letter(){
        return site_letter;
    }

    public void setSite_letter(String site_letter){
        this.site_letter = site_letter;
    }

    public String getSite_name(){
        return site_name;
    }

    public void setSite_name(String site_name){
        this.site_name = site_name;
    }

    public String getSite_type(){
        return site_type;
    }

    public void setSite_type(String site_type){
        this.site_type = site_type;
    }

    public String getLinkman(){
        return linkman;
    }

    public void setLinkman(String linkman){
        this.linkman = linkman;
    }

    public String getMobile(){
        return mobile;
    }

    public void setMobile(String mobile){
        this.mobile = mobile;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getFreeze(){
        return freeze;
    }

    public void setFreeze(String freeze){
        this.freeze = freeze;
    }

    public String getEntrance(){
        return entrance;
    }

    public void setEntrance(String entrance){
        this.entrance = entrance;
    }

    public String getRisk(){
        return risk;
    }

    public void setRisk(String risk){
        this.risk = risk;
    }

    public String getRemark(){
        return remark;
    }

    public void setRemark(String remark){
        this.remark = remark;
    }
}