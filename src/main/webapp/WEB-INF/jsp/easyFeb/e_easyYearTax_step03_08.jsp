<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
						<div class="group_tit skyblue">
							<div class="icon"></div>
							<div class="text NanumSquare_R">공제항목별 지출명세 <span class="dec col_red NanumSquare_R">※ 관리자(최종) 금액이 최종 연말정산 계산하기에 반영됩니다. </span></div>
							<div class="btns">
								<a class="btn_type NanumSquare_R">문의 </a>
							</div>
						</div>
					</div>
					<!--  group_column : 데이터를 가로 추가할수 있는 테이블형 -->
					<!--  group-cell : +&-기능을 사용하는 데이터를 가로 추가할수 있는 테이블형 -->
					<div class="group-cell NanumSquare_R">
						<div class="fix_viewer">
							<ul class="fix_column cell-div">
								<li class="thead text_center col_merger2">공제항목명</li>
								<li class="cell_headbox">소득공제 명세</li>
								<li class="tbody itemgroup">
									<input id="empGroup-1" type="checkbox" class="dep-1 hidden">
									<div class="item-gnb">
										<label for="empGroup-1" class="btn_type Material_icons icon" ></label>
										<label for="empGroup-1" class="label tit">연금 보험료공제 </label>
									</div>
									<ul class="item-list">
										<li class="nav">국민연금보험료<a class="btn_type btn_edit">수정</a></li>
										<li class="nav">국민연금 외 공적 연금 보험료<a class="btn_type btn_edit">수정</a></li>
									</ul>
								</li>
								<li class="tbody itemgroup">
									<input id="empGroup-2" type="checkbox" class="dep-1 hidden">
									<div class="item-gnb">
										<label for="empGroup-2"class="btn_type Material_icons icon"></label>
										<label for="empGroup-2"class="label tit">특별소득공제 </label>
									</div>
									<ul class="item-list">
										<li class="nav">국민건강보험료<a class="btn_type btn_edit">수정</a></li>
										<li class="nav">장기요양보험료</li>
										<li class="nav">고용보험료</li>
										<li class="sub-nav">
											<input id="sub-group-3" type="checkbox" class="dep-2 hidden">
											<div class="sub-item-gnb">
												<a class="btn_type btn_edit">수정</a>
												<label for="sub-group-3" class="btn_type Material_icons icon"></label>
												<label for="sub-group-3"class="label sub-tit" >주택임차차입금원리금상환액</label>
											</div>
											<ul class="sub-item-list">
												<li>대출기관</li>
												<li>거주자</li>
											</ul>
										</li>
										<li class="nav">장기주택저당차입금이자상환액<a class="btn_type btn_edit">수정</a></li>
									</ul>
								</li>
								
								<li class="tbody"><div class="item-gnb">개인연금저축 <a class="btn_type btn_edit">수정</a></div></li>
								<li class="tbody"><div class="item-gnb">주택마련저축<a class="btn_type btn_edit">수정</a></div></li>
								<li class="tbody"><div class="item-gnb">신용카드 <a class="btn_type btn_edit">수정</a></div></li>
								<li class="tbody"><div class="item-gnb">장기집합투자증권저축 <a class="btn_type btn_edit">수정</a></div></li>
								<li class="tbody itemgroup">
									<input id="empGroup-4" type="checkbox" class="dep-1 hidden">
									<div class="item-gnb">
										<label for="empGroup-4" class="btn_type Material_icons icon"></label>
										<label for="empGroup-4"class="label tit" >그밖의소득공제</label>
									</div>
									<ul class="item-list">
										<li class="nav">소기업·소상공인공제부금<a class="btn_type btn_edit">수정</a></li>
										<li class="nav">투자조합출자등<a class="btn_type btn_edit">수정</a></li>
										<li class="nav">우리사주조합출연금<a class="btn_type btn_edit">수정</a></li>
										<li class="nav">고용유지중소기업근로자<a class="btn_type btn_edit">수정</a></li>
									</ul>
								</li>
							</ul>
							<ul class="fix_column cell-div child-2">
								<li class="cell_headbox">세액감면ㆍ공제명세</li>
								<li class="tbody itemgroup">
									<input id="empGroup-5" type="checkbox" class="dep-1 hidden">
									<div class="item-gnb">
										<label for="empGroup-5" class="btn_type Material_icons icon"></label>
										<label for="empGroup-5"class="label tit" >연금계좌</label>
									</div>
									<ul class="item-list">
										<li class="nav">과학기술인공제<a class="btn_type btn_edit">수정</a></li>
										<li class="nav">퇴직연금<a class="btn_type btn_edit">수정</a></li>
										<li class="nav">연금저축<a class="btn_type btn_edit">수정</a></li>
									</ul>
								</li>
								<li class="tbody itemgroup">
									<input id="empGroup-6" type="checkbox" class="dep-1 hidden">
									<div class="item-gnb">
										<label for="empGroup-6" class="btn_type Material_icons icon"></label>
										<label for="empGroup-6"class="label tit" >특별세액공제</label>
									</div>
									<ul class="item-list">
										<li class="sub-nav">
											<input id="sub-group-7" type="checkbox" class="dep-2 hidden">
											<div class="sub-item-gnb">
												<a class="btn_type btn_edit">수정</a>
												<label for="sub-group-7" class="btn_type Material_icons icon"></label>
												<label for="sub-group-7"class="label sub-tit" >보험료</label>
											</div>
											<ul class="sub-item-list">
												<li>보장성</li>
												<li>장애인전용보장성</li>
											</ul>
										</li>
										<li class="sub-nav">
											<input id="sub-group-8" type="checkbox" class="dep-2 hidden">
											<div class="sub-item-gnb">
												<a class="btn_type btn_edit">수정</a>
												<label for="sub-group-8" class="btn_type Material_icons icon"></label>
												<label for="sub-group-8"class="label sub-tit" >의료비_안경구입비</label>
											</div>
											<ul class="sub-item-list">
												<li>본인·65세이상자·장애인</li>
												<li>난임시술비</li>
												<li>그밖의공제대상자</li>
											</ul>
										</li>
										<li class="sub-nav">
											<input id="sub-group-9" type="checkbox" class="dep-2 hidden">
											<div class="sub-item-gnb">
												<a class="btn_type btn_edit">수정</a>
												<label for="sub-group-9" class="btn_type Material_icons icon"></label>
												<label for="sub-group-9"class="label sub-tit" >교육비_교복구입비/체험학습비</label>
											</div>
											<ul class="sub-item-list">
												<li>본인</li>
												<li>취학전아동</li>
												<li>초·중·고등학교</li>
												<li>대학생(대학원 불포함)</li>
												<li>장애인</li>
											</ul>
										</li>
										<li class="sub-nav">
											<input id="sub-group-10" type="checkbox" class="dep-2 hidden">
											<div class="sub-item-gnb">
												<a class="btn_type btn_edit">수정</a>
												<label for="sub-group-10" class="btn_type Material_icons icon"></label>
												<label for="sub-group-10"class="label sub-tit" >기부금</label>
											</div>
											<ul class="sub-item-list">
												<li>정치자금기부금</li>
												<li>법정기부금</li>
												<li>우리사주기부금</li>
												<li>종교단체외지정기부금</li>
												<li>종교단체지정기부금</li>
											</ul>
										</li>
									</ul>
								</li>
								<li class="tbody"><div class="item-gnb">월세액<a class="btn_type btn_edit">수정</a></div></li>
								<li class="tbody"><div class="item-gnb">주택차입금<a class="btn_type btn_edit">수정</a></div></li>
								<li class="tbody"><div class="item-gnb">세액감면<a class="btn_type btn_edit">수정</a></div></li>
								<li class="tbody"><div class="item-gnb">외국납부세액<a class="btn_type btn_edit">수정</a></div></li>
							</ul>
						</div>	
						<div class="column_viewer">
							<div class="column_data employee">
								<div class="merge-cell">근로자</div>
								<div class="merge-cell">
									<ul class="data-list">
										<li class="thead text_center">① 국세청자료</li>
										<li class="tbody itemgroup empGroup-1">
											<div class="item-gnb data">2,000,000</div>
											<ul class="item-list-data">
												<li class="data">0</li>
												<li class="data">0</li>
											</ul>
										</li>
										<li class="tbody itemgroup empGroup-2">
											<div class="item-gnb data">110,000</div>
											<ul class="item-list-data">
												<li class="data">1</li>
												<li class="data">2</li>
												<li class="data">3</li>
												<li class="sub-nav-data sub-group-3">
													<div class="sub-item-gnb data">2,000,000</div>
													<ul class="sub-item-list-data">
														<li class="data">7,000,000</li>
														<li class="data">8</li>
													</ul>
												</li>
												<li class="data">9,000,000</li>
											</ul>
										</li>
										<li class="tbody"><div class="item-gnb data">100,000,000</div></li>
										<li class="tbody"><div class="item-gnb data">5,000,000</div></li>
										<li class="tbody"><div class="item-gnb data">5,000,000</div></li>
										<li class="tbody"><div class="item-gnb data">6,000,000</div></li>
										<li class="tbody itemgroup last-child empGroup-4">
											<div class="item-gnb data">110,000,000</div>
											<ul class="item-list-data">
												<li class="data">1</li>
												<li class="data">2</li>
												<li class="data">3</li>
												<li class="data last-child">4</li>
											</ul>
										</li>
										<li class="tbody itemgroup empGroup-5">
											<div class="item-gnb data">2,000,000</div>
											<ul class="item-list-data">
												<li class="data">5,000,000</li>
												<li class="data">5,000,000</li>
												<li class="data">5,000,000</li>
											</ul>
										</li>
										<li class="tbody itemgroup empGroup-6">
											<div class="item-gnb data">5,000,000</div>
											<ul class="item-list-data ">
												<li class="sub-nav-data sub-group-7">
													<div class="sub-item-gnb data">9,000,000</div>
													<ul class="sub-item-list-data">
														<li class="data">5,000,000</li>
														<li class="data">1,000,000</li>
													</ul>
												</li>
												<li class="sub-nav-data sub-group-8">
													<input id="" type="checkbox" class="dep-2 hidden">
													<div class="sub-item-gnb data">150,000</div>
													<ul class="sub-item-list-data ">
														<li class="data">6,000,000</li>
														<li class="data">2,000,000</li>
														<li class="data">2,000,000</li>
													</ul>
												</li>
												<li class="sub-nav-data sub-group-9">
													<div class="sub-item-gnb data">50,000,000</div>
													<ul class="sub-item-list-data">
														<li class="data">400,000</li>
														<li class="data">10,000</li>
														<li class="data">1,000</li>
														<li class="data">2,000</li>
														<li class="data">4,000</li>
													</ul>
												</li>
												<li class="sub-nav-data sub-group-10">
													<div class="sub-item-gnb data">110,000,000</div>
													<ul class="sub-item-list-data">
														<li class="data">70,000</li>
														<li class="data">600,000</li>
														<li class="data">70,000</li>
														<li class="data">20,000</li>
														<li class="data">40,000</li>
													</ul>
												</li>
											</ul>
										</li>
										<li class="tbody"><div class="item-gnb data">100,000</div></li>
										<li class="tbody"><div class="item-gnb data">60,000</div></li>
										<li class="tbody"><div class="item-gnb data">550,000</div></li>
										<li class="tbody"><div class="item-gnb data">90,000</div></li>
									</ul>
									
									<ul class="data-list">
										<li class="thead text_center">② 기타자료</li>
										<li class="tbody itemgroup empGroup-1">
											<div class="item-gnb data">2,000,000</div>
											<ul class="item-list-data">
												<li class="data">2,000,000</li>
												<li class="data">2,000,000</li>
											</ul>
										</li>
										<li class="tbody itemgroup empGroup-2">
											<div class="item-gnb data">110,000</div>
											<ul class="item-list-data">
												<li class="data">1</li>
												<li class="data">2</li>
												<li class="data">3</li>
												<li class="sub-nav-data sub-group-3">
													<div class="sub-item-gnb data">2,000,000</div>
													<ul class="sub-item-list-data">
														<li class="data">7,000,000</li>
														<li class="data">8</li>
													</ul>
												</li>
												<li class="data">9,000,000</li>
											</ul>
										</li>
										<li class="tbody"><div class="item-gnb data">100,000,000</div></li>
										<li class="tbody"><div class="item-gnb data">400,000</div></li>
										<li class="tbody"><div class="item-gnb data">500,000</div></li>
										<li class="tbody"><div class="item-gnb data">6,000,000</div></li>
										<li class="tbody itemgroup last-child empGroup-4">
											<div class="item-gnb data">110,000,000</div>
											<ul class="item-list-data">
												<li class="data">5</li>
												<li class="data">6</li>
												<li class="data">7</li>
												<li class="data last-child">8</li>
											</ul>
										</li>
										<li class="tbody itemgroup empGroup-5">
											<div class="item-gnb data">2,000,000</div>
											<ul class="item-list-data">
												<li class="data">20,000</li>
												<li class="data">20,000</li>
												<li class="data">20,000</li>
											</ul>
										</li>
										<li class="tbody itemgroup empGroup-6">
											<div class="item-gnb data">200,000</div>
											<ul class="item-list-data ">
												<li class="sub-nav-data sub-group-7">
													<div class="sub-item-gnb data">9,000,000</div>
													<ul class="sub-item-list-data ">
														<li class="data">f</li>
														<li class="data">f</li>
													</ul>
												</li>
												<li class="sub-nav-data sub-group-8">
													<input id="" type="checkbox" class="dep-2 hidden">
													<div class="sub-item-gnb data">150,000</div>
													<ul class="sub-item-list-data">
														<li class="data">6,000,000</li>
														<li class="data">2,000,000</li>
														<li class="data">2,000,000</li>
													</ul>
												</li>
												<li class="sub-nav-data sub-group-9">
													<div class="sub-item-gnb data">50,000,000</div>
													<ul class="sub-item-list-data">
														<li class="data">6,000,000</li>
														<li class="data">9,000,000</li>
														<li class="data">6,000,000</li>
														<li class="data">2,000,000</li>
														<li class="data">6,000,000</li>
													</ul>
												</li>
												<li class="sub-nav-data sub-group-10">
													<div class="sub-item-gnb data">110,000,000</div>
													<ul class="sub-item-list-data">
														<li class="data">100,000,000</li>
														<li class="data">66,000,000</li>
														<li class="data">9,000,000</li>
														<li class="data">50,000,000</li>
														<li class="data">7,000,000</li>
													</ul>
												</li>
											</ul>
										</li>
										<li class="tbody"><div class="item-gnb data">2,000,000</div></li>
										<li class="tbody"><div class="item-gnb data">2,000,000</div></li>
										<li class="tbody"><div class="item-gnb data">2,000,000</div></li>
										<li class="tbody"><div class="item-gnb data">2,000,000</div></li>
									</ul>
									<ul class="data-list sum_data">
										<li class="thead text_center">합계 (①+②)</li>
										<li class="tbody itemgroup empGroup-1">
											<div class="item-gnb data">2,000,000</div>
											<ul class="item-list-data">
												<li class="data">2,000,000</li>
												<li class="data">2,000,000</li>
											</ul>
										</li>
										<li class="tbody itemgroup empGroup-2">
											<div class="item-gnb data">110,000</div>
											<ul class="item-list-data">
												<li class="data">101,000</li>
												<li class="data">200,000</li>
												<li class="data">300,000</li>
												<li class="sub-nav-data sub-group-3">
													<div class="sub-item-gnb data">2,000,000</div>
													<ul class="sub-item-list-data">
														<li class="data">7,000,000</li>
														<li class="data">2,000,000</li>
													</ul>
												</li>
												<li class="data">9,000,000</li>
											</ul>
										</li>
										<li class="tbody"><div class="item-gnb data">100,000,000</div></li>
										<li class="tbody"><div class="item-gnb data">2,000,000</div></li>
										<li class="tbody"><div class="item-gnb data">2,000,000</div></li>
										<li class="tbody"><div class="item-gnb data">6,000,000</div></li>
										<li class="tbody itemgroup last-child empGroup-4">
											<div class="item-gnb data">110,000,000</div>
											<ul class="item-list-data">
												<li class="data">2,000,000</li>
												<li class="data">6,000,000</li>
												<li class="data">6,000</li>
												<li class="data last-child">3,000,000</li>
											</ul>
										</li>
										<li class="tbody itemgroup empGroup-5">
											<div class="item-gnb data">2,000,000</div>
											<ul class="item-list-data">
												<li class="data">6,000,000</li>
												<li class="data">60,000</li>
												<li class="data">70,000</li>
											</ul>
										</li>
										<li class="tbody itemgroup empGroup-6">
											<div class="item-gnb data">70,000</div>
											<ul class="item-list-data " >
												<li class="sub-nav-data sub-group-7">
													<div class="sub-item-gnb data">9,000,000</div>
													<ul class="sub-item-list-data ">
														<li class="data">s</li>
														<li class="data">s</li>
													</ul>
												</li>
												<li class="sub-nav-data sub-group-8">
													<input id="" type="checkbox" class="dep-2 hidden">
													<div class="sub-item-gnb data">150,000</div>
													<ul class="sub-item-list-data">
														<li class="data">6,000,000</li>
														<li class="data">2,000,000</li>
														<li class="data">2,000,000</li>
													</ul>
												</li>
												<li class="sub-nav-data sub-group-9">
													<div class="sub-item-gnb data">50,000,000</div>
													<ul class="sub-item-list-data">
														<li class="data">6,000,000</li>
														<li class="data">9,000,000</li>
														<li class="data">6,000,000</li>
														<li class="data">2,000,000</li>
														<li class="data">6,000,000</li>
													</ul>
												</li>
												<li class="sub-nav-data sub-group-10">
													<div class="sub-item-gnb data">110,000,000</div>
													<ul class="sub-item-list-data">
														<li class="data">100,000,000</li>
														<li class="data">66,000,000</li>
														<li class="data">9,000,000</li>
														<li class="data">50,000,000</li>
														<li class="data">7,000,000</li>
													</ul>
												</li>
											</ul>
										</li>
										<li class="tbody"><div class="item-gnb data">500,000</div></li>
										<li class="tbody"><div class="item-gnb data">200,000</div></li>
										<li class="tbody"><div class="item-gnb data">2,000,000</div></li>
										<li class="tbody"><div class="item-gnb data">600,000</div></li>
									</ul>
								</div>
							</div> <!--  column_view -->
						</div>
					</div> <!-- group_column -->
