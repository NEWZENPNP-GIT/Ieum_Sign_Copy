<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
					<div class="group_tit skyblue mar-bot-5">
						<div class="icon"></div>
						<div class="text NanumSquare_R">보험료</div>
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
							<span class="dec col_red NanumSquare_R">※ 보험료는 연 100만원 한도까지 자동 반영됩니다. </span>
						</div>
						<!-- <div class="notice_wrap nonborder NanumSquare_R ">
							<div class="notice_con bg_orange col_darkred border-5 sort_center pad_thin10">
								※ 보험료는 연 100만원 한도까지 자동 반영됩니다.
							</div>
						</div> -->
						<div class="grid_field full">
							<ul class="">
								<li class="btns NanumRound field_sort_right">
									<a class="btn_type btn_addSubmit">추가 서류 제출</a>
								</li>
							</ul>
						</div>
						<div class="group_table hasSubType mar-bot-5">
							<table class="hasHead NanumSquare_R mar-bot-5">
								<colgroup>
									<col width="4%">
									<col width="16%">
									<col width="16%">
									<col width="16%">
									<col width="16%">
									<col width="16%">
									<col width="16%">
								</colgroup>
								<thead>
									<tr>
										<th class="chk_area"><input type="checkbox"></th>								
										<th>대상자</th>
										<th>종제종류</th>
										<th>자료구분</th>
										<th>의료비증빙코드</th>
										<th>차감금액</th>
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
										<td class="value disable"><input type="text" class="input_txt" disabled></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled></td>
										<td class="value"><input type="text" class="input_txt sort_right" ></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled></td>
									</tr>
									<tr>
										<td class="chk_area"><input type="checkbox"></td>
										<td class="value">
											<select class="NanumSquare_R" >
												<option value=""></option>
											</select></td>
										<td class="value disable">
											<select class="NanumSquare_R" >
												<option value=""></option>
											</select></td>
										<td class="value"><input type="text" class="input_txt" ></td>
										<td class="value"><input type="text" class="input_txt sort_right" ></td>
										<td class="value"><input type="text" class="input_txt sort_right" ></td>
										<td class="value"><input type="text" class="input_txt sort_right" ></td>
									</tr>
								</tbody>
							</table>
						</div>
						
					</div> <!-- group_fold -->