package com.zony.app;

import com.google.common.collect.Lists;
import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.vo.SystemDocumentDetailVo;
import com.zony.app.enums.InstLevelEnum;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.InstStatusTypeEnum;
import com.zony.app.repository.SystemDocumentRepository;
import com.zony.app.service.*;
import com.zony.app.service.dto.DocExplainDto;
import com.zony.app.service.dto.SystemDocSubListDto;
import com.zony.app.service.dto.SystemDocumentWordDto;
import com.zony.common.utils.JsonUtil;
import com.zony.common.utils.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SystemDocumentTests {

    @Autowired
    RegulationlibraryService regulationlibraryService;
    @Autowired
    SystemDocumentService systemDocmentService;
    @Autowired
    SystemDocumentRepository systemDocumentRepository;
    @Autowired
    EditService editService;
    @Autowired
    HistoryImportService historyImportService;
    @Autowired
    ExportService exportService;

    @Test
    public void exportService() throws IOException {
        //"CG-01":公司治理-公司章程
        exportService.exportExcel(Lists.newArrayList(52373L,52375L,52378L));
    }

    @Test
    public void importHistoryData() {
        historyImportService.doImport("E:\\项目文档\\中国海洋石油有限公司制度平台\\有限制度历史数据\\1-有限制度_test");
    }

    @Test
    public void queryLevelImage() {
        List<SystemDocSubListDto> list = regulationlibraryService.queryLevelImage("1019-CG-04");
        System.out.println(list);
    }
    @Test
    public void view() {
//        SystemDocumentFormVo systemDocumentFormVo = systemDocmentService.view(37002L);
//        System.out.println(systemDocumentFormVo);
        List<SystemDocumentDetailVo> volist = regulationlibraryService.view("44645456");
        System.out.println(volist);
    }

    @Test
    public void readWordTable() throws Exception {
        String tableJson = editService.readTable("C:\\zony\\temp\\Level_3.doc");
        List<String> valueList = (List<String>)JsonUtil.getInstance().json2obj(tableJson,List.class);
        System.out.println(valueList);
    }

    @Test
    public void readWordText() throws Exception {
        //SystemDocumentWordDto systemDocumentWordDto = editService.readFromWord("C:\\zony\\temp\\Level_3.doc");
        //System.out.println(systemDocumentWordDto);

        SystemDocument systemDocument1 = editService.readFromWord("Level_1.doc");
        SystemDocument systemDocument3 = editService.readFromWord("Level_3.doc");
        System.out.println(systemDocument1);
        System.out.println(systemDocument3);
    }


    @Test
    public void writeToWord() throws Exception {
        JsonUtil jsonUtil = JsonUtil.getInstance();
        SystemDocument systemDocument = new SystemDocument();
        systemDocument.setSystemTitle("测试标题");
        systemDocument.setInstLevel(InstLevelEnum.OPERATING_RULE);
        systemDocument.setCompanyName("公司名test");
        String str = editService.writeToWord(systemDocument);
        System.out.println(str);
    }

    @Test
    public void queryMultiLevelList() {
        SystemDocumentWordDto systemDocument = new SystemDocumentWordDto();
        systemDocument.setCompanyName("公司名test");
        systemDocument.setDocExplain(Lists.newArrayList(new DocExplainDto("释义test1","公司1"), new DocExplainDto("释义test2","公司2")));
        JsonUtil jsonUtil = JsonUtil.getInstance();
        String objJson = jsonUtil.obj2json(systemDocument);
        Map<String, Object> replaceMap = (Map<String, Object>) jsonUtil.json2obj(objJson, Map.class);
        System.out.println(replaceMap);
    }

    @Test
    public void buildData() {
        for (int i = 1; i < 10; i++) {
            SystemDocument systemDocument = new SystemDocument();
            systemDocument.setCurrentVersionFlag(true);
            systemDocument.setInstStatus(InstStatusEnum.ACCEPTED);
            systemDocument.setInstStatusType(InstStatusTypeEnum.LIBRARY);
            systemDocument.setInstLevel(InstLevelEnum.OPERATING_RULE);
            systemDocument.setSystemCode("HR-" + StringUtils.leftPad(i + "", 2, "0") + "-" + StringUtils.leftPad(i + "", 2, "0") + "-" + StringUtils.leftPad(i + "", 2, "0"));
            systemDocument.setSystemTitle("中国海油石油总公司机构与编制操作细则" + StringUtils.leftPad(i + "", 2, "0"));
            systemDocumentRepository.save(systemDocument);
        }

    }
}
