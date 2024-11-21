// 전역으로 처리 예정
var userInfo = {
		loginType: getCookie('loginType'),
		loginId: getCookie('loginId'),
		userId : getCookie('userId')
  	};

var model = {
		workList: [
			{ workCode: '001', workCodeName: '인사관리' },
			{ workCode: '002', workCodeName: '전자문서' },
			{ workCode: '003', workCodeName: '연차관리' },
			{ workCode: '004', workCodeName: '급여명세' },
			{ workCode: '005', workCodeName: '캔디모바일' },
			{ workCode: '006', workCodeName: '포인트결제' },
		],
		list: [],
		totalCount: 0,
		perPage: 10,
		tempPage: 1,
		currPage: 1,
		search: {
			bbsId: '180709094128003',
			startPage: 0,
			endPage: 0,
			searchKey: 'subject',
			searchValue: '',
			workCode: '001',
		}
    };

// faq 작성종류
var faqMode = '';
var faqbbsNo = '';

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
	
	// 검색시
	$('#model_search_searchValue').keydown(function(e){
		//enter 일경우
	    if(e.keyCode == 13){
	    	getSearch();
	    }
	});
	
	//버튼
	if(userInfo.loginType == '9'){
		$("#faq_list").addClass("admin_mode");		
		$("#btnGroupDiv").html("<a class='btn_type' onClick='goWrite()'>추가하기</a>");
	}
	
	var paramWorkCode = getCookie('paramWorkCode');
	if(isNotNull(paramWorkCode)){
		model.search.workCode = paramWorkCode;		
		setCookie("paramWorkCode", "");
	}
	
	//탭 타이틀 
	var workTabTitleHtml = "";
	for(var idx = 0 ; idx < model.workList.length ; idx++){
		row = model.workList[idx];
		//[ngClass]="{'active': model.search.workCode == row.workCode}"
		
		if(row.workCode == model.search.workCode){
			workTabTitleHtml += "<li><a class='btn_type active' id='TAB_"+row.workCode+"' onClick='goTab(\""+row.workCode+"\")'>"+ row.workCodeName +"</a></li>";
		}else{
			workTabTitleHtml += "<li><a class='btn_type' id='TAB_"+row.workCode+"' onClick='goTab(\""+row.workCode+"\")'>"+ row.workCodeName +"</a></li>";
		}

	}	
	$("#workTabTitle").html(workTabTitleHtml);
	
	//리스트 조회
	getList();	
});

//리스트 조회
function getList(){
	
	model.search.startPage = (model.currPage - 1) * 10;
    model.search.endPage = model.perPage;

    var formData = new FormData();
	formData.append('bbsId', model.search.bbsId);
	formData.append('searchKey', model.search.searchKey);
	formData.append('searchValue', model.search.searchValue);	
	formData.append('startPage', model.search.startPage);
	formData.append('endPage', model.search.endPage);
	formData.append('workCode', model.search.workCode);
    
    //var params = model.search;
    
	//blockUi 호출
	fncOnBlockUI();
	
    var url = rootPath + 'bbs/getBbsContentsList.do';
	
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
				model.totalCount = res['total'];				
				model.currPage = model.tempPage;
				
				var tmpList = [];
				$.each(res['data'], function(i, v) {
					v.contents = v.contents.replace(/\n/g, '<br />');
			        tmpList.push(v);
				});
				model.list = tmpList;
				
				//리스트 작성
				var faqListHtml = "";
				
				faqListHtml +="";
				if(model.list != null && model.list.length > 0){
			        for(var idx = 0 ; idx < model.list.length ; idx++){
			        	var row = model.list[idx];
			        	
			        	faqListHtml +="<li>";
			        	faqListHtml +="<input id='item-tit-"+idx+"' type='checkbox' class='hidden' />";
			        	faqListHtml +="<div class='item-top'>";
			        	faqListHtml +="<div class='tabName NanumGothic'>Q</div>";
			        	faqListHtml +="<label class='question NanumGothic' for='item-tit-"+idx+"'>"+row.subject+"</label><span class='hot'></span>";
			        	faqListHtml +="<label class='btn_type Material_icons icon' for='item-tit-"+idx+"'></label>";
			        	faqListHtml +="</div>";
			        	faqListHtml +="<div class='item-con'>";
			        	faqListHtml +="<div class='answer NanumGothic'>"+row.contents+"</div>";
			        	if(userInfo.loginType == 9){
				        	faqListHtml +="<div class='admin_btns'>";
				        	faqListHtml +="<a class='btn_type btn_del NanumGothic' onClick='doRemove(\""+row.bbsNo+"\")'>삭제</a>";
				        	faqListHtml +="<a class='btn_type btn_edit NanumGothic' onClick='goModify(\""+row.bbsNo+"\")'>수정</a>";
				        	faqListHtml +="</div>";
			        	}
			        	faqListHtml +="</div>";
			        	faqListHtml +="</li>";
			        	
			        }		
				}else{
					faqListHtml +="<li>";
					faqListHtml +="<div class='answer' style='text-align:center;'>";
					faqListHtml +="조회 결과가 없습니다.";
					faqListHtml +="</div>";
					faqListHtml +="</li>";
				}
				$("#model_list").html(faqListHtml);
										
				//페이징 처리 
				var paginateOption = {
						total: Math.ceil(model.totalCount / model.perPage),
						page: model.currPage
					};  
					
				if (model.totalCount > 0) {
					pageSelection(paginateOption);
				} else {
					$('#page-selection').hide();
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

//탭이동
function goTab(workCode) {
	
	for(var idx = 0 ; idx < model.workList.length ; idx++){
		row = model.workList[idx];	
		
		if(row.workCode != workCode){		
			$("#TAB_"+row.workCode).removeClass("active");
		}
	}
	$("#TAB_"+workCode).addClass("active");
	
	$("#model_search_searchValue").val('');
	model.search.searchValue = '';
	
    model.search.workCode = workCode;
    model.currPage = 1;
    getList();
}

//검색
function getSearch() {
	model.tempPage = 1;
    model.currPage = 1;
        
    model.search.searchValue = $.trim($("#model_search_searchValue").val());
    
    getList();
}

//추가하기
function goWrite() {
	//this.boardFaqWritePopup.open('write', this.model.search.workCode, '');
	
	faqMode = 'write';
	openWin (rootPath + "/html/board/board_faq_writePopup.html","faqWritePop","540","510");
	
}

//수정
function goModify(bbsNo) {
    //this.boardFaqWritePopup.open('modify', this.model.search.workCode, bbsNo);
	
	faqMode = 'modify';
	faqbbsNo = bbsNo;
	openWin (rootPath + "/html/board/board_faq_writePopup.html","faqWritePop","540","510");
}


//삭제하기
function doRemove(bbsNo) {
	
    var formData = new FormData();
    formData.append('bbsId', model.search.bbsId);
    formData.append('bbsNo', bbsNo);
    formData.append('fileNo', '');

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
    						model.tempPage = 1;
    					    model.currPage = 1;
    					    
    						getList();
    					});
    					
    					$.unblockUI();
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


//페이징 관련 
function pageSelection(opt) {
 
    var settings = $.extend({}, {
      total: 0,
      page: model.currPage,
      maxVisible: 5,
      leaps: true,
      firstLastUse: true,
      first: '',
      last: '',
      wrapClass: 'pagination',
      activeClass: 'active',
      disabledClass: 'disabled',
      nextClass: 'btn_next',
      prevClass: 'btn_prev',
      lastClass: 'end_page',
      firstClass: 'first_page'
    }, opt);

    $('#page-selection').show().bootpag(settings).off('page').on('page', function(event, num) {
    	model.tempPage = num;
    	onChangeperPage();
    });
    $('.grid_paging .select_box select').val(model.perPage);
}


function onChangeperPage() {
	//perPage = $("#perPage").val();
	
	model.search.startPage = (model.tempPage - 1 ) * model.perPage;
    if (model.perPage > model.totalCount) {
    	model.search.startPage = 0;
    }
    model.search.endPage = model.perPage;
    model.currPage =  model.tempPage;

    getList();
}

