// 전역으로 처리 예정
var userInfo = {
		loginType: getCookie('loginType'),
		loginId: getCookie('loginId'),
		userId : getCookie('userId')
  	};

var model = {
		detail: {},
		fileList: [],
  	};

var paramTempPage = '';

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
	
	if(isNotNull(getURLParameters("paramTempPage"))){
		paramTempPage = getURLParameters("paramTempPage");
	}
	
	getDetail();
});

// 상세조회
function getDetail() {
	
	var formData = new FormData();
    formData.append('bbsId', '180706135108002');
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
				model.detail = res['data'];
				model.fileList = res['file'];
			    
				//버튼생성
				var btnGroupDivHtml = "";
				if((userInfo.loginType > 8 && model.detail != null && userInfo.loginType >= model.detail['userType']) || (model.detail != null && userInfo.userId == model.detail['userId'])){
					//$("#btnGroupDiv").html("<a class='btn_type Material_icons btn_write' onClick='fncBoardWrite();'>글쓰기</a>");
					btnGroupDivHtml += "<a class='btn_type' onClick='doRemove()'>삭제</a>";
					btnGroupDivHtml += "<a class='btn_type' onClick='goModify()'>수정</a>";
					
				}
				btnGroupDivHtml += "<a class='btn_type' onClick='goList()'>목록</a>";
				$("#btnGroupDiv").html(btnGroupDivHtml);
				
				
				$("#model_detail_subject").html(model.detail.subject);
				$("#model_detail_empName").html(model.detail.empName);
				$("#model_detail_contents").html(model.detail.contents);
				
				var fileListHtml = "";
				if(model.fileList != null){
					for(var idx = 0 ; idx < model.fileList.length ; idx++){
						var row = model.fileList[idx];
						fileListHtml += "<span class='fileName'>"+ row.orgFileName +"</span><a class='btn_type btn_download' onClick='doDownload(\""+row.fileNo+"\")'>다운로드</a>";					
					}
				}
				$("#model_fileList").html(fileListHtml);
				
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

//파일 다운로드
function doDownload(fileNo) {
	var url = rootPath + 'bbs/getBbsFile.do';
    url += '?bbsId=' + model.detail['bbsId'];
    url += '&bbsNo=' + model.detail['bbsNo'];
    url += '&fileNo=' + fileNo;

    downloadDataUrlFromJavascript(url);
}

//삭제
function doRemove() {
	
    var formData = new FormData();
    formData.append('bbsId', '180706135108002');
    formData.append('bbsNo', getCookie("paramBbsNo"));
    formData.append('fileNo', '');

    if (model.fileList != null) {
    	formData.append('fileNo', model.fileList[0].fileNo);
    } else {
    	formData.append('fileNo', '');
    }

    jConfirm('삭제 하시겠습니까?','',function(cfrm){
    	
    	if(cfrm){
    		//blockUi 호출
    		fncOnBlockUI();
    	    
    	    var url = rootPath + 'bbs/delBbsContents.do';
    		
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
    					jAlert(res['message'],'',function(){
    						goList();
    					});
    				}else{
    					$.unblockUI();
    					//jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
    					jAlert(res['message'],'');
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
    
}

//목록으로 이동
function goList() {
	
	$("#contents_wrap").empty();
	$(window).off("resize");
	
	$.ajax({
		url: rootPath + "html/board/board_notice_list.html",
		success: function(result) {			
			setCookie("paramBbsNo", "");
			
			$("#contents_wrap").html(result);
		},
		error: function(request, status, error) {
			jAlert("다시 확인해주시기 바랍니다."+error);
		}
	});
	
	
    //const url = this.activeRoute.snapshot.queryParams.retUrl !== undefined ? this.activeRoute.snapshot.queryParams.retUrl : '/board_notice_list/1';
    //this.router.navigate([url]);
}

//수정페이지 이동
function goModify() {
	
    //this.router.navigate(['/board_notice_modify/' + this.activeRoute.snapshot.params.bbsNo]);
	$("#contents_wrap").empty();
	$(window).off("resize");
	
	$.ajax({
		url: rootPath + "html/board/board_notice_modify.html",
		success: function(result) {						
			$("#contents_wrap").html(result);
		},
		error: function(request, status, error) {
			jAlert("다시 확인해주시기 바랍니다."+error);
		}
	});
	
}
