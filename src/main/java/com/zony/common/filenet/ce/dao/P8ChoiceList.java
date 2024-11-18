package com.zony.common.filenet.ce.dao;

import com.filenet.api.admin.*;
import com.filenet.api.constants.ChoiceType;
import com.filenet.api.constants.PropertyNames;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.constants.TypeID;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.zony.common.filenet.util.FnEnumList;
import com.zony.common.filenet.util.ZonyStringUtil;
import com.zony.common.filenet.vo.ChoiceListVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * @fileName P8ChoiceList.java
 * @package com.zony.filenet.dao
 * @function CE中类似数据字典对象ChoiseList对象操作相关
 * @version 1.0.0
 * @date 2014-8-4
 * @author Jeffrey
 * @Copyright (C) 2014, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
 */
public class P8ChoiceList {
	public static Logger logger = LoggerFactory.getLogger(P8ChoiceList.class);

	/**
	 * @title: createChoiceList
	 * @date 2014-8-4
	 * @author Jeffrey
	 * @description: 创建String 类型 ChoiseList 对象，根据名称及内容对象Item的Map集合
	 * @param os
	 *            目标存储库对象
	 * @param choiceListStrName
	 *            对象名称
	 * @param choiceListDescription
	 *            对象描述
	 * @param choiceMap
	 *            对象内容集合
	 */
	public static void createChoiceList(ObjectStore os,
                                        String choiceListStrName, String choiceListDescription,
                                        LinkedHashMap<String, String> choiceMap) {
		ChoiceList objChoiceListStr = Factory.ChoiceList.createInstance(os);
		objChoiceListStr.set_ChoiceValues(Factory.Choice.createList());
		objChoiceListStr.set_DataType(TypeID.STRING);
		objChoiceListStr.set_DisplayName(choiceListStrName);
		objChoiceListStr.set_DescriptiveText(choiceListDescription);
		Set<String> keySet = choiceMap.keySet();
		int index = 0;
		for (String keyStr : keySet) {
			Choice objChoiceItem = Factory.Choice.createInstance();
			objChoiceItem.set_ChoiceType(ChoiceType.STRING);
			objChoiceItem.set_DisplayName(keyStr);
			objChoiceItem.set_ChoiceStringValue(choiceMap.get(keyStr));
			objChoiceListStr.get_ChoiceValues().add(index, objChoiceItem);
			index++;
		}
		objChoiceListStr.save(RefreshMode.REFRESH);
	}

	/**
	 * @title: createIntChoiceList
	 * @date 2014-8-4
	 * @author Jeffrey
	 * @description: 创建Integer 类型 ChoiseList 对象，根据名称及内容对象Item的Map集合
	 * @param os
	 *            目标存储库对象
	 * @param choiceListStrName
	 *            对象名称
	 * @param choiceListDescription
	 *            对象描述
	 * @param choiceIntMap
	 *            对象内容集合
	 */
	public static void createIntChoiceList(ObjectStore os,
                                           String choiceListStrName, String choiceListDescription,
                                           LinkedHashMap<String, Integer> choiceIntMap) {
		ChoiceList objChoiceListInt = Factory.ChoiceList.createInstance(os);
		objChoiceListInt.set_ChoiceValues(Factory.Choice.createList());
		objChoiceListInt.set_DataType(TypeID.LONG);
		objChoiceListInt.set_DisplayName(choiceListStrName);
		objChoiceListInt.set_DescriptiveText(choiceListDescription);
		Set<String> keySet = choiceIntMap.keySet();
		int index = 0;
		for (String keyStr : keySet) {
			Choice objChoiceItem = Factory.Choice.createInstance();
			objChoiceItem.set_ChoiceType(ChoiceType.INTEGER);
			objChoiceItem.set_DisplayName(keyStr);
			objChoiceItem.set_ChoiceIntegerValue(choiceIntMap.get(keyStr));
			objChoiceListInt.get_ChoiceValues().add(index, objChoiceItem);
			index++;
		}
		objChoiceListInt.save(RefreshMode.REFRESH);
	}

	/**
	 * 
	 * @Title: createItemStrChoiceListByID
	 * @Description: 通过ID创建ITEM对象(String 类型)(只能在最顶层目录下创建)
	 * @Version 1.0
	 * @Date 2016年6月20日
	 * @Author why
	 * @param choiceListStrName
	 *            对象名称
	 * @param choiceListDescription
	 *            对象描述
	 * @return
	 */
	public static void createItemStrChoiceListByID(ObjectStore os,
                                                   String choiceListStrName, String value, String id) {
		List<ChoiceList> allChoiceList = P8ChoiceList.getTopChoiceList(os);
		for (ChoiceList choiceList : allChoiceList) {
			if (choiceList.get_Id().toString().equals(id)) {
				Choice objChoiceItem = Factory.Choice.createInstance();
				objChoiceItem.set_ChoiceType(ChoiceType.STRING);
				objChoiceItem.set_DisplayName(choiceListStrName);
				objChoiceItem.set_ChoiceStringValue(value);
				choiceList.get_ChoiceValues().add(objChoiceItem);
				choiceList.save(RefreshMode.REFRESH);
			}
		}
	}
	/**
	 * 
	 * @Title: createItemStrbyChoiceList 
	 * @Description: 根据id新加choiselist项
	 * @Version 1.0
	 * @Date 2016-10-11
	 * @Author michael
	 * @param os
	 * @param choiceListSymbolicName
	 * @param itemName
	 * @param value
	 * @param parentId
	 */
	public static void createItemStrbyChoiceList(ObjectStore os, String choiceListSymbolicName, String groupName, String id){
		ChoiceList choiceList = null;
		Iterator iterChoiceList = os.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		while (iterChoiceList.hasNext()) {
			objChoiceList = (ChoiceList) iterChoiceList.next();
			String clSymbolicName = objChoiceList.get_Name();
			if (ZonyStringUtil.equalsIgnoreCase(clSymbolicName,
					choiceListSymbolicName)) {
				choiceList=objChoiceList;				
				break;
			}
		}
	
		int i = 0;
		int j = 0;
		Iterator<?> iterator = choiceList.get_ChoiceValues().iterator();
		while (iterator.hasNext()) {
			Choice objChoiceItem = (Choice) iterator.next();
			if (ZonyStringUtil.equalsIgnoreCase(objChoiceItem.get_Id()
					.toString(), id)) {
				Choice choiceItem = Factory.Choice.createInstance();
				choiceItem.set_ChoiceType(ChoiceType.MIDNODE_STRING);
				choiceItem.set_DisplayName(groupName);				
				objChoiceItem.get_ChoiceValues().add(choiceItem);
				objChoiceList.save(RefreshMode.REFRESH);
				break;
			}
			
		}
	}
	/**
	 * 
	 * @Title: createChoiceListGroupORItem 
	 * @Description: 根据id新加choiselist项
	 * @Version 1.0
	 * @Date 2016-10-26
	 * @Author yangsw
	 * @param os
	 * @param groupName
	 * @param itemName
	 * @param id
	 * @param parentId
	 */
	public static void createChoiceListGroupORItem(ObjectStore os, String id,
                                                   String parentId, String groupName, String choiceListSymbolicName) throws Exception {
					Iterator iterChoiceList = os.get_ChoiceLists().iterator();
					ChoiceList objChoiceList = null;
					while (iterChoiceList.hasNext()) {
						objChoiceList = (ChoiceList) iterChoiceList.next();
						String clSymbolicName = objChoiceList.get_Name();
						if (ZonyStringUtil.equalsIgnoreCase(clSymbolicName,
								choiceListSymbolicName)) {
							if(ZonyStringUtil.equalsIgnoreCase(parentId, id)){							
								Choice choiceItem = Factory.Choice.createInstance();
								choiceItem.set_ChoiceType(ChoiceType.MIDNODE_STRING);
								choiceItem.set_DisplayName(groupName);				
								objChoiceList.get_ChoiceValues().add(choiceItem);
								objChoiceList.save(RefreshMode.REFRESH);
							}
							break;
						}
					}
				Iterator<?> iterator = objChoiceList.get_ChoiceValues().iterator();
				while (iterator.hasNext()) {
					Choice objChoiceItem= (Choice) iterator.next();
					if(ZonyStringUtil.equalsIgnoreCase(objChoiceItem.get_Id().toString(),id)){
						Choice newChoice = Factory.Choice.createInstance();
						newChoice.set_ChoiceType(ChoiceType.MIDNODE_STRING);
						newChoice.set_DisplayName(groupName);	
						objChoiceItem.get_ChoiceValues().add(newChoice);
						objChoiceList.save(RefreshMode.REFRESH);
						break;
					}else{
						createItem(objChoiceItem,id,groupName);
						}
					}
				objChoiceList.save(RefreshMode.REFRESH);
			}
			
	


public static void createItem(Choice objChoiceItem, String id, String groupName){
			for(int i = 0; i < objChoiceItem.get_ChoiceValues().size(); i++){
				Choice objChoiceValue = (Choice) objChoiceItem
						.get_ChoiceValues().get(i);
				if (ZonyStringUtil.equalsIgnoreCase(objChoiceValue.get_Id().toString(), id)) {
					Choice choiceItem = Factory.Choice.createInstance();
					choiceItem.set_ChoiceType(ChoiceType.MIDNODE_STRING);
					choiceItem.set_DisplayName(groupName);				
					objChoiceValue.get_ChoiceValues().add(choiceItem);			
					break;
				}else if(objChoiceValue.get_ChoiceValues().size()>0){
					createItem(objChoiceValue,id,groupName);
				}
		}
	}
	
	
	
	/**
	 * 
	 * @Title: createItemIntChoiceListByID
	 * @Description: 通过ID创建ITEM对象(Integer类型)(只能在最顶层目录下创建)
	 * @Version 1.0
	 * @Date 2016年6月20日
	 * @Author why
	 * @param choiceListStrName
	 *            对象名称
	 * @param choiceListDescription
	 *            对象描述
	 * @return
	 */
	public static void createItemIntChoiceListByID(ObjectStore os,
                                                   String choiceListStrName, Integer value, String id) {
		List<ChoiceList> allChoiceList = P8ChoiceList.getTopChoiceList(os);
		for (ChoiceList choiceList : allChoiceList) {
			if (choiceList.get_Id().toString().equals(id)) {
				Choice objChoiceItem = Factory.Choice.createInstance();
				objChoiceItem.set_ChoiceType(ChoiceType.INTEGER);
				objChoiceItem.set_DisplayName(choiceListStrName);
				objChoiceItem.set_ChoiceIntegerValue(value);
				choiceList.get_ChoiceValues().add(objChoiceItem);
				choiceList.save(RefreshMode.REFRESH);
			}
		}
	}

	/**
	 * @title: createStrChoicesList
	 * @date 2014-8-4
	 * @author Jeffrey
	 * @description: 创建ChoiceList 对象
	 *               支持多级目录结构，具体调用方法参见（com.zony.test.junit.P8Test
	 *               .P8ChoiceListTest.createStrChoicesListTest()）
	 * @param os
	 *            目标存储库对象
	 * @param choiceListStrName
	 *            对象名称
	 * @param choiceListDescription
	 *            对象描述
	 * @param choiceItemMap
	 *            对象内容集合
	 */
	public static void createStrChoicesList(ObjectStore os,
                                            String choiceListStrName, String choiceListDescription,
                                            LinkedHashMap<String, Object> choiceItemMap) {
		ChoiceList objChoiceList = Factory.ChoiceList.createInstance(os);
		objChoiceList.set_ChoiceValues(Factory.Choice.createList());
		objChoiceList.set_DataType(TypeID.STRING);
		objChoiceList.set_DisplayName(choiceListStrName);
		objChoiceList.set_DescriptiveText(choiceListDescription);
		Set<String> keySet = choiceItemMap.keySet();
		int index = 0;
		for (String keyStr : keySet) {
			if (ZonyStringUtil.equalsIgnoreCase(keyStr,
					FnEnumList.ChoiceListTypeEnum.ITEM.toString())) {
				LinkedHashMap<String, Object> choiceMap = (LinkedHashMap<String, Object>) choiceItemMap
						.get(keyStr);
				Set<String> keySetTemp = choiceMap.keySet();
				for (String keyStrT1 : keySetTemp) {
					Choice objChoiceItem = Factory.Choice.createInstance();
					objChoiceItem.set_DisplayName(keyStrT1);
					objChoiceItem.set_ChoiceType(ChoiceType.STRING);
					objChoiceItem.set_ChoiceStringValue(String
							.valueOf(choiceMap.get(keyStrT1)));
					objChoiceList.get_ChoiceValues().add(index, objChoiceItem);
					index++;
				}
			} else {
				if (choiceItemMap.get(keyStr) instanceof String) {
					Choice objChoiceGroup = Factory.Choice.createInstance();
					objChoiceGroup.set_DisplayName(keyStr);
					objChoiceGroup.set_ChoiceType(ChoiceType.MIDNODE_STRING);
					objChoiceList.get_ChoiceValues().add(index, objChoiceGroup);
					index++;
				} else if (choiceItemMap.get(keyStr) instanceof Map) {
					Choice objChoiceGroup = Factory.Choice.createInstance();
					objChoiceGroup.set_DisplayName(keyStr);
					objChoiceGroup.set_ChoiceType(ChoiceType.MIDNODE_STRING);
					objChoiceGroup
							.set_ChoiceValues(Factory.Choice.createList());
					LinkedHashMap<String, Object> choiceMap = (LinkedHashMap<String, Object>) choiceItemMap
							.get(keyStr);
					Set<String> keySetTemp = choiceMap.keySet();
					for (String keyStrT2 : keySetTemp) {
						buildNewChoiceList(objChoiceGroup.get_ChoiceValues(),
								0, keyStrT2, choiceMap.get(keyStrT2),
								ChoiceType.STRING_AS_INT);
					}
					objChoiceList.get_ChoiceValues().add(index, objChoiceGroup);
					index++;
				}
			}
		}
		objChoiceList.save(RefreshMode.REFRESH);
	}

	/**
	 * @title: createIntChoicesList
	 * @date 2014-8-4
	 * @author Jeffrey
	 * @description: 创建ChoiceList 对象
	 *               支持多级目录结构，具体调用方法参见（com.zony.test.junit.P8Test
	 *               .P8ChoiceListTest.createIntChoicesListTest()）
	 * @param os
	 *            目标存储库对象
	 * @param choiceListStrName
	 *            对象名称
	 * @param choiceListDescription
	 *            对象描述
	 * @param choiceItemMap
	 *            对象内容集合
	 */
	public static void createIntChoicesList(ObjectStore os,
                                            String choiceListStrName, String choiceListDescription,
                                            LinkedHashMap<String, Object> choiceItemMap) {
		ChoiceList objChoiceList = Factory.ChoiceList.createInstance(os);
		objChoiceList.set_ChoiceValues(Factory.Choice.createList());
		objChoiceList.set_DataType(TypeID.LONG);
		objChoiceList.set_DisplayName(choiceListStrName);
		objChoiceList.set_DescriptiveText(choiceListDescription);
		Set<String> keySet = choiceItemMap.keySet();
		int index = 0;
		for (String keyStr : keySet) {
			if (ZonyStringUtil.equalsIgnoreCase(keyStr,
					FnEnumList.ChoiceListTypeEnum.ITEM.toString())) {
				LinkedHashMap<String, Object> choiceMap = (LinkedHashMap<String, Object>) choiceItemMap
						.get(keyStr);
				Set<String> keySetTemp = choiceMap.keySet();
				for (String keyStrT1 : keySetTemp) {
					Choice objChoiceItem = Factory.Choice.createInstance();
					objChoiceItem.set_DisplayName(keyStrT1);
					objChoiceItem.set_ChoiceType(ChoiceType.INTEGER);
					objChoiceItem.set_ChoiceIntegerValue(Integer
							.parseInt(choiceMap.get(keyStrT1).toString()));
					objChoiceList.get_ChoiceValues().add(index, objChoiceItem);
					index++;
				}
			} else {
				if (choiceItemMap.get(keyStr) instanceof String) {
					Choice objChoiceGroup = Factory.Choice.createInstance();
					objChoiceGroup.set_DisplayName(keyStr);
					objChoiceGroup.set_ChoiceType(ChoiceType.MIDNODE_INTEGER);
					objChoiceList.get_ChoiceValues().add(index, objChoiceGroup);
					index++;
				} else if (choiceItemMap.get(keyStr) instanceof Map) {
					Choice objChoiceGroup = Factory.Choice.createInstance();
					objChoiceGroup.set_DisplayName(keyStr);
					objChoiceGroup.set_ChoiceType(ChoiceType.MIDNODE_INTEGER);
					objChoiceGroup
							.set_ChoiceValues(Factory.Choice.createList());
					LinkedHashMap<String, Object> choiceMap = (LinkedHashMap<String, Object>) choiceItemMap
							.get(keyStr);
					Set<String> keySetTemp = choiceMap.keySet();
					for (String keyStrT2 : keySetTemp) {
						buildNewChoiceList(objChoiceGroup.get_ChoiceValues(),
								0, keyStrT2, choiceMap.get(keyStrT2),
								ChoiceType.INTEGER_AS_INT);
					}
					objChoiceList.get_ChoiceValues().add(index, objChoiceGroup);
					index++;
				}
			}
		}
		objChoiceList.save(RefreshMode.REFRESH);
	}

	private static void buildNewChoiceList(
			com.filenet.api.collection.ChoiceList objChoiceList, int index,
			String itemType, Object choiceListObj, int ctype) {
		if (ZonyStringUtil.equalsIgnoreCase(itemType,
				FnEnumList.ChoiceListTypeEnum.ITEM.toString())) {
			LinkedHashMap<String, Object> choiceMap = (LinkedHashMap<String, Object>) choiceListObj;
			Set<String> keySet = choiceMap.keySet();
			for (String keyStr : keySet) {
				Choice objChoiceItem = Factory.Choice.createInstance();
				objChoiceItem.set_DisplayName(keyStr);
				if (ctype == ChoiceType.STRING_AS_INT) {
					objChoiceItem.set_ChoiceType(ChoiceType.STRING);
					objChoiceItem.set_ChoiceStringValue(String
							.valueOf(choiceMap.get(keyStr)));
				} else {
					objChoiceItem.set_ChoiceType(ChoiceType.INTEGER);
					objChoiceItem.set_ChoiceIntegerValue(Integer
							.parseInt(String.valueOf(choiceMap.get(keyStr))));
				}
				objChoiceList.add(index, objChoiceItem);
				index++;
			}
		} else {
			if (choiceListObj instanceof String) {
				Choice objChoiceGroup = Factory.Choice.createInstance();
				objChoiceGroup.set_DisplayName(itemType);
				if (ctype == ChoiceType.STRING_AS_INT) {
					objChoiceGroup.set_ChoiceType(ChoiceType.MIDNODE_STRING);
				} else {
					objChoiceGroup.set_ChoiceType(ChoiceType.MIDNODE_INTEGER);
				}
				objChoiceList.add(index, objChoiceGroup);
				index++;
			} else if (choiceListObj instanceof Map) {
				Choice objChoiceGroup = Factory.Choice.createInstance();
				objChoiceGroup.set_DisplayName(itemType);
				if (ctype == ChoiceType.STRING_AS_INT) {
					objChoiceGroup.set_ChoiceType(ChoiceType.MIDNODE_STRING);
				} else {
					objChoiceGroup.set_ChoiceType(ChoiceType.MIDNODE_INTEGER);
				}
				LinkedHashMap<String, Object> choiceMap = (LinkedHashMap<String, Object>) choiceListObj;
				Set<String> keySet = choiceMap.keySet();
				objChoiceGroup.set_ChoiceValues(Factory.Choice.createList());// 添加代码，
																				// 解决新增异常
																				// _by
																				// lity
				for (String keyStr : keySet) {
					buildNewChoiceList(objChoiceGroup.get_ChoiceValues(),
							index++, keyStr, choiceMap.get(keyStr), ctype);
				}
				objChoiceList.add(index, objChoiceGroup);
				index++;
			}
		}
	}

	/**
	 * 
	 * @Title: getTopChoiceList
	 * @Description: 获取CE目录下ChoiseList目录下所有Choice(只获取父级层目录)
	 * @Version 1.0
	 * @Date 2016年6月17日
	 * @Author why
	 * @return choiceList
	 */
	public static ArrayList<ChoiceList> getTopChoiceList(ObjectStore auos) {
		Iterator<?> iterChoiceList = auos.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		ArrayList<ChoiceList> choiceList = new ArrayList<>();
		while (iterChoiceList.hasNext()) {
			objChoiceList = (ChoiceList) iterChoiceList.next();
			choiceList.add(objChoiceList);
		}
		return choiceList;
	}

	/**
	 * 
	 * @Title: getAllListChoice
	 * @Description: 获取CE目录下ChoiseList目录下所有Choice对象
	 * @Version 1.0
	 * @Date 2016年6月17日
	 * @Author why
	 * @return choiceList
	 */
	public static ArrayList<ChoiceListVo> getAllListChoice(ObjectStore auos) {
		Iterator<?> choiceTop = P8ChoiceList.getTopChoiceList(auos).iterator();
		ArrayList<String> choiceNameList = new ArrayList<String>();
		ChoiceList objChoiceList = null;
		while (choiceTop.hasNext()) {
			objChoiceList = (ChoiceList) choiceTop.next();
			choiceNameList.add(objChoiceList.get_Name());
		}
		ArrayList<ChoiceListVo> choiceList = new ArrayList<ChoiceListVo>();
		Iterator<?> iterchoiceName = choiceNameList.iterator();
		while (iterchoiceName.hasNext()) {
			ArrayList<ChoiceListVo> choiceListJoin = new ArrayList<ChoiceListVo>();
			String clSymbolicName = (String) iterchoiceName.next();
			choiceListJoin = (ArrayList<ChoiceListVo>) P8ChoiceList
					.getListOfChoiceList(auos, clSymbolicName);
			if (choiceList == null) {
				choiceList = choiceListJoin;
			} else {
				choiceList.addAll(choiceListJoin);
			}
		}
		return choiceList;
	}

	/**
	 * @title: getListOfChoiceList
	 * @date 2014年8月28日
	 * @author Jeffrey
	 * @description: 集合方式获取Choise对象内容
	 * @param os
	 *            存储库对象
	 * @param choiceListSymbolicName
	 *            choiseList对象名称
	 * @return 对象内容集合
	 */
	public static List<ChoiceListVo> getListOfChoiceList(ObjectStore os,
                                                         String choiceListSymbolicName) {
		List<ChoiceListVo> choiseListRe = new ArrayList<ChoiceListVo>();
		String clSymbolicName;
		Iterator iterChoiceList = os.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		while (iterChoiceList.hasNext()) {
			objChoiceList = (ChoiceList) iterChoiceList.next();
			clSymbolicName = objChoiceList.get_Name();
			if (ZonyStringUtil.equalsIgnoreCase(clSymbolicName,
					choiceListSymbolicName)) {
				ChoiceListVo clistvo = new ChoiceListVo();
				clistvo.setId(objChoiceList.get_Id().toString());
				clistvo.setParentId("0");
				// 构造树，增加 setText setParent _by lity
				clistvo.setParent("0");
				clistvo.setText(objChoiceList.get_Name());
				clistvo.setName(objChoiceList.get_Name());
				clistvo.setDisplayName(objChoiceList.get_DisplayName());
				clistvo.setDataType(objChoiceList.get_DataType().getValue());
				clistvo.setItemType(FnEnumList.ChoiceListTypeEnum.GROUP.toString());
				clistvo.setValue(objChoiceList.get_DescriptiveText());
				clistvo.setType(objChoiceList.get_DataType().toString());
				choiseListRe.add(clistvo);
				getChoiseListItem(objChoiceList.get_ChoiceValues(),
						objChoiceList.get_Id().toString(), choiseListRe);
			}
		}
		return choiseListRe;
	}

	private static List<ChoiceListVo> getChoiseListItem(
			com.filenet.api.collection.ChoiceList objChoiceList,
			String parentId, List<ChoiceListVo> choiseListRe) {
		Iterator iterator = objChoiceList.iterator();
		while (iterator.hasNext()) {
			Choice objChoiceItem = (Choice) iterator.next();
			if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.MIDNODE_STRING_AS_INT) {
				ChoiceListVo clistvo = new ChoiceListVo();
				clistvo.setId(objChoiceItem.get_Id().toString());
				clistvo.setParentId(parentId);
				// 构造树，增加 setText setParent _by lity
				clistvo.setParent(parentId);
				clistvo.setpId(parentId);
				clistvo.setText(objChoiceItem.get_Name());
				clistvo.setName(objChoiceItem.get_Name());
				clistvo.setDisplayName(objChoiceItem.get_DisplayName());
				clistvo.setDataType(objChoiceItem.get_ChoiceType().getValue());
				clistvo.setItemType(FnEnumList.ChoiceListTypeEnum.GROUP.toString());
				clistvo.setType(objChoiceItem.get_ChoiceType().toString());
				if (clistvo.getDataType() == ChoiceType.STRING_AS_INT) {
					clistvo.setValue(objChoiceItem.get_ChoiceStringValue());
				} else if (clistvo.getDataType() == ChoiceType.INTEGER_AS_INT) {
					clistvo.setValue(objChoiceItem.get_ChoiceIntegerValue());
				}
				choiseListRe.add(clistvo);
				if (objChoiceItem.get_ChoiceValues().size() > 0) {
					getChoiseListItem(objChoiceItem.get_ChoiceValues(),
							objChoiceItem.get_Id().toString(), choiseListRe);
				}
			} else if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.STRING_AS_INT) {
				ChoiceListVo clistvo = new ChoiceListVo();
				clistvo.setId(objChoiceItem.get_Id().toString());
				clistvo.setParentId(parentId);
				// 构造树，增加 setText setParent _by lity
				clistvo.setParent(parentId);
				clistvo.setpId(parentId);
				clistvo.setText(objChoiceItem.get_Name());
				clistvo.setName(objChoiceItem.get_Name());
				clistvo.setDisplayName(objChoiceItem.get_DisplayName());
				clistvo.setItemType(FnEnumList.ChoiceListTypeEnum.ITEM.toString());
				clistvo.setDataType(objChoiceItem.get_ChoiceType().getValue());
				clistvo.setType(objChoiceItem.get_ChoiceType().toString());
				if (clistvo.getDataType() == ChoiceType.STRING_AS_INT) {
					clistvo.setValue(objChoiceItem.get_ChoiceStringValue());
				} else if (clistvo.getDataType() == ChoiceType.INTEGER_AS_INT) {
					clistvo.setValue(objChoiceItem.get_ChoiceIntegerValue());
				}
				choiseListRe.add(clistvo);
			}
		}
		return choiseListRe;
	}

	/**
	 * @title: getChoiceList
	 * @date 2014-8-4
	 * @author Jeffrey
	 * @description: 获取ChoiceList对象下的对象集合 Map （此方法只支持一级结构）(String类型)
	 * @param os
	 *            目标存储库对象
	 * @param choiceListSymbolicName
	 *            ChoiceList对象名称
	 * @return 对象ChoiceList下的Item对象集合
	 */
	public static LinkedHashMap<String, String> getChoiceList(ObjectStore os,
                                                              String choiceListSymbolicName) {
		LinkedHashMap<String, String> clValuesMap = new LinkedHashMap<String, String>();
		String clSymbolicName;
		Iterator iterChoiceList = os.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		while (iterChoiceList.hasNext()) {
			objChoiceList = (ChoiceList) iterChoiceList.next();
			clSymbolicName = objChoiceList.get_Name();
			if (ZonyStringUtil.equalsIgnoreCase(clSymbolicName,
					choiceListSymbolicName)) {
				Iterator iterator = objChoiceList.get_ChoiceValues().iterator();
				while (iterator.hasNext()) {
					Choice objChoiceItem = (Choice) iterator.next();
					clValuesMap.put(objChoiceItem.get_DisplayName(),
							objChoiceItem.get_ChoiceStringValue());
				}
			}
		}
		return clValuesMap;
	}

	/**
	 * @title: getChoiceList
	 * @date 2016-6-20
	 * @author why
	 * @description: 获取ChoiceList对象下的对象(只获取第1层)
	 * @param os
	 *            目标存储库对象
	 * @param choiceListSymbolicName
	 *            ChoiceList对象名称
	 * @return 对象ChoiceList下的Item对象集合
	 */
	public static List<ChoiceListVo> getChoiceItemList(ObjectStore os,
                                                       String choiceListSymbolicName) {
		List<ChoiceListVo> choiseList = new ArrayList<ChoiceListVo>();
		LinkedHashMap<String, Integer> clValuesMap = new LinkedHashMap<String, Integer>();
		String clSymbolicName;
		Iterator iterChoiceList = os.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		while (iterChoiceList.hasNext()) {
			objChoiceList = (ChoiceList) iterChoiceList.next();
			clSymbolicName = objChoiceList.get_Name();
			if (ZonyStringUtil.equalsIgnoreCase(clSymbolicName,
					choiceListSymbolicName)) {
				Iterator iterator = objChoiceList.get_ChoiceValues().iterator();
				while (iterator.hasNext()) {
					Choice objChoiceItem = (Choice) iterator.next();
					ChoiceListVo choiceItem = new ChoiceListVo();
					choiceItem.setId(objChoiceItem.get_Id().toString());
					choiceItem.setParentId(objChoiceList.get_Id().toString());
					choiceItem.setpId(objChoiceList.get_Id().toString());
					choiceItem.setName(objChoiceItem.get_Name());
					choiceItem.setDisplayName(objChoiceItem.get_DisplayName());
					choiceItem.setItemType(FnEnumList.ChoiceListTypeEnum.ITEM.toString());
					if (ZonyStringUtil.equalsIgnoreCase(objChoiceList
							.get_DataType().toString(), "LONG")) {
						choiceItem.setValue(objChoiceItem
								.get_ChoiceIntegerValue());
					} else if (ZonyStringUtil.equalsIgnoreCase(objChoiceList
							.get_DataType().toString(), "STRING")) {
						choiceItem.setValue(objChoiceItem
								.get_ChoiceStringValue());
					}
					choiceItem.setDataType(objChoiceList.get_DataType()
							.getValue());
					choiceItem.setParent(objChoiceList.get_Id().toString());
					choiceItem.setText(objChoiceItem.get_Name());
					choiceItem.setType(objChoiceList.get_DataType().toString());

					choiseList.add(choiceItem);
					
				}
			}
		}
		return choiseList;
	}
	/**
	 * 
	 * @Title: getAllChoiceItemList 
	 * @Description: 获取ChoiceList对象下 的所有对象
	 * @Version 1.0
	 * @Date 2016-9-12
	 * @Author michael
	 * @param os
	 * @param choiceListSymbolicName
	 * @return
	 */
	public static List<ChoiceListVo> getAllChoiceItemList(ObjectStore os,
                                                          String choiceListSymbolicName) {
		List<ChoiceListVo> choiseList = new ArrayList<ChoiceListVo>();
		LinkedHashMap<String, Integer> clValuesMap = new LinkedHashMap<String, Integer>();
		String clSymbolicName;
		Iterator iterChoiceList = os.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		while (iterChoiceList.hasNext()) {
			objChoiceList = (ChoiceList) iterChoiceList.next();
			clSymbolicName = objChoiceList.get_Name();
			if (ZonyStringUtil.equalsIgnoreCase(clSymbolicName,
					choiceListSymbolicName)) {
				Iterator iterator = objChoiceList.get_ChoiceValues().iterator();
				while (iterator.hasNext()) {
					Choice objChoiceItem = (Choice) iterator.next();
					ChoiceListVo choiceItem = new ChoiceListVo();
					choiceItem.setId(objChoiceItem.get_Id().toString());
					choiceItem.setParentId(objChoiceList.get_Id().toString());
					choiceItem.setpId("#");
					choiceItem.setName(objChoiceItem.get_Name());
					choiceItem.setDisplayName(objChoiceItem.get_DisplayName());
					choiceItem.setItemType(FnEnumList.ChoiceListTypeEnum.ITEM.toString());
					if (ZonyStringUtil.equalsIgnoreCase(objChoiceList
							.get_DataType().toString(), "LONG")) {
						choiceItem.setValue(objChoiceItem
								.get_ChoiceIntegerValue());
					} else if (ZonyStringUtil.equalsIgnoreCase(objChoiceList
							.get_DataType().toString(), "STRING")) {
						choiceItem.setValue(objChoiceItem
								.get_ChoiceStringValue());
					}
					choiceItem.setDataType(objChoiceList.get_DataType()
							.getValue());
					choiceItem.setParent(objChoiceList.get_Id().toString());
					choiceItem.setText(objChoiceItem.get_Name());
					choiceItem.setType(objChoiceList.get_DataType().toString());

					choiseList.add(choiceItem);
					if (objChoiceItem.get_ChoiceValues() != null
							&& objChoiceItem.get_ChoiceValues().size() > 0) {

						getSubChoiceItemList(objChoiceItem.get_ChoiceValues()
								.iterator(), choiseList, choiceItem.getId());
					}
				}
			}
		}
		return choiseList;
	}
	/**
	 * 
	 * @Title: getSubChoiceItemList
	 * @Description: 递归获取ChoiceList对象下的对象
	 * @Version 1.0
	 * @Date 2016-9-12
	 * @Author michael
	 * @param iterator
	 * @param choiseList
	 * @param pId
	 */
	public static void getSubChoiceItemList(Iterator iterator,
			List<ChoiceListVo> choiseList, String pId) {

		while (iterator.hasNext()) {
			Choice objChoiceItem = (Choice) iterator.next();
			ChoiceListVo choiceItem = new ChoiceListVo();
			choiceItem.setId(objChoiceItem.get_Id().toString());
			choiceItem.setParentId(pId);
			choiceItem.setpId(pId);
			choiceItem.setName(objChoiceItem.get_Name());
			choiceItem.setDisplayName(objChoiceItem.get_DisplayName());
			choiceItem.setItemType(FnEnumList.ChoiceListTypeEnum.ITEM.toString());
			if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.STRING_AS_INT) {
				choiceItem.setValue(objChoiceItem.get_ChoiceStringValue());
			} else if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.INTEGER_AS_INT) {
				choiceItem.setValue(objChoiceItem.get_ChoiceIntegerValue());
			}
			choiceItem.setDataType(objChoiceItem.get_ChoiceType().getValue());
			choiceItem.setParent(pId);
			choiceItem.setText(objChoiceItem.get_Name());

			if (objChoiceItem.get_ChoiceValues() != null
					&& objChoiceItem.get_ChoiceValues().size() > 0) {
				getSubChoiceItemList(objChoiceItem.get_ChoiceValues()
						.iterator(), choiseList, choiceItem.getId());
			}
			choiseList.add(choiceItem);
		}

	}

	/**
	 * @title: getStrChoicesList
	 * @date 2014-8-4
	 * @author Jeffrey
	 * @description: 获取String 类型的ChoiceList对象集合，支持多层级对象
	 * @param os
	 *            目标存储库对象
	 * @param choiceListSymbolicName
	 *            ChoiceList对象名称
	 * @return 对象ChoiceList下的Item对象集合
	 */
	public static LinkedHashMap<String, Object> getStrChoicesMap(
            ObjectStore os, String choiceListSymbolicName) {
		String clSymbolicName;
		Iterator iterChoiceList = os.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		LinkedHashMap<String, Object> rootMap = new LinkedHashMap<String, Object>();
		while (iterChoiceList.hasNext()) {
			objChoiceList = (ChoiceList) iterChoiceList.next();
			clSymbolicName = objChoiceList.get_Name();
			if (ZonyStringUtil.equalsIgnoreCase(clSymbolicName,
					choiceListSymbolicName)) {
				Iterator iterator = objChoiceList.get_ChoiceValues().iterator();
				LinkedHashMap<String, Object> clValuesMap = new LinkedHashMap<String, Object>();
				while (iterator.hasNext()) {
					Choice objChoiceItem = (Choice) iterator.next();
					if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.MIDNODE_STRING_AS_INT) {
						if (objChoiceItem.get_ChoiceValues().size() < 1) {
							rootMap.put(objChoiceItem.get_Name(),
									objChoiceItem.get_DisplayName());
						} else {
							rootMap.put(
									objChoiceItem.get_Name(),
									getNewChoicesMap(
											objChoiceItem.get_ChoiceValues(),
											new LinkedHashMap<String, Object>(),
											ChoiceType.STRING_AS_INT));
						}
					} else if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.STRING_AS_INT) {
						clValuesMap.put(objChoiceItem.get_Name(),
								objChoiceItem.get_ChoiceStringValue());
					}
				}
				rootMap.put(FnEnumList.ChoiceListTypeEnum.ITEM.toString(), clValuesMap);
			}
		}
		return rootMap;
	}

	/**
	 * @title: getIntChoicesList
	 * @date 2014-8-4
	 * @author Jeffrey
	 * @description: 获取 Integer 类型ChoiceList对象集合，支持多层级对象
	 * @param os
	 *            目标存储库对象
	 * @param choiceListSymbolicName
	 *            ChoiceList对象名称
	 * @return 对象ChoiceList下的Item对象集合
	 */
	public static LinkedHashMap<String, Object> getIntChoicesMap(
            ObjectStore os, String choiceListSymbolicName) {
		String clSymbolicName;
		Iterator iterChoiceList = os.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		LinkedHashMap<String, Object> rootMap = new LinkedHashMap<String, Object>();
		while (iterChoiceList.hasNext()) {
			objChoiceList = (ChoiceList) iterChoiceList.next();
			clSymbolicName = objChoiceList.get_Name();
			if (ZonyStringUtil.equalsIgnoreCase(clSymbolicName,
					choiceListSymbolicName)) {
				Iterator iterator = objChoiceList.get_ChoiceValues().iterator();
				LinkedHashMap<String, Object> clValuesMap = new LinkedHashMap<String, Object>();
				while (iterator.hasNext()) {
					Choice objChoiceItem = (Choice) iterator.next();
					if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.MIDNODE_INTEGER_AS_INT) {
						if (objChoiceItem.get_ChoiceValues().size() < 1) {
							rootMap.put(objChoiceItem.get_Name(),
									objChoiceItem.get_DisplayName());
						} else {
							rootMap.put(
									objChoiceItem.get_Name(),
									getNewChoicesMap(
											objChoiceItem.get_ChoiceValues(),
											new LinkedHashMap<String, Object>(),
											ChoiceType.INTEGER_AS_INT));
						}
					} else if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.INTEGER_AS_INT) {
						clValuesMap.put(objChoiceItem.get_Name(),
								objChoiceItem.get_ChoiceIntegerValue());
					}
				}
				rootMap.put(FnEnumList.ChoiceListTypeEnum.ITEM.toString(), clValuesMap);
			}
		}
		return rootMap;
	}

	private static LinkedHashMap<String, Object> getNewChoicesMap(
			com.filenet.api.collection.ChoiceList choiceList,
			LinkedHashMap<String, Object> choiceListMap, int ctype) {
		LinkedHashMap<String, Object> clValuesMap = new LinkedHashMap<String, Object>();
		Iterator iterator = choiceList.iterator();
		while (iterator.hasNext()) {
			Choice objChoiceItem = (Choice) iterator.next();
			if (ctype == ChoiceType.STRING_AS_INT) {
				if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.MIDNODE_STRING_AS_INT) {
					if (objChoiceItem.get_ChoiceValues().size() < 1) {
						choiceListMap.put(objChoiceItem.get_Name(),
								objChoiceItem.get_DisplayName());
					} else {
						choiceListMap.put(
								objChoiceItem.get_Name(),
								getNewChoicesMap(
										objChoiceItem.get_ChoiceValues(),
										new LinkedHashMap<String, Object>(),
										ctype));
					}
				} else if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.STRING_AS_INT) {
					clValuesMap.put(objChoiceItem.get_Name(),
							objChoiceItem.get_ChoiceStringValue());
				}
			} else {
				if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.MIDNODE_INTEGER_AS_INT) {
					if (objChoiceItem.get_ChoiceValues().size() < 1) {
						choiceListMap.put(objChoiceItem.get_Name(),
								objChoiceItem.get_DisplayName());
					} else {
						choiceListMap.put(
								objChoiceItem.get_Name(),
								getNewChoicesMap(
										objChoiceItem.get_ChoiceValues(),
										new LinkedHashMap<String, Object>(),
										ctype));
					}
				} else if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.INTEGER_AS_INT) {
					clValuesMap.put(objChoiceItem.get_Name(),
							objChoiceItem.get_ChoiceIntegerValue());
				}
			}
		}
		if (clValuesMap.size() > 0) {
			choiceListMap.put(FnEnumList.ChoiceListTypeEnum.ITEM.toString(), clValuesMap);
		}

		return choiceListMap;
	}

	/**
	 * @title: AssociatingChoiceListWithProTemplate
	 * @date 2014-8-4
	 * @author Jeffrey
	 * @description: 关联一个ChoiceList 对象到指定的属性模板对象中
	 * @param os
	 *            目标存储库对象
	 * @param choiceListSymbolicName
	 *            ChoiceList对象名称
	 * @param propertyTemplateSymbolicName
	 *            属性模板对象名称
	 */
	public static void AssociatingChoiceListWithProTemplate(ObjectStore os,
                                                            String choiceListSymbolicName, String propertyTemplateSymbolicName) {
		String prpSymbolicName;
		String clSymbolicName;
		Iterator iter = os.get_PropertyTemplates().iterator();
		PropertyTemplate objPropertyTemplate = null;
		while (iter.hasNext()) {
			objPropertyTemplate = (PropertyTemplate) iter.next();
			prpSymbolicName = objPropertyTemplate.get_SymbolicName();
			if (ZonyStringUtil.equalsIgnoreCase(prpSymbolicName,
					propertyTemplateSymbolicName)) {
				Iterator iterChoiceList = os.get_ChoiceLists().iterator();
				ChoiceList objChoiceList = null;
				while (iterChoiceList.hasNext()) {
					objChoiceList = (ChoiceList) iterChoiceList.next();
					clSymbolicName = objChoiceList.get_Name();
					if (ZonyStringUtil.equalsIgnoreCase(clSymbolicName,
							choiceListSymbolicName)) {
						objPropertyTemplate.set_ChoiceList(objChoiceList);
						objPropertyTemplate.save(RefreshMode.REFRESH);
					}
				}
			}
		}
	}

	/**
	 * @title: AssociatingChoiceListWithProDefinition
	 * @date 2014-8-4
	 * @author Jeffrey
	 * @description: 关联一个ChoiceList 对象到指定类的指定属性定义对象中
	 * @param os
	 *            目标存储库对象
	 * @param choiceListSymbolicName
	 *            ChoiceList对象名称
	 * @param classSymbolicName
	 *            指定文件类型的名称（标识）
	 * @param propertyDefinitionSymbolicName
	 *            指定文件类型中的属性定义名称
	 */
	public static void AssociatingChoiceListWithProDefinition(ObjectStore os,
                                                              String choiceListSymbolicName, String classSymbolicName,
                                                              String propertyDefinitionSymbolicName) {
		PropertyFilter objPropertyFilter = new PropertyFilter();
		String prpSymbolicName;
		String clSymbolicName;
		objPropertyFilter.addIncludeProperty(new FilterElement(null, null,
				null, PropertyNames.PROPERTY_DEFINITIONS, null));
		ClassDefinition objClassDefinition = Factory.ClassDefinition
				.fetchInstance(os, classSymbolicName, objPropertyFilter);
		Iterator iter = objClassDefinition.get_PropertyDefinitions().iterator();
		PropertyDefinition objPropertyDefinition = null;
		while (iter.hasNext()) {
			objPropertyDefinition = (PropertyDefinition) iter.next();
			prpSymbolicName = objPropertyDefinition.get_SymbolicName();
			if (ZonyStringUtil.equalsIgnoreCase(prpSymbolicName,
					propertyDefinitionSymbolicName)) {
				Iterator iterChoiceList = os.get_ChoiceLists().iterator();
				ChoiceList objChoiceList = null;
				while (iterChoiceList.hasNext()) {
					objChoiceList = (ChoiceList) iterChoiceList.next();
					clSymbolicName = objChoiceList.get_Name();
					if (ZonyStringUtil.equalsIgnoreCase(clSymbolicName,
							choiceListSymbolicName)) {
						objPropertyDefinition.set_ChoiceList(objChoiceList);
						objClassDefinition.save(RefreshMode.REFRESH);
					}
				}
			}
		}
	}

	/**
	 * 更新ChoiceList中的Item项，支持String和Integer类型； <br/>
	 * Method updateChoiceItem Createby [jeffrey] at 2016年6月20日 下午6:27:43
	 * 
	 * @see
	 * @param os
	 *            存储库对象
	 * @param choiceListSymbolicName
	 *            ChoiceList 标识名称
	 * @param itemName
	 *            需要更新Item对象的Key值
	 * @param itemValue
	 *            需要更新Item对象的Value值，Object类型支持String和Integer
	 * @throws Exception
	 *             如果未找到当前更新的Item对象Key方法则会抛出异常信息：“没找到需要更新的对象！”
	 */
	public static void updateChoiceItem(ObjectStore os,
                                        String choiceListSymbolicName, String itemName, Object itemValue)
			throws Exception {
		Iterator<?> iterChoiceList = os.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		boolean isHave = false;
		while (iterChoiceList.hasNext()) {
			objChoiceList = (ChoiceList) iterChoiceList.next();
			if (ZonyStringUtil.equalsIgnoreCase(objChoiceList.get_Name(),
					choiceListSymbolicName)) {
				isHave = fetchItem(objChoiceList.get_ChoiceValues(), itemName,
						itemValue);
				if (isHave) {
					objChoiceList.save(RefreshMode.REFRESH);
				}
				break;
			}
		}
		if (!isHave) {
			throw new Exception("没找到需要更新的对象！");
		}
	}

	private static boolean fetchItem(
			com.filenet.api.collection.ChoiceList objChoiceList,
			String itemName, Object itemValue) {
		Iterator<?> iterator = (objChoiceList).iterator();
		boolean isHave = false;
		while (iterator.hasNext()) {
			Choice objChoiceItem = (Choice) iterator.next();
			if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.MIDNODE_STRING_AS_INT) {
				isHave = fetchItem(objChoiceItem.get_ChoiceValues(), itemName,
						itemValue);
			}
			if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.MIDNODE_INTEGER_AS_INT) {
				isHave = fetchItem(objChoiceItem.get_ChoiceValues(), itemName,
						itemValue);
			}
			if (ZonyStringUtil.equalsIgnoreCase(objChoiceItem.get_Name(),
					itemName)) {
				if (itemValue instanceof String) {
					objChoiceItem.set_ChoiceStringValue((String) itemValue);
				} else if (itemValue instanceof Integer) {
					objChoiceItem.set_ChoiceIntegerValue((Integer) itemValue);
				}
				isHave = true;
			}
		}
		return isHave;
	}

	public static void delChoiceListObj(ObjectStore os, String id,
                                        String parentId, String itemType) throws Exception {
		Iterator<?> iterChoiceList = os.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		boolean isHave = false;
		while (iterChoiceList.hasNext()) {
			objChoiceList = (ChoiceList) iterChoiceList.next();
			if (ZonyStringUtil.equalsIgnoreCase(objChoiceList.get_Id()
					.toString(), id)
					|| ZonyStringUtil.equalsIgnoreCase(objChoiceList.get_Id()
							.toString(), parentId)) {
				if (ZonyStringUtil.equalsIgnoreCase(itemType, "GROUP")) {
					objChoiceList.delete();
					isHave = true;
				} else if (ZonyStringUtil.equalsIgnoreCase(itemType, "ITEM")) {
					fetchDeItem(objChoiceList, id);
				}

				objChoiceList.save(RefreshMode.REFRESH);
			}
		}
	}
	
	/** 
	 * @Title: delChoiceListGroupORItem 
	 * @Description: 删除指定ChoiceList下的指定choice 
	 * @Version 1.0
	 * @Date 2016年10月13日
	 * @Author ZYY//yangsw修改
	 * @param os
	 * @param id
	 * @param parentId
	 * @param itemType
	 * @throws Exception 
	*/
	public static void delChoiceListGroupORItem(ObjectStore os, String id,
                                                String parentId, String itemType) throws Exception {
		Iterator<?> iterChoiceList = os.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		boolean isHave = false;
		while (iterChoiceList.hasNext()) {
			objChoiceList = (ChoiceList) iterChoiceList.next();
			if (parentId.equals(objChoiceList.get_Id().toString())) {
				Iterator iterator = objChoiceList.get_ChoiceValues().iterator();
				while (iterator.hasNext()) {
					Choice next = (Choice) iterator.next();
					if(next.get_Id().toString().equals(id)){
						objChoiceList.get_ChoiceValues().remove(next);
						objChoiceList.save(RefreshMode.REFRESH);
						break;
						}else{
							deleteItem(next,id,itemType);			
						}
					}
				objChoiceList.save(RefreshMode.REFRESH);
				}
			}
	} 
	
	public static void deleteItem(Choice next , String id, String Item){
			for(int i = 0; i < next.get_ChoiceValues().size(); i++){
				Choice objChoiceValue = (Choice) next
						.get_ChoiceValues().get(i);
			if (ZonyStringUtil.equalsIgnoreCase(objChoiceValue.get_Id().toString(), id)) {
				next.get_ChoiceValues().remove(i);
				break;
			}else if(objChoiceValue.get_ChoiceValues().size()>0){
				deleteItem(objChoiceValue,id,Item);
			}
		}
	}
	/** 
	 * @Title: updateChoiceListGroupORItem 
	 * @Description: 修改指定ChoiceList下的指定choice
	 * @Version 1.0
	 * @Date 2016年10月13日
	 * @Author yangsw
	 * @param os
	 * @param id
	 * @param parentId
	 * @param itemType
	 * @throws Exception 
	*/
	public static void updateChoiceListGroupORItem(ObjectStore os, String id,
                                                   String parentId, String displayNames) throws Exception {
		Iterator<?> iterChoiceList = os.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		boolean isHave = false;
		while (iterChoiceList.hasNext()) {
			objChoiceList = (ChoiceList) iterChoiceList.next();
			if (parentId.equals(objChoiceList.get_Id().toString())) {
				Iterator iterator = objChoiceList.get_ChoiceValues().iterator();
				while (iterator.hasNext()) {
					Choice next = (Choice) iterator.next();
					if(next.get_Id().toString().equals(id)){
						Choice newChoice = Factory.Choice.createInstance();
						next.set_DisplayName(displayNames);
						break;
						}else{
							updateItem(next,id,displayNames);
						}
					}
				objChoiceList.save(RefreshMode.REFRESH);
				}
			}
	}


public static void updateItem(Choice next , String id, String displayNames){
			for(int i = 0; i < next.get_ChoiceValues().size(); i++){
				Choice objChoiceValue = (Choice) next
						.get_ChoiceValues().get(i);
			if (ZonyStringUtil.equalsIgnoreCase(objChoiceValue.get_Id().toString(), id)) {
				objChoiceValue.set_DisplayName(displayNames);
				break;
			}else if(objChoiceValue.get_ChoiceValues().size()>0){
				updateItem(objChoiceValue,id,displayNames);
			}
		}
	}
	

	// 删除ITEM方法
	private static void fetchDeItem(ChoiceList objChoiceList, String id) {
		ChoiceList choiceList = objChoiceList;
		int i = 0;
		int j = 0;
		Iterator<?> iterator = choiceList.get_ChoiceValues().iterator();
		while (iterator.hasNext()) {
			Choice objChoiceItem = (Choice) iterator.next();
			if (ZonyStringUtil.equalsIgnoreCase(objChoiceItem.get_Id()
					.toString(), id)) {
				j = i;
			}
			i++;
		}
		choiceList.get_ChoiceValues().remove(j);
	}

	// 删除方法
	private static boolean fetchDelObj(
			com.filenet.api.collection.ChoiceList objChoiceList, String id,
			String parentid) {
		Iterator<?> iterator = objChoiceList.iterator();
		boolean isHave = false;
		outterLoop: while (iterator.hasNext()) {
			Choice objChoiceItem = (Choice) iterator.next();
			if (ZonyStringUtil.equalsIgnoreCase(objChoiceItem.get_Id()
					.toString(), parentid)) {
				for (int i = 0; i < objChoiceItem.get_ChoiceValues().size(); i++) {
					Choice objChoiceValue = (Choice) objChoiceItem
							.get_ChoiceValues().get(i);
					if (ZonyStringUtil.equalsIgnoreCase(objChoiceValue.get_Id()
							.toString(), id)) {
						objChoiceItem.get_ChoiceValues().remove(i);
						isHave = true;
						break outterLoop;
					}
				}
			} else {
				if (objChoiceItem.get_ChoiceType().getValue() == ChoiceType.MIDNODE_STRING_AS_INT) {
					isHave = fetchDelObj(objChoiceItem.get_ChoiceValues(), id,
							parentid);
					if (isHave) {
						break;
					}
				}
			}
		}
		return isHave;
	}
	/**
	 * 
	 * @Title: getTopChoiceList
	 * @Description: 获取CE目录下ChoiseList目录下所有Choice(只获取父级层目录)
	 * @Version 1.0
	 * @Date 2017年7月3日
	 * @Author Michael
	 * @return choiceList
	 */
	public static Map<String, List<ChoiceListVo>> getChoiceList(ObjectStore auos) {
		Map<String, List<ChoiceListVo>> map=new HashMap<String, List<ChoiceListVo>>();
		
		Iterator<?> iterChoiceList = auos.get_ChoiceLists().iterator();
		ChoiceList objChoiceList = null;
		ArrayList<ChoiceList> choiceList = new ArrayList<ChoiceList>();
		while (iterChoiceList.hasNext()) {
			
			objChoiceList = (ChoiceList) iterChoiceList.next();
			String clSymbolicName = objChoiceList.get_Name();
			List<ChoiceListVo> choiseList=new ArrayList<>();
				Iterator iterator = objChoiceList.get_ChoiceValues().iterator();
				while (iterator.hasNext()) {
				
					Choice objChoiceItem = (Choice) iterator.next();
					ChoiceListVo choiceItem = new ChoiceListVo();
					choiceItem.setId(objChoiceItem.get_Id().toString());
					choiceItem.setParentId(objChoiceList.get_Id().toString());
					choiceItem.setpId(objChoiceList.get_Id().toString());
					choiceItem.setName(objChoiceItem.get_Name());
					choiceItem.setDisplayName(objChoiceItem.get_DisplayName());
					choiceItem.setItemType(FnEnumList.ChoiceListTypeEnum.ITEM.toString());
					if (ZonyStringUtil.equalsIgnoreCase(objChoiceList
							.get_DataType().toString(), "LONG")) {
						choiceItem.setValue(objChoiceItem
								.get_ChoiceIntegerValue());
					} else if (ZonyStringUtil.equalsIgnoreCase(objChoiceList
							.get_DataType().toString(), "STRING")) {
						choiceItem.setValue(objChoiceItem
								.get_ChoiceStringValue());
					}
					choiceItem.setDataType(objChoiceList.get_DataType()
							.getValue());
					choiceItem.setParent(objChoiceList.get_Id().toString());
					choiceItem.setText(objChoiceItem.get_Name());
					choiceItem.setType(objChoiceList.get_DataType().toString());

					choiseList.add(choiceItem);
					
				}
			map.put(clSymbolicName, choiseList);
		}
		return map;
	}
}
