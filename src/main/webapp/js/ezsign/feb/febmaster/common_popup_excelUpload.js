var isExcel = false; 	// 엑셀 레이어 노출
var isLayer = false; 	// 엑셀 적용 레이어 노출
var isValidate = false; // 오류검증 버튼 노출
var isReady = false; 	// 확인 버튼 disable 클래스
var isChecked = false;
//var isNumber = 0; 		// 숫자 셀 여부
var isMatchCount = 0; 	// mapping count
var isMAtched = 0; 		// 매핑될 테이블 갯수
var isMapped = false;

//상단 리스트 데이타 배열
var excelMappingDatas = null;

//상단 예시 모델
var getLists = {
		rows: [],
		cols: []
	};
// 엑셀 파싱 모델
var setLists = {
		rows: [],
		cols: []
	};
// 매핑 정보 모델
var mapModel = {
		columnId: 0,
		excelType: '',
		mappingOrder: 0
	};
// 셀 클릭 정보 모델
var clkModel = {
		setMapped: false
	};

var tmpGet = [];
var tmpCabinet = {};
var setIndex = 0;		//선택 헤드 인덱스
var mapIndex = 0;
var setKeyVal = 0;		//선택 키값
var limit = 5;
var excelFile = '';
var excelBizId = '';
var excelFebYear = '';
var excelYearContractId = '';
var data = [];
var validateTxt = [];
var carrySeletion = [];
var carryCnt = 0;
var carryOver = 0;
var dataUrl = '';

$(document).ready(function() {

	excelBizId = opener.getSessionStorageValue("bizId");
	excelFebYear = opener.getSessionStorageValue("febYear");
	excelYearContractId = opener.getSessionStorageValue("yearContractId");
	mapModel.excelType = opener.getSessionStorageValue("paramExcelType");
	
	// 계약아이디가 없으면 메인으로 튕겨내기
    if (isNull(excelYearContractId) || isNull(mapModel.excelType)) {    	
    	jAlert('사용불가 메뉴 입니다. 기본설정값을 확인 해주세요.' , 'System Message', function() {
    		window.close();
    	});
    } else {  
    	
    	/*$('html').click(function(e) {
    		if(!$(e.target).hasClass('excelDataArea')){
    			alert("영역 밖");
    		}
    	});*/
    	
    	if(isLayer){
    		$("#alert_popup").show();
    	}else{
    		$("#alert_popup").hide();
    	}
    	
    	if(!isValidate){
      	  	$("#isValidate_false").show();
      	  	$("#excelDataDiv").hide();
      	  	$("#isValidateBtn").hide();      	  
        }
    	    	
    	if(!isReady){
    		$("#confirmExcelBtn").addClass("disable");
        	$("#confirmExcelBtn").attr("onClick","");
    	}
    	
    	getExcelType(mapModel.excelType);
    }

});

//초기화
function initExcel() {
			
	/*setLists.rows.forEach(function(v, i){
			v.setValidate = false;
			v.validateTxt = [];
		});*/
	for(var idx = 0 ; idx < setLists.rows.length ; idx++){
		var v = setLists.rows[idx];
		
		v.setValidate = false;
		v.validateTxt = [];
	}
	
	tmpGet = [];
	getLists.rows = [];
	//setLists.rows = [];
	
	if(excelMappingDatas != null){
		excelMappingDatas = null;
	}
	
	setIndex = 0;		//선택 헤드 인덱스
	mapIndex = 0;
	setKeyVal = 0;		//선택 키값
	isChecked = false;

	//컴럼 타이틀 초기화 
	for(var idx = 0 ; idx < getLists.cols.length ; idx++){
		var cols = getLists.cols[idx];
		
		//$("#title_0_"+cols.columnId).css("background","#DBE8EC");
		$("#title_0_"+cols.columnId).css("background","#e5eff3");
		
		var titleText = $("#title_0_"+cols.columnId).text();
		if(titleText.indexOf("[") > -1){
			titleText = titleText.substring(0, titleText.indexOf("["));
			$("#title_0_"+cols.columnId).text(titleText)
		}
	}
	
	var columnNameTableLen = ($('#columnNameTable').find('tr').length - 1);
	for(var idx = 0 ; idx < columnNameTableLen ; idx++){
		$('#columnNameTable > tbody:last > tr:last').remove();
	}

	//엑셀 데이타 초기화 
	for(var idx = 0 ; idx < setLists.cols.length ; idx++){
		var cols = setLists.cols[idx];
		
		//$("#title_0_"+cols.columnId).css("background","#DBE8EC");
		$("#data_0_"+idx).css("background","#e5eff3");
	}
	
	isExcel = false;
	isReady = false;
	isValidate = false;
	$("#confirmExcelBtn").addClass("disable");
	$("#confirmExcelBtn").attr("onClick","");
	
}

function getExcelType(type) {
	// type
    // 1. 부서등록
    // 2. 근로자정보
    // 3. 부양가족
    // 4. 사내기부금
    // 5. 이월기부금
    // 6. 공제불가회사지원금
    // 7. 종전근무지
	initExcel();
	
	isExcel = true;

	var formData = new FormData();
	formData.append("excelType", type);
	
	var url = rootPath + 'febmaster/getExcelMappingPost.do';
		
	//blockUi 호출
	fncOnBlockUI();
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		processData: false,
	    contentType: false,
	    cache: false,
		data: formData,
		success:function(res) {	
			
			if (res.success) {
				getLists.cols = res['excelMappings'];
				
				for(var idx = 0 ; idx < getLists.cols.length ; idx++){
					var v = getLists.cols[idx];
					
					v['setMapped'] = false;
				}
								
				/*getLists.cols.forEach(function(v, i){
					v['setMapped'] = false;
				});*/
								
				
			    var pointClass = '';
				var tableWidth = 150 * getLists.cols.length;
				var columnTitleHtml = "<table id='columnNameTable' class='NanumSquare_R'><thead><tr>";				
				for(var idx = 0 ; idx < getLists.cols.length ; idx++){
					var cols = getLists.cols[idx];
				
					//필수값이면 
					if(cols.required == 1){
						pointClass = 'point';
					}else{
						pointClass = '';
					}
					
					//logD("# getLists.cols  : " + JSON.stringify(cols) );					
					columnTitleHtml += "<th id='title_0_"+cols.columnId+"' onclick='getUpIndex(\""+idx+"\", \""+cols.columnId+"\", true);'  class='"+pointClass+"' >"+cols.displayName+"</th>";
					
				}
				columnTitleHtml += "</tr></thead><tbody></tbody></table>"

				$("#columnNameDiv").html(columnTitleHtml);
 
			} else {
				jAlert('정보 조회에 실패하였습니다.','');				
			}
			
			$.unblockUI();
		},
		error:function(request,status,error){
			$.unblockUI();
			
			if (request.status=="401") {
				clearSession();
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
				//location.href=rootPath;
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		}
	});	

	
}

//엑셀 업로드 버튼
function uploadExcel(evt) {
	
	if (getLists.rows.length > 0) {
		jConfirm('저장하지 않은 데이터는 삭제됩니다.\n다시 업로드 하시겠습니까?', '', function(cfrm){		
			if(cfrm){
				initExcel();
				getExcel(evt);
			}
		});	
	} else {
		getExcel(evt);
	}
	
}

function getExcel(evt) {
	
	isMAtched = 0;
    //var file = evt.srcElement.files[0];
	var file = $("input[name=uploadfile]")[0].files[0];
	
	if(isNull(file)){
		return false;
	}
		
    var formData = new FormData();
    formData.append('file', file);
    
    var url = rootPath + 'febmaster/uploadExcel.do';
    
    //blockUi 호출
	fncOnBlockUI();
	
    $.ajax({
	      url: url,
	      data: formData,
	      processData: false,
	      contentType: false,
	      cache: false,
	      type: 'POST',
	      success: function(res){
	    	  
	    	  if (res.success) {
	    		  setLists.cols = res.headers;
	    		  setLists.rows = res.rows;
	    		  
	    		  // 필수 항목 갯수
	    		  for(var idx = 0 ; idx < getLists.cols.length ; idx++){
	    				var v = getLists.cols[idx];
	    				
	    				if (v.required == '1') {
	    					isMAtched++;
	    				}
	    		  }
	    		  for(var idx = 0 ; idx < setLists.cols.length ; idx++){
	    				var v = setLists.cols[idx];
	    				
	    				v['setMapped'] = false;
	    				v['setIndex'] = idx;
	    		  }
	    		  for(var idx = 0 ; idx < setLists.rows.length ; idx++){
	    				var v = setLists.rows[idx];
	    				
	    				v.setValidate = false;
	    				v.validateTxt = [];
	    		  }
	    		  /*
	    		  getLists.cols.forEach(function(v){
	    	          if (v.required == '1') {
	    	            this.isMAtched++;
	    	          }
	    	       });
	    	      setLists.cols.forEach(function(v, i){
	    	    	  v['setMapped'] = false;
	    	    	  v['setIndex'] = i;
	    	      });
	    	      setLists.rows.forEach(function(v){
	    	    	  v.setValidate = false;
	    	    	  v.validateTxt = [];
	    	      });
	    		  */
	    		  
	    	      isValidate = true;	    
	    	          
	    	      if(isValidate){
	    	    	  $("#isValidate_false").hide();
	    	    	  $("#excelDataDiv").show();
	    	    	  $("#isValidateBtn").show();
	    	      }
	    	      
	    	      if(isReady){
	    	    	  $("#confirmExcelBtn").removeClass("disable");
	    	    	  $("#confirmExcelBtn").attr("onClick","confirmExcel()");
	    	      }
	    	    
	    	      var tableWidth = 150 * setLists.cols.length;
	    	      var excelDataHtml = "<table id='excelDataTable' class='NanumSquare_R'><thead><tr>";				
	    	      for(var idx = 0 ; idx < setLists.cols.length ; idx++){
	    	    	  var cols = setLists.cols[idx];
					
	    	    	  //logD("# colsData ==> " + JSON.stringify(cols) );
	    	    	  
	    	    	  excelDataHtml += "<th id='data_0_"+idx+"' onclick='setUpIndex(\""+idx+"\", \""+cols.setIndex+"\", \""+cols.name+"\");'>"+cols.name+"</th>";
						
	    	      }
	    	      excelDataHtml += "</tr><tbody></tbody></table>"

	    	      $("#excelDataDiv").html(excelDataHtml);
				 
	    	      //jqGrid 데이타 셋팅
	    	      var excelDataTableHtml = "<tr>";	    	      
	    	      for(var idx = 0 ; idx < setLists.rows.length ; idx++){
	    	    	  var rows = setLists.rows[idx];
	    	    	  
	    	    	  //logD("# rowsData ==> " + JSON.stringify(rows) );
	    	    	  
	    	    	  excelDataTableHtml = "<tr>";	    	    	   
	    	    	  var index = 0;
	    	    	  //json 키값 추출 
	    	    	  for(var key in rows ) {	    	    		  
	    	    		  if(key != 'setValidate' && key != 'validateTxt'){	    	    			  
	    	    			  excelDataTableHtml += "<td id='data_"+(idx+1)+"_"+index+"' >" + rows[key] + "</td>";
			    	    	  index++;
	    	    		  }		    	    	  
		    	      }
	    	    	  excelDataTableHtml += "</tr>";
	    	    	  //logD("# rowData ==> " + JSON.parse(JSON.stringify(rowData)) );
	    	    	  
	    	    	  $('#excelDataTable > tbody:last').append(excelDataTableHtml);
	    	      }
	    	      
	    	      //클릭이벤트 생성
	    	      for(var idx = 0 ; idx < setLists.cols.length ; idx++){
	    	    	  var cols = setLists.cols[idx];
	    	    	  
	    	    	  $("#data_0_"+idx).on("click", {paramIdx : idx, paramSetIndex : cols.setIndex, paramSetKey : cols.name}, function(event) {
							//logD("#### paramIdx : "  + event.data.paramIdx);
							//logD("#### Excel Data : "  + $(this).attr('id') );
							
							//전체 테투리 초기화
							for(var subIdx = 0 ; subIdx < setLists.cols.length ; subIdx++){
								$("#data_0_"+subIdx).css("border", "");								
							}

							//클리시 테두리 색깔변경
							if(event.data.paramIdx > 0){
								$("#data_0_"+(event.data.paramIdx-1)).css("border-right", "1px solid #00a0e9");
							}
							$(this).css("border", "1px solid #00a0e9"); 
							 
						    setUpIndex(event.data.paramIdx, event.data.paramSetIndex, event.data.paramSetKey);
	    	    	  });
	    	      }

	    	      //맵핑 레이아웃 호출
	    	      isLayer = false;	
	    	  	  $("#alert_popup").show();
	    	      
	    	  } else{
	    		  jAlert('엑셀 업로드에 실패했습니다.', 'Error Message');
	    	  }
	    	  
	    	  $.unblockUI();
	      },
	      error:function(request,status,error){
				$.unblockUI();
				
				if (request.status=="401") {
					jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
				} else {
					jAlert('입력하신 정보를 다시 확인해주시기 바랍니다.'+error , 'Error Message');
				}
			}
	});

}

//엑셀 파싱 헤더
function setUpIndex(idx, mapIdx, mapKey) {
	setIndex = idx;
	mapIndex = mapIdx;
	setKeyVal = mapKey;
	isChecked = true;
	
	//$("#data_0_"+setIndex).css("background","#fff28d");
	//$("#data_0_"+setIndex).addClass("selected");
}

//상단 예시 헤더 클릭
function getUpIndex(idx, columnId, dataInsertChk) {
	
	var numIdx = parseInt(idx);
		
	//logD("# idx : " +  idx );
	//logD("# columnId : " +  columnId );
	//logD("# getLists.cols[idx] : " +  JSON.stringify( getLists.cols[numIdx] ) );
	
	var setData = setLists.rows;
	var chgCols = setLists.cols[setIndex];
	var getCols = getLists.cols[idx];
	var tarSet = getLists.cols[idx]['mappingOrder'];

	if (!isChecked) {
		if (getLists.cols[idx]['setMapped']) {
			if (confirm('이미 정렬 되어 있습니다. 수정하시겠습니까?')) {

				console.log(getLists.rows, tarSet);
				
				if(idx < setLists.cols.length){
					setLists.cols[idx]['setMapped'] = false;
				}
				if(idx < getLists.cols.length){
					getLists.cols[idx]['setMapped'] = false;
				}

				for(var i = 0 ; i < getLists.rows.length ; i++){
					var v = getLists.rows[i];
					
					var delObj = getLists.cols[idx]['displayName'];
					delete v[delObj];
				}
				
				/*getLists.rows.forEach(function(v, i) {
					var delObj = getLists.cols[idx]['displayName'];
					delete v[delObj];
				});*/

			}
		} else {
			jAlert('아래 테이블에서 정렬할 셀을 먼저 선택하여 주세요.','', function() {
				return false;
			});		
		}

	} else {
		
		//logD("# setIndex : " + setIndex );		
		//logD("# chgCols : " + JSON.stringify(chgCols));
		
		//엑셀데이타 부분 재배치 
		/*setLists.cols.splice(setIndex, 1);
		setLists.cols.splice(idx, 0, chgCols);*/
		
		isChecked = false;

		var setHeader = chgCols['name'];
		var getHeader = getCols['displayName'];
		var getColumn = getCols['columnName'];
		
		//logD("#### setHeader " + setHeader );		
		
		for(var i = 0 ; i < setData.length ; i++){
			var v = setData[i];
		
			//logD("#### getHeader " + getHeader );
			//logD("#### getColumn " + getColumn );
			
			if (tmpGet[i]) {				
				//tmpGet[i][getHeader] = v[setHeader];
				//tmpGet[i][getColumn] = v[setHeader];
				tmpGet[i][getHeader] = v[setHeader];
				tmpGet[i][getColumn] = v[setHeader];
				
				//tmpGet[i].push(getHeader, v[setHeader]);
				//tmpGet[i].push(getColumn, v[setHeader]);
				//tmpGet[i].push('{'+getHeader + ':' + v[setHeader] + ',' + getColumn + ':' + v[setHeader] + '}');
			} else{
				/*tmpGet[i] = {
					getHeader: v[setHeader],
					getColumn: v[setHeader]
				};*/
				tmpGet[i] = {};
				//tmpGet[i].push('{'+getHeader + ':' + v[setHeader] + ',' + getColumn + ':' + v[setHeader] + '}');
				//tmpGet[i].push('{'+getColumn + ':' + v[setHeader] + '}');
				tmpGet[i][getHeader] = v[setHeader];
				tmpGet[i][getColumn] = v[setHeader];
			}
		}
				
		getLists.rows = tmpGet;
		//getLists.rows = JSON.parse(JSON.stringify(tmpGet));
		
		//logD("# tmpGet : " + JSON.stringify(tmpGet));
		
		if(idx < setLists.cols.length){
			setLists.cols[idx]['setMapped'] = true;
		}
		if(idx < getLists.cols.length){
			getLists.cols[idx]['setMapped'] = true;
		}

		mapModel.columnId = columnId;
		mapModel.mappingOrder = mapIndex;

		// 상단 레이아웃 초기데이타 셋팅 
		var forMaxLength = 0;
		if(setLists.rows.length < 5){
			 forMaxLength = setLists.rows.length; 
		 }else{
			 forMaxLength = 5;
		 }
				
		if($('#columnNameTable').find('tr').length == 1){			
			 logD("## forMaxLength : " + forMaxLength );
			 var columnNameTableHtml = "";
			 for(var i = 0 ; i < forMaxLength ; i++){
				 columnNameTableHtml = "<tr>";
				 for(var j = 0 ; j < getLists.cols.length ; j++){
					 var cols = getLists.cols[j];
					 
					 columnNameTableHtml += "<td id='title_"+(i+1)+"_"+cols.columnId+"' >&nbsp;</td>";
				 }
				 columnNameTableHtml += "</tr>";
				 
				 $('#columnNameTable > tbody:last').append(columnNameTableHtml);
			 }
			 
		}
		
		//전체 테투리 초기화
		if(setIndex > 0){
			$("#data_0_"+(setIndex-1)).css("border-right", "");
		}
		$("#data_0_"+setIndex).css("border", "");
		
		//배경색깔변경
		$("#title_0_"+columnId).css("background","#fffaae");
		$("#data_0_"+setIndex).css("background","#fffaae");
				
		 //클릭 데이타 맵핑 시키기
		 for(var i = 0 ; i < forMaxLength ; i++){
			 var setRows = setLists.rows[i];	
			 
			 $('#title_'+(i+1)+'_'+columnId).html(setRows[setKeyVal]); 			 
		 }
		 
		 //////////////////////////////////////		 
		 var setText = $("#data_0_"+setIndex).text();
		 var titleText = $("#title_0_"+columnId).text();
		 
		 //logD("# setText :  "  +  setText );
		 //logD("# titleText :  "  +  titleText );
		 if(titleText.indexOf("[") > -1){
			 titleText = titleText.substring(0, titleText.indexOf("["));
		 }
		 		 
		 $("#title_0_"+columnId).text(titleText + " ["+setText+"]")		 		 

		isMatchCount++;
		  
		//데이타 등록 여부가 true 일때만 등록한다. (엑셀 컬럼 맵핑 데이타 DB 등록)
		/*if(dataInsertChk){
			var formData = new FormData();
			formData.append("columnId", columnId);
			formData.append("excelType", mapModel.excelType);
			formData.append("mappingOrder", mapIndex);
			
			var url = rootPath + 'febmaster/insExcelMapping.do';
				
			//blockUi 호출
			fncOnBlockUI();
			
			$.ajax({
				url:url,
			    crossDomain : true,
				dataType:"json",
				type:"POST",
				processData: false,
			    contentType: false,
			    cache: false,
				data: formData,
				success:function(res) {	
					
					if (res.success) {
						//logD(JSON.stringify(res));
					}else{
						logE("# Excel Mapping 정보 등록중 오류가 발생했습니다.");
					}
					
					$.unblockUI();
				},
			      error:function(request,status,error){
						$.unblockUI();
						
						if (request.status=="401") {
							jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
						} else {
							jAlert('입력하신 정보를 다시 확인해주시기 바랍니다.'+error , 'Error Message');
						}
					}
			});
		}*/
		 
		 //엑셀 컬럼 맵핑 정보 json 생성
		 if(dataInsertChk){ 
			 if(excelMappingDatas == null){
				 excelMappingDatas = newMap();
			 }
			 
			 //excelMappingDatas.put(columnId,mapIndex);		 
			 excelMappingDatas.put(mapIndex, columnId);
		 }
		 
	}
}

//데이터 확인 버튼
function validateData() {
	
	var tmpTxt = '';
	carrySeletion = [];
	
	for( var idx = 0 ; idx < setLists.rows.length ; idx++ ){
		var v = setLists.rows[idx]
		
		v.setValidate = false;
		v.validateTxt = [];
	}
	
	logD("## isMatchCount : " + isMatchCount );
	logD("## isMAtched : " + isMAtched );
	
	if(isMAtched == 0){
		jAlert('엑셀 파일 업로드를 먼저 해주십시오.','', function() {
			return false;
		});		
	}else if($('#columnNameTable').find('tr').length == 1){
		jAlert('데이터 맵핑을 먼저 해주십시오.','', function() {
			return false;
		});		
	}else{
		
		var mappingChk = true;
		
		// 필수 항목 값 체크
		for(var idx = 0 ; idx < getLists.cols.length ; idx++){
			var v = getLists.cols[idx];
			//logD("# v.required : " + v.required );
			if (v.required == '1') {
				if(!v.setMapped){
					mappingChk = false;
					jAlert('[ '+v.displayName+' ] 항목  데이터 매핑이 완료되지 않았습니다.','');
  		  			return false;
				}
	  	  	}
		}
			
		for( var rowsIdx = 0 ; rowsIdx < getLists.rows.length ; rowsIdx++ ){
			var rowsVal = getLists.rows[rowsIdx];

//			var dataKey = Object.keys(rowsVal);
//	        var dataValue = Object.values(rowsVal);
			
			var dataKey = "";
			var dataValue = "";
			for(var key in rowsVal) {
				dataKey += key + ",";
				dataValue += rowsVal[key] + ",";	  
			}			
			if(dataKey.length > 0){
				dataKey = dataKey.substring(0, dataKey.length-1);
			}
			if(dataValue.length > 0){
				dataValue = dataValue.substring(0, dataValue.length-1);
			}
			
	        carryCnt = 0;
	        
	        //logD("# dataKey : " + dataKey );
	        //logD("# dataValue : " + dataValue );
	        //logD("# getLists.cols.length : " + getLists.cols.length );
	        for( var colsIdx = 0 ; colsIdx < getLists.cols.length ; colsIdx++ ){	
	        	 var colsVal = getLists.cols[colsIdx];
	        			        
	        	//logD("# colsVal : " + JSON.stringify(colsVal));
	        	var tarSet = dataKey.indexOf(colsVal.displayName);
	        	
	        	//logD("# tarSet : " + tarSet );
	        	//logD("# dataKey[tarSet] : " + dataKey[tarSet] );
		        //logD("# colsVal.displayName : " + colsVal.displayName );
	        	
	        	if (dataKey[tarSet] == colsVal.displayName) {
	        		//logD("# colsVal.required : " + colsVal.required );
	        		//logD("# dataValue[tarSet] : " + dataValue[tarSet] );
	        		//logD("# colsVal.displayName : " + colsVal.displayName );
	        		
	        		// 필수 입력값인 경우 null값 체크 
	        		if (colsVal.required == '1' && dataValue[tarSet] == '') {
	        			tmpTxt = "[ " +(rowsIdx+1) + " 라인 - "+dataKey[tarSet]+" ] 칸은 필수 입력 칸입니다.";
	        			
	        			//setLists.rows[rowsIdx]['setValidate'] = true;
	                    //setLists.rows[rowsIdx]['validateTxt'].push(tmpTxt);
	                    
	                    jAlert(tmpTxt,'');
	                    
	                    return false;
	        		}
	        		else{
	        			//logD("# colsVal.columnType : " + colsVal.columnType );
	        			switch (colsVal.columnType) {
	        				case '1': // 일반 텍스트
	        				// console.log('텍스트', dataKey[ii]);
	        				break;
	        				case '2': // 전화번호
	        				// console.log('전화번호', dataKey[ii]);
	        				break;
	        				case '3': // 숫자
	        					//console.log('숫자', dataKey[colsIdx]);
	        					if(!isNull($.trim(dataValue[tarSet]))){
	        						if( !isNumber(dataValue[tarSet])) {
	        							tmpTxt = "[ " +(rowsIdx+1)+" 라인 - "+dataKey[tarSet]+" ] 형식이 올바르지 않습니다.";

	        	                        //setLists.rows[rowsIdx]['setValidate'] = true;
	        	                        //setLists.rows[rowsIdx]['validateTxt'].push(tmpTxt);
	        	                        
	        	                        jAlert(tmpTxt,'');	        	                       
	        	                        return false;
	        						}
	        					}
	        				break;
	        				case '4': // 이메일
	        					if( !isEmail(dataValue[tarSet])) {
	        						tmpTxt = "[ " +(rowsIdx+1)+" 라인 - "+dataKey[tarSet] + ' 형식이 올바르지 않습니다. ';

	        						//setLists.rows[i]['setValidate'] = true;
	        						//setLists.rows[i]['validateTxt'].push(tmpTxt);
	        						
	        						jAlert(tmpTxt,'');	        	                       
        	                        return false;
	        					}	        	                 
	        	            break;
	        				case '5': // 코드값 필드
	        					if (colsVal.codes != null && !isNull(dataValue[tarSet])) {
	        						if (colsVal.codes.indexOf(dataValue[tarSet]) == -1) {
	        							tmpTxt = tmpTxt = "[ " +(rowsIdx+1)+" 라인 - "+dataKey[tarSet] + ' 코드값을 확인하여 주세요.';

	        							//setLists.rows[i]['setValidate'] = true;
	        							//setLists.rows[i]['validateTxt'].push(tmpTxt);
	        							
	        							jAlert(tmpTxt,'');	        	                       
	        	                        return false;
	        						}

	        	                    // 이월기부금 중복체크
	        						if (mapModel.excelType == '5') {
	        							var carryTmp = {
	        								기부년도: rowsVal.기부년도,
	        								기부금종류코드: dataValue[tarSet],
	        								사번: rowsVal.사번
	        							};

	        							carrySeletion.push(carryTmp);
	        							/* 20190121 - 이월기부금 중복 체크 제거 */
	        							/*this.carrySeletion.forEach((cv, ci) => {
	        	                        if (cv.기부년도 == v.기부년도 && cv.사번 == v.사번) {
	        	                          if (cv.기부금종류코드 == v.기부금종류코드) {
	        	                            this.carryCnt++;
	        	                            if (this.carryCnt > 1) {
	        	                              this.carryOver = i;
	        	                              tmpTxt = v.성명 + '님의 ' + v.기부년도 + '년도 기부금종류가 중복됩니다';
	        	                              alert(tmpTxt);
	        	                              this.setLists.rows[this.carryOver]['setValidate'] = true;
	        	                              this.setLists.rows[this.carryOver]['validateTxt'].push(tmpTxt);
	        	                            }
	        	                          }
	        	                        }
	        	                      	});*/
	        						}
	        					}
	        				break;
	        				case '6': // 개인식별번호
	        					var perIdn = dataValue[tarSet].replace(/-/g, '');

	        					if( !allCheck(perIdn)) {
	        						tmpTxt = "[ " +(rowsIdx+1)+" 라인 - "+dataKey[tarSet] + ' 형식이 올바르지 않습니다. ';

	        	                    //setLists.rows[i]['setValidate'] = true;
	        	                    //setLists.rows[i]['validateTxt'].push(tmpTxt);
	        	                    
	        	                    jAlert(tmpTxt,'');	        	                       
        	                        return false;
	        					}
	        	            break;
	        				case '7': // 사업자등록번호
	        					if( mapModel.excelType == '6' ) {
	        						if( !bizNumCheck(dataValue[tarSet]) && !isNull(dataValue[tarSet]) ) {
	        							tmpTxt = "[ " +(rowsIdx+1)+" 라인 - "+dataKey[tarSet] + ' 형식이 올바르지 않습니다. ';

	        							//setLists.rows[i]['setValidate'] = true;
	        							//setLists.rows[i]['validateTxt'].push(tmpTxt);
	        							
	        							jAlert(tmpTxt,'');	        	                       
	        	                        return false;
	        						}

	        					} else {
	        						if( !bizNumCheck(dataValue[tarSet])) {
	        							tmpTxt = "[ " +(rowsIdx+1)+" 라인 - "+dataKey[tarSet] + ' 형식이 올바르지 않습니다. ';

	        							//setLists.rows[i]['setValidate'] = true;
	        							//setLists.rows[i]['validateTxt'].push(tmpTxt);
	        							
	        							jAlert(tmpTxt,'');	        	                       
	        	                        return false;
	        	                    }
	        					}
	        					
	        	            break;
	        				case '8': // 날짜
	        	            // console.log('날짜', dataKey[ii]);
	        				break;
	        			}
	        		}
	        		 
	        	}

	        };

		};
		
		
		// 데이터 확인
		var chkReady = [];
		for(var ci = 0 ; ci < setLists.rows.length ; ci++){
			var chk = setLists.rows[ci];
			
			chkReady.push(chk['setValidate']);
			/*if (chk['setValidate'] == true) {
				toggleRows(setLists.rows[ci]);
	        }*/
		}
		
		/*setLists.rows.forEach(function(chk, ci){
			chkReady.push(chk['setValidate']);
			if (chk['setValidate'] == true) {
				toggleRows(setLists.rows[ci]);
	        }
		});*/

		if (chkReady.indexOf(true) > -1) {
			isReady = false;
		} else {
			isReady = true;
			jAlert('데이터 매핑이 완료되었습니다.','');
			
			if(isReady){
  	    	  $("#confirmExcelBtn").removeClass("disable");
  	    	  $("#confirmExcelBtn").attr("onClick","confirmExcel()");
  	      	}
		}
		
	}
	
    /*
	if (isMatchCount < isMAtched) {
		jAlert('['+(isMAtched - isMatchCount) + '건] 데이터 매핑이 완료되지 않았습니다.','');
	} else {
	    	
	}*/
}

// 저장 버튼
function confirmExcel() {
	
	if(isReady) {
		jConfirm('등록 하시겠습니까?', '', function(cfrm){		
			if(cfrm){
				
				/*var formData = new FormData();
				formData.append("계약ID", excelYearContractId);
				formData.append("data", getLists.rows);*/
				var dataParam = {
						data: getLists.rows,
						'계약ID': excelYearContractId,
						'근무시기': ''
				};
								
				// 엑셀 업로드 저장
				var url = '';

				logD("# saveExcel Type : " + mapModel.excelType );

				switch (mapModel.excelType) {
					case '1':
						url = rootPath + 'febmaster/saveExcelType1.do';
						break;
					case '2':
						url = rootPath + 'febmaster/saveExcelType2.do';
						break;
					case '3':
						url = rootPath + 'febmaster/saveExcelType3.do';
						break;
					case '4':
						url = rootPath + 'febmaster/saveExcelType4.do';
						break;
					case '5':
						url = rootPath + 'febmaster/saveExcelType5.do';
						break;
					case '6':
						url = rootPath + 'febmaster/saveExcelType6.do';
						break;
					case '7':
						url = rootPath + 'febmaster/saveExcelType7.do';
						break;
					case '8':
						url = rootPath + 'febmaster/saveExcelType8.do';
						break;
					case '9':
						url = rootPath + 'febmaster/saveExcelType9.do';
												
						dataParam['근무시기'] = $("#근무시기", opener.document).val();						
						break;	
				}
											
								
				//blockUi 호출
				fncOnBlockUI();
				
//				logD("  dataParam : " + JSON.stringify(dataParam));
				
				
				$.ajax({
					url:url,
				    crossDomain : true,
					dataType:"json",
					type:"POST",
					//processData: false,
					contentType: "application/json; charset=UTF-8",
				    cache: false,
					data: JSON.stringify(dataParam),
					success:function(res) {	
						
						if (res['success']) {
							
							var resMessage = "저장되었습니다.";
							if(isNotNull(res['message'])){
								resMessage = res['message'];
							}
							
							//logI("# empList : " + JSON.stringify(res['empList']));
							
							//컴럼 맵핑 정보 등록 
							//saveExcelMappingData(resMessage, res['empList']);
							
							
							if(mapModel.excelType == '9' && res['empList'] != null && res['empList'].length > 0 ){
								//재집계 실행 
								saveTotalIncome(resMessage, res['empList']);
							}else {
								jAlert(resMessage,'', function() {	
									$.unblockUI();
									//컴럼 맵핑 정보 등록 
									saveExcelMappingData();
								});
							}

						}else{
							$.unblockUI();
							
							if(isNull(res['message'])){
								jAlert('저장시 오류가 발생했습니다.','');
							}else{
								jAlert(res['message'],'');
							}
						}
						
						//$.unblockUI();
					},
				      error:function(request,status,error){
							$.unblockUI();
							
							if (request.status=="401") {
								jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
							} else {
								jAlert('입력하신 정보를 다시 확인해주시기 바랍니다.'+error , 'Error Message');
							}
						}
				});

			}
		});
		
	} else {
		jAlert('데이터 확인이 완료되지 않았습니다.','');
		return false;
	}
}

//근로자 재집계 처리
function saveTotalIncome(resMessage, empList){

	//근로자 재집계 처리 
	for(var index = 0 ; index < empList.length ; index++){
				
		//logI("# res['empList']['계약년도'] : " + empList[index]['계약년도']);
		//logI("# res['empList']['근무시기'] : " + empList[index]['근무시기']);
		//logI("# res['empList']['계약ID'] : " + empList[index]['계약ID']);
		//logI("# res['empList']['사용자ID'] : " + empList[index]['사용자ID']);
		
		yearTaxTotalIncome(empList[index]['계약년도'], empList[index]['계약ID'], empList[index]['사용자ID'], empList[index]['근무시기']);
	}
	
	//logI("#######  totalIncomeProc : " + totalIncomeProc );
	if(totalIncomeProc){
		jAlert(resMessage,'', function() {
			$.unblockUI();
			//컴럼 맵핑 정보 등록 
			saveExcelMappingData();		
		});
	}else{
		jAlert("저장시 오류가 발생했습니다.",'', function() {
			$.unblockUI();
			return false;
		});
	}
}


//컬럼 맵핑 정보를 등록한다.
function saveExcelMappingData(){
	
	if(excelMappingDatas == null){		
		// 상태 초기화
		initExcel();
		
		opener.getList();
		fncClose();		
		return false;
	}
	var jSonData = JSON.parse(JSON.stringify(excelMappingDatas));			 
	
	var formData = new FormData();
	formData.append("mappingData", JSON.stringify(jSonData['value']));
	formData.append("excelType", mapModel.excelType);
	
	var url = rootPath + 'febmaster/saveExcelMapping.do';
		
	$.ajax({
		url:url,
		crossDomain : true,
		dataType:"json",
		type:"POST",
		processData: false,
		//contentType:'application/json; charset=utf-8',
		contentType: false,
		cache: false,
		//data: JSON.stringify(formData),
		data: formData,
		success:function(res) {	
				
			logD("# 맵핑정보 등록 : " + res['success'] );
			if (!res['success']) {
				jAlert('맵핑 정보 등록중 오류가 발생했습니다.','');
			}else{
				
				// 상태 초기화
				initExcel();
				
				opener.getList();
				fncClose();
			}
			
			$.unblockUI();

		},
		error:function(request,status,error){
			$.unblockUI();
					
			if (request.status=="401") {
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
			} else {
				jAlert('입력하신 정보를 다시 확인해주시기 바랍니다.'+error , 'Error Message');
			}
		}
	});
}

//서식 다운로드 버튼
function downExcel(evt) {
  // type
  // 1. 부서등록
  // 2. 근로자정보
  // 3. 부양가족
  // 4. 사내기부금
  // 5. 이월기부금
  // 6. 공제불가회사지원금
  // 7. 종전근무지
  dataUrl = rootPath + 'data/excelUpload/';
  
  switch (mapModel.excelType) {
    case '1':
    	dataUrl = dataUrl + '샘플_부서등록.xlsx';
    break;
    case '2':
    	dataUrl = dataUrl + '샘플_근로자등록.xlsx';
    break;
    case '3':
    	dataUrl = dataUrl + '샘플_부양가족.xlsx';
    break;
    case '4':
    	dataUrl = dataUrl + '샘플_사내기부금.xlsx';
    break;
    case '5':
    	dataUrl = dataUrl + '샘플_이월기부금.xlsx';
    break;
    case '6':
    	dataUrl = dataUrl + '샘플_공제불가회사지원금.xlsx';
    break;
    case '7':
    	dataUrl = dataUrl + '샘플_종전근무지.xlsx';
    break;
    case '8':
    	dataUrl = dataUrl + '샘플_원천징수영수증관리번호.xlsx';
    break;
    case '9':	// 간이지급명세서 근로자 정보 
    	dataUrl = dataUrl + '샘플_근로자등록.xlsx';
    break;
  }
  
  evt.target.setAttribute('href', dataUrl);  
}

//취소 / 닫기
function fncClose() {

	if (isValidate) {
		jConfirm('작업 진행 중인 데이터가 삭제됩니다.\n취소하시겠습니까?', '', function(cfrm){		
			if(cfrm){
			  // 상태 초기화
			  isExcel = false;
			  isReady = false;
			  isValidate = false;
			  initExcel();

			  window.close();
			  // 데이터 초기화 로직
			  //$("#excel-upload-popup").html("");
			}
		});
	} else {
		isExcel = false;
		window.close();
		//$("#excel-upload-popup").html("");
	}
}

/* JSON 객체를 사용하여 정적 map */
function newMap() {
	var map = {};
	map.value = {};
	map.getKey = function(id) {
		return id;
	};
	map.put = function(id, value) {
		var key = map.getKey(id);
		map.value[key] = value;
	};
	map.contains = function(id) {
		var key = map.getKey(id);
		if(map.value[key]) {
			return true;
	    } else {
	    	return false;
	    }
	};
	map.get = function(id) {
		var key = map.getKey(id);
		if(map.value[key]) {
			return map.value[key];
		}
		return null;
	};
	map.remove = function(id) {
		var key = map.getKey(id);
		if(map.contains(id)){
			map.value[key] = undefined;
		}
	};
	 
	return map;
}


//엑셀 맵핑 레이아웃 close
function fncLayerPopClose(){
	
	isLayer = false;	
	$("#alert_popup").hide();
	
}

//엑셀 자동 맵핑
function fncAutoMapping(){
	
	var autoUpdCnt = 0;
	
	for(var cIdx = 0 ; cIdx < getLists.cols.length ; cIdx++){
		var colsVal = getLists.cols[cIdx];

		for(var rIdx = 0 ; rIdx < setLists.cols.length ; rIdx++){
			var rowsVal = setLists.cols[rIdx];
    		
			if(colsVal.displayName == rowsVal.name){
    		
				//데이타 선택
				setUpIndex(rIdx, rowsVal.setIndex, rowsVal.name);
				//컬럼타이틀 선택 
				getUpIndex(cIdx, colsVal.columnId, false);
				
    			autoUpdCnt++;
			 }
		 }
	}
	
	fncLayerPopClose();
	
	if(autoUpdCnt > 0){
		jAlert(autoUpdCnt + '건의 컬럼이 맵핑 되었습니다.','');
	}
}

//엑셀 사용자 맵핑
function fncUserMapping(){
	
	var autoUpdCnt = 0;
	
	 for(var idx = 0 ; idx < getLists.cols.length ; idx++){
		 var colsVal = getLists.cols[idx];
		 var rIdx = parseInt(colsVal['mappingOrder']);
  
		 if(rIdx > -1){
			 var rowsVal = setLists.cols[rIdx];
	  
			 if(!isNull(rowsVal)){
	
				//if(colsVal.displayName == rowsVal.name){	    	      			
				 //데이타 선택
				 setUpIndex(rIdx, rowsVal.setIndex, rowsVal.name);
				 //컬럼타이틀 선택 
				 getUpIndex(idx, colsVal.columnId, false);
	
				 autoUpdCnt++;
				//}
			 }
		 }
	}

	fncLayerPopClose();
	
	if(autoUpdCnt > 0){
		jAlert(autoUpdCnt + '건의 컬럼이 맵핑 되었습니다.','');
	}
}

