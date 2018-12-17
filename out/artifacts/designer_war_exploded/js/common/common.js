//var imgURL="http://www.geggee.cn/sfez/pic/";//http://bsciwechat.com:9000:8000/soldier/a/
//var imgURL="http://192.168.174.1:8000/soldier/a/";
//var	jsonURL="http://www.geggee.cn/sfez/php/";
var	jsonURL="http://localhost:8080/soldier/";


var clientVersion="3.0";
var clientDate="2016-01-01";
var jsonData, jsonCallback, positionCallback;
var aPage=[], iMenu, oSwitch={}, oTab={};
var weixinJsApiSign, weixinShareData;

/********* JSON OPERATION *********/

function jsonRequest(file, action, data, callback, message) {
	jsonData = data || {};
	jsonData.file=file;
	jsonData.action=action;
	var login_token=localStorage.getItem("login_token");
	if (login_token) {
		jsonData.login_token=login_token;
		jsonData.client_version=clientVersion;
	}
	if (!getAccount()) {
		jsonData.login_account=1;
	}
	if (urlParam("debug")) {
		console.log(jsonData);
	}
	jsonCallback = callback || $.noop;
	if (isConnected()) {
		loadingMessage(message);
		$.post(jsonURL+file+".json.php", jsonData, onJsonResponse).error(onAjaxError);
	} else {
		alertMessage("请检查网络连接!");
	}
}

function onJsonResponse (response) {
	loadingMessage();
	if (!response) {
		showError("服务器暂时无法返回任何信息");
	} else if (response.error_code==null) {
		showError(response);
	} else if (response.error_code==4) {
		localStorage.removeItem("login_token");
		location.href='login.html';
	} else if (response.error_code==7) {
		onSqlError(response);
	} else if (response.error_code>0) {
		showError(response.error_message);
	} else {
		if (response.account) {
			sessionStorage.setItem("account", JSON.stringify(response.account));
		}
		if (jsonCallback) {
			jsonCallback(response);
		}
	}
}

function onSqlError (response) {
	var msg="Database error:<br>";
	msg+="<br><br>Error:<br>"+response.sql_error;
	msg+="<br><br>Command:<br>"+response.sql_command;
	msg+="<br><br>Parameter:<br>"+response.sql_parameter;
	showError(msg);
}

function onAjaxError(xhr, text, exc) {
	if (xhr && xhr.readyState==0) {
		alertMessage("网络连接异常，请稍后重试");
	} else {
		var msg="";
		msg+="<br>JSON Request Data:<br>"+JSON.stringify(jsonData);
		msg+="<br>XML HTTP Request:<br>"+JSON.stringify(xhr);
		msg+="<br>Status Text:<br>"+text;
		msg+="<br>Exception Handler:<br>"+JSON.stringify(exc);
		showError(msg);
	}
}

function showError(msg) {
	var html="<html><head><link href='css/common.css' rel='stylesheet'/></head><body>";
	html+="<div class='div_page' style='display:block'>";
	html+="<div class='div_page_title'>错误信息</div>";
	html+="<div class='div_page_back' onclick='location=\"index.html\";'></div>";
	html+="<div class='div_error'>"+msg+"</div>";
	html+="</div></body></html>";
	document.write(html);
}

/********* BROWSER OPERATION *********/
function bang() {
	event.stopPropagation();
}

function isAgent(s) {
	var ua = window.navigator.userAgent.toLowerCase();
	s=s.toLowerCase();
	if (ua.indexOf(s)>=0 || urlParam(s)) { 
		return true; 
	} else { 
		return false; 
	} 
}

function isWide() {
	var b=$(window).width()>=640;
	return b;
}


/********* HTML OPERATION *********/

function div(css_class, html_str, on_click, div_id) {
	html_str=html_str || "";
	if (typeof html_str!="string") {
		html_str=html_str.toString();
	}
	var html = '<div class="'+css_class+'"';
	if (div_id!=null) {
		html+=' id="'+div_id+'"';
	}
	if (on_click!=null) {
		if (on_click.indexOf(".html")>=0 && on_click.indexOf("'")==-1) {
			on_click="location.href='"+on_click+"';";	
		}	
		html+=' onclick="'+on_click+'"';
	}
	html+='>'+html_str+'</div>';
	return html;
}

function divNoClass(css_class, html_str, on_click, div_id) {
	html_str=html_str || "";
	if (typeof html_str!="string") {
		html_str=html_str.toString();
	}
	var html = '<div>';
	if (div_id!=null) {
		html+=' id="'+div_id+'"';
	}
	if (on_click!=null) {
		if (on_click.indexOf(".html")>=0 && on_click.indexOf("'")==-1) {
			on_click="location.href='"+on_click+"';";	
		}	
		html+=' onclick="'+on_click+'"';
	}
	html+='>'+html_str+'</div>';
	return html_str;
}

function img(css_class, img_url, on_click, img_id) {
	var html='<img class="'+css_class+'"';
	html+=' src="'+imgURL+img_url+'" ';
	if (on_click!=null) {
		if (on_click.indexOf(".html")>=0 || on_click.indexOf(".jpg")>=0) {	
			on_click="location.href='"+on_click+"';";	}	
		html+=' onclick="'+on_click+'"';
	}
	if (img_id) {
		html+=' id="'+img_id+'"';
	}
	html += ' />';
	return html;
}

function imgNoClass(css_class, img_url, on_click, img_id) {
	var html='<img  id="'+img_id+'" class="'+css_class+'"';
	html+=' src="'+imgURL+img_url+'"';
	if (on_click!=null && on_click != "") {
		if (on_click.indexOf(".html")>=0 || on_click.indexOf(".jpg")>=0) {	
			on_click="location.href='"+on_click+"';";	}	
		html+=' onclick="'+on_click+'"';
	}
//	if (img_id) {
//		html+=' id="'+img_id+'"';
//	}
	html += '>';
	return html;
}

function urlImg(url, def) {
	if (!url && def) {
		url = def;
	}
	if (url && url.indexOf("http")==-1) {
		url = img_prefix + url;
	}
	return url;
}
	
function field(content, width, alignment) {
	var html="";
	for (var i=0; i<content.length; i++) {
		html+='<div class="div_field_item'+(i<content.length-1?' div_field_border':"")+'" ';
		html+='style="width:'+width[i]+';';
		if (alignment) {
			html+=' text-align:'+alignment[i]+';';
		} else {
			html+=' text-align:center';
		}
		html+='">'+content[i]+'</div>';
	}
	return html;
}

function cell(content, width, alignment, color) {
	var html='<div class="div_cell" ';
	html+='style="width:'+width+';';
	html+=' text-align:'+(alignment || "center")+';';
	html+=' color:'+(color || "#606060")+';';
	html+='">'+content+'</div>';
	return html;
}

function sheet(content, width, alignment, color) {
	var html="";
	for (var i=0; i<content.length; i++) {
		html+='<div class="div_row">';
		for (var j=0; j<content[i].length; j++) {
			html+='<div class="div_cell" ';
			html+='style="width:'+width[j]+';';
			if (alignment) {
				html+=' text-align:'+alignment[j]+';';
			} else {
				html+=' text-align:center';
			}
			if (color) {
				html+=' color:'+color[i][j]+';';
			} else {
				html+=' color:#606060;';
			}
			html+='">'+content[i][j]+'</div>';
		}
		html+='</div>';
	}
	return html;
}

function property(label, content) {
	var html="";
	for (var i=0; i<label.length; i++) {
		html+=div("div_property_row", div("div_property_label", label[i]+":")+div("div_property_content", content[i]));
	}
	return html;
}

function format (num) { 
	return (num.toFixed(2) + '').replace(/\d{1,3}(?=(\d{3})+(\.\d*)?$)/g, '$&,');
}

function htmlEncode (str) {
	if (typeof(str)!="string") {
		s=""+str;
	} else if (str==null || str==undefined || str.length==0)	{	
		s=""; 
	} else { 
		s=str.replace(/\n/g, "<br>"); 
		s=s.replace(/&/g, "&amp;");   
		s=s.replace(/</g, "&lt;");   
		s=s.replace(/>/g, "&gt;");   
		s=s.replace(/ /g, "&nbsp;");   
		s=s.replace(/\'/g, "&#39;");   
		s=s.replace(/\"/g, "&quot;");  	
	}
	return s;
}

/********* POP-UP OPERATION *********/

function loadingMessage (msg) {
	var p=$('#div_prompt');
	if (p.is(":animated")) {
		p.stop(true, true);
	}
	p.hide();
	if (msg==null) {
		p.fadeOut();
	} else {
		p.html(div("div_loading_message", msg));
		p.fadeIn();
	}
}

function alertMessage (msg) {
	var p=$('#div_prompt');
	if (p.is(":animated")) {
		p.stop(true, true);
	}
	p.hide();
	if (msg!=null) {
		p.html(div("div_alert_message", msg));
		p.fadeIn().delay(2000).fadeOut();
	}	
}

function showConfirm(label, fx) {
	var html=div("div_confirm_area",
			div("div_confirm_label", label)+
			div("div_confirm_cancel", "取消", "hideConfirm()")+
			div("div_confirm_ok", "确定", "hideConfirm(); "+fx));
	$("#div_confirm").html(html);
	$("#div_confirm").fadeIn();
}

function hideConfirm() {
	$("#div_confirm").fadeOut();
}

/********* COOKIE / PARAMETER *********/

function addCookie(name,value,expiresHours) { 
	var cookieString=name+"="+escape(value); 
	if(expiresHours>0){ 
		var date=new Date(); 
		date.setTime(date.getTime+expiresHours*3600*1000); 
		cookieString=cookieString+"; expires="+date.toGMTString(); 
	} 
	document.cookie=cookieString; 
}
 
function getCookie(name) { 
	var strCookie=document.cookie; 
	var arrCookie=strCookie.split("; "); 
	for(var i=0;i<arrCookie.length;i++) { 
		var arr=arrCookie[i].split("="); 
		if(arr[0]==name) {
			return arr[1];
		}
	} 
	return ""; 
}

function deleteCookie(name) { 
	var date=new Date(); 
	date.setTime(date.getTime()+10000); 
	document.cookie=name+"=v; expires="+date.toGMTString();
}

function urlParam (name, def) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); 
	var r = location.search.substr(1).match(reg);
	if (r != null) { return unescape(r[2]); }
	else {	return def; }
}

function getAccount() {
	var account=sessionStorage.getItem("account");
	return account? JSON.parse(account) : null;
}

/********* NETWORK STATUS *********/

function isConnected() {
// 	if (navigator.connection && !isAgent("micromessenger")) {
// 		return (navigator.connection.type!="none");
// 	}
	return navigator.onLine;
}

function onOffline() {
	alertMessage("网络连接已断开");
	sessionStorage.setItem("disconnected", "1");
}

function onOnline() {
	if (sessionStorage.getItem("disconnected")) {
		alertMessage("网络连接已恢复");
		sessionStorage.removeItem("disconnected");
	}
}

/********* MENU NAVIGATION *********/

function menuSwitch(active) {
	menuOff();
	iMenu = active;
	menuOn();
	pageClear();
	pageGo(active);
}

function menuOn() {
	$("#menu_" + iMenu).attr("class", "div_menu_on");
	var icon=$("#menuImg_"+iMenu);
	var src=icon.attr("src").replace("_off", "_on");
	icon.attr("src", src);
}

function menuOff() {
	if (iMenu) {
		$("#menu_" + iMenu).attr("class", "div_menu_off");
		var icon=$("#menuImg_"+iMenu);
		var src=icon.attr("src").replace("_on", "_off");
		icon.attr("src", src);
	}
}

function toggleSwitch(series, active) {
	var prev=oSwitch[series];
	if (prev!=null) {
		$("#switch_"+series+"_"+prev).removeClass("div_switch_on");
		$("#switch_"+series+"_"+prev).addClass("div_switch_off");
	}
	oSwitch[series]=active;
	$("#switch_"+series+"_"+active).removeClass("div_switch_off");
	$("#switch_"+series+"_"+active).addClass("div_switch_on");
	showVisualAid();
}

/********* PAGE NAVIGATION *********/

function pageClear() {
	if (aPage.length>0 && isWide()) {
		$("#page_"+aPage.pop()).hide();
		aPage=[];
	}
}

function pageGo(to, fx) {
	fx=fx || $.noop;
	if (aPage.length>0) {
		var fr=aPage[aPage.length-1];
		var p0=$("#page_"+fr); 
	}
	aPage.push(to);
	var p1=$("#page_"+to);
	if (isWide() || aPage.length==1){
		if (aPage.length>1) {
			p0.hide();
		}
		p1.fadeIn(300, fx);
	} else {
		var w0=$(window).width(), w1=Math.floor(w0/2);
		var s0={left:0, right:0}, e0={left:-w1, right:w1};
		var s1={left:w0, right:-w0}, e1={left:0, right:0};
		if (p0.is(":animated")) {
			p0.stop(true, true);
		}
		if (p1.is(":animated")) {
			p1.stop(true, true);
		}
		p0.css(s0);
		p1.css(s1);
		p1.show();
		showVisualAid(function(){
			p0.animate(e0, 300, function(){p0.hide(); p0.css(s0); fx(); } );
			p1.animate(e1, 300);
		});
	}
	$(".div_page_back").toggle( aPage.length>1 );
}

function pageBackC(fx) {
	fx=fx || $.noop;
	var fr=aPage.pop();
	var to=aPage[aPage.length-1];
	var p0=$("#page_"+fr), p1=$("#page_"+to);
	if (isWide()) {
		p0.hide();
		p1.fadeIn(300);
		$(".div_page_back").toggle( aPage.length>1 );
	} else {
		var w0=$(window).width(), w1=Math.floor(w0/2);
		var s0={left:"0", right:"0"}, e0={left:w0+"px", right:-w0+"px"};
		var s1={left:-w1+"px", right:w1+"px"}, e1={left:"0", right:"0"};
		if (p0.is(":animated")) {
			p0.stop(true, true);
		}
		if (p1.is(":animated")) {
			p1.stop(true, true);
		}
		p0.css(s0);
		p1.css(s1);
		p1.show();
		showVisualAid(function(){
			p0.animate(e0, 300, function(){p0.hide(); p0.css(s0); menuOff(); fx();} );
			p1.animate(e1, 300);
		});
	}
}

function pageInit(first, aid) {
	if (isWide()) {
		aPage=[];
	} else {
		aPage=["menu"];
	}
	$("#page_menu").fadeIn(300);
	if (isWide() && first) {
		first();
	}
	Origami.fastclick(document.body);
	document.addEventListener("backbutton", onBackButton, false);
	document.addEventListener("online", onOnline, false);	
	document.addEventListener("offline", onOffline, false);
	$(".div_menu_off").on("touchstart", function(){ $(this).attr("class", "div_menu_on"); });
}

function onBackButton() {
	if (aPage.length<=1) {
		goMainMenu();
	} else {
		var fr=aPage.pop(), to=aPage[aPage.length-1];
		$("#page_"+fr).hide();
		$("#page_"+to).show();
		menuOff();
	}
}

function goMainMenu() {
	showVisualAid(function(){
		history.back();
	});
}

function showVisualAid(fx) {
	fx=fx || $.noop;
	var x=window.event.pageX, y=window.event.pageY, s0={}, s1={};
	var enabled=localStorage.getItem("visual_aid");
	if (enabled && x && y) {
		s0.left=(x-40)+"px";
		s0.top=(y-40)+"px";
		s0.width="80px";
		s0.height="80px";
		s0['border-radius']="40px";
		s0.display="block";
		s1.left=x+"px";
		s1.top=y+"px";
		s1.width="0";
		s1.height="0";
		s1['border-radius']="0";
		var p0=$("#div_visual");
		p0.html("<div id='div_visual_aid'></div>");
		var p1=$("#div_visual_aid");
		if (p1.is(":animated")) {
			p1.stop(true, true);
		}
		p1.css(s0);
		p0.show();
		p1.animate(s1, 300, function(){ p0.hide(); fx(); });
	} else {
		fx();
	}
}

function goLink(link, param) {
	if (param) {
		link += "?"+param;
	}
	location.href=link;
}

function goBack() {
	history.back();
}


/********* LOCATION POSITION *********/

function loadPosition(fx) {
	if (navigator.geolocation) {
		loadingMessage("正在获取地理位置信息……请稍候");
		positionCallback=fx || $.noop;
		navigator.geolocation.getCurrentPosition(onPositionResponse, onPositionError, {timeout:8000});
	} else {
		alert("无法获取地理位置信息");
	}
}

function onPositionResponse(res) {
	loadingMessage();
	var pos={};
	pos.longitude=(res.coords.longitude || 0).toFixed(6);
	pos.latitude=(res.coords.latitude || 0).toFixed(6);
	pos.altitude=(res.coords.altitude || 0).toFixed(2);
	pos.accuracy=(res.coords.accuracy || 0).toFixed(2);
	sessionStorage.setItem("position", JSON.stringify(pos));
	if (positionCallback) {
		positionCallback();
	}
}

function onPositionError(res) {
	loadingMessage();
	alertMessage("获取地理位置信息错误<br>("+res.message+")");
}

function getPosition() {
	var position=sessionStorage.getItem("position");
	return JSON.parse(position);
}

/********* WEIXIN *********/

function weixinInit() {
	if (isAgent("micromessenger") && !weixinJsApiSign) {
		jsonRequest("weixin", "load_jsapi", {jsapi_url:location.href}, weixinOnJsApiResponse);
	}
}

function weixinOnJsApiResponse(res) {
	if (res.jsapi_sign) {
		weixinJsApiSign=res.jsapi_sign;
		weixinJsApiSign.jsApiList=['checkJsApi', 'chooseImage', 'uploadImage', 'onMenuShareTimeline', 'onMenuShareAppMessage', 'onMenuShareQQ', 'getLocation'];
		//weixinJsApiSign.debug=true;
		wx.config(weixinJsApiSign);
		wx.ready(weixinOnReady);
	}
}

function weixinShare(title, desc, link, icon) {
	weixinShareData=[];
	weixinShareData.type='link';
	weixinShareData.title=title;
	weixinShareData.desc=desc;
	weixinShareData.link=link;
	weixinShareData.imgUrl=icon;
	weixinShareData.succuss=$.noop;
	weixinShareData.cancel=$.noop;
}

function weixinOnReady() {
	wx.onMenuShareAppMessage(weixinShareData);
	wx.onMenuShareQQ(weixinShareData);
	wx.onMenuShareTimeline(weixinShareData);
}


function weixinGuide(label) {
	if (isAgent("micromessenger")) {
		var browser=isAgent("iPhone OS")? "Safari":"浏览器";
		label=label || "请在"+browser+"中打开";
		$("#div_guide").html(
			div("div_guide_icon")+
			div("div_guide_label", label) );
		$("#div_guide").fadeIn().delay(5000).fadeOut();
	}
}

