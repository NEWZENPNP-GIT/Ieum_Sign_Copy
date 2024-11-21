<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
					<div class="group_tit skyblue mar-bot-5">
						<div class="icon"></div>
						<div class="text NanumSquare_R">연금계좌</div>
						<div class="btns">
							<a class="btn_type NanumSquare_R">문의 </a>
						</div>
					</div>
					
					<div class="group_fold ">
						<div class="group_table description mar-top-15">
							<table class="hasHead NanumSquare_R mar-bot-5">
								<colgroup>
									<col width="20%">
									<col width="40%">
									<col width="40%">
								</colgroup>
								<thead>
									<tr>
										<th>입력할 항목</th>
										<th colspan="2">항목별 요약설명 및 공제요건</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td class="subTit">과학기술인공제</td>
										<td class="sort_left">과학기술인공제회법에 따라 근로자가 부담하는 부담금</td>
										<td rowspan="3" class="descript rows-bot">
											<dl class="dot">
												<dt class="mar-top-3">*</dt><dd>연금계좌 세액공제 대상금액 : 연 700만원 한도(전체)<br>
												연금저축 납입액 : 연 400만원 한도 (총급여 1억2천만원 또는   종합소득금액 1억원 초과자 300만원)</dd>
											</dl>
											<dl class="dot">
												<dt  class="mar-top-3">*</dt><dd>세액공제율 : 연금계좌에 납입한 금액의 12%<br>
												 (총급여 5,500만원 이하인 경우 15%)
												</dd>
											</dl>
										</td>
									</tr>
									<tr>
										<td class="subTit">근로자 퇴직연금</td>
										<td class="cols-right sort_left">근로자퇴직급여보장법에 따라 확정 기여형(DC형) 퇴직연금제도 또는 개인형퇴직연금(IRP)제도에 근로자가 부담하는 부담금<br>
										   (확정기여형 퇴직연금 등 회사부담액 제외)
										</td>
									</tr>
									<tr>
										<td class="subTit">연금저축</td>
										<td class="cols-right sort_left">과학기술인공제회법에 따라 근로자가 부담하는 부담금</td>
									</tr>
									
								</tbody>
							</table>
						</div>
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
									<col width="11%">
									<col width="11%">
									<col width="17%">
									<col width="17%">
									<col width="10%">
									<col width="10%">
									<col width="10%">
									<col width="10%">
								</colgroup>
								<thead>
									<tr>
										<th class="chk_area"><input type="checkbox"></th>
										<th>자료구분</th>
										<th>퇴직연금 구분</th>
										<th>금융회사 등 명칭</th>
										<th>계좌번호(또는 증권번호)</th>
										<th>납입금액</th>
										<th>차감금액</th>
										<th>합계</th>
										<th>공제대상금액</th>
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
										<td class="value disable"><input type="text" class="input_txt" disabled value="중소기업은행"></td>
										<td class="value disable"><input type="text" class="input_txt" disabled></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled></td>
										<td class="value"><input type="text" class="input_txt sort_right" ></td>
										<td class="value disable"><input type="text" class="input_txt sort_right" disabled></td>
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
										<td class="value "><input type="text" class="input_txt"  value=""></td>
										<td class="value "><input type="text" class="input_txt" ></td>
										<td class="value "><input type="text" class="input_txt sort_right" ></td>
										<td class="value"><input type="text" class="input_txt sort_right" ></td>
										<td class="value "><input type="text" class="input_txt sort_right" ></td>
										<td class="value "><input type="text" class="input_txt sort_right" ></td>
									</tr>
								</tbody>
							</table>
						</div>
					</div> <!-- group_fold -->
