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
    @ApiModelProperty(notes = "是否已审核",required = false,value = "是否已审核(0未审核;1已审核)编辑时必填项")
    private Integer flag;
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
    @ApiModelProperty(notes = "监测场所类型(场所类型)",required = true,value = "传递的值是列表场所类型去kid的值")
    private String site_type;

    /*

        audit_user	审核人的userId(sys_user.kid)默认为登记人
        craete_userid	登记人id
        site_type	监测场所类型(场所类型)
        freeze_type	冷库类型[若为冷库请选择类型]
        market_name	市场名称
        vendor_name	摊主姓名
        phone	联系电话
        vendor_code	摊位编号
        source	产品来源地（填写至国家、省、市）
        entrance	是否为进口产品(0否;1是)
        entrance_serial	若为进口产品请填写批号
        sample_name	标本名称（写明具体标本名称，如三文鱼涂抹物）
        freeze_related	是否冷链相关(0否;1是)
        sample_type	标本类型(数据字典)
        sampling_date	采样日期
        detection_date	检测日期
        result	新冠核酸检测结果(1未检测;2阴性;3阳性)
        remark	备注
        modify_userid	修改人(默认为登记人)
        create_date	创建时间
        modify_date	修改时间


     */
}