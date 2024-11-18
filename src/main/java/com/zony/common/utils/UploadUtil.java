package com.zony.common.utils;

import cn.novelweb.pojo.file.Files;
import cn.novelweb.tool.http.Result;
import cn.novelweb.tool.upload.local.LocalUpload;
import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
import java.util.List;

/**
 * @Description: 上传文件工具类
 * @Date 2020-03-18
 * @Author ZLK
 */
@Slf4j
public class UploadUtil extends LocalUpload {

    public static Result checkFileMd5(String fileMd5, String fileName, String confFilePath, String tmpFilePath) throws Exception {
        boolean isParamEmpty = StringUtils.isBlank(fileMd5) || StringUtils.isBlank(fileName) || StringUtils.isBlank(confFilePath) || StringUtils.isBlank(tmpFilePath);
        if (isParamEmpty) {
            throw new Exception("参数值为空。");
        } else {
            File confFile = new File(confFilePath + File.separatorChar + fileMd5 + "_" + fileName + ".conf");
            File tempFile = new File(tmpFilePath + File.separatorChar + fileMd5 + "_" + fileName + "_tmp");
            if (confFile.exists() && tempFile.exists()) {
                byte[] completeList = FileUtils.readFileToByteArray(confFile);
                List<String> missChunkList = new LinkedList();

                for(int i = 0; i < completeList.length; ++i) {
                    if (completeList[i] != 127) {
                        missChunkList.add(Integer.toString(i));
                    }
                }

                JSONArray jsonArray = JSON.parseArray(JSONObject.toJSONString(missChunkList));
                return Result.ok(HttpStatus.PARTIAL_CONTENT.toString(), "文件已经上传了一部分。", jsonArray);
            } else {
                boolean isFileExists = (new File(tmpFilePath + File.separatorChar + fileMd5 + "_" + fileName)).exists();
                return isFileExists && confFile.exists() ? Result.ok(HttpStatus.OK.toString(), "文件已上传成功。") : Result.ok(HttpStatus.NOT_FOUND.toString(), "文件不存在");
            }
        }
    }

    public static Result fragmentFileUploader(UploadFileParam param, String confFilePath, String filePath, long chunkSize, HttpServletRequest request) throws Exception {
        boolean isParamEmpty = StringUtils.isBlank(filePath) || StringUtils.isBlank(confFilePath) && param.getFile() == null;
        if (isParamEmpty) {
            throw new Exception("参数值为空。");
        } else {
            boolean isMultipart = ServletFileUpload.isMultipartContent(request);
            if (!isMultipart) {
                throw new IllegalArgumentException("上传内容不是有效的multipart/form-data类型。");
            } else {
                try {
                    File tmpFile = new File(filePath, String.format("%s_tmp", param.getMd5() + "_" + param.getName()));
                    File tmpFolder = new File(filePath);
                    if (!tmpFolder.exists()) {
                        tmpFolder.mkdirs();
                    }
                    RandomAccessFile accessTmpFile = new RandomAccessFile(tmpFile, "rw");
                    long offset = chunkSize * (long)param.getChunk();
                    accessTmpFile.seek(offset);
                    accessTmpFile.write(param.getFile().getBytes());
                    accessTmpFile.close();
                    File confFile = new File(confFilePath, String.format("%s.conf", param.getMd5() + "_" + param.getName()));
                    File confFolder = new File(confFilePath);
                    if (!confFolder.exists()) {
                        confFolder.mkdirs();
                    }
                    RandomAccessFile accessConfFile = new RandomAccessFile(confFile, "rw");
                    accessConfFile.setLength((long)param.getChunks());
                    accessConfFile.seek((long)param.getChunk());
                    accessConfFile.write(127);
                    byte[] completeList = FileUtils.readFileToByteArray(confFile);
                    byte isComplete = 127;

                    for(int i = 0; i < completeList.length && isComplete == 127; ++i) {
                        isComplete &= completeList[i];
                    }

                    accessConfFile.close();
                    if (isComplete != 127) {
                        return Result.ok(HttpStatus.OK.toString(), "文件上传成功。");
                    } else {
                        String fileName = param.getName();
                        Files files = Files.builder().hash(param.getMd5()).name(fileName).type(param.getFile().getContentType()).path(tmpFile.getParent() + File.separatorChar + fileName).createTime(System.currentTimeMillis()).build();
                        boolean isSuccess = renameFile(tmpFile, param.getMd5() + "_" + param.getName());
                        if (!isSuccess) {
                            throw new Exception("文件重命名时失败。");
                        } else {
                            return Result.ok(HttpStatus.OK.toString(), HttpStatus.OK.toString(), files);
                        }
                    }
                } catch (IOException var20) {
                    var20.printStackTrace();
                    return Result.error("文件上传失败");
                }
            }
        }
    }
    private static boolean renameFile(File toBeRenamed, String toFileNewName) {
        log.info("-----------文件开始合并---------------");
        if (toBeRenamed.exists() && !toBeRenamed.isDirectory()) {
            File newFile = new File(toBeRenamed.getParent() + File.separatorChar + toFileNewName);
            log.info("-----------文件合并成功---------------");
            return toBeRenamed.renameTo(newFile);
        } else {
            log.info("-----------文件合并失败---------------");
            return false;
        }
    }
    public static File createOrRenameFile(File from){
        String[] fileInfo = getFileInfo(from);
        String toPrefix=fileInfo[0];
        String toSuffix=fileInfo[1];
        File file;
        file = createOrRenameFile(from, toPrefix, toSuffix);
        return file;

    }

    /**
     * sdcard/pic/tanyang.jpg
     *
     * toPrefix: tanyang
     * toSuffix: tanyang.jpg
     * @param from
     * @param toPrefix
     * @param toSuffix
     * @return
     */

    public static File createOrRenameFile(File from, String toPrefix, String toSuffix) {
        File directory = from.getParentFile();
        if (!directory.exists()) {
            if (directory.mkdir()) {
                log.info("Created directory " + directory.getAbsolutePath());
            }
        }
        File newFile = new File(directory, toPrefix + toSuffix);
        for (int i = 1; newFile.exists() && i < Integer.MAX_VALUE; i++) {
            newFile = new File(directory, toPrefix + '(' + i + ')' + toSuffix);
        }
        return newFile;
    }
    public static String[] getFileInfo(File from){
        String fileName=from.getName();
        int index = fileName.lastIndexOf(".");
        String toPrefix="";
        String toSuffix="";
        if(index==-1){
            toPrefix=fileName;
        }else{
            toPrefix=fileName.substring(0,index);
            toSuffix=fileName.substring(index,fileName.length());
        }
        return new String[]{toPrefix,toSuffix};
    }
}
