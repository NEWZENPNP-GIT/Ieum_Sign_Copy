var model = {
	isShow: false,
    mode: '',
    workList: [
    	{ workCode: '001', workCodeName: '인사관리' },
    	{ workCode: '002', workCodeName: '전자문서' },
    	{ workCode: '003', workCodeName: '연차관리' },
    	{ workCode: '004', workCodeName: '급여명세' },
    	{ workCode: '005', workCodeName: '캔디모바일' },
    	{ workCode: '006', workCodeName: '포인트결제' },
    ],
    write: {
    	bbsId: '180709094128003',
    	bbsNo: '',
    	workCode: '',
    	subject: '',
    	contentstype: '0',
    	contents: '',
    }
 };

//CSS 파일 로드
var loadCssFile = function(){
	var fontPcCss = document.createElement( "link" );
	fontPcCss.href = rootPath + "css/font_pc.css";
	fontPcCss.type = "text/css";
	fontPcCss.rel = "stylesheet";
	fontPcCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( fontPcCss );
	
	var jqueryAlertsCss = document.createElement( "link" );
	jqueryAlertsCss.href = rootPath + "css/jquery.alerts.css";
	jqueryAlertsCss.type = "text/css";
	jqueryAlertsCss.rel = "stylesheet";
	jqueryAlertsCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( jqueryAlertsCss );
	
	var jqueryUiMinCss = document.createElement( "link" );
	jqueryUiMinCss.href = rootPath + "css/jquery-ui.min.css";
	jqueryUiMinCss.type = "text/css";
	jqueryUiMinCss.rel = "stylesheet";
	jqueryUiMinCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( jqueryUiMinCss );
	
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
	
	var boardFaqWritePopupCss = document.createElement( "link" );
	boardFaqWritePopupCss.href = rootPath + "css/ezsign/board/board_faq_writePopup.css";
	boardFaqWritePopupCss.type = "text/css";
	boardFaqWritePopupCss.rel = "stylesheet";
	boardFaqWritePopupCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( boardFaqWritePopupCss );
} 

$(document).ready(function() {

	//CSS 파일 로드
	loadCssFile();
	
	model.mode = opener.faqMode;
	model.write.workCode = opener.model.search.workCode;

	var strSelected = "";
	var workListHtml = "<option value=''>분류</option>";
	for(var idx = 0 ; idx < model.workList.length ; idx++){
		var row = model.workList[idx];
		
		if(row.workCode == model.write.workCode){
			strSelected = "selected";
		}else{
			strSelected = "";
		}		
		workListHtml += "<option value='"+row.workCode+"' "+strSelected+" >"+ row.workCodeName +"</option>";
	}
	$("#model_write_workCode").html(workListHtml);
	    
	
	if (model.mode == 'write') {
		//model.isShow = true;
	} else if (model.mode == 'modify') {
		
		var formData = new FormData();
	    formData.append('bbsId', model.write.bbsId);
	    formData.append('bbsNo', opener.faqbbsNo);

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
					
					//model.isShow = true;
					model.write = res['data'];
			        

					//$("#model_write_workCode").val(model.write.workCode);
					$("#model_write_subject").val(model.write.subject);
					$("#model_write_contents").val(model.write.contents);
					
					
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
});

//작성완료
function doWrite(){

	model.write.workCode = $("#model_write_workCode").val();
	model.write.subject = $("#model_write_subject").val();
	model.write.contents = $("#model_write_contents").val();
	
    if (model.write.workCode == '') {
    	jAlert('분류를 선택 하세요.','');
    	return false;
    }

    if (model.write.subject == '') {
    	jAlert('제목을 입력 하세요.','');
    	return false;
    }

    if (model.write.contents == '') {
    	jAlert('내용을 입력 하세요.','');
    	return false;
    }

    var formData = new FormData();

    $.each(model.write, function(k, v) {
    	if (k == 'bbsNo' && model.mode == 'modify') {
    		formData.append(k, v);
    	} else {
    		formData.append(k, v);
    	}
    });

    //blockUi 호출
	fncOnBlockUI();
    
    var url = '';
    if(model.mode == 'write'){
    	formData.append('hitCnt', '0');
    	url = rootPath + 'bbs/insBbsContents.do';
    }else if(model.mode == 'modify'){
    	url = rootPath + 'bbs/updBbsContents.do';
    }
    	
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

					$("#model_search_searchValue",opener.document).val('');
					//opener.getList();
					opener.getSearch();
					
					winClose();
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

// 팝업을 닫는다.
function winClose(){
	opener.faqMode = '';
	opener.faqbbsNo = '';
	
	self.close();
}
