<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset= UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=yes, target-densitydpi=medium-dpi" />
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">

<title>전자문서</title>
<link rel="stylesheet" type="text/css" href="/css/font.css">
<link rel="stylesheet" type="text/css" href="/css/member_Join.css">
<script src="/js/jquery.min.js"></script>
<script src="/js/common.js"></script>

<script type="text/javascript">

	$(document).ready(function(){
	
		$("#checkAll").change(function () {
		    $("input:checkbox").prop('checked', $(this).prop("checked"));
		});
		
	});
	
	function fn_candyboxJoin() {	
		location.href="/menu/goMemberJoinUserType.do";
	}
	
	function fn_kakao_login(){
		openWin("${kakaoUrl}", "카카오로그인", 600, 700);
	}
	
	function fn_naver_login(){
		openWin("${naverUrl}", "네이버로그인", 600, 700);
	}
	
	function fn_google_login(){
		openWin("${googleUrl}", "구글로그인", 600, 700);
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
					<li class="step on"><span>STEP 01</span><br> <span>약관동의</span><br></li>
					<li class="step"><span>STEP 02</span><br> <span>사용자 정보</span><br></li>
					<li class="step"><span>STEP 03</span><br> <span>가입완료</span><br></li>
				</ul>
			</nav>
			<!--  mobile-btns 영역 보이려면 hidden 클래스 삭제 -->
			<div class="mobile-btns hidden">
				<a class="btn_type Material_icons" onclick="history.back(); return false;"> <span class="arrow"></span> <span class="text"></span>
				</a>
			</div>
			<!-- end mobile-btns -->
			<div id="terms_wrap">
				<div id="terms_inner_step01">
					<div id="join_title">
						<span class="txt_tit1 NanumGothic">IEUMSIGN</span> <span class="txt_tit2 NanumGothic">회원가입<!--  페이지입니다. --></span>
						<div class="join_candybox01">
							<h3 class="NanumGothic">IEUMSIGN 회원가입</h3>
							<div class="join_btncandy">
								<a href="javascript:fn_candyboxJoin();">이음싸인 회원가입</a>
							</div>

							<div class="sns_joinDiv">
								<h3 class="NanumGothic">SNS 회원가입</h3>
								<ul class="snsbtn">
									<li><a class="NanumGothic naver" onclick="javascript:fn_naver_login();"><span></span>네이버 계정으로 회원가입</a></li>
									<li><a class="NanumGothic kakao" onclick="javascript:fn_kakao_login();"><span></span>카카오 계정으로 회원가입</a></li>
									<li><a class="NanumGothic google" onclick="javascript:fn_google_login();"><span></span>구글 계정으로 회원가입</a></li>
								</ul>
							</div>
						</div>
					</div>


				</div>
				<!-- content -->
			</div>
		</section>
	</div>

</body>
</html>