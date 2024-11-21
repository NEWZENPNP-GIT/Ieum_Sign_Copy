<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
					<div class="group_tit skyblue mar-bot-5">
						<div class="icon"></div>
						<div class="text NanumSquare_R">기부금</div>
						<div class="btns">
							<a class="btn_type NanumSquare_R">문의 </a>
						</div>
					</div>
					
					<div class="group_fold ">
						
						<div class="grid_field full">
							<ul class="">
								<li class="table_title NanumSquare_R v-center">해당연도 기부 명세</li>
								<li class="btns NanumRound field_sort_right">
									<a class="btn_type btn_addSubmit">추가 서류 제출</a>
								</li>
							</ul>
						</div>
						
						<div class="group_table hasSubType mar-bot-30">
							<table class="hasHead NanumSquare_R mar-bot-5">
								<colgroup>
									<col width="3%">
									<col width="9%">
									<col width="13%">
									<col width="8%">
									<col width="8%">
									<col width="12%">
									<col width="11%">
									<col width="4%">
									<col width="8%">
									<col width="8%">
									<col width="8%">
									<col width="8%">
								</colgroup>
								<thead>
									<tr>
										<th rowspan="2" colspan="2">자료구분</th>
										<th rowspan="2">기부코드</th>
										<th rowspan="2">기부내용</th>
										<th rowspan="2">기부자</th>
										<th colspan="2">기부처</th>
										<th colspan="5">기부명세</th>
									</tr>
									<tr>
										
										<th>사업자(주민)등록번호</th>
										<th>상호(성명)</th>
										<th>건수</th>
										<th>합계</th>
										<th>공제대상기부금</th>
										<th>기부장려금</th>
										<th>기타</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="chk_area"><input type="checkbox"></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value="">국세청</option>
											</select></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value="">정치자금기부금 </option>
											</select></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value="">금전 </option>
											</select></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value="">홍길동 </option>
											</select></td>
										<td class="value disable"><input type="text" class="input_txt" disabled value="0"></td>
										<td class="value disable"><input type="text" class="input_txt" disabled value="종교단체"></td>
										<td class="value disable"><input type="text" class="input_txt" disabled value="5" ></td>
										<td class="value disable">2</td>
										<td class="value"><input type="text" class="input_txt sort_right" value="1078000" ></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled value="0"></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled value="0"></td>
									</tr>
									<tr>
										<td class="chk_area"><input type="checkbox"></td>
										<td class="value">
											<select class="NanumSquare_R">
												<option value="">국세청</option>
											</select></td>
										<td class="value">
											<select class="NanumSquare_R">
												<option value="">정치자금기부금 </option>
											</select></td>
										<td class="value">
											<select class="NanumSquare_R">
												<option value="">금전 </option>
											</select></td>
										<td class="value">
											<select class="NanumSquare_R">
												<option value="">홍길동 </option>
											</select></td>
										<td class="value"><input type="text" class="input_txt" value="0"></td>
										<td class="value"><input type="text" class="input_txt" value="종교단체"></td>
										<td class="value"><input type="text" class="input_txt" value="5" ></td>
										<td class="value">2</td>
										<td class="value"><input type="text" class="input_txt sort_right" value="1078000" ></td>
										<td class="value"><input type="text" class="input_txt sort_right" disabled value="0"></td>
										<td class="value"><input type="text" class="input_txt sort_right" disabled value="0"></td>
									</tr>
								</tbody>
							</table>
						</div>
						
						<div class="grid_field full">
							<ul class="">
								<li class="table_title NanumSquare_R v-center">기부코드별 기부금의 합계</li>
							</ul>
						</div>
						
						<div class="group_table hasSubType mar-bot-30">
							<table class="hasHead NanumSquare_R mar-bot-5">
								<colgroup>
									<col width="12%">
									<col width="12%">
									<col width="12%">
									<col width="12%">
									<col width="12%">
									<col width="11%">
									<col width="11%">
									<col width="10%">
									<col width="10%">
								</colgroup>
								<thead>
									<tr>
										<th rowspan="2">기부자 구분</th>
										<th rowspan="2">총계</th>
										<th colspan="5">공제대상 기부금</th>
										<th colspan="2">공제제외 기부금</th>
									</tr>
									<tr>
										<th>법정기부금</th>
										<th>정치자금기부금</th>
										<th>종교단체외지정기부금</th>
										<th>종교단체지정기부금</th>
										<th>우리사주조합기부금</th>
										<th>기부장려금신청금액</th>
										<th>기타</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="subTit">합계</td>
										<td class="value sort_right disable"></td>
										<td class="value sort_right disable">1,350,000</td>
										<td class="value sort_right disable">500,000</td>
										<td class="value sort_right disable">3,400,000</td>
										<td class="value sort_right disable">80,000</td>
										<td class="value sort_right disable">0</td>
										<td class="value sort_right disable">0</td>
										<td class="value sort_right disable">0</td>
									</tr>
									<tr>
										<td class="subTit">본인</td>
										<td class="value sort_right"></td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
									</tr>
									<tr>
										<td class="subTit">배우자</td>
										<td class="value sort_right"></td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
									</tr>
									<tr>
										<td class="subTit">직계비속</td>
										<td class="value sort_right"></td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
									</tr>
									<tr>
										<td class="subTit">직계존속</td>
										<td class="value sort_right"></td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
									</tr>
									<tr>
										<td class="subTit">형제자매</td>
										<td class="value sort_right"></td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
									</tr>
									<tr>
										<td class="subTit">그 외</td>
										<td class="value sort_right"></td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
										<td class="value sort_right">0</td>
									</tr>
								</tbody>
							</table>
						</div>
						
						<div class="grid_field full">
							<ul class="">
								<li class="table_title NanumSquare_R v-center">기부금 조정 명세</li>
								<li class="btns NanumSquare_R field_sort_right">
									<a class="btn_type type1">이월분 입력</a>
									<a class="btn_type type2">선택삭제</a>
									<a class="btn_type type1">이월 기부금 반영</a>
								</li>
							</ul>
						</div>
						
						<div class="group_table hasSubType mar-bot-30">
							<table class="hasHead NanumSquare_R mar-bot-5">
								<colgroup>
									<col width="4%">
									<col width="12%">
									<col width="14%">
									<col width="14%">
									<col width="14%">
									<col width="14%">
									<col width="14%">
									<col width="14%">
								</colgroup>
								<thead>
									<tr>
										<th class="chk_area"><input type="checkbox"></th>
										<th>기부금 코드</th>
										<th>기부연도</th>
										<th>기부금액</th>
										<th>전년까지 공제된 금액</th>
										<th>공제대상기부금</th>
										<th>이월금액</th>
										<th>소멸금액</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="chk_area"><input type="checkbox"></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value="">홍길동</option>
											</select></td>
										<td class="value disable"><input type="text" class="input_txt" disabled value="2017"></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled value="100,000"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="1,078,000" ></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled value="128,000"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="1,078,000" ></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled value="124,000"></td>
									</tr>
									<tr>
										<td class="chk_area"><input type="checkbox"></td>
										<td class="value">
											<select class="NanumSquare_R">
												<option value="">홍길동</option>
											</select></td>
										<td class="value"><input type="text" class="input_txt" value="2017"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="100,000"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="1,078,000" ></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="128,000"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="1,078,000" ></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="124,000"></td>
									</tr>
								</tbody>
							</table>
						</div>
					
					</div> <!-- group_fold -->
