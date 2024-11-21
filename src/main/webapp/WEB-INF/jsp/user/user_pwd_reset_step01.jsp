<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset= UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=no">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=yes, target-densitydpi=medium-dpi" />
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">

<title>비밀번호 재설정</title>
<link rel="stylesheet" type="text/css" href="/css/font.css">
<link rel="stylesheet" type="text/css" href="/css/member_pwReset.css">
<script src="/js/jquery.min.js"></script>
<script src="/js/common.js"></script>
<script>

function fn_resetUserPwd() {
	if(!confirm("비밀번호를 재설정하시겠습니까?")) return;
	
	var url = rootPath+"user/resetUserPwd.do";
	
	var userNonce = getURLParameters("id");
	var userPwd = $("#userPwd").val();
	var userPwd2 = $("#userPwd2").val();
	if(userPwd.length==0) {
		alert("비밀번호를 입력해주시기 바랍니다.");
		$("#userPwd").focus();
		return;
	}
	if(userPwd2.length==0) {
		alert("비밀번호확인을 입력해주시기 바랍니다.");
		$("#userPwd2").focus();
		return;
	}
	if(!checkPassword(userPwd)) {
		return;
	}
	if(userPwd!=userPwd2) {
		alert("입력하신 비밀번호가 맞지 않습니다.\r\n다시 비밀번호를 입력해주시기 바랍니다.");
		return;
	}

	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		data: {
			userNonce:userNonce,
			userPwd:userPwd
		},
		success:function(result)
		{
			if (result.success==true) {
				document.location.href="/menu/goUserPwd2.do";
			} else {
				alert("입력하신 정보로 비밀번호 재설정을 진행할 수 없습니다.\r\n다시 입력해주시기 바랍니다.\r\nMessage:"+result.message);
			}
		},
		error:function(request,status,error){
			alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
	        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
		}
	});
}
</script>
</head>
<body>
	<header id="join_header">
		<nav>
			<ul>
				<li class="logo"></li>
			</ul>
		</nav>
	</header>
	
	<div id="join_wrap">
		<aside id="join_side" class="fr">
			<ul>
				<li><span class="NanumGothic">비밀번호 재설정</span></li>
			</ul>
		</aside>
		
		<section id="pwReset_contents" class="fr">
			<nav id="stateBar" class="NanumGothic">
				<ul>
					<li class="step on" >
						<span>STEP 01</span><br>
						<span>비밀번호 재설정</span><br>
					</li>
					<li class="step">
						<span>STEP 02</span><br>
						<span>비밀번호 재설정 완료</span><br>
					</li>
				</ul>
			</nav>
			<div id="pwReset_wrap">
				<div id="pwReset_inner_step01">
					<ul id="pwReset_info">
						<!-- user_info:단구성 클래스 -> column1 / column2 -->
						<!-- dl -> label과 input박스 set -->
						<li><div class="icon outline-settings"></div><div class="info_tit NanumGothic">비밀번호 재설정</div></li>
						<li class="info input_bx">
							
								<label class="label_txt NanumGothic">비밀번호</label>
								<input id="userPwd" type="password"class="input_txt size06">
						</li>
						<li class="info input_bx">
								<label class="label_txt NanumGothic">비밀번호확인</label>
								<input id="userPwd2" type="password"class="input_txt size06">
								<span class="box_dec pwCheck NanumGothic"></span>
						</li>
						<li class="caution">
						       <span class="label_txt NanumGothic">※ 비밀번호는 영문,숫자 혼합하여 5자리~15자리 이내로 입력해주세요.</span>
						</li>
						<li class="btn_area">
							<div class="btn_type Material_icons" onClick="fn_resetUserPwd();"><a class="check">확인</a></div>
                        </li>
					</ul>
			</div><!-- content -->
			</div>
		</section>
		<!--  mobile-btns 영역 보이려면 hidden 클래스 삭제 -->
		<div class="mobile-btns hidden"></div>
	</div><!-- join_wrap -->
	
	<!--  POPUP class(숨기기:hidden) -->
	<div id="popup_wrap" class="hidden">
		<div id="popup_bg"></div>
		<div id="alert_bg">
			<div id="popup_close" class="btn_type col_black"></div>
			<div id="alert_img"><img src="/css/image/alert_icon_warning.png"></div>
			<div id="alert_text"><span class="NanumGothic col_red">사용할수 없는 아이디입니다.</span></div>
			<div id="alert_btn"  class="col_red NanumGothic">
				<ul>
					<li class="btn_type col_red"><a>확인</a></li>
				</ul>
			</div>
		</div>
	</div><!-- popup_wrap -->
	
	<script  type="text/javascript">
	$(document).ready(function(){
		showPopup();
	});
	function showPopup(){
		$("#alert_bg").css("top", Math.max(0, (($(window).height() - $("#alert_bg").outerHeight()) / 2) + $(window).scrollTop()) + "px");
		$("#alert_bg").css("left", Math.max(0, (($(window).width() - $("#alert_bg").outerWidth()) / 2) + $(window).scrollLeft()) + "px");
	}
	</script>
</body>
</html>