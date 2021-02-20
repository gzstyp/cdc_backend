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
        final Row labelRow = sheet.createRow(0);//第1行
        labelRow.setHeightInPoints(30);
        final Cell row0cell0 = labelRow.createCell(0);//第1行的第1个单元格
        row0cell0.setCellValue("附件名称");
        int totalCell = 0;
        for(int i = 0; i < listType.size(); i++){
            totalCell += (Long) listType.get(i).get("crowdTotal");
        }
        totalCell = totalCell * 3 + (listType.size() * 3) + 3 + 1;//+1是第1行的第1个单元格;+3是总统计
        for (int j = 1; j <= totalCell; j++){
            final Cell cell = labelRow.createCell(j);
            cell.setCellValue(label);
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
        final Cell labelCell = labelRow.getCell(1);
        labelCell.setCellStyle(styleCenter);
        labelCell.setCellValue(label);

        return wb;
    }
}