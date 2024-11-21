var isValidate = false; // 오류검증 버튼 노출
var isChecked = false;
var isMAtched = 0;		// 매핑될 테이블 갯수

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
		id: '',
		name: '',
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

var data = [];
var validateTxt = [];
var carrySeletion = [];
var carryCnt = 0;
var carryOver = 0;
var dataUrl = '';
var excelFileUrl = '';

//CSS 파일 로드
var loadCssFile = function(){
	var fontPcCss = document.createElement( "link" );
	fontPcCss.href = rootPath + "css/font_pc.css";
	fontPcCss.type = "text/css";
	fontPcCss.rel = "stylesheet";
	fontPcCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( fontPcCss );
	
	var materialIconsMinCss = document.createElement( "link" );
	materialIconsMinCss.href = rootPath + "css/material-icons.min.css";
	materialIconsMinCss.type = "text/css";
	materialIconsMinCss.rel = "stylesheet";
	materialIconsMinCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( materialIconsMinCss );
	
	var bootstrapCandyCss = document.createElement( "link" );
	bootstrapCandyCss.href = rootPath + "css/bootstrap_candy.css";
	bootstrapCandyCss.type = "text/css";
	bootstrapCandyCss.rel = "stylesheet";
	bootstrapCandyCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( bootstrapCandyCss );
	
	var jqueryAlertsCss = document.createElement( "link" );
	jqueryAlertsCss.href = rootPath + "css/jquery.alerts.css";
	jqueryAlertsCss.type = "text/css";
	jqueryAlertsCss.rel = "stylesheet";
	jqueryAlertsCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( jqueryAlertsCss );
	
	var jqueryUiMinCss = document.createElement( "link" );
	jqueryUiMinCss.href = rootPath + "css/jquery-ui.min.css";
	jqueryUiMinCss.type = "text/css";
	jqueryUiMinCss.rel = "stylesheet";
	jqueryUiMinCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( jqueryUiMinCss );
	
	var yearTaxCommonCss = document.createElement( "link" );
	yearTaxCommonCss.href = rootPath + "css/yearTax_common.css";
	yearTaxCommonCss.type = "text/css";
	yearTaxCommonCss.rel = "stylesheet";
	yearTaxCommonCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( yearTaxCommonCss );
	
	var excelPopupCss = document.createElement( "link" );
	excelPopupCss.href = rootPath + "css/excel_popup.css";
	excelPopupCss.type = "text/css";
	excelPopupCss.rel = "stylesheet";
	excelPopupCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( excelPopupCss );
	
	var popupExcelUploadCss = document.createElement( "link" );
	popupExcelUploadCss.href = rootPath + "css/popup/popup_excelUpload.css";
	popupExcelUploadCss.type = "text/css";
	popupExcelUploadCss.rel = "stylesheet";
	popupExcelUploadCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( popupExcelUploadCss );
} 

$(document).ready(function() {

	//CSS 파일 로드
	loadCssFile();
	
	mapModel.excelType = getParams().paramExcelType;
	mapModel.id = getParams().paramID;
		
	// 엑셀등록타입 없으면 메인으로 튕겨내기
    if (isNull(mapModel.excelType)) {    	
    	jAlert('사용불가 메뉴 입니다. 기본설정값을 확인 해주세요.' , 'System Message', function() {
    		window.close();
    	});
    } else {  
    	
    	/*$('html').click(function(e) {
    		if(!$(e.target).hasClass('excelDataArea')){
    			alert("영역 밖");
    		}
    	});*/
    	    	
    	if(!isValidate){
      	  	$("#isValidate_false").show();
      	  	$("#excelDataDiv").hide();
      	  	$("#isValidateBtn").hide();      	  
        }
    	
    	getExcelType(mapModel.excelType, mapModel.id);
    	
    	getExcelMaster(mapModel.excelType, mapModel.id);
    }

});

//초기화
function initExcel(reset) {
			
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

		cols['setMapped'] = false;
		cols['mappingOrder'] = -1;
		
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
		if(reset) {
			$("#data_0_"+idx).text("");
		}
	}

	if(reset) {
		var dataTableLen = ($('#excelDataTable').find('tr').length - 1);
		for(var idx = 0 ; idx < dataTableLen ; idx++){
			$('#excelDataTable > tbody:last > tr:last').remove();
		}
	}
	
	isValidate = false;
	excelFileUrl = '';
}

function getExcelType(type, id) {
	// type
    // 1. 근로자정보
    // C. 계약조건
	initExcel(true);
	
	var url = '';
	var formData = new FormData();
	formData.append("excelType", type);
	formData.append("id", id);

	switch (mapModel.excelType) {
		case 'C': // 계약조건
			url = rootPath + 'popup/getExcelMappingContract.do';				
			break;
		default:
			url = rootPath + 'popup/getExcelMapping.do';
			break;
	}
	
		
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
				excelFileUrl = res.sampleExcelFile;
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
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
				window.close();
				//location.href=rootPath;
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		}
	});
}

function getExcelMaster(type, id) {

	var url = '';
	var formData = new FormData();
	formData.append("fileId", id);
	formData.append("excelType", type);

	url = rootPath + 'popup/getExcelMapMasterList.do';
	
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
				data = res['excelMappings'];				
				
				var mapInfoHtml = "<option value='' >선택</option>";
				
				for(var idx = 0 ; idx < data.length ; idx++){
					var vo = data[idx];
					
					mapInfoHtml += "<option value='"+vo.mapId+"' >"+vo.mapName+"</option>";
					
				}
				$("#mapList").html(mapInfoHtml);
			} else {
				jAlert('정보 조회에 실패하였습니다.','');				
			}
			
			$.unblockUI();
		},
		error:function(request,status,error){
			$.unblockUI();
			
			if (request.status=="401") {				
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
				window.close();
				//location.href=rootPath;
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		}
	});
}


//데이터초기화
function resetData() {
	initExcel(false);	
}


//엑셀 업로드 버튼
function uploadExcel(evt) {
	
	if (getLists.rows.length > 0) {
		jConfirm('저장하지 않은 데이터는 삭제됩니다.\n다시 업로드 하시겠습니까?', '', function(cfrm){		
			if(cfrm){
				initExcel(true);
				getExcel(evt);
			}
		});	
	} else {
		getExcel(evt);
	}
	
}

function getExcel(evt) {
	
    //var file = evt.srcElement.files[0];
	var file = $("input[name=uploadfile]")[0].files[0];
	
	if(isNull(file)){
		return false;
	}
		
    var formData = new FormData();
    formData.append('file', file);
    
    var url = rootPath + 'popup/uploadExcel.do';
    
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
    		  
    	    	  $("#isValidate_false").hide();
    	    	  $("#excelDataDiv").show();
    	    	  $("#isValidateBtn").show();
	    	    
	    	      var tableWidth = 150 * setLists.cols.length;
	    	      var excelDataHtml = "<table id='excelDataTable' class='NanumSquare_R'><thead><tr>";				
	    	      for(var idx = 0 ; idx < setLists.cols.length ; idx++){
	    	    	  var cols = setLists.cols[idx];
					
	    	    	  excelDataHtml += "<th id='data_0_"+idx+"' onclick='setUpIndex(\""+idx+"\", \""+cols.setIndex+"\", \""+cols.name+"\");'>"+cols.name+"</th>";
						
	    	      }
	    	      excelDataHtml += "</tr><tbody></tbody></table>"

	    	      $("#excelDataDiv").html(excelDataHtml);
				 
	    	      //jqGrid 데이타 셋팅
	    	      var excelDataTableHtml = "<tr>";	    	      
	    	      for(var idx = 0 ; idx < setLists.rows.length ; idx++){
	    	    	  var rows = setLists.rows[idx];
	    	    	  
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
	    	    	  $('#excelDataTable > tbody:last').append(excelDataTableHtml);
	    	      }
	    	      
	    	      //클릭이벤트 생성
	    	      for(var idx = 0 ; idx < setLists.cols.length ; idx++){
	    	    	  var cols = setLists.cols[idx];
	    	    	  
	    	    	  $("#data_0_"+idx).on("click", {paramIdx : idx, paramSetIndex : cols.setIndex, paramSetKey : cols.name}, function(event) {							
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
	    	      
	    	      $("input[name=uploadfile]").val('');
	    	      fncAutoMapping();
	    	      
	    	  } else{
	    		  jAlert('엑셀 업로드에 실패했습니다.', 'Error Message');
	    	  }
	    	  
	    	  $.unblockUI();
	      },
	      error:function(request,status,error){
				$.unblockUI();
				
				if (request.status=="401") {
					jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
					window.close();
				} else {
					jAlert('입력하신 정보를 다시 확인해주시기 바랍니다.'+error , 'Error Message');
				}
			}
	});

}

// 매크로선택값 저장
function setMapId() {
	var id = $("#mapList option:selected").val();
	$("#mapId").val(id);
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
	
	var setData = setLists.rows;
	var chgCols = setLists.cols[setIndex];
	var getCols = getLists.cols[idx];
	var tarSet = getLists.cols[idx]['mappingOrder'];
	
	/*
	if(tarSet>-1) {
		$("#data_0_"+tarSet).css("background","#e5eff3");
	}
	*/
	
	if (!isChecked) {
		if (getLists.cols[idx]['setMapped']) {
			if (confirm('이미 선택되어 있습니다. 해제하시겠습니까?')) {

				if(idx < getLists.cols.length){
					var getColumnId = getLists.cols[idx].columnId;
					var setColumnIdx = getLists.cols[idx]['mappingOrder'];
					
					var cntColumnDuplicate = 0;
					for(var c=0;c<getLists.cols.length;c++) {
						v = getLists.cols[c];
						if(v['mappingOrder'] == setColumnIdx) cntColumnDuplicate++;
					}
					if(cntColumnDuplicate==1)
						$("#data_0_"+setColumnIdx).css("background","#e5eff3");
					
					setLists.cols[setColumnIdx]['setMapped'] = false;
					
					$("#title_0_"+getColumnId).css("background","#e5eff3");
					$("#title_0_"+getColumnId).html(getLists.cols[idx]['displayName']);
					
					getLists.cols[idx]['setMapped'] = false;
					getLists.cols[idx]['mappingOrder'] = -1;
					delete excelMappingDatas.value[getColumnId];
				}

				for(var i = 0 ; i < getLists.rows.length ; i++){
					var v = getLists.rows[i];

					var delObj = getLists.cols[idx]['columnName'];
					delete v[delObj];
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
		//엑셀데이타 부분 재배치 
		/*setLists.cols.splice(setIndex, 1);
		setLists.cols.splice(idx, 0, chgCols);*/

		isChecked = false;

		var setHeader = chgCols['name'];
		var getHeader = getCols['displayName'];
		var getColumn = getCols['columnName'];

		for(var i = 0 ; i < setData.length ; i++){
			var v = setData[i];
		
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

		 if(titleText.indexOf("[") > -1){
			 titleText = titleText.substring(0, titleText.indexOf("["));
		 }
		 		 
		 $("#title_0_"+columnId).text(titleText + " ["+setText+"]")		 		 
		 
		 //엑셀 컬럼 맵핑 정보 json 생성
		 if(dataInsertChk){ 
			 if(excelMappingDatas == null){
				 excelMappingDatas = newMap();
			 }
			 getCols['mappingOrder'] = mapIndex;
			 excelMappingDatas.put(columnId, mapIndex);
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
	
	if($('#columnNameTable').find('tr').length == 1){
		jAlert('데이터 선택을 먼저 해주십시오.','', function() {
			return false;
		});		
	}else{
		
		var mappingChk = true;
		
		// 필수 항목 값 체크
		for(var idx = 0 ; idx < getLists.cols.length ; idx++){
			var v = getLists.cols[idx];

			if (v.required == '1') {
				if(!v.setMapped){
					mappingChk = false;
					jAlert('[ '+v.displayName+' ] 항목  데이터 선택이 완료되지 않았습니다.','');
  		  			return false;
				}
	  	  	}
		}
			
		for( var rowsIdx = 0 ; rowsIdx < getLists.rows.length ; rowsIdx++ ){
			var rowsVal = getLists.rows[rowsIdx];
			
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

	        for( var colsIdx = 0 ; colsIdx < getLists.cols.length ; colsIdx++ ){	
	        	 var colsVal = getLists.cols[colsIdx];

	        	var tarSet = dataKey.indexOf(colsVal.displayName);	        	
	        	if (dataKey[tarSet] == colsVal.displayName) {	        		
	        		// 필수 입력값인 경우 null값 체크 
	        		if (colsVal.required == '1' && dataValue[tarSet] == '') {
	        			tmpTxt = "[ " +(rowsIdx+1) + " 라인 - "+dataKey[tarSet]+" ] 칸은 필수 입력 칸입니다.";
	        			
	        			//setLists.rows[rowsIdx]['setValidate'] = true;
	                    //setLists.rows[rowsIdx]['validateTxt'].push(tmpTxt);
	                    
	                    jAlert(tmpTxt,'');
	                    
	                    return false;
	        		}
	        		else{
	        			switch (colsVal.columnType) {
	        				case '1': // 일반 텍스트
	        				// logD('텍스트', dataKey[ii]);
	        				break;
	        				case '2': // 전화번호
	        				// logD('전화번호', dataKey[ii]);
	        				break;
	        				case '3': // 숫자
	        					//logD('숫자', dataKey[colsIdx]);
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
	        	            // logD('날짜', dataKey[ii]);
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
		} else {
			isValidate = true;
			jAlert('데이터 선택이 완료되었습니다.','');
			return true;
		}
		
	}
	
}

// 저장 버튼
function confirmExcel() {
	if (!validateData()) return;
	
	if(getLists.cols.length==0) {
		jAlert("데이터를 먼저 선택하시기 바랍니다.", '');
		return false;		
	}
	
	if(!isValidate) {
		jAlert("데이터확인을 먼저 해주십시오.", '');
		return false;		
	}
	
	
	jConfirm('등록 하시겠습니까?', '', function(cfrm){		
		if(cfrm){
			
			var dataParam = {
					data: getLists.rows,						
					'id': mapModel.id
			};
							
			// 엑셀 업로드 저장
			var url = '';

			switch (mapModel.excelType) {
				case '1': // 근로자정보
					url = rootPath + 'popup/saveExcelType1.do';
					break;
				case 'C': // 계약조건
					url = rootPath + 'popup/saveExcelTypeC.do';					
					break;
			}
							
			//blockUi 호출
			fncOnBlockUI();
			
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
					$.unblockUI();
					
					if (res['success']) {
						
						var resMessage = "저장되었습니다.";
						if(isNotNull(res['message'])){
							resMessage = res['message'];
						}
						
						
						jAlert(resMessage,'', function() {	
							window.close();
							//컴럼 맵핑 정보 등록 
							//saveExcelMappingData();
						});

					}else{
						
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
							window.close();
						} else {
							jAlert('입력하신 정보를 다시 확인해주시기 바랍니다.'+error , 'Error Message');
						}
					}
			});

		}
	});
	
}

//컬럼 맵핑 정보를 조회한다.
function getExcelMappingData() {
	var mapId = $("#mapId").val();
	if(mapId=='') {
		alert("매크로정보를 선택해주시기 바랍니다.");
		return;
	}
	
	var get_list_count = getLists.cols.length;
	var set_list_count = setLists.cols.length;
	
	var formData = new FormData();
	formData.append("mapId", mapId);
	formData.append("excelType", mapModel.excelType);
	
	var url = rootPath + 'popup/getExcelMapDetailList.do';

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

			$.unblockUI();
			
			if (res['success']) {
				data = res['excelMappings'];
				
				if(data.length!=set_list_count) {
					if(confirm("저장된 매크로 항목갯수와 현재 칼럼갯수가 맞지 않습니다.\r\n그래도 진행을 하시겠습니까?")) {
						fncUserMapping(data);
					}
				}
			}
			

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

//컬럼 맵핑 정보를 조회한다.
function delExcelMapping() {
	var mapName = $("#mapList option:selected").text();
	var mapId = $("#mapId").val();	
	if(mapId=='') {
		alert("매크로정보를 선택해주시기 바랍니다.");
		return false;
	}
	
	if(!confirm("매크로정보["+mapName+"]를 삭제하시겠습니까?")) return false;
	
	var formData = new FormData();
	formData.append("mapId", mapId);
	formData.append("excelType", mapModel.excelType);
	
	var url = rootPath + 'popup/delExcelMapping.do';

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
			$.unblockUI();
			
			if (res['success']) {
				getExcelMaster(mapModel.excelType, mapModel.id);
				jAlert('삭제되었습니다.' , '');
			}
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

//컬럼 맵핑 정보를 등록한다.
function saveExcelMappingData(){
	var mapId = '';
	var mapName = prompt("매크로로 저장할 명칭을 입력해주세요.");
	if(isNull(mapName)) {
		alert("명칭을 입력해주시기 바랍니다.");
		return false;		
	}
	if(excelMappingDatas == null){		
		// 상태 초기화
		initExcel(true);		
		
		fncClose();		
		return false;
	}
	var jSonData = JSON.parse(JSON.stringify(excelMappingDatas));			 
	
	var formData = new FormData();
	formData.append("mappingData", JSON.stringify(jSonData['value']));
	formData.append("id", mapModel.id);
	formData.append("mapId", mapId);
	formData.append("mapName", mapName);
	formData.append("excelType", mapModel.excelType);
	
	var url = rootPath + 'popup/saveExcelMapping.do';
	
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
				
			if (!res['success']) {
				jAlert('매크로 정보 등록중 오류가 발생했습니다.','');
			}else {
				getExcelMaster(mapModel.excelType, mapModel.id);
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
  // 1. 근로자정보
  // C. 계약조건
  dataUrl = rootPath + 'data/';
  
  switch (mapModel.excelType) {
    case '1':
    	dataUrl = dataUrl + 'personnel/샘플_인사데이터.xls';
    	break;
    case 'C':
    	fn_fileDownload(excelFileUrl);
    	return;
    	break;    	
  }

  evt.target.setAttribute('href', dataUrl);  
}

//취소 / 닫기
function fncClose() {

	if (isValidate) {
		jConfirm('작업 진행 중인 데이터가 삭제됩니다.\n취소하시겠습니까?', '', function(cfrm){		
			if(cfrm){			  
			  isValidate = false;
			  initExcel(true);

			  window.close();
			}
		});
	} else {		
		window.close();
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
				getUpIndex(cIdx, colsVal.columnId, true);
				
    			autoUpdCnt++;
			 }
		 }
	}

	/*
	if(autoUpdCnt > 0){
		jAlert(autoUpdCnt + '건의 컬럼이 자동 맵핑 되었습니다.','');
    }
    */

}

//엑셀 사용자 맵핑
function fncUserMapping(data){
	
	var autoUpdCnt = 0;
	
	 for(var idx = 0 ; idx < getLists.cols.length ; idx++){
		 var colsVal = getLists.cols[idx];
		 
		 for(var r = 0 ; r < data.length; r++ ) {
			 if(colsVal.columnId == data[r].columnId) {
				 var rIdx = parseInt(data[r]['mappingOrder']);
				 
				 if(rIdx > -1) {
					 var rowsVal = setLists.cols[rIdx];
					 if(!isNull(rowsVal)){
						//데이타 선택
						 setUpIndex(rIdx, rowsVal.setIndex, rowsVal.name);
						 //컬럼타이틀 선택 
						 getUpIndex(idx, colsVal.columnId, true);
			
						 autoUpdCnt++;						 
					 }
				 }
			 }
		 }
	}
	 
	if(autoUpdCnt > 0){
		jAlert(autoUpdCnt + '건의 컬럼이 선택 되었습니다.','');
	}

}

