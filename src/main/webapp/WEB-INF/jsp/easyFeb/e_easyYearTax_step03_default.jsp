<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
<link rel="stylesheet" type="text/css" href="/css/yearTax_common.css">
	<div id="yearTax_wrap" class="yearTax easy">
		<div class="container">
			<div class="location"><span class="loc_main NanumSquare_R">연말정산 >  </span><span class="loc_sub NanumSquare_R">지출내역 확인</span></div>
			
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
					<div class="box current">
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
						<!-- <label for="step04"></label> -->
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
			</div> <!--  group_wrap -->
			<div class="contents">
				<div class="sub_item NanumSquare_R">
					<div class="sub_top">
						<a class="btn_type btn_pdfUpload"> 국세청 PDF 업로드</a>
					</div>
					<ul class="item_list">
						<li>
							<div class="item_top">
								<div class="item_tit">보험료</div>
								<div class="item_edit"><a class="btn_type btn_edit"></a></div>
							</div>
							<div class="item_data">
								<div><span class="tit">PDF :</span><span class="value">30,000,000</span><span class="unit">원</span></div>
								<div><span class="tit">기타 :</span><span class="value">0</span><span class="unit">원</span></div>
							</div>
						</li>
						<li>
							<div class="item_top">
								<div class="item_tit">의료비</div>
								<div class="item_edit"><a class="btn_type btn_edit"></a></div>
							</div>
							<div class="item_data">
								<div><span class="tit">PDF :</span><span class="value">30,000,000</span><span class="unit">원</span></div>
								<div><span class="tit">기타 :</span><span class="value">0</span><span class="unit">원</span></div>
							</div>
						</li>
						<li>
							<div class="item_top">
								<div class="item_tit">교육비</div>
								<div class="item_edit"><a class="btn_type btn_edit"></a></div>
							</div>
							<div class="item_data">
								<div><span class="tit">PDF :</span><span class="value">30,000,000</span><span class="unit">원</span></div>
								<div><span class="tit">기타 :</span><span class="value">5,000,000</span><span class="unit">원</span></div>
							</div>
						</li>
						<li>
							<div class="item_top">
								<div class="item_tit">신용카드 등</div>
								<div class="item_edit"><a class="btn_type btn_edit"></a></div>
							</div>
							<div class="item_data">
								<div><span class="tit">PDF :</span><span class="value">30,000,000</span><span class="unit">원</span></div>
								<div><span class="tit">기타 :</span><span class="value">300,000</span><span class="unit">원</span></div>
							</div>
						</li>
						<li>
							<div class="item_top">
								<div class="item_tit">연금계좌</div>
								<div class="item_edit"><a class="btn_type btn_edit"></a></div>
							</div>
							<div class="item_data">
								<div><span class="tit">PDF :</span><span class="value">30,000,000</span><span class="unit">원</span></div>
								<div><span class="tit">기타 :</span><span class="value">400,000</span><span class="unit">원</span></div>
							</div>
						</li>
						<li>
							<div class="item_top">
								<div class="item_tit">주택자금</div>
								<div class="item_edit"><a class="btn_type btn_edit"></a></div>
							</div>
							<div class="item_data">
								<div><span class="tit">PDF :</span><span class="value">30,000,000</span><span class="unit">원</span></div>
								<div><span class="tit">기타 :</span><span class="value">1,000,000</span><span class="unit">원</span></div>
							</div>
						</li>
						<li>
							<div class="item_top">
								<div class="item_tit">기부금</div>
								<div class="item_edit"><a class="btn_type btn_edit"></a></div>
							</div>
							<div class="item_data">
								<div><span class="tit">PDF :</span><span class="value">30,000,000</span><span class="unit">원</span></div>
								<div><span class="tit">기타 :</span><span class="value"></span><span class="unit">원</span></div>
							</div>
						</li>
						<li>
							<div class="item_top">
								<div class="item_tit">기타 공제 항목</div>
								<div class="item_edit"><a class="btn_type btn_edit"></a></div>
							</div>
							<div class="item_data">
								<div><span class="tit">PDF :</span><span class="value">30,000,000</span><span class="unit">원</span></div>
								<div><span class="tit">기타 :</span><span class="value">2,000,000</span><span class="unit">원</span></div>
							</div>
						</li>
						<li class="full_line">
							<div class="item_top">
								<div class="item_tit">기타 PDF 금액</div>
								<div class="item_edit"><a class="btn_type btn_edit"></a></div>
							</div>
							<div class="part">
								<div class="item_data">
									<div><span class="tit">장기집합투자증권  :</span><span class="value">30,000,000</span><span class="unit">원</span></div>
									<div><span class="tit">소기업.소상공인 공제부금  :</span><span class="value"></span><span class="unit">원</span></div>
								</div>
							</div>
							<div class="part">
								<div class="item_data">
									<div><span class="tit">투자조합출자 등 :</span><span class="value">30,000,000</span><span class="unit">원</span></div>
									<div><span class="tit">기타 :</span><span class="value"></span><span class="unit">원</span></div>
								</div>
							</div>
						</li>
					</ul>
					
				</div>
			
			</div>
			
			<div class="group_wrap"></div>
			
			<div class="group_btns foot NanumSquare_R">
				<a class="btn_type type1">＜  이전</a>
				<a class="btn_type type3">다음 ＞</a>
			</div>
		</div> <!--  container -->
		
		<div class="layerPop_wrap">
			<div class="popup_bg small">
			
				<div class="popup_tit">
					<div class="popup_close"><a class="btn_type btn_close"></a></div>
				</div>
				<ul class="popup_content">
					<li class="icon"></li>
					<li class="text NanumRound">
						<span>국세청 PDF를 업로드하시면 </span>
						<span>아래 항목이 자동 입력됩니다.</span>
						 
					</li>
					<li class="dec NanumSquare_R">
						<span>기타 내용이 있다면 해당 버튼을 눌러 수정해주세요.</span>
					</li>
				</ul>
			</div>
		</div>
		
	</div>

<script type="text/javascript">
	$(document).ready(function() {
		
		// 이전화면
		$(".btn_type.type1").on("click", function(){
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
		
		// 다음화면
		$(".btn_type.type3").on("click", function(){
			var url = rootPath + "${ctx}/easyFeb/e_easyYearTax_step04_01.do";
			
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
		
		$(".sub_item > .item_list > li").on("click", function(e){
			
			var $me = $(this);
			var $item = $(".sub_item > .item_list > li");
			$item.removeClass()
			
			$(".sub_item > .item_list > li:eq(8)").addClass("full_line");
			$me.addClass("active");
			
			$index = $me.index();
			
			
			if($index === 0){//보험료
				getItemHtml("${ctx}/easyFeb/e_easyYearTax_step03_01.do");
			}else if($index === 1){//의료비
				getItemHtml("${ctx}/easyFeb/e_easyYearTax_step03_02.do");
			}else if($index === 2){//교육비
				getItemHtml("${ctx}/easyFeb/e_easyYearTax_step03_03.do");
			}else if($index === 3){//신용카드
				getItemHtml("${ctx}/easyFeb/e_easyYearTax_step03_04.do");
			}else if($index === 4){//연금계좌
				getItemHtml("${ctx}/easyFeb/e_easyYearTax_step03_05.do");
			}else if($index === 5){//주택자금
				getItemHtml("${ctx}/easyFeb/e_easyYearTax_step03_06.do");
			}else if($index === 6){//기부금
				getItemHtml("${ctx}/easyFeb/e_easyYearTax_step03_07.do");
			}else if($index === 7){//기타공제항목
				getItemHtml("${ctx}/easyFeb/e_easyYearTax_step03_08.do");
			}else if($index === 8){//기타PDF금액
				getItemHtml("${ctx}/easyFeb/e_easyYearTax_step03_09.do");
			}
		});
	});

	function getItemHtml(url){
		request = $.ajax({
	        url: url,
	        type: "get",
	        cache: false,
	        dataType:"html"
	    });
		
		request.done(function (result){
			$(".group_wrap").empty();
			$(".group_wrap").html(result);
		});
	}
	
</script>