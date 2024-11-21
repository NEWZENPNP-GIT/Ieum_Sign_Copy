<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset= UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="content-type" content="text/html; charset=utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=yes, target-densitydpi=medium-dpi" />
<meta http-equiv="Cache-Control" content="no-cache">
<meta http-equiv="Pragma" content="no-cache">

<title>서비스 이용 가입</title>
<link rel="stylesheet" type="text/css" href="/css/font.css">
<link rel="stylesheet" type="text/css" href="/css/member_Join.css">
<script src="/js/jquery.min.js"></script>
<script src="/js/common.js"></script>
<script src="/js/jquery.blockUI.js"></script>

<script type="text/javascript">
	var chkUserId = false;
	var chkUserIdSave = "";

	$(document).ready(function() {
		$("#userType").val(getURLParameters("menuType"));
	});
	
	// 아이디 중복확인
	function fn_checkUserId() {
		var url = rootPath+"user/getUserList.do";
		
		var loginId = $("#loginId").val();
		
		if (loginId.length==0) {
			alert("아이디를 입력해주시기 바랍니다.");
			return;
		}
		if(!checkEmail(loginId)) {
			alert("아이디는 이메일 형식으로 입력해주시기 바랍니다.");
			return;
		}
		chkUserId = false;

        $.blockUI();
	    
		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"GET",
			data: {
				userId:loginId
			},
			success:function(result)
			{
				if (result.success==true) {
					
					if(result.total==0) {
						chkUserId = true;
						chkUserIdSave = loginId;
						alert("사용가능하신 아이디입니다.");
					} else {
						alert("입력하신 아이디는 사용중인 아이디입니다.\r\n다시 입력해주시기 바랍니다.");
					}
				} else {
					alert("입력하신 아이디는 사용중인 아이디입니다.\r\n다시 입력해주시기 바랍니다.");
				}
			},
			error:function(request,status,error){
				alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
		$.unblockUI();

	}
	
	// 사용자 서비스 가입
	function fn_memberJoin() {
		if(!confirm("가입 신청을 하시겠습니까?")) return;
		
		var url = rootPath+"user/insUser.do";

		var loginId = $("#loginId").val();		
		var userPwd = $("#userPwd").val();
		var userPwd2 = $("#userPwd2").val();
		var userType = $("#userType").val();
		
		var bizName = $("#bizName").val();
		var ownerName = $("#ownerName").val();
		var businessNo = $("#businessNo").val();
		var companyAddr = $("#companyAddr").val();
		
		var userName = $("#empName").val();
		var eMail = $("#eMail").val();
		var phoneNum = $("#phoneNum").val();

		if(businessNo.length > 0) {
			businessNo = businessNo.replace(/-/gi, "");
		}
		
		businessNo = removeSpecialChars(businessNo);
		phoneNum = removeSpecialChars(phoneNum);

		if (bizName.length==0) {
			alert("기업명을 입력해주시기 바랍니다.");
			return;
		}
		if (bizName.indexOf("_") != -1) {
			alert("기업명에 특수문자(_)를 입력하실  수 없습니다.");
			return;
		}
		if (ownerName.length==0) {
			alert("대표자명을 입력해주시기 바랍니다.");
			return;
		}
		if (businessNo.length==0) {
			alert("사업자번호를 입력해주시기 바랍니다.");
			return;
		}
		if(!check_busino(businessNo)) {
			alert("사업자번호가 맞지 않습니다.");
			return;
		}
		if (loginId.length==0) {
			alert("아이디를 입력해주시기 바랍니다.");
			return;
		}
		if (userPwd.length==0) {
			alert("비밀번호를 입력해주시기 바랍니다.");
			return;
		}
		if (userName.length==0) {
			alert("사용자명을 입력해주시기 바랍니다.");
			return;
		}
		if (userName.indexOf("_") != -1) {
			alert("사용자명에 특수문자(_)를 입력하실  수 없습니다.");
			return;
		}
		if(!checkEmail(loginId)) {
			alert("아이디는 이메일 형식으로 입력해주시기 바랍니다.");
			return;
		}
		if(!chkUserId) {
			alert("아이디 중복확인을 진행해주시기 바랍니다.");
			return;
		}
		if(chkUserIdSave!=loginId) {
			alert("중복확인된 아이디와 입력하신 아이디가 맞지 않습니다.\r\n다시 아이디의 중복확인을 진행해주시기 바랍니다.");
			return;
		}
		if(!checkPassword(userPwd)) {
			return;
		}
		if(userPwd!=userPwd2) {
			alert("입력하신 비밀번호가 맞지 않습니다.\r\n다시 비밀번호를 입력해주시기 바랍니다.");
			return;
		}

		if(!checkEmail(eMail)) {
			alert("담당자 이메일은 이메일 형식으로 입력해주시기 바랍니다.");
			return;
		}
		if (phoneNum.length==0) {
			alert("휴대폰번호를 입력해주시기 바랍니다.");
			return;
		}
		if(!checkPhoneNum(phoneNum)) {
			alert("담당자 휴대폰번호를 형식에 맞게 입력해주시기 바랍니다.");
			return;
		}
		
		// 데이터설정
		var formData = new FormData();
		var file = document.getElementsByName("attachFile");
		for(var i=0;i<file.length;i++) {
			if (file[i].files[0] != undefined) {
				if(!checkFileExt(file[i].files[0].name, "png")) {
					alert("파일확장자는 png파일만 가능합니다.");
					return;
				}
				if(checkSpecial(file[i].files[0].name)) {
					alert("파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.");
					return;
				}
			    formData.append("addFile"+i,file[i].files[0]);
			}
		}
	    formData.append("loginId",loginId);
	    formData.append("userPwd",userPwd);
	    formData.append("userType",userType);

	    formData.append("bizName",bizName);
	    formData.append("ownerName",ownerName);
	    formData.append("businessNo",businessNo);
	    formData.append("companyAddr", companyAddr);
	    
	    formData.append("userName",userName);
	    formData.append("eMail",eMail);
	    formData.append("phoneNum",phoneNum);
	    
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
				if (result.success==true) {
					document.location.href="/menu/goMemberJoin3.do";
				} else {
					alert("입력하신 정보로 가입을 진행할 수 없습니다.\r\n다시 입력해주시기 바랍니다.\r\nMessage:"+result.message);
				}
				$.unblockUI();
			},
			error:function(request,status,error){
				$.unblockUI();
				alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
			}
		});
		
	}
	
</script>

</head>
<body>
	<header id="join_header">
		<nav>
			<ul>
				<li class="logo"></li>
			</ul>
		</nav>
	</header>
	
	<div id="join_wrap">
		<aside id="join_side" class="fr">
			<ul>
				<li><span class="NanumGothic">회원가입</span></li>
			</ul>
		</aside>
		
		<section id="join_contents" class="fr">
			<nav id="stateBar" class="NanumGothic">
				<ul>
					<li class="step" >
						<span>STEP 01</span><br>
						<span>약관동의</span><br>
					</li>
					<li class="step on">
						<span>STEP 02</span><br>
						<span>사용자 정보</span><br>
					</li>
					<li class="step">
						<span>STEP 03</span><br>
						<span>가입완료</span><br>
					</li>
				</ul>
			</nav>
			<!--  mobile-btns 영역 보이려면 hidden 클래스 삭제 -->
			<div class="mobile-btns hidden">
				<a class="btn_type Material_icons" onclick="history.back(); return false;">
					<span class="arrow"></span>
					<span class="text"></span>
				</a>
			</div>
			<!-- end mobile-btns -->
			<div id="terms_wrap">
				<div id="terms_inner_step02">
					<ul id="join_info">
						<li class="tit NanumGothic">
							<span>*로 표시된 항목은 필수입력 사항입니다.</span>
						</li>
						<!-- user_info:단구성 클래스 -> column1 / column2 -->
						<!-- dl -> label과 input박스 set -->
                        <li class="info user_info column1">
                            <div class="info_tit NanumGothic">사용자 계정 정보</div>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>아이디</label></dt>
                                <dd><input type="text" class="input_txt size08" placeholder="이메일 형식으로 입력해주세요." id="loginId">
                                    <a id="btn_doubleCheck" class="btn_type Material_icons" onClick="fn_checkUserId()">중복확인</a>
                                </dd>
                            </dl>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>비밀번호</label></dt>
                                <dd><input type="password" class="input_txt" id="userPwd">
                                    <span class="box_dec  NanumGothic">영문,숫자 혼합하여 5자리~15자리 이내로 입력해주세요.</span>
                                </dd>
                            </dl>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic">비밀번호확인</label></dt>
                                <dd><input type="password" class="input_txt"  id="userPwd2"></dd>
                            </dl>
                        </li>
                        
                        <li class="info manager_info column1">
                            <div class="info_tit NanumGothic">업체 정보</div>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>회사명</label></dt>
                                <dd><input type="text" class="input_txt" id="bizName">
                                </dd>
                            </dl>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>대표자명</label></dt>
                                <dd><input type="text" class="input_txt" id="ownerName">
                                </dd>
                            </dl>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>사업자번호</label></dt>
                                <dd><input type="text" class="input_txt" id="businessNo"></dd>
                            </dl>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic">주소</label></dt>
                                <dd><input type="text" class="input_txt" id="companyAddr"></dd>
                            </dl>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic">직인파일 업로드</label></dt>
                                <dd><input type="file" class="input_txt size10" id="attachFile" name="attachFile"></dd>
                            </dl>
                        </li>
                        
						<li class="tit NanumGothic">
							<span>※ 직인 이미지는 배경이 투명한 이미지(PNG)파일로 등록 부탁드리며, <br>이미지 사이즈(Pixel)는 150x85 사이즈로 작업해주시기 바랍니다.</span>
						</li>
						
                        <input type="hidden" id="bizId">
                        <input type="hidden" id="userId">
                        <input type="hidden" id="userType">
                        
                        <li class="info manager_info column1">
                            <div class="info_tit NanumGothic">담당자 정보</div>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>담당자명</label></dt>
                                <dd><input type="text" class="input_txt" id="empName">
                                </dd>
                            </dl>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>이메일 주소</label></dt>
                                <dd><input type="email" class="input_txt" id="eMail"></dd>
                            </dl>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>휴대폰 번호</label></dt>
                                <dd><input type="tel" class="input_txt" id="phoneNum">
                                </dd>
                            </dl>
                        </li>
                    </ul>
                    <div id="btn_area" class="btnGroup1">
                        <a class="btn_type Material_icons" onClick="fn_memberJoin()">회원가입</a>
                    </div>
				</div><!-- terms_inner_step02 -->
			</div> <!-- terms_wrap  -->
		</section>
	</div>
</body>
</html>