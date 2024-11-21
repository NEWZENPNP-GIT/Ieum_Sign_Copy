// 전역으로 처리 예정
var userInfo = {
		loginType: getCookie('loginType'),
		loginId: getCookie('loginId'),
		userId : getCookie('userId')
  	};

var oEditors;
var model = {
		write: {
			bbsId: '211200000000001',
			bbsNo: getCookie('paramBbsNo'),
			workCode: '',
			subject: '',
			contentsType: '',
			contents: '',
		}
  	};

//CSS 파일 로드
var loadCssFile = function(){
	var yearTaxCommonCss = document.createElement( "link" );
	yearTaxCommonCss.href = rootPath + "css/yearTax_common.css";
	yearTaxCommonCss.type = "text/css";
	yearTaxCommonCss.rel = "stylesheet";
	yearTaxCommonCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( yearTaxCommonCss );
	
	var boardNoticeCss = document.createElement( "link" );
	boardNoticeCss.href = rootPath + "css/board_notice.css";
	boardNoticeCss.type = "text/css";
	boardNoticeCss.rel = "stylesheet";
	boardNoticeCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( boardNoticeCss );
} 

$(document).ready(function() {		

	//CSS 파일 로드
	loadCssFile();
	
	$("#userName").html(userInfo.loginName);
	getDetail();

});

function getDetail() {
	
    var formData = new FormData();
    formData.append('bbsId', '211200000000001');
    formData.append('bbsNo', getCookie("paramBbsNo"));

    //blockUi 호출
	fncOnBlockUI();
    
    var url = rootPath + 'bbs/getBbsContents.do';
	
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
				var tmpData = res['data'];
				
				if(tmpData.contentsType == '1'){
					$("input:checkbox[id='model_write_contentsType']").prop("checked", true);
				}
			    model.write = tmpData;
				
			    $("#model_write_subject").val(model.write.subject);
			    $("#editor").val(model.write.contents);
			    
			    
			    //에디터 활성화
				initEditor();
				
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

// 수정
function doWrite() {
    
	model.write.subject = $("#model_write_subject").val();
    model.write.contents = CKEDITOR.instances['editor'].getData();

    if($("input:checkbox[id='model_write_contentsType']").is(":checked")){
    	model.write.contentsType = '1';
    }else{
    	model.write.contentsType = '0';
    }

    var formData = new FormData();
    formData.append('bbsId', model.write.bbsId);
    formData.append('bbsNo', model.write.bbsNo);
    formData.append('workCode', model.write.workCode);
    formData.append('contentsType', model.write.contentsType);
    formData.append('contents', model.write.contents);
    formData.append('subject', model.write.subject);

    var file = document.getElementsByName('attachFile');
    for (var i = 0; i < file.length; i++) {
    	if (file[i]['files'][0] != undefined && file[i]['files'][0] != null) {
    		if (checkSpecial(file[i]['files'][0].name)) {
    			jAlert('파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.');
    			return false;
    		}
    		formData.append('addFile' + i, file[i]['files'][0]);
    	}
    }
    
    //blockUi 호출
	fncOnBlockUI();
    
    var url = rootPath + 'bbs/updBbsContents.do';
	
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
				jAlert('수정 되었습니다.','',function(){
					
					$("#contents_wrap").empty();
					$(window).off("resize");
					
					$.ajax({
						url: rootPath + "html/board/board_candycash_notice_view.html",
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

    // if (browser === 'Microsoft Internet Explorer') {
    //  const oas = new ActiveXObject('Scripting.FileSystemObject');
    //  fileSize = oas.getFile(file.value).size;
    // } else {
    //  fileSize = file.files[0].size;
    // }

    if (fileSize > maxSize) {
    	jAlert('파일은 ' + (maxSize / 1024 / 1024) + 'MB 이하의 파일만 첨부 가능합니다','');
    	return false;
    }else{
    	jAlert('파일이 첨부 되었습니다.','');
    }

}

// 에디터 활성화
function initEditor() {

    var ckeditor = CKEDITOR.replace('editor', {
    	filebrowserImageUploadUrl: rootPath + '/bbs/insBbsImage.do',
    	// enterMode: CKEDITOR.ENTER_BR,
    	// height: 450
    });

    // 파일업로드 request
    ckeditor.on('fileUploadRequest', function(evt) {
    	var fileLoader = evt.data.fileLoader;

    	// 파일명 특수문자 체크
    	if (checkSpecial(fileLoader.file.name)) {
    		jAlert('파일명에 특수문자가 포함되어 있습니다. 제거하시고 다시 등록해주시기 바랍니다.','');
    		return false;
    	}

    	// 파일 용량 제한
    	var maxSize  = 5 * 1024 * 1024; // 5MB
    	var fileSize = fileLoader.file.size;

    	if (fileSize > maxSize) {
    		jAlert('파일은 ' + (maxSize / 1024 / 1024) + 'MB 이하의 파일만 첨부 가능합니다','');
    		return false;
    	}

    	// 전송
    	var formData = new FormData();
    	var xhr = fileLoader.xhr;

    	xhr.open('POST', fileLoader.uploadUrl, true);
    	formData.append('addImageFile0', fileLoader.file, fileLoader.fileName);
    	fileLoader.xhr.send(formData);

    	evt.stop();
    }, null, null, 4);

    // 파일 업로드 response
    ckeditor.on('fileUploadResponse', function (evt) {
    	evt.stop();

    	var data = evt.data;
    	var xhr = data.fileLoader.xhr;
    	var response = xhr.responseText.split('|');

    	if (response[1]) {
    		// An error occurred during upload.
    		data.message = response[1];
    		evt.cancel();
    	} else {
    		var res = JSON.parse(response[0]);
    		var imgUrl = '';
    		if (res['success']) {
    			imgUrl = rootPath + res['data'][0]['fileStrePath'] + res['data'][0]['fileStreNm'];
    		} else {
    			jAlert('오류가 발생했습니다.','');
    			return false;
    		}

    		// 이미지 테스트
    		// imgUrl = 'https://ckeditor.com/docs/ckeditor5/latest/assets/img/umbrellas.jpg';

    		data.url = imgUrl;
    	}
    });

}