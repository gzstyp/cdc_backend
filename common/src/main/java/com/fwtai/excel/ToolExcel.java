package com.fwtai.excel;

import com.fwtai.bean.PageFormData;
import com.fwtai.tool.ToolString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Excel文件操作
 * @作者 田应平
 * @版本 v1.0
 * @创建时间 2017年1月10日 下午9:13:34
 * @QQ号码 444141300
 * @官网 http://www.fwtai.com
*/
public final class ToolExcel{

	/**
	 * 固定导入解析Excel数据表格的表头,含xls或xlsx文件解析,不推荐使用,表头必须在第1列
	 * @param fields 对应的列的头部,有多少列就有多少个field,fields从左到右的表头顺序务必与List的add顺序要一一对应
	 * @param excelPath Excel文件路径，绝对全路径
	 * @param index 从Excel文件的第几行开始解析
	 * @作者 田应平
	 * @返回值类型 ArrayList< HashMap< String,Object>>
	 * @创建时间 2016年9月6日 下午4:08:19 
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	protected static ArrayList<HashMap<String,Object>> parseListMap(final ArrayList<String> fields, final String excelPath, int index) throws Exception {
		final ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		final File excelFile = new File(excelPath); //创建文件对象
		final FileInputStream is = new FileInputStream(excelFile); // 文件流
		final Workbook workbook = WorkbookFactory.create(is);//关键
		final int sheetCount = workbook.getNumberOfSheets(); //Sheet的数量
		//遍历每个Sheet
		for(int s = 0;s< sheetCount; s++){
			final Sheet sheet = workbook.getSheetAt(s);//获取第s个Sheet页
			if(sheet.getRow(s) != null){
				final int rowCount = sheet.getLastRowNum();//getLastRowNum()是获取最后一行的编号
				final Row header = sheet.getRow(0);//表头必须在第1列
				if(header != null){
					final int rowCellCount = header.getLastCellNum();
					index = index < 0 ? 0 :index ;
					index = index >= rowCount ? rowCount : index ;
					if(rowCount > 0 && rowCellCount > 0){
						for(int i = index; i <= rowCount; i++){//index=0就是从第1行开始读取，index=1就是从第2行开始读取
							final Row row = sheet.getRow(i);//获取第i行
							final HashMap<String, Object> map = readExcelRow(row,fields);
							list.add(map);
						}
					}
				}
			}
		}
		is.close();
		return list;
	}
	
	/**
	 * 动态导入解析Excel数据表格的表头,含xls或xlsx文件解析,只要求表头列的map的key和字段对应即可,不要求顺序一一对应,表头必须在第1列
	 * @param mapper map类型的和列的头部,有多少列就有多少个map,如：mapper.put("姓名","name");如：mapper.put("性别","sex");key为对应的表头列,value是字段
	 * @param excelPath Excel文件路径，绝对物理全路径
	 * @param index 从Excel文件的第几行开始解析,0就是从第1行开始读取,1就是从第2行开始读取
	 * @作者 田应平
	 * @返回值类型 ArrayList< HashMap< String,Object>>
	 * @创建时间 2017年8月26日 12:17:27
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static ArrayList<HashMap<String,Object>> parseListMap(final HashMap<String,String> mapper,final String excelPath, int index) throws Exception{
		final ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
		final File excelFile = new File(excelPath); //创建文件对象
		final FileInputStream is = new FileInputStream(excelFile); // 文件流
		final Workbook workbook = WorkbookFactory.create(is);//关键
		final int sheetCount = workbook.getNumberOfSheets(); //Sheet的数量
		final ArrayList<String> fields = new ArrayList<String>();
		//遍历每个Sheet
		for(int s = 0;s< sheetCount; s++){
			final Sheet sheet = workbook.getSheetAt(s);//获取第s个Sheet页
			if(sheet.getRow(s) != null){
				final int rowCount = sheet.getLastRowNum();//getLastRowNum()是获取最后一行的编号
				final Row header = sheet.getRow(0);//表头必须在第1列
				if(header != null){
					final int rowCellCount = header.getLastCellNum();
					index = index < 0 ? 0 :index ;
					index = index >= rowCount ? rowCount : index;
					final int head = index <= 0 ? 0 :index - 1;//判断是否有标题行
					boolean b = true;
					if(rowCount > 0 && rowCellCount > 0){
						if (rowCellCount != mapper.size()){
							throw new Exception("读取失败,表头列和字段数不一致");
						}
						for(int i = index; i <= rowCount; i++){//index=0就是从第1行开始读取，index=1就是从第2行开始读取
							if(head >= 0 && b){
								fields.clear();
								b = false;
								final Row row = sheet.getRow(head< 0 ? 0 : head);//获取第i-1行,即表头行
								for (int j = 0; j < rowCellCount; j++){
									final Cell cell = row.getCell(j);//获取第head行的第j个单元格
									if(ToolString.isBlank(cell)){
										continue;//读取为空时处理
									}
									switch (cell.getCellType()){
									default:
										final String temp = cell.getStringCellValue();
										for (String key : mapper.keySet()){
											if (key.equalsIgnoreCase(temp)) {
												fields.add(mapper.get(key));
												break;
											}
										}
										break;
									}
								}
							}
							final Row row = sheet.getRow(i);//获取第i行
							final HashMap<String, Object> map = readExcelRow(row,fields);
							list.add(map);
						}
					}
				}
			}
		}
		is.close();
		return list;
	}
	
	/**
	 * 解析Excel行的数据
	 * @param row 
	 * @param fields 数据库表字段与Excel表格的从左到右的表头顺序务必要一一对应
	 * @作者 田应平
	 * @返回值类型 HashMap< String,Object>
	 * @创建时间 2017年5月17日 上午10:29:49 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	protected static HashMap<String,Object> readExcelRow(final Row row,final ArrayList<String> fields){
		final HashMap<String,Object> map = new HashMap<String,Object>();
		for (int j = 0; j < row.getLastCellNum(); j++){
			final Cell cell = row.getCell(j);//第j个单元格的数据
			if(ToolString.isBlank(cell)){
				continue;//读取为空时处理
			}
			switch (cell.getCellType()){
			case Cell.CELL_TYPE_NUMERIC:
				final DecimalFormat df = new DecimalFormat("0");  
				final String value = df.format(cell.getNumericCellValue());
				map.put(fields.get(j),value);
				break;
			case Cell.CELL_TYPE_STRING:
				map.put(fields.get(j),cell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				map.put(fields.get(j),cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				map.put(fields.get(j),cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_BLANK:
				map.put(fields.get(j),"");
				break;
			default:
				map.put(fields.get(j),String.valueOf(cell.getStringCellValue()));
				break;
			}
		}
		return map;
	}
	
	/**
	 * 导入解析Excel数据表格,含xls或xlsx文件解析,不推荐使用,表头必须在第1列
	 * @param fields 数据库表字段与Excel表格的从左到右的表头顺序务必要一一对应
	 * @param excelPath Excel文件路径，绝对路径
	 * @param index 从Excel文件的第几行开始解析
	 * @作者 田应平
	 * @返回值类型 ArrayList< HashMap< String,Object>>
	 * @创建时间 2016年9月6日 下午4:08:19 
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	protected static ArrayList<PageFormData> parseListPageFormData(final ArrayList<String> fields,final String excelPath,int index) throws Exception{
		final ArrayList<PageFormData> listPd = new ArrayList<PageFormData>();
		final File excelFile = new File(excelPath); //创建文件对象
		final FileInputStream is = new FileInputStream(excelFile); // 文件流
		final Workbook workbook = WorkbookFactory.create(is);//关键
		final int sheetCount = workbook.getNumberOfSheets(); //Sheet的数量
		//遍历每个Sheet
		for(int s = 0;s< sheetCount; s++){
			final Sheet sheet = workbook.getSheetAt(s);//获取第s个Sheet页
			if(sheet.getRow(s) != null){
				final int rowCount = sheet.getLastRowNum();//getLastRowNum()是获取最后一行的编号,共有多少行
				final Row header = sheet.getRow(0);//表头必须在第1列
				if(header != null){
					final int rowCellCount = header.getLastCellNum();
					index = index < 0 ? 0 :index ;
					index = index >= rowCount ? rowCount : index ;
					if(rowCount > 0 && rowCellCount > 0){
						for(int i = index; i <= rowCount; i++){//index=0就是从第1行开始读取，index=1就是从第2行开始读取
							listPd.add(readRowPageFormData(fields,sheet.getRow(i)));//获取第i行
						}
					}
				}
			}
		}
		is.close();
		return listPd;
	}
	
	/**
	 * 动态导入解析Excel数据表格的表头,含xls或xlsx文件解析,只要求表头列的map的key和字段对应即可,不要求顺序一一对应,表头必须在第1列
	 * @param mapper map类型的和列的头部,有多少列就有多少个map,如：mapper.put("姓名","name");如：mapper.put("性别","sex");key为对应的表头列,value是字段
	 * @param excelPath Excel文件路径，绝对物理全路径
	 * @param index 从Excel文件的第几行开始解析,0就是从第1行开始读取,1就是从第2行开始读取
	 * @作者 田应平
	 * @返回值类型 ArrayList< PageFormData>
	 * @创建时间 2017年8月31日 09:48:56
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static ArrayList<PageFormData> parseListPageFormData(final HashMap<String,String> mapper,final String excelPath, int index) throws Exception{
		final ArrayList<PageFormData> list = new ArrayList<PageFormData>();
		final File excelFile = new File(excelPath); //创建文件对象
		final FileInputStream is = new FileInputStream(excelFile); // 文件流
		final Workbook workbook = WorkbookFactory.create(is);//关键
		final int sheetCount = workbook.getNumberOfSheets(); //Sheet的数量
		final ArrayList<String> fields = new ArrayList<String>();
		//遍历每个Sheet
		for(int s = 0;s< sheetCount; s++){
			final Sheet sheet = workbook.getSheetAt(s);//获取第s个Sheet页
			if(sheet.getRow(s) != null){
				final int rowCount = sheet.getLastRowNum();//getLastRowNum()是获取最后一行的编号
				final Row header = sheet.getRow(0);//表头必须在第1列
				if(header != null){
					final int rowCellCount = header.getLastCellNum();
					index = index < 0 ? 0 :index ;
					index = index >= rowCount ? rowCount : index;
					final int head = index <= 0 ? 0 :index - 1;//判断是否有标题行
					boolean b = true;
					if(rowCount > 0 && rowCellCount > 0){
						if (rowCellCount != mapper.size()){
							throw new Exception("读取失败,表头列和字段数不一致");
						}
						for(int i = index; i <= rowCount; i++){//index=0就是从第1行开始读取，index=1就是从第2行开始读取
							if(head >= 0 && b){
								fields.clear();
								b = false;
								final Row row = sheet.getRow(head< 0 ? 0 : head);//获取第i-1行,即表头行
								for (int j = 0; j < rowCellCount; j++){
									final Cell cell = row.getCell(j);//获取第head行的第j个单元格
									if(ToolString.isBlank(cell)){
										continue;//读取为空时处理
									}
									switch (cell.getCellType()){
									default:
										final String temp = cell.getStringCellValue();
										for (String key : mapper.keySet()){
											if (key.equalsIgnoreCase(temp)) {
												fields.add(mapper.get(key));
												break;
											}
										}
										break;
									}
								}
							}
							list.add(readRowPageFormData(fields,sheet.getRow(i)));//获取并解析第i行
						}
					}
				}
			}
		}
		is.close();
		return list;
	}
	
	/**
	 * 解析Excel行的数据
	 * @param row 
	 * @param fields 要与Excel表格的表头务必要一一对应
	 * @作者 田应平
	 * @返回值类型 HashMap< String,Object>
	 * @创建时间 2017年5月17日 上午10:29:49 
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	protected static PageFormData readRowPageFormData(final ArrayList<String> fields,final Row row){
		final PageFormData pageData = new PageFormData();
		for (int j = 0; j < row.getLastCellNum(); j++){
			final Cell cell = row.getCell(j);//第j个单元格数据
			if (ToolString.isBlank(cell)){
				continue;
			}
			switch (cell.getCellType()){
			case Cell.CELL_TYPE_NUMERIC:
				final DecimalFormat df = new DecimalFormat("0");  
				final String value = df.format(cell.getNumericCellValue());
				pageData.put(fields.get(j),value);
				break;
			case Cell.CELL_TYPE_STRING:
				pageData.put(fields.get(j),cell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				pageData.put(fields.get(j),cell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				pageData.put(fields.get(j),cell.getCellFormula());
				break;
			case Cell.CELL_TYPE_BLANK:
				pageData.put(fields.get(j),"");
				break;
			default:
				pageData.put(fields.get(j),String.valueOf(cell.getStringCellValue()));
				break;
			}
		}
		return pageData;
	}
	
	/**
	 * 动态导入解析Excel数据表格的表头,含xls或xlsx文件解析,只要求表头列的map的key和字段对应即可,不要求顺序一一对应,如果某行的第1列为空则不读取后面的数据,即跳出循环!推荐使用,表头必须在第1列
	 * @param mapper map类型的和列的头部,有多少列就有多少个map,如：mapper.put("姓名","NAME");如：mapper.put("性别","SEX");key为对应的表头列,value是字段
	 * @param excelPath Excel文件路径，绝对物理全路径
	 * @param index 从Excel文件的第几行开始解析,0就是从第1行开始读取,1就是从第2行开始读取
	 * @作者 田应平
	 * @返回值类型 ArrayList< HashMap< String,Object>>
	 * @创建时间 2017年9月15日 09:16:54
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static ArrayList<HashMap<String,Object>> parseExcel(final HashMap<String,String> mapper,final String excelPath, int index) throws Exception{
		final ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>(0);
		final File excelFile = new File(excelPath); //创建文件对象
		final FileInputStream is = new FileInputStream(excelFile); // 文件流
		final Workbook workbook = WorkbookFactory.create(is);//关键
		final int sheetCount = workbook.getNumberOfSheets(); //Sheet的数量
		final ArrayList<String> fields = new ArrayList<String>(0);
		//遍历每个Sheet
		for(int s = 0;s< sheetCount; s++){
			final Sheet sheet = workbook.getSheetAt(s);//获取第s个Sheet页
			if(sheet.getRow(s) != null){
				final int rowCount = sheet.getLastRowNum();//getLastRowNum()是获取最后一行的编号
				final Row header = sheet.getRow(0);//表头必须在第1列
				if(header != null){
					final int rowCellCount = header.getLastCellNum();
					index = index < 0 ? 0 :index ;
					index = index >= rowCount ? rowCount : index;
					final int head = index <= 0 ? 0 :index - 1;//判断是否有标题行
					boolean b = true;
					boolean isBreak = false;
					if(rowCount > 0 && rowCellCount > 0){
						for(int i = index; i <= rowCount; i++){//index=0就是从第1行开始读取，index=1就是从第2行开始读取
							if(head >= 0 && b){
								fields.clear();
								b = false;
								final Row row = sheet.getRow(head< 0 ? 0 : head);//获取第i-1行,即表头行
								for (int j = 0; j < rowCellCount; j++){
									final Cell cell = row.getCell(j);//获取第head行的第j个单元格
									if(ToolString.isBlank(cell)){
										continue;//读取为空时处理
									}
									switch (cell.getCellType()){
									default:
										final String value = cell.getStringCellValue();
										for (String key : mapper.keySet()){
											if (key.equalsIgnoreCase(value)){
												fields.add(mapper.get(key));
												break;
											}
										}
										break;
									}
								}
							}
							if (isBreak){
								break;
							}
							final Row row = sheet.getRow(i);//获取第i行
							final HashMap<String,Object> map = new HashMap<String,Object>(0);
							for(int j = 0; j < row.getLastCellNum(); j++){
								final Cell cell = row.getCell(j);//第j个单元格
								if(ToolString.isBlank(cell) && j == 0){
									isBreak = true;
									break;
								}
								if(ToolString.isBlank(cell)){
									continue;//读取为空时处理
								}
								switch (cell.getCellType()){
								case Cell.CELL_TYPE_NUMERIC:
									final DecimalFormat df = new DecimalFormat("0");  
									final String value = df.format(cell.getNumericCellValue());
									map.put(fields.get(j),value);
									break;
								case Cell.CELL_TYPE_STRING:
									map.put(fields.get(j),cell.getStringCellValue());
									break;
								case Cell.CELL_TYPE_BOOLEAN:
									map.put(fields.get(j),cell.getBooleanCellValue());
									break;
								case Cell.CELL_TYPE_FORMULA:
									map.put(fields.get(j),cell.getCellFormula());
									break;
								case Cell.CELL_TYPE_BLANK:
									map.put(fields.get(j),"");
									break;
								default:
									map.put(fields.get(j),String.valueOf(cell.getStringCellValue()));
									break;
								}
							}
							if(!isBreak)list.add(map);
						}
					}
				}
			}
		}
		is.close();
		return list;
	}
	
	/**
	 * 生成创建2007或2010版本的Excel表格文件,会按着表头标题栏的下一行写入数据,即从第2行开始写入数据
	 * @作者 田应平
	 * @param list 需要导出的数据集合
	 * @param fields 从数据库读取的字段名
	 * @param titles 表头第1行名
	 * @param sheetName sheet名称
	 * @返回值类型 Workbook
	 * @创建时间 2017年1月10日 上午10:18:24
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	private static Workbook createWorkBook(final List<Map<String, Object>> list,final ArrayList<String> fields, final ArrayList<String> titles,final String sheetName){
		final XSSFWorkbook wb = new XSSFWorkbook();
		int count = 0;
		int indexName = 1;
		final Integer batch = 65535;
		if(list.size() > batch){
			final ArrayList<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
			for(int i = 0; i < list.size();i++){
				listData.add(list.get(i));
				count++;
				if(count % batch==0){
					createSheet(listData,fields,titles,wb,sheetName+indexName);
					listData.clear();
					count=0;
					indexName++;
				}
			}
			if (count > 0){
				createSheet(listData,fields,titles,wb,sheetName+indexName);
			}
			return wb;
		}else{
			createSheet(list,fields,titles,wb,sheetName);
			return wb;
		}
	}
	
	/**
	 * 生成创建2007或2010版本的Excel表格文件,数据是从第指定startRow+1行可是写入数据,因为1是表头标题栏
	 * @作者 田应平
	 * @param list 需要导出的数据集合
	 * @param fields 从数据库读取的字段名
	 * @param titles 表头第1行名
	 * @param sheetName sheet名称
	 * @param startRow 从开始行startRow写起;0表示第1行,1表示第2行,
	 * @param titleDescribe 给导出的Excel表格起个标题或描述
	 * @返回值类型 Workbook
	 * @创建时间 2017年1月10日 上午10:18:24
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	protected static Workbook createWorkBook(final List<Map<String, Object>> list, final ArrayList<String> fields, final ArrayList<String> titles,final String sheetName,final int startRow,final String titleDescribe){
		final XSSFWorkbook wb = new XSSFWorkbook();
		int count = 0;
		int indexName = 1;
		final Integer batch = 65535;
		if(list.size() > batch){
			final ArrayList<Map<String,Object>> listData = new ArrayList<Map<String,Object>>();
			for(int i = 0; i < list.size();i++){
				listData.add(list.get(i));
				count++;
				if(count % batch==0){
					createSheet(listData,fields,titles,wb,sheetName+indexName,startRow,titleDescribe);
					listData.clear();
					count=0;
					indexName++;
				}
			}
			if (count > 0){
				createSheet(listData,fields,titles,wb,sheetName+indexName,startRow,titleDescribe);
			}
			return wb;
		}else{
			createSheet(list,fields,titles,wb,sheetName, startRow,titleDescribe);
			return wb;
		}
	}
	
	/**
	 * 生成 Sheet工作簿,数据是从第2行可是写入数据
	 * @param list 数据集合
	 * @param fields 数据库字段,要与titles的add顺序要一致
	 * @param titles 标题栏,要与fields的add顺序要一致
	 * @param sheetName
	 * @作者 田应平
	 * @返回值类型 XSSFSheet
	 * @创建时间 2017年9月5日 15:19:45
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	private static XSSFSheet createSheet(final List<Map<String, Object>> list,final ArrayList<String> fields, final ArrayList<String> titles,final XSSFWorkbook wb,final String sheetName){
		final XSSFSheet sheet = wb.createSheet(sheetName);
		// 手动设置列宽。第一个参数表示要为第几列设,第二个参数表示列的宽度，n为列高的像素数。
		for (int i = 0; i < fields.size();i++){
			sheet.setColumnWidth(i,(int) (35.7 * 150));
		}
		// 创建第一行,表头行
		final Row rowTitle = sheet.createRow(0);
		// 创建两种单元格格式
        final XSSFCellStyle csHead = wb.createCellStyle();
        csHead.setFillPattern(FillPatternType.SOLID_FOREGROUND);//设置前景填充样式
        csHead.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);//前景填充色
        final XSSFCellStyle csContent = wb.createCellStyle();
		// 创建两种字体
		final Font fontHead = wb.createFont();
		final Font fontContent = wb.createFont();
		// 创建第一种字体样式（用于表头）
		fontHead.setFontHeightInPoints((short)12);//设置字号
		fontHead.setColor(IndexedColors.WHITE.getIndex());//设置字体颜色
        fontHead.setBold(true);
		// 创建第二种字体样式（用于值）
		fontContent.setFontHeightInPoints((short)11);
		fontContent.setColor(IndexedColors.BLACK.getIndex());

		// 设置第一种单元格的样式（用于表头）
        rowCellStyle(csHead,fontHead);
        // 设置第二种单元格的样式（用于数据行）
        rowCellStyle(csContent,fontContent);
        // 设置列名,表头
		for (int i = 0; i < titles.size();i++){
			final Cell cell = rowTitle.createCell(i);
			cell.setCellValue(titles.get(i));
			cell.setCellStyle(csHead);
		}
		// 设置每行每列的值
		for (int i = 0; i < list.size();i++){
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			final Row rowData = sheet.createRow(i+1);
			// 在row行上创建一个方格
			for (int j = 0; j < fields.size(); j++){
				final Cell cell = rowData.createCell(j);
				cell.setCellValue(list.get(i).get(fields.get(j)) == null ? " " : list.get(i).get(fields.get(j)).toString());
				cell.setCellStyle(csContent);
			}
		}
		return sheet;
	}

	/*单元格的样式*/
    private static void rowCellStyle(final XSSFCellStyle style,final Font font){
        style.setFont(font);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.LEFT);
    }

    protected static XSSFWorkbook workbook(final String label,final List<HashMap<String,Object>> data){
        final XSSFWorkbook wb = new XSSFWorkbook();
        final XSSFSheet sheet = wb.createSheet("核酸检测日报");
        final Row labelRow = sheet.createRow(0);//第1行
        final Cell labelCell = labelRow.createCell(0);
        final XSSFCellStyle style = wb.createCellStyle();
        final Font labelFont = wb.createFont();
        labelFont.setFontHeightInPoints((short)14);//设置字号
        labelFont.setColor(IndexedColors.BLACK.getIndex());//设置字体颜色
        labelFont.setBold(true);
        style.setFont(labelFont);
        style.setAlignment(HorizontalAlignment.CENTER_SELECTION);//水平居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中
        labelCell.setCellStyle(style);
        labelCell.setCellValue(label);

        int crowdTotalCell = 0;
        final int tabsTotal = data.size();
        for(int x = 0;x < data.size();x++){
            final HashMap<String,Object> map = data.get(x);
            final String sampling = (String)map.get("sampling");
            final String[] items = sampling.split(",");
            crowdTotalCell = crowdTotalCell + items.length  * 3;
        }
        cellRangeAddress(sheet,0,0,0,(tabsTotal * 3 + 3) + crowdTotalCell - 1);//第1行标题,1是第一格的内容是'分类',但是不需要+1的,因为它是从0开始算起,而此处是从1算起;[x3的各项的合计;3是核酸总计]

        final Row crowdRow = sheet.createRow(1);//第2行
        final Row typeRow = sheet.createRow(2);//第3行,人群类型
        final Row rowTotal = sheet.createRow(3);//第3行,人群类型:已采样人数,已检测人数,检测阳性人数

        int crowdCell = 0;
        for(int x = 0;x < data.size();x++){//处理人员类型
            final HashMap<String,Object> map = data.get(x);
            final String crowdName = (String)map.get("crowdName");
            final String[] crowdType = ((String)map.get("crowdType")).split(",");
            final String[] masculine = ((String)map.get("masculine")).split(",");
            final String[] detection = ((String)map.get("detection")).split(",");
            final String[] sampling = ((String)map.get("sampling")).split(",");
            final int length = sampling.length;
            final Cell cell = crowdRow.createCell(crowdCell);//第1格子
            cell.setCellValue(crowdName);
            final int len = length * 3;
            if(x == tabsTotal -1){//处理最后一个人群类型时附加总计
                cellRangeAddress(sheet,1,1,crowdCell,(crowdCell + len - 1) + 6);//附加总计;6 = 3 + 3,其中的一个3是合计，其中一个3是总计
                for(int z = 0; z < length; z++){
                    final Cell _cell = typeRow.createCell(z*3+crowdCell);//人群类型
                    cellRangeAddress(sheet,2,2,z*3+crowdCell,z*3+2+crowdCell);//人群类型
                    _cell.setCellValue(crowdType[z]);
                    if(z == length - 1){
                        final Cell cell_ = typeRow.createCell((z + 1)*3+crowdCell);//合计
                        cellRangeAddress(sheet,2,2,(z + 1)*3+crowdCell,(z + 1)*3+2+crowdCell);//合计
                        cell_.setCellValue(crowdName+"合计");

                        final Cell _cell_ = typeRow.createCell((z + 2)*3+crowdCell);//总计
                        cellRangeAddress(sheet,2,2,(z + 2)*3+crowdCell,(z + 2)*3+2+crowdCell);//总计
                        _cell_.setCellValue("核酸总计");
                    }
                }
            }else{
                cellRangeAddress(sheet,1,1,crowdCell,(crowdCell + len - 1) + 3);//附加合计,一个3是合计
                crowdCell = crowdCell + len + 3;
                for(int z = 0; z < length; z++){
                    final Cell _cell = typeRow.createCell(z*3);//人群类型
                    cellRangeAddress(sheet,2,2,z*3,z*3+2);//人群类型
                    _cell.setCellValue(crowdType[z]);

                    System.out.print(crowdType[z]);
                    System.out.print(sampling[z]);
                    System.out.print(","+detection[z]);
                    System.out.print(","+masculine[z]);
                    System.out.println();



                    final Cell cellTotal = rowTotal.createCell(z*3+crowdCell);//人群类型

                    if(z == length - 1){
                        final Cell cell_ = typeRow.createCell((z + 1)*3);//合计
                        cellRangeAddress(sheet,2,2,(z + 1)*3,(z + 1)*3+2);//合计
                        cell_.setCellValue(crowdName+"合计");
                    }
                }

                for(int z = 0; z < crowdCell; z++){
                    /*final Cell cell1 = rowTotal.createCell(z);
                    cell1.setCellValue("已采样人数");

                    final Cell cell2 = rowTotal.createCell(z+1);
                    cell2.setCellValue("已检测人数");

                    final Cell cell3 = rowTotal.createCell(z+2);
                    cell3.setCellValue("检测阳性人数");*/
                }
            }
        }

        for (int i = 0; i < 10;i++){
            // Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
            // 创建一行，在页sheet上
            final Row rowData = sheet.createRow(i+10);//上面累加,在row行上创建一个方格

            /*for (int j = 0; j < 10; j++){
                final Cell cell = rowData.createCell(j);
                final XSSFCellStyle cellStyle = wb.createCellStyle();
                cellStyle.setAlignment(HorizontalAlignment.CENTER_SELECTION);//水平居中
                cellStyle.setVerticalAlignment(VerticalAlignment.TOP);//靠上对齐
                cellStyle.setWrapText(true);//换行显示,即非一行显示!!!
                cell.setCellStyle(cellStyle);
                //cell.setCellValue("我是内容啊");
            }*/
        }
        return wb;
    }

    public static void main(String[] args) throws Exception {
        //创建一个Excel
        final Workbook wb = new HSSFWorkbook();
        //创建一个输出流对象,以便将创建好的Excel写入文件
        final FileOutputStream fileOut = new FileOutputStream("C:\\WORKBOOK.xls");
        //
        final CreationHelper createHelper = wb.getCreationHelper();
        //创建一个sheet
        final Sheet sheet = wb.createSheet("附表");
        //合并一个单元格,这个地方会在下面用图介绍
        sheet.addMergedRegion(new CellRangeAddress(
            0, //first row (0-based)
            10, //last row  (0-based)
            0, //first column (0-based)
            1  //last column  (0-based)
        ));

        //创建一个行对象,下标从0开始
        Row row1=sheet.createRow(0);
        //创建一个样式
        CellStyle cellStyle1 = wb.createCellStyle();
        //设置左右对齐居中
        cellStyle1.setAlignment(HorizontalAlignment.CENTER_SELECTION);
        //垂直对其居中
        cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
        //设置true让Cell中的内容以多行显示
        cellStyle1.setWrapText(true);
        //创建一个单元格,单元格下标从0开始
        Cell c1=row1.createCell(0);
        //创建富文本,如果你想每一行显示一个字体那么就按照我下面的写,如果想多个显示在一行,那么去掉他们之间的\r\n就好了
        c1.setCellValue(createHelper.createRichTextString("合\r\n并\r\n后\r\n选\r\n择\r\n我\r\n，\r\n这\r\n只\r\n值\r\n！"));
        //设置样式
        c1.setCellStyle(cellStyle1);
        //写入文件
        wb.write(fileOut);
        //关闭输出流
        fileOut.close();
    }

    //合并单元格,指定 4 个参数，起始行，结束行，起始列，结束列。这个区域将被合并;更复杂些的，需要自己计算好行与列即可
    private static int cellRangeAddress(final XSSFSheet sheet,final int startRow,final int endRow,final int startCol,final int endCol){
        return sheet.addMergedRegion(new CellRangeAddress(startRow, endRow, startCol, endCol));
    }

    public static void export(final String label,final List<HashMap<String,Object>> data,final HttpServletResponse response) throws Exception {
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        workbook(label,data).write(os);//填充数据
        final byte[] content = os.toByteArray();
        final InputStream is = new ByteArrayInputStream(content);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((ToolString.getIdsChar32() + ".xlsx").getBytes(), "iso-8859-1"));
        final ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            final byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }

    /**
	 * 生成 Sheet工作簿,数据是从第指定startRow+1行可是写入数据,因为1是表头标题栏
	 * @param list 数据集合
	 * @param fields 数据库字段,要与titles的add顺序要一致
	 * @param titles 标题栏,要与fields的add顺序要一致
	 * @param wb
	 * @param sheetName sheet名称
	 * @param startRow 从开始行startRow写起;0表示第1行,1表示第2行,
	 * @param titleDescribe 给导出的Excel表格起个标题或描述
	 * @作者 田应平
	 * @返回值类型 XSSFSheet
	 * @创建时间 2017年9月5日 16:11:51
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	private static XSSFSheet createSheet(final List<Map<String, Object>> list,final ArrayList<String> fields, final ArrayList<String> titles,final XSSFWorkbook wb,final String sheetName,int startRow,final String titleDescribe){
		final XSSFSheet sheet = wb.createSheet(sheetName);
		// 手动设置列宽。第一个参数表示要为第几列设,第二个参数表示列的宽度，n为列高的像素数。
		for (int i = 0; i < fields.size();i++){
			sheet.setColumnWidth(i,(int)(35.7 * 150));
		}
		if (startRow < 4)startRow = 4;
		int line = 1;//让其‘titleDescribe’上下各隔一行
		final Row rowDescribe = sheet.createRow(line);//第几行行创建row
		final XSSFCellStyle cellStyleDescribe = wb.createCellStyle();
		final int lastRow = startRow-2;
		final int firstCol = 1;
		final int lastCol = fields.size()-2;
		sheet.addMergedRegion(new CellRangeAddress(line,lastRow,firstCol,lastCol));//合并单元格;0,fields.size()-1
		final Cell cellDescribe = rowDescribe.createCell(firstCol);//第几列
        cellStyleDescribe.setAlignment(HorizontalAlignment.CENTER_SELECTION);//水平居中显示
        cellStyleDescribe.setVerticalAlignment(VerticalAlignment.CENTER);//垂直居中显示
		cellDescribe.setCellValue(titleDescribe);
		cellDescribe.setCellStyle(cellStyleDescribe);
		final Font font = wb.createFont();
		// 创建第一种字体样式（用于表头）
		font.setFontHeightInPoints((short)14);
		font.setColor(IndexedColors.BLACK.getIndex());
		cellStyleDescribe.setFont(font);

		// 创建第一行,表头行
		final Row rowTitle = sheet.createRow(startRow);
		// 创建两种单元格格式
        final XSSFCellStyle csHead = wb.createCellStyle();
		final XSSFCellStyle csContent = wb.createCellStyle();
        csHead.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        csHead.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
		// 创建两种字体
		final Font fontHead = wb.createFont();
		final Font fontContent = wb.createFont();
		// 创建第一种字体样式（用于表头）
		fontHead.setFontHeightInPoints((short) 12);
		fontHead.setColor(IndexedColors.WHITE.getIndex());
        fontHead.setBold(true);
		// 创建第二种字体样式（用于数据行）
		fontContent.setFontHeightInPoints((short) 11);
		fontContent.setColor(IndexedColors.BLACK.getIndex());

        // 设置第一种单元格的样式（用于表头）
        rowCellStyle(csHead,fontHead);
        // 设置第二种单元格的样式（用于数据行）
        rowCellStyle(csContent,fontContent);
        // 设置列名,表头
		for (int i = 0; i < titles.size();i++){
			final Cell cell = rowTitle.createCell(i);
			cell.setCellValue(titles.get(i));
			cell.setCellStyle(csHead);
		}
		// 设置每行每列的值
		for (int i = 0; i < list.size();i++){
			// Row 行,Cell 方格 , Row 和 Cell 都是从0开始计数的
			// 创建一行，在页sheet上
			final Row rowData = sheet.createRow(i+startRow+1);
			// 在row行上创建一个方格
			for (int j = 0; j < fields.size(); j++){
				final Cell cell = rowData.createCell(j);
				cell.setCellValue(list.get(i).get(fields.get(j)) == null ? " " : list.get(i).get(fields.get(j)).toString());
				cell.setCellStyle(csContent);
			}
		}
		return sheet;
	}

    /**
	 * 生成并下载Excel表格,通过fields和titles的添加顺序生成想要的表头顺序
	 * @param listData 要导出的最初数据集合
	 * @param fields 从数据库读取的字段名集合,fields和titles添加顺序一定要对应起来,注意添加字段的顺序
	 * @param titles Excel表格表头第1行名集合,titles和fields添加顺序一定要对应起来
	 * @param sheetName 左下角第一块的名称
	 * @param fileName 导出后下载的文件名,如果为空则以生成32位的UUID作为文件名.xlsx
	 * @作者 田应平
	 * @创建时间 2017年1月10日 上午10:28:46
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static void exportExcel(final List<Map<String, Object>> listData, final ArrayList<String> fields, final ArrayList<String> titles, final String sheetName, final String fileName, final HttpServletResponse response) throws Exception {
		if(fields.size() != titles.size())throw new Exception("导出失败,标题和列数不一致");
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		createWorkBook(listData,fields,titles,sheetName).write(os);// 填充数据
		final byte[] content = os.toByteArray();
		final InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + new String(((fileName == null ? ToolString.getIdsChar32() : fileName) + ".xlsx").getBytes(), "iso-8859-1"));
		final ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			final byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}
	
	/**
	 * 生成并下载Excel表格,通过fields和titles的添加顺序生成想要的表头顺序
	 * @param listData 要导出的最初数据集合
	 * @param fields 从数据库读取的字段名集合,fields和titles添加顺序一定要对应起来,注意添加字段的顺序
	 * @param titles 表头第1行名集合,titles和fields添加顺序一定要对应起来
	 * @param sheetName 左下角第一块的名称
	 * @param fileName 导出后下载的文件名,如果为空则以生成32位的UUID作为文件名.xlsx
	 * @param startRow 从开始行startRow写起;0表示第1行,1表示第2行
	 * @param titleDescribe 给导出的Excel表格起个标题或描述
	 * @作者 田应平
	 * @创建时间 2017年9月5日 20:16:55
	 * @QQ号码 444141300
	 * @主页 http://www.fwtai.com
	*/
	public static void exportExcel(final List<Map<String, Object>> listData, final ArrayList<String> fields, final ArrayList<String> titles, final String sheetName, final String fileName,final int startRow,final String titleDescribe,final HttpServletResponse response) throws Exception {
		if(fields.size() != titles.size())throw new Exception("导出失败,标题和列数不一致");
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		createWorkBook(listData,fields,titles,sheetName,startRow,titleDescribe).write(os);// 填充数据
		final byte[] content = os.toByteArray();
		final InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + new String(((fileName == null ? ToolString.getIdsChar32() : fileName) + ".xlsx").getBytes(), "iso-8859-1"));
		final ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			final byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}

	/**
	 * 生成并下载Excel表格,表头顺序不固定
	 * @param listData 要导出的最初数据集合
	 * @param mapper → mapper.put("name","姓名");mapper.put("age","年龄");
	 * @param sheetName 左下角第一块的名称
	 * @param fileName 导出后下载的文件名,如果为空则以生成32位的UUID作为文件名.xlsx
	 * @作者 田应平
	 * @创建时间 2017年8月31日 20:14:25
	 * @QQ号码 444141300
	 * @官网 http://www.fwtai.com
	*/
	public static void exportExcel(final List<Map<String,Object>> listData,final HashMap<String,String> mapper,final String sheetName, final String fileName, final HttpServletResponse response) throws Exception {
		final ByteArrayOutputStream os = new ByteArrayOutputStream();
		final ArrayList<String> fields = new ArrayList<String>(mapper.size());
		final ArrayList<String> header = new ArrayList<String>(mapper.size());
		for (final String key : mapper.keySet()){
			header.add(mapper.get(key));
			fields.add(key);
		}
		createWorkBook(listData,fields,header,sheetName).write(os);//填充数据
		final byte[] content = os.toByteArray();
		final InputStream is = new ByteArrayInputStream(content);
		// 设置response参数，可以打开下载页面
		response.reset();
		response.setContentType("application/vnd.ms-excel;charset=utf-8");
		response.setHeader("Content-Disposition", "attachment;filename=" + new String(((fileName == null ? ToolString.getIdsChar32() : fileName) + ".xlsx").getBytes(), "iso-8859-1"));
		final ServletOutputStream out = response.getOutputStream();
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		try {
			bis = new BufferedInputStream(is);
			bos = new BufferedOutputStream(out);
			final byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
		} catch (final IOException e) {
			throw e;
		} finally {
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
		}
	}
}