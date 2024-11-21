var bizInfo;
var userInfo;

var bizParam;
var manInfo;
var udpInfo = {
		ownerName: '',
		businessNo: '',
		addr1: '',
		companyTelNum: '',
		companyFaxNum: '',
		empName: '',
		phoneNum: '',
		eMail: ''
  	};
var compInfo = {
		empName: '',
		phoneNum: '',
		EMail: '',
		loginId: ''
  	};

// 계약ID
var 계약ID;

$(document).ready(function() {
	계약ID = getCookie('yearContractId');
	
	getUserInfo();
});


function getUserInfo(){
	
	var formData = new FormData();
	var url = rootPath + 'mypage/getMyPageBizInfo.do';
	
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
				  bizInfo = res['bizInfo'];
			      compInfo = res['empInfo'];
			      userInfo = res['userInfo'];
			      
			      var bizInfoHtml = "";
			      
			      for(var idx = 0 ; idx < bizInfo.length ; idx++){
			    	  var info = bizInfo[idx];
			    	  
			    	  bizInfoHtml +="<div class='group_row row1'>";
			    	  bizInfoHtml +="<dl class='NanumGothic rat1n-9'>";
			    	  bizInfoHtml +="<dt>";
			    	  bizInfoHtml +="<label class='label_txt NanumGothic'>기업명</label>";
			    	  bizInfoHtml +="</dt>";
			    	  bizInfoHtml +="<dd class='default_box'>";
			    	  bizInfoHtml +="<div class='disable'>"+info.bizName+"</div>";
			    	  bizInfoHtml +="</dd>";
			    	  bizInfoHtml +="</dl>";
			    	  bizInfoHtml +="</div>";
			    	  bizInfoHtml +="<div class='group_row row1'>";
			    	  bizInfoHtml +="<dl class='NanumGothic rat1n-9'>";
			    	  bizInfoHtml +="<dt>";
			    	  bizInfoHtml +="<label class='label_txt NanumGothic'>사번</label>";
			    	  bizInfoHtml +="</dt>";
			    	  bizInfoHtml +="<dd class='default_box'>";
			    	  bizInfoHtml +="<div class='disable'>adimn</div>";
			    	  bizInfoHtml +="</dd>";
			    	  bizInfoHtml +="</dl>";
			    	  bizInfoHtml +="</div>";
			    	  bizInfoHtml +="<div class='group_row row1'>";
			    	  bizInfoHtml +="<dl class='NanumGothic rat1n-9'>";
			    	  bizInfoHtml +="<dt>";
			    	  bizInfoHtml +="<label class='label_txt NanumGothic'>대표자명</label>";
			    	  bizInfoHtml +="</dt>";
			    	  bizInfoHtml +="<dd class='input_box'>";
			    	  bizInfoHtml +="<input class='' type='text' name='ownerName' value='"+info.ownerName+"' id='info_ownerName'>";
			    	  bizInfoHtml +="</dd>";
			    	  bizInfoHtml +="</dl>";
			    	  bizInfoHtml +="</div>";
			    	  bizInfoHtml +="<div class='group_row row1'>";
			    	  bizInfoHtml +="<dl class='NanumGothic rat1n-9'>";
			    	  bizInfoHtml +="<dt>";
			    	  bizInfoHtml +="<label class='label_txt NanumGothic'>사업자등록번호</label>";
			    	  bizInfoHtml +="</dt>";
			    	  bizInfoHtml +="<dd class='input_box'>";
			    	  bizInfoHtml +="<input class='' type='text' name='businessNo' value='"+bizNumberHyphen(info.businessNo)+"' id='info_businessNo' onChange='changeBizNumber(\"info_businessNo\")' maxlength='10'>";
			    	  bizInfoHtml +="</dd>";
			    	  bizInfoHtml +="</dl>";
			    	  bizInfoHtml +="</div>";
			    	  bizInfoHtml +="<div class='group_row row1'>";
			    	  bizInfoHtml +="<dl class='NanumGothic rat1n-9'>";
			    	  bizInfoHtml +="<dt>";
			    	  bizInfoHtml +="<label class='label_txt NanumGothic'>법인등록번호</label>";
			    	  bizInfoHtml +="</dt>";
			    	  bizInfoHtml +="<dd class='input_box'>";
			    	  bizInfoHtml +="<input class='' type='text' name='pinNo' value='"+info.pinNo+"' id='info_pinNo'>";
			    	  bizInfoHtml +="</dd>";
			    	  bizInfoHtml +="</dl>";
			    	  bizInfoHtml +="</div>";
			    	  bizInfoHtml +="<div class='group_row row1'>";
			    	  bizInfoHtml +="<dl class='NanumGothic rat1n-9'>";
			    	  bizInfoHtml +="<dt>";
			    	  bizInfoHtml +="<label class='label_txt NanumGothic'>주소</label>";
			    	  bizInfoHtml +="</dt>";
			    	  bizInfoHtml +="<dd class='input_box'>";
			    	  bizInfoHtml +="<input class='' type='text' name='addr1' value='"+info.addr1+"' id='info_addr1'>";
			    	  bizInfoHtml +="</dd>";
			    	  bizInfoHtml +="</dl>";
			    	  bizInfoHtml +="</div>";
			    	  bizInfoHtml +="<div class='group_row row1'>";
			    	  bizInfoHtml +="<dl class='NanumGothic rat1n-9'>";
			    	  bizInfoHtml +="<dt>";
			    	  bizInfoHtml +="<label class='label_txt NanumGothic'>대표번호</label>";
			    	  bizInfoHtml +="</dt>";
			    	  bizInfoHtml +="<dd class='input_box'>";
			    	  bizInfoHtml +="<input class='' type='text' name='companyTelNum' value='"+phoneNumberHyphen(info.companyTelNum)+"' id='info_companyTelNum' onChange='changePhoneNumber(\"info_companyTelNum\")' >";
			    	  bizInfoHtml +="</dd>";
			    	  bizInfoHtml +="</dl>";
			    	  bizInfoHtml +="</div>";
			    	  bizInfoHtml +="<div class='group_row row1'>";
			    	  bizInfoHtml +="<dl class='NanumGothic rat1n-9'>";
			    	  bizInfoHtml +="<dt>";
			    	  bizInfoHtml +="<label class='label_txt NanumGothic'>팩스번호</label>";
			    	  bizInfoHtml +="</dt>";
			    	  bizInfoHtml +="<dd class='input_box'>";
			    	  bizInfoHtml +="<input class='' type='text' name='companyFaxNum' value='"+phoneNumberHyphen(info.companyFaxNum)+"' id='info_companyFaxNum' onChange='changePhoneNumber(\"info_companyFaxNum\")' >";
			    	  bizInfoHtml +="</dd>";
			    	  bizInfoHtml +="</dl>";
			    	  bizInfoHtml +="</div>";
			    	  bizInfoHtml +="<div class='group_row row1'>";
			    	  bizInfoHtml +="<dl class='NanumGothic rat1n-9'>";
			    	  bizInfoHtml +="<dt>";
			    	  bizInfoHtml +="<label class='label_txt NanumGothic'>아이디</label>";
			    	  bizInfoHtml +="</dt>";
			    	  bizInfoHtml +="<dd class='default_box'>";
			    	  bizInfoHtml +="<div class='disable'>"+compInfo.loginId+"</div>";
			    	  bizInfoHtml +="</dd>";
			    	  bizInfoHtml +="</dl>";
			    	  bizInfoHtml +="</div>";
			      }
			      
			      $("#bizInfoDiv").html(bizInfoHtml);
			      
			      $("#compInfo_empName").val(compInfo.empName);
			      $("#compInfo_phoneNum").val(phoneNumberHyphen(compInfo.phoneNum));
			      $("#compInfo_EMail").val(compInfo.EMail);
			      
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

//취소
function cancelMyBiz() {
	
	jConfirm('취소하시겠습니까?','',function(cfrm){
		
		if(cfrm){
			$("#contents_wrap").empty();
			$(window).off("resize");
			
			$.ajax({
				url: rootPath + "html/mypage/myPage_memberInfo.html",
				success: function(result) {							
					$("#contents_wrap").html(result);
				},
				error: function(request, status, error) {
					jAlert("다시 확인해주시기 바랍니다."+error);
				}
			});
		}
		
	});

}

//저장
function updMyPageBiz() {
	
	var mdBizInfo = {
		계약ID: 계약ID,
		ownerName: $("#info_ownerName").val(),
		businessNo: $("#info_businessNo").val().replace(/-/gi,""),
		pinNo: $("#info_pinNo").val().replace(/-/gi,""),
		addr1: $("#info_addr1").val(),
		companyTelNum: $("#info_companyTelNum").val().replace(/-/gi,""),
		companyFaxNum: $("#info_companyFaxNum").val().replace(/-/gi,""),
		empName: $("#compInfo_empName").val(),
		phoneNum: $("#compInfo_phoneNum").val().replace(/-/gi,""),
		eMail: $("#compInfo_EMail").val(),
		loginId: compInfo.loginId,
		startPage: '0',
		endPage: '10'
    };

    var formData = new FormData();
    formData.append('계약ID', mdBizInfo.계약ID);
    formData.append('ownerName', mdBizInfo.ownerName);
    formData.append('businessNo', mdBizInfo.businessNo);
    formData.append('pinNo', mdBizInfo.pinNo);
    formData.append('addr1', mdBizInfo.addr1);
    formData.append('companyTelNum', mdBizInfo.companyTelNum.replace(/\-/g, ''));
    formData.append('companyFaxNum', mdBizInfo.companyFaxNum.replace(/\-/g, ''));
    formData.append('empName', mdBizInfo.empName);
    formData.append('phoneNum', mdBizInfo.phoneNum.replace(/\-/g, ''));
    formData.append('eMail', mdBizInfo.eMail);
    formData.append('loginId', mdBizInfo.loginId);
    formData.append('startPage', mdBizInfo.startPage);
    formData.append('endPage', mdBizInfo.endPage);
    
    var url = rootPath + 'mypage/updMyPageBizInfo.do';
	
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
				setCookie('userName', mdBizInfo.empName);
			     $('#loginName').html(textLengthOverCut(mdBizInfo.empName, 10, '...'));

			     jAlert('저장이 완료 되었습니다.','',function(){
			    	 $("#contents_wrap").empty();
			    	 $(window).off("resize");
					
			    	 $.ajax({
			    	 	url: rootPath + "html/mypage/myPage_memberInfo.html",
			    	 	success: function(result) {							
			    	 		$("#contents_wrap").html(result);
			    	 	},
			    	 	error: function(request, status, error) {
			    	 		jAlert("다시 확인해주시기 바랍니다."+error);
			    	 	}
			    	 });
			     });
				
				
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