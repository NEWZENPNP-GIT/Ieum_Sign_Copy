<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
<link rel="stylesheet" type="text/css" href="/css/document_input.css">

<script src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/jquery.bootpag_candy.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<script type="text/javascript" src="/js/ezsign/session_storage.js"></script>
<script type="text/javascript" src="/js/postcode.v2.js"></script>


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

/*		$.each(formResult, function(i, v) {
			if(isNotNull(v.formDataValue)){
				console.log(v.formDataId+", "+v.formDataValue);
				$("#"+v.formDataId+"").val(v.formDataValue);
			}
		});*/
		
		if(isNotNull(result.comment)){
			alert("[담당자 메모]\r\n"+result.comment.replace(/<br>/g, "\r\n"));
		}
		
		element_layer = document.getElementById('layer');

		$(".DB").hide();
		$(".DC").hide();


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
		
		var txt00045="";
		var txt00046="";
		var txt00045_46=$('input[name=TXT_00045_46]:checked').val();


		if(txt00045_46 == "" || txt00045_46 == undefined ){
			alert("퇴직연금제도를 선택해주세요.");
			return false;
		}
		if(txt00045_46 === "DB"){
			txt00045 = "V";
			txt00046 = " ";
		}

		if(txt00045_46 === "DC"){
			txt00045 = " ";
			txt00046 = "V";
		}




		
		multiData = "";
		multiData += "TXT_00045-"+txt00045+"|";
		multiData += "TXT_00046-"+txt00046;
		
/* 		$("#step01").remove();
		$("#step02").css("display", "block"); */
		fn_saveContract();
		
		
	}
	
	function fn_contractView() {
		var id = getParameterByName('id');
		location.href=rootPath+"contract/contractPersonView.do?id="+id;	
	}

	function showHiddenImage(){
		var txt00045_46=$('input[name=TXT_00045_46]:checked').val();

		if(txt00045_46 == "DB"){
			$(".DB").show();
			$(".DC").hide();
		}

		if(txt00045_46 == "DC"){
			$(".DB").hide();
			$(".DC").show();
		}

	}

</script>
</head>

<body>
	<div id="step01" class="document_inputWrap">
		<div class="header">
			<div class="subtitle NanumGothic">문서정보 입력</div>
		</div>

		<div class="contents_txt">
			<dl style="padding:10px">
				<dt>‘<span class="spanBizName"></span>’에서 발송한 ‘<span class="spanContractFileName"></span>’의 정보입력 요청입니다.</dt>
				<dd>
					<span>1</span>아래내용을 참조하여 퇴직급여제도를 선택하시기 바랍니다.
				</dd>
				<dd>
					<span>2</span>[제출하기]버튼을 눌러 입력한 내용을 제출 합니다.
				</dd>
			</dl>
		</div>

		<div style="margin-top: 10px">
			<a style="font-family: 'NanumGothic',sans-serif; font-weight:bold;color:#009cff; font-size: 18px; padding: 10px">&nbsp;■ 퇴직연금제도 비교표 </a>
			<img src="/images/kolon/all.png" style="max-width: 100%; height: auto; display: block; margin: 0 auto">
		</div>

		<div>
			<div class="contents_tb" style="padding:10px 10px 10px 0">
				<table id="rows" >
					<tr>
						<th class="NanumSquare_R" style="font-family: 'NanumGothic',sans-serif; font-weight:bold;color:#777;text-align: center">퇴직연금제도<br>선택</th>
						<td oninput="showHiddenImage()" style="font-family: 'NanumGothic',sans-serif; font-weight:bold;color:#777;">
							<input type="radio" name="TXT_00045_46" value="DB"> &nbsp;확정급여형&nbsp;
							<input type="radio" name="TXT_00045_46" value="DC"> &nbsp;확정기여형
						</td>
					</tr>
					
				</table>
			</div>

			<div class="DB">
				<a style="font-family: 'NanumGothic',sans-serif; font-weight:bold;color:#777;"> &nbsp; - 확정급여형(DB)를 선택하셨습니다.<br></a>
				<img src="/images/kolon/DB.png" style="max-width: 100%; height: auto; display: block; margin: 0 auto">
			</div>

			<div class="DC">
				<a style="font-family: 'NanumGothic',sans-serif; font-weight:bold;color:#777;"> &nbsp; - 확정기여형(DC)를 선택하셨습니다.<br></a>
				<img src="/images/kolon/DC.png" style="max-width: 100%; height: auto; display: block; margin: 0 auto">
			</div>

			<div class="btn_wrap">
				<a class="btn_send Material_icons" style="cursor: pointer;" onclick="fn_goNextPage()">제출하기</a>
			</div>
		</div>
	</div>

	
</body>
</html>