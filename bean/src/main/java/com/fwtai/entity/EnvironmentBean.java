package com.fwtai.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 环境检测实体
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-12-18 17:41
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
 */
public final class EnvironmentBean implements Serializable{

    @ApiModelProperty(notes = "主键",required = false,value = "kid主键,添加时有后端生成,编辑时必填项")
    private String kid;
    @ApiModelProperty(notes = "手机端的appid",required = true,value = "手机端的appid,用于标识手机端")
    private String appid;
    @ApiModelProperty(notes = "是否已审核",required = false,value = "是否已审核(0未审核;1已审核),默认为0未审核")
    private Integer flag;
    @ApiModelProperty(notes = "",required = false,value = "审核人的userId,默认为登记人")
    private String audit_user;
    @ApiModelProperty(notes = "标本实验编号",required = true,value = "标本实验编号")
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
    @ApiModelProperty(notes = "监测场所类型(场所类型)",required = true,value = "传递的值是列表场所类型取值是(经营场所类型数据)的kid的值")
    private String site_type;
    @ApiModelProperty(notes = "冷库类型",required = false,value = "冷库类型[若监测场所类型为冷库请选择[经营场所类型]数据的kid的值")
    private String freeze_type;
    @ApiModelProperty(notes = "市场名称",required = true,value = "市场名称[即经营场所名称],通过首写字母搜索得下拉列表获取保存所选的kid")
    private String market_name;
    @ApiModelProperty(notes = "摊主姓名",required = true,value = "摊主姓名")
    private String vendor_name;
    @ApiModelProperty(notes = "摊主联系电话",required = true,value = "摊主联系电话")
    private String phone;
    @ApiModelProperty(notes = "摊位编号",required = false,value = "摊位编号")
    private String vendor_code;
    @ApiModelProperty(notes = "产品来源地",required = false,value = "产品来源地（填写至国家、省、市）")
    private String source;
    @ApiModelProperty(notes = "是否为进口产品",required = true,value = "是否为进口产品(0否;1是)")
    private String entrance;
    @ApiModelProperty(notes = "若为进口产品请填写批号",required = false,value = "若为进口产品请填写批号")
    private String entrance_serial;
    @ApiModelProperty(notes = "标本名称",required = true,value = "标本名称（写明具体标本名称，如三文鱼涂抹物）")
    private String sample_name;
    @ApiModelProperty(notes = "是否冷链相关",required = true,value = "是否冷链相关(0否;1是)")
    private Integer freeze_related;
    @ApiModelProperty(notes = "标本类型",required = true,value = "标本类型[取值来自于基础数据的'标本类型(用于环境监测)'的某个kid值]")
    private String sample_type;
    @ApiModelProperty(notes = "采样日期",required = false,value = "采样日期,格式:2020-12-19",example = "2020-12-19")
    private String sampling_date;
    @ApiModelProperty(notes = "检测日期",required = false,value = "检测日期,格式:2020-12-19",example = "2020-12-19")
    private String detection_date;
    @ApiModelProperty(notes = "新冠核酸检测结果",required = true,value = "新冠核酸检测结果(1未检测;2阴性;3阳性),默认为11未检测")
    private Integer result;
    @ApiModelProperty(notes = "备注说明",required = false,value = "备注说明")
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

    public String getSite_type(){
        return site_type;
    }

    public void setSite_type(String site_type){
        this.site_type = site_type;
    }

    public String getFreeze_type(){
        return freeze_type;
    }

    public void setFreeze_type(String freeze_type){
        this.freeze_type = freeze_type;
    }

    public String getMarket_name(){
        return market_name;
    }

    public void setMarket_name(String market_name){
        this.market_name = market_name;
    }

    public String getVendor_name(){
        return vendor_name;
    }

    public void setVendor_name(String vendor_name){
        this.vendor_name = vendor_name;
    }

    public String getPhone(){
        return phone;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public String getVendor_code(){
        return vendor_code;
    }

    public void setVendor_code(String vendor_code){
        this.vendor_code = vendor_code;
    }

    public String getSource(){
        return source;
    }

    public void setSource(String source){
        this.source = source;
    }

    public String getEntrance(){
        return entrance;
    }

    public void setEntrance(String entrance){
        this.entrance = entrance;
    }

    public String getEntrance_serial(){
        return entrance_serial;
    }

    public void setEntrance_serial(String entrance_serial){
        this.entrance_serial = entrance_serial;
    }

    public String getSample_name(){
        return sample_name;
    }

    public void setSample_name(String sample_name){
        this.sample_name = sample_name;
    }

    public Integer getFreeze_related(){
        return freeze_related;
    }

    public void setFreeze_related(Integer freeze_related){
        this.freeze_related = freeze_related;
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