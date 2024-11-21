// 전역으로 처리 예정
var userInfo = {
		loginType: getCookie('loginType'),
		loginId: getCookie('loginId'),
		userId : getCookie('userId')
  	};

var model = {
     fixedList: [],
     list: [],
     totalCount: 0,
     perPage: 10,
     tmpPage: 1,
     currPage: 1,
     search: {
    	 bbsId: '180706135108002',
    	 startPage: 0,
    	 endPage: 0,
    	 searchKey: 'subject',
    	 searchValue: '',
    	 contentsType: '0',
     }
  };

var fixedListHtml = "";
var noticeListHtml = "";

//페이징 관련
var totalCount =  0;  // 총 건수
var perPage =  10;
var currPage =  1;
var startPage =  0;
var endPage =  10;
var tempPage =  1;

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
	
	if(userInfo.loginType > 8){
		$("#btnGroupDiv").html("<a class='btn_type Material_icons btn_write' onClick='fncBoardWrite();'>글쓰기</a>");
	}
	
	// 검색시
	$('#model_search_searchValue').keydown(function(e){
		//enter 일경우
	    if(e.keyCode == 13){
	    	getSearch();
	    }
	});
	
	if(isNotNull(getCookie("paramBbsNo"))){
		setCookie("paramBbsNo","")
	}
	/*if(isNotNull(getCookie("paramTempPage"))){
		tempPage = getCookie("paramTempPage");
		setCookie("paramTempPage", "")
	}*/
	if(isNotNull(getURLParameters("paramTempPage"))){
		tempPage = getURLParameters("paramTempPage");
	}

	getList();	
});


//리스트 조회
function getList() {
	
	//초기화
	//$("#noticeListTbody").html("");
	
    getFixedList();

    model.search.startPage = (tempPage - 1) * 10;
    model.search.endPage = perPage;

    logI("# perPage : " + tempPage );
    logI("# perPage : " + perPage );
    
    logI("# model.search.startPage : " + model.search.startPage );
    logI("# model.search.endPage : " + model.search.endPage );
    
    var params = model.search;
    
    var formData = new FormData();
	formData.append('bbsId', model.search.bbsId);
	formData.append('searchKey', model.search.searchKey);
	formData.append('searchValue', model.search.searchValue);
	formData.append('contentsType', model.search.contentsType);
	formData.append('startPage', model.search.startPage);
	formData.append('endPage', model.search.endPage);
	
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
		//data: JSON.stringify(params),
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {		
			if (res.success) {
				var tmpList = [];
	    	    $.each(res['data'], function(i, v) {
	    	        v.isNew = moment(v.insDate, 'YYYYMMDDHHmmss').add(7, 'days').isSameOrAfter(moment());
	    	        tmpList.push(v);
	    	    });
	    	    model.list = tmpList;
	    	    totalCount = res['total'];
	    	    currPage = tempPage;
	    	    
	    	    logI("# currPage : " + currPage );
	    	    logI("# totalCount : " + totalCount );
	    	    logI("# model.list.length : " + model.list.length );
	    	    
	    	    if(model.list != null && model.list.length > 0){	    	    	
	    	    	//var noticeListHtml = "";
	    	    	noticeListHtml = "";
	    	    	for(var idx = 0 ; idx < model.list.length ; idx++){
	    	    		var row = model.list[idx];
	    	    		
	    	    		var listNumber = (totalCount+1) - ((idx+1) + ((currPage - 1) * perPage)); 
	    	    		noticeListHtml +="<tr class='databox'>";
	    	    		noticeListHtml +="<td>"+listNumber+"</td>";
	    	    		if(row.isNew){
	    	    			noticeListHtml +="<td><a onClick='goDetail(\""+row.bbsNo+"\")'>"+row.subject+"</a> <span class='new'></span></td>";
	    	    		}else{
	    	    			noticeListHtml +="<td><a onClick='goDetail(\""+row.bbsNo+"\")'>"+row.subject+"</a></td>";
	    	    		}
	    	    		noticeListHtml +="<td>"+row.empName+"</td>";	    	    		
	    	    		if(isNotNull(row.insDate)){ 
	    	    			noticeListHtml +="<td class='value'>"+moment(row.insDate,"YYYYMMDD").format('YYYY-MM-DD')+"</td>";
				    	}else{
				    		noticeListHtml +="<td class='value'></td>";
				    	}	    	    		
	    	    		noticeListHtml +="<td>"+row.hitCnt+"</td>";
	    	    		noticeListHtml +="</tr>";	    	    		
	    	    	}
	    	    	
	    	    	//$("#noticeListTbody").after(noticeListHtml);	
	    	    	$("#noticeListTbody").html(fixedListHtml+noticeListHtml);
	    	    }else{
	    	    	
	    	    	if(isNotNull(fixedListHtml)){
	    	    		$("#noticeListTbody").html(fixedListHtml);
	    	    	}else{
	    	    		$("#noticeListTbody").html("<tr><td colspan='5'>조회 결과가 없습니다.</td></tr>");
	    	    	}
	    	    }
	    	    
	    	    //페이징 처리
				var paginateOption = {
						total: Math.ceil(totalCount / perPage),
						page: currPage
					};  
					
				if (totalCount > 0) {
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

function getFixedList() {
   
	var formData = new FormData();
	formData.append('bbsId', '180706135108002');
	formData.append('startPage', 0);
	formData.append('endPage', 5);
	formData.append('contentsType', '1');
	formData.append('searchKey', '');
	formData.append('searchValue', '');
	
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
		//data: JSON.stringify(idFaq),
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {		
			
			if (res.success) {
				var tmpList = [];
	    	    $.each(res['data'], function(i, v) {
	    	        v.isNew = moment(v.insDate, 'YYYYMMDDHHmmss').add(7, 'days').isSameOrAfter(moment());
	    	        tmpList.push(v);
	    	    });
	    	    model.fixedList = tmpList;
	    	    
	    	    if(model.fixedList != null && model.fixedList.length > 0){
	    	    
	    	    	//var fixedListHtml = "";
	    	    	fixedListHtml = "";
	    	    	for(var idx = 0 ; idx < model.fixedList.length ; idx++){
	    	    		var row = model.fixedList[idx];
	    	    		
	    	    		fixedListHtml +="<tr class='databox'>";
	    	    		fixedListHtml +="<td>[공지]</td>";
	    	    		if(row.isNew){
	    	    			fixedListHtml +="<td><a onClick='goDetail(\""+row.bbsNo+"\")'>"+row.subject+"</a> <span class='new'></span></td>";
	    	    		}else{
	    	    			fixedListHtml +="<td><a onClick='goDetail(\""+row.bbsNo+"\")'>"+row.subject+"</a></td>";
	    	    		}
	    	    		fixedListHtml +="<td>"+row.empName+"</td>";	    	    		
	    	    		if(isNotNull(row.insDate)){ 
	    	    			fixedListHtml +="<td class='value'>"+moment(row.insDate,"YYYYMMDD").format('YYYY-MM-DD')+"</td>";
				    	}else{
				    		fixedListHtml +="<td class='value'></td>";
				    	}	    	    		
	    	    		fixedListHtml +="<td>"+row.hitCnt+"</td>";
	    	    		fixedListHtml +="</tr>";	    	    		
	    	    	}
	    	    		    	    	
	    	    	//$("#noticeListTbody").html(fixedListHtml);
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

//글쓰기
function fncBoardWrite(){
	
	$("#contents_wrap").empty();
	$(window).off("resize");
	
	$.ajax({
		url: rootPath + "html/board/board_notice_write.html",
		success: function(result) {			
			$("#contents_wrap").html(result);
		},
		error: function(request, status, error) {
			jAlert("다시 확인해주시기 바랍니다."+error);
		}
	});

}

//상세보기
function goDetail(bbsNo) {
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

//검색시도
function getSearch() {
    
	model.tmpPage = 1;
    model.currPage = 1;
        
    model.search.searchKey = $("#model_search_searchKey").val();
    model.search.searchValue = $.trim($("#model_search_searchValue").val());
    
    getList();
}

//페이징 관련 
function pageSelection(opt) {
    var _this = this;    
    var settings = $.extend({}, {
      total: 0,
      page: _this.currPage,
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
      _this.tempPage = num;
      _this.onChangeperPage();
    });
    $('.grid_paging .select_box select').val(perPage);
}


function onChangeperPage() {
	//perPage = $("#perPage").val();
		
    startPage = (tempPage - 1 ) * perPage;
    if (perPage > totalCount) {
      startPage = 0;
    }
    endPage = perPage;
    currPage =  tempPage;

    getList();
}