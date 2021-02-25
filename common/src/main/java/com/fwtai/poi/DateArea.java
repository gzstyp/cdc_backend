package com.fwtai.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * 日期区域查询-导出
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021/2/23 22:20
 * @QQ号码 444141300
 * @Email service@yinlz.com
 * @官网 <url>http://www.yinlz.com</url>
*/
public final class DateArea{

    public static void exportExcel(final String label,final List<HashMap<String,Object>> data,final List<String> listType,final String fileName,final HttpServletResponse response) throws Exception {
        ToolExcel.downloadExcel(reportExcel(label,data,listType),fileName,response);
    }

    /**
     * 单元格水平垂直居中
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/2/23 22:53
    */
    protected static XSSFCellStyle cellStyle(final XSSFWorkbook wb,final Cell cell){
        final XSSFCellStyle styleCenter = wb.createCellStyle();
        styleCenter.setAlignment(HorizontalAlignment.CENTER_SELECTION);//水平居中
        styleCenter.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        cell.setCellStyle(styleCenter);
        return styleCenter;
    }

    static XSSFWorkbook reportExcel(final String label,final List<HashMap<String,Object>> data,final List<String> listType){
        final XSSFWorkbook wb = new XSSFWorkbook();
        final XSSFSheet sheet = wb.createSheet("中高风险地区");
        sheet.setColumnWidth(0,(int) (35.7 * 100));
        final Row labelRow = sheet.createRow(0);//第1行
        labelRow.setHeightInPoints(48);
        final Cell row0cell0 = labelRow.createCell(0);//创建第1行的第1个单元格
        cellStyle(wb,row0cell0);
        row0cell0.setCellValue(label);
        int totalCell = listType.size();
        totalCell = totalCell * 4 + 1 + 4 + 1;//第1个+1是A单元格;+4是合计的4项;第2个+1是备注
        for (int j = 0; j < totalCell; j++){
            if(j != 0){
                final Cell cell = labelRow.createCell(j);
                cellStyle(wb,cell);
            }
        }
        sheet.setColumnWidth(totalCell-1,(int)(35.7 * 200));//单元格的宽度
        //合并单元格
        ToolExcel.cellRangeAddress(sheet,0,0,0,totalCell-2);

        final Row rowArea = sheet.createRow(1);//第2行,地区
        rowArea.setHeightInPoints(24);
        for (int j = 0; j < totalCell; j++){
            final Cell cell = rowArea.createCell(j);
            cellStyle(wb,cell);
            if(j == 0){
                cell.setCellValue("地区");
            }else if(j == totalCell - 1){
                cell.setCellValue("备注");
            }
        }
        //填充地区
        for(int i = 0; i < listType.size();i++){
            if(i == 0){
                createTabArea(sheet,rowArea,1,4,listType.get(i));//处理行的第1个
            }else{
                final int tab = i * 4;
                createTabArea(sheet,rowArea,(tab + 1),(tab + 4),listType.get(i));
                if(i == listType.size()-1){//处理最后的合计
                    createTabArea(sheet,rowArea,(tab + 1 + 4),(tab + 4 + 4),"合计");//处理最后一个的合计
                }
            }
        }

        final Row rowDate = sheet.createRow(2);//第3行,时间
        rowDate.setHeightInPoints(40);
        final Cell row2cell0 = rowDate.createCell(0);//创建第3行的第1个单元格
        cellStyle(wb,row2cell0);
        row2cell0.setCellValue("时间");

        for(int i = 0; i < listType.size();i++){
            if(i == 0){//处理行的第1个
                for(int x = 1; x < 5; x++){
                    itemArea(wb,x,rowDate.createCell(i + x));
                }
            }else{
                final int tab = i * 4;
                for(int x = 1; x < 5; x++){
                    itemArea(wb,x,rowDate.createCell(tab + x));
                }
                if(i == listType.size()-1){//处理最后一个的合计
                    for(int x = 1; x < 5; x++){
                        itemArea(wb,x,rowDate.createCell(tab + x + 4));
                    }
                }
            }
        }

        //数据行
        final Row rowData = sheet.createRow(3);//第4行,日期的数据行
        rowData.setHeightInPoints(20);
        final Cell row3cell0 = rowData.createCell(0);//创建第4行的第1个单元格
        cellStyle(wb,row3cell0);
        row3cell0.setCellValue("2021-02-25");

        //splitData(data,totalCell,wb,sheet);

        return wb;
    }

    /**
     * 填充以日期的数据行
     */
    private static void splitData(final List<HashMap<String,Object>> list,final int cells,final XSSFWorkbook wb,final XSSFSheet sheet){
        for(int i = 0; i < list.size(); i++){
            final int rowIndex = 3+i;//3++都是数据行
            final Row row = sheet.createRow(rowIndex);
            row.setHeightInPoints(20);
            final HashMap<String,Object> map = list.get(i);
            final String crowd_date = String.valueOf(map.get("crowd_date"));
            final String[] crowdNames = ((String)map.get("crowdName")).split("\\|");
            final Cell cellDate = row.createCell(0);//创建数据行的第N行的第1个单元格且赋值
            cellStyle(wb,cellDate);
            cellDate.setCellValue(crowd_date);
            for (int j = 1; j <= cells;j++){
                final Cell cell = row.createCell(j);//创建空单元格
                cellStyle(wb,cell);
                cell.setCellValue(0);
            }
            int masculine = 0;
            int detection = 0;
            int sampling = 0;
            for(int x = 0; x < crowdNames.length; x++){
                final String crowdName = crowdNames[x];//愿检尽检人群|应检尽检人群
                renderTotalData(sheet,row,crowdName,cells,map,x);//每行的合计或总计
                renderDataRow(sheet,row,cells,map,x);//每行的数据填充
                masculine += Integer.parseInt(getIndexData(map,"totalMasculine",x));
                detection += Integer.parseInt(getIndexData(map,"totalDetection",x));
                sampling += Integer.parseInt(getIndexData(map,"totalSampling",x));
            }
            rowTotal(row,cells,sampling,detection,masculine);
        }
    }
    
    /**
     * 地区或合计
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/2/25 23:16
    */
    private static void createTabArea(final XSSFSheet sheet,final Row row,final int startCol,final int endCol,final String area){
        final Cell cell = row.getCell(startCol);
        cell.setCellValue(area);
        ToolExcel.cellRangeAddress(sheet,1,1,startCol,endCol);
    }

    /**
     * 处理每个地区的合计及检测结果
     * @param 
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/2/25 23:13
    */
    private static void itemArea(final XSSFWorkbook wb,final int tabIndex,final Cell cell){
        final XSSFCellStyle style = cellStyle(wb,cell);
        style.setWrapText(true);
        switch (tabIndex){
            case 1:
                cell.setCellValue("采样数");
                break;
            case 2:
                cell.setCellValue("阴性");
                break;
            case 3:
                cell.setCellValue("阳性");
                break;
            case 4:
                cell.setCellValue("待出");
                break;
            default:
                break;
        }
    }

    /**
     * 计算最后行的累计统计
     * @param row 累计数
     * @param cells 总人群类型数量
     * @param number 数据的行数
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/2/24 10:32
    */
    private static void handleTotal(final XSSFSheet sheet,final Row row,final int cells,final int number){
        for(int i = 1; i <= cells; i++){
            final int value = handle2DValue(sheet,cells,number,i);
            final Cell cell = row.createCell(i);
            cellStyle(sheet.getWorkbook(),cell);
            cell.setCellValue(value);
        }
    }

    /**
     * 计算每一行的第几个单元格的合计|总计
     * @param
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/2/24 19:31
    */
    private static int handle2DValue(final XSSFSheet sheet,final int cells,final int number,final int position){
        int result = 0;
        for(int i = 0; i < number; i++){
            final XSSFRow xssfRow = sheet.getRow(4+i);
            for(int x = 1; x <= cells; x++){
                if(x == position){
                    final XSSFCell cell = xssfRow.getCell(x);
                    final double value = Double.parseDouble(String.valueOf(cell));
                    result += value;
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 每行的总计显示
    */
    private static void rowTotal(final Row row,final int cells,final int sampling,final int detection,final int masculine){
        final Cell cellTotal2 = row.getCell(cells - 2);//倒数第3个
        final Cell cellTotal1 = row.getCell(cells - 1);//倒数第2个
        final Cell cellTotal0 = row.getCell(cells - 0);//倒数第1个
        cellTotal2.setCellValue(sampling);
        cellTotal1.setCellValue(detection);
        cellTotal0.setCellValue(masculine);
    }

    /**
     * 每行的数据填充
    */
    private static void renderDataRow(final XSSFSheet sheet,final Row row,final int cells,final HashMap<String,Object> map,final int tabIndex){
        final String crowdType = getIndexData(map,"crowdType",tabIndex);
        final String masculine = getIndexData(map,"masculine",tabIndex);
        final String detection = getIndexData(map,"detection",tabIndex);
        final String sampling = getIndexData(map,"sampling",tabIndex);
        final String crowd_name = getIndexData(map,"crowdName",tabIndex);
        final String[] samplings = sampling.split(",");//已采样
        final String[] detections = detection.split(",");//已检测
        final String[] masculines = masculine.split(",");//阳性人数
        final String[] values = crowdType.split(",");//应检尽检发热门诊就诊患者
        for(int x = 0; x < values.length; x++){
            final String v = values[x];
            final int position = dataGetPosition(sheet,cells,(crowd_name + v));
            if(position != -1){
                for(int z = 0; z < 3; z++){
                    final Cell rowCell = row.getCell(position + z);
                    switch (z){
                        case 0:
                            rowCell.setCellValue(samplings[x]);
                            break;
                        case 1:
                            rowCell.setCellValue(detections[x]);
                            break;
                        case 2:
                            rowCell.setCellValue(masculines[x]);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    /**
     * 计算每行的合计
    */
    private static void renderTotalData(final XSSFSheet sheet,final Row row,final String crowd_name,final int cells,final HashMap<String,Object> map,final int tabIndex){
        for (int j = 0; j < cells;j=j+3){
            final int index = j + 1;
            final XSSFRow xssfRow = sheet.getRow(2);
            final XSSFCell cell = xssfRow.getCell(index);
            final String value = cell.getStringCellValue();
            if(value.length() > 0){
                if((crowd_name+"合计").equals(value)){
                    final Cell cellTotal1 = row.getCell(j + 1);
                    final Cell cellTotal2 = row.getCell(j + 2);
                    final Cell cellTotal3 = row.getCell(j + 3);
                    final String totalMasculine = getIndexData(map,"totalMasculine",tabIndex);
                    final String totalDetection = getIndexData(map,"totalDetection",tabIndex);
                    final String totalSampling = getIndexData(map,"totalSampling",tabIndex);
                    cellTotal1.setCellValue(totalSampling);
                    cellTotal2.setCellValue(totalDetection);
                    cellTotal3.setCellValue(totalMasculine);
                }
            }
        }
    }

    /**
     * 获取人群类型的位置
    */
    private static int dataGetPosition(final XSSFSheet sheet,final int cells,final String value){
        for (int j = 1; j <= cells;j++){
            final XSSFRow xssfRow = sheet.getRow(2);
            final XSSFCell cell = xssfRow.getCell(j);
            final String v = cell.getStringCellValue();
            if(value.equals(v)){
                return j;
            }
        }
        return -1;
    }

    private static String getIndexData(final HashMap<String,Object> map,final String key,final int tabIndex){
        final String[] values = ((String)map.get(key)).split("\\|");
        return values[tabIndex];
    }

    private static void render_data_row(final XSSFSheet sheet,final Row row,final String crowd_name,final int cells,final HashMap<String,Object> map,final int tabIndex){
        for (int j = 0; j < cells;j=j+3){
            final int index = j + 1;
            final XSSFRow xssfRow = sheet.getRow(2);//人群类型,应检尽检->第1遍是正确的;
            final XSSFCell cell = xssfRow.getCell(index);
            final String value = cell.getStringCellValue();
            if(value.length() > 0){
                if(!(crowd_name+"合计").equals(value)){
                    final String crowdType = getIndexData(map,"crowdType",tabIndex);
                    final String masculine = getIndexData(map,"masculine",tabIndex);
                    final String detection = getIndexData(map,"detection",tabIndex);
                    final String sampling = getIndexData(map,"sampling",tabIndex);

                    final String[] samplings = sampling.split(",");//已采样
                    final String[] detections = detection.split(",");//已检测
                    final String[] masculines = masculine.split(",");//阳性人数
                    final String[] values = crowdType.split(",");//应检尽检发热门诊就诊患者
                    for(int x = 0; x < values.length; x++){
                        final String v = values[x];
                        final int position = dataGetPosition(sheet,cells,(crowd_name + v));
                        if(position != -1){
                            System.out.println(crowd_name + v+",position = " + position);
                            System.out.println(samplings[x]+","+detections[x]+","+masculines[x]);
                            System.out.println("----------------");
                        }
                        /*if((crowd_name+v).equals(value)){
                            System.out.println("index = " + index);
                            break;
                            //System.out.println(j + ",value = " + value + "-->"+crowd_name+v);//为空字符串的是'核酸总计'
                            //System.out.println(j + ",value = " + value + ",");
                            for(int z = 0; z < 3; z++){
                                final Cell rowCell = row.getCell(j + z + 1);
                                switch (z){
                                    case 0:
                                        rowCell.setCellValue(samplings[x]);
                                        break;
                                    case 1:
                                        rowCell.setCellValue(detections[x]);
                                        break;
                                    case 2:
                                        rowCell.setCellValue(masculines[x]);
                                        break;
                                    default:
                                        break;
                                }
                            }
                        }*/
                    }
                }
            }
        }
    }
}