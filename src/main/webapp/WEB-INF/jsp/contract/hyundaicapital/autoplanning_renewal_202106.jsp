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

		$.each(formResult, function(i, v) {
			if(isNotNull(v.formDataValue)){
				console.log(v.formDataId+", "+v.formDataValue);
				$("#"+v.formDataId+"").val(v.formDataValue);
			}
		});
		$("#TXT_00001").val(result.empName);
		//null포인트 에러를 위한 객체 데이터 세팅
		multiData = "";
		multiData += "TXT_00001-" + result.empName;
		
		if(isNotNull(result.comment)){
			alert("[담당자 메모]\r\n"+result.comment.replace(/<br>/g, "\r\n"));
		}
		
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
		var file1 = document.getElementsByName("imageFile1");
		for(var i=0;i<file1.length;i++) {
			if(file1[i].files[0] == undefined){
				alert("이행(지급)보증보험증권 이미지를 첨부해주세요.");
				return;
			}
			if (file1[i].files[0] != undefined) {
				if(checkSpecial(file1[i].files[0].name)) {
					alert("이행(지급)보증보험증권 파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
					return;
				}
				if(!file1[i].files[0].name.match(reg)){
					alert("이행(지급)보증보험증권 파일이 이미지가 아닙니다. 이미지로 등록해주세요.");
					return;
				}
			    formData.append("ETC_1",file1[i].files[0]);
			}
		}

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
					location.reload();
				} else {
					alert("이미지 등록 중 오류가 발생하였습니다. \r\n 오류메세지 : "+result.message);
					$.unblockUI();
				}
			},
			error:function(request,status,error){
				$.unblockUI();
		        alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});	
	}

	function fn_selectFile(index){
		$("#imageFile"+index).click();
	}
	
	function fn_changedFile(index){
		//var file = document.getElementsByName("imageFile"+index);
		$("#fileName"+index).html("선택됨");
	}
	
	
</script>
</head>

<body>
		
	<div id="step01" class="document_inputWrap">
				<div class="header">
			<div class="logo">
				<span class="title NanumGothic">전자문서</span>
			</div>
			<div class="subtitle NanumGothic">사진 / 서류 등록</div>
		</div>

				<div class="contents_txt">
			<ul>
				<li> <span class="txtblue"><span class="spanEmpName"></span></span>님의
				</li>
				<li>필수서류를 등록해주세요.<br>
				필수서류 : <span class="txtbold">이행(지급)보증보험증권 </span>
				</li>
				<li><span class="txtred">※ 사진 제출 시 시간이 오래걸릴 수 있습니다.</span></li>
			</ul>
		</div>

		<div class="additionalInfo">
			<table>
				<tr>
					<th>이행(지급)보증보험증권</th>
					<td><div id="fileName1" class="spanEtc"></div></td>
					<td>
						<div onclick="fn_selectFile('1')" class="selectbtn">사진등록</div> 
					</td>
				</tr>
			</table>
		</div>
		<input type="file" style="display:none;" id="imageFile1" onchange="fn_changedFile('1')" name="imageFile1">
		<!-- null포인트 에러 제어를 위한 숨김변수 전달선언 -->
		<div type="text" id="TXT_00001" class="txtinput01" style="display:none"></div>
	


		<div class="btn_wrap">
			<a class="btn_send Material_icons" style="cursor: pointer;" onclick="fn_saveContract()">제출하기</a>
		</div>
	</div>
</body>
</html>