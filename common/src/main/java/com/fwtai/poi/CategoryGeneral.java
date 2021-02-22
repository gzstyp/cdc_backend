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

    protected static void cellStyle(final XSSFWorkbook wb,final Cell cell){
        final XSSFCellStyle styleCenter = wb.createCellStyle();
        styleCenter.setAlignment(HorizontalAlignment.CENTER_SELECTION);//水平居中
        styleCenter.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        cell.setCellStyle(styleCenter);
    }

    static XSSFWorkbook reportExcel(final String label,final List<HashMap<String,Object>> data,final List<HashMap<String,Object>> listType){
        final XSSFWorkbook wb = new XSSFWorkbook();
        final XSSFSheet sheet = wb.createSheet("日期分类统计");
        final Row labelRow0 = sheet.createRow(0);//第1行
        labelRow0.setHeightInPoints(30);
        final Cell row0cell0 = labelRow0.createCell(0);//创建第1行的第1个单元格
        cellStyle(wb,row0cell0);
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
        final Cell row1cell0 = row1.createCell(0);//创建第2行的第1个单元格
        cellStyle(wb,row1cell0);
        row1cell0.setCellValue("分类");

        //第2行,计算并创建总的单元格
        for (int j = 1; j <= totalCell;j++){
            row1.createCell(j);
        }
        //第2行合并单元格操作
        int intCrowdTypeTotal = 0;
        for(int i = 0; i < listType.size(); i++){
            final HashMap<String,Object> map = listType.get(i);
            final String crowdName = (String) map.get("crowdName");
            final long crowdTotal = (Long) map.get("crowdTotal")*3;
            final int tabs = (int) crowdTotal + 3;
            if(i==0){
                ToolExcel.cellRangeAddress(sheet,1,1,1,tabs);//第1个分类
                final Cell cell = row1.getCell(1);
                cellStyle(wb,cell);
                cell.setCellValue(crowdName);
            }else {
                final Cell cell = row1.getCell(intCrowdTypeTotal+1);
                cellStyle(wb,cell);
                cell.setCellValue(crowdName);
                ToolExcel.cellRangeAddress(sheet,1,1,intCrowdTypeTotal+1,(intCrowdTypeTotal+tabs));//此处要-3;+1是因为要比上一次循环的位置上移1个单元格
            }
            intCrowdTypeTotal = intCrowdTypeTotal + tabs;//此处+3,每个分类的合计需要3个单元格
        }
        final Row row2 = sheet.createRow(2);//创建第3行
        row2.setHeightInPoints(50);//高度
        final Cell row2cell0 = row2.createCell(0);//创建第3行的第1个单元格
        cellStyle(wb,row2cell0);
        row2cell0.setCellValue("类型");

        //创建第3行
        for (int j = 1; j <= totalCell;j++){
            row2.createCell(j);
        }

        int intCrowdType = 0;
        for(int i = 0; i < listType.size(); i++){
            final HashMap<String,Object> map = listType.get(i);
            final String crowdName = (String) map.get("crowdName");
            final String crowdType = (String) map.get("crowdType");
            final long crowdTotal = (Long) map.get("crowdTotal")*3;
            final int tabs = (int) crowdTotal + 3;
            final String[] arrays = crowdType.split(",");
            if(i==0){
                for(int x = 0; x < arrays.length; x++){
                    final String array = arrays[x];
                    final int tab = (x * 3) + 1;//1-3;4-6;7-9
                    final Cell cell = row2.getCell((x == 0) ? 1 : tab);//1、4、7、10
                    cellStyle(wb,cell);
                    cell.setCellValue(array);
                    final boolean bl = (x == 0);
                    ToolExcel.cellRangeAddress(sheet,2,2,(bl?1:tab),(bl?3:tab+2));
                    if(x == arrays.length-1){
                        final int tab3 = tab+3;
                        final Cell cellTotal = row2.getCell(tab3);
                        cellStyle(wb,cellTotal);
                        cellTotal.setCellValue(crowdName+"合计");
                        ToolExcel.cellRangeAddress(sheet,2,2,tab3,tab3+2);
                    }
                }
            }else{
                for(int x = 0; x < arrays.length; x++){
                    final String array = arrays[x];
                    final int tab = (x * 3) + 1;
                    final Cell cell = row2.getCell((x == 0) ? intCrowdType+1 : intCrowdType+tab);
                    cellStyle(wb,cell);
                    cell.setCellValue(array);
                    final boolean bl = (x == 0);
                    ToolExcel.cellRangeAddress(sheet,2,2,(bl?(intCrowdType+1):intCrowdType+tab),(bl?(intCrowdType+3):intCrowdType+tab+2));
                    if(x == arrays.length-1){
                        final int tab3 = intCrowdType+tab+3;
                        final Cell cellTotal = row2.getCell(tab3);
                        cellStyle(wb,cellTotal);
                        cellTotal.setCellValue(crowdName+"合计");
                        ToolExcel.cellRangeAddress(sheet,2,2,tab3,tab3+2);
                    }
                }
            }
            intCrowdType = intCrowdType + tabs;
        }
        //总计-合并单元格
        final int minus3Plus1 = totalCell - 3 + 1;
        ToolExcel.cellRangeAddress(sheet,1,2,minus3Plus1,totalCell);//核酸总计
        final Cell cellTotal = row1.getCell(minus3Plus1);
        cellStyle(wb,cellTotal);
        cellTotal.setCellValue("核酸总计");

        //人群类型
        /*for (int j = 0; j < totalCell;j=j+3){
            System.out.println(j+1);
            row2.createCell(j+1);
            for(int z = 0; z < 3; z++){
                //System.out.println(j+z+1);
            }
        }*/



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