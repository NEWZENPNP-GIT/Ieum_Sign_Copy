/**
 * @author bkj 2016.06.13
 *
 */
package com.ezsign.contract.schedule;

import javax.annotation.Resource;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ezsign.contract.service.ContractService;

@Component
public class ContractScheduled {
    
	@Resource(name = "contractService")
	private ContractService contractService;
	
	/**
	 * 기업별 현재 체결 건수 로그 생성, temp 제거
	 * @param 
	 * @return 
	 * @throws Exception
	 * 새벽 5시 실행
	 */
	@Scheduled(cron = "0 0 5 * * *")
	public void ContractLogScheduled() throws Exception {

		System.out.println("::::::::::::::::::: ContractLogScheduled ::::::::::::::::::::");
		contractService.insContractLog();
		
		/*String filePath = MultipartFileUtil.getSystemPath()+"temp/";
		
		Calendar cal = Calendar.getInstance() ;
		long now = cal.getTimeInMillis() ; 

		FileUtil.deleteAllFilesBeforeDate(filePath, true, now);*/
	}

	// 매일 00시 미리보기로 생성된 TestPreView 삭제
	@Scheduled(cron = "0 0 4 * * *")
	public void ContractPreviewDelScheculed() throws  Exception {
		System.out.println("::::::::::::::::::: ContractPreviewDelScheculed ::::::::::::::::::::");
		contractService.delPreview();
	}
}


