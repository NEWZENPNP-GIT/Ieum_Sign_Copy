// 전역으로 처리 예정
var userInfo = {
		loginType: getCookie('loginType'),
		loginName: getCookie('loginName'),
  	};
var bizInfo;
var bbsInfo;
var infoModify;
var model = {
		currPage: 1
  	};

var naverUrl = "";
var kakaoUrl = "";
var googleUrl = "";

// tab 영역 초기 값
var viewTab = 'tabActive_0';

$(document).ready(function() {
	getUserInfo();
	
	//버튼생성
	var btnGroupHtml = "";
	
	//수퍼관리자
	/*if(userInfo.loginType == 9){
		btnGroupHtml += "<a class='btn_type type2' onClick='dataTrannnsferPop();'>이관 데이터</a>";
	}*/
	if(userInfo.loginType >= 5){
		btnGroupHtml += "<a class='btn_type type1' onClick='fncInfoModify();'>정보 수정</a>";
	}
	btnGroupHtml += "<a class='btn_type type2' onClick='openPopPwChange()'>비밀번호 변경</a>";
	
	$("#btnGroupDiv").html(btnGroupHtml);

});


function getUserInfo() {

    var formData = new FormData();
	
	var url = rootPath + 'mypage/getMyPage.do';
	
	//blockUi 호출
	fncOnBlockUI();
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		//contentType: "application/json; charset=UTF-8",
		//data: JSON.stringify(params),
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {
			
			if (res.success) {
				if(res.total > 0){
					bizInfo = res['data'];
					bbsInfo = res['bbs'];
					infoModify = bizInfo[0].userId;
				    
					var bizInfoTabHtml = "";
					for(var idx = 0 ; idx < bizInfo.length ; idx++){
						bizInfoTabHtml += "<li>";
						
						if(viewTab == 'tabActive_'+idx){
							bizInfoTabHtml += "<a class='btn_type active' onClick='tabButton("+idx+")' id='bizTabBtn_"+idx+"' >"+bizInfo[idx].bizName+"</a>";
						}else{
							bizInfoTabHtml += "<a class='btn_type' onClick='tabButton("+idx+")' id='bizTabBtn_"+idx+"' >"+bizInfo[idx].bizName+"</a>";
						}
		          
			          	bizInfoTabHtml += "</li>";
					}				
					$("#bizInfoTab").html(bizInfoTabHtml);
					
					
					var viewTabHtml = "";
					var strDisplay = "";
					for(var idx = 0 ; idx < bizInfo.length ; idx++){
						var info = bizInfo[idx];
						
						if(viewTab == 'tabActive_'+idx){
							strDisplay = "";
						}else{
							strDisplay = "style='display:none'";
						}
						
						viewTabHtml +="<div class='box_wrap' id='tabActive_"+idx+"' "+strDisplay+" >";
						viewTabHtml +="<div class='group_row row2'>";
						viewTabHtml +="<dl class='NanumGothic rat2-8'>";
						viewTabHtml +="<dt>";
						viewTabHtml +="<label class='label_txt NanumGothic'>사번</label>";
						viewTabHtml +="</dt>";
						viewTabHtml +="<dd class='default_box'>";
						viewTabHtml +="<div class='' id='info_empNo'>"+info.empNo+"</div>";
						viewTabHtml +="</dd>";
						viewTabHtml +="</dl>";
						viewTabHtml +="<dl class='NanumGothic rat2-8'>";
						viewTabHtml +="<dt>";
						viewTabHtml +="<label class='label_txt NanumGothic'>회사명</label>";
						viewTabHtml +="</dt>";
						viewTabHtml +="<dd class='default_box'>";
						viewTabHtml +="<div class='' id='info_bizName'>"+info.bizName+"</div>";
						viewTabHtml +="</dd>";
						viewTabHtml +="</dl>";
						viewTabHtml +="</div>";
						viewTabHtml +="<div class='group_row row2'>";
						viewTabHtml +="<dl class='NanumGothic rat2-8'>";
						viewTabHtml +="<dt>";
						viewTabHtml +="<label class='label_txt NanumGothic'>이름(담당자)</label>";
						viewTabHtml +="</dt>";
						viewTabHtml +="<dd class='default_box'>";
						viewTabHtml +="<div class=''>"+info.empName+"</div>";
						viewTabHtml +="</dd>";
						viewTabHtml +="</dl>";
						viewTabHtml +="<dl class='NanumGothic rat2-8'>";
						viewTabHtml +="<dt>";
						viewTabHtml +="<label class='label_txt NanumGothic'>이메일</label>";
						viewTabHtml +="</dt>";
						viewTabHtml +="<dd class='default_box'>";
						viewTabHtml +="<div class=''>"+info.EMail+"</div>";
						viewTabHtml +="</dd>";
						viewTabHtml +="</dl>";
						viewTabHtml +="</div>";
						viewTabHtml +="<div class='group_row row2'>";
						viewTabHtml +="<dl class='NanumGothic rat2-8'>";
						viewTabHtml +="<dt>";
						viewTabHtml +="<label class='label_txt NanumGothic'>부서</label>";
						viewTabHtml +="</dt>";
						viewTabHtml +="<dd class='default_box'>";
						viewTabHtml +="<div class=''>"+info.deptName+"</div>";
						viewTabHtml +="</dd>";
						viewTabHtml +="</dl>";
						viewTabHtml +="<dl class='NanumGothic rat2-8'>";
						viewTabHtml +="<dt>";
						viewTabHtml +="<label class='label_txt NanumGothic'>휴대전화</label>";
						viewTabHtml +="</dt>";
						viewTabHtml +="<dd class='default_box'>";
						viewTabHtml +="<div class=''>"+phoneNumberHyphen(info.phoneNum)+"</div>";
						viewTabHtml +="</dd>";
						viewTabHtml +="</dl>";
						viewTabHtml +="</div>";		
						viewTabHtml +="<div class='group_row row2'>";
						viewTabHtml +="<dl class='NanumGothic rat2-8'>";
						viewTabHtml +="<dt>";
						viewTabHtml +="<label class='label_txt NanumGothic'>입사일</label>";
						viewTabHtml +="</dt>";
						viewTabHtml +="<dd class='default_box'>";
						viewTabHtml +="<div class=''>"+ formatYYYYMMDD(info.joinDate)+"</div>";
						viewTabHtml +="</dd>";
						viewTabHtml +="</dl>";
						viewTabHtml +="<dl class='NanumGothic rat2-8'>";
						viewTabHtml +="<dt>";
						viewTabHtml +="<label class='label_txt NanumGothic'>아이디</label>";
						viewTabHtml +="</dt>";
						viewTabHtml +="<dd class='default_box'>";
						viewTabHtml +="<div class=''>"+info.loginId+"</div>";
						viewTabHtml +="</dd>";
						viewTabHtml +="</dl>";
						viewTabHtml +="</div>";
						viewTabHtml +="<div class='group_row row2'>";
						viewTabHtml +="<dl class='NanumGothic rat2-8'>";
						viewTabHtml +="<dt>";
						viewTabHtml +="<label class='label_txt NanumGothic'>퇴사일</label>";
						viewTabHtml +="</dt>";
						viewTabHtml +="<dd class='default_box'>";
						viewTabHtml +="<div class=''>"+formatYYYYMMDD(info.leaveDate)+"</div>";
						viewTabHtml +="</dd>";
						viewTabHtml +="</dl>";
						viewTabHtml +="<dl class='NanumGothic rat2-8'>";
						viewTabHtml +="<dt>";
						viewTabHtml +="<label class='label_txt NanumGothic'>권한</label>";
						viewTabHtml +="</dt>";
						viewTabHtml +="<dd class='default_box'>";
						viewTabHtml +="<div class=''>"+info.empTypeName+"</div>";
						viewTabHtml +="</dd>";
						viewTabHtml +="</dl>";
						viewTabHtml +="</div>";		
						viewTabHtml +="</div>";
					}
	
					$("#viewTab").html(viewTabHtml);
					$("#snsViewTab").html('');
					snsInfo = res['sns'];
					
					kakaoUrl = res.kakaoUrl;
					naverUrl = res.naverUrl;
					googleUrl = res.googleUrl;
					
					var kakaoCnt = 0;
					var naverCnt = 0;
					var googleCnt = 0;
					
					$.each(snsInfo, function(i, row){
						switch(row.snsType){
						case "N" :
							naverCnt++;
							var htmlData = '<tr><th><img src="/css/image/login_sns_naver.png" alt="naver"> 네이버</th>';
							htmlData += '<td class="email">'+row.snsEMail+'</td>';
							htmlData += '<td class="date">(연결 : '+convertDateTime(row.insDate).format("yyyy-MM-dd")+')</td>';
							htmlData += '<td class="btn" onclick="fn_disConnSns(\''+row.snsType+'\',\''+row.userSnsId+'\');">';
							htmlData += '	<a>연결해제</a>';
							htmlData += '</td></tr>';
							$("#snsViewTab").append(htmlData);
							break;
						case "K" : 
							kakaoCnt++;
							var htmlData = '<tr><th><img src="/css/image/login_sns_kakao.png" alt="kakao"> 카카오</th>';
							htmlData += '<td class="email">'+row.snsEMail+'</td>';
							htmlData += '<td class="date">(연결 : '+convertDateTime(row.insDate).format("yyyy-MM-dd")+')</td>';
							htmlData += '<td class="btn" onclick="fn_disConnSns(\''+row.snsType+'\',\''+row.userSnsId+'\');">';
							htmlData += '	<a>연결해제</a>';
							htmlData += '</td></tr>';
							$("#snsViewTab").append(htmlData);
							break;
						case "G" :
							googleCnt++;
							var htmlData = '<tr><th><img src="/css/image/login_sns_google.png" alt="google"> 구글</th>';
							htmlData += '<td class="email">'+row.snsEMail+'</td>';
							htmlData += '<td class="date">(연결 : '+convertDateTime(row.insDate).format("yyyy-MM-dd")+')</td>';
							htmlData += '<td class="btn" onclick="fn_disConnSns(\''+row.snsType+'\',\''+row.userSnsId+'\');">';
							htmlData += '	<a>연결해제</a>';
							htmlData += '</td></tr>';
							$("#snsViewTab").append(htmlData);
							break;
						}
					});

					if(kakaoCnt == 0){
						var htmlData = '<tr><th><img src="/css/image/login_sns_kakao.png" alt="kakao"> 카카오</th>';
						htmlData += '<td class="email">연결된 정보가 없습니다.</td>';
						htmlData += '<td class="date"></td>';
						htmlData += '<td class="btn" onclick="fn_kakao_login();">';
						htmlData += '	<a class="on">연결하기</a>';
						htmlData += '</td></tr>';
						$("#snsViewTab").append(htmlData);
					}
					if(naverCnt == 0){
						var htmlData = '<tr><th><img src="/css/image/login_sns_naver.png" alt="kakao"> 네이버</th>';
						htmlData += '<td class="email">연결된 정보가 없습니다.</td>';
						htmlData += '<td class="date"></td>';
						htmlData += '<td class="btn" onclick="fn_naver_login();">';
						htmlData += '	<a class="on">연결하기</a>';
						htmlData += '</td></tr>';
						$("#snsViewTab").append(htmlData);
					}
					if(googleCnt == 0){
						var htmlData = '<tr><th><img src="/css/image/login_sns_google.png" alt="google"> 구글</th>';
						htmlData += '<td class="email">연결된 정보가 없습니다.</td>';
						htmlData += '<td class="date"></td>';
						htmlData += '<td class="btn" onclick="fn_google_login();">';
						htmlData += '	<a class="on">연결하기</a>';
						htmlData += '</td></tr>';
						$("#snsViewTab").append(htmlData);
					}
					
					
					$("#info_empNo").html(bbsInfo['empNo']);
					$("#info_bizName").html(bbsInfo['bizName']);
					$.unblockUI();
				} else {
					jAlert('선택된 기업으로는 회원정보 조회 또는 수정이 불가능합니다.', '', fn_loadFailed);
					$.unblockUI();
					//location.href="/menu/goMainMenu.do";
				}
			}else{
				$.unblockUI();
				jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
			}
			
		},
		error:function(request,status,error){
			$.unblockUI();
			if (request.status=="401") {
				clearSession();
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
			}
		}
	});

	function fn_loadFailed(){
		location.href="/menu/goMainMenu.do";
	}
}

//탭이동
function tabButton(index) {
	
	for(var idx = 0 ; idx < bizInfo.length ; idx++){
		$("#bizTabBtn_"+idx).removeClass("active");
		$("#tabActive_"+idx).hide();
	}	
	
	infoModify = bizInfo[index].userId;
	viewTab = "tabActive_" + index;
	
	$("#bizTabBtn_"+index).addClass("active");
	$("#tabActive_"+index).show();

}

//정보수정
function fncInfoModify(){
	
	$("#contents_wrap").empty();
	$(window).off("resize");
	
	$.ajax({
		url: rootPath + "html/mypage/myPage_edit_companyInfo.html",
		success: function(result) {			
			//setCookie("paramUserId", infoModify);
			
			$("#contents_wrap").html(result);
		},
		error: function(request, status, error) {
			jAlert("다시 확인해주시기 바랍니다."+error);
		}
	});
	
}

//비밀번호 변경
function openPopPwChange() {
    //this.popPwChange.open();
	openWin(rootPath + "html/mypage/myPage_popup_pwchange.html","popPwChange","600","435");
}


function dataTrannnsferPop(){
	openWin(rootPath + "html/feb/febsystem/yearTax_system_dataControl.html","dataTrannnsferPop","1280","768");
}

function fn_disConnSns(snsType, userSnsId){
	
	jConfirm("SNS 로그인을 해제하시면,\r\n해당 SNS 계정으로는 로그인이 불가능합니다.\r\n연결을 해제하시겠습니까?", "", function(r){
		if(r){
			fncOnBlockUI();
			var url = "";
			
			switch(snsType){
				case "K" : url = "/sns/delKakaoLogin.do"; break;
				case "N" : url = "/sns/delNaverLogin.do"; break;
				case "G" : url = "/sns/delGoogleLogin.do"; break;
			}
			
			$.ajax({
				url:url,
			    crossDomain : true,
				dataType:"json",
				type:"POST",
				data: {
					userSnsId:userSnsId
				},
				success:function(res) {		
					if (res.success == "Y") {
						getUserInfo();
						jAlert('연결이 해제되었습니다.');
				        $.unblockUI();
					}else{
						$.unblockUI();
						jAlert('오류가 발생하였습니다.\r\nMessage : '+res.message);
						getUserInfo();
					}    
				},
				error:function(request,status,error){
					$.unblockUI();			
					if (request.status=="401") {
						location.href=rootPath;
						jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
					} else {
						jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
					}
				}
			});
		}
	});
}

function fn_kakao_login(){
	openWin(kakaoUrl, "카카오로그인", 500, 700);
}

function fn_naver_login(){
	openWin(naverUrl, "네이버로그인", 500, 700);
}

function fn_google_login(){
	openWin(googleUrl, "구글로그인", 500, 700);
}
