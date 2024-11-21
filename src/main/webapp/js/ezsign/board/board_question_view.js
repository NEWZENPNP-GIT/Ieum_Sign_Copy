//전역으로 처리 예정
var userInfo = {
		loginType: getCookie('loginType'),
		loginId: getCookie('loginId'),
		userId : getCookie('userId'),
		loginName : getCookie('loginName'),
		bizName : getCookie('bizName')
  	};
  
var model = {
		detail: {},
		fileList: [],
		comment: [],
		write: {
			bbsId: getCookie('paramBbsId'),
			bbsNo: getCookie('paramBbsNo'),
			workCode: '',
			subject: '',
			contentsType: '',
			contents: '',
			hitCnt: '0'
		},
		writeComment: {
			commentContents: ''
		},
		modifyComment: {
			commentContents: ''
		},
		currPage: 1,
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
	
	//상세
	getDetail();
		
});


//상세 조회 
function getDetail() {
	
	var formData = new FormData();
	formData.append('bbsId', getCookie('paramBbsId'));	
	formData.append('bbsNo', getCookie('paramBbsNo'));
	
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
		//data: JSON.stringify(params),
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {		
			
			if (res.success) {
				model.detail = res['data'];
			    model.fileList = res['file'];
			    model.write = res['data'];
			    
			    var tmpList = [];
			    $.each(res['comment'], function(i, v) {
			    	v.tmpCommentContents = v.commentContents;
			    	v.commentContents = v.commentContents.replace(/\n/g, '<br />');
			    	
			    	tmpList.push(v);
			    });
			    model.comment = tmpList;
			      
			    				
				//버튼생성
				var btnGroupHtml = "";
				if(userInfo.loginType >= model.detail['userType']){
					btnGroupHtml += "<a class='btn_type' onClick='doRemove()'>삭제</a>";
				}
				btnGroupHtml += "<a class='btn_type' onClick='goList()'>목록</a>";
				$("#btnGroupDiv").html(btnGroupHtml);
				
				//상세내용
				$("#model_detail_bizName").html(model.detail.bizName);
				$("#model_detail_companyTelNum").html(model.detail.companyTelNum);
				$("#model_detail_workName").html(model.detail.workName);
				$("#model_detail_subject").html(model.detail.subject);
				$("#model_detail_empName").html(model.detail.empName);
				$("#model_detail_contents").html(model.detail.contents);
				
				var fileListHtml = "";
				if(model.fileList != null){
					for(var idx = 0 ; idx < model.fileList.length ; idx++){
						var row = model.fileList[idx];
						
						fileListHtml += "<div>";
						fileListHtml += "<span class='fileName'>"+ row.orgFileName +"</span><a class='btn_type btn_download' onClick='doDownload(\""+row.fileNo+"\")'>다운로드</a>";
						fileListHtml += "</div>";
					}
				}				
				$("#fileListTd").html(fileListHtml);

				//댓글창
				var commentListHtml = "";
				
				if(model.comment != null){
					
					for(var idx = 0 ; idx < model.comment.length ; idx++){
						var row = model.comment[idx];
						
						commentListHtml +="<li>";
						commentListHtml +="<div class='area head_group'><div class='company'>"+row['bizName']+"</div><div class='userName'>"+row['empName']+"</div><div class='date'>"+moment(row.updDate,"YYYYMMDDHHmmss").format('YYYY-MM-DD HH:mm:ss')+"</div></div>";    
						commentListHtml +="<div class='area text_group' id='commentContestsDiv_"+idx+"'>";
						
						if(row.isEditMode){
							commentListHtml +="<textarea rows='' cols='' id='row_tmpCommentContents_"+idx+"' style='resize:none;'>"+row.tmpCommentContents+"</textarea>";
						}else{
							commentListHtml +="<div class='NanumGothic'>"+row.commentContents+"</div>";
						}
						
						commentListHtml +="</div>";
						
						if(userInfo.loginType >= row.userType){
							commentListHtml +="<div class='area right_btngroup' id='commentBtngroupDiv_"+idx+"'>";
							
							if(row.isEditMode){
								commentListHtml +="<a class='btn_type btn_del NanumGothic' onClick='fncIsEditModeFalse("+idx+");'>취소</a>";
								commentListHtml +="<a class='btn_type btn_edit NanumGothic' onClick='doModifyComment(\""+idx+"\",\""+row.bbsId+"\", \""+row.bbsNo+"\", \""+row.commNo+"\")'>저장</a>";
							}else{
								commentListHtml +="<a class='btn_type btn_del NanumGothic' onClick='doRemoveComment(\""+row.bbsId+"\", \""+row.bbsNo+"\", \""+row.commNo+"\")'>삭제</a>";
								commentListHtml +="<a class='btn_type btn_edit NanumGothic' onClick='fncIsEditModeTrue("+idx+");'>수정</a>";
							}
							commentListHtml +="</div>";
						}						
						commentListHtml +="</li>";
					}
				}

				commentListHtml +="<li class='write'>";
				commentListHtml +="<div class='area head_group'><div class='company'>"+userInfo.bizName+"</div><div class='userName'>"+userInfo.loginName+"</div></div>";
				commentListHtml +="<div class='area text_group'>"; 
				commentListHtml +="<textarea rows='' cols='' id='model_writeComment_commentContents' style='resize:none;'></textarea>";
				commentListHtml +="</div>";
				commentListHtml +="<div class='area right_btngroup'><a class='btn_type btn_write NanumGothic' onClick='doWriteComment()'>등록</a></div>";
				commentListHtml +="</li>";
				
				$("#commentListUl").html(commentListHtml);
				
				
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

//삭제
function doRemove() {
	
    var formData = new FormData();
    formData.append('bbsId', model.write.bbsId);
    formData.append('bbsNo', getCookie("paramBbsNo"));
    formData.append('fileNo', '');

    if (model.fileList != null && model.fileList.length > 0) {
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

//파일 다운로드
function doDownload(fileNo) {
	var url = rootPath + 'bbs/getBbsFile.do';
    url += '?bbsId=' + model.detail['bbsId'];
    url += '&bbsNo=' + model.detail['bbsNo'];
    url += '&fileNo=' + fileNo;

    downloadDataUrlFromJavascript(url);
}

//목록
function goList() {
    //const url = this.activeRoute.snapshot.queryParams.retUrl !== undefined ? this.activeRoute.snapshot.queryParams.retUrl : '/board_question_admin_list/' + this.model.bbsList[0].bbsId + '/1';
    //this.router.navigate([url]);
	
	$("#contents_wrap").empty();
	$(window).off("resize");
	
	$.ajax({
		url: rootPath + "html/board/board_question_list.html",
		success: function(result) {						
			setCookie("paramBbsId", "");
			setCookie("paramBbsNo", "");
			
			$("#contents_wrap").html(result);
		},
		error: function(request, status, error) {
			jAlert("다시 확인해주시기 바랍니다."+error);
		}
	});
}

function fncIsEditModeFalse(idx){	
	$("#commentContestsDiv_"+idx).html("<div class='NanumGothic'>"+model.comment[idx].commentContents+"</div>");
	
	var commentBtngroupHtml = "";
	commentBtngroupHtml +="<a class='btn_type btn_del NanumGothic' onClick='doRemoveComment(\""+model.comment[idx].bbsId+"\", \""+model.comment[idx].bbsNo+"\", \""+model.comment[idx].commNo+"\")'>삭제</a>";
	commentBtngroupHtml +="<a class='btn_type btn_edit NanumGothic' onClick='fncIsEditModeTrue("+idx+");'>수정</a>";	
	$("#commentBtngroupDiv_"+idx).html(commentBtngroupHtml);
}

function fncIsEditModeTrue(idx){		
	$("#commentContestsDiv_"+idx).html("<textarea rows='' cols='' id='row_tmpCommentContents_"+idx+"' style='resize:none;'>"+model.comment[idx].tmpCommentContents+"</textarea>");
	
	var commentBtngroupHtml = "";
	commentBtngroupHtml +="<a class='btn_type btn_del NanumGothic' onClick='fncIsEditModeFalse("+idx+");'>취소</a>";
	commentBtngroupHtml +="<a class='btn_type btn_edit NanumGothic' onClick='doModifyComment(\""+idx+"\",\""+model.comment[idx].bbsId+"\", \""+model.comment[idx].bbsNo+"\", \""+model.comment[idx].commNo+"\")'>저장</a>";	
	$("#commentBtngroupDiv_"+idx).html(commentBtngroupHtml);
}

//댓글 등록
function doWriteComment() {
	
	if($.trim($("#model_writeComment_commentContents").val()) == ''){
		jAlert("댓글을 입력해주십시오.","",function(){
			$("#model_writeComment_commentContents").focus();			
		});		
		return false;
	}
	
	model.writeComment.commentContents = $("#model_writeComment_commentContents").val();
	
    var formData = new FormData();
    formData.append('bbsId', model.write.bbsId);
    formData.append('bbsNo', model.write.bbsNo);
    formData.append('userId', userInfo.loginId);
    formData.append('commentContents', model.writeComment.commentContents);

    //blockUi 호출
	fncOnBlockUI();
    
    var url = rootPath + 'bbs/insBbsComment.do';
	
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
					getDetail();
				});		          
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

//댓글 수정
function doModifyComment(idx, bbsId, bbsNo, commNo) {
	
    var formData = new FormData();
    formData.append('bbsId', bbsId);
    formData.append('bbsNo', bbsNo);
    formData.append('commNo', commNo);
    formData.append('commentContents', $("#row_tmpCommentContents_"+idx).val());
    
    //blockUi 호출
	fncOnBlockUI();
    
    var url = rootPath + 'bbs/updBbsComment.do';
	
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
					getDetail();
				});		          
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

//댓글 삭제
function doRemoveComment(bbsId, bbsNo, commNo) {

	jConfirm('삭제 하시겠습니까?','',function(cfrm){
		if(cfrm){
		
			var formData = new FormData();
		    formData.append('bbsId', bbsId);
		    formData.append('bbsNo', bbsNo);
		    formData.append('commNo', commNo);
		    
		    //blockUi 호출
			fncOnBlockUI();
		    
		    var url = rootPath + 'bbs/delBbsComment.do';
			
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
						jAlert('삭제 되었습니다.','',function(){
							getDetail();
						});		          
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

}

