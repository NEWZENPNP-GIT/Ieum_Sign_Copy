<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
					<div class="group_tit skyblue mar-bot-5">
						<div class="icon"></div>
						<div class="text NanumSquare_R">교육비</div>
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
								<li class="btns NanumRound field_sort_right">
									<a class="btn_type btn_addSubmit">추가 서류 제출</a>
								</li>
							</ul>
						</div>
						
						<div class="group_table hasSubType mar-bot-30">
							<table class="hasHead NanumSquare_R mar-bot-5">
								<colgroup>
									<col width="4%">
									<col width="11%">
									<col width="11%">
									<col width="14%">
									<col width="10%">
									<col width="10%">
									<col width="10%">
									<col width="10%">
									<col width="10%">
									<col width="10%">
								</colgroup>
								<thead>
									<tr>
										<th class="chk_area"><input type="checkbox"></th>
										<th>피교육자</th>
										<th>자료구분</th>
										<th>공제종류</th>
										<th>공납금</th>
										<th>차감금액</th>
										<th>교복구입비</th>
										<th>차감금액</th>
										<th>체험학습비</th>
										<th>차감금액</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="chk_area"><input type="checkbox"></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value="">홍길동</option>
											</select></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value="">국세청</option>
											</select></td>
										<td class="value disable">
											<select class="NanumSquare_R" disabled>
												<option value="">초.중.고등학생 </option>
											</select></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled value="0"></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled value="1"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="1078000" ></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled value="0"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="1078000" ></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled value="0"></td>
									</tr>
									<tr>
										<td class="chk_area"><input type="checkbox"></td>
										<td class="value">
											<select class="NanumSquare_R" >
												<option value=""></option>
											</select></td>
										<td class="value">
											<select class="NanumSquare_R">
												<option value=""></option>
											</select></td>
										<td class="value">
											<select class="NanumSquare_R">
												<option value=""></option>
											</select></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="0"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="1"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="1078000" ></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="0"></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="1078000" ></td>
										<td class="value"><input type="text" class="input_txt sort_right" value="0"></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div> <!-- group_fold -->
