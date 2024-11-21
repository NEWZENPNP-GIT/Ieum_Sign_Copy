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
		<script type="text/javascript" src="/js/common.js"></script>
		<script>

			//***************					윈도우 로딩					***************//


			$(document).ready(function () {

				$("#logMessage").focus();

				var result   = '${result}';
				var jsonData = JSON.parse(result);

				$("#docuId").val(jsonData.document.docuId);
				$("#bizId").val(jsonData.document.bizId);
				$("#recvUserId").val('${recvUserId}');
				$("#userId").val(jsonData.document.userId);
				$("#recvType").val('${recvType}');
				$("#contactType").val(getCookie("contactType"));
			});


			//***************					정정요청 완료					***************//


			function fn_save() {

				var formData	   = new FormData();
				var url			   = rootPath + "document/forwardDocument.do";
				var docuId		   = $("#docuId").val();
				var recvUserId	   = $("#recvUserId").val();
				var bizId		   = $("#bizId").val();
				var userId		   = $("#userId").val();
				var recvType	   = $("#recvType").val();
				var contactType    = $("#contactType", opener.document).val();
				var logMessage	   = $("#logMessage").val();
				var attachFileList = opener.document.getElementsByName("attachFile");

				if (isNull(logMessage)) alert("정정사유를 입력해주세요.");

				formData.append("docuId", docuId);
				formData.append("recvUserId", recvUserId);
				formData.append("bizId", bizId);
				formData.append("recvStatus", "8");
				formData.append("userId", userId);
				formData.append("recvMessage", logMessage);
				formData.append("recvType", recvType);
				formData.append("contactType", contactType);

				for (var i=0; i<attachFileList.length; i++) {
					if (attachFileList[i].files[0] !== undefined) {
						if (checkSpecial(attachFileList[i].files[0].name)) {
							alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
							return;
						}
						formData.append("ATTACH_FILE["+i+"]",attachFileList[i].files[0]);
					}
				}

				$.ajax({
					url:url,
					crossDomain : true,
					dataType:"json",
					async:false,
					type:"POST",
					processData: false,
					contentType: false,
					data:formData,
					success: function (result) {
						if (result.success === true) {
							alert("정정요청이 등록되었습니다.")
							opener.moveDocumentRejectComplete();
							window.close();
							window.shouldClose = true;
						} else { alert("정정요청중 문제가 발생하였습니다.\r\n계약서 작성자에게 확인 요청드립니다."); }
					},
					error: function (request, status, error) {
						$.unblockUI(); alert("전자문서 정보 전송 중 장애가 발생하였습니다. 잠시 후 다시 진행해주시기 바랍니다.");
					}
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
	<input type="hidden" id="contactType">
	<div id="electronic_layout">
		<div class="popupstylediv">
			<div class="pop_title">
				<h3>정정요청</h3>
			</div>
			<div class="popupdiv03">
				<textarea id="logMessage" name="" rows="" cols=""></textarea>
			</div>
			<div class="footbtnareadiv footcenterdiv">
				<a class="electronic_btn02" onclick="fn_save();">확인</a>
				<a class="electronic_btn03" onclick="window.close();">닫기</a>
			</div>
		</div>
	</div>
	</body>
	</html>