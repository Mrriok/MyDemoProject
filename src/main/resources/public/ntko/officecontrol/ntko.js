var ntko;//控件对象

//初始化去打开文档

function init(md5, fnId, fileName) {
    ntko = document.getElementById("TANGER_OCX");
    ntko.IsNoCopy=true;
    ntko.IsStrictNoCopy=true;
    ntko.CustomToolBar = false;// 显示/隐藏自定义工具栏。
    ntko.toolbars = false;
    ntko.FullScreenMode = false;// 可以将控件设定为全屏/或者退出全屏状态。
    ntko.IsShowFullScreenButton = false;   //全屏按钮是否展示
    ntko.IsEnableDoubleClickFSM = false;//双击全屏功能
    ntko.filesaveas=false
    ntko.filesave=false


    //菜单栏隐藏
    ntko.menubar = true;
    ntko.Titlebar = false;
    ntko.FileNew = false;
    ntko.FileOpen = false;


    if (window.navigator.platform === "Win64") {
        ntko.AddDocTypePlugin(".pdf", "PDF.NtkoDocument", "4.0.2.0", "officecontrol/ntkooledocallx64.cab", 51, true);
    } else {
        ntko.AddDocTypePlugin(".pdf", "PDF.NtkoDocument", "4.0.2.0", "officecontrol/ntkooledocall.cab", 51, true);//版增加对于PDF文件的支持
    }
    debugger
    ntko.beginOpenFromURL("http://" + window.location.host + "/cnooc_system/api/file/viewAttach?md5="+md5 + (fnId ? "&fnId=" +fnId : "") +"&fileName="+fileName);
    // ntko.OpenFromUrl("http://www.ntko.com/demo/officetest/tempatefile/test.pdf");

}



function SetNoCopy() {
    ntko.IsNoCopy = true;
}

function SetReadOnly(boolvalue) {
    debugger
    var i;
    try {
        if (boolvalue) ntko.IsShowToolMenu = false;
        with (ntko.ActiveDocument) {
            if (ntko.DocType == 1) //word
            {
                if ((ProtectionType != -1) && !boolvalue) {
                    Unprotect();
                }
                if ((ProtectionType == -1) && boolvalue) {
                    Protect(1, true, "ntko123");
                }
            } else if (ntko.DocType == 2)//excel
            {
                for (i = 1; i <= Application.Sheets.Count; i++) {
                    if (boolvalue) {
                        Application.Sheets(i).Protect("", true, true, true);
                    } else {
                        Application.Sheets(i).Unprotect("");
                    }
                }
                if (boolvalue) {
                    Application.ActiveWorkbook.Protect("", true);
                } else {
                    Application.ActiveWorkbook.Unprotect("");
                }
            }
        }
    } catch (err) {
        alert("错误：" + err.number + ":" + err.description);
    } finally {
    }
}



//KaMi NTKO坑爹手册
//1.AddCustomToolButton配置第三个ButtonIndex 参数的时候会导致控件无法打开文件 按钮只能按照JS顺序来推算ButtonIndex
//2.修改了代码之后会出现严重的缓存问题，将上方alert注释去除，便可以解决问题，alert以下的所有代码都会更新，alert以上的都会存在缓存，原因不知
