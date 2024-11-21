<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
					<div class="group_tit skyblue mar-bot-5">
						<div class="icon"></div>
						<div class="text NanumSquare_R">의료비</div>
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
						<div class="sub_field">
							<span class="dec col_red NanumSquare_R">※ 의료비 지출액이 최소금액에 도달하지 않은 경우는 작성하지 않습니다.</span>
						</div>
						<!-- <div class="notice_wrap nonborder NanumSquare_R ">
							<div class="notice_con bg_orange col_darkred border-5 sort_center pad_thin10">
								※ 의료비 지출액이 최소금액에 도달하지 않은 경우는 작성하지 않습니다.
							</div>
						</div> -->
						<div class="grid_field full">
							<ul class="">
								<li><div class="table_title NanumSquare_R v-center"><span class="col_red">의료비 최소금액(총급여의 3% 초과)</span><span class="price NanumSquare_R underline">11,250,000</span><span class="unit">원</span></div></li>
								<li class="btns NanumRound field_sort_right">
									<a class="btn_type btn_addSubmit">추가 서류 제출</a>
								</li>
							</ul>
						</div>
						
						<div class="group_table hasSubType mar-bot-30">
							<table class="hasHead NanumSquare_R mar-bot-5">
								<colgroup>
									<col width="4%">
									<col width="8%">
									<col width="8%">
									<col width="8%">
									<col width="8%">
									<col width="9%">
									<col width="9%">
									<col width="6%">
									<col width="8%">
									<col width="8%">
									<col width="8%">
									<col width="8%">
								</colgroup>
								<thead>
									<tr>
										<th class="chk_area"><input type="checkbox"></th>
										<th>대상자</th>
										<th>공제종류</th>
										<th>자료구분</th>
										<th>의료비증빙코드</th>
										<th>지급처 사업자등록번호</th>
										<th>상호</th>
										<th>건수</th>
										<th>의료비유형</th>
										<th>지출액</th>
										<th>차감액</th>
										<th>합계</th>
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
										<td class="value disable"><select class="NanumSquare_R" disabled>
												<option value=""></option>
											</select></td>
										<td class="value"><input type="text" class="input_txt"></td>
										<td class="value"><input type="text" class="input_txt"></td>
										<td class="value"><input type="text" class="input_txt" value="1" ></td>
										<td class="value"><select class="NanumSquare_R">
												<option value=""></option>
											</select></td>
										<td class="value"><input type="text" class="input_txt sort_right"></td>
										<td class="value"><input type="text" class="input_txt sort_right"></td>
										<td class="value"><input type="text" class="input_txt sort_right"></td>
									</tr>
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
										<td class="value disable"><select class="NanumSquare_R" disabled>
												<option value=""></option>
											</select></td>
										<td class="value"><input type="text" class="input_txt" disabled></td>
										<td class="value"><input type="text" class="input_txt"></td>
										<td class="value"><input type="text" class="input_txt" value="1" ></td>
										<td class="value"><select class="NanumSquare_R">
												<option value=""></option>
											</select></td>
										<td class="value"><input type="text" class="input_txt sort_right"></td>
										<td class="value"><input type="text" class="input_txt sort_right"></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled></td>
									</tr>
								</tbody>
							</table>
						</div>
						
					</div> <!-- group_fold -->

