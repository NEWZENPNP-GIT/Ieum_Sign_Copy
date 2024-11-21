/*
 * 세션정보를 셋팅한다.
 */
function setSessionInfo(){

    if ( typeof(Storage) !== "undefined" ) {
    	
    	var userId = sessionStorage.getItem('userId');
    	var yearContractId = sessionStorage.getItem('yearContractId');
    	var paramEncryptPassword = sessionStorage.getItem('paramEncryptPassword');
    	
    	//세션정보 확인
    	if(isNull(userId) || isNull(yearContractId) || isNull(paramEncryptPassword)){		
    		//세션정보 생성
    		getSessionInfo();
    	}else{
    		
    		var createDate = sessionStorage.getItem("createSessionStorageKey");
    		
    		//세션 생성시간이 없으면 세션을 초기화 하고 새로생성 
    		if(isNull(createDate)){
    			clearSession();
    			//세션정보 생성
        		getSessionInfo();
    		}else{
    			var now = new Date();
    			var expiration = new Date(createDate);
    			expiration.setMinutes(expiration.getMinutes() + 60);	//60분
    		
        	    //세션 만료시간 체크
        	    if (now.getTime() > expiration.getTime()) {
        	    	clearSession();
        			//세션정보 생성
            		getSessionInfo();
        	    }
        	    
    		}
    		
    	}
    	
    }else{
    	/*jAlert("WebStorage를 사용할 수 없습니다. 브라우저  정보확인이 필요합니다.", '', function(){
			return false;
		});*/
    	jAlert("WebStorage를 사용할 수 없습니다. 브라우저  정보확인이 필요합니다.", '');
    	return false;
    }
    
}	

/*
 * 세션 정보 확인
 */
function getSessionInfo(){
	
	var paramEncryptPassword = "";
	var yearContractId = "";	// 계약번호
	var loginId = "";			//로그인아이디
	var febYear = "";
	var userType = "";			//로그인 사용자 타입
	
	var url = rootPath + 'getSessionInfo.do';
	
	$.ajax({
		url:url,
		async: false,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		processData: false,
	    contentType: false,
		data: '',
		success:function(res) {	
			
			if(res['success']){				
				sessionStorage.setItem('userId', res['userId']);								//사용자아이디
				sessionStorage.setItem('loginId', res['loginId']);								//로그인아이디
				sessionStorage.setItem('userName', res['userName']);							//로그인 사용자 이름
				sessionStorage.setItem('userType', res['userType']);							//로그인 사용자 타입
				sessionStorage.setItem('bizId', res['bizId']);									//기업아이디
				sessionStorage.setItem('bizName', res['bizName']);								//기업이름
				sessionStorage.setItem('paramEncryptPassword', res['paramEncryptPassword']);	//파라메타 암호화 비밀번호				
				sessionStorage.setItem("createSessionStorageKey", new Date());					//세션 생성 시간
				var febYear = sessionStorage.getItem('febYear');
				var yearContractId = sessionStorage.getItem('yearContractId');
				if (isNull(febYear)) {
					 sessionStorage.setItem('febYear', res['febYear']);								//연말정산계약년도
				}
				if (isNull(yearContractId)) {
					 sessionStorage.setItem('yearContractId', res['yearContractId']);				//계약번호
				}
				
			}else{
				jAlert("세션 정보 조회에 실패 하였습니다.", 'Error Message');
				return false;
			}
			
		},
		error:function(request,status,error){		
			if (request.status=="401") {
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
				return false;
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
				return false;
			}
		}
	});
	
}


/**
 * 세션 정보를 삭제한다.
 * 
 */
function clearSession(){
	
	sessionStorage.removeItem('userId');
	sessionStorage.removeItem('loginId');
	sessionStorage.removeItem('userName');							//로그인 사용자 이름
	sessionStorage.removeItem('bizId');									//기업아이디
	sessionStorage.removeItem('bizName');
	sessionStorage.removeItem('userType');
	sessionStorage.removeItem('febYear');				
	sessionStorage.removeItem('yearContractId');
	sessionStorage.removeItem('paramEncryptPassword');
	sessionStorage.removeItem("createSessionStorageKey");
	
	//파라메타 관련 정보
	sessionStorage.removeItem("paramUserId");
	sessionStorage.removeItem("paramExcelType");
	
	//세션 전체 삭제
	sessionStorage.clear();
	
}

/*
 * sessionStorage value값 리턴
 */
function getSessionStorageValue(key){
	
	if(isNull(sessionStorage.getItem(key))){
		return "";
	}else{
		return sessionStorage.getItem(key);
	}
	
}
