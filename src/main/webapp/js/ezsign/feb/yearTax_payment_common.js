var winH,headH,footerH;
var targetWrap;
var userType;

//페이지 이동 체크 값
var checkUnload = false;
$(document).ready(function(){
	
	userType = getCookie("loginType");
	sessionStorage.setItem("userType", userType);
	sessionStorage.setItem("yearContractId", getCookie("yearContractId"));
		
	// 관리자일때 실행
	if (isNotNull(userType) && userType >= 5) {
		winH=$(window).height();
		headH=$("#header").height();
		footerH=$("#section_footer").height();
		
		$("#employ-list-wrap").css("height",winH-headH-footerH); //리스트 높이
		emplistPostion(winH);
		
		//목록 html을 가져온다
		//$("#employ-list-wrap").load("e_yearTax_basicsData_empolyeeList.html");
		var userId = "";
		
		$.ajax({
	        url : rootPath + "/html/feb/febmaster/yearTax_payment_include.html",
	        success : function(resHtml) {
	            $("#employ-list-wrap").html(resHtml);
	        }
	    });
	}
	
});

$(window).resize(function(){
	if (isNotNull(userType) && userType >= 5) {
		emplistPostion($(window).height());
	}
});

//페이지내에서 스크롤 이동
function pageScrollPos(){
	$("#contents_wrap").scrollTop(0);
	$("#contents_wrap").scrollLeft(0);

	this.openToggle = sessionStorage.getItem('openToggle');
	if(isNotNull(this.openToggle) && $("[id='"+this.openToggle+"']").length > 0){
		
		var pageTarget=$('#' + this.openToggle).next(".sub_header").offset().top - $("#header").height()-90;
		$("#contents_wrap").scrollTop(pageTarget);
	}
}

function emplistPostion(_winH){
	winH=_winH; 
	
	//가로 스크롤 생성여부에 따라 리스트 높이 조절
	if($("#contents_wrap").prop("scrollWidth")-$("#contents_wrap").prop("clientWidth" ) > 0 ){
		$("#employ-list-wrap").css("height", winH-headH-footerH - 15); //스크롤 높이 제외한 리스트 높이
	}else{
		$("#employ-list-wrap").css("height", winH-headH-footerH); //리스트 높이
	}
	
	//리스트 y위치값+(리스트높이/2)-(버튼높이/2)
	var _btnposY=$("#employ-list-wrap").offset().top+($("#employ-list-wrap").outerHeight()/2)-($(".btn_emplist").height()/2);
	
	$(".btn_emplist").css("top",_btnposY);
}

$("#btn_left").change(function(){
	//근로자 목록(숨김/펼침) 클릭시에 스크롤 여부에 따른 높이 조정
	emplistPostion($(window).height());
});

//**** 사이드 펼치기 event
$( "input[type=checkbox]" ).on( "click", function(){
	var _target=$(this).attr("id");
	if($(this).is(":checked")){
		$('[class~="'+_target+'"]').addClass("show");
			
	}else{
		$('[class~="'+_target+'"]').removeClass("show");
	}
});

//퍼블리싱 스크립트 소스
function scriptCode() {
  /*  *************** 모두보기 토글 버튼   ***************  */
  $('.btn_toggle').click(function() {

    var isState = $(this).hasClass('view') ? true : false;
    if ( isState ) {
      $(this).removeClass('view');
      $(this).text('모두 닫기');
    } else {
      $(this).addClass('view');
      $(this).text('모두 보기');
    }
    
    $('.group_wrap').find('input[id^="group"]').each(function() {
      if (!isState) { // 현재 모두 보기 상태
        $(this).prop('checked', false);
      } else {
        $(this).prop('checked', true);
      }
    });
  });
}	

//현재 화면 모두 펼치기
function allListView(){	

	$(".btn_toggle").removeClass('view');
	$(".btn_toggle").text('모두 닫기');
	$('.group_wrap').find('input[id^="group"]').each(function() {
		$(this).prop('checked', true);
	});
}

//문의 팝업
function callQnaPop(typeQna) {
  this.openWin (rootPath + 'html/bbs/board_question_popup.html?windowCode=' + typeQna, '문의하기', '905', '730');
}

//map 페이지 이동
function goMapLink() {	
	//location.assign(rootPath + "app.html#"+urlForMap+"?userId="+this.userId);
	// location.assign(rootPath + "/html/feb/febworker/e_yearTax_basicsData_map_admin_pdf.html?userId="+paramEncrypt(userId));
	
	if(이월기부금반영) {
		jAlert('이월기부금 반영을 실행하여 주시기 바랍니다.','');
		return false;
	}

	if(checkUnload) {
		
		jConfirm('이 페이지를 벗어나면 작성된 내용은 저장되지 않습니다.', '', function(cfrm){
    		if(cfrm){
    			goMap();
    		}
    	});
		
	}else{
		goMap();
	}
	
	
}

function goMap() {
	$("#contents_wrap").empty();
    $(window).off("resize");
    
    var url = "";

	if (isNotNull(userType) && userType >= 5) {
		// 관리자 Map
		url = rootPath + "/html/feb/febworker/e_yearTax_basicsData_map_admin_pdf.html";
	} else {
		// 근로자 Map
		url = rootPath + "/html/feb/febworker/e_yearTax_basicsData_map_employee_pdf.html";
	}
    $.ajax({
        url : url,
        success : function(result) {
        	sessionStorage.setItem("paramUserId", userId);
            $("#contents_wrap").html(result);
        },
		error: function(request,status, error) {
			alert("다시 확인해주시기 바랍니다."+error);
		}
    });
}