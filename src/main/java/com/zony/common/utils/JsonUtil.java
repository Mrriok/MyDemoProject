package com.zony.common.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class JsonUtil {
    private static JsonUtil ju;
    private static JsonFactory jf;
    private static ObjectMapper mapper;

    private JsonUtil() {
    }

    public static JsonUtil getInstance() {
        if (ju == null) {
            ju = new JsonUtil();
        }
        return ju;
    }

    public static ObjectMapper getMapper() {
        if (mapper == null) {
            mapper = new ObjectMapper();
            //不校验未知属性com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException: Unrecognized field "modelName" (class tech.wetech.ecm.web.dto.system.GeneratorDto), not marked as ignorable (31 known properties: "jspTargetFolder", "controllerName", "generateKeys", "controllerPackage", "domainObjectName", "daoTargetFolder", "daoPackage", "moduleName", "controllerTargetFolder", "modelPackageTargetFolder", "modelPackage", "comment", "serviceImplName", "projectFolder", "mappingXMLTargetFolder", "ignoredColumns", "mapperName", "annotation", "columnOverrides", "jspName", "serviceImplTargetFolder", "tableName", "useTableNameAlias", "serviceImplPackage", "mappingXMLPackage", "serviceName", "offsetLimit", "serviceTargetFolder", "needToStringHashcodeEquals", "useActualColumnNames", "servicePackage"])
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return mapper;
    }

    public static JsonFactory getFactory() {
        if (jf == null) {
            jf = new JsonFactory();
        }
        return jf;
    }

    public String obj2json(Object obj) {
        JsonGenerator jg = null;
        try {
            jf = getFactory();
            mapper = getMapper();
            StringWriter out = new StringWriter();
            jg = jf.createGenerator(out);
            mapper.writeValue(jg, obj);
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (jg != null) {
                    jg.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Object json2obj(String json, Class<?> clz) {
        try {
            mapper = getMapper();
            return mapper.readValue(json, clz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public final <T> List<T> json2list(String jsonStr, Class<T> tC) {
        try {
            List<T> tList = new ArrayList<>();
            List<LinkedHashMap<String, Object>> linkedHashMapList = (List<LinkedHashMap<String, Object>>) this.json2obj(jsonStr, List.class);
            for (LinkedHashMap<String, Object> map : linkedHashMapList) {
                T tObj = tC.newInstance();
                for (Map.Entry<String, Object> mapEntry : map.entrySet()) {
                    BeanUtils.setProperty(tObj, mapEntry.getKey(), mapEntry.getValue());
                }
                tList.add(tObj);
            }
            return tList;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Map<String, Object>> transToMapList(List objList) {
        return (List<Map<String, Object>>) JsonUtil.getInstance().json2obj(JsonUtil.getInstance().obj2json(objList), List.class);
    }

    public Map<String, Object> transToMap(Object obj) {
        return (Map<String, Object>) JsonUtil.getInstance().json2obj(JsonUtil.getInstance().obj2json(obj), Map.class);
    }

    public static void main(String[] args) {
        //List<List<String>> retList = JsonUtil.getInstance().geStringsList("[[\"file_common.com_period\",\"dic_period.item_code\"],[\"dic_period.type_code\",\"'period'\"]]");
        //System.out.println(retList);
//        List dataList = new ArrayList();
//        dataList.add(new PrintArchiveRangeModel("A", "党群工作类", "01", "党务工作", "党委综合工作、党员代表大会或党委其他有关会议、党委办公室其它事务性工作等。"));
//        System.out.println();
//        List list = (List) JsonUtil.getInstance().json2obj(JsonUtil.getInstance().obj2json(dataList), List.class);
//        System.out.println(list.size());
    }
}
