
// 엑셀 업로드
var isExcel = false;

// 페이지이동시 데이터변경체크
var differ;
var arrayDiffers = {};
var isChanged;

var departmentRList;
// 계약 ID
var contractId;
// 사업장 정보
var companyWorkplaceInfo;
// 검색
var searchText;

// 페이징 관련
var totalCount =  0;  // 총건수
var perPage =  10;
var currPage =  1;
var startPage =  0;
var endPage =  100;
var tempPage =  1;

var perPagenavi = ['10', '20', '30', '50', '100'];

var selectBasic = {
	사업장명: '전체',
	사업장ID: ''
  };
var selectWorkplaceInfo = []; // 사업장

var model = {
	departmentParams : {
      '계약ID' : contractId,
      '사업장ID': '',
      '사업장명':  '',
      '부서ID': '',
      '부서명':  '',
      dbMode:  'C',
      chk: false,
      febYear: ''
    }
 };


$(document).ready(function() {
	
	contractId = getCookie('yearContractId');
	
	//계약아이디가 없으면 메인으로 튕겨내기
    if (isNull(contractId)) {
    	alert( '사용불가 메뉴 입니다. 기본설정값을 확인 해주세요' );
    	window.location.href = rootPath;
    } else {
    	searchText = '';
    	
    	/* 리스트 select */
    	/*var perPageHtml = "";
    	var strSelected = "";
    	for(var idx = 0 ; idx < perPagenavi.length ; idx++){
    		
    		if(perPagenavi[idx] == perPage){
    			strSelected = "selected";
    		}else{
    			strSelected = "";
    		}
    		
    		perPageHtml += "<option "+strSelected+">"+perPagenavi[idx]+"</option>";
    	}
    	$("#perPage").html(perPageHtml);*/
    	
    	/* 검색어 엔터이벤트 */
    	$("#searchText").keyup(function(e){if(e.keyCode == 13)  getList(); });
    	
        getCompanyInfo();
    }
    
});


//사업장 정보 조회
function getCompanyInfo() {
	
	var params = { // ajax data params
		계약ID: contractId,
		startPage: 0,
		endPage: endPage
	};

	var url = rootPath + 'febsystem/getYS030List.do';
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"GET",
		data: params,
		success:function(res) {
			
			console.log("## result.success : " + res.success );
			
			if (res.success) {
				
				companyWorkplaceInfo = res['data'];
				

			    /*this.selectWorkplaceInfo = res['data'].map(option => ({
			      label: option['사업장명'],
			      value: option['사업장ID']
			    }));*/
			    selectWorkplaceInfo = res['data'];
			    
				//배열 앞에 추가
				selectWorkplaceInfo.unshift(selectBasic);
				
			    getList();
								
			}else{
				jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
			}
		},
		error:function(request,status,error){
			if (request.status=="401") {
				alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
				window.location.href = rootPath;				
			} else {
				alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
			}
		}
	});

}


// 부서등록 리스트 조회
function getList() {
	
	searchText = $("#searchText").val();
	
	var params = {
		'계약ID' : contractId,
		'부서명': searchText,
		startPage: startPage,
		endPage: endPage
	};
	
	//blockUi 호출
	fncOnBlockUI();
	 
	var url = rootPath + 'febsystem/getYS031List.do';
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"GET",
		data: params,
		success:function(res) {
			
			console.log("## result.success : " + res.success );
			
			if (res.success) {
				
				for (var index = 0; index < res['data'].length; index++) {
					res['data'][index]['chk'] = false;
					res['data'][index]['dbMode'] = 'R';
				}
				departmentRList = res['data'];
			    totalCount  = res['total'];
				
			    var paginateOption = {
			           total: Math.ceil(totalCount / perPage),
			           page: currPage
			    };  
			    
			    /*페이징 처리 숨김
			    if (totalCount > 0) {
			        pageSelection(paginateOption);
			    } else {
			        $('#page-selection').hide();
			    }*/
			    
			    			    			                    
			    /*var selectWorkplaceInfoOptions = "";
			    for(var idx = 0 ; idx < selectWorkplaceInfo.length ; idx++){
			    	selectWorkplaceInfoOptions +="<option value='"+selectWorkplaceInfo[idx]['사업장ID']+"'>"+selectWorkplaceInfo[idx]['사업장명']+"</option>";
			    }*/
			    
			    
			    
			    /*<!--
                <ng-select
                  [options]="selectWorkplaceInfo"
                  placeholder="선택하세요"
                  [(ngModel)]="row['사업장ID']"
                  (selected)="changeddbMode(i,'U')"
                  notFoundMsg="일치하는 항목이 없습니다."
                  highlightColor="#9575cd"
                  highlightTextColor="#fff">
                </ng-select>
                -->*/
			    
			    var departmentRListHtml = "";
			    var strSelected = "";
			    for(var idx = 0; idx < departmentRList.length; idx++){
			    	var row = departmentRList[idx];
			    	//console.log("# row ==> " + JSON.stringify(row));			    	
			    	
			    	if(row['dbMode'] != 'D'){
			    		departmentRListHtml +="<tr id='부서_TR_"+idx+"'>";		                
			    		departmentRListHtml +="<td class='chk_area'><input type='checkbox' id='row_chk' value='"+idx+"'> </td>";
			    		departmentRListHtml +="<td class='tit'>사업장</td>";
			    		departmentRListHtml +="<td class='value'>";			    					    		
			    		departmentRListHtml +="<select id='selectWorkplaceInfo_"+idx+"' data-placeholder='일치하는 항목이 없습니다.' class='chosen-select'>";
			    		//departmentRListHtml += selectWorkplaceInfoOptions;
			    		
			    		for(var subIdx = 0 ; subIdx < selectWorkplaceInfo.length ; subIdx++){
			    			
			    			if(row['사업장ID'] == selectWorkplaceInfo[subIdx]['사업장ID']){
			    				strSelected = "selected";
			    			}else{
			    				strSelected = "";
			    			}
			    			
			    			departmentRListHtml +="<option value='"+selectWorkplaceInfo[subIdx]['사업장ID']+"' "+strSelected+">"+selectWorkplaceInfo[subIdx]['사업장명']+"</option>";
					    }			    					    		
			    		departmentRListHtml +="</select>";						
			    		departmentRListHtml +="</td>";			                  
			    		departmentRListHtml +="<td class='tit'>부서명</td>";
			    		departmentRListHtml +="<td class='value'><input type='text' class='input_txt kor' id='row_부서명_"+idx+"' value='"+row['부서명']+"' onChange='changeddbMode("+idx+",\"U\")' ></td>";
		                departmentRListHtml +="</tr>";
			    	}
			    }
			    $("#departmentRListBody").html(departmentRListHtml);
			    
			    setDocumentEvent();
			    
			    $(".chosen-select").chosen({ width: '100%'});
				$(".chosen-select").trigger("chosen:updated");
			    
			    /*
				<tr *ngFor="let row of departmentRList; let i = index">
                <ng-container *ngIf="row['dbMode'] !=='D'">
                  <td class="chk_area"><input type="checkbox" [(ngModel)]="row['chk']"> </td>
                  <td class="tit">사업장</td>
                  <td class="value">
                  		<select id="selectWorkplaceInfo" data-placeholder="일치하는 항목이 없습니다." class="chosen-select">
                			<option value=''></option>
                		</select>						
					<!--
                    <ng-select
                      [options]="selectWorkplaceInfo"
                      placeholder="선택하세요"
                      [(ngModel)]="row['사업장ID']"
                      (selected)="changeddbMode(i,'U')"
                      notFoundMsg="일치하는 항목이 없습니다."
                      highlightColor="#9575cd"
                      highlightTextColor="#fff">
                    </ng-select>
                    -->
                  </td>
                  <td class="tit">부서명</td>
                  <td class="value"><input type="text" class="input_txt kor" [(ngModel)]="row['부서명']"  (change)="changeddbMode(i,'U')" ></td>
                </ng-container>
				 </tr>
				 */
			    
			    
			    
			}else{
				jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
			}	
			
			$.unblockUI();
		},
		error:function(request,status,error){
			$.unblockUI();
			
			if (request.status=="401") {
				alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
				window.location.href = rootPath;				
			} else {
				alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
			}
		}
	});
	
}

//부서저장
function setDepartmontInfo() {

	for (var index = 0; index < departmentRList.length; index++) {
		if (departmentRList[index]['dbMode'] != 'D') {
			departmentRList[index]['사업장ID'] = $("#selectWorkplaceInfo_"+index).val();
			departmentRList[index]['부서명'] = $("#row_부서명_"+index).val();
		}
	}
	
	//console.log("# row ==> " + JSON.stringify(departmentRList));
	
	var chkvalidation = true;
    var chkarr = [];
    for (var index = 0; index < departmentRList.length; index++) {
    	
    	if (departmentRList[index]['dbMode'] != 'D') {
    		if ( departmentRList[index]['사업장ID'] == '' ) {
    			chkvalidation = false;
    			alert("사업장을 선택해주십시오.");
    			$("#selectWorkplaceInfo_"+index).trigger('chosen:activate');
    			return false;
    		}
    		if ( departmentRList[index]['부서명'] == '' ) {
    			chkvalidation = false;
    			alert("부서명을 선택해주십시오.");
    			$("#row_부서명_"+index).focus();
    			return false;
    		}
    		var NdeptName = departmentRList[index]['부서명'];
    		var NworkplaceId = departmentRList[index]['사업장ID'];

    		// 신규등록된 부서 중복 체크
    		for (var i = 0; i < departmentRList.length; i++) {
    			if ( i != index && departmentRList[i]['dbMode'] != 'D') {
    				if (( NdeptName == departmentRList[i]['부서명'] ) && ( NworkplaceId == departmentRList[i]['사업장ID'] ) ) {
    					alert( '같은 사업장 내 중복되는 부서명이 있습니다.');
    					return false;
    				}
    			}
    		}

    		// 기존등록된 부서 중복 체크
    		chkarr.push(departmentRList[index]['사업장ID']);
    		for (var j = 0; j < index; j ++) {
    			if (  chkarr[index] ==  chkarr[j] ) {
    				if ( departmentRList[index]['부서명'] == departmentRList[j]['부서명']) {
    					alert('같은 사업장 내 중복되는 부서명이 있습니다.');
    					return false;
    				}
    			}
    		}
      }
    }

    if ( chkvalidation ) {
    	        
    	var url = rootPath + 'febsystem/saveYS031.do';
    	
    	$.ajax({
    		url:url,
    	    crossDomain : true,
    		dataType:"json",
    		type:"POST",
    		//processData: false,
    	    //contentType: false,
    		contentType: "application/json; charset=UTF-8",
    		data: JSON.stringify(departmentRList),
    		success:function(res) {			
    			if (res['success']) {
    				alert('저장 되었습니다.');
    			} else {
    				alert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.');
    			}						
    		},
    		error:function(request,status,error){
    			$.unblockUI();
    			
    			if (request.status=="401") {
    				alert("사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.");
    				window.location.href = rootPath;
    			} else {
    				alert("입력하신 정보를 다시 확인해주시기 바랍니다."+error);
    			}
    		}
    	});
    	
    	
    }
    
    /*
    if ( chkvalidation ) {
      this.service.saveList(this.departmentRList).subscribe(res => {

        if (res['success']) {
          alert('저장 되었습니다.');
          process.nextTick(function() {
            self.isChanged = false;
          });
        } else {
          alert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.');
        }
      });
    } else {
      alert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.');
    }*/

}


// 라인 추가
function addDepartmentList() {

	var setworkplace = companyWorkplaceInfo[0]['사업장ID'];

	var setdepartmentParams = {
		'계약ID' : contractId,
		'사업장ID': setworkplace,
		'사업장명':  '',
		'부서ID': '',
		'부서명':  '',
		dbMode:  'C',
		chk: false,
		febYear: ''
	};

	departmentRList.push( setdepartmentParams );

	 var selectWorkplaceInfoOptions = "";
	 for(var idx = 0 ; idx < selectWorkplaceInfo.length ; idx++){
		 selectWorkplaceInfoOptions +="<option value='"+selectWorkplaceInfo[idx]['사업장ID']+"'>"+selectWorkplaceInfo[idx]['사업장명']+"</option>";
	 }
	    
	var index = departmentRList.length - 1;
		
	var departmentRListHtml = "";			    
	departmentRListHtml +="<tr id='부서_TR_"+index+"'>";		                
	departmentRListHtml +="<td class='chk_area'><input type='checkbox' id='row_chk' value='"+index+"'> </td>";
	departmentRListHtml +="<td class='tit'>사업장</td>";
	departmentRListHtml +="<td class='value'>";			    					    		
	departmentRListHtml +="<select id='selectWorkplaceInfo_"+index+"' data-placeholder='일치하는 항목이 없습니다.' class='chosen-select'>";
	departmentRListHtml += selectWorkplaceInfoOptions;
	departmentRListHtml +="</select>";						
	departmentRListHtml +="</td>";			                  
	departmentRListHtml +="<td class='tit'>부서명</td>";
	departmentRListHtml +="<td class='value'><input type='text' class='input_txt kor' id='row_부서명_"+index+"' value='' onChange='changeddbMode("+index+",\"U\")' ></td>";
    departmentRListHtml +="</tr>";
    
    $("#departmentRListBody").append(departmentRListHtml);
	
	//document Event 셋팅 
	setDocumentEvent();
	
	$(".chosen-select").chosen({ width: '100%'});
	$(".chosen-select").trigger("chosen:updated");
    
}

//라인삭제
function delDepartmentList() {

	var delindex = [];
	var chkCnt = 0;
	
	$("#row_chk:checked").each(function() {
		chkCnt++;
		delindex.push($(this).val());
	});
	
	if (chkCnt == 0) {
		alert('선택된 정보가 없습니다.');
		return false;
	}
	
	if (confirm( '선택한 데이터를 삭제하시겠습니까?\n저장하지 않은 데이터는 반영되지 않습니다')) {
		
		for (var index = 0; index < delindex.length; index++) {
			logD("# delindex : " + delindex[index] );
			logD("# dataList['dbMode'] : " + departmentRList[delindex[index]]['dbMode'] );
			
			if (departmentRList[delindex[index]]['dbMode'] == 'R' || departmentRList[delindex[index]]['dbMode'] == 'U') {
				departmentRList[delindex[index]]['dbMode'] = 'D';
				$("#부서_TR_"+delindex[index]).css("display", "none");
			} else if (departmentRList[delindex[index]]['dbMode'] == 'C') {
				departmentRList[delindex[index]]['dbMode'] = 'D';
				$("#부서_TR_"+delindex[index]).css("display", "none");
			}
		}
		
	}
}

// 그리드 상태값 변경 - C , U , D
function changeddbMode(index, chk) {
	
	if (departmentRList[index]['dbMode'] == 'C') {
		var NdeptName = departmentRList[index]['부서명'];
		var NworkplaceId = departmentRList[index]['사업장ID'];
		for (var i = 0; i < departmentRList.length; i++) {
			if ( i != index ) {
				if (( NdeptName == departmentRList[i]['부서명'] ) && ( NworkplaceId == departmentRList[i]['사업장ID'] ) ) {
					alert( '같은 사업장 내 중복되는 부서명이 있습니다.');
				}
			}
		}
	} else if (departmentRList[index]['dbMode'] == 'D') {
	} else {
		var NdeptName = departmentRList[index]['부서명'];
		var NworkplaceId = departmentRList[index]['사업장ID'];
		for (var i = 0; i < departmentRList.length; i++) {
			if ( i != index ) {
				if (( NdeptName == departmentRList[i]['부서명'] ) && ( NworkplaceId == departmentRList[i]['사업장ID'] ) ) {
					alert( '같은 사업장 내 중복되는 부서명이 있습니다.');
				}
			}
		}
		departmentRList[index]['dbMode'] = 'U';
	}
}

//페이징 관련 
function pageSelection(opt) {
    var _this = this;    
    var settings = $.extend({}, {
      total: 0,
      page: _this.currPage,
      maxVisible: 5,
      leaps: true,
      firstLastUse: true,
      first: '',
      last: '',
      wrapClass: 'pagination',
      activeClass: 'active',
      disabledClass: 'disabled',
      nextClass: 'btn_next',
      prevClass: 'btn_prev',
      lastClass: 'end_page',
      firstClass: 'first_page'
    }, opt);

    $('#page-selection').show().bootpag(settings).off('page').on('page', function(event, num) {
      _this.tempPage = num;
      _this.onChangeperPage();
    });
    $('.grid_paging .select_box select').val(this.perPage);
}

function onChangeperPage() {
	perPage = $("#perPage").val();
		
    startPage = (tempPage - 1 ) * perPage;
    if (perPage > totalCount) {
      startPage = 0;
    }
    endPage = perPage;
    currPage =  tempPage;

    getList();
}


//엑셀 업로드
function openExcel(type) {
	    
	sessionStorage.setItem("bizId", getCookie('bizId'));
	sessionStorage.setItem("febYear", getCookie('febYear'));	
	sessionStorage.setItem("yearContractId", contractId);
	sessionStorage.setItem("paramExcelType", type);	
	
	openWin (rootPath + "html/feb/febmaster/common_popup_excelUpload.html","excelUploadPop","1200","850");		

}

//서식 다운로드 버튼
function downExcel(evt) {

  var dataUrl = rootPath + 'data/excelUpload/샘플_부서등록.xlsx';
  
  evt.target.setAttribute('href', dataUrl);  
}
