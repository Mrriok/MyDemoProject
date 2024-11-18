package com.zony.common.filenet.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Set;

public class MimeTypeMap {

	public final static String FOLDER = "folder";

	public final static HashMap<String, String> mimetypeMap = new HashMap<String, String>();

	static {
		ResourceBundle mimeTypes = ResourceBundle.getBundle("MimeTypes");
		Enumeration<String> enumeration = mimeTypes.getKeys();
		while (enumeration.hasMoreElements()) {
			String key = enumeration.nextElement();
			String value = mimeTypes.getString(key);
			mimetypeMap.put(key, value);
		}
	}

	public static String getMimetype(String extension) {
		Assert.notEmpty(extension, "extension is null");
		String mimetype = mimetypeMap.get(extension.toLowerCase());
		if (mimetype == null) {
			throw new NullPointerException("Extension '" + extension + "' does not have a mime type specified. Please add"
					+ " its mime type in MimeTypes.properties");
		}
		return mimetype;
	}

	public static void main(String[] args) {
		Set<String> keySet = MimeTypeMap.mimetypeMap.keySet();
		for (String string : keySet) {
			System.out.println("Key【" + string + "】" + "Value 【" + MimeTypeMap.mimetypeMap.get(string) + "】");
		}
	}
}
