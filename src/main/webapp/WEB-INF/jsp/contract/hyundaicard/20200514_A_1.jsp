﻿﻿﻿<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
		
		/* 2021.11.15 요청으로 신원보험증권 주석처리
		var file1 = document.getElementsByName("imageFile1");
		for(var i=0;i<file1.length;i++) {
			if(file1[i].files[0] == undefined){
				alert("신원보증보험증권 이미지를 첨부해주세요.");
				return;
			}
			if (file1[i].files[0] != undefined) {
				if(checkSpecial(file1[i].files[0].name)) {
					alert("신원보증보험증권 파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
					return;
				}
				if(!file1[i].files[0].name.match(reg)){
					alert("신원보증보험증권 파일이 이미지가 아닙니다. 이미지로 등록해주세요.");
					return;
				}
			    formData.append("ETC_1",file1[i].files[0]);
			}
		}
		*/
		var file1 = document.getElementsByName("imageFile1");
		for(var i=0;i<file1.length;i++) {
			if(file1[i].files[0] == undefined){
				alert("모집종사자 등록(말소)이력조회 이미지를 첨부해주세요.");
				return;
			}
			if (file1[i].files[0] != undefined) {
				if(checkSpecial(file1[i].files[0].name)) {
					alert("모집종사자 등록(말소)이력조회 파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
					return;
				}
				if(!file1[i].files[0].name.match(reg)){
					alert("모집종사자 등록(말소)이력조회 파일이 이미지가 아닙니다. 이미지로 등록해주세요.");
					return;
				}
			    formData.append("ETC_1",file1[i].files[0]);
			}
		}
		var file2 = document.getElementsByName("imageFile2");
		for(var i=0;i<file2.length;i++) {
			if(file2[i].files[0] == undefined){
				alert("통장사본을 첨부해주세요.");
				return;
			}
			if (file2[i].files[0] != undefined) {
				if(checkSpecial(file2[i].files[0].name)) {
					alert("통장사본 파일 파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
					return;
				}
				if(!file2[i].files[0].name.match(reg)){
					alert("통장사본 파일이 이미지가 아닙니다. 이미지로 등록해주세요.");
					return;
				}
			    formData.append("ETC_2",file2[i].files[0]);
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
	
	function fn_goNextPage(){
		
		var txt00001 = $.trim($("#TXT_00001").val());
		var txt00004 = $.trim($("#TXT_00004").val());
		var txt00005= $.trim($("#TXT_00005").val());
		var txt00006 = $.trim($("#TXT_00006 :selected").val());
		
		if(txt00001.length == 0){
			alert("계약자 성명을 입력해주세요.");
			$("#TXT_00001").focus();
			return false;
		}
		if(txt00004.length == 0){
			alert("계약자 주소를 입력해주세요.");
			$("#TXT_00004").focus();
			return false;
		}
		if(txt00005.length != 8){
			alert("생년월일 8자리를 입력해주세요.");
			$("#TXT_00005").focus();
			return false;
		}
		if(txt00006.length == 0){
			alert("업체명을 선택해주세요.");
			$("#TXT_00006").focus();
			return false;
		}
		
		multiData = "";
		multiData += "TXT_00001-"+txt00001+"|";
		multiData += "TXT_00004-"+txt00004+"|";
		multiData += "TXT_00005-"+txt00005+"|";
		multiData += "TXT_00006-"+txt00006;
		
		$("#step01").remove();
		$("#step02").css("display", "block");
	}
	function fn_selectFile(index){
		$("#imageFile"+index).click();
	}
	
	function fn_changedFile(index){
		//var file = document.getElementsByName("imageFile"+index);
		$("#fileName"+index).html("선택됨");
	}
	
	function fn_addrList() {
		// window.open(rootPath+"html/commute/commute_zipcodePopup.html", "zipcode", "width=710, height=680, scrollbars=no");
        window.open(rootPath+"html/commute/address.html", "zipcode", "width=450, height=680");
	}

	function setSearchAddr(result){
		$('#TXT_00004').val(result.saddr);
	}
	
	function test(){
		$('[data-popup="popup-1"]').fadeOut();
	}
	
	 function closeDaumPostcode() {
        // iframe을 넣은 element를 안보이게 한다.
        element_layer.style.display = 'none';
    }

    function sample2_execDaumPostcode() {
        new daum.Postcode({
            oncomplete: function(data) {
                // 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.
	
				var zonecode;
				var addr;
				var saddr;

				if (data.userSelectedType === 'R') {
					saddr = data.roadAddress;
					if (data.buildingName) {
						saddr = saddr + ' (' + data.buildingName + ')';
					}
				} else {
					saddr = data.jibunAddress;
				}

				$("#TXT_00004").val(saddr);

				// iframe을 넣은 element를 안보이게 한다.
				// (autoClose:false 기능을 이용한다면, 아래 코드를 제거해야 화면에서 사라지지 않는다.)
				element_layer.style.display = 'none';
			},
			width : '100%',
			height : '100%',
			maxSuggestItems : 5
		}).embed(element_layer);

		// iframe을 넣은 element를 보이게 한다.
		element_layer.style.display = 'block';

		// iframe을 넣은 element의 위치를 화면의 가운데로 이동시킨다.
		initLayerPosition();
	}

	// 브라우저의 크기 변경에 따라 레이어를 가운데로 이동시키고자 하실때에는
	// resize이벤트나, orientationchange이벤트를 이용하여 값이 변경될때마다 아래 함수를 실행 시켜 주시거나,
	// 직접 element_layer의 top,left값을 수정해 주시면 됩니다.
	function initLayerPosition() {
		var width = 300; //우편번호서비스가 들어갈 element의 width
		var height = 400; //우편번호서비스가 들어갈 element의 height
		var borderWidth = 5; //샘플에서 사용하는 border의 두께

		// 위에서 선언한 값들을 실제 element에 넣는다.
		element_layer.style.width = width + 'px';
		element_layer.style.height = height + 'px';
		element_layer.style.border = borderWidth + 'px solid';
		// 실행되는 순간의 화면 너비와 높이 값을 가져와서 중앙에 뜰 수 있도록 위치를 계산한다.
		element_layer.style.left = (((window.innerWidth || document.documentElement.clientWidth) - width) / 2 - borderWidth)
				+ 'px';
		element_layer.style.top = '10px';
	}
</script>
</head>
<!--<body>


</body>
</html>-->

<body>
		
	<div id="layer" style="display:none;position:fixed;overflow:hidden;z-index:1;-webkit-overflow-scrolling:touch;">
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
					<span>1</span>아래에 입력이 필요한 내용을 확인합니다.
				</dd>
				<dd>
					<span>2</span>[제출하기]버튼을 눌러 입력한 내용을 제출 합니다.
				</dd>
				<dd>
					<span>3</span>제출된 내용을 관리자가 확인 후 최종 전자문서를 전송할 예정입니다.
				</dd>
			</dl>
		</div>
		
		<div>
			<div class="contents_tb">
				<table id="rows">
					<tr>
						<th class="NanumSquare_R">계약자명</th>
						<td>
							<input type="text" id="TXT_00001" class="txtinput01">
						</td>
					</tr>
					<tr>
						<th class="NanumSquare_R">계약자 주소</th>
						<td>
							<div class="search_div">
								<textarea class="word_input NanumSquare_R" id="TXT_00004"></textarea><a class="btn_search btn_type Material_icons" onclick="sample2_execDaumPostcode()">search</a>
							</div>
						</td>
					</tr>
					<tr>
						<th class="NanumSquare_R">생년월일</th>
						<td>
							<input type="text" id="TXT_00005" class="txtinput01 NanumSquare_R" placeholder="생년월일 8자리(YYYYMMDD)" maxlength="8">
						</td>
					</tr>
					<tr>
						<th class="NanumSquare_R">업체명</th>
						<td>
							<select class="txtinput01" id="TXT_00006">
								<option value="">선택</option>
								<option value="메리츠화재_합정">메리츠화재_합정</option>
								<option value="메리츠화재_광주">메리츠화재_광주</option>
								<option value="메리츠화재_대구">메리츠화재_대구</option>
								<option value="동양생명_대구">동양생명_대구</option>
								<option value="동양생명_을지로">동양생명_을지로</option>
								<option value="동양생명_을지로2">동양생명_을지로2</option>
								<option value="AIA생명_수원">AIA생명_수원</option>
								<option value="AIA생명_부산">AIA생명_부산</option>
								<option value="AIA생명_은평">AIA생명_은평</option>
								<option value="KB생명_인천">KB생명_인천</option>
								<option value="라이나_광화문">라이나_광화문</option>
								<option value="라이나_전주">라이나_전주</option>
								<option value="라이나_대전">라이나_대전</option>
								<option value="라이나생명_안양">라이나생명_안양</option>	
								<option value="현대해상_대전">현대해상_대전</option>
								<option value="DB손보_대구">DB손보_대구</option>
								<option value="ACE손보_부평">ACE손보_부평</option>
								<option value="ACE손보_춘천">ACE손보_춘천</option>
								<option value="현대해상_충정로">현대해상_충정로</option>
								<option value="ACE손보_의정부">ACE손보_의정부</option>		
								<option value="KB생명_공덕">KB생명_공덕</option>
								<option value="동양생명_홍대">동양생명_홍대</option>
								<option value="동양생명_안양">동양생명_안양</option>
								<option value="DB손해보험_대구">DB손해보험_대구</option>
								<option value="DB손해보험_동묘">DB손해보험_동묘</option>
								<option value="라이나_대전2">라이나_대전2</option>
								<option value="ACE손보_안양">ACE손보_안양</option>
								<option value="ACE손보_홍대">ACE손보_홍대</option>
								<option value="AIG손보_서소문">AIG손보_서소문</option>
								
							</select>
						</td>
					</tr>
				</table>
			</div>
			<div class="btn_wrap">
				<a class="btn_send Material_icons" style="cursor: pointer;" onclick="fn_goNextPage();">사진/서류등록</a>
			</div>
		</div>
	</div>
	
	<div id="step02" class="document_inputWrap" style="display:none;">
		<div class="header">
			<div class="logo">
				<span class="title NanumGothic">전자문서</span>
			</div>
			<div class="subtitle NanumGothic">사진 / 서류 등록</div>
		</div>

		<div class="contents_txt">
			<ul>
				<li><span class="spanBizName"></span>와 <span class="txtblue"><span class="spanEmpName"></span></span>님의
				</li>
				<li>필수서류 <span class="txtbold">(모집종사자 등록(말소) 이력조회, 통장사본)</span>를 등록해주세요.
				</li>
				<li><span class="txtred">※ 사진 제출 시 시간이 오래걸릴 수 있습니다.</span></li>
			</ul>
		</div>

		<div class="additionalInfo">
			<table>
			<!--  2021.11.15 신원보증보험 주석  
				<tr>
					<th>신원보증보험증권</th>
					<td><div id="fileName1" class="spanEtc"></div></td>
					<td>
						<div onclick="fn_selectFile('1')" class="selectbtn">사진등록</div> 
					</td>
				</tr>
				
				  -->
				<tr>
					<th>모집종사자 등록(말소)<br>이력조회</th>
					<td><span id="fileName1" class="spanEtc" style="width:35%"></span></td>
					<td>
						<div onclick="fn_selectFile('1')" class="selectbtn">사진등록</div> 
					</td>
				</tr>
				<tr>
					<th>통장사본</th>
					<td><span id="fileName2" class="spanEtc" style="width:35%"></span></td>
					<td>
						<div onclick="fn_selectFile('2')" class="selectbtn">사진등록</div> 
					</td>
				</tr>
			</table>
		</div>
		<!--<input type="file" style="display:none;" id="imageFile1" onchange="fn_changedFile('1')" name="imageFile1"> 
			2021.11.15 신원보증보험 주석      -->
		<input type="file" style="display:none;" id="imageFile1" onchange="fn_changedFile('1')" name="imageFile1">
		<input type="file" style="display:none;" id="imageFile2" onchange="fn_changedFile('2')" name="imageFile2">

		<div class="btn_wrap">
			<a class="btn_send Material_icons" style="cursor: pointer;" onclick="fn_saveContract()">제출하기</a>
		</div>
	</div>
</body>
</html>