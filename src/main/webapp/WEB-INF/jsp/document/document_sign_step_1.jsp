
	<!--****************************************************************************************************************
	* 상기 프로그램에 대한 저작권을 포함한 직접재산권은 NewZen P&P에 있으며 NewZen P&P가 명시적으로 허용하지 않은
	* 사용, 복사, 변경, 제3자에 의한 공개 배포는 엄격히 금지되며 NewZen P&P의 지적재산권 침해에 해당합니다.
	********************************************************************************************************************
	--------------------------------------------------------------------------------------------------------------------
	* 회사       : 뉴젠피앤피
	* 화면명     : 본인확인 페이지
	* 화면설명   : 전자계약 메일 열기 > 본인확인
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
	<meta http-equiv="content-type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, user-scalable=no">
	<meta http-equiv="Cache-Control" content="no-cache">
	<meta http-equiv="Pragma" content="no-cache">

	<link rel="stylesheet" type="text/css" href="/css/font.css">
	<link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
	<link rel="stylesheet" type="text/css" href="/css/electornic_contract.css">

	<!-- Link Swiper's CSS -->
	<link rel="stylesheet" href="/css/swiper.css">

	<script type="text/javascript" src="/js/jquery.min.js"></script>
	<script type="text/javascript" src="/js/loader.js"></script>
	<script type='text/javascript' src="/js/jquery.bpopup.js"></script>
	<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
	<script type="text/javascript" src="/js/common.js"></script>

	<!-- Swiper JS -->
	<script src="/js/swiper.min.js"></script>

	<!--  개발 URL :  -->
	<!-- <script src="https://scert.mobile-ok.com/resources/js/index.js"></script>  -->
	<!--  운영 URL : -->
	<script src="https://cert.mobile-ok.com/resources/js/index.js"></script>

	<!-- 공인인증서 관련 -->
	<script type="text/javascript" src="/js/nxts/nxts.min.js"></script>
	<script type="text/javascript" src="/js/nxts/nxtspki_config.js"></script>
	<script type="text/javascript" src="/js/nxts/nxtspki.js"></script>

	<!-- 공인인증서 관련 -->
	<script type="text/javascript" src="/js/nxts/nxts.min.js"></script>
	<script type="text/javascript" src="/js/nxts/nxtspki_config.js"></script>
	<script type="text/javascript" src="/js/nxts/nxtspki.js"></script>

	<script type="text/javascript">

		var nextStep = false;
		var id		 = "";
		var authType = "";
		var empPhone = "";

		function getParameterByName(name) {
			name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
			var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"), results = regex.exec(location.search);
			return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
		}


		//***************					본인 인증					***************//


		function startMok() {

			if (!nextStep) { alert("전자문서가 완료된 문서이거나 전자문서서비스 장애로 인하여 진행을 하실 수 없습니다.\r서비스 이용에 불편을 드려 죄송합니다.\n잠시 후 다시 진행해주시기 바랍니다."); return; }

			var contType = $("#contactType").val();
			var param 	 = getParams();
			var url;

			function openPhoneAuth() {
				// var url = "https://ieumsign.newzenpnp.co.kr/mok/mok_std_request.jsp"; // 개발 서버
				var url = "https://ieumsign.co.kr/mok/mok_std_request.jsp"; // 운영 서버

				MOBILEOK.process(url, "WB", "result");
			}

			if (authType === "M" || (authType === "MC" && contType === "P")) {
				openPhoneAuth();
			} else if (authType === "C" || (authType === "MC" && contType === "C")) {
				callSignData();
			} else if (authType === "P") {
				url = rootPath + "document/goDocumentPasswordPad.do?id="+param.id;
				openWin(url, "비밀번호 인증", 400, 200);
			}
			// 본인인증 버튼을 숨기기(로딩중)
			$(".CertBtn").hide();
			$(".CertNotice").show();
		}


		//***************					윈도우 로딩					***************//


		$(document).ready(function() {
			var json   = '${document}';
			var result = $.parseJSON(json);
			id 	   	   = getParameterByName('id');

			if (json !== "null") {

				$("#spanUserId").val(id);
				$("#spanRecvUserName").text(result.recvUserName);
				$("#spanUserId").text(result.userName);
				$("#spanDocumentName").text(result.documentName);
				empPhone = result.phoneNum;
				$("#phoneNum").val(result.phoneNum);
				$("#empName").val(result.recvUserName);
				$("#recvUserId").val(result.recvUserId);
				$("#contactType").val(result.contactType);

				var authTypeMap = {
					"M": "휴대폰 본인인증",
					"C": "공인인증서 본인인증",
					"P": "비밀번호 본인인증"
				};

				authType = result.authType;

				if (authType === "MC") authType = $("#contactType").val() === "P" ? "M" : "C";

				if (!authTypeMap[authType]) location.href = "/document/documentView.do?id="+id;
				else 						nextStep = true;

			} else 		alert("개인정보를 가져올 수 없습니다.\n잠시 후에 다시 시도하세요.");

		});


		//***************					공인인증서 본인인증					***************//


		function callSignData() {
			var options  = {};
			var signdata = [];
			signdata.push(getURLParameters('id'));
			if (signdata.length > 0) nxTSPKI.multiSignData(signdata, options, sign_complete_callback);
		}


		function sign_complete_callback(res) {
			if (res.code === 0) {
				if (res.data.length !==1) { alert("전자서명한 문서의 건수가 맞지 않습니다.\r\n다시 전자서명을 진행해주시기 바랍니다."); return; }
				location.href = "/document/documentView.do?id=" + getURLParameters('id');
			} else { alert(res.errorMessage); }
		}


		//***************					본인인증 정보					***************//


		function result(result) {
			try {
				result = JSON.parse(result);
				document.querySelector("#result").value = JSON.stringify(result, null, 5);

				var checkPhone  = result.userMokVO.userPhone;
				fn_insUserMok(result.userMokVO);

				if (checkPhone === empPhone) {
					alert('본인인증이 완료되었습니다.');
					location.href = "/document/documentView.do?id="+id;
				} else {
					alert('본인인증이 실패하였습니다.');
				}
			} catch (error) {
				alert('에러');
				document.querySelector("#result").value = result;
			}
		}


		//***************					본인인증 정보 저장					***************//


		function fn_insUserMok(userMokVO) {

			var url = rootPath+"user/insUserMok.do";

			$.ajax({
				url:url,
				crossDomain : true,
				dataType:"json",
				type:"POST",
				data: {
					userName: userMokVO.userName,
					siteID: userMokVO.siteID,
					clientTxId: userMokVO.clientTxId,
					txId: userMokVO.txId,
					providerId: userMokVO.providerId,
					serviceType: userMokVO.serviceType,
					ci: userMokVO.ci,
					di: userMokVO.di,
					userPhone: userMokVO.userPhone,
					userBirthday: userMokVO.userBirthday,
					userGender: userMokVO.userGender,
					userNation: userMokVO.userNation,
					reqAuthType: userMokVO.reqAuthType,
					reqDate: userMokVO.reqDate,
					issueDate: userMokVO.issueDate,
					issuer: userMokVO.issuer
				}
			});
		}



	</script>
	</head>

	<body>
	<input type="hidden" id="digitNonce" name="digitNonce" />
	<input type="hidden" id="bizName" name="bizName" />
	<input type="hidden" id="empName" name="empName" />
	<input type="hidden" id="statusCode" name="statusCode" />
	<input type="hidden" id="phoneNum" name="phoneNum" />
	<input type="hidden" id="recvUserId" name="" />
	<input type="hidden" id="contactType" name="contactType" />
		<div id="eleContract_wrap" class="">
			<div class="header">
				<div class=""></div>
				<div class="pagetit">
					<span class="NanumGothic">전자계약</span>
				</div>
			</div>
			<div class="container item-center">
				<div class="contit">
					<span class="tit NanumGothic">본인확인</span>
				</div>
				<div class="con userCheck">
					<ul>
						<li class="icon"></li>
						<li class="text desc gray NanumGothic"> <span id="spanUserId"></span>과(와)
							<span class="sky_blue" id="spanRecvUserName"></span> 님의
							<span id="spanDocumentName"></span> 확인을 위해
							<span id="authType"></span>을 진행합니다<br>
							아래 버튼을 눌러 인증하시기 바랍니다</li>
						<li class="btn_group CertBtn">
							<a class="btn_type Material_icons" style="cursor: pointer;" onclick="startMok()">인증하기</a>
						</li>
						<br>
						<li class="CertNotice" style="display: none">
							<span>본인인증을 진행중입니다.<br>잠시만 기다리시면 계약서 페이지로 이동합니다.</span>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<iframe id="myNullFrame" name="myNullFrame" width="0" height="0"  style="display: none;"></iframe>
		<textarea id="result" rows="20" style="display: none"></textarea>
	</body>
	</html>