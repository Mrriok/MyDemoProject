package com.zony.common.filenet.ce.dao;

import com.filenet.api.collection.AccessPermissionList;
import com.filenet.api.constants.AccessLevel;
import com.filenet.api.constants.AccessType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.security.AccessPermission;

import javax.naming.NamingException;
import java.util.List;


public class P8Security {
	
	/**
	 * 
	 * @Title: editUserPermissionToDocument 
	 * @Description: 修改指定用户的权限
	 * @param doc
	 * @param userId
	 * @param accessType
	 * @param accessLevel
	 * @throws NamingException
	 * @throws Exception
	 * @Author NiMH
	 * @Date 2020年3月19日
	 */
	public static void editUserPermissionToDocument(Document doc, String userId, AccessType accessType,
                                                    int accessLevel) throws NamingException, Exception{
		AccessPermissionList apl = doc.get_Permissions();
		AccessPermission ap = Factory.AccessPermission.createInstance();
		
		String granteeName = userId;
		for (int i = 0; i < apl.size(); i++) {
			AccessPermission p = (AccessPermission) apl.get(i);
			String name = p.get_GranteeName().split("@")[0];
			if (name.equals(granteeName)) {
				apl.remove(i);
			}
		}
		ap.set_GranteeName(granteeName);
		ap.set_AccessType(accessType);
		ap.set_AccessMask(accessLevel);
		apl.add(ap);
		doc.set_Permissions(apl);
		doc.save(RefreshMode.REFRESH);
	}
	
	
	/**
	 * 
	 * @Title: assignUsersPermissionToDocument 
	 * @Description: 分配用户权限信息到指定文档对象
	 * @param doc
	 * @param granteeNames
	 * @param accessType
	 * @param accessLevel
	 * @throws Exception 
	 * @throws NamingException 
	 * @Author NiMH
	 * @Date 2020年3月18日
	 */
	public static void assignPermissionUsersToDocument(Document doc, List<String> userIds, AccessType accessType,
                                                       int accessLevel) throws NamingException, Exception {
		AccessPermissionList apl = doc.get_Permissions();
		AccessPermission ap = null;
	
		for (String userId : userIds) {
			ap = Factory.AccessPermission.createInstance();
		
			String granteeName = userId;
			ap.set_GranteeName(granteeName);
			ap.set_AccessType(accessType);
			ap.set_AccessMask(accessLevel);
			apl.add(ap);
			doc.set_Permissions(apl);
			doc.save(RefreshMode.REFRESH);
		}
	}
	
	
	/**
	 * @title: assignAccessPermissionToDocument
	 * @date 2014年9月4日
	 * @author Jeffrey
	 * @description: 分配用户权限信息到指定文档对象
	 * @param os
	 *            存储库对象
	 * @param granteeName
	 *            授权用户或组名称
	 * @param docId
	 *            文档ID表示
	 * @param accessType
	 *            访问类型对象
	 * @param accessLevel
	 *            访问级别对象
	 */
	public static void assignAccessPermissionToDocument(ObjectStore os, String granteeName, String docId,
                                                        AccessType accessType, AccessLevel accessLevel) {
		Document doc = P8Document.fetchDocumentById(os, docId);
		assignAccessPermissionToDocument(doc, granteeName, accessType, accessLevel);
	}

	/**
	 * @title: assignAccessPermissionToDocument
	 * @date 2014年9月4日
	 * @author Jeffrey
	 * @description: 分配用户权限信息到指定文档对象
	 * @param doc
	 *            文档对象
	 * @param granteeName
	 *            授权用户或组名称
	 * @param accessType
	 *            访问类型对象
	 * @param accessLevel
	 *            访问级别对象
	 */
	@SuppressWarnings("deprecation")
	public static void assignAccessPermissionToDocument(Document doc, String granteeName, AccessType accessType,
                                                        AccessLevel accessLevel) {
		AccessPermissionList apl = doc.get_Permissions();
		AccessPermission ap = Factory.AccessPermission.createInstance();
		ap.set_GranteeName(granteeName);
		ap.set_AccessType(accessType);
		ap.set_AccessMask(accessLevel.getValue());
		apl.add(ap);
		doc.set_Permissions(apl);
		doc.save(RefreshMode.REFRESH);
	}

	/**
	 * @title: assignAccessPermissionToDocument
	 * @date 2014年9月4日
	 * @author Jeffrey
	 * @description: 分配用户权限信息到指定文档对象
	 * @param os
	 *            存储库对象
	 * @param granteeName
	 *            授权用户或组名称
	 * @param docId
	 *            文档ID标识
	 * @param accessType
	 *            访问类型
	 * @param accessMask
	 *            访问权限
	 */
	@SuppressWarnings("unchecked")
	public static void assignAccessPermissionToDocument(ObjectStore os, String granteeName, String docId,
                                                        int accessType, int accessMask) {
		Document doc = P8Document.fetchDocumentById(os, docId);
		assignAccessPermissionToDocument(doc, granteeName, accessType, accessMask);
	}

	/**
	 * @title: assignAccessPermissionToDocument
	 * @date 2014年9月4日
	 * @author Jeffrey
	 * @description: 分配用户权限信息到指定文档对象
	 * @param doc
	 *            文档对象
	 * @param granteeName
	 *            授权用户或组名称
	 * @param accessType
	 *            访问类型
	 * @param accessMask
	 *            访问权限
	 */
	public static void assignAccessPermissionToDocument(Document doc, String granteeName, int accessType,
                                                        int accessMask) {
		AccessPermissionList apl = doc.get_Permissions();
		AccessPermission ap = Factory.AccessPermission.createInstance();
		ap.set_GranteeName(granteeName);
		ap.set_AccessType(AccessType.getInstanceFromInt(accessType));
		ap.set_AccessMask(accessMask);
		apl.add(ap);
		doc.set_Permissions(apl);
		doc.save(RefreshMode.REFRESH);
	}

	/**
	 * @title: assignViewAccessPermissionToDocument
	 * @date 2014年10月13日
	 * @author Jeffrey
	 * @description: 文档阅读权限设置
	 * @param doc
	 *            文档对象
	 * @param granteeName
	 *            授权用户或组名称
	 */
	public static void assignViewAccessPermissionToDocument(Document doc, String granteeName) {
		AccessPermissionList apl = doc.get_Permissions();
		boolean isHaveDownPerm = false;
		for (int i = 0; i < apl.size(); i++) {
			AccessPermission ap = (AccessPermission) apl.get(i);
			String granteeName1 = ap.get_GranteeName().split("@")[0];
			if (granteeName1.equals(granteeName)
					&& ap.get_AccessMask().intValue() == AccessLevel.FULL_CONTROL_DOCUMENT_AS_INT) {
				isHaveDownPerm = true;
			}
		}
		if (isHaveDownPerm) {
			return;
		}
		AccessPermission ap = Factory.AccessPermission.createInstance();
		ap.set_GranteeName(granteeName);
		ap.set_AccessType(AccessType.ALLOW);
		ap.set_AccessMask(AccessLevel.VIEW_AS_INT);
		apl.add(ap);
		doc.set_Permissions(apl);
		doc.save(RefreshMode.REFRESH);
	}

	/**
	 * 在文档上添加需要授权的用户权限，此访问为添加查看权限 <br/>
	 * Method assignViewAccessPermissionToDocument Createby [Jeff] at 2016年3月9日
	 * 下午1:26:42
	 * 
	 * @see
	 * @param doc
	 *            文档对象
	 * @param granteeNames
	 *            需要授权的用户标识集合
	 */
	public static void assignViewAccessPermissionToDocument(Document doc, List<String> granteeNames) {
		AccessPermissionList apl = doc.get_Permissions();
		for (String granteeName : granteeNames) {
			boolean isHaveDownPerm = false;
			for (int i = 0; i < apl.size(); i++) {
				AccessPermission ap = (AccessPermission) apl.get(i);
				String granteeName1 = ap.get_GranteeName().split("@")[0];
				if (granteeName1.equals(granteeName)
						&& ap.get_AccessMask().intValue() == AccessLevel.FULL_CONTROL_DOCUMENT_AS_INT) {
					isHaveDownPerm = true;
				}
			}
			if (isHaveDownPerm) {
				continue;
			}
			AccessPermission ap = Factory.AccessPermission.createInstance();
			ap.set_GranteeName(granteeName);
			ap.set_AccessType(AccessType.ALLOW);
			ap.set_AccessMask(AccessLevel.VIEW_AS_INT);
			apl.add(ap);
		}
		doc.set_Permissions(apl);
		doc.save(RefreshMode.REFRESH);
	}

	/**
	 * 文档完全控制权限设置,将指定用户添加到文档完全控制权限中 <br/>
	 * Method assignFullControlAccessPermissionToDocument Createby [Jeff] at
	 * 2016年3月9日 下午1:29:11
	 * 
	 * @see
	 * @param doc
	 *            文档对象
	 * @param granteeName
	 *            需要授权的用户标识
	 */
	public static void assignFullControlAccessPermissionToDocument(Document doc, String granteeName) {
		AccessPermissionList apl = doc.get_Permissions();
		for (int i = 0; i < apl.size(); i++) {
			AccessPermission ap = (AccessPermission) apl.get(i);
			String granteeName1 = ap.get_GranteeName().split("@")[0];
			if (granteeName1.equals(granteeName)) {
				apl.remove(i);
			}
		}
		AccessPermission ap = Factory.AccessPermission.createInstance();
		ap.set_GranteeName(granteeName);
		ap.set_AccessType(AccessType.ALLOW);
		ap.set_AccessMask(AccessLevel.FULL_CONTROL_DOCUMENT_AS_INT);
		apl.add(ap);
		doc.set_Permissions(apl);
		doc.save(RefreshMode.REFRESH);
	}

	/**
	 * 文档完全控制权限设置,将指定用户(集合)添加到文档完全控制权限中 <br/>
	 * Method assignFullControlAccessPermissionToDocument Createby [Jeff] at
	 * 2016年3月9日 下午1:30:14
	 * 
	 * @see
	 * @param doc
	 *            文档对象
	 * @param granteeNames
	 *            需要授权的用户标识集合
	 */
	public static void assignFullControlAccessPermissionToDocument(Document doc, List<String> granteeNames) {
		AccessPermissionList apl = doc.get_Permissions();
		for (String granteeName : granteeNames) {
			for (int i = 0; i < apl.size(); i++) {
				AccessPermission ap = (AccessPermission) apl.get(i);
				String granteeName1 = ap.get_GranteeName().split("@")[0];
				if (granteeName1.equals(granteeName)) {
					apl.remove(i);
				}
			}
			AccessPermission ap = Factory.AccessPermission.createInstance();
			ap.set_GranteeName(granteeName);
			ap.set_AccessType(AccessType.ALLOW);
			ap.set_AccessMask(AccessLevel.FULL_CONTROL_DOCUMENT_AS_INT);
			apl.add(ap);
		}
		doc.set_Permissions(apl);
		doc.save(RefreshMode.REFRESH);
	}

	/**
	 * @title: revokeAccessPermissionFromDocument
	 * @date 2014年9月4日
	 * @author Jeffrey
	 * @description: 移除指定文档下指定用户的所有权限
	 * @param os
	 *            存储库对象
	 * @param granteeName
	 *            授权用户或组名称
	 * @param docId
	 *            文档ID标识
	 */
	public static void revokeAccessPermissionFromDocument(ObjectStore os, String granteeName, String docId) {
		Document doc = P8Document.fetchDocumentById(os, docId);
		AccessPermissionList apl = doc.get_Permissions();
		for (int i = 0; i < apl.size(); i++) {
			AccessPermission ap = (AccessPermission) apl.get(i);
			String granteeName1 = ap.get_GranteeName().split("@")[0];
			if (granteeName1.equals(granteeName)) {
				apl.remove(i);
			}
		}
		doc.set_Permissions(apl);
		doc.save(RefreshMode.REFRESH);
	}

	/**
	 * @title: revokeAccessPermissionFromDocument
	 * @date 2014年9月4日
	 * @author Jeffrey
	 * @description: 移除文档中特定用户的指定权限信息
	 * @param os
	 *            存储库对象
	 * @param granteeName
	 *            授权用户或组名称
	 * @param docId
	 *            文档ID标识
	 * @param accessType
	 *            权限对象
	 */
	public static void revokeAccessPermissionFromDocument(ObjectStore os, String granteeName, String docId,
                                                          AccessType accessType) {
		Document doc = P8Document.fetchDocumentById(os, docId);
		revokeAccessPermissionFromDocument(doc, granteeName, accessType.getValue(), false);
	}

	/**
	 * @title: revokeAccessPermissionFromDocument
	 * @date 2014年9月4日
	 * @author Jeffrey
	 * @description: 移除文档中特定用户的指定权限信息
	 * @param doc
	 *            文档对象标识
	 * @param granteeName
	 *            用户对象
	 * @param accessType
	 *            权限对象
	 * @param isAll
	 *            是否移除用户的全部相同权限
	 */
	public static void revokeAccessPermissionFromDocument(Document doc, String granteeName, int accessType,
                                                          boolean isAll) {
		AccessPermissionList apl = doc.get_Permissions();
		for (int i = 0; i < apl.size(); i++) {
			AccessPermission ap = (AccessPermission) apl.get(i);
			String granteeName1 = ap.get_GranteeName().split("@")[0];
			if (granteeName1.equals(granteeName)
					&& ap.get_AccessType().equals(AccessType.getInstanceFromInt(accessType))) {
				apl.remove(i);
				if (!isAll) {
					break;
				}
			}
		}
		doc.set_Permissions(apl);
		doc.save(RefreshMode.REFRESH);
	}

	/**
	 * @title: assignAccessPermissionToFolder
	 * @date 2014年9月4日
	 * @author Jeffrey
	 * @description: 分配用户权限到指定文件夹上
	 * @param os
	 *            存储库对象
	 * @param granteeName
	 *            授权用户或组名称
	 * @param folderId
	 *            文件夹标识
	 * @param accessType
	 *            访问类型对象
	 * @param accessLevel
	 *            访问级别对象
	 */
	public static void assignAccessPermissionToFolder(ObjectStore os, String granteeName, String folderId,
                                                      AccessType accessType, AccessLevel accessLevel, int inheritableDepth) {
		Folder folder = P8Folder.fetchFolderById(os, folderId);
		assignAccessPermissionToFolder(folder, granteeName, accessType, accessLevel, inheritableDepth);
	}

	/**
	 * @title: assignAccessPermissionToFolder
	 * @date 2014年9月5日
	 * @author Jeffrey
	 * @description: 分配用户权限到指定文件夹上
	 * @param folder
	 *            文件夹对象
	 * @param granteeName
	 *            授权用户或组名称
	 * @param accessType
	 *            访问类型对象
	 * @param accessLevel
	 *            访问级别对象
	 * @param inheritableDepth
	 *            继承深度 详见CEConstants.INHERITABLE_DEPTH... 常量及描述
	 */
	public static void assignAccessPermissionToFolder(Folder folder, String granteeName, AccessType accessType,
                                                      AccessLevel accessLevel, int inheritableDepth) {
		assignAccessPermissionToFolder(folder, granteeName, accessType.getValue(), accessLevel.getValue(),
				inheritableDepth);
	}

	/**
	 * @title: assignAccessPermissionToFolder
	 * @date 2014年9月5日
	 * @author Jeffrey
	 * @description: 分配用户权限到指定文件夹上
	 * @param folder
	 *            文件夹对象
	 * @param granteeName
	 *            授权用户或组名称
	 * @param accessType
	 *            访问类型值
	 * @param accessLevel
	 *            访问级别值
	 * @param inheritableDepth
	 *            继承深度 详见CEConstants.INHERITABLE_DEPTH... 常量及描述
	 */
	public static void assignAccessPermissionToFolder(Folder folder, String granteeName, int accessType,
                                                      int accessLevel, int inheritableDepth) {
		AccessPermissionList apl = folder.get_Permissions();
		AccessPermission ap = Factory.AccessPermission.createInstance();
		ap.set_GranteeName(granteeName);
		ap.set_AccessType(AccessType.getInstanceFromInt(accessType));
		ap.set_AccessMask(accessLevel);
		ap.set_InheritableDepth(inheritableDepth);
		apl.add(ap);
		folder.set_Permissions(apl);
		folder.save(RefreshMode.REFRESH);
	}

	/**
	 * 
	 * @Title: getAccessPermissionByDoc
	 * @Description: 根据文档获取所有的权限信息
	 * @Version 1.0
	 * @Date 2016年3月21日
	 * @Author huangpan
	 * @param doc
	 * @return
	 */
	public static AccessPermissionList getAccessPermissionByDoc(Document doc) {
		AccessPermissionList apl = doc.get_Permissions();
		return apl;
	}

	/**
	 * 0
	 * @Title: ceSecurityInDoc 
	 * @Description: 为CE文档对象添加单个用户(组)权限
	 * @Version 1.0
	 * @Date 2016年6月23日
	 * @Author zhangwenjun
	 * @param doc CE文档对象
	 * @param userId 用户ID或者用户组名
	 * @param isSeeContent 是否可以查看附件
	 * @param isSeeProperties 是否可以查看属性
	 * @return
	 * @throws Exception
	 */
	public static Document ceSecurityInDoc(Document doc, String userId, boolean isSeeContent, boolean isSeeProperties)
			throws Exception {
		AccessPermissionList apl = doc.get_Permissions();
		// RMS权限设置
		// GroupModelImpl groupModel = rmsList.get(j);
		// CE权限设置
		AccessPermission ap = Factory.AccessPermission.createInstance();
		ap.set_GranteeName(userId);
		ap.set_AccessType(AccessType.ALLOW);
		if (isSeeContent) {
			ap.set_AccessMask(AccessLevel.VIEW_AS_INT);
		} else if (isSeeProperties) {
			ap.set_AccessMask(AccessLevel.READ_AS_INT);
		} else {
			return doc;
		}
		apl.add(ap);
		doc.set_Permissions(apl);
		doc.save(RefreshMode.REFRESH);
		return doc;
	}
}
