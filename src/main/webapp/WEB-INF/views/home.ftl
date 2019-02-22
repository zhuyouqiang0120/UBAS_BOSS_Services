<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="Chasonx">
<title>UBAS-欢迎回来</title>
<link rel="stylesheet" href="${PATH}/resources/skin/main.css"/>
<#include "common/head.ftl">
<script type="text/javascript" src="${PATH}/resources/js/mod/home/index.js"></script>



<style type="text/css">
    .wrap-menu { overflow:auto; width:300px; background:#F6F6F6; font:12px/1.5 Tahoma,Arial,sans-serif}
    .wrap-menu ul{ list-style:none; margin:0; padding:0;}
    .wrap-menu ul li{ text-indent:3em; white-space:nowrap; }
    .wrap-menu ul li h2{ cursor:pointer; height:100%; width:100%; margin:0 0 1px 0; font:12px/31px '宋体'; color:#fff; background:red;}
    .wrap-menu ul li a{ display:block; outline:none; height:25px; line-height:25px; margin:1px 0; color:#1A385C; text-decoration:none;}
    .wrap-menu ul li img{ margin-right:10px; margin-left:-17px; margin-top:9px; width:7px; height:7px; background:url(images/arrow.gif) no-repeat; border:none;}
    .wrap-menu ul li img.unfold{ background-position:0 -9px;}
    .wrap-menu ul li a:hover{ background-color:#ccc; background-image:none;}
</style>



</head>

<body>
<div id="Home_Top">
统一业务代理系统
<span style="font-size:14px;font-weight:bold;color:#56b4ef;margin-left:850px;">用户名：${session.LOGINUSERS}</span>

<input type="button" onclick="UpdateMyPassWd()" class="button btnsmall green" style="font-size:13px;color:yellow;" value="修改密码"/>


<a href='logout'>
<input type="submit" onclick="window.location.href('logout')" style="margin-left:30px;" class="button red" value="退出"/>
</a>
</div>


<div id="Home_Left">

    <span id="returnMenu"></span>
	
	<div class="MenuBox">
		<div class="Title"><i class="icon_home" style="top:4px;position:relative;"></i>菜单</div>
		<div class="MenuItemBox">
			<div class="MenuItem" url="home/demo">Demo</div>
		</div>
	</div>
	<div class="MenuBox">
		<div class="Title"><i class="icon_home" style="top:4px;position:relative;"></i>菜单</div>
		<div class="MenuItemBox">
			<div class="MenuItem" url="home/demo">Demo</div>
		</div>
	</div>
	
	<div class="wrap-menu"></div>
	

	
</div>
<div id="Home_Right"><iframe id="PreviewPanel"></iframe></div>
<div id="Home_Bottom">Universal Business Agent System v0.0.1 build2016</div>
</body>
</html>