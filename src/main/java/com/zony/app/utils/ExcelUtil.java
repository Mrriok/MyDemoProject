package com.zony.app.utils;

import org.jxls.area.Area;
import org.jxls.builder.AreaBuilder;
import org.jxls.builder.xml.XmlAreaBuilder;
import org.jxls.common.CellRef;
import org.jxls.common.Context;
import org.jxls.transform.Transformer;
import org.jxls.transform.poi.PoiTransformer;
import org.jxls.util.JxlsHelper;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class ExcelUtil {

	private String dir = "public/excelTemplate";
	
	public InputStream getTemplate(String path) throws FileNotFoundException{
		String CLASS_PATH="classpath:";
		InputStream is;
		if(path.startsWith(CLASS_PATH)){
			is = ExcelUtil.class.getClassLoader().getResourceAsStream(path.substring(CLASS_PATH.length()));
		}else{
			is= new FileInputStream(path);
		}
		return is;
	}
	
	public void exportExcel(String tmplName,Map<String,Object> contextMap,OutputStream outputStream) throws IOException{
		InputStream tmplInputStream = getTemplate(dir+"/"+tmplName);
		Context context = new Context();
		if(contextMap != null){
			context.toMap().putAll(contextMap);
		}
		JxlsHelper.getInstance().processTemplate(tmplInputStream, outputStream,context);
	}
    /**
     * @param excelName
     *            要生成的文件名字
     * @return
     * @throws IOException
     */
    private ServletOutputStream generateResponseExcel(String excelName, HttpServletResponse response) throws IOException {
        excelName = excelName == null || "".equals(excelName) ? "excel" : URLEncoder.encode(excelName, "UTF-8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + excelName + ".xlsx");

        return response.getOutputStream();
    }

	public void exportExcel(InputStream inputStream,Map<String,Object> contextMap,OutputStream outputStream) throws IOException{
		Context context = new Context();
		if(contextMap != null){
			context.toMap().putAll(contextMap);
		}
		JxlsHelper.getInstance().processTemplate(inputStream, outputStream,context);
	}
	
	/**
	 * 动态列
	 */
	public void exportExcel(InputStream tmpl,InputStream tmplcfg,List<String> headers,List<List<Object>> rows,OutputStream outputStream) throws IOException{
		Transformer transformer = JxlsHelper.getInstance().createTransformer(tmpl, outputStream);
        AreaBuilder areaBuilder = new XmlAreaBuilder(tmplcfg, transformer);
        List<Area> xlsAreaList = areaBuilder.build();
        Area xlsArea = xlsAreaList.get(0);
        Context context = PoiTransformer.createInitialContext();
        context.putVar("headers", headers);
        context.putVar("rows", rows);
        xlsArea.applyAt(new CellRef("Result!A1"), context);
        transformer.write();
	}
	
	public void close(InputStream inputStream, InputStream xmlStream, OutputStream out) {
		try {
			if (inputStream != null) {
				inputStream.close();
			}
			if (xmlStream != null) {
				xmlStream.close();
			}
			if (out != null) {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
