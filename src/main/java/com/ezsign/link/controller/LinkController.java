package com.ezsign.link.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import net.sf.json.JSONObject;

import com.ezsign.biz.service.BizService;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.ezsign.link.service.LinkService;
import com.ezsign.menu.vo.MenuVO;


@Controller
@RequestMapping("/")
public class LinkController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "bizService")
	private BizService bizService;

	@Resource(name = "linkService")
	private LinkService linkService;
	
	// 기업 이음싸인 가입여부
	@RequestMapping(method = RequestMethod.POST , value = "checkMemberJoin.do")
	@ResponseBody
	public ResponseEntity<JSONObject> checkMemberJoinCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request)
	{
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		
		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		
		try {
			System.out.println(":::::::::::::::::::: checkMemberJoin :::::::::::::::::::");
			System.out.println("사업자번호 : " + vo.getBusinessNo());
			vo.setStartPage(0);
			vo.setEndPage(1);
			List<BizVO> resultList = bizService.getBizList(vo);
			if(resultList!=null&&resultList.size()>0) {
				result = true;
			} 
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			//throw ex;
		}
		jsonObject.put("success", result);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 기업 연말정산 가입여부
	@RequestMapping(method = RequestMethod.POST , value = "checkService.do")
	@ResponseBody
	public ResponseEntity<JSONObject> checkServiceCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request)
	{
		
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		
		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		int total = 0;
		try {
			System.out.println(":::::::::::::::::::: checkService :::::::::::::::::::");
			System.out.println("사업자번호 : " + vo.getBusinessNo()+"/"+vo.getServiceType());
			total = linkService.checkService(vo);
			
			if(total > 0) {
				result = true;
			}
			
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			//throw ex;
		}
		jsonObject.put("success", result);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method=RequestMethod.GET, value="serviceJoin.do")
	public ModelAndView serviceJoinCtrl(@RequestParam("businessNo") String businessNo, @RequestParam("serviceType") String serviceType ) {
		String redirectUri = "service/service_join_yearTax_klep";
		ModelAndView view = new ModelAndView();
		System.out.println(":::::::::::::::::::: serviceJoin :::::::::::::::::::");
		
		try {
			if(serviceType.equals("003")) {
				// 연말정산
				redirectUri = "service/service_join_yearTax_kclep";
			}
			view.addObject("businessNo", businessNo);
			view.addObject("serviceType", serviceType);
			view.setViewName(redirectUri);
		} catch (Exception ex) {
			logService.error(ex.getMessage(), new Throwable(ex));
		}
		return view;
	}
	
	@RequestMapping(method = RequestMethod.POST , value = "sendRawDataXML.do")
	@ResponseBody
	public ResponseEntity<JSONObject> sendRawDataXMLCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) 
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		responseHeaders.add("Access-Control-Allow-Origin", "*");
		
		HttpStatus status = HttpStatus.OK;
		boolean result = false;
		
		System.out.println("[sendRawDataXML] 사업자번호 : " + vo.getBusinessNo() +"/"+ vo.getServiceType() + " 귀속년도 : " + vo.getFebYear());
		
		try {

			// 서비스구분에 따른 데이터 처리
			
			if(StringUtil.isNull(vo.getBusinessNo())) {
				System.out.println("[sendRawDataXML] 기업정보가 없습니다.");
				status = HttpStatus.BAD_REQUEST;
			} else {
				if(StringUtil.isNull(vo.getServiceType())) {
					System.out.println("[sendRawDataXML] 서비스타입이 없습니다.");
					status = HttpStatus.BAD_REQUEST;
				} else {
					if(vo.getServiceType().equals("003")) {
						if(StringUtil.isNull(vo.getFebYear())) {
							System.out.println("[sendRawDataXML] 귀속년도가 없습니다.");
							status = HttpStatus.BAD_REQUEST;
						}
					}
				}
			}
			
			if(status==HttpStatus.OK) {
				// 파일생성
				String szMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate();
				String szSavePath = MultipartFileUtil.getSystemPath()+"temp/xml"+szMonthPath;
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = resultFileList.size();
				System.out.println("[sendRawDataXML] XML File Count : "+resultFileList.size());
				
				if(total==1) {
					// 전달받은 파일리스트
					
					FileVO fileVO = resultFileList.get(0);
					// DATABASE 처리
					result = linkService.setLinkXML(vo, fileVO);
					
					jsonObject.put("message", vo.getMessage());
					
				} else {
					System.out.println("XML파일이 잘못 전달되었습니다.");
					status = HttpStatus.BAD_REQUEST;
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			//throw ex;
		}
		jsonObject.put("success", result);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 기업 서비스 추가
	@RequestMapping(method = RequestMethod.POST , value = "insBizServiceAdd.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insBizServiceAddCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;
		try {
			System.out.println(":::::::::::::::::::: insBiz :::::::::::::::::::");		
			System.out.println("사업자번호 : " + vo.getBusinessNo());
			System.out.println("서비스타입 : " + vo.getServiceType());
			
			total = bizService.insBizServiceAdd(vo);
			
			if (total > 0) {
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
