package com.fwtai.poi;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalInt;

/**
 * word导出
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021-01-06 18:19
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
public final class WordExport{

    public static void exportWord(final String start,final String end,final List<HashMap<String,Object>> listEmployee,final List<HashMap<String,Object>> listSiteType,final String fileName,final HttpServletResponse response) throws Exception{
        final XWPFDocument doc = new XWPFDocument();//创建新文档
        final String title = "贵州省冷冻冷藏肉品新冠病毒监测结果报告";
        ToolWord.singleRow(doc,title,18,ParagraphAlignment.CENTER,true,true);

        final String describe = "为了贯彻落实国务院联防联控机制《关于开展冷冻冷藏肉品风险排查的通知》（联防联控机制综发〔2020〕210 号）文件的要求。为科学规范指导开展我省新冠病毒环境监测工作，深入开展病毒溯源和疫情防控，2020年8月10日贵州省卫生健康委员会联合多部委下发了《贵州省农贸（集贸）市场及冷冻冷藏产品新冠病毒环境和人员监测方案》。要求各市（州）及县（区），为加强对农贸（集贸）市场及大型食品冷库新冠病毒环境监测。现将我省此次新冠肺炎监测结果报告如下，采样时间为"+handleDate(start,end)+"。";
        ToolWord.paragraph(doc,describe,14,false,false);

        final String paragraph1 = "一、监测概况";
        ToolWord.paragraph(doc,paragraph1,14,true,true);

        final String paragraph2 = "本周"+listEmployee.size()+"市（州）全部完成了采样及检测任务。本次共采集相关样本xx份，经新冠核酸检测均为阴性";
        ToolWord.paragraph(doc,paragraph2,14,true,true);

        final String paragraph3 = "其中冷库食品xx份（其中冷库水产品xx份，其他冷冻肉类xx份），外环境样本xx份（其中产品外包装xx份），从业人员咽拭子xx份，共计xx份。监测结果见表1。";
        ToolWord.paragraph(doc,paragraph3,14,true,false);

        ToolWord.singleRow(doc,"表5  不同从业人员监测情况",14,ParagraphAlignment.CENTER,true,false);

        final int colsEmployee = getMax(listEmployee,"profession");
        final int colsSiteType = getMax(listSiteType,"site_type");

        ToolWord.createDocTable(doc,listEmployee,colsEmployee,"profession","name","地区","profession_total","合计");

        ToolWord.newLine(doc);//换行

        final String paragraph20 = "二、食品及外环境样本监测结果";
        ToolWord.paragraph(doc,paragraph20,14,true,true);

        final String paragraph21 = "（一）不同类型场所监测结果   本次共监测"+colsSiteType+"种类型的销售场所，即"+getItems(listSiteType,"site_type",colsSiteType)+"。详见表2。";
        ToolWord.paragraph(doc,paragraph21,14,true,false);

        ToolWord.singleRow(doc,"表2 全省不同类型场所监测情况",14,ParagraphAlignment.CENTER,true,false);

        ToolWord.createDocTable(doc,listSiteType,colsSiteType,"site_type","area","地区","type_total","合计");

        ToolWord.downloadWord(doc,fileName,response);
    }

    private static String getItems(final List<HashMap<String,Object>> listData,final String horizontalKey,final int maxColumn){
        HashMap<String,Object> result = new HashMap<>();
        for(int i = 0; i < listData.size(); i++){
            final String[] values = ((String) listData.get(i).get(horizontalKey)).split(",");
            if(maxColumn == values.length){
                result = listData.get(i);
                break;
            }
        }
        final String[] values = ((String)result.get(horizontalKey)).split(",");
        StringBuilder sb = new StringBuilder();
        for(int x = 0; x < values.length; x++){
            final String value = values[x];
            if(sb.length() > 0){
                sb.append("、").append(value);
            }else{
                sb = new StringBuilder(value);//无需处理最后一个字符
            }
        }
        return sb.toString();
    }

    /**获取最大值*/
    private static int getMax(final List<HashMap<String,Object>> listData,final String key){
        //final OptionalInt optMax = data.stream().mapToInt(HashMap::size).max();//简化代码
        final OptionalInt optional = listData.stream().mapToInt(value -> {
            final String arrs = (String) value.get(key);
            final String[] split = arrs.split(",");
            return split.length;
        }).max();
        return optional.getAsInt();
    }

    private static String handleDate(final String start,final String end){
        String date = "";
        if(start != null && end != null){
            date = start +"至"+end;
        }
        if(start == null && end == null){
            date = "截至"+new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        if(start == null && end != null){
            date = "截至"+end;
        }
        if(start != null && end == null){
            date = start+"至"+new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        }
        return date;
    }
}