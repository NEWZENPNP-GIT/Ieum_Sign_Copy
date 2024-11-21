var yearContractId;				// 계약 ID
var 근무시기;	
var workplaceId = '';			// 사업장 ID
var userId;						// 사용자 ID
var deptNo = '';				// 부서 ID
var empStatus = '0';			// 근무상태
var empName = '';				// 성명
var empNo = '';				  	// 사번  ID
var positionName = '';		  	// 직책  ID

//페이징 관련
var totalCount = 0;  // 총건수
var perPage = 50;   //
var currPage = 1;
var startPage = 0;
var endPage = 50;
var tempPage = 1;

var empInfoData = [
    {
      '근무시기': '',
      'EMail': '',
      'address': '',
      'bizId': '',
      'code': '',
      'deptCode': '',
      'deptName': '',
      'empName': '',
      'empNo': '',
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
      'joinDate': '',
      'k01': '',
      'leaveDate': '',
      'm01': '',
      'm02': '',
      'm03': '',
      'm1': '',
      'm10': '',
      'm11': '',
      'm12': '',
      'm2': '',
      'm3': '',
      'm4': '',
      'm5': '',
      'm6': '',
      'm7': '',
      'm8': '',
      'm9': '',
      'message': '',
      'o01': '',
      'phoneNum': '',
      'positionName': '',
      'q01': '',
      'r10': '',
      'r11': '',
      's01': '',
      't01': '',
      't10': '',
      't11': '',
      't12': '',
      't20': '',
      'userPwd': '',
      'y02': '',
      'y03': '',
      'y04': '',
      'y22': '',
      '감면기간_FROM': '',
      '감면기간_TO': '',
      '감면시작일': '',
      '감면율': '',
      '감면종료일': '',
      '개인식별번호': '',
      '거주구분': '',
      '거주지국코드': '',
      '건강보험료': '',
      '계약ID': '',
      '고용보험료': '',
      '공무원연금': '',
      '관리자1차확정_건수': '',
      '관리자최종확정_건수': '',
      '국가코드': '',
      '국민연금보험료': '',
      '국세청PDF유형': '',
      '국외근로제공여부': '',
      '군인연금': '',
      '근로소득금액': '',
      '근로자확정_건수': '',
      '근무시작일': '',
      '근무종료일': '',
      '근무지구분': '',
      '급여': '',
      '나이': '',
      '내외국인구분': '',
      '농어촌특별세': '',
      '등록일시': '',
      '별정우체국연금': '',
      '부서ID': '',
      '부서명': '',
      '부서표시여부': '',
      '사내내선번호': '',
      '사립학교교직원연금': '',
      '사업자등록번호': '',
      '사업장ID': '',
      '사업장명': '',
      '사용자ID': '',
      '사용여부': '',
      '상여': '',
      '생산직등야간근로비과세': '',
      '세대주여부': '',
      '소득세': '',
      '소득세적용률': '',
      '연말정산분납여부': '',
      '외국법인파견근로자': '',
      '외국인단일세율적용': '',
      '우리사주조합인출금': '',
      '원천명세ID': '',
      '인정상여': '',
      '일련번호': '',
      '임원퇴직소득금액한도초과액': '',
      '장기요양보험료': '',
      '전년도총급여': '',
      '종전근무지_납세조합유무': '',
      '주식매수선택권행사이익': '',
      '중소기업취업감면여부': '',
      '지방소득세': '',
      '진행상태코드': '',
      '진행중_건수': '',
      '총건수': '',
      '총급여': '',
      '현근무지여부': '',
      '회사명': '',
      '연말대상': '미배정',
    }
  ];


$(document).ready(function() {
	
	yearContractId = getCookie('yearContractId');
	
	// 계약아이디가 없으면 메인으로 튕겨내기
    if (isNull(yearContractId)) {    	
    	jAlert('사용불가 메뉴 입니다. 기본설정값을 확인 해주세요.' , 'System Message', goHome);    	
    } else {     
    	//근무시기를 셋팅한다.
    	sessionStorage.setItem("workingPeriod", $("#근무시기").val());
    	
    	getList();
    	
    	//마지막 검색창시에는 검색시도
    	$('#searchParams_positionName').keydown(function(e){
    		//enter 일경우
    	    if(e.keyCode == 13){
    	    	srchEmpDetail();
    	    }
    	});
    	
    	//input,select focus 이동
    	$('input,select').on('keypress', function (e) {
    		if (e.which == 13) {
    			e.preventDefault();    			
    			var nxtIdex = $('input,select').index(this) + 1;    			
    			if(nxtIdex < 6){    				
    				$('input,select')[nxtIdex].focus();
    			}
    		}    		
    	});
    	
    	//전체선택 체크박스 클릭 
    	$("#isAllchkShow").click(function(){  
    		if($("#isAllchkShow").prop("checked")) { 
    			$("input[type=checkbox]").prop("checked",true);  
    		} else { 
    			$("input[type=checkbox]").prop("checked",false); 
    		} 
    	});
    	    	
    }
});

//근로자정보 전체조회
function getList(){
	근무시기 = $("#근무시기").val();
		
	var formData = new FormData();
	formData.append("계약ID", yearContractId);
	formData.append("근무시기", 근무시기);	
	// 검색필터
	formData.append("사업장ID", workplaceId);	
	formData.append("부서ID", deptNo);
	formData.append("근무상태", empStatus);
	formData.append("empName", empName);
	formData.append("empNo", empNo);
	formData.append("positionName", positionName);
	formData.append("startPage", startPage);
	formData.append("endPage", endPage);
	
	
	var url = rootPath + 'febworker/getYP000List.do';
	
	
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
			logD("# res.success : " + res.success );
			
			if (res.success) {
				
				// 체크박스를 위한 true / false 셋팅
				for (var index = 0; index < res['data'].length; index++) {
					res['data'][index]['chk'] = false;
			        res['data'][index]['dbMode'] = 'R';
					
			        // 근무상태 입력
			        if ( res['data'][index]['leaveDate'] !== '' ) {
			          res['data'][index]['근무상태'] = '1';
			        } else {
			          res['data'][index]['근무상태'] = '2';
			        }
			        
			        res['data'][index]['사용여부'] =
			            ( res['data'][index]['사용여부'] === ''  ||  res['data'][index]['사용여부'] === null )  ? '2' : res['data'][index]['사용여부'] ;
				}
				
				empInfoData = res['data'];
				
				//사업장 선택
				var selectWorkplaceInfoHtml = "<option value=''>전체</option>";
				if(res['ys030'] != null){
					for(var index = 0 ; index < res['ys030'].length ; index++){					
						selectWorkplaceInfoHtml += "<option value='"+res['ys030'][index]['사업장ID']+"'>"+res['ys030'][index]['사업장명']+"</option>";
					}
				}
				$("#searchParams_사업장ID").html(selectWorkplaceInfoHtml);
								
				// 근무상태 선택 varempStatusInfo = res['code420'];
				var selectEmpStatusInfoHtml = "";
				if(res['code420'] != null){
					for(var index = 0 ; index < res['code420'].length ; index++){					
						selectEmpStatusInfoHtml += "<option value='"+res['code420'][index]['commCode']+"'>"+res['code420'][index]['commName']+"</option>";
					}
				}
				$("#searchParams_근무상태").html(selectEmpStatusInfoHtml);
				
				$(".chosen-select").trigger("chosen:updated");
				
				var empInfoHtml = "";
				for(var index = 0 ; index < empInfoData.length ; index++){
					empInfoHtml += "<tr>";
					
					empInfoHtml += "<td class='chk_area isAllchkShow'><input type='checkbox' id='row_chk' name='row_chk' value='"+empInfoData[index]['empNo']+"' /></td>";					
					empInfoHtml += "<td class='value'>"+empInfoData[index]['사업장명']+"</td>";
					empInfoHtml += "<td class='value'><a href='javascript:addEmployee(\""+empInfoData[index]['계약ID']+"\",\""+empInfoData[index]['사용자ID']+"\")' class='btn_type btn_detailView'>"+empInfoData[index]['empName']+"</a></td>";
					empInfoHtml += "<td class='value'>"+empInfoData[index]['empNo']+"</td>";
					empInfoHtml += "<td class='value'>"+empInfoData[index]['부서명']+"</td>";
					empInfoHtml += "<td class='value'>"+empInfoData[index]['EMail']+"</td>";
					empInfoHtml += "<td class='value'>"+empInfoData[index]['사내내선번호']+"</td>";
					empInfoHtml += "<td class='value'>"+empInfoData[index]['phoneNum']+"</td>";
					
					if(isNull(empInfoData[index]['연말대상'])){
						empInfoHtml += "<td class='value'>미대상</td>";
					}else{
						empInfoHtml += "<td class='value'>"+empInfoData[index]['연말대상']+"</td>";
					}
					
					empInfoHtml += "</tr>";
					
				}

				$("#empInfoBody").html(empInfoHtml);
				
				
				// 페이징 관련
			    totalCount = res['total'];
			    
			    logD("# this.totalCount : " + totalCount );
			    logD("# this.perPage : " + perPage);
			    
			    var paginateOption = {
			            total: Math.ceil(totalCount / perPage),
			            page: currPage
			          };  
			    
			    logD("# paginateOption['total'] : " + paginateOption['total']);
			    logD("# paginateOption['page'] : " + paginateOption['page']);
				
			    if (totalCount > 0) {
			        pageSelection(paginateOption);
			    } else {
			        $('#page-selection').hide();
			    }
			    
			} else {
				jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
			}
			
			$.unblockUI();
		},
		error:function(request,status,error){
			$.unblockUI();
			
			if (request.status=="401") {
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
				//location.href=rootPath;
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		}
	});	
}


//사업장 선택
function ngSelectPlace() {
    workplaceId = $("#searchParams_사업장ID").val();
    
    if(isNull(workplaceId)){
    	// 부서정보
		var selectDeptInfoHtml = "<option value=''>전체</option>";		
		$("#searchParams_부서ID").html(selectDeptInfoHtml);
		$("#searchParams_부서ID").trigger("chosen:updated");
		
    }else{
    	getDept();
    }
    
    
    logD("# this.workplaceId : " + workplaceId);
}

// 부서 조회
function getDept() {

    var formData = new FormData();
	formData.append("계약ID", yearContractId);
	formData.append("사업장ID", workplaceId);
	formData.append("startPage", 0);
	formData.append("endPage", 10);
    
    //blockUi 호출
	fncOnBlockUI();
	
	var url = rootPath + 'febsystem/getYS031List.do';
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {	
			logD("# res.success : " + res.success );
			
			if (res.success) {
				// 부서정보
				var selectDeptInfoHtml = "<option value=''>전체</option>";
				for(var index = 0 ; index < res['data'].length ; index++){					
					selectDeptInfoHtml += "<option value='"+res['data'][index]['부서ID']+"'>"+res['data'][index]['부서명']+"</option>";
				}
				$("#searchParams_부서ID").html(selectDeptInfoHtml);
				$("#searchParams_부서ID").trigger("chosen:updated");
			}
			
			$.unblockUI();
		},
		error:function(request,status,error){
			$.unblockUI();
			
			if (request.status=="401") {
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
				//location.href=rootPath;
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		}
	});		
	
}


//페이징 관련 
function pageSelection(opt) {
    var _this = this;    
    var settings = $.extend({}, {
      total: 0,
      page: _this.currPage,
      maxVisible: 5,
      leaps: true,
      firstLastUse: true,
      first: '',
      last: '',
      wrapClass: 'pagination',
      activeClass: 'active',
      disabledClass: 'disabled',
      nextClass: 'btn_next',
      prevClass: 'btn_prev',
      lastClass: 'end_page',
      firstClass: 'first_page'
    }, opt);

    $('#page-selection').show().bootpag(settings).off('page').on('page', function(event, num) {
      _this.tempPage = num;
      _this.onChangeperPage();
    });
    $('.grid_paging .select_box select').val(this.perPage);
}


function onChangeperPage() {
	perPage = $("#perPage").val();
		
    startPage = (tempPage - 1 ) * perPage;
    if (perPage > totalCount) {
      startPage = 0;
    }
    endPage = perPage;
    currPage =  tempPage;

    getList();
}


//근로자 상세 검색
function srchEmpDetail() {	
  startPage = 0;
  endPage = 10;
  근무시기 = $("#근무시기").val();
  
  workplaceId = $("#searchParams_사업장ID").val();  
  deptNo = $("#searchParams_부서ID").val();
  empStatus = $("#searchParams_근무상태").val();
  empName = $("#searchParams_empName").val();
  empNo = $("#searchParams_empNo").val();
  positionName = $("#searchParams_positionName").val();
  
  var formData = new FormData();
  formData.append("계약ID", yearContractId);
  formData.append("근무시기", 근무시기);
  //검색필터
  formData.append("사업장ID", workplaceId);
  formData.append("부서ID", deptNo);
  formData.append("근무상태", empStatus);
  formData.append("empName", empName);
  formData.append("empNo", empNo);
  formData.append("positionName", positionName);
  formData.append("startPage", startPage);
  formData.append("endPage", endPage);
  
	
  var url = rootPath + 'febworker/getYP000List.do';
  
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
			logD("# res.success : " + res.success );
			
			if (res.success) {
				
				empInfoData = res['data'];
				
				var empInfoHtml = "";
				for(var index = 0 ; index < empInfoData.length ; index++){
					empInfoHtml += "<tr>";
					
					empInfoHtml += "<td class='chk_area isAllchkShow'><input type='checkbox' id='row_chk' name='row_chk' value='"+empInfoData[index]['empNo']+"' /></td>";					
					empInfoHtml += "<td class='value'>"+empInfoData[index]['사업장명']+"</td>";
					empInfoHtml += "<td class='value'><a href='javascript:addEmployee(\""+empInfoData[index]['계약ID']+"\",\""+empInfoData[index]['사용자ID']+"\")' class='btn_type btn_detailView' >"+empInfoData[index]['empName']+"</a></td>";
					empInfoHtml += "<td class='value'>"+empInfoData[index]['empNo']+"</td>";
					empInfoHtml += "<td class='value'>"+empInfoData[index]['부서명']+"</td>";
					empInfoHtml += "<td class='value'>"+empInfoData[index]['EMail']+"</td>";
					empInfoHtml += "<td class='value'>"+empInfoData[index]['사내내선번호']+"</td>";
					empInfoHtml += "<td class='value'>"+empInfoData[index]['phoneNum']+"</td>";
					
					if(isNull(empInfoData[index]['연말대상'])){
						empInfoHtml += "<td class='value'>미대상</td>";
					}else{
						empInfoHtml += "<td class='value'>"+empInfoData[index]['연말대상']+"</td>";
					}
					
					empInfoHtml += "</tr>";
					
				}

				$("#empInfoBody").html(empInfoHtml);
				
				// 페이징 관련
			    totalCount = res['total'];
			    
			    logD("# this.totalCount : " + totalCount );
			    logD("# this.perPage : " + perPage);
			    
			    var paginateOption = {
			            total: Math.ceil(totalCount / perPage),
			            page: currPage
			          };  
			    
			    logD("# paginateOption['total'] : " + paginateOption['total']);
			    logD("# paginateOption['page'] : " + paginateOption['page']);
				
			    if (totalCount > 0) {
			        pageSelection(paginateOption);
			    } else {
			        $('#page-selection').hide();
			    }
			    
			} else {
				jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
			}
			
			$.unblockUI();
		},
		error:function(request,status,error){
			$.unblockUI();
				
			if (request.status=="401") {
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
			}
		}
	});		
			
}


// 사용여부 저장
function setemployeeDinfo(yn) {
	logD("# yn : " + yn);
	var chkCnt = 0;
	 $("#row_chk:checked").each(function() {
		 chkCnt++;
		 
		 if(yn == 'use'){

			for (var index = 0; index < empInfoData.length; index++) {					 
				if($(this).val() == empInfoData[index]['empNo']){
					empInfoData[index]['계약ID'] = yearContractId;
					empInfoData[index]['dbMode'] = 'U';
					empInfoData[index]['사용여부'] = '1';
				}					 
			 }

		 } else if (yn == 'disuse') {
			 
			for (var index = 0; index < empInfoData.length; index++) {
				if($(this).val() == empInfoData[index]['empNo']){
					empInfoData[index]['계약ID'] = yearContractId;
					empInfoData[index]['dbMode'] = 'U';
					empInfoData[index]['사용여부'] = '2';
				}				 
			 }
			
		 }

	 });
	 
	 if(chkCnt == 0){
		 jAlert('선택된 정보가 없습니다.','');
		 return;
	 }

	 var url = rootPath + 'febworker/saveYP000Assign.do';
	  
	 //blockUi 호출
	 fncOnBlockUI();
		
	 $.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify(empInfoData),
		success:function(res) {	
			
			if (res.success) {										  
				jAlert('저장되었습니다.','');
				$("#isAllchkShow").prop("checked",false);
				getList();
			} else {
				jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
			}
			
			$.unblockUI();
		},
		error:function(request,status,error){
			$.unblockUI();
				
			if (request.status=="401") {
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
			}
		}
	 });		
}


//상세페이지 이동
function addEmployee(yearContractId, userId) {	
	//this.router.navigate(['/yearTax_basicsData_employee_detail'], { queryParams: { contractId: contractId, userID: userId }});
	// location.assign(rootPath + "html/feb/febworker/yearTax_basicsData_employee_detail.html?userId="+paramEncrypt(userId));
	
	$("#contents_wrap").empty();
	$(window).off("resize");
	
	
	$.ajax({
		url: rootPath + "html/feb/employee/yearTax_payment_employee_detail.html",
		success: function(result) {
			sessionStorage.setItem("paramWorkPeriod", 근무시기);
			sessionStorage.setItem("paramUserId", userId);						
			sessionStorage.setItem("yearContractId", yearContractId);
			sessionStorage.setItem("febYear", getCookie('febYear'));
			
			$("#contents_wrap").html(result);
		},
		error: function(request, status, error) {
			jAlert("다시 확인해주시기 바랍니다."+error);
		}
	});
}

//엑셀 업로드
function openExcel(type) {
    //getExcelType(type);
	
	sessionStorage.setItem("bizId", getCookie('bizId'));
	sessionStorage.setItem("febYear", getCookie('febYear'));	
	sessionStorage.setItem("yearContractId", yearContractId);
	sessionStorage.setItem("paramExcelType", type);	
	
	openWin (rootPath + "html/feb/febmaster/common_popup_excelUpload.html","excelUploadPop","1200","850");

}

//서식 다운로드 버튼
function downExcel(evt) {

  var dataUrl = rootPath + 'data/excelUpload/샘플_근로자등록.xlsx';
  
  evt.target.setAttribute('href', dataUrl);  
}

