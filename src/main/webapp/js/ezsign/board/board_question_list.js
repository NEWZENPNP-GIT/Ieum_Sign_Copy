// 전역으로 처리 예정
var userInfo = {
		loginType: getCookie('loginType'),
		loginId: getCookie('loginId'),
		userId : getCookie('userId'),
		loginName : getCookie('loginName')
  	};

var model = {
		list: [],
	    totalCount: 0,
	    perPage: 10,
	    tmpPage: 1,
	    currPage: 1,
	    search: {
	    	bbsId: '',
	    	userId:getCookie('userId'),
	    	startPage: 0,
	    	endPage: 0,
	    	searchKey: 'subject',
	    	searchValue: '',
	    	workCode: '',
	    	searchDateFrom: '',
	    	searchDateTo: '',
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
	
	//버튼
	if(userInfo.loginType <= 5){
		$("#btnGroupDiv").html("<a class='btn_type Material_icons btn_write' onClick='goWrite()'>글쓰기</a>");
	}
	
	//검색창
	$('#model_search_searchValue').keydown(function(e){
		//enter 일경우
	    if(e.keyCode == 13){
	    	getSearch();
	    }
	});
	
	//리스트 조회
	getList();
	
});

//리스트조회
function getList() {
    
	model.search.startPage = (model.tmpPage - 1) * 10;
    model.search.endPage = model.perPage;

    var formData = new FormData();
	formData.append('bbsId', model.search.bbsId);
	formData.append('userId', model.search.userId);	
	formData.append('searchKey', model.search.searchKey);
	formData.append('searchValue', model.search.searchValue);
	formData.append('startPage', model.search.startPage);
	formData.append('endPage', model.search.endPage);
	formData.append('workCode', model.search.workCode);

	if (model.search.searchDateFrom.length == 10 && model.search.searchDateTo.length == 10) {		
		formData.append('searchDateFrom', moment(model.search.searchDateFrom, 'YYYY-MM-DD').format('YYYYMMDD'));
		formData.append('searchDateTo', moment(model.search.searchDateTo, 'YYYY-MM-DD').add(1, 'days').format('YYYYMMDD'));
    } else {
    	formData.append('searchDateFrom', '');
    	formData.append('searchDateTo', '');
    }

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
				var tmpList = [];
				$.each(res['data'], function(i, v) {
					v.isNew = moment(v.insDate, 'YYYYMMDDHHmmss').add(7, 'days').isSameOrAfter(moment());
					tmpList.push(v);
				});
				model.list = tmpList;
			    model.totalCount = res['total'];
			    model.currPage = model.tmpPage;
				
			  //리스트 
			    var modelListHtml = "";
			    if(model.list != null && model.list.length > 0){
				    for(var idx = 0; idx < model.list.length; idx++){
				    	var row = model.list[idx];
				    	var isNewHtml = ""
				    	if(row.isNew){
				    		isNewHtml = "<span class='new'></span>";
				    	}else{
				    		isNewHtml = "";
				    	}
				    	
				    	var listNumber = (model.totalCount+1) - ((idx+1) + ((model.currPage - 1) * model.perPage));
				    	modelListHtml += "<tr class='databox'>";
				    	modelListHtml +="<td>"+listNumber+"</td>";
				    	modelListHtml += "<td>"+ row.serviceName +"_"+ row.workName +"</td>";
				    	modelListHtml += "<td><a onClick='goDetail(\""+row.bbsId+"\",\""+row.bbsNo+"\")'>"+row.subject+"["+row.commentCount+"]</a> "+isNewHtml+"</td>";
				    	modelListHtml += "<td>"+row.empName+"</td>";
				    	modelListHtml += "<td>"+moment(row.insDate,"YYYYMMDD").format('YYYY-MM-DD')+"</td>";
				    	if(row.statusCode == '1'){
				    		modelListHtml += "<td class='state complete'>"+row.statusName+"</td>";
				    	}else{
				    		modelListHtml += "<td class='state'>"+row.statusName+"</td>";
				    	}
				    	modelListHtml += "</tr>";
				    }
			    }else{
			    	modelListHtml += "<tr>";
			    	modelListHtml += "<td colspan='6'>조회 결과가 없습니다.</td>";
			    	modelListHtml += "</tr>";
			    }
			    $("#modelListTbody").html(modelListHtml);
			  
              
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

//검색시도
function getSearch() {
    
	model.tmpPage = 1;
    model.currPage = 1;
    
    model.search.searchDateFrom = $("#model_search_searchDateFrom").val();
    model.search.searchDateTo = $("#model_search_searchDateTo").val();
    model.search.searchKey = $("#model_search_searchKey").val();
    model.search.searchValue = $.trim($("#model_search_searchValue").val());
    
    getList();
}

//글쓰기
function goWrite() {
	
	$("#contents_wrap").empty();
	$(window).off("resize");
	
	$.ajax({
		url: rootPath + "html/board/board_question_write.html",
		success: function(result) {
			$("#contents_wrap").html(result);
		},
		error: function(request, status, error) {
			jAlert("다시 확인해주시기 바랍니다."+error);
		}
	});
}

//상세보기
function goDetail(bbsId, bbsNo) {
	    
	$("#contents_wrap").empty();
	$(window).off("resize");
	
	$.ajax({
		url: rootPath + "html/board/board_question_view.html",
		success: function(result) {			
			setCookie("paramBbsId", bbsId);
			setCookie("paramBbsNo", bbsNo);
			
			$("#contents_wrap").html(result);
			
			$("#contents_wrap").css("width","100%");
		},
		error: function(request, status, error) {
			jAlert("다시 확인해주시기 바랍니다."+error);
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
    	model.tmpPage = num;
    	onChangeperPage();
    });
    $('.grid_paging .select_box select').val(model.perPage);
}


function onChangeperPage() {
	//perPage = $("#perPage").val();
	
	model.search.startPage = (model.tmpPage - 1 ) * model.perPage;
    if (model.perPage > model.totalCount) {
    	model.search.startPage = 0;
    }
    model.search.endPage = model.perPage;
    model.currPage =  model.tmpPage;
    
    getList();
}

