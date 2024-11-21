<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">

<link rel="stylesheet" type="text/css" href="/css/font.css">
<link rel="stylesheet" type="text/css" href="/css/member_Join.css">
<script src="/js/jquery.min.js"></script>
<script src="/js/common.js"></script>
<title>전자문서</title>
</head>
<style>
#terms_wrap{
	min-height:0px !important;
}
</style>
<script>
/* opener.location.href="/menu/goMemberJoinLogin.do?userSnsId="+getURLParameters("userSnsId");
window.close(); */
$(document).ready(function(){
	
	var success = "${success}";
	var message = "${message}";
	
	if(success == "Y"){
		//캔디O SNS O
		$("#div2").removeClass("hidden");
	} else if(success == "J"){
		//캔디O SNS O
		$("#div1").removeClass("hidden");
	} else if(success == "N"){
		$("#div3").removeClass("hidden");
		$("#failed").html(message);
	}
	
});

function fn_bizJoin(){
	if(opener) {
		opener.location.href="/menu/goMemberJoin.do";
	} else {
		parent.location.href="/menu/goMemberJoin.do";
	}
	self.close(true);
}

function fn_userJoin(){
	if(opener) {
		opener.location.href="/menu/goMemberJoinUser.do";
	} else {
		parent.location.href="/menu/goMemberJoinUser.do";
	}
	self.close(true);
}

function fn_login(){
	if(opener) {
		opener.location.href="/login.do";
	} else {
		parent.location.href="/login.do";
	}
	self.close(true);
}

function fn_close(){
	self.close(true);
}

</script>
<body>
	<div id="join_wrap">
		<div id="div1" class="hidden">
		<aside id="join_side" class="fr">
		<ul>
			<li><span class="txt_tit1 NanumGothic">SNS로그인</span></li>
		</ul>
		</aside>

		<section id="join_contents" class="fr"> <!--  mobile-btns 영역 보이려면 hidden 클래스 삭제 -->
			<div class="mobile-btns hidden">
				<a class="btn_type Material_icons" onclick="history.back(); return false;"> <span class="arrow"></span> <span class="text"></span>
				</a>
			</div>
			<!-- end mobile-btns -->
			<div id="join_wrap">
	
				<section id="join_contents" class="fr"> <!--  mobile-btns 영역 보이려면 hidden 클래스 삭제 -->
				<div class="mobile-btns hidden">
					<a class="btn_type Material_icons" onclick="history.back(); return false;"> <span class="arrow"></span> <span class="text"></span>
					</a>
				</div>
				<!-- end mobile-btns -->
				<div id="terms_wrap">
					<div id="terms_inner_step01">
						<div id="join_title">
							<span class="txt_tit1 NanumGothic">SNS로그인</span><span class="txt_tit2 NanumGothic">에 성공하였습니다!</span> <br> <br> <br>
							<ul class="join_type_txt01">
								<li class="txt_desc NanumGothic">- 기업 관리자 이신 경우 <span>'기업회원 서비스 가입'</span> 버튼을 눌러주세요.
								</li>
								<li class="txt_desc NanumGothic">- 기업의 임직원 이신 경우 <span>'임직원 서비스 가입'</span> 버튼을 눌러주세요.
								</li>
							</ul>
						</div>
	
	
						<div class="join_btn_Div">
							<ul>
								<li><a class="NanumGothic" onclick="fn_bizJoin();">기업회원 서비스 가입</a> <a class="NanumGothic light" onclick="fn_userJoin();">임직원 서비스 가입</a></li>
								<li></li>
							</ul>
						</div>
	
	
					</div>
					<!-- content -->
				</div>
				</section>
			</div>
		</section>
	</div>



		<div id="div2" class="hidden">
			<aside id="join_side" class="fr">
			<ul>
				<li><span class="txt_tit1 NanumGothic">SNS로그인</span></li>
			</ul>
			</aside>

			<section id="join_contents" class="fr"> <!--  mobile-btns 영역 보이려면 hidden 클래스 삭제 -->
			<div class="mobile-btns hidden">
				<a class="btn_type Material_icons" onclick="history.back(); return false;"> <span class="arrow"></span> <span class="text"></span>
				</a>
			</div>
			<!-- end mobile-btns -->
			<div id="join_wrap">

				<section id="join_contents" class="fr"> <!--  mobile-btns 영역 보이려면 hidden 클래스 삭제 -->
				<div class="mobile-btns hidden">
					<a class="btn_type Material_icons" onclick="history.back(); return false;"> <span class="arrow"></span> <span class="text"></span>
					</a>
				</div>
				<!-- end mobile-btns -->
				<div id="terms_wrap">
					<div id="terms_inner_step01">
						<div id="join_title">
							<span class="txt_tit1 NanumGothic">SNS로그인</span><span class="txt_tit2 NanumGothic">에 성공하였습니다!</span> <br> <br> <br>
							<ul class="join_type_txt01">
								<li class="txt_desc NanumGothic">이미 이음싸인에 가입이 되어있습니다.</li>
								<li class="txt_desc NanumGothic"><span>'로그인하기'</span> 버튼을 클릭하여 로그인을 진행해주세요!</li>
							</ul>
						</div>


						<div class="join_btn_Div">
							<ul>
								<li><a class="NanumGothic" onclick="fn_login();">로그인하기</a></li>
							</ul>
						</div>


					</div>
					<!-- content -->
				</div>
				</section>
			</div>
		</div>
		
		<div id="div3" class="hidden">
			<aside id="join_side" class="fr">
			<ul>
				<li><span class="txt_tit1 NanumGothic">SNS로그인</span></li>
			</ul>
			</aside>

			<section id="join_contents" class="fr"> <!--  mobile-btns 영역 보이려면 hidden 클래스 삭제 -->
			<div class="mobile-btns hidden">
				<a class="btn_type Material_icons" onclick="history.back(); return false;"> <span class="arrow"></span> <span class="text"></span>
				</a>
			</div>
			<!-- end mobile-btns -->
			<div id="join_wrap">

				<section id="join_contents" class="fr"> <!--  mobile-btns 영역 보이려면 hidden 클래스 삭제 -->
				<div class="mobile-btns hidden">
					<a class="btn_type Material_icons" onclick="history.back(); return false;"> <span class="arrow"></span> <span class="text"></span>
					</a>
				</div>
				<!-- end mobile-btns -->
				<div id="terms_wrap">
					<div id="terms_inner_step01">
						<div id="join_title">
							<span class="txt_tit1 NanumGothic">SNS로그인</span><span class="txt_tit2 NanumGothic">에 실패하였습니다!</span> <br> <br> <br>
							<ul class="join_type_txt01">
								<li class="txt_desc NanumGothic">데이터 처리 중 오류가 발생하였습니다.<br></li><br>
								<li class="txt_desc NanumGothic">사유 : <span id="failed"></span></li>
							</ul>
						</div>


						<div class="join_btn_Div">
							<ul>
								<li><a class="NanumGothic" onclick="fn_close();">닫기</a></li>
							</ul>
						</div>


					</div>
					<!-- content -->
				</div>
				</section>
			</div>
		</div>
		
	</body>
</html>