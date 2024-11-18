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

import com.filenet.api.collection.FolderSet;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.property.PropertyFilter;
import com.filenet.api.util.Id;
import com.zony.common.filenet.vo.FolderVo;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 
 * 
 * @version $Rev: 998 $ $Date: 2012-01-09 15:00:10 +0800 (星期一, 09 一月 2012) $
 */
public class P8Folder {

	private static final Logger logger = LoggerFactory.getLogger(P8Folder.class);

	public static Folder createFolder(ObjectStore os, Folder parent,
                                      String folderName, boolean inherit) {
		Folder folder = Factory.Folder.createInstance(os, null);
		
		if (inherit) {
			folder.set_InheritParentPermissions(true);
		}
		folder.set_Parent(parent);
		
		
		
		folder.set_FolderName(folderName);
		folder.save(RefreshMode.REFRESH);
		return folder;
	}

	public static Folder createFolder(ObjectStore os, String symbolicName,
                                      Folder parent, String folderName, boolean inherit) {
		Folder folder = Factory.Folder.createInstance(os, symbolicName);
		if (inherit) {
			folder.set_InheritParentPermissions(true);
		}
		folder.set_Parent(parent);
		folder.set_FolderName(folderName);
		folder.save(RefreshMode.REFRESH);
		return folder;
	}

	public static Folder createFolder(ObjectStore os, String symbolicName,
                                      Folder parent, String folderName, Map<String, Object> propMap,
                                      boolean inherit) {
		if (propMap == null || propMap.isEmpty())
			throw new RuntimeException("Property map is null or empty.");
		Folder folder = Factory.Folder.createInstance(os, symbolicName);
		folder.set_Parent(parent);
		if (inherit) {
			folder.set_InheritParentPermissions(true);
		}
		folder.set_FolderName(folderName);
		Set<String> propKeySet = propMap.keySet();
		Properties props = folder.getProperties();
		for (String propName : propKeySet) {
			Object obj = propMap.get(propName);
			if (null != obj) {
				props.putObjectValue(propName, propMap.get(propName));
			}
		}
		folder.save(RefreshMode.REFRESH);

		return folder;
	}

	public static Folder createFolderById(ObjectStore os, String parentId,
                                          String folderName, boolean inherit) {
		Folder parent = fetchFolderById(os, parentId, null);
		return createFolder(os, parent, folderName,inherit);
	}

	public static Folder createFolderById(ObjectStore os, String symbolicName,
                                          String parentId, String folderName, boolean inherit) {
		Folder parent = fetchFolderById(os, parentId, null);
		return createFolder(os, symbolicName, parent, folderName,inherit);
	}

	public static Folder createFolderByPath(ObjectStore os, String parentPath,
                                            String folderName, boolean inherit) {
		Folder parent = fetchFolderByPath(os, parentPath, null);
		return createFolder(os, parent, folderName,inherit);
	}

	public static Folder createFolderByPath(ObjectStore os,
                                            String symbolicName, String parentPath, String folderName, boolean inherit) {
		Folder parent = fetchFolderByPath(os, parentPath, null);
		return createFolder(os, symbolicName, parent, folderName,inherit);
	}
	public static Folder createFolderByPath(ObjectStore os,
                                            String symbolicName, String parentPath, String folderName, Map<String, Object> propMap, boolean inherit) {
		Folder parent = fetchFolderByPath(os, parentPath, null);
		return createFolder(os, symbolicName, parent, folderName,propMap,inherit);
	}
	
	public static void createFolderCycle(ObjectStore os, String fullPath){
		String[] pathArray = fullPath.split("/");
		if(pathArray!=null){
			String parentPath = "";
			for(String folderName : pathArray){
				if(!StringUtils.isEmpty(folderName)){
					parentPath = createFolderIfNotExist(os, parentPath, folderName);
				}
			}
		}
	}
	/**
	 * @Title: createFolderIfNotExist 
	 * @Description: 根据目录路径，如果不存在则创建
	 * @param os
	 * @param parentPath
	 * @param folderName
	 * @return
	 */
	public static String createFolderIfNotExist(ObjectStore os, String parentPath, String folderName){
		String path = parentPath + "\\" + folderName;
		Folder folder = fetchFolderByPath(os, path);
		if(folder==null && StringUtils.isEmpty(parentPath)){
			createFolderByPath(os, "\\", folderName);
		}else if (folder==null && !StringUtils.isEmpty(parentPath)){
			createFolderByPath(os, parentPath, folderName);
		}
		return path;
	}
	public static Folder createFolderByPath(final ObjectStore os, final String parentPath, final String folderName) {
		final Folder parent = fetchFolderByPath(os, parentPath, null);
		return createFolder(os, parent, folderName);
	}
	public static Folder createFolder(final ObjectStore os, final Folder parent, final String folderName) {
		final Folder folder = Factory.Folder.createInstance(os, null);
		folder.set_Parent(parent);
		folder.set_FolderName(folderName);
		folder.save(RefreshMode.REFRESH);
		return folder;
	}
	
	public static void updateFolderName(Folder folder, String name) {
		folder.set_FolderName(name);
		folder.save(RefreshMode.NO_REFRESH);
	}
	public static void updateFolder(Folder folder, String name, Map<String, Object> propMap) {
		folder.set_FolderName(name);
		Properties properties = folder.getProperties();
		Iterator<?> mapIt = propMap.keySet().iterator();
		while (mapIt.hasNext()) {// 依次遍历对应key的value
			String key = (String) mapIt.next();
			for (Iterator it = properties.iterator(); it.hasNext();) {
				Property prop = (Property) it.next();
				if(key.equals(prop.getPropertyName())){
					prop.setObjectValue(propMap.get(key));
				}
							
			}
		}		
		folder.save(RefreshMode.NO_REFRESH);
	}
	public static void updateFolderNameByPath(ObjectStore os, String path,
                                              String name) {
		Folder folder = fetchFolderByPath(os, path);
		updateFolderName(folder, name);
	}
	public static void updateFolderByPath(ObjectStore os, String path,
                                          String name, Map<String, Object> propMap) {
		Folder folder = fetchFolderByPath(os, path);
		updateFolder(folder, name, propMap);
	}
	public static void deleteFolder(Folder folder) {
		folder.delete();
		folder.save(RefreshMode.NO_REFRESH);
	}

	public static void deleteFolderById(ObjectStore os, String folderId) {
		Folder folder = fetchFolderById(os, folderId);
		deleteFolder(folder);
	}

	public static void deleteFolderAndSubfolder(ObjectStore os, Folder folder) {
		try {
			Iterator<?> it = folder.get_SubFolders().iterator();
			while (it.hasNext()) {
				Folder subFolder = (Folder) it.next();
				deleteFolderAndSubfolder(os, subFolder);
			}
			deleteFolder(folder);
		} catch (Exception e) {
			System.out.println("删除文件错误！" + e.getMessage());
		}
	}

	public static void deleteFolderAndSubfolderByPath(ObjectStore os,
                                                      String path) {
		Folder folder = fetchFolderByPath(os, path);
		deleteFolderAndSubfolder(os, folder);
	}

	public static Folder fetchFolderById(ObjectStore os, String folderId) {
		return fetchFolderById(os, folderId, null);
	}

	public static Folder fetchFolderById(ObjectStore os, String folderId,
                                         PropertyFilter folderPropFilter) {
		try {
			return Factory.Folder.fetchInstance(os, new Id(folderId),
					folderPropFilter);
		} catch (EngineRuntimeException ex) {
			if (ex.getExceptionCode() == ExceptionCode.E_OBJECT_NOT_FOUND) {
				logger.error("Folder cannot be found " + "by id '" + folderId
						+ "'.", ex);
				return null;
			} else
				throw ex;
		}
	}

	public static Folder fetchFolderByPath(ObjectStore os, String folderPath) {
		return fetchFolderByPath(os, folderPath, null);
	}

	public static Folder fetchFolderByPath(ObjectStore os, String folderPath,
                                           PropertyFilter folderPropFilter) {
		try {
			return Factory.Folder.fetchInstance(os, folderPath,
					folderPropFilter);
		} catch (EngineRuntimeException ex) {
			if (ex.getExceptionCode() == ExceptionCode.E_OBJECT_NOT_FOUND) {
				logger.error("Folder cannot be found " + "by path '"
						+ folderPath + "'.");
				return null;
			} else
				throw ex;
		}
	}

	public static Folder getRootFolder(ObjectStore os) {
		return os.get_RootFolder();
	}

	public static boolean hasSubfolders(Folder folder) {
		FolderSet folderSet = folder.get_SubFolders();
		if (null == folderSet || folderSet.isEmpty())
			return false;
		else
			return true;
	}

	public static boolean hasSubfoldersById(ObjectStore os, String parentId) {
		Folder parent = fetchFolderById(os, parentId);
		return hasSubfolders(parent);
	}

	public static boolean hasSubfoldersByPath(ObjectStore os, String parentPath) {
		Folder parent = fetchFolderByPath(os, parentPath);
		return hasSubfolders(parent);
	}

	public static List<Folder> listSubfolders(Folder parent) {
		List<Folder> folderList = new ArrayList<Folder>();

		FolderSet folderSet = parent.get_SubFolders();
		@SuppressWarnings("unchecked")
		Iterator<Folder> it = folderSet.iterator();
		while (it.hasNext()) {
			folderList.add(it.next());
		}

		return folderList;
	}

	public static List<Folder> listSubfolders(ObjectStore os) {
		return listSubfolders(getRootFolder(os));
	}

	public static List<Folder> listSubfoldersById(ObjectStore os,
                                                  String parentId) {
		Folder parent = fetchFolderById(os, parentId);
		return listSubfolders(parent);
	}

	public static List<Folder> listSubfoldersByPath(ObjectStore os,
                                                    String parentPath) {
		Folder parent = fetchFolderByPath(os, parentPath);
		return listSubfolders(parent);
	}

	public static List<Folder> searchForFolders(ObjectStore os,
                                                String symbolicClassName, boolean includeSubclasses,
                                                String whereClause) {
		P8Search search = new P8Search();
		return search
				.setObjectSql(symbolicClassName, includeSubclasses,
						whereClause, null).setScope(os).setPropertyFilter()
				.fetchFolders();
	}

	public static List<Folder> searchForFolders(ObjectStore os,
                                                String symbolicClassName, String whereClause) {
		P8Search search = new P8Search();
		return search.setObjectSql(symbolicClassName, false, whereClause, null)
				.setScope(os).setPropertyFilter().fetchFolders();
	}

	/**
	 * 获知指定文件夹路径下的所有文件夹含子文件夹对象（递归查找子文件夹对象） <br/>
	 * Method listSubFolders Createby [Jeff] at 2016年3月1日 下午5:51:41
	 * 
	 * @see
	 * @param os
	 *            存储库对象
	 * @param parentFolderPath
	 *            文件夹路径
	 * @return 文件夹对象集合
	 */
	public static List<FolderVo> listSubFolders(ObjectStore os,
                                                String parentFolderPath) {
		List<FolderVo> listFolderVo = new ArrayList<FolderVo>();
		FolderVo folderVo = new FolderVo();
		Folder folder = fetchFolderByPath(os, parentFolderPath);
		folderVo.setId(folder.get_Id().toString());
		folderVo.setParent("#");
		folderVo.setFolderPath(folder.get_PathName());
		folderVo.setText(folder.get_FolderName());
		folderVo.setSymbolicName(folder.get_ClassDescription()
				.get_SymbolicName());
		
		folderVo.setCreateDate(folder.get_DateCreated());
		//陈军新增2016-05-31 ，增加文件夹规则属性获取
		
		Properties properties = folder.getProperties();
	
		/*for (Iterator it = properties.iterator(); it.hasNext();) {
			Property prop = (Property) it.next();
			if(prop.getPropertyName().equals(ZonyConstant.FOLDERPROP_FSYS_RULE))
				folderVo.setFolderRule(prop.getObjectValue()==null?"":prop.getObjectValue().toString());
			//WXZ新增2016-08-24 ，增加文件夹顺序属性获取
			if(prop.getPropertyName().equals(ZonyConstant.FOLDERPROP_FSYS_ORDER))
				folderVo.setFolderOrder(prop.getObjectValue()==null?"":prop.getObjectValue().toString());
		}*/

		listFolderVo.add(folderVo);
		FolderSet folderSet = folder.get_SubFolders();
		if (!folderSet.isEmpty()) {
			iterativeFolder(folder, listFolderVo);
		}
		return listFolderVo;
	}

	/**
	 * 获取子文件夹的FolderVo集合 (不递归)
	 * 
	 * @param os
	 *            存款库对象
	 * @param parentFolderPath
	 *            父文件夹
	 * @param isIncParent
	 *            是否含父文件夹
	 * @return 父文件夹及子文件夹集合
	 */
	public static List<FolderVo> listFoldersOfSub(ObjectStore os,
                                                  String parentFolderPath, boolean isIncParent) {
		List<FolderVo> listFolderVo = new ArrayList<FolderVo>();
		FolderVo folderVo = new FolderVo();
		Folder folder = fetchFolderByPath(os, parentFolderPath);
		folderVo.setId(folder.get_Id().toString());
		folderVo.setParent("#");
		folderVo.setFolderPath(folder.get_PathName());
		folderVo.setText(folder.get_FolderName());
		folderVo.setSymbolicName(folder.get_ClassDescription()
				.get_SymbolicName());
		if (isIncParent) {
			listFolderVo.add(folderVo);
		}
		FolderSet folderSet = folder.get_SubFolders();
		if (!folderSet.isEmpty()) {
			Iterator iterator = folder.get_SubFolders().iterator();
			while (iterator.hasNext()) {
				Folder subFolder = (Folder) iterator.next();
				FolderVo folderVoTemp = new FolderVo();
				folderVoTemp.setId(subFolder.get_Id().toString());
				folderVoTemp.setParent(folder.get_Id().toString());
				folderVoTemp.setFolderPath(subFolder.get_PathName());
				folderVoTemp.setText(subFolder.get_FolderName());
				folderVoTemp.setSymbolicName(subFolder.get_ClassDescription()
						.get_SymbolicName());
				listFolderVo.add(folderVoTemp);
			}
		}
		return listFolderVo;
	}

	public static void iterativeFolder(Folder folder,
                                       List<FolderVo> listFolderVo) {
		Iterator iterator = folder.get_SubFolders().iterator();
		while (iterator.hasNext()) {
			Folder subFolder = (Folder) iterator.next();
			FolderVo folderVo = new FolderVo();
			folderVo.setId(subFolder.get_Id().toString());
			folderVo.setParent(folder.get_Id().toString());
			folderVo.setFolderPath(subFolder.get_PathName());
			folderVo.setText(subFolder.get_FolderName());
			folderVo.setSymbolicName(subFolder.get_ClassDescription()
					.get_SymbolicName());
			
			folderVo.setCreateDate(folder.get_DateCreated());
			//陈军新增2016-05-31 ，增加文件夹规则属性获取
			Properties properties = subFolder.getProperties();
			
			/*for (Iterator it = properties.iterator(); it.hasNext();) {
				Property prop = (Property) it.next();
				if(prop.getPropertyName().equals(ZonyConstant.FOLDERPROP_FSYS_RULE))
					
						folderVo.setFolderRule(prop.getObjectValue()==null?"":prop.getObjectValue().toString());		
				//WXZ新增2016-08-24 ，增加文件夹显示顺序属性获取
				if(prop.getPropertyName().equals(ZonyConstant.FOLDERPROP_FSYS_ORDER))
					folderVo.setFolderOrder(prop.getObjectValue()==null?"":prop.getObjectValue().toString());	
			}*/
			
			listFolderVo.add(folderVo);
			FolderSet folderSet = subFolder.get_SubFolders();
			if (!folderSet.isEmpty()) {
				iterativeFolder(subFolder, listFolderVo);
			}
		}
	}

	/**
	 * 获知指定文件夹路径下的所有文件夹含子文件夹对象（不含文件夹本身）（递归查找子文件夹对象） <br/>
	 * Method listSubFolders Createby [Litengyu] at 2016年3月7日 上午10:45:41
	 * 
	 * @see
	 * @param os
	 *            存储库对象
	 * @param parentFolderPath
	 *            文件夹路径
	 * @return 文件夹对象集合
	 */
	public static List<FolderVo> listSubFoldersNoPNode(ObjectStore os,
                                                       String parentFolderPath) {
		List<FolderVo> listFolderVo = new ArrayList<FolderVo>();
		List<Folder> subList = listSubfoldersByPath(os, parentFolderPath);
		for (Folder folder : subList) {
			List<FolderVo> partList = listSubFolders(os, folder.get_PathName());
			listFolderVo.addAll(partList);
		}
		Collections.sort(listFolderVo);
		return listFolderVo;
	}
	
}
