<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<script>
$(document).ready(function(){
	
	var success = "${success}";
	var message = "${message}";
	var type = "${type}";
	
	if(success == "Y"){
 		if(opener) {
			if(type == "MYPAGE") {
				if(typeof(opener.getUserInfo) == "undefined"){
					//세션이 있으면서 재 로그인을 시도한 경우
					opener.location.href="/menu/goMainMenu.do";
				} else {
					opener.getUserInfo();
				}
			} else {

				var today = new Date();
				setCookie("loginId", "${loginId}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("userId", "${userId}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("loginName", "${userName}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("loginType", "${userType}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("bizId", "${bizId}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("bizName", "${bizName}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("curPoint", "${curPoint}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("loginDate", today.format("yyyy.MM.dd hh시mm분ss초"),new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("yearContractId", "${yearContractId}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("febYear", "${febYear}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("useContractMenu", "${useContractMenu}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				
				opener.location.href="/menu/goMainMenu.do";
			}
		} else {
			if(type == "MYPAGE") {
				if(typeof(opener.getUserInfo) == "undefined"){
					parent.location.href="/menu/goMainMenu.do";
				} else {
					parent.getUserInfo();
				}
			} else {

				var today = new Date();
				setCookie("loginId", "${loginId}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("userId", "${userId}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("loginName", "${userName}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("loginType", "${userType}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("bizId", "${bizId}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("bizName", "${bizName}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("curPoint", "${curPoint}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("loginDate", today.format("yyyy.MM.dd hh시mm분ss초"),new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("yearContractId", "${yearContractId}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				setCookie("febYear", "${febYear}",new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)), '/');
				
				parent.location.href="/menu/goMainMenu.do";
			}
		}
		self.close(true);
		
	} else if(success == "J"){
		//캔디O SNS O
		$("#div1").removeClass("hidden");
	} else if(success == "N"){
		$("#div3").removeClass("hidden");
		$("#failed").html(message);
	}
	
});

function fn_linkUser(){
	if(opener) {
		opener.location.href="/menu/goMemberJoinLogin.do";
	} else {
		parent.location.href="/menu/goMemberJoinLogin.do";
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
								<li class="txt_desc NanumGothic">- SNS 계정과 연동된 이음싸인 계정이 존재하지 않습니다.
								</li>
								<li class="txt_desc NanumGothic">-  <span>'계정연동하기'</span> 버튼을 클릭하여 이음싸인 계정과 SNS계정을 <br>연동해주세요!
								</li>
							</ul>
						</div>
	
	
						<div class="join_btn_Div">
							<ul>
								<li><a class="NanumGothic" onclick="fn_linkUser();">계정연동하기</a></li>
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