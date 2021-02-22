package com.fwtai.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

public final class CategoryGeneral{

    public static void exportExcel(final String label,final List<HashMap<String,Object>> data,final List<HashMap<String,Object>> listType,final String fileName,final HttpServletResponse response) throws Exception {
        ToolExcel.downloadExcel(reportExcel(label,data,listType),fileName,response);
    }

    static XSSFWorkbook reportExcel(final String label,final List<HashMap<String,Object>> data,final List<HashMap<String,Object>> listType){
        final XSSFWorkbook wb = new XSSFWorkbook();
        final XSSFSheet sheet = wb.createSheet("日期分类统计");
        final Row labelRow0 = sheet.createRow(0);//第1行
        labelRow0.setHeightInPoints(30);
        final Cell row0cell0 = labelRow0.createCell(0);//创建第1行的第1个单元格
        row0cell0.setCellValue("附件名称");
        int totalCell = 0;
        for(int i = 0; i < listType.size(); i++){
            totalCell += (Long) listType.get(i).get("crowdTotal");
        }
        totalCell = totalCell * 3 + (listType.size() * 3) + 3;//(listType.size() * 3)是人群分类;+3是总统计的各3项
        for (int j = 1; j <= totalCell; j++){
            labelRow0.createCell(j);
        }
        //合并单元格
        ToolExcel.cellRangeAddress(sheet,0,0,1,totalCell);
        final XSSFCellStyle styleCenter = wb.createCellStyle();
        final Font labelFont = wb.createFont();
        labelFont.setFontHeightInPoints((short)14);//设置字号
        labelFont.setColor(IndexedColors.BLACK.getIndex());//设置字体颜色
        labelFont.setBold(true);
        styleCenter.setFont(labelFont);
        styleCenter.setAlignment(HorizontalAlignment.CENTER_SELECTION);//水平居中
        styleCenter.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        styleCenter.setWrapText(true);//自动换行显示,即非一行显示!!!
        final Cell labelCell = labelRow0.getCell(1);
        labelCell.setCellStyle(styleCenter);
        labelCell.setCellValue(label);

        final Row row1 = sheet.createRow(1);//创建第2行
        row1.setHeightInPoints(22);//高度
        final Cell row1cell1 = row1.createCell(0);//创建第2行的第1个单元格
        row1cell1.setCellValue("分类");

        //计算并创建总的单元格
        for (int j = 1; j <= totalCell;j++){
            row1.createCell(j);
        }

        //第2行合并单元格操作
        for(int i = 0; i < listType.size(); i++){
            final HashMap<String,Object> map = listType.get(i);
            final long crowdTotal = (Long) map.get("crowdTotal");

        }

        int intCrowdTypeCount = 0;
        //计算总的单元格
        /*for(int i = 0; i < listType.size(); i++){
            final HashMap<String,Object> map = listType.get(i);
            final long crowdTotal = (Long) map.get("crowdTotal");
            intCrowdTypeCount += crowdTotal * 3 + 3;
            for(int x = 0; x < intCrowdTypeCount;x++){
                row1.createCell(x+1);
            }
        }*/

        /*for(int i = 0; i < listType.size(); i++){
            final HashMap<String,Object> map = listType.get(i);
            final long crowdTotal = (Long) map.get("crowdTotal");
            intCrowdTypeCount += crowdTotal * 3 + 3;
            final String crowdName = (String) map.get("crowdName");
            final String crowdType = (String) map.get("crowdType");
            final String[] arrays = crowdType.split(",");
            for(int x = 0; x <= intCrowdTypeCount;x++){
                row1.createCell(x+1);
            }
            System.out.println(crowdTotal);
            System.out.println(crowdTotal * 3 + 3 + 1);
            if(i == 0){
                ToolExcel.cellRangeAddress(sheet,1,1,1,intCrowdTypeCount);
            }else{
                ToolExcel.cellRangeAddress(sheet,1,1,58,intCrowdTypeCount);//(int)(crowdTotal * 3 + 3 + 1)
            }
        }*/
        return wb;
    }
}