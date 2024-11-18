package com.zony.app.rest;

import cn.novelweb.tool.http.Result;
import cn.novelweb.tool.upload.local.pojo.UploadFileParam;
import com.zony.app.service.FileUploadService;
import com.zony.common.annotation.Log;
import com.zony.common.annotation.rest.AnonymousGetMapping;
import com.zony.common.annotation.rest.AnonymousPostMapping;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * 电子文件通用访问层
 *
 * @Author gubin
 * @Date 2020-08-28
 */
@Api(tags = "文件上传")
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileUploadController {

    private final FileUploadService fileUploadService;
    private final HttpServletRequest request;
    private final HttpServletResponse response;

    @ApiOperation("校验电子附件是否已上传")
    @AnonymousGetMapping
    @RequestMapping(value = "/checkFile", method = RequestMethod.GET)
    public Result<Object> checkFile(String md5, String fileName) {
        return fileUploadService.checkFile(md5, fileName);
    }

    @Log("上传电子附件")
    @ApiOperation("上传电子附件")
    @AnonymousPostMapping
    @RequestMapping(value = "/upload",
            consumes = "multipart/*", headers = "content-type=multipart/form-data",
            produces = "application/json;charset=UTF-8", method = RequestMethod.POST)
    public Result<Object> upload(UploadFileParam param) {
        return fileUploadService.upload(param, request);
    }

    @Log("浏览电子附件")
    @ApiOperation("浏览电子附件")
    @AnonymousGetMapping
    @RequestMapping(value = "/viewAttach", method = RequestMethod.GET)
    public void viewAttach(String fnId, String md5, String fileName) throws UnsupportedEncodingException {
        fileUploadService.viewAttach(request, response, fnId, md5, fileName);
    }

    @Log("下载临时文件")
    @ApiOperation("下载临时文件")
    @AnonymousGetMapping
    @RequestMapping(value = "/downloadTemp", method = RequestMethod.GET)
    public void downloadTemp(String fileName) throws UnsupportedEncodingException {
        fileUploadService.downloadTemp(request, response, fileName);
    }

    @Log("打包下载文件")
    @ApiOperation("打包下载文件")
    @AnonymousGetMapping("/createZip")
    public ResponseEntity<Object> createZip(long mainId, String mainTable) throws IOException {
        return new ResponseEntity<>(fileUploadService.createZip(mainId, mainTable), HttpStatus.OK);
    }
}
