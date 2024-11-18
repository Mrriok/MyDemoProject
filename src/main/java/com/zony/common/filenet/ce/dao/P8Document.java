/*
 * @copyright(disclaimer)
 *
 * Licensed Materials - Property of IBM
 * (C) Copyright IBM Corp. 2010  All Rights Reserved.
 *
 * The source code for this program is not published or otherwise
 * divested of its trade secrets, irrespective of what has been
 * deposited with the U.S. Copyright Office.
 *
 * @endCopyright
 */
package com.zony.common.filenet.ce.dao;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.collection.*;
import com.filenet.api.constants.*;
import com.filenet.api.core.*;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.zony.common.filenet.vo.AttachmentVo;
import com.zony.common.filenet.vo.Pagination;
import com.zony.common.filenet.vo.FnAttachIsVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

/**
 * @version $Rev: 998 $ $Date: 2012-01-09 15:00:10 +0800 (星期一, 09 一月 2012) $
 */
public class P8Document {
    private static final Logger logger = LoggerFactory.getLogger(P8Document.class);
    public static final String SEPARATOR = ",";

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#createCustomObject(com.filenet
     * .api.core.ObjectStore, java.lang.String, java.util.Map)
     */

    public static boolean cancelCheckOutDocument(ObjectStore os, String docId) {
        boolean flag = false;

        try {
            Document doc = fetchDocumentById(os, docId);
            Document reservation = (Document) doc.get_Reservation();
            doc.cancelCheckout();
            reservation.save(RefreshMode.REFRESH);
            flag = true;
        } catch (EngineRuntimeException ex) {
            logger.error("Error occurred when cancelling document's check out status.", ex);
        }

        return flag;
    }

    public static void changeDocumentClass(ObjectStore os, String docId, String className) {
        Document doc = fetchDocumentById(os, docId);
        changeDocumentClass(doc, className);
    }

    public static void changeDocumentClass(Document doc, String className) {
        doc.changeClass(className);
        doc.save(RefreshMode.REFRESH);
    }

    // public static boolean checkInDocument(ObjectStore os, Document doc,
    // Map<String, Object> propMap, ContentElementList contentElementList) {
    // return checkInDocument(os, doc, propMap, contentElementList,
    // CheckinType.MAJOR_VERSION);
    // }

    public static boolean checkInDocument(ObjectStore os, Document doc, Map<String, Object> propMap, ContentElementList contentElementList, CheckinType checkinType) {
        boolean flag = false;
        try {
            checkInDocument(null, os, doc, propMap, contentElementList, checkinType);
            flag = true;
        } catch (EngineRuntimeException ex) {
            logger.error("Error occurred when checking in the document '" + doc.get_Id() + "'.", ex);
        }
        return flag;
    }

    public static boolean checkInDocumentAsMajor(ObjectStore os, Document doc, Map<String, Object> propMap, ContentElementList contentElementList) {
        return checkInDocument(os, doc, propMap, contentElementList, CheckinType.MAJOR_VERSION);

    }

    // public static void checkInDocument(UpdatingBatch ub, ObjectStore os,
    // Document doc, Map<String, Object> propMap, ContentElementList
    // contentElementList) {
    // checkInDocument(ub, os, doc, propMap, contentElementList,
    // CheckinType.MAJOR_VERSION);
    // }

    private static void checkInDocument(UpdatingBatch ub, ObjectStore os, Document doc, Map<String, Object> propMap, ContentElementList contentElementList, CheckinType checkinType) {
        try {
            Document released;
            try {
                released = (Document) doc.get_Reservation();
                // /*
                // * @author ozz 没有权限时无法获取附件内容
                // */
                // if (released == null) {
                // throw new RuntimeException("没有文档'" + doc.get_Name() + "'(" +
                // doc.get_Id() + ")附件的权限");
                // }
            } catch (Exception e) {
                released = doc;
            }

            Properties props = released.getProperties();
            updateAttributes(props, propMap);
            setContentElementList(released, contentElementList);
            released.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, checkinType);
            if (ub == null) {
                released.save(RefreshMode.REFRESH);
            } else {
                ub.add(released, null);
            }
        } catch (EngineRuntimeException ex) {
            logger.error("Error occurred when checking in the document '" + doc.get_Id() + "'.", ex);
        }
    }

    public static void checkInDocumentAsMajor(UpdatingBatch ub, ObjectStore os, Document doc, Map<String, Object> propMap, ContentElementList contentElementList) {
        checkInDocument(ub, os, doc, propMap, contentElementList, CheckinType.MAJOR_VERSION);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#createDocument(com.filenet.api
     * .core.ObjectStore, java.lang.String, java.lang.String, java.util.Map)
     */

    private static boolean checkInDocument(ObjectStore os, String docId, Map<String, Object> propMap, ContentElementList contentElementList, CheckinType checkinType) {
        boolean flag = false;
        try {
            Document doc = fetchDocumentById(os, docId);
            flag = checkInDocument(os, doc, propMap, contentElementList, checkinType);
        } catch (EngineRuntimeException ex) {
            logger.error("Error occurred when checking in the document '" + docId + "'.", ex);
        }

        return flag;
    }

    public static boolean checkInDocumentAsMajor(ObjectStore os, String docId, Map<String, Object> propMap, ContentElementList contentElementList) {
        return checkInDocument(os, docId, propMap, contentElementList, CheckinType.MAJOR_VERSION);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#createDocument(com.filenet.api
     * .core.ObjectStore, java.lang.String, java.lang.String, java.util.Map,
     * com.filenet.api.core.Folder)
     */

    public static boolean checkOutDocument(ObjectStore os, Document doc) {
        boolean flag = false;

        try {
            // Document doc = fetchDocumentById(os, docId);
            doc.checkout(ReservationType.OBJECT_STORE_DEFAULT, null, null, null);
            doc.save(RefreshMode.REFRESH);
            flag = true;
        } catch (EngineRuntimeException ex) {
            logger.error("Error occurred when checking out the document '" + doc.get_Id() + "'.", ex);
        }

        return flag;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#createDocument(com.filenet.api
     * .core.ObjectStore, java.lang.String, java.lang.String, java.util.Map,
     * java.io.InputStream)
     */

    public static boolean checkOutDocument(ObjectStore os, String docId) {
        boolean flag = false;
        try {
            Document doc = fetchDocumentById(os, docId);
            flag = checkOutDocument(os, doc);
        } catch (EngineRuntimeException ex) {
            logger.error("Error occurred when checking out the document '" + docId + "'.", ex);
        }
        return flag;
    }

    @SuppressWarnings("unchecked")
    public static void checkInDocument(ObjectStore os, String docId, Map<String, InputStream> content) {
        Document doc = Factory.Document.fetchInstance(os, new Id(docId), null);
        try {
            Document released;
            try {
                released = (Document) doc.get_Reservation();
            } catch (Exception e) {
                released = doc;
            }

            if (content != null && !content.isEmpty()) {
                ContentElementList contentElementList = Factory.ContentElement.createList();
                Iterator<String> it = content.keySet().iterator();
                while (it.hasNext()) {
                    String name = it.next();
                    InputStream ins = content.get(name);
                    ContentTransfer transfer = Factory.ContentTransfer.createInstance();
                    transfer.setCaptureSource(ins);
                    transfer.set_RetrievalName(name);
                    contentElementList.add(transfer);
                }
                released.set_ContentElements(contentElementList);
            }
            released.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
            released.save(RefreshMode.REFRESH);
        } catch (EngineRuntimeException ex) {
            logger.error("Error occurred when checking in the document '" + doc.get_Id() + "'.", ex);
        }
    }


    @SuppressWarnings("unchecked")
    /**
     *
     * @Title: checkInDocument
     * @Description: TODO(这里用一句话描述这个方法的作用)
     * @Version 1.0
     * @Date 2016-5-25新增
     * @Author zhangwenjun
     * @param os
     * @param doc
     * @param content
     */
    public static void checkInDocument(ObjectStore os, Document doc, Map<String, InputStream> content) {
//		Document doc = Factory.Document.fetchInstance(os, new Id(docId), null);
        try {
            Document released;
            try {
                released = (Document) doc.get_Reservation();
            } catch (Exception e) {
                released = doc;
            }

            if (content != null && !content.isEmpty()) {
                ContentElementList contentElementList = Factory.ContentElement.createList();
                Iterator<String> it = content.keySet().iterator();
                while (it.hasNext()) {
                    String name = it.next();
                    InputStream ins = content.get(name);
                    ContentTransfer transfer = Factory.ContentTransfer.createInstance();
                    transfer.setCaptureSource(ins);
                    transfer.set_RetrievalName(name);
                    contentElementList.add(transfer);
                }
                released.set_ContentElements(contentElementList);
            }
            released.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
            released.save(RefreshMode.REFRESH);
        } catch (EngineRuntimeException ex) {
            logger.error("Error occurred when checking in the document '" + doc.get_Id() + "'.", ex);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#createDocument(com.filenet.api
     * .core.ObjectStore, java.lang.String, java.lang.String, java.util.Map,
     * java.util.Map)
     */

    public static ComponentRelationship createComponentRelationship(ObjectStore os, Document parentDoc, Document childDoc) {
        if (parentDoc.get_CompoundDocumentState().getValue() != CompoundDocumentState.COMPOUND_DOCUMENT_AS_INT)
            throw new RuntimeException(parentDoc.get_Id() + "不是复合文档");
        ComponentRelationship cr = Factory.ComponentRelationship.createInstance(os, null);
        cr.set_ParentComponent(parentDoc);
        cr.set_ChildComponent(childDoc);
        cr.set_ComponentCascadeDelete(com.filenet.api.constants.ComponentCascadeDeleteAction.CASCADE_DELETE);
        return cr;

    }

    /**
     * @param os          CE存储库对象
     * @param compoundDoc 复合文档对象
     * @param childDoc    子文档对象
     * @return 添加完成后的复合文档对象
     * @title: addChildDocumentToCompoundDocument
     * @date 2014年11月17日
     * @author Jeffrey
     * @description: 添加子文档到复合文档中
     */
    public static Document addChildDocumentToCompoundDocument(ObjectStore os, Document compoundDoc, Document childDoc) {
        if (compoundDoc.get_CompoundDocumentState().equals(CompoundDocumentState.COMPOUND_DOCUMENT)) {
            Iterator crIter = compoundDoc.get_ChildRelationships().iterator();
            ComponentRelationship cr = Factory.ComponentRelationship.createInstance(os, null);
            cr.set_ParentComponent(compoundDoc);
            cr.set_ChildComponent(childDoc);
            if (crIter.hasNext()) {
                int count = 1;
                while (crIter.hasNext()) {
                    crIter.next();
                    count++;
                }
                cr.set_ComponentSortOrder(new Integer(count));
            } else {
                cr.set_ComponentSortOrder(new Integer(1));
            }
            cr.set_ComponentRelationshipType(ComponentRelationshipType.DYNAMIC_CR);
            cr.set_VersionBindType(VersionBindType.LATEST_MAJOR_VERSION);
            cr.save(RefreshMode.REFRESH);
        } else {
            throw new RuntimeException(compoundDoc.get_Id() + "不是复合文档");
        }
        return compoundDoc;
    }

    /**
     * @param os          CE存储对象
     * @param compoundDoc 复合文档对象
     * @param docList     子文档对象列表
     * @return 添加完子文档的复合我文档
     * @title: addChildDocumentToCompoundDocument
     * @date 2014年11月17日
     * @author Jeffrey
     * @description: 向复合文档中添加子文档对象列表
     */
    public static Document addChildDocumentToCompoundDocument(ObjectStore os, Document compoundDoc, List<Document> docList) {
        if (compoundDoc.get_CompoundDocumentState().equals(CompoundDocumentState.COMPOUND_DOCUMENT)) {
            if (docList != null && docList.size() > 0) {
                int num = 1;
                ComponentRelationship cr = null;
                for (Document childDoc : docList) {
                    cr = Factory.ComponentRelationship.createInstance(os, null);
                    cr.set_ParentComponent(compoundDoc);
                    cr.set_ChildComponent(childDoc);
                    cr.set_ComponentCascadeDelete(com.filenet.api.constants.ComponentCascadeDeleteAction.CASCADE_DELETE);
                    cr.set_ComponentSortOrder(new Integer(num));
                    cr.set_ComponentRelationshipType(ComponentRelationshipType.DYNAMIC_CR);
                    cr.set_VersionBindType(VersionBindType.LATEST_MAJOR_VERSION);
                    cr.save(RefreshMode.REFRESH);
                    num++;
                }
            }
        } else {
            throw new RuntimeException(compoundDoc.get_Id() + "不是复合文档");
        }
        return compoundDoc;
    }

    /**
     * @param os          CE存储库对象
     * @param compoundDoc 复合文档对象
     * @param orderIndex  子文档在复合文档中的排序号
     * @return 删除子文档后的复合文档
     * @title: delChildDocumentFromCompoundDocument
     * @date 2014年11月17日
     * @author Jeffrey
     * @description: 删除复合文档中的指定排序号的子文档对象
     */
    public static Document delChildDocumentFromCompoundDocument(ObjectStore os, Document compoundDoc, Integer orderIndex) {
        if (compoundDoc.get_CompoundDocumentState().equals(CompoundDocumentState.COMPOUND_DOCUMENT)) {
            Iterator crIter = compoundDoc.get_ChildRelationships().iterator();
            int index = 0;
            while (crIter.hasNext() == true) {
                ComponentRelationship cr = (ComponentRelationship) crIter.next();
                index++;
                if (index == orderIndex) {
                    cr.delete();
                    cr.save(RefreshMode.REFRESH);
                    break;
                }
            }
            compoundDoc.refresh();
            compoundDoc.save(RefreshMode.REFRESH);
        } else {
            throw new RuntimeException(compoundDoc.get_Id() + "不是复合文档");
        }
        return compoundDoc;
    }

    /**
     * @param os          CE存储库
     * @param compoundDoc 复合文档对象
     * @return 删除子文档后的复合文档对象
     * @title: delAllChildDocumentOfCompoundDocument
     * @date 2014年11月17日
     * @author Jeffrey
     * @description: 删除复合文档中的所有的子文档对象
     */
    public static Document delAllChildDocumentOfCompoundDocument(ObjectStore os, Document compoundDoc) {
        if (compoundDoc.get_CompoundDocumentState().equals(CompoundDocumentState.COMPOUND_DOCUMENT)) {
            Iterator crIter = compoundDoc.get_ChildRelationships().iterator();
            while (crIter.hasNext() == true) {
                ComponentRelationship cr = (ComponentRelationship) crIter.next();
                cr.delete();
                cr.save(RefreshMode.REFRESH);
            }
            compoundDoc.refresh();
            compoundDoc.save(RefreshMode.REFRESH);
        } else {
            throw new RuntimeException(compoundDoc.get_Id() + "不是复合文档");
        }
        return compoundDoc;
    }

    public static Document createDocument(ObjectStore os, String documentTitle) {
        return createDocument(os, "Document", documentTitle, null, false);
    }

    public static Document createDocument(ObjectStore os, String symbolicName, String documentTitle, Map<String, Object> propMap) {
        return createDocument(os, symbolicName, documentTitle, propMap, false);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#fetchDocumentById(com.filenet
     * .api.core.ObjectStore, java.lang.String,
     * com.filenet.api.property.PropertyFilter)
     */

    public static Document createDocument(ObjectStore os, String symbolicName,
                                          String documentTitle, Map<String, Object> propMap, boolean isCompoundDocument) {
        Document doc = Factory.Document.createInstance(os, symbolicName);

        Properties props = doc.getProperties();

        if (propMap != null) {
            String techProcDocId = "techProcDocId";
            String dcoTitle = "DocumentTitle";
            props.putValue(dcoTitle, documentTitle);
            if (propMap.get(techProcDocId) != null) {
                props.putObjectValue(techProcDocId, new Id(propMap.get(techProcDocId).toString()));
                propMap.remove(techProcDocId);
            }
            if (propMap.get(dcoTitle) != null) {
                propMap.remove(dcoTitle);
            }
            updateAttributes(props, propMap);
        } else {
            String dcoTitle = "DocumentTitle";
            props.putValue(dcoTitle, documentTitle);
        }
        if (isCompoundDocument) {
            doc.set_CompoundDocumentState(CompoundDocumentState.COMPOUND_DOCUMENT);
        }
        doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
        doc.save(RefreshMode.REFRESH);

        return doc;
    }

    /**
     * @param props
     * @param propMap
     */
    public static void updateAttributes(Properties props, Map<String, Object> propMap) {
        if (propMap != null && !propMap.isEmpty()) {
            Iterator<String> it = propMap.keySet().iterator();
            while (it.hasNext()) {
                String key = it.next();
                Object value = propMap.get(key);

                /*
                 * 取消无效属性赋值
                 */
                if (!props.isPropertyPresent(key) && value == null) {
                    continue;
                }

                try {
                    props.putObjectValue(key, value);
                } catch (Exception e) {
                    logger.error("", e);
                }
            }
        }
    }

    public static Document createDocument(ObjectStore os, String symbolicName, String documentTitle,
                                          Map<String, Object> propMap, ContentElementList contentElementList) {
        return createDocument(os, symbolicName, documentTitle, propMap, false, contentElementList);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#fetchDocumentsByIdList(com.filenet
     * .api.core.ObjectStore, java.lang.String[])
     */

    public static Document createDocument(ObjectStore os, String symbolicName, String documentTitle,
                                          Map<String, Object> propMap, boolean isCompoundDocument, ContentElementList contentElementList) {
        Document doc = Factory.Document.createInstance(os, symbolicName);

        if (propMap != null) {
            Properties props = doc.getProperties();
            String docTitle = "documentTitle";
            props.putValue(docTitle, documentTitle);
            if (propMap.get(docTitle) != null) {
                propMap.remove(docTitle);
            }
            updateAttributes(props, propMap);
        }

        if (contentElementList != null) {
            setContentElementList(doc, contentElementList);
            // doc.save(RefreshMode.NO_REFRESH);
        }
        if (isCompoundDocument) {
            doc.set_CompoundDocumentState(CompoundDocumentState.COMPOUND_DOCUMENT);
        }
        doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
        doc.save(RefreshMode.REFRESH);

        return doc;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#fetchDocumentByPath(com.filenet
     * .api.core.ObjectStore, java.lang.String)
     */

    public static Document createDocument(ObjectStore os, String symbolicName,
                                          String documentTitle, Map<String, Object> propMap, Folder folder) {
        Document doc = createDocument(os, symbolicName, documentTitle, propMap);

        ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE, null, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
        rcr.set_ContainmentName(documentTitle);
        rcr.save(RefreshMode.NO_REFRESH);

        return doc;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#fetchDocumentByPath(com.filenet
     * .api.core.ObjectStore, java.lang.String,
     * com.filenet.api.property.PropertyFilter)
     */

    @SuppressWarnings("unchecked")
    public static Document createDocument(ObjectStore os, String symbolicName, String documentTitle,
                                          Map<String, Object> propMap, InputStream ins) {
        Document doc = Factory.Document.createInstance(os, symbolicName);
        String docTitle = "DocumentTitle";
        Properties props = doc.getProperties();
        props.putValue(docTitle, documentTitle);
        if (propMap != null) {
            if (propMap.get(docTitle) != null) {
                propMap.remove(docTitle);
            }
            updateAttributes(props, propMap);
        }
        doc.save(RefreshMode.NO_REFRESH);
        if (ins != null) {
            ContentElementList contentElementList = Factory.ContentElement.createList();
            ContentTransfer transfer = Factory.ContentTransfer.createInstance();

            transfer.setCaptureSource(ins);
            transfer.set_RetrievalName(documentTitle);

            contentElementList.add(transfer);

            setContentElementList(doc, contentElementList);
            doc.save(RefreshMode.NO_REFRESH);
        }

        doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
        doc.save(RefreshMode.REFRESH);

        return doc;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#fetchCurrentVersionDocument(
     * com.filenet.api.core.ObjectStore, java.lang.String)
     */

    @SuppressWarnings("unchecked")
    public static Document createDocument(ObjectStore os, String symbolicName,
                                          String documentTitle, Map<String, Object> propMap, Map<String, InputStream> content) {
        Document doc = Factory.Document.createInstance(os, symbolicName);

        if (propMap != null) {
            Properties props = doc.getProperties();
            String docTitle = "DocumentTitle";
            props.putValue(docTitle, documentTitle);
            if (propMap.get(docTitle) != null) {
                propMap.remove(docTitle);
            }
            updateAttributes(props, propMap);
        }

        doc.save(RefreshMode.NO_REFRESH);

        if (content != null && !content.isEmpty()) {
            ContentElementList contentElementList = Factory.ContentElement.createList();
            Iterator<String> it = content.keySet().iterator();
            while (it.hasNext()) {
                String name = it.next();
                InputStream ins = content.get(name);
                ContentTransfer transfer = Factory.ContentTransfer.createInstance();
                transfer.setCaptureSource(ins);
                transfer.set_RetrievalName(name);
                contentElementList.add(transfer);
            }
            setContentElementList(doc, contentElementList);
            doc.save(RefreshMode.NO_REFRESH);
        }

        doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
        doc.save(RefreshMode.REFRESH);

        return doc;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#fetchReleasedDocument(com.filenet
     * .api.core.ObjectStore, java.lang.String)
     */

    public static void deleteDocument(Document document) {
        document.delete();
        document.save(RefreshMode.NO_REFRESH);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#fetchReservedDocument(com.filenet
     * .api.core.ObjectStore, java.lang.String)
     */

    /**
     * @param os
     * @param docId
     * @Title: deleteDocumentById
     * @Description: 根据ID删除文档
     * @Version 1.0
     * @Date 2017-3-21
     * @Author michael
     */
    public static void deleteDocumentById(ObjectStore os, String docId) {
        Document doc = fetchDocumentById(os, docId);
        deleteDocument(doc);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#checkInDocument(com.filenet.
     * api.core.ObjectStore, java.lang.String, java.util.Map,
     * com.filenet.api.collection.ContentElementList)
     */

    /**
     * @param os
     * @param docPath
     * @Title: deleteDocumentByPath
     * @Description: 根据路径删除文档
     * @Version 1.0
     * @Date 2017-3-21
     * @Author michael
     */
    public static void deleteDocumentByPath(ObjectStore os, String docPath) {
        Document doc = fetchDocumentByPath(os, docPath);
        deleteDocument(doc);
    }

    public static void deleteDocumentWithAllVersions(ObjectStore os, String docId) {
        Document doc = P8Document.fetchDocumentById(os, docId);
        deleteDocumentWithAllVersions(doc);
    }

    public static void deleteDocumentWithAllVersions(Document doc) {
        VersionSeries verSeries = doc.get_VersionSeries();
        verSeries.delete();
        verSeries.save(RefreshMode.REFRESH);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#checkInDocument(com.filenet.
     * api.core.ObjectStore, com.filenet.api.core.Document, java.util.Map,
     * com.filenet.api.collection.ContentElementList)
     */

    public static Document fetchCurrentVersionDocument(ObjectStore os, String docId) {
        Versionable versionable = fetchDocumentById(os, docId).get_CurrentVersion();
        return versionable == null ? null : (Document) versionable;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#checkOutDocument(com.filenet
     * .api.core.ObjectStore, java.lang.String)
     */

    /**
     * @param os
     * @param docId
     * @return
     * @Title: fetchDocumentById
     * @Description: 根据ID获取文档信息
     * @Version 1.0
     * @Date 2017-3-21
     * @Author michael
     */
    public static Document fetchDocumentById(ObjectStore os, String docId) {
        return fetchDocumentById(os, docId, null);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#checkOutDocument(com.filenet
     * .api.core.ObjectStore, com.filenet.api.core.Document)
     */

    public static Document fetchDocumentById(ObjectStore os, String docId, PropertyFilter docPropFilter) {
        try {
            return Factory.Document.fetchInstance(os, new Id(docId), docPropFilter);
        } catch (EngineRuntimeException ex) {
            if (ex.getExceptionCode() == ExceptionCode.E_OBJECT_NOT_FOUND) {
                logger.error("Document cannot be found by id '" + docId + "'.", ex);
                return null;
            } else
                throw ex;
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#cancelCheckOutDocument(com.filenet
     * .api.core.ObjectStore, java.lang.String)
     */

    /**
     * @param os
     * @param docPath
     * @return
     * @Title: fetchDocumentByPath
     * @Description: 根据路径获取文档信息
     * @Version 1.0
     * @Date 2017-3-21
     * @Author michael
     */
    public static Document fetchDocumentByPath(ObjectStore os, String docPath) {
        return fetchDocumentByPath(os, docPath, null);
    }

    public static Document fetchDocumentCurrentByPath(ObjectStore os, String docPath) {
        return fetchDocumentReservationByPath(os, docPath, null);
    }

    /**
     * 根据文件夹Id获取文件夹下的所有文档，支持获取文件夹下子文件夹的所以文档对象。
     * <br/>Method fetchDocumentByFolderPath Createby [Jeff] at 2016年3月1日 上午11:37:14
     *
     * @param os              内容库对象
     * @param folderId        文件夹标识
     * @param includSubFolder 是否包含子文件夹
     * @return 查询结果文档对象集合
     * @see
     */
    public static List<Document> fetchDocumentByFolderId(ObjectStore os, String folderId, boolean includSubFolder) {
        String folderpath = Factory.Folder.fetchInstance(os, new Id(folderId), null).get_PathName();
        String whereClause = "d.This " + (includSubFolder ? "INSUBFOLDER" : "INFOLDER") + " '" + folderpath + "' AND [IsCurrentVersion] = TRUE ";
        return searchForDocuments(os, "Document", true, whereClause);
    }
    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#getContentElement(com.filenet
     * .api.core.ObjectStore, java.lang.String, int)
     */

    public static Document fetchDocumentByPath(ObjectStore os, String docPath, PropertyFilter docPropFilter) {
        try {
            return Factory.Document.fetchInstance(os, docPath, docPropFilter);
        } catch (EngineRuntimeException ex) {
            if (ex.getExceptionCode() == ExceptionCode.E_OBJECT_NOT_FOUND) {
                //logger.error("Document cannot be found " + "by path '" + docPath + "'.");
                return null;
            } else
                throw ex;
        }
    }

    public static Document fetchDocumentReservationByPath(ObjectStore os, String docPath, PropertyFilter docPropFilter) {
        try {
            return (Document) Factory.Document.fetchInstance(os, docPath, docPropFilter).get_CurrentVersion();
        } catch (EngineRuntimeException ex) {
            if (ex.getExceptionCode() == ExceptionCode.E_OBJECT_NOT_FOUND) {
                logger.error("Document cannot be found " + "by path '" + docPath + "'.", ex);
                return null;
            } else
                throw ex;
        }
    }



    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#isCheckedOut(com.filenet.api
     * .core.ObjectStore, java.lang.String)
     */

    public static Document fetchReleasedDocument(ObjectStore os, String docId) {
        Versionable versionable = fetchDocumentById(os, docId).get_ReleasedVersion();
        return versionable == null ? null : (Document) versionable;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#listContainedDocumentsById(com
     * .filenet.api.core.ObjectStore, java.lang.String)
     */

    public static Document fetchReservedDocument(ObjectStore os, String docId) {
        IndependentObject ido = fetchDocumentById(os, docId).get_Reservation();
        return ido == null ? null : (Document) ido;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#listContainedDocumentsByPath
     * (com.filenet.api.core.ObjectStore, java.lang.String)
     */

    /**
     * @param doc
     * @param folder
     * @param inherit
     * @Title: file
     * @Description: 映射文档
     * @Version 1.0
     * @Date 2017-3-21
     * @Author michael
     */
    public static void file(Document doc, Folder folder) {
        file(null, doc, folder, true);
    }

    public static void file(Document doc, Folder folder, boolean inherit) {
        file(null, doc, folder, inherit);
    }

    @SuppressWarnings("rawtypes")
    public static void file(UpdatingBatch ub, Document doc, Folder folder, boolean inherit) {
        /*
         * 重复File校验
         */
        Iterator it = doc.get_FoldersFiledIn().iterator();
        while (it.hasNext()) {
            if (((Folder) it.next()).get_Id().equals(folder.get_Id())) {
                logger.warn("重复File: DocumentID:" + doc.get_Id() + ",FolderId:" + folder.get_Id());
                return;
            }
        }
        if (inherit) {
            doc.set_SecurityFolder(folder);
        }
        ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE, null, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
        rcr.set_ContainmentName(doc.getProperties().getStringValue("DocumentTitle"));
        // doc.set_SecurityFolder(folder);
        if (ub == null)
            rcr.save(RefreshMode.NO_REFRESH);
        else if (ub.hasPendingExecute()) {
            ub.add(rcr, null);
        }
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#listContainedDocuments(com.filenet
     * .api.core.Folder)
     */

    public static void file(IndependentlyPersistableObject independentlyPersistableObject, Folder folder, String containmentName) {
        ReferentialContainmentRelationship rcr = folder.file(independentlyPersistableObject, AutoUniqueName.AUTO_UNIQUE, null, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
        if (containmentName != null && !containmentName.equals(""))
            rcr.set_ContainmentName(containmentName);
        rcr.save(RefreshMode.NO_REFRESH);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#listDocuments(com.filenet.api
     * .core.ObjectStore, java.lang.String)
     */

    public static void file(ObjectStore os, String docId, String folderId, boolean inherit) {
        Folder folder = P8Folder.fetchFolderById(os, folderId);
        Document doc = fetchDocumentById(os, docId);
        file(doc, folder, inherit);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#listDocuments(com.filenet.api
     * .core.ObjectStore, java.lang.String, boolean)
     */

    public static List<Document> getAllVersionDocuments(ObjectStore os, String docId) {
        List<Document> items = new ArrayList<Document>();
        Document doc = fetchDocumentById(os, docId);

        VersionableSet versions = doc.get_Versions();
        Iterator<?> it = versions.iterator();
        while (it.hasNext()) {
            Document subDoc = (Document) it.next();
            items.add(subDoc);
        }
        return items;
    }

    /**
     * @param os
     * @param docId
     * @return
     * @Title: getInitVersionDocuments
     * @Description: 获得初识版本的文档
     * @Version 1.0
     * @Date 2016-8-19
     * @Author michael
     */
    public static Document getInitVersionDocument(ObjectStore os, String docId) {
        List<Document> items = new ArrayList<Document>();
        Document doc = fetchDocumentById(os, docId);
        VersionableSet versions = doc.get_Versions();
        Iterator<?> it = versions.iterator();
        while (it.hasNext()) {
            Document subDoc = (Document) it.next();
            items.add(subDoc);
        }
        if (items != null && items.size() > 0) {
            return items.get(items.size() - 1);
        } else {
            return null;
        }

    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#listCustomObjects(com.filenet
     * .api.core.ObjectStore, java.lang.String)
     */

    @Deprecated
    public static List<AttachmentVo> getAttachments(ObjectStore os, String docId) {
        List<AttachmentVo> result = new ArrayList<AttachmentVo>();

        Document doc = fetchDocumentById(os, docId);
        ContentElementList contentList = doc.get_ContentElements();
        for (int i = 0; i < contentList.size(); i++) {
            ContentTransfer transfer = (ContentTransfer) contentList.get(i);
            AttachmentVo att = new AttachmentVo();
            att.setIndex(i);
            att.setRetrievalName(transfer.get_RetrievalName());
            att.setContentType(transfer.get_ContentType());
            att.setContentSize(transfer.get_ContentSize());
            result.add(att);
        }

        return result;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#searchForDocuments(com.filenet
     * .api.core.ObjectStore, java.lang.String, java.lang.String)
     */

    @SuppressWarnings("rawtypes")
    public static Map<String, Object> getCEObjectProperties(ObjectStore os, String docId) {
        Map<String, Object> map = new HashMap<String, Object>();
        Document doc = fetchDocumentById(os, docId);

        Properties ps = doc.getProperties();
        String className = doc.getClassName();
        ClassDefinition objClzDef = Factory.ClassDefinition.fetchInstance(os, className, null);
        PropertyDefinitionList pdd = objClzDef.get_PropertyDefinitions();
        for (Iterator itp = pdd.iterator(); itp.hasNext(); ) {
            PropertyDefinition pd = (PropertyDefinition) itp.next();
            if (!pd.get_IsSystemOwned()) {
                String propName = pd.get_SymbolicName();
                Object obj = ps.getObjectValue(propName);
                propName = StringUtils.uncapitalize(propName);
                map.put(propName, obj);
            }
        }
        map.put("id", docId);

        /**
         * Properties ps = doc.getProperties(); Iterator<Property> it =
         * ps.iterator(); Property p = null; while (it.hasNext()) { p =
         * it.next(); String propName = p.getPropertyName(); Object obj =
         * ps.getObjectValue(propName); propName =
         * StringUtils.uncapitalize(propName); map.put(propName, obj); }
         */
        return map;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#searchForDocuments(com.filenet
     * .api.core.ObjectStore, java.lang.String, java.lang.String, boolean)
     */

    public static InputStream getContentElement(ObjectStore os, String docId, int index) {
        Document doc = fetchDocumentById(os, docId);
        ContentElementList contentList = doc.get_ContentElements();
        ContentTransfer transfer = (ContentTransfer) contentList.get(index);
        return transfer.accessContentStream();
    }

    public static InputStream getContentElementByDoc(ObjectStore os, Document doc, int index) {
        ContentElementList contentList = doc.get_ContentElements();
        ContentTransfer transfer = (ContentTransfer) contentList.get(index);

        return transfer.accessContentStream();
    }

    // /*
    // * (non-Javadoc)
    // * @see
    // * com.ibm.ecm.nuclear.data.filenet.api.OSI#searchForDocuments(com.filenet
    // * .api.core.ObjectStore, java.lang.String, boolean, java.lang.String)
    // */
    //
    // public static List<Vo> getDocClassLinkedContent(ObjectStore os, String
    // docId) {
    // List<Vo> items = new ArrayList<Vo>();
    // List<CustomObject> customObjects = new ArrayList<CustomObject>();
    // Document document = fetchDocumentById(os, docId);
    // customObjects = P8CustomObjectDao.searchForCustomObjects(os,
    // "RecordClassRelationship", "sourceClass = '" +
    // document.getClassName().toString() + "'");
    // for (CustomObject customObject : customObjects) {
    // String destinationClass =
    // customObject.getProperties().get("destinationClass").getStringValue().toString();
    // String destinationProperty =
    // customObject.getProperties().get("destinationProperty").getStringValue().toString();
    // String sourceProperty =
    // customObject.getProperties().get("sourceProperty").getStringValue().toString();
    // String sourcePropetyValue =
    // document.getProperties().getStringValue(sourceProperty);
    // for (Item item : ExtDocumentMapper.map(searchForDocuments(os,
    // destinationClass, "d." + destinationProperty + "='" + sourcePropetyValue
    // + "'"))) {
    // items.add(item);
    // }
    // }
    // return items;
    // }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#searchForDocuments(com.filenet
     * .api.core.ObjectStore, java.lang.String, java.lang.String, boolean,
     * java.lang.String, java.lang.String)
     */

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#iterateDocumentAttributes(com
     * .filenet.api.core.ObjectStore, com.filenet.api.core.Document)
     */

    public static boolean isCheckedOut(ObjectStore os, String docId) {
        return fetchReservedDocument(os, docId) != null;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#iterateDocumentAttributesByPath
     * (com.filenet.api.core.ObjectStore, java.lang.String)
     */

    public static Map<String, Object> iterateDocumentAttributes(ObjectStore os, Document doc) {
        Map<String, Object> map = new HashMap<String, Object>();

        Properties props = doc.getProperties();
        Iterator<?> it = props.iterator();
        while (it.hasNext()) {
            Property prop = (Property) it.next();
            map.put(prop.getPropertyName(), prop.getObjectValue());
        }

        return map;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#iterateDocumentAttributesById
     * (com.filenet.api.core.ObjectStore, java.lang.String)
     */

    public static Map<String, Object> iterateDocumentAttributesById(ObjectStore os, String docId) {
        Document doc = fetchDocumentById(os, docId);
        return iterateDocumentAttributes(os, doc);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#updateDocumentAttributes(com
     * .filenet.api.core.ObjectStore, com.filenet.api.core.Document,
     * java.util.Map)
     */

    public static Map<String, Object> iterateDocumentAttributesByPath(ObjectStore os, String docPath) {
        Document doc = fetchDocumentByPath(os, docPath);
        return iterateDocumentAttributes(os, doc);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#updateDocumentAttributes(com
     * .filenet.api.core.ObjectStore, java.lang.String, java.lang.String,
     * java.util.Map)
     */

    public static List<Document> listContainedDocuments(Folder parent) {
        List<Document> docList = new ArrayList<Document>();

        DocumentSet docSet = parent.get_ContainedDocuments();
        @SuppressWarnings("unchecked")
        Iterator<Document> iterator = docSet.iterator();
        while (iterator.hasNext()) {
            Document doc = iterator.next();
            Versionable cersionable = doc.get_CurrentVersion();
            doc = cersionable == null ? doc : (Document) cersionable;
            docList.add(doc);
        }

        return docList;
    }


    public static List<Document> listContainedDocumentsByPageSize(Folder parent, int pageSize) {
        List<Document> docList = new ArrayList<Document>();

        DocumentSet docSet = parent.get_ContainedDocuments();


        @SuppressWarnings("unchecked")

        List<Document> documentList = new ArrayList<Document>();
        DocumentSet documentSet = parent.get_ContainedDocuments();
        Iterator<?> iterator = documentSet.iterator();
        while (iterator.hasNext()) {
            Document document = (Document) iterator.next();


            documentList.add(document);


        }


        int totDocs = documentList.size();


        int fromIndex = (1 - 1) * 20;
        if (fromIndex < 0)
            fromIndex = 0;

        int toIndex = fromIndex + 20;
        if (toIndex > totDocs - 1)
            toIndex = totDocs;

        if (fromIndex < toIndex) {
            documentList = documentList.subList(fromIndex, toIndex);
        }


        return documentList;
    }

    public static List<Document> listChildDocuments(Document compoundDoc) {
        List<Document> list = new ArrayList<Document>();
        DocumentSet set = compoundDoc.get_ChildDocuments();
        @SuppressWarnings("unchecked")
        Iterator<Document> iterator = set.iterator();
        while (iterator.hasNext()) {
            Document doc = iterator.next();
            Versionable cersionable = doc.get_CurrentVersion();
            doc = cersionable == null ? doc : (Document) cersionable;
            list.add(doc);
        }
        return list;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#updateAttributes(com.filenet
     * .api.core.ObjectStore,
     * com.filenet.api.core.IndependentlyPersistableObject, java.util.Map)
     */

    public static List<Document> listContainedDocumentsById(ObjectStore os, String parentId) {
        Folder parent = P8Folder.fetchFolderById(os, parentId);
        return listContainedDocuments(parent);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#deleteDocumentById(com.filenet
     * .api.core.ObjectStore, java.lang.String)
     */

    public static List<Document> listContainedDocumentsByPath(ObjectStore os, String parentPath) {
        Folder parent = P8Folder.fetchFolderByPath(os, parentPath);
        return listContainedDocuments(parent);
    }


    public static List<Document> listContainedDocumentsByPathAndSzie(ObjectStore os, String parentPath, int pageSize) {
        //CE计时
        long startFindCETime = System.currentTimeMillis();
        Folder parent = P8Folder.fetchFolderByPath(os, parentPath);
        long endFindCETime = System.currentTimeMillis();
        logger.info("getting folder costs：" + (endFindCETime - startFindCETime));
        return listContainedDocumentsByPageSize(parent, pageSize);
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#deleteDocumentByPath(com.filenet
     * .api.core.ObjectStore, java.lang.String)
     */

    public static List<CustomObject> listCustomObjects(ObjectStore os, String symbolicName) {
        P8Search search = new P8Search();
        return search.setObjectSql(symbolicName, false, null, null).setScope(os).setPropertyFilter().fetchCustomObjects();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#deleteDocument(com.filenet.api
     * .core.Document)
     */

    public static List<Document> listDocuments(ObjectStore os, String symbolicName) {
        List<Document> docList = new ArrayList<Document>();

        String queryString = "SELECT * FROM Document d WHERE ISCLASS(d, [" + symbolicName + "]) AND [IsCurrentVersion] = TRUE";
        SearchSQL searchSQL = new SearchSQL(queryString);
        logger.debug("SearchSQL:" + searchSQL);
        SearchScope searchScope = new SearchScope(os);
        IndependentObjectSet set = searchScope.fetchObjects(searchSQL, null, null, false);

        @SuppressWarnings("unchecked")
        Iterator<Document> iter = set.iterator();
        while (iter.hasNext()) {
            docList.add(iter.next());
        }

        return docList;
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#searchForDocumentsWithPagination
     * (com.filenet.api.core.ObjectStore, java.lang.String, boolean,
     * java.lang.String, int, int)
     */

    public static List<Document> listDocuments(ObjectStore os, String symbolicName, boolean includeSubclasses) {
        P8Search search = new P8Search();
        return search.setObjectSql(symbolicName, includeSubclasses, null, null).setScope(os).setPropertyFilter().fetchDocuments();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#searchForDocumentsWithPagination
     * (com.filenet.api.core.ObjectStore, java.lang.String, boolean,
     * java.lang.String, int, int, boolean)
     */

    public static List<Document> searchForDocuments(ObjectStore os, String symbolicClassName, boolean includeSubclasses, String whereClause) {
        P8Search search = new P8Search();
        return search.setObjectSql(symbolicClassName, includeSubclasses, whereClause, null).setScope(os).setPropertyFilter().fetchDocuments();
    }

    public static List<Document> searchForDocuments(ObjectStore os, String symbolicClassName, boolean includeSubclasses, String whereClause, int maxRecords) {
        P8Search search = new P8Search();
        return search.setObjectSql(symbolicClassName, includeSubclasses, whereClause, null, maxRecords).setScope(os).setPropertyFilter().fetchDocuments();
    }

    public static List<Document> searchForDocuments(ObjectStore os, String symbolicClassName, boolean includeSubclasses, String whereClause, String orderByClause) {
        P8Search search = new P8Search();
        return search.setObjectSql(symbolicClassName, includeSubclasses, whereClause, null).setOrder(orderByClause).setScope(os).setPropertyFilter().fetchDocuments();
    }

    public static List<Document> searchForDocumentsCount(ObjectStore os, String symbolicClassName, boolean includeSubclasses, String whereClause) {
        P8Search search = new P8Search();
        return search.setObjectSql("d.Id", symbolicClassName, includeSubclasses, whereClause, null, -1).setScope(os).setPropertyFilter().fetchDocuments();
    }

    /*
     * (non-Javadoc)
     * @see
     * com.ibm.ecm.nuclear.data.filenet.api.OSI#getDocLinkedContent(com.filenet
     * .api.core.ObjectStore, java.lang.String)
     */

    public static List<Document> searchForDocuments(ObjectStore os, String symbolicClassName, String whereClause) {
        P8Search search = new P8Search();
        return search.setObjectSql(symbolicClassName, false, whereClause, null).setScope(os).setPropertyFilter().fetchDocuments();
    }

    /**
     * @param os                存储库对象
     * @param symbolicClassName 查询文件类型
     * @param whereClause       查询检索条件
     * @param isFullText        是否全文搜索
     * @return 查询文件集合
     * @title: searchForDocuments
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 根据条件查询检索文件，支持全文搜索
     */
    public static List<Document> searchForDocuments(ObjectStore os, String symbolicClassName, String whereClause, boolean isFullText) {
        P8Search search = new P8Search();
        return search.setObjectSql(symbolicClassName, false, whereClause, null, -1, true).setScope(os).setPropertyFilter().fetchDocuments();
    }

    /**
     * @param os                存储库对象
     * @param selectClause      查询字段语句
     * @param symbolicClassName 当前查询文件类型
     * @param includeSubclasses 是否包含子类型
     * @param whereClause       查询检索条件语句
     * @param orderByClause     查询检索排序语句
     * @return 查询文件结合
     * @title: searchForDocuments
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 根据条件查询检索文件，然后进行排序返回
     */
    public static List<Document> searchForDocuments(ObjectStore os, String selectClause, String symbolicClassName, boolean includeSubclasses, String whereClause, String orderByClause) {
        boolean distinct = true;
        P8Search search = new P8Search();
        return search.setObjectSql(distinct, selectClause, symbolicClassName, includeSubclasses, whereClause, orderByClause).setScope(os).setPropertyFilter().fetchDocuments();
    }

    /**
     * @param os                存储库对象
     * @param symbolicClassName 查询的文件类型标识
     * @param includeSubclasses 是否包含子文件类型
     * @param whereClause       查询检索条件SQL语句
     * @param pageSize          分页大小
     * @param desiredPageIndex  当前页数
     * @param selectList        当前需要查询的字段属性
     * @return 分页对象
     * @title: searchForDocumentsWithPagination
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 根据条件查询检索文件，分页形式返回给调用方
     */
    public static Pagination searchForDocumentsWithPagination(ObjectStore os, String symbolicClassName, boolean includeSubclasses, String whereClause, int pageSize, int desiredPageIndex, List<String> selectList) {
        P8Search search = new P8Search();
        return search.setObjectSql(symbolicClassName, includeSubclasses, whereClause, selectList).setScope(os).setPropertyFilter().setPageSize(pageSize).setDesiredPageIndex(desiredPageIndex).fetchDocumentsWithPagination();
    }


    /**
     * @param os
     * @param symbolicClassName
     * @param includeSubclasses 查询是否包含子类
     * @param whereClause       传null
     * @param orderByClause     排序语句
     * @param pageSize
     * @param desiredPageIndex  第几页
     * @param isFullText        是否全文
     * @param searchKeyword     查询关键词
     * @return
     * @Title: searchForDocumentsWithPaginationForFullTextSearch
     * @Description: 全文收索带分页
     * @Version 1.0
     * @Date 2016-3-21
     * @Author chenjun
     */
    public static Pagination searchForDocumentsWithPaginationForFullTextSearch(ObjectStore os, String symbolicClassName, boolean includeSubclasses,
                                                                               String whereClause, String orderByClause, int pageSize, int desiredPageIndex, boolean isFullText, String searchKeyword) {
        P8Search search = new P8Search();
        Pagination items = search.setObjectSqlForFullTextSearch(symbolicClassName, includeSubclasses, whereClause, orderByClause, pageSize, searchKeyword, isFullText)
                .setScope(os).setPropertyFilter().setPageSize(pageSize).setDesiredPageIndex(desiredPageIndex).fetchDocumentsWithPagination();
        return items;
    }


    /**
     * @param doc    文档对象
     * @param folder 文件夹对象
     * @title: unfile
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 从指定文件夹中移除文件
     */
    public static void unfile(Document doc, Folder folder) {
        unfile(null, doc, folder);
    }

    /**
     * @param ub     事务对象
     * @param doc    文档对象
     * @param folder 文件夹对象
     * @title: unfile
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 从指定文件夹中移除文件
     */
    public static void unfile(UpdatingBatch ub, Document doc, Folder folder) {
        ReferentialContainmentRelationship rcr = folder.unfile(doc);
        if (ub == null)
            rcr.save(RefreshMode.NO_REFRESH);
        else if (ub.hasPendingExecute()) {
            ub.add(rcr, null);
        }
    }

    /**
     * @param os       存储库对象
     * @param docId    文件唯一Id标识
     * @param folderId 文件夹唯一Id标识
     * @title: unfile
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 从指定标识文件夹中移除文档对象
     */
    public static void unfile(ObjectStore os, String docId, String folderId) {
        Folder folder = P8Folder.fetchFolderById(os, folderId);
        Document doc = fetchDocumentById(os, docId);
        unfile(doc, folder);
    }

    /**
     * @param os           存储库对象
     * @param ipo          操作对象
     * @param updatedAttrs 属性集合
     * @return 是否操作成功
     * @title: updateAttributes
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 更新文件属性
     */
    public static boolean updateAttributes(ObjectStore os, IndependentlyPersistableObject ipo, Map<String, Object> updatedAttrs) {
        return updateAttributes(os, ipo, updatedAttrs, null);
    }

    public static boolean updateAttributesWithoutSave(ObjectStore os, IndependentlyPersistableObject ipo, Map<String, Object> updatedAttrs) {
        return updateAttributesWithoutSave(os, ipo, updatedAttrs, null);
    }

    /**
     * @param os           存储库对象
     * @param ipo          操作对象
     * @param updatedAttrs 属性集合
     * @param ub           实物对象
     * @return 是否操作成功
     * @title: updateAttributes
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 更新文件属性
     */
    public static boolean updateAttributes(ObjectStore os, IndependentlyPersistableObject ipo, Map<String, Object> updatedAttrs, UpdatingBatch ub) {
        boolean flag = false;

        if (updatedAttrs == null || updatedAttrs.isEmpty())
            return true;

        Properties props = ipo.getProperties();
        String techProcDocId = "techProcDocId";
        if (updatedAttrs.get(techProcDocId) != null) {
            props.putObjectValue(techProcDocId, new Id(updatedAttrs.get(techProcDocId).toString()));
            updatedAttrs.remove(techProcDocId);
        }
        updateAttributes(props, updatedAttrs);
        if (ub == null) {
            ipo.save(RefreshMode.REFRESH);
        } else {
            ub.add(ipo, null);
        }
        flag = true;

        return flag;
    }

    public static boolean updateAttributesWithoutSave(ObjectStore os, IndependentlyPersistableObject ipo, Map<String, Object> updatedAttrs, UpdatingBatch ub) {
        boolean flag = false;

        if (updatedAttrs == null || updatedAttrs.isEmpty())
            return true;

        Properties props = ipo.getProperties();
        String techProcDocId = "techProcDocId";
        if (updatedAttrs.get(techProcDocId) != null) {
            props.putObjectValue(techProcDocId, new Id(updatedAttrs.get(techProcDocId).toString()));
            updatedAttrs.remove(techProcDocId);
        }
        updateAttributes(props, updatedAttrs);
        if (ub == null) {
//			ipo.save(RefreshMode.REFRESH);
        } else {
            ub.add(ipo, null);
        }
        flag = true;

        return flag;
    }


    /**
     * @param os            存储库对象
     * @param doc           文件对象
     * @param propertyName  属性名称
     * @param propertyValue 属性值
     * @title: updateDocumentAttribute
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 更新文件指定属性值
     */
    public static void updateDocumentAttribute(ObjectStore os, Document doc, String propertyName, Object propertyValue) {
        updateDocumentAttribute(os, doc, propertyName, propertyValue, null);
    }


    /**
     * @param os            存储库对象
     * @param doc           文件对象
     * @param propertyName  属性名称
     * @param propertyValue 属性值
     * @param ub            实物对象
     * @title: updateDocumentAttribute
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 更新文件指定属性值
     */
    public static void updateDocumentAttribute(ObjectStore os, Document doc, String propertyName, Object propertyValue, UpdatingBatch ub) {
        Map<String, Object> updatedAttrs = new HashMap<String, Object>();
        updatedAttrs.put(propertyName, propertyValue);
        updateAttributes(os, doc, updatedAttrs, ub);
    }

    /**
     * @param os           存储库对象
     * @param doc          文件对象
     * @param updatedAttrs 属性对象集合
     * @title: updateDocumentAttributes
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 更新文件属性
     */
    public static void updateDocumentAttributes(ObjectStore os, Document doc, Map<String, Object> updatedAttrs) {
        updateAttributes(os, doc, updatedAttrs);
    }

    public static void updateDocumentAttributesWithoutSave(ObjectStore os, Document doc, Map<String, Object> updatedAttrs) {
        updateAttributesWithoutSave(os, doc, updatedAttrs);
    }

    /**
     * @param os                存储库对象
     * @param docId             文件唯一Id标识
     * @param symbolicClassName 文件类标识
     * @param updatedAttrs      属性对象集合
     * @title: updateDocumentAttributes
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 更新文件属性
     */
    public static void updateDocumentAttributes(ObjectStore os, String docId, String symbolicClassName, Map<String, Object> updatedAttrs) {
        updateAttributes(os, fetchDocumentById(os, docId), updatedAttrs);
    }

    /**
     * @param os    存储库对象
     * @param docId 文件唯一Id标识
     * @return 文件夹集合
     * @title: getFiledFolders
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 获取指定文件的所有存储文件夹集合
     */
    public static FolderSet getFiledFolders(ObjectStore os, String docId) {
        Document document = fetchDocumentById(os, docId);
        FolderSet result = document.get_FoldersFiledIn();
        return result;
    }

    /**
     * @param os         文件存储库对象
     * @param doc        文档对象
     * @param destFolder 目标文件夹
     * @param origFolder 源文件文件夹
     * @title: move
     * @date 2014年12月4日
     * @author Jeffrey
     * @description: 移动文件到指定文件夹
     */
    public static void move(ObjectStore os, Document doc, Folder destFolder, Folder origFolder) {
        UpdatingBatch ub = UpdatingBatch.createUpdatingBatchInstance(os.get_Domain(), RefreshMode.REFRESH);
        ReferentialContainmentRelationship rcr1 = origFolder.unfile(doc);
        ub.add(rcr1, null);
        ReferentialContainmentRelationship rcr = destFolder.file(doc, AutoUniqueName.AUTO_UNIQUE, null, DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
        rcr.set_ContainmentName(doc.getProperties().getStringValue("DocumentTitle"));
        // doc.set_SecurityFolder(destFolder);
        ub.add(rcr, null);
        ub.updateBatch();
    }

    public static void setContentElementList(Document released, ContentElementList contentElementList) {
        if (contentElementList != null) {
            released.set_ContentElements(contentElementList);
        } else {
            released.set_ContentElements(released.get_ContentElements());
        }
    }


    @SuppressWarnings("rawtypes")
    public CmThumbnail getThumbnail(ObjectStore os, String docId) {
        Document document = P8Document.fetchCurrentVersionDocument(os, docId);
        CmThumbnailSet thumbnails = document.get_CmThumbnails();
        if (!document.get_CmThumbnails().isEmpty()) {
            Iterator iter = thumbnails.iterator();
            while (iter.hasNext()) {
                CmThumbnail tn = (CmThumbnail) iter.next();
                return tn;
            }
        }
        return null;
    }

    public static String uploadNewDocument(ObjectStore os, Folder folder, FnAttachIsVo attachObj) throws Exception {
        ContentElementList newContentElementList = Factory.ContentElement.createList();
        String attachName = attachObj.getAttachName();
        ContentTransfer newTransfer = Factory.ContentTransfer.createInstance();
        newTransfer.setCaptureSource(attachObj.getIs());
        newTransfer.set_RetrievalName(attachName);
        newContentElementList.add(newTransfer);
        String containmentName = attachObj.getId();
        Document document = createDocument(os, "Document", attachName, folder,
                newContentElementList, containmentName);
        if (attachObj.getIs() != null) {
            attachObj.getIs().close();
        }
        return document.get_Id().toString();
    }

    public static Document createDocument(ObjectStore os, final String symbolicName, final String documentTitle,
                                          final Folder folder,
                                          final ContentElementList contentElementList, String containmentName) {
        final Document doc = Factory.Document.createInstance(os, symbolicName);
        doc.save(RefreshMode.REFRESH);
        final Properties props = doc.getProperties();
        final String docTitle = "DocumentTitle";
        props.putValue(docTitle, documentTitle);
        if (contentElementList != null && contentElementList.size() > 0) {
            setContentElementList(doc, contentElementList);
            doc.save(RefreshMode.NO_REFRESH);
        }
        doc.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY, CheckinType.MAJOR_VERSION);
        doc.save(RefreshMode.REFRESH);

        final ReferentialContainmentRelationship rcr = folder.file(doc, AutoUniqueName.AUTO_UNIQUE, null,
                DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
        rcr.set_ContainmentName(containmentName);
        rcr.save(RefreshMode.NO_REFRESH);
        return doc;
    }

    public static ContentTransfer getAttachFileFromDocument(ObjectStore os, String id) {
        Document doc = fetchDocumentById(os, id);
        final ContentElementList contentElementList = doc.get_ContentElements();
        ContentTransfer transfer = (ContentTransfer) contentElementList.get(0);
        return transfer;
    }
}
