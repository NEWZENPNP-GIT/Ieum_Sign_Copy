package com.ezsign.framework.mail;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.activation.*;
import javax.mail.*;
import javax.mail.internet.*;

import org.apache.log4j.Logger;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;

import com.ezsign.framework.util.StringUtil;

public class MultiPartEmail{
	
	Logger logger = Logger.getLogger(this.getClass());
	
	private String mailId;
	private String mailPwd;
	
	/**
	 * 메일을 발송한다.
	 * 
	 * @param vo 메일 내용
	 * @return 발송 결과
	 */
	public boolean send(MailVO vo){
		int nCnt = 0;
		try{
			
			String mailHost = System.getProperty("mail.smtp-host");
			String mailPort = System.getProperty("mail.smtp-send-port");
			
			mailId = System.getProperty("mail.smtp-id");
			mailPwd = System.getProperty("mail.smtp-pwd");
			
			Properties props = System.getProperties();

			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.host", mailHost);
			props.put("mail.smtp.port", mailPort);
			props.put("mail.smtp.ssl.enable", "true");
			props.put("mail.smtp.auth", "true");
			props.put("mail.debug", "false");
			
			Authenticator auth = new getAuthenticator();
			
			Session session = Session.getInstance(props, auth);
			
			MimeMessage message = new MimeMessage(session);
			
			// FROM
			String[] fromArrayName = StringUtil.split(vo.getFrom(), " ");
			
			nCnt = fromArrayName.length;
			
			String fromAddr = fromArrayName[0];
			String fromName = "";
			if (nCnt > 1)
				fromName= fromArrayName[1];
			
			Address fromAddress = new InternetAddress(fromAddr, fromName);
			message.setFrom(fromAddress);
			
			
			// TO
			String[] toArray = StringUtil.split(vo.getTo(), ",");
			
			nCnt = toArray.length;
			
			Address toAddress[] = new Address[nCnt];
			for(int i=0;i<nCnt;i++) {
				String toName = toArray[i];
				String[] toArrayName = StringUtil.split(toName, " ");
				if (toArrayName.length > 1)
					toAddress[i] = new InternetAddress(toArrayName[0], toArrayName[1]);
				else
					toAddress[i] = new InternetAddress(toName);
			}
			message.setRecipients(Message.RecipientType.TO, toAddress);
			
			// CC
			if (StringUtil.nvl(vo.getCc(), "").length() > 0) {
				String[] ccArray = StringUtil.split(vo.getCc(), ",");
				
				nCnt = ccArray.length;
	
				if (nCnt > 0) {
					Address ccAddress[] = new Address[nCnt];
					for(int i=0;i<nCnt;i++) {
						String ccName = ccArray[i];
						String[] ccArrayName = StringUtil.split(ccName, " ");
						if (ccArrayName.length > 1)
							ccAddress[i] = new InternetAddress(ccArrayName[0], ccArrayName[1]);
						else
							ccAddress[i] = new InternetAddress(ccName);
					}
					message.setRecipients(Message.RecipientType.CC, ccAddress);
				}
			}

			// BCC
			if (StringUtil.nvl(vo.getBcc(), "").length() > 0) {
				String[] bccArray = StringUtil.split(vo.getBcc(), ",");
				
				nCnt = bccArray.length;
	
				if (nCnt > 0) {
					Address bccAddress[] = new Address[nCnt];
					for(int i=0;i<nCnt;i++) {
						String bccName = bccArray[i];
						String[] bccArrayName = StringUtil.split(bccName, " ");
						if (bccArrayName.length > 1)
							bccAddress[i] = new InternetAddress(bccArrayName[0], bccArrayName[1]);
						else
							bccAddress[i] = new InternetAddress(bccName);
					}
					message.setRecipients(Message.RecipientType.BCC, bccAddress);
				}
			}
			message.setSubject(vo.getSubject(), "UTF-8");

			Multipart multipart = new MimeMultipart();

			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(vo.getText(),  "text/html;charset=UTF-8");
			multipart.addBodyPart(messageBodyPart);
			
			for(int i=0;i<vo.getFileList().size();i++) {
				String filePath = vo.getFileList().get(i);

				BodyPart attachBodyPart = new MimeBodyPart();
				File f = new File(filePath);
				DataSource source = new FileDataSource(f.getPath());
				attachBodyPart.setDataHandler(new DataHandler(source));
				String fileName = MimeUtility.encodeText(f.getName(), "KSC5601", "B"); 
				attachBodyPart.setFileName(fileName);
				multipart.addBodyPart(attachBodyPart);
				
			}
			
			message.setContent(multipart);
			
			Transport.send(message);
			
	    }catch(MessagingException ex){
	    	logger.error("Sending Mail Exception : " +  ex.getCause() + " [failure when sending the message]", ex);
	    	return false;
	    }catch(Exception ex){
	    	logger.error("Sending Mail Exception : " +  ex.getCause(), ex);
	    	return false;
	    }
	    return true;
	}
	
	
	private class getAuthenticator extends Authenticator {
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(mailId, mailPwd);
		}
	}
	
	public boolean recv(MailVO vo) {
		try{
			
			String mailHost = System.getProperty("mail.smtp-host");
			String mailPort = System.getProperty("mail.smtp-recv-port");
			mailId = System.getProperty("mail.smtp-id");
			mailPwd = System.getProperty("mail.smtp-pwd");
			
			Properties properties = System.getProperties();

			Session session = Session.getDefaultInstance(properties);

			Store store = session.getStore("pop3");
			
			store.connect(mailHost, mailId, mailPwd);
			
			Folder folder = store.getFolder("inbox");

			folder.open(Folder.READ_ONLY);

			Message[] messages = folder.getMessages();

			for (int i = 0; i < messages.length; i++) {

				System.out.println("------------ Message " + (i + 1) + " ------------");

				String recvFrom = InternetAddress.toString(messages[i].getFrom());

				if (recvFrom != null) {
					System.out.println("recvFrom: " + recvFrom);
				}
				String replyTo = InternetAddress.toString(messages[i].getReplyTo());
				if (replyTo != null) {
					System.out.println("Reply-to: " + replyTo);
				}
				String recvTo = InternetAddress.toString(messages[i].getRecipients(Message.RecipientType.TO));
				if (recvTo != null) {
					System.out.println("To: " + recvTo);
				}
				String cc = InternetAddress.toString(messages[i].getRecipients(Message.RecipientType.CC));
				if (cc != null) {
					System.out.println("Cc: " + cc);
				}
				String bcc = InternetAddress.toString(messages[i].getRecipients(Message.RecipientType.BCC));
				if (bcc != null) {
					System.out.println("Bcc: " + bcc);
				}
				String subject = messages[i].getSubject();
				if (subject != null) {
					System.out.println("Subject: " + subject);
				}
				Date sent = messages[i].getSentDate();
				if (sent != null) {
					System.out.println("Sent: " + sent);
				}
				Date received = messages[i].getReceivedDate();
				if (received != null) {
					System.out.println("Received: " + received);
				}
				String fileName = messages[i].getFileName();
				if (fileName != null) {
					System.out.println("messages-fileName: " + fileName);
				}
				try {
					System.out.println("ContentType: " + messages[i].getContentType());
					if (messages[i].getContentType().indexOf("multipart") == 0) {
					
						Multipart multipart = (Multipart)messages[i].getContent();
						for(int f=0;f<multipart.getCount();f++) {
							BodyPart messageBodyPart = multipart.getBodyPart(f);
							System.out.println("messageBodyPart-contentType: " + messageBodyPart.getContentType());
							System.out.println("messageBodyPart-content: " + messageBodyPart.getContent());
							System.out.println("messageBodyPart-fileName: " + messageBodyPart.getFileName());
						}
					} else {
						String content = (String)messages[i].getContent();
						System.out.println("content: " + content);
						
					}
				} catch(Exception ex) {
					ex.printStackTrace();
				}
				System.out.println();
			}
	
			folder.close(true);
			store.close();
	    }catch(MessagingException ex){
	    	logger.error("Sending Mail Exception : " +  ex.getCause() + " [failure when sending the message]", ex);
	    	return false;
	    }catch(Exception ex){
	    	logger.error("Sending Mail Exception : " +  ex.getCause(), ex);
	    	return false;
	    }
		return true;
	}
	
}
