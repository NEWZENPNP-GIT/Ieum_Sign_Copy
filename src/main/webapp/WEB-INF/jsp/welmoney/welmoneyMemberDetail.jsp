<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset= UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, user-scalable=no">
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">

<link rel="stylesheet" type="text/css" href="/css/font_pc.css">
<link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
<link rel="stylesheet" type="text/css" href="/css/personnel_management.css">

<script type="text/javascript" src="/js/jquery.min.js"></script>
<script type="text/javascript" src="/js/common.js"></script>
<script type="text/javascript" src="/js/jquery.blockUI.js"></script>
<title>인적자원관리 전문IT기업 - 뉴젠P&amp;P</title>
</head>
<script type="text/javascript">

	var paramUserId = getURLParameters("userId");
	
	$(document).ready(function(){
		if(!isNull(paramUserId)){
			fn_empInfo();
			$("#userId").attr("readonly", "readonly");
		}
	});
	
	function fn_empInfo() {
		var url = rootPath+"welmoney/getMember.do";
		
		
		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"GET",
			data: {
				userId:paramUserId
			},
			success:function(result)
			{
				if (result.success==true) {				
					// 인사정보
					$("#userId").val(result.data.userId);
					$("#userName").val(result.data.userName);
					if(!isNull(result.data.startDate) && result.data.startDate!="0") {
						$("#startDate").val(convertDate(result.data.startDate).format("yyyyMMdd"));
					}
					if(!isNull(result.data.endDate) && result.data.endDate!="0" && result.data.endDate != "99999999") {
						$("#endDate").val(convertDate(result.data.endDate).format("yyyyMMdd"));
					}
					$("#phoneNum").val(result.data.phoneNum);
					$("#eMail").val(result.data.EMail);
				} else {
					alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
				}
			},
			error:function(request,status,error){
				if (request.status=="401") {
					alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
					window.close();
				} else {
					alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
			        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			}
		});
	}
	
	function fn_save() {
		if(!confirm("저장하시겠습니까?")) return;
		
		var url = "";
		if(isNull(paramUserId)){
			url = rootPath+"welmoney/insMember.do";
		}else{
			url = rootPath+"welmoney/updMember.do";
		}
		
		var userId = $("#userId").val();
		var userName = $("#userName").val();
		var startDate = $("#startDate").val();
		var endDate = $("#endDate").val();
		var eMail = $("#eMail").val();
		var phoneNum = $("#phoneNum").val();
		
		if(startDate.length>0) startDate=startDate.replace(/-/gi,"");
		if(endDate.length>0) {
			endDate=endDate.replace(/-/gi,"");
		} else {
			endDate="0";
		}
		
		if(startDate.length==0) {
			alert("시작일자를 입력해주시기 바랍니다.");
			$("#startDate").focus();
			return;
		} else {
			if(!checkDateNoHypen(startDate)) {
				alert("시작일자 날짜 형식이 맞지 않습니다.\r\n다시 입력해주시기 바랍니다.");
				$("#joinDate").focus();
				return;
			}
		}
		
		if(endDate != "0") {
			if(!checkDateNoHypen(endDate)) {
				alert("종료일자 날짜 형식이 맞지 않습니다.\r\n다시 입력해주시기 바랍니다.");
				$("#endDate").focus();
				return;
			}
		} else {
			endDate = "";
		}
		
		if(phoneNum.length==0) {
			alert("휴대폰번호를 입력해주시기 바랍니다.");
			$("#phoneNum").focus();
			return;
		} else {
			if(!checkPhoneNumNoHypen(phoneNum)) {
				alert("휴대폰번호 형식이 맞지 않습니다.\r\n다시 입력해주시기 바랍니다.");
				$("#phoneNum").focus();
				return;
			}
		}
	
		if(eMail.length==0) {
			alert("이메일을 입력해주시기 바랍니다.");
			$("#eMail").focus();
			return;
		} else {
			if (!checkEmail(eMail)) {
				alert("이메일 형식이 맞지 않습니다.\r\n다시 입력해주시기 바랍니다.");
				$("#eMail").focus();
				return;
			}
		}	
		
		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"POST",
			data: {
				userId:userId,
				userName:userName,
				startDate:startDate,
				endDate:endDate,
				eMail:eMail,
				phoneNum:phoneNum
			},
			success:function(result)
			{
				if (result.success==true) {				
					alert("저장 되었습니다.");	
					window.close();
					opener.fn_memberList(1);
				} else {
					alert("이미 등록된 ID 입니다.");
				}
			},
			error:function(request,status,error){
				if (request.status=="401") {
					alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
					window.close();
				} else {
					alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
			        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			}
		});
		
	}
	
	function fn_delete() {
		if(!confirm("삭제를 진행하시겠습니까?")) return;
	
		fncOnBlockUI();
		
		var url = rootPath+"welmoney/delMember.do";
		
		var userId = $("#userId").val();
		
		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"POST",
			data: {
				userId:userId
			},
			success:function(result)
			{
				if (result.success==true) {	
					alert("삭제 되었습니다.");
					window.close();
					opener.fn_memberList(1);	
				} else {
					alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
				}
				$.unblockUI();
			},
			error:function(request,status,error){
				$.unblockUI();
				if (request.status=="401") {
					alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
					window.close();
				} else {
					alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
			        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
				}
			}
		});
		
	}
</script>
<body>
<!-- *********************************************  이용자 관리  *********************************************  -->
<div id="person_popup" class="edit" style="height: auto; width: 100%;">
	<div class="container">
		<div id="popup_tit">
			<div class="tit_icon"></div>
			<div class="tit_text col_skyblue NanumGothic">사용자정보변경</div>
			<div id="popup_close" class="btn_type col_skyblue" onclick="window.close()"></div>
		</div>
		<div class="contents" style="height: auto;">
			<div class="input_wrap">
				<dl class="boxRow input_bx NanumGothic">
					<dt><label class="label_txt NanumGothic">로그인 ID</label></dt>
					<dd><input type="text" id="userId" value="" name="" class="input_txt"></dd>
				</dl>
				<dl class="boxRow input_bx NanumGothic">
					<dt><label class="label_txt NanumGothic">사용자명</label></dt>
					<dd><input type="text" id="userName" value="" name="" class="input_txt"></dd>
				</dl>
				<dl class="boxRow input_bx NanumGothic">
					<dt><label class="label_txt NanumGothic">시작일자</label></dt>
					<dd><input type="text" id="startDate" value="" name="" class="input_txt" placeholder="예) 19990101"></dd>
				</dl>
				<dl class="boxRow input_bx NanumGothic">
					<dt><label class="label_txt NanumGothic">종료일자</label></dt>
					<dd><input type="text" id="endDate" value="" name="" class="input_txt" placeholder="예) 19990101"></dd>
				</dl>
				<dl class="boxRow input_bx NanumGothic">
					<dt><label class="label_txt NanumGothic">휴대폰번호</label></dt>
					<dd><input type="text" id="phoneNum" value="" name="" class="input_txt" placeholder="예) 01099991234"></dd>
				</dl>
				<dl class="boxRow input_bx NanumGothic">
					<dt><label class="label_txt NanumGothic">이메일</label></dt>
					<dd><input type="text" id="eMail" value="" name="" class="input_txt" placeholder="예) abc@company.com"></dd>
				</dl>
			</div>
		</div>
		<div class="btn_group">
			<div class="btn_type type1 NanumGothic" onclick="fn_save()">저장하기</div>
			<div id="btnDel" class="btn_type type2 NanumGothic" onclick="fn_delete()">삭제하기</div>
		</div>
	</div>
</div>
</body>
</html>