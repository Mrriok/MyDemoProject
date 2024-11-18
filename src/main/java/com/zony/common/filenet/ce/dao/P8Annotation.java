package com.zony.common.filenet.ce.dao;

import com.filenet.api.collection.AnnotationSet;
import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.ClassNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @fileName P8ChoiceList.java
 * @package com.zony.filenet.dao
 * @function 中类似数据字典对象操作相关
 * @version 1.0.0
 * @date 2014-8-4
 * @author Jeffrey
 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
 */
public class P8Annotation {
	public static Logger logger = LoggerFactory.getLogger(P8Annotation.class);

	/**
	 * @title: hasAnnotationById
	 * @date 2014年9月2日
	 * @author Jeffrey
	 * @description: 判断当前文档是否存在标注文件，存在返回true，否则返回false
	 * @param os
	 *            文档存储库对象
	 * @param docId
	 *            文档唯一标识
	 * @return 是否存在，true 存在、false 不存在
	 */
	public static boolean hasAnnotationById(ObjectStore os, String docId) {
		boolean hasAnno = false;
		try {
			Document doc = P8Document.fetchDocumentById(os, docId);
			AnnotationSet annSet = doc.get_Annotations();
			Iterator<Annotation> annIterator = annSet.iterator();
			if (annIterator.hasNext()) {
				hasAnno = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return hasAnno;
	}

	/**
	 * @title: saveAnnotation2LocalById
	 * @date 2014年9月2日
	 * @author Jeffrey
	 * @description: 获取CE中文件中文档对象中的Annotation对象保存本地路径下
	 * @param os
	 *            文档存储库对象
	 * @param savePath
	 *            本地保存的文件路径
	 * @param annFileName
	 *            CE文档中Annotation的名称
	 * @param docId
	 *            文档唯一标识
	 * @return 是否成功
	 */
	public static boolean saveAnnotation2LocalById(ObjectStore os, String savePath, String annFileName, String docId) {
		try {
			Document doc = P8Document.fetchDocumentById(os, docId);
			AnnotationSet annSet = doc.get_Annotations();
			Iterator<Annotation> annIterator = annSet.iterator();
			while (annIterator.hasNext()) {
				Annotation ann = (Annotation) annIterator.next();
				System.out.println("annotation name=" + ann.get_Name());
				ContentElementList annContentList = ann.get_ContentElements();
				Iterator<ContentElement> annContentInterator = annContentList.iterator();
				while (annContentInterator.hasNext()) {
					ContentTransfer annContent = (ContentTransfer) annContentInterator.next();
					InputStream is = annContent.accessContentStream();
					// String fileName = annContent.get_RetrievalName();
					System.out.println("annotation content name=" + annContent.get_RetrievalName());
					OutputStream outputStream = new FileOutputStream(savePath + annFileName);
					byte[] nextBytes = new byte[64000];
					int nBytesRead;
					while ((nBytesRead = is.read(nextBytes)) != -1) {
						outputStream.write(nextBytes, 0, nBytesRead);
						outputStream.flush();
					}
					outputStream.close();
					is.close();
				}
			}
		} catch (Exception e) {
			logger.error("update and save annotation error ; ", e);
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * @title: saveAnnotation2CEById
	 * @date 2014年9月2日
	 * @author Jeffrey
	 * @description: 保存文件到CE文档中的Annotation对象
	 * @param os
	 *            文档存储库对象
	 * @param annFilePath
	 *            Annotation文件路径
	 * @param docId
	 *            文档唯一标识
	 * @throws Exception
	 *             异常信息
	 */
	public static void saveAnnotation2CEById(ObjectStore os, String annFilePath, String docId) throws Exception {
		try {
			Document doc = P8Document.fetchDocumentById(os, docId);
			delAnnotation(doc);
			Annotation myAnno = Factory.Annotation.createInstance(os, ClassNames.ANNOTATION);
			ContentElementList contentList = Factory.ContentElement.createList();
			ContentTransfer content = Factory.ContentTransfer.createInstance();
			content.setCaptureSource(new FileInputStream(annFilePath));
			// content.set_RetrievalName(annFileName);
			content.set_RetrievalName("annotation.xml");
			contentList.add(content);
			myAnno.set_ContentElements(contentList);
			myAnno.set_MimeType("text/xml");
			myAnno.set_AnnotatedObject(doc);
			myAnno.save(RefreshMode.REFRESH);
		} catch (Exception e) {
			logger.error("update and save annotation error ; ", e);
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @title: saveAnnotation2CEById
	 * @date 2014年9月2日
	 * @author Jeffrey
	 * @description: 保存文件到CE文档中的Annotation对象，根据文件流方式保存
	 * @param os
	 *            文档存储库对象
	 * @param is
	 *            需要保存的文件流
	 * @param docId
	 *            文档唯一标识
	 * @throws Exception
	 *             异常对象
	 */
	public static void saveAnnotation2CEById(ObjectStore os, InputStream is, String docId) throws Exception {
		try {
			Document doc = P8Document.fetchDocumentById(os, docId);
			delAnnotation(doc);
			Annotation myAnno = Factory.Annotation.createInstance(os, ClassNames.ANNOTATION);
			ContentElementList contentList = Factory.ContentElement.createList();
			ContentTransfer content = Factory.ContentTransfer.createInstance();
			content.setCaptureSource(is);
			// content.set_RetrievalName(annFileName);
			content.set_RetrievalName("annotation.xml");
			contentList.add(content);
			myAnno.set_ContentElements(contentList);
			myAnno.set_MimeType("text/xml");
			myAnno.set_AnnotatedObject(doc);
			myAnno.save(RefreshMode.REFRESH);
		} catch (Exception e) {
			logger.error("update and save annotation error ; ", e);
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * @title: getAnnotationInStream
	 * @date 2014年9月2日
	 * @author Jeffrey
	 * @description: 获取文档对象中的Annotation对象内容集合
	 * @param doc
	 *            文档对象
	 * @return Annotation对象流集合
	 */
	public static List<InputStream> getAnnotationInStream(Document doc) {
		List<InputStream> isList = new ArrayList<InputStream>();
		AnnotationSet annSet = doc.get_Annotations();
		if (!annSet.isEmpty()) {// 如果有标注的话
			Iterator<Annotation> annIterator = annSet.iterator();
			while (annIterator.hasNext()) {
				Annotation ann = (Annotation) annIterator.next();
				System.out.println("annotation name=" + ann.get_Name());
				ContentElementList annContentList = ann.get_ContentElements();
				Iterator<ContentElement> annContentInterator = annContentList.iterator();
				while (annContentInterator.hasNext()) {
					ContentTransfer annContent = (ContentTransfer) annContentInterator.next();
					InputStream is = annContent.accessContentStream();
					isList.add(is);
				}
			}
		}
		return isList;
	}

	/**
	 * @title: delAnnotation
	 * @date 2014年9月2日
	 * @author Jeffrey
	 * @description: 删除文档中的Annotation对象
	 * @param doc
	 *            文档对象
	 */
	public static void delAnnotation(Document doc) {
		AnnotationSet annSet = doc.get_Annotations();
		Iterator<?> annIterator = annSet.iterator();
		while (annIterator.hasNext()) {
			Annotation docAn = (Annotation) annIterator.next();
			System.out.println(docAn.get_Id());
			docAn.delete();
			docAn.save(RefreshMode.REFRESH);
		}
	}

}
