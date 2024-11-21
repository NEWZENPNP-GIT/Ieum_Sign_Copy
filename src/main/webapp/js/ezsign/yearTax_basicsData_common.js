//페이지 이동 체크 값
var checkUnload = false;
var 이월기부금반영 = false;
var userType;
$(document).ready(function() {
	
	//페이지 이동체크
	$(window).on("beforeunload", function(){
		if(this.checkUnload) return "이 페이지를 벗어나면 작성된 내용은 저장되지 않습니다.";
	});   
	
	if($("#saveBtn").length > 0){
		$("#saveBtn").click(function() {
			checkUnload = false;	    
		});
	}
	
});

/** 
 * Windows Event 셋팅
 * @returns
 */
function setDocumentEvent(){
	
	//input text 컴마있는 number 설정
	if($("input:text[commaNumberOnly]").length > 0){
		$("input:text[commaNumberOnly]").on("keyup", function() {
	        $(this).val(addCommas($(this).val().replace(/[^0-9]/g,"")));
	    });
	}
	
	//input text 컴마없는 number 설정
	if($("input:text[noCommaNumberOnly]").length > 0){
		$("input:text[noCommaNumberOnly]").on("keyup", function() {
	        $(this).val($(this).val().replace(/[^0-9]/g,""));
	    });
	}
	
	//페이지 이동 체크 (input창에 이벤트가 발생할때만)
	if($("input[type='text'], input[type='date']").length > 0){
		$("input[type='text'], input[type='date']").keydown(function() {
			checkUnload = true;	    
		});
	}
	if($("textarea").length >0){
		$("textarea").keydown(function() {
			checkUnload = true;
	 	});
	}

}


/*
 * 모든항목을 활성화 시킨다.
 */
function objEnabled(){
	
	$('.group_wrap').find('a[id^="addDataBtn"]').each(function() {
		$(this).removeClass("disabled");
   		//$(this).css("pointer-events","").css("cursor", "").css("background-color", "").css("border", "").css("color", "");
   	});
	$('.group_wrap').find('a[id^="delDataBtn"]').each(function() {
		$(this).removeClass("disabled");
   		//$(this).css("pointer-events","").css("cursor", "").css("background-color", "").css("border", "").css("color", "");
   	});
    
	if($("input[workerEditCheck]").length > 0){
		$("input[workerEditCheck]").each(function() {
			$(this).prop("disabled", false);
		});
	}
	if($("select[workerEditCheck]").length > 0){
		$("select[workerEditCheck]").each(function() {
			$(this).prop("disabled", false);
		});
	}
	if($("textarea[workerEditCheck]").length > 0){
		$("textarea[workerEditCheck]").each(function() {
			$(this).prop("disabled", false);
		});
	}
	
	if($("#saveBtn").length > 0){
		$("#saveBtn").css("pointer-events","").css("cursor", "").css("background-color", "");
	}
	
	if($('.chosen-select').length > 0){
		$(".chosen-select").chosen({ width: '100%'});
		$(".chosen-select").trigger("chosen:updated");
	}
    
}

/*
 * 모든항목을 비활성화 시킨다.
 */
function objDisabled(){
	
	$('.group_wrap').find('a[id^="addDataBtn"]').each(function() {
		$(this).addClass("disabled");
   		//$(this).css("pointer-events","").css("pointer-events","none").css("cursor", "default").css("background-color", "#f7f8f9").css("border", "1px solid #bac2c5").css("color", "#c6cfd4");
   	});
	$('.group_wrap').find('a[id^="delDataBtn"]').each(function() {
		$(this).addClass("disabled");
		//$(this).css("pointer-events","").css("pointer-events","none").css("cursor", "default").css("background-color", "#f7f8f9").css("border", "1px solid #bac2c5").css("color", "#c6cfd4");
   	});
    	
	if($("input[workerEditCheck]").length > 0){
		$("input[workerEditCheck]").each(function() {
		   $(this).prop("disabled", true);
		});
	}
	if($("select[workerEditCheck]").length > 0){
		$("select[workerEditCheck]").each(function() {
		   $(this).prop("disabled", true);
		});
	}
	if($("textarea[workerEditCheck]").length > 0){
		$("textarea[workerEditCheck]").each(function() {
	       $(this).prop("disabled", true);
		});
	}
	
	if($("#saveBtn").length > 0){
		$("#saveBtn").css("pointer-events","none").css("cursor", "default").css("background-color", "#d6d9da");
	}
	
	if($('.chosen-select').length > 0){
		$(".chosen-select").chosen({ width: '100%', max_selected_options: -1 });
		$(".chosen-select").trigger("chosen:updated");
	}
    
}

/*
 * 파라메타 정보를 암호화 시킨다.
 */
function paramEncrypt(param){
	var encrypt = "";
	var hashedPassword = "";
	
	if(isNull(sessionStorage.getItem('paramEncryptPassword'))){
		jAlert("파라메타 암호화  패스워드 정보가 없습니다.", 'Error Message', goHome);
		return false;
	}else{
		hashedPassword = CryptoJS.SHA256(sessionStorage.getItem('paramEncryptPassword')).toString();
		encrypt = CryptoJS.AES.encrypt(param, hashedPassword);
	}
	
	return encrypt;
}

/*
 * 파라메타 정보를 복호화 한다.
 */
function paramDecrypt(param){	
	var decrypted = "";
	var hashedPassword = "";
	var result = "";
	
	if(isNull(sessionStorage.getItem('paramEncryptPassword'))){
		jAlert("파라메타 복호화 패스워드 정보가 없습니다.", 'Error Message', goHome);
		return false;
	}else{
		hashedPassword = CryptoJS.SHA256(sessionStorage.getItem('paramEncryptPassword')).toString();
		decrypted = CryptoJS.AES.decrypt(param, hashedPassword );
		result = decrypted.toString(CryptoJS.enc.Utf8);
	}
	
	return result;
}


function setSelectionRange(input, selectionStart, selectionEnd) {
	if (input.setSelectionRange) {
		input.focus();
		input.setSelectionRange(selectionStart, selectionEnd);
	}
	else if (input.createTextRange) {
		var range = input.createTextRange();
		range.collapse(true);
		range.moveEnd('character', selectionEnd);
		range.moveStart('character', selectionStart);
		range.select();
	}
}

function setShortcutKey() {
	 if($( "#btn_left[type=checkbox]" ).length > 0){
		isShortcutKey=true;
		$('#yearTax_wrap').chaves({
			activeClass: 'on',
			bindings: [
				['`', '', function(){ $( "#btn_left[type=checkbox]" ).trigger("click"); }]
			],
			//className: 'keys',
			//childSelector: 'p',
			//enableUpDown: true
			//searchSelector: '#q'
	    });
	 }
}