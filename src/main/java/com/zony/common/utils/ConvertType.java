package com.zony.common.utils;

public class ConvertType {

	public static String getType(final String fileName) {
		String type = "";
		final int lastSuff = fileName.lastIndexOf(".");
		final String lastString = fileName.substring(lastSuff + 1);
		if (lastString.equalsIgnoreCase("doc")) {
			type = "application/msword";
		} else if (lastString.equalsIgnoreCase("xls")) {
			type = "application/vnd.ms-excel";
		} else if (lastString.equalsIgnoreCase("ppt")) {
			type = "application/vnd.ms-powerpoint";
		} else if (lastString.equalsIgnoreCase("docx")) {
			type = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
		} else if (lastString.equalsIgnoreCase("xlsx")) {
			type = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
		} else if (lastString.equalsIgnoreCase("pptx")) {
			type = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
		} else if (lastString.equalsIgnoreCase("xlsb")) {
			type = "application/vnd.ms-excel.sheet.binary.macroEnabled.12";
		} else if (lastString.equalsIgnoreCase("xlsm")) {
			type = "application/vnd.ms-excel.sheet.macroEnabled.12";
		} else if (lastString.equalsIgnoreCase("ppsx")) {
			type = "application/vnd.openxmlformats-officedocument.presentationml.slideshow";
		} else if (lastString.equalsIgnoreCase("docm")) {
			type = "application/vnd.ms-word.document.macroEnabled.12";
		} else if (lastString.equalsIgnoreCase("dotm")) {
			type = "application/vnd.ms-word.template.macroEnabled.12";
		} else if (lastString.equalsIgnoreCase("dotx")) {
			type = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
		} else if (lastString.equalsIgnoreCase("potm")) {
			type = "application/vnd.ms-powerpoint.template.macroEnabled.12";
		} else if (lastString.equalsIgnoreCase("potx")) {
			type = "application/vnd.openxmlformats-officedocument.presentationml.template";
		} else if (lastString.equalsIgnoreCase("ppam")) {
			type = "application/vnd.ms-powerpoint.addin.macroEnabled.12";
		} else if (lastString.equalsIgnoreCase("ppsm")) {
			type = "application/vnd.ms-powerpoint.slideshow.macroEnabled.12";
		} else if (lastString.equalsIgnoreCase("pptm")) {
			type = "application/vnd.ms-powerpoint.presentation.macroEnabled.12";
		} else if (lastString.equalsIgnoreCase("xlam")) {
			type = "application/vnd.ms-excel.addin.macroEnabled.12";
		} else if (lastString.equalsIgnoreCase("xltm")) {
			type = "application/vnd.ms-excel.template.macroEnabled.12";
		} else if (lastString.equalsIgnoreCase("xltx")) {
			type = "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
		} else if (lastString.equalsIgnoreCase("svg")) {
			type = "image/svg+xml";
		} else if (lastString.equalsIgnoreCase("xfdl")) {
			type = "application/vnd.xfdl";
		} else if (lastString.equalsIgnoreCase("xfdd")) {
			type = "application/vnd.xfdl.design";
		} else if (lastString.equalsIgnoreCase("bmp")) {
			type = "image/bmp";
		} else if (lastString.equalsIgnoreCase("pdf")) {
			type = "application/pdf";
		} else if (lastString.equalsIgnoreCase("jpg")) {
			type = "image/jpeg";
		} else if (lastString.equalsIgnoreCase("gif")) {
			type = "image/gif";
		} else if (lastString.equalsIgnoreCase("avi")) {
			type = "video/x-msvideo";
		} else if (lastString.equalsIgnoreCase("wav")) {
			type = "audio/x-wav";
		} else if (lastString.equalsIgnoreCase("js")) {
			type = "text/javascript";
		} else if (lastString.equalsIgnoreCase("sql")) {
			type = "text/plain";
		} else if (lastString.equalsIgnoreCase("HTML")) {
			type = "text/html";
		} else if (lastString.equalsIgnoreCase("asf")) {
			type = "video/x-ms-asf";
		} else if (lastString.equalsIgnoreCase("mov")) {
			type = "video/quicktime";
		} else if (lastString.equalsIgnoreCase("rmvb")) {
			type = "application/vnd.rn-realmedia";
		} else if (lastString.equalsIgnoreCase("rm")) {
			type = "application/vnd.rn-realmedia";
		} else if (lastString.equalsIgnoreCase("mpeg")) {
			type = "video/mpeg";
		} else if (lastString.equalsIgnoreCase("mp3")) {
			type = "audio/x-mpeg";
		} else if (lastString.equalsIgnoreCase("vsd")) {
			type = "application/x-visio";
		} else if (lastString.equalsIgnoreCase("asx")) {
			type = "video/x-ms-asf";
		} else if (lastString.equalsIgnoreCase("ico")) {
			type = "image/x-icon";
		} else if (lastString.equalsIgnoreCase("ram")) {
			type = "audio/x.pn-realaudio";
		} else if (lastString.equalsIgnoreCase("rmi")) {
			type = "audio/mid";
		} else if (lastString.equalsIgnoreCase("rtf")) {
			type = "application/rtf";
		} else if (lastString.equalsIgnoreCase("jar")) {
			type = "application/java-archive";
		} else if (lastString.equalsIgnoreCase("snd")) {
			type = "audio/basic";
		} else if (lastString.equalsIgnoreCase("swf")) {
			type = "application/x-shockwave-flash";
		} else if (lastString.equalsIgnoreCase("m3u")) {
			type = "audio/x-mpegurl";
		} else if (lastString.equalsIgnoreCase("tar")) {
			type = "application/x-tar";
		} else if (lastString.equalsIgnoreCase("tif")) {
			type = "image/tiff";
		} else if (lastString.equalsIgnoreCase("ra")) {
			type = "audio/vnd.rn-realaudio";
		} else if (lastString.equalsIgnoreCase("mid")) {
			type = "audio/x-midi";
		} else if (lastString.equalsIgnoreCase("ddl")) {
			type = "text/plain";
		} else if (lastString.equalsIgnoreCase("ai")) {
			type = "application/postscript";
		} else if (lastString.equalsIgnoreCase("eps")) {
			type = "application/postscript";
		} else if (lastString.equalsIgnoreCase("mov")) {
			type = "video/quicktime";
		} else if (lastString.equalsIgnoreCase("exe")) {
			type = "application/x-msdownload";
		} else if (lastString.equalsIgnoreCase("mp2")) {
			type = "audio/x-mpeg";
		} else if (lastString.equalsIgnoreCase("aif")) {
			type = "audio/x-aiff";
		} else if (lastString.equalsIgnoreCase("flc")) {
			type = "video/flc";
		} else if (lastString.equalsIgnoreCase("vst")) {
			type = "application/vnd.visio";
		} else if (lastString.equalsIgnoreCase("xla")) {
			type = "application/vnd.ms-excel";
		} else if (lastString.equalsIgnoreCase("xlb")) {
			type = "application/vnd.ms-excel";
		} else if (lastString.equalsIgnoreCase("xlc")) {
			type = "application/vnd.ms-excel";
		} else if (lastString.equalsIgnoreCase("xld")) {
			type = "application/vnd.ms-excel";
		} else if (lastString.equalsIgnoreCase("xlk")) {
			type = "application/vnd.ms-excel";
		} else if (lastString.equalsIgnoreCase("zip")) {
			type = "application/zip";
		} else if (lastString.equalsIgnoreCase("pic")) {
			type = "image/pict";
		} else if (lastString.equalsIgnoreCase("pps")) {
			type = "application/vnd.ms-powerpoint";
		} else if (lastString.equalsIgnoreCase("pot")) {
			type = "application/vnd.ms-powerpoint";
		} else if (lastString.equalsIgnoreCase("psd")) {
			type = "image/x-photoshop";
		} else if (lastString.equalsIgnoreCase("cdr")) {
			type = "image/pjpeg";
		} else if (lastString.equalsIgnoreCase("pcd")) {
			type = "image/pjpeg";
		} else if (lastString.equalsIgnoreCase("dxf")) {
			type = "image/pjpeg";
		} else if (lastString.equalsIgnoreCase("ufo")) {
			type = "image/pjpeg";
		} else if (lastString.equalsIgnoreCase("eps")) {
			type = "application/postscript";
		} else if (lastString.equalsIgnoreCase("png")) {
			type = "image/png";
		} else if (lastString.equalsIgnoreCase("pcx")) {
			type = "image/pjpeg";
		} else if (lastString.equalsIgnoreCase("tiff")) {
			type = "image/tiff";
		} else if (lastString.equalsIgnoreCase("jpeg")) {
			type = "image/jpeg";
		} else if (lastString.equalsIgnoreCase("gif")) {
			type = "image/gif";
		} else if (lastString.equalsIgnoreCase("tga")) {
			type = "image/pjpeg";
		} else if (lastString.equalsIgnoreCase("exif")) {
			type = "image/pjpeg";
		} else if (lastString.equalsIgnoreCase("fpx")) {
			type = "image/pjpeg";
		} else if (lastString.equalsIgnoreCase("svg")) {
			type = "image/svg+xml";
		} else if (lastString.equalsIgnoreCase("bmp")) {
			type = "image/bmp";
		}

		// 默认
		if (type.equals("")) {
			type = "application/octet-stream";
		}

		return type;
	}

	public static String getBackType(final String type) {
		if (type.equalsIgnoreCase("application/msword")) {
			return "Office Word";
		} else if (type.equalsIgnoreCase("application/vnd.ms-excel")) {
			return "Office Excel";
		} else if (type.equalsIgnoreCase("application/vnd.ms-powerpoint")) {
			return "Office Power Point";
		} else if (type.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
			return "Office Word";
		} else if (type.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
			return "Office Excel";
		} else if (type.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.presentationml.presentation")) {
			return "Office Power Point";
		} else if (type.equalsIgnoreCase("application/vnd.ms-excel.sheet.binary.macroEnabled.12")) {
			return "Office Excel";
		} else if (type.equalsIgnoreCase("application/vnd.ms-excel.sheet.macroEnabled.12")) {
			return "Office Excel";
		} else if (type.equalsIgnoreCase("image/bmp")) {
			return "Image BMP";
		} else if (type.equalsIgnoreCase("application/pdf")) {
			return "Adobe PDF";
		} else {
			return "Other File Type";
		}

	}
}
