<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
<link rel="stylesheet" type="text/css" href="/css/yearTax_common.css">
	<div id="yearTax_wrap" class="yearTax easy">
		<div class="container">
			<div class="location"><span class="loc_main NanumSquare_R">연말정산 >  </span><span class="loc_sub NanumSquare_R">추가서류 제출</span></div>
			
			<div class="process_wrap mask">
				<div class="stepBar"> 
					<div class="box clear">
						<div class="process-step NanumSquare_R">
							<div class="bar"><span class="line"></span><span class="no">1</span></div>
							<div class="process-panel">
									<span class="icon"></span>
									<span class="name NanumRound">소득확인</span>	
							</div>
							<div class="process-arrow"></div>
						</div>
					</div>
					<div class="box clear">
						<div class="process-step NanumSquare_R">
							<div class="bar"><span class="line"></span><span class="no">2</span></div>
							<div class="process-panel">
								<span class="icon"></span>
								<span class="name NanumRound">부양가족</span>
							</div>
							<div class="process-arrow"></div>
						</div>
					</div>
					<div class="box clear">
						<div class="process-step NanumSquare_R">
							<div class="bar"><span class="line"></span><span class="no">3</span></div>
							<div class="process-panel">
								<span class="icon"></span>
								<span class="name NanumRound">지출내역 확인</span>
							</div>
							<div class="process-arrow"></div>
						</div>
					</div>
					<div class="box current">
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
			
			<div class="contents">
				<div class="sub_item NanumSquare_R">
					<div class="item_btnlist">
						<a class="btn_type"><span class="txt"><span class="row_tit">부양가족</span><span class="row_sub_tit NanumRound">인적공제</span></span><span class="val"><span class="count">3</span><span class="unit">건</span></span></a>
						<a class="btn_type"><span class="txt"><span class="row_tit">보험료</span><span class="row_sub_tit NanumRound">보장성/장애인</span></span><span class="val"><span class="count">3</span><span class="unit">건</span></span></a>
						<a class="btn_type"><span class="txt"><span class="row_tit">의료비</span><span class="row_sub_tit NanumRound">안경/난임</span></span><span class="val"><span class="count">0</span><span class="unit">건</span></span></a>
						<a class="btn_type"><span class="txt"><span class="row_tit">교육비</span><span class="row_sub_tit NanumRound">교복/체험학습/방과후</span></span><span class="val"><span class="count">0</span><span class="unit">건</span></span></a>
						<a class="btn_type"><span class="txt"><span class="row_tit">기부금</span><span class="row_sub_tit NanumRound"></span></span><span class="val"><span class="count">0</span><span class="unit">건</span></span></a>
						<a class="btn_type"><span class="txt"><span class="row_tit">신용카드</span><span class="row_sub_tit NanumRound"></span></span><span class="val"><span class="count">0</span><span class="unit">건</span></span></a>
						<a class="btn_type"><span class="txt"><span class="row_tit">종(전) 근무지</span><span class="row_sub_tit NanumRound"></span></span><span class="val"><span class="count">0</span><span class="unit">건</span></span></a>
						<a class="btn_type"><span class="txt"><span class="row_tit">주택자금</span><span class="row_sub_tit NanumRound"></span></span><span class="val"><span class="count">0</span><span class="unit">건</span></span></a>
						<a class="btn_type"><span class="txt"><span class="row_tit">월세액</span><span class="row_sub_tit NanumRound"></span></span><span class="val"><span class="count">0</span><span class="unit">건</span></span></a>
						<a class="btn_type"><span class="txt"><span class="row_tit">기타 영수증</span><span class="row_sub_tit NanumRound"></span></span><span class="val"><span class="count">0</span><span class="unit">건</span></span></a>
					</div>
				</div>
				
			</div> <!--  contents -->
			
			<div class="group_btns foot NanumSquare_R">
				<a class="btn_type type1">＜  이전</a>
				<a class="btn_type type3">다음 ＞</a>
			</div>
		</div> <!--  container -->
		
		<div class="layerPop_wrap hidden">
			<div class="popup_bg small">
			
				<div class="popup_tit">
					<div class="popup_close"><a class="btn_type btn_close"></a></div>
				</div>
				<ul class="popup_content">
					<li class="icon"></li>
					<li class="text NanumRound">
						<span>등록된 추가서류를 다시한번 확인하시고</span>
						<span>누락된 내용은 추가 업로드 바랍니다. </span>
					</li>
					<li class="dec NanumRound">
						<span>단, 근로자 입력기간(2019년 1월 25일)까지</span>
						<span>완료 하신 내용까지 반영됩니다. </span>
					</li>
				</ul>
			</div>
		</div>
	</div>
<script type="text/javascript">

	$(document).ready(function() {
		
		// 이전화면
		$(".btn_type.type1").on("click", function(){
			var url = rootPath + "${ctx}/easyFeb/e_easyYearTax_step03_default.do";
			
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
			var url = rootPath + "${ctx}/easyFeb/e_easyYearTax_step05_01.do";
			
			$("#contents_wrap").empty();
		    $(window).off("resize");
		    $.ajax({
		        url : url, 
		        success : function(result) {
		            $("#contents_wrap").html(result);
		        }
		    });
		});

	});
	
</script>