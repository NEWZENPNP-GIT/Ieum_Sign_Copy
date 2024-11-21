var yearContractId = "";
var userId = "";
var placeId;
var selectUserId;
var userType;

var companyWorkplaceInfo;
var edinfoList;

$(document).ready(function() {
	
	yearContractId = sessionStorage.getItem("yearContractId");
	userType = sessionStorage.getItem("userType");
	
	// console.log("map_include");
	// 계약아이디가 없으면 메인으로 튕겨내기
    if (isNull(yearContractId)) {    	
    	//jAlert('사용불가 메뉴 입니다. 기본설정값을 확인 해주세요' , 'System Message', goHome);
    } else {    	
    	if(!isNull(sessionStorage.getItem('paramUserId'))){
    		userId = sessionStorage.getItem('paramUserId');
    	}
    	
    	if (isNotNull(userType) && userType >= 5) {
    		getCompanyWorkplaceInfo();
    		
    		//검색창 검색 기능 추가
    	    $("#searchTerm").keyup(function() {
    	        var kVal = $(this).val(); 
    	        
    	        $("#empList > li").hide();        
    	        var temp = $("#empList > li > .name:nth-child(1n):contains('" + kVal + "')");
    	        $(temp).parent().show();        
    	    });
    	}
    	
    }
    
});


//사업장 정보 조회
function getCompanyWorkplaceInfo() {
	
	var params = { 
	        계약ID: yearContractId,
	        startPage: 0,
	        endPage: 10
	      };
			
	var url = rootPath + '/febsystem/getYS030List.do';
	
	//blockUi 호출
	fncOnBlockUI();
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"GET",
		data: params,		
		success:function(res) {	
			
			companyWorkplaceInfo = res['data'];			
			placeId = companyWorkplaceInfo[0]['사업장ID'];
			
			logD("# 사업장ID : " + placeId);
			logD("# this.companyWorkplaceInfo.length : " + companyWorkplaceInfo.length);
			
			var placeIdHtml = "";
			if(companyWorkplaceInfo != null && companyWorkplaceInfo.length > 0){
				for(var i = 0 ; i < companyWorkplaceInfo.length ; i++){
					
					if(placeId == companyWorkplaceInfo[i]['사업장ID']){
						placeIdHtml += "<option value='"+companyWorkplaceInfo[i]['사업장ID']+"' selected >"+companyWorkplaceInfo[i]['사업장명']+"</option>";
					}else{
						placeIdHtml += "<option value='"+companyWorkplaceInfo[i]['사업장ID']+"'>"+companyWorkplaceInfo[i]['사업장명']+"</option>";
					}
					
					
				}
			}			
			$("#placeId").html(placeIdHtml);
			
			getEmployeeList();
		},
		error:function(request,status,error){		
			$.unblockUI();
			
			if (request.status=="401") {
				//jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		}
	});
	
}

//사용자 리스트 
function getEmployeeList() {
    
    var formData = new FormData();
	formData.append("계약ID", yearContractId);
	formData.append("사업장ID", placeId);
	formData.append("근무시기", sessionStorage.getItem("workingPeriod"));	
	formData.append("startPage", 0);
	formData.append("endPage", 999999);
	
    var url = rootPath + '/febmaster/getPaymentUserStatusList.do';

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
		        edinfoList = res['data'];
		        
				var empListHtml = "";
				var activeClass = "";
				if(edinfoList != null && edinfoList.length > 0){
					
					for(var i = 0 ; i < edinfoList.length ; i++){					

						if(userId == edinfoList[i]['사용자ID']){
							activeClass = "active";	
						}else{
							activeClass = "";
						}
						empListHtml += "<li class='tbody "+activeClass+"' id='USER_LIST_"+edinfoList[i]['사용자ID']+"' onClick='selectEmployee(\""+edinfoList[i]['사용자ID']+"\",\""+i+"\")'>";
						empListHtml += "<div class='no'><p style='cursor:pointer'>"+edinfoList[i]['empNo']+"</p></div>";
						empListHtml += "<div class='name'><p style='cursor:pointer'>"+edinfoList[i]['empName']+"</p></div>";
						empListHtml += "</li>";
					}
					
					$("#empList").html(empListHtml);
					
					//선택시 active 설정
				    $("#empList").find(".tbody").click(function(e){
				    	$("#empList").find(".tbody").removeClass("active");
				    	$(e.currentTarget).addClass("active");
				    });
				    
				    
				    
				}								
				
			} else {
				jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
			}
			
			$.unblockUI();
			
		},
		error:function(request,status,error){	
			$.unblockUI();
			
			if (request.status=="401") {
				//jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		}
    });
}


function selectEmployee(사용자ID, idx) {
//    this.selectUserId = data.사용자ID;
//    this.outputEmployeeProperty.emit(data);

	//logD("# selectEmployee 사용자ID : " + 사용자ID);
	//logD("# window.location.pathname : " + window.location.pathname);
	//logD("# selectEmployee idx : " + idx);	
	var proc = false;
	// 이월기부금 페이지 이동 체크
	if(이월기부금반영) {
		jAlert('이월기부금 반영을 실행하여 주시기 바랍니다.','');
		$("#USER_LIST_"+userId).addClass('active');
		$("#USER_LIST_"+사용자ID).removeClass('active');
		
		return false;
	}

	if(checkUnload) {
		
		jConfirm('이 페이지를 벗어나면 작성된 내용은 저장되지 않습니다.', '', function(cfrm){
    		if(cfrm){
    			 goPageLink(사용자ID);
    		}else{
    			$("#USER_LIST_"+userId).addClass('active');
    			$("#USER_LIST_"+사용자ID).removeClass('active');
    		}
    	});
		
	}else{
		 goPageLink(사용자ID);
	}

}

function goPageLink(사용자ID){
	checkUnload = false;
	// 파라메타 userId 셋팅
	sessionStorage.setItem('paramUserId', 사용자ID);
	/*if(window.location.pathname.indexOf("yearTax_basicsData_employee_detail") > -1){
		getemployeeDinfo(사용자ID);
	}else if(window.location.pathname.indexOf("e_yearTax_basicsData_taxService_pdf") > -1){
		getEmployeeInfo(사용자ID);
	}else{
		
	}*/
	getList(사용자ID);
	
}

function setChanged() {
    placeId = $("#placeId").val();

   	getEmployeeList();
}


