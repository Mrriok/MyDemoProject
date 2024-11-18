package com.zony.app.service;

import com.filenet.api.core.Document;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.zony.app.domain.FileAttach;
import com.zony.app.domain.Regulationsort;
import com.zony.app.domain.SystemDocument;
import com.zony.app.enums.InstLevelEnum;
import com.zony.app.enums.InstStatusEnum;
import com.zony.app.enums.InstStatusTypeEnum;
import com.zony.app.enums.SystemDocAttachEnum;
import com.zony.app.repository.FileAttachRepository;
import com.zony.app.repository.RegulationsortRepository;
import com.zony.app.repository.SystemDocumentRepository;
import com.zony.common.filenet.ce.dao.P8Document;
import com.zony.common.filenet.ce.dao.P8Folder;
import com.zony.common.filenet.util.AuthenticatedObjectStore;
import com.zony.common.utils.ZonyFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 历史数据导入服务
 *
 * @Author gubin
 * @Date 2020-10-16
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryImportService {

    private final EditService editService;
    private final SystemDocumentRepository systemDocumentRepository;
    private final FileAttachRepository fileAttachRepository;
    private final RegulationsortRepository regulationsortRepository;

    /**
     * 解析导入文件夹
     *
     * @Author gubin
     * @Date 2020-10-16
     */
    public void doImport(String baseRoot) {
        File rootFolder = new File(baseRoot);
        File[] typeFolderArray = rootFolder.listFiles();
        //分类目录
        for (File typeFolder : typeFolderArray) {
            String folderName = typeFolder.getName();
            int leftIndex = folderName.indexOf("(");
            int rightIndex = folderName.indexOf(")");
            if (leftIndex == -1 || rightIndex == -1) {
                continue;
            }
            //分类号
            String typeCode = folderName.substring(leftIndex + 1, rightIndex).toUpperCase();
            File[] subTypeFolderArray = typeFolder.listFiles();
            //子分类目录
            for (File subTypeFolder : subTypeFolderArray) {
                String subTypeFolderName = subTypeFolder.getName();
                String subTypeCodeStr = subTypeFolderName.split(" ")[0];
                String[] subTypeCodeStrArray = subTypeCodeStr.split("-");
                //子分类号
                String subTypeCode = subTypeCodeStrArray[1];
                File[] levelFolderArray = subTypeFolder.listFiles();
                for (File levelFolder : levelFolderArray) {
                    switch (levelFolder.getName()) {
                        case "基本制度":
                            this.readLevelFolder(InstLevelEnum.BASE_INST, levelFolder, typeCode, subTypeCode);
                            break;
                        case "管理办法":
                            this.readLevelFolder(InstLevelEnum.MANAGEMENT_METHOD, levelFolder, typeCode, subTypeCode);
                            break;
                        case "操作细则":
                            this.readLevelFolder(InstLevelEnum.OPERATING_RULE, levelFolder, typeCode, subTypeCode);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    public void readLevelFolder(InstLevelEnum instLevel, File levelFolder, String typeCode, String subTypeCode) {
        File[] documentFileArray = levelFolder.listFiles();
        for (File documentFile : documentFileArray) {
            String documentFileName = documentFile.getName().toLowerCase();
            if (documentFileName.endsWith(".doc") || documentFileName.endsWith(".docx")) {
                try {
                    Regulationsort mainType = regulationsortRepository.findByMenuId(typeCode);
                    Regulationsort subType = regulationsortRepository.findByMenuIdAndPid(subTypeCode, mainType.getId());
                    //制度编号
                    String documentCode = documentFileName.split(" ")[0].toUpperCase();
                    SystemDocument systemDocument = editService.readFromWord(null, documentFile.getAbsolutePath());
                    systemDocument.setRegulationsort(subType);
                    systemDocument.setInstLevel(instLevel);
                    systemDocument.setInstStatusType(InstStatusTypeEnum.LIBRARY);
                    systemDocument.setInstStatus(InstStatusEnum.ACCEPTED);
                    if (documentFileName.matches(".+_(\\d)+\\.(doc|docx)")) {
                        systemDocument.setCurrentVersionFlag(false);
                    } else {
                        systemDocument.setCurrentVersionFlag(true);
                    }
                    systemDocumentRepository.save(systemDocument);
                    this.saveAttachFile(documentFile, levelFolder, documentCode, systemDocument.getId(), SystemDocument.class.getSimpleName());
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    public void saveAttachFile(File documentFile, File levelFolder, String documentCode, Long mainId, String mainTable) throws Exception {
        ObjectStore os = AuthenticatedObjectStore.createDefault().getObjectStore();
        String attachFolderName = null;
        if (documentFile.getName().toLowerCase().endsWith(".doc")) {
            attachFolderName = documentFile.getName().replace(".doc", "");
        } else if (documentFile.getName().toLowerCase().endsWith(".docx")) {
            attachFolderName = documentFile.getName().replace(".docx", "");
        }
        File attachFolder = new File(levelFolder, attachFolderName + "_fjxx");
        if (attachFolder.isDirectory()) {
            File[] attachFileArray = attachFolder.listFiles();
            for (int i = 0; i < attachFileArray.length; i++) {
                File attachFile = attachFileArray[i];
                SystemDocAttachEnum systemDocAttachEnum;
                if (attachFile.getName().contains("流程图")) {
                    systemDocAttachEnum = SystemDocAttachEnum.FLOW_DIAGRAM;
                } else if (attachFile.getName().contains("控制矩阵")) {
                    systemDocAttachEnum = SystemDocAttachEnum.CONTROL_MATRIX;
                } else if (attachFile.getName().contains("权限清单")) {
                    systemDocAttachEnum = SystemDocAttachEnum.PERMISSIONS;
                }
                //其他附件
                else {
                    systemDocAttachEnum = SystemDocAttachEnum.OTHER;
                }
                String folderPath = "/SystemDocument/" + documentCode;
                Document fnDocument;
                try (InputStream ins = new FileInputStream(attachFile)) {
                    fnDocument = P8Document.createDocument(os, "Document", attachFile.getName(), null, ins);
                }
                P8Folder.createFolderCycle(os, folderPath);
                Folder fnFolder = P8Folder.fetchFolderByPath(os, folderPath);
                P8Document.file(fnDocument, fnFolder);
                FileAttach fileAttach = new FileAttach();
                fileAttach.setAttachSize(attachFile.length());
                fileAttach.setAttachTitle(attachFile.getName());
                fileAttach.setAttachType(ZonyFileUtil.getFirstNameAndType(attachFile.getName())[1]);
                fileAttach.setFnId(fnDocument.get_Id().toString());
                fileAttach.setMainId(mainId);
                fileAttach.setMainTable(mainTable);
                fileAttach.setMd5(null);
                fileAttach.setPath(folderPath);
                fileAttach.setSeqNum(i + 1);
                fileAttach.setSystemDocType(systemDocAttachEnum);
                fileAttachRepository.save(fileAttach);
            }
        }
    }
}
