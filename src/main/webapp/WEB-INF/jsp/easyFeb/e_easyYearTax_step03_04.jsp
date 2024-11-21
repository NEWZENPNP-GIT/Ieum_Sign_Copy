<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
					<div class="group_tit skyblue mar-bot-5">
						<div class="icon"></div>
						<div class="text NanumSquare_R">신용카드 등</div>
						<div class="btns">
							<a class="btn_type NanumSquare_R">문의 </a>
						</div>
					</div>
					
					<!-- <div class="sub_header NanumSquare_R">
						<div><a class="btn_type btn_question">문의 </a></div>
						<div class="sub_tit NanumSquare_R">보험료 내역</div>
						<div class="btn_drop">
							<label for="group-1" class="btn_type btn_fold"></label>
						</div>
					</div> -->
					
					<div class="group_fold ">
						<div class="grid_field full">
							<ul class="">
								<li><div class="table_title NanumSquare_R v-center"><span class="icon">신용카드 등 최소금액(총급여의 25%)</span><span class="price NanumSquare_R underline">11,250,000</span><span class="unit">원</span></div></li>
								<li class="btns NanumRound field_sort_right">
									<a class="btn_type btn_addSubmit">추가 서류 제출</a>
								</li>
							</ul>
						</div>
						
						<div class="group_table hasSubType_thin">
							<table class="hasHead NanumSquare_R">
								<colgroup>
									<col width="3%">
									<col width="9%">
									<col width="8%">
									<col width="9%">
									<col width="10%">
									<col width="10%">
									<col width="10%">
									<col width="11%">
									<col width="10%">
									<col width="10%">
									<col width="10%">
								</colgroup>
								<thead>
									<tr>
										<th colspan="4">국세청 자료</th>
										<th colspan="7">회사(근무지)자료</th>
									</tr>
									<tr>
										<th colspan="2">사용자</th>
										<th>자료구분</th>
										<th>기간 구분</th>
										<th class="sub_total">소계</th>
										<th>신용카드</th>
										<th>현금영수증</th>
										<th>직불ㆍ선불카드</th>
										<th>도서공연</th>
										<th>전통시장</th>
										<th>대중교통</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="chk_area"><input type="checkbox"></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value=""></option>
											</select></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value=""></option>
											</select></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value=""></option>
											</select></td>
										<td class="value sort_right disable sub_total">1,708,000</td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled></td>
										<td class="value"><input type="text" class="input_txt sort_right" ></td>
										<td class="value"><input type="text" class="input_txt sort_right"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="200,800"></td>
									</tr>
									<tr class="last-child">
										<td class="chk_area"><input type="checkbox"></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value=""></option>
											</select></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value=""></option>
											</select></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value=""></option>
											</select></td>
										<td class="value sort_right disable sub_total">1,708,000</td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled value="0"></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled value="0"></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled value="0"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="0"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="0"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="200,800"></td>
									</tr>
									<tr class="tfoot">
										<td colspan="4">합계</td>
										<td class="value sort_right sub_total">1,703,000</td>
										<td class="value sort_right">1,704,000</td>
										<td class="value sort_right">1,705,000</td>
										<td class="value sort_right">1,707,000</td>
										<td class="value sort_right">1,706,000</td>
										<td class="value sort_right">1,707,000</td>
										<td class="value sort_right">1,708,000</td>
									</tr>
								</tbody>
							</table>
						</div>
					</div> <!-- group_fold -->
