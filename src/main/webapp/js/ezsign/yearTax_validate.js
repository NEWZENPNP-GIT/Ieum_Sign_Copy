function isEmpty(param) {
    // 빈값체크
    var trimParam = param == undefined || param == null || (typeof param != 'number' && $.trim(param) == '');

    // 빈값일때
    if (trimParam) {
        return true;
    }
    // 빈값이 아닐때
    else {
        return false;
    }
}


/**
*
* @param rrn 주민번호, 외국인번호, 법인번호 일괄 체크
*/
function allCheck(rrn){
   if (rrnCheck(rrn) || fgnCheck(rrn) || bznCheck(rrn) || bzSanCheck(rrn)) {
       return true;
   }

   return false;
}

/**
*
* @param rrn 주민번호 체크
*/
function rrnCheck(rrn){

   var sum = 0;
   if (rrn.length != 13) {
       return false;
   } else if (rrn.substr(6, 1) != 1 && rrn.substr(6, 1) != 2 && rrn.substr(6, 1) != 3 && rrn.substr(6, 1) != 4) {
       return false;
   }

   for (var i = 0; i < 12; i++) {
       sum += Number(rrn.substr(i, 1)) * ((i % 8) + 2);
   }

   if (((11 - (sum % 11)) % 10) == Number(rrn.substr(12, 1))) {
       return true;
   }
   return false;
}

/**
*
* @param fgnno 외국인번호 체크
*/
function fgnCheck(fgnno){
	var sum = 0;
	if (fgnno.length != 13) {
		return false;
	} else if (fgnno.substr(6, 1) != 5 && fgnno.substr(6, 1) != 6 && fgnno.substr(6, 1) != 7 && fgnno.substr(6, 1) != 8) {
		return false;
	}
	if (Number(fgnno.substr(7, 2)) % 2 != 0) {
		return false;
	}
	for (var i = 0; i < 12; i++) {
		sum += Number(fgnno.substr(i, 1)) * ((i % 8) + 2);
	}
	if ((((11 - (sum % 11)) % 10 + 2) % 10) == Number(fgnno.substr(12, 1))) {
		return true;
	}
	return false;
}

/**
*
* @param bubinNum 법인번호 체크
*/
function bznCheck(bubinNum){

   var as_Biz_no = String(bubinNum);
   var I_TEMP_SUM = 0;
   var I_CHK_DIGIT = 0;

   if (bubinNum.length != 13) {
       return false;
   }

   for(var index01 = 1; index01 < 13; index01++) {
       var i = index01 % 2;
       var j = 0;

       if (i == 1) {
           j = 1;

       } else if (i == 0) {
           j = 2;

       }

       I_TEMP_SUM = I_TEMP_SUM + parseInt(as_Biz_no.substring(index01 - 1, index01), 10) * j;
   }

   I_CHK_DIGIT = I_TEMP_SUM % 10;

   if (I_CHK_DIGIT != 0) {
       I_CHK_DIGIT = 10 - I_CHK_DIGIT;
   }

   if (as_Biz_no.substring(12, 13) != String(I_CHK_DIGIT)) {
       return false;

   } else {
       return true;

   }

}

function bzSanCheck(n) {
    var re = /-/g;
    var bizID = n.replace(re,'');
    var checkID = new Array(1, 3, 7, 1, 3, 7, 1, 3, 5, 1);
    var tmpBizID, i, chkSum=0, c2, remander;

    for (i=0; i<=7; i++){
    	chkSum += checkID[i] * bizID.charAt(i);
    }

    c2 = "0" + (checkID[8] * bizID.charAt(8));
    c2 = c2.substring(c2.length - 2, c2.length);

    chkSum += Math.floor(c2.charAt(0)) + Math.floor(c2.charAt(1));

    remander = (10 - (chkSum % 10)) % 10 ;

    if (Math.floor(bizID.charAt(9)) == remander){
    	return true; // OK!
    }
    return false;
}


/**
 * 이메일 형식 체크
 * param : 문자열
 */
function isEmail(param) {
    var pattern = /^(\".*\"|[A-Za-z0-9_-]([A-Za-z0-9_-]|[\+\.])*)@(\[\d{1,3}(\.\d{1,3}){3}]|[A-Za-z0-9][A-Za-z0-9_-]*(\.[A-Za-z0-9][A-Za-z0-9_-]*)+)$/i;
    // 이메일 형식
    if (pattern.test(param)) {
        return true;
    }
    // 이메일 형식 아닐때
    else {
        return false;
    }
}

/**
 * 숫자인지 체크
 * param : 문자열
 */
function isNumber(param) {
    var pattern = /([^0-9])/i;
    // 숫자일때
    if (!pattern.test(param)) {
        return true;
    }
    // 숫자가 아닐때
    else {
        return false;
    }
}

/**
 * 영문인지 체크
 * param : 문자열
 */
function isEng(param) {
    var pattern = /([^a-z^A-Z])/i;
    // 영문일때
    if (!pattern.test(param)) {
        return true;
    }
    // 영문이 아닐때
    else {
        return false;
    }
}

/**
 * 금액 3자리 , 처리
 * @param money
 */
function moneyComma(money){
    var money_tmp = commaClear(String(money));
    return money_tmp.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
}

/**
 * , 제거
 * @param money
 */
function commaClear(money){
    // money = String(money);
    // return money.replace( /,/gi, '');
    return money;
}

/**
 * 앞자리 0 제거
 * @param money
 */
function removeLeftZero(money){
    return money;
    //return money.replace(/(^0+)/, "");

}

/**
 * , 제거 후 앞자리 0 제거
 * @param money
 */
function cleanMoney(money){
    //return this.commaClear(money);
    return money;

}

/**
 * 앞자리 0 제거 후 , 처리
 * @param money
 */
function cleanMoneyComma(money){
    return money;
    //return this.moneyComma(money);
}


/**
*
* @param bizNum 사업자번호 체크
*/
function bizNumCheck(bizNum){
   console.log("# validate.util.ts => bizNumCheck #" );
   console.log("# bizNum : " + bizNum );
   
   var as_Biz_no = String(bizNum).replace(/-/g, '');
   console.log("# as_Biz_no : " + as_Biz_no );
   
   var I_TEMP_SUM = 0;
   var I_CHK_DIGIT = 0;

   if (as_Biz_no.length != 10) {
      return false;
   }

   return true;
}