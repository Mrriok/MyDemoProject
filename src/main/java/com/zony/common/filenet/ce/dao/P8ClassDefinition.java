package com.zony.common.filenet.ce.dao;

import com.filenet.api.admin.*;
import com.filenet.api.collection.ClassDefinitionSet;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.PropertyDefinitionList;
import com.filenet.api.constants.PropertySettability;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.exception.EngineRuntimeException;
import com.filenet.api.exception.ExceptionCode;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.zony.common.filenet.util.FnEnumList;
import com.zony.common.filenet.vo.DocumentClassVo;
import com.zony.common.filenet.vo.PropertyDefinitionVo;
import com.zony.common.filenet.util.ZonyStringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @fileName DocumentClass.java
 * @package com.zony.filenet.classdefinition
 * @project foton_tdms
 * @function 关于系统文件类型操作工具类
 * @version 1.0.0
 * @date 2014-7-31
 * @author Jeffrey
 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
 */
public class P8ClassDefinition {
	public static Logger logger = LoggerFactory.getLogger(P8ClassDefinition.class);

	/**
	 * @title: createClassDefinition
	 * @date 2014-8-2
	 * @author Jeffrey
	 * @description: 创建文件类型
	 * @param os
	 *            文件类型存储的OS对象
	 * @param parentSymbolicName
	 *            上级文件类型名称
	 * @param symbolicName
	 *            创建的文件类型名称
	 * @param displayNameMap
	 *            显示的名称对象集合
	 * @return 创建完成的文件类型对象
	 */
	public static ClassDefinition createClassDefinition(ObjectStore os,
                                                        String parentSymbolicName, String symbolicName,
                                                        Map<String, String> displayNameMap) {
		ClassDefinition parent = Factory.ClassDefinition.fetchInstance(os,
				parentSymbolicName, null);
		return createClassDefinition(os, parent, symbolicName, displayNameMap);
	}

	/**
	 * @title: createClassDefinition
	 * @date 2014-8-2
	 * @author Jeffrey
	 * @description: 创建文件类型
	 * @param os
	 *            文件类型存储的OS对象
	 * @param parent
	 *            上级文件类型对象
	 * @param symbolicName
	 *            创建的文件类型名称
	 * @param displayNameMap
	 *            显示的名称对象集合
	 * @return 创建完成的文件类型对象
	 */
	@SuppressWarnings("unchecked")
	public static ClassDefinition createClassDefinition(ObjectStore os,
                                                        ClassDefinition parent, String symbolicName,
                                                        Map<String, String> displayNameMap) {
		ClassDefinition def = parent.createSubclass();
		try {
			def.set_SymbolicName(symbolicName);
			def.set_DisplayNames(Factory.LocalizedString.createList());
			Set<String> keySet = displayNameMap.keySet();
			for (String key : keySet) {
				LocalizedString displayName = Factory.LocalizedString
						.createInstance();
				displayName.set_LocaleName(key);
				displayName.set_LocalizedText(displayNameMap.get(key));
				def.get_DisplayNames().add(displayName);
			}

			def.save(RefreshMode.REFRESH);
		} catch (Exception e) {
			System.out.println("error:" + e.getMessage() + ":" + e.getCause());
			e.printStackTrace();
		}
		return def;
	}

	/**
	 * @title: addPropertyDefinitionsToClassDefinition
	 * @date 2014-8-2
	 * @author Jeffrey
	 * @description: 添加属性定义到指定文件类型上
	 * @param os
	 *            文件类型所在的存储库OS对象
	 * @param classDefinition
	 *            文件类型对象
	 * @param propTemplateIdList
	 *            属性模板对象集合
	 */
	public static void addPropertyDefinitionsToClassDefinition(ObjectStore os,
                                                               ClassDefinition classDefinition,
                                                               List<PropertyTemplate> propTemplateIdList) {
		PropertyDefinitionList propDefList = classDefinition
				.get_PropertyDefinitions();
		for (PropertyTemplate propTemplate : propTemplateIdList) {
			PropertyDefinition newPropDef = (PropertyDefinition) propTemplate
					.createClassProperty();
			newPropDef.set_Settability(PropertySettability.READ_WRITE);
			propDefList.add(newPropDef);
		}
		
		classDefinition.save(RefreshMode.REFRESH);
	}

	/**
	 * @title: addPropertyDefinitionsToClassDefinitionBySymbolicName
	 * @date 2014-8-2
	 * @author Jeffrey
	 * @description: 添加属性定义到指定文件类型上
	 * @param os
	 *            文件类型所在的存储库OS对象
	 * @param classDefinition
	 *            文件类型对象
	 * @param propTemplateSymbolicNameList
	 *            属性模板的SymbolicName名称集合
	 */
	public static void addPropertyDefinitionsToClassDefinitionBySymbolicName(
            ObjectStore os, ClassDefinition classDefinition,
            List<String> propTemplateSymbolicNameList) {
		PropertyDefinitionList propDefList = classDefinition
				.get_PropertyDefinitions();
		List<String> propSymblicNameList = new ArrayList<String>();
		Iterator<PropertyDefinition> it = propDefList.iterator();
		PropertyDefinition pdf = null;
		while (it.hasNext()) {
			pdf = it.next();
			propSymblicNameList.add(pdf.get_SymbolicName());
			System.out.println("SymbolicName：" + pdf.get_SymbolicName());
		}
		PropertyTemplate propTemplate = null;
		List<PropertyTemplate> propTemplateList = null;
		PropertyDefinition newPropDef = null;
		for (String symbolicName : propTemplateSymbolicNameList) {

			propTemplateList = P8PropertyTemplate.fetchPropertyTemplates(os,
					symbolicName);
			if (propTemplateList == null || propTemplateList.size() > 0) {
				propTemplate = propTemplateList.get(0);
				newPropDef = propTemplate.createClassProperty();
				newPropDef.set_Settability(PropertySettability.READ_WRITE);
				if (!isClassHasThisProperty(propSymblicNameList, newPropDef)) {
					propDefList.add(newPropDef);
				}
			}
		}
		classDefinition.save(RefreshMode.REFRESH);
	}

	private static boolean isClassHasThisProperty(List<String> propDefList,
			PropertyDefinition newPropDef) {
		boolean bHas = false;
		String propDef = null;
		if (propDefList != null && propDefList.size() > 0) {
			Iterator<String> it = propDefList.iterator();
			while (it.hasNext()) {
				propDef = it.next();
				if (propDef.equals(newPropDef.get_PropertyTemplate()
						.get_SymbolicName())) {
					bHas = true;
					break;
				}
			}
		}
		return bHas;
	}

	/**
	 * @title: deleteClassDefinition
	 * @date 2014-8-2
	 * @author Jeffrey
	 * @description: 删除文件类型，根据文件类型的唯一标识名称——symbolicName
	 * @param os
	 *            文件类型所在的存储库
	 * @param symbolicName
	 *            文件类型名称
	 */
	public static void deleteClassDefinition(ObjectStore os, String symbolicName) {
		ClassDefinition classDefinition = Factory.ClassDefinition
				.fetchInstance(os, symbolicName, null);
		deleteClassDefinition(os, classDefinition);
	}

	/**
	 * @title: deleteClassDefinition
	 * @date 2014-8-2
	 * @author Jeffrey
	 * @description: 删除文件类型，根据文件类型对象
	 * @param os
	 *            文件类型所在的存储库
	 * @param classDefinition
	 *            文件类型对象
	 * @param symbolicName
	 *            文件类型名称
	 */
	public static void deleteClassDefinition(ObjectStore os,
                                             ClassDefinition classDefinition) {
		classDefinition.delete();
		classDefinition.save(RefreshMode.REFRESH);
	}

	public static ClassDefinitionSet getImmediateSubclassDefinitions(
            ObjectStore os, String symbolicName) {
		ClassDefinition def = Factory.ClassDefinition.fetchInstance(os,
				symbolicName, null);
		return def.get_ImmediateSubclassDefinitions();
	}

	/**
	 * 根据文档类型获取自定义属性对象集合 <br/>
	 * Method getPropertyDefinitionList Createby [Jeff] at 2016年3月1日 下午12:10:51
	 * 
	 * @see
	 * @param os
	 *            存储库对象
	 * @param symbolicName
	 *            文档类型标识
	 * @param type
	 *            扩展属性类型 1：界面只能读，2,界面能读写 文档类型标识
	 * @return 自定义属性列表集合
	 */
	private static List<PropertyDefinitionVo> getPropertyDefinitionList(
            ObjectStore os, String symbolicName) {
		ClassDefinition clzDef = Factory.ClassDefinition.fetchInstance(os,
				symbolicName, null);
		PropertyDefinitionList proList = clzDef.get_PropertyDefinitions();
		Iterator proIteratror = proList.iterator();
		List<PropertyDefinitionVo> proDefinitionList = null;
		while (proIteratror.hasNext()) {
			if (null == proDefinitionList) {
				proDefinitionList = new ArrayList<PropertyDefinitionVo>();
			}
			PropertyDefinition propDef = (PropertyDefinition) proIteratror
					.next();
			PropertyDefinitionVo propertyDefinitionVo = new PropertyDefinitionVo();
			if (!propDef.get_IsHidden() && !propDef.get_IsSystemOwned()
					&& propDef.get_Settability().getValue() == 0) {
				// chenjun 2016-03-10 DocumentTitle 字段不作为扩展属性
				if (!"DocumentTitle".equals(propDef.get_SymbolicName())) {
					if (propDef.get_SymbolicName().startsWith("RW_")) {
						propertyDefinitionVo.setUiType(2);
					} else if (propDef.get_SymbolicName().startsWith("R_")) {
						// 1:不显示，2显示可编辑，3显示不可编辑
						propertyDefinitionVo.setUiType(3);
					} else {
						propertyDefinitionVo.setUiType(1);
					}
					propertyDefinitionVo.setSymbolicName(propDef
							.get_SymbolicName());
					propertyDefinitionVo.setName(propDef.get_Name());
					propertyDefinitionVo.setDisplayName(propDef
							.get_DisplayName());
					propertyDefinitionVo.setHidden(propDef.get_IsHidden());
					propertyDefinitionVo.setCardinalityType(propDef
							.get_Cardinality().getValue());
					propertyDefinitionVo.setDataType(propDef.get_DataType()
							.getValue());
					// 获取属性定义的默认值
					Properties properties = propDef.getProperties();
					try {
						Property property = properties
								.get("PropertyDefaultString");
						if (null != property
								&& property.getObjectValue() instanceof String) {
							propertyDefinitionVo.setDefaultValue(property
									.getStringValue());
						}
					} catch (Exception e) {
						logger.debug("当前类" + symbolicName
								+ "下字段默认值为非String类型，忽略继续任务！属性定义："
								+ propertyDefinitionVo.toString());
					}
					if (propertyDefinitionVo.getDataType() == FnEnumList.PropertyEnum.STRING
							.valueOf()) {
						propertyDefinitionVo.setLength(propDef.getProperties()
								.getInteger32Value("MaximumLengthString"));
					}
					if (null != propDef.get_ChoiceList()) {
						propertyDefinitionVo.setChoiceList(true);
						propertyDefinitionVo.setChoiceListName(propDef
								.get_ChoiceList().get_Name());
					}
					propertyDefinitionVo.setValueRequired(propDef
							.get_IsValueRequired());
					proDefinitionList.add(propertyDefinitionVo);
				}

			}
		}
		return proDefinitionList;
	}

	/**
	 * @title: hasPropertyDefinition
	 * @date 2014年11月28日
	 * @author Jeffrey
	 * @description: 检索当前文件类型定义中是否包含指定属性的定义
	 * @param os
	 *            存储库
	 * @param clzSymbolicName
	 *            文件类型唯一标识
	 * @param propSymbolicName
	 *            属性定义唯一标识
	 * @return 是否存在
	 */
	public static boolean hasPropertyDefinition(ObjectStore os,
                                                String clzSymbolicName, String propSymbolicName) {
		boolean flag = false;

		ClassDefinition clzDef = Factory.ClassDefinition.fetchInstance(os,
				clzSymbolicName, null);
		PropertyDefinitionList propDefList = clzDef.get_PropertyDefinitions();
		for (int i = 0; i < propDefList.size(); i++) {
			PropertyDefinition propDef = (PropertyDefinition) propDefList
					.get(i);
			if (ZonyStringUtil.equals(propDef.get_SymbolicName(),
					propSymbolicName)) {
				flag = true;
			}
		}
		return flag;
	}

	/**
	 * @title: proDefinitionDefaultValueInClass
	 * @date 2014年11月28日
	 * @author Jeffrey
	 * @description: 查询获取指定文件类型定义中的指定属性定义默认值
	 * @param os
	 *            存储库
	 * @param symbolicName
	 *            文件类型唯一标识
	 * @param proName
	 *            属性定义名称唯一标识
	 * @return 属性定义默认值
	 * @throws Exception
	 *             异常信息
	 */
	public static String getProDefinitionDefaultValueInClass(ObjectStore os,
                                                             String symbolicName, String proName) throws Exception {
		try {
			ClassDefinition classDef = P8ClassDefinition.fetchClassDefinition(
					os, symbolicName);
			PropertyDefinitionList proDefList = classDef
					.get_PropertyDefinitions();
			for (int i = 0; i < proDefList.size(); i++) {
				PropertyDefinition propertyDef = (PropertyDefinition) proDefList
						.get(i);
				if (propertyDef.get_SymbolicName().equals(proName)) {
					Properties properties = propertyDef.getProperties();
					Property property = properties.get("PropertyDefaultString");
					if (null != property
							&& property.getObjectValue() instanceof String) {
						return property.getStringValue();
					}
				}
			}
		} catch (Exception e) {
			throw new Exception("查询获取指定文件类型定义中的指定属性定义默认值发生异常，异常异常信息："
					+ e.getMessage(), e);
		}
		return null;
	}

	/**
	 * @title: setProDefinitionDefaultValueInClass
	 * @date 2014年11月28日
	 * @author Jeffrey
	 * @description: 设置更新指定文件类型定义中的指定属性定义默认值
	 * @param os
	 *            存储库
	 * @param symbolicName
	 *            文件类型唯一标识
	 * @param proName
	 *            属性定义名称唯一标识
	 * @param defaultValue
	 *            属性定义的默认值
	 * @throws Exception
	 *             异常信息
	 */
	public static void setProDefinitionDefaultValueInClass(ObjectStore os,
                                                           String symbolicName, String proName, String defaultValue)
			throws Exception {
		try {
			ClassDefinition classDef = P8ClassDefinition.fetchClassDefinition(
					os, symbolicName);
			PropertyDefinitionList proDefList = classDef
					.get_PropertyDefinitions();
			for (int i = 0; i < proDefList.size(); i++) {
				PropertyDefinition propertyDef = (PropertyDefinition) proDefList
						.get(i);
				if (propertyDef.get_SymbolicName().equals(proName)) {
					Properties properties = propertyDef.getProperties();
					properties.putValue("PropertyDefaultString", defaultValue);
				}
			}
			classDef.set_PropertyDefinitions(proDefList);
			classDef.save(RefreshMode.REFRESH);
		} catch (Exception e) {
			throw new Exception("设置更新指定文件类型定义中的指定属性定义默认值发生异常，异常异常信息："
					+ e.getMessage(), e);
		}
	}

	/**
	 * @title: fetchClassDefinition
	 * @date 2014-8-2
	 * @author Jeffrey
	 * @description: 获取文件类型对象，根据文件类型的标识名称——symbolicName
	 * @param os
	 *            文件所在的存储库OS对象
	 * @param symbolicName
	 *            文件类型标识名称
	 * @return 文件类型对象
	 */
	public static ClassDefinition fetchClassDefinition(ObjectStore os,
                                                       String symbolicName) {
		try {
			if (os == null) {
				throw new NullPointerException(
						"ObjectStore is null when get ClassDefinition of "
								+ symbolicName);
			}
			return Factory.ClassDefinition
					.fetchInstance(os, symbolicName, null);
		} catch (EngineRuntimeException e) {
			if (e.getExceptionCode() == ExceptionCode.E_BAD_CLASSID) {
				logger.warn(os.get_Name() + " : " + e.getMessage());
				return null;
			} else {
				throw e;
			}
		}
	}

	/**
	 * @title: listDocumentClasses
	 * @date 2014年8月8日
	 * @author Jeffrey
	 * @description: 获取内容库中所有的文档类型定义
	 * @param os
	 *            文件所在的存储库OS对象
	 * @return 文档类型定义集合
	 */
	public static List<ClassDefinition> listDocumentClasses(ObjectStore os) {
		List<ClassDefinition> defList = new ArrayList<ClassDefinition>();
		String queryString = "SELECT * FROM DocumentClassDefinition WHERE [IsHidden] = FALSE AND [AllowsInstances] = TRUE";
		SearchSQL searchSQL = new SearchSQL(queryString);
		SearchScope searchScope = new SearchScope(os);
		IndependentObjectSet set = searchScope.fetchObjects(searchSQL, null,
				null, false);
		@SuppressWarnings("unchecked")
		Iterator<DocumentClassDefinition> iter = set.iterator();
		while (iter.hasNext()) {
			defList.add(iter.next());
		}
		return defList;
	}

	/**
	 * @title: getDocumentClass
	 * @date 2014年8月29日
	 * @author Jeffrey
	 * @description: 获取单个节点，不带属性对象
	 * @param os
	 *            文件存储对象
	 * @param symbolicName
	 *            文件分类标识名称
	 * @return 文件分类对象
	 */
	public static DocumentClassVo getDocumentClass(ObjectStore os,
                                                   String symbolicName) {
		List<DocumentClassVo> docClassVoList = new ArrayList<DocumentClassVo>();
		DocumentClassVo docClassVo = new DocumentClassVo();
		ClassDefinition classDefinition = fetchClassDefinition(os, symbolicName);
		docClassVo.setId(classDefinition.get_Id().toString());
		docClassVo.setSymbolicName(classDefinition.get_SymbolicName());
		docClassVo.setText(classDefinition.get_DisplayName());
		docClassVo.setParent("#");
		docClassVo.setSubNood(false);
		return docClassVo;
	}

	/**
	 * @title: getDocumentClass
	 * @date 2014年8月29日
	 * @author Jeffrey
	 * @description: 获取文件分类及下属分类集合
	 * @param os
	 *            文件存储对象
	 * @param symbolicName
	 *            文件分类标识名称
	 * @param isLoadProDefinition
	 *            是否加载属性
	 * @return 文件分类对象集合
	 */
	public static List<DocumentClassVo> getDocumentClass(ObjectStore os,
                                                         String symbolicName, boolean isLoadProDefinition) {
		List<DocumentClassVo> docClassVoList = new ArrayList<DocumentClassVo>();
		DocumentClassVo docClassVo = new DocumentClassVo();
		ClassDefinition classDefinition = fetchClassDefinition(os, symbolicName);
		docClassVo.setId(classDefinition.get_Id().toString());
		docClassVo.setSymbolicName(classDefinition.get_SymbolicName());
		docClassVo.setText(classDefinition.get_DisplayName());
		docClassVo.setName(classDefinition.get_DisplayName());
		docClassVo.setParent("#");
		if (isLoadProDefinition) {

			List<PropertyDefinitionVo> propertyDefinitionVoList = buildPropertyDefinitionVo(classDefinition
					.get_PropertyDefinitions());
			docClassVo.setPropTemplateIdList(propertyDefinitionVoList);
		}
		ClassDefinitionSet subSet = classDefinition
				.get_ImmediateSubclassDefinitions();
		if (!subSet.isEmpty()) {
			docClassVo.setSubNood(false);
			docClassVoList.add(docClassVo);
			iterativeDocumentClass(classDefinition, docClassVoList,
					isLoadProDefinition);
		} else {
			docClassVo.setSubNood(true);
			docClassVoList.add(docClassVo);
		}
		return docClassVoList;
	}

	public static List<DocumentClassVo> getDocumentClass(ObjectStore os,
                                                         String symbolicName, String prentId, boolean isLoadProDefinition) {
		List<DocumentClassVo> docClassVoList = new ArrayList<DocumentClassVo>();
		DocumentClassVo docClassVo = new DocumentClassVo();
		ClassDefinition classDefinition = fetchClassDefinition(os, symbolicName);
		docClassVo.setId(classDefinition.get_Id().toString());
		docClassVo.setSymbolicName(classDefinition.get_SymbolicName());
		docClassVo.setText(classDefinition.get_DisplayName());
		docClassVo.setName(classDefinition.get_DisplayName());
		docClassVo.setParent(prentId);
		docClassVo.setpId(prentId);
		if (isLoadProDefinition) {

			List<PropertyDefinitionVo> propertyDefinitionVoList = buildPropertyDefinitionVo(classDefinition
					.get_PropertyDefinitions());
			docClassVo.setPropTemplateIdList(propertyDefinitionVoList);
		}
		ClassDefinitionSet subSet = classDefinition
				.get_ImmediateSubclassDefinitions();
		if (!subSet.isEmpty()) {
			docClassVo.setSubNood(false);
			docClassVoList.add(docClassVo);
			iterativeDocumentClass(classDefinition, docClassVoList,
					isLoadProDefinition);
		} else {
			docClassVo.setSubNood(true);
			docClassVoList.add(docClassVo);
		}
		return docClassVoList;
	}
	/**
	 * @title: getDocumentClass
	 * @date 2014年8月29日
	 * @author Jeffrey
	 * @description: 获取文件分类下属(不包含分类)分类集合
	 * @param os
	 *            文件存储对象
	 * @param symbolicName
	 *            文件分类标识名称
	 * @param isLoadProDefinition
	 *            是否加载属性
	 * @return 文件分类对象集合
	 */
	public static List<DocumentClassVo> getDocumentClassWithoutTop(ObjectStore os,
                                                                   String symbolicName, boolean isLoadProDefinition) {
		List<DocumentClassVo> docClassVoList = new ArrayList<DocumentClassVo>();
		DocumentClassVo docClassVo = new DocumentClassVo();
		ClassDefinition classDefinition = fetchClassDefinition(os, symbolicName);
		docClassVo.setId(classDefinition.get_Id().toString());
		docClassVo.setSymbolicName(classDefinition.get_SymbolicName());
		docClassVo.setText(classDefinition.get_DisplayName());
		docClassVo.setParent("#");
		if (isLoadProDefinition) {

			List<PropertyDefinitionVo> propertyDefinitionVoList = buildPropertyDefinitionVo(classDefinition
					.get_PropertyDefinitions());
			docClassVo.setPropTemplateIdList(propertyDefinitionVoList);
		}
		ClassDefinitionSet subSet = classDefinition
				.get_ImmediateSubclassDefinitions();
		if (!subSet.isEmpty()) {
			docClassVo.setSubNood(false);			
			iterativeDocumentClass(classDefinition, docClassVoList,
					isLoadProDefinition);
		} else {
			docClassVo.setSubNood(true);
			docClassVoList.add(docClassVo);
		}
		return docClassVoList;
	}
	private static void iterativeDocumentClass(ClassDefinition o,
                                               List<DocumentClassVo> docClassVoList, boolean isLoadProDefinition) {
		ClassDefinitionSet subSet = o.get_ImmediateSubclassDefinitions();
		Iterator itt = subSet.iterator();
		while (itt.hasNext()) {
			DocumentClassVo docClassVo = new DocumentClassVo();
			ClassDefinition oo = (ClassDefinition) itt.next();
			if (!oo.get_IsHidden().booleanValue()
					&& !oo.get_IsSystemOwned().booleanValue()) {
				docClassVo.setId(oo.get_Id().toString());
				docClassVo.setParent(o.get_Id().toString());
				docClassVo.setpId(o.get_Id().toString());
				docClassVo.setSymbolicName(oo.get_SymbolicName());
				docClassVo.setText(oo.get_DisplayName());
				docClassVo.setName(oo.get_DisplayName());
				if (isLoadProDefinition) {
					PropertyDefinitionList proList = oo
							.get_PropertyDefinitions();

					docClassVo
							.setPropTemplateIdList(buildPropertyDefinitionVo(proList));
				}
				ClassDefinitionSet ssubSet = oo
						.get_ImmediateSubclassDefinitions();
				if (!ssubSet.isEmpty()) {
					docClassVo.setSubNood(false);
					docClassVoList.add(docClassVo);
					iterativeDocumentClass(oo, docClassVoList,
							isLoadProDefinition);
				} else {
					docClassVo.setSubNood(true);
					docClassVoList.add(docClassVo);
				}
			}
		}
	}

	/**
	 * 构建统一的PropertyDefinitionVo 对象 <br/>
	 * Method buildPropertyDefinitionVo Createby [Jeff] at 2016年3月15日 下午2:42:33
	 * 
	 * @see
	 * @param proList
	 *            CE产品层面对象
	 * 
	 * @return PropertyDefinitionVo 属性定义集合
	 * 
	 * 
	 */
	private static List<PropertyDefinitionVo> buildPropertyDefinitionVo(
			PropertyDefinitionList proList) {
		Iterator<?> proIteratror = proList.iterator();
		List<PropertyDefinitionVo> proDefinitionList = null;
		List<PropertyDefinitionVo> sysDefinitionList = new ArrayList<PropertyDefinitionVo>();
		while (proIteratror.hasNext()) {
			if (null == proDefinitionList) {
				proDefinitionList = new ArrayList<PropertyDefinitionVo>();
			}
			PropertyDefinition propDef = (PropertyDefinition) proIteratror
					.next();
			PropertyDefinitionVo propertyDefinitionVo = new PropertyDefinitionVo();

			

			if ((!propDef.get_IsHidden() && !propDef.get_IsSystemOwned() && propDef
					.get_Settability().getValue() == 0) ) {
				if (!"DocumentTitle".equals(propDef.get_SymbolicName())) {
					if (propDef.get_SymbolicName().startsWith("RW_")) {
						propertyDefinitionVo.setUiType(2);
					} else if (propDef.get_SymbolicName().startsWith("R_")) {
						// 1:不显示，2显示可编辑，3显示不可编辑
						propertyDefinitionVo.setUiType(3);
					} else {
						propertyDefinitionVo.setUiType(1);
					}
					
					if("RW_SYS_EditDate".equals(propDef.get_SymbolicName())||"DateCreated".equals(propDef.get_SymbolicName())||"RW_SYS_ProduceTime".equals(propDef.get_SymbolicName())
							||"RW_SYS_StartTime".equals(propDef.get_SymbolicName())||"RW_SYS_EndTime".equals(propDef.get_SymbolicName())||"RW_SYS_PublishTime".equals(propDef.get_SymbolicName())
							||"RW_SYS_ExecuteTime".equals(propDef.get_SymbolicName())||"RW_SYS_ArchiveTime".equals(propDef.get_SymbolicName())||"RW_SYS_ReorganizeTime".equals(propDef.get_SymbolicName())){
						propertyDefinitionVo.setDateType(true);
					}else{
						propertyDefinitionVo.setDateType(false);
					}
					propertyDefinitionVo.setSymbolicName(propDef
							.get_SymbolicName());
					propertyDefinitionVo.setName(propDef.get_Name());
					propertyDefinitionVo.setDisplayName(propDef
							.get_DisplayName());
					propertyDefinitionVo.setHidden(propDef.get_IsHidden());
					propertyDefinitionVo.setCardinalityType(propDef
							.get_Cardinality().getValue());
					propertyDefinitionVo.setDataType(propDef.get_DataType()
							.getValue());
					// 获取属性定义的默认值
					Properties properties = propDef.getProperties();
					try {
						Property property = properties
								.get("PropertyDefaultString");
						if (null != property
								&& property.getObjectValue() instanceof String) {
							propertyDefinitionVo.setDefaultValue(property
									.getStringValue());
						}
					} catch (Exception e) {
						logger.debug("当前类下字段默认值为非String类型，忽略继续任务！属性定义："
								+ propertyDefinitionVo.toString());
					}
					if (propertyDefinitionVo.getDataType() == FnEnumList.PropertyEnum.STRING
							.valueOf()) {
						propertyDefinitionVo.setLength(propDef.getProperties()
								.getInteger32Value("MaximumLengthString"));
					}
					if (null != propDef.get_ChoiceList()) {
						propertyDefinitionVo.setChoiceList(true);
						propertyDefinitionVo.setChoiceListName(propDef
								.get_ChoiceList().get_Name());
					}
					propertyDefinitionVo.setValueRequired(propDef
							.get_IsValueRequired());
					
						proDefinitionList.add(propertyDefinitionVo);
					

				}
			}
		}
		proDefinitionList.addAll(sysDefinitionList);
		return proDefinitionList;
	}

	/**
	 * @title: getDocumentClasses
	 * @date 2014年8月8日
	 * @author Jeffrey
	 * @description: 获取指定文档类型下的所有分类信息
	 * @param os
	 *            文件所在的存储库OS对象
	 * @param symbolicName
	 *            文档类型SymbolicName
	 * @param isLoadProDefinition
	 *            是否加载自定义属性
	 * @return 文档类型定义对象
	 */
	public static DocumentClassVo getDocumentClasses(ObjectStore os,
                                                     String symbolicName, boolean isLoadProDefinition) {
		DocumentClassVo root = new DocumentClassVo();
		ClassDefinition od = fetchClassDefinition(os, symbolicName);
		root.setSymbolicName(od.get_SymbolicName());
		root.setText(od.get_DisplayName());
		if (isLoadProDefinition) {

			List<PropertyDefinitionVo> propertyDefinitionVoList = buildPropertyDefinitionVo(od
					.get_PropertyDefinitions());
			root.setPropTemplateIdList(propertyDefinitionVoList);
		}
		iterativeDocumentClass(od, root, isLoadProDefinition);
		if (null == root.getChildren() || root.getChildren().size() < 1) {
			root.setSubNood(true);
		}
		return root;
	}

	private static void iterativeDocumentClass(ClassDefinition o,
                                               DocumentClassVo bean, boolean isLoadProDefinition) {
		ClassDefinitionSet subSet = o.get_ImmediateSubclassDefinitions();
		Iterator itt = subSet.iterator();
		while (itt.hasNext()) {
			ClassDefinition oo = (ClassDefinition) itt.next();
			if (!oo.get_IsHidden().booleanValue()
					&& !oo.get_IsSystemOwned().booleanValue()) {
				DocumentClassVo child = new DocumentClassVo();
				child.setSymbolicName(oo.get_SymbolicName());
				child.setText(oo.get_DisplayName());
				if (isLoadProDefinition) {

					List<PropertyDefinitionVo> propertyDefinitionVoList = buildPropertyDefinitionVo(oo
							.get_PropertyDefinitions());
					child.setPropTemplateIdList(propertyDefinitionVoList);
				}
				if (bean.getChildren() == null) {
					List<DocumentClassVo> children = new ArrayList<DocumentClassVo>();
					children.add(child);
					bean.setChildren(children);
				} else {
					bean.getChildren().add(child);
				}
				iterativeDocumentClass(oo, child, isLoadProDefinition);
				if (null == child.getChildren()
						|| child.getChildren().size() < 1) {
					child.setSubNood(true);
				}
			}
		}
	}

	/**
	 * 
	 * @Title: removePropertyDefinitionsToClassDefinitionBySymbolicName
	 * @Description: 删除指定文档类型的属性
	 * @Version 1.0
	 * @Date 2016年4月12日
	 * @Author huangpan
	 * @param os
	 * @param classDefinition
	 * @param propTemplateSymbolicNameList
	 *            删除的属性的 SymbolicName
	 */
	public static void removePropertyDefinitionsToClassDefinitionBySymbolicName(
            ObjectStore os, ClassDefinition classDefinition,
            List<String> propTemplateSymbolicNameList) {
		PropertyDefinitionList propDefList = classDefinition
				.get_PropertyDefinitions();
		Iterator<PropertyDefinition> it = propDefList.iterator();
		PropertyDefinition pdf = null;
		List<PropertyDefinition> removeList = new ArrayList<PropertyDefinition>();
		while (it.hasNext()) {
			pdf = it.next();
			if (propTemplateSymbolicNameList.contains(pdf.get_SymbolicName())) {
				removeList.add(pdf);
				logger.info("remove SymbolicName：" + pdf.get_SymbolicName());
			}
		}
		propDefList.removeAll(removeList);
		classDefinition.save(RefreshMode.REFRESH);
	}

	/**
	 * 
	 * @Title: getPropertyDefinitionMap
	 * @Description:根据文档类型返回元数据
	 * @Version 1.0
	 * @Date 2016年4月22日
	 * @Author huangpan
	 * @param os
	 * @param symbolicName
	 * @return
	 */
	private static List<PropertyDefinitionVo> getPropertyDefinitionMap(
            ObjectStore os, String symbolicName) {
		ClassDefinition clzDef = Factory.ClassDefinition.fetchInstance(os,
				symbolicName, null);
		PropertyDefinitionList proList = clzDef.get_PropertyDefinitions();
		Iterator proIteratror = proList.iterator();
		List<PropertyDefinitionVo> proDefinitionList = null;
		List<PropertyDefinitionVo> proDefinitionList2 = null;
		while (proIteratror.hasNext()) {
			if (null == proDefinitionList) {
				proDefinitionList = new ArrayList<PropertyDefinitionVo>();
				proDefinitionList2 = new ArrayList<PropertyDefinitionVo>();
			}
			PropertyDefinition propDef = (PropertyDefinition) proIteratror
					.next();
			PropertyDefinitionVo propertyDefinitionVo = new PropertyDefinitionVo();
			
				if (!"DocumentTitle".equals(propDef.get_SymbolicName())) {
					if (propDef.get_SymbolicName().startsWith("RW_")) {
						propertyDefinitionVo.setUiType(2);
					} else if (propDef.get_SymbolicName().startsWith("R_")) {
						// 1:不显示，2显示可编辑，3显示不可编辑
						propertyDefinitionVo.setUiType(3);
					} else {
						propertyDefinitionVo.setUiType(1);
					}
					propertyDefinitionVo.setSymbolicName(propDef
							.get_SymbolicName());
					propertyDefinitionVo.setName(propDef.get_Name());
					propertyDefinitionVo.setDisplayName(propDef
							.get_DisplayName());
					propertyDefinitionVo.setHidden(propDef.get_IsHidden());
					propertyDefinitionVo.setCardinalityType(propDef
							.get_Cardinality().getValue());
					propertyDefinitionVo.setDataType(propDef.get_DataType()
							.getValue());
					// 获取属性定义的默认值
					Properties properties = propDef.getProperties();
					try {
						Property property = properties
								.get("PropertyDefaultString");
						if (null != property
								&& property.getObjectValue() instanceof String) {
							propertyDefinitionVo.setDefaultValue(property
									.getStringValue());
						}
					} catch (Exception e) {
						logger.debug("当前类" + symbolicName
								+ "下字段默认值为非String类型，忽略继续任务！属性定义："
								+ propertyDefinitionVo.toString());
					}
					if (propertyDefinitionVo.getDataType() == FnEnumList.PropertyEnum.STRING
							.valueOf()) {
						propertyDefinitionVo.setLength(propDef.getProperties()
								.getInteger32Value("MaximumLengthString"));
					}
					if (null != propDef.get_ChoiceList()) {
						propertyDefinitionVo.setChoiceList(true);
						propertyDefinitionVo.setChoiceListName(propDef
								.get_ChoiceList().get_Name());
					}
					propertyDefinitionVo.setValueRequired(propDef
							.get_IsValueRequired());
					proDefinitionList.add(propertyDefinitionVo);
				}
			
		}  
		return proDefinitionList;
	}
	private static void iterativeDocumentClassNew(ClassDefinition o,
                                                  List<DocumentClassVo> docClassVoList, boolean isLoadProDefinition) {
		ClassDefinitionSet subSet = o.get_ImmediateSubclassDefinitions();
		Iterator itt = subSet.iterator();
		while (itt.hasNext()) {
			DocumentClassVo docClassVo = new DocumentClassVo();
			ClassDefinition oo = (ClassDefinition) itt.next();
			if (!oo.get_IsHidden().booleanValue()
					&& !oo.get_IsSystemOwned().booleanValue()) {
				docClassVo.setId(oo.get_SymbolicName());
				docClassVo.setParent(o.get_SymbolicName());
				docClassVo.setpId(o.get_SymbolicName());
				docClassVo.setSymbolicName(oo.get_SymbolicName());
				docClassVo.setText(oo.get_DisplayName());
				docClassVo.setName(oo.get_DisplayName());
		
				docClassVo.setCreate(true);
				if (isLoadProDefinition) {
					PropertyDefinitionList proList = oo
							.get_PropertyDefinitions();

					docClassVo
							.setPropTemplateIdList(buildPropertyDefinitionVo(proList));
				}
				ClassDefinitionSet ssubSet = oo
						.get_ImmediateSubclassDefinitions();
				if (!ssubSet.isEmpty()) {
					docClassVo.setSubNood(false);
					docClassVoList.add(docClassVo);
					iterativeDocumentClassNew(oo, docClassVoList,
							isLoadProDefinition);
				} else {
					docClassVo.setSubNood(true);
					docClassVoList.add(docClassVo);
				}
			}
		}
	}
	public static List<DocumentClassVo> getDocumentClassNew(ObjectStore os,
                                                            String symbolicName, boolean isLoadProDefinition) {
		List<DocumentClassVo> docClassVoList = new ArrayList<DocumentClassVo>();
		DocumentClassVo docClassVo = new DocumentClassVo();
		ClassDefinition classDefinition = fetchClassDefinition(os, symbolicName);
		//shw 2017-7-11 17:33:51 只有顶层目录才为#
//		if(symbolicName.equals(ZonyConstant.ZONYDOCUMENT)){
		docClassVo.setId(classDefinition.get_SymbolicName());
		docClassVo.setSymbolicName(classDefinition.get_SymbolicName());
		docClassVo.setText(classDefinition.get_DisplayName());
		docClassVo.setName(classDefinition.get_DisplayName());
		docClassVo.setpId("#");
		docClassVo.setParent("#");
		
		docClassVo.setCreate(true);
//		}
		if (isLoadProDefinition) {

			List<PropertyDefinitionVo> propertyDefinitionVoList = buildPropertyDefinitionVo(classDefinition
					.get_PropertyDefinitions());
			docClassVo.setPropTemplateIdList(propertyDefinitionVoList);
		}
		ClassDefinitionSet subSet = classDefinition
				.get_ImmediateSubclassDefinitions();
		if (!subSet.isEmpty()) {
			docClassVo.setSubNood(false);
			docClassVoList.add(docClassVo);
			iterativeDocumentClassNew(classDefinition, docClassVoList,
					isLoadProDefinition);
		} else {
			docClassVo.setSubNood(true);
			docClassVoList.add(docClassVo);
		}
		return docClassVoList;
	}
}
