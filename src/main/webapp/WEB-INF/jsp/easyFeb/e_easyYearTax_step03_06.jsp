<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
					<div class="group_tit skyblue mar-bot-5">
						<div class="icon"></div>
						<div class="text NanumSquare_R">주택자금</div>
						<div class="btns">
							<a class="btn_type NanumSquare_R">문의 </a>
						</div>
					</div>
					
					<div class="group_fold ">
						<div class="grid_field full">
							<ul class="">
								<li class="btns NanumRound field_sort_right">
									<a class="btn_type btn_addSubmit">추가 서류 제출</a>
								</li>
							</ul>
						</div>
						<!-- item1 -->
						<input id="group-2-1" type="checkbox" class="dep-2 hidden" />
						<div class="sub_header item NanumSquare_R">
							<div><a class="btn_type btn_question">문의 </a></div>
							<div class="sub_tit NanumSquare_R">주택임차차입금원리금상환액</div>
							<div class="btn_drop">
								<label for="group-2-1" class="btn_type btn_fold"></label>
							</div>
						</div>
						<div class="item_fold">
							
							<div class="group_table description mar-bot-10">
								<table class="hasHead NanumSquare_R mar-bot-5">
									<colgroup>
										<col width="15%">
										<col width="15%">
										<col width="70%">
									</colgroup>
									<thead>
										<tr>
											<th colspan="2">입력할 항목</th>
											<th>항목별 요약설명 및 공제요건</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td class="subTit">주택임차차입금</td>
											<td class="subTit">원리금상환액</td>
											<td class="descript">
												<dl class="dot">
													<dt class="mar-top-3">*</dt>
													<dd>공제대상 : 과세기간 종료일 현재 무주택 세대의 세대주(세대주가 주택 관련 공제를 받지 않은 경우 세대원도 가능)로서 근로소득이 있는자 <br> (단, 대부업을 영위하지 않는 거주자로부터 차입한 경우에는 총급여액 5천만원 이하인 자만 가능)</dd>
												</dl>
												<dl class="dot">
													<dt class="mar-top-3">*</dt>
													<dd>공제금액 : 원리금상환액 X 40%</dd>
												</dl>
												<dl class="dot">
													<dt class="mar-top-3">*</dt>
													<dd>공제한도 : 연 300만원 (주택마련저축 납입액 공제와 합하여 연 300만원을 초과할 수 없음)</dd>
												</dl>
											</td>
										</tr>
										
									</tbody>
								</table>
							</div>
							<div class="group_table hasSubType">
								<table class="hasHead NanumSquare_R mar-bot-5">
									<colgroup>
										<col width="14%">
										<col width="19%">
										<col width="19%">
										<col width="19%">
										<col width="19%">
									</colgroup>
									<thead>
										<tr>
											<th>차입구분</th>
											<th>국세청 자료</th>
											<th>차감금액</th>
											<th>기타 자료</th>
											<th>상환액 합계</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td class="subTit">대출 기관</td>
											<td class="value disable"><input type="text" class="input_txt sort_right" disabled></td>
											<td class="value"><input type="text" class="input_txt sort_right" ></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value disable"><input type="text" class="input_txt sort_right" value="0"  disabled></td>
										</tr>
										<tr>
											<td class="subTit">거주자</td>
											<td class="value disable"><input type="text" class="input_txt sort_right" disabled></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value disable" ><input type="text" class="input_txt" disabled></td>
											<td class="value disable"><input type="text" class="input_txt sort_right" value="0" disabled></td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="explain_wrap mar-bot-40"><span class="NanumSquare_R col_skyblue">※ 주택입차차입금원리금상환액 차입구분이 거주자의 기타 자료는 3.거주자간 주택임차차입금 원리금 상환액 소득공제 명세서를 작성하시면 금액이 자동 반영됩니다. </span></div>
						</div> <!--  item-1 fold -->
						
						<!-- item2 -->
						<input id="group-2-2" type="checkbox" class="dep-2 hidden" />
						<div class="sub_header item NanumSquare_R">
							<div><a class="btn_type btn_question">문의 </a></div>
							<div class="sub_tit NanumSquare_R">거주자 간 주택임차차입금 원리금 상환액 소득공제 명세</div>
							<div class="btn_drop">
								<label for="group-2-2" class="btn_type btn_fold"></label>
							</div>
						</div>
						<div class="item_fold">
							<div class="notice_wrap nonborder NanumSquare_R ">
								<div class="notice_con bg_orange col_darkred border-5 sort_center pad_thin10">
									총 급여 5천만원 이하만 공제 가능 / 총 급여 5천만원 이상 입력 불가
								</div>
							</div>
							
							<div class="grid_field full">
								<ul class="">
									<li class="table_title NanumSquare_R v-center">1) 금전소비대차 계약내용</li>
								</ul>
							</div>
							<div class="group_table">
								<table class="hasHead NanumGothic mar-bot-20">
									<colgroup>
										<col width="4%">
										<col width="10%">
										<col width="14%">
										<col width="12%">
										<col width="12%">
										<col width="12%">
										<col width="12%">
										<col width="12%">
										<col width="12%">
									</colgroup>
									<thead>
										<tr>
											<th class="chk_area"><input type="checkbox"></th>
											<th>대주(貸主) 성명</th>
											<th>주민등록번호</th>
											<th>금전소비대차 계약 개시일</th>
											<th>금전소비대차 계약 종료일</th>
											<th>차입금 이자율(%)</th>
											<th>원금</th>
											<th>이자</th>
											<th>원리금 상환액 계</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td class="chk_area"><input type="checkbox"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value"><input type="date" class="input_txt" value="2010-08-14"></td>
											<td class="value"><input type="date" class="input_txt" value="2010-08-14"></td>
											<td class="value unit"><input type="text" class="input_txt sort_right"><span class="unit">%</span></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value disable"><input type="text" class="input_txt sort_right" value="0" disabled></td>
										</tr>
									</tbody>
								</table>
							</div>
							<div class="grid_field full">
								<ul class="">
									<li class="table_title NanumSquare_R v-center">2) 임대차 계약내용</li>
								</ul>
							</div>
							<div class="group_table mar-bot-40">
								<table class="hasHead NanumSquare_R">
									<colgroup>
										<col width="4%">
										<col width="12%">
										<col width="12%">
										<col width="12%">
										<col width="12%">
										<col width="12%">
										<col width="12%">
										<col width="12%">
										<col width="12%">
									</colgroup>
									<thead>
										<tr>
											<th class="chk_area"><input type="checkbox"></th>
											<th>임대인성명 (상호)</th>
											<th>주민등록번호  (사업자등록번호)</th>
											<th>유형</th>
											<th>계약면적(㎡)</th>
											<th>임대차계약서상 주소지(물건지)</th>
											<th>계약서상 임대차 계약기간 개시일</th>
											<th>계약서상 임대차 계약기간 종료일</th>
											<th>전세보증금</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td class="chk_area"><input type="checkbox"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value">
												<select class="NanumSquare_R">
													<option value=""></option>
												</select></td>
											<td class="value unit"><input type="text" class="input_txt sort_right"><span class="unit">㎡</span></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value"><input type="date" class="input_txt"></td>
											<td class="value"><input type="date" class="input_txt" required="required"></td>
											<td class="value"><input type="text" class="input_txt sort_right" value="10,800,800"></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div> <!--  item-2 fold -->
						
						<input id="group-2-3" type="checkbox" class="dep-2 hidden" />
						<div class="sub_header item NanumSquare_R">
							<div><a class="btn_type btn_question">문의 </a></div>
							<div class="sub_tit NanumSquare_R">장기주택저당차입금이자상환액</div>
							<div class="btn_drop">
								<label for="group-2-3" class="btn_type btn_fold"></label>
							</div>
						</div>
						<div class="item_fold">
							
							<div class="group_table description mar-bot-5">
								<table class="hasHead NanumSquare_R mar-bot-5">
									<colgroup>
										<col width="15%">
										<col width="10%">
										<col width="10%">
										<col width="20%">
										<col width="10%">
										<col width="35%">
									</colgroup>
									<thead>
										<tr>
											<th colspan="3">입력할 항목</th>
											<th colspan="3">항목별 요약설명 및 공제요건</th>
										</tr>
									</thead>
									<tbody>
										<tr><td rowspan="10" class="left_subTit">장기주택저당차입금 이자상환액</td>
											<td class="subTit">차입시기</td>
											<td class="subTit">상환기간</td>
											<td class="subTit">상환방법</td>
											<td class="subTit">공제한도</td>
											<td rowspan="10" class="lsat-child">(주택임차차입금 원리금상환액공제 + 장기주택저당 차입금이자상환액공제 + 주택마련 저축납입액 공제) 금액이 해당 장기주택저당 차입금이자상환액 공제한도를 초과하는 경우 그 초과하는 금액은 없는 것으로 함</td>
										</tr>
										<tr><td rowspan="3">2011년 이전</td>
											<td class="">10년 ~ 14년</td>
											<td rowspan="3">011년 이전</td>
											<td class="sort_right">600만원</td>
											<td>
											</td>
										</tr>
										<tr><td class="">15년 ~ 29년</td>
											<td class="cols-right sort_right">1,000만원</td></tr>
										<tr><td class="">30년 이상</td>
											<td class="cols-right sort_right">1,500만원</td></tr>
										<tr><td rowspan="2">30년 이상</td>
											<td rowspan="2" class="sort_right">1,500만원</td>
											<td class="">고정금리 또는 비거치 분할상환방식</td>
											<td class="cols-right sort_right" >1,500만원</td></tr>
										<tr><td class="">기타</td>
											<td class="cols-right sort_right">500만원</td></tr>
										<tr><td rowspan="4" class="lsat-child">2015년 이후</td>
											<td rowspan="3">15년 이상</td>
											<td class="">고정금리이고 비거치 분할상환방식</td>
											<td class="cols-right sort_right">1,800만원</td></tr>
										<tr><td class="">고정금리이고 비거치 분할상환방식</td>
											<td class="cols-right sort_right">1,500만원</td></tr>
										<tr><td class="">기타</td>
											<td class="cols-right sort_right">500만원</td></tr>
										<tr><td class="">30년 이상</td>
											<td class="">고정금리 또는 비거치분할상환방식</td>
											<td class="cols-right sort_right">300만원</td></tr>
									</tbody>
								</table>
							</div>
							<div class="notice_wrap bor_skyblue mar-bot-20 border-5">
								<div class="notice_con list NanumSquare_R">
									<dl class="w-95">
										<dt class="col_skyblue mar-top-3"><span class="공정금리 방식">* 공정금리 방식 :</span></dt>
										<dd class="multiLine">차입금의 100분의 70 이상의 금액에 상당하는 분에 대한 이자를 상환기간 동안 고정금리로 지급하는 경우<br> (5년 이상 단위로 금리 변경하는 경우 포함)</dd>
									</dl>
									<dl class="w-145">
										<dt class="col_skyblue">* 비거치식 분할상환 방식 :</dt>
										<dd class="">차입일이 속하는 과세기간의 다음 과세기간부터 차입금 상환기간의 말일이 속하는 과세기간까지 매년 기준금액(차입금의 100분의 70/상환기간 연수) 이상의 차입금을  상환하는 경우 </dd>
									</dl>
								</div>
							</div>
							<div class="group_table hasSubType mar-bot-40">
								<table class="hasHead NanumSquare_R mar-bot-5">
									<colgroup>
										<col width="14%">
										<col width="22%">
										<col width="9%">
										<col width="14%">
										<col width="14%">
										<col width="14%">
										<col width="14%">
									</colgroup>
									<thead>
										<tr class="col_dark">
											<th>차입시기</th>
											<th>상환기간.방법</th>
											<th>한도</th>
											<th>국세청 자료</th>
											<th>차감금액</th>
											<th>기타 자료</th>
											<th>합계</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td class="subTit" rowspan="3">2011년 이전 차입분</td>
											<td class="subTit-1" >15년 미만</td><td class="disable sort_right" >600만원</td>	
											<td class="value disable"><input type="text" class="input_txt" disabled></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value disable"><input type="text" class="input_txt sort_right" value="0" disabled ></td>
										</tr>
										<tr>
											<td class="subTit-1" >15년~29년</td><td class="disable sort_right" >1,000만원</td>	
											<td class="value disable"><input type="text" class="input_txt" disabled></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value disable"><input type="text" class="input_txt sort_right" value="0" disabled ></td>
										</tr>
										<tr>
											<td class="subTit-1" >30년 이상</td><td class="disable sort_right" >1,500만원</td>	
											<td class="value disable"><input type="text" class="input_txt" disabled></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value disable"><input type="text" class="input_txt sort_right" value="0" disabled ></td>
										</tr>
										<tr>
											<td class="subTit" rowspan="2">2012년 이후 차입분</td>
											<td class="subTit-1" >15년 이상 고정금리 또는 비거치식</td><td class="disable sort_right" >1,500만원</td>	
											<td class="value disable"><input type="text" class="input_txt" disabled></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value disable"><input type="text" class="input_txt sort_right" value="0" disabled ></td>
										</tr>
										<tr>
											<td class="subTit-1" >15년 이상 기타</td><td class="disable sort_right" >500만원</td>	
											<td class="value disable"><input type="text" class="input_txt" disabled></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value disable"><input type="text" class="input_txt sort_right" value="0" disabled ></td>
										</tr>
										<tr>
											<td class="subTit" rowspan="4">2015년 이후 차입분</td>
											<td class="subTit-1" >15년 이상 고정금리이고 비거치식</td><td class="disable sort_right" >1,800만원</td>	
											<td class="value disable"><input type="text" class="input_txt" disabled></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value disable"><input type="text" class="input_txt sort_right" value="0" disabled ></td>
										</tr>
										<tr>
											<td class="subTit-1" >15년 이상 고정금리 또는 비거치식</td><td class="disable sort_right" >1,500만원</td>	
											<td class="value disable"><input type="text" class="input_txt" disabled></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value disable"><input type="text" class="input_txt sort_right" value="0" disabled ></td>
										</tr>
										<tr>
											<td class="subTit-1" >15년 이상 기타</td><td class="disable sort_right" >500만원</td>	
											<td class="value disable"><input type="text" class="input_txt" disabled></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value disable"><input type="text" class="input_txt sort_right" value="0" disabled ></td>
										</tr>
										<tr>
											<td class="subTit-1" >15년~15년 고정금리 또는 비거치식</td><td class="disable sort_right" >300만원</td>	
											<td class="value disable"><input type="text" class="input_txt" disabled></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value disable"><input type="text" class="input_txt sort_right" value="0" disabled ></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div> <!--  item2-3 fold -->
						
						<input id="group-2-4" type="checkbox" class="dep-2 hidden" />
						<div class="sub_header item NanumSquare_R">
							<div><a class="btn_type btn_question">문의 </a></div>
							<div class="sub_tit NanumSquare_R">월세액공제</div>
							<div class="btn_drop">
								<label for="group-2-4" class="btn_type btn_fold"></label>
							</div>
						</div>
						<div class="item_fold">
							
							<div class="group_table description mar-bot-15">
								<table class="hasHead NanumSquare_R mar-bot-5">
									<colgroup>
										<col width="30%">
										<col width="70%">
									</colgroup>
									<thead>
										<tr>
											<th>입력할 항목</th>
											<th>항목별 요약설명 및 공제요건</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td class="subTit">월세액공제</td>
											<td class="descript">
												<dl>
													<dt>* 공제대상 : </dt>
													<dd> 해당 과세기간 총급여액이 7천만원 이하(종합소득금액이 6천만원 이하)인 근로소득자인 무주택<br>
													세대의 세대주 (세대주가 주택 관련 공제를 받지 않은 경우 세대원도 가능)가 국민주택규모의 주택 <br>
													(주거용 오피스텔, 고시원 포함)을 임차하기 위해 지급하는 월세액</dd>
												</dl>
												<dl class="w-95">
													<dt>* 세액공제금액 : </dt>
													<dd>월세 지급액(750만원 한도) x 10%</dd>
												</dl>
											</td>
										</tr>
										
									</tbody>
								</table>
							</div>
							<div class="grid_field full mar-top-3">
								<ul class="">
									<li class="table_title NanumSquare_R v-center">
										월세액 세액공제 명세
									</li>
								</ul>
							</div>
							<div class="group_table mar-bot-5">
								<table class="hasHead NanumSquare_R">
									<colgroup>
										<col width="3%">
										<col width="12%">
										<col width="12%">
										<col width="12%">
										<col width="7%">
										<col width="10%">
										<col width="11%">
										<col width="11%">
										<col width="11%">
										<col width="11%">
									</colgroup>
									<thead>
										<tr>
											<th class="chk_area"><input type="checkbox"></th>
											<th>임대인성명 (상호)</th>
											<th>주민등록번호 (사업자등록번호)</th>
											<th>유형</th>
											<th>계약면적(㎡)</th>
											<th>임대차계약서상 주소지 (물건지)</th>
											<th>계약서상 임대차 계약기간 개시일</th>
											<th>계약서상 임대차 계약기간 종료일</th>
											<th>연간 월세액</th>
											<th>공제대상금액</th>
										</tr>
									</thead>
									<tbody>
										<tr>
											<td class="chk_area"><input type="checkbox"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value">
												<select class="NanumSquare_R">
													<option value=""></option>
												</select></td>
											<td class="value unit"><input type="text" class="input_txt sort_right"><span class="unit">㎡</span></td>
											<td class="value"><input type="text" class="input_txt"></td>
											<td class="value"><input type="date" class="input_txt"></td>
											<td class="value"><input type="date" class="input_txt"></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
											<td class="value"><input type="text" class="input_txt sort_right"></td>
										</tr>
									</tbody>
								</table>
							</div>
						</div> <!--  item-4 fold -->
					</div> <!-- group_fold -->
