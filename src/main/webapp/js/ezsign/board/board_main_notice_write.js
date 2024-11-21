// 전역으로 처리 예정
var userInfo = {
		loginType: getCookie('loginType'),
		loginId: getCookie('loginId'),
		userId : getCookie('userId')
  	};

var model = {
	write: {
		bbsId: '202005130548001',
		workCode: '1',
		subject: '',
		contentsType: '2',
		contents: '',
		expireDate:'',
	}
};

$(document).ready(function() {
	
	$("#userName").html(userInfo.loginName);
});

// 작성완료
function doWrite() {

	model.write.subject = $("#model_write_subject").val();
	model.write.expireDate = $("#model_write_expire_date").val();

    if ($.trim(model.write.subject) == '') {
    	jAlert('제목을 입력 하세요.','', function(){
    		$("#model_write_subject").focus();
    	});
      
    	return false;
    }
    
    if ($.trim(model.write.expireDate) == '') {
    	jAlert('공지 만료일을  선택 하세요.','', function(){
    		$("#model_write_expire_date").focus();
    	});
      
    	return false;
    }
    
    var formData = new FormData();
    formData.append('bbsId', model.write.bbsId);
    formData.append('workCode', model.write.workCode);
    formData.append('subject', model.write.subject);
    formData.append('contentsType', model.write.contentsType);
    formData.append('expireDate', model.write.expireDate);
    formData.append('hitCnt', '0');

    var file = document.getElementsByName('attachFile');
    for (var i = 0; i < file.length; i++) {
    	if (file[i]['files'][0] != undefined && file[i]['files'][0] != null) {
    		if (checkSpecial(file[i]['files'][0].name)) {
    			jAlert('파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.','');
    			return false;
    		}
    		formData.append('addFile' + i, file[i]['files'][0]);
    	}
    }

    logI('>> formData=', formData);
    
    //blockUi 호출
	fncOnBlockUI();
    
    var url = rootPath + 'bbs/insBbsContents.do';
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		//async:false,
		//contentType: "application/json; charset=UTF-8",
		//data: JSON.stringify(idFaq),
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {		
			
			if (res.success) {
				jAlert('등록 되었습니다.','',function(){
					
					$("#contents_wrap").empty();
					$(window).off("resize");
					
					$.ajax({
						url: rootPath + "html/board/board_main_notice_list.html",
						success: function(result) {			
							$("#contents_wrap").html(result);
						},
						error: function(request, status, error) {
							jAlert("다시 확인해주시기 바랍니다."+error);
						}
					});
					
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
				location.href=rootPath;
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
			}
		}
	});

}

//첨부파일 클릭
function attachFile() {
    $('*[name=attachFile]').click();
}

function attachFileOk() {

    var file = $("input[name=attachFile]")[0].files[0];    
    var maxSize  = 5 * 1024 * 1024; // 5MB
    var fileSize = 0;
    var browser = navigator.appName;

    fileSize = file.size;
    
	var reg = /(.*?)\.(jpg|jpeg|png|gif|bmp|tif)$/;
	
    if (fileSize > maxSize) {
    	jAlert('파일은 ' + (maxSize / 1024 / 1024) + 'MB 이하의 파일만 첨부 가능합니다','');
    	return false;
    }else{
    	
    	if(!file.name.match(reg)){
    		jAlert('이미지 파일만 업로드 가능합니다.','');
        	return false;
    	} else {
        	jAlert('파일이 첨부 되었습니다.','');
        	
        	var reader = new FileReader();

        	reader.onload = function (e) {
        		$('#blah').attr('src', e.target.result);
        	}

        	reader.readAsDataURL(file);
    	}
    }

}

