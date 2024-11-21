
	<!--****************************************************************************************************************
	* 상기 프로그램에 대한 저작권을 포함한 직접재산권은 NewZen P&P에 있으며 NewZen P&P가 명시적으로 허용하지 않은
	* 사용, 복사, 변경, 제3자에 의한 공개 배포는 엄격히 금지되며 NewZen P&P의 지적재산권 침해에 해당합니다.
	********************************************************************************************************************
	--------------------------------------------------------------------------------------------------------------------
	* 회사       : 뉴젠피앤피
	* 화면명     : 로그인 페이지
	* 화면설명   :
	* 작성자     :
	* 생성일     :
	--------------------------------------------------------------------------------------------------------------------
	* 수정자     : 이정훈
	* 수정일     : 2024.05.21
	* 수정내용   :
	------------------------------------------------------------------------------------------------------------------->

	<!DOCTYPE html>
	<%@ page language="java" contentType="text/html; charset= UTF-8" pageEncoding="UTF-8"%>
	<html>
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, user-scalable=no">
	<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=yes, target-densitydpi=medium-dpi" />
	<meta name="google-signin-scope" content="">
	<meta name="google-signin-client_id" content="59382001111-j6qe2a2jmv7dmhc34tjku0j3l34hpeb3.apps.googleusercontent.com">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Pragma" content="no-cache">

	<script src="https://apis.google.com/js/platform.js" async defer></script>
	<title>서비스 접속 페이지</title>
	<link rel="stylesheet" type="text/css" href="/css/font.css">
	<link rel="stylesheet" type="text/css" href="/css/login.css">
	<script src="/js/jquery.min.js"></script>
	<script src="/js/jquery.blockUI.js"></script>
	<script src="/js/common.js"></script>
	<script>

		var save_id_check = false;


		function isExplore(showAlert) {
			var agent = navigator.userAgent.toLowerCase();
			if ((navigator.appName === 'Netscape' && navigator.userAgent.search('Trident') !== -1) || (agent.indexOf("msie") !== -1) ) {
				if (showAlert) { alert('본사이트는 Internet Explore를 사용하실 수 없습니다.'); }
				return true;
			} else { return false; }
		}


		$(document).ready(function() {

			// 시스템 정기 점검 공지
			// openWin (rootPath+"html/popup/popup_notice_systemChecking.html","연말정산 접속 긴급 공지","470","570");

			if ('${loginVO}' !== "") {
				var nowDate = new Date();
				setCookie("loginId", '${loginVO.loginId}');
				setCookie("userId", '${loginVO.userId}');
				setCookie("loginName", '${loginVO.userName}');
				setCookie("loginType", '${loginVO.userType}');
				setCookie("bizId", '${loginVO.bizId}');
				setCookie("bizName", '${loginVO.bizName}');
				setCookie("curPoint", '${loginVO.curPoint}');
				setCookie("loginDate", nowDate.format("yyyy.MM.dd hh시mm분ss초"));
				setCookie("yearContractId", '${loginVO.yearContractId}');
				setCookie("febYear", '${loginVO.febYear}');
				setCookie("useContractMenu", '${loginVO.useContract}');
				setCookie("openVoucherType", '${loginVO.openVoucherType}');
				setCookie("useElecDoc", '${loginVO.useElecDoc}');
				setCookie("usePayStub", '${loginVO.usePayStub}');
				setCookie("bizMngYn", '${loginVO.bizMngYn}');
				setCookie("mainBizYn", '${loginVO.mainBizYn}');
				setCookie("deptCode", '${loginVO.deptCode}');
				setCookie("useContractData", '${loginVO.useContractData}');
				location.href = $("#redirect_uri").val();
			}

			$("#userId").keyup(function(event)  { if (event.keyCode == 13) fn_login(); });
			$("#userPwd").keyup(function(event) { if (event.keyCode == 13) fn_login(); });

			var saveId = getCookie("saveId");

			if (saveId.length > 0) { $("#save_email").addClass("on"); save_id_check=true; }

			$('#userId').val(saveId);
			$('#userId').focus();

			if (isExplore(false)) { openWin("/html/popup/popup_ie_end.html", "service", 475, 575); }
		});


		function fn_login() {

			if (isExplore(true)) return;

			var url			 = rootPath+"login.do";
			var userId		 = $("#userId").val();
			var userPwd		 = $("#userPwd").val();
			var redirect_uri = $("#redirect_uri").val();

			if (userId.length === 0 || userPwd.length === 0) { alert("로그인정보를 입력해주시기 바랍니다."); return; }

			$.ajax({
				url:url,
				crossDomain : true,
				dataType:"json",
				type:"POST",
				data: {
					userId:userId,
					userPwd:userPwd
				},
				success:function(result) {
					if (result.success === true) {
						if (save_id_check) setCookie("saveId", userId);
						else 			   setCookie("saveId", "");
						var nowDate = new Date();
						setCookie("loginId", result.loginId);
						setCookie("userId", result.userId);
						setCookie("loginName", result.userName);
						setCookie("loginType", result.userType);
						setCookie("bizId", result.bizId);
						setCookie("bizName", result.bizName);
						setCookie("deptCode", result.deptCode);
						setCookie("curPoint", result.curPoint);
						setCookie("loginDate", nowDate.format("yyyy.MM.dd hh시mm분ss초"));
						setCookie("yearContractId", result.yearContractId);
						setCookie("febYear", result.febYear);
						setCookie("useContractMenu", result.useContractMenu);
						setCookie("openVoucherType", result.openVoucherType);
						setCookie("useElecDoc", result.useElecDoc);
						setCookie("usePayStub", result.usePayStub);
						setCookie("bizMngYn", result.bizMngYn);
						setCookie("mainBizYn", result.mainBizYn);
						setCookie("useContractData", result.useContractData);
						location.href = redirect_uri;
					} else alert(result.message);
				},
				error:function(request,status,error) { alert("입력하신 정보를 다시 확인해주시기 바랍니다."); }
			});
		}


		function fn_requestService() {
			if (isExplore(true)) return;
			location.href = "menu/goMemberJoinLoginType.do";
		}

		function fn_service() {
			if (isExplore(true)) return;
			openWin("http://www.ezsign.co.kr/", "service");
		}

		function fn_findUserId() {
			if (isExplore(true)) return;
			var url 	 = rootPath+"user/findUserId.do";
			var userName = $("#search_id").val();
			var phoneNum = $("#search_phone").val();
			phoneNum 	 = hyphenRemove(phoneNum);
			if (userName.length === 0 || phoneNum.length === 0) { alert("아이디 찾기위한 정보를 입력해주시기 바랍니다."); return; }

			$.blockUI();

			$.ajax({
				url:url,
				crossDomain : true,
				dataType:"json",
				type:"GET",
				data: {
					userName:userName,
					phoneNum:phoneNum
				},
				success:function(result) {
					$.unblockUI();
					if (result.success === true) {
						var userId = "";
						$.each(result.data, function(i, row) { userId += "["+row.userId +"]\n"; });
						if (result.total > 0) alert(userName+"님의 아이디는\n"+userId+"입니다.");
						else 				  alert("입력하신 정보로 아이디를 찾을 수 없습니다.\r\n다시 정확히 입력해주시기 바랍니다.");
					} else alert("입력하신 계정이 존재하지 않습니다.\r\n 정확히 입력해주시기 바랍니다.");
				},
				error:function(request,status,error) { $.unblockUI(); alert("입력하신 정보를 다시 확인해주시기 바랍니다."); }
			});
		}

		function fn_findUserPwd() {
			if (isExplore(true)) return;
			var url 	 = rootPath+"user/findUserPwd.do";
			var userName = $("#search_userName").val();
			var loginId  = $("#search_userId").val();
			var phoneNum = $("#search_userPhone").val();
			phoneNum 	 = hyphenRemove(phoneNum);

			if (userName.length === 0 || phoneNum.length === 0 || loginId.length === 0) { alert("비밀번호 찾기위한 정보를 입력해주시기 바랍니다."); return; }

			$.blockUI();

			$.ajax({
				url:url,
				crossDomain : true,
				dataType:"json",
				type:"GET",
				data: {
					userName:userName,
					userId:loginId,
					phoneNum:phoneNum
				},
				success:function(result) {
					$.unblockUI();
					if (result.success === true) alert("사용자 아이디로 비밀번호 재설정 이메일을 발송하였습니다.\r\n이메일을 확인해주시기 바랍니다.");
					else 					     alert("입력하신 계정이 존재하지 않습니다.\r\n 정확히 입력해주시기 바랍니다.");
				},
				error:function(request,status,error) { $.unblockUI(); alert("입력하신 정보를 다시 확인해주시기 바랍니다."); }
			});
		}


		function fn_requestUserService() {
			if (isExplore(true)) return;
			location.href = "menu/goMemberJoinUser.do";
		}

	</script>
	</head>

	<body oncontextmenu="return false">
	<input type="hidden"  id="redirect_uri" value="/menu/goMainMenu.do" />
		<div id="login_bg">
			<div id="bg_gradient"></div>
			<div id="bg_bottom"></div>
		</div>
		<div id="login_wrap">
			<div id="login_content">
				<div id="tit_box" class="NanumGothic">
					<span>Welcome to the IeumSign.</span>
				</div>
				<div id="btn_box">
					<ul class="NanumGothic">
						<li>
							<a class="btn btn_type btn_join" onClick="fn_requestService();">
								<span>서비스가입</span>
								<span></span>
							</a>
						</li>
						<li>
							<a class="btn btn_type btn_int" onClick="fn_service();">서비스소개</a>
						</li>
					</ul>
				</div>
				<div id="login_box">
					<div id="box_tit" class="NanumGothic">LOGIN</div>
					<div id="input_box">
						<ul>
							<li>
								<label id="label_id" class="input_icon"></label>
								<input type="text" id="userId" placeholder="아이디" class="inputbx NanumGothic">
							</li>
							<li>
								<label id="label_pw" class="input_icon"></label>
								<input type="password" id="userPwd" placeholder="비밀번호"class="inputbx">
							</li>
							<li>
								<div id="save_email" class="btn_type NanumBarunGothic">
									<a>이메일 저장</a>
								</div>
							</li>
							<li>
								<div id="btn_login" class="btn_type Material_icons"  onClick="fn_login();">
									<a>로그인</a>
								</div>
							</li>
							<li></li>
							<li>
							<div id="btn_info" class="NanumGothic">
								<a id="btn_pw" class="btn_type btn_serach">비밀번호 찾기</a>
							</div>
								<span class="NanumGothic division">/</span>
								<div id="btn_info" class="NanumGothic">
									<a id="btn_id" class="btn_type btn_serach">아이디 찾기</a>
								</div>
							</li>
						</ul>
					</div> <!--  input_box -->
				</div>
			</div> <!--  login_content -->
		</div><!--  login_wrap -->

		<!--  POPUP class(숨기기:hidden) -->
		<!-- 아이디 찾기 popup_wrap -->
		<div id="popup_wrap" class="search_id hidden">
			<div id="popup_bg"></div>
			<div id="alert_bg" class="alert_bg">
				<div id="popup_close" class="btn_type col_skyblue"></div>
				<div id="alert_tit">
					<span class="tit_text col_skyblue NanumGothic">아이디 찾기</span>
				</div>
				<div id="alert_inputbx">
					<dl class="input_bx">
						<dt>
							<label class="label_txt NanumGothic">사용자 성명</label>
						</dt>
						<dd>
							<input type="text" id="search_id" value="" class="input_txt">
						</dd>
					</dl>
					<dl class="input_bx">
						<dt>
							<label class="label_txt NanumGothic">휴대폰 번호</label>
						</dt>
						<dd>
							<input type="tel" id="search_phone" value="" class="input_txt">
						</dd>
					</dl>
				</div>
				<div id="alert_btn" class="col_skyblue">
					<ul>
						<li class="btn_type col_skyblue NanumGothic" onClick="fn_findUserId()"><a>확인</a></li>
					</ul>
				</div>
			</div>
		</div><!-- id popup_wrap -->

		<!-- 비밀번호 찾기 popup_wrap -->
		<div id="popup_wrap" class="search_pw hidden">
			<div id="popup_bg"></div>
			<div id="alert_bg" class="alert_bg">
				<div id="popup_close" class="btn_type col_skyblue"></div>
				<div id="alert_tit">
					<span class="tit_text col_skyblue NanumGothic">비밀번호 찾기</span>
				</div>
					<div id="alert_inputbx" class="wide">
					<dl class="input_bx">
						<dt>
							<label class="label_txt NanumGothic">사용자 성명</label>
						</dt>
						<dd>
							<input type="text" id="search_userName" value="" class="input_txt">
						</dd>
					</dl>
					<dl class="input_bx">
						<dt>
							<label class="label_txt NanumGothic">사용자 아이디</label>
						</dt>
						<dd>
							<input type="text" id="search_userId" value="" class="input_txt">
						</dd>
					</dl>
					<dl class="input_bx">
						<dt>
							<label class="label_txt NanumGothic">휴대폰 번호</label>
						</dt>
						<dd>
							<input type="tel" id="search_userPhone" value="" class="input_txt">
						</dd>
					</dl>
				</div>
				<div id="alert_warning_box" class="NanumGothic">
					<p>위의 이메일로 비밀번호를 변경할 수 있는 방법을 안내합니다.</p>
					<p>받은메일함에서 확인이 되지 않으면 스팸 메일함을 확인하여 주시기 바랍니다.</p>
				</div>
				<div id="alert_btn" class="col_skyblue">
					<ul>
						<li class="btn_type col_skyblue NanumGothic" onClick="fn_findUserPwd()">
							<a>찾기</a>
						</li>
					</ul>
				</div>
			</div>
		</div><!-- password  popup_wrap -->

		<script  type="text/javascript">

			$(document).ready(function(){ displaySize(); showPopup(); });

			function fn_kakao_login(){
				if (isExplore(true)) return;
				openWin("${kakaoUrl}", "카카오로그인", 500, 700);
			}

			function fn_naver_login(){
				if (isExplore(true)) return;
				openWin("${naverUrl}", "네이버로그인", 500, 700);
			}

			function fn_google_login() {
				if (isExplore(true)) return;
				openWin("${googleUrl}", "구글로그인", 500, 700);
			}

			$(window).resize(function() { displaySize(); showPopup(); });

			/** e-Mail Toggle **/
			$("#save_email").click(function() {

				if ($(this).hasClass("on")) { $(this).removeClass("on"); }
				else 					 	{ $(this).addClass("on"); save_id_check = true; }
			});

			/** id/pw 찾기 **/
			$("#btn_info > .btn_type").click(function(){
				if (isExplore(true)) return;
				var target = $(this).attr("id").substring(4,6);
				if (target === "id") $("#popup_wrap.search_id").removeClass("hidden");
				else 				 $("#popup_wrap.search_pw").removeClass("hidden");
			});

			$("#popup_close.btn_type").click(function() { $(this).parent().parent().addClass("hidden"); })
			/* <!-- id/pw찾기 --> */

			function displaySize() {
				$("#login_content").css("margin-left",($(document).width()-$("#login_wrap").width())/2+"px");
				$("#input_box > ul").css("margin-left",($("#login_box").width()-$("#input_box > ul").width())/2+"px");
				$("#login_box").css("margin-left",($("#login_content").width()-$("#login_box").width())/2+"px");
				$("#login_bg").css("height",$(document).height()+"px");
				$("#bg_bottom").css("top",($("#login_bg").height()-$("#bg_bottom").height())+"px");
			}

			function showPopup() {
				$("#alert_bg.alert_bg").css("top", Math.max(0, (($(window).height() - $("#alert_bg").outerHeight()) / 2) + $(window).scrollTop()) + "px");
				$("#alert_bg.alert_bg").css("left", Math.max(0, (($(window).width() - $("#alert_bg").outerWidth()) / 2) + $(window).scrollLeft()) + "px");
			}

		</script>
	</body>
	</html>