<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset= UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=no">
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=yes, target-densitydpi=medium-dpi" />
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">

<meta name="google-signin-scope" content="">
<meta name="google-signin-client_id" content="59382001111-j6qe2a2jmv7dmhc34tjku0j3l34hpeb3.apps.googleusercontent.com">
<script src="https://apis.google.com/js/platform.js" async defer></script>
<title>서비스 접속 페이지</title>
<link rel="stylesheet" type="text/css" href="/css/font.css">
<link rel="stylesheet" type="text/css" href="/css/login.css">
<script src="/js/jquery.min.js"></script>
<script src="/js/jquery.blockUI.js"></script>
<script src="/js/common.js"></script>
<style>
</style>
<script type="text/javascript">
	var save_id_check = false;
	
	$(document).ready(function() {

		// 시스템 정기 점검 공지
		// openWin (rootPath+"html/popup/popup_notice_systemChecking.html","연말정산 접속 긴급 공지","470","570");
		
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
		
		var saveWelId = getCookie("saveWelId");

		if(saveWelId.length>0) {
			$("#save_email").addClass("on");
			save_id_check=true;
		}
	    $('#userId').val(saveWelId);
	    $('#userId').focus();
	    
	});
	function fn_login() {
		
		var url = rootPath+"welmoney/login.do";
		var userId = $("#userId").val();
		var userPwd = $("#userPwd").val();
		var redirect_uri = rootPath+$("#redirect_uri").val();
		
		if (userId.length==0 || userPwd.length==0) {
			alert("로그인정보를 입력해주시기 바랍니다.");
			return;
		}
		
		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"POST",
			data: {
				userId:userId,
				userPwd:userPwd
			},
			success:function(result)
			{
				if (result.success==true) {
					if(save_id_check) {						
						setCookie("saveWelId", userId);
					} else {
						setCookie("saveWelId", "");
					}
					location.href=redirect_uri;
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
</head>

<body oncontextmenu="return false">
<input type="hidden"  id="redirect_uri" value="welmoney/memberPage.do" />
	<div id="login_bg">
		<div id="bg_gradient"></div>
		<div id="bg_bottom"></div>
	</div>
	<div id="login_wrap">
		<div id="login_content">
			<div id="tit_box" class="NanumGothic"><span>WelMoney Admin Login.</span></div>
			<div id="login_box2">			
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
						<!-- /** check했을 경우 Class 추가 : on/off **/ -->
							<div id="save_email" class="btn_type NanumBarunGothic"><a>이메일 저장</a></div>
						</li>
						<li>
							<div id="btn_login" class="btn_type Material_icons"  onClick="fn_login();"><a>로그인</a></div>
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

	<script  type="text/javascript">
		$(document).ready(function(){
			displaySize();
			showPopup();
		});
		
		function fn_kakao_login(){
			openWin("${kakaoUrl}", "카카오로그인", 500, 700);
		}
		
		function fn_naver_login(){
			openWin("${naverUrl}", "네이버로그인", 500, 700);
		}
		
		function fn_google_login(){
			openWin("${googleUrl}", "구글로그인", 500, 700);
		}
        
		$(window).resize(function() {
	        displaySize();
	        showPopup();
	    });
		
		/** e-Mail Toggle **/
		$("#save_email").click(function(){
			
	        if($(this).hasClass("on")){
	            $(this).removeClass("on");
	        }else{
	        	/** checked true **/
	            $(this).addClass("on");
	            save_id_check = true;
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
        })
        /* <!-- id/pw찾기 --> */

		function displaySize(){
			//if($(document).width()>360){
			$("#login_content").css("margin-left",($(document).width()-$("#login_wrap").width())/2+"px");
	        $("#input_box > ul").css("margin-left",($("#login_box2").width()-$("#input_box > ul").width())/2+"px");
	        $("#login_box2").css("margin-left",($("#login_content").width()-$("#login_box2").width())/2+"px");

	        $("#login_bg").css("height",$(document).height()-1+"px");
			$("#bg_bottom").css("top",($("#login_bg").height()-$("#bg_bottom").height())+"px");
		}
		
		function showPopup(){
	        $("#alert_bg.alert_bg").css("top", Math.max(0, (($(window).height() - $("#alert_bg").outerHeight()) / 2) + $(window).scrollTop()) + "px");
	        $("#alert_bg.alert_bg").css("left", Math.max(0, (($(window).width() - $("#alert_bg").outerWidth()) / 2) + $(window).scrollLeft()) + "px");
	    }
	</script>
</body>
</html>