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
<link rel="stylesheet" type="text/css" href="/css/member_companySearch.css">
<script src="/js/jquery.min.js"></script>
<script src="/js/common.js"></script>
<script type="text/javascript" src="/mobile/assets/cordova/cordova_load.js"></script>

<script type="text/javascript">
	var chkUserId = false;
	var chkUserIdSave = "";
	var chkCert = false;
	var chkBizId = "";
	var chkBizName = "";

	$(document).ready(function() {
		$("#userType").val(getURLParameters("menuType"));
		
		// 모바일 뒤로가기 버튼 표시
		if (!isNull(appType)) {
			$('.mobile-btns').removeClass('hidden');
		}
		
		snsType = "${snsType}";

		if(!(snsType == "K" || snsType == "N" || snsType == "G")){
			$(".showPwd").removeClass("hidden");
		} else {
			$("#loginId").val("${snsEmail}");
			$("#empName").val("${snsName}");
			$("#eMail").val("${snsEmail}");
		}
		
	});
	
	// 아이디 중복확인
	function fn_checkUserId() {
		var url = rootPath+"user/getUserList.do";
		
		var loginId = $("#loginId").val().trim();
		
		if (loginId.length==0) {
			alert("아이디를 입력해주시기 바랍니다.");
			return;
		}
		if(!checkEmail(loginId)) {
			alert("아이디는 이메일 형식으로 입력해주시기 바랍니다.");
			return;
		}
		chkUserId = false;
		
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
		
	}
	
	// 기업 검색
	function fn_findBizId() {		
		var url = rootPath+"biz/findBiz.do";
		
		var startPage = 0;
		var endPage = 10;
		var bizName = $("#bizName").val();
		$("#findBizName").val(bizName);
		
		if(bizName.length<2) {
			alert("2자이상 입력해주시기 바랍니다.");
			return;
		}
		
		$("#bizId").val("");
		$("#bizName").val("");

		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"GET",
			data: {
				bizName:bizName,
				startPage:startPage,
				endPage:endPage
			},
			success:function(result)
			{
				if (result.success==true) {
					if(result.total==1) {
						$.each(result.data, function(i, row) {
							bizId = row.bizId;
							bizName = row.bizName;
						});
						
						fn_setBiz(bizId, bizName);
							
						alert("["+bizName+"]으로 검색이 되었습니다.\r\n해당 기업이 맞다면 <확인>을 선택하여 계속 진행해주시기 바랍니다.");
					} else if(result.total>1) {
						
						
						var url = rootPath+"html/popup/popup_biz.html?bizName="+encodeURIComponent(bizName);
						if (isNull(appType)) {
							openWin(url, "searchBiz", 610, 470);
						} else {
							// 모바일
							$("#companySearch_wrap").removeClass("hidden");
							$("#companySearch_wrap").css('height', $(document).height());
							fn_searchBizId();
						}
						//alert("검색된 기업정보가 여러건이 존재합니다.\r\n다시 정확히 입력해주시기 바랍니다.");						
					} else {
						alert("입력하신 정보로 기업정보를 찾을 수 없습니다.\r\n다시 정확히 입력해주시기 바랍니다.");
					}
				} else {
					alert("입력하신 기업명이 존재하지 않습니다.\r\n 정확히 입력해주시기 바랍니다.");
				}
			},
			error:function(request,status,error){
				alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	}
	
	function fn_setBiz(bizId, bizName) {
		$("#bizId").val(bizId);
		$("#bizName").val(bizName);
	}
	
	// 인증번호 전송
	function fn_certSend() {		
		var url = rootPath+"user/insUserJoinCert.do";
		
		if(chkCert) {
			alert("이미 직원인증을 진행하였습니다.");
			return;
		}
		
		chkCert = false;
		
		var bizId = $("#bizId").val();
		var userId = $("#userId").val();
		//var userDate = $("#userDate").val();
		//var empName = $("#empName").val();
		//var phoneNum = $("#phoneNum").val();
		
		//userDate = hyphenRemove(userDate);
		//phoneNum = hyphenRemove(phoneNum);
		
		if (bizId.length==0 ) {
			alert("인증번호 발급을 위한 정보(회사이름)를 입력해주시기 바랍니다.");
			return;
		}
		if(userId.length == 0){
			alert("입력하신 정보로 직원정보를 찾을 수 없습니다.\r\n다시 정확히 입력해주시기 바랍니다.");
			return;
		}
		/*if (userDate.length==0 ) {
			alert("인증번호 발급을 위한 정보(생년월일)를 입력해주시기 바랍니다.");
			return;
		}
		if (empName.length==0 ) {
			alert("인증번호 발급을 위한 정보(성명)를 입력해주시기 바랍니다.");
			return;
		}
		if (phoneNum.length==0) {
			alert("인증번호 발급을 위한 정보(휴대폰번호)를 입력해주시기 바랍니다.");
			return;
		}*/
		
		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"POST",
			data: {
				bizId:bizId,
				userId:userId
				//userDate:userDate,
				//empName:empName,
				//phoneNum:phoneNum
			},
			success:function(result)
			{
				if (result.success==true) {
					//$("#userId").val(result.data);
					if(result.total>0) {
						$("#countMinute").show();
						alert("입력하신 휴대폰 번호로 인증번호를 발송하였습니다.\r\n인증번호를 입력하여 주시기 바랍니다.");
						clearInterval($("#countMinute").attr("timer"));
						
						getDateCount($("#countMinute"));
					} else {
						alert("입력하신 정보로 직원정보를 찾을 수 없습니다.\r\n다시 정확히 입력해주시기 바랍니다.");
					}
				} else {
					alert("입력하신 정보가 존재하지 않습니다.\r\n 정확히 입력해주시기 바랍니다.");
				}
			},
			error:function(request,status,error){
				alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	}
	
	// 직원인증
	function fn_certEmp() {		
		var url = rootPath+"user/getCertEmp.do";
		
		var bizId = $("#bizId").val();
		var userDate = $("#userDate").val();
		var empName = $("#empName").val();
		var phoneNum = $("#phoneNum").val();
		
		userDate = hyphenRemove(userDate);
		phoneNum = hyphenRemove(phoneNum);
		
		if (bizId.length==0 ) {
			alert("인증번호 발급을 위한 정보(회사이름)를 입력해주시기 바랍니다.");
			return;
		}
		if (userDate.length==0 ) {
			alert("인증번호 발급을 위한 정보(생년월일)를 입력해주시기 바랍니다.");
			return;
		}
		if (empName.length==0 ) {
			alert("인증번호 발급을 위한 정보(성명)를 입력해주시기 바랍니다.");
			return;
		}
		if (phoneNum.length==0) {
			alert("인증번호 발급을 위한 정보(휴대폰번호)를 입력해주시기 바랍니다.");
			return;
		}
		
		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"POST",
			data: {
				bizId:bizId,
				userDate:userDate,
				empName:empName,
				phoneNum:phoneNum
			},
			success:function(result)
			{
				if (result.success==true) {
					console.log(result);
					if(result.total == 1){
						$("#userId").val(result.data[0].userId);
						fn_certSend();
					} else if(result.total == 0){
						if(result.join_total <= 0){
							alert("입력하신 정보가 존재하지 않습니다.\r\n정확히 입력해주시기 바랍니다.");
						} else {
							alert("이미 가입되어 있습니다.\r\n로그인을 진행해주세요.");
						}
					} else {
						alert("동일한 인사정보가 두 개 이상 존재합니다.\r\n인사담당자에게 문의해주시기 바랍니다.");
					}
				} else {
					alert("입력하신 정보가 존재하지 않습니다.\r\n정확히 입력해주시기 바랍니다.");
				}
			},
			error:function(request,status,error){
				alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	}
	
	// 인증번호 확인
	function fn_checkCert() {
		var url = rootPath+"user/getUserJoinCert.do";
		
		var phonNo = $("#phoneNum").val();
		var certNo = $("#certNo").val();
		
		phonNo = hyphenRemove(phonNo);
		if(chkCert) {
			alert("이미 직원인증을 진행하였습니다.");
			return;
		}
		if (phonNo.length==0 || certNo.length==0 ) {
			alert("인증번호 확인을 위한 정보를 입력해주시기 바랍니다.");
			return;
		}
		
		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"GET",
			data: {
				phonNo:phonNo,
				certNo:certNo
			},
			success:function(result)
			{
				if (result.success==true) {
					if(result.total==1) {
						chkCert = true;
						$("#countMinute").hide();
						alert("입력하신 인증번호가 정상적입니다.\r\n회원가입을 계속 진행하여 주시기 바랍니다.");
					} else {
						alert("입력하신 인증번호가 존재하지 않거나 인증번호 발송한 시간이 지난 경우입니다.\r\n다시 인증번호 발송을 진행하여 주시기 바랍니다.");
					}
				} else {
					alert("입력하신 정보가 존재하지 않습니다.\r\n 정확히 입력해주시기 바랍니다.");
				}
			},
			error:function(request,status,error){
				alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	}
	
	// 사용자 서비스 가입
	function fn_memberJoin() {
		if(!confirm("가입 신청을 하시겠습니까?")) return;
		
		var url = rootPath+"user/insUser.do";
		
		var loginId = $("#loginId").val().trim();
		var userId = $("#userId").val();
		var userPwd = $("#userPwd").val();
		var userPwd2 = $("#userPwd2").val();
		var userName = $("#empName").val();
		var userType = $("#userType").val();

		if (loginId.length==0) {
			alert("아이디를 입력해주시기 바랍니다.");
			return;
		}
		if (userId.length==0) {
			alert("인증절차를 진행해주시기 바랍니다.");
			return;
		}
		if(!(snsType == "K" || snsType == "N" || snsType == "G")){
			if (userPwd.length==0) {
				alert("비밀번호를 입력해주시기 바랍니다.");
				return;
			}
			if(!checkPassword(userPwd)) {
				return;
			}
			if(userPwd!=userPwd2) {
				alert("입력하신 비밀번호가 맞지 않습니다.\r\n다시 비밀번호를 입력해주시기 바랍니다.");
				return;
			}
		}
		if (userName.length==0) {
			alert("성명을 입력해주시기 바랍니다.");
			return;
		}
		if (userName.indexOf("_") != -1) {
			alert("성명에 특수문자(_)를 입력하실  수 없습니다.");
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
		if(!chkCert) {
			alert("직원인증 절차를 먼저 진행해주시기 바랍니다.");
			return;
		}

		var formData = new FormData();
		formData.append("userId",userId);
	    formData.append("loginId",loginId);
	    formData.append("userType",userType);
	    formData.append("userName",userName);

	    if(!(snsType == "K" || snsType == "N" || snsType == "G")){
	   		formData.append("userPwd",userPwd);
	    }
	    
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
			},
			error:function(request,status,error){
				alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	}
	
	function runScript(e) {
		if (e.keyCode == 13) {
			fn_searchBizId();
			return false;
		}
	}

	function fn_setBizId() {
		var selectBizId = "";
		var selectBizName = "";
	    var chk = document.getElementsByName("chk");
	    var tot = chk.length;
	    var cnt = 0;
	    for (i = 0; i < tot; i++) {
	        if (chk[i].checked == true) {
	            var bizInfo = chk[i].value;
	            var bizData = bizInfo.split("_");
	            selectBizId = bizData[0];
	            selectBizName = bizData[1];
	            cnt++;
	        }
	    }
	    if(cnt!=1) {
	    	alert("검색된 회사에서 하나만 선택하여 주시기 바랍니다.");
	    	return;
	    }
	    fn_setBiz(selectBizId, selectBizName);
	    // 레이어팝업 숨김
	    $("#companySearch_wrap").addClass("hidden");
	    //window.close();
	}
	
	// 기업 검색
	function fn_searchBizId() {
		var url = rootPath+"biz/findBiz.do";
		
		var startPage = 0;
		var endPage = 10;
		var searchBizName = $("#findBizName").val();

		if(searchBizName.length<2) {
			alert("2자이상 입력해주시기 바랍니다.");
			return;
		}

		$.ajax({
			url:url,
		    crossDomain : true,
			dataType:"json",
			type:"GET",
			data: {
				bizName:searchBizName,
				startPage:startPage,
				endPage:endPage
			},
			success:function(result)
			{
				if (result.success==true) {
					var htmlData = "";
					$.each(result.data, function(i, row) {
						htmlData+="<tr class=\"databox\">";
						htmlData+="	<td class=\"chk_area\"><input type=\"checkbox\" id=\"chk\" name=\"chk\" value='"+row.bizId+"_"+row.bizName+"'></td>";
						htmlData+="	<td class=\"title\">"+row.bizName+"</td>";
						htmlData+="	<td class=\"companyNo\">"+row.businessNo+"</td>"; 
						htmlData+="	<td class=\"ceoName\">"+row.ownerName+"</td>";
						htmlData+="</tr>";						
					});
					
					$("#dataList").html(htmlData);
				} else {
					alert("입력하신 기업명이 존재하지 않습니다.\r\n 정확히 입력해주시기 바랍니다.");
				}
			},
			error:function(request,status,error){
				alert("입력하신 정보를 다시 확인해주시기 바랍니다.");
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		});
	}
	
	function fn_close(){
		// 레이어팝업 숨김
	    $("#companySearch_wrap").addClass("hidden");
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
							<span><span class="dot_red">*</span>로 표시된 항목은 필수입력 사항입니다.</span>
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
                            <dl class="input_bx showPwd hidden">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>비밀번호</label></dt>
                                <dd><input type="password" class="input_txt" id="userPwd">
                                    <span class="box_dec  NanumGothic">영문,숫자 혼합하여 5자리~15자리 이내로 입력해주세요.</span>
                                </dd>
                            </dl>
                            <dl class="input_bx showPwd hidden">
                                <dt><label class="label_txt NanumGothic">비밀번호확인</label></dt>
                                <dd><input type="password" class="input_txt"  id="userPwd2"></dd>
                            </dl>
                        </li>
                        
                        <input type="hidden" id="bizId">
                        <input type="hidden" id="userId">
                        <input type="hidden" id="userType">
                        
                        <li class="info manager_info column1">
                            <div class="info_tit NanumGothic">직원 정보</div>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>회사이름</label></dt>
                                <dd><input type="text" class="input_txt" id="bizName">
                                    <a id="btn_companyCheck" class="btn_type Material_icons" onClick="fn_findBizId()">검 색</a>
                                </dd>
                            </dl>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>생년월일</label></dt>
                                <dd><input type="text" class="input_txt" id="userDate"><span class="box_dec NanumGothic">예) 19971231</span></dd>
                            </dl>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>성명</label></dt>
                                <dd><input type="text"class="input_txt size06" id="empName"></dd>
                            </dl>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>휴대폰 번호</label></dt>
                                <dd><input type="tel" class="input_txt" id="phoneNum">
                                    <a id="btn_certSend" class="btn_type Material_icons" onClick="fn_certEmp()">인증번호 발송</a>
									<span id="countMinute" class="label_txt" sec="180" style="display:none;">
										<span id="min">0</span>분
										<span id="sec">0</span>초
									</span>
                                </dd>
                            </dl>
                            <dl class="input_bx">
                                <dt><label class="label_txt NanumGothic"><span class="dot_red">*</span>인증번호</label></dt>
                                <dd><input type="number" class="input_txt" id="certNo">
                                    <a id="btn_certCheck" class="btn_type Material_icons" onClick="fn_checkCert()">확 인</a>
                                </dd>
                            </dl>
                        </li>
						<li class="tit NanumGothic">
							<span>회사에 등록되어 있는 인사정보를 기준으로 가입이 진행되오니 <br> 가입절차 진행이 원할하지 않는경우 회사 담당자에게 문의하시기 바랍니다.</span>
						</li>
                    </ul>
                    <div id="btn_area" class="btnGroup1">
                        <a class="btn_type Material_icons" onClick="fn_memberJoin()">회원가입</a>
                    </div>
				</div><!-- terms_inner_step02 -->
			</div> <!-- terms_wrap  -->
		</section>
	</div>
	
	<!--  POPUP class(숨기기:hidden) -->
	<div id="companySearch_wrap" class="hidden">
		<ul id="popup_bg">
			<li class="popup_frame popup_name">
				<div class="icon"></div><div class="text NanumGothic">회사검색</div><div class="btn_type popup_close Material_icons" onclick="fn_close()">close</div>
			</li>
			<li class="popup_frame popup_tit">
				<div class="bg"><span class="NanumGothic">재직중이신 회사명을 검색하여 등록하세요. 단, 회사명이 검색되지 않으면 우선 기업담당자가 이음싸인 기업회원으로 가입하셔야 됩니다.</span></div>
			</li>
			
			<li class="popup_frame sehInputBox solo">
			<!-- 
				<select id="searchKey">
					<option value=""> 회사명</option>
					<option value=""> 사업자번호</option>
					<option value=""> 대표자명</option>
				</select>
			 -->
				<form>
					<fieldset>
					<input type="text" id="findBizName" class="word_input NanumGothic" placeholder="회사명" onkeypress="return runScript(event)">
					<a onclick="fn_searchBizId()" class="btn_search btn_type Material_icons">search</a>
					</fieldset>
				</form>
			</li>
			<li class="popup_frame popup_res">
			<!-- 
			<div class="txt_area"><span class="seh_word font_black NanumGothic" id="searchName">'뉴젠'</span><span class="res_Word font_black NanumGothic"> 검색결과</span><span class="notfind">'검색결과가 없습니다.'</span></div>
			 -->
			<div class="seh_res" style="height : 220px">
				<table>
					<colgroup>
						<col width="6%">
						<col width="47%">
						<col width="28%">
						<col width="20%">
					</colgroup>
					<thead>
						<tr>
							<th class="chk_area"></th>
							<th>회사명</th>
							<th>사업자번호</th> 
							<th>대표자명</th>
						</tr>
					</thead>
					<tbody id="dataList">
						<tr class="databox">
							<td colspan="4">데이터가 존재하지 않습니다.</td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- 
			<div class="pagingnav_wrap">
			    <ul id="pagingNav">
			        <li><a class="first_page arr left"></a><a class="btn_prev arr right"></a></li>
			        <li class="paging_numbers"><a id="p1" class="btn_type">1</a><a id="p2" class="btn_type">2</a><a id="p2" class="btn_type">3</a>
			        <a id="p2" class="btn_type">4</a><a id="p2" class="btn_type">5</a></li>
			        <li><a class="btn_next arr left "></a><a class="end_page arr right"></a></li>
			    </ul>
			</div>
			 -->
			</li>
			<li class="popup_btnGroup">
				<a onclick="fn_setBizId()" class="btn_type check">확인</a><a onclick="fn_close()" class="btn_type close">닫기</a>
			</li>
		</ul>
	</div><!-- companySearch_wrap -->
</body>
</html>