
/**
 * 表格选中一行
 * @param obj
 * @param name
 * @param pid
 */
function _setTrFocus(obj,name,pid,E){
	E = E || window.event;
	var target = E.target;
	
	if(target.type == 'checkbox'){
		$(target).parent().parent().attr('class',$(target).attr('checked')?'dataGridTrFocus':'dataGridTr');
	}else{
		$(".dataGridTrFocus").removeClass().addClass('dataGridTr');
		$("input[type='checkbox'][name='"+ name +"']").attr('checked',false);
		var $cb = $(obj).find("input[type='checkbox'][name='"+ name +"']");
		$cb.attr('checked',!$cb.attr('checked'));
		$(obj).removeClass().addClass($cb.attr('checked')?'dataGridTrFocus':'dataGridTr');
	}
	if($("input[type='checkbox'][name='"+ name +"']:checked").size() == $("input[type='checkbox'][name='"+ name +"']").size()) $("#" + pid).attr('checked',true);
	else $("#" + pid).attr('checked',false);
}
/**
 * 表格全选
 * @param obj
 * @param name
 */
function _selectAll(obj,name){
	$("input[type='checkbox'][name='"+ name +"']").attr('checked',obj.checked).each(function(){$(this).parent().parent().removeClass().addClass(obj.checked?'dataGridTrFocus':'dataGridTr');});
}
/**
 * 格式字符串
 * @param S
 * @returns
 */
function getString(S){
	if(S == '' || S == 'null' || S == null || S == undefined) S = '无数据';
	return S;
}
/**
 * 检测是否为空
 * @param S
 */
function StringHasText(S){
	return S != null && S != undefined? S.replace(/\s+/gi,'').length > 0:false;
}

function fileSizeForamt(size){
	if(null == size || size == '') return "0 Bytes";
	
	var unitArr = ["Bytes","KB","MB","GB","TB","PB","EB","ZB","YB"],idx = 0;
	size = parseFloat(size);
	var _size = size / Math.pow(1024,(idx = Math.floor(Math.log(size)/Math.log(1024)))); 
	return _size.toFixed(2) + unitArr[idx];
}

function fileNameSubstr(files){
	return files.substring(files.indexOf('/'),files.length);
}

function getAjaxData(url,data,cb){
	data = data || {};
	$.ajax({
		url:url,
		type:'post',
	    dataType:'json',
	    data:data,
	    success:function(d){
	    	if(typeof(cb) == "function") cb(d);
	    },
	    error:function(e){
	    	console.debug(e);
	    	if(typeof(cb) == "function") cb(e);
	    	Chasonx.Hint.Faild(e.responseText);
	    }
	});
}

function RegexUrl(url){
	var strRegex = /((http|ftp|https|file):\/\/([\w\-]+\.)+[\w\-]+(\/[\w\u4e00-\u9fa5\-\.\/?\@\%\!\&=\+\~\:\#\;\,]*)?)/ig;
	return !!url.match(strRegex);
}

function RegexNumbber(v){
	var regex = /^[\d,]*$/ig;
	return !!v.match(regex);
}

function addLoadHandler(func){
	var load = window.onload;
	if(typeof(load) != 'function'){
		window.onload = func;
	}else{
		load();
		func();
	}
}

function getRandomColor(){
	return '#'+('00000'+(Math.random()*0x1000000<<0).toString(16)).slice(-6);
}

function CCopyObject(obj){
	var nb = new Object();
	__loopObj(obj,nb);
	return nb;
}
function __loopObj(obj,tarObj){
	for(var f in obj){
		if(typeof obj[f] == 'object'){
			tarObj[f] = new Object();
			__loopObj(obj[f],tarObj[f]);
		}else{
			tarObj[f] = obj[f];
		}
	}
}
