<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset= UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=no">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=yes, target-densitydpi=medium-dpi" />
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">

<title>서비스 이용 약관</title>
<link rel="stylesheet" type="text/css" href="/css/font.css">
<link rel="stylesheet" type="text/css" href="/css/member_Join.css">
<script src="/js/jquery.min.js"></script>
<script src="/js/common.js"></script>
<script type="text/javascript" src="/mobile/assets/cordova/cordova_load.js"></script>

<script type="text/javascript">
	
	function fn_login() {
		if (isNull(appType)) {
			location.href="/login.do";	
		} else {
			window.location.href="/mobile/#/mobile_login_type_idnpw";		
		}
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
				<li><span class="NanumGothic">회원가입</span></li>
			</ul>
		</aside>
		
		<section id="join_contents" class="fr">
			<nav id="stateBar" class="NanumGothic">
				<ul>
					<li class="step" >
						<span>STEP 01</span><br>
						<span>약관동의</span><br>
					</li>
					<li class="step">
						<span>STEP 02</span><br>
						<span>사용자 정보</span><br>
					</li>
					<li class="step on">
						<span>STEP 03</span><br>
						<span>가입완료</span><br>
					</li>
				</ul>
			</nav>
			<!--  mobile-btns 영역 보이려면 hidden 클래스 삭제 -->
			<div class="mobile-btns hidden">
				<a class="btn_type Material_icons" onclick="history.back(); return false;">
					<span class="arrow"></span>
					<span class="text"></span>
				</a>
			</div>
			<!-- end mobile-btns -->
			<div id="terms_wrap">
				<div id="terms_inner_step03">
					<ul id="join_complete">
						<li class="tit">
							<div class='icon'></div>
						</li>
						<li class="textArea">
							<p class="text_welcome NanumGothic">회원가입을 환영 합니다.</p>
							<p class="text_complete NanumGothic">IEUMSIGN 회원가입이 성공적으로 완료되었습니다.</p>
						</li>
						<li class="btn_group"><div class=" NanumGothic"><a class="btn_type btn_login" onClick="fn_login()">로그인 하기 ></a></div></li>
					</ul>
				</div><!-- terms_inner_step02 -->
			</div> <!-- terms_wrap  -->
		</section>
		
	</div>
</body>
</html>