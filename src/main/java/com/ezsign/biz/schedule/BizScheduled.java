package com.ezsign.biz.schedule;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ezsign.approval.vo.ApprovalMasterVO;
import com.ezsign.biz.service.BizService;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.framework.mail.MailVO;
import com.ezsign.framework.mail.MultiPartEmail;

@Component
public class BizScheduled {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name = "bizService")
	private BizService bizService;
	
	/**
	 * 비대면 바우쳐대상 계약 기간 종료시 자동 업데이트
	 * @param 
	 * @return 
	 * @throws Exception
	 * 새벽 1시 실행
	 */
	@Scheduled(cron = "0 0 1 * * *")
	public void ContractLogScheduled() throws Exception{

		System.out.println("::::::::::::::::::: Biz OpenVoucher Status Change ::::::::::::::::::::");
		BizVO bizVO = new BizVO();
		List<BizVO> openVoucherList = bizService.getupdateOpenVoucher(bizVO);
		for(BizVO vo : openVoucherList){
			bizService.updateOpenVoucherPoint(vo);
		}
		bizService.updateOpenVoucherContractEnd();
		
		//계약만료 기업 메일전송
		System.out.println("::::::::::::::::::: Biz Send Mail ::::::::::::::::::::");
		//계약만료 30일전
		List<BizVO> d30List = bizService.getContractD30List(bizVO);
		
		for(BizVO vo : d30List){
			String content = "";
			
			content +="<!DOCTYPE html> ";
			content +="<html> ";
			content +="<head> ";
			content +="<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content +="<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
			content +="<title>전자문서</title> ";
			content +="</head> ";
			content +="<body> ";
			content +="	<div style='width: 100%; text-align: center;'>";
			content +="	<img src='https://ieumsign.co.kr/images/expireD-30.png' usemap='#image-map'> ";
			content +="	<map name='image-map'> ";
			content +="	<area target='_blank' alt='' href='https://forms.gle/5eqV4N7sfp9KcRUV6' coords='277,470,621,542' shape='rect'> ";
			content +="	<area target='_blank' alt='' href='http://www.candycash.co.kr' coords='237,2444,665,2507' shape='rect'> ";
			content +="	</map> ";
			content +="	</div>";
			content +="</body> ";
			content +="</html> ";
			
			MultiPartEmail email = new MultiPartEmail();
			MailVO mailVO = new MailVO();
			mailVO.setFrom("no_reply@newzenpnp.com");
			mailVO.setTo(vo.getPersonEMail());
			mailVO.setCc("");
			mailVO.setBcc("");
			mailVO.setSubject("📢 [ 종료임박 D-30 ] 뉴젠피앤피의 서비스가 곧 종료됩니다.");
			mailVO.setText(content);
			
			logger.info("IeumSign 전자문서 이메일을 발송. email : " + vo.getPersonEMail());
			
			email.send(mailVO);
		}
		
		//계약만료 후
		List<BizVO> endList = bizService.getContractEndList(bizVO);
		
		for(BizVO vo : endList){
			String content = "";
			
			content +="<!DOCTYPE html> ";
			content +="<html> ";
			content +="<head> ";
			content +="<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content +="<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
			content +="<title>전자문서</title> ";
			content +="</head> ";
			content +="<body> ";
			content +="	<div style='width: 100%; text-align: center;'>";
			content +="	<img src='https://ieumsign.co.kr/images/expireD-day.png' usemap='#image-map'> ";
			content +="	<map name='image-map'> ";
			content +="	<area target='_blank' alt='' href='https://forms.gle/9HTP4ze1XhjXPUb99' coords='52,503,435,578' shape='rect'> ";
			content +="	<area target='_blank' alt='' href='https://forms.gle/dBXRzqDPCRp9Jiku7' coords='459,501,843,578' shape='rect'> ";
			content +="	<area target='_blank' alt='' href='https://newzenpnp.co.kr' coords='237,644,663,701' shape='rect'> ";
			content +="	</map> ";
			content +="	</div> ";
			content +="</body> ";
			content +="</html> ";
			
			MultiPartEmail email = new MultiPartEmail();
			MailVO mailVO = new MailVO();
			mailVO.setFrom("no_reply@newzenpnp.com");
			mailVO.setTo(vo.getPersonEMail());
			mailVO.setCc("");
			mailVO.setBcc("");
			mailVO.setSubject("😔 [ 서비스 종료 ] 앗, 내 계약서! 뉴젠피앤피의 전자계약 서비스 사용이  종료되었습니다.");
			mailVO.setText(content);
			
			logger.info("IeumSign 전자문서 이메일을 발송. email : " + vo.getPersonEMail());
			
			email.send(mailVO);
		}
		
		//계약만료 30일후
		List<BizVO> expireList = bizService.getDownloadExpireList(bizVO);
		
		for(BizVO vo : expireList){
			String content = "";
			
			content +="<!DOCTYPE html> ";
			content +="<html> ";
			content +="<head> ";
			content +="<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content +="<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
			content +="<title>전자문서</title> ";
			content +="</head> ";
			content +="<body> ";
			content +="	<div style='width: 100%; text-align: center;'>";
			content +="	<img src='https://ieumsign.co.kr/images/downloadExpire.png' usemap='#image-map'> ";
			content +="	<map name='image-map'> ";
			content +="	<area target='_blank' alt='' href='https://newzenpnp.co.kr' coords='179,534,723,608' shape='rect'> ";
			content +="	</map> ";
			content +="	</div>";
			content +="</body> ";
			content +="</html> ";
			
			MultiPartEmail email = new MultiPartEmail();
			MailVO mailVO = new MailVO();
			mailVO.setFrom("no_reply@newzenpnp.com");
			mailVO.setTo(vo.getPersonEMail());
			mailVO.setCc("");
			mailVO.setBcc("");
			mailVO.setSubject("[ 뉴젠피앤피 ] 계약서 다운로드 기간이 만료되었습니다.");
			mailVO.setText(content);
			
			logger.info("IeumSign 전자문서 이메일을 발송. email : " + vo.getPersonEMail());
			
			email.send(mailVO);
		}
	}
}
