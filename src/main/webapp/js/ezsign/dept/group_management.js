var isShow = false;
var groupList;
var model = {
		list: [],
		subAdminList: [],
		userList: [],
		search: {
			deptName: ''
		},
		managerName: '',
		srchGroup: '',
		chkDbMode: '',
		changeCnt: 0,
		admEmpName: '',
		listIdx: 0,
  	};
var chkDup = false; // 중복 값
var msgSave = '자료를 저장하시겠습니까?';
var msgDelete = '선택한 자료를 삭제하시겠습니까?';
var msgConfirm = '저장되지 않은 데이터가 있습니다. 저장하시겠습니까? ';
var selectedMiddelUserId = [];

//CSS 파일 로드
var loadCssFile = function(){
	var yearTaxCommonCss = document.createElement( "link" );
	yearTaxCommonCss.href = rootPath + "css/yearTax_common.css";
	yearTaxCommonCss.type = "text/css";
	yearTaxCommonCss.rel = "stylesheet";
	yearTaxCommonCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( yearTaxCommonCss );
	
	var companyManageCss = document.createElement( "link" );
	companyManageCss.href = rootPath + "css/company_manage.css";
	companyManageCss.type = "text/css";
	companyManageCss.rel = "stylesheet";
	companyManageCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( companyManageCss );
}

$(document).ready(function() {	
	
	//CSS 파일 로드
	loadCssFile();
	
	getDeptList();
    chkDup = false
    
    /* 검색어 엔터이벤트 */
	$("#model_search_deptName").keyup(function(e){
		if(e.keyCode == 13) {
			getSearch(); 
		}
	});	
});


function getDeptList() {
	
	var formData = new FormData();
	formData.append('deptName', model.search.deptName);
	
	//blockUi 호출
	fncOnBlockUI();
		
	var url = rootPath + 'dept/getDeptList.do';
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		async:false,
		//contentType: "application/json; charset=UTF-8",
		//data: JSON.stringify(notice),
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {		
			
			if (res.success) {
				var tmpList = res['data'];
				var reTmpList = [];

				for (var i = 0; i < tmpList.length; i++) {
					if (tmpList[i].subAdminList.length == 0) {
						tmpList[i].subAdminList.push({empName: '', userId: '', dbMode: ''});
					}
			        model.admEmpName = tmpList[i].subAdminList[0].empName;

			        var addTmp = {admEmpName: model.admEmpName};			        
			        initAssign();
			        reTmpList[i] = Object.assign(addTmp, tmpList[i]);
				}

				model.list = reTmpList;			      
				
				var modelListHtml = "";
				$("#modelListTbody").html(modelListHtml);
				if(model.list != null && model.list.length > 0){					
					for(var idx = 0 ; idx < model.list.length ; idx++){
						var row = model.list[idx];
						
						modelListHtml +="<tr>";
						modelListHtml +="<td class='chk_area'><input type='checkbox' id='row_chk' name='row_chk' value='"+idx+"' /></td>";
						modelListHtml +="<td class='tit'>부문명</td>";
						modelListHtml +="<td class='value'>";
						modelListHtml +="<input type='text' class='input_txt kor' value='"+row.deptName+"' onChange='chkDbMode("+idx+")' onBlur='chkDupl("+idx+")' id='row_deptName_"+idx+"' >";
						modelListHtml +="</td>";
						modelListHtml +="<td class='tit'>부문관리자</td>";
						modelListHtml +="<td class='value'>";
						modelListHtml +="<fieldset>";
						modelListHtml +="<div class='default_box' id='row_admEmpName_"+idx+"'>"+row.admEmpName+"</div>";
						if(row.admEmpName != ""){
							modelListHtml +="<a class='btn_type btn_close Material_icons' onClick='delMiddle("+idx+")'></a>";

						}
						else{
							modelListHtml +="<a class='btn_type btn_search Material_icons' onClick='openSearchMiddle("+idx+")'>search</a>";
						}
						modelListHtml +="</fieldset>";
						modelListHtml +="</td>";
						modelListHtml +="<td class='tit'>소속인원</td>";
						modelListHtml +="<td class='value'>";
						modelListHtml +="<fieldset>";
						modelListHtml +="<div class='default_box'>"+row.userList.length+" 명</div>";
						modelListHtml +="</fieldset>";
						modelListHtml +="</td>";
						modelListHtml +="</tr>";
					}
					
					$("#modelListTbody").html(modelListHtml);
				}else{
					//$("#modelListTbody").html("<tr><td colspan='7'>조회 결과가 없습니다.</td></tr>");					
				}
						
				$.unblockUI();
			}else{
				$.unblockUI();
				jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
			}    
		},
		error:function(request,status,error){
			$.unblockUI();			
			if (request.status=="401") {
				clearSession();
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
			}
		}
	});

}

//그룹추가
function addGroupList() {

    var chkBlank = false;
    var setGroupList = {
    	deptName: '',
    	subAdminList: [
    		{
    			dbMode: 'C',
    			userId: ''
        	}
        ],
        userList: [],
        dbMode:  'C',
        chk: false,
    };
    
    model.list.forEach(function(v){
    	if (v.dbMode == 'C' && v.deptName == '') {
    		chkBlank = true;
    	}
    });
    
    if (chkBlank) {
    	jAlert('부문명을 입력하여 주시기 바랍니다.','');
    	return false;
    } else if (chkDup) {
    	jAlert('부문명이 중복되었습니다.','');
    	return false;
    } else {
    	model.list.push(setGroupList);
    }
    
    var index = model.list.length - 1;
    
    var modelListHtml = "";
	modelListHtml +="<tr>";
	modelListHtml +="<td class='chk_area'><input type='checkbox' id='row_chk' name='row_chk' value='"+index+"' /></td>";
	modelListHtml +="<td class='tit'>부문명</td>";
	modelListHtml +="<td class='value'>";
	modelListHtml +="<input type='text' class='input_txt kor' value='' onChange='chkDbMode("+index+")' onBlur='chkDupl("+index+")' id='row_deptName_"+index+"'>";
	modelListHtml +="</td>";
	modelListHtml +="<td class='tit'>부문관리자</td>";
	modelListHtml +="<td class='value'>";
	modelListHtml +="<fieldset>";
	modelListHtml +="<div class='default_box' id='row_admEmpName_"+index+"'></div>";
	modelListHtml +="<a class='btn_type btn_search Material_icons' onClick='openSearchMiddle("+index+")'>search</a>";
	modelListHtml +="</fieldset>";
	modelListHtml +="</td>";
	modelListHtml +="<td class='tit'>소속인원</td>";
	modelListHtml +="<td class='value'>";
	modelListHtml +="<fieldset>";
	modelListHtml +="<div class='default_box'>0 명</div>";
	modelListHtml +="</fieldset>";
	modelListHtml +="</td>";
	modelListHtml +="</tr>";

	$("#modelListTbody").append(modelListHtml);
}

//선택삭제
function delGroupList(){
	
	var delindex = [];
	var chkCnt = 0;
	$("#row_chk:checked").each(function() {
		chkCnt++;
		delindex.push($(this).val());		
	});
	
	if(chkCnt == 0){
		 jAlert('선택된 정보가 없습니다.','');
		 return;
	}

	jConfirm(msgDelete, '', function(cfrm){
		if(cfrm){
			for(var index = 0 ; index < delindex.length ; index++){

				if (model.list[delindex[index]]['dbMode'] == 'R' || model.list[delindex[index]]['dbMode'] == 'U') {
					model.list[delindex[index]]['dbMode'] = 'D';

					for (var j = 0; j < model.list[delindex[index]]['subAdminList'].length; j++) {
						model.list[delindex[index]]['subAdminList'][j]['dbMode'] = 'D';
			        }
			        for (var k = 0; k < model.list[delindex[index]]['userList'].length; k++) {
			        	model.list[delindex[index]]['userList'][k]['dbMode'] = 'D';
			        }					
					
		         } else if (model.list[delindex[index]]['dbMode'] == 'C') {
		        	 model.list[delindex[index]]['dbMode'] = 'X';
		        	 for (var j = 0; j < model.list[delindex[index]]['subAdminList'].length; j++) {
		        		 model.list[delindex[index]]['subAdminList'][j]['dbMode'] = 'X';
		        	 }		        	        	
		         }
				
			}			
			
			//
			var url = rootPath + 'dept/saveDept.do';
			
			//blockUi 호출
			fncOnBlockUI();
			
			$.ajax({
				url:url,
			    crossDomain : true,
				dataType:"json",
				type:"POST",
				contentType: "application/json; charset=UTF-8",
				data: JSON.stringify(model.list),
				//processData: false,
			    //contentType: false,
				//data: formData,
				success:function(res) {	
					
					if (res.success) {						
						jAlert('삭제되었습니다.','');
						getDeptList();
						
						$.unblockUI();	    						
					} else {
						$.unblockUI();		
						jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
					}
					
				},
				error:function(request,status,error){
					$.unblockUI();
						
					if (request.status=="401") {
						clearSession();
						jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
					} else {
						jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
					}
				}
			});			
						
		}
	});	
	
}

//저장
function saveDeptList() {
    var tmpList = [];
    var chkBlank = false;

    model.list.forEach(function(v, i){
    	var params = {
    		dbMode: v.dbMode,
    		deptCode: v.deptCode,
    		deptName: v.deptName,
    		subAdminList: [],
    		userList: [],
    	};

    	v.subAdminList.forEach(function(vvv, iii){
    		params.subAdminList.push({
    			dbMode: vvv.dbMode,
    			userId: vvv.userId
    		});
    	});

    	v.userList.forEach(function(vv, ii){
    		params.userList.push({
    			dbMode: vv.dbMode,
    			userId: vv.userId
    		});
    	});

    	// 빈 값 체크
    	if (v.dbMode == 'C' && v.deptName == '') {
    		chkBlank = true;
    	}
    	tmpList.push(params);
    });

    if (chkBlank) {
    	jAlert('부문명을 입력하여 주시기 바랍니다.','');
    	return false;
    } else if (chkDup) {
    	jAlert('부문명이 중복되었습니다.','');
    	return false;
    } else {
    	
    	jConfirm(msgSave,'',function(cfrm){
    		if(cfrm){
    			
    			var url = rootPath + 'dept/saveDept.do';
    			
    			//blockUi 호출
    			fncOnBlockUI();
    			
    			$.ajax({
    				url:url,
    			    crossDomain : true,
    				dataType:"json",
    				type:"POST",
    				contentType: "application/json; charset=UTF-8",
    				data: JSON.stringify(tmpList),
    				//processData: false,
    			    //contentType: false,
    				//data: formData,
    				success:function(res) {	
    					
    					if (res.success) {
    						jAlert('저장이 완료되었습니다.','');
    						getDeptList();
    						$.unblockUI();	    						
    					} else {
    						$.unblockUI();		
    						jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
    					}
    					
    				},
    				error:function(request,status,error){
    					$.unblockUI();
    						
    					if (request.status=="401") {
    						clearSession();
    						jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
    					} else {
    						jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
    					}
    				}
    			});			
    			
    		}
    	});

    }
}

//검색
function getSearch() {
	model.search.deptName = $("#model_search_deptName").val();
	getDeptList();
}

// 관리자 등록
function fn_addSubUser() {
	openWin(rootPath + 'html/dept/dept_popup_addManager.html', '기업부문관리자', '440', '470');
}

function callFunc() {
}


//DB Mode 변경
function chkDbMode(index) {
	
	model.list[index].deptName = $("#row_deptName_"+index).val();
	
    if (model.list[index].dbMode != 'C') {
    	model.list[index].dbMode = "U";
    }
}

//그룹명 중복체크
function chkDupl(index) {
	var idxDeptName = model.list[index].deptName;
	chkDup = false;
	
	model.list.forEach(function(v, ii) {
		if ( index != ii ) {
			if (v.deptName == idxDeptName) {
				chkDup = true;
			}
		}
    });
	
}

function openSearchMiddle(idx) {
	model.listIdx = idx;
    //this.GroupPopupMiddleManagerComponent.open(idx);
	
	openWin(rootPath + "html/dept/group_popup_middleManager.html","popMiddleManager","400","500");	
}

function setManager(openResult){
	//logI(JSON.stringify(openResult));	
	
	var subAdminIndex = 0;	
	if(model.list[model.listIdx].subAdminList.length > 0){
		subAdminIndex = (model.list[model.listIdx].subAdminList.length - 1);
	}
	
	if(isNull(model.list[model.listIdx].subAdminList[subAdminIndex].dbMode)) {
		model.list[model.listIdx].subAdminList[subAdminIndex].dbMode = "C";
		model.list[model.listIdx].subAdminList[subAdminIndex].userId = openResult.userId;
	}else if(model.list[model.listIdx].subAdminList[subAdminIndex].dbMode == "R") {
		model.list[model.listIdx].subAdminList[subAdminIndex].dbMode = "D";		
		model.list[model.listIdx].subAdminList.push({'dbMode': 'C', 'userId': openResult.userId});		
	}else if(model.list[model.listIdx].subAdminList[subAdminIndex].dbMode == "C") {
		model.list[model.listIdx].subAdminList[subAdminIndex].userId = openResult.userId;
	}
		
	/*if (model.list[model.listIdx].dbMode != 'C') {
		model.list[model.listIdx].dbMode = "U";
	}*/	
	
	$("#row_admEmpName_"+model.listIdx).html(openResult.empName);
	selectedMiddelUserId.push(openResult.userId);
}


function initAssign(){
	if (!Object.assign) {
		  Object.defineProperty(Object, 'assign', {
		    enumerable: false,
		    configurable: true,
		    writable: true,
		    value: function(target) {
		      'use strict';
		      if (target === undefined || target === null) {
		        throw new TypeError('Cannot convert first argument to object');
		      }

		      var to = Object(target);
		      for (var i = 1; i < arguments.length; i++) {
		        var nextSource = arguments[i];
		        if (nextSource === undefined || nextSource === null) {
		          continue;
		        }
		        nextSource = Object(nextSource);

		        var keysArray = Object.keys(Object(nextSource));
		        for (var nextIndex = 0, len = keysArray.length; nextIndex < len; nextIndex++) {
		          var nextKey = keysArray[nextIndex];
		          var desc = Object.getOwnPropertyDescriptor(nextSource, nextKey);
		          if (desc !== undefined && desc.enumerable) {
		            to[nextKey] = nextSource[nextKey];
		          }
		        }
		      }
		      return to;
		    }
		  });
	}
}


//중간관리자 배정삭제
function delMiddle(idx) {
	var params = {
		subAdminList: [],
		deptCode: model.list[idx].deptCode
	};
	params.subAdminList.push({
		userId: model.list[idx].subAdminList[0].userId
	});



console.log(model.list[idx].userId);

	jConfirm('배정된 부문관리자를 삭제하시겠습니까?','',function(cfrm){
			if(cfrm){

				var url = rootPath + 'dept/delMiddle.do';

				//blockUi 호출
				fncOnBlockUI();

				$.ajax({
					url:url,
					crossDomain : true,
					dataType:"json",
					type:"POST",
					contentType: "application/json; charset=UTF-8",
					data: JSON.stringify(params),
					//processData: false,
					//contentType: false,
					//data: formData,
					success:function(res) {

						if (res.success) {
							jAlert('삭제가 완료되었습니다.','');
							selectedMiddelUserId = [];
							getDeptList();
							$.unblockUI();
						} else {
							$.unblockUI();
							jAlert('오류가 발생 했습니다. 브라우저를 새로고침 후 다시 시도해 주세요.','');
						}

					},
					error:function(request,status,error){
						$.unblockUI();

						if (request.status=="401") {
							clearSession();
							jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
						} else {
							jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
						}
					}
				});

			}
		});


}