
var model = {
		user: {
			userId: '',
			userPwd: '',
			prevUserPwd: ''
		}
  	};

$(document).ready(function() {
	
	model.user.userPwd = '';
    model.user.userPwd1 = '';
    model.user.prevUserPwd = '';
    model.user.userId = getCookie('loginId');
    
});


//비밀번호 변경
function changePw() {

	model.user.prevUserPwd = $.trim($("#model_user_prevUserPwd").val());
	model.user.userPwd = $.trim($("#model_user_userPwd").val());
	model.user.userPwd1 = $.trim($("#model_user_userPwd1").val());
	
	if (isNull(model.user.prevUserPwd)){
		jAlert('이전 비밀번호를 입력해주십시오.','',function(){
			$("#model_user_prevUserPwd").focus();
		});
		return false;
    }
	
	// 비밀번호 검증 : 8자리
	if (isNull(model.user.userPwd)){
		jAlert('변경할 비밀번호를 입력해주십시오.','',function(){
			$("#model_user_userPwd").focus();
		});
		return false;
    }
	if (isNull(model.user.userPwd1)){
		jAlert('비밀번호 확인을 입력해주십시오.','',function(){
			$("#model_user_userPwd1").focus();
		});
		return false;
    }
    if (model.user.userPwd != model.user.userPwd1) {
    	jAlert('입력하신 신규 비밀번호가 일치하지 않습니다.','',function(){
			$("#model_user_userPwd1").focus();
		});
    	return false;
    }
    
    
    var formData = new FormData();
    formData.append('userId', model.user.userId);
    formData.append('userPwd', model.user.userPwd);
    formData.append('prevUserPwd', model.user.prevUserPwd);

    
	var url = rootPath + 'mypage/pwdChange.do';
	
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
				jAlert(res['message'],'',function(){
					self.close();
				});
			}else{
				//jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
				jAlert(res['message'],'');
			}
		},
		error:function(request,status,error){
			if (request.status=="401") {
				clearSession();
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
			}
		}
	});

}

function winClose(){
	self.close();
}