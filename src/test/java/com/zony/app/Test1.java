package com.zony.app;

import com.google.common.collect.Lists;
import com.zony.app.domain.AnalysisTable;
import com.zony.app.domain.Jurisdiction;
import com.zony.app.domain.SystemDocument;
import com.zony.app.domain.User;
import com.zony.app.repository.AnalysisTableRepository;
import com.zony.app.repository.JurisdictionReposity;
import com.zony.app.repository.UserRepository;
import com.zony.app.service.AnalysisService;
import com.zony.app.service.FileUploadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Test1 {

    @Autowired
    private AnalysisService jurisdictionService;
    @Autowired
    private JurisdictionReposity jurisdictionReposity;
    @Autowired
    private AnalysisTableRepository analysisTableRepository;
    @Autowired
    private FileUploadService fileUploadService;
    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional(rollbackFor=Exception.class)
    @Rollback(false)
    public void test1() throws IOException {

        String path = fileUploadService.createZip(139830, "SystemDocument");
        System.out.println(path);
//        //根据ID查询权限手册主体
//        Jurisdiction jurisdiction = jurisdictionReposity.findById(86742L).orElseGet(Jurisdiction::new);
//        jurisdictionReposity.delete(jurisdiction);
//        Set<AnalysisTable> tableSet = jurisdiction.getAnalysisTableSet();
//        tableSet.forEach(table->analysisTableRepository.deleteById(table.getId()));
    }

    @Test
    @Transactional(rollbackFor=Exception.class)
    @Rollback(false)
    public void test2() throws IOException {
        List<User> list = userRepository.findByUsernameIn(Lists.newArrayList("test","admin"));
        System.out.println(list);
    }
}
