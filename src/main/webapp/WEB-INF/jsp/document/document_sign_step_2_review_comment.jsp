
	<!--****************************************************************************************************************
	* 상기 프로그램에 대한 저작권을 포함한 직접재산권은 NewZen P&P에 있으며 NewZen P&P가 명시적으로 허용하지 않은
	* 사용, 복사, 변경, 제3자에 의한 공개 배포는 엄격히 금지되며 NewZen P&P의 지적재산권 침해에 해당합니다.
	********************************************************************************************************************
	--------------------------------------------------------------------------------------------------------------------
	* 회사       : 뉴젠피앤피
	* 화면명     : 전자문서
	* 화면설명   : 전자문서 검토 완료 메세지 팝업창
	* 작성자     :
	* 생성일     :
	--------------------------------------------------------------------------------------------------------------------
	* 수정자     : 이정훈
	* 수정일     : 2023.10.31
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
	<script type="text/javascript" src="/js/common.js"></script>
	<script>

		//***************					정정요청 완료					***************//


		function fn_save() {

			var url				= rootPath + "document/forwardDocument.do";
			var logMessage		= $("#logMessage").val();
			var formData		= new FormData();
			var docuId			= $("#docuId", opener.document).val();
			var recvUserId		= $("#recvUserId", opener.document).val();
			var bizId			= $("#bizId", opener.document).val();
			var userId			= $("#userId", opener.document).val();
			var recvType		= $("#recvType", opener.document).val();
			var contactType		= $("#contactType", opener.document).val();
			var reviewType		= $("#reviewType", opener.document).val();
			var attachFileList	= opener.document.getElementsByName("attachFile");

			formData.append("docuId", docuId);
			formData.append("recvUserId", recvUserId);
			formData.append("bizId", bizId);
			formData.append("recvStatus", "4");
			formData.append("userId", userId);
			formData.append("recvMessage", logMessage);
			formData.append("recvType", recvType);
			formData.append("contactType", contactType);
			formData.append("reviewType", reviewType);

			for (var i=0; i<attachFileList.length; i++) {
				if (attachFileList[i].files[0] !== undefined) {
					if (checkSpecial(attachFileList[i].files[0].name)) {
						alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
						return;
					}
					formData.append("ATTACH_FILE["+i+"]",attachFileList[i].files[0]);
				}
			}

			for (var j=0; j<opener.userFileList.length; j++) {
				if (opener.userFileList[j].value !== undefined) {
					if (checkSpecial(opener.userFileList[j].value.name)) {
						alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
						return;
					}
					formData.append("ATTACH_FILE[" + (attachFileList.length + j) + "]",  opener.userFileList[j].value);
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
						alert("검토가 완료되었습니다.")
						opener.moveDocumentComplete("4");
						window.close();
						window.shouldClose = true;
					} else { alert("검토 완료 처리 중 문제가 발생하였습니다.\r\n계약서 작성자에게 확인 요청드립니다."); }
				},
				error: function (request, status, error) {
					$.unblockUI();
					alert("전자문서 정보 전송 중 장애가 발생하였습니다. 잠시 후 다시 진행해주시기 바랍니다.");
				}
			});
		}

	</script>
	</head>

	<body>
	<div id="electronic_layout">
		<div class="popupstylediv">
			<div class="pop_title">
				<h3>검토 완료 메모</h3>
			</div>
			<div class="popupdiv03">
				<textarea id="logMessage" name="" rows="" cols="" style="height:100px; overflow:auto;"></textarea>
			</div>
			<div class="footbtnareadiv footcenterdiv">
				<a class="electronic_btn02" onclick="fn_save();">확인</a>
				<a class="electronic_btn03">닫기</a>
			</div>
		</div>
	</div>
</body>
</html>