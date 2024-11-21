<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
<link rel="stylesheet" type="text/css" href="/css/yearTax_common.css">

	<div id="yearTax_wrap" class="yearTax easy">
		<div class="container">
			<div class="location"><span class="loc_main NanumSquare_R">연말정산 >  </span><span class="loc_sub NanumSquare_R">소득확인</span></div>
			
			<div class="process_wrap mask">
				<div class="stepBar"> 
					<div class="box current">
						<div class="process-step NanumSquare_R">
							<div class="bar"><span class="line"></span><span class="no">1</span></div>
							<div class="process-panel">
									<span class="icon"></span>
									<span class="name NanumRound">소득확인</span>	
							</div>
							<div class="process-arrow"></div>
						</div>
					</div>
					<div class="box">
						<div class="process-step NanumSquare_R">
							<div class="bar"><span class="line"></span><span class="no">2</span></div>
							<div class="process-panel">
								<span class="icon"></span>
								<span class="name NanumRound">부양가족</span>
							</div>
							<div class="process-arrow"></div>
						</div>
					</div>
					<div class="box">
						<div class="process-step NanumSquare_R">
							<div class="bar"><span class="line"></span><span class="no">3</span></div>
							<div class="process-panel">
								<span class="icon"></span>
								<span class="name NanumRound">지출내역 확인</span>
							</div>
							<div class="process-arrow"></div>
						</div>
					</div>
					<div class="box">
						<div class="process-step NanumSquare_R">
							<div class="bar"><span class="line"></span><span class="no">4</span></div>
							<div class="process-panel">
								<span class="icon"></span>
								<span class="name NanumRound">추가서류 제출</span>
							</div>
							<div class="process-arrow"></div>
						</div>
					</div>
					<div class="box">
						<div class="process-step NanumSquare_R">
							<div class="bar"><span class="line"></span><span class="no">5</span></div>
							<div class="process-panel">
								<span class="icon"></span>
								<span class="name NanumRound">연말정산 요약</span>
							</div>
						</div>
					</div>
				</div>
			</div> <!--  process_wrap -->
				
			<div class="pagtit">
				<span class="NanumSquare_R">소득명세</span>
				<ul class="top_items NanumSquare_R">
					<li><a class="btn_type btn_reCount">재 집계 <span class="icon icon_reCount"></span></a></li>
				</ul>
			</div>
			
			<div class="contents">
				<div class="notice_wrap bg_skyblue ">
					<div class="notice_tit NanumGothic h_45">
						<span class="txt col_black font_20 userName">홍길동 </span><span class="txt col_black font_20">님의 2018년</span><span class="txt col_skyblue font_20 boxunderline">귀속 소득명세합계</span><span  class="txt col_black font_20"> 입니다.</span>
						<div class="btns">
							<a class="btn_type NanumSquare_R">문의 </a>
						</div>
					</div>
					<div class="notice_con NanumSquare_R sort_center">
						<div class="summery">
							<div class="align box">2018년 귀속 소득 명세 합계</div>
							<div class="align data">종전근무지 2건</div>
						</div>
					</div>
				</div>
				<div class="explain_wrap"><span class="NanumSquare_R col_darkblue">※ 하기 금액은 기초등록의 소득 정보로 작성된 내역이므로 기초등록을 수정하는 경우 변경될 수 있습니다.</span></div>
					
				
				<!--  nonbot : borer-bottom 삭제 -->
				<div class="group_column NanumSquare_R">
					<ul class="fix_column">
						<li class="thead text_center">구분</li>
						<li class="tbody tit">근무처명</li>
						<li class="tbody tit">사업자등록번호</li>
						<li class="tbody tit">근무기간</li>
						<li class="tbody nonbot tit">감면기간</li>
						<li class="tbody borTnb sum">총급여</li>
						<li class="tbody tit">급여</li>
						<li class="tbody tit">상여</li>
						<li class="tbody nonbot tit">비과세 한도초과액</li>
						<li class="tbody borTnb sum">비과세 합계</li>
						<li class="tbody tit">야간근로수당</li>
						<li class="tbody nonbot tit">기업연구소 연구 보조비</li>
						<li class="tbody borTnb sum">감면소득 합계</li>
						<li class="tbody nonbot tit">중소기업취업청년 감면100%</li>
						<li class="tbody borTnb sum">공제합계</li>
						<li class="tbody tit">건강보험료</li>
						<li class="tbody tit">기요양보험료</li>
						<li class="tbody tit">고용보험</li>
						<li class="tbody nonbot tit">국민연금 보험료</li>
						<li class="tbody cell_divide borTnb">
							<div class="block col_merger3 sum"> 기납부 세액</div>
							<div class="block">
								<div class="cell-child tit">소득세</div>
								<div class="cell-child tit">지방소득세</div>
								<div class="cell-child tit">농어촌특별세</div>
							</div>
						</li>
					</ul>
					<ul class="fix_column sort_right col_skyblue sum_data">
						<li class="thead text_center">합계</li>
						<li class="tbody"></li>
						<li class="tbody"></li>
						<li class="tbody"></li>
						<li class="tbody nonbot"></li>
						<li class="tbody borTnb sum ">50,000,000</li>
						<li class="tbody">100,000,000</li>
						<li class="tbody">100,000,000</li>
						<li class="tbody nonbot"></li>
						<li class="tbody borTnb sum">50,000,000</li>
						<li class="tbody">50,000,000</li>
						<li class="tbody nonbot"></li>
						<li class="tbody borTnb sum">50,000,000</li>
						<li class="tbody nonbot"></li>
						<li class="tbody borTnb sum">50,000,000</li>
						<li class="tbody">100,000,000</li>
						<li class="tbody">100,000,000</li>
						<li class="tbody">100,000,000</li>
						<li class="tbody borb"></li>
						<li class="tbody ">50,000,000</li>
						<li class="tbody ">50,000,000</li>
						<li class="tbody borb">50,000,000</li>
					</ul>
					<div class="column_viewer">
						<div class="column_data">
							
						 	<ul class="column sort_right data">
								<li class="thead text_center">주(현)근무지</li>
								<li class="tbody text_center">웰마켓</li>
								<li class="tbody text_center">201-80-1234567</li>
								<li class="tbody text_center"></li>
								<li class="tbody text_center nonbot"></li>
								<li class="tbody col_black borTnb ">50,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody nonbot"></li>
								<li class="tbody borTnb">50,000,000</li>
								<li class="tbody">50,000,000</li>
								<li class="tbody nonbot"></li>
								<li class="tbody borTnb">50,000,000</li>
								<li class="tbody nonbot"></li>
								<li class="tbody borTnb">50,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody borb"></li>
								<li class="tbody ">50,000,000</li>
								<li class="tbody ">50,000,000</li>
								<li class="tbody borb">50,000,000</li>
							</ul>
							<ul class="column sort_right data"> 
								<li class="thead text_center">종전 근무지 1</li>
								<li class="tbody text_center"></li>
								<li class="tbody text_center"></li>
								<li class="tbody text_center"></li>
								<li class="tbody text_center nonbot"></li>
								<li class="tbody col_black borTnb">50,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody nonbot"></li>
								<li class="tbody borTnb">50,000,000</li>
								<li class="tbody">50,000,000</li>
								<li class="tbody nonbot"></li>
								<li class="tbody borTnb">50,000,000</li>
								<li class="tbody nonbot"></li>
								<li class="tbody borTnb">50,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody borb"></li>
								<li class="tbody ">50,000,000</li>
								<li class="tbody ">50,000,000</li>
								<li class="tbody borb">50,000,000</li>
							</ul> 
							<ul class="column sort_right data"> 
								<li class="thead text_center">종전 근무지 2</li>
								<li class="tbody text_center"></li>
								<li class="tbody text_center"></li>
								<li class="tbody text_center"></li>
								<li class="tbody text_center nonbot"></li>
								<li class="tbody col_black borTnb">50,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody nonbot"></li>
								<li class="tbody borTnb">50,000,000</li>
								<li class="tbody">50,000,000</li>
								<li class="tbody nonbot"></li>
								<li class="tbody borTnb">50,000,000</li>
								<li class="tbody nonbot"></li>
								<li class="tbody borTnb">50,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody">100,000,000</li>
								<li class="tbody borb"></li>
								<li class="tbody ">50,000,000</li>
								<li class="tbody ">50,000,000</li>
								<li class="tbody borb">50,000,000</li>
							</ul> 
						</div> <!--  column_view -->
					</div>
				</div> <!-- group_column -->
				
				<div class="group_btns foot NanumSquare_R">
					<a class="btn_type type1">＜  이전</a>
					<a class="btn_type type2"><span class="icon_save Material_icons"></span>적용</a>
					<a class="btn_type type3">다음 ＞</a>
				</div>
			</div> <!-- content -->
		</div> <!--  container -->
		<div class="layerPop_wrap">
			<div class="popup_bg small">
			
				<div class="popup_tit">
					<div class="popup_close"><a class="btn_type btn_close"></a></div>
				</div>
				<ul class="popup_content">
					<li class="icon"></li>
					<li class="text NanumRound">
						<span>종전근무지 정보 반영을 위해서</span>
						<span>재집계를 수행하세요.</span> 
					</li>
				</ul>
			</div>
		</div>
	</div>

<script type="text/javascript">
	$(document).ready(function() {
		
		// 이전화면
		$(".btn_type.type1").on("click", function(){
			var url = rootPath + "${ctx}/easyFeb/e_easyYearTax_step01_01.do";
			
			$("#contents_wrap").empty();
		    $(window).off("resize");
		    $.ajax({
		        url : url, 
		        success : function(result) {
		            $("#contents_wrap").html(result);
		        }
		    });
		});
		
		// 다음화면
		$(".btn_type.type3").on("click", function(){
			var url = rootPath + "${ctx}/easyFeb/e_easyYearTax_step02_01.do";
			
			$("#contents_wrap").empty();
		    $(window).off("resize");
		    $.ajax({
		        url : url, 
		        success : function(result) {
		            $("#contents_wrap").html(result);
		        }
		    });
		});
		
		$(".btn_close").click(function(e){
			$(this).parent().parent().parent().parent().addClass("hidden");
		});
		
		총급여 = "";
		비과세합계 = "";
		감면소득합계 = "";
		공제합계 = "";
		소득세 = "";
		지방소득세 = "";
		농어촌특별세 = "";
		
		// 소득명세 조회
		//fn_getYE040();		
	});
	
	function fn_qna(windowcode) {
		var url = rootPath + "html/bbs/board_question_popup.html?windowCode=" + windowcode;
		
		window.open(url, '문의하기', 'width=905, height=730, location=no, menubar=no, status=no, scrollbars=no, toolbar=no');
	}
	
</script>
