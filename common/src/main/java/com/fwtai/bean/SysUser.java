package com.fwtai.bean;

public final class SysUser{

    private String kid;

    private String userName;

    private String userPassword;

    private Integer enabled;

    private Integer errorCount;

    private String errorTime;

    private Integer audit;//是否具有审核功能权限

    private Long areaId;//区域的id

    private String areaName;

    private Integer areaLevel;

    private Long cityId;

    private Long countyId;

    private Long provinceId;

    public String getKid(){
        return kid;
    }

    public void setKid(String kid){
        this.kid = kid;
    }

    public String getUserName(){
        return userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getUserPassword(){
        return userPassword;
    }

    public void setUserPassword(String userPassword){
        this.userPassword = userPassword;
    }

    public Integer getEnabled(){
        return enabled;
    }

    public void setEnabled(Integer enabled){
        this.enabled = enabled;
    }

    public Integer getErrorCount(){
        return errorCount;
    }

    public void setErrorCount(Integer errorCount){
        this.errorCount = errorCount;
    }

    public String getErrorTime(){
        return errorTime;
    }

    public void setErrorTime(String errorTime){
        this.errorTime = errorTime;
    }

    public Integer getAudit(){
        return audit;
    }

    public void setAudit(Integer audit){
        this.audit = audit;
    }

    public Long getAreaId(){
        return areaId;
    }

    public void setAreaId(Long areaId){
        this.areaId = areaId;
    }

    public String getAreaName(){
        return areaName;
    }

    public void setAreaName(String areaName){
        this.areaName = areaName;
    }

    public Integer getAreaLevel(){
        return areaLevel;
    }

    public void setAreaLevel(Integer areaLevel){
        this.areaLevel = areaLevel;
    }

    public Long getCityId(){
        return cityId;
    }

    public void setCityId(Long cityId){
        this.cityId = cityId;
    }

    public Long getCountyId(){
        return countyId;
    }

    public void setCountyId(Long countyId){
        this.countyId = countyId;
    }

    public Long getProvinceId(){
        return provinceId;
    }

    public void setProvinceId(Long provinceId){
        this.provinceId = provinceId;
    }
}