﻿﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="user-scalable=no, width=device-width, initial-scale=1, maximum-scale=1">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">

<link rel="stylesheet" type="text/css" href="/css/font.css">
<link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
<link rel="stylesheet" type="text/css" href="/css/electornic_contract.css">

<script src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/jquery.bootpag_candy.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/ezsign/session_storage.js"></script>
<script type="text/javascript" src="/js/postcode.v2.js"></script>

<link rel="stylesheet" type="text/css" href="/css/font.css">
<link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
<link rel="stylesheet" type="text/css" href="/css/document_input.css">
<script>
	
	var multiData = "";
	var element_layer = "";
	
	$(document).ready(function() {
		
	    // 스코롤 레이어
		var currentPosition = parseInt($("#txtArea").css("top")); 
		$(window).scroll(function() { 
			var position = $(window).scrollTop(); 
			$("#txtArea").stop().animate({"top":position+currentPosition+"px"},0); 
		});
		
		var json = '<%=request.getAttribute("contract")%>';
		var formJson = '<%=request.getAttribute("formList")%>';
		var result = $.parseJSON(json);
		var formResult = $.parseJSON(formJson);

		$(".spanBizName").text(result.bizName);
		$(".spanEmpName").text(result.empName);
		$(".spanContractFileName").text(result.contractFileName);

		// $.each(formResult, function(i, v) {
		// 	if(isNotNull(v.formDataValue)){
		// 		console.log(v.formDataId+", "+v.formDataValue);
		// 		$("#"+v.formDataId+"").val(v.formDataValue);
		// 	}
		// });
		// $("#TXT_00001").val(result.empName);
		
		// if(isNotNull(result.comment)){
		// 	alert("[담당자 메모]\r\n"+result.comment.replace(/<br>/g, "\r\n"));
		// }
		
		element_layer = document.getElementById('layer');
		
	});
	
	function getParameterByName(name) {
	    name = name.replace(/[\[]/, "\\[").replace(/[\]]/, "\\]");
	    var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
	        results = regex.exec(location.search);
	    return results === null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
	}
	
	function fn_saveContract() {

		var url = rootPath+"contract/saveContract.do";
		var id = getParameterByName('id');
		var reg = /(.*?)\.(jpg|jpeg|png|gif|bmp|tif|JPG|JPEG|PNG|GIF|BMP|TIF)$/;
		
		console.log(multiData);
		// 데이터설정
		var formData = new FormData();
		formData.append("digitNonce",id);
		formData.append("multiData",multiData);

		$.blockUI();
		
		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"POST",
			data:formData,
			contentType:false,
			processData:false,
			cache:false,
			success:function(result)
			{
				if(result.success){
					//완료페이지로 이동
					fn_contractView();
				} else {
					alert("정보 등록 중 오류가 발생하였습니다. \r\n 오류메세지 : "+result.message);
					$.unblockUI();
				}
			},
			error:function(request,status,error){
				$.unblockUI();
		        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});	
	}
	
	function fn_goNextPage(){
		
		var txt00009 = $.trim($("#TXT_00009 :selected").val());
		var txt00010 = $.trim($("#TXT_00010 :selected").val());


		if(txt00009.length == 0){
			alert("결제일을 선택해주세요.");
			return false;
		}
		if(txt00010.length == 0){
			alert("가입료를 선택해주세요.");
			return false;
		}
		
		multiData = "";
		multiData += "TXT_00009-"+txt00009+"|";
		multiData += "TXT_00010-"+txt00010;
		
		fn_saveContract();
	}

	function fn_contractView() {
		var id = getParameterByName('id');
		location.href=rootPath+"contract/contractPersonView.do?id="+id;
	}

</script>
</head>

<body>
	<div id="layer" style="display:none;position:fixed;overflow:hidden;z-index:1;-webkit-overflow-scrolling:touch;
		<img src="/css/image/contract/close.png" id="btnCloseLayer" style="cursor:pointer;position:absolute;right:-3px;top:-3px;z-index:1" onclick="closeDaumPostcode()" alt="닫기 버튼">
	</div>
		
	<div id="step01" class="document_inputWrap">
		<div class="header">
			<div class="logo">
				<span class="title NanumGothic">전자문서</span>
			</div>
			<div class="subtitle NanumGothic">문서정보 입력</div>
		</div>

		<div class="contents_txt">
			<dl>
				<dt>‘<span class="spanBizName"></span>’에서 발송한 ‘<span class="spanContractFileName"></span>’의 정보입력 요청입니다.</dt>
				<dd>
					<span>1</span>아래에 입력이 필요한 내용을 확인하고 입력 합니다.
				</dd>
				<dd>
					<span>2</span>[제출하기]버튼을 눌러 입력한 내용을 제출 합니다.
				</dd>
			</dl>
			</dl>
		</div>
		
		<div>
			<div class="contents_tb">
				<table id="rows">

					<tr>
						<th class="NanumSquare_R">결제일</th>
						<td>
							<select class="txtinput01" id="TXT_00009">
								<option value="">선택</option>
								<option value="5">5일</option>
								<option value="10">10일</option>
								<option value="15">15일</option>
								<option value="20">20일</option>
								<option value="25">25일</option>
								<option value="30">30일</option>
							</select>
						</td>
					</tr>

					<tr>
						<th class="NanumSquare_R">가입료</th>
						<td>
							<select class="txtinput01" id="TXT_00010">
								<option value="">선택</option>
								<option value="50,000">￦50,000</option>
								<option value="90,000">￦90,000</option>
								<option value="120,000">￦120,000</option>
							</select>
						</td>
					</tr>

				</table>
			</div>
			<div class="btn_wrap">
				<a class="btn_send Material_icons" style="cursor: pointer;" onclick="fn_goNextPage()">제출하기</a>
			</div>
		</div>
	</div>

</body>
</html>