<!DOCTYPE html>
<!--
                                                  
            .rrrsrssrsrrii;:.                     
           .8A&GG8898999999993S5s:                
           ;XGGGGG8889889999999999h.              
           ;XXXXXG888888889898888981              
           iXXXXXGGGG888888888889883.             
           rXXX&XXXXGGGGG88888898888;              
           1&XXX&XXGGG8G88888G898898h     ,ih3889;
           h&XXXXX&XGGG88GGGG8GG88889. ;h9X&A&&&H9  天 一 皇 不
  .,:;;;;:,9&AAA&X&&GGXGGGGX&GGXXGG8838&A&XXXXX&B1  下 入 图 胜
h5998888888&&AA&&&&&XXXGGXXXXXGGGGX&AA&&&X&&&&HBS   风 江 霸 人
89993933338XXXGGXGGG8898888998GXABMMMBHAAAHBBBXs    云 湖 业 生
9399333399888G888888888888GGXAM##M##MBBM#MHXS;      出 岁 谈 一
9399998888G88888888888GGXXXX89GAHBBMBBA3hi,         我 月 笑 场
89898888988899888GGXXXX&&&AG93S5S9HMH8S             辈 催 中 醉
G8GG8GG8GXXXXXX&AAA&88G&&&X899351s9BG95              ，； ， 。
55399933S51s8XX&&X&X33398933S55h1s3X55;           
            h8GGG8G9SS33333S5hhh1r983r            
            :39889955S533SS5h111ssGBG,            
             i39889S3333SS5h11ssi39s,             
              s999883333SS5h11si1Xs               
               h8GXXXGG8935h1srhXS1:              
                1998893S555h113XShh1ri,.          
           r5hr..s9999SS55hh3G8555h1h3hhi   .:;irs
           3Gi,;;;s83S93S38G8S555hh1hSh3s     .,;r
           3S:;;;i:13;SX&XG35SS55hh1h55s          
           89ii;;i;;s3XXG933SSS555hhSSs           
.         .XXiii;;;;iH#X989333SSSS5S3r            
.         :&Xsii;;;i:GBX899993SSSS3S;             
.         ;AAh;iii;;:XX389933SSSSS1,              
          rHH9;ii;;;:8S;;13993SS5i                
          sHH8;iii;;:3hi::;rsrii,                 
,    .  ::5HH9,:::::,91;;.                        
          							
 -->
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="Chasonx">
<title>接口描述</title>
<style type="text/css">
body			{background:#000;color: #ccc; font-family: '微软雅黑';}
label 			{display:inline-block;width:150px;text-align:right;}
.defaultTable   {float:left;width:50%;}
.defaultTable tr:first-child{background:#b1adaf;color:#000;}
.defaultTable tr td{border:1px solid #ccc;font-size: 14px; padding: 4px; text-align: center; font-weight: lighter;}
</style>
</head>
<body>
<h1>UBAS-BOSS 区域管理接口列表：</h1>
<#list ApiDescData as api>
<hr>
<h2><label>接口名称：</label>${(api.desc)!}</h2>
<h3><label>URL： </label>${(api.baseUrl)!}${(api.action)!}</h3>
<h3><label>请求方式： </label>${(api.method)!}</h3>
<h3><label style="float:left;">参数： </label>
<table class="defaultTable" cellspacing="0" cellpadding="0">
	<tr>
	  <td width="20%">参数名</td>
	  <td width="20%">类型</td>
	  <td width="20%">长度</td>
	  <td width="20%">允许为空</td>
	  <td width="20%">描述</td>
	</tr>
	<#list api.paramList as para>
	 <tr>
	 	<td>${(para.fieldName)!}</td>
	 	<td>${(para.type)!}</td>
	 	<td>${(para.minLength)!}-${(para.maxLength)!}</td>
	 	<td>${para.isNull}</td>
	 	<td>${(para.desc)!}</td>
	 </tr>
	</#list>
</table>
</h3>
<div style="clear:both;"></div>
<h3><label>返回格式： </label>${(api.retrunData)!}</h3>
<br>
</#list>
</body>
</html>
