// 전역으로 처리 예정
var userInfo = {
		loginType: getCookie('loginType'),
		loginId: getCookie('loginId'),
		userId : getCookie('userId')
  	};

var noticeInfo;
var faqInfo;

var model = {
		noticeList: {
	      bbsId: '180706135108002',
	      startPage: 0,
	      endPage: 5,
	      searchKey: '',
	      searchValue: '',
	      searchDateFrom: '',
	      searchDateTo: ''
		},
	    faqList: {
	      bbsId: '180709094128003',
	      startPage: 0,
	      endPage: 5,
	      searchKey: '',
	      searchValue: '',
	      searchDateFrom: '',
	      searchDateTo: ''
	    },
	    search: {
	      bbsId: '180709094128003',
	      startPage: 0,
	      endPage: 5,
	      searchKey: 'subject',
	      searchValue: '',
	      workCode: '',
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
	
	if(isNotNull(getCookie("paramBbsNo"))){
		setCookie("paramBbsNo", "")
	}
	
	// 검색시
	/*$('#model_search_searchValue').keydown(function(e){
		//enter 일경우
	    if(e.keyCode == 13){
	    	getFaqSearch();
	    }
	});*/
	
	getList();
    getFaq();
});


//공지사항 조회
function getList() {
  
	//var notice = model.noticeList;

	 var formData = new FormData();
	 formData.append('bbsId', model.noticeList.bbsId);
	 formData.append('startPage', model.noticeList.startPage);
	 formData.append('endPage', model.noticeList.endPage);
	 formData.append('searchKey', model.noticeList.searchKey);
	 formData.append('searchValue', model.noticeList.searchValue);
	 formData.append('searchDateFrom', model.noticeList.searchDateFrom);
	 formData.append('searchDateTo', model.noticeList.searchDateTo);
	
	//blockUi 호출
	fncOnBlockUI();
		
	var url = rootPath + 'bbs/getBbsContentsList.do';
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		async:false,
		//contentType: "application/json; charset=UTF-8",
		//data: JSON.stringify(notice),
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {		
			
			if (res.success) {
				
				noticeInfo = res['data'];
				
				var noticeInfoHtml = "";
				for(var idx = 0 ; idx < noticeInfo.length ; idx++){
					row = noticeInfo[idx];
					
					noticeInfoHtml += "<tr>";
					noticeInfoHtml += "<td>"+(noticeInfo.length - idx)+"</td>";					
					noticeInfoHtml += "<td><a onClick='goNoticeDetail(\""+row.bbsNo+"\")' style='cursor:pointer'>"+row.subject+"</a></td>";
					
					if(isNotNull(row['insDate'])){ 
						noticeInfoHtml +="<td>"+moment(row['insDate'],"YYYYMMDD").format('YYYY-MM-DD')+"</td>";
			    	}else{
			    		noticeInfoHtml +="<td></td>";
			    	}
					
					noticeInfoHtml += "<td>"+row.hitCnt+"</td>";
					noticeInfoHtml += "</tr>";
				}
				
				$("#noticeInfoTbody").html(noticeInfoHtml);
				
              
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

//공지사항 목록
function goNoticeList(){
	$("#contents_wrap").empty();
	$(window).off("resize");
	
	$.ajax({
		url: rootPath + "html/board/board_notice_list.html",
		success: function(result) {						
			$("#contents_wrap").html(result);
		},
		error: function(request, status, error) {
			jAlert("다시 확인해주시기 바랍니다."+error);
		}
	});
}


//공지사항 상세보기
function goNoticeDetail(bbsNo) {
    //var retUrl = '/board_notice_list/' + model.currPage;
    //this.router.navigate(['/board_notice_view/' + bbsNo], { queryParams: { retUrl: retUrl } });
	
	$("#contents_wrap").empty();
	$(window).off("resize");
	
	$.ajax({
		url: rootPath + "html/board/board_notice_view.html",
		success: function(result) {			
			setCookie("paramBbsNo", bbsNo);
			//setCookie("paramTempPage", tempPage);
			
			$("#contents_wrap").html(result);
		},
		error: function(request, status, error) {
			jAlert("다시 확인해주시기 바랍니다."+error);
		}
	});
}

// FAQ 조회
function getFaq() {
  
	//var idFaq = model.faqList;
	var formData = new FormData();
	formData.append('bbsId', model.faqList.bbsId);
	formData.append('startPage', model.faqList.startPage);
	formData.append('endPage', model.faqList.endPage);
	formData.append('searchKey', model.faqList.searchKey);
	formData.append('searchValue', model.faqList.searchValue);
	formData.append('searchDateFrom', model.faqList.searchDateFrom);
	formData.append('searchDateTo', model.faqList.searchDateTo);
		
	var url = rootPath + 'bbs/getBbsContentsList.do';
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		async:false,
		//contentType: "application/json; charset=UTF-8",
		//data: JSON.stringify(idFaq),
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {		
			
			if (res.success) {
				
				var tmpList = [];
			    $.each(res['data'], function(i, v) {
			      v.contents = v.contents.replace(/\n/g, '<br />');
			      tmpList.push(v);
			    });
			    faqInfo = tmpList;
				
				var faqInfoHtml = "";
				for(var idx = 0 ; idx < faqInfo.length ; idx++){
					row = faqInfo[idx];
					
					faqInfoHtml += "<li>";
					faqInfoHtml += "<input id='item-tit-"+idx+"' type='checkbox' class='hidden' />";
					faqInfoHtml += "<div class='item-top'>";
					faqInfoHtml += "<div class='tabName NanumGothic' onclick='goFaqList(\""+row.workCode+"\")' style='cursor:pointer'>"+row.workName+"</div>";
					faqInfoHtml += "<label class='question NanumGothic' for='item-tit-"+idx+"'>"+row.subject+"</label>";
					faqInfoHtml += "<span class='hot'></span>";
					faqInfoHtml += "<label class='btn_type Material_icons icon' for='item-tit-"+idx+"'></label>";
					faqInfoHtml += "</div>";
					faqInfoHtml += "<div class='item-con'>";
					faqInfoHtml += "<div class='answer NanumGothic'>"+row.contents+"</div>";
					faqInfoHtml += "</div>";
					faqInfoHtml += "</li>";					
				}
				
				$("#faqInfoTbody").html(faqInfoHtml);
				
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

//FAQ 리스트
function goFaqList(workCode){
	//[routerLink]='['/board_faq_list/'+row.workCode+'/1']'
	
	$("#contents_wrap").empty();
	$(window).off("resize");
	
	$.ajax({
		url: rootPath + "html/board/board_faq_list.html",
		success: function(result) {	
			setCookie("paramWorkCode",workCode);
			
			$("#contents_wrap").html(result);
		},
		error: function(request, status, error) {
			jAlert("다시 확인해주시기 바랍니다."+error);
		}
	});
	
}


//검색
function getFaqSearch(){
	
	if(isNotNull($.trim($("#model_search_searchValue").val()))){
		model.faqList.searchKey = 'subject';
		model.faqList.searchValue = $.trim($("#model_search_searchValue").val());		
	}else{
		model.faqList.searchKey = '';
		model.faqList.searchValue = '';
	}
	
	getFaq();	
}

