package com.fwtai.tool;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Excel处理,依赖com.alibaba.easyexcel
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-12-29 11:37
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
 */
public final class ToolExcel{

    /**
     * 动态写入数据到Excel,
     * @注意选项 注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
     * @param excelFullPath excelFullPath带后缀名.xlsx全路径,最终生成的文件
     * @param data 数据
     * @param head 表头的实体类的字段带注解还有get和set @ExcelProperty("样本编号") private String sample_code;
     * @param sheet sheet名称,为空时是Sheet1
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/12/29 12:43
    */
    public static boolean writeExcel(final String excelFullPath,final List<?> data,final Class<?> head,final String sheet){
        // 这里 需要指定写用哪个class去写
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(excelFullPath,head).build();
            WriteSheet writeSheet = EasyExcel.writerSheet(sheet == null ? "Sheet1" : sheet).build();
            excelWriter.write(data,writeSheet);
            return true;
        } catch (final Exception e) {
            return false;
        }finally {
            // 千万别忘记finish 会帮忙关闭流
            if (excelWriter != null) {
                excelWriter.finish();
            }
        }
    }

    /**
     * 根据模版填充list的时候还要注意 模板中{.} 多了个点 表示list,分多次 填充 会使用文件缓存（省内存）
     * @注意选项 注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
     * @param excelFullPath excelFullPath带后缀名.xlsx全路径,最终生成的文件
     * @param data 数据
     * @param templateFileName 模版文件全路径,如 "G:\\excel\\employee.xlsx";
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/12/29 12:43
     */
    public static boolean writeExcelTemplate(final String excelFullPath,final List<?> data,final String templateFileName){
        ExcelWriter excelWriter = null;
        try {
            excelWriter = EasyExcel.write(excelFullPath).withTemplate(templateFileName).build();
            final WriteSheet writeSheet = EasyExcel.writerSheet().build();
            excelWriter.fill(data,writeSheet);
            return true;
        } catch (final Exception e) {
            e.printStackTrace();
            return false;
        }finally{
            if(excelWriter != null){
                excelWriter.finish();
            }
        }
    }

    /**
     * 适用于 service层或controller层调用,当然支持导出失败返回错误的json数据
     * @param fileName 下载时显示的文件名
     * @param head 表头的实体类的字段带注解还有get和set @ExcelProperty("样本编号") private String sample_code;
     * @param data 就是list参数head的集合
     * @作者 田应平
     * @QQ 444141300
     * @创建时间 2020/12/29 19:09
    */
    public static void writeExcelDownload(final String fileName,final Class<?> head,final List<?> data,final HttpServletResponse response)throws IOException{
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition","attachment;filename*=utf-8''" + fileName + ".xlsx");
        // 这里需要设置不关闭流
        EasyExcel.write(response.getOutputStream(),head).autoCloseStream(Boolean.FALSE).sheet("模板").doWrite(data);
    }
}