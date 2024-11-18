package com.zony.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.UUID;

/**
 * @Function 文件工具类
 * @Version 1.0.0.0
 * @Date 2016年3月17日
 * @Author Lity
 * @Copyright (C) 2016, ShangHai ZonySoft Co .,Ltd All Rights Reserved.
 */
public class ZonyFileUtil {

    public static Logger log = LoggerFactory.getLogger(ZonyFileUtil.class);

    /**
     * @param sourcePath
     * @param targetPath
     * @return
     * @Title: copyFiles
     * @Description: 根据文件路径复制文件
     */
    public static boolean copyFiles(String sourcePath, String targetPath) {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        boolean copyflag = true;
        try {
            File srcfile = new File(sourcePath);
            File tarfile = new File(targetPath);
            File dirFolder = tarfile.getParentFile();
            if (!dirFolder.exists()) {
                dirFolder.mkdirs();
            }
            if (srcfile.exists()) {
                fis = new FileInputStream(srcfile);
                fos = new FileOutputStream(tarfile);
                byte[] buffer = new byte[20480];
                int count = 0;
                while ((count = fis.read(buffer)) != -1) {
                    fos.write(buffer, 0, count);
                }
            } else {
                copyflag = false;
                log.debug("电子文件不存在：" + sourcePath);
            }
        } catch (Exception e) {
            copyflag = false;
            e.printStackTrace();
            log.error(e.toString());
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
                copyflag = false;
                e.printStackTrace();
                log.error(e.toString());
            }
        }
        return copyflag;
    }

    /**
     * @param fromFileStream
     * @param to_name
     * @return
     * @throws IOException
     * @Title: copyFilesFromStream
     * @Description: 根据文件流复制文件
     */
    public static boolean copyFilesFromStream(InputStream fromFileStream, String to_name) throws IOException {
        boolean flag = true;// 是否有错误
        try {
            // 首先定义两个文件，它们都是File的实例
            File to_file = new File(to_name);
            if (to_file.exists()) {
                if (!to_file.canWrite()) {
                    flag = false;
                    throw new IOException("目标文件不是可写文件：" + to_name);
                }
            }
            String parent = to_file.getParent(); // 目标目录
            File dir = new File(parent); // 将它转换成文件。
            if (!dir.exists()) {
                boolean b = dir.mkdirs();
                if (!b) {
                    flag = false;
                    throw new IOException("目标目录不存在。 " + parent);
                }
            }
            if (dir.isFile()) {
                flag = false;
                throw new IOException("指定的目标不是目录 " + parent);
            }
            if (!dir.canWrite()) {
                flag = false;
                throw new IOException("指定的目标目录不是可写的 " + parent);
            }
            // 如果程序运行到这里，证明一切正常，这时开始复制文件。
            InputStream from = null; // 读取源文件的数据流
            FileOutputStream to = null; // 写入目标文件的数据流
            try {
                from = fromFileStream; // 创建输入流
                to = new FileOutputStream(to_file); // 创建输出流
                byte[] buffer = new byte[20480];
                int bytes_read; // 缓存中的字节数目
                while ((bytes_read = from.read(buffer)) != -1) {
                    to.write(buffer, 0, bytes_read); // 写入
                }
                return true;
            } catch (IOException ex) {
                flag = false;
            }
            // 关闭流
            finally {
                if (from != null) {
                    try {
                        from.close();
                    } catch (IOException e) {
                        flag = false;
                    }
                }
                if (to != null) {
                    try {
                        to.flush();
                        to.close();
                    } catch (IOException e) {
                        flag = false;
                    }
                }
            }
        } catch (RuntimeException re) {
            throw re;
        }
        return flag;
    }

    /**
     * @param fileName
     * @param fileBody
     * @param charsetName
     * @return
     * @Title: writeFile
     * @Description: 构造指定编码的文件
     */
    public static boolean writeFile(String fileName, String fileBody) {
        return writeFile(fileName, fileBody, null);
    }

    public static boolean writeFile(String fileName, String fileBody, String charsetName) {
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        try {
            fos = new FileOutputStream(fileName);
            if (charsetName == null) {
                osw = new OutputStreamWriter(fos);
            } else {
                osw = new OutputStreamWriter(fos, charsetName);
            }
            osw.write(fileBody);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (osw != null) {
                try {
                    osw.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    /**
     * 检查一个目录是否存在，若不存在则直接创建，支持多级目录创建
     *
     * @param filepath String
     * @return boolean
     */
    public static boolean createDir(String filepath) {
        try {
            File dir = new File(filepath);
            if (dir.isFile())
                return false;
            if (dir.exists()) {
                return true;
            }
            return dir.mkdirs();
        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 删除子目录及文件，不包含自身
     * <br/>
     * Method deleteDirChild Createby [gubin] at 2017年10月20日 下午5:20:40
     *
     * @param dir
     * @see
     */
    public static void deleteDirChild(File dir) {
        if (dir.exists()) {
            if (dir.isDirectory()) {
                File[] files = dir.listFiles();
                for (int i = 0; i < files.length; i++) {
                    deleteDir(files[i]);
                }
            }
        }
    }

    /**
     * @param dir
     * @Title: deleteDir
     * @Description: 递归删除文件夹
     */
    public static void deleteDir(File dir) {
        if (dir.exists()) {
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    deleteDir(new File(dir, children[i]));
                }
            }
            dir.delete();
        }
    }

    /**
     * 创建文件目录
     * <br/>
     * Method createFileDir Createby [gubin] at 2017年9月13日 上午11:54:46
     *
     * @param dst
     * @see
     */
    public static void createFileDir(String dst) {
        File dstFile = new File(dst);
        File dstFolder = dstFile.getParentFile();
        if (dstFolder != null && !dstFolder.exists()) {
            dstFolder.mkdirs();
        }
    }

    /**
     * 如果系统有设置临时目录，则创建并返回
     * <br/>
     * Method createTempFolder Createby [gubin] at 2017年9月18日 下午1:39:52
     *
     * @return
     * @see
     */
    public static File getRootTempFolder(String tempDirPath) {
        File tempDirFile = null;
        if (tempDirPath != null && !"".equals(tempDirPath)) {
            tempDirFile = new File(tempDirPath);
            if (!tempDirFile.exists()) {
                tempDirFile.mkdirs();
            }
        }
        return tempDirFile;
    }

    /**
     * 获取文件名和后缀 <br/>
     * Method getFirstNameAndType Createby [gubin] at 2017年10月6日 下午3:11:00
     *
     * @param filename
     * @return
     * @see
     */
    public static String[] getFirstNameAndType(String filename) {
        try {
            int indexOfType = filename.lastIndexOf(".");
            int indexOfPath = filename.lastIndexOf("/");
            if (indexOfPath == -1) {
                indexOfPath = filename.lastIndexOf("\\");
            }
            String name = filename.substring(indexOfPath + 1, indexOfType).toLowerCase();
            String type = filename.substring(indexOfType + 1).toLowerCase();
            return new String[]{name, type};
        } catch (Exception e) {
            return new String[]{"", ""};
        }
    }

    /**
     * 获取不带后缀的文件名 <br/>
     * Method getFirstName Createby [gubin] at 2017年10月6日 下午3:10:43
     *
     * @param filename
     * @return
     * @see
     */
    public static String getFirstName(String filename) {
        return getFirstNameAndType(filename)[0];
    }

    /**
     * 根据文件路径获取文件名
     * <br/>
     * Method getFileNameByPath Createby [gubin] at 2017年10月20日 上午10:39:05
     *
     * @param filePath
     * @return
     * @see
     */
    public static String getFileNameByPath(String filePath) {
        int indexOfPath = filePath.lastIndexOf("/");
        if (indexOfPath == -1) {
            indexOfPath = filePath.lastIndexOf("\\");
        }
        return filePath.substring(indexOfPath + 1);
    }

    /**
     * 根据文件路径获取目录路径
     * <br/>
     * Method getFileNameByPath Createby [gubin] at 2017年10月20日 上午10:39:05
     *
     * @param filePath
     * @return
     * @see
     */
    public static String getFolderByFilePath(String filePath) {
        int indexOfPath = filePath.lastIndexOf("/");
        if (indexOfPath == -1) {
            indexOfPath = filePath.lastIndexOf("\\");
        }
        return filePath.substring(0, indexOfPath);
    }
}
