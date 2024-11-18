package com.zony.app.service;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.jacob.com.ComThread;
import com.jacob.com.Dispatch;
import com.jacob.com.Variant;
import com.zony.app.domain.SystemDocument;
import com.zony.app.service.dto.*;
import com.zony.app.service.mapstruct.SystemDocumentWordMapper;
import com.zony.common.config.FileProperties;
import com.zony.common.utils.JsonUtil;
import com.zony.common.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.util.*;


/**
 * 制度文档编制服务
 *
 * @Author gubin
 * @Date 2020-09-17
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class EditService {

    private final FileProperties properties;
    private final SystemDocumentWordMapper systemDocumentWordMapper;

    /**
     * 方法1：读取文档页眉数据
     */
    public String readHeader(String filePath) {
        String resultStr = "";
        ComThread.InitSTA();
        try {
            Dispatch test = new Dispatch("ZonySpire.ReadWord");
            Variant result = Dispatch.call(test, "ReadHeader", filePath);
            resultStr = result.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ComThread.Release();
        }
        return resultStr;
    }

    /**
     * 方法2：读取文档表格数据
     */
    public String readTable(String filePath) {
        String resultStr = "[]";
        ComThread.InitSTA();
        try {
            Dispatch test = new Dispatch("ZonySpire.ReadWord");
            Variant result = Dispatch.call(test, "ReadTable", filePath);
            resultStr = result.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ComThread.Release();
        }
        return resultStr;
    }

    /**
     * 方法3：读取文档文本段数据
     */
    public String readText(String filePath) {
        String resultStr = "[]";
        ComThread.InitSTA();
        try {
            Dispatch test = new Dispatch("ZonySpire.ReadWord");
            Variant result = Dispatch.call(test, "ReadText", filePath);
            resultStr = result.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ComThread.Release();
        }
        return resultStr;
    }

    /**
     * 方法4：读取文档文本框数据
     */
    public String readTextBox(String filePath) {
        String resultStr = "[]";
        ComThread.InitSTA();
        try {
            Dispatch test = new Dispatch("ZonySpire.ReadWord");
            Variant result = Dispatch.call(test, "ReadTextBox", filePath);
            resultStr = result.toString();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ComThread.Release();
        }
        return resultStr;
    }

    /**
     * 方法5：替换文档数据
     */
    public boolean replaceText(String templateFilePath, String outputFilePath, Map<String, String> replaceMap) {
        boolean resultFlag = false;
        ComThread.InitSTA();
        try {
            Dispatch test = new Dispatch("ZonySpire.ReadWord");
            String mapJson = JsonUtil.getInstance().obj2json(replaceMap);
            Variant result = Dispatch.call(test, "ReplaceText", templateFilePath,
                    outputFilePath, mapJson);
            resultFlag = result.getBoolean();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ComThread.Release();
        }
        return resultFlag;
    }

    /**
     * 生成制度正文
     *
     * @Author gubin
     * @Date 2020-09-17
     */
    public String writeToWord(SystemDocument systemDocument) throws Exception {
        return this.writeToWordFromDto(systemDocumentWordMapper.toDto(systemDocument));
    }

    public String writeToWordFromDto(SystemDocumentWordDto systemDocument) throws Exception {
        String templateName;
        //设置页眉
        this.setHeaderForWriteToWord(systemDocument);
        //设置修改码
        this.setModifyCodeForWriteToWord(systemDocument);
        //对象转Map
        JsonUtil jsonUtil = JsonUtil.getInstance();
        String objJson = jsonUtil.obj2json(systemDocument);
        Map<String, Object> objMap = (Map<String, Object>) jsonUtil.json2obj(objJson, Map.class);
        Map<String, String> replaceMap = new HashMap<>();
        Map<String, String> mapKeyMap = ImmutableMap.of("docAccording", "systemTitle", "docReplyRiskTarget", "value", "docExplain", "value");
        objMap.forEach((key, value) -> {
            String replaceKey = "#{" + key.toUpperCase() + "}";
            if (value == null) {
                replaceMap.put(replaceKey, "");
            } else {
                if (value instanceof List) {
                    StringBuilder valueStr = new StringBuilder();
                    String mapKeyStr = mapKeyMap.get(key);
                    if (mapKeyStr != null) {
                        List<Map<String, Object>> valueList = (List<Map<String, Object>>) value;
                        for (int i = 0; i < valueList.size(); i++) {
                            if (i != valueList.size() - 1) {
                                valueStr.append(valueList.get(i).get(mapKeyStr)).append("\r");
                            } else {
                                valueStr.append(valueList.get(i).get(mapKeyStr));
                            }
                        }
                    } else {
                        List<String> valueList = (List<String>) value;
                        for (int i = 0; i < valueList.size(); i++) {
                            if (i != valueList.size() - 1) {
                                valueStr.append(valueList.get(i)).append("\r");
                            } else {
                                valueStr.append(valueList.get(i));
                            }
                        }
                    }
                    replaceMap.put(replaceKey, valueStr.toString());
                } else {
                    replaceMap.put(replaceKey, value.toString());
                }
            }
        });
        //获取制度正文模版
        switch (systemDocument.getInstLevel()) {
            case BASE_INST:
                templateName = "Level_1.doc";
                break;
            case MANAGEMENT_METHOD:
                templateName = "Level_2.doc";
                break;
            case OPERATING_RULE:
                templateName = "Level_3.doc";
                break;
            default:
                throw new RuntimeException("参数不合法。");
        }
        //制度正文模版存放目录
        String templateFolder = ResourceUtils.getURL("classpath:").getPath().substring(1) + "public/systemDoc";
        String templateFilePath = templateFolder + "/" + templateName;
        templateFilePath = templateFilePath.replace("/", "\\");
        //生成的临时文件
        String tempFileName = UUID.randomUUID() + ".docx";
        String tempFilePath = properties.getPath().getPath() + tempFileName;
        boolean flag = this.replaceText(templateFilePath, tempFilePath, replaceMap);
        if (!flag) {
            throw new RuntimeException("文档生成失败。");
        }
        return tempFileName;
    }

    /**
     * 设置页眉
     *
     * @Author gubin
     * @Date 2020-10-15
     */
    private void setHeaderForWriteToWord(SystemDocumentWordDto systemDocument) {
        String headerPartOne = systemDocument.getInstitutionalName() + "制度体系-" + systemDocument.getSystemCode();
        String headerPartTwo = systemDocument.getCompanyName() + systemDocument.getSystemTitle();
        //页眉
        int spaceCount = 91 - (this.getStrLength(headerPartOne) + this.getStrLength(headerPartTwo));
        //页眉太长，省去公司名称
        if (spaceCount < 0) {
            headerPartTwo = systemDocument.getSystemTitle();
            spaceCount = 91 - (this.getStrLength(headerPartOne) + this.getStrLength(headerPartTwo));
        }
        StringBuilder spaceStr = new StringBuilder(" ");
        for (int i = 0; i < spaceCount - 1; i++) {
            spaceStr.append(" ");
        }
        systemDocument.setHeader(headerPartOne + spaceStr + headerPartTwo);
    }

    /**
     * 设置修改码
     *
     * @Author gubin
     * @Date 2020-10-15
     */
    private void setModifyCodeForWriteToWord(SystemDocumentWordDto systemDocument) {
        StringBuilder setModifyCodeStr = new StringBuilder();
        for (int i = 0; i <= 5; i++) {
            setModifyCodeStr.append(i);
            if (String.valueOf(i).equals(systemDocument.getModifyCode())) {
                setModifyCodeStr.append("√");
            } else {
                setModifyCodeStr.append("□");
            }
        }
        systemDocument.setModifyCode(setModifyCodeStr.toString());
    }

    /**
     * 读取制度正文
     *
     * @Author gubin
     * @Date 2020-09-17
     */
    public SystemDocument readFromWord(String fileName) {
        return readFromWord(fileName, null);
    }

    public SystemDocument readFromWord(String fileName, String filePath) {
        return systemDocumentWordMapper.toEntity(this.readFromWordToDto(fileName, filePath));
    }

    public SystemDocumentWordDto readFromWordToDto(String fileName, String filePath) {
        SystemDocumentWordDto systemDocument = new SystemDocumentWordDto();
        File file;
        if (StringUtils.isBlank(filePath)) {
            String path = properties.getPath().getPath() + File.separatorChar + "upload-file" + File.separatorChar + "file";
            file = new File(path, fileName);
        } else {
            file = new File(filePath);
        }
        String textBoxJson = this.readTextBox(file.getAbsolutePath());
        String tableJson = this.readTable(file.getAbsolutePath());
        String textJson = this.readText(file.getAbsolutePath());
        List<String> textBoxValueList = (List<String>) JsonUtil.getInstance().json2obj(textBoxJson, List.class);
        List<String> textValueList = (List<String>) JsonUtil.getInstance().json2obj(textJson, List.class);
        List<String> tableValueList = (List<String>) JsonUtil.getInstance().json2obj(tableJson, List.class);
        if (tableValueList.size() > 23) {
            systemDocument.setInstitutionalName(tableValueList.get(2));//体系名称
            systemDocument.setSystemCode(tableValueList.get(4));//编码
            systemDocument.setVersion(tableValueList.get(6));//版本号
            systemDocument.setModifyCode(this.readModifyCode(tableValueList.get(8)));//修改码
            systemDocument.setCompanyName(tableValueList.get(11));//公司名称
            systemDocument.setApprover(tableValueList.get(13));//批准人
            systemDocument.setAccording(tableValueList.get(15));//批准依据
            systemDocument.setPublishRange(tableValueList.get(17));//发布范围
            systemDocument.setPublishSymbol(tableValueList.get(19));//发布文号
            systemDocument.setPublishDateStr(tableValueList.get(21));//发布日期
            systemDocument.setEffectiveDateStr(tableValueList.get(23));//生效日期
        } else {
            String endRegex = "[:：]([\\w\\W]*)";
            List<String> expList = Lists.newArrayList(toRegex("公司名称") + endRegex,
                    toRegex("批准") + endRegex, toRegex("批准依据") + endRegex,
                    toRegex("发布文号") + endRegex, toRegex("发布日期") + endRegex,
                    toRegex("生效日期") + endRegex, toRegex("发布范围") + endRegex);
            List<Integer> indexList = Lists.newArrayList(-1, -1, -1, -1, -1, -1, -1);
            this.matchExpAndSetIndex(textValueList, expList, indexList);
            systemDocument.setCompanyName(this.getStringValue(textValueList, indexList.get(0)));//公司名称
            systemDocument.setApprover(this.getStringValue(textValueList, indexList.get(1)));//批准人
            systemDocument.setAccording(this.getStringValue(textValueList, indexList.get(2)));//批准依据
            systemDocument.setPublishSymbol(this.getStringValue(textValueList, indexList.get(3)));//发布文号
            systemDocument.setPublishDateStr(this.getStringValue(textValueList, indexList.get(4)));//发布日期
            systemDocument.setEffectiveDateStr(this.getStringValue(textValueList, indexList.get(5)));//生效日期
            systemDocument.setPublishRange(this.getStringValue(textValueList, indexList.get(6)));//发布范围

            String valueRegex = "([\\w\\W]*)";
            String keyEndRegex = "[:：]";
            //体系名称
            String systemNameKeyRegex = toRegex("体系名称") + keyEndRegex;
            String systemNameValue = getStringValueByRegex(textBoxValueList, systemNameKeyRegex + valueRegex, systemNameKeyRegex);
            systemDocument.setInstitutionalName(systemNameValue);
            //编码
            String codingKeyRegex = toRegex("编码") + keyEndRegex;
            String codingValue = getStringValueByRegex(textBoxValueList, codingKeyRegex + valueRegex, codingKeyRegex);
            systemDocument.setSystemCode(codingValue);
            //版本
            String versionKeyRegex = toRegex("版本") + keyEndRegex;
            String versionValue = getStringValueByRegex(textBoxValueList, versionKeyRegex + valueRegex, versionKeyRegex);
            systemDocument.setVersion(versionValue);
            //修改码
            String modiyCodeKeyRegex = toRegex("修改码") + keyEndRegex;
            String modiyCodeValue = getStringValueByRegex(textBoxValueList, modiyCodeKeyRegex + valueRegex, modiyCodeKeyRegex);
            systemDocument.setModifyCode(this.readModifyCode(modiyCodeValue));
        }
        List<String> expList = Lists.newArrayList(toRegex("1目的"), toRegex("2适用范围"),
                toRegex("3编制依据"), toRegex("4主要应对的风险及应对目标"),
                toRegex("5释义"), toRegex("6职责分工"));
        List<Integer> indexList = Lists.newArrayList(-1, -1, -1, -1, -1, -1);
        this.matchExpAndSetIndex(textValueList, expList, indexList);
        //1 目的
        systemDocument.setDocPurpose(this.getListValue(textValueList, indexList.get(0), indexList.get(1)));
        //2 适用范围
        systemDocument.setDocApplyRange(this.getListValue(textValueList, indexList.get(1), indexList.get(2)));
        //3 编制依据
        List<String> docDocAccordingStrList = this.getListValue(textValueList, indexList.get(2), indexList.get(3));
        systemDocument.setDocAccording(this.createDocAccordingDtoList(docDocAccordingStrList, systemDocument.getCompanyName()));
        //4 主要应对的风险及应对目标
        List<String> docReplyRiskTargetStrList = this.getListValue(textValueList, indexList.get(3), indexList.get(4));
        systemDocument.setDocReplyRiskTarget(this.createDocReplyRiskTargetDtoList(docReplyRiskTargetStrList, systemDocument.getCompanyName()));
        //5 释义
        List<String> docExplainStrList = this.getListValue(textValueList, indexList.get(4), indexList.get(5));
        systemDocument.setDocExplain(this.createDocExplainStrList(docExplainStrList, systemDocument.getCompanyName()));
        //读取页眉中的制度名称
        String textHeader = this.readHeader(file.getAbsolutePath());//页眉
        systemDocument.setSystemTitle(this.readSystemTitleFromHeader(textHeader, systemDocument.getCompanyName()));//制度名称
        return systemDocument;
    }

    /**
     * 字符串转为SystemDocumentSmallDto，带上公司名称作为来源
     *
     * @Author gubin
     * @Date 2020-10-12
     */
    private List<SystemDocumentSmallDto> createDocAccordingDtoList(List<String> docDocAccordingStrList, String companyName) {
        List<SystemDocumentSmallDto> list = new ArrayList<>();
        for (String str : docDocAccordingStrList) {
            list.add(new SystemDocumentSmallDto(str, companyName));
        }
        return list;
    }

    /**
     * 字符串转为DocReplyRiskTargetDto，带上公司名称作为来源
     *
     * @Author gubin
     * @Date 2020-10-12
     */
    private List<DocReplyRiskTargetDto> createDocReplyRiskTargetDtoList(List<String> docReplyRiskTargetStrList, String companyName) {
        List<DocReplyRiskTargetDto> list = new ArrayList<>();
        for (String str : docReplyRiskTargetStrList) {
            list.add(new DocReplyRiskTargetDto(str, companyName));
        }
        return list;
    }

    /**
     * 字符串转为DocExplainDto，带上公司名称作为来源
     *
     * @Author gubin
     * @Date 2020-10-12
     */
    private List<DocExplainDto> createDocExplainStrList(List<String> docExplainStrList, String companyName) {
        List<DocExplainDto> list = new ArrayList<>();
        for (String str : docExplainStrList) {
            list.add(new DocExplainDto(str, companyName));
        }
        return list;
    }

    /**
     * 字符串转正则表达式，插入空格制表符
     *
     * @Author gubin
     * @Date 2020-09-28
     */
    private String toRegex(String str) {
        String regexSpace = "(\\s*|\\t*)";
        List<String> list = new ArrayList<>();
        for (char c : str.toCharArray()) {
            list.add(String.valueOf(c));
        }
        return regexSpace + StringUtils.join(list, regexSpace) + regexSpace;
    }

    /**
     * 匹配正则表达式，设置索引值
     *
     * @Author gubin
     * @Date 2020-09-17
     */
    private void matchExpAndSetIndex(List<String> textValueList, List<String> expList, List<Integer> indexList) {
        for (int i = 0; i < textValueList.size(); i++) {
            String textValue = textValueList.get(i);
            for (int j = 0; j < expList.size(); j++) {
                if (textValue.matches(expList.get(j))) {
                    indexList.set(j, i);
                    break;
                }
            }
        }
    }

    /**
     * 按正则表达式切割取值
     *
     * @Author gubin
     * @Date 2020-09-17
     */
    private String getStringValueByRegex(List<String> allList, String wholeRegex, String splitRegex) {
        for (String valueStr : allList) {
            if (valueStr.matches(wholeRegex)) {
                String[] valueArray = valueStr.split(splitRegex);
                if (valueArray.length >= 1) {
                    return valueArray[1];
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 按索引切割取值
     *
     * @Author gubin
     * @Date 2020-09-17
     */
    private String getStringValue(List<String> allList, int index) {
        if (index != -1 && allList.size() > index) {
            return allList.get(index).split("[:：]")[1].trim();
        }
        return null;
    }

    /**
     * 按区间取值
     *
     * @Author gubin
     * @Date 2020-09-17
     */
    private List<String> getListValue(List<String> allList, int beginIndex, int endIndex) {
        List<String> retList = new ArrayList<>();
        if (beginIndex != -1 && endIndex != -1) {
            for (int i = beginIndex + 1; i < endIndex; i++) {
                retList.add(allList.get(i));
            }
        }
        return retList;
    }

    /**
     * 计算中英文字符串的字节长度,一个中文算2个字节
     *
     * @Author gubin
     * @Date 2020-10-14
     */
    private int getStrLength(String str) {
        if (str == null || str.length() == 0) {
            return 0;
        }
        int len = 0;
        for (int i = 0, j = str.length(); i < j; i++) {
            len += (str.charAt(i) > 255 ? 2 : 1);
        }
        return len;
    }

    /**
     * 读取页眉属性(制度名称)
     *
     * @Author gubin
     * @Date 2020-10-15
     */
    private String readSystemTitleFromHeader(String header, String companyName) {
        try {
            if (StringUtils.isNotBlank(header)) {
                String[] properyArray = header.split("(\\s|\\t)+");
                //去除公司名称
                return properyArray[1].replace(companyName, "");
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 读取修改码
     *
     * @Author gubin
     * @Date 2020-10-15
     */
    private String readModifyCode(String modifyCode) {
        if (StringUtils.isNotBlank(modifyCode)) {
            String readModifyCode = modifyCode.replace(" ", "");
            int checkIndex = readModifyCode.indexOf("√");
            if (checkIndex == -1) {
                return "0";
            } else {
                return String.valueOf(readModifyCode.charAt(checkIndex - 1));
            }
        } else {
            return null;
        }
    }
}
