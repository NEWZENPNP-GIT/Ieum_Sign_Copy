
	<!--****************************************************************************************************************
	* 상기 프로그램에 대한 저작권을 포함한 직접재산권은 NewZen P&P에 있으며 NewZen P&P가 명시적으로 허용하지 않은
	* 사용, 복사, 변경, 제3자에 의한 공개 배포는 엄격히 금지되며 NewZen P&P의 지적재산권 침해에 해당합니다.
	********************************************************************************************************************
	--------------------------------------------------------------------------------------------------------------------
	* 회사       : 뉴젠피앤피
	* 화면명     : 본인확인 페이지
	* 화면설명   : 전자계약 메일 열기 > 비밀번호 인증
	* 작성자     :
	* 생성일     :
	--------------------------------------------------------------------------------------------------------------------
	* 수정자     : 이정훈
	* 수정일     : 2023.10.27
	* 수정내용   :
	------------------------------------------------------------------------------------------------------------------->

	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<!DOCTYPE html>
	<html>
	<head>
	<meta http-equiv="content-type" content="text/html; charset=utf-8"/>
	<meta name="viewport" content="width=device-width, user-scalable=no">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Pragma" content="no-cache">

	<link rel="stylesheet" type="text/css" href="/css/font.css">
	<link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
	<link rel="stylesheet" type="text/css" href="/css/electornic_contract.css">
	<link rel="stylesheet" type="text/css" href="/css/electronic_layout.css">

	<script type="text/javascript" src="/js/jquery.min.js"></script>
	<script type="text/javascript" src="/js/loader.js"></script>
	<script type='text/javascript' src="/js/jquery.bpopup.js"></script>
	<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
	<script type="text/javascript" src="/js/common.js"></script>
	<style>
		.popupstylediv .pop_title{border-bottom:0px;}
		#electronic_layout .popupdiv02{padding-top:5px;margin-top:5px;}
	</style>
	<script>


		//***************					윈도우 로딩					***************//


		$(document).ready(function() {
			$('#pwd').keypress(function(event) {
				var keycode = (event.keyCode ? event.keyCode : event.which);
				if (keycode === '13') fn_checkAuthCode(1);
				event.stopPropagation();
			});
			$("#pwd").focus();
		});


		//***************					비밀번호 체크					***************//


		function fn_checkAuthCode() {

			var id		 = getURLParameters('id').split("_")[0];
			var url		 = "/document/checkAuthCode.do";
			var authCode = $("#pwd").val();

			if (isNull(authCode)) { alert("비밀번호를 입력해주세요."); return false; }

			$.ajax({
				url:url,
				crossDomain : true,
				dataType:"json",
				type:"POST",
				data: {
					authCode:authCode,
					digitNonce:id
				},
				success:function(result) {
					if (result.success) {
						opener.location.href = "/document/documentView.do?id=" + getURLParameters('id');
						window.close();
					} else { alert("비밀번호가 올바르지 않습니다."); }
				},
				error:function(request,status,error) { goInvalidPage(request, error); }
			});
		}

	</script>
	</head>

	<body>
	<input type="hidden" id="docuId">
	<input type="hidden" id="bizId">
	<input type="hidden" id="recvUserId">
	<input type="hidden" id="userId">
	<input type="hidden" id="recvType">
		<div id="electronic_layout">
			<div class="popupstylediv">
				<div class="pop_title">
					<h3>비밀번호를 입력해주세요</h3>
				</div>
				<div class="popupdiv02">
					<input id="pwd" type="password" name="" class="passwordinput">
				</div>
				<div class="footbtnareadiv footcenterdiv">
					<a class="electronic_btn02" onclick="fn_checkAuthCode()">확인</a>
				</div>
			</div>
		</div>
	</body>
	</html>