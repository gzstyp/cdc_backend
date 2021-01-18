package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.AreaLevel;

public final class DataFilter{

    static void getAreaLevel(final PageFormData formData){
        final Integer areaLevel = AreaLevel.get();
        final Object areaKid = formData.get("areaKid");
        if(areaLevel != null)
        switch (areaLevel){
            case 1:
                formData.put("areaProvince",areaKid);
                break;
            case 2:
                formData.put("areaCity",areaKid);
                break;
            case 3:
                formData.put("areaCounty",areaKid);
                break;
            default:
                break;
        }
    }
}