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
package com.zony.common.filenet.util;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.core.ContentTransfer;
import com.zony.common.filenet.ce.dao.P8Document;
import com.zony.common.filenet.vo.AttachmentVo;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 
 * @version $Rev$ $Date$
 */
public class AttachmentMapper {
	public static AttachmentVo map(ContentTransfer content, int index) {
		AttachmentVo attachment = new AttachmentVo();
		attachment.setIndex(index);
		String name = content.get_RetrievalName();
		String id = getAttachmentIDFromRetrievalName(name);
		attachment.setId(id);
		attachment.setRetrievalName(getAttachmentNameFromRetrievalName(name));
		attachment.setContentSize(content.get_ContentSize());
		attachment.setContentType(content.get_ContentType());
		return attachment;
	}

	public static String getAttachmentPlantIDFromRetrievalName(String name) {
		if (name != null) {
			String[] nameSplit = name.split(P8Document.SEPARATOR);
			if (nameSplit.length == 5) {
				return nameSplit[2];
			}
		}
		return null;
	}

	public static List<AttachmentVo> map(ContentElementList contentElementList) {
		List<AttachmentVo> attachments = new ArrayList<AttachmentVo>();
		for (int i = 0; i < contentElementList.size(); i++) {
			if (contentElementList.get(i) instanceof ContentTransfer) {
				AttachmentVo entry = AttachmentMapper.map((ContentTransfer) contentElementList.get(i), i + 1);
				attachments.add(entry);
			}
		}

		return attachments;
	}

	public static String getAttachmentIDFromRetrievalName(String name) {
		String result = null;
		if (name != null && name.split(P8Document.SEPARATOR).length != 4) {// 决策信息的附件名称比较特殊为:
																			// siteid,plantid,docid,name
			int sepIndex = name.indexOf(P8Document.SEPARATOR);
			if (sepIndex > 0) {
				result = name.substring(0, sepIndex);
			}
		}
		return result;
	}

	public static String getAttachmentNameFromRetrievalName(String name) {
		String result = null;
		int sepIndex = name.lastIndexOf(P8Document.SEPARATOR);
		if (name != null) {
			if (sepIndex > 0) {
				result = name.substring(sepIndex + 1, name.length());
			} else {
				result = name;
			}
		}
		return result;

	}

}
