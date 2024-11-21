package com.ezsign.payment.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ezsign.bbs.service.BbsService;
import com.ezsign.bbs.vo.BbsContentsVO;
import com.ezsign.biz.service.BizService;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.emp.vo.EmpVO;
import com.ezsign.framework.util.*;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.point.service.PointService;
import com.ezsign.point.vo.PointVO;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezsign.common.controller.BaseController;
import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.payment.service.PaymentService;
import com.ezsign.payment.vo.PaymentVO;

import net.sf.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/payment/")
public class PaymentController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "paymentService")
	private PaymentService paymentService;

	@Resource(name = "pointService")
	private PointService pointService;

	@Resource(name = "bbsService")
	private BbsService bbsService;

	@Resource(name = "bizService")
	private BizService bizService;

	@RequestMapping(method = RequestMethod.POST , value = "goPaymentResult.do")
	public String goPaymentResultCtrl(@ModelAttribute("PaymentVO") PaymentVO vo, HttpServletRequest request, HttpServletResponse response)
	{
		System.out.println(":::::::::::::::::::: goPaymentResult :::::::::::::::::::");
		// PG결제에 대한 처리부분
		String redirectUri = "payment/pay_fail";
		
		if(vo.getPStateCd().equals("0021")) {
			redirectUri = "payment/pay_complete";
		} else if(vo.getPStateCd().equals("0051")) {
			redirectUri = "payment/pay_waiting";
		}
		return redirectUri;
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "insPayment.do")
	public String insPaymentCtrl(@ModelAttribute("PaymentVO") PaymentVO vo, HttpServletRequest request, HttpServletResponse response)
	{
		// 포인트 결제 처리
		boolean result = false; 
		String redirectUri = "json/json";
		
		try {
			System.out.println(":::::::::::::::::::: insPayment :::::::::::::::::::");
			
			paymentService.insPayment(vo);
			
			result = true;
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
		}

		if(result) {
			request.setAttribute("json", "OK");
		} else {
			request.setAttribute("json", "FAIL");
		}
		return redirectUri;
	}

	@RequestMapping(method = RequestMethod.GET , value = "insPaymentRequest.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insPaymentRequestCtrl(@ModelAttribute("PaymentVO") PaymentVO vo, HttpServletRequest request) throws Exception
	{
		// 게약목록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String pOid = "";
		
		try {
			System.out.println(":::::::::::::::::::: insPaymentRequest :::::::::::::::::::");
			
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				
				pOid = loginVO.getBizId()+DateUtil.getTimeStamp(7);
				if(!StringUtil.isNotNull(vo.getBizId())) {
					vo.setBizId(loginVO.getBizId());
				}
				vo.setUserId(loginVO.getUserId());
				vo.setPOid(pOid);
				paymentService.insPaymentRequest(vo);
				
				success = true;
				jsonObject.put("data", pOid);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "insTaxPaymentRequest.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insTaxPaymentRequestCtrl(@ModelAttribute("PointVO") PointVO vo, HttpServletRequest request) throws Exception
	{
		// 게약목록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String pOid = "";

		try {
			System.out.println(":::::::::::::::::::: insTaxPaymentRequest :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				// 파일생성
				String szSavePath = MultipartFileUtil.getSystemTempPath();
				List<FileVO> uploadFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = uploadFileList.size();
				System.out.println("개시글 첨부파일 File Count : "+uploadFileList.size());
				System.out.println("bizId=>"+loginVO.getBizId());

				if(!StringUtil.isNotNull(vo.getBizId())) {
					vo.setBizId(loginVO.getBizId());
				}

				// Point 이력이 없는 경우(세무사랑 가입 등) 검색후 기업명으로 세팅 (검색이 안될시 세금계산서 기재 기업명으로 세팅)
				if(!StringUtil.isNotNull(vo.getBizName())) {
					System.out.println("포인트 정보 신규생성 : " + vo.getTaxBillBizName());
					BizVO searchBizVO = new BizVO();
					searchBizVO.setBizId(vo.getBizId());
					searchBizVO.setEndPage(1);
					searchBizVO.setStartPage(0);
					List<BizVO> resultBizVO = bizService.getBizList(searchBizVO);
					if(resultBizVO.size() > 0) {
						vo.setBizName(resultBizVO.get(0).getBizName());
					} else{
						vo.setBizName(vo.getTaxBillBizName());
					}
				}			
				
				vo.setUserId(loginVO.getUserId());
				vo.setComment("포인트 충전(세금계산서)");
				int fileCount = 0;
				int userType = Integer.parseInt(loginVO.getUserType());
				pointService.updPointNew(vo);

				BizVO bizVO = new BizVO();
				bizVO.setBizId(vo.getBizId());
				bizVO.setTaxBillBizName(vo.getTaxBillBizName());
				bizVO.setTaxBillName(vo.getTaxBillName());
				bizVO.setTaxBillTel(vo.getTaxBillTel());
				bizVO.setTaxBillEmail(vo.getTaxBillEmail());
				bizService.updBizTax(bizVO);

				BbsContentsVO bbsContentsVO = new BbsContentsVO();
				bbsContentsVO.setBizId(vo.getBizId());
				bbsContentsVO.setUserId(loginVO.getUserId());
				bbsContentsVO.setBbsId("181226150423015");
				bbsContentsVO.setContentsType("3");
				bbsContentsVO.setWorkCode("");
				bbsContentsVO.setHitCnt("0");
				bbsContentsVO.setStatusCode("0");  // 처리상태코드
				SimpleDateFormat format1 = new SimpleDateFormat( "yyyy.MM.dd");
				Date time = new Date();
				String sysdate = format1.format(time);
				String depositYn = "";
				String depositDate= "";
				if(vo.getDepositYn().equals("Y")){
					depositYn = "입금";
				}else{
					depositYn = "미입금";
					depositDate = vo.getDepositDate();
				}

				String content = "";
				content += "<p style=\"color: rgb(102, 102, 102); font-family: NanumGothic, sans-serif; background-color: rgb(255, 255, 255); line-height: 1.8;\"><span style=\"margin: 0px; padding: 0px; font-size: 11pt; font-family: 나눔고딕, NanumGothic; color: rgb(0, 0, 0);\">&nbsp;</span></p>";
				content += "<p style=\"color: rgb(102, 102, 102); font-family: NanumGothic, sans-serif; background-color: rgb(255, 255, 255); line-height: 1.8;\"><span style=\"margin: 0px; padding: 0px; font-size: 11pt; font-family: 나눔고딕, NanumGothic; color: rgb(0, 0, 0);\">※ 포인트 충전 및 세금계산서 발행 요청</span></p>";
				content += "<p style=\"color: rgb(102, 102, 102); font-family: NanumGothic, sans-serif; background-color: rgb(255, 255, 255); line-height: 1.8;\"><span style=\"margin: 0px; padding: 0px; font-size: 11pt; font-family: 나눔고딕, NanumGothic; color: rgb(0, 0, 0);\">&nbsp; 회사명 : "+vo.getTaxBillBizName()+"</span></p>";
				content += "<p style=\"color: rgb(102, 102, 102); font-family: NanumGothic, sans-serif; background-color: rgb(255, 255, 255); line-height: 1.8;\"><span style=\"margin: 0px; padding: 0px; font-size: 11pt; font-family: 나눔고딕, NanumGothic; color: rgb(0, 0, 0);\">&nbsp; 담당자명 : "+vo.getTaxBillName()+"</span></p>";
				content += "<p style=\"color: rgb(102, 102, 102); font-family: NanumGothic, sans-serif; background-color: rgb(255, 255, 255); line-height: 1.8;\"><span style=\"margin: 0px; padding: 0px; font-size: 11pt; font-family: 나눔고딕, NanumGothic; color: rgb(0, 0, 0);\">&nbsp; 연락처 : "+vo.getTaxBillTel()+"</span></p>";
				content += "<p style=\"color: rgb(102, 102, 102); font-family: NanumGothic, sans-serif; background-color: rgb(255, 255, 255); line-height: 1.8;\"><span style=\"margin: 0px; padding: 0px; font-size: 11pt; font-family: 나눔고딕, NanumGothic; color: rgb(0, 0, 0);\">&nbsp; 요청일자 : "+sysdate+"</span></p>";
				content += "<p style=\"color: rgb(102, 102, 102); font-family: NanumGothic, sans-serif; background-color: rgb(255, 255, 255); line-height: 1.8;\"><span style=\"margin: 0px; padding: 0px; font-size: 11pt; font-family: 나눔고딕, NanumGothic; color: rgb(0, 0, 0);\">&nbsp; 발행이메일 : "+vo.getTaxBillEmail()+"</span></p>";
				content += "<p style=\"color: rgb(102, 102, 102); font-family: NanumGothic, sans-serif; background-color: rgb(255, 255, 255); line-height: 1.8;\"><span style=\"margin: 0px; padding: 0px; font-size: 11pt; font-family: 나눔고딕, NanumGothic; color: rgb(0, 0, 0);\">&nbsp; 충전요청 포인트 : "+StringUtil.toNumFormat(vo.getChargePoint())+"P</span></p>";
				content += "<p style=\"color: rgb(102, 102, 102); font-family: NanumGothic, sans-serif; background-color: rgb(255, 255, 255); line-height: 1.8;\"><span style=\"margin: 0px; padding: 0px; font-size: 11pt; font-family: 나눔고딕, NanumGothic; color: rgb(0, 0, 0);\">&nbsp; 입금여부 : "+depositYn+"</span></p>";
				if(vo.getDepositYn().equals("N")){
					content += "<p style=\"color: rgb(102, 102, 102); font-family: NanumGothic, sans-serif; background-color: rgb(255, 255, 255); line-height: 1.8;\"><span style=\"margin: 0px; padding: 0px; font-size: 11pt; font-family: 나눔고딕, NanumGothic; color: rgb(0, 0, 0);\">&nbsp; 입금예정일 : "+depositDate+"</span></p>";
				}
				content += "<p style=\"color: rgb(102, 102, 102); font-family: NanumGothic, sans-serif; background-color: rgb(255, 255, 255); line-height: 1.8;\"><span style=\"margin: 0px; padding: 0px; font-size: 11pt; font-family: 나눔고딕, NanumGothic; color: rgb(0, 0, 0);\">&nbsp; 위와 같이 포인트 충전 및 세금계산서 발행을 요청하였습니다.</span></p>";

				bbsContentsVO.setContents(content);
				bbsContentsVO.setSubject("포인트 충전 및 세금계산서 발행 요청");

				// 전달받은 파일리스트
				for(int i=0;i<uploadFileList.size();i++) {
					FileVO fileVO = uploadFileList.get(i);

					//TEMP폴더 파일 위치
					String uploadPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();

					//복사할 폴더 파일위치
					String newFileId = StringUtil.getUUID();
					String saveMonthPath  = MultipartFileUtil.SEPERATOR+ com.jarvis.common.util.DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+ com.jarvis.common.util.DateUtil.getDate()+MultipartFileUtil.SEPERATOR;
					//복사할 파일 FULLPATH
					String targetName = newFileId+"."+fileVO.getFileExt();

					//String targetPath = MultipartFileUtil.getFilePath("BBS_ATTACHED_FILE")+saveMonthPath+targetName;
					FileUtil.createDirectory(MultipartFileUtil.getFilePath("BBS_ATTACHED_FILE") + saveMonthPath);

					String targetPath = "";
					//메인 공지일 경우 파일 위치 변경
					if("181226150423015".equals(BbsService.MAINNOTICE)){
						targetPath = szSavePath+targetName;
					} else {
						targetPath = MultipartFileUtil.getFilePath("BBS_ATTACHED_FILE")+saveMonthPath+targetName;
						FileUtil.createDirectory(MultipartFileUtil.getFilePath("BBS_ATTACHED_FILE") + saveMonthPath);
					}

					if(FileUtil.fileCopy(uploadPath, targetPath)) {
						// 파일위치 변경
						fileVO.setFileStreNm(targetName);
						fileVO.setFileStrePath(saveMonthPath);
						uploadFileList.set(i, fileVO);
						fileCount++;
					}
				}

				if(fileCount==total) {
					int result = bbsService.insBbsContents(bbsContentsVO, uploadFileList, userType);
				}

				paymentService.sendContractNewUploadEmail(vo);

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

}
