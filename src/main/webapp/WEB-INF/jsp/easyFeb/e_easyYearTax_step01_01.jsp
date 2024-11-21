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
			<div class="contents">
				<div class="top_field NanumSquare_R">
					<div class="work_list">
						<a class="btn_type">근무지 1</a>
						<a class="btn_type">근무지 2</a>
						<a class="btn_type">근무지 3</a>
						<a class="btn_type">근무지 4</a>
					</div>
					<div class="work_btns ">
						<a class="btn_type btn_add">추가</a>
						<a class="btn_type btn_del">삭제</a>
					</div>
				</div>
				<div class="receipt_wrap">
					<div class="receipt_panel">
						<div class="receipt_image"><img src="/css/image/easyTax/receipt_2018.jpg"></div>
						<ul class="receipt_input">
							<li class="part1">
								<div class="block1">
									<div class="_line1 w35"><input type="radio" name="p1_b1_l1" id="p1_b1_l1" /><label for="p1_b1_l1"></label></div>
									<div class="_line2 w35"><input type="radio" name="p1_b1_l1" id="p1_b1_l2" /><label for="p1_b1_l2"></label></div>
									<div class="_line3 flex_between">
										<span><input type="radio" name="p1_b1_l3" id="p1_b1_l3_1" /><label for="p1_b1_l3_1"></label></span>
										<span><input type="radio" name="p1_b1_l3" id="p1_b1_l3_2" /><label for="p1_b1_l3_2"></label></span>
										<span><input type="radio" name="p1_b1_l3" id="p1_b1_l3_2" /><label for="p1_b1_l3_2"></label></span>
									</div>
								</div>
								<div class="block2">
									<div class="_line1">
										<span><input type="radio" name="p1_b2_l1" id="p1_b2_l1_1" /><label for="p1_b2_l1_1"></label></span>
										<span><input type="radio" name="p1_b2_l1" id="p1_b2_l1_2" /><label for="p1_b2_l1_2"></label></span>
									</div>
									<div class="_line2 flex_between"><input type="text" id="p1_b2_l2_1" /><input type="text" id="p1_b2_l2_2" /></div>
									<div class="_line3">
										<span><input type="radio" name="p1_b2_l3" id="p1_b2_l3_1" /><label for="p1_b2_l3_1"></label></span>
										<span><input type="radio" name="p1_b2_l3" id="p1_b2_l3_2" /><label for="p1_b2_l3_2"></label></span>
									</div>
									<div class="_line4">
										<span><input type="radio" name="p1_b2_l4" id="p1_b2_l4_1" /><label for="p1_b2_l4_1"></label></span>
										<span><input type="radio" name="p1_b2_l4" id="p1_b2_l4_2" /><label for="p1_b2_l4_2"></label></span>
									</div>
									<div class="_line5">
										<span><input type="radio" name="p1_b2_l5" id="p1_b2_l5_1" /><label for="p1_b2_l5_1"></label></span>
										<span><input type="radio" name="p1_b2_l5" id="p1_b2_l5_2" /><label for="p1_b2_l5_2"></label></span>
									</div>
									<div class="_line6 flex_between"><input type="text" id="p1_b2_l6_1"> <input type="text" id="p1_b2_l6_2"></div>
									<div class="_line7">
										<span><input type="radio" name="p1_b2_l7" id="p1_b2_l7_1" /><label for="p1_b2_l7_1"></label></span>
										<span><input type="radio" name="p1_b2_l7" id="p1_b2_l7_2" /><label for="p1_b2_l7_2"></label></span>
									</div>
									<div class="_line8">
										<span><input type="radio" name="p1_b2_l8" id="p1_b2_l8_1" /><label for="p1_b2_l8_1"></label></span>
										<span><input type="radio" name="p1_b2_l8" id="p1_b2_l8_2" /><label for="p1_b2_l8_2"></label></span>
									</div>
								</div>
							</li>
							<li class="part2">
								<div class="_line1 flex_between"><input type="text" /><input type="text" /></div>
								<div class="_line2 flex_between"><input type="text" /><input type="text" /></div>
								<div class="_line3 flex_between">
									<div>
										<span><input type="radio" name="p2_b1_l3" id="p1_b1_l3_1" /><label for="p2_b1_l3_1"></label></span>
										<span><input type="radio" name="p2_b1_l3" id="p1_b1_l3_2" /><label for="p2_b1_l3_2"></label></span>
									</div>
									<input type="text" id="p1_l3_2" /></div>
								<div class="_line4"><input type="text" /></div>
								<div class="_line5 flex_between"><input type="text" id="p2_l5_1" /><input type="text" id="p1_l5_2" /></div>
								<div class="_line6"><input type="text" /></div>
							</li>
							<li class="part3">
								<div class="block1">
									<input type="text" />
									<input type="text" />
									<span><input type="text" /><input type="text" /></span>
									<span><input type="text" /><input type="text" /></span>
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
								</div>
							</li>
							<li class="part4">
								<div class="block1">
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
								</div>
							</li>
							<li class="part5">
								<div class="block1">
									<input type="text" />
									<input type="text" />
									<input type="text" />
								</div>
								<div class="block2">
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
								</div>
								<div class="block3">
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
								</div>
								<div class="block4">
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
								</div>
							</li>
							<li class="part6">
								<div class="_line1 flex_between"><input type="text" /><input type="text" /><input type="text" /></div>
								<div class="_line2"><input type="text" /></div>
							</li>
							<li class="part7 NanumSquare_R">
								<div class="block1">
									<input type="text" />
									<input type="text" />
									<input type="text" />
									<input type="text" />
								</div>
							</li>
						</ul>
					</div>
					
				</div>
				<div class="group_btns foot NanumSquare_R">
					<a class="btn_type type1">＜  이전</a>
					<a class="btn_type type2"><span class="icon_save Material_icons"></span>적용</a>
					<a class="btn_type type3">다음 ＞</a>
				</div>
			</div>	
		</div> <!--  container -->
		
		<!--  layer popup -->
		<div class="layerPop_wrap workCheck">
			<div class="popup_bg small">
			
				<div class="popup_tit">
					<div class="popup_close"><a class="btn_type btn_close workCheck"></a></div>
				</div>
				<ul class="popup_content">
					<li class="icon"></li>
					<li class="text NanumRound">
						<span>1.종(전) 근무지가 있나요?</span>
					</li>
				</ul>
				<div id="foot_btns" class="NanumRound">
					<a class="btn_type type1 yes">예</a>
					<a class="btn_type type2 no">아니오</a>
				</div>
			</div>
		</div>
		<!--  layer popup -->
		<div class="layerPop_wrap hidden alert_guide">
			<div class="popup_bg">
				<div class="popup_tit">
					<div class="popup_close"><a class="btn_type btn_close alert_guide"></a></div>
				</div>
				<ul class="popup_content">
					<li class="icon"></li>
					<li class="text NanumRound">
						<span>종전 근무지에서 발급받은 원천징수영수증에 </span>
						<span>내용을 입력해주세요.</span>
					</li>
					<li class="dec NanumRound">
						<span>전 근무지가 여러곳이라면</span>
						<span><span class="col_red inline">추가</span>를 눌러 계속 입력해주시기 바랍니다.</span>
					</li>
				</ul>
			</div>
		</div>
	</div>

<script type="text/javascript">
	
$(document).ready(function(){

	$(".workCheck").find("#foot_btns > .btn_type").click(function(e){
		console.log($(e.currentTarget).attr("class"));
		if($(e.currentTarget).hasClass("yes")){
			$(".alert_guide").removeClass("hidden");	
		}
		$(".workCheck").addClass("hidden");
	});
	
	$(".btn_close").click(function(e){
		$(this).parent().parent().parent().parent().addClass("hidden");
	});
	
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
		var url = rootPath + "${ctx}/easyFeb/e_easyYearTax_step01_02.do";
		
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

function fn_income() {
	var url = rootPath + "${ctx}/easyFeb/e_easyYearTax_step01_02.do";
	
	$("#contents_wrap").empty();
    $(window).off("resize");
    $.ajax({
        url : url, 
        success : function(result) {
            $("#contents_wrap").html(result);
        }
    });
}
</script>
	
	