package com.fwtai.poi;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STJc;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STVerticalJc;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalInt;

/**
 * poi操作word
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2021-01-05 10:09
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
*/
public final class ToolWord{

    /**
     生成表头,格式如下
       ************************************
       *  地区  * 运输 * 加工 * 管理员 * 合计 *
       ************************************
       * 贵阳市 * 98  * 25   *　17　  * 140 *
       ************************************
       * 安顺市 * 102  * 61  *　  　  * 163 *
       ************************************
     * @param startColumnText 指定第1行的第1列的文字内容,用于不规则的2D表格
     * @param endColumnText 指定第1行的最后1列的文字内容
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/5 13:19
    */
    protected static void initTableTitle(final XWPFTableRow row,final String[] values,final String startColumnText,final String endColumnText){
        cellCenter(row.getCell(0),startColumnText,12);
        for(final String value : values){
            final XWPFTableCell cell = row.addNewTableCell();//在当前行继续创建新列
            cellCenter(cell,value,12);
        }
        cellCenter(row.addNewTableCell(),endColumnText,12);
    }

    /**
     * 先确定表头后,再执行填充数据行,即先执行initTableTitle()生成表头
     * @param startColumnText 第N行的第1列的文字内容
     * @param endColumnPosition 第N行的最后1列的位置索引,它和后一个参数对应
     * @param endColumnText 第N行的最后1列的文字内容,它和前一个参数对应
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/5 13:23
    */
    protected static void fillRowData(final XWPFTableRow row,final String[] values,final String startColumnText,final int endColumnPosition,final String endColumnText){
        cellCenter(row.getCell(0),startColumnText,12);
        for(int x = 0; x < values.length; x++){
            final XWPFTableCell cell = row.getCell(x+1);
            cellCenter(cell,values[x],12);
        }
        cellCenter(row.getCell(endColumnPosition),endColumnText,12);
    }

    //示例代码
    private static void indexCell(final XWPFTableRow row,final int cellIndex,final String content){
        final XWPFTableCell cell = row.getCell(cellIndex);
        final XWPFParagraph paragraph = cell.addParagraph();
        final XWPFRun run = paragraph.createRun();
        run.setText(content);
        cellCenter(cell,content,12);
    }

    /**
     * 指定单元格赋值文本内容及字体大小,默认的垂直居中和水平居中
     * @param cell 单元格
     * @param content 文本内容
     * @param fontSize 字体大小
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021年1月5日 12:22:21
    */
    protected static void cellCenter(final XWPFTableCell cell,final String content,final int fontSize){
        //给当前列中添加段落，就是给列添加内容
        final XWPFParagraph paragraph = cell.getParagraphs().get(0);
        final XWPFRun run = paragraph.createRun();
        run.setText(content);//设置内容
        run.setFontSize(fontSize);//设置大小
        //给列中的内容设置样式
        rowCellAlign(cell,STVerticalJc.CENTER,STJc.CENTER);//上下居中,左右居中
    }

    /**
     * 设置单元格的对齐方式
     * @param cell 单元格
     * @param vertical 垂直对齐方式
     * @param horizontal 水平对齐方式
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/5 12:18
    */
    protected static void rowCellAlign(final XWPFTableCell cell,final STVerticalJc.Enum vertical,final STJc.Enum horizontal){
        final CTTc cttc = cell.getCTTc();
        final CTTcPr ctPr = cttc.addNewTcPr();
        ctPr.addNewVAlign().setVal(vertical);
        cttc.getPList().get(0).addNewPPr().addNewJc().setVal(horizontal);
        final CTTblWidth tblWidth = ctPr.isSetTcW() ? ctPr.getTcW() : ctPr.addNewTcW();
        tblWidth.setType(STTblWidth.DXA);
        tblWidth.setW(BigInteger.valueOf(360*8));//宽度
    }

    protected static void rowCellAlign(final XWPFTableCell cell,final STVerticalJc.Enum vertical,final STJc.Enum horizontal,final int fontSize,final String content){
        //给当前列中添加段落，就是给列添加内容
        final XWPFParagraph paragraph = cell.getParagraphs().get(0);
        final XWPFRun run = paragraph.createRun();
        run.setText(content);//设置内容
        run.setFontSize(fontSize);//设置大小
        //给列中的内容设置样式
        rowCellAlign(cell,vertical,horizontal);
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

    public static void exportWord(final String start,final String end,final List<HashMap<String,Object>> listEmployee,final List<HashMap<String,Object>> listSiteType,final String fileName,final HttpServletResponse response) throws Exception{
        final XWPFDocument doc = new XWPFDocument();//创建新文档
        final String title = "贵州省冷冻冷藏肉品新冠病毒监测结果报告";
        singleRow(doc,title,18,ParagraphAlignment.CENTER,true,true);

        final String describe = "为了贯彻落实国务院联防联控机制《关于开展冷冻冷藏肉品风险排查的通知》（联防联控机制综发〔2020〕210 号）文件的要求。为科学规范指导开展我省新冠病毒环境监测工作，深入开展病毒溯源和疫情防控，2020年8月10日贵州省卫生健康委员会联合多部委下发了《贵州省农贸（集贸）市场及冷冻冷藏产品新冠病毒环境和人员监测方案》。要求各市（州）及县（区），为加强对农贸（集贸）市场及大型食品冷库新冠病毒环境监测。现将我省此次新冠肺炎监测结果报告如下，采样时间为"+handleDate(start,end)+"。";
        paragraph(doc,describe,14,false,false);

        final String paragraph1 = "一、监测概况";
        paragraph(doc,paragraph1,14,true,true);

        final String paragraph2 = "本周"+listEmployee.size()+"市（州）全部完成了采样及检测任务。本次共采集相关样本xx份，经新冠核酸检测均为阴性";
        paragraph(doc,paragraph2,14,true,true);

        final String paragraph3 = "其中冷库食品xx份（其中冷库水产品xx份，其他冷冻肉类xx份），外环境样本xx份（其中产品外包装xx份），从业人员咽拭子xx份，共计xx份。监测结果见表1。";
        paragraph(doc,paragraph3,14,true,false);

        singleRow(doc,"表5  不同从业人员监测情况",14,ParagraphAlignment.CENTER,true,false);

        //final OptionalInt optMax = data.stream().mapToInt(HashMap::size).max();//简化代码
        final OptionalInt optMax = listEmployee.stream().mapToInt(value -> {
            final String profession = (String) value.get("profession");
            final String[] split = profession.split(",");
            return split.length;
        }).max();
        final int cols = optMax.getAsInt();//获取最大值
        HashMap<String,Object> _map_ = new HashMap<>(cols);
        for(int i = 0; i < listEmployee.size(); i++){
            final String[] professions = ((String) listEmployee.get(i).get("profession")).split(",");
            if(cols == professions.length){
                _map_ = listEmployee.get(i);
                break;
            }
        }
        final XWPFTable table = doc.createTable();
        final XWPFTableRow titleRow = table.getRow(0);//创建的的一行一列的表格，获取第一行
        initTableTitle(titleRow,((String)_map_.get("profession")).split(","),"地区","合计");
        for(int i = 0; i < listEmployee.size(); i++){
            final HashMap<String,Object> map = listEmployee.get(i);
            final XWPFTableRow row = table.createRow();
            final String[] profession_totals = ((String) map.get("profession_total")).split(",");
            long itemTotal = 0;
            for(int x = 0; x < profession_totals.length; x++){
                itemTotal += Long.parseLong(profession_totals[x]);
            }
            final String item = ((String) map.get("name")).split(",")[0];
            fillRowData(row,profession_totals,item,cols+1,String.valueOf(itemTotal));
        }
        downloadWord(doc,fileName,response);
    }

    /**
     * 单行文本内容出处理
     * @param content 内容
     * @param fontSize 字体大小
     * @param align 文本对齐方式
     * @param newline 末尾是否换行
     * @param bold 文本是否加粗
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/4 19:17
    */
    protected static void singleRow(final XWPFDocument doc,final String content,final int fontSize,final ParagraphAlignment align,final boolean newline,final boolean bold){
        final XWPFParagraph paragraph = doc.createParagraph();
        paragraph.setAlignment(align);//对齐方式
        //final XWPFRun title = paragraph.insertNewRun(0);
        final XWPFRun title = paragraph.createRun();//有问题试试上一行的方法
        title.setText(content);
        title.setFontSize(fontSize);
        if(bold)
            title.setBold(true);
        if(newline)
            title.addCarriageReturn();
    }

    /**
     * 单行文本内容出处理
     * @param content 内容
     * @param fontSize 字体大小
     * @param newline 末尾是否换行
     * @param bold 文本是否加粗
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/4 19:17
    */
    protected static void paragraph(final XWPFDocument doc,final String content,final int fontSize,final boolean newline,final boolean bold){
        final XWPFParagraph paragraph = doc.createParagraph();
        final XWPFRun run = paragraph.createRun();
        run.setText(content);
        run.setFontSize(fontSize);
        if(newline)
            run.addCarriageReturn();
        if(bold)
            run.setBold(true);
    }

    /**
     * word导出下载
     * @param fileName 含后缀名
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2021/1/4 15:52
    */
    protected static void downloadWord(final XWPFDocument doc,final String fileName,final HttpServletResponse response) throws IOException{
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        doc.write(os);
        response.reset();
        response.setContentType("application/vnd.ms-word;charset=utf-8");
        response.setHeader("Content-Disposition","attachment;filename=" + new String((fileName).getBytes(), "iso-8859-1"));
        final ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new ByteArrayInputStream(os.toByteArray()));
            bos = new BufferedOutputStream(out);
            final byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))){
                bos.write(buff, 0, bytesRead);
            }
        }catch (final IOException e) {
            throw e;
        }finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }
}