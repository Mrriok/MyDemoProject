<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <style>
        @page {
            margin: 0;
        }
    </style>
</head>
<body style="margin: 0px;
            padding: 0px;
			font: 100% SimSun, Microsoft YaHei, Times New Roman, Verdana, Arial, Helvetica, sans-serif;
            color: #000;">
<div style="height: auto;
			width: 820px;
			min-width: 820px;
			margin: 0 auto;
			margin-top: 20px;
            border: 1px solid #eee;">
    <div style="padding: 10px;padding-bottom: 0px;">
        <!-- handlerName为：当前处理人姓名，未定字段，后续自行修改，-->
        <p style="margin-bottom: 10px;padding-bottom: 0px;">${nickName} 您好：</p>
        <!-- drafterName：流程拟稿/提交人姓名，未定字段，后续自行修改 -->
        <!-- processTitle：流程标题，未定字段，后续自行修改 -->
        <p style="text-indent: 2em; margin-bottom: 10px;">您有一项 《${instName}》的编制计划即将到截止日期，请您登录 中国海洋石油有限公司制度管理系统 进行处理。</p>
        <p style="
			text-align: center;
			font-family: Times New Roman;
			font-size: 20px;
			color: #C60024;
			padding: 20px 0px;
			margin-bottom: 10px;
			font-weight: bold;">请点击进入《中国海洋石油有限公司制度管理系统》：<a hover="color: #DA251D;" style="color: #999;" href="${url}" target="_blank">点击进入</a></p>
        <div class="foot-hr hr" style="margin: 0 auto;
			z-index: 111;
			width: 800px;
			margin-top: 30px;
			border-top: 1px solid #DA251D;">
            <a href="${url}">
                <img src="public/img/background.jpg" border="0" width="220" height="100"/>
            </a>
        </div>
        <div style="text-align: center;
			font-size: 12px;
			padding: 20px 0px;
			font-family: Microsoft YaHei;">
            Copyright &copy;${.now?string("yyyy")}CNOOC LIMITED Management Ststem 中国海洋石油有限公司制度管理系统 All Rights Reserved.
        </div>

    </div>
</div>
</body>
</html>
