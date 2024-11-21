
var inCheckUpload = false;

// 계약 ID
var contractId;
// 사업장 ID
var placeId;
// 회사 정보
var companyInfo;
// 세무서 정보
var code401Info;
// 선택된 세무서 명
var chkTaxName;
// 지방소득세납세지 정보
var code402Info;
// 선택된 지방소득납세지 명
var chkLocalName;
// 계약 년도
var yearInfo;
var chkYearNm;
// 회사 기본설정 정보
var getCompanyStatus;
// 단위과세자 여부
var isSetData;

var model = {
	setCompanyInfoParams : {
	      'bizType' : '',
	      '계약ID' : '',
	      '사업장ID': '',
	      '지점여부' : '',
	      '사업장명' : '',
	      '대표자명' : '',
	      '사업자등록번호' : '',
	      '법인등록번호_개인식별번호' : '',
	      '대상인원수' : '',
	      '계약년도' : '',
	      '회사주소1' : '',
	      '회사전화1' : '',
	      '회사팩스1' : '',
	      '회사주소2' : '',
	      '회사전화2' : '',
	      '회사팩스2' : '',
	      '단위과세자여부' : '',
	      '종사업자일련번호' : '',
	      '관할세무서코드' : '',
	      '지방소득세납세지' : '',
	      '관할세무서코드명' : '',
	      '지방소득세납세지명' : '',
	      '홈택스아이디' : '',
	      '제출담당부서명' : '',
	      '제출담당자성명' : '',
	      '제출담당자전화번호' : ''
		}
	};

// ngSelect 관할 세무서
var selectOfficeInfo = [];
// ngSelect 법정동
var selectAreaIncome = [];

	  
$(document).ready(function() {
	
	contractId = getCookie('yearContractId');
    isSetData = false;
    
    //계약아이디가 없으면 메인으로 튕겨내기
    if (isNull(contractId)) {
    	alert( '사용불가 메뉴 입니다. 기본설정값을 확인 해주세요' );
    	window.location.href = rootPath;
    } else {
    	
    	//페이지 이동체크
    	$(window).on("beforeunload", function(){
    		if(this.checkUnload) return "이 페이지를 벗어나면 작성된 내용은 저장되지 않습니다.";
    	});   
    	
    	if($("#saveBtn").length > 0){
    		$("#saveBtn").click(function() {
    			checkUnload = false;	    
    		});
    	}
    	
    	getStatus();
    }
    
});


//회사 기본 설정 조회
function getStatus() {
	
	var params = {
		계약ID: contractId
	};
	
	var url = rootPath + 'febsystem/getYS010List.do';
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"GET",
		data: params,
		success:function(res) {
			
			console.log("## result.success : " + res.success );
			
			if (res.success) {
				
				if(res['data'].length > 0){
					getCompanyStatus = res['data']['0'];
					
					if (res['data'][0]['사업장관리여부'] == '1') {
						isSetData = true;
						model.setCompanyInfoParams['단위과세자여부'] = '1';
					}
					
					getCompanyInfo();  // 사업장 본점 정보 조회
				}else{
					jAlert('회사 기초정보가 없습니다.' , 'System Message', function(){
						window.location.href = rootPath;
					});
				}
			}else{
				jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
			}
		},
		error:function(request,status,error){
			if (request.status=="401") {
				alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
				window.location.href = rootPath;				
			} else {
				alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
			}
		}
	});
	
}

// 회사 정보 조회
function getCompanyInfo() {
  
	var params = { // ajax data params
		계약ID: contractId,
		지점여부: '0',
		startPage: 0,
		endPage: 10
	};
	
	//blockUi 호출
	fncOnBlockUI();
	
	var url = rootPath + 'febsystem/getYS030List.do';
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"GET",
		data: params,
		success:function(res) {
			
			console.log("## res['total'] : " + res['total'] );
			
			if ( res['total'] > 0 ) {
				
				code401Info = res['code401'];
		        code402Info = res['code402'];
		        yearInfo = res['yearData'];
		        
		        companyInfo = res['data'][0];
		        placeId = res['data'][0]['사업장ID'];
		        
		        model.setCompanyInfoParams.bizType      = res['data'][0]['bizType'];
		        model.setCompanyInfoParams.계약ID      = res['data'][0]['계약ID'];
		        model.setCompanyInfoParams.사업장ID   = res['data'][0]['사업장ID'];
		        model.setCompanyInfoParams.지점여부      = res['data'][0]['지점여부'];
		        model.setCompanyInfoParams.사업장명      = res['data'][0]['사업장명'];
		        model.setCompanyInfoParams.대표자명      = res['data'][0]['대표자명'];
		        model.setCompanyInfoParams.사업자등록번호   = res['data'][0]['사업자등록번호'];
		        model.setCompanyInfoParams.법인등록번호_개인식별번호 = res['data'][0]['법인등록번호_개인식별번호'];
		        model.setCompanyInfoParams.대상인원수 = res['data'][0]['대상인원수'];
		        // model.setCompanyInfoParams.계약년도 = res['data'][0]['계약년도'];
		        model.setCompanyInfoParams.회사주소1   = res['data'][0]['회사주소1'];
		        model.setCompanyInfoParams.회사전화1   = res['data'][0]['회사전화1'];
		        model.setCompanyInfoParams.회사팩스1   = res['data'][0]['회사팩스1'];
		        model.setCompanyInfoParams.회사주소2   = res['data'][0]['회사주소2'];
		        model.setCompanyInfoParams.회사전화2   = res['data'][0]['회사전화2'];
		        model.setCompanyInfoParams.회사팩스2   = res['data'][0]['회사팩스2'];
		        model.setCompanyInfoParams.단위과세자여부   = res['data'][0]['단위과세자여부'];
		        model.setCompanyInfoParams.종사업자일련번호    = '0000';
		        model.setCompanyInfoParams.관할세무서코드   = res['data'][0]['관할세무서코드'];
		        model.setCompanyInfoParams.지방소득세납세지    = res['data'][0]['지방소득세납세지'];
		        model.setCompanyInfoParams.홈택스아이디    = res['data'][0]['홈택스아이디'];
		        model.setCompanyInfoParams.제출담당부서명   = res['data'][0]['제출담당부서명'];
		        model.setCompanyInfoParams.제출담당자성명   = res['data'][0]['제출담당자성명'];
		        model.setCompanyInfoParams.제출담당자전화번호   = res['data'][0]['제출담당자전화번호'];
		        
		       if (!isNull(model.setCompanyInfoParams.관할세무서코드)) {
		        	// 관할세무서 명
		            chkTaxName = code401Info.filter(function(attr){
		            	return attr['commCode'] == model.setCompanyInfoParams.관할세무서코드
		            });
		            model.setCompanyInfoParams.관할세무서코드명   = res['data'][0]['관할세무서코드'] + '  [ ' + chkTaxName[0]['commName'] + ' ]' ;
		        }
		       	if (!isNull(model.setCompanyInfoParams.지방소득세납세지)) {
		       		// 지방소득납세지 명
		       		chkLocalName = code402Info.filter(function(attr){
		       			return attr['commCode'] == model.setCompanyInfoParams.지방소득세납세지;
		       		});
		       		model.setCompanyInfoParams.지방소득세납세지명    = res['data'][0]['지방소득세납세지'] + '  [ ' + chkLocalName[0]['commName'] + ' ]';
		        }
		       
		       	for (var index = 0; index < yearInfo.length; index++) {
		       		if ( index == 0) {
		       			chkYearNm = yearInfo[index]['febYear'];
		            } else {
		            	chkYearNm = chkYearNm + ' , ' + yearInfo[index]['febYear'];
		            }
		       	}
		       	
		       	model.setCompanyInfoParams.계약년도 = chkYearNm;
		       	
		       	
		       	//html 셋팅
		       	if(model.setCompanyInfoParams['bizType'] == '1'){		       		
		       		$("#no1-1").attr("checked", true);
		       		$("#no1-2").attr("checked", false);
		       	}else if(model.setCompanyInfoParams['bizType'] == '2'){
		       		$("#no1-1").attr("checked", false);
		       		$("#no1-2").attr("checked", true);
		       	}
		       	
		       	$("#model_setCompanyInfoParams_사업장명").val(model.setCompanyInfoParams['사업장명']);
		       	$("#model_setCompanyInfoParams_대표자명").val(model.setCompanyInfoParams['대표자명']);
		       	$("#model_setCompanyInfoParams_사업자등록번호").val(bizNumberHyphen(model.setCompanyInfoParams['사업자등록번호']));
		       	$("#model_setCompanyInfoParams_법인등록번호_개인식별번호").val(juminNumberHyphen(model.setCompanyInfoParams['법인등록번호_개인식별번호']));
		       	$("#model_setCompanyInfoParams_계약년도").html(model.setCompanyInfoParams['계약년도']);		       	
		       	$("#model_setCompanyInfoParams_대상인원수").html(model.setCompanyInfoParams['대상인원수']);		       	
		       	$("#model_setCompanyInfoParams_회사주소1").val(model.setCompanyInfoParams['회사주소1']);
		       	$("#model_setCompanyInfoParams_회사주소2").val(model.setCompanyInfoParams['회사주소2']);		       	
		       	$("#model_setCompanyInfoParams_회사전화1").val(phoneNumberHyphen(model.setCompanyInfoParams['회사전화1']));
		       	$("#model_setCompanyInfoParams_회사전화2").val(model.setCompanyInfoParams['회사전화2']);		       	
		       	$("#model_setCompanyInfoParams_회사팩스1").val(phoneNumberHyphen(model.setCompanyInfoParams['회사팩스1']));
		       	$("#model_setCompanyInfoParams_회사팩스2").val(model.setCompanyInfoParams['회사팩스2']);
		       	
		       	if(model.setCompanyInfoParams['단위과세자여부'] == '1'){		       		
		       		$("#no2-1").attr("checked", true);
		       		$("#no2-2").attr("checked", false);
		       	}else if(model.setCompanyInfoParams['단위과세자여부'] == '0'){
		       		$("#no2-1").attr("checked", false);
		       		$("#no2-2").attr("checked", true);
		       	}
		       	$("#model_setCompanyInfoParams_종사업자일련번호").html(model.setCompanyInfoParams['종사업자일련번호']);	
		       	
		       	
			}else {
				code401Info = res['code401'];
		        code402Info = res['code402'];
		        yearInfo = res['yearData'];
			}
			
			
			// 관할세무서
			selectAreaIncome = code401Info;
			var strSelected = "";
			var selectAreaIncomeHtml = "";
			if(selectAreaIncome != null){
				for(var index = 0 ; index < selectAreaIncome.length ; index++){		
					
					if(selectAreaIncome[index]['commCode'] == model.setCompanyInfoParams.관할세무서코드){
						strSelected = "selected";
					}else{
						strSelected = "";
					}
					
					selectAreaIncomeHtml += "<option value='"+selectAreaIncome[index]['commCode']+"' "+strSelected+" >  "+selectAreaIncome[index]['commCode'] + '   ' + selectAreaIncome[index]['commName']+" </option>";
				}
			}			
			$("#model_setCompanyInfoParams_관할세무서코드명").html(selectAreaIncomeHtml);
			
			// 지방소득납세지
		    selectOfficeInfo = code402Info;
		    var selectOfficeInfoHtml = "";
			if(selectOfficeInfo != null){
				for(var index = 0 ; index < selectOfficeInfo.length ; index++){		
					
					if(selectOfficeInfo[index]['commCode'] == model.setCompanyInfoParams.지방소득세납세지){
						strSelected = "selected";
					}else{
						strSelected = "";
					}
					
					selectOfficeInfoHtml += "<option value='"+selectOfficeInfo[index]['commCode']+"' "+strSelected+" >  "+selectOfficeInfo[index]['commCode'] + '   ' + selectOfficeInfo[index]['commName']+" </option>";
				}
			}			
			$("#model_setCompanyInfoParams_지방소득세납세지명").html(selectOfficeInfoHtml);
			
			$(".chosen-select").trigger("chosen:updated");

			$("#model_setCompanyInfoParams_홈택스아이디").val(model.setCompanyInfoParams['홈택스아이디']);
			$("#model_setCompanyInfoParams_제출담당부서명").val(model.setCompanyInfoParams['제출담당부서명']);
			$("#model_setCompanyInfoParams_제출담당자성명").val(model.setCompanyInfoParams['제출담당자성명']);
			$("#model_setCompanyInfoParams_제출담당자전화번호").val(phoneNumberHyphen(model.setCompanyInfoParams['제출담당자전화번호']));
						
			//document Event 셋팅 
			setDocumentEvent();
			
			$.unblockUI();
		},
		error:function(request,status,error){
			$.unblockUI();
			
			if (request.status=="401") {
				alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
				window.location.href = rootPath;
			} else {
				alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
			}
		}
	});
		  
}


//시스템관리 > 기본설정 > 저장
function saveSystemCompony() {
  
	var formData = new FormData();

	model.setCompanyInfoParams.bizType = $("input:radio[name=no1]:checked").val();
	model.setCompanyInfoParams.사업장명 = $("#model_setCompanyInfoParams_사업장명").val();
	model.setCompanyInfoParams.대표자명 = $("#model_setCompanyInfoParams_대표자명").val();
	model.setCompanyInfoParams.사업자등록번호 = $("#model_setCompanyInfoParams_사업자등록번호").val().replace(/-/gi,"");
	model.setCompanyInfoParams.법인등록번호_개인식별번호 = $("#model_setCompanyInfoParams_법인등록번호_개인식별번호").val().replace(/-/gi,"");
	model.setCompanyInfoParams.회사주소1 = $("#model_setCompanyInfoParams_회사주소1").val();
	model.setCompanyInfoParams.회사주소2 = $("#model_setCompanyInfoParams_회사주소2").val();
	model.setCompanyInfoParams.회사전화1 = $("#model_setCompanyInfoParams_회사전화1").val().replace(/-/gi,"");
	model.setCompanyInfoParams.회사팩스1 = $("#model_setCompanyInfoParams_회사팩스1").val().replace(/-/gi,"");	
	
	if($("input:radio[id='no2-1']").is(":checked")){
		model.setCompanyInfoParams.단위과세자여부 = '1';
	}else if($("input:radio[id='no2-2']").is(":checked")){ 
		model.setCompanyInfoParams.단위과세자여부 = '0';
	}else{
		model.setCompanyInfoParams.단위과세자여부 = '';
	}
	
	model.setCompanyInfoParams.종사업자일련번호 = $("#model_setCompanyInfoParams_종사업자일련번호").val();	
	model.setCompanyInfoParams.관할세무서코드 = $("#model_setCompanyInfoParams_관할세무서코드명").val();
	model.setCompanyInfoParams.지방소득세납세지 = $("#model_setCompanyInfoParams_지방소득세납세지명").val();
	model.setCompanyInfoParams.홈택스아이디 = $("#model_setCompanyInfoParams_홈택스아이디").val();
	model.setCompanyInfoParams.제출담당부서명 = $("#model_setCompanyInfoParams_제출담당부서명").val();
	model.setCompanyInfoParams.제출담당자성명 = $("#model_setCompanyInfoParams_제출담당자성명").val();
	model.setCompanyInfoParams.제출담당자전화번호 = $("#model_setCompanyInfoParams_제출담당자전화번호").val().replace(/-/gi,"");
		
	
	formData.append('bizType', model.setCompanyInfoParams.bizType );
	formData.append('계약ID', contractId );
	formData.append('사업장ID', model.setCompanyInfoParams.사업장ID );
	formData.append('사업장명', model.setCompanyInfoParams.사업장명 );
	formData.append('지점여부', model.setCompanyInfoParams.지점여부 );
	formData.append('대표자명', model.setCompanyInfoParams.대표자명 );
	formData.append('사업자등록번호', model.setCompanyInfoParams.사업자등록번호 );
	formData.append('법인등록번호_개인식별번호', model.setCompanyInfoParams.법인등록번호_개인식별번호 );
	//formData.append('대상인원수', model.setCompanyInfoParams.대상인원수 );
	//formData.append('계약년도', model.setCompanyInfoParams.계약년도 );
	formData.append('회사주소1', model.setCompanyInfoParams.회사주소1 );
	formData.append('회사전화1', model.setCompanyInfoParams.회사전화1 );
	formData.append('회사팩스1', model.setCompanyInfoParams.회사팩스1 );
	formData.append('회사주소2', model.setCompanyInfoParams.회사주소2 );
	//formData.append('회사전화2', model.setCompanyInfoParams.회사전화2 );
	//formData.append('회사팩스2', model.setCompanyInfoParams.회사팩스2 );
	formData.append('단위과세자여부', model.setCompanyInfoParams.단위과세자여부 );
	formData.append('종사업자일련번호', model.setCompanyInfoParams.종사업자일련번호 );
	formData.append('관할세무서코드', model.setCompanyInfoParams.관할세무서코드 );
	formData.append('지방소득세납세지', model.setCompanyInfoParams.지방소득세납세지 );
	formData.append('홈택스아이디', model.setCompanyInfoParams.홈택스아이디 );
	formData.append('제출담당부서명', model.setCompanyInfoParams.제출담당부서명 );
	formData.append('제출담당자성명', model.setCompanyInfoParams.제출담당자성명 );
	formData.append('제출담당자전화번호', model.setCompanyInfoParams.제출담당자전화번호 );

	// 필수값, 유효성검사 체크
	// 법인 / 개인
	if (model.setCompanyInfoParams.bizType == '') {
		alert('법인 또는 개인을 선택해주세요.');
		return false;
	}
	// 사업자등록번호
	if (model.setCompanyInfoParams.사업자등록번호 == '') {
		alert('사업자등록번호를 입력해주세요.');
		return false;
	}
	// 사업자등록번호 유효성검사
	if (!bzSanCheck(model.setCompanyInfoParams['사업자등록번호'])) {
		alert('사업자등록번호 형식이 아닙니다.');
		return false;
	}

	// 주민등록번호
	if (model.setCompanyInfoParams.법인등록번호_개인식별번호 == '') {
		alert('주민(외국인) 등록번호를 입력해주세요.');
		return false;
	}
	// 20181023 법인/개인 선택에 따라 법인이면 법인번호만, 개인 이면 주민/외국인 번호만 검사 하도록 변경
	if (model.setCompanyInfoParams['bizType'] == '1' ) {
		
		if(model.setCompanyInfoParams['법인등록번호_개인식별번호'].length != 13){
			jAlert('법인등록번호 형식이 아닙니다.','');
			//$('#model_setCompanyInfoParams_법인등록번호_개인식별번호').focus().select();
			return false;
		}
		
		// 20190220 법인등록번호 유효성 체크 숨김
		// if (!Validate.bznCheck(this.model.setCompanyInfoParams['법인등록번호_개인식별번호'])) {
		//   alert('법인등록번호 형식이 아닙니다.');
		//   $('#rrnbzn').focus().select();
		//   return false;
		// }
	} else {
		
		if(model.setCompanyInfoParams['법인등록번호_개인식별번호'].length != 13){
			jAlert('주민(외국인) 등록번호 형식이 아닙니다.','');
			$('#model_setCompanyInfoParams_법인등록번호_개인식별번호').focus().select();
			return false;
		}else{		
			// 주민등록번호 유효성검사
			if (!rrnCheck(model.setCompanyInfoParams['법인등록번호_개인식별번호'])) {
				if (!fgnCheck(model.setCompanyInfoParams['법인등록번호_개인식별번호'])) {
					jAlert('주민(외국인) 등록번호 형식이 아닙니다.','');
					$('#model_setCompanyInfoParams_법인등록번호_개인식별번호').focus().select();
					return false;
				}
			}
		}
	}

	// 20181023 법인/개인 선택에 따라 법인이면 법인번호만, 개인 이면 주민/외국인 번호만 검사 하도록 변경에 따른 주석처리
	// // 주민등록번호 유효성검사
	// if (!Validate.allCheck(this.model.setCompanyInfoParams['법인등록번호_개인식별번호'])) {
	//   alert('주민(법인) 등록번호 형식이 아닙니다.');
	//   return false;
	// }

	if (isEmpty(model.setCompanyInfoParams['관할세무서코드'])) {
		alert('관할세무서코드를 입력해주세요.');
		return false;
	}
	if (isEmpty(model.setCompanyInfoParams['지방소득세납세지'])) {
		alert('지방소득세납세지를 입력해주세요.');
		return false;
	}
	if (isEmpty(model.setCompanyInfoParams['홈택스아이디'])) {
		alert('홈택스아이디를 입력해주세요.');
		return false;
	}
	if (isEmpty(model.setCompanyInfoParams['제출담당부서명'])) {
		alert('제출 담당 부서명를 입력해주세요.');
		return false;
	}
	if (isEmpty(model.setCompanyInfoParams['제출담당자성명'])) {
		alert('제출 담당자 성명를 입력해주세요.');
		return false;
	}
	if (isEmpty(model.setCompanyInfoParams['제출담당자전화번호'])) {
		alert('제출 담당자 전화번호를 입력해주세요.');
		return false;
	}
 
	var url = rootPath + 'febsystem/updYS030.do';
		
	//blockUi 호출
	fncOnBlockUI();
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {	
			
			if (res['success']) {				
				updMyPageBiz();
			}else {
				alert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.');
		    }
			
			$.unblockUI();
		},
		error:function(request,status,error){
			$.unblockUI();
			
			if (request.status=="401") {
				alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
				window.location.href = rootPath;
			} else {
				alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
			}
		}
	});

}

//시스템 관리 > 회사 정보  > 회사 정보 수정 - 회사 주소 1 은 기업정보 수정해야함
function updMyPageBiz(){
	
	var formData = new FormData();
	formData.append('계약ID', model.setCompanyInfoParams.계약ID );
	formData.append('addr1', model.setCompanyInfoParams.회사주소1 );
    
	var url = rootPath + 'mypage/updMyPageBizInfo.do';
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {				
			if (res['success']) {
				alert('저장 되었습니다.');
			} else {
				alert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.');
			}						
		},
		error:function(request,status,error){
			$.unblockUI();
			
			if (request.status=="401") {
				alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
				window.location.href = rootPath;
			} else {
				alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
			}
		}
	});
}



// 20181023 법인/개인 선택에 따라 법인이면 법인번호만, 개인 이면 주민/외국인 번호만 검사 하도록 변경
function changechknum() {
	
	model.setCompanyInfoParams['bizType'] = $("input:radio[name=no1]:checked").val() 
	model.setCompanyInfoParams['법인등록번호_개인식별번호'] = $("#model_setCompanyInfoParams_법인등록번호_개인식별번호").val().replace(/-/gi,"");	
	$("#model_setCompanyInfoParams_법인등록번호_개인식별번호").val(juminNumberHyphen($("#model_setCompanyInfoParams_법인등록번호_개인식별번호").val()));
	

	// 20181023 법인/개인 선택에 따라 법인이면 법인번호만, 개인 이면 주민/외국인 번호만 검사 하도록 변경
	if (model.setCompanyInfoParams['bizType'] == '1' ) {
		// 20190220 법인등록번호 유효성 체크 숨김
		// if ( this.model.setCompanyInfoParams['법인등록번호_개인식별번호'] !== '') {
		//   if (!Validate.bznCheck(this.model.setCompanyInfoParams['법인등록번호_개인식별번호'])) {
		//     alert('법인등록번호 형식이 아닙니다.');
		//     $('#rrnbzn').focus().select();
		//     return false;
		//   }
		// }
	} else {
		// 주민등록번호 유효성검사		
		if ( model.setCompanyInfoParams['법인등록번호_개인식별번호'] != '') {
			if (!rrnCheck(model.setCompanyInfoParams['법인등록번호_개인식별번호'])) {
				if (!fgnCheck(model.setCompanyInfoParams['법인등록번호_개인식별번호'])) {
					alert('주민(외국인) 등록번호 형식이 아닙니다.');
					//$('#model_setCompanyInfoParams_법인등록번호_개인식별번호').focus().select();
					$('#model_setCompanyInfoParams_법인등록번호_개인식별번호').focus();
					$("input:radio[id='no1-1']").prop("checked", true);
		       		$("input:radio[id='no1-2']").prop("checked", false);
					return false;
				}
			}
		}
	}
	
}

//사업자번호 변경
function changeBizNumber(){
	$("#model_setCompanyInfoParams_사업자등록번호").val(bizNumberHyphen($("#model_setCompanyInfoParams_사업자등록번호").val()));
}

//전화번호 변경
function changePhoneNumber(objId){
	$("#"+objId).val(phoneNumberHyphen($("#"+objId).val()));
}

// 사업자단위과세자 여부 선택
function chkTax(val) {
	// 사업자단위과세자 여부가 '여' 이면 단위과세자여부 '부' 선택 안됨
	if (isSetData == true ) {
		if ( val != 1 ) {
			//$("#no2-1").attr("checked", true);
       		//$("#no2-2").attr("checked", false);
       		//$("#no2-1").css("background","url(/css/image/point/btn_radio_checked.png) no-repeat 50% 50%");       		
			$("input:radio[id='no2-1']").prop("checked", true);
       		$("input:radio[id='no2-2']").prop("checked", false);
       		
			alert('기본설정값을 확인 해주세요.');			
			model.setCompanyInfoParams.단위과세자여부 = 1;
			
			return false;
		} else {
			model.setCompanyInfoParams.단위과세자여부 = val;
		}
	} else {
		model.setCompanyInfoParams.단위과세자여부 = val;
	}
	console.log('this.model.setCompanyInfoParams.단위과세자여부', model.setCompanyInfoParams.단위과세자여부);
}


//주소 검색 팝업 오픈
function goAddressSearch() {
	window.open(rootPath+"html/commute/address.html", "zipcode", "width=400, height=680");
}
function setSearchAddr(result){
	//$("#edinfo_zipCode").val(result.zonecode);
	$("#model_setCompanyInfoParams_회사주소1").val(result.addr.replace(result.zonecode+' ',''));
	$("#model_setCompanyInfoParams_회사주소2").val('');
}
