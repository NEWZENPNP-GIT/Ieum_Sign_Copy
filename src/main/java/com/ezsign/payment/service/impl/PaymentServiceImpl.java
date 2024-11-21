package com.ezsign.payment.service.impl;

import java.net.URLDecoder;
import java.security.MessageDigest;
import java.util.List;

import javax.annotation.Resource;

import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.mail.MailVO;
import com.ezsign.framework.mail.MultiPartEmail;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.contract.vo.ContractFormVO;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.payment.dao.PaymentDAO;
import com.ezsign.payment.service.PaymentService;
import com.ezsign.payment.vo.PaymentVO;
import com.ezsign.point.dao.PointDAO;
import com.ezsign.point.vo.PointLogVO;
import com.ezsign.point.vo.PointVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;


@Service("paymentService")
public class PaymentServiceImpl extends AbstractServiceImpl implements PaymentService {
	
	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="paymentDAO")
	private PaymentDAO paymentDAO;
	
	@Resource(name="pointDAO")
	private PointDAO pointDAO;

	@Resource(name="empDAO")
	private EmpDAO empDAO;
	
	@Override
	public void insPayment(PaymentVO vo) throws Exception {
		String PG_AUTH_KEY = System.getProperty("payment.auth.key");
		
		StringBuffer sbNoti = new StringBuffer();
		sbNoti.append(vo.getPStateCd());   // 거래상태 : 0021(성공), 0031(실패), 0051(입금대기중)
		sbNoti.append(vo.getPTrno());    // 거래번호
		sbNoti.append(vo.getPAuthDt());  // 승인시간
		sbNoti.append(vo.getPType());     // 거래종류 (CARD, BANK)
		sbNoti.append(vo.getPMid());      // 회원사아이디
		sbNoti.append(vo.getPOid());      // 주문번호
		sbNoti.append(vo.getPAmt());      // 거래금액
		sbNoti.append(PG_AUTH_KEY);
		
		System.out.println(sbNoti.toString());
		
		String sNoti = sbNoti.toString();		
		byte[] bNoti = sNoti.getBytes();
		
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] digest = md.digest(bNoti);
		
		StringBuffer strBuf = new StringBuffer();
		
		for (int i = 0; i < digest.length; i++) {
		    int c = digest[i] & 0xff;
		    if (c <= 15)
		        strBuf.append("0");
		    strBuf.append(Integer.toHexString(c));
		}
		
		String HashedData = strBuf.toString();
		
		PaymentVO result = paymentDAO.getPaymentRequest(vo);
		
		if(result!=null) {
			vo.setBizId(result.getBizId());
			vo.setUserId(result.getUserId());
		}
		
		paymentDAO.insPayment(vo);
		
		System.out.println("hashedData1 => "+HashedData);
		System.out.println("hashedData2 => "+vo.getPHash());
		
		if(StringUtil.isNotNull(vo.getBizId())&&HashedData.equals(vo.getPHash()) && vo.getPStateCd().equals("0021")) {
			int paymentAmt = Integer.parseInt(vo.getPAmt());
			int curPoint = -1 * (paymentAmt / 110);
			PointVO pointVO = new PointVO();
			pointVO.setBizId(vo.getBizId());
			pointVO.setCurPoint(curPoint);			
			PointVO resultPointVO = pointDAO.getPoint(pointVO);
			
			if(resultPointVO!=null) {
				pointDAO.balancePoint(pointVO);
			} else {
				curPoint =(paymentAmt / 110);
				pointVO.setCurPoint(curPoint);	
				pointVO.setUserId(vo.getUserId());
				pointDAO.insPoint(pointVO);
			}
			
			PointLogVO pointLogVO = new PointLogVO();
			pointLogVO.setBizId(vo.getBizId());
			pointLogVO.setUserId(vo.getUserId());
			pointLogVO.setPointType("1");
			pointLogVO.setPointPrice(curPoint);
			if(resultPointVO!=null) {
				pointLogVO.setPointResult(resultPointVO.getCurPoint() - curPoint);
			} else{
				pointLogVO.setPointResult(curPoint);
			}
			pointLogVO.setPaymentAmt(paymentAmt);
			pointLogVO.setEtcDesc("포인트 충전(계좌이체)");
			
			pointDAO.insPointLog(pointLogVO);
		}
		
	}

	@Override
	public void insPaymentRequest(PaymentVO vo) throws Exception {
		paymentDAO.insPaymentRequest(vo);
	}

	@Override
	public void sendContractNewUploadEmail(PointVO vo) throws Exception {
		EmpVO empVO = new EmpVO();
		empVO.setBizId("171220094226009"); //  운영 시스템관리자 bizID
		//empVO.setBizId("190806151212008"); // 개발 시스템관리자 bizID
		empVO.setEmpType("9");
		empVO.setStartPage(-1);
		empVO.setEndPage(-1);
		List<EmpVO> result = empDAO.getEmpList(empVO);

		for(int i=0;i<result.size();i++){
			EmpVO adminVO = result.get(i);

			String content = "";

			content +="<!DOCTYPE html> ";
			content +="<html> ";
			content +="<head> ";
			content +="<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content +="<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
			content +="<title>전자문서</title> ";
			content +="</head> ";
			content +="<body> ";
			content +="	<div style=\"width: 670px; border:1px solid #898989; margin:0 auto\" > ";
			content +="		<div style=\" padding: 37px 0 0 0;\"> ";
			content +="			<div style=\"padding-left: 50px; font-size: 25px; margin-left: 4px;\"> ";
			content +="				<span style=\"font-weight: bold; color:#00a9e9;\">IEUM</span><span class=\"logo_gray box\" style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;SIGN</span> ";
			content +="			</div> ";
			content +="			<div style=\"padding-left: 50px;  font-size: 44px; margin-top: 63px; line-height: 56px; word-break: keep-all; letter-spacing: -6.4px;\"> ";
			content +="				<span style=\"color:#00a9e9; letter-spacing: -6.1px;\">기업관리자가 세금계산서 발행을 요청</span><span class=\"font_black\" style=\"color:#202020;\">하였습니다.</span>";
			content +="			</div> ";
			content +="			<div style=\"padding-left: 50px; font-size: 17px; margin-top: 41px; font-family: 'tahoma'; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
			content +="				<span > 기업명 : "+vo.getBizName()+"<br> ";
			content +="			</div> ";
			content +="			<div style=\"margin-top: 74px; text-align: center;\"> ";
			content +="				<div style=\"width: 255px; height: 43px; padding-top: 15px; margin-bottom: 32px; background-color: #00a9e9; border-radius: 8px; display: inline-block; text-align: center; cursor: pointer;\"> ";
			content +="					<a href=\""+System.getProperty("system.feb.url")+"\" style=\"color: #fff; font-size: 18px; font-weight: bold; font-family: 'tahoma'; text-decoration: none;\">로그인하기  </a> ";
			content +="				</div> ";
			content +="			</div> ";
			content +="		</div> ";
			content +="		<div style=\"background-color: #2b2b2b; width: 100%; min-height: 60px; font-size: 13px; font-family: 'dotum'; \"> ";
			content +="			<div style=\"padding: 12px 0 0 60px; line-height: 17px;\"> ";
			content +="				<span style=\"color:#838383\">이 이메일은 ㈜뉴젠피앤피에서 제공하는 발신 전용 이메일입니다.<br> ";
			content +="				문의사항이 있으시면 </span><span style=\"color:#3da6ff\"><a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \">service@newzenpnp.com</a></span > ";
			content +="				<span style=\"color:#838383\">으로 문의주시면 확인후 답변드리겠습니다.</span> ";
			content +="			</div> ";
			content +="		</div> ";
			content +="	</div> ";
			content +="</body> ";
			content +="</html> ";

			MultiPartEmail email = new MultiPartEmail();
			MailVO mailVO = new MailVO();
			mailVO.setFrom("no_reply@newzenpnp.com");
			mailVO.setTo(adminVO.getEMail());
			mailVO.setCc("");
			mailVO.setBcc("");
			mailVO.setSubject("[뉴젠피앤피] "+vo.getBizName()+" 업체에서 세금계산서 발행을 요청하였습니다.");
			mailVO.setText(content);

			System.out.println("IeumSign 전자문서 슈퍼관리자 알림 이메일을 발송. email : " + adminVO.getEMail());

			email.send(mailVO);
		}
	}

}
