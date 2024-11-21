	<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
	<%@ page import ="java.util.*,java.text.SimpleDateFormat"%>
	<%@ page import ="com.dreamsecurity.crypt.*"%>
	<%@ page import ="java.net.URLEncoder"%>
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

	<script>

		//***************					윈도우 로딩					***************//


		var nextStep = false;
		var id		 = "";
		var empPhone = "";

		$(document).ready(function() {

			var json   = '<%=request.getAttribute("contract")%>';
			var result = $.parseJSON(json);
			id 		   = getParameterByName('id');

			if (json !== "null") {
				console.log(result.data.contractFileName);
				$("#digitNonce").val(id);
				$("#bizName").val(result.data.bizName);
				$("#empName").val(result.data.empName);
				$("#statusCode").val(result.data.statusCode);
				empPhone = result.data.phoneNum;
				$("#phoneNum").val(result.data.phoneNum);
				$("#spanContractFileName").val(result.data.contractFileName);

				$("#spanBizName").text($("#bizName").val());
				$("#spanEmpName").text($("#empName").val());
				$("#spanContractFileName").text($("#spanContractFileName").val());

				// 거래처계약 본인인증 추가에 따른 수정
				$("#authType").val(result.data.authType);
				$("#authCode").val(result.data.authCode);

				nextStep = true;
			} else { alert("개인정보를 가져올 수 없습니다.\n잠시 후에 다시 시도하세요."); }
		});

		function getParameterByName(name) {
			name 	  = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
			var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
					results = regex.exec(location.search);
			return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
		}


		//***************					본인인증					***************//


		function startMok() {

			var authType = $("#authType").val()

			if (authType === "M") {
				if (!nextStep) { alert("전자문서가 완료된 문서이거나 전자문서서비스 장애로 인하여 진행을 하실 수 없습니다.\r서비스 이용에 불편을 드려 죄송합니다.\n잠시 후 다시 진행해주시기 바랍니다."); return; }

				//var url = "https://ieumsign.newzenpnp.co.kr/mok/mok_std_request.jsp";       // 개발 서버
				var url = "https://ieumsign.co.kr/mok/mok_std_request.jsp";     // 운영 서버

				MOBILEOK.process(url, "WB", "result");

			} else if (authType === "C" ) { // 인증타입이 공동인증서 인 경우
				fn_signCert();
			} else if (authType === "P") { // 인증타입이 비밀번호 인 경우
				var param = getParams();
				var url   = rootPath + "contract/goPasswordPad.do?id="+param.id;
				openWin(url, "비밀번호 인증", 400, 200);
			} else if (authType === "N") { // 인증타입이 인증없음 인 경우
				location.href = "/contract/contractPersonEtcView.do?id=" + getURLParameters('id');
			}
			// 본인인증 버튼을 숨기기(로딩중)
			$(".CertBtn").hide();
			$(".CertNotice").show();
		}


		//***************					공인인증서 서명					***************//


		function fn_signCert(){ setTimeout("callSignData();", 1000); }


		//***************					공인인증서 본인인증					***************//


		function callSignData() {
			var options  = {};
			var signdata = [];
			signdata.push(getURLParameters('id'));
			if (signdata.length > 0) nxTSPKI.multiSignData(signdata, options, sign_complete_callback);
		}


		//***************					공인인증서 서명 확인					***************//


		function sign_complete_callback(res) {
			if (res.code === 0) {
				if (res.data.length !==1) {
					alert("전자서명한 문서의 건수가 맞지 않습니다.\r\n다시 전자서명을 진행해주시기 바랍니다.");
					return;
				}
				location.href = "/contract/contractPersonEtcView.do?id=" + getURLParameters('id');
			} else { alert(res.errorMessage); }
		}


		//***************					본인인증 정보					***************//


		function result(result) {
			try {
				result = JSON.parse(result);
				document.querySelector("#result").value = JSON.stringify(result, null, 5);

				var checkPhone = result.userMokVO.userPhone;
				fn_insUserMok(result.userMokVO);

				if (checkPhone === empPhone) {
					alert('본인인증이 완료되었습니다.');
					location.href = rootPath + "contract/contractPersonEtcView.do?id=" + id;
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
	<input type="hidden" id="authType" name="authType" />
	<input type="hidden" id="authCode" name="authCode" />
	<div id="eleContract_wrap" class="">
		<div class="header">
			<div class="logo"></div>
			<div class="pagetit"><span class="NanumGothic">전자문서</span></div>
		</div>
		<div class="container item-center">
			<div class="contit"><span class="tit NanumGothic">본인확인</span></div>
			<div class="con userCheck">
				<ul>
					<li class="icon"></li>
					<li class="text desc gray NanumGothic"><span id="spanBizName"></span>과(와)
						<span class="sky_blue" id="spanEmpName"></span> 님의
						<span id="spanContractFileName"></span> 확인을 위해 본인인증을 진행합니다<br>
						아래 버튼을 눌러 본인인증을 진행하시기 바랍니다</li>
					<li class="btn_group CertBtn"><a class="btn_type Material_icons" style="cursor:pointer;" onclick="startMok()">본인인증</a></li><br>
					<li class="CertNotice" style="display: none"><span>본인인증을 진행중입니다.<br>잠시만 기다리시면 계약서 페이지로 이동합니다.</span></li>
				</ul>
			</div>
		</div>
	</div>
	<iframe id="myNullFrame" name="myNullFrame" width="0" height="0"  style="display: none;"></iframe>
	<textarea id="result" rows="20" style="display: none"></textarea>
	</body>
	</html>