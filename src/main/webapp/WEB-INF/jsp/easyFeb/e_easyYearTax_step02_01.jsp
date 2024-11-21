<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/jsp/inc/noCache.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" scope="request" />
<link rel="stylesheet" type="text/css" href="/css/material-icons.min.css">
<link rel="stylesheet" type="text/css" href="/css/yearTax_common.css">
	<div id="yearTax_wrap" class="yearTax easy">
		<div class="container">
			<div class="location"><span class="loc_main NanumSquare_R">연말정산 >  </span><span class="loc_sub NanumSquare_R">부양가족</span></div>
			
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
					<div class="box current">
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
			<div class="que_content">
				<div class="text NanumRound">
					<span>2. 아래 부양가족 현황 변동이 있습니까?</span>
				</div>				
				<div id="que_btns" class="NanumRound">
					<a class="btn_type type1">예</a>
					<a class="btn_type type2">아니오</a>
				</div>
			</div>
			<div class="pagtit">
				<span class="NanumSquare_R">부양 가족</span>
				<ul class="top_items NanumSquare_R">
					<li><a class="btn_type icon_file ">첨부 파일 <span class="icon icon_file"></span></a></li>
				</ul>
				
			</div>
			<div class="contents">
			
				<div class="group_wrap">
					<div class="group_tit skyblue">
						<div class="icon"></div>
						<div class="text NanumSquare_R">부양가족(인적)공제 명세</div>
						<div class="btns">
							<a class="btn_type NanumSquare_R">문의 </a>
						</div>
					</div>
				
					<div class="grid_field full">
						<ul class="">
							<li class="btns NanumSquare_R field_sort_right">
								<a class="btn_type type1">부양가족추가<span class="icon add"></span></a>
								<a class="btn_type type2">선택삭제</a>
							</li>
						</ul>
					</div>
					<div class="group_table">
						<table class="hasHead NanumSquare_R">
							<colgroup>
								<col width="4%">
								<col width="7%">
								<col width="8%">
								<col width="10%">
								<col width="15%">
								<col width="7%">
								<col width="7%">
								<col width="7%">
								<col width="7%">
								<col width="7%">
								<col width="7%">
								<col width="7%">
								<col width="7%">
							</colgroup>
							<thead>
								<tr>
									<th class="chk_area"><input type="checkbox"> </th>
									<th>연말관계</th>
									<th>내외국인</th>
									<th>성명</th>
									<th>주민등록번호</th>
									<th>나이</th>
									<th>기본공제</th>
									<th>부녀자</th>
									<th>한부모</th>
									<th>경로우대</th>
									<th>장애인</th>
									<th>자녀</th>
									<th>출산입양</th>
								</tr>
									
							</thead>
							<tbody>
								<tr>
									<td class="chk_area"><input type="checkbox"> </td>
									<td class="value"><input type="text" class="input_txt" value="본인"></td>
									<td class="value"><input type="text" class="input_txt" value="내국인"></td>
									<td class="value"><input type="text" class="input_txt" value="이름입니다"></td>
									<td class="value"><input type="text" class="input_txt" value="899999-9999999"></td>
									<td class="value">34</td>
									<td class="value">
										<select class="NanumSquare_R">
											<option value="" >Y</option>
										</select>
									</td>
									<td class="value">
										<select class="NanumSquare_R">
											<option value="" >Y</option>
										</select>
									</td>
									<td class="value">
										<select class="NanumSquare_R">
											<option value="" >Y</option>
										</select>
									</td>
									<td class="value">
										<select class="NanumSquare_R">
											<option value="" >Y</option>
										</select>
									</td>
									<td class="value">
										<select class="NanumSquare_R">
											<option value="" >Y</option>
										</select>
									</td>
									<td class="value">
										<select class="NanumSquare_R">
											<option value="" >Y</option>
										</select>
									</td>
									<td class="value">
										<select class="NanumSquare_R">
											<option value="" >Y</option>
										</select>
									</td>
								</tr>
								<tr>
									<td class="chk_area"><input type="checkbox"> </td>
									<td class="value"><input type="text" class="input_txt" ></td>
									<td class="value"><input type="text" class="input_txt" placeholder="이름 입력"></td>
									<td class="value"><input type="text" class="input_txt"></td>
									<td class="value"><input type="text" class="input_txt"></td>
									<td class="value">34</td>
									<td class="value">
										<select class="NanumSquare_R disable">
											<option value="" >Y</option>
										</select>
									</td>
									<td class="value">
										<select class="NanumSquare_R">
											<option value="" >Y</option>
										</select>
									</td>
									<td class="value">
										<select class="NanumSquare_R">
											<option value="" >Y</option>
										</select>
									</td>
									<td class="value">
										<div class="default_box disable sort_center">N</div>
									</td>
									<td class="value">
										<select class="NanumSquare_R">
											<option value="" >Y</option>
										</select>
									</td>
									<td class="value">
										<select class="NanumSquare_R">
											<option value="" >Y</option>
										</select>
									</td>
									<td class="value">
										<select class="NanumSquare_R">
											<option value="" >Y</option>
										</select>
									</td>
								</tr>
							</tbody>
						</table>
					</div> <!--  group_table -->
				</div>
			</div> <!--  contents -->
			
			<div class="group_btns foot NanumSquare_R">
				<a class="btn_type type1">＜  이전</a>
				<a class="btn_type type2"><span class="icon_save Material_icons"></span>저장</a>
				<a class="btn_type type3">다음 ＞</a>
			</div>
		</div> <!--  container -->
		
		<div class="layerPop_wrap">
			<div class="popup_bg small">
			
				<div class="popup_tit">
					<div class="popup_close"><a class="btn_type btn_close"></a></div>
				</div>
				<ul class="popup_content">
					<li class="icon"></li>
					<li class="text NanumRound">
						<span>부양가족에 변동내역이 있으시면 </span>
						<span>주민등록표등본 또는  가족관계증명서를</span>
						<span> 첨부하시기 바랍니다.</span>
					</li>
				</ul>
			</div>
		</div>
	</div>

<script type="text/javascript">
	$(document).ready(function() {
		
		// 이전화면
		$(".btn_type.type1").on("click", function(){
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
		
		// 다음화면
		$(".btn_type.type3").on("click", function(){
			var url = rootPath + "${ctx}/easyFeb/e_easyYearTax_step03_default.do";
			
			$("#contents_wrap").empty();
		    $(window).off("resize");
		    $.ajax({
		        url : url, 
		        success : function(result) {
		            $("#contents_wrap").html(result);
		        }
		    });
		});
		
		$(".btn_close").click(function(e){
			$(this).parent().parent().parent().parent().addClass("hidden");
		});	
	});

</script>