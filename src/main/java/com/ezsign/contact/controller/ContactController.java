package com.ezsign.contact.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ezsign.contact.service.ContactService;
import com.ezsign.contact.vo.ContactCertVO;
import com.ezsign.contact.vo.ContactGrpVO;
import com.ezsign.contact.vo.ContactVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.HttpUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.SessionVO;

@Controller
@RequestMapping("/contact")
public class ContactController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "contactService")
	private ContactService contactService;
	
	//그룹조회(페이지없음)
	@RequestMapping(method = {RequestMethod.POST} , value = "getContactGrpList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContactGrpListCtrl(@ModelAttribute("ContactGrpVO") ContactGrpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getContactGrpList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				vo.setUserId(loginVO.getUserId());
				List<ContactGrpVO> result = contactService.getContactGrpList(vo);
				jsonObject.put("data", result);
				total   = result.size();
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 그룹등록
	@RequestMapping(method = RequestMethod.POST , value = "insContactGrp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insContactGrpCtrl(@ModelAttribute("ContactGrpVO") ContactGrpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: insContactGrp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				contactService.insContactGrp(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 그룹명변경
	@RequestMapping(method = RequestMethod.POST , value = "updContactGrp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updContactGrpCtrl(@ModelAttribute("ContactGrpVO") ContactGrpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: updContactGrp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				int total = contactService.updContactGrp(vo);
				success   = true;
				jsonObject.put("total", total);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 그룹삭제
	@RequestMapping(method = RequestMethod.POST , value = "delContactGrp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delContactGrpCtrl(@ModelAttribute("ContactGrpVO") ContactGrpVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: delContactGrp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				contactService.delContactGrp(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 해당 주소록에 근로계약 체결 내역이 있는지 확인
	@RequestMapping(method = RequestMethod.POST , value = "delContactUser.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delContactUserCtrl(@ModelAttribute("ContactVO") ContactVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: delContactUser :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo != null) {
					logger.info("bizId=>"+loginVO.getBizId());
					String contId   = vo.getContId();
					String[] contIds = contId.split("-");
					for (String id : contIds) {
						vo.setContId(id);
						vo.setBizId(loginVO.getBizId());
						contactService.delContactUser(vo);
					}
				}
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	//주소록조회
	@RequestMapping(method = {RequestMethod.POST} , value = "getContactList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getContactListCtrl(@ModelAttribute("ContactVO") ContactVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		int total 		  = 0;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: getContactList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("권한있음.");
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());

				vo.setUserId(loginVO.getUserId());
				List<ContactVO> result = contactService.getContactList(vo);
				total   = contactService.getContactListCount(vo);
				jsonObject.put("data", result);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("total", total);
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 주소록등록
	@RequestMapping(method = RequestMethod.POST , value = "insContact.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insContactCtrl(@ModelAttribute("ContactVO") ContactVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: insContact :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				int checkDuplicate = contactService.checkContactDuplicate(vo);
				if (checkDuplicate == 0) {
					contactService.insContact(vo);
					success = true;
				} else {
					String message = "중복된 데이터가 " + checkDuplicate + "개 존재합니다.";
					jsonObject.put("message", message);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 주소록변경
	@RequestMapping(method = RequestMethod.POST , value = "updContact.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updContactCtrl(@ModelAttribute("ContactVO") ContactVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: updContact :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				int total = contactService.updContact(vo);
				success   = true;
				jsonObject.put("total", total);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 사용자계정변경
	@RequestMapping(method = RequestMethod.POST , value = "addContactUser.do")
	@ResponseBody
	public ResponseEntity<JSONObject> addContactUserCtrl(@ModelAttribute("ContactVO") ContactVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: addContactUser :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				int total = contactService.addContactUser(vo);
				success   = true;
				jsonObject.put("total", total);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 주소록삭제
	@RequestMapping(method = RequestMethod.POST , value = "delContact.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delContactCtrl(@ModelAttribute("ContactVO") ContactVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: delContact :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				String contId   = vo.getContId();
				String[] contIds = contId.split("-");
				for (String id : contIds) { vo.setContId(id); contactService.delContact(vo); }
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	// 인사정보 연동
	@RequestMapping(method = RequestMethod.POST , value = "insContactUser.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insContactUserCtrl(@ModelAttribute("ContactVO") ContactVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: insContactUser :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				contactService.insContactUser(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	//거래처 정보 연동
	@RequestMapping(method = RequestMethod.POST , value = "customContactUser.do")
	@ResponseBody
	public ResponseEntity<JSONObject> customContactUserCtrl(@ModelAttribute("ContactVO") ContactVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		try {
			logger.info(":::::::::::::::::::: customContactUser :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if (vo.getBizId() == null) vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				contactService.customContactUser(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 본인인증
	@RequestMapping(method = {RequestMethod.POST} , value = "sendCertURL.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendCertURLCtrl(@ModelAttribute("ContactCertVO") ContactCertVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		String reqNum 	  = "";
		try {
			logger.info(":::::::::::::::::::: sendCertURLCtrl :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String bizId = loginVO.getBizId();
				vo.setBizId(bizId);
				String serverName = request.getServerName();

				if (StringUtils.isNotEmpty(serverName)) { vo.setDomainName(request.getScheme() + "://" + request.getServerName()); }
				else 									{ vo.setDomainName(System.getProperty("system.open.url")); }

				success = contactService.sendCertURL(vo);
				reqNum  = vo.getId();
				vo.setReqNum(reqNum);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		jsonObject.put("reqNum", reqNum);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 수신자 본인인증
	@RequestMapping(method = {RequestMethod.GET} , value = "recvCertChk.do")
	public ModelAndView recvCertChkCtrl(ContactCertVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: recvCertChkCtrl :::::::::::::::::::");
		ModelAndView view = new ModelAndView();
		String id 		  = request.getParameter("id");
		String viewURL	  = "/contact/contact_sign_step_1";
		view.addObject("id",id);
		view.setViewName(viewURL);
		return view;
	}
	
	// 본인인증체크
	@RequestMapping(method = RequestMethod.POST , value = "getUserCertCount.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getUserCertCountCtrl(@ModelAttribute("ContactCertVO") ContactCertVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject 		= new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		HttpStatus status = HttpStatus.OK;
		boolean success   = false;
		int total		  = 0;
		try {
			logger.info(":::::::::::::::::::: getUserCertCountCtrl :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				String reqNum 	 = vo.getReqNum();
				String id		 = HttpUtil.base64Decode(reqNum);
				String[] array	 = id.split("-");
				String day		 = array[1];
				String randomStr = array[2];
				reqNum			 = day + randomStr;
				vo.setReqNum(reqNum);

				total   = contactService.checkCert(vo);
				success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		jsonObject.put("total", total);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 본인인증 완료 
	@RequestMapping(method = {RequestMethod.GET} , value = "completeCert.do")
	public ModelAndView completeCertCtrl(ContactCertVO vo, HttpServletRequest request) throws Exception {
		logger.info(":::::::::::::::::::: completeCertCtrl :::::::::::::::::::");
		ModelAndView view = new ModelAndView();
		String viewURL	  = "/contact/contact_completeCert";
		view.setViewName(viewURL);
		return view;
	}
}
