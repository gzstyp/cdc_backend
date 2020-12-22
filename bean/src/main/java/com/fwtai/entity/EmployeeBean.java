package com.fwtai.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

public final class EmployeeBean implements Serializable{

    @ApiModelProperty(notes = "主键",required = false,value = "kid主键,添加时有后端生成,编辑时必填项")
    private String kid;
    @ApiModelProperty(notes = "手机端的appid",required = true,value = "手机端的appid,用于标识手机端")
    private String appid;
    @ApiModelProperty(notes = "是否已审核",required = false,value = "是否已审核(0未审核;1已审核),默认为0未审核")
    private Integer flag;
    @ApiModelProperty(required = false,value = "审核人的userId,默认为登记人")
    private String audit_user;
    @ApiModelProperty(required = true,value = "标本实验编号")
    private String sample_code;
    @ApiModelProperty(notes = "区域的主键kid",required = true,value = "取值是用当前登录成功后返回的areaData里的kid值")
    private Long area_id;
    @ApiModelProperty(notes = "省（市）主键值",required = true,value = "取值是用当前登录成功后返回的areaData里的province_id值")
    private Long province_id;
    @ApiModelProperty(notes = "市（州）主键值",required = true,value = "取值是用当前登录成功后返回的areaData里的city_id值")
    private Long city_id;
    @ApiModelProperty(notes = "县（区）主键值",required = true,value = "取值是用当前登录成功后返回的areaData里的county_id值")
    private Long county_id;
    @ApiModelProperty(notes = "级别1-5对应的是省市县镇村",required = true,value = "取值是用当前登录成功后返回的areaData里的area_level值")
    private Long area_level;
    @ApiModelProperty(required = true,value = "人员姓名")
    private String real_name;
    @ApiModelProperty(required = true,value = "联系电话")
    private String phone;
    @ApiModelProperty(required = true,value = "人员性别(1男;2女)")
    private Integer gender;
    @ApiModelProperty(required = true,value = "人员年龄")
    private Integer age;
    @ApiModelProperty(required = true,value = "从业场所名称,从下拉列表获取直接保存文字,无需保存所选的kid")
    private String work_site;
    @ApiModelProperty(required = true,value = "从业场所类型(场所类型),取值于某个是列表场所类型(经营场所类型)的kid的值")
    private String work_type;
    @ApiModelProperty(required = false,value = "冷库类型[若监测场所类型为冷库请选择[经营场所类型]数据的kid的值")
    private String freeze_type;
    @ApiModelProperty(notes = "工种",required = true,value = "工种,取值域‘基础数据’的工种的某个kid的值")
    private String profession;
    @ApiModelProperty(required = true,value = "是否冷链相关从业人员(0否;1是)")
    private Integer cold_chain;
    @ApiModelProperty(required = true,value = "样本类型(来源数据字典)")
    private String sample_type;
    @ApiModelProperty(notes = "采样日期",required = true,value = "采样日期,格式:2020-12-19",example = "2020-12-19")
    private String sampling_date;
    @ApiModelProperty(notes = "检测日期",required = false,value = "检测日期,格式:2020-12-19",example = "2020-12-19")
    private String detection_date;
    @ApiModelProperty(required = true,value = "新冠核酸检测结果(1未检测;2阴性;3阳性),默认为1未检测")
    private Integer result;
    @ApiModelProperty(required = false,value = "备注")
    private String remark;

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

    public Integer getFlag(){
        return flag;
    }

    public void setFlag(Integer flag){
        this.flag = flag;
    }

    public String getAudit_user(){
        return audit_user;
    }

    public void setAudit_user(String audit_user){
        this.audit_user = audit_user;
    }

    public String getSample_code(){
        return sample_code;
    }

    public void setSample_code(String sample_code){
        this.sample_code = sample_code;
    }

    public Long getArea_id(){
        return area_id;
    }

    public void setArea_id(Long area_id){
        this.area_id = area_id;
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

    public Long getArea_level(){
        return area_level;
    }

    public void setArea_level(Long area_level){
        this.area_level = area_level;
    }

    public String getReal_name(){
        return real_name;
    }

    public void setReal_name(String real_name){
        this.real_name = real_name;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public Integer getGender(){
        return gender;
    }

    public void setGender(Integer gender){
        this.gender = gender;
    }

    public Integer getAge(){
        return age;
    }

    public void setAge(Integer age){
        this.age = age;
    }

    public String getWork_site(){
        return work_site;
    }

    public void setWork_site(String work_site){
        this.work_site = work_site;
    }

    public String getWork_type(){
        return work_type;
    }

    public void setWork_type(String work_type){
        this.work_type = work_type;
    }

    public String getFreeze_type(){
        return freeze_type;
    }

    public void setFreeze_type(String freeze_type){
        this.freeze_type = freeze_type;
    }

    public String getProfession(){
        return profession;
    }

    public void setProfession(String profession){
        this.profession = profession;
    }

    public Integer getCold_chain(){
        return cold_chain;
    }

    public void setCold_chain(Integer cold_chain){
        this.cold_chain = cold_chain;
    }

    public String getSample_type(){
        return sample_type;
    }

    public void setSample_type(String sample_type){
        this.sample_type = sample_type;
    }

    public String getSampling_date(){
        return sampling_date;
    }

    public void setSampling_date(String sampling_date){
        this.sampling_date = sampling_date;
    }

    public String getDetection_date(){
        return detection_date;
    }

    public void setDetection_date(String detection_date){
        this.detection_date = detection_date;
    }

    public Integer getResult(){
        return result;
    }

    public void setResult(Integer result){
        this.result = result;
    }

    public String getRemark(){
        return remark;
    }

    public void setRemark(String remark){
        this.remark = remark;
    }
}