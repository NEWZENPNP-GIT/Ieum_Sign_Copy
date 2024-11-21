var totalIncomeProc = false;


/* 근로자 정보 저장후 재집계 실행 */
function yearTaxTotalIncome(계약년도, 계약ID, 사용자ID, 근무시기){
	
	/*var params = {
	      계약ID: contractId,
	      사용자ID: userId,
	      근무시기: workPeriod
	    };*/
	
	var formData = new FormData();
    formData.append('계약ID', 계약ID);
    formData.append('사용자ID', 사용자ID);
    formData.append('근무시기', 근무시기);
  
	var url = rootPath + 'febworker/getYP040.do';
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		//contentType: "application/json; charset=UTF-8",
		async:false,
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {
						
			var totalIncome = [{
				    '회사명': '',
				    'bizId': '',
				    'code': '',
				    'febYear': '',
				    'g01': '',
				    'h01': '',
				    'h05': '',
				    'h06': '',
				    'h07': '',
				    'h08': '',
				    'h09': '',
				    'h10': '',
				    'h11': '',
				    'h12': '',
				    'h13': '',
				    'h14': '',
				    'h15': '',
				    'h16': '',
				    'i01': '',
				    'k01': '',
				    'm01': '',
				    'm02': '',
				    'm03': '',
				    'message': '',
				    'o01': '',
				    'q01': '',
				    'r10': '',
				    'r11': '',
				    's01': '',
				    't01': '',
				    't10': '',
				    't11': '',
				    't12': '',
				    't20': '',
				    'y02': '',
				    'y03': '',
				    'y04': '',
				    'y22': '',
				    '감면소득합계': '',
				    '감면시작일': '',
				    '감면종료일': '',
				    '건강보험료': '',
				    '계약ID': '',
				    '고용보험료': '',
				    '공무원연금': '',
				    '공제합계': '',
				    '국민연금보험료': '',
				    '군인연금': '',
				    '근로소득금액': '',
				    '근무시작일': '',
				    '근무종료일': '',
				    '근무지구분': '',
				    '급여': '',
				    '농어촌특별세': '',
				    '등록일시': '',
				    '별정우체국연금': '',
				    '비과세합계': '',
				    '사립학교교직원연금': '',
				    '사업자등록번호': '',
				    '사용자ID': '',
				    '상여': '',
				    '소득세': '',
				    '수정일시': '',
				    '우리사주조합인출금': '',
				    '인정상여': '',
				    '임원퇴직소득금액한도초과액': '',
				    '장기요양보험료': '',
				    '주식매수선택권행사이익': '',
				    '지방소득세': '',
				    '총급여': '',
				    '현근무지여부': '',
				    // 2018 분기
				    '직무발명보상금': '', //  - 046
				    't13': '', // 2018신규 중소기업90% -047
				    'h17': '', // 2018신규 종교활동비 -048
				    'u01': '', // 2018신규 벤처기업 주식매수선택권 -049
				    'h03': ''  // SBS 차량지원금 -050
				  }];
			
			var sumdata = {
				    'g01': '',
				    'h01': '',
				    'h05': '',
				    'h06': '',
				    'h07': '',
				    'h08': '',
				    'h09': '',
				    'h10': '',
				    'h11': '',
				    'h12': '',
				    'h13': '',
				    'h14': '',
				    'h15': '',
				    'h16': '',
				    'i01': '',
				    'k01': '',
				    'm01': '',
				    'm02': '',
				    'm03': '',
				    'o01': '',
				    'q01': '',
				    'r10': '',
				    'r11': '',
				    's01': '',
				    't01': '',
				    't10': '',
				    't11': '',
				    't12': '',
				    't20': '',
				    'y02': '',
				    'y03': '',
				    'y04': '',
				    'y22': '',
				    '감면소득합계': '', //
				    '감면시작일': '',
				    '감면종료일': '',
				    '건강보험료': '',
				    '고용보험료': '',
				    '공무원연금': '',
				    '공제합계': '', //
				    '국민연금보험료': '',
				    '군인연금': '',
				    '근로소득금액': '',
				    '급여': '',
				    '농어촌특별세': '', //
				    '별정우체국연금': '',
				    '비과세합계': '', //
				    '비과세한도초과액': '',
				    '사립학교교직원연금': '',
				    '상여': '',
				    '소득세': '', //
				    '우리사주조합인출금': '',
				    '인정상여': '',
				    '임원퇴직소득금액한도초과액': '',
				    '장기요양보험료': '',
				    '주식매수선택권행사이익': '',
				    '지방소득세': '', //
				    '총급여': '',
				    // 2018 분기
				    '직무발명보상금': '', //  - 046
				    't13': '', // 2018신규 중소기업90% -047
				    'h17': '', // 2018신규 종교활동비 -048
				    'u01': '', // 2018신규 벤처기업 주식매수선택권 -049
				    'h03': ''  // SBS 차량지원금 -050
				  };
			
			var sumdataCalc = {
				    'g01': '',
				    'h01': '',
				    'h05': '',
				    'h06': '',
				    'h07': '',
				    'h08': '',
				    'h09': '',
				    'h10': '',
				    'h11': '',
				    'h12': '',
				    'h13': '',
				    'h14': '',
				    'h15': '',
				    'h16': '',
				    'i01': '',
				    'k01': '',
				    'm01': '',
				    'm02': '',
				    'm03': '',
				    'o01': '',
				    'q01': '',
				    'r10': '',
				    'r11': '',
				    's01': '',
				    't01': '',
				    't10': '',
				    't11': '',
				    't12': '',
				    't20': '',
				    'y02': '',
				    'y03': '',
				    'y04': '',
				    'y22': '',
				    '감면소득합계': '',
				    '건강보험료': '',
				    '고용보험료': '',
				    '공무원연금': '',
				    '공제합계': '', //
				    '국민연금보험료': '',
				    '군인연금': '',
				    '근로소득금액': '', //
				    '급여': '',
				    '농어촌특별세': '', //
				    '별정우체국연금': '',
				    '비과세합계': '', //
				    '비과세한도초과액': '',
				    '사립학교교직원연금': '',
				    '상여': '',
				    '소득세': '', //
				    '우리사주조합인출금': '',
				    '인정상여': '',
				    '임원퇴직소득금액한도초과액': '',
				    '장기요양보험료': '',
				    '주식매수선택권행사이익': '',
				    '지방소득세': '', //
				    '총급여': '',
				    // 2018 분기
				    '직무발명보상금': '', //  - 046
				    't13': '', // 2018신규 중소기업90% -047
				    'h17': '', // 2018신규 종교활동비 -048
				    'u01': '', // 2018신규 벤처기업 주식매수선택권 -049
				    'h03': ''  // SBS 차량지원금 -050
				  };
			
			var payItem = {
				    'bizId': '',
				    'code': '',
				    'dbMode': '',
				    'febYear': '',
				    'g01': '0',
				    'h01': '0',
				    'h05': '0',
				    'h06': '1',
				    'h07': '0',
				    'h08': '0',
				    'h09': '0',
				    'h10': '0',
				    'h11': '0',
				    'h12': '1',
				    'h13': '1',
				    'h14': '0',
				    'h15': '1',
				    'h16': '1',
				    'i01': '0',
				    'k01': '0',
				    'm01': '1',
				    'm02': '1',
				    'm03': '1',
				    'o01': '1',
				    'q01': '1',
				    'r10': '0',
				    'r11': '1',
				    's01': '0',
				    't01': '0',
				    't10': '1',
				    't11': '1',
				    't12': '1',
				    't20': '1',
				    'y02': '0',
				    'y03': '0',
				    'y04': '0',
				    'y22': '0',
				    '건강보험료': '1',
				    '고용보험료': '1',
				    '국민연금보험료': '1',
				    '급여': '0',
				    '상여': '0',
				    '소득세': '1',
				    '우리사주조합인출금': '1',
				    '인정상여': '1',
				    '임원퇴직소득금액한도초과액': '0',
				    '장기요양보험료': '1',
				    '주식매수선택권행사이익': '1',
				    '지방소득세': '0',
				    // 2018 분기
				    '직무발명보상금': '0', //  - 046
				    't13': '0', // 2018신규 중소기업90% -047
				    'h17': '0', // 2018신규 종교활동비 -048
				    'u01': '0', // 2018신규 벤처기업 주식매수선택권 -049
				    'h03': '0'  // SBS 차량지원금 -050
				  };
			
			var year = "";
			var totalNum = 0;
			var 외국인단일세율적용;
			var taxOverParams;
			
			if (res.success) {
				//logD("## yearTaxTotalIncome .success : " + res.success );
				
				year = res['계약년도'];
		        totalIncome = res['data'];
		        totalNum = res['total'];
		        외국인단일세율적용 = res['근무년월'].외국인단일세율적용;
		        if (totalNum != 0) {
		           sumdata = res['sumData'];
		        }
		        payItem = res['ys050'];
		        
		        // 비과세한도초과액 객체 초기화
		        taxOverParams = [];
		        
		        logD("# totalNum : " + totalNum );
		        
		        for ( var i = 0; i < totalNum; i++) {
		            totalIncome[i]['근무시작일'] = moment(totalIncome[i]['근무시작일'], 'YYYYMMDD').format('YYYY-MM-DD');
		            totalIncome[i]['근무종료일'] = moment(totalIncome[i]['근무종료일'], 'YYYYMMDD').format('YYYY-MM-DD');
		            totalIncome[i]['감면시작일'] = moment(totalIncome[i]['감면시작일'], 'YYYYMMDD').format('YYYY-MM-DD');
		            totalIncome[i]['감면종료일'] = moment(totalIncome[i]['감면종료일'], 'YYYYMMDD').format('YYYY-MM-DD');

		            if (totalIncome[i]['근무시작일'] == 'Invalid date' || totalIncome[i]['근무종료일'] == 'Invalid date') {
		            	if (!(i == 0 && totalIncome[i]['근무시작일'] != 'Invalid date')) {
		            		alert('근무기간이 입력되지 않았습니다. 근로자정보 페이지로 돌아갑니다.');
		            		//this.router.navigate(['/yearTax_basicsData_employee'], { queryParams: { }});
		            		return false;
		            	}
		            }

		            if ( i == 0 && totalIncome[i]['근무종료일'] == 'Invalid date') {
		            	//상반기
		   			 	if(근무시기 == '1'){
		   			 		totalIncome[i]['근무종료일'] = moment(res['계약년도'] + '0630', 'YYYYMMDD').format('YYYY-MM-DD');
		   			 	}
		   			 	//하반기
		   			 	else if(근무시기 == '2'){	
		   			 		totalIncome[i]['근무종료일'] = moment(res['계약년도'] + '1231', 'YYYYMMDD').format('YYYY-MM-DD');
		   			 	}
		            }

		            if (totalIncome[i]['감면시작일'] == 'Invalid date') {
		            	totalIncome[i]['감면시작일'] = '';
		            }
		            if (totalIncome[i]['감면종료일'] == 'Invalid date') {
		            	totalIncome[i]['감면종료일'] = '';
		            }

		            var startDate = moment(totalIncome[i]['근무시작일'], 'YYYYMMDD').format('YYYY');
		            if (Number(startDate) < Number(res['계약년도']) ) {
		            	//상반기
		   			 	if(근무시기 == '1'){
		   			 		totalIncome[i]['근무시작일'] = moment(res['계약년도'] + '0101', 'YYYYMMDD').format('YYYY-MM-DD');
		   			 	}
		   			 	//하반기
		   			 	else if(근무시기 == '2'){
		   			 		totalIncome[i]['근무시작일'] = moment(res['계약년도'] + '0701', 'YYYYMMDD').format('YYYY-MM-DD');
		   			 	}
		            }

		            startDate = moment(totalIncome[i]['감면시작일'], 'YYYYMMDD').format('YYYY');
		            if (Number(startDate) < Number(res['계약년도']) ) {		            	
		            	//상반기
		   			 	if(근무시기 == '1'){
		   			 		totalIncome[i]['감면시작일'] = moment(res['계약년도'] + '0101', 'YYYYMMDD').format('YYYY-MM-DD');
		   			 	}
		   			 	//하반기
		   			 	else if(근무시기 == '2'){
		   			 		totalIncome[i]['감면시작일'] = moment(res['계약년도'] + '0701', 'YYYYMMDD').format('YYYY-MM-DD');
		   			 	}
		            }

		            var endtDate = moment(totalIncome[i]['근무종료일'], 'YYYYMMDD').format('YYYY');
		            if (Number(endtDate) > Number(res['계약년도']) ) {
		            	//상반기
		   			 	if(근무시기 == '1'){
		   			 		totalIncome[i]['근무종료일'] = moment(res['계약년도'] + '0630', 'YYYYMMDD').format('YYYY-MM-DD');
		   			 	}
		   			 	//하반기
		   			 	else if(근무시기 == '2'){	
		   			 		totalIncome[i]['근무종료일'] = moment(res['계약년도'] + '1231', 'YYYYMMDD').format('YYYY-MM-DD');
		   			 	}		            	
		            }

		            endtDate = moment(totalIncome[i]['감면종료일'], 'YYYYMMDD').format('YYYY');
		            if (Number(endtDate) > Number(res['계약년도']) ) {
		            	//상반기
		   			 	if(근무시기 == '1'){
		   			 		totalIncome[i]['감면종료일'] = moment(res['계약년도'] + '0630', 'YYYYMMDD').format('YYYY-MM-DD');
		   			 	}
		   			 	//하반기
		   			 	else if(근무시기 == '2'){	
		   			 		totalIncome[i]['감면종료일'] = moment(res['계약년도'] + '1231', 'YYYYMMDD').format('YYYY-MM-DD');
		   			 	}
		            }

		            // 총 근무 개월 수 계산
		            var 근무시작월 = moment(totalIncome[i]['근무시작일'], 'YYYYMMDD').format('MM');
		            var 근무종료월 = moment(totalIncome[i]['근무종료일'], 'YYYYMMDD').format('MM');
		            var 근무개월 = Number(근무종료월) - Number(근무시작월) + 1;

		            // 항목별 총계 계산
		            for (var key in totalIncome[i]) {		              
		            	//console.log("## key : " + key );
		            	if ( key == '총급여' || key == '비과세급여' || key == '비과세한도초과액' || key == '감면소득합계' || key == '공제합계' ) {
		            		continue;
		            	}
		            	sumdataCalc[key] = String(Number(sumdataCalc[key]) + Number(totalIncome[i][key]));
		            }

		            // 비과세한도초과액 계산
		            var 비과세한도초과액 = 0;
		            if ( Number(totalIncome[i]['m01']) > 1000000 * 근무개월) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['m01']) - (1000000 * 근무개월));
		            }
		            if (Number(totalIncome[i]['m02']) > 3000000 * 근무개월) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['m02']) - (3000000 * 근무개월));
		            }
		            if (Number(totalIncome[i]['o01']) > 2400000 ) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['o01']) - 2400000);
		            }
		            if (Number(totalIncome[i]['q01']) > 100000 * 근무개월) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['q01']) - (100000 * 근무개월));
		            }
		            if (Number(totalIncome[i]['h08']) > 200000 * 근무개월) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['h08']) - (200000 * 근무개월));
		            }
		            if (Number(totalIncome[i]['h09']) > 200000 * 근무개월) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['h09']) - (200000 * 근무개월));
		            }
		            if (Number(totalIncome[i]['h10']) > 200000 * 근무개월) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['h10']) - (200000 * 근무개월));
		            }
		            if (Number(totalIncome[i]['h11']) > 200000 * 근무개월) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['h11']) - (200000 * 근무개월));
		            }
		            if (Number(totalIncome[i]['h12']) > 200000 * 근무개월) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['h12']) - (200000 * 근무개월));
		            }
		            if (Number(totalIncome[i]['주식매수선택권']) > 30000000) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['주식매수선택권']) - (30000000));
		            }
		            비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['y02']) / 2);
		            비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['y03']) / 4);
		            if (Number(totalIncome[i]['h16']) > 200000 * 근무개월) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['h16']) - (200000 * 근무개월));
		            }
		            if (Number(totalIncome[i]['r11']) > 3000000) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['r11']) - 3000000);
		            }
		            if (Number(totalIncome[i]['h06']) > 200000 * 근무개월) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['h06']) - (200000 * 근무개월));
		            }
		            if (Number(totalIncome[i]['h07']) > 200000 * 근무개월) {
		            	비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['h07']) - (200000 * 근무개월));
		            }

		            // 2018 분기
		            if (계약년도 == '2018') {
		               if (Number(totalIncome[i]['u01']) > 20000000) {
		            	  비과세한도초과액 = 비과세한도초과액 + (Number(totalIncome[i]['u01']) - (20000000));
		               }
		            }

		            totalIncome[i]['비과세한도초과액'] = String(Math.round(비과세한도초과액));

		            sumdataCalc['비과세한도초과액'] = String(
		            		Number(sumdataCalc['비과세한도초과액']) + Number(totalIncome[i]['비과세한도초과액'])
		            );

		            // 총급여 계산
		            totalIncome[i]['총급여'] = String(
		            		Number(totalIncome[i]['총급여']) +
		            		Number(totalIncome[i]['급여']) +
		            		Number(totalIncome[i]['상여']) +
		            		Number(totalIncome[i]['인정상여']) +
		            		Number(totalIncome[i]['주식매수선택권행사이익']) +
		            		Number(totalIncome[i]['우리사주조합인출금']) +
		            		Number(totalIncome[i]['임원퇴직소득금액한도초과액']) +
		            		Number(totalIncome[i]['비과세한도초과액'])
		            );
		            
		            // 2018 분기
		            if (계약년도 == '2018') {
		            	totalIncome[i]['총급여'] = String(
		            		Number(totalIncome[i]['총급여']) +
		            		Number(totalIncome[i]['직무발명보상금'])
		              );
		            }

		            sumdataCalc['총급여'] = String(
		            	Number(sumdataCalc['총급여']) +
		            	Number(totalIncome[i]['총급여'])
		            );

		            // 비과세합계 계산
		            totalIncome[i]['비과세합계'] = String(
		            	Number(totalIncome[i]['m01']) +
		            	Number(totalIncome[i]['m02']) +
		            	Number(totalIncome[i]['m03']) +
		            	Number(totalIncome[i]['o01']) +
		            	Number(totalIncome[i]['q01']) +
		            	Number(totalIncome[i]['h08']) +
		            	Number(totalIncome[i]['h09']) +
		            	Number(totalIncome[i]['h10']) +
		            	Number(totalIncome[i]['g01']) +
		            	Number(totalIncome[i]['h11']) +
		            	Number(totalIncome[i]['h12']) +
		            	Number(totalIncome[i]['h13']) +
		            	Number(totalIncome[i]['h01']) +
		            	Number(totalIncome[i]['k01']) +
		            	Number(totalIncome[i]['s01']) +
		            	Number(totalIncome[i]['y02']) +
		            	Number(totalIncome[i]['y03']) +
		            	Number(totalIncome[i]['y04']) +
		            	Number(totalIncome[i]['h05']) +
		            	Number(totalIncome[i]['i01']) +
		            	Number(totalIncome[i]['r10']) +
		            	Number(totalIncome[i]['h14']) +
		            	Number(totalIncome[i]['h15']) +
		            	Number(totalIncome[i]['h16']) +
		            	Number(totalIncome[i]['r11']) +
		            	Number(totalIncome[i]['h06']) +
		            	Number(totalIncome[i]['h07']) +
		            	Number(totalIncome[i]['y22']) -
		            	Number(totalIncome[i]['비과세한도초과액'])
		            );
		            // 2018 분기
		            if (계약년도 == '2018') {
		            	totalIncome[i]['비과세합계'] = String(
		            		Number(totalIncome[i]['비과세합계']) +
		            		Number(totalIncome[i]['h17']) +
		            		Number(totalIncome[i]['u01'])
		            	);
		            }

		            sumdataCalc['비과세합계'] = String(
		            	Number(sumdataCalc['비과세합계']) +
		            	Number(totalIncome[i]['비과세합계'])
		            );

		            // 감면소득합계 계산
		            totalIncome[i]['감면소득합계'] = String(
		            	Number(totalIncome[i]['t01']) +
		            	Number(totalIncome[i]['t10']) +
		            	Number(totalIncome[i]['t11']) +
		            	Number(totalIncome[i]['t12']) +
		            	Number(totalIncome[i]['t20'])
		            );

		            // 2018 분기
		            if (계약년도 == '2018') {
		            	totalIncome[i]['감면소득합계'] = String(
		            		Number(totalIncome[i]['감면소득합계']) +
		            		Number(totalIncome[i]['t13'])
		              );
		            }
		            sumdataCalc['감면소득합계'] = String(
		            	Number(sumdataCalc['감면소득합계']) +
		            	Number(totalIncome[i]['감면소득합계'])
		            );

		            // 공제합계 계산
		            totalIncome[i]['공제합계'] = String(
		            	Number(totalIncome[i]['건강보험료']) +
		            	Number(totalIncome[i]['장기요양보험료']) +
		            	Number(totalIncome[i]['고용보험료']) +
		            	Number(totalIncome[i]['국민연금보험료']) +
		            	Number(totalIncome[i]['공무원연금']) +
		            	Number(totalIncome[i]['군인연금']) +
		            	Number(totalIncome[i]['사립학교교직원연금']) +
		            	Number(totalIncome[i]['별정우체국연금'])
		            );

		            // 외국인단일세율적용 대상자인 경우
		            // 총급여에서 연금저축을 제외하기 위한 보험별 합계계산
		            // this.sumdataCalc['국민연금보험료'] = String(
		            //   Number(this.sumdataCalc['국민연금보험료']) +
		            //   Number(this.totalIncome[i]['국민연금보험료'])
		            // );
		            // console.log('국민연금보험료 sumdataCalc >>> ', this.sumdataCalc['국민연금보험료']);
		            // console.log('국민연금보험료 totalIncome >>> ', this.totalIncome[i]['국민연금보험료']);
		            // this.sumdataCalc['공무원연금'] = String(
		            //   Number(this.sumdataCalc['공무원연금']) +
		            //   Number(this.totalIncome[i]['공무원연금'])
		            // );

		            // this.sumdataCalc['군인연금'] = String(
		            //   Number(this.sumdataCalc['군인연금']) +
		            //   Number(this.totalIncome[i]['군인연금'])
		            // );

		            // this.sumdataCalc['사립학교교직원연금'] = String(
		            //   Number(this.sumdataCalc['사립학교교직원연금']) +
		            //   Number(this.totalIncome[i]['사립학교교직원연금'])
		            // );

		            // this.sumdataCalc['별정우체국연금'] = String(
		            //   Number(this.sumdataCalc['별정우체국연금']) +
		            //   Number(this.totalIncome[i]['별정우체국연금'])
		            // );


		            sumdataCalc['공제합계'] = String(
		            	Number(sumdataCalc['공제합계']) +
		            	Number(totalIncome[i]['공제합계'])
		            );

		            // 비과세한도초과액 객체 정보
		            taxOverParams.push({
		            	계약ID: totalIncome[i]['계약ID'],
		            	사용자ID: totalIncome[i]['사용자ID'],
		            	원천명세ID: totalIncome[i]['원천명세ID'],
		            	근무시기: totalIncome[i]['근무시기'],
		            	비과세한도초과액: totalIncome[i]['비과세한도초과액'],
		            	dbMode: 'U'
		            });

		        }
		        
		        
		        //재집계 항목 저장
		        if(totalNum > 0){
		        
			        // 외국인단일세율적용 대상자인 경우
			        // 총급여 = 총급여 + 비과세합계 + 공제합계 - 국민연금보험료합계
			        if (외국인단일세율적용) {
			        	sumdataCalc['총급여'] = String(
			        		Number(sumdataCalc['총급여']) +
			        		Number(sumdataCalc['비과세합계']) +
			        		Number(sumdataCalc['공제합계']) -
			        		Number(sumdataCalc['국민연금보험료'])
			        	);
	
			        	// 총급여로 합산되었으므로 0으로 설정
			        	sumdataCalc['비과세합계'] = '0';
			        	sumdataCalc['공제합계'] = '0';
			        }

			        //재집계 합계 저장처리
			        doInsert(sumdataCalc, taxOverParams, 계약ID, 사용자ID, 근무시기);
			        
		        }
		        
			} else {
				$.unblockUI();
				logI('[사용자ID : '+사용자ID+'] 재집계 데이터 조회에 실패하였습니다.');
				totalIncomeProc = false;
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


//재집계 합계 저장처리
function doInsert(sumdataCalc, taxOverParams, 계약ID, 사용자ID, 근무시기) {
	
	var formData = new FormData();
    formData.append('계약ID', 계약ID);
    formData.append('사용자ID', 사용자ID);
    formData.append('근무시기', 근무시기);
    formData.append('총급여', sumdataCalc['총급여']);
    formData.append('비과세합계', sumdataCalc['비과세합계']);
    formData.append('감면소득합계', sumdataCalc['감면소득합계']);
    formData.append('공제합계', sumdataCalc['공제합계']);
    formData.append('소득세', sumdataCalc['소득세']);
    formData.append('지방소득세', sumdataCalc['지방소득세']);
    formData.append('농어촌특별세', sumdataCalc['농어촌특별세']);
	
	//logI("# formData : " + JSON.stringify(formData) );
    
    /*for (var value of formData.values()) {
    	logI("# formData : " + value ); 
    }*/
    
       
    var url = rootPath + 'febworker/insYP040.do';
    
    $.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		async:false,
		processData: false,
	    contentType: false,
		data: formData,
		success:function(res) {
			
			if (res['success']) {
				logI('[사용자ID : '+사용자ID+'] 재집계가 완료되었습니다.');
		        // 비과세한도초과액 저장
		        taxOverSum(taxOverParams, 사용자ID);
		        //jAlert('재집계가 완료되었습니다.','');
			} else {
				//jAlert('재집계가 실패하였습니다.','');
				$.unblockUI();
				logI('[사용자ID : '+사용자ID+'] 데이터 저장에 실패하였습니다.');				
				totalIncomeProc = false;
			}
		},
		error:function(request,status,error){
			$.unblockUI();
			
			if (request.status=="401") {
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
				//location.href=rootPath;
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		}
	});
}

//비과세한도 초과액 저장
function taxOverSum(taxOverParams, 사용자ID){
	
	var url = rootPath + 'febworker/saveTaxOverSumPayment.do';
	
	//logI("###### " + JSON.stringify(taxOverParams) );
	
	$.ajax({
		url:url,
	    crossDomain : true,
		dataType:"json",
		type:"POST",
		async:false,
		//processData: false,
	    //contentType: false,
		contentType: "application/json; charset=UTF-8",
		data: JSON.stringify(taxOverParams),
		success:function(res) {
			
			if (res['success']) {
				logI('[사용자ID : '+사용자ID+'] 비과세한도 초과액 저장 성공');
				totalIncomeProc = true;								
			} else {
				$.unblockUI();
				logI('[사용자ID : '+사용자ID+'] 비과세한도 초과액 저장 실패');
				totalIncomeProc = false;
			}

		},
		error:function(request,status,error){
			$.unblockUI();
			
			if (request.status=="401") {
				jAlert('사용자 권한이 존재하지 않습니다.\r\n다시 접속하여 주시기 바랍니다.' , 'System Message', goHome);
				//location.href=rootPath;
			} else {
				jAlert("입력하신 정보를 다시 확인해주시기 바랍니다."+error , 'Error Message');
		        //alert("code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);
			}
		}
	});
	
}


