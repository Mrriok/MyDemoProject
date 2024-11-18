/**
 * Created by zhongyiming on 2018/3/13.
 */
(function ($) {
    $.fn.serializeJson = function () {
        return this.serializeArray();
        //var serializeObj={};
        //$( this .serializeArray()).each( function (){
        //    serializeObj[ this .name]= this .value;
        //});
        //return serializeObj;
    };
})(jQuery);
//全局变量定义点
var projectManageSub_treeNode = {};//项目管理-项目管理-平台列表树节点对象 stanzhao
var projectModule_treeNode = {};//项目管理-模块管理树节点对象 stanzhao
var baas = {
    BASE_URL: "http://" + window.location.host + "/dpms",
    SCP_BASE_URL: function () {
        var host = window.location.host;
        if (host == "localhost:8090") {
            return "http://192.168.52.52:8080"
        } else if (host == "dpms.wm-motor.com:9080") {
            return "http://scp.wm-motor.com:8080"
        } else if (host == "10.0.201.62:9081") {
            return "http://scp-test.wm-motor.com:8080"
        } else if (host == "localhost:8080") {
            return "http://localhost:8090"
        }
    }(),
    SCPSSO_LOGIN_URL: function () {
        var host = window.location.host;
        if (host == "localhost:8090") {
            return "https://ssotest.wm-motor.com/profile/oauth2/authorize?client_id=s1jjqDPoz9&redirect_uri=http://localhost:8013/ssoLogin&response_type=code"
        } else if (host == "dpms.wm-motor.com:9080") {
            return "https://sso.wm-motor.com/profile/oauth2/authorize?client_id=7FRTnBgxbL&redirect_uri=http://scp.wm-motor.com/ssoLogin&response_type=code"
        } else if (host == "10.0.201.62:9081") {
            return "https://ssotest.wm-motor.com/profile/oauth2/authorize?client_id=s1jjqDPoz9&redirect_uri=http://scp-test.wm-motor.com/ssoLogin&response_type=code"
        } else if (host == "localhost:8080") {
            return "https://ssotest.wm-motor.com/profile/oauth2/authorize?client_id=s1jjqDPoz9&redirect_uri=http://localhost:8013/ssoLogin&response_type=code"
        }
    }(),


    standardDeptCode: "dept_649",//标准化部门文件夹对应CE的标识
    standardDeptId: "649",//标准化部门的部门标识
    esDeptId: "642",//产品数据支持部的部门标识

    associateProjectRequiredClass: [
        "WMTechnologyDocument1T4",//1T4-变更申请
        "WMTechnologyDocument1A1",//1A1-整车配置表
        "WMTechnologyDocument1A4",//1A4-BOM
        "WMTechnologyDocument171",//171-认证证书
        "WMTechnologyDocument215",//215-产品开发要求说明（SOR)
        "WMTechnologyDocument216",//216-设计说明书
        "WMTechnologyDocument217",//217-零部件检验规格书
        "WMTechnologyDocument218",//218-零部件检查基准书
        "WMTechnologyDocument219",//219-产品技术条件
        "WMTechnologyDocument21D",//21D-开模令
        "WMTechnologyDocument161",//161-技术协议及补充协议
        "WMTechnologyDocument1D2",//1D2-产品使用说明书
        "WMTechnologyDocument1D3",//1D3-备件目录
        "WMTechnologyDocument1D4",//1D4-维修手册
        "WMTechnologyDocument1D5",//1D5-拆解手册
        "WMTechnologyDocument1D6",//1D6-电器原理图
        "WMTechnologyDocument24A"//24A-工装样件认可报告
    ],
    parameterRequireDocumentType: [
        "WMTechnologyDocument215",//-产品开发要求说明（SOR）
        "WMTechnologyDocument161",//-技术协议及补充协议）
        "WMTechnologyDocument23L",//-失效分析报告）
        "WMTechnologyDocument21D",//-开模令
        "WMTechnologyDocument187",//-零部件设计验证计划（DVP）
        "WMTechnologyDocument182",//-零部件认可管制表”
        "WMTechnologyDocument194",//-材料清单
        "WMTechnologyDocument217",//-零部件检验规格书
        "WMTechnologyDocument242",//-零部件试验报告、验收报告
        "WMTechnologyDocument24F",//-供应商工装样件保证书
        "WMTechnologyDocument24J",//-MB1综合匹配认可报告
        "WMTechnologyDocument24K",//-MB2综合匹配认可报告
        "WMTechnologyDocument24A",//-工装样件认可报告
    ],
    //机密
    //秘密
    //内部公开
    //公开
    //绝密
    secretDegree: {
        "WMTechnologyDocument11": "秘密",
        "WMTechnologyDocument12": "秘密",
        "WMTechnologyDocument13": "秘密",
        //14
        "WMTechnologyDocument141": "机密",
        "WMTechnologyDocument142": "机密",
        "WMTechnologyDocument143": "机密",
        "WMTechnologyDocument14Z": "秘密",
        //15
        "WMTechnologyDocument151": "秘密",
        "WMTechnologyDocument152": "内部公开",
        "WMTechnologyDocument15Z": "内部公开",
        //16
        "WMTechnologyDocument161": "机密",
        "WMTechnologyDocument162": "秘密",
        "WMTechnologyDocument163": "机密",
        "WMTechnologyDocument164": "秘密",
        "WMTechnologyDocument165": "秘密",
        "WMTechnologyDocument16Z": "秘密",
        //17
        "WMTechnologyDocument171": "秘密",
        "WMTechnologyDocument17Z": "秘密",
        //18
        "WMTechnologyDocument181": "秘密",
        "WMTechnologyDocument182": "秘密",
        "WMTechnologyDocument183": "秘密",
        "WMTechnologyDocument184": "秘密",
        "WMTechnologyDocument185": "秘密",
        "WMTechnologyDocument186": "秘密",
        "WMTechnologyDocument187": "秘密",
        "WMTechnologyDocument188": "秘密",
        "WMTechnologyDocument18Z": "秘密",
        //19
        "WMTechnologyDocument191": "秘密",
        "WMTechnologyDocument192": "秘密",
        "WMTechnologyDocument193": "秘密",
        "WMTechnologyDocument194": "秘密",
        "WMTechnologyDocument195": "秘密",
        "WMTechnologyDocument196": "秘密",
        "WMTechnologyDocument197": "秘密",
        "WMTechnologyDocument198": "秘密",
        "WMTechnologyDocument199": "秘密",
        "WMTechnologyDocument19Z": "秘密",
        //1A
        "WMTechnologyDocument1A1": "秘密",
        "WMTechnologyDocument1A2": "秘密",
        "WMTechnologyDocument1A3": "秘密",
        "WMTechnologyDocument1A4": "秘密",
        "WMTechnologyDocument1A5": "秘密",
        "WMTechnologyDocument1AZ": "秘密",
        //1B
        "WMTechnologyDocument1B1": "秘密",
        "WMTechnologyDocument1B2": "秘密",
        "WMTechnologyDocument1BZ": "秘密",
        //1C
        "WMTechnologyDocument1C": "秘密",
        //1D
        "WMTechnologyDocument1D1": "内部公开",
        "WMTechnologyDocument1D2": "公开",
        "WMTechnologyDocument1D3": "秘密",
        "WMTechnologyDocument1D4": "内部公开",
        "WMTechnologyDocument1D5": "内部公开",
        "WMTechnologyDocument1D6": "秘密",
        "WMTechnologyDocument1DZ": "内部公开",
        //1F
        "WMTechnologyDocument1F1": "内部公开",
        "WMTechnologyDocument1F2": "内部公开",
        "WMTechnologyDocument1F3": "内部公开",
        //1T
        "WMTechnologyDocument1T1": "秘密",
        "WMTechnologyDocument1T2": "秘密",
        "WMTechnologyDocument1T4": "秘密",
        "WMTechnologyDocument1T5": "秘密",
        "WMTechnologyDocument1T6": "秘密",
        "WMTechnologyDocument1T7": "秘密",
        "WMTechnologyDocument1T8": "秘密",
        "WMTechnologyDocument1T9": "秘密",
        "WMTechnologyDocument1TZ": "秘密",
        //21
        "WMTechnologyDocument211": "机密",
        "WMTechnologyDocument212": "机密",
        "WMTechnologyDocument213": "机密",
        "WMTechnologyDocument214": "秘密",
        "WMTechnologyDocument215": "秘密",
        "WMTechnologyDocument216": "秘密",
        "WMTechnologyDocument217": "秘密",
        "WMTechnologyDocument218": "秘密",
        "WMTechnologyDocument219": "秘密",
        "WMTechnologyDocument21A": "秘密",
        "WMTechnologyDocument21B": "秘密",
        "WMTechnologyDocument21C": "秘密",
        "WMTechnologyDocument21D": "秘密",
        "WMTechnologyDocument21E": "秘密",
        "WMTechnologyDocument21F": "秘密",
        "WMTechnologyDocument21G": "秘密",
        "WMTechnologyDocument21Z": "秘密",
        "WMTechnologyDocument21H": "机密",
        "WMTechnologyDocument21J": "机密",
        //22
        "WMTechnologyDocument221": "秘密",
        "WMTechnologyDocument222": "秘密",
        "WMTechnologyDocument223": "机密",
        "WMTechnologyDocument224": "机密",
        "WMTechnologyDocument225": "秘密",
        "WMTechnologyDocument22Z": "秘密",
        "WMTechnologyDocument226": "秘密",
        //23
        "WMTechnologyDocument231": "内部公开",
        "WMTechnologyDocument232": "内部公开",
        "WMTechnologyDocument233": "秘密",
        "WMTechnologyDocument234": "秘密",
        "WMTechnologyDocument235": "秘密",
        "WMTechnologyDocument236": "绝密",
        "WMTechnologyDocument237": "机密",
        "WMTechnologyDocument238": "机密",
        "WMTechnologyDocument239": "机密",
        "WMTechnologyDocument23A": "秘密",
        "WMTechnologyDocument23B": "秘密",
        "WMTechnologyDocument23C": "秘密",
        "WMTechnologyDocument23D": "秘密",
        "WMTechnologyDocument23E": "秘密",
        "WMTechnologyDocument23F": "秘密",
        "WMTechnologyDocument23G": "秘密",
        "WMTechnologyDocument23H": "秘密",
        "WMTechnologyDocument23J": "秘密",
        "WMTechnologyDocument23K": "秘密",
        "WMTechnologyDocument23L": "秘密",
        "WMTechnologyDocument23Z": "秘密",
        //24
        "WMTechnologyDocument241": "机密",
        "WMTechnologyDocument242": "机密",
        "WMTechnologyDocument243": "机密",
        "WMTechnologyDocument244": "机密",
        "WMTechnologyDocument245": "机密",
        "WMTechnologyDocument246": "机密",
        "WMTechnologyDocument247": "秘密",
        "WMTechnologyDocument248": "内部公开",
        "WMTechnologyDocument249": "秘密",
        "WMTechnologyDocument24A": "秘密",
        "WMTechnologyDocument24B": "机密",
        "WMTechnologyDocument24C": "秘密",
        "WMTechnologyDocument24D": "秘密",
        "WMTechnologyDocument24E": "秘密",
        "WMTechnologyDocument24F": "秘密",
        "WMTechnologyDocument24G": "秘密",
        "WMTechnologyDocument24H": "秘密",
        "WMTechnologyDocument24J": "秘密",
        "WMTechnologyDocument24K": "秘密",
        "WMTechnologyDocument24Z": "秘密",
        //25
        "WMTechnologyDocument251": "机密",
        "WMTechnologyDocument252": "机密",
        "WMTechnologyDocument253": "机密",
        "WMTechnologyDocument254": "绝密",
        "WMTechnologyDocument255": "机密",
        "WMTechnologyDocument256": "秘密",

        //237
        "WMTechnologyDocument237W": "秘密",
        "WMTechnologyDocument237Z": "秘密"
    },
    bigProject: [
        {
            name: "图形0",
            value:"type0",
            url: "image/bigProjectImage/type0.jpg",
            timeLine: false,
        }, {
            name: "图形1",
            value: "type1",
            url: "image/bigProjectImage/type1.jpg",
            timeLine: false,
        }, {
            name: "图形2",
            value: "type2",
            url: "image/bigProjectImage/type2.jpg",
            timeLine: false,
        }, {
            name: "图形3",
            value: "type3",
            url: "image/bigProjectImage/type3.jpg",
            timeLine: false,
        }, {
            name: "图形4",
            value: "type4",
            url: "image/bigProjectImage/type4.jpg",
            timeLine: false,
        }, {
            name: "图形5",
            value: "type5",
            url: "image/bigProjectImage/type5.jpg",
            timeLine: false,
        }, {
            name: "图形6",
            value: "type6",
            url: "image/bigProjectImage/type6.jpg",
            timeLine: false,
        }, {
            name: "图形7",
            value: "type7",
            url: "image/bigProjectImage/type7.jpg",
            timeLine: false,
        }, {
            name: "图形8",
            value: "type8",
            url: "image/bigProjectImage/type8.jpg",
            timeLine: false,
        }, {
            name: "图形9",
            value: "type9",
            url: "image/bigProjectImage/type9.jpg",
            timeLine: false,
        }, {
            name: "图形10",
            value: "type10",
            url: "image/bigProjectImage/type10.jpg",
            timeLine: false,
        }, {
            name: "图形11",
            value: "type11",
            url: "image/bigProjectImage/type11.jpg",
            timeLine: false,
        }, {
            name: "图形12",
            value: "type12",
            url: "image/bigProjectImage/type12.jpg",
            timeLine: false,
        }, {
            name: "图形13",
            value: "type13",
            url: "image/bigProjectImage/type13.jpg",
            timeLine: false,
        }, {
            name: "图形14",
            value: "type14",
            url: "image/bigProjectImage/type14.jpg",
            timeLine: false,
        }, {
            name: "图形15",
            value: "type15",
            url: "image/bigProjectImage/type15.jpg",
            timeLine: false,
        }, {
            name: "图形16(时间轴)",
            value: "type16",
            url: "image/bigProjectImage/type16.jpg",
            timeLine: true,
        }, {
            name: "图形17(时间轴)",
            value: "type17",
            url: "image/bigProjectImage/type17.jpg",
            timeLine: true,
        }, {
            name: "图形18(时间轴)",
            value: "type18",
            url: "image/bigProjectImage/type18.jpg",
            timeLine: true,
        }, {
            name: "图形19(时间轴)",
            value: "type19",
            url: "image/bigProjectImage/type19.jpg",
            timeLine: true,
        }, {
            name: "图形20(时间轴)",
            value: "type20",
            url: "image/bigProjectImage/type20.jpg",
            timeLine: true,
        }, {
            name: "图形21(时间轴)",
            value: "type21",
            url: "image/bigProjectImage/type21.jpg",
            timeLine: true,
        }, {
            name: "图形22(时间轴)",
            value: "type22",
            url: "image/bigProjectImage/type22.jpg",
            timeLine: true,
        }, {
            name: "图形23",
            value: "type23",
            url: "image/bigProjectImage/type23.jpg",
            timeLine: true,
        }, {
            name: "图形24",
            value: "type24",
            url: "image/bigProjectImage/type24.jpg",
            timeLine: false,
        }, {
            name: "图形25(时间轴)",
            value: "type25",
            url: "image/bigProjectImage/type25.jpg",
            timeLine: true,
        },
    ],
    sendRequest: function (options) {
        var self = this;

        if (options.contentType == null) {
            options.contentType = "application/x-www-form-urlencoded";
        }

        var async = options.async;

        if (async === undefined) {
            async = true;
        }
        $.ajax({
            "type": "post",
            "async": async,
            "dataType": "json",
            "contentType": options.contentType,
            "url": baas.BASE_URL + options.url,
            "data": options.params,
            "complete": function (xhr) {
                debugger
                //"{"result":0,"isShowInfo":1,"successInfo":"操作成功","errorInfo":"操作失败"}"
                if (xhr.readyState == 4 && xhr.status == 200) {
                    var sessionstatus = xhr.getResponseHeader("sessionstatus");
                    if (sessionstatus == "timeout") {
                        //如果超时就处理 ，指定要跳转的页面
                        baas.showError("session失效,请重新登录");
                        window.location.replace("login.html");
                        return;
                    }
                    var result = xhr.responseJSON;
                    if (result != null) {
                        if (result.result == 1) {
                            if (result.isShowInfo == 1) {
                                self.showSuccess(result.successInfo);
                            }
                            if (options.success) {
                                options.success.call(this, result, xhr);
                            }
                        } else if (result.result == 2) {
                            if (result.isShowInfo == 1) {
                                self.showError(result.errorInfo);
                            }
                            if (options.success) {
                                options.success.call(this, result, xhr);
                            }
                        } else {
                            if (result.isShowInfo == 1) {
                                self.showError(result.errorInfo);
                            }
                        }
                    } else {
                        self.showError("后台未知错误！无返回！");
                    }
                } else {
                    var msg = self.getErrorMsg(xhr);
                    if (options.error) {
                        options.error.call(this, msg, xhr);
                    } else {
                        self.showError(msg);
                    }
                }
            }
        });
    },
    sendFileRequest: function (options) {

        if ($("#" + options.fileElementId)[0].files.length == 0) {
            baas.showError("文件未上传！");
            return
        }
        var self = this;

        var async = options.async;

        if (async === undefined) {
            async = true;
        }
        $.ajaxFileUpload({
            "type": "post",
            "async": async,
            "dataType": "JSON",
            "contentType": options.contentType,
            "secureuri": false,
            "fileElementId": options.fileElementId,
            "url": baas.BASE_URL + options.url,
            "data": options.params,
            "complete": function (xhr) {
                //"{"result":0,"isShowInfo":1,"successInfo":"操作成功","errorInfo":"操作失败"}"
                //if (xhr.readyState == 4 && xhr.status == 200) {
                //    var sessionstatus=xhr.getResponseHeader("sessionstatus");
                //    if(sessionstatus=="timeout"){
                //        //如果超时就处理 ，指定要跳转的页面
                //        baas.showError("session失效,请重新登录");
                //        window.location.replace("login.html");
                //        return;
                //    }
                var result = JSON.parse(xhr.responseText);
                if (result != null) {
                    if (result.result == 1) {
                        if (result.isShowInfo == 1) {
                            self.showSuccess(result.successInfo);
                        }
                        if (options.success) {
                            options.success.call(this, result, xhr);
                        }
                    } else {
                        if (result.isShowInfo == 1) {
                            self.showError(result.errorInfo);
                        }
                    }
                } else {
                    self.showError("后台未知错误！无返回！");
                }
                //} else {
                //    var msg = self.getErrorMsg(xhr);
                //    if (options.error) {
                //        options.error.call(this, msg, xhr);
                //    } else {
                //        self.showError(msg);
                //    }
                //}

            }
        });
    },
    getErrorMsg: function (xhr) {
        return $(xhr.responseText).filter("h1:first").text() || xhr.statusText;
    },

    showError: function (msg) {

    },

    showWarning: function (title, msg) {

    },

    showChoose: function (msg, fun, fun2) {

    },



    showSuccess: function (title, msg) {

    },

    showInfoWindow: function (msg) {

    },

    showInfo: function (title, msg) {

    },




};





