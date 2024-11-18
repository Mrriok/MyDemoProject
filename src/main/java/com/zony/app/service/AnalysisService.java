package com.zony.app.service;

import com.google.common.collect.Sets;
import com.zony.app.domain.AnalysisGroup;
import com.zony.app.domain.AnalysisItem;
import com.zony.app.domain.AnalysisRights;
import com.zony.app.domain.AnalysisTable;
import com.zony.app.service.dto.ExcelAreaDto;
import com.zony.app.service.dto.ExcelCombineCellDto;
import com.zony.app.service.dto.ExcelRegularDto;
import com.zony.common.utils.JsonUtil;
import com.zony.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.stereotype.Service;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 解析权限手册Excel服务
 *
 * @Author gubin
 * @Date 2020-09-22
 */
@Service
@RequiredArgsConstructor
public class AnalysisService {

    /**
     * 解析Excel
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public List<AnalysisTable> readFromExcel(String excelPath) throws Exception {
        Workbook workbook = WorkbookFactory.create(new FileInputStream(excelPath));
        List<AnalysisTable> analysisTableList = new ArrayList<>();
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            //判断页签是否为空和页签是否是正确的页签
            if (sheet.getSheetName().trim().toLowerCase().contains("sheet")) {
                continue;
            }
            analysisTableList.addAll(this.readTableOfOneSheet(sheet));
        }
        return analysisTableList;
    }

    /**
     * 解析Excel中的一页
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public List<AnalysisTable> readTableOfOneSheet(Sheet sheet) {
        ExcelRegularDto excelRegularDto = new ExcelRegularDto();
        List<AnalysisTable> analysisTableList = new ArrayList<>();
        List<CellRangeAddress> cellRangeAddressList = this.getCombineCellList(sheet);
        List<Integer> tableRowIndexList = this.readTableRowIndexList(sheet, excelRegularDto);
        for (int i = 0; i < tableRowIndexList.size(); i++) {
            AnalysisTable analysisTable = new AnalysisTable();
            List<Integer> groupRowIndexList = new ArrayList<>();//事项组-行序号
            List<ExcelAreaDto> accordingAreaList = new ArrayList<>();//依据
            List<ExcelAreaDto> timeLimitAreaList = new ArrayList<>();//时限
            int tableRowIndex = tableRowIndexList.get(i);
            Row seqRow = sheet.getRow(tableRowIndex);//序号行
            Cell seqCell = seqRow.getCell(excelRegularDto.getSeqCellIndex());//序号列
            ExcelCombineCellDto seqCombineCellDto = this.getCombineInfo(cellRangeAddressList, seqCell);
            Cell itemCell = seqRow.getCell(excelRegularDto.getSeqCellIndex() + seqCombineCellDto.getColCount());//审批备案列
            ExcelCombineCellDto itemCombineCellDto = this.getCombineInfo(cellRangeAddressList, itemCell);
            Cell roleRightsTitleCell = seqRow.getCell(itemCell.getColumnIndex() + itemCombineCellDto.getColCount());//角色权限标题列
            ExcelCombineCellDto roleRightsTitleCombineCellDto = this.getCombineInfo(cellRangeAddressList, roleRightsTitleCell);
            Row roleRightsValueRow = sheet.getRow(tableRowIndex + roleRightsTitleCombineCellDto.getRowCount());//角色权限内容行
            int roleCount = roleRightsTitleCombineCellDto.getColCount();//角色数量
            int[] tableRowIndexRange = this.getIndexRange(tableRowIndexList, i, sheet.getLastRowNum());
            Cell accordingTitleCell = seqRow.getCell(roleRightsTitleCell.getColumnIndex() + roleCount);//编制依据标题列
            ExcelCombineCellDto accordingCombineCellDto = this.getCombineInfo(cellRangeAddressList, accordingTitleCell);
            Cell timeLimitTitleCell = seqRow.getCell(accordingTitleCell.getColumnIndex() + accordingCombineCellDto.getColCount());//审批时限要求标题列
            //扫描各类数据所在行序号
            this.readRowIndexListInTable(analysisTable, tableRowIndexRange, sheet, cellRangeAddressList, excelRegularDto, seqCombineCellDto,
                    accordingTitleCell, timeLimitTitleCell, groupRowIndexList, accordingAreaList, timeLimitAreaList);
            //获取表格内的事项组
            List<AnalysisGroup> analysisGroupList = this.getAnalysisGroupListOfTable(analysisTable, sheet, excelRegularDto, cellRangeAddressList, groupRowIndexList,
                    tableRowIndexRange[1], seqCombineCellDto, accordingAreaList, timeLimitAreaList, roleRightsTitleCell, roleRightsValueRow, roleCount);
            if (analysisGroupList.size() > 0) {
                this.setAnalysisTableValue(analysisTable, sheet, excelRegularDto, cellRangeAddressList);
                analysisTable.setAnalysisGroupSet(Sets.newHashSet(analysisGroupList));
                analysisTableList.add(analysisTable);
            }
        }
        return analysisTableList;
    }

    /**
     * 设置表格相关属性值
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public void setAnalysisTableValue(AnalysisTable analysisTable, Sheet sheet, ExcelRegularDto excelRegularDto, List<CellRangeAddress> cellRangeAddressList) {
        ExcelCombineCellDto titleExcelCombineCellDto = getCombineInfo(cellRangeAddressList, sheet.getRow(excelRegularDto.getTitleRowIndex()).getCell(excelRegularDto.getTitleCellIndex()));
        //业务名称行
        Row businessNameRow = sheet.getRow(excelRegularDto.getBusinessNameRowIndex());
        String businessName = this.getCellValue(businessNameRow.getCell(excelRegularDto.getBusinessNameCellIndex()));
        String[] businessNameArray = businessName.split(excelRegularDto.getBussinessNameValue());
        if (businessNameArray.length == 2) {
            analysisTable.setBusinessName(businessNameArray[1]);
        }
        String departName = this.getCellValue(businessNameRow.getCell(titleExcelCombineCellDto.getColCount() - 1));
        String[] departNameArray = departName.split(excelRegularDto.getDepartValue());
        if (departNameArray.length == 2) {
            analysisTable.setDepartName(departNameArray[1]);
        }
    }

    /**
     * 获取表格内的事项组
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public List<AnalysisGroup> getAnalysisGroupListOfTable(AnalysisTable analysisTable, Sheet sheet, ExcelRegularDto excelRegularDto, List<CellRangeAddress> cellRangeAddressList, List<Integer> groupRowIndexList,
                                                           int maxTableRowIndex, ExcelCombineCellDto seqCombineCellDto, List<ExcelAreaDto> accordingAreaList, List<ExcelAreaDto> timeLimitAreaList,
                                                           Cell roleRightsTitleCell, Row roleRightsValueRow, int roleCount) {
        List<AnalysisGroup> analysisGroupList = new ArrayList<>();
        for (int i = 0; i < groupRowIndexList.size(); i++) {
            int groupIndex = groupRowIndexList.get(i);
            int[] groupRowIndexRange = this.getIndexRange(groupRowIndexList, i, maxTableRowIndex);
            //事项组
            AnalysisGroup analysisGroup = this.createAnalysisGroup(analysisTable, sheet, groupIndex, excelRegularDto, seqCombineCellDto);
            if (analysisGroup == null) {
                continue;
            }
            List<AnalysisItem> itemList = new ArrayList<>();
            for (int j = groupRowIndexRange[0]; j < groupRowIndexRange[1]; j++) {
                Row itemRow = sheet.getRow(j);
                if (itemRow == null) {
                    continue;
                }
                //事项行
                AnalysisItem analysisItem = this.createAnalysisItem(itemRow, analysisGroup, excelRegularDto, seqCombineCellDto, accordingAreaList, timeLimitAreaList, cellRangeAddressList);
                if (analysisItem == null) {
                    continue;
                }
                //角色权限
                List<AnalysisRights> rightsList = this.createAnalysisRightsList(roleCount, itemRow, analysisItem, roleRightsTitleCell, roleRightsValueRow);
                if (rightsList.size() > 0) {
                    analysisItem.setAnalysisRights(Sets.newHashSet(rightsList));
                    itemList.add(analysisItem);
                }
            }
            if (itemList.size() > 0) {
                analysisGroup.setAnalysisItemSet(Sets.newHashSet(itemList));
                analysisGroupList.add(analysisGroup);
            }
        }
        return analysisGroupList;
    }

    /**
     * 扫描各类数据所在行序号
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public void readRowIndexListInTable(AnalysisTable analysisTable, int[] tableRowIndexRange, Sheet sheet, List<CellRangeAddress> cellRangeAddressList, ExcelRegularDto excelRegularDto,
                                        ExcelCombineCellDto seqCombineCellDto, Cell accordingTitleCell, Cell timeLimitTitleCell,
                                        List<Integer> groupRowIndexList, List<ExcelAreaDto> accordingAreaList, List<ExcelAreaDto> timeLimitAreaList) {

        for (int i = tableRowIndexRange[0]; i < tableRowIndexRange[1]; i++) {
            Row tableBodyRow = sheet.getRow(i);
            if (tableBodyRow == null) {
                continue;
            }
            //扫描：事项组
            Cell groupCell = tableBodyRow.getCell(excelRegularDto.getSeqCellIndex() + seqCombineCellDto.getColCount());//事项组列
            ExcelCombineCellDto groupCombineCellDto = this.getCombineInfo(cellRangeAddressList, groupCell);
            if (groupCombineCellDto.isCombineFlag() && groupCombineCellDto.getColCount() >= excelRegularDto.getGroupColCount()) {
                groupRowIndexList.add(i);
            }
            //扫描：依据文档
            if (accordingTitleCell != null) {
                Cell accordingValueCell = tableBodyRow.getCell(accordingTitleCell.getColumnIndex());//依据列
                ExcelCombineCellDto accordingValueCombineCellDto = this.getCombineInfo(cellRangeAddressList, accordingValueCell);
                String accordingStr = this.getCellValue(accordingValueCell);
                if (StringUtils.isNotBlank(accordingStr)) {
                    accordingAreaList.add(new ExcelAreaDto(accordingStr, tableBodyRow.getRowNum(), accordingValueCombineCellDto.getRowCount(), accordingValueCombineCellDto.getColCount()));
                }
            }
            //扫描：时限要求
            if (timeLimitTitleCell != null) {
                Cell timeLimitCell = tableBodyRow.getCell(timeLimitTitleCell.getColumnIndex());//时限列
                ExcelCombineCellDto timeLimitCombineCellDto = this.getCombineInfo(cellRangeAddressList, timeLimitCell);
                String timeLimitStr = this.getCellValue(timeLimitCell);
                if (StringUtils.isNotBlank(timeLimitStr)) {
                    timeLimitAreaList.add(new ExcelAreaDto(timeLimitStr, tableBodyRow.getRowNum(), timeLimitCombineCellDto.getRowCount(), timeLimitCombineCellDto.getColCount()));
                }
            }
            //扫描：审核要点
            Cell keyPointCell = tableBodyRow.getCell(excelRegularDto.getKeyPointIndex());//审核要点
            String keyPointCellValue = this.getCellValue(keyPointCell);
            if (keyPointCellValue != null && keyPointCellValue.matches(excelRegularDto.getKeyPointValue())) {
                List<String> keyPointStrList = new ArrayList<>();
                for (int j = i + 1; j < tableRowIndexRange[1]; j++) {
                    Row keyPointRow = sheet.getRow(j);
                    if (keyPointRow == null) {
                        continue;
                    }
                    Cell keyPointValueCell = keyPointRow.getCell(excelRegularDto.getKeyPointIndex());
                    String keyPointStr = this.getCellValue(keyPointValueCell);
                    if (StringUtils.isNotBlank(keyPointStr)) {
                        keyPointStrList.add(keyPointStr);
                    }
                }
                if (keyPointStrList.size() > 0) {
                    analysisTable.setKeyPoint(JsonUtil.getInstance().obj2json(keyPointStrList));
                }
            }
        }
    }

    /**
     * 计算表格序号
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public List<Integer> readTableRowIndexList(Sheet sheet, ExcelRegularDto excelRegularDto) {
        List<Integer> tableRowIndexList = new ArrayList<>();//表格组（序号）
        for (int i = sheet.getFirstRowNum(); i < sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null) {
                continue;
            }
            if (excelRegularDto.getTitleValue().equals(this.getCellValue(row.getCell(excelRegularDto.getTitleCellIndex())))) {
                excelRegularDto.setTitleRowIndex(i);
            }
            if (excelRegularDto.getSeqValue().equals(this.getCellValue(row.getCell(excelRegularDto.getSeqCellIndex())))) {
                tableRowIndexList.add(i);
            }
        }
        return tableRowIndexList;
    }

    /**
     * 构造事项组
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public AnalysisGroup createAnalysisGroup(AnalysisTable analysisTable, Sheet sheet, int groupIndex, ExcelRegularDto excelRegularDto, ExcelCombineCellDto seqCombineCellDto) {
        Cell groupNameCell = sheet.getRow(groupIndex).getCell(excelRegularDto.getSeqCellIndex() + seqCombineCellDto.getColCount());
        String groupName = this.getCellValue(groupNameCell);
        if (StringUtils.isBlank(groupName)) {
            return null;
        }
        AnalysisGroup analysisGroup = new AnalysisGroup();
        analysisGroup.setGroupSeq(this.getCellValue(sheet.getRow(groupIndex).getCell(excelRegularDto.getSeqCellIndex())));
        analysisGroup.setGroupName(groupName);
        analysisGroup.setAnalysisTable(analysisTable);
        return analysisGroup;
    }

    /**
     * 构造事项
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public AnalysisItem createAnalysisItem(Row itemRow, AnalysisGroup analysisGroup, ExcelRegularDto excelRegularDto, ExcelCombineCellDto seqCombineCellDto,
                                           List<ExcelAreaDto> accordingAreaList, List<ExcelAreaDto> timeLimitAreaList, List<CellRangeAddress> cellRangeAddressList) {
        Cell itemSeqCell = itemRow.getCell(excelRegularDto.getSeqCellIndex() + seqCombineCellDto.getColCount());
        if (itemSeqCell == null) {
            return null;
        }
        ExcelCombineCellDto itemSeqExcelCombineCellDto = getCombineInfo(cellRangeAddressList, itemSeqCell);
        Cell itemNameCell = itemRow.getCell(itemSeqCell.getColumnIndex() + itemSeqExcelCombineCellDto.getColCount());
        String itemName = this.getCellValue(itemNameCell);
        if (StringUtils.isBlank(itemName)) {
            return null;
        }
        AnalysisItem analysisItem = new AnalysisItem();
        analysisItem.setItemSeq(this.getCellValue(itemSeqCell));
        analysisItem.setItemName(itemName);
        analysisItem.setAnalysisGroup(analysisGroup);
        analysisItem.setAccording(this.getBelongToArea(itemRow.getRowNum(), accordingAreaList).getCellValue());
        analysisItem.setTimeLimit(this.getBelongToArea(itemRow.getRowNum(), timeLimitAreaList).getCellValue());
        return analysisItem;
    }

    /**
     * 查找所属区域
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public ExcelAreaDto getBelongToArea(int itemRowIndex, List<ExcelAreaDto> accordingAreaList) {
        for (ExcelAreaDto accordingArea : accordingAreaList) {
            if (accordingArea.getRowIndex() <= itemRowIndex && accordingArea.getRowIndex() + accordingArea.getRowCount() > itemRowIndex) {
                return accordingArea;
            }
        }
        return new ExcelAreaDto(null, 0, 0, 0);
    }

    /**
     * 构造事项角色权限(集合）
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public List<AnalysisRights> createAnalysisRightsList(int roleCount, Row itemRow, AnalysisItem analysisItem, Cell roleRightsTitleCell, Row roleRightsValueRow) {
        List<AnalysisRights> rightsList = new ArrayList<>();
        for (int k = 0; k < roleCount; k++) {
            int roleRightsIndex = roleRightsTitleCell.getColumnIndex() + k;
            String roleValue = this.getCellValue(roleRightsValueRow.getCell(roleRightsIndex));
            String rightsValue = this.getCellValue(itemRow.getCell(roleRightsIndex));
            if (StringUtils.isNotBlank(rightsValue)) {
                AnalysisRights analysisRights = new AnalysisRights();
                analysisRights.setRoleName(roleValue);
                analysisRights.setRoleRights(rightsValue);
                analysisRights.setAnalysisItem(analysisItem);
                rightsList.add(analysisRights);
            }
        }
        return rightsList;
    }

    /**
     * 计算序号区间
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public int[] getIndexRange(List<Integer> indexList, int start, int max) {
        int startValue = indexList.get(start) + 1;
        int endValue = indexList.get(indexList.size() - 1);
        if (start + 1 < indexList.size()) {
            endValue = indexList.get(start + 1);
        }
        if (startValue >= endValue) {
            endValue = max;
        }
        return new int[]{startValue, endValue};
    }

    /**
     * 获取合并单元格集合
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public List<CellRangeAddress> getCombineCellList(Sheet sheet) {
        List<CellRangeAddress> list = new ArrayList<>();
        //获得一个 sheet 中合并单元格的数量
        int sheetmergerCount = sheet.getNumMergedRegions();
        //遍历所有的合并单元格
        for (int i = 0; i < sheetmergerCount; i++) {
            //获得合并单元格保存进list中
            CellRangeAddress ca = sheet.getMergedRegion(i);
            list.add(ca);
        }
        return list;
    }

    /**
     * 判断cell是否为合并单元格，是的话返回合并行数和列数
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public ExcelCombineCellDto getCombineInfo(List<CellRangeAddress> combineCellList, Cell cell) {
        ExcelCombineCellDto excelCombineCellDto = new ExcelCombineCellDto();
        if (cell != null && StringUtils.isNotBlank(cell.toString())) {
            for (CellRangeAddress ca : combineCellList) {
                int firstC = ca.getFirstColumn();
                int lastC = ca.getLastColumn();
                int firstR = ca.getFirstRow();
                int lastR = ca.getLastRow();
                if (cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR) {
                    if (cell.getColumnIndex() >= firstC && cell.getColumnIndex() <= lastC) {
                        int mergedRow = lastR - firstR + 1;
                        int mergedCol = lastC - firstC + 1;
                        excelCombineCellDto.setCombineFlag(true);
                        excelCombineCellDto.setRowCount(mergedRow);
                        excelCombineCellDto.setColCount(mergedCol);
                        return excelCombineCellDto;
                    }
                }
            }
        }
        excelCombineCellDto.setCombineFlag(false);
        excelCombineCellDto.setRowCount(1);
        excelCombineCellDto.setColCount(1);
        return excelCombineCellDto;
    }

    /**
     * 获取单元格过滤后的值
     *
     * @Author gubin
     * @Date 2020-09-22
     */
    public String getCellValue(Cell cell) {
        if (cell == null) {
            return null;
        } else {
            return cell.toString().replaceAll("[\\n\\s]", "");
        }
    }
}
