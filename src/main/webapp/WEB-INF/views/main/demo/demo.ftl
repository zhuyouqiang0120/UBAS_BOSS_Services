<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta name="author" content="Chasonx">
<title>Insert title here</title>
<#include "../../common/head.ftl">
<script type="text/javascript" src="${PATH}/resources/js/plugins/datepicker/WdatePicker.js"></script>
<style type="text/css">
#DragMain    {width:200px;height:40px;border:1px solid #000;position:absolute;}
#contextMenu {width:300px;height:300px;border:1px solid red;}
</style>
<script type="text/javascript">
function loading(){
   Chasonx.Wait.Show('稍等几分钟...');
   setTimeout(function(){ Chasonx.Wait.Hide();},3000);
}

function dialogTest(){
	new Chasonx({
		title : '你好',
		html : 'Hello,<font color="red">Jack</font>',
		width : 400,
		height : 500,
		modal : true,
		success : function(){
		
			return true;
		}
	});
}

function dragTest(){
	Chasonx.Drag($("#DragMain")[0]);
}

window.onload = function(){
	Chasonx.Tips.Move({id:'tipsMove',direction:'right',text:'移上去就显示',height:120});
	
	var menu = Chasonx.ContextMenu.init({id:'contextMenu',items:[
         {text:'新建文本',todo:function(){ 
         	alert('新建文本');
         	menu.Hide();
         }},
         {text:'编辑',todo:function(){ alert('编辑');}},
         {hr : true},
         {text:'删除',todo:function(){ alert('删除');}}
     ]});
};	
</script>
</head>
<body>
<p>按钮：</p>
<p>
<input type="button" class="button blue" value="demo"/>
<input type="button" class="button green" value="demo"/>
<input type="button" class="button gray" value="demo"/>
<input type="button" class="button red" value="demo"/>
<input type="button" class="button btnsmall blue" value="demo"/>
<input type="button" class="button group gleft" value="demo"/>
<input type="button" class="button group gcen" value="demo"/>
<input type="button" class="button group gright" value="demo"/>
</p>
<p><input type="text" class="inputText textBrowse" /><input type="button" class="button browse" value="demo"/></p>
<p><span class="badge badge_blue">demo</span><span class="badge badge_upd">demo</span><span class="badge badge_del">demo</span></p>
<p>输入框：</p>
<p>
<input type="text" class="inputText" />
<input type="text" class="inputText textReadyOnly" />
<input type="text" class="inputText inputTextErr" />
<input type="text" onclick="WdatePicker({dateFmt:'yyyy-MM-dd',realFullFmt:'%Date'})" class="inputText Wdate"/>
<p>
<p>提示</p>
<p style="text-align:center;">
<a href="javascript:void(0)" onclick="dialogTest()">Dialog 框</a><br><br>

<a href="javascript:void(0)" onclick="Chasonx.Alert({alertType:'normal',modal : true,success:function(){ alert('Hello'); return true;}})">Alert Normal</a><br><br>
<a href="javascript:void(0)" onclick="Chasonx.Alert({alertType:'warning',modal : true,success:function(){ alert('Hello'); return true;}})">Alert Warning</a><br><br>
<a href="javascript:void(0)" onclick="Chasonx.Alert({alertType:'error',modal : true,success:function(){ alert('Hello'); return true;}})">Alert Error</a><br><br>
<a href="javascript:void(0)" onclick="Chasonx.Alert({alertType:'normal',modal : true})">Alert 提示</a><br><br>
<a href="javascript:void(0)" onclick="Chasonx.Hint.Success('Hello')">提示 Success</a><br><br>
<a href="javascript:void(0)" onclick="Chasonx.Hint.Faild('Hello')">提示 Faild</a><br><br>
<a href="javascript:void(0)" onclick="loading()">Loading</a><br><br>
<a href="javascript:void(0)"  id="tips" onclick="Chasonx.Tips.Show({id:'tips',text:'我的显示位置在上面我的显示位置在上面',height:120})">Top Tips</a><br/><br/>
<a href="javascript:void(0)"  id="tips2" onclick="Chasonx.Tips.Show({id:'tips2',text:'我的显示位置在下面',direction:'bottom'})">Bottom Tips</a><br/><br/>
<a href="javascript:void(0)"  id="tips3" onclick="Chasonx.Tips.Show({id:'tips3',text:'我的显示位置在左面',direction:'left',width:300})">Left Tips</a><br/><br/>
 <a href="javascript:void(0)" id="tips4" onclick="Chasonx.Tips.Show({id:'tips4',text:'我的显示位置在右面',direction:'right',width:300})">Right Tips</a><br/><br/>
 <a href="javascript:void(0)" id="tipsMove" >Move Tips</a><br/><br/>
 
</p>
<p>拖动</p>
<p>
<a href="javascript:void(0)" onclick="dragTest()" >设置拖动</a>
<div id="DragMain"></div>
</p>
<p style="margin-top:80px;">
<p>右键菜单</p>
<div id="contextMenu"></div>
</p>
</body>
</html>