package com.zony.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ExcelUtil {
    private static final Logger log = LoggerFactory.getLogger(ExcelUtil.class);

   /**
    * @Description: 权限手册解析
    * @Date 2020/09/01 11:45
    * @Author Michael
    * @param inputStream 需要解析的excel文件流
    * @param jurisdictionId 权限手册主体数据库Id
    * @return
    */
   /*
    public static List<JurisdictionAnalysisGroup> importExcel(InputStream inputStream , long jurisdictionId) throws FileNotFoundException {
        List<JurisdictionAnalysisGroup> list=new ArrayList<>();
        log.info("导入解析开始");
        try {
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet;
            Row row;
            Cell cell;
            String sheetName;
            HSSFWorkbook hssfWorkbook  = new HSSFWorkbook();;
            //遍历所有sheet
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                List<JurisdictionAnalysisGroup> sheetList=new ArrayList<>();
                //获取当前页签
                sheet = workbook.getSheetAt(i);
                sheetName=sheet.getSheetName();
                //判断页签是否为空和页签是否是正确的页签
                if (sheet == null||(sheet.getSheetName().contains("sheet"))) {
                    continue;
                }
                //当前序号
                String currentNumber="";
                //当前事项
                String currentItem="";
                //角色所在行
                int roleRow=0;
                //角色集合
                Map<Integer,String> roleMap = new HashedMap ();
                //具体数据所在行
                Boolean flag=false;
                //遍历当前sheet的所有行
               for (int j = sheet.getFirstRowNum(); j <sheet.getLastRowNum(); j++) {
                    row = sheet.getRow(j);
                    if (row == null ) {
                        continue;
                    }
                    if("序号".equals(row.getCell(0)+"")){
                        roleRow=j+1;
                        flag=true;
                        continue;
                    }
                    if(j==roleRow){
                        for (int y = 3; y < row.getLastCellNum(); y++) {
                            cell = row.getCell(y);
                            if("".equals(cell+"")){
                                if(roleMap.size()==0){
                                    continue;
                                }
                                break;
                            }
                            roleMap.put(y,cell+"");
                        }
                        continue;
                    }
                    //序号之后的行
                    if(flag){
                        if(StringUtils.isNotEmpty(row.getCell(0)+"")&&!(row.getCell(0)+"").contains("（")){
                            currentNumber=row.getCell(0)+"";
                            currentItem=row.getCell(1)+"";
                            continue;
                        }
                        if(StringUtils.isEmpty(row.getCell(1)+"")){
                            continue;
                        }
                        JurisdictionAnalysisGroup jurisdiction = new JurisdictionAnalysisGroup();
                        JSONObject jsonObject=new JSONObject();
                        jurisdiction.setSerialNumber(currentNumber);
                        jurisdiction.setItem(currentItem);
                        //权限手册主体ID，用于主体和解析数据关联
                        jurisdiction.setJurisdictionId(jurisdictionId);
                        //遍历每一个单元格
                        for (int y = 1; y <row.getLastCellNum(); y++) {
                            cell = row.getCell(y);
                            if(y==1){
                                jurisdiction.setSubNumber(cell+"");
                                continue;
                            }
                            if(y==2){
                                jurisdiction.setSubItem(cell+"");
                                continue;
                            }
                            if(roleMap.containsKey(y)){
                                jsonObject.put(roleMap.get(y),cell+"");
                            }
                        }
                        jurisdiction.setRoleRights(JSON.toJSONString(jsonObject));
                        jurisdiction.setBusinessClass(sheetName);
                        sheetList.add(jurisdiction);
                    }
                }
                list.addAll(sheetList);
            }
        }catch (Exception e){
            log.info("导入文件解析失败！");
            e.printStackTrace();
        }

        return list;
    }
    */
    /*
    public static void exportExcel(List<JurisdictionAnalysisGroup> list, String sheetName, HSSFWorkbook workbook) throws IOException, IOException {
        //创建Excel工作表对象
        HSSFSheet sheet = workbook.createSheet(sheetName);
        for(int i=0;i<list.size();i++){
            JurisdictionAnalysisGroup jurisdiction= list.get(i);
            //创建行的单元格，从0开始
            HSSFRow row = sheet.createRow(i);
            //创建单元格1
            HSSFCell cell1 = row.createCell(0);
            cell1.setCellValue(jurisdiction.getSerialNumber());
            //创建单元格2
            HSSFCell cell2 = row.createCell(1);
            cell2.setCellValue(jurisdiction.getSubNumber());
            //创建单元格3
            HSSFCell cell3 = row.createCell(2);
            cell3.setCellValue(jurisdiction.getItem());
            //创建单元格4
            HSSFCell cell4 = row.createCell(3);
            cell4.setCellValue(jurisdiction.getSubItem());

            JSONObject jsonObject= (JSONObject) JSON.parse(jurisdiction.getRoleRights());
            int j=5;
            //fastjson解析方法
            for (Map.Entry<String, Object> entry : jsonObject.entrySet()) {
                HSSFCell cell= row.createCell(j-1);
                cell.setCellValue(entry.getKey());
                j++;
            }
        }
    }
    */

    public static  void main(String[] args) throws Exception {
//        List<JurisdictionAnalysis> list=
//                importExcel(new FileInputStream("D:\\有限公司权限手册示例 20200721.xlsx"));
//        log.info(String.valueOf(list.size()));
    }
}
