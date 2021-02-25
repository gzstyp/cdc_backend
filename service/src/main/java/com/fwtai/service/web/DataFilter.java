package com.fwtai.service.web;

import com.fwtai.bean.PageFormData;
import com.fwtai.config.AreaLevel;

public final class DataFilter{

    static void getAreaLevel(final PageFormData formData){
        final Integer areaLevel = AreaLevel.get();
        final Object areaKid = formData.get("areaKid");
        formData.remove("areaKid");
        if(areaLevel != null)
        switch (areaLevel){
            case 1:
                if(areaKid != null)
                formData.put("areaProvince",areaKid);
                break;
            case 2:
                if(areaKid != null)
                formData.put("areaCity",areaKid);
                break;
            case 3:
                if(areaKid != null)
                formData.put("areaCounty",areaKid);
                break;
            default:
                break;
        }
    }
}