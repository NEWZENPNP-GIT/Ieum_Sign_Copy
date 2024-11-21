package com.ezsign.framework.mail;

import java.util.ArrayList;
import java.util.List;

public class MailVO {

	/** 메일보내는 사람 접속id*/
	private String id = "";

	/** 메일보내는 사람 접속pwd*/
	private String pwd = "";
	
	/** 메일보내는 사람 메일주소*/
	private String from = "";
	
	/** 메일보내는 사람 이름 */
	private String fromNm = "";
	
	/** 메일받는사람 메일주소 */
	private String to = "";
	
	/** 메일받는사람 이름 */
	private String toNm = "";
	
	/** 메일 참조 */
	private String cc = "";
	
	/** 메일 숨은 참조 */
	private String bcc = "";
	
	/** 메일 제목 */
	private String subject = "";
	
	/** 메일내용 */
	private String text = "";
	
	/** 첨부파일 1 */
	private List<String> fileList = new ArrayList();

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getFromNm() {
		return fromNm;
	}

	public void setFromNm(String fromNm) {
		this.fromNm = fromNm;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getToNm() {
		return toNm;
	}

	public void setToNm(String toNm) {
		this.toNm = toNm;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPwd() {
		return pwd;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public List<String> getFileList() {
		return fileList;
	}

	public void setFileList(List<String> fileList) {
		this.fileList = fileList;
	}
	
	public void addFile(String fileName) {
		this.fileList.add(fileName);
	}

	public String mailContents(String subject, int seqNo, int lineNo, String docNo, String sendDate, String sendName, String workType) {
		String contents = "";
		String serverURL = System.getProperty("system.web.url");
		
		contents="<html><head></head><body><div style='width:658px; height:auto; margin:0 auto;'> "+   
				"<div style='width:658px; height:200px; margin:0 auto; "+
				"           background:url("+serverURL+"/images/mail/bgimg_top.png) no-repeat;'></div> "+
				"<div style=' float:left;width:656px; height:auto; border-left:1px solid #d9d9d9; "+
				"             border-right:1px solid #d9d9d9; margin:0; padding:0;'> "+
				"	<ul style='width:656px; height:auto; font-size:12px; list-style:none; padding-top:10px; padding-bottom:10px;'> "+
				"    <li style='line-height:30px;'>메일이 도착하였습니다.</li> "+
				"    <li style='line-height:30px;'>문서번호 : "+docNo+"</li> "+
				"    <li style='line-height:30px;'>제 목 : "+subject+"</li> "+
				"    <li style='line-height:30px;'>기안일자 : "+sendDate+"</li> "+
				"    <li style='line-height:30px;'>기안자 : "+sendName+"</li> "+
				"    <li style='line-height:30px;'>메일내용을 확인하시려면 아래 확인하기 버튼을 눌러주세요.</li> "+
				"    </ul> "+
				"</div> "+
				"<div style='width:658px; height:112px; text-align:center; "+
				"             background:url("+serverURL+"/images/mail/bgimg_bottom.png) no-repeat; "+
				"            clear:both; padding-top:15px;'> "+
				"  <a href='"+serverURL+"/login.jsp?key="+seqNo+"&no="+lineNo+"&type="+workType+"' target='_blank'> "+
				"    <img src='"+serverURL+"/images/mail/button.png' /> "+
				"  </a></div> "+
				"</div></body></html> ";
		

		/*
		String szURL = System.getProperty("system.web.url");
		String szHtml = "";
		
		szHtml="<html><head></head><body><div style='width:658px; height:auto; margin:0 auto;'> "+   
				"<div style='width:658px; height:200px; margin:0 auto; "+
				"           background:url("+szURL+"/images/mail/bgimg_top.png) no-repeat;'></div> "+
				"<div style=' float:left;width:656px; height:auto; border-left:1px solid #d9d9d9; "+
				"             border-right:1px solid #d9d9d9; margin:0; padding:0;'> "+
				"	<ul style='width:656px; height:auto; font-size:12px; list-style:none; padding-top:10px; padding-bottom:10px;'> "+
				"    <li style='line-height:30px;'>메일이 도착하였습니다.</li> "+
				"    <li style='line-height:30px;'>내     용 : "+vo.getSubject()+"</li> "+
				"    <li style='line-height:30px;'>메일내용을 확인하시려면 아래 버튼을 눌러주세요.</li> "+
				"    </ul> "+
				"</div> "+
				"<div style='width:658px; height:112px; text-align:center; "+
				"             background:url("+szURL+"/images/mail/bgimg_bottom.png) no-repeat; "+
				"            clear:both; padding-top:15px;'> "+
				"  <a href='"+szURL+"/login.jsp?key="+vo.getIdxNo()+"&no="+(i+1)+"&type="+vo.getWorkType()+"' target='_blank'> "+
				"    <img src='"+szURL+"/images/mail/button.png' /> "+
				"  </a></div> "+
				"</div></body></html> ";	
				
		MultiPartEmail email = new MultiPartEmail();
		MailVO mailVO = new MailVO();
		mailVO.setFrom(vo.getSendEMail());
		mailVO.setTo(mailInfo[0]);
		mailVO.setCc("");
		mailVO.setBcc("");
		mailVO.setSubject(szSubject);
		mailVO.setText(email.mailContents(szSubject, vo.getIdxNo(), i+1, vo.getWorkType()));
		mailVO.setFileList(fileList);
		
		email.send(mailVO);
		*/
		
		return contents;
	}

	public String mailChgPwd(String password) {
		String contents = "";
		String serverURL = System.getProperty("system.web.url");
		
		contents="<html><head></head><body><div style='width:658px; height:auto; margin:0 auto;'> "+   
				"<div style='width:658px; height:200px; margin:0 auto; "+
				"           background:url("+serverURL+"/images/mail/bgimg_pwd_top.png) no-repeat;'></div> "+
				"<div style=' float:left;width:656px; height:auto; border-left:1px solid #d9d9d9; "+
				"             border-right:1px solid #d9d9d9; margin:0; padding:0;'> "+
				"	<ul style='width:656px; height:auto; font-size:12px; list-style:none; padding-top:10px; padding-bottom:10px;'> "+
				"    <li style='line-height:30px;'> </li> "+
				"    <li style='line-height:30px;'>임시비밀번호 : "+password+"</li> "+
				"    <li style='line-height:30px;'> </li> "+
				"    </ul> "+
				"</div> "+
				"<div style='width:658px; height:112px; text-align:center; "+
				"             background:url("+serverURL+"/images/mail/bgimg_bottom.png) no-repeat; "+
				"            clear:both; padding-top:15px;'> "+
				"</div> "+
				"</div></body></html> ";
		

		/*
		String szURL = System.getProperty("system.web.url");
		String szHtml = "";
		
		szHtml="<html><head></head><body><div style='width:658px; height:auto; margin:0 auto;'> "+   
				"<div style='width:658px; height:200px; margin:0 auto; "+
				"           background:url("+szURL+"/images/mail/bgimg_top.png) no-repeat;'></div> "+
				"<div style=' float:left;width:656px; height:auto; border-left:1px solid #d9d9d9; "+
				"             border-right:1px solid #d9d9d9; margin:0; padding:0;'> "+
				"	<ul style='width:656px; height:auto; font-size:12px; list-style:none; padding-top:10px; padding-bottom:10px;'> "+
				"    <li style='line-height:30px;'>메일이 도착하였습니다.</li> "+
				"    <li style='line-height:30px;'>내     용 : "+vo.getSubject()+"</li> "+
				"    <li style='line-height:30px;'>메일내용을 확인하시려면 아래 버튼을 눌러주세요.</li> "+
				"    </ul> "+
				"</div> "+
				"<div style='width:658px; height:112px; text-align:center; "+
				"             background:url("+szURL+"/images/mail/bgimg_bottom.png) no-repeat; "+
				"            clear:both; padding-top:15px;'> "+
				"  <a href='"+szURL+"/login.jsp?key="+vo.getIdxNo()+"&no="+(i+1)+"&type="+vo.getWorkType()+"' target='_blank'> "+
				"    <img src='"+szURL+"/images/mail/button.png' /> "+
				"  </a></div> "+
				"</div></body></html> ";	
				
		MultiPartEmail email = new MultiPartEmail();
		MailVO mailVO = new MailVO();
		mailVO.setFrom(vo.getSendEMail());
		mailVO.setTo(mailInfo[0]);
		mailVO.setCc("");
		mailVO.setBcc("");
		mailVO.setSubject(szSubject);
		mailVO.setText(email.mailContents(szSubject, vo.getIdxNo(), i+1, vo.getWorkType()));
		mailVO.setFileList(fileList);
		
		email.send(mailVO);
		*/
		
		return contents;
	}
	
}
