var model = {
		idx: 0,
		list: [{
	      'EMail': '',
	      'addr1': '',
	      'addr2': '',
	      'bizId': '',
	      'bizName': '',
	      'businessNo': '',
	      'companyFaxNum': '',
	      'companyTelNum': '',
	      'confirmType': '',
	      'countryType': '',
	      'dbMode': '',
	      'deptCode': '',
	      'deptName': '',
	      'documentType': '',
	      'empName': '',
	      'empNo': '',
	      'empNonce': '',
	      'empType': '',
	      'empTypeName': '',
	      'endPage': '',
	      'excelRow': '',
	      'excelSheet': '',
	      'fileData': '',
	      'insDate': '',
	      'insEmpNo': '',
	      'joinDate': '',
	      'lastConnTime': '',
	      'leaveDate': '',
	      'loginId': '',
	      'message': '',
	      'ownerName': '',
	      'phoneNum': '',
	      'positionName': '',
	      'searchKey': '',
	      'searchValue': '',
	      'serviceId': '',
	      'smsSendType': '',
	      'sortName': '',
	      'sortOrder': '',
	      'startPage': '',
	      'stepName': '',
	      'telNum': '',
	      'updDate': '',
	      'updEmpNo': '',
	      'useYn': '',
	      'userDate': '',
	      'userId': '',
	      'userPwd': '',
	      'chk': false
		}],
		search: {
		   empType: 5,
		   startPage: 0,
		   endPage: 99999,
		},
		isShow: false,
		chk: false,
		isShowResult: false,
  	};

var sendName = '';
var srchType = 'all';
var srchField = ['empName', 'empNo', 'deptName'];
var userEnter = '';

// 검색 결과 여부
var isShowResult = false;

//CSS 파일 로드
var loadCssFile = function(){
	var fontPcCss = document.createElement( "link" );
	fontPcCss.href = rootPath + "css/font_pc.css";
	fontPcCss.type = "text/css";
	fontPcCss.rel = "stylesheet";
	fontPcCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( fontPcCss );
	
	var bootstrapCandyCss = document.createElement( "link" );
	bootstrapCandyCss.href = rootPath + "css/bootstrap_candy.css";
	bootstrapCandyCss.type = "text/css";
	bootstrapCandyCss.rel = "stylesheet";
	bootstrapCandyCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( bootstrapCandyCss );
				
	var jqueryAlertsCss = document.createElement( "link" );
	jqueryAlertsCss.href = rootPath + "css/jquery.alerts.css";
	jqueryAlertsCss.type = "text/css";
	jqueryAlertsCss.rel = "stylesheet";
	jqueryAlertsCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( jqueryAlertsCss );
	
	var ngxDatatableCss = document.createElement( "link" );
	ngxDatatableCss.href = rootPath + "css/ngx-datatable.css";
	ngxDatatableCss.type = "text/css";
	ngxDatatableCss.rel = "stylesheet";
	ngxDatatableCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( ngxDatatableCss );
	
	var companyManageCss = document.createElement( "link" );
	companyManageCss.href = rootPath + "css/company_manage.css";
	companyManageCss.type = "text/css";
	companyManageCss.rel = "stylesheet";
	companyManageCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( companyManageCss );		
	
	var popupMiddleManagerCss = document.createElement( "link" );
	popupMiddleManagerCss.href = rootPath + "css/ezsign/dept/group_popup_middleManager.css";
	popupMiddleManagerCss.type = "text/css";
	popupMiddleManagerCss.rel = "stylesheet";
	popupMiddleManagerCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( popupMiddleManagerCss );
} 


$(document).ready(function() {	
	
	//CSS 파일 로드
	loadCssFile();
	
	isShowResult = false;
	getDeptList();
	
	srchType = 'all';
    
    if(isShowResult){
    	$("#model_isShowResult").show();
    }else{
    	$("#model_isShowResult").hide();
    }    
    $("#srchType").val(srchType);
    
});


function getDeptList() {
	
    var formData = new FormData();
	formData.append('empType', model.search.empType);
	formData.append('startPage', model.search.startPage);
	formData.append('endPage', model.search.endPage);
	
	//blockUi 호출
	fncOnBlockUI();
		
	var url = rootPath + 'emp/getEmpList.do';
    
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		async:false,
		//contentType: "application/json; charset=UTF-8",
		//data: JSON.stringify(notice),
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {		
			
			if (res.success) {
				
				if(res.total != 0){
					model.list = res['data'];
					
					var modelListHtml = "";				
					for(var idx = 0 ; idx < model.list.length ; idx++){
						row = model.list[idx];
						if((row.deptCode == '' || row.deptCode == null) &&
							!(opener.selectedMiddelUserId.includes(row.userId))){
							modelListHtml += "<tr onClick='setManager(\""+idx+"\");' style='cursor:pointer;'>";
							//modelListHtml += "<td class='chk_area'>";
							//modelListHtml += "<input type='checkbox' id='row_chk' name='row_chk' onChange='setManager(row.empName)'>";
							//modelListHtml += "</td>";
							modelListHtml += "<td>"+row.empName+"</td>";
							modelListHtml += "<td>"+row.empNo+"</td>";
							modelListHtml += "<td>"+row.deptName+"</td>";
							modelListHtml += "</tr>";
						}
					}
		            $("#modelListTbody").html(modelListHtml);
				}
	            $("#userEnter").keyup(function() {
	                var k = $(this).val();
	                $("#empTable > tbody > tr").hide();
	                
	                if($("#srchType").val() == "empName"){
	                	var temp = $("#empTable > tbody > tr > td:nth-child(1):contains('" + k + "')");
		                $(temp).parent().show();
	                }else if($("#srchType").val() == "empNo"){
	                	var temp = $("#empTable > tbody > tr > td:nth-child(2):contains('" + k + "')");
		                $(temp).parent().show();
	                }else if($("#srchType").val() == "deptName"){
	                	var temp = $("#empTable > tbody > tr > td:nth-child(3):contains('" + k + "')");
		                $(temp).parent().show();
	                }else{
	                	/*var temp = $("#empTable > tbody > tr > td:nth-child(4n+2):contains('" + k + "')");
		                $(temp).parent().show();*/
	                	var temp = $("#empTable > tbody > tr > td:nth-child(1n+1):contains('" + k + "')");
		                $(temp).parent().show();
	                }	                
	            });
	            
	            $.unblockUI();			
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

}


function selectSrch() {

    userEnter = '';
    $("#userEnter").val('');
    srchType = $("#srchType").val();
        
    srchField = [srchType];
    if (srchType == 'all') {
    	srchField = ['empName', 'empNo', 'deptName'];
    }
    
    //리트스 초기화
    var temp = $("#empTable > tbody > tr > td:nth-child(1n+1):contains('')");
    $(temp).parent().show();
    
}

function setManager(idx){
	opener.setManager(model.list[idx]);	
	winClose();
}

function winClose(){
	self.close();
}
