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
<link rel="stylesheet" type="text/css" href="/css/login.css">
<script src="/js/jquery.min.js"></script>
<script src="/js/jquery.blockUI.js"></script>
<script src="/js/common.js"></script>
<style>
#alert_bg{
	height: 442px !important;
}
.new_join_loginbox{
	margin-left: 0px !important;
}
</style>
</head>
<script>
	function fn_goJoin(){
		location.href="/menu/goMemberJoinUserType1.do";
	}
</script>
<body>
<input type="hidden"  id="redirect_uri" value="/menu/goMainMenu.do" />
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
						<span class="txt_tit1 NanumGothic">SNS </span> <span class="txt_tit2 NanumGothic">로그인 성공</span>
						<ul class="join_type_txt01">
							<li class="NanumGothic">SNS계정과 이음싸인 계정이 연동되어있지 않습니다.</li>
						</ul>

						<!--  로그인박스-->
						<div class="new_join_loginbox">
							<p class="attention NanumGothic">이미 계정이 있으신가요?</p>
							<div id="login_box" class="">
								<div id="input_box">
									<ul>
										<li><label id="label_id" class="input_icon"></label> <input type="text" id="userId" value="" placeholder="아이디" name="id" title="아이디" class="inputbx NanumBarunGothic"></li>
										<li><label id="label_pw" class="input_icon"></label> <input type="password" id="userPwd" value="" placeholder="비밀번호" name="id" title="비밀번호" class="inputbx NanumBarunGothic"></li>
										<li>
											<!-- /** check했을 경우 Class 추가 : on/off **/ -->
											<div id="save_email" class="btn_type NanumBarunGothic">
												<a>이메일 저장</a>
											</div>
										</li>
										<li>
											<div id="btn_login" class="btn_type NanumBarunGothic" onClick="fn_login();">
												<a>로그인</a>
											</div>
										</li>
										<li>
											<div id="btn_info" class="NanumBarunGothic">
												<a id="btn_pw" class="btn_type btn_serach">비밀번호 찾기</a>
											</div> <span class="NanumBarunGothic division">/</span>
											<div id="btn_info" class="NanumBarunGothic">
												<a id="btn_id" class="btn_type btn_serach">아이디 찾기</a>
											</div>

										</li>
									</ul>
								</div>
							</div>
							<!--  //로그인박스-->

							<div class="noaccount">
								<p class="attention NanumGothic">계정이 없으신가요?</p>
								<p class="join_btncandy">
									<a href="javascript:fn_goJoin()" class="NanumGothic">서비스 가입</a>
								</p>
							</div>

						</div>


					</div>
					<!-- content -->
				</div>
		</section>
	</div>

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
                     <dt><label class="label_txt NanumGothic">사용자 성명</label>
                     </dt>
                     <dd><input type="text" id="search_id" value="" class="input_txt"></dd>
                 </dl>
                 <dl class="input_bx">
                     <dt><label class="label_txt NanumGothic">휴대폰 번호</label>
                     </dt>
                     <dd><input type="tel" id="search_phone" value="" class="input_txt"></dd>
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
                     <dt><label class="label_txt NanumGothic">사용자 성명</label>
                     </dt>
                     <dd><input type="text" id="search_userName" value="" class="input_txt"></dd>
                 </dl>
                 <dl class="input_bx">
                     <dt><label class="label_txt NanumGothic">사용자 아이디</label>
                     </dt>
                     <dd><input type="text" id="search_userId" value="" class="input_txt"></dd>
                 </dl>
                 <dl class="input_bx">
                     <dt><label class="label_txt NanumGothic">휴대폰 번호</label>
                     </dt>
                     <dd><input type="tel" id="search_userPhone" value="" class="input_txt"></dd>
                 </dl>
            </div>
            <div id="alert_warning_box" class="NanumGothic">
                <p>위의 이메일로 비밀번호를 변경할 수 있는 방법을 안내합니다.</p>
                <p>받은메일함에서 확인이 되지 않으면 스팸 메일함을 확인하여 주시기 바랍니다.</p>
            </div>
            <div id="alert_btn" class="col_skyblue">
                <ul>
                    <li class="btn_type col_skyblue NanumGothic" onClick="fn_findUserPwd()"><a>찾기</a></li>
                </ul>
            </div>
        </div>
    </div><!-- password  popup_wrap -->


	<script type="text/javascript">
		/** e-Mail Toggle **/
		var save_id_check = false;
		
		$(document).ready(function(){
			displaySize();
			showPopup();
			
			$("#userId").keyup(function(event){
			    if(event.keyCode == 13){
			    	fn_login();
			    }
			});
			$("#userPwd").keyup(function(event){
			    if(event.keyCode == 13){
			    	fn_login();
			    }
			});
			
			var saveId = getCookie("saveId");

			if(saveId.length>0) {
				$("#save_email").addClass("on");
				save_id_check=true;
			}
		    $('#userId').val(saveId);
		    $('#userId').focus();
		});
		
		
		$("#save_email").click(function() {

			if ($(this).hasClass("on")) {
				$(this).removeClass("on");
			} else {
				/** checked true **/
				$(this).addClass("on");
			}
		});
		
		/** id/pw 찾기 **/
        $("#btn_info > .btn_type").click(function(){
        	var target=$(this).attr("id").substring(4,6);
        	
        	if(target=="id"){
        		$("#popup_wrap.search_id").removeClass("hidden");
        	}else{
        		$("#popup_wrap.search_pw").removeClass("hidden");
        	}
        });
        
        $("#popup_close.btn_type").click(function(){
        	$(this).parent().parent().addClass("hidden");
        });
        
        function displaySize(){

	        $("#login_bg").css("height",$(document).height()+"px");
			$("#bg_bottom").css("top",($("#login_bg").height()-$("#bg_bottom").height())+"px");
		}
		
		function showPopup(){
			$("#alert_bg").css("height", "500px");
	        $("#alert_bg.alert_bg").css("top", Math.max(0, (($(window).height() - $("#alert_bg").outerHeight()) / 2) + $(window).scrollTop()) + "px");
	        $("#alert_bg.alert_bg").css("left", Math.max(0, (($(window).width() - $("#alert_bg").outerWidth()) / 2) + $(window).scrollLeft()) + "px");
	    }
		
		function fn_findUserId() {
			
			var url = rootPath+"user/findUserId.do";
			var userName = $("#search_id").val();
			var phoneNum = $("#search_phone").val();
			phoneNum = hyphenRemove(phoneNum);
			if (userName.length==0 || phoneNum.length==0) {
				alert("아이디 찾기위한 정보를 입력해주시기 바랍니다.");
				return;
			}

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
				success:function(result)
				{
					$.unblockUI();
					if (result.success==true) {

						var userId = "";
						$.each(result.data, function(i, row) {
							userId += "["+row.userId +"]\n";
						});
						if(result.total>0) {
							alert(userName+"님의 아이디는\n"+userId+"입니다.");
						} else {
							alert("입력하신 정보로 아이디를 찾을 수 없습니다.\r\n다시 정확히 입력해주시기 바랍니다.");
						}
					} else {
						alert("입력하신 계정이 존재하지 않습니다.\r\n 정확히 입력해주시기 바랍니다.");
					}
				},
				error:function(request,status,error){
					$.unblockUI();
					alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
			        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});
		}
		
		function fn_findUserPwd() {
			
			var url = rootPath+"user/findUserPwd.do";
			var userName = $("#search_userName").val();
			var loginId = $("#search_userId").val();
			var phoneNum = $("#search_userPhone").val();
			phoneNum = hyphenRemove(phoneNum);
			if (userName.length==0 || phoneNum.length==0 || loginId.length==0) {
				alert("비밀번호 찾기위한 정보를 입력해주시기 바랍니다.");
				return;
			}

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
				success:function(result)
				{
					$.unblockUI();
					if (result.success==true) {
						alert("사용자 아이디로 비밀번호 재설정 이메일을 발송하였습니다.\r\n이메일을 확인해주시기 바랍니다.");
					} else {
						alert("입력하신 계정이 존재하지 않습니다.\r\n 정확히 입력해주시기 바랍니다.");
					}
				},
				error:function(request,status,error){
					$.unblockUI();
					alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
			        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});
		}
		
		function fn_login() {
			
			var url = rootPath+"/sns/snsJoinLogin.do";
			var userId = $("#userId").val();
			var userPwd = $("#userPwd").val();
			var redirect_uri = $("#redirect_uri").val();
			
			if (userId.length==0 || userPwd.length==0) {
				alert("로그인정보를 입력해주시기 바랍니다.");
				return;
			}
			
			var userSnsId = getURLParameters("userSnsId");
			
			$.ajax({
				url:url,
			    crossDomain : true,
				dataType:"json",
				type:"POST",
				data: {
					userId:userId,
					loginId:userId,
					userPwd:userPwd,
					userSnsId:userSnsId
				},
				success:function(result)
				{
					if (result.success==true) {
						if(result.returnCode == "100"){
							var today = new Date();
							if(save_id_check) {						
								setCookie("saveId", userId, new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)),'/');
							} else {
								setCookie("saveId", "", new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)),'/');
							}
							var nowDate = new Date();
							setCookie("loginId", result.loginId, new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)),'/');
							setCookie("userId", result.userId, new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)),'/');
							setCookie("loginName", result.userName, new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)),'/');
							setCookie("loginType", result.userType, new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)),'/');
							setCookie("bizId", result.bizId, new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)),'/');
							setCookie("bizName", result.bizName, new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)),'/');
							setCookie("curPoint", result.curPoint, new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)),'/');
							setCookie("loginDate", nowDate.format("yyyy.MM.dd hh시mm분ss초"), new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)),'/');
							setCookie("yearContractId", result.yearContractId, new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)),'/');
							setCookie("febYear", result.febYear, new Date(today.getTime() + (1 * 24 * 60 * 60 * 1000)),'/');
							
							alert("SNS계정과 이음싸인 계정 연동이 완료되었습니다. 메인페이지로 이동합니다.");
							location.href=redirect_uri;
						} else if(result.returnCode == "-100"){
							alert("[SNS로그인] 접근토큰 조회에 실패하였습니다.");
						} else if(result.returnCode == "-200"){
							alert("[SNS로그인] 이미 이음싸인 계정과 연동이 완료된 SNS계정입니다. 메인페이지로 이동합니다.");
							location.href=redirect_uri;
						} else if(result.returnCode == "-300"){
							alert("[SNS로그인] SNS종류가 올바르지 않습니다.");
						} else if(result.returnCode == "-500"){
							alert("[SNS로그인] SNS로그인 후 접속하실 수 있습니다.");
							location.href="/login.do";
						}
						
						
					} else {
						alert(result.message);
					}
				},
				error:function(request,status,error){
					alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
			        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			});
		}
	</script>
</body>
</html>
