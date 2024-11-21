// 계약 ID
var contractId;
//사용자 ID
var userId;
//2018 분기
// 계약 년도
var febYear;
// 일련번호 ID
var seqNo;
// 사업장 ID
var workplaceId;
// 근무시기
var workPeriod;

//주소 담아놓기
var addressText;
var addressTextBox;

//금액체크
var isOver = false;
var month10;
var month20;
var month100;
var month300;
var year240;
var year300;
var year3000;

//근로자 데이터 입력 및 수정 여부 체크
var workerEditCheckCode;

//계약년도
var contractYear;

var useDepartment = {
	    '근로자부서표시여부코드': '',
	};

//비과세 사용여부 카운트
var cntTaxExem;
//급여항목 사용 여부
var taxExList = {
		  '계약ID': '',
		  'bizId': '',
		  'code': '',
		  'dbMode': '',
		  'febYear': '',
		  'g01': '',
		  'h01': '',
		  'h05': '',
		  'h06': '',
		  'h07': '',
		  'h08': '',
		  'h09': '',
		  'h10': '',
		  'h11': '',
		  'h12': '',
		  'h13': '',
		  'h14': '',
		  'h15': '',
		  'h16': '',
		  'i01': '',
		  'k01': '',
		  'm01': '',
		  'm02': '',
		  'm03': '',
		  'message': '',
		  'o01': '',
		  'q01': '',
		  'r10': '',
		  'r11': '',
		  's01': '',
		  't01': '',
		  't10': '',
		  't11': '',
		  't12': '',
		  't20': '',
		  'y02': '',
		  'y03': '',
		  'y04': '',
		  'y22': '',
		  '건강보험료': '',
		  '고용보험료': '',
		  '국민연금보험료': '',
		  '급여': '',
		  '등록일시': '',
		  '상여': '',
		  '소득세': '',
		  '수정일시': '',
		  '우리사주조합인출금': '',
		  '인정상여': '',
		  '임원퇴직소득금액한도초과액': '',
		  '장기요양보험료': '',
		  '주식매수선택권행사이익': '',
		  '지방소득세': '',
		  '납부특례세액_소득세' : '',
		  '납부특례세액_지방소득세' : '',
		  '납부특례세액_농어촌특별세' : '',
		  // 2018 분기
		  '직무발명보상금' : '',
		  't13': '0', // 2018신규 중소기업90% -047
		  'h17': '0', // 2018신규 종교활동비 -048
		  'u01': '0', // 2018신규 벤처기업 주식매수선택권 -049
		  '비과세한도초과액': '0' //
	};

var edinfo = {
	    '계약ID': '',
	    '사용자ID': '',
	    '근무시기': '',
	    '일련번호': '',
	    '원천명세ID': '',
	    'dbMode' : '',
	    '사업장ID': '',
	    '사용여부' : '',
	    'empNo': '',
	    'empName': '',
	    'EMail': '',
	    'joinDate': '',
	    'leaveDate': '',
	    'address1': '', // 주소 입력방식 변경
	    'address2': '', // 주소 입력방식 변경
	    'zipCode': '', // 주소 입력방식 변경
	    'phoneNum': '',
	    'deptName': '',
	    'positionName': '',
	    '진행상태코드': '',
	    '개인식별번호': '',
	    // '나이': '',
	    '부서ID' : '',
	    '부서표시여부': '',
	    '사내내선번호': '',
	    '외국법인파견근로자': '',
	    '내외국인구분': '',
	    '국가코드': '',
	    '거주구분': '',
	    '거주지국코드': '',
	    '외국인단일세율적용': '',
	    '국외근로제공여부': '',
	    '중소기업취업감면여부': '',
	    '감면기간_FROM': '',
	    '감면기간_TO': '',
	    '감면율': '',
	    '생산직등야간근로비과세': '',
	    '전년도총급여': '',
	    '세대주여부': '',
	    '소득세적용률': '',
	    '연말정산분납여부': '',
	    '종전근무지_납세조합유무': '',
	    '급여': '',
	    '상여': '',
	    '인정상여': '',
	    '주식매수선택권행사이익': '',
	    '우리사주조합인출금': '',
	    '임원퇴직소득금액한도초과액': '',
	    '소득세': '' ,
	    '지방소득세': '' ,
	    '농어촌특별세': '',
	    '건강보험료': '',
	    '장기요양보험료': '',
	    '국민연금보험료': '',
	    '고용보험료': '',
	    '공무원연금': '',
	    '군인연금': '',
	    '사립학교교직원연금': '',
	    '별정우체국연금': '',
	    '납부특례세액_소득세' : '1',
	    '납부특례세액_지방소득세' : '1',
	    '납부특례세액_농어촌특별세' : '1',
	    'm01': '',
	    'm02': '',
	    'm03': '',
	    'o01': '',
	    'q01': '',
	    'h08': '',
	    'h09': '',
	    'h10': '',
	    'g01': '',
	    'h11': '',
	    'h12': '',
	    'h13': '',
	    'h01': '',
	    'k01': '',
	    's01': '',
	    't01': '',
	    'y02': '',
	    'y03': '',
	    'y04': '',
	    'h05': '',
	    'i01': '',
	    'r10': '',
	    'h14': '',
	    'h15': '',
	    't10': '',
	    't11': '',
	    't12': '',
	    't20': '',
	    'h16': '',
	    'r11': '',
	    'h06': '',
	    'h07': '',
	    'y22': '',
	    // 2018 분기
	    '직무발명보상금' : '',
	    't13': '0', // 2018신규 중소기업90% -047
	    'h17': '0', // 2018신규 종교활동비 -048
	    'u01': '0', // 2018신규 벤처기업 주식매수선택권 -049
	    '비과세한도초과액': '0', //
	    '원천징수영수증_관리번호': ''
	};

//신고 금액 
var edinfo_aggre = {
		'm01': '0',
		'm02': '0',
		'm03': '0',
		'o01': '0',
		'q01': '0',
		'h08': '0',
		'h09': '0',
		'h10': '0',
		'g01': '0',
		'h11': '0',
		'h12': '0',
		'h13': '0',
		'h01': '0',
		'k01': '0',
		's01': '0',
		't01': '0',
		'y02': '0',
		'y03': '0',
		'y04': '0',
		'h05': '0',
		'i01': '0',
		'r10': '0',
		'h14': '0',
		'h15': '0',
		't10': '0',
		't11': '0',
		't12': '0',
		't20': '0',
		'h16': '0',
		'r11': '0',
		'h06': '0',
		'h07': '0',
		'y22': '0',
		// 2018 분기
	    '직무발명보상금' : '',
	    't13': '0', // 2018신규 중소기업90% -047
	    'h17': '0', // 2018신규 종교활동비 -048
	    'u01': '0', // 2018신규 벤처기업 주식매수선택권 -049
   	};		


$(document).ready(function() {
	
	contractId = sessionStorage.getItem('yearContractId');	
	// 2018 분기
	febYear = sessionStorage.getItem('febYear');
	
	userId = sessionStorage.getItem("paramUserId");
	workPeriod = sessionStorage.getItem("paramWorkPeriod");  // 근무시기
	
	
	logD("# contractId : " + contractId );
	logD("# userId : " + userId );
	logD("# febYear : " + febYear );
	logD("# workPeriod : " + workPeriod );
	
	// 계약아이디가 없으면 메인으로 튕겨내기
    if (isNull(contractId)) {    	
    	jAlert('사용불가 메뉴 입니다. 기본설정값을 확인 해주세요.' , 'System Message', goHome);    	
    } else {        	
    	//사용자 리스트 view
        $("#btn_left").prop('checked',false);
    	
    	getDepartment();
    	getTaxExem();
    	getList(userId);
    }
    
});


//부서 사용여부 가져오기
function getDepartment() {
	
	var formData = new FormData();
	formData.append("계약ID", contractId);
	
	var url = rootPath + 'febsystem/getYS010List.do';
	  
	//blockUi 호출
	fncOnBlockUI();
		
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		//async:false,
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {	
			
			logD("# res.success : " + res.success );

			if (res.success) {										  
				useDepartment = res['data'][0];
			} else {
				$.unblockUI();
				jAlert('부서 정보 조회에 실패하였습니다.','');				
			}

			//$.unblockUI();
			
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
		    
}


// 비과세 사용여부 가져오기
function getTaxExem() {

	var formData = new FormData();
	formData.append("계약ID", contractId);
	
  
	var url = rootPath + 'febsystem/getYS050.do';
  
	//blockUi 호출
	//fncOnBlockUI();
		
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		async:false,
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {	
			
			if (res.success) {										  
				if (res['data'] == undefined ) {
					res['data'] = taxExList;
				} else {
					taxExList = res['data'];
				}
								
				// 근로자 급여 정보 를 전체 오픈 시킨다. - 19.05.30
			    for (key in taxExList) {
			    	if(key != 'bizId' && key != 'code' && key != 'dbMode' && key != 'febYear' && key != 'message' 
			    		 && key != '계약ID' && key != '등록일시' && key != '수정일시'){
			    		taxExList[key] = '1';
			    	}
			    }
			    ///////////////////////////////////
			    			
				// 사용중인 비과세 항목의 합계를 구해야 함. => 모두 미사용이면 비과세 항목 비노출
			    cntTaxExem = 0; // 초기화
				
			    // 국외근로(월 100만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.m01);
			    // 국외근로(월 300만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.m02);
			    // 국외근로(전액)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.m03);
			    // 야간근로수당(년 240만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.o01);
			    // 출산보육수당(월 10만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.q01);
			    // 특별법 연구보조비(월 20만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h08);
			    // 연구기관 등 연구보조비(월 20만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h09);
			    // 기업부설연구소 연구보조비(월 20만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h10);
			    // 비과세학자금(납입금액)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.g01);
			    // 취재수당(월 20만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h11);
			    // 벽지수당(월 20만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h12);
			    // 재해관련급여(전액)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h13);
			    // 무보수위원수당(전액)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h01);
			    // 외국주둔군인등(전액)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.k01);
			    // 주식매수선택권(년 3,000만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.s01);
			    // 외국인기술자(년 근로소득세의 50% 한도)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.t01);
			    // 우리사주조합인출금50%(년 인출금의 50% 한도)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.y02);
			    // 우리사주조합인출금75%(년 인출금의 75% 한도)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.y03);
			    // 우리사주조합인출금100%(년 인출금의 100% 한도)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.y04);
			    // 경호･승선수당
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h05);
			    // 외국정부등근무자(전액)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.i01);
			    // 근로장학금(전액)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.r10);
			    // 보육교사 근무환경개선비(전액)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h14);
			    // 사립유치원수석교사･교사의 인건비(전액)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h15);
			    // 중소기업취업청년 소득세 감면100%(소득세의 100%)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.t10);
			    // 중소기업취업청년 소득세 감면50%(소득세의 50%)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.t11);
			    // 중소기업취업청년 소득세 감면70%(소득세의 70%)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.t12);
			    // 조세조약상 교직자감면
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.t20);
			    // 정부･공공기관지방이전기관 종사자 이주수당(월 20만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h16);
			    // 직무발명보상금(년 300만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.r11);
			    // 유아･초중등 연구보조비(월 20만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h06);
			    // 고등교육법 연구보조비(월 20만원)
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.h07);
			    // 전공의 수련 보조 수당
			    cntTaxExem = Number(cntTaxExem) + Number(taxExList.y22);
			    
			    // 2018 분기
			    if ( febYear == '2018') {
			    	// 중소기업 90%
			        if ( taxExList.t13 == '0' || taxExList.t13 == '1' ) {
			        	cntTaxExem = Number(cntTaxExem) + Number(taxExList.t13);
			        }
			        // 종교활동비
			        if ( taxExList.h17 == '0' || taxExList.h17 == '1' ) {
			        	cntTaxExem = Number(cntTaxExem) + Number(taxExList.h17);
			        }
			        // 벤처기업 주식매수선택권
			        if ( taxExList.u01 == '0' || taxExList.u01 == '1' ) {
			        	cntTaxExem = Number(cntTaxExem) + Number(taxExList.u01);
			        }
			    }
			    			    
			} else {
				$.unblockUI();
				jAlert('비과세 정보 조회에 실패하였습니다.','');				
			}
			
			//$.unblockUI();
			
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
}



// 근로자 조회
function getList(사용자ID)  {
	userId = 사용자ID;

    var formData = new FormData();
	formData.append("계약ID", contractId);
	formData.append("사용자ID", userId);
	formData.append("근무시기", workPeriod);
    
    var url = rootPath + 'febworker/getYP000.do';
    
	//blockUi 호출
	fncOnBlockUI();
		
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		async:false,
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {	
			
			logD("# getList res.success : " + res.success );			
			
			if (res.success) {			

				//CSS 초기화
				$("#yearTax_wrap").find(".overload").removeClass("overload");
				$("#yearTax_wrap").find(".colorY").removeClass("colorY");
				
				edinfo = res['data'];
				
				// edinfo.계약ID = contractId;
				edinfo['dbMode'] = 'R';
				addressTextBox =  res['data']['address'];
				edinfo.joinDate = edinfo.joinDate != '' ? moment(edinfo.joinDate, 'YYYYMMDD').format('YYYY-MM-DD') : '';
		        edinfo.leaveDate = edinfo.leaveDate != '' ? moment(edinfo.leaveDate, 'YYYYMMDD').format('YYYY-MM-DD') : '';
		        edinfo.감면기간_FROM = edinfo.감면기간_FROM != '' ? moment(edinfo.감면기간_FROM, 'YYYYMMDD').format('YYYY-MM-DD') : '';
		        edinfo.감면기간_TO = edinfo.감면기간_TO != '' ? moment(edinfo.감면기간_TO, 'YYYYMMDD').format('YYYY-MM-DD') : '';
		        logD("# edinfo.joinDate : " + edinfo.joinDate );
		        
		        addressText = '';		        
		        chkOver();
		        contractYear = res['계약년도'];
		        
		        // 기본값 설정
		        edinfo.세대주여부 = edinfo.세대주여부 != '' ? edinfo.세대주여부 : '1';
		        edinfo.국외근로제공여부 = edinfo.국외근로제공여부 != '' ? edinfo.국외근로제공여부 : '4';
		        edinfo.중소기업취업감면여부 = edinfo.중소기업취업감면여부 != '' ? edinfo.중소기업취업감면여부 : '2';
		        edinfo.생산직등야간근로비과세 = edinfo.생산직등야간근로비과세 != '' ? edinfo.생산직등야간근로비과세 : '2';
		        edinfo.소득세적용률 = edinfo.소득세적용률 != '' ? edinfo.소득세적용률 : '1';
		        edinfo.연말정산분납여부 = edinfo.연말정산분납여부 != '' ? edinfo.연말정산분납여부 : '2';
		        edinfo.종전근무지_납세조합유무 = edinfo.종전근무지_납세조합유무 != '' ? edinfo.종전근무지_납세조합유무 : '2';
		        edinfo.외국법인파견근로자 = edinfo.외국법인파견근로자 != '' ? edinfo.외국법인파견근로자  : '2';
		        
		        // 2018 분기
		        if ( febYear == '2018') {
		          edinfo.직무발명보상금 = edinfo.직무발명보상금 != '' ? edinfo.직무발명보상금  : '0';
		        }
		        
		        $("#edinfo_empNo").html(edinfo.empNo);
		        $("#edinfo_empName").val(edinfo.empName);
		        $("#edinfo_empName").focus();
		        $("#edinfo_개인식별번호").val(edinfo.개인식별번호);
		        $("#edinfo_joinDate").val(edinfo.joinDate);
		        $("#edinfo_leaveDate").val(edinfo.leaveDate);
		        $("#edinfo_positionName").val(edinfo.positionName);		        
		        $("#edinfo_감면기간_FROM").val(edinfo.감면기간_FROM);
		        $("#edinfo_감면기간_TO").val(edinfo.감면기간_TO);
		        $("#edinfo_phoneNum").val(edinfo.phoneNum);		        
		        $("#edinfo_사내내선번호").val(edinfo.사내내선번호);
		        $("#edinfo_EMail").val(edinfo.EMail);
		        $("#edinfo_원천징수영수증_관리번호").val(edinfo.원천징수영수증_관리번호);		        
		        $("#edinfo_zipCode").val(edinfo.zipCode);
		        $("#edinfo_address1").val(edinfo.address1);
		        $("#edinfo_address2").val(edinfo.address2);
		        		       
		        /*//// 과세 대상 급여  */
		        if(taxExList['급여'] == '1'){
		        	$("#edinfo_급여").val(addCommas(edinfo.급여));
		        	$("#taxExList_급여").show();
		        }
		        if(taxExList['상여'] == '1'){
		        	$("#edinfo_상여").val(addCommas(edinfo.상여));
		        	$("#taxExList_상여").show();
		        }
		        if(taxExList['인정상여'] == '1'){
		        	$("#edinfo_인정상여").val(addCommas(edinfo.인정상여));
		        	$("#taxExList_인정상여").show();
		        }
		        if(taxExList['주식매수선택권행사이익'] == '1'){
		        	$("#edinfo_주식매수선택권행사이익").val(addCommas(edinfo.주식매수선택권행사이익));
		        	$("#taxExList_주식매수선택권행사이익").show();
		        }
		        if(taxExList['우리사주조합인출금'] == '1'){
		        	$("#edinfo_우리사주조합인출금").val(addCommas(edinfo.우리사주조합인출금));
		        	$("#taxExList_우리사주조합인출금").show();
		        }
		        if(taxExList['임원퇴직소득금액한도초과액'] == '1'){
		        	$("#edinfo_임원퇴직소득금액한도초과액").val(addCommas(edinfo.임원퇴직소득금액한도초과액));
		        	$("#taxExList_임원퇴직소득금액한도초과액").show();
		        }
		        
		        if(febYear == '2018'){
			        if(taxExList['직무발명보상금'] == '1'){
			        	$("#edinfo_직무발명보상금").val(addCommas(edinfo.직무발명보상금));
			        	$("#taxExList_직무발명보상금").show();
			        }
		        }
		        if(taxExList['비과세한도초과액'] == '1'){
		        	$("#edinfo_비과세한도초과액").val(addCommas(edinfo.비과세한도초과액));
		        	$("#taxExList_비과세한도초과액").show();
		        }
		        /*  과세 대상 급여 /// */
		        
		        /*//// 2. 공제 대상 급여 */
		        if(taxExList['소득세'] == '1'){
		        	$("#edinfo_소득세").val(addCommas(edinfo.소득세));
		        	$("#taxExList_소득세").show();
		        }
		        if(taxExList['지방소득세'] == '1'){
		        	$("#edinfo_지방소득세").val(addCommas(edinfo.지방소득세));
		        	$("#taxExList_지방소득세").show();
		        }
		        if(taxExList['국민연금보험료'] == '1'){
		        	$("#edinfo_국민연금보험료").val(addCommas(edinfo.국민연금보험료));
		        	$("#taxExList_국민연금보험료").show();
		        }
		        if(taxExList['건강보험료'] == '1'){
		        	$("#edinfo_건강보험료").val(addCommas(edinfo.건강보험료));
		        	$("#taxExList_건강보험료").show();
		        }
		        if(taxExList['장기요양보험료'] == '1'){
		        	$("#edinfo_장기요양보험료").val(addCommas(edinfo.장기요양보험료));
		        	$("#taxExList_장기요양보험료").show();
		        }
		        if(taxExList['고용보험료'] == '1'){
		        	$("#edinfo_고용보험료").val(addCommas(edinfo.고용보험료));
		        	$("#taxExList_고용보험료").show();
		        }
		        if(taxExList['공무원연금'] == '1'){
		        	$("#edinfo_공무원연금").val(addCommas(edinfo.공무원연금));
		        	$("#taxExList_공무원연금").show();
		        }
		        if(taxExList['군인연금'] == '1'){
		        	$("#edinfo_군인연금").val(addCommas(edinfo.군인연금));
		        	$("#taxExList_군인연금").show();
		        }
		        if(taxExList['사립학교교직원연금'] == '1'){
		        	$("#edinfo_사립학교교직원연금").val(addCommas(edinfo.사립학교교직원연금));
		        	$("#taxExList_사립학교교직원연금").show();
		        }
		        if(taxExList['별정우체국연금'] == '1'){
		        	$("#edinfo_별정우체국연금").val(addCommas(edinfo.별정우체국연금));
		        	$("#taxExList_별정우체국연금").show();
		        }
		        if(taxExList['납부특례세액_소득세'] == '1'){
		        	$("#edinfo_납부특례세액_소득세").val(addCommas(edinfo.납부특례세액_소득세));
		        	$("#taxExList_납부특례세액_소득세").show();
		        }
		        if(taxExList['납부특례세액_지방소득세'] == '1'){
		        	$("#edinfo_납부특례세액_지방소득세").val(addCommas(edinfo.납부특례세액_지방소득세));
		        	$("#taxExList_납부특례세액_지방소득세").show();
		        }
		        if(taxExList['납부특례세액_농어촌특별세'] == '1'){
		        	$("#edinfo_납부특례세액_농어촌특별세").val(addCommas(edinfo.납부특례세액_농어촌특별세));
		        	$("#taxExList_납부특례세액_농어촌특별세").show();
		        }
		        /*2. 공제 대상 급여 ///// */
		        
		        // 신고대상 금액 계산  - 2019-05-28 (신고금액 표시)
		        taxAggregation();
		        /////////////////////////
		        
		        var nationListHtml = "<option value=''></option>";
		        for(var idx = 0 ; idx < res['NationCodeList'].length ; idx++){
		        	nationListHtml += "<option value='"+res['NationCodeList'][idx]['commCode']+"'>"+res['NationCodeList'][idx]['commCode']+" "+res['NationCodeList'][idx]['commName']+"</option>";
		        }		       		  		        
		        $("#edinfo_국가코드").html(nationListHtml);
		        $("#edinfo_국가코드").val(edinfo.국가코드);
		        $("#edinfo_거주지국코드").html(nationListHtml);
		        $("#edinfo_거주지국코드").val(edinfo.거주지국코드);
	        
		        // selectOptionYN
		        $("#edinfo_세대주여부").val(edinfo.세대주여부);
		        $("#edinfo_부서표시여부").val(edinfo.부서표시여부);
		        $("#edinfo_외국인단일세율적용").val(edinfo.외국인단일세율적용);		        
		        if(edinfo.내외국인구분 == '1'){
		        	$('#edinfo_외국인단일세율적용').next().hide();
		        	//$('#edinfo_외국인단일세율적용').next().fadeIn();
		        }
		        $("#edinfo_거주구분").val(edinfo.거주구분);
		        $("#edinfo_외국법인파견근로자").val(edinfo.외국법인파견근로자);
		        $("#edinfo_중소기업취업감면여부").val(edinfo.중소기업취업감면여부);
		        $("#edinfo_생산직등야간근로비과세").val(edinfo.생산직등야간근로비과세);
		        $("#edinfo_연말정산분납여부").val(edinfo.연말정산분납여부);
		        		        
		        //selectOption01
		        $("#edinfo_내외국인구분").val(edinfo.내외국인구분);
		        
		        //selectOption02
		        $("#edinfo_감면율").val(edinfo.감면율);
		        if($("#edinfo_중소기업취업감면여부").val() == '2'){
		        	$('#edinfo_감면율').next().hide();		        	
		        	$('#edinfo_감면기간_FROM').hide();
		        	$('#edinfo_감면기간_TO').hide();
		        	$('#edinfo_감면기간').hide();
		        }
		        
		        //selectOption03
		        $("#edinfo_국외근로제공여부").val(edinfo.국외근로제공여부);
		        
		        //selectOption04
		        $("#edinfo_소득세적용률").val(edinfo.소득세적용률);
		        
		        
		        var workplaceHtml = "<option value=''></option>";
		        for(var idx = 0 ; idx < res['ys030List'].length ; idx++){
		        	workplaceHtml += "<option value='"+res['ys030List'][idx]['사업장ID']+"'>"+res['ys030List'][idx]['사업장명']+"</option>";
		        }	
		        $("#edinfo_사업장ID").html(workplaceHtml);
		        $("#edinfo_사업장ID").val(edinfo.사업장ID);
		        
		        if (isNull(edinfo.사업장ID)) {
		            edinfo.사업장ID = res['ys030List'][0]['사업장ID'];
		            workplaceId  = res['ys030List'][0]['사업장ID'];
		            getDept();
		        } else {
		            workplaceId = edinfo.사업장ID;
		            getDept();
		        }

		        // 근로자 데이터 입력 및 수정 여부 체크
				workerEditCheckCode =  res['data']['진행상태코드'];	
				logI("# 근로자 상태코드 : " + workerEditCheckCode );
				//document Event 셋팅 
				setDocumentEvent();
				
				if (workerEditCheckCode == 4 ) {
					logD("# .workerEditCheck = true");
					
					objDisabled();
			    }else{			    	
			    	logD("# .workerEditCheck = false");
			    	
			    	objEnabled();
			    }

			} else {
				jAlert('근로자 정보 조회에 실패하였습니다.','');				
			}
			
			$.unblockUI();
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
}    


//부서 조회
function getDept() {
	
	var formData = new FormData();
	formData.append("계약ID", contractId);
	formData.append("사업장ID", workplaceId);
	formData.append("startPage", 0);
	formData.append("endPage", 10);
	

	var url = rootPath + 'febsystem/getYS031List.do';
    
	//blockUi 호출
	fncOnBlockUI();
		
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		//async:false,
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {				
			logD("# getDept res.success : " + res.success );						
			if (res.success) {	
				
				var departmentHtml = "<option value=''></option>";
				for(var idx = 0 ; idx < res['data'].length ; idx++){
					departmentHtml += "<option value='"+res['data'][idx]['부서ID']+"'>"+res['data'][idx]['부서명']+"</option>";
		        }	
		        $("#edinfo_부서ID").html(departmentHtml);
		        $("#edinfo_부서ID").val(edinfo.부서ID);
		        $("#edinfo_부서ID").trigger("chosen:updated");
		        
			} else {
				jAlert('부서 정보 조회에 실패하였습니다.','');				
			}
			
			$.unblockUI();	
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
}


//금액 초과시 컬러 변경
function chkOver() {
  
	var 근무시작일 = '';
	var 근무종료일 = '';
	
	if (isNull(edinfo.leaveDate) || edinfo.leaveDate.length != 8) {
		//상반기
		if(workPeriod == '1'){
			근무종료일 = moment(febYear + '0630', 'YYYYMMDD').format('YYYY-MM-DD');
		}
		//하반기
		else if(workPeriod == '2'){	
			근무종료일 = moment(febYear + '1231', 'YYYYMMDD').format('YYYY-MM-DD');
		}
	}else{
		근무종료일 = edinfo.leaveDate;
		
		var endtYear = moment(근무종료일, 'YYYYMMDD').format('YYYY');
	    if (Number(endtYear) > Number(febYear) ) {
	    	//상반기
	    	if(workPeriod == '1'){
	    		근무종료일 = moment(febYear + '0630', 'YYYYMMDD').format('YYYY-MM-DD');
	    	}
	    	//하반기
	    	else if(workPeriod == '2'){	
	    		근무종료일 = moment(febYear + '1231', 'YYYYMMDD').format('YYYY-MM-DD');
	    	}		            	
	    }
	    
	}

	var startYear = moment(edinfo.joinDate, 'YYYYMMDD').format('YYYY');
    if (Number(startYear) < Number(febYear) ) {
    	//상반기
    	if(workPeriod == '1'){
    		근무시작일 = moment(febYear + '0101', 'YYYYMMDD').format('YYYY-MM-DD');
    	}
    	//하반기
    	else if(workPeriod == '2'){
    		근무시작일 = moment(febYear + '0701', 'YYYYMMDD').format('YYYY-MM-DD');
    	}
    }
	
    logI("# 근무시작일 : " + 근무시작일 );
    logI("# 근무종료일 : " + 근무종료일 );
    
    // 총 근무 개월 수 계산
    var 근무시작월 = moment(근무시작일, 'YYYYMMDD').format('MM');
    var 근무종료월 = moment(근무종료일, 'YYYYMMDD').format('MM');
    var 근무개월 = Number(근무종료월) - Number(근무시작월) + 1;
    
    logI("# 근무개월 : " + 근무개월 );

    month10 = 근무개월 * 100000 + 1;
    month20 = 근무개월 * 200000 + 1;
    month100 = 근무개월 * 1000000 + 1;
    month300 = 근무개월 * 3000000 + 1;
    year240 = 2400000 + 1;
    year300 = 3000000 + 1;
    year3000 = 30000000 + 1;
    
}

/* 내외국인 구분 선택시에 내국인 선택시에 국적 KR로 변경 */
function onChangeLocalforeigner(){
  // 내국인 선택 -- 대한민국 선택
  if($("#edinfo_내외국인구분").val() == '1'){
	  $('#edinfo_외국인단일세율적용').next().hide();
	  $('#edinfo_외국인단일세율적용').val('').trigger("chosen:updated");
	  
      edinfo['국가코드'] = 'KR';
      $("#edinfo_국가코드").val('KR').trigger("chosen:updated");
  }else if($("#edinfo_내외국인구분").val() == '2'){
	  $('#edinfo_외국인단일세율적용').next().fadeIn();
  }
}

/* 거주구분이 Y일경우 거주지국 KR 선택 */
function onChangeResidence(obj){
  if($("#edinfo_거주구분").val() == '1'){
      edinfo['거주지국코드'] = 'KR';
      $("#edinfo_거주지국코드").val('KR').trigger("chosen:updated");
  }
}

/* 중소기업 취업감면 여부 */
function selectOption02(){
	if($("#edinfo_중소기업취업감면여부").val() == '1'){
		$('#edinfo_감면율').next().fadeIn(); 
		$('#edinfo_감면기간_FROM').show();
    	$('#edinfo_감면기간_TO').show();
    	$('#edinfo_감면기간').show();
	}else if($("#edinfo_중소기업취업감면여부").val() == '2'){
		$('#edinfo_감면율').next().hide();
		$('#edinfo_감면율').val('').trigger("chosen:updated");
		
		$('#edinfo_감면기간_FROM').hide();
    	$('#edinfo_감면기간_TO').hide();
    	$('#edinfo_감면기간').hide();
    	$('#edinfo_감면기간_FROM').val('');
    	$('#edinfo_감면기간_TO').val('');
	}
}

//사업장 선택시 부서 조회 실행
function ngSelectPlace() {
  workplaceId = $("#edinfo_사업장ID").val();
  getDept();
}


//지방소득세 계산
function calculating() {
  var value1 = parseFloat(removeCommas($("#edinfo_소득세").val()));
  var value2;
  
  if(!isNull(value1)){
	  edinfo['소득세'] = value1;
	  value2 = Math.floor(parseFloat( value1 * 0.1)) ;
	  
	  edinfo['지방소득세'] = value2;	  
	  $("#edinfo_지방소득세").val(addCommas(value2));
  }
}

//주소 검색 팝업 오픈
function goAddressSearch() {
	window.open(rootPath+"html/commute/address.html", "zipcode", "width=400, height=680");
}
function setSearchAddr(result){
	$("#edinfo_zipCode").val(result.zonecode);
	$("#edinfo_address1").val(result.addr.replace(result.zonecode+' ',''));
}


function onChangecalc() {
	var value1 = parseFloat(removeCommas($("#edinfo_r11").val()));
		
	// 과세 직무발명보상금이 사용 중이라면
	if ( taxExList['직무발명보상금'] == '1') {    	  
		var 비과세직무발명보상금액 = value1;
		if ( 비과세직무발명보상금액 > 3000000 ) {          
			edinfo['r11'] = '3000000';
			$("#edinfo_r11").val(addCommas('3000000'));
    	
			var 과세직무발명보상금 = parseFloat(비과세직무발명보상금액) - 3000000;
			edinfo['직무발명보상금'] = String(과세직무발명보상금);
			$("#edinfo_직무발명보상금").val(addCommas(edinfo['직무발명보상금']));
		}
	}
}

// 근로자 정보 저장(입력,수정)
function setemployeeDinfo() {

  setEdinfoData();
  
  var params  = new FormData();

  // 필수값 체크
  // 사업장 선택
  if (edinfo.사업장ID == '') {
    jAlert('사업장을 입력해주세요.','', function(){
	    $("#edinfo_사업장ID").focus();
	});
    return false;
  }
  // 성명
  if (edinfo.empName == '') {
	jAlert('성명을 입력해주세요.','', function(){
	    $("#edinfo_empName").focus();
	});
    return false;
  }
  // 주민등록번호
  if (edinfo.개인식별번호 == '') {
	  jAlert('주민(외국인) 등록번호를 입력해주세요.','', function(){
		    $("#edinfo_개인식별번호").focus();
	  });
      return false;
  }
  // 주민등록번호 유효성검사
  if ( !allCheck(edinfo.개인식별번호 ) ) {
	  jAlert('주민(외국인) 등록번호 형식이 아닙니다.','', function(){
	    $("#edinfo_개인식별번호").focus();
	  });
	  return false;
  }
  // 이메일
  if (edinfo.EMail == '') {
	  jAlert('이메일을 입력해주세요.','', function(){
	     $("#edinfo_EMail").focus();
	  });
	  return false;
  }
  // 이메일 유효성검사
  if ( !isEmail(edinfo.EMail )) {
	  jAlert('이메일 형식이 아닙니다.','', function(){
	      $("#edinfo_EMail").focus();
	  });
	  return false;
  }
  // 입사일자
  if (edinfo.joinDate == '') {
	  jAlert('입사일자를 입력해주세요.','', function(){
	      $("#edinfo_joinDate").focus();
	  });
	  return false;
  }

  // 퇴사일자
  if (edinfo.leaveDate != '' ) {
	  edinfo.leaveDate = edinfo.leaveDate.replace(/-/gi, '');

	  if (!checkDateNoHypen(edinfo.leaveDate)) {
		  jAlert('퇴사일자 날짜 형식이 맞지 않습니다.\r\n다시 입력해주시기 바랍니다.','', function(){
			  edinfo.leaveDate = '';
			  $("#edinfo_leaveDate").val('');
		      $("#edinfo_leaveDate").focus();
		  });      
		  return;
	  }
  } else {
	  edinfo.leaveDate = '0';
  }

  // 주소
  if (addressTextBox == '') {
    if ( addressText == ''  ) {
    	jAlert('주소를 입력해주세요.','', function(){
		      $("#edinfo_zipCode").focus();
		 });      
        return false;
    }
  }
  // 휴대폰번호
  if (edinfo.phoneNum == '') {
	  jAlert('휴대폰번호를 입력해주세요.','', function(){
	      $("#edinfo_phoneNum").focus();
	  });  
	  return false;
  }
  // 내외국인구분
  if (edinfo.내외국인구분 == '') {
	  jAlert('내외국인구분을 입력해주세요.','', function(){
	      $("#edinfo_내외국인구분").focus();
	  });  
	  return false;
  }
  // 거주구분
  if (edinfo.거주구분 == '') {
	  jAlert('거주구분을 입력해주세요.','', function(){
	      $("#edinfo_거주구분").focus();
	 });  
	  return false;
  }
  // 급여
  if (edinfo.급여 == '') {
	  jAlert('급여를 입력해주세요.','', function(){
	      $("#edinfo_급여").focus();
	  });  
	  return false;
  }
  // 외국인 일 때 필수 값
  if (edinfo.내외국인구분 == '2') {
    if (edinfo.국가코드 == '') {
    	jAlert('국적코드를 입력해주세요.','', function(){
    		$("#edinfo_국가코드").focus();
  	  	});  
    	return false;
    } else if (edinfo.외국인단일세율적용 == '') {
    	jAlert('외국인단일세율적용 여부를 입력해주세요.','', function(){
    		$("#edinfo_외국인단일세율적용").focus();
  	  	});  
    	return false;
    }
  }
  // 거주구분 N일 때 필수 값
  if (edinfo.거주구분 == '2') {
    if (edinfo.거주지국코드 == '') {
    	jAlert('거주지국코드를 입력해주세요.','', function(){
    		$("#edinfo_거주지국코드").focus();
  	  	});  
    	return false;
    }
  }
  // 부서표시여부 및 부서 필수 값
  if (useDepartment.근로자부서표시여부코드 == '1' ) {
	  edinfo.부서표시여부 = '1';
	  if ( edinfo.부서ID == '' ) {
		  jAlert('부서명을 입력해주세요.','', function(){
	    		$("#edinfo_부서ID").focus();
	  	  }); 
		  return false;
	  }
  } else if (useDepartment.근로자부서표시여부코드 =='0'  ) {
	  edinfo.부서ID = '';
  } else if (useDepartment.근로자부서표시여부코드 == '2' ) {
	  if ( edinfo.부서표시여부 == '1' ) {
		  if ( edinfo.부서ID == '' ) {
			  jAlert('부서명을 입력해주세요.','', function(){
		    		$("#edinfo_부서ID").focus();
		  	  }); 
			  return false;
		  }
	  }
  }
  // 야간근로 비과세 체크
  if ( edinfo.전년도총급여 > '25000000' ) {
	  edinfo.생산직등야간근로비과세 = '2';
	  if ( edinfo.생산직등야간근로비과세 == '1' ) {
		  jAlert('전년도 총급여액이 2500만원 이하인 근로자만 생산직 등 야간근로비과세를 적용 받을 수 있습니다.','', function(){
	    		$("#edinfo_생산직등야간근로비과세").focus();
	  	  }); 
		  edinfo.생산직등야간근로비과세 = '2';
	  }
  }

  // 감면기간_FROM
  if (edinfo.감면기간_FROM != '' ) {
	  edinfo.감면기간_FROM = edinfo.감면기간_FROM.replace(/-/gi, '');

	  if (!checkDateNoHypen(edinfo.감면기간_FROM)) {
		  jAlert('감면기간_FROM 날짜 형식이 맞지 않습니다.\r\n다시 입력해주시기 바랍니다.','', function(){
			  	edinfo.감면기간_FROM = '';
			  	$("#edinfo_감면기간_FROM").val('');
	    		//$("#edinfo_감면기간_FROM").focus();
	  	  });       
		  return;
	  }

  } else {
	  edinfo.감면기간_FROM = '0';
  }

  // 감면기간_TO
  if (edinfo.감면기간_TO != '' ) {
	  edinfo.감면기간_TO = edinfo.감면기간_TO.replace(/-/gi, '');

	  if (!checkDateNoHypen(edinfo.감면기간_TO)) {
		  jAlert('감면기간_TO 날짜 형식이 맞지 않습니다.\r\n다시 입력해주시기 바랍니다.','', function(){
			  	edinfo.감면기간_TO = '';
			  	$("#edinfo_감면기간_TO").val('');
	    		//$("#edinfo_감면기간_TO").focus();
	  	  });   
		  return;
	  }
  } else {
	  edinfo.감면기간_TO = '0';
  }

  // 나이 계산 후 저장
  var juno = edinfo['개인식별번호'];
  var junoYear = juno.substr(0, 2);
  var junocnt = juno.length;
  var junoCode = juno.substr( 6, 1);
  var age =  0;
  var nowDate =  contractYear;
  if (junoCode == '1' || junoCode == '2' || junoCode == '5' || junoCode == '6' ) {
	  age = 1900 + Number( junoYear );
  } else {
	  age = 2000 + Number( junoYear );
  }
  edinfo['나이'] =  Number(nowDate) -  Number(age);

  // 계약ID 저장
  edinfo.계약ID = contractId;
  edinfo.사용여부 =  edinfo.사용여부 != '' ?  edinfo.사용여부 :  '1';
  edinfo.joinDate = edinfo.joinDate != '' ? moment(edinfo.joinDate, 'YYYY-MM-DD').format('YYYYMMDD') : '';
  edinfo.leaveDate = edinfo.leaveDate != '' && edinfo.leaveDate != '0' ? moment(edinfo.leaveDate, 'YYYY-MM-DD').format('YYYYMMDD') : '0';
  edinfo.감면기간_FROM = edinfo.감면기간_FROM != '' && edinfo.감면기간_FROM != '0' ? moment(edinfo.감면기간_FROM, 'YYYY-MM-DD').format('YYYYMMDD') : '0';
  edinfo.감면기간_TO = edinfo.감면기간_TO != '' && edinfo.감면기간_TO != '0' ? moment(edinfo.감면기간_TO, 'YYYY-MM-DD').format('YYYYMMDD') : '0';
  edinfo.전년도총급여 = ( edinfo.전년도총급여 == '' ||  edinfo.전년도총급여 == null ) ? '0' : edinfo.전년도총급여  ;
  edinfo.급여 = ( edinfo.급여 == '' ||  edinfo.급여 == null ) ? '0' : edinfo.급여  ;
  edinfo.상여 =  ( edinfo.상여 == '' ||  edinfo.상여 == null )  ? '0' : edinfo.상여 ;
  edinfo.인정상여 = ( edinfo.인정상여 == ''  ||  edinfo.인정상여 == null )  ? '0' : edinfo.인정상여 ;
  edinfo.주식매수선택권행사이익 =  ( edinfo.주식매수선택권행사이익  == ''  ||  edinfo.주식매수선택권행사이익 == null )  ?  '0' : edinfo.주식매수선택권행사이익;
  edinfo.우리사주조합인출금 = ( edinfo.우리사주조합인출금  == ''  ||  edinfo.우리사주조합인출금 == null )  ?  '0' : edinfo.우리사주조합인출금 ;
  edinfo.임원퇴직소득금액한도초과액 = ( edinfo.임원퇴직소득금액한도초과액 == ''  ||  edinfo.임원퇴직소득금액한도초과액 == null )  ?  '0' : edinfo.임원퇴직소득금액한도초과액;
  edinfo.소득세 = ( edinfo.소득세 == ''  ||  edinfo.소득세 == null ) ?  '0' : edinfo.소득세 ;
  edinfo.지방소득세 = ( edinfo.지방소득세 == '' || edinfo.지방소득세 == null )  ?  '0' : edinfo.지방소득세 ;
  edinfo.농어촌특별세 = ( edinfo.농어촌특별세 == '' || edinfo.농어촌특별세 == null ) ? '0' : edinfo.농어촌특별세 ;
  edinfo.건강보험료 = ( edinfo.건강보험료 == '' || edinfo.건강보험료 == null ) ?  '0' : edinfo.건강보험료 ;
  edinfo.장기요양보험료 =  ( edinfo.장기요양보험료 == ''  ||  edinfo.장기요양보험료 == null )  ? '0' : edinfo.장기요양보험료 ;
  edinfo.국민연금보험료 =  ( edinfo.국민연금보험료 == ''  ||  edinfo.국민연금보험료 == null )  ? '0' : edinfo.국민연금보험료 ;
  edinfo.고용보험료 =  ( edinfo.고용보험료 == ''  ||  edinfo.고용보험료 == null )  ? '0' : edinfo.고용보험료 ;
  edinfo.공무원연금 =  ( edinfo.공무원연금 == ''  ||  edinfo.공무원연금 == null )  ? '0' : edinfo.공무원연금 ;
  edinfo.군인연금 =  ( edinfo.군인연금 == ''  ||  edinfo.군인연금 == null )  ? '0' : edinfo.군인연금 ;
  edinfo.사립학교교직원연금 =  ( edinfo.사립학교교직원연금 == ''  ||  edinfo.사립학교교직원연금 == null )  ? '0' : edinfo.사립학교교직원연금 ;
  edinfo.별정우체국연금 =  ( edinfo.별정우체국연금 == ''  ||  edinfo.별정우체국연금 == null )  ? '0' : edinfo.별정우체국연금 ;
  edinfo.m01 = ( edinfo.m01 == ''  ||  edinfo.m01 == null )  ? '0' : edinfo.m01 ;
  edinfo.m02 = ( edinfo.m02 == ''  ||  edinfo.m02 == null )  ? '0' : edinfo.m02 ;
  edinfo.m03 = ( edinfo.m03 == ''  ||  edinfo.m03 == null )  ? '0' : edinfo.m03 ;
  edinfo.o01 = ( edinfo.o01 == ''  ||  edinfo.o01 == null )  ? '0' : edinfo.o01 ;
  edinfo.q01 = ( edinfo.q01 == ''  ||  edinfo.q01 == null )  ? '0' : edinfo.q01 ;
  edinfo.h08 = ( edinfo.h08 == ''  ||  edinfo.h08 == null )  ? '0' : edinfo.h08 ;
  edinfo.h09 = ( edinfo.h09 == ''  ||  edinfo.h09 == null )  ? '0' : edinfo.h09 ;
  edinfo.h10 = ( edinfo.h10 == ''  ||  edinfo.h10 == null )  ? '0' : edinfo.h10 ;
  edinfo.g01 = ( edinfo.g01 == ''  ||  edinfo.g01 == null )  ? '0' : edinfo.g01 ;
  edinfo.h11 = ( edinfo.h11 == ''  ||  edinfo.h11 == null )  ? '0' : edinfo.h11 ;
  edinfo.h12 = ( edinfo.h12 == ''  ||  edinfo.h12 == null )  ? '0' : edinfo.h12 ;
  edinfo.h13 = ( edinfo.h13 == ''  ||  edinfo.h13 == null )  ? '0' : edinfo.h13 ;
  edinfo.h01 = ( edinfo.h01 == ''  ||  edinfo.h01 == null )  ? '0' : edinfo.h01 ;
  edinfo.k01 = ( edinfo.k01 == ''  ||  edinfo.k01 == null )  ? '0' : edinfo.k01 ;
  edinfo.s01 = ( edinfo.s01 == ''  ||  edinfo.s01 == null )  ? '0' : edinfo.s01 ;
  edinfo.t01 = ( edinfo.t01 == ''  ||  edinfo.t01 == null )  ? '0' : edinfo.t01 ;
  edinfo.y02 = ( edinfo.y02 == ''  ||  edinfo.y02 == null )  ? '0' : edinfo.y02 ;
  edinfo.y03 = ( edinfo.y03 == ''  ||  edinfo.y03 == null )  ? '0' : edinfo.y03 ;
  edinfo.y04 = ( edinfo.y04 == ''  ||  edinfo.y04 == null )  ? '0' : edinfo.y04 ;
  edinfo.h05 = ( edinfo.h05 == ''  ||  edinfo.h05 == null )  ? '0' : edinfo.h05 ;
  edinfo.i01 = ( edinfo.i01 == ''  ||  edinfo.i01 == null )  ? '0' : edinfo.i01 ;
  edinfo.r10 = ( edinfo.r10 == ''  ||  edinfo.r10 == null )  ? '0' : edinfo.r10 ;
  edinfo.h14 = ( edinfo.h14 == ''  ||  edinfo.h14 == null )  ? '0' : edinfo.h14 ;
  edinfo.h15 = ( edinfo.h15 == ''  ||  edinfo.h15 == null )  ? '0' : edinfo.h15 ;
  edinfo.t10 = ( edinfo.t10 == ''  ||  edinfo.t10 == null )  ? '0' : edinfo.t10 ;
  edinfo.t11 = ( edinfo.t11 == ''  ||  edinfo.t11 == null )  ? '0' : edinfo.t11 ;
  edinfo.t12 = ( edinfo.t12 == ''  ||  edinfo.t12 == null )  ? '0' : edinfo.t12 ;
  edinfo.t20 = ( edinfo.t20 == ''  ||  edinfo.t20 == null )  ? '0' : edinfo.t20 ;
  edinfo.h16 = ( edinfo.h16 == ''  ||  edinfo.h16 == null )  ? '0' : edinfo.h16 ;
  edinfo.r11 = ( edinfo.r11 == ''  ||  edinfo.r11 == null )  ? '0' : edinfo.r11 ;
  edinfo.h06 = ( edinfo.h06 == ''  ||  edinfo.h06 == null )  ? '0' : edinfo.h06 ;
  edinfo.h07 = ( edinfo.h07 == ''  ||  edinfo.h07 == null )  ? '0' : edinfo.h07 ;
  edinfo.y22 = ( edinfo.y22 == ''  ||  edinfo.y22 == null )  ? '0' : edinfo.y22 ;
  edinfo.h06 = ( edinfo.h06 == ''  ||  edinfo.h06 == null )  ? '0' : edinfo.h06 ;
  edinfo.h07 = ( edinfo.h07 == ''  ||  edinfo.h07 == null )  ? '0' : edinfo.h07 ;
  edinfo.y22 = ( edinfo.y22 == ''  ||  edinfo.y22 == null )  ? '0' : edinfo.y22 ;

  edinfo.직무발명보상금 = ( edinfo.직무발명보상금 == ''  ||  edinfo.직무발명보상금 == null )  ?  '0' : edinfo.직무발명보상금;
  edinfo.t13 = ( edinfo.t13 == ''  ||  edinfo.t13 == null )  ? '0' : edinfo.t13 ;
  edinfo.h17 = ( edinfo.h17 == ''  ||  edinfo.h17 == null )  ? '0' : edinfo.h17 ;
  edinfo.u01 = ( edinfo.u01 == ''  ||  edinfo.u01 == null )  ? '0' : edinfo.u01 ;

  edinfo.납부특례세액_소득세 =  ( edinfo.납부특례세액_소득세 == ''  ||  edinfo.납부특례세액_소득세 == null )  ? '0' : edinfo.납부특례세액_소득세 ;
  edinfo.납부특례세액_지방소득세 =  ( edinfo.납부특례세액_지방소득세 == ''  ||  edinfo.납부특례세액_지방소득세 == null )  ? '0' : edinfo.납부특례세액_지방소득세 ;
  edinfo.납부특례세액_농어촌특별세 = ( edinfo.납부특례세액_농어촌특별세 == ''  ||  edinfo.납부특례세액_농어촌특별세 == null )  ? '0' : edinfo.납부특례세액_농어촌특별세 ;

  // 비과세한도초과액 추가 2019-02-18
  edinfo.비과세한도초과액 = ( edinfo.비과세한도초과액 == ''  ||  edinfo.비과세한도초과액 == null )  ? '0' : edinfo.비과세한도초과액;

  // 원천징수영수증 관리번호
  edinfo.원천징수영수증_관리번호 =  edinfo.원천징수영수증_관리번호 != '' ?  edinfo.원천징수영수증_관리번호 :  '0';

  
  //edinfo dbmode 변경 및 입력 & 수정
  if ( edinfo['일련번호'] == '' ) {
	  edinfo['dbMode'] = 'C';	  	
  }else{
	  edinfo['dbMode'] = 'U';
  }
  
  
  // form data로 변경
  $.each(edinfo, function(k, v) {
	  params.append(k, v);
  });
  
  /*for (var key in data) {
	    document.writeln(key + ": " + data[key]);
	    console.log(key + ": " + data[key]); 

	}*/ 

	 
  

  // 근로자상세정보저장
  var url = rootPath + 'febworker/saveYP000.do';		
  //blockUi 호출
  fncOnBlockUI();
  
  $.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		//async:false,
		//contentType: "application/json; charset=UTF-8",
		contentType: false,
		processData: false,
		data: params,
		//data: JSON.stringify(edinfo),
		success:function(res) {		
			
			if (res != false) {
				//getList(userId);					
				//jAlert('데이터 저장에 성공하였습니다.','');
				logI('근로자 데이터 저장에 성공하였습니다.');
				//재집계 실행
				yearTaxTotalIncome(febYear, contractId, userId, workPeriod);
				
				if(totalIncomeProc){
					jAlert('데이터 저장에 성공하였습니다.','', function(){
						$.unblockUI();
						getList(userId);
					});
				}else{
					jAlert('데이터 저장에 실패하였습니다.','', function(){
						$.unblockUI();
						return false;
					});
				}
		    } else {
		    	$.unblockUI();
		    	jAlert('데이터 저장에 실패하였습니다.','');
		    }

			//$.unblockUI();
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
	
  edinfo.joinDate = edinfo.joinDate != '' ? moment(edinfo.joinDate, 'YYYYMMDD').format('YYYY-MM-DD') : '';
  edinfo.leaveDate = edinfo.leaveDate != '' ? moment(edinfo.leaveDate, 'YYYYMMDD').format('YYYY-MM-DD') : '';
  edinfo.감면기간_FROM = edinfo.감면기간_FROM != '' ? moment(edinfo.감면기간_FROM, 'YYYYMMDD').format('YYYY-MM-DD') : '';
  edinfo.감면기간_TO = edinfo.감면기간_TO != '' ? moment(edinfo.감면기간_TO, 'YYYYMMDD').format('YYYY-MM-DD') : '';

}


/**
 * 신고 대상 금액  
 */
function taxAggregation(){
	 var 근무시작일 = edinfo['joinDate'];
	 var 근무종료일 = edinfo['leaveDate'];
	
	 if (isNotNull(근무시작일)) {

		 if(isNull(근무종료일)){
			 //상반기
			 if(workPeriod == '1'){
				 근무종료일 = moment(febYear + '0630', 'YYYYMMDD').format('YYYY-MM-DD');
			 }
			 //하반기
			 else if(workPeriod == '2'){
				 근무종료일 = moment(febYear + '1231', 'YYYYMMDD').format('YYYY-MM-DD');
			 }
		 }
		 
		 var endtDate = moment(근무종료일, 'YYYYMMDD').format('YYYY');
		 if (Number(endtDate) > Number(febYear)) {
			 //상반기
			 if(workPeriod == '1'){
				 근무종료일 = moment(febYear + '0630', 'YYYYMMDD').format('YYYY-MM-DD');
			 }
			 //하반기
			 else if(workPeriod == '2'){
				 근무종료일 = moment(febYear + '1231', 'YYYYMMDD').format('YYYY-MM-DD');
			 }
		 }
		 var startDate = moment(근무시작일, 'YYYYMMDD').format('YYYY');
		 if (Number(startDate) < Number(febYear) ) {			 
			 //상반기
			 if(workPeriod == '1'){
				 근무시작일 = moment(febYear + '0101', 'YYYYMMDD').format('YYYY-MM-DD');
			 }
			 //하반기
			 else if(workPeriod == '2'){
				 근무시작일 = moment(febYear + '0701', 'YYYYMMDD').format('YYYY-MM-DD');
			 }
         }
		 
		 
		 logI("# Aggregation - 근무시작일 : " + 근무시작일 );
		 logI("# Aggregation - 근무종료일 : " + 근무종료일 );
		 
		 //총 근무 개월 수 계산
		 var 근무시작월 = moment(근무시작일, 'YYYYMMDD').format('MM');
         var 근무종료월 = moment(근무종료일, 'YYYYMMDD').format('MM');
         var 근무개월 = Number(근무종료월) - Number(근무시작월) + 1;
         
         logI("# Aggregation - 근무개월 : " + 근무개월 );
         
         if ( Number(edinfo['m01']) > (1000000 * 근무개월) ) {
     		edinfo_aggre['m01'] = (1000000 * 근무개월);
     	 }else{
     		edinfo_aggre['m01'] = edinfo['m01'];
     	 }

         if (Number(edinfo['m02']) > (3000000 * 근무개월) ) {
        	 edinfo_aggre['m02'] = (3000000 * 근무개월);
         }else{
        	 edinfo_aggre['m02'] = edinfo['m02']; 
         }
         
         edinfo_aggre['m03'] = edinfo['m03'];
         
         if (Number(edinfo['o01']) > 2400000) {
        	 edinfo_aggre['o01'] = '2400000';
         }else{
        	 edinfo_aggre['o01'] = edinfo['o01']; 
         }
         
         if (Number(edinfo['q01']) > (100000 * 근무개월) ) {
        	 edinfo_aggre['q01'] = (100000 * 근무개월);
         }else{
        	 edinfo_aggre['q01'] = edinfo['q01']; 
         }
         
         if (Number(edinfo['h08']) > (200000 * 근무개월) ) {
        	 edinfo_aggre['h08'] = (200000 * 근무개월);
         }else{
        	 edinfo_aggre['h08'] = edinfo['h08']; 
         }
         
         if (Number(edinfo['h09']) > (200000 * 근무개월) ) {
        	 edinfo_aggre['h09'] = (200000 * 근무개월);
         }else{
        	 edinfo_aggre['h09'] = edinfo['h09']; 
         }
         
         if (Number(edinfo['h10']) > (200000 * 근무개월) ) {
        	 edinfo_aggre['h10'] = (200000 * 근무개월);
         }else{
        	 edinfo_aggre['h10'] = edinfo['h10']; 
         }
         
         edinfo_aggre['g01'] = edinfo['g01'];
         
         if (Number(edinfo['h11']) > (200000 * 근무개월) ) {
        	 edinfo_aggre['h11'] = (200000 * 근무개월);
         }else{
        	 edinfo_aggre['h11'] = edinfo['h11']; 
         }
         
         if (Number(edinfo['h12']) > (200000 * 근무개월) ) {
        	 edinfo_aggre['h12'] = (200000 * 근무개월);
         }else{
        	 edinfo_aggre['h12'] = edinfo['h12']; 
         }
         
         edinfo_aggre['h13'] = edinfo['h13'];
         edinfo_aggre['h01'] = edinfo['h01'];
         edinfo_aggre['k01'] = edinfo['k01'];
         
         if (Number(edinfo['s01']) > (30000000) ) {
        	 edinfo_aggre['s01'] = (30000000);
         }else{
        	 edinfo_aggre['s01'] = edinfo['s01']; 
         }
         
         //edinfo_aggre['y04'] = edinfo['y04'];
         edinfo_aggre['h05'] = edinfo['h05'];
         edinfo_aggre['i01'] = edinfo['i01'];         
         edinfo_aggre['r10'] = edinfo['r10'];
         edinfo_aggre['h14'] = edinfo['h14'];
         edinfo_aggre['h15'] = edinfo['h15'];                
         //edinfo_aggre['t10'] = edinfo['t10'];
         //edinfo_aggre['t20'] = edinfo['t20'];

         if (Number(edinfo['h16']) > (200000 * 근무개월) ) {
        	 edinfo_aggre['h16'] = (200000 * 근무개월);
         }else{
        	 edinfo_aggre['h16'] = edinfo['h16']; 
         }
         
         if (Number(edinfo['r11']) > (3000000) ) {
        	 edinfo_aggre['r11'] = (3000000);
         }else{
        	 edinfo_aggre['r11'] = edinfo['r11']; 
         }
         
         if (Number(edinfo['h06']) > (200000 * 근무개월) ) {
        	 edinfo_aggre['h06'] = (200000 * 근무개월);
         }else{
        	 edinfo_aggre['h06'] = edinfo['h06']; 
         }
         
         if (Number(edinfo['h07']) > (200000 * 근무개월) ) {
        	 edinfo_aggre['h07'] = (200000 * 근무개월);
         }else{
        	 edinfo_aggre['h07'] = edinfo['h07']; 
         }
         
         // 2018 분기
         if (febYear == '2018') {        	 
        	 if (Number(edinfo['u01']) > (20000000) ) {
            	 edinfo_aggre['u01'] = (20000000);
             }else{
            	 edinfo_aggre['u01'] = edinfo['u01']; 
             }
         }
         
         

	        /*/////////// 3. 비과세(감면) 대상 급여 */
	        if(taxExList['m01'] == '1'){		        	
	        	$("#edinfo_m01").val(addCommas(edinfo.m01));
	        	$("#edinfo_m01_aggre").val(addCommas(edinfo_aggre['m01']));	
	        	
	        	$("#taxExList_m01").show();
	        	
	        	if(edinfo['국외근로제공여부'] != '1'){
	        		$("#taxExList_m01").addClass('colorY');
	        	}
	        	if(edinfo['m01'] >= month100 && edinfo['국외근로제공여부'] == '1'){
	        		$("#edinfo_m01").addClass('overload');
	        	}  else {
	        		$("#edinfo_m01").removeClass('overload');
	        	}
	        }
	        if(taxExList['m02'] == '1'){
	        	$("#edinfo_m02").val(addCommas(edinfo.m02));
	        	$("#edinfo_m02_aggre").val(addCommas(edinfo_aggre['m02']));
	        	
	        	$("#taxExList_m02").show();
	        	
	        	if(edinfo['국외근로제공여부'] != '2'){
	        		$("#taxExList_m02").addClass('colorY');
	        	}
	        	if(edinfo['m02'] >= month300 && edinfo['국외근로제공여부'] == '2'){
	        		$("#edinfo_m02").addClass('overload');
	        	} else {
	        		$("#edinfo_m02").removeClass('overload');
	        	}
	        }
	        if(taxExList['m03'] == '1'){
	        	$("#edinfo_m03").val(addCommas(edinfo.m03));
	        	$("#edinfo_m03_aggre").val(addCommas(edinfo_aggre['m03']));
	        	
	        	$("#taxExList_m03").show();
	        	
	        	if(isOver){
	        		$("#edinfo_m03").addClass('overload');
	        	} else {
	        		$("#edinfo_m03").removeClass('overload');
	        	}
	        }
	        if(taxExList['o01'] == '1'){
	        	$("#edinfo_o01").val(addCommas(edinfo.o01));
	        	$("#edinfo_o01_aggre").val(addCommas(edinfo_aggre['o01']));
	        	
	        	$("#taxExList_o01").show();
	        	
	        	if(edinfo['생산직등야간근로비과세'] == '2'){
	        		$("#taxExList_o01").addClass('colorY');
	        	}		        	
	        	if(edinfo['o01'] >= year240 && edinfo['생산직등야간근로비과세'] == '1'){
	        		$("#edinfo_o01").addClass('overload');
	        	} else {
	        		$("#edinfo_o01").removeClass('overload');
	        	}
	        }
	        if(taxExList['q01'] == '1'){
	        	$("#edinfo_q01").val(addCommas(edinfo.q01));
	        	$("#edinfo_q01_aggre").val(addCommas(edinfo_aggre['q01']));
	        	
	        	$("#taxExList_q01").show();
	        	
	        	if(edinfo.q01 >= month10){
	        		$("#edinfo_q01").addClass('overload');
	        	} else {
	        		$("#edinfo_q01").removeClass('overload');
	        	}
	        }
	        if(taxExList['h08'] == '1'){
	        	$("#edinfo_h08").val(addCommas(edinfo.h08));
	        	$("#edinfo_h08_aggre").val(addCommas(edinfo_aggre['h08']));
	        	
	        	$("#taxExList_h08").show();
	        	
	        	if(edinfo.h08 >= month20){
	        		$("#edinfo_h08").addClass('overload');
	        	} else {
	        		$("#edinfo_h08").removeClass('overload');
	        	}
	        }
	        if(taxExList['h09'] == '1'){
	        	$("#edinfo_h09").val(addCommas(edinfo.h09));
	        	$("#edinfo_h09_aggre").val(addCommas(edinfo_aggre['h09']));
	        	
	        	$("#taxExList_h09").show();
	        	
	        	if(edinfo.h09 >= month20){
	        		$("#edinfo_h09").addClass('overload');
	        	} else {
	        		$("#edinfo_h09").removeClass('overload');
	        	}
	        }
	        if(taxExList['h10'] == '1'){
	        	$("#edinfo_h10").val(addCommas(edinfo.h10));
	        	$("#edinfo_h10_aggre").val(addCommas(edinfo_aggre['h10']));
	        	
	        	$("#taxExList_h10").show();
	        	
	        	if(edinfo.h10 >= month20){
	        		$("#edinfo_h10").addClass('overload');
	        	} else {
	        		$("#edinfo_h10").removeClass('overload');
	        	}
	        }
	        if(taxExList['g01'] == '1'){
	        	$("#edinfo_g01").val(addCommas(edinfo.g01));
	        	$("#edinfo_g01_aggre").val(addCommas(edinfo_aggre['g01']));
	        	
	        	$("#taxExList_g01").show();
	        	
	        	if(isOver){
	        		$("#edinfo_g01").addClass('overload');
	        	} else {
	        		$("#edinfo_g01").removeClass('overload');
	        	}
	        }
	        if(taxExList['h11'] == '1'){
	        	$("#edinfo_h11").val(addCommas(edinfo.h11));
	        	$("#edinfo_h11_aggre").val(addCommas(edinfo_aggre['h11']));
	        	
	        	$("#taxExList_h11").show();
	        	
	        	if(edinfo.h11 >= month20){
	        		$("#edinfo_h11").addClass('overload');
	        	} else {
	        		$("#edinfo_h11").removeClass('overload');
	        	}
	        }
	        if(taxExList['h12'] == '1'){
	        	$("#edinfo_h12").val(addCommas(edinfo.h12));
	        	$("#edinfo_h12_aggre").val(addCommas(edinfo_aggre['h12']));
	        	
	        	$("#taxExList_h12").show();
	        	
	        	if(edinfo.h12 >= month20){
	        		$("#edinfo_h12").addClass('overload');
	        	} else {
	        		$("#edinfo_h12").removeClass('overload');
	        	}
	        }
	        if(taxExList['h13'] == '1'){
	        	$("#edinfo_h13").val(addCommas(edinfo.h13));
	        	$("#edinfo_h13_aggre").val(addCommas(edinfo_aggre['h13']));
	        	
	        	$("#taxExList_h13").show();
	        	
	        	if(isOver){
	        		$("#edinfo_h13").addClass('overload');
	        	} else {
	        		$("#edinfo_h13").removeClass('overload');
	        	}
	        }
	        if(taxExList['h01'] == '1'){
	        	$("#edinfo_h01").val(addCommas(edinfo.h01));
	        	$("#edinfo_h01_aggre").val(addCommas(edinfo_aggre['h01']));
	        	
	        	$("#taxExList_h01").show();
	        	
	        	if(isOver){
	        		$("#edinfo_h01").addClass('overload');
	        	} else {
	        		$("#edinfo_h01").removeClass('overload');
	        	}
	        }
	        if(taxExList['k01'] == '1'){
	        	$("#edinfo_k01").val(addCommas(edinfo.k01));
	        	$("#edinfo_k01_aggre").val(addCommas(edinfo_aggre['k01']));
	        	
	        	$("#taxExList_k01").show();
	        	
	        	if(isOver){
	        		$("#edinfo_k01").addClass('overload');
	        	} else {
	        		$("#edinfo_k01").removeClass('overload');
	        	}
	        }
	        if(taxExList['s01'] == '1'){
	        	$("#edinfo_s01").val(addCommas(edinfo.s01));
	        	$("#edinfo_s01_aggre").val(addCommas(edinfo_aggre['s01']));
	        	
	        	$("#taxExList_s01").show();
	        	
	        	if(edinfo.s01 >= year3000){
	        		$("#edinfo_s01").addClass('overload');
	        	} else {
	        		$("#edinfo_s01").removeClass('overload');
	        	}
	        }
	        if(taxExList['t01'] == '1'){
	        	$("#edinfo_t01").val(addCommas(edinfo.t01));
	        	$("#edinfo_t01_aggre").val(addCommas(edinfo_aggre['t01']));
	        	
	        	$("#taxExList_t01").show();
	        	
	        	if(isOver){
	        		$("#edinfo_t01").addClass('overload');
	        	} else {
	        		$("#edinfo_t01").removeClass('overload');
	        	}
	        }
	        if(taxExList['y02'] == '1'){
	        	$("#edinfo_y02").val(addCommas(edinfo.y02));
	        	$("#edinfo_y02_aggre").val(addCommas(edinfo_aggre['y02']));
	        			        	
	        	$("#taxExList_y02").show();
	        	
	        	if(isOver){
	        		$("#edinfo_y02").addClass('overload');
	        	} else {
	        		$("#edinfo_y02").removeClass('overload');
	        	}
	        }
     	if(taxExList['y03'] == '1'){
	        	$("#edinfo_2018_y03").val(addCommas(edinfo.y03));
	        	$("#edinfo_2018_y03_aggre").val(addCommas(edinfo_aggre['y03']));
	        	
	        	$("#taxExList_2018_y03").show();

	        	if(edinfo.y03 >= month20){
	        		$("#edinfo_2018_y03").addClass('overload');
	        	} else {
	        		$("#edinfo_2018_y03").removeClass('overload');
	        	}
	        }
	        if(taxExList['y04'] == '1'){
	        	$("#edinfo_y04").val(addCommas(edinfo.y04));
	        	$("#edinfo_y04_aggre").val(addCommas(edinfo_aggre['y04']));
	        	
	        	$("#taxExList_y04").show();

	        	if(isOver){
	        		$("#edinfo_y04").addClass('overload');
	        	} else {
	        		$("#edinfo_y04").removeClass('overload');
	        	}
	        }
	        if(taxExList['h05'] == '1'){
	        	$("#edinfo_h05").val(addCommas(edinfo.h05));
	        	$("#edinfo_h05_aggre").val(addCommas(edinfo_aggre['h05']));
	        	
	        	$("#taxExList_h05").show();

	        	if(isOver){
	        		$("#edinfo_h05").addClass('overload');
	        	} else {
	        		$("#edinfo_h05").removeClass('overload');
	        	}
	        }
	        if(taxExList['i01'] == '1'){
	        	$("#edinfo_i01").val(addCommas(edinfo.i01));
	        	$("#edinfo_i01_aggre").val(addCommas(edinfo_aggre['i01']));
	        	
	        	$("#taxExList_i01").show();

	        	if(isOver){
	        		$("#edinfo_i01").addClass('overload');
	        	} else {
	        		$("#edinfo_i01").removeClass('overload');
	        	}
	        }
	        if(taxExList['r10'] == '1'){
	        	$("#edinfo_r10").val(addCommas(edinfo.r10));
	        	$("#edinfo_r10_aggre").val(addCommas(edinfo_aggre['r10']));
	        	
	        	$("#taxExList_r10").show();

	        	if(isOver){
	        		$("#edinfo_r10").addClass('overload');
	        	} else {
	        		$("#edinfo_r10").removeClass('overload');
	        	}
	        }
	        if(taxExList['h14'] == '1'){
	        	$("#edinfo_h14").val(addCommas(edinfo.h14));
	        	$("#edinfo_h14_aggre").val(addCommas(edinfo_aggre['h14']));
	        	
	        	$("#taxExList_h14").show();

	        	if(isOver){
	        		$("#edinfo_h14").addClass('overload');
	        	} else {
	        		$("#edinfo_h14").removeClass('overload');
	        	}
	        }
	        if(taxExList['h15'] == '1'){
	        	$("#edinfo_h15").val(addCommas(edinfo.h15));
	        	$("#edinfo_h15_aggre").val(addCommas(edinfo_aggre['h15']));
	        	
	        	$("#taxExList_h15").show();

	        	if(isOver){
	        		$("#edinfo_h15").addClass('overload');
	        	} else {
	        		$("#edinfo_h15").removeClass('overload');
	        	}
	        }
	        if(taxExList['t10'] == '1'){
	        	$("#edinfo_t10").val(addCommas(edinfo.t10));
	        	$("#edinfo_t10_aggre").val(addCommas(edinfo_aggre['t10']));
	        	
	        	$("#taxExList_t10").show();

	        	if(isOver){
	        		$("#edinfo_t10").addClass('overload');
	        	} else {
	        		$("#edinfo_t10").removeClass('overload');
	        	}
	        }
	        if(taxExList['t11'] == '1'){
	        	$("#edinfo_t11").val(addCommas(edinfo.t11));
	        	$("#edinfo_t11_aggre").val(addCommas(edinfo_aggre['t11']));
	        	
	        	$("#taxExList_t11").show();

	        	if(isOver){
	        		$("#edinfo_t11").addClass('overload');
	        	} else {
	        		$("#edinfo_t11").removeClass('overload');
	        	}
	        }
	        if(taxExList['t12'] == '1'){
	        	$("#edinfo_t12").val(addCommas(edinfo.t12));
	        	$("#edinfo_t12_aggre").val(addCommas(edinfo_aggre['t12']));
	        	
	        	$("#taxExList_t12").show();

	        	if(isOver){
	        		$("#edinfo_t12").addClass('overload');
	        	} else {
	        		$("#edinfo_t12").removeClass('overload');
	        	}
	        }
	        if(febYear == '2018'){
		        if(taxExList['t13'] == '1'){
		        	$("#edinfo_t13").val(addCommas(edinfo.t13));
		        	$("#edinfo_t13_aggre").val(addCommas(edinfo_aggre['t13']));
		        	
		        	$("#taxExList_t13").show();

		        	if(isOver){
		        		$("#edinfo_t13").addClass('overload');
		        	} else {
		        		$("#edinfo_t13").removeClass('overload');
		        	}
		        }
	        }
	        if(taxExList['t20'] == '1'){
	        	$("#edinfo_t20").val(addCommas(edinfo.t20));
	        	$("#edinfo_t20_aggre").val(addCommas(edinfo_aggre['t20']));
	        	
	        	$("#taxExList_t20").show();

	        	if(isOver){
	        		$("#edinfo_t20").addClass('overload');
	        	} else {
	        		$("#edinfo_t20").removeClass('overload');
	        	}
	        }
	        if(taxExList['h16'] == '1'){
	        	$("#edinfo_h16").val(addCommas(edinfo.h16));
	        	$("#edinfo_h16_aggre").val(addCommas(edinfo_aggre['h16']));
	        	
	        	$("#taxExList_h16").show();

	        	if(edinfo.h16 >= month20){
	        		$("#edinfo_h16").addClass('overload');
	        	} else {
	        		$("#edinfo_h16").removeClass('overload');
	        	}
	        }
	        if(febYear == '2018'){
		        if(taxExList['h17'] == '1'){
		        	$("#edinfo_h17").val(addCommas(edinfo.h17));
		        	$("#edinfo_h17_aggre").val(addCommas(edinfo_aggre['h17']));
		        	
		        	$("#taxExList_h17").show();

		        	if(isOver){
		        		$("#edinfo_h17").addClass('overload');
		        	} else {
		        		$("#edinfo_h17").removeClass('overload');
		        	}
		        }
		        if(taxExList['u01'] == '1'){
		        	$("#edinfo_u01").val(addCommas(edinfo.u01));
		        	$("#edinfo_u01_aggre").val(addCommas(edinfo_aggre['u01']));
		        	
		        	$("#taxExList_u01").show();

		        	if(isOver){
		        		$("#edinfo_u01").addClass('overload');
		        	} else {
		        		$("#edinfo_u01").removeClass('overload');
		        	}
		        }
	        }
	        if(taxExList['r11'] == '1'){
	        	$("#edinfo_r11").val(addCommas(edinfo.r11));
	        	$("#edinfo_r11_aggre").val(addCommas(edinfo_aggre['r11']));
	        	
	        	$("#taxExList_r11").show();

	        	if(edinfo.r11 >= year300){
	        		$("#edinfo_r11").addClass('overload');
	        	} else {
	        		$("#edinfo_r11").removeClass('overload');
	        	}
	        }
	        if(taxExList['h06'] == '1'){
	        	$("#edinfo_h06").val(addCommas(edinfo.h06));
	        	$("#edinfo_h06_aggre").val(addCommas(edinfo_aggre['h06']));
	        	
	        	$("#taxExList_h06").show();

	        	if(edinfo.h06 >= month20){
	        		$("#edinfo_h06").addClass('overload');
	        	} else {
	        		$("#edinfo_h06").removeClass('overload');
	        	}
	        }
	        if(taxExList['h07'] == '1'){
	        	$("#edinfo_h07").val(addCommas(edinfo.h07));
	        	$("#edinfo_h07_aggre").val(addCommas(edinfo_aggre['h07']));
	        	
	        	$("#taxExList_h07").show();

	        	if(edinfo.h07 >= month20){
	        		$("#edinfo_h07").addClass('overload');
	        	} else {
	        		$("#edinfo_h07").removeClass('overload');
	        	}
	        }
	        if(taxExList['y22'] == '1'){
	        	$("#edinfo_y22").val(addCommas(edinfo.y22));
	        	$("#edinfo_y22_aggre").val(addCommas(edinfo_aggre['y22']));
	        	
	        	$("#taxExList_y22").show();

	        	if(isOver){
	        		$("#edinfo_y22").addClass('overload');
	        	} else {
	        		$("#edinfo_y22").removeClass('overload');
	        	}
	        }
	        /* 3. 비과세(감면) 대상 급여 //////////// */
	        
	        
     }

}


function setEdinfoData(){
	edinfo.사업장ID = $("#edinfo_사업장ID").val();
	edinfo.근무시기 = workPeriod;
	edinfo.empNo = $("#edinfo_empNo").val();
	edinfo.empName = $("#edinfo_empName").val();
	edinfo.개인식별번호 = $("#edinfo_개인식별번호").val();
	edinfo.세대주여부 = $("#edinfo_세대주여부").val();
	edinfo.joinDate = $("#edinfo_joinDate").val();
	edinfo.leaveDate = $("#edinfo_leaveDate").val();
	edinfo.부서ID = $("#edinfo_부서ID").val();
	edinfo.부서표시여부 = $("#edinfo_부서표시여부").val();
	edinfo.positionName = $("#edinfo_positionName").val();
	edinfo.내외국인구분 = $("#edinfo_내외국인구분").val();
	edinfo.국가코드 = $("#edinfo_국가코드").val();
	edinfo.외국인단일세율적용 = $("#edinfo_외국인단일세율적용").val();
	edinfo.거주구분 = $("#edinfo_거주구분").val();
	edinfo.거주지국코드 = $("#edinfo_거주지국코드").val();
	edinfo.외국법인파견근로자 = $("#edinfo_외국법인파견근로자").val();
	edinfo.중소기업취업감면여부 = $("#edinfo_중소기업취업감면여부").val();
	edinfo.감면기간_FROM = $("#edinfo_감면기간_FROM").val();
	edinfo.감면기간_TO = $("#edinfo_감면기간_TO").val();
	edinfo.감면율 = $("#edinfo_감면율").val();
	edinfo.국외근로제공여부 = $("#edinfo_국외근로제공여부").val();
	edinfo.생산직등야간근로비과세 = $("#edinfo_생산직등야간근로비과세").val();
	edinfo.소득세적용률 = $("#edinfo_소득세적용률").val();
	edinfo.연말정산분납여부 = $("#edinfo_연말정산분납여부").val();
	edinfo.phoneNum = $("#edinfo_phoneNum").val();
	edinfo.사내내선번호 = $("#edinfo_사내내선번호").val();
	edinfo.EMail = $("#edinfo_EMail").val();
	edinfo.원천징수영수증_관리번호 = $("#edinfo_원천징수영수증_관리번호").val();
	edinfo.zipCode = $("#edinfo_zipCode").val();
	edinfo.address1 = $("#edinfo_address1").val();
	edinfo.address2 = $("#edinfo_address2").val();
	edinfo.급여 = removeCommas($("#edinfo_급여").val());
	edinfo.상여 = removeCommas($("#edinfo_상여").val());
	edinfo.인정상여 = removeCommas($("#edinfo_인정상여").val());
	edinfo.주식매수선택권행사이익 = removeCommas($("#edinfo_주식매수선택권행사이익").val());
	edinfo.우리사주조합인출금 = removeCommas($("#edinfo_우리사주조합인출금").val());
	edinfo.임원퇴직소득금액한도초과액 = removeCommas($("#edinfo_임원퇴직소득금액한도초과액").val());
	edinfo.직무발명보상금 = removeCommas($("#edinfo_직무발명보상금").val());
	edinfo.비과세한도초과액 = removeCommas($("#edinfo_비과세한도초과액").val());
	edinfo.소득세 = removeCommas($("#edinfo_소득세").val());
	edinfo.지방소득세 = removeCommas($("#edinfo_지방소득세").val());
	edinfo.국민연금보험료 = removeCommas($("#edinfo_국민연금보험료").val());
	edinfo.건강보험료 = removeCommas($("#edinfo_건강보험료").val());
	edinfo.장기요양보험료 = removeCommas($("#edinfo_장기요양보험료").val());
	edinfo.고용보험료 = removeCommas($("#edinfo_고용보험료").val());
	edinfo.공무원연금 = removeCommas($("#edinfo_공무원연금").val());
	edinfo.군인연금 = removeCommas($("#edinfo_군인연금").val());
	edinfo.사립학교교직원연금 = removeCommas($("#edinfo_사립학교교직원연금").val());
	edinfo.별정우체국연금 = removeCommas($("#edinfo_별정우체국연금").val());
	edinfo.납부특례세액_소득세 = removeCommas($("#edinfo_납부특례세액_소득세").val());
	edinfo.납부특례세액_지방소득세 = removeCommas($("#edinfo_납부특례세액_지방소득세").val());
	edinfo.납부특례세액_농어촌특별세 = removeCommas($("#edinfo_납부특례세액_농어촌특별세").val());
	edinfo.m01 = removeCommas($("#edinfo_m01").val());
	edinfo.m02 = removeCommas($("#edinfo_m02").val());
	edinfo.m03 = removeCommas($("#edinfo_m03").val());
	edinfo.o01 = removeCommas($("#edinfo_o01").val());
	edinfo.q01 = removeCommas($("#edinfo_q01").val());
	edinfo.h08 = removeCommas($("#edinfo_h08").val());
	edinfo.h09 = removeCommas($("#edinfo_h09").val());
	edinfo.h10 = removeCommas($("#edinfo_h10").val());
	edinfo.g01 = removeCommas($("#edinfo_g01").val());
	edinfo.h11 = removeCommas($("#edinfo_h11").val());
	edinfo.h12 = removeCommas($("#edinfo_h12").val());
	edinfo.h13 = removeCommas($("#edinfo_h13").val());
	edinfo.h01 = removeCommas($("#edinfo_h01").val());
	edinfo.k01 = removeCommas($("#edinfo_k01").val());
	edinfo.s01 = removeCommas($("#edinfo_s01").val());
	edinfo.t01 = removeCommas($("#edinfo_t01").val());
	edinfo.y02 = removeCommas($("#edinfo_y02").val());
	
	edinfo.y03 = removeCommas($("#edinfo_2018_y03").val());
	
	edinfo.y04 = removeCommas($("#edinfo_y04").val());
	edinfo.h05 = removeCommas($("#edinfo_h05").val());
	edinfo.i01 = removeCommas($("#edinfo_i01").val());
	edinfo.r10 = removeCommas($("#edinfo_r10").val());
	edinfo.h14 = removeCommas($("#edinfo_h14").val());
	edinfo.h15 = removeCommas($("#edinfo_h15").val());
	edinfo.t10 = removeCommas($("#edinfo_t10").val());
	edinfo.t11 = removeCommas($("#edinfo_t11").val());
	edinfo.t12 = removeCommas($("#edinfo_t12").val());
	edinfo.t13 = removeCommas($("#edinfo_t13").val());
	edinfo.t20 = removeCommas($("#edinfo_t20").val());
	edinfo.h16 = removeCommas($("#edinfo_h16").val());
	edinfo.h17 = removeCommas($("#edinfo_h17").val());
	edinfo.u01 = removeCommas($("#edinfo_u01").val());
	edinfo.r11 = removeCommas($("#edinfo_r11").val());
	edinfo.h06 = removeCommas($("#edinfo_h06").val());
	edinfo.h07 = removeCommas($("#edinfo_h07").val());
	edinfo.y22 = removeCommas($("#edinfo_y22").val());
	
	
	logD("# edinfo.부서ID :  " + edinfo.부서ID );
	logD("# edinfo.거주지국코드 :  " + edinfo.거주지국코드 );
	logD("# edinfo.국가코드 :  " + edinfo.국가코드 );
}
