var model = {
		nonedata: [],
		nonedataCheckedAll: false,
		noneSrchType: 'all',
		noneSrchField: ['empName', 'empNo', 'deptName'],
		noneKeyword: '',
		allotdata: [],
		allotdataCheckedAll: false,
		allotSrchType: 'all',
		allotSrchField: ['empName', 'empNo', 'deptName'],
		allotKeyword: '',
		grpName: '',
		idx: 0,
		deptCode: '',
		useYn: 'Y'
  	};

//CSS 파일 로드
var loadCssFile = function(){
	var fontPcCss = document.createElement( "link" );
	fontPcCss.href = rootPath + "css/font_pc.css";
	fontPcCss.type = "text/css";
	fontPcCss.rel = "stylesheet";
	fontPcCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( fontPcCss );
	
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
	
	var ngxDatatableCss = document.createElement( "link" );
	ngxDatatableCss.href = rootPath + "css/ngx-datatable.css";
	ngxDatatableCss.type = "text/css";
	ngxDatatableCss.rel = "stylesheet";
	ngxDatatableCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( ngxDatatableCss );
	
	var companyManageCss = document.createElement( "link" );
	companyManageCss.href = rootPath + "css/company_manage.css";
	companyManageCss.type = "text/css";
	companyManageCss.rel = "stylesheet";
	companyManageCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( companyManageCss );		
	
	var popupEmployeeSearchCss = document.createElement( "link" );
	popupEmployeeSearchCss.href = rootPath + "css/ezsign/dept/group_popup_employeeSearch.css";
	popupEmployeeSearchCss.type = "text/css";
	popupEmployeeSearchCss.rel = "stylesheet";
	popupEmployeeSearchCss.media = "screen,print";
	document.getElementsByTagName( "head" )[0].appendChild( popupEmployeeSearchCss );
} 


$(document).ready(function() {	
	//CSS 파일 로드
	loadCssFile();
	
	model.idx = opener.paramIndex;
	model.grpName = opener.paramDeptName;	
	model.deptCode = opener.paramDeptCode;
	
	$("#model_grpName").html(model.grpName);
	
	getEmpList();	
});


function getEmpList() {
	
	var formData = new FormData();
    if (isNotNull(model.deptCode)) {
    	formData.append('deptCode', model.deptCode);
    	formData.append('useYn', model.useYn);
    }

    //blockUi 호출
	fncOnBlockUI();
		
	var url = rootPath + 'dept/getDeptEmpList.do';
	
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
				model.allotdata = res['allotdata'];
				model.nonedata = res['nonedata'];
				
				//테이블작성
				createTbody();

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

//태아불울 작성한다.
function createTbody(){
	
	var noneDataHtml = "";				
	for(var idx = 0 ; idx < model.nonedata.length ; idx++){
		var row = model.nonedata[idx];
		
		noneDataHtml +="<tr>";
		noneDataHtml +="<td class='chk_area'><input type='checkbox' id='nonedata_state_"+idx+"' onChange='checkEach(\""+idx+"\", \"nonedata\")'></td>";
		noneDataHtml +="<td>"+row.empName+"</td>";
		noneDataHtml +="<td>"+row.empNo+"</td>";
		noneDataHtml +="<td>"+row.deptName+"</td>";
		noneDataHtml +="</tr>";
	}
	$("#noneDataTbody").html(noneDataHtml);
	
	$("#noneKeyword").keyup(function() {
        var k = $(this).val();
        $("#noneDataTable > tbody > tr").hide();
        
        if($("#noneSrchType").val() == "empName"){
        	var temp = $("#noneDataTable > tbody > tr > td:nth-child(2):contains('" + k + "')");
            $(temp).parent().show();
        }else if($("#noneSrchType").val() == "empNo"){
        	var temp = $("#noneDataTable > tbody > tr > td:nth-child(3):contains('" + k + "')");
            $(temp).parent().show();
        }else if($("#noneSrchType").val() == "deptName"){
        	var temp = $("#noneDataTable > tbody > tr > td:nth-child(4):contains('" + k + "')");
            $(temp).parent().show();
        }else{
        	/*var temp = $("#empTable > tbody > tr > td:nth-child(4n+2):contains('" + k + "')");
            $(temp).parent().show();*/
        	var temp = $("#noneDataTable > tbody > tr > td:nth-child(1n+1):contains('" + k + "')");
            $(temp).parent().show();
        }	                
    });
	
    var allotDataHtml = "";				
	for(var idx = 0 ; idx < model.allotdata.length ; idx++){
		var row = model.allotdata[idx];
		
		allotDataHtml +="<tr>";
		allotDataHtml +="<td class='chk_area'><input type='checkbox' id='allotdata_state_"+idx+"' onChange='checkEach(\""+idx+"\", \"allotdata\")'></td>";
		allotDataHtml +="<td>"+row.empName+"</td>";
		allotDataHtml +="<td>"+row.empNo+"</td>";
		allotDataHtml +="<td>"+row.deptName+"</td>";
		allotDataHtml +="</tr>";
	}
	$("#allotDataTbody").html(allotDataHtml);

	$("#allotKeyword").keyup(function() {
        var k = $(this).val();
        $("#allotDataTable > tbody > tr").hide();
        
        if($("#allotSrchType").val() == "empName"){
        	var temp = $("#allotDataTable > tbody > tr > td:nth-child(2):contains('" + k + "')");
            $(temp).parent().show();
        }else if($("#allotSrchType").val() == "empNo"){
        	var temp = $("#allotDataTable > tbody > tr > td:nth-child(3):contains('" + k + "')");
            $(temp).parent().show();
        }else if($("#allotSrchType").val() == "deptName"){
        	var temp = $("#allotDataTable > tbody > tr > td:nth-child(4):contains('" + k + "')");
            $(temp).parent().show();
        }else{
        	/*var temp = $("#empTable > tbody > tr > td:nth-child(4n+2):contains('" + k + "')");
            $(temp).parent().show();*/
        	var temp = $("#allotDataTable > tbody > tr > td:nth-child(1n+1):contains('" + k + "')");
            $(temp).parent().show();
        }	                
    });
	
}

// 적용
function appendEmployee() {
    /*this.outputEmployeeProperty.emit({
      employee: this.model.allotdata,
      noneEmp: this.model.nonedata,
      idx: this.model.idx
    });*/
    
	var employeeInfo = {
			employee: model.allotdata,
			noneEmp: model.nonedata,
			idx: model.idx
		};
	
	opener.closeEmployee(employeeInfo);	
    winClose();
}

function selectSrch(type) {

    $("#"+type+"Keyword").val('');
    model[type + 'SrchType'] = $("#"+type+"SrchType").val();
        
    model[type + 'SrchField'] = [$("#"+type+"SrchType").val()];
    if (model[type + 'SrchType'] == 'all') {
    	model[type + 'SrchField'] = ['empName', 'empNo', 'deptName'];
    }
    
    //리트스 초기화
    var temp = $("#"+type+"DataTable > tbody > tr > td:nth-child(1n+1):contains('')");
    $(temp).parent().show();
    
}

function checkAll(target) {

	if($("input:checkbox[id='"+target+"CheckedAll']").is(":checked")){
		model[target].forEach(function(item, index){
			$("input:checkbox[id='"+target+"_state_"+index+"']").prop("checked", true);			
			item.state = true;
		});
	}else{
		model[target].forEach(function(item, index){
			$("input:checkbox[id='"+target+"_state_"+index+"']").prop("checked", false);			
			item.state = false;
		});
	}
	
}

function checkEach(index, target) {
    var checkedCnt = 0;
    
    if($("input:checkbox[id='"+target+"_state_"+index+"']").is(":checked")){
    	model[target][index]['state'] = true; 
    }else{
    	model[target][index]['state'] = false;
    }
}

//배정근로자로 이동한다.
function empAssign(isAll) {
	
	//blockUi 호출
	fncOnBlockUI();
	
    var tmpList = [];
    model.nonedata.forEach(function(v, idx){
    	if (isAll) {
    		v.state = true;
    		v.dbMode = 'C';
    		v.deptCode = model.deptCode;
    		v.deptName = model.grpName;
    	}
    	if (v.state) {
    		v.state = false;
    		v.dbMode = 'C';
    		v.deptCode = model.deptCode;
    		v.deptName = model.grpName;
    		model.allotdata.push(v);
    	} else {
    		tmpList.push(v);
    	}
    });
    model.nonedata = tmpList;

    //배열 정렬
    model.allotdata.sort(function(a, b) {
        return a.empNo < b.empNo ? -1 : a.empNo > b.empNo ? 1 : 0;
    });
    
    model.nonedataCheckedAll = false;
    model.allotdataCheckedAll = false;

    $("input:checkbox[id='nonedataCheckedAll']").prop("checked", false);
    $("input:checkbox[id='allotdataCheckedAll']").prop("checked", false); 

    //테이블작성
    createTbody();
    
    $.unblockUI();
}

//배정근로자를 제거한다.
function empReturn(isAll) {
    
	//blockUi 호출
	fncOnBlockUI();
	
	var tmpList = [];
    model.allotdata.forEach(function(v, idx){
    	if (isAll) {
    		v.state = true;
    		v.dbMode = 'D';
    		v.deptCode = '';
    		v.deptName = '';
    	}
    	if (v.state) {
    		v.state = false;
    		v.deptCode = '';
    		v.deptName = '';
    		v.dbMode = 'D';
    		model.nonedata.push(v);
    	} else {
    		tmpList.push(v);
    	}
    });                
    model.allotdata = tmpList;
    
    //배열 정렬
    model.nonedata.sort(function(a, b) {
        return a.empNo < b.empNo ? -1 : a.empNo > b.empNo ? 1 : 0;
    });
    
    model.nonedataCheckedAll = false;
    model.allotdataCheckedAll = false;

    $("input:checkbox[id='nonedataCheckedAll']").prop("checked", false);
    $("input:checkbox[id='allotdataCheckedAll']").prop("checked", false); 

    //테이블작성
    createTbody();
    
    $.unblockUI();
}

function winClose(){
	self.close();
}