var preview = function () {
    var docStyle = "doc,docx,pdf,xls,xlsx,xlsm,xlt,xltx,xltm,ppt,wps,pptx,PDF"; //文档类型
    var pictureStyle = "jpg,bmp,gif,svg,png,jpeg,PNG,JPG,JPEG,GIF"; //判断图片类型
    return {
        basicInfo: function (md5, fnId, fileName) {
            $("#title").text(decodeURIComponent(fileName))
            var docSuffix = fileName.substring(fileName.lastIndexOf('.') + 1);
            /*判定是展示格式文件*/
            if (docStyle.indexOf(docSuffix) >= 0) {  //NTKO文本文件
                init(md5, fnId, fileName);
                $("#main").show();
                $("#imgGroup").hide();
                //$('#previewHistoryForm').html('');
            } else if (pictureStyle.indexOf(docSuffix) >= 0) { //图片预览
                var url = "http://" + window.location.host + "/cnooc_system/api/file/viewAttach?fnId="+fnId+"&md5="+md5+"&fileName="+fileName;
                $("#main").hide();
                $("#imgGroup").show();
                $("#imgGroupItem").attr("src", url);
            }
        },
    }
}();
