package com.zony.common.filenet.util;

import com.filenet.api.admin.ClassDefinition;
import com.filenet.api.admin.PropertyDefinition;
import com.filenet.api.collection.*;
import com.filenet.api.core.Document;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.property.Properties;
import com.filenet.api.property.*;
import com.zony.common.filenet.ce.dao.P8ClassDefinition;

import java.util.*;

public class PropertyUtil {

	private static String ARRAY_SPLIT_FLAG = ",";
	private static Map<String, Object> ignoreAttributeMap = PropertyUtil.setIgnoreAttributeArray();

	/**
	 * 解析有效属性
	 * 
	 * @param doc
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> parseDefinitionProperty(Document doc) {
		Map<String, Object> properties = new HashMap<String, Object>();
		Properties props = doc.getProperties();
		Iterator it = props.iterator();
		while (it.hasNext()) {
			Property prop = (Property) it.next();
			if (PropertyUtil.isAppAttribute(prop.getPropertyName())) {// 是否有效
				if (prop instanceof PropertyBoolean) {
					properties.put(prop.getPropertyName(), prop.getBooleanValue() == null ? "" : prop.getBooleanValue());
				} else if (prop instanceof PropertyString) {
					properties.put(prop.getPropertyName(), prop.getStringValue() == null ? "" : prop.getStringValue());
				} else if (prop instanceof PropertyFloat64) {
					properties.put(prop.getPropertyName(), prop.getFloat64Value() == null ? "" : prop.getFloat64Value());
				} else if (prop instanceof PropertyInteger32) {
					properties.put(prop.getPropertyName(), prop.getInteger32Value() == null ? "" : prop.getInteger32Value());
				} else if (prop instanceof PropertyDateTime) {
					properties.put(prop.getPropertyName(), prop.getDateTimeValue() == null ? "" : ZonyDateUtil.dateToString(prop.getDateTimeValue()));
				} else if (prop instanceof PropertyBooleanList) {
					String list = "";
					BooleanList boolList = prop.getBooleanListValue();
					Iterator it1 = boolList.iterator();
					while (it1.hasNext()) {
						Boolean b = (Boolean) it1.next();
						list += PropertyUtil.ARRAY_SPLIT_FLAG + b;
					}
					properties.put(prop.getPropertyName(), list.length() == 0 ? list : list.substring(PropertyUtil.ARRAY_SPLIT_FLAG.length()));
				} else if (prop instanceof PropertyStringList) {
					String list = "";
					StringList stringList = prop.getStringListValue();
					Iterator it1 = stringList.iterator();
					while (it1.hasNext()) {
						String s = (String) it1.next();
						list += PropertyUtil.ARRAY_SPLIT_FLAG + s;
					}
					properties.put(prop.getPropertyName(), list.length() == 0 ? list : list.substring(PropertyUtil.ARRAY_SPLIT_FLAG.length()));
				} else if (prop instanceof PropertyFloat64List) {
					String list = "";
					Float64List floatList = prop.getFloat64ListValue();
					Iterator it1 = floatList.iterator();
					while (it1.hasNext()) {
						Float f = (Float) it1.next();
						list += PropertyUtil.ARRAY_SPLIT_FLAG + f;
					}
					properties.put(prop.getPropertyName(), list.length() == 0 ? list : list.substring(PropertyUtil.ARRAY_SPLIT_FLAG.length()));
				} else if (prop instanceof PropertyInteger32List) {
					String list = "";
					Integer32List integerList = prop.getInteger32ListValue();
					Iterator it1 = integerList.iterator();
					while (it1.hasNext()) {
						Integer i = (Integer) it1.next();
						list += PropertyUtil.ARRAY_SPLIT_FLAG + i;
					}
					properties.put(prop.getPropertyName(), list.length() == 0 ? list : list.substring(PropertyUtil.ARRAY_SPLIT_FLAG.length()));
				} else if (prop instanceof PropertyDateTimeList) {
					String list = "";
					DateTimeList stringList = prop.getDateTimeListValue();
					Iterator it1 = stringList.iterator();
					while (it1.hasNext()) {
						list += PropertyUtil.ARRAY_SPLIT_FLAG + ZonyDateUtil.dateToString((Date) it1.next());
					}
					properties.put(prop.getPropertyName(), list.length() == 0 ? list : list.substring(PropertyUtil.ARRAY_SPLIT_FLAG.length()));
				} else {
					// 其他类型不处理
				}
			}
		}

		return properties;
	}

	private static boolean isAppAttribute(String propertyName) {
		return true;
	}

	private static Map<String, Object> setIgnoreAttributeArray() {
		Map<String, Object> map = new HashMap<String, Object>();
		return map;
	}

	private static List<String> getPropertyList(ObjectStore os, String documentClass) {
		List<String> list = new ArrayList<String>();
		if (os != null) {
			ClassDefinition cd = P8ClassDefinition.fetchClassDefinition(os, documentClass);
			if (cd != null) {
				@SuppressWarnings("rawtypes")
				Iterator it = cd.get_PropertyDefinitions().iterator();
				int i = 0;
				while (it.hasNext()) {
					i++;
					if (i > cd.get_ProtectedPropertyCount()) {
						PropertyDefinition p = (PropertyDefinition) it.next();
						list.add(p.get_SymbolicName());
					} else {
						it.next();
					}
				}
			}
		}
		return list;
	}

	public static Object[] getPropertyValue(Property property) {
		Object[] values = null;
		if (property instanceof PropertyInteger32List) {
			if (property.getInteger32ListValue() != null) {
				Integer32List list = property.getInteger32ListValue();
				values = new Integer[list.size()];
				for (int i = 0; i < list.size(); i++)
					values[i] = (Integer) list.get(i);
			}
		} else if (property instanceof PropertyInteger32) {
			if (property.getInteger32Value() != null) {
				values = new Integer[1];
				values[0] = property.getInteger32Value();
			}
		} else if (property instanceof PropertyStringList) {
			if (property.getStringListValue() != null) {
				StringList list = property.getStringListValue();
				values = new String[list.size()];
				for (int i = 0; i < list.size(); i++)
					values[i] = list.get(i).toString();
			}
		} else if (property instanceof PropertyString) {
			if (property.getStringValue() != null) {
				values = new String[1];
				values[0] = property.getStringValue();
			}
		} else if (property instanceof PropertyFloat64List) {
			if (property.getFloat64ListValue() != null) {
				Float64List list = property.getFloat64ListValue();
				values = new Double[list.size()];
				for (int i = 0; i < list.size(); i++)
					values[i] = (Double) list.get(i);
			}
		} else if (property instanceof PropertyFloat64) {
			if (property.getFloat64Value() != null) {
				values = new Double[1];
				values[0] = property.getFloat64Value();
			}
		} else if (property instanceof PropertyDateTimeList) {
			if (property.getDateTimeListValue() != null) {
				DateTimeList list = property.getDateTimeListValue();
				values = new String[list.size()];
				for (int i = 0; i < list.size(); i++)
					values[i] = ZonyDateUtil.dateToString((Date) list.get(i));
			}
		} else if (property instanceof PropertyDateTime) {
			if (property.getDateTimeValue() != null) {
				values = new String[1];
				values[0] = ZonyDateUtil.dateToString(property.getDateTimeValue());
			}
		} else if (property instanceof PropertyBooleanList) {
			if (property.getBooleanListValue() != null) {
				BooleanList list = property.getBooleanListValue();
				values = new Boolean[list.size()];
				for (int i = 0; i < list.size(); i++)
					values[i] = (Boolean) list.get(i);
			}
		} else if (property instanceof PropertyBoolean) {
			if (property.getBooleanValue() != null) {
				values = new Boolean[1];
				values[0] = property.getBooleanValue();
			}
		} else if (property instanceof PropertyIdList) {
			if (property.getIdListValue() != null) {
				IdList list = property.getIdListValue();
				values = new String[list.size()];
				for (int i = 0; i < list.size(); i++)
					values[i] = list.get(i).toString();
			}
		} else if (property instanceof PropertyId) {
			if (property.getIdValue() != null) {
				values = new String[1];
				values[0] = property.getIdValue().toString();
			}
		} else if (property instanceof PropertyEngineObject) {
		}
		return values;
	}

	public static void init() {
		System.out.println(ignoreAttributeMap);
	}

}
