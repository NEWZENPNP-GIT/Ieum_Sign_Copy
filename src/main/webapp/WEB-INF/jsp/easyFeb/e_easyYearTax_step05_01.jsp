<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
<link rel="stylesheet" type="text/css" href="/css/yearTax_common.css">
	<div id="yearTax_wrap" class="yearTax easy">
		<div class="container">
			<div class="location"><span class="loc_main NanumSquare_R">연말정산 >  </span><span class="loc_sub NanumSquare_R">연말정산 요약</span></div>
			
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
					<div class="box clear">
						<div class="process-step NanumSquare_R">
							<div class="bar"><span class="line"></span><span class="no">4</span></div>
							<div class="process-panel">
								<span class="icon"></span>
								<span class="name NanumRound">추가서류 제출</span>
							</div>
							<div class="process-arrow"></div>
						</div>
					</div>
					<div class="box current">
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
				<div class="notice_wrap bg_skyblue ">
					<div class="notice_tit NanumGothic h_45">
						<span class="txt col_black font_20 userName">홍길동 </span><span class="txt col_black font_20">님은 환급액의 증가 또는 추징세액의 감소를 위하여</span><span class="txt col_skyblue font_20 boxunderline">일반세액공제</span><span  class="txt col_black font_20">를 적용합니다.</span>
						<div class="btns">
							<a class="btn_type NanumSquare_R">문의 </a>
						</div>
					</div>
					<div class="notice_con NanumSquare_R sort_center">
						<div class="summery">
							<div class="align box">2018년 귀속 연말정산 요약</div>
							<div class="align data">일반세액공제</div>
						</div>
					</div>
				</div>
				<div class="explain_wrap"><span class="NanumSquare_R col_darkblue">하기 금액은 추정 금액이므로 확정 이후 최종 금액과 차이가 있을 수 있습니다. </span></div>
				
				<div class="group_wrap">
					<div class="group_tit">
						<div class="icon"></div>
						<div class="text NanumSquare_R">환급/추가납부 세액</div>
						<div class="taxResult NanumSquare_R col_red"><span class="price">- 449,118</span><span class="res_txt"> 환급</span> </div>
					</div>
				</div>
				<!--  group_column : 데이터를 가로 추가할수 있는 테이블형 -->
				<!--  group-cell : +&-기능을 사용하는 데이터를 가로 추가할수 있는 테이블형 -->
				<div class="group-cell NanumSquare_R summary">
					<div class="fix_viewer">
						<ul class="fix_column">
							<li class="thead">총급여</li>
							<li class="tbody sub">근로소득공제<div class="markSign">-</div></li>
							<li class="tbody subTit">근로소득금액</li>
							<li class="tbody itemgroup">
								<input id="group-1" type="checkbox" class="dep-1 hidden">
								<div class="item-gnb">
									<div class="markSign">-</div>
									<label for="group-1" class="btn_type Material_icons icon" ></label>
									<label for="group-1" class="label tit">인적공제 </label>
								</div>
								<ul class="item-list">
									<li class="nav">본인</li>
									<li class="nav">배우자</li>
									<li class="nav">부양가족 <div class="add">( <span class="data">3</span> ) 명</div></li>
									<li class="nav">경로우대<div class="add">( <span class="data">2</span> ) 명</div></li>
									<li class="nav">장애인<div class="add">( <span class="data">1</span> ) 명</div></li>
									<li class="nav">부녀자</li>
									<li class="nav">한부모</li>
								</ul>
							</li>
							<li class="tbody itemgroup">
								<input id="group-2" type="checkbox" class="dep-1 hidden">
								<div class="item-gnb">
									<div class="markSign">-</div>
									<label for="group-2"class="btn_type Material_icons icon"></label>
									<label for="group-2"class="label tit">연금보험료공제 </label>
								</div>
								<ul class="item-list">
									<li class="nav">국민연금보험료</li>
									<li class="nav">공무원연금</li>
									<li class="nav">군인연금</li>
									<li class="nav">사립학교교직원</li>
									<li class="nav">별정우체국연금</li>
								</ul>
							</li>
							<li class="tbody itemgroup">
								<input id="group-3" type="checkbox" class="dep-1 hidden">
								<div class="item-gnb">
									<div class="markSign">-</div>
									<label for="group-3"class="btn_type Material_icons icon"></label>
									<label for="group-3"class="label tit">특별소득공제 </label>
								</div>
								<ul class="item-list">
									<li class="nav">건강보험료</li>
									<li class="nav">고용보험료</li>
									<li class="nav">주택임차차입금<div class="add">대출기관</div></li>
									<li class="nav">주택임차차입금<div class="add">거주자</div></li>
									<li class="nav">장기주택저당차입금이자상환액<div class="add"><div>2011년 이전 차입분</div><div>15년 미만</div></div></li>
									<li class="nav">장기주택저당차입금이자상환액<div class="add"><div>2011년 이전 차입분</div><div>15년~29년</div></div></li>
									<li class="nav">장기주택저당차입금이자상환액<div class="add"><div>2011년 이전 차입분</div><div>30년 이상</div></div></li>
									<li class="nav">장기주택저당차입금이자상환액<div class="add"><div>2012년 이후 차입분(15년 이상)</div><div>고정금리OR비거치상환 대출</div></div></li>
									<li class="nav">장기주택저당차입금이자상환액<div class="add"><div>2012년 이후 차입분(15년 이상)</div><div>그 밖의 대출</div></div></li>
									<li class="nav">장기주택저당차입금이자상환액<div class="add"><div>2012년 이후 차입분(15년 이상)</div><div>고정금리AND비거치상환 대출</div></div></li>
									<li class="nav">장기주택저당차입금이자상환액<div class="add"><div>2012년 이후 차입분(15년 이상)</div><div>고정금리OR비거치상환 대출</div></div></li>
									<li class="nav">장기주택저당차입금이자상환액<div class="add"><div>2012년 이후 차입분(15년 이상)</div><div>그 밖의 대출</div></div></li>
									<li class="nav">장기주택저당차입금이자상환액<div class="add"><div>2012년 이후 차입분(15년 이상)</div><div>고정금리OR비거치상환 대출</div></div></li>
									<li class="nav">기부금(이월분)</li>
								</ul>
							</li>
							<li class="tbody subTit"><div class="item-gnb">차감소득금액</div></li>
							<li class="tbody itemgroup">
								<input id="group-4" type="checkbox" class="dep-1 hidden">
								<div class="item-gnb">
									<div class="markSign">-</div>
									<label for="group-4"class="btn_type Material_icons icon"></label>
									<label for="group-4"class="label tit">그밖의 소득공제 </label>
								</div>
								<ul class="item-list">
									<li class="nav">개인연금저축</li>
									<li class="nav">소기업·소상공인공제부금</li>
									<li class="nav">주택마련저축소득공제<div class="add">청약저축</div></li>
									<li class="nav">주택마련저축소득공제<div class="add">주택청약종합저축</div></li>
									<li class="nav">주택마련저축소득공제<div class="add">근로자주택마련저축</div></li>
									<li class="nav">투자조합출자 등</li>
									<li class="nav">신용카드 등 사용액</li>
									<li class="nav">우리사주조합 출연금</li>
									<li class="nav">고용유지 중소기업 근로자</li>
									<li class="nav">장기집합투자증권저축</li>
								</ul>
							</li>
							<li class="tbody sub"><div class="item-gnb">소득공제 종합한도 초과액</div><div class="markSign">+</div></li>
							<li class="tbody subTit"><div class="item-gnb">종합소득과세표준</div></li>
							<li class="tbody subTit"><div class="item-gnb">산출세액</div></li>
							<li class="tbody itemgroup">
								<input id="group-5" type="checkbox" class="dep-1 hidden">
								<div class="item-gnb">
									<div class="markSign">-</div>
									<label for="group-5"class="btn_type Material_icons icon"></label>
									<label for="group-5"class="label tit">세액감면</label>
								</div>
								<ul class="item-list">
									<li class="nav">소득세법에 의한 감면</li>
									<li class="nav">조세특례제한법<div class="add">외국인기술자 등 감면</div> </li>
									<li class="nav">중소기업 취업자에 대한 감면<div class="add">100%</div></li>
									<li class="nav">중소기업 취업자에 대한 감면<div class="add">50%</div></li>
									<li class="nav">중소기업 취업자에 대한 감면<div class="add">70%</div></li>
									<li class="nav">조세조약에 의한 감면</li>
								</ul>
							</li>
							<li class="tbody sub"><div class="item-gnb">근로소득세액공제<div class="markSign">-</div></div></li>
							<li class="tbody itemgroup">
								<input id="group-6" type="checkbox" class="dep-1 hidden">
								<div class="item-gnb">
									<div class="markSign">-</div>
									<label for="group-6"class="btn_type Material_icons icon"></label>
									<label for="group-6"class="label tit">자녀세액공제</label>
								</div>
								<ul class="item-list">
									<li class="nav">자녀<div class="add">( <span class="data">1</span> ) 명</div></li>
									<li class="nav">출산·입양<div class="add">( <span class="data">1</span> ) 명</div> </li>
								</ul>
							</li>
							<li class="tbody itemgroup">
								<input id="group-7" type="checkbox" class="dep-1 hidden">
								<div class="item-gnb">
									<div class="markSign">-</div>
									<label for="group-7"class="btn_type Material_icons icon"></label>
									<label for="group-7"class="label tit">연금계좌</label>
								</div>
								<ul class="item-list">
									<li class="nav">과학기술인공제회</li>
									<li class="nav">근로자퇴직연금 </li>
									<li class="nav">연금저축 </li>
								</ul>
							</li>
							<li class="tbody itemgroup">
								<input id="group-8" type="checkbox" class="dep-1 hidden">
								<div class="item-gnb">
									<div class="markSign">-</div>
									<label for="group-8"class="btn_type Material_icons icon"></label>
									<label for="group-8"class="label tit">특별세액공제</label>
								</div>
								<ul class="item-list">
									<li class="nav">보장성보험</li>
									<li class="nav">장애인전용보장성보험 </li>
									<li class="nav">의료비</li>
									<li class="nav">교육비</li>
									<li class="nav">정치자금기부금<div class="add">10만원 이하 </div></li>
									<li class="nav">정치자금기부금<div class="add">10만원 초과 </div></li>
									<li class="nav">법정기부금</li>
									<li class="nav">우리사주조합기부금</li>
									<li class="nav">지정기부금<div class="add">종교단체 외</div></li>
									<li class="nav">지정기부금<div class="add">종교단체 </div></li>
								</ul>
							</li>
							<li class="tbody sub"><div class="item-gnb">표준세액공제<div class="markSign">-</div></div></li>
							<li class="tbody sub"><div class="item-gnb">납세조합공제<div class="markSign">-</div></div></li>
							<li class="tbody sub"><div class="item-gnb">주택차입금<div class="markSign">-</div></div></li>
							<li class="tbody sub"><div class="item-gnb">외국납부<div class="markSign">-</div></div></li>
							<li class="tbody sub"><div class="item-gnb">월세세액</div><div class="markSign">-</div></li>
							<li class="tbody subTit"><div class="item-gnb">결정세액</div></li>
						</ul>
					</div>	
					<div class="column_viewer">
						<div class="column_data">
							<ul class="data-list">
								<li class="thead"><div class="item-gnb data">57,400,609</div></li>
								<li class="tbody sub"><div class="item-gnb data">1,2620,030</div></li>
								<li class="tbody subTit"><div class="item-gnb data">44,780,579</div></li>
								<li class="tbody itemgroup group-1">
									<div class="item-gnb data">14,500,000</div>
									<ul class="item-list-data">
										<li class="data">1,500,000</li>
										<li class="data">7,500,000</li>
										<li class="data">2,500,000</li>
										<li class="data">2,500,000</li>
										<li class="data">2,000,000</li>
										<li class="data">-</li>
										<li class="data">-</li>
									</ul>
								</li>
								<li class="tbody itemgroup group-2">
									
									<div class="item-gnb data">2,384,100</div>
									<ul class="item-list-data">
										<li class="data">2,384,100</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
									</ul>
								</li>
								<li class="tbody itemgroup group-3">
								
									<div class="item-gnb data">2,602,910</div>
									<ul class="item-list-data">
										<li class="data">1,838,220</li>
										<li class="data">379,834</li>
										<li class="data">384,856</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
									</ul>
								</li>
								<li class="tbody subTit"><div class="item-gnb data">25,293,569</div></li>
								<li class="tbody itemgroup group-4">
									<div class="item-gnb data">3,395,260</div>
									<ul class="item-list-data">
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">96,000</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">3,299,260</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
									</ul>
								</li>
								<li class="tbody sub"><div class="item-gnb data">-</div></li>
								<li class="tbody subTit"><div class="item-gnb data">21,898,309</div></li>
								<li class="tbody subTit"><div class="item-gnb data">2,204,746</div></li>
								<li class="tbody itemgroup group-5">
									
									<div class="item-gnb data">-</div>
									<ul class="item-list-data">
										<li class="data">-</li>
										<li class="data">- </li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
									</ul>
								</li>
								<li class="tbody sub"><div class="item-gnb data">660,000</div></li>
								<li class="tbody itemgroup group-6">
									
									<div class="item-gnb data">150,000</div>
									<ul class="item-list-data">
										<li class="data">150,000</li>
										<li class="data">-</li>
									</ul>
								</li>
								<li class="tbody itemgroup group-7">
									
									<div class="item-gnb data">-</div>
									<ul class="item-list-data">
										<li class="data">-</li>
										<li class="data">- </li>
										<li class="data">- </li>
									</ul>
								</li>
								<li class="tbody itemgroup group-8">
									
									<div class="item-gnb data">606,750</div>
									<ul class="item-list-data">
										<li class="data">120,000</li>
										<li class="data">- </li>
										<li class="data">-</li>
										<li class="data">141,750</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">-</li>
										<li class="data">345,000</li>
									</ul>
								</li>
								<li class="tbody sub"><div class="item-gnb data">-</div></li>
								<li class="tbody sub"><div class="item-gnb data">-</div></li>
								<li class="tbody sub"><div class="item-gnb data">-</div></li>
								<li class="tbody sub"><div class="item-gnb data">-</div></li>
								<li class="tbody sub"><div class="item-gnb data">-</div></li>
								<li class="tbody subTit"><div class="item-gnb data">787,996</div></li>
							</ul>
								
								
						</div> <!--  column_data -->
					</div> <!--  column_view -->
					
				</div> <!-- group_column -->
				
				<div class="group-cellBox NanumSquare_R">
					<ul class="fix_column">
						<li class="thead">구분</li>
						<li class="tbody">결정세액</li>
						<li class="tbody">기납부세액_전근무지</li>
						<li class="tbody">기납부세액_현근무지</li>
						<li class="tbody">납부특례세액</li>
						<li class="tfoot">차감징수세액</li>
					</ul>
					<ul class="column_data">
						<li class="thead">소득세</li>
						<li class="tbody sort_right">787,996</li>
						<li class="tbody sort_right">-</li>
						<li class="tbody sort_right">1,237,180</li>
						<li class="tbody sort_right">-</li>
						<li class="tfoot sort_right">449,180</li>
					</ul>
					<ul class="column_data">
						<li class="thead">지방소득세</li>
						<li class="tbody sort_right">78,799</li>
						<li class="tbody sort_right">-</li>
						<li class="tbody sort_right">123,700</li>
						<li class="tbody sort_right">-</li>
						<li class="tfoot sort_right">44,900</li>
					</ul>
					<ul class="column_data">
						<li class="thead">농어촌특별세</li>
						<li class="tbody sort_right">-</li>
						<li class="tbody sort_right">-</li>
						<li class="tbody sort_right">-</li>
						<li class="tbody sort_right">-</li>
						<li class="tfoot sort_right">-</li>
					</ul>
					<ul class="column_data">
						<li class="thead">합계</li>
						<li class="tbody sort_right">866,795</li>
						<li class="tbody sort_right">-</li>
						<li class="tbody sort_right">1,360,880</li>
						<li class="tbody sort_right">-</li>
						<li class="tfoot sort_right">494,080</li>
					</ul>
					
				</div>

				<ul class="explain_wrap NanumSquare_R">
					<li class="">
						<dl class="">
							<dt><span class="NanumGothic arrow">≫ </span><label class="label_txt NanumSquare_R">[추가징수세액 분할납부 신청]</label></dt>
							<dd class="radio_box">
								<div class="radSet"><input type="radio" name="no1"><label>신청</label></div>
								<div class="radSet"><input type="radio" name="no1"><label>미신청</label></div>
							</dd>
						</dl>
						<div class="dec">차감징수세액(소득세 기준)이 10만원을 초과하는 경우 분할납부 신청이 가능합니다.</div>
					</li>
					<li>
						<dl class="">
							<dt><span class="NanumGothic arrow">≫ </span><label class="label_txt NanumSquare_R">[원천징수세액 변경 신청]</label></dt>
							<dd class="radio_box">
								<div class="radSet"><input type="radio" name="no2"><label>120%</label></div>
								<div class="radSet"><input type="radio" name="no2"><label>100%</label></div>
								<div class="radSet"><input type="radio" name="no2"><label>80%</label></div>
							</dd>
						</dl>
						<div class="dec">근로자 본인이 원하는 경우 매월 원천징수하는 세액을 법령상 세액의 120%, 100%, 80% 중 선택할 수 있습니다. </div>
					</li>
					<li><span class="col_black">현재 요약보기의 환급/추가납부 세액은 추정 금액입니다.&nbsp;</span><span class="col_darkblue"> 실제 차감징수세액과 다를 수 있습니다. </span></li>
					<li class="col_darkblue">하단의 [근로자 확정] 실행하여 저장하시기 바랍니다 </li>
				</ul>
			</div> <!--  contents -->
			
			<div class="group_btns foot NanumSquare_R">
				<a class="btn_type type2">근로자 확정</a>
				<a class="btn_type type3">최종 확정</a>
				<!-- <a class="btn_type type4">Map ＞</a> -->
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
						<span>공제 예상 금액 결과 화면입니다. </span>
					</li>
				</ul>
			</div>
		</div>
	</div>
<script type="text/javascript">

	//**** 펼치기 event
	$(document).ready(function() {
		
		$( "input[type=checkbox]" ).on( "click", function(){
			var _target=$(this).attr("id");
			if($(this).is(":checked")){
				$('[class~="'+_target+'"]').addClass("show");
				
			}else{
				$('[class~="'+_target+'"]').removeClass("show");
			}
			console.log(_target)
		});

	});
</script>