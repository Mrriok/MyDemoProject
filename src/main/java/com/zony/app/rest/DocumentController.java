
package com.zony.app.rest;

import com.zony.app.domain.vo.AttachmentVo;
import com.zony.app.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import com.zony.common.annotation.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;


@Api(tags = "文档管理")
@RestController
@RequestMapping("/api/document")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;
    private final UserService userService;
    private final DataService dataService;



    @Log("上传附件")
    @ApiOperation("上传附件")
    @PostMapping(value = "/uploadAttachments")
    public ResponseEntity<Object> uploadAttachments(@RequestParam(value = "file")MultipartFile file,  HttpServletRequest request) throws IOException {
        List<AttachmentVo> attachmentVoList = new ArrayList<AttachmentVo>();
        AttachmentVo attachmentVo=documentService.uploadAttachments(file);
        return new ResponseEntity<>(attachmentVo,HttpStatus.OK);

    }




}
