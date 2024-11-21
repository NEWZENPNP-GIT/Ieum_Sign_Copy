package com.ezsign.cscenter.service.impl;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.biz.dao.BizDAO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.cscenter.service.CSCenterService;
import com.ezsign.cscenter.vo.CSRequestBizVO;
import com.ezsign.framework.mail.MailVO;
import com.ezsign.framework.mail.MultiPartEmail;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("csCenterService")
public class CSCenterServiceImpl extends AbstractServiceImpl implements CSCenterService {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="bizDAO")
	private BizDAO bizDAO;
	
	@Override
	public int requestBiz(CSRequestBizVO vo) throws Exception {
		int result = 0;
		String toEmail = vo.getEmail();
		
		// 사업자등록번호 중복체크
		BizVO dupBizVO = new BizVO();
		dupBizVO.setBusinessNo(vo.getBusinessNo());
		int dupBizCount = bizDAO.getBizListCount(dupBizVO);
		// 존재시 리턴
		if(dupBizCount>0) return 100;
		
		String linkURL = "https://gift.newzenpnp.co.kr/";

		
		String content = "";
		
		if(vo.getFunnel().equals("100")){ // 일반
			content += "<!DOCTYPE html> ";
			content += "<html lang=\"kr\"> ";
			content += " <head> ";
			content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\">  ";
			content += "<title>서비스 신청하기</title> ";
			content += "</head> ";
			content += "<body> ";
			content += "<br> ";
			content += "    <div style=\"font-family:gyeonggi_Title_Light,arial,Apple SD Gothic Neo,Malgun Gothic,맑은 고딕,sans-serif;\"> ";
			content += "		<div style=\"width:670px; border:2px solid #277CAF; margin:0 auto;\"> 	 ";
			content += "			<div style=\"width: 100%;background: #277CAF;height: 100px;text-align: center;line-height: 7.5;\"> ";
			content += "				<img src=\""+linkURL+"images/newzenpnp_logo_email_01.png\" alt=\"\" style=\"width: 160px;\"> ";
			content += "			</div> ";
			content += "			<div style=\"margin-top: 50px; text-align: center;\">  ";
			content += "				<h1 style=\"color: #277CAF;font-weight: bold;\"> 전자계약 서비스 가입 안내 </h1>	 ";
			content += "				<h2 style=\"font-size:20px;\"> 안녕하세요. 뉴젠P&P입니다.</h2> ";
			content += "				<h2 style=\"font-size:20px;\"> 종이 없이 웹으로 작성해서 모바일로 받아보는 간단한 <br> 전자계약 사용을 위해 서비스 가입을 진행해주세요! <br> 업무시간을 단축시켜줄 놀라운 기능을 만나보세요:)  </h2> ";
			content += "				<div style=\"width: 255px; height: 43px;line-height:45px;margin-top:30px; margin-bottom: 32px; background-color: #277CAF; border-radius: 8px; display: inline-block; text-align: center; cursor: pointer;\"> 		 ";
			content += "					<a href=\""+linkURL+"menu/goMemberJoin.do?funnel="+vo.getFunnel()+"\" style=\"display:block; color: #fff; font-size: 18px; font-weight: bold; font-family: tahoma; text-decoration: none;\" rel=\"noopener noreferrer\" target=\"_blank\">가입하기  </a> ";
			content += "				</div> ";
			content += "			</div> ";
			content += "			<div style=\"padding: 37px 0 0 0;\"> 		 ";
			content += "				<hr style=\"width: 95%\"> ";
			content += "				<div style=\"padding: 12px 0 20px 0; line-height: 17px;font-size: 14px;text-align: center;\"> 	 ";
			content += "					<span style=\"color:#838383\">본 메일은 <b>(주)뉴젠피앤피</b>에서 제공하는 기업 전용 발신 메일입니다. <br> 문의사항은 <b>대표번호 02-3270-6285</b> 또는  ";
			content += "					<a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \" rel=\"noopener noreferrer\" target=\"_blank\"><b>service@newzenpnp.com</b></a> ";
			content += "					으로 문의바랍니다. </span>";
			content += "				</div>  ";
			content += "				<div style=\"text-align: center;padding:0px 20px 12px 20px;\"> ";
			content += "					<span style=\"font-size:16px;font-weight: bold;color:#9B9999;\"> 가장 안전하고 간편한 대한민국 No.1 전자계약 전문가 </span> ";
			content += "					<img alt=\"\" src=\""+linkURL+"images/newzenpnp_logo_email_02.png\" style=\"vertical-align: middle;\"> ";
			content += "				</div> ";
			content += "			</div> ";
			content += "		</div> ";
			content += "	</div> ";
			content += "<br> ";
			content += "</body> ";
			content += "</html> ";
		}else if(vo.getFunnel().equals("200")){ // 비대면
			content += "<!DOCTYPE html> ";
			content += "<html lang=\"kr\"> ";
			content += " <head> ";
			content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\">  ";
			content += "<title>서비스 신청하기</title> ";
			content += "</head> ";
			content += "<body> ";
			content += "<br> ";
			content += "    <div style=\"font-family:gyeonggi_Title_Light,arial,Apple SD Gothic Neo,Malgun Gothic,맑은 고딕,sans-serif;\"> ";
			content += "		<div style=\"width:670px; border:2px solid #277CAF; margin:0 auto;\"> 	 ";
			content += "			<div style=\"width: 100%;background: #277CAF;height: 100px;text-align: center;line-height: 7.5;\"> ";
			content += "				<img src=\""+linkURL+"images/newzenpnp_logo_email_01.png\" alt=\"\" style=\"width: 160px;\"> ";
			content += "			</div> ";
			content += "			<div style=\"margin-top: 50px; text-align: center;\">  ";
			content += "				<h1 style=\"color: #277CAF;font-weight: bold;\"> 전자계약 서비스 바우처 구매완료 </h1>	 ";
			content += "				<div style=\"width: 255px; height: 43px;line-height:45px;margin-top:30px; margin-bottom: 32px; background-color: #277CAF; border-radius: 8px; display: inline-block; text-align: center; cursor: pointer;\"> 		 ";
			content += "					<a href=\""+linkURL+"menu/goMemberJoin.do?funnel="+vo.getFunnel()+"&funnelYear="+vo.getFunnelYear()+"\" style=\"display:block; color: #fff; font-size: 18px; font-weight: bold; font-family: tahoma; text-decoration: none;\" rel=\"noopener noreferrer\" target=\"_blank\">가입하기  </a> ";
			content += "				</div> ";
			content += "				<h2 style=\"font-size:20px;\"> 전자계약 서비스 구매가 완료되었습니다. <br> 지금 바로 전자계약서비스를 사용해 업무 시간을 단축시키세요! </h2> ";
			content += "				<h2 style=\"font-size:20px;\"> <br> <span style=\"color: #FF0000; background-color: #FFFF00;\"><b><ins> ※ 비대면 바우처 사용 주의사항 : 매월 1회 이상 로그인 필수 </ins></b></span></h2>  ";
			content += "				<h2 style=\"font-size:15px;\"> 비대면 바우처 공급기업은 <b><ins>수요기업의  </b></ins><span style=\"color: #FF0000;\"><b><ins> 이용실적을 매월 보고</b></ins></span>할 의무가 있습니다. "; 
			content += "				<br> 이용기록이 확인되지 않는 경우, <ins>바우처가 환수 조치 될 수 있으니</ins>";
			content += "				<br><span style=\"color: #FF0000;\"><b><ins> 반드시 매월 1회 이상 로그인</ins></b></span> 하셔아 합니다. </h2>";			
			content += "			</div> ";
			content += "			<div style=\"padding: 37px 0 0 0;\"> 		 ";
			content += "				<hr style=\"width: 95%\"> ";
			content += "				<div style=\"padding: 12px 0 20px 0; line-height: 17px;font-size: 14px;text-align: center;\"> 	 ";
			content += "					<span style=\"color:#838383\">본 메일은 <b>(주)뉴젠피앤피</b>에서 제공하는 기업 전용 발신 메일입니다. <br> 문의사항은 <b>대표번호 02-3270-6285</b> 또는  ";
			content += "					<a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \" rel=\"noopener noreferrer\" target=\"_blank\"><b>service@newzenpnp.com</b></a> ";
			content += "					으로 문의바랍니다. </span>";
			content += "				</div>  ";
			content += "				<div style=\"text-align: center;padding:0px 20px 12px 20px;\"> ";
			content += "					<span style=\"font-size:16px;font-weight: bold;color:#9B9999;\"> 가장 안전하고 간편한 대한민국 No.1 전자계약 전문가 </span> ";
			content += "					<img alt=\"\" src=\""+linkURL+"images/newzenpnp_logo_email_02.png\" style=\"vertical-align: middle;\"> ";
			content += "				</div> ";
			content += "			</div> ";
			content += "		</div> ";
			content += "	</div> ";
			content += "<br> ";
			content += "</body> ";
			content += "</html> ";
		}else if(vo.getFunnel().equals("300")){ // 솔루션
			content += "<!DOCTYPE html> ";
			content += "<html lang=\"kr\"> ";
			content += " <head> ";
			content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\">  ";
			content += "<title>서비스 신청하기</title> ";
			content += "</head> ";
			content += "<body> ";
			content += "<br> ";
			content += "    <div style=\"font-family:gyeonggi_Title_Light,arial,Apple SD Gothic Neo,Malgun Gothic,맑은 고딕,sans-serif;\"> ";
			content += "		<div style=\"width:670px; border:2px solid #277CAF; margin:0 auto;\"> 	 ";
			content += "			<div style=\"width: 100%;background: #277CAF;height: 100px;text-align: center;line-height: 7.5;\"> ";
			content += "				<img src=\""+linkURL+"images/newzenpnp_logo_email_01.png\" alt=\"\" style=\"width: 160px;\"> ";
			content += "			</div> ";
			content += "			<div style=\"margin-top: 50px; text-align: center;\">  ";
			content += "				<h1 style=\"color: #277CAF;font-weight: bold;\"> 안녕하세요. 뉴젠P&P입니다.<br>전자계약서비스를 소개합니다.</h1><br>";
			content += "				<h2 style=\"font-size:20px;\">모바일 급여명세서를 사용하신다면<br>안전하고 간편한 전자계약서비스를 사용해보시기 바랍니다.</h2><br>";
			content += "				<h2 style=\"font-size:20px;\">업무시간 단축은 물론, 종이를 보관하는 공간까지 절약!<br>뉴젠피앤피의 전자계약의 멋진 기능을 만나보세요!</h2> ";
			content += "				<div style=\"width: 255px; height: 43px;line-height:45px;margin-top:30px; margin-bottom: 32px; background-color: #277CAF; border-radius: 8px; display: inline-block; text-align: center; cursor: pointer;\"> 		 ";
			content += "					<a href=\""+linkURL+"menu/goMemberJoin.do?funnel="+vo.getFunnel()+"\" style=\"display:block; color: #fff; font-size: 18px; font-weight: bold; font-family: tahoma; text-decoration: none;\" rel=\"noopener noreferrer\" target=\"_blank\">서비스 둘러보기</a> ";
			content += "				</div> ";
			content += "			</div> ";
			content += "			<div style=\"padding: 37px 0 0 0;\"> 		 ";
			content += "				<hr style=\"width: 95%\"> ";
			content += "				<div style=\"padding: 12px 0 20px 0; line-height: 17px;font-size: 14px;text-align: center;\"> 	 ";
			content += "					<span style=\"color:#838383\">본 메일은 <b>(주)뉴젠피앤피</b>에서 제공하는 기업 전용 발신 메일입니다. <br> 문의사항은 <b>대표번호 02-3270-6285</b> 또는  ";
			content += "					<a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \" rel=\"noopener noreferrer\" target=\"_blank\"><b>service@newzenpnp.com</b></a> ";
			content += "					으로 문의바랍니다. </span>";
			content += "				</div>  ";
			content += "				<div style=\"text-align: center;padding:0px 20px 12px 20px;\"> ";
			content += "					<span style=\"font-size:16px;font-weight: bold;color:#9B9999;\"> 가장 안전하고 간편한 대한민국 No.1 전자계약 전문가 </span> ";
			content += "					<img alt=\"\" src=\""+linkURL+"images/newzenpnp_logo_email_02.png\" style=\"vertical-align: middle;\"> ";
			content += "				</div> ";
			content += "			</div> ";
			content += "		</div> ";
			content += "	</div> ";
			content += "<br> ";
			content += "</body> ";
			content += "</html> ";
		}else if(vo.getFunnel().equals("500")){ // 데모
			content += "<!DOCTYPE html> ";
			content += "<html lang=\"kr\"> ";
			content += " <head> ";
			content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\">  ";
			content += "<title>서비스 신청하기</title> ";
			content += "</head> ";
			content += "<body> ";
			content += "<br> ";
			content += "    <div style=\"font-family:gyeonggi_Title_Light,arial,Apple SD Gothic Neo,Malgun Gothic,맑은 고딕,sans-serif;\"> ";
			content += "		<div style=\"width:670px; border:2px solid #277CAF; margin:0 auto;\"> 	 ";
			content += "			<div style=\"width: 100%;background: #277CAF;height: 100px;text-align: center;line-height: 7.5;\"> ";
			content += "				<img src=\""+linkURL+"images/newzenpnp_logo_email_01.png\" alt=\"\" style=\"width: 160px;\"> ";
			content += "			</div> ";
			content += "			<div style=\"margin-top: 50px; text-align: center;\">  ";
			content += "				<h1 style=\"color: #277CAF;font-weight: bold;\"> 이음싸인 전자계약 데모버전 사용 안내 </h1>	 ";
			content += "				<h2 style=\"font-size:20px;\"> 안녕하세요. 뉴젠P&P입니다.</h2> ";
			content += "				<h2 style=\"font-size:20px;\"> 종이 없이 웹으로 작성해서 모바일로 받아보는 간단한 전자계약! <br> 데모버전 사용을 위해 서비스 가입을 진행해주세요. <br> 업무시간을 단축시켜줄 놀라운 기능을 만나보세요😀  </h2> ";
			content += "				<div style=\"width: 255px; height: 43px;line-height:45px;margin-top:30px; margin-bottom: 32px; background-color: #277CAF; border-radius: 8px; display: inline-block; text-align: center; cursor: pointer;\"> 		 ";
			content += "					<a href=\""+linkURL+"menu/goMemberJoin.do?funnel="+vo.getFunnel()+"\" style=\"display:block; color: #fff; font-size: 18px; font-weight: bold; font-family: tahoma; text-decoration: none;\" rel=\"noopener noreferrer\" target=\"_blank\">가입하기  </a> ";
			content += "				</div> ";
			content += "			</div> ";
			content += "			<div style=\"padding: 37px 0 0 0;\"> 		 ";
			content += "				<hr style=\"width: 95%\"> ";
			content += "				<div style=\"padding: 12px 0 20px 0; line-height: 17px;font-size: 14px;text-align: center;\"> 	 ";
			content += "					<span style=\"color:#838383\">본 메일은 <b>(주)뉴젠피앤피</b>에서 제공하는 기업 전용 발신 메일입니다. <br> 문의사항은 <b>대표번호 02-3270-6285</b> 또는  ";
			content += "					<a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \" rel=\"noopener noreferrer\" target=\"_blank\"><b>service@newzenpnp.com</b></a> ";
			content += "					으로 문의바랍니다. </span>";
			content += "				</div>  ";
			content += "				<div style=\"text-align: center;padding:0px 20px 12px 20px;\"> ";
			content += "					<span style=\"font-size:16px;font-weight: bold;color:#9B9999;\"> 가장 안전하고 간편한 대한민국 No.1 전자계약 전문가 </span> ";
			content += "					<img alt=\"\" src=\""+linkURL+"images/newzenpnp_logo_email_02.png\" style=\"vertical-align: middle;\"> ";
			content += "				</div> ";
			content += "			</div> ";
			content += "		</div> ";
			content += "	</div> ";
			content += "<br> ";
			content += "</body> ";
			content += "</html> ";
		}
		
		// 이메일 발송
		MailVO mailVO = new MailVO();
		mailVO.setFrom("no_reply@newzenpnp.com");
		mailVO.setTo(toEmail);
		mailVO.setCc("");
		mailVO.setBcc("");
		if(vo.getFunnel().equals("100")){ // 일반
			mailVO.setSubject("[뉴젠피앤피] 전자계약서비스 가입을 진행해주세요!");
		}else if(vo.getFunnel().equals("200")){ // 비대면
			mailVO.setSubject("[뉴젠피앤피] 전자계약 서비스 구매가 완료되었습니다!");
		}else{ // 솔루션
			mailVO.setSubject("[뉴젠피앤피] 전자계약서비스를 소개합니다!");
		}
		mailVO.setText(content);
		
		logger.info("전자문서 기업신청 이메일을 발송. email : " + toEmail);
		
		MultiPartEmail email = new MultiPartEmail();
		email.send(mailVO);
		
		// 담당자 전화번호로 알림톡 발송
		String contentBizTalk = "";
		if(vo.getFunnel().equals("200")){ // 비대면
			contentBizTalk += "[뉴젠피앤피 전자계약서비스 바우처 구매완료]\n\n";
			contentBizTalk += vo.getBizName()+"님! \n";
			contentBizTalk += "바우처에 등록된 이메일("+vo.getEmail()+")로\n전자계약 서비스 가입 안내서가 발송되었습니다.\n";
			contentBizTalk += "양식에 맞추어 회원가입을 진행해 주시기 바랍니다.\n";
		}else{ // 일반, 솔루션
			contentBizTalk += "[뉴젠피앤피 전자계약서비스 회원가입]\n\n";
			contentBizTalk += vo.getBizName()+"님! \n";
			contentBizTalk += "이메일("+vo.getEmail()+")로\n전자계약서비스 가입 안내서가 발송되었습니다.\n";
			contentBizTalk += "양식에 맞추어 회원가입을 진행해주시기 바랍니다.\n";
		}
		
		String url = "https://mms.ezsign.co.kr/biztalk/sendMMS.do";
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		// 전달하고자 하는 PARAMETER를 List객체에 담는다
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("bizId", "171129192253020"));
		nvps.add(new BasicNameValuePair("sendType", "003"));
		nvps.add(new BasicNameValuePair("subject", ""));
		nvps.add(new BasicNameValuePair("content", contentBizTalk));
		nvps.add(new BasicNameValuePair("recipientNum", vo.getPhoneNum()));
		nvps.add(new BasicNameValuePair("encType", "0"));
		nvps.add(new BasicNameValuePair("senderKey", System.getProperty("biztalk.kko.sender_key")));
		if(vo.getFunnel().equals("200")){ // 비대면
			nvps.add(new BasicNameValuePair("templateCode", "openplatform012"));
		}else{ // 일반, 솔루션
			nvps.add(new BasicNameValuePair("templateCode", "openplatform013"));
		}
		// UTF-8은 한글
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		CloseableHttpResponse response = httpclient.execute(httpPost);
		try {
			System.out.println("Send MMS ==>" + vo.getPhoneNum() + "/" + response.getStatusLine().getStatusCode());
			// API서버로부터 받은 JSON 문자열 데이터
			System.out.println(EntityUtils.toString(response.getEntity()));
			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);

			System.out.println("구매완료안내 카카오톡 발송. PHONE NUM : " + vo.getPhoneNum() + "/ response code : " + response.getStatusLine().getStatusCode());
			System.out.println(contentBizTalk);
			if (response.getStatusLine().getStatusCode() == 200) {
				System.out.println("구매완료안내 카카오톡 발송 성공");
			}
		} finally {
			response.close();
		}		
		
		return result;
	}

	
	@Override
	public int completedBiz(CSRequestBizVO vo) throws Exception {
	
		String toEmail = vo.getEmail();
		String url = "https://mms.ezsign.co.kr/biztalk/sendMMS.do";
		String linkURL = "https://ieumsign.co.kr/";
	
		
		// 담당자 처리완료 알림톡 발송
		String contentBizTalk = "[전자계약 서비스 오픈 안내]\n\n";
		contentBizTalk += vo.getBizName()+"님! \n";
		contentBizTalk += "요청하신 전자계약 서비스 오픈이 완료되었습니다.\n";
		contentBizTalk += "이제부터 자유롭게 서비스를 이용해 주시기 바랍니다.\n";
		contentBizTalk += "감사합니다.\n\n";
		contentBizTalk += linkURL;
		
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		// 전달하고자 하는 PARAMETER를 List객체에 담는다
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		nvps.add(new BasicNameValuePair("bizId", "171129192253020"));
		nvps.add(new BasicNameValuePair("sendType", "003"));
		nvps.add(new BasicNameValuePair("subject", ""));
		nvps.add(new BasicNameValuePair("content", contentBizTalk));
		nvps.add(new BasicNameValuePair("recipientNum", vo.getPhoneNum()));
		nvps.add(new BasicNameValuePair("encType", "0"));
		nvps.add(new BasicNameValuePair("senderKey", System.getProperty("biztalk.kko.sender_key")));
		nvps.add(new BasicNameValuePair("templateCode", "openplatform011"));
		// UTF-8은 한글
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		CloseableHttpResponse response = httpclient.execute(httpPost);
		try {
			System.out.println("Send MMS ==>" + vo.getPhoneNum() + "/" + response.getStatusLine().getStatusCode());
			// API서버로부터 받은 JSON 문자열 데이터
			System.out.println(EntityUtils.toString(response.getEntity()));
			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);

			System.out.println("서비스오픈안내 카카오톡 발송. PHONE NUM : " + vo.getPhoneNum()+ "/ response code : " + response.getStatusLine().getStatusCode());
			System.out.println(contentBizTalk);
			if (response.getStatusLine().getStatusCode() == 200) {
				System.out.println("서비스오픈안내 카카오톡 발송 성공");
			}
		} finally {
			response.close();
		}
		
		String content = "";
		if(vo.getFunnel().equals("100")){
			content += "<!DOCTYPE html> ";
			content += "<html lang=\"kr\"> ";
			content += " <head> ";
			content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
			content += "<title>서비스 오픈안내</title> ";
			content += "</head> ";
			content += "<body> ";
			content += "<br> ";
			
			content += "<p style=\"text-align: center;\"><span style=\"color: rgb(17, 17, 17); font-family: &quot;font-size:14.6667px;font-style:normal;font-variant-ligatures:normal;font-variant-caps:normal;font-weight:400;letter-spacing:normal;orphans:2;text-align:center;text-indent:0px;text-transform:none;white-space:normal;widows:2;word-spacing:0px;-webkit-text-stroke-width:0px;background-color:rgb(255, 255, 255);text-decoration-style:initial;text-decoration-color:initial;display:inline !important;float:none;&quot;; font-size: 14pt;\">😀 뉴젠피앤피 전자계약 첫 가입을 감사드립니다. </span><span style=\"color:rgb(17,17,17);font-size:14.6667px;font-style:normal;font-variant-ligatures:normal;font-variant-caps:normal;font-weight:400;letter-spacing:normal;orphans:2;text-align:start;text-indent:0px;text-transform:none;white-space:normal;widows:2;word-spacing:0px;-webkit-text-stroke-width:0px;background-color:rgb(255, 255, 255);text-decoration-style:initial;text-decoration-color:initial;font-family:&quot;text-decoration-thickness:initial;display:inline !important;float:none;\"><span style=\"font-size: 14pt;\">😀</span></span></p>";
			content += "<p style=\"text-align: center;\"><span style=\"color: rgb(17, 17, 17); font-style: normal; font-variant-ligatures: normal; font-variant-caps: normal; font-weight: 400; letter-spacing: normal; orphans: 2; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; widows: 2; word-spacing: 0px; -webkit-text-stroke-width: 0px; background-color: rgb(255, 255, 255); text-decoration-style: initial; text-decoration-color: initial; font-family: &quot;text-decoration-thickness:initial;display:inline !important;float:none;&quot;;\"><span style=\"font-size: 14pt;\">홈페이지 접속 : <u><a href=\"https://ieumsign.co.kr/\">ieumsign.co.kr</a></span></span></p>";
			content += "<br> ";
			content += "<p style=\"text-align: center;\"><span style=\"color:rgb(17,17,17);font-size:14.6667px;font-style:normal;font-variant-ligatures:normal;font-variant-caps:normal;font-weight:400;letter-spacing:normal;orphans:2;text-align:start;text-indent:0px;text-transform:none;white-space:normal;widows:2;word-spacing:0px;-webkit-text-stroke-width:0px;background-color:rgb(255, 255, 255);text-decoration-style:initial;text-decoration-color:initial;font-family:&quot;text-decoration-thickness:initial;display:inline !important;float:none;\"><img src=\""+linkURL+"images/complete_mail_normal.png\"></span><br></p>";
			content += "<br> ";
			content += "</body> ";
			content += "</html> ";
		}else{
			content += "<!DOCTYPE html> ";
			content += "<html lang=\"kr\"> ";
			content += " <head> ";
			content += "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" /> ";
			content += "<meta name=\"viewport\" content=\"width=device-width, user-scalable=no\"> ";
			content += "<title>서비스 오픈안내</title> ";
			content += "</head> ";
			content += "<body> ";
			content += "<br> ";
			content += "    <div style=\"font-family:arial,Apple SD Gothic Neo,Malgun Gothic,맑은 고딕,sans-serif;\"> ";
			content += "		<div style=\"width: 670px; border:2px solid #F2F2F2; margin:0 auto;\"> 	 ";
			content += "			<div style=\"width: 100%;text-align: right;height:30px; ;padding-top:20px;\"> ";
			content += "				<span style=\"font-weight: bold;color:#808080;\">가장 안전하고 간편한 대한민국 NO.1 전자계약 전문가</span> ";
			content += "				<img src=\""+linkURL+"images/newzenpnp_logo_email_02.png\" alt=\"\" style=\"vertical-align: middle;\"> ";
			content += "			</div> ";
			content += "			<div style=\"background-image: url("+linkURL+"images/tri_bg.png); height: 20px;width: 100%;margin-top:30px;\"></div> ";
			content += "			<div style=\"background: #F2F2F2;\"> ";
			content += "				<div style=\"padding-top: 50px; text-align: center;\">  ";
			content += "					<h1 style=\"color:#0071BC;margin:0 0 -30px 125px;font-size:40px;\">• •</h1> ";
			content += "					<h1 style=\"color: #F7931E;font-weight: bold;\">당사의 서비스 오픈이 완료되었습니다.</h1>	 ";
			content += "					<div style=\"width: 255px; height: 43px;line-height:45px;margin-top:20px; margin-bottom: 20px; background-color: #277CAF; border-radius: 8px; display: inline-block; text-align: center; cursor: pointer;\"> "; 		
			content += "							<a href=\""+linkURL+"\" style=\"display:block; color: #fff; font-size: 18px; font-weight: bold; font-family: tahoma; text-decoration: none;\" rel=\"noopener noreferrer\" target=\"_blank\">연결하기  </a> ";
			content += "					</div> ";
			content += "					<h2>서비스 사용을 위해 안내서를 참고하세요.</h2> ";
			content += "				</div> ";
			content += "			<div style=\"width: 100%;padding:30px 0 30px 0;text-align: center;\"> ";
			content += "					<a href=\""+linkURL+"data/open/뉴젠피앤피_전자계약서비스_통합메뉴얼_20210116.pdf\" target=\"_blank\"><img src=\""+linkURL+"/images/pdf_btn_white.png\" style=\"\"></a> ";
			content += "					<a href=\"https://www.youtube.com/watch?v=RBNdNPUuijM&feature=youtu.be\" target=\"_blank\"><img src=\""+linkURL+"images/youtube_btn_white.png\" style=\"padding-left:50px;\"></a> ";
			content += "			</div> ";
			content += "					<div style=\" padding: 37px 0 0 0;\"> 		 ";
			content += "						<div style=\"padding-left: 50px; font-size: 20px; margin-left: 4px;\"> 	 ";
			content += "							<span style=\"font-weight: bold; \">알림</span> ";
			content += "							<span style=\"letter-spacing: -0.5px; color:#6f6f6f;\">&nbsp;</span>  ";
			content += "						</div> 	 ";
			content += "						<hr style=\"width: 95%\"> ";
			content += "						<div style=\"padding-left:20px; font-size: 17px; margin-top: 20px; font-family: tahoma; word-break: keep-all; font-weight: 500; line-height: 30px; letter-spacing: -1px; color:#838383\"> ";
			content += "							<ul style=\"list-style: none;\"> ";
			content += "								<li><span style=\"color:#0071BC;font-size:24px;\">•</span> 자세한 문의사항은 아래 하단에 대표번호 또는 이메일로 문의주시기 바랍니다. </li> ";
			content += "								<li><span style=\"color:#0071BC;font-size:24px;\">•</span> 접수 된 원본 양식 중 개정 된 개인정보보호법을 따라 주민등록번호 수집을 <br>금하오니 생년월일로 대체하여 제공드립니다. </li> ";
			content += "							</ul> ";
			content += "						</div> ";
			content += "						<hr style=\"width: 95%\"> ";
			content += "					</div> ";
			content += "				</div> ";
			content += "				<div style=\"padding: 30px 0 20px 0; line-height: 17px;font-size: 14px;text-align: center;\"> 	 ";
			content += "						<span style=\"color:#838383\">본 메일은 <b>(주)뉴젠피앤피</b>에서 제공하는 전자계약서비스 기업 전용 발신 메일입니다. <br> 문의사항은 <b>대표번호 02-3270-6285</b> 또는  ";
			content += "						<a href=\"mailto:service@newzenpnp.com\" style=\"text-decoration: underline; color:#00a9e9; \" rel=\"noopener noreferrer\" target=\"_blank\"><b>service@newzenpnp.com</b></a>으로 문의바랍니다.</span> ";
			content += "				</div>  ";
			content += "		</div> ";
			content += "	</div> ";
			content += "<br> ";
			content += "</body> ";
			content += "</html> ";
		}
		
		// 이메일 발송
		MailVO mailVO = new MailVO();
		mailVO.setFrom("no_reply@newzenpnp.com");
		mailVO.setTo(toEmail);
		mailVO.setCc("");
		mailVO.setBcc("");
		mailVO.setSubject("[뉴젠피앤피] 전자계약서비스 오픈 안내");
		mailVO.setText(content);
		
		logger.info("전자문서 오픈안내 이메일을 발송. email : " + toEmail);
		
		MultiPartEmail email = new MultiPartEmail();
		email.send(mailVO);
		
		// 기업상태코드 업데이트 변경
		BizVO bizVO = new BizVO();
		bizVO.setBizId(vo.getBizId());
		bizVO.setBizStatus(vo.getBizStatus());
		
		// 업데이트 상태값 저장	
		int resultBizStatus = bizDAO.updBiz(bizVO);
		
		return resultBizStatus;
	}
	
	@Override
	public void insertBizRequst(CSRequestBizVO vo) throws Exception {
		int result = 0;
		
		// 기업상태코드 업데이트 변경
		BizVO bizVO = new BizVO();
		
		bizVO.setBizName(vo.getBizName());
		bizVO.setBusinessNo(vo.getBusinessNo());
		bizVO.setPersonEMail(vo.getEmail());
		bizVO.setPersonUserTelNum(vo.getPhoneNum());
		bizVO.setFunnel(vo.getFunnel());
		bizVO.setFunnelYear(vo.getFunnelYear());
		bizVO.setUserId(vo.getUserId());
		
		bizDAO.insertBizRequst(bizVO);
	}
	
}
