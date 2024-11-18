package com.zony.app.service;

import cn.novelweb.tool.http.Result;
import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.zony.app.domain.FileAttach;
import com.zony.app.domain.vo.AttachFormVo;
import com.zony.app.enums.SystemDocAttachEnum;
import com.zony.app.repository.FileAttachRepository;
import com.zony.app.service.dto.FileAttachDto;
import com.zony.app.service.mapstruct.FileAttachMapper;
import com.zony.app.utils.ZipUtil;
import com.zony.common.base.BaseEntity;
import com.zony.common.config.FileProperties;
import com.zony.common.filenet.ce.dao.P8Document;
import com.zony.common.filenet.ce.dao.P8Folder;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import com.zony.common.utils.ConvertType;
import com.zony.common.utils.UploadUtil;
import com.zony.common.utils.ZonyFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 电子附件通用处理类
 *
 * @Author gubin
 * @Date 2020-08-28
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FileUploadService {

    private final FileAttachMapper fileAttachMapper;
    private final FileAttachRepository fileAttachRepository;
    private final FileProperties properties;

    /**
     * 获取上传临时路径
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public String getUploadTempPath() {
        return properties.getPath().getPath() + File.separatorChar + "upload-file" + File.separatorChar + "file";
    }

    /**
     * 校验电子文件是否已存在
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public Result<Object> checkFile(String md5, String fileName) {
        String path = properties.getPath().getPath() + File.separatorChar + "upload-file" + File.separatorChar;
        String confFilePath = path + "conf";
        String tmpFilePath = path + "file";
        try {
            Result<Object> result = UploadUtil.checkFileMd5(md5, fileName, confFilePath, tmpFilePath);
            if (HttpStatus.OK.toString().equals(result.getCode())) {
                log.info("秒传成功。");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Result.error("上传失败。");
        }
    }

    /**
     * 分片上传电子文件
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public Result<Object> upload(UploadFileParam param, HttpServletRequest request) {
        Result<Object> result;
        String path = properties.getPath().getPath() + File.separatorChar + "upload-file" + File.separatorChar;
        String confFilePath = path + "conf";
        String tmpFilePath = path + "file";
        try {
            result = UploadUtil.fragmentFileUploader(param, confFilePath,
                    tmpFilePath, 5242880L, request);
            log.info("-----------------------" + result.getCode());
            if (HttpStatus.OK.toString().equals(result.getCode())) {
                log.info("上传成功。");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            return Result.error("上传失败。");
        }
    }

    /**
     * 获取临时文件访问路径
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public String getPreviewPath(HttpServletRequest request, String md5, String fileName) {
        return this.getRequestPrefix(request) + "/file/upload-file/file/" + md5 + "_" + fileName;
    }

    /**
     * 获取url请求前缀,获取当前项目地址：http://localhost:8080/test
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public String getRequestPrefix(HttpServletRequest request) {
        String networkProtocol = request.getScheme();
        String ip = request.getServerName();
        int port = request.getServerPort();
        String webApp = request.getContextPath();
        String urlPrefix = networkProtocol + "://" + ip + ":" + port + webApp;
        return urlPrefix;
    }

    /**
     * 新增附件
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public void create(ObjectStore os, String folderPath, AttachFormVo submitVo) {
        BaseEntity baseEntity = submitVo.getFormObj();
        for (int i = 0; i < submitVo.getAttachList().size(); i++) {
            FileAttachDto fileAttachDto = submitVo.getAttachList().get(i);
            this.create(os, folderPath, baseEntity, fileAttachDto, i + 1);
        }
    }

    /**
     * 新增附件
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public FileAttach create(ObjectStore os, String folderPath, BaseEntity baseEntity, FileAttachDto fileAttachDto, int seqNum) {
        File tmpFile = new File(getUploadTempPath(), fileAttachDto.getMd5() + "_" + fileAttachDto.getFileName());
        Document fnDocument = P8Document.createDocument(os, fileAttachDto.getFileName());
        P8Folder.createFolderCycle(os, folderPath);
        Folder fnFolder = P8Folder.fetchFolderByPath(os, folderPath);
        P8Document.file(fnDocument, fnFolder);
        FileAttach fileAttach = new FileAttach();
        fileAttach.setAttachSize(tmpFile.length());
        fileAttach.setAttachTitle(fileAttachDto.getFileName());
        fileAttach.setAttachType(ZonyFileUtil.getFirstNameAndType(fileAttachDto.getFileName())[1]);
        fileAttach.setFnId(fnDocument.get_Id().toString());
        fileAttach.setMainId(this.getId(baseEntity));
        fileAttach.setMainTable(this.getMainTable(baseEntity));
        fileAttach.setMd5(fileAttachDto.getMd5());
        fileAttach.setPath(folderPath);
        fileAttach.setSeqNum(seqNum);
        fileAttach.setSystemDocType(fileAttachDto.getSystemDocType());
        fileAttachRepository.save(fileAttach);
        return fileAttach;
    }

    /**
     * 获取ID
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public Long getId(Object entity) {
        try {
            return (Long) PropertyUtils.getProperty(entity, "id");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取所属主表
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public String getMainTable(Object baseEntity) {
        return baseEntity.getClass().getSimpleName();
    }

    /**
     * 查看附件列表
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public List<FileAttachDto> view(BaseEntity baseEntity) {
        List<FileAttach> fileAttachList = fileAttachRepository.findByMainIdAndMainTable(this.getId(baseEntity), this.getMainTable(baseEntity));
        List<FileAttachDto> fileAttachDtoList = new ArrayList<>();
        fileAttachList.forEach(fileAttach -> {
            FileAttachDto fileAttachDto = fileAttachMapper.toDto(fileAttach);
            fileAttachDto.setFileName(fileAttach.getAttachTitle());
            fileAttachDtoList.add(fileAttachDto);
        });
        return fileAttachDtoList;
    }

    /**
     * 删除附件
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public void delete(ObjectStore os, BaseEntity baseEntity) {
        List<FileAttach> fileAttachList = fileAttachRepository.findByMainIdAndMainTable(this.getId(baseEntity), this.getMainTable(baseEntity));
        fileAttachList.forEach(fileAttach -> {
            if (StringUtils.isNotBlank(fileAttach.getFnId())) {
                P8Document.deleteDocumentById(os, fileAttach.getFnId());
            }
            fileAttachRepository.delete(fileAttach);
        });
    }

    /**
     * 更改替换附件
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public void update(AttachFormVo submitVo, String folderPath) {
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        List<String> existFnIdList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(submitVo.getAttachList())) {
            for (int i = 0; i < submitVo.getAttachList().size(); i++) {
                FileAttachDto fileAttachDto = submitVo.getAttachList().get(i);
                //新附件
                if (StringUtils.isBlank(fileAttachDto.getFnId())) {
                    FileAttach fileAttach = this.create(os, folderPath, submitVo.getFormObj(), fileAttachDto, i + 1);
                    existFnIdList.add(fileAttach.getFnId());
                }
                //存留附件
                else {
                    existFnIdList.add(fileAttachDto.getFnId());
                }
            }
        }
        //删除非存留附件
        List<FileAttach> fileAttachList = fileAttachRepository.findByMainIdAndMainTable(this.getId(submitVo.getFormObj()), this.getMainTable(submitVo.getFormObj()));
        fileAttachList.forEach(fileAttach -> {
            if (!existFnIdList.contains(fileAttach.getFnId())) {
                fileAttachRepository.delete(fileAttach);
                P8Document.deleteDocumentById(os, fileAttach.getFnId());
            }
        });
    }

    /**
     * 浏览下载临时文件
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public void downloadTemp(HttpServletRequest request, HttpServletResponse response, String fileName) throws UnsupportedEncodingException {
        fileName = URLDecoder.decode(fileName, "utf-8");
        String filePath = properties.getPath().getPath() + fileName;
        this.downloadFile(request, response, null, filePath);
    }

    /**
     * 浏览下载存档附件
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public void viewAttach(HttpServletRequest request, HttpServletResponse response, String fnId, String md5, String fileName) throws UnsupportedEncodingException {
        fileName = URLDecoder.decode(fileName, "utf-8");
        String filePath = properties.getPath().getPath() + "/upload-file/file/" + md5 + "_" + fileName;
        this.downloadFile(request, response, fnId, filePath);
    }

    /**
     * 将附件打包提供下载
     *
     * @Author gubin
     * @Date 2020-09-24
     */
    public String createZip(long mainId, String mainTable) throws IOException {
        String zipFileName = UUID.randomUUID() + ".zip";
        String zipFilePath = properties.getPath().getPath() + zipFileName;
        List<FileAttach> fileAttachList = fileAttachRepository.findByMainIdAndMainTable(mainId, mainTable);
        for (FileAttach fileAttach : fileAttachList) {
            String tempFilePath = properties.getPath().getPath() + UUID.randomUUID() + "." + fileAttach.getAttachType();
            this.downloadFileNetFileToLocal(tempFilePath, fileAttach.getFnId());
            ZipUtil.zip(tempFilePath, zipFilePath, false, null);
        }
        return zipFileName;
    }

    /**
     * 将Filenet文件下载到服务器本地
     *
     * @Author gubin
     * @Date 2020-09-24
     */
    public void downloadFileNetFileToLocal(String localPath, String fnId) throws IOException {
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        ContentTransfer fileTransfer = P8Document.getAttachFileFromDocument(os, fnId);
        FileUtils.copyInputStreamToFile(fileTransfer.accessContentStream(), new File(localPath));
    }

    /**
     * 浏览下载通用方法
     *
     * @Author gubin
     * @Date 2020-08-28
     */
    public void downloadFile(HttpServletRequest request, HttpServletResponse response, String fnId, String filePath) {
        ServletOutputStream ouStream = null;
        InputStream inStream = null;
        String responseFileName;// 显示文件名
        String responseContentType;// 显示文件类型
        int responseContentLength;// 显示字节数
        try {
            ouStream = response.getOutputStream();
            File tmpFile = new File(filePath);
            if (tmpFile.exists()) {
                responseFileName = tmpFile.getName();
                responseContentType = ConvertType.getType(tmpFile.getName());
                responseContentLength = (int) tmpFile.length();
                inStream = new FileInputStream(tmpFile);
            } else {
                ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
                ContentTransfer fileTransfer = P8Document.getAttachFileFromDocument(os, fnId);
                responseFileName = fileTransfer.get_RetrievalName();
                responseContentType = fileTransfer.get_ContentType();
                responseContentLength = fileTransfer.get_ContentSize().intValue();
                inStream = fileTransfer.accessContentStream();
            }
            response.setContentType(responseContentType);
            response.setHeader("Content-disposition", "attach;filename=" + URLEncoder.encode(responseFileName, "UTF-8"));
            response.setHeader("Pragma", "no-cache");
            response.setContentLength(responseContentLength);
            byte[] buffer = new byte[5242880];
            int len;
            while ((len = inStream.read(buffer)) > 0) {
                ouStream.write(buffer, 0, len);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (inStream != null) {
                try {
                    inStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (ouStream != null) {
                try {
                    ouStream.flush();
                    ouStream.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
}
