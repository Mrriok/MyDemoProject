package com.zony.app.service;

import com.zony.app.domain.Regulationsort;
import com.zony.app.domain.SystemDocument;
import com.zony.app.enums.InstLevelEnum;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.InstStatusTypeEnum;
import com.zony.app.repository.RegulationsortRepository;
import com.zony.app.repository.SystemDocumentRepository;
import com.zony.app.service.criteria.RegulationlibraryQueryCriteria;
import com.zony.app.service.dto.SystemDocExportMainDto;
import com.zony.app.service.dto.SystemDocExportVersionDto;
import com.zony.common.config.FileProperties;
import com.zony.common.utils.QueryHelp;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.awt.Color;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 制度库数据导出
 *
 * @Author gubin
 * @Date 2020-10-22
 */
@Service
@RequiredArgsConstructor
public class ExportService {

    private final SystemDocumentRepository systemDocumentRepository;
    private final RegulationsortRepository regulationsortRepository;
    private final FileProperties properties;

    public String exportExcel(List<Long> regulationsortIdList) throws IOException {
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook();
        for (int i = 0; i < regulationsortIdList.size(); i++) {
            Long regulationsortId = regulationsortIdList.get(i);
            Regulationsort regulationsort = regulationsortRepository.findById(regulationsortId).get();
            Regulationsort regulationsortParent = regulationsortRepository.findById(regulationsort.getPid()).get();
            String classSystemCode = regulationsortParent.getMenuId() + "-" + regulationsort.getMenuId();
            XSSFSheet sheet = xssfWorkbook.createSheet();
            xssfWorkbook.setSheetName(i, regulationsort.getMenuId() + "." + regulationsort.getName());
            XSSFRow headRow = sheet.createRow(0);
            headRow.setHeight((short) (26.25 * 20));
            XSSFCellStyle valueStyle = genValueStyle(xssfWorkbook);
            List<SystemDocExportMainDto> mainDtoList = queryLevel(classSystemCode);
            //计算所占行数
            mainDtoList.forEach(this::doCalculateRowCount);
            //计算开始行索引
            this.doCalculateRowIndex(mainDtoList, 1);
            //构造表头
            this.createHeader(xssfWorkbook, sheet, headRow);
            //构造数据行
            Map<Integer, XSSFRow> rowMap = new HashMap<>();
            this.setRowValue(1, rowMap, sheet, mainDtoList, valueStyle);
            //合并制度编号
            this.setMergedRegion(sheet, mainDtoList, 1);
            //所有单元格画线
            this.drawLineForCell(sheet, valueStyle);
        }
        String tempFilePath = properties.getPath().getPath() + UUID.randomUUID() + ".xlsx";
        try (OutputStream outputStream = new FileOutputStream(tempFilePath)) {
            xssfWorkbook.write(outputStream);
        }
        return tempFilePath;
    }

    /**
     * 构造表头
     *
     * @Author gubin
     * @Date 2020-10-23
     */
    public void createHeader(XSSFWorkbook xssfWorkbook, XSSFSheet sheet, XSSFRow headRow) {
        XSSFCellStyle baseHeaderStyle = genHeaderStyle(xssfWorkbook, InstLevelEnum.BASE_INST);
        XSSFCellStyle manageHeaderStyle = genHeaderStyle(xssfWorkbook, InstLevelEnum.MANAGEMENT_METHOD);
        XSSFCellStyle operateHeaderStyle = genHeaderStyle(xssfWorkbook, InstLevelEnum.OPERATING_RULE);
        //列宽
        double[] sheetWidth = {
                12.25, 37.5, 16.75, 12.63, 27, 18.25, 11.75,//基本制度
                15.88, 31, 17.13, 9.88, 25.75, 18.25, 14.75,//管理办法
                19.88, 28.5, 16.63, 12.25, 27.75, 17.13, 11.25//操作细则
        };
        //标题
        String[] titleHeader = {
                "编号", "基本制度", "编写部门", "当前版本", "当前版本发布文号", "当前版本发布日期", "生效期",
                "编号", "管理办法（或指引）", "编写部门", "当前版本", "当前版本发布文号", "当前版本发布日期", "生效期",
                "编号", "操作细则", "编写部门", "当前版本", "当前版本发布文号", "当前版本生效日期", "生效期"
        };
        for (int i = 0; i < sheetWidth.length; i++) {
            sheet.setColumnWidth(i, (int) (256 * sheetWidth[i] + 184));
            XSSFCell cell = headRow.createCell(i);
            cell.setCellValue(titleHeader[i]);
            if (i < 7) {
                cell.setCellStyle(baseHeaderStyle);
            } else if (i < 14) {
                cell.setCellStyle(manageHeaderStyle);
            } else {
                cell.setCellStyle(operateHeaderStyle);
            }
        }
    }

    /**
     * 画单元格框线
     *
     * @Author gubin
     * @Date 2020-10-23
     */
    public void drawLineForCell(XSSFSheet sheet, XSSFCellStyle valueStyle) {
        for (int i = 0; i <= sheet.getLastRowNum(); i++) {
            for (int j = 0; j <= 20; j++) {
                XSSFCell cell = sheet.getRow(i).getCell(j);
                if (cell == null) {
                    createXSSFCell(sheet.getRow(i), j, null, valueStyle);
                }
            }
        }
    }

    /**
     * 合并单元格
     *
     * @Author gubin
     * @Date 2020-10-23
     */
    public void setMergedRegion(XSSFSheet sheet, List<SystemDocExportMainDto> mainDtoList, int levelCount) {
        int colIndex = 0;
        if (levelCount == 1) {
            colIndex = 0;
        } else if (levelCount == 2) {
            colIndex = 7;
        } else {
            colIndex = 14;
        }
        for (SystemDocExportMainDto mainDto : mainDtoList) {
            int startRowIndex = mainDto.getStartRowIndex();
            int endRowIndex = mainDto.getStartRowIndex() + mainDto.getVersionList().size() - 1;
            if (endRowIndex > startRowIndex) {
                CellRangeAddress region = new CellRangeAddress(startRowIndex, endRowIndex, colIndex, colIndex);
                sheet.addMergedRegion(region);
            }
            this.setMergedRegion(sheet, mainDto.getSubLevelList(), levelCount + 1);
        }
    }

    /**
     * 构造表格行
     *
     * @Author gubin
     * @Date 2020-10-22
     */
    public void setRowValue(int levelCount, Map<Integer, XSSFRow> rowMap, XSSFSheet sheet, List<SystemDocExportMainDto> mainDtoList, XSSFCellStyle valueStyle) {
        for (SystemDocExportMainDto mainDto : mainDtoList) {
            List<SystemDocExportVersionDto> versionDtoList = mainDto.getVersionList();
            for (int i = 0; i < versionDtoList.size(); i++) {
                int rowIndex = mainDto.getStartRowIndex() + i;
                SystemDocExportVersionDto versionDto = versionDtoList.get(i);
                XSSFRow row = rowMap.get(rowIndex);
                if (row == null) {
                    row = sheet.createRow(rowIndex);
                    row.setHeight((short) (34.5 * 20));
                    rowMap.put(rowIndex, row);
                }
                createXSSFCell(row, (levelCount - 1) * 7, versionDto.getSystemCode(), valueStyle);
                createXSSFCell(row, (levelCount - 1) * 7 + 1, versionDto.getSystemTitle(), valueStyle);
                createXSSFCell(row, (levelCount - 1) * 7 + 2, versionDto.getDeptName(), valueStyle);
                createXSSFCell(row, (levelCount - 1) * 7 + 3, versionDto.getVersion(), valueStyle);
                createXSSFCell(row, (levelCount - 1) * 7 + 4, versionDto.getPublishSymbol(), valueStyle);
                createXSSFCell(row, (levelCount - 1) * 7 + 5, versionDto.getPublishDateStr(), valueStyle);
                createXSSFCell(row, (levelCount - 1) * 7 + 6, versionDto.getEffectiveDateStr(), valueStyle);
            }
            this.setRowValue(levelCount + 1, rowMap, sheet, mainDto.getSubLevelList(), valueStyle);
        }
    }

    /**
     * 构造表头单元格样式
     *
     * @Author gubin
     * @Date 2020-10-22
     */
    public XSSFCellStyle genHeaderStyle(XSSFWorkbook workbook, InstLevelEnum instLevelEnum) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        XSSFFont titleFont = workbook.createFont();
        titleFont.setBold(true);
        titleFont.setFontName("微软雅黑");
        titleFont.setFontHeightInPoints((short) 12);
        style.setFont(titleFont);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        switch (instLevelEnum) {
            case BASE_INST:
                style.setFillForegroundColor(new XSSFColor(new Color(252, 228, 214)));
                break;
            case MANAGEMENT_METHOD:
                style.setFillForegroundColor(new XSSFColor(new Color(244, 176, 132)));
                break;
            case OPERATING_RULE:
                style.setFillForegroundColor(new XSSFColor(new Color(198, 89, 17)));
                break;
        }
        return style;
    }

    /**
     * 构造单元格样式
     *
     * @Author gubin
     * @Date 2020-10-22
     */
    public XSSFCellStyle genValueStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        XSSFFont titleFont = workbook.createFont();
        titleFont.setBold(false);
        titleFont.setFontName("微软雅黑");
        titleFont.setFontHeightInPoints((short) 12);
        style.setFont(titleFont);
        return style;
    }

    /**
     * 构造单元格
     *
     * @Author gubin
     * @Date 2020-10-22
     */
    public void createXSSFCell(XSSFRow row, int cellIndex, String cellValue, CellStyle cellStyle) {
        XSSFCell cell = row.createCell(cellIndex);
        cell.setCellValue(createCellValue(cellValue));
        cell.setCellStyle(cellStyle);
    }

    /**
     * 设置单元格内容值
     *
     * @Author gubin
     * @Date 2020-10-22
     */
    public String createCellValue(String value) {
        return StringUtils.isBlank(value) ? "" : value.trim();
    }

    /**
     * 基本制度查询条件
     *
     * @Author gubin
     * @Date 2020-10-22
     */
    public RegulationlibraryQueryCriteria createBasicQueryCriteria(String classSystemCode) {
        RegulationlibraryQueryCriteria basicQueryCriteria = new RegulationlibraryQueryCriteria();
        basicQueryCriteria.setSystemCode(classSystemCode);
        basicQueryCriteria.setInstStatusType(InstStatusTypeEnum.LIBRARY);
        basicQueryCriteria.setInstStatus(InstStatusEnum.ACCEPTED);
        basicQueryCriteria.setInstLevel(InstLevelEnum.BASE_INST);
        return basicQueryCriteria;
    }

    /**
     * 管理办法查询条件
     *
     * @Author gubin
     * @Date 2020-10-22
     */
    public RegulationlibraryQueryCriteria createRegulationQueryCriteria(String basicSystemCode) {
        RegulationlibraryQueryCriteria regulationQueryCriteria = new RegulationlibraryQueryCriteria();
        regulationQueryCriteria.setSystemCode(basicSystemCode);
        regulationQueryCriteria.setInstStatusType(InstStatusTypeEnum.LIBRARY);
        regulationQueryCriteria.setInstStatus(InstStatusEnum.ACCEPTED);
        regulationQueryCriteria.setInstLevel(InstLevelEnum.MANAGEMENT_METHOD);
        return regulationQueryCriteria;
    }

    /**
     * 操作细则查询条件
     *
     * @Author gubin
     * @Date 2020-10-22
     */
    public RegulationlibraryQueryCriteria createOperationQueryCriteria(String regulationSystemCode) {
        RegulationlibraryQueryCriteria operationQueryCriteria = new RegulationlibraryQueryCriteria();
        operationQueryCriteria.setSystemCode(regulationSystemCode);
        operationQueryCriteria.setInstStatusType(InstStatusTypeEnum.LIBRARY);
        operationQueryCriteria.setInstStatus(InstStatusEnum.ACCEPTED);
        operationQueryCriteria.setInstLevel(InstLevelEnum.OPERATING_RULE);
        return operationQueryCriteria;
    }

    /**
     * 构造版本对象
     *
     * @Author gubin
     * @Date 2020-10-22
     */
    public SystemDocExportVersionDto createVersionDto(SystemDocument docObj) {
        SystemDocExportVersionDto versionDto = new SystemDocExportVersionDto();
        versionDto.setDeptName(docObj.getCompanyName());
        versionDto.setEffectiveDateStr(docObj.getEffectiveDateStr());
        versionDto.setPublishDateStr(docObj.getPublishDateStr());
        versionDto.setPublishSymbol(docObj.getPublishSymbol());
        versionDto.setSystemCode(docObj.getSystemCode());
        versionDto.setSystemTitle(docObj.getSystemTitle());
        versionDto.setVersion(docObj.getVersion());
        return versionDto;
    }

    /**
     * 构造版本组合对象
     *
     * @Author gubin
     * @Date 2020-10-22
     */
    public List<SystemDocExportMainDto> createMainDto(List<SystemDocument> docList) {
        List<SystemDocExportMainDto> mainDtoList = new ArrayList<>();
        SystemDocExportMainDto curMainDto = new SystemDocExportMainDto();
        for (SystemDocument docObj : docList) {
            SystemDocExportVersionDto versionDto = this.createVersionDto(docObj);
            if (curMainDto.getSystemCode() == null || !curMainDto.getSystemCode().equals(docObj.getSystemCode())) {
                SystemDocExportMainDto mainDto = new SystemDocExportMainDto();
                curMainDto = mainDto;
                mainDtoList.add(mainDto);
                mainDto.setSystemCode(docObj.getSystemCode());
            }
            curMainDto.getVersionList().add(versionDto);
        }
        return mainDtoList;
    }

    /**
     * 查询导出数据
     *
     * @Author gubin
     * @Date 2020-10-22
     */
    public List<SystemDocExportMainDto> queryLevel(String classSystemCode) {
        Sort sort = new Sort(Sort.Direction.ASC, "systemCode", "version", "modifyCode");
        //第一层
        RegulationlibraryQueryCriteria basicQueryCriteria = this.createBasicQueryCriteria(classSystemCode);
        List<SystemDocument> basicList = systemDocumentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, basicQueryCriteria, criteriaBuilder), sort);
        List<SystemDocExportMainDto> basicMainDtoList = this.createMainDto(basicList);
        for (SystemDocExportMainDto basicMainDto : basicMainDtoList) {
            //第二层
            RegulationlibraryQueryCriteria regulationQueryCriteria = this.createRegulationQueryCriteria(basicMainDto.getSystemCode());
            List<SystemDocument> regulationList = systemDocumentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, regulationQueryCriteria, criteriaBuilder), sort);
            List<SystemDocExportMainDto> regulationMainDtoList = this.createMainDto(regulationList);
            basicMainDto.setSubLevelList(regulationMainDtoList);//第一层设置下级
            basicMainDto.setLevelNo(1);
            for (SystemDocExportMainDto regulationMainDto : regulationMainDtoList) {
                //第三层
                RegulationlibraryQueryCriteria operationQueryCriteria = this.createOperationQueryCriteria(regulationMainDto.getSystemCode());
                List<SystemDocument> operationList = systemDocumentRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root, operationQueryCriteria, criteriaBuilder), sort);
                List<SystemDocExportMainDto> operationMainDtoList = this.createMainDto(operationList);
                regulationMainDto.setParent(basicMainDto);//第二层设置上级
                regulationMainDto.setSubLevelList(operationMainDtoList);//第二层设置下级
                regulationMainDto.setLevelNo(2);
                for (SystemDocExportMainDto operationMainDto : operationMainDtoList) {
                    operationMainDto.setParent(regulationMainDto);//第三层设置上级
                    operationMainDto.setLevelNo(3);
                }
            }
        }
        return basicMainDtoList;
    }


    /**
     * 计算所占行数：下级所占最大行数与本级版本数，取最大值
     *
     * @Author gubin
     * @Date 2020-10-23
     */
    public int doCalculateRowCount(SystemDocExportMainDto mainDto) {
        int rowCount = 0;
        //有下级
        if (mainDto.getLevelNo() != 3) {
            //累加下级所占行数
            for (SystemDocExportMainDto subMainDto : mainDto.getSubLevelList()) {
                rowCount += this.doCalculateRowCount(subMainDto);
            }
            //与本级版本数比较，取最大值
            if (rowCount < mainDto.getVersionList().size()) {
                rowCount = mainDto.getVersionList().size();
            }
        }
        //最下级
        else {
            rowCount = mainDto.getVersionList().size();
        }
        mainDto.setRowCount(rowCount);
        return rowCount;
    }

    /**
     * 计算开始行索引=上一个元素行索引+所占行数，第一个元素继承上级索引
     *
     * @Author gubin
     * @Date 2020-10-23
     */
    public void doCalculateRowIndex(List<SystemDocExportMainDto> mainDtoList, int startRowIndex) {
        for (SystemDocExportMainDto mainDto : mainDtoList) {
            mainDto.setStartRowIndex(startRowIndex);
            this.doCalculateRowIndex(mainDto.getSubLevelList(), mainDto.getStartRowIndex());
            startRowIndex = startRowIndex + mainDto.getRowCount();
        }
    }
}
