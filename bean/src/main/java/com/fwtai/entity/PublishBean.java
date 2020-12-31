package com.fwtai.entity;

import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 发布
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-12-31 10:18
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
 */
public final class PublishBean implements Serializable{

    @ApiModelProperty(notes = "kid主键",required = true,value = "kid主键")
    private String kid;
    @ApiModelProperty(notes = "新冠核酸检测结果",required = true,value = "新冠核酸检测结果(1未检测;2阴性;3阳性)")
    private Integer result;

    public String getKid(){
        return kid;
    }

    public void setKid(String kid){
        this.kid = kid;
    }

    public Integer getResult(){
        return result;
    }

    public void setResult(Integer result){
        this.result = result;
    }
}