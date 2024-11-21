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
		
		var txt00001 = $.trim($("#TXT_00001").val());
		var txt00002= $.trim($("#TXT_00002").val());
		var txt00003 = $.trim($("#TXT_00003").val());
		var txt00004 = $.trim($("#TXT_00004").val());
		var txt00005 = $.trim($("#TXT_00005").val());
		
		if(txt00001.length == 0){
			alert("계약자 성명을 입력해주세요.");
			$("#TXT_00001").focus();
			return false;
		}
		if(txt00002.length == 0){
			alert("주민등록번호를 입력해주세요.");
			$("#TXT_00002").focus();
			return false;
		}
		if(txt00003.length == 0){
			alert("주소를 입력해주세요.");
			$("#TXT_00003").focus();
			return false;
		}
		if(txt00004.length == 0){
			alert("이메일을 입력해주세요.");
			$("#TXT_00004").focus();
			return false;
		}
		if(txt00005.length == 0){
			alert("연락처를 입력해주세요.");
			$("#TXT_00005").focus();
			return false;
		}
		
		// 주민번호와 연락처에 하이픈 넣기
		txt00002 = fn_addhyphen(txt00002);
		txt00005 = fn_addhyphen(txt00005);
		
		
		multiData = "";
		multiData += "TXT_00001-"+txt00001+"|";
		multiData += "TXT_00002-"+txt00002+"|";
		multiData += "TXT_00003-"+txt00003+"|";
		multiData += "TXT_00004-"+txt00004+"|";
		multiData += "TXT_00005-"+txt00005;
		
/* 		$("#step01").remove();
		$("#step02").css("display", "block"); */
		fn_saveContract();
		
		
	}
	function fn_selectFile(index){
		$("#imageFile"+index).click();
	}
	
	function fn_changedFile(index){
		//var file = document.getElementsByName("imageFile"+index);
		$("#fileName"+index).html("선택됨");
	}
	
	function fn_addrList() {
		//window.open(rootPath+"html/commute/commute_zipcodePopup.html", "zipcode", "width=710, height=680, scrollbars=no");
        window.open(rootPath+"html/commute/address.html", "zipcode", "width=450, height=680");
	}

	function setSearchAddr(result){
		$('#TXT_00003').val(result.saddr);
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

				$("#TXT_00003").val(saddr);

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
	
	function fn_contractView() {
		var id = getParameterByName('id');
		location.href=rootPath+"contract/contractPersonView.do?id="+id;	
	}
	
	function fn_addhyphen(num) {
		var result = "";
		
		if (num.length == 11) { // 전화번호일 경우
			result = num.substr(0, 3) + "-" + num.substr(3, 4) + "-" + num.substr(7);
		}
		else if(num.length == 13){ // 주민번호일 경우
			result = num.substr(0, 6) + "-" + num.substr(6);
		}
		return result;
	} 
	
	function inNumber(){
        if(event.keyCode<48 || event.keyCode>57){
           event.returnValue=false;
        }
}
	
	 
	
</script>
</head>

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
					<span>1</span>아래에 입력이 필요한 내용을 확인하고, 수정이 필요한경우 수정합니다.
				</dd>
<!-- 			<dd>
					<span>2</span>[사진/서류등록]->[제출하기]버튼을 눌러 입력한 내용을 제출 합니다.
				</dd> 
-->
				<dd>
					<span>2</span>제출된 내용을 담당부서가 확인 후 최종 전자문서를 전송할 예정입니다.
				</dd>
			</dl>
		</div>
		
		<div>
			<div class="contents_tb">
				<table id="rows">
					<tr>
						<th class="NanumSquare_R">성명</th>
						<td>
							<input type="text" id="TXT_00001" class="txtinput01 NanumSquare_R">
						</td>
					</tr>
					
					<tr>
						<th class="NanumSquare_R">주민등록번호</th>
						<td>
							<input type="text" id="TXT_00002" class="txtinput01 NanumSquare_R" maxlength="13" placeholder="예) 9901011111111" 
							 onkeypress="inNumber();">
						</td>
					</tr>
					
					<th class="NanumSquare_R">주소</th>
						<td>
							<div class="search_div">
								<textarea class="word_input NanumSquare_R" id="TXT_00003"></textarea><a class="btn_search btn_type Material_icons" onclick="sample2_execDaumPostcode()">search</a>
							</div>
						</td>
					</tr>
					<tr>
						<th class="NanumSquare_R">이메일</th>
						<td>
							<input type="text" id="TXT_00004" class="txtinput01 NanumSquare_R">
						</td>
					</tr>
					
					<tr>
						<th class="NanumSquare_R">연락처</th>
						<td>
							<input type="text" id="TXT_00005" class="txtinput01 NanumSquare_R" maxlength="11" placeholder="예) 01012341234" 
								onkeypress="inNumber();">
						</td>
					</tr>					
					
				</table>
			</div>
<!-- 			<div class="btn_wrap">
				<a class="btn_send Material_icons" style="cursor: pointer;" onclick="fn_goNextPage();">사진/서류등록</a>
			</div> -->
			<div class="btn_wrap">
				<a class="btn_send Material_icons" style="cursor: pointer;" onclick="fn_goNextPage()">제출하기</a>
			</div>
		</div>
	</div>

<!-- 	 
	<div id="step02" class="document_inputWrap" style="display:none;">
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
				필수서류 : <span class="txtbold">첨부서류 </span>
				</li>
				<li><span class="txtred">※ 사진 제출 시 시간이 오래걸릴 수 있습니다.</span></li>
			</ul>
		</div>

		<div class="additionalInfo">
			<table>
				<tr>
					<th>첨부서류</th>
					<td><div id="fileName1" class="spanEtc"></div></td>
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
		<input type="file" style="display:none;" id="imageFile1" onchange="fn_changedFile('1')" name="imageFile1">
 		<input type="file" style="display:none;" id="imageFile2" onchange="fn_changedFile('2')" name="imageFile2">
		<div class="btn_wrap">
			<a class="btn_send Material_icons" style="cursor: pointer;" onclick="fn_saveContract()">제출하기</a>
		</div>
		
	</div>
-->
	
</body>
</html>