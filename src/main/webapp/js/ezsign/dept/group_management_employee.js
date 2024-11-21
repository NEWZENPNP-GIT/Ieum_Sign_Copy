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
		changeJdg: [],
		admEmpName: '',
		chgUser: [],
		modelIdx: 0,
		userLength: 0
  	};
  	dumModel = [
  		{
  			list: [],
  			userList: []
    	}
    ];
var msgSave = '자료를 저장하시겠습니까?';
var msgDelete = '선택한 자료를 삭제하시겠습니까?';
var msgConfirm = '저장되지 않은 데이터가 있습니다. 저장하시겠습니까? ';

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
			    	model.userLength = tmpList[i].userList.length;

			        var	addTmp = {admEmpName: model.admEmpName};
			        var addLength = { userLength: model.userLength};
			        initAssign();
			        var setInfo = Object.assign(addTmp, addLength);
			        reTmpList[i] = Object.assign(setInfo, tmpList[i]);
			    }

			    model.list = reTmpList;
				
			    var modelListHtml = "";
			    $("#modelListTbody").html(modelListHtml);
			    
			    if(model.list != null && model.list.length > 0){					
					for(var idx = 0 ; idx < model.list.length ; idx++){
						var row = model.list[idx];
						
						if(row['dbMode'] != 'D'){
							modelListHtml +="<tr>";
							modelListHtml +="<td class='chk_area'><input type='checkbox' id='row_chk' name='row_chk' value='"+idx+"' /></td>";
							modelListHtml +="<td class='tit'>부문명</td>";
							modelListHtml +="<td class='value'>";
							modelListHtml +="<input type='text' class='input_txt kor' readonly='readonly' value='"+row.deptName+"' id='row_deptName_"+idx+"' >";
							modelListHtml +="</td>";
							modelListHtml +="<td class='tit'>부문관리자</td>";
							modelListHtml +="<td class='value'>";
							modelListHtml +="<fieldset>";
							modelListHtml +="<div class='default_box' id='row_admEmpName_"+idx+"'>"+row.admEmpName+"</div>";
							modelListHtml +="</fieldset>";
							modelListHtml +="</td>";
							modelListHtml +="<td class='tit'>소속인원</td>";
							modelListHtml +="<td class='value'>";
							modelListHtml +="<fieldset>";
							modelListHtml +="<div class='default_box'>"+row.userLength+" 명</div>";
							modelListHtml +="<a class='btn_type btn_search Material_icons' onClick='openSearchEmployee(\""+row.deptName+"\","+idx+", \""+row.deptCode+"\")'>search</a>";
							modelListHtml +="</fieldset>";
							modelListHtml +="</td>";
							modelListHtml +="</tr>";
						}
					}
					
					$("#modelListTbody").html(modelListHtml);
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

//검색
function getSearch() {
	model.search.deptName = $("#model_search_deptName").val();
	getDeptList();
}


var paramDeptName = "";
var paramIndex = "";
var paramDeptCode = "";
function openSearchEmployee(deptName, idx, deptCode) {
	paramDeptName = deptName;
	paramIndex = idx;
	paramDeptCode = deptCode;
	openWin( rootPath + "html/dept/group_popup_employeeSearch.html","popEmployeeSearch","654","571");	
}


function closeEmployee(employeeInfo) {
	
//	logI(JSON.stringify(employeeInfo));
	
	// 선택한 열의 중간관리자 정보의 dbMode -> D 로 변경
    var subChk = model.list[employeeInfo.idx].userList;
    subChk.forEach(function(v, i){
    	v.dbMode = 'D';
    	v.userId = v.userId;
    });
    
    // 선택된 열의 dbMode -> U 로 변경
    var chkDbmode = model.list[employeeInfo.idx].dbMode;
    if ( chkDbmode != 'C') {
    	model.list[employeeInfo.idx].dbMode = 'U';
    }
    
    var tmpList = [];
    employeeInfo.employee.forEach(function(vv, ii){
    	var params = {
    		dbMode: vv.dbMode,
    		empName: vv.empName,
    		userId: vv.userId
    	};
    	tmpList.push(params);
    });
    
    var tmpNone = [];
    employeeInfo.noneEmp.forEach(function(vv, ii){
    	var params = {
    		dbMode: vv.dbMode,
    		empName: vv.empName,
    		userId: vv.userId
    	};
    	tmpNone.push(params);
    });
    
    model.list[employeeInfo.idx].userList = tmpList;    
    model.chgUser = tmpList.concat(tmpNone);
    
    saveDeptList();
}

// 저장
function saveDeptList(){
	
	var tmpList = [];
    model.list[model.modelIdx].userList = model.chgUser;

    model.list.forEach(function(v, i){
    	if (v.dbMode != 'C') {
    		if (v.dbMode != 'D') {
    			v.dbMode = 'U';
    		}
    	}

    	var params = {
    		dbMode: v.dbMode,
    		deptCode: v.deptCode,
    		deptName: v.deptName,
    		subAdminList: [],
    		userList: [],
    	};

    	v.subAdminList.forEach(function(vvv, iii){
    		if ( v.dbMode == 'U') {
    			vvv.dbMode = 'U';
    		}
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

    	tmpList.push(params);
    });

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