package com.fwtai.tool;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.PictureRenderData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文档操作,http://deepoove.com/poi-tl/#_why_poi_tl
 * http://deepoove.com/poi-tl/#_why_poi_tl
 * https://www.jianshu.com/p/0a32d8bd6878
 * http://www.zzvips.com/article/108386.html
 * https://blog.csdn.net/w8700569/article/details/7288149
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2020-12-25 21:56
 * @QQ号码 444141300
 * @Email service@dwlai.com
 * @官网 http://www.fwtai.com
 */
public final class ToolDoc{

    private static Logger logger = LoggerFactory.getLogger(ToolDoc.class);

    /**
     * 根据模板填充内容生成word
     * 调用方法参考下面的main方法，详细文档参考官方文档
     * Poi-tl模板引擎官方文档：http://deepoove.com/poi-tl/
     *
     * @param templatePath word模板文件路径
     * @param fileDir      生成的文件存放地址
     * @param fileName     生成的文件名,不带格式。假如要生成abc.docx，则fileName传入abc即可
     * @param paramMap     替换的参数集合
     * @return 生成word成功返回生成的文件的路径，失败返回空字符串
     */
    public static String createWord(String templatePath, String fileDir, String fileName, Map<String, Object> paramMap) {
        Assert.notNull(templatePath, "word模板文件路径不能为空");
        Assert.notNull(fileDir, "生成的文件存放地址不能为空");
        Assert.notNull(fileName, "生成的文件名不能为空");

        // 生成的word格式
        String formatSuffix = ".docx";
        // 拼接后的文件名
        fileName = fileName + formatSuffix;

        // 生成的文件的存放路径
        if (!fileDir.endsWith("/")) {
            fileDir = fileDir + File.separator;
        }

        File dir = new File(fileDir);
        if (!dir.exists()) {
            logger.info("生成word数据时存储文件目录{}不存在,为您创建文件夹!", fileDir);
            dir.mkdirs();
        }

        String filePath = fileDir + fileName;
        // 读取模板templatePath并将paramMap的内容填充进模板，即编辑模板+渲染数据
        final XWPFTemplate template = XWPFTemplate.compile(templatePath).render(paramMap);
        try {
            // 将填充之后的模板写入filePath
            template.writeToFile(filePath);
            template.close();
        } catch (Exception e) {
            logger.error("生成word异常", e);
            e.printStackTrace();
            return "";
        }
        return filePath;
    }
    public static void main(String[] args) {
        Map<String, Object> params = new HashMap<>();
        params.put("partyaName", "张三");
        params.put("title", "Poi-tl模板引擎");
        params.put("header",new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));
        // 渲染图片
        params.put("picture", new PictureRenderData(120, 120, "E:\\Images\\android.jpg"));
        String templatePath = "H:\\zdd.docx";
        String fileDir = "H:\\template";
        String fileName = "zdd2";

        String wordPath = createWord(templatePath, fileDir, fileName, params);
        System.out.println("生成文档路径：" + wordPath);
        String pdfPath = wordPath.substring(0, wordPath.lastIndexOf(".") + 1) + "pdf";
    }
}