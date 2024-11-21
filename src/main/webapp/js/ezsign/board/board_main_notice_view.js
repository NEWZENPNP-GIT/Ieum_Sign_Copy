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

$(document).ready(function() {	
	
	//CSS 파일 로드
	
	if(isNotNull(getURLParameters("paramTempPage"))){
		paramTempPage = getURLParameters("paramTempPage");
	}
	
	getDetail();
});

// 상세조회
function getDetail() {
	
	var formData = new FormData();
    formData.append('bbsId', '202005130548001');
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
				
				
				$("#model_detail_subject").html(model.detail.subject);
				$("#model_detail_empName").html(model.detail.empName);
				$("#model_detail_expireDate").val(convertDateTime(model.detail.expireDate).format("yyyy-MM-dd"));
				/*$("input[name=showYn][value="+model.detail.showYn+"]").attr("checked", true);*/
				
				var fileListHtml = "";
				if(model.fileList != null){
					for(var idx = 0 ; idx < model.fileList.length ; idx++){
						var row = model.fileList[idx];
						console.log(row);
						//fileListHtml += "<span class='fileName'>"+ row.orgFileName +"</span><a class='btn_type btn_download' onClick='doDownload(\""+row.fileNo+"\")'>다운로드</a><a class='btn_type btn_download' onClick='fn_delFile(\""+row.fileNo+"\")'>삭제</a>";

						$("#attachFile").attr("src", rootPath+"/temp/file/"+row.filePath+row.fileName);
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
    formData.append('bbsId', '202005130548001');
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
		url: rootPath + "html/board/board_main_notice_list.html",
		success: function(result) {			
			setCookie("paramBbsNo", "");
			
			$("#contents_wrap").html(result);
		},
		error: function(request, status, error) {
			jAlert("다시 확인해주시기 바랍니다."+error);
		}
	});
}

function fn_save(){

    var formData = new FormData();
    formData.append('bbsId', '202005130548001');
    formData.append('bbsNo', getCookie("paramBbsNo"));
    formData.append('expireDate', $("#model_detail_expireDate").val());
    /*formData.append('showYn', $("input[name=showYn]:checked").val());*/
    
	fncOnBlockUI();
    
    var url = rootPath + 'bbs/updBbsContents.do';
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {		
			
			if (res.success) {
				jAlert('수정 되었습니다.','',function(){
					
					$("#contents_wrap").empty();
					$(window).off("resize");
					
					$.ajax({
						url: rootPath + "html/board/board_main_notice_view.html",
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
