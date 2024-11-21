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

function fn_login() {
	location.href = "/";
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
					<li class="step" >
						<span>STEP 01</span><br>
						<span>비밀번호 재설정</span><br>
					</li>
					<li class="step on">
						<span>STEP 02</span><br>
						<span>비밀번호 재설정 완료</span><br>
					</li>
				</ul>
			</nav>
			<div id="pwReset_wrap">
				<div id="pwReset_inner_step02">
					<ul id="pwReset_complete">
                        <li class="tit">
                            <div class='icon'></div>
                        </li>
                        <li class="textArea">
                            <p class="text_welcome NanumGothic">비밀번호 재설정 완료</p>
                            <p class="text_complete NanumGothic">성공적으로 IEUMSIGN 비밀번호를 재설정 하였습니다.</p>
                        </li>
                        <li class="btn_group"><div class=" NanumGothic" onClick="fn_login()"><a class="btn_type btn_login">로그인 하러 가기 </a></div></li>
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