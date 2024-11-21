package com.ezsign.contact.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;
import javax.transaction.Transactional;

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

import com.ezsign.contact.dao.ContactDAO;
import com.ezsign.contact.service.ContactService;
import com.ezsign.contact.vo.ContactCertVO;
import com.ezsign.contact.vo.ContactGrpVO;
import com.ezsign.contact.vo.ContactVO;
import com.ezsign.emp.dao.EmpDAO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.util.HttpUtil;
import com.ezsign.framework.util.SecurityUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.user.dao.UserDAO;
import com.ezsign.user.vo.UserCertVO;
import com.ezsign.user.vo.UserVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;


@Service("contactService")
public class ContactServiceImpl extends AbstractServiceImpl implements ContactService {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="contactDAO")
	private ContactDAO contactDAO;

	@Resource(name="empDAO")
	private EmpDAO empDAO;

	@Resource(name="userDAO")
	private UserDAO userDAO;

	// 주소록 중복 체크
	@Override
	public int checkContactDuplicate(ContactVO vo)  throws Exception {
		return contactDAO.checkContactDuplicate(vo);
	}

	// 주소록 조회
	@Override
	public List<ContactVO> getContactList(ContactVO vo)  throws Exception {
		return contactDAO.getContactList(vo);
	}

	// 주소록 등록 갯수 조회
	@Override
	public int getContactListCount(ContactVO vo)  throws Exception {
		return contactDAO.getContactListCount(vo);
	}


	// 주소록(인사/거래처) 등록
	@Override
	public void insContact(ContactVO vo) throws Exception {
		contactDAO.insContact(vo);
	}

	@Override
	public int updContact(ContactVO vo) throws Exception {
		return contactDAO.updContact(vo);
	}

	@Override
	public int delContact(ContactVO vo) throws Exception {
		vo.setStartPage(0);
		vo.setEndPage(1);
		List<ContactVO> resContactVO = contactDAO.getContactList(vo);
		
		int result = 0;
		
		if(resContactVO != null && !resContactVO.isEmpty()) {
			//주소록 정보 삭제
			result = contactDAO.delContact(vo);
			//계정정보가 존재할 경우 계정정보 삭제
			if (StringUtil.isNotNull(resContactVO.get(0).getLoginId())) {
				UserVO paramUserVO = new UserVO();
				paramUserVO.setUserId(resContactVO.get(0).getLoginId());
				userDAO.delUser(paramUserVO);
			}
		}
		return result;
	}
	
	@Override
	@Transactional
	public int addContactUser(ContactVO vo) throws Exception {
		int result 			= 0;
		ContactVO contactVO = new ContactVO();
		contactVO.setBizId(vo.getBizId());
		contactVO.setContId(vo.getContId());
		contactVO.setLoginId(vo.getLoginId());
		result		   = contactDAO.updContact(contactVO);
		UserVO userVO  = new UserVO();
        String userPwd = SecurityUtil.encrypt(vo.getLoginPwd());
        
        userVO.setBizId(vo.getBizId());
        userVO.setUserId(vo.getLoginId());
        userVO.setUserName(vo.getContName());
        userVO.setUserPwd(userPwd);
        userVO.setAppType("N");
        userVO.setUserType("1");
        userDAO.insUser(userVO);
        
        //만약 emp가 존재하지 않을 경우
        EmpVO empVO = new EmpVO();
        empVO.setBizId(vo.getBizId());
        empVO.setEmpName(vo.getContName());
        empVO.setEmpType("1");
        empVO.setLoginId(vo.getLoginId());
        empDAO.insEmpNo(empVO);
        
		return result;
	}
	
	@Override
	public List<ContactGrpVO> getContactGrpList(ContactGrpVO vo)  throws Exception {
		return contactDAO.getContactGrpList(vo);
	}
	
	@Override
	public void insContactGrp(ContactGrpVO vo) throws Exception {
		contactDAO.insContactGrp(vo);
	}

	@Override
	public int updContactGrp(ContactGrpVO vo) throws Exception {
		return contactDAO.updContactGrp(vo);
	}

	@Override
	public int delContactGrp(ContactGrpVO vo) throws Exception {
		return contactDAO.delContactGrp(vo);
	}

	// 인사 정보 연동
	@Override
	public void insContactUser(ContactVO vo) {
		// 회사 명칭이 존재하는지 확인
		ContactGrpVO grpVO = new ContactGrpVO();
		grpVO.setBizId(vo.getBizId());
		grpVO.setUserId(vo.getUserId());
		grpVO.setGrpId(vo.getBizId());
		List<ContactGrpVO> grpList = contactDAO.getContactGrpList(grpVO);

		if (grpList.isEmpty()) { grpVO.setGrpName("회사"); contactDAO.insContactGrpById(grpVO); }

		//인사정보 중 type1 조회
		EmpVO empVO = new EmpVO();
		empVO.setBizId(vo.getBizId());
		empVO.setStartPage(0);
		empVO.setEndPage(9999);
		empVO.setEmpType("1");
		List<EmpVO> empList = empDAO.getEmpList(empVO);
		
		for (EmpVO tmpVO : empList) {
			vo.setBizId(vo.getBizId());
	        vo.setCorpAddr("");
	        vo.setCorpDept("");
	        vo.setCorpFax("");
	        vo.setCorpName("");
	        vo.setCorpPos("");
	        vo.setCorpTel("");
	        vo.setCorpUrl("");
	        vo.setPhoneNumber("");
	        vo.setMailAddr("");
			vo.setContName(tmpVO.getEmpName());
			vo.setPhoneNumber(tmpVO.getPhoneNum());
			vo.setMailAddr(tmpVO.getEMail());
			vo.setContType("1");
			vo.setContactType("P");

			int cnt = contactDAO.getContactListCount(vo);
			logger.info("cnt : "+cnt);

			if (cnt == 0) {
				vo.setUserId(vo.getUserId());
				vo.setGrpId(vo.getBizId());
				contactDAO.insContact(vo);
			}
		}
	}

	// 거래처 정보 연동
	@Override
	public void customContactUser(ContactVO vo) {

		// 회사 명칭이 존재하는지 확인
		ContactGrpVO grpVO = new ContactGrpVO();
		grpVO.setBizId(vo.getBizId());
		grpVO.setUserId(vo.getUserId());
		grpVO.setGrpId(vo.getBizId());

		List<ContactGrpVO> grpList = contactDAO.getContactGrpList(grpVO);
		if (grpList.isEmpty()) { grpVO.setGrpName("회사"); contactDAO.insContactGrpById(grpVO); }
		//거래처 정보 중 type1 조회
		EmpVO empVO = new EmpVO();
		empVO.setBizId(vo.getBizId());
		empVO.setStartPage(0);
		empVO.setEndPage(9999);
		empVO.setEmpType("1");
		List<EmpVO> customerList = empDAO.getCustomerList(empVO);

		for (EmpVO tmpVO : customerList) {
			vo.setBizId(vo.getBizId());
			vo.setCorpName(tmpVO.getCustomerName());		// 거래처명
			vo.setCorpBizNum(tmpVO.getBusinessNo());		// 사업자 등록 번호
			vo.setCorpRepName(tmpVO.getOwnerName());		// 대표자명
			vo.setCorpAddr(tmpVO.getAddr1());				// 주소
			vo.setContName(tmpVO.getManagerName());			// 담당자명
			vo.setPhoneNumber(tmpVO.getManagerPhoneNum());	// 담당자 전화번호
			vo.setMailAddr(tmpVO.getManagerEmail());		// 담당자 이메일
			vo.setCorpDept(tmpVO.getManagerDeptName());		// 담당자 부서명
			vo.setContType("1");
			vo.setContactType("C");

			int cnt = contactDAO.getContactListCount(vo);
			logger.info("cnt : "+cnt);

			if (cnt == 0) {
				vo.setUserId(vo.getUserId());
				vo.setGrpId(vo.getBizId());
				contactDAO.insContact(vo);
			}
		}
	}

	@Override
	public void insContactGrpById(ContactGrpVO grpVO) {
		contactDAO.insContactGrpById(grpVO);
	}

	@Override
	public void delContactUser(ContactVO vo) {
		ContactVO contactVO = new ContactVO();
		contactVO.setContId(vo.getContId());
		
		//group id로 loginid 찾기
		List<ContactVO> resContactVO = contactDAO.getContactList(vo);
		logger.info("############"+resContactVO);
		//login id로 user정보 찾기
		if (!resContactVO.isEmpty()) {
			
			//계정정보가 존재할 경우 계정정보 삭제
			if (StringUtil.isNotNull(resContactVO.get(0).getLoginId())) {
				UserVO paramUserVO = new UserVO();
				paramUserVO.setUserId(resContactVO.get(0).getLoginId());
				userDAO.delUser(paramUserVO);
			}
			//emp정보가 없을 경우
			contactDAO.delContact(contactVO); 
		}
	}
	
	
	// 본인인증 url전송
	@Override
	public boolean sendCertURL(ContactCertVO vo) throws Exception {
		String userName  = vo.getRecvUserName();
		String userPhone = vo.getRecvUserPhone();
		String randomStr = "";
		
		//날짜 생성
	    Calendar today		 = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	    String day			 = sdf.format(today.getTime());
	    java.util.Random ran = new Random();

	    //랜덤 문자 길이
	    int numLength = 6;	   

	    for (int i = 0; i < numLength; i++) {
	        //0 ~ 9 랜덤 숫자 생성
	        randomStr += ran.nextInt(10);
	    }

		if (StringUtil.isNull(userPhone)) {
			logger.error(userName + "님의 휴대폰정보가 없습니다.");
			throw new Exception(userName + "님의 휴대폰정보가 없습니다.");
		}
		String id = userPhone+"-"+day+"-"+randomStr;
		logger.info("서비스임플id $$$$$$$$$$$$$$$:     "+id);
		id 		  = HttpUtil.base64Encode(id);
		String linkURL = vo.getDomainName() + "/contact/recvCertChk.do?id="+ id;
		vo.setId(id);
		String content = "[전자계약 * 본인인증]\n\n";
		content += "아래의 주소를 클릭하여 휴대폰 본인인증을 진행해주시기 바랍니다.\n";
		content += linkURL;

		String url = System.getProperty("system.mms.url");

		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost(url);
		// 전달하고자 하는 PARAMETER를 List객체에 담는다
		List<NameValuePair> nvps = new ArrayList<>();
		nvps.add(new BasicNameValuePair("bizId", "171129192253020"));
		nvps.add(new BasicNameValuePair("sendType", "003"));
		nvps.add(new BasicNameValuePair("subject", ""));
		nvps.add(new BasicNameValuePair("content", content));
		nvps.add(new BasicNameValuePair("recipientNum", userPhone));
		nvps.add(new BasicNameValuePair("encType", "0"));
		nvps.add(new BasicNameValuePair("senderKey", System.getProperty("biztalk.kko.sender_key")));
		nvps.add(new BasicNameValuePair("templateCode", "openplatform008"));
		nvps.add(new BasicNameValuePair("kkoBtnType", "1"));
		nvps.add(new BasicNameValuePair("kkoBtnInfo", "전자계약서^WL^" + linkURL + "^" + linkURL));
		// UTF-8은 한글
		httpPost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
		CloseableHttpResponse response = httpclient.execute(httpPost);
		try {
			logger.info("Send MMS ==>" + userPhone + "/" + response.getStatusLine().getStatusCode());
			// API서버로부터 받은 JSON 문자열 데이터
			logger.info(EntityUtils.toString(response.getEntity()));
			HttpEntity entity = response.getEntity();
			EntityUtils.consume(entity);

			logger.info("전자계약서비스 카카오톡 발송. PHONE NUM : " + userPhone + "/ response code : " + response.getStatusLine().getStatusCode());
			logger.info(content);
			if (response.getStatusLine().getStatusCode() == 200) logger.info("전자문서 발송 성공");
		} finally { response.close(); }
		return true;
	}
	
	/*성공여부 체크*/
	@Override
	public int checkCert(ContactCertVO vo) throws Exception {
		UserCertVO userCertVO = new UserCertVO();
		userCertVO.setReqNum(vo.getReqNum());
		return userDAO.getCntUserCert(userCertVO);
	}
	
}
