package com.zony.common.utils;



import com.zony.app.domain.SystemDocument;
import com.zony.app.enums.InstLevelEnum;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.collections.map.HashedMap;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class WordUtil {
    private static final Logger log = LoggerFactory.getLogger(WordUtil.class);
    private static final String COMPANYNAME="公司名称：";
    private static final String APPROVER="批 准 人：";
    private static final String APPROVALBASIS="批准依据：";
    private static final String PUBLISHNUMBER="发布文号：";
    private static final String PUBLISHDATE="发布日期：";
    private static final String PUBLISHSCOPE="发布范围：";
    private static final String VALIDDATE="生效日期：";

    private static final String COMPANYNAME_OPERATINGRULE="公司名称\u0007";
    private static final String APPROVER_OPERATINGRULE="批准人\u0007";
    private static final String APPROVALBASIS_OPERATINGRULE="批准依据\u0007";
    private static final String PUBLISHNUMBER_OPERATINGRULE="发布文号\u0007";
    private static final String PUBLISHDATE_OPERATINGRULE="发布日期\u0007";
    private static final String PUBLISHSCOPE_OPERATINGRULE="发布范围\u0007";
    private static final String VALIDDATE_OPERATINGRULE="生效日期\u0007";

    private static final String COMPANYNAME_MAP="companyName";
    private static final String APPROVER_MAP="approver";
    private static final String APPROVALBASIS_MAP="approvalBasis";
    private static final String PUBLISHNUMBER_MAP="publishNumber";
    private static final String PUBLISHDATE_MAP="publishDate";
    private static final String PUBLISHSCOPE_MAP="publishScope";
    private static final String VALIDDATE_MAP="validDate";
    private static final String TARGET_MAP="target";
    private static final String APPLYSCOPE_MAP="applyScope";
    private static final String ESTABLISHBASIS_MAP="establishBasis";
    private static final String RISK_MAP="risk";
    private static final String EXPLANATION_MAP="explanation";

    private static final String TARGET="1  目的";
    private static final String APPLYSCOPE="2  适用范围";
    private static final String ESTABLISHBASIS="3  编制依据";
    private static final String RISK="4  主要应对的风险及应对目标";
    private static final String EXPLANATION="5  释义";
    private static final String DUTY="6  职责分工";

    private static final String RISK_OPERATINGRULE="4 主要应对的风险及应对目标";
    private static final String EXPLANATION_OPERATINGRULE="5 释义";
    private static final String DUTY_OPERATINGRULE="6 职责分工";

    /**
     * 方法名：importWord
     * 功能：导入
     * 描述：
     * 创建人：Michael
     * 创建时间：2020/09/03 11:45
     * 修改人：
     * 修改描述：
     * 修改时间：
     * type: 基本制度、管理办法、操作细则
     */
    public static  SystemDocument importWord(InputStream fis,String type) throws IOException {
        SystemDocument systemDocument=new SystemDocument();


        try {



                WordExtractor wordExtractor= new WordExtractor(fis);//使用HWPF组件中WordExtractor类从Word文档中提取文本或段落
                int i=1;
                String target="";//目的
                String applyScope="";//适用范围
                String establishBasis="";//编制依据
                String risk="";//应对风险及应对目标
                String explanation="";//释义
                Boolean targetFlag=false;//是否获取目的
                Boolean applyScopeFlag=false;//是否获取适用范围
                Boolean establishBasisFlag=false;//是否获取编制依据
                Boolean risklag=false;//是否获取应对风险及应对目标
                Boolean explanationFlag=false;//是否获取释义

                Boolean companyNameFlag=false;//是否获取公司名称
                Boolean approverFlag=false;//是否获取批准人
                Boolean approvalBasisFlag=false;//是否获取批准依据
                Boolean publishScopeFlag=false;//是否获取发布范围
                Boolean publishNumberFlag=false;//是否获取发布文号
                Boolean publishDateFlag=false;//是否获取发布日期
                Boolean validDateFlag=false;//是否获取生效日期

                for(String words : wordExtractor.getParagraphText()){//获取段落内容
                    words=words.replaceAll("\r|\n","");
                    //System.out.println(words);

                    if(StringUtils.isNotEmpty(words)){
                        if(InstLevelEnum.getItem("baseInst").getCode().equals(type)||InstLevelEnum.getItem("managementMethod").getCode().equals(type)){//基本制度或管理办法
                            //获取公司名称
                            if(words.startsWith(COMPANYNAME)){
                                systemDocument.setCompanyName(words.replace(COMPANYNAME,""));
                                continue;
                            }
                            //获取批准人
                            if(words.startsWith(APPROVER)){
                                systemDocument.setApprover(words.replace(APPROVER,""));
                                continue;
                            }
                            //获取批准依据
                            if(words.startsWith(APPROVALBASIS)){
                                systemDocument.setAccording(words.replace(APPROVALBASIS,""));
                                continue;
                            }
                            //获取发布文号
                            if(words.startsWith(PUBLISHNUMBER)){
                                systemDocument.setPublishSymbol(words.replace(PUBLISHNUMBER,""));
                                continue;
                            }
                            //获取发布日期
                            if(words.startsWith(PUBLISHDATE)){
                                systemDocument.setPublishDateStr(words.replace(PUBLISHDATE,""));
                                continue;
                            }
                            //获取生效日期
                            if(words.startsWith(VALIDDATE)){
                                systemDocument.setEffectiveDateStr(words.replace(VALIDDATE,""));
                                continue;
                            }
                            //获取发布范围
                            if(words.startsWith(PUBLISHSCOPE)){
                                systemDocument.setPublishRange(words.replace(PUBLISHSCOPE,""));
                                continue;
                            }
                        }else if(InstLevelEnum.getItem("operatingRule").getCode().equals(type)){//操作细则
                            if("\u0007".equals(words)){
                                continue;
                            }
                            //获取公司名称
                            if(words.equals(COMPANYNAME_OPERATINGRULE)){
                                companyNameFlag=true;
                                continue;
                            }
                            //获取批准人
                            if(words.equals(APPROVER_OPERATINGRULE)){
                                companyNameFlag=false;
                                approverFlag=true;
                                continue;
                            }
                            //获取批准依据
                            if(words.equals(APPROVALBASIS_OPERATINGRULE)){
                                approverFlag=false;
                                approvalBasisFlag=true;
                                continue;
                            }
                            //获取发布范围
                            if(words.equals(PUBLISHSCOPE_OPERATINGRULE)){
                                approvalBasisFlag=false;
                                publishScopeFlag=true;

                                continue;
                            }
                            //获取发布文号
                            if(words.equals(PUBLISHNUMBER_OPERATINGRULE)){
                                publishScopeFlag=false;
                                publishNumberFlag=true;
                                continue;
                            }
                            //获取发布日期
                            if(words.equals(PUBLISHDATE_OPERATINGRULE)){
                                publishNumberFlag=false;
                                publishDateFlag=true;
                                continue;
                            }
                            //获取生效日期
                            if(words.equals(VALIDDATE_OPERATINGRULE)){
                                publishDateFlag=false;
                                validDateFlag=true;

                                continue;
                            }
                            if(companyNameFlag){
                                systemDocument.setCompanyName(words.replace("\u0007",""));
                                continue;
                            }
                            if(approverFlag){
                                systemDocument.setAccording(words.replace("\u0007",""));

                                continue;
                            }
                            if(approvalBasisFlag){
                                systemDocument.setApprover(words.replace("\u0007",""));
                                continue;
                            }
                            if(publishScopeFlag){
                                systemDocument.setPublishRange(words.replace("\u0007",""));
                                continue;
                            }
                            if(publishNumberFlag){
                                systemDocument.setPublishSymbol(words.replace("\u0007",""));
                                continue;
                            }
                            if(publishDateFlag){
                                systemDocument.setPublishDateStr(words.replace("\u0007",""));
                                continue;
                            }
                            if(validDateFlag){
                                systemDocument.setEffectiveDateStr(words.replace("\u0007",""));
                                validDateFlag=false;
                                continue;
                            }
                        }
                        //获取目的
                        if(words.startsWith(TARGET)){
                            targetFlag=true;
                            continue;
                        }


                        //获取适用范围
                        if(words.startsWith(APPLYSCOPE)){
                            targetFlag=false;
                            applyScopeFlag=true;
                            continue;
                        }

                        //获取编制依据
                        if(words.startsWith(ESTABLISHBASIS)){
                            applyScopeFlag=false;
                            establishBasisFlag=true;
                            continue;
                        }


                        //获取应对风险和应对目标
                        if(words.startsWith(RISK)||words.startsWith(RISK_OPERATINGRULE)){
                            establishBasisFlag=false;
                            risklag=true;
                            continue;
                        }


                        //获取释义
                        if(words.startsWith(EXPLANATION)||words.startsWith(EXPLANATION_OPERATINGRULE)){
                            risklag=false;
                            explanationFlag=true;
                            continue;
                        }

                        //获取职责
                        if(words.startsWith(DUTY)||words.startsWith(DUTY_OPERATINGRULE)){
                            break;
                        }
                        if(targetFlag){
                            target+=words;
                            continue;
                        }
                        if(applyScopeFlag){
                            applyScope+=words;
                            continue;
                        }
                        if(establishBasisFlag){
                            establishBasis+=words;
                            continue;
                        }
                        if(risklag){
                            risk+=words;
                            continue;
                        }
                        if(explanationFlag){
                            explanation+=words;
                            continue;
                        }

                    }else{
                        continue;
                    }


                    i++;
                }
                systemDocument.setDocPurpose(target);
                systemDocument.setDocApplyRange(applyScope);
                systemDocument.setDocAccording(establishBasis);
                //systemDocument.setDocReplyRisk(risk);
                systemDocument.setDocExplain(explanation);



        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            fis.close();
        }
        return  systemDocument;


    }
    /**
     * 方法名：writeWord
     * 功能：写入word
     * 描述：
     * 创建人：Michael
     * 创建时间：2020/09/08 11:45
     * 修改人：
     * 修改描述：
     * 修改时间：
     *
     */
    public static void writeWord(String templateFolder,String templateFileName, String newFile,SystemDocument systemDocument) throws IOException, TemplateException {
        Map<String,String> map=new HashedMap();

        Configuration  configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        try {
            //根据位置加载模板
            configuration.setDirectoryForTemplateLoading(new File(templateFolder));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //根据文件名在模板文件夹路径中找到模板
        Template freemarkerTemplate = configuration.getTemplate(templateFileName);

        InputStream fin = null;
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(newFile);
            // 这个地方不能使用FileWriter因为需要指定编码类型否则生成的Word文档会因为有无法识别的编码而无法打开
            Writer w = new OutputStreamWriter(fos, "utf-8");
            map=createMap(map,systemDocument);
            freemarkerTemplate.process(map, w);
            w.close();

        } finally {
            if(fin != null) fin.close();
            if(fos != null) fos.close();
        }
        System.out.println("写入成功！！");
    }

    /**
     * 构建map,替换word模版中的占位符
     * @param map
     * @param systemDocument
     * @return
     */
    public static Map<String,String> createMap(Map<String,String> map,SystemDocument systemDocument){
        map.put(COMPANYNAME_MAP,systemDocument.getCompanyName());//公司名称
        map.put(APPROVER_MAP,systemDocument.getApprover());//批准人
        map.put(APPROVALBASIS_MAP,systemDocument.getAccording());//批准依据
        map.put(PUBLISHSCOPE_MAP,systemDocument.getPublishRange());//发布范围
        /*map.put(PUBLISHDATE_MAP,systemDocument.getPublishDateStr());//发布日期
        map.put(PUBLISHNUMBER_MAP,systemDocument.getPublishSymbol());//发布文号
        map.put(VALIDDATE_MAP,systemDocument.getEffectiveDateStr());//生效日期*/
        map.put(TARGET_MAP,systemDocument.getDocPurpose());//目的
        map.put(APPLYSCOPE_MAP,systemDocument.getDocApplyRange());//适用范围
        map.put(ESTABLISHBASIS_MAP,systemDocument.getDocAccording());//编制依据
        //map.put(RISK_MAP,systemDocument.getDocReplyRisk());//主要应对风险和应对目标
        map.put(EXPLANATION_MAP,systemDocument.getDocExplain());//释义
        return map;
    }


    public static  void main(String[] args){
        /*try {
            InputStream inputStream=new FileInputStream("D:\\zonysoft\\中海油有限制度管理系统\\02.需求分析\\6月份现场需求确认过程\\最新模板\\写入测试.doc");
            writeWord(inputStream,null,null);
        } catch (IOException e) {
            e.printStackTrace();
        }*/
       /* SystemDocument systemDocument=new SystemDocument();
        systemDocument.setCompanyName("公司名称test");
        systemDocument.setApprover("批准人test");
        systemDocument.setAccording("批准依据test");
        systemDocument.setPublishSymbol("发布文号test");
        systemDocument.setPublishRange("发布范围test");
        systemDocument.setPublishDateStr("发布日期test");
        systemDocument.setEffectiveDateStr("生效日期test");
        systemDocument.setDocPurpose("目的test");
        systemDocument.setDocApplyRange("适用范围test");
        systemDocument.setDocAccording("编制依据test");
        systemDocument.setDocReplyRisk("应对风险和应对目标test");
        systemDocument.setDocExplain("释义test");
        try {
            writeWord("D:\\zonysoft\\中海油有限制度管理系统\\02.需求分析\\6月份现场需求确认过程\\最新模板","写入测试.xml","D:\\write.doc",systemDocument);
        } catch (IOException | TemplateException e) {
            e.printStackTrace();
        }*/
        String path= null;
        try {
            path = ResourceUtils.getURL("classpath:").getPath();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println(path);
    }
}
