// 전역으로 처리 예정
var userInfo = {
		loginType: getCookie('loginType'),
		loginId: getCookie('loginId'),
		userId : getCookie('userId')
  	};

var oEditors;
var boardBbsIdList;
var boardWorkCodeList;
var model = {
		write: {
			bbsId: '',
			workCode: '000',
			subject: '',
			contentsType: '0',
			contents: '',
			commCode: '',
		}
	};

var bbsList;
var tempCodeList;
var windowCodeList;
var commCode;

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
	
	initEditor();
    initCode();
});

function initCode() {
	
	var formData = new FormData();
	formData.append('grpCommCode', '020');	
	
	//blockUi 호출
	fncOnBlockUI();
	
    var url = rootPath + 'code/getCodeList.do';
	
    $.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		async:false,
		//contentType: "application/json; charset=UTF-8",
		//data: JSON.stringify(params),
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {		
			
			if (res.success) {
				boardBbsIdList = res['data'];
				
				if (isNull(model.write.commCode)) {
					model.write.commCode = res['data'][0].commCode;					
				}
				
				var tempBbsId = '';
				$.each(res['data'], function(i, row) {							
					if (row.commCode == model.write.commCode) {
						tempBbsId = row.refCode;
					}
				});
				model.write.bbsId = tempBbsId;
				
				//분류값
				var writeCommCodeHtml = "<option value=''>분류</option>";
				for(var idx = 0; idx < boardBbsIdList.length; idx++){
					var row = boardBbsIdList[idx];
					
					if(model.write.commCode == row.commCode){
						writeCommCodeHtml += "<option value=\""+row.commCode+"\" selected >"+ row.commName +"</option>";
					}else{
						writeCommCodeHtml += "<option value=\""+row.commCode+"\">"+ row.commName +"</option>";
					}
				}
				$("#model_write_commCode").html(writeCommCodeHtml);
				
				//사용자명
				$("#userName").html(userInfo.loginName);
				
				//두번째 분류 검색
				getBoardCodeList();
				
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

//분류선택
function getBoardCodeList() {
	
	//초기화	
	if(isNull($("#model_write_commCode").val())){
		model.write.bbsId = '';
		$("#model_write_workCode").html("<option value=''></option>");
	}else{
		$("#model_write_workCode").html('');
	}
	
	var formData = new FormData();
	formData.append('grpCommCode', '021');	
	
	//blockUi 호출
	fncOnBlockUI();
	
    var url = rootPath + 'code/getCodeList.do';
	
    $.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		async:false,
		//contentType: "application/json; charset=UTF-8",
		//data: JSON.stringify(params),
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {		
			
			if (res.success) {								
				var tempBoardWorkCodeList = [];
				model.write.commCode = $("#model_write_commCode").val();
			    var paramCode = model.write.commCode;
				
			    //model.write.bbsId 셋팅			    
			    for(var idx = 0; idx < boardBbsIdList.length; idx++){
					var row = boardBbsIdList[idx];
					
					if(model.write.commCode == row.commCode){
						model.write.bbsId = row.refCode;
					}
				}

			    //세컨드 분류
			    $.each(res['data'], function(i, row) {			    	
			        if (row.refCode == paramCode) {
			        	tempBoardWorkCodeList.push(row);
			        }
			    });
				
			    boardWorkCodeList = tempBoardWorkCodeList;
			    if (isNull(model.write.workCode)) {
			    	model.write.workCode = boardWorkCodeList[0].commCode;
			    }
			    			    
			    if(boardWorkCodeList != null && boardWorkCodeList.length > 0){
			    	
			    	var writeWorkCodeHtml = "";
			    	for(var idx = 0; idx < boardWorkCodeList.length; idx++){
			    		var row = boardWorkCodeList[idx];
			    		writeWorkCodeHtml += "<option value='"+row.commCode+"'>"+ row.commName +"</option>";
			    	}
			    	$("#model_write_workCode").html(writeWorkCodeHtml);
				}else{
					$("#model_write_workCode").html("<option value=''></option>");
				}
			    
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

//저장처리
function doWrite() {

    model.write.subject = $("#model_write_subject").val();
    model.write.contents = CKEDITOR.instances['editor'].getData();
    model.write.workCode = $("#model_write_workCode").val();
    
    if (isNull(model.write.bbsId)) {
    	jAlert('분류를 선택 하세요.','');
    	return false;
    }

    if (isNull(model.write.subject)) {
    	jAlert('제목을 입력 하세요.','');
    	return false;
    }

    if (isNull(model.write.contents)) {
    	jAlert('내용을 입력 하세요.','');
    	return false;
    }

    var formData = new FormData();
    formData.append('bbsId', model.write.bbsId);
    formData.append('workCode', model.write.workCode);
    formData.append('subject', model.write.subject);
    formData.append('contentstype', model.write.contentsType);
    formData.append('contents', model.write.contents);
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
						url: rootPath + "html/board/board_question_list.html",
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

//첨부파일
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

//에디터 생성
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