package com.fwtai.poi;

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
        final XSSFSheet sheet = wb.createSheet("核酸检测日报");
        return wb;
    }
}