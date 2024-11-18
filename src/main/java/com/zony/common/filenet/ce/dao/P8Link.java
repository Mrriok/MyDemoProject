package com.zony.common.filenet.ce.dao;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.Link;
import com.filenet.api.core.ObjectStore;
import com.zony.common.filenet.os.Search;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Map;

public class P8Link {

	private static final Logger logger = LoggerFactory.getLogger(P8Link.class);

	/*
	 * (non-Javadoc)
	 * @see
	 * com.ibm.ecm.nuclear.data.filenet.api.OSI#createLink(com.filenet.api.core
	 * .ObjectStore, com.filenet.api.core.IndependentObject,
	 * com.filenet.api.core.IndependentObject)
	 */

	public static Link createLink(ObjectStore os, IndependentObject head, IndependentObject tail) {
		return createLink(os, head, tail, null);
	}

	public static Link createLink(ObjectStore os, IndependentObject head, IndependentObject tail, Map<String, Object> propMap) {
		Link link = Factory.Link.createInstance(os, "ZonyECMLink");
		P8Document.updateAttributes(link.getProperties(), propMap);
		link.set_Head(head);
		link.set_Tail(tail);
		link.save(RefreshMode.REFRESH);
		return link;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.ibm.ecm.nuclear.data.filenet.api.OSI#fetchLinks(com.filenet.api.core
	 * .ObjectStore, java.lang.String)
	 */

	public static List<Link> fetchLinks(ObjectStore os, String headID/*
																	 * ,
																	 * Containable
																	 * tailObject
																	 */) {

		String className = "ZonyECMLink";
		Search search = new Search();
		// String headObjectID = headObject.get_Id().toString();
		String headObjectID = headID;

		String whereClause = "((Head = Object('" + headObjectID + "') and Nuclear_RelationDirection <> 'RelationFrom') or (Tail = Object('" + headObjectID
				+ "') and Nuclear_RelationDirection <> 'RelationTo'))";
		// String whereClause = "(Head = Object('" + headObjectID + "'))";
		logger.debug(whereClause);
		return search.setObjectSql(className, true, whereClause).setScope(os).setPropertyFilter().fetchLinks();
	}

	/**
	 * Fetch all links which relate to the given object, no matter the object is
	 * head or tail
	 * 
	 * @param os
	 * @param docId
	 * @return
	 */
	public static List<Link> fetchAllLinks(ObjectStore os, String docId) {
		String className = "ZonyECMLink";
		Search search = new Search();
		String whereClause = "(Head = Object('" + docId + "')) or (Tail = Object('" + docId + "'))";
		return search.setObjectSql(className, true, whereClause).setScope(os).setPropertyFilter().fetchLinks();
	}

	/**
	 * Fetch links by tail object
	 * 
	 * @param os
	 * @param tailId
	 * @return
	 */
	public static List<Link> fetchLinksByTail(ObjectStore os, String tailId) {
		String className = "ZonyECMLink";
		Search search = new Search();
		String whereClause = "(Tail = Object('" + tailId + "'))";
		return search.setObjectSql(className, true, whereClause).setScope(os).setPropertyFilter().fetchLinks();
	}

	/**
	 * Fetch links by head object
	 * 
	 * @param os
	 * @param headId
	 * @return
	 */
	public static List<Link> fetchLinksByHead(ObjectStore os, String headId) {
		String className = "ZonyECMLink";
		Search search = new Search();
		String whereClause = "(Head = Object('" + headId + "'))";
		return search.setObjectSql(className, true, whereClause).setScope(os).setPropertyFilter().fetchLinks();
	}
}