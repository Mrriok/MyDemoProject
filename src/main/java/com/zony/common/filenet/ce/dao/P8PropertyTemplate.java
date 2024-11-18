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

import com.filenet.api.admin.ChoiceList;
import com.filenet.api.admin.LocalizedString;
import com.filenet.api.admin.PropertyTemplate;
import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.LocalizedStringList;
import com.filenet.api.collection.PropertyTemplateSet;
import com.filenet.api.constants.*;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.filenet.api.util.Id;
import com.zony.common.filenet.util.EngineCollectionUtils;

import java.util.*;

/**
 * 
 * 
 * @version $Rev: 1000 $ $Date: 2012-01-09 15:42:48 +0800 (星期一, 09 一月 2012) $
 */
public class P8PropertyTemplate {
	/*
	 * (non-Javadoc)
	 * @see
	 * com.ibm.ecm.nuclear.data.filenet.api.OSI#createPropertyTemplate(com.filenet
	 * .api.core.ObjectStore, int, int, java.lang.String, java.util.Map)
	 */

	public static PropertyTemplate createPropertyTemplate(ObjectStore os, int dataType, int cardinality, String symbolicName, Map<String, String> displayNameMap) {
		PropertyTemplate prop = createProp(os, dataType, cardinality, symbolicName, displayNameMap);
		prop.save(RefreshMode.REFRESH);
		return prop;
	}

	public static PropertyTemplate createPropertyTemplate(ObjectStore os, int dataType, int cardinality, String symbolicName,
                                                          Map<String, String> displayNameMap, int maxLength) {
		PropertyTemplate prop = createProp(os, dataType, cardinality, symbolicName, displayNameMap);
		prop.getProperties().putValue("MaximumLengthString", maxLength);
		prop.save(RefreshMode.REFRESH);
		return prop;
	}

	public static PropertyTemplate createPropertyTemplate(ObjectStore os, int dataType, int cardinality, String symbolicName,
                                                          Map<String, String> displayNameMap, String choiceListName) {
		PropertyTemplate prop = createProp(os, dataType, cardinality, symbolicName, displayNameMap);
		bindChoiceList(os, prop, choiceListName);
		prop.save(RefreshMode.REFRESH);
		return prop;
	}
	
	public static PropertyTemplate createPropertyTemplate(ObjectStore os, int dataType, int cardinality, String symbolicName,
                                                          Map<String, String> displayNameMap, int maxLength, String choiceListName) {
		PropertyTemplate prop = createProp(os, dataType, cardinality, symbolicName, displayNameMap);
		prop.getProperties().putValue("MaximumLengthString", maxLength);
		bindChoiceList(os, prop, choiceListName);
		prop.save(RefreshMode.REFRESH);
		return prop;
	}

	public static PropertyTemplate createPropertyTemplate(ObjectStore os, int dataType, int cardinality, String symbolicName,
                                                          Map<String, String> displayNameMap, int maxLength, boolean isRequest, String choiceListName) {
		PropertyTemplate prop = createProp(os, dataType, cardinality, symbolicName, displayNameMap);
		prop.getProperties().putValue("MaximumLengthString", maxLength);
		if (choiceListName!=null) {
			bindChoiceList(os, prop, choiceListName);
		}
		prop.set_IsValueRequired(isRequest);
		prop.save(RefreshMode.REFRESH);
		return prop;
	}
	
	private static PropertyTemplate bindChoiceList(ObjectStore os, PropertyTemplate prop, String choiceListName) {
		Iterator<?> it = os.get_ChoiceLists().iterator();
		while (it.hasNext()) {
			ChoiceList choiceList = (ChoiceList) it.next();
			if (choiceListName.equals(choiceList.get_Name())) {
				prop.set_ChoiceList(choiceList);
				break;
			}
		}
		return prop;
	}
	
	@SuppressWarnings("unchecked")
	private static PropertyTemplate createProp(ObjectStore os, int dataType, int cardinality, String symbolicName, Map<String, String> displayNameMap) {
		PropertyTemplate prop = null;
		switch (dataType) {
		case TypeID.BINARY_AS_INT:
			prop = Factory.PropertyTemplateBinary.createInstance(os);
			break;
		case TypeID.BOOLEAN_AS_INT:
			prop = Factory.PropertyTemplateBoolean.createInstance(os);
			break;
		case TypeID.DATE_AS_INT:
			prop = Factory.PropertyTemplateDateTime.createInstance(os);
			break;
		case TypeID.DOUBLE_AS_INT:
			prop = Factory.PropertyTemplateFloat64.createInstance(os);
			break;
		case TypeID.GUID_AS_INT:
			prop = Factory.PropertyTemplateId.createInstance(os);
			break;
		case TypeID.LONG_AS_INT:
			prop = Factory.PropertyTemplateInteger32.createInstance(os);
			break;
		case TypeID.OBJECT_AS_INT:
			prop = Factory.PropertyTemplateObject.createInstance(os);
			break;
		case TypeID.STRING_AS_INT:
			prop = Factory.PropertyTemplateString.createInstance(os);
			break;
		default:
			prop = Factory.PropertyTemplateObject.createInstance(os);
		}
		switch (cardinality) {
		case Cardinality.SINGLE_AS_INT:
			prop.set_Cardinality(Cardinality.SINGLE);
			break;
		case Cardinality.LIST_AS_INT:
			prop.set_Cardinality(Cardinality.LIST);
			break;
		case Cardinality.ENUM_AS_INT:
			prop.set_Cardinality(Cardinality.ENUM);
			break;
		default:
			prop.set_Cardinality(Cardinality.SINGLE);
		}
		prop.set_SymbolicName(symbolicName);
		prop.set_DisplayNames(Factory.LocalizedString.createList());
		prop.set_DescriptiveTexts(Factory.LocalizedString.createList());
		Set<String> keySet = displayNameMap.keySet();
		for (String key : keySet) {
			LocalizedString displayName = Factory.LocalizedString.createInstance();
			displayName.set_LocaleName(key);
			displayName.set_LocalizedText(displayNameMap.get(key));
			prop.get_DisplayNames().add(displayName);
			prop.get_DescriptiveTexts().add(displayName);
		}
		return prop;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.ibm.ecm.nuclear.data.filenet.api.OSI#fetchPropertyTemplates(com.filenet
	 * .api.core.ObjectStore, java.lang.String)
	 */

	public static void deletePropertyTemplate(ObjectStore os, Id propTemplateId) {
		PropertyTemplate prop = Factory.PropertyTemplate.fetchInstance(os, propTemplateId, null);
		prop.delete();
		prop.save(RefreshMode.REFRESH);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.ibm.ecm.nuclear.data.filenet.api.OSI#fetchPropertyTemplate(com.filenet
	 * .api.core.ObjectStore, java.lang.String)
	 */

	public static PropertyTemplate fetchPropertyTemplateById(ObjectStore os, String id) {
		PropertyTemplate propertyTemplate = Factory.PropertyTemplate.fetchInstance(os, new Id(id), null);
		return propertyTemplate;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.ibm.ecm.nuclear.data.filenet.api.OSI#updateDisplayNamesOfPropertyTemplate
	 * (com.filenet.api.admin.PropertyTemplate, java.util.Map)
	 */

	public static List<PropertyTemplate> fetchPropertyTemplates(ObjectStore os, String symbolicName) {
		SearchScope searchScope = new SearchScope(os);
		String sql = "SELECT p." + PropertyNames.THIS + ",P." + PropertyNames.ID + ",P." + PropertyNames.SYMBOLIC_NAME + ",P." + PropertyNames.DISPLAY_NAMES
				+ ",P." + PropertyNames.DATA_TYPE + ",P." + PropertyNames.CARDINALITY + ",P." + PropertyNames.CHOICE_LIST + ",P." + PropertyNames.DISPLAY_NAME
				+ ",P." + PropertyNames.PROPERTY_DISPLAY_CATEGORY + " FROM " + ClassNames.PROPERTY_TEMPLATE + " p WHERE p." + PropertyNames.SYMBOLIC_NAME
				+ "='" + symbolicName + "'";
		SearchSQL searchSQL = new SearchSQL(sql);
		IndependentObjectSet objects = searchScope.fetchObjects(searchSQL, null, null, true);
		List<PropertyTemplate> propTemplates = new ArrayList<PropertyTemplate>();
		for (PropertyTemplate propTemplate : EngineCollectionUtils.c(objects, PropertyTemplate.class)) {
			propTemplates.add(propTemplate);
		}
		System.out.println("Search SQL:" + sql);
		return propTemplates;
	}

	public static PropertyTemplate fetchPropertyTemplate(ObjectStore os, String symbolicName) {
		SearchScope searchScope = new SearchScope(os);
		String sql = "SELECT p." + PropertyNames.THIS + ",P.*" + "FROM " + ClassNames.PROPERTY_TEMPLATE + " p WHERE p." + PropertyNames.SYMBOLIC_NAME + "='"
				+ symbolicName + "'";
		SearchSQL searchSQL = new SearchSQL(sql);
		IndependentObjectSet objects = searchScope.fetchObjects(searchSQL, null, null, true);
		for (PropertyTemplate propTemplate : EngineCollectionUtils.c(objects, PropertyTemplate.class)) {
			return propTemplate;
		}
		return null;
	}
	/**
	 * 
	 * @Title: deletePropertyTemplate 
	 * @Description: 根据属性唯一标识删除属性 
	 * @Version 1.0
	 * @Date 2016年4月22日
	 * @Author huangpan
	 * @param os
	 * @param symbolicName
	 */
	public static void deletePropertyTemplate(ObjectStore os, String symbolicName) {
		
		PropertyTemplate pt = fetchPropertyTemplate(os, symbolicName);
		if(pt!=null){
			pt.delete();
			pt.save(RefreshMode.REFRESH);
		}
		
	}

	/** 
	 * @Title: deletePropertyTemplates 
	 * @Description: 删除属性 
	 * @Version 1.0
	 * @Date 2016年4月22日
	 * @Author ZYY
	 * @param os
	 * @param symbolicName 
	*/
	public static void deletePropertyTemplates(ObjectStore os, List<String> symbolicName) {
		StringBuilder stringBuilder = new StringBuilder();
		
		SearchScope searchScope = new SearchScope(os);
		String sql = "SELECT p." + PropertyNames.THIS + ",P." + PropertyNames.ID + ",P." + PropertyNames.SYMBOLIC_NAME + ",P." + PropertyNames.DISPLAY_NAMES
				+ ",P." + PropertyNames.DATA_TYPE + ",P." + PropertyNames.CARDINALITY + ",P." + PropertyNames.CHOICE_LIST + ",P." + PropertyNames.DISPLAY_NAME
				+ ",P." + PropertyNames.PROPERTY_DISPLAY_CATEGORY + " FROM " + ClassNames.PROPERTY_TEMPLATE + " p WHERE p." + PropertyNames.SYMBOLIC_NAME
				+ " in (" + stringBuilder + ")";
		SearchSQL searchSQL = new SearchSQL(sql);
		IndependentObjectSet objects = searchScope.fetchObjects(searchSQL, null, null, true);
//		List<PropertyTemplate> propTemplates = new ArrayList<PropertyTemplate>();
		for (PropertyTemplate propTemplate : EngineCollectionUtils.c(objects, PropertyTemplate.class)) {
			propTemplate.delete();
			propTemplate.save(RefreshMode.REFRESH);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.ibm.ecm.nuclear.data.filenet.api.OSI#deletePropertyTemplate(com.filenet
	 * .api.core.ObjectStore, com.filenet.api.util.Id)
	 */

	public static PropertyTemplateSet getPropertyTemplates(ObjectStore os) {
		return os.get_PropertyTemplates();
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.ibm.ecm.nuclear.data.filenet.api.OSI#getPropertyTemplates(com.filenet
	 * .api.core.ObjectStore)
	 */

	public static PropertyTemplate updateDisplayNamesOfPropertyTemplate(PropertyTemplate propertyTemplate, Map<String, String> displayNameMap) {
		LocalizedStringList displayNames = propertyTemplate.get_DisplayNames();
		Set<String> keySet = displayNameMap.keySet();
		boolean exist;// does language areas already exists
		for (String key : keySet) {
			exist = false;
			String key1 = key.replace("_", "-");
			for (Object obj : displayNames) {
				LocalizedString localStr = (LocalizedString) obj;
				if (localStr.get_LocaleName().equalsIgnoreCase(key1)) {
					localStr.set_LocalizedText(displayNameMap.get(key));
					exist = true;
					break;
				}
			}
			if (!exist) {
				LocalizedString displayName = Factory.LocalizedString.createInstance();
				displayName.set_LocaleName(key);
				displayName.set_LocalizedText(displayNameMap.get(key));
				displayNames.add(displayName);
			}
		}
		propertyTemplate.save(RefreshMode.REFRESH);
		return propertyTemplate;
	}
}
