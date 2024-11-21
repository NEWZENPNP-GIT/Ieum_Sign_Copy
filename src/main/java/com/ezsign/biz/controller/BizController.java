package com.ezsign.biz.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

import com.ezsign.common.controller.BaseController;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.jarvis.common.util.DateUtil;
import com.jarvis.common.util.FileUtil;
import com.jarvis.common.util.StringUtil;
import com.ezsign.biz.service.BizService;
import com.ezsign.biz.vo.BizGrpVO;
import com.ezsign.biz.vo.BizVO;
import com.ezsign.code.service.CodeService;
import com.ezsign.code.vo.CodeVO;

@Controller
@RequestMapping("/biz/")
public class BizController extends BaseController {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name = "logService")
	private LogService logService;

	@Resource(name = "bizService")
	private BizService bizService;

	@Resource(name = "codeService")
	private CodeService codeService;

	@RequestMapping(method = RequestMethod.POST , value = "getCheckService.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getCheckServiceCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;

		try {
			logger.info(":::::::::::::::::::: getCheckService :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());

				total = bizService.getCheckService(vo);
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

	@RequestMapping(method = RequestMethod.GET , value = "findBiz.do")
	@ResponseBody
	public ResponseEntity<JSONObject> findBizCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total = 0;

		try {
			logger.info(":::::::::::::::::::: findBiz :::::::::::::::::::");
			List<BizVO> result= bizService.getBizList(vo);
			total = bizService.getBizListCount(vo);

			if(result != null && result.size() != 0){
				jsonObject.put("success", true);
				jsonObject.put("total", total);
				jsonObject.put("data", result);
			}else{
				jsonObject.put("success", false);
				jsonObject.put("total", 0);
				jsonObject.put("data", null);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getBizList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBizListCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total = 0;

		try {
			logger.info(":::::::::::::::::::: getBizList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				List<BizVO> result= bizService.getBizList(vo);
				total = bizService.getBizListCount(vo);

				if(result != null && result.size() != 0){
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				}else{
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
				// 21.05.25  기업코드 추가
				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("005");
				List<CodeVO> codeList = codeService.getCodeList(codeVO);
				jsonObject.put("bizStatus", codeList);
				// 21.11.01  유입경로 추가
				CodeVO codeVO1 = new CodeVO();
				codeVO1.setGrpCommCode("010");
				List<CodeVO> codeList1 = codeService.getCodeList(codeVO1);
				jsonObject.put("funnel", codeList1);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.GET , value = "getCsBizList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getCsBizListCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		int total = 0;

		try {
			logger.info(":::::::::::::::::::: getCSBizList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				List<BizVO> result= bizService.getCsBizList(vo);
				total = bizService.getCsBizListCount(vo);

				if(result != null && result.size() != 0){
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				}else{
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
				// 21.05.25  기업코드 추가
				CodeVO codeVO = new CodeVO();
				codeVO.setGrpCommCode("005");
				List<CodeVO> codeList = codeService.getCodeList(codeVO);
				jsonObject.put("bizStatus", codeList);
				// 21.11.01  유입경로 추가
				CodeVO codeVO1 = new CodeVO();
				codeVO1.setGrpCommCode("010");
				List<CodeVO> codeList1 = codeService.getCodeList(codeVO1);
				jsonObject.put("funnel", codeList1);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "insBiz.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insBizCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String bizID = "";

		try {
			logger.info(":::::::::::::::::::: insBiz :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				/*bizService.insAdminBiz(vo);

				String[] data = StringUtil.split(vo.getServiceId(), "|");
				for(int i=0;i<data.length;i++) {
					vo.setServiceId(data[i]);
					bizService.insBizService(vo);
				}

				success = true;
				*/
				vo.setBizId(loginVO.getBizId());

				bizID = bizService.insAdminBiz(vo);

				if(bizID.equals("-1")) {
					jsonObject.put("message", "이미 가입하신 기업입니다.");
				}

				if(bizID.length() > 0) success = true;

			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("bizId", bizID);
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "delBiz.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delBizCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: delBiz :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				bizService.delBiz(vo);

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


	@RequestMapping(method = RequestMethod.POST , value = "updBiz.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updBizCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: updBiz :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());

				String szMonthPath  = MultipartFileUtil.SEPERATOR+vo.getBizId();
				String szSavePath = MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = resultFileList.size();
				logger.info("기업 직인이미지 File Count : "+resultFileList.size());

				if(total==1) {
					// 전달받은 파일리스트
					for(int i=0;i<resultFileList.size();i++) {
						FileVO fileVO = resultFileList.get(i);
						String imgPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
						String targetPath = MultipartFileUtil.getSystemPath()+"images/sign"+MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR;
						String targetName = vo.getBizId()+"."+fileVO.getFileExt();
						logger.info(imgPath);
						logger.info(targetPath);
						FileUtil.createDirectory(targetPath);
						FileUtil.fileCopy(imgPath, targetPath+targetName);
						vo.setCompanyImage(DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+vo.getBizId()+"."+fileVO.getFileExt());
					}
				}

				int result = bizService.updBiz(vo);


				if(result>0) success = true;
				jsonObject.put("total", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "updBizPoint.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updBizPointCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: updBizPoint :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());

				int result = bizService.updBiz(vo);

				if(result>0) success = true;
				jsonObject.put("total", result);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}


	@RequestMapping(method = RequestMethod.POST , value = "insBizGrp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insBizGrpCtrl(@RequestBody BizGrpVO vo, HttpServletRequest request) throws Exception
	{
		// 기업관리
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: insBizGrp :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				logger.info("data=>"+vo.toString());


				vo.setBizId(loginVO.getBizId());

				String szMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getTimeStamp(14);
				String szSavePath = System.getProperty("system.stamp.path")+szMonthPath;
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = resultFileList.size();

				if(total>0) {
					// 전달받은 직인이미지 리스트
					for(int i=0;i<resultFileList.size();i++) {
						FileVO fileVO = resultFileList.get(i);
						String imgPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
						vo.setCompanyImage(imgPath);
					}
				}

				int result = bizService.insBizGrp(vo);
				if(result==-100) {
					jsonObject.put("message", "이미 가입하신 기업입니다.");
				}
				if(result>0) success = true;
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		jsonObject.put("success", success);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "addBizGrp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> addBizGrpCtrl(@ModelAttribute("BizGrpVO") BizGrpVO vo, HttpServletRequest request) throws Exception
	{
		// 기업관리
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: addBizGrp :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				vo.setBizId(loginVO.getBizId());

				int result = bizService.addBizGrp(vo);

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


	@RequestMapping(method = RequestMethod.POST , value = "addBizGrpList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> addBizGrpListCtrl(@ModelAttribute("BizGrpVO") BizGrpVO vo , HttpServletRequest request) throws Exception
	{
		// 기업관리
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: addBizGrpList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				List<BizGrpVO> list = new ArrayList<BizGrpVO>();

				String refList = vo.getRefId();
				if(refList.length() > 0) {
					String[] refIdData = StringUtil.split(refList, "-");

					for(String data : refIdData) {
						if(data != null && !data.equals("")) {
							BizGrpVO bizGrpVO = new BizGrpVO();
							bizGrpVO.setBizId(vo.getBizId());
							bizGrpVO.setGrpType("B");
							bizGrpVO.setRefId(data);
							list.add(bizGrpVO);
						}
					}
				}

				int result = bizService.addBizGrpList(list);

				jsonObject.put("total", result);
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

	@RequestMapping(method = RequestMethod.POST , value = "delBizGrpList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delBizGrpListCtrl(@ModelAttribute("BizGrpVO") BizGrpVO vo , HttpServletRequest request) throws Exception
	{
		// 기업관리
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: delBizGrpList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				List<BizGrpVO> list = new ArrayList<BizGrpVO>();

				String refList = vo.getRefId();
				if(refList.length() > 0) {
					String[] refIdData = StringUtil.split(refList, "-");

					for(String data : refIdData) {
						BizGrpVO bizGrpVO = new BizGrpVO();
						bizGrpVO.setBizId(vo.getBizId());
						bizGrpVO.setGrpType("B");
						bizGrpVO.setRefId(data);
						list.add(bizGrpVO);
					}
				}

				int result = bizService.delBizGrpList(list);

				jsonObject.put("total", result);
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

	@RequestMapping(method = RequestMethod.POST , value = "updBizGrp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updBizGrpCtrl(@RequestBody BizGrpVO vo, HttpServletRequest request) throws Exception
	{
		// 기업관리
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: updBizGrp :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				String szMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getTimeStamp(14);
				String szSavePath = System.getProperty("system.stamp.path")+szMonthPath;
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = resultFileList.size();

				if(total>0) {
					// 전달받은 직인이미지 리스트
					for(int i=0;i<resultFileList.size();i++) {
						FileVO fileVO = resultFileList.get(i);
						String imgPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
						vo.setCompanyImage(imgPath);
					}
				}
				vo.setStatusCode("RES");

				if(total>0) {
					vo.setCompanyImage(vo.getCompanyImage());
				}
				bizService.updBizGrp(vo);
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


	@RequestMapping(method = RequestMethod.POST , value = "delBizGrp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delBizGrpCtrl(@ModelAttribute("BizGrpVO") BizGrpVO vo, HttpServletRequest request) throws Exception
	{
		// 사용자 그룹권한관리 등록
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: delBizGrp :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());
				bizService.delBizGrp(vo);
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

	@RequestMapping(method = RequestMethod.GET , value = "getBizGrp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBizGrpCtrl(@ModelAttribute("BizGrpVO") BizGrpVO vo, HttpServletRequest request) throws Exception
	{
		// 사용자 그룹권한 리스트
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: getBizGrp :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+vo.getBizId());
				List<BizGrpVO> result= bizService.getBizGrp(vo);

				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
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

	@RequestMapping(method = RequestMethod.GET , value = "getBizGrpList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBizGrpListCtrl(@ModelAttribute("BizGrpVO") BizGrpVO vo, HttpServletRequest request) throws Exception
	{
		// 기업별 그룹권한 리스트
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: getBizGrpList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());

				List<BizGrpVO> result= bizService.getBizGrpList(vo);

				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
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

	@RequestMapping(method = RequestMethod.GET , value = "getBizGrpCombo.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBizGrpComboCtrl(@ModelAttribute("BizGrpVO") BizGrpVO vo, HttpServletRequest request) throws Exception
	{
		// 공통코드  리스트
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: getBizGrpCombo :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				vo.setBizId(loginVO.getBizId());
				List<BizGrpVO> result= bizService.getBizGrpCombo(vo);

				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
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

	@RequestMapping(method = RequestMethod.GET , value = "getBizGrpCombo2.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBizGrpCombo2Ctrl(@ModelAttribute("BizGrpVO") BizGrpVO vo, HttpServletRequest request) throws Exception
	{
		// 공통코드  리스트
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: getBizGrpCombo :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				if(vo.getBizId()==null){
					vo.setBizId(loginVO.getBizId());
				}
				List<BizGrpVO> result= bizService.getBizGrpCombo(vo);

				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
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

	// 기업 로고 변경
	@RequestMapping(method = RequestMethod.POST , value = "updBizLogo.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updBizLogoCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;

		try {
			logger.info(":::::::::::::::::::: updBizLogo :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("LoginBizId=>"+loginVO.getBizId());
				logger.info("selectBizId=>"+request.getParameter("bizId"));
				vo.setBizId(request.getParameter("bizId"));

				String szMonthPath  = MultipartFileUtil.SEPERATOR+vo.getBizId();
				String szSavePath = MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				total = resultFileList.size();
				logger.info("기업 로그이미지 File Count : "+resultFileList.size());

				if(total==1) {
					// 전달받은 파일리스트
					for(int i=0;i<resultFileList.size();i++) {
						FileVO fileVO = resultFileList.get(i);

						total = bizService.updBizLogo(vo, fileVO);
					}
				}

				success = true;
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


	// 기업 이미지 삭제
	@RequestMapping(method = RequestMethod.POST , value = "delBizLogo.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delBizLogoCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;

		try {
			logger.info(":::::::::::::::::::: delBizLogo :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());

				total = bizService.delBizLogo(vo);

				success = true;
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



	// 기업 직인이미지 변경
	@RequestMapping(method = RequestMethod.POST , value = "updBizStamp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updBizStampCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;

		try {
			logger.info(":::::::::::::::::::: updBizStamp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("LoginBizId=>"+loginVO.getBizId());
				logger.info("selectBizId=>"+request.getParameter("bizId"));
				vo.setBizId(request.getParameter("bizId"));

				String szMonthPath  = MultipartFileUtil.SEPERATOR+vo.getBizId();
				String szSavePath = MultipartFileUtil.getSystemTempPath();
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				total = resultFileList.size();
				logger.info("기업 직인이미지 File Count : "+resultFileList.size());

				if(total==1) {
					// 전달받은 파일리스트
					for(int i=0;i<resultFileList.size();i++) {
						FileVO fileVO = resultFileList.get(i);

						total = bizService.updBizStamp(vo, fileVO);
					}
				}

				success = true;
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

	// 기업 직인이미지 삭제
	@RequestMapping(method = RequestMethod.POST , value = "delBizStamp.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delBizStampCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		int total = 0;

		try {
			logger.info(":::::::::::::::::::: delBizStamp :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setBizId(loginVO.getBizId());

				total = bizService.delBizStamp(vo);

				success = true;
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

	// 기업 로고 조회
	@RequestMapping(method = RequestMethod.POST , value = "getBizLogo.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBizLogoCtrl(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: getBizLogo :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("LoginBizId=>"+loginVO.getBizId());
				logger.info("selectBizId=>"+request.getParameter("bizId"));
				vo.setBizId(request.getParameter("bizId"));
				vo.setStartPage(0);
				vo.setEndPage(10);
				List<BizVO> result  = bizService.getBizList(vo);

				if(result != null && result.size() != 0){
					BizVO bizVO = result.get(0);

					// 기업 로고 및 이미지
					success = true;
					jsonObject.put("total", result.size());
					jsonObject.put("companyLogo", bizVO.getCompanyLogo());
					jsonObject.put("companyimage", bizVO.getCompanyImage());
				}else{
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
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


	@RequestMapping(method = RequestMethod.POST , value = "updBizGrpSubAdmin.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updBizGrpSubAdminCtrl(@RequestBody BizGrpVO vo, HttpServletRequest request) throws Exception
	{
		// 기업관리
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: updBizGrpSubAdmin :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());

				String szMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getTimeStamp(14);
				String szSavePath = System.getProperty("system.stamp.path")+szMonthPath;
				List<FileVO> resultFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = resultFileList.size();

				if(total>0) {
					// 전달받은 직인이미지 리스트
					for(int i=0;i<resultFileList.size();i++) {
						FileVO fileVO = resultFileList.get(i);
						String imgPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
						vo.setCompanyImage(imgPath);
					}
				}
				vo.setStatusCode("RES");

				if(total>0) {
					vo.setCompanyImage(vo.getCompanyImage());
				}
				bizService.updBizGrpSubAdmin(vo);
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

	@RequestMapping(method = RequestMethod.GET , value = "getBizGrpSubAdminList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBizGrpSubAdminListCtrl(@ModelAttribute("BizGrpVO") BizGrpVO vo, HttpServletRequest request) throws Exception
	{
		// 기업별 그룹권한 리스트
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: getBizGrpSubAdminList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());

				List<BizGrpVO> result= bizService.getBizGrpSubAdminList(vo);

				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
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
			logger.info(":::::::::::::::::::: insBiz :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				logger.info("서비스타입 : " + vo.getServiceType());
				vo.setBizId(loginVO.getBizId());

				total = bizService.insBizServiceAdd(vo);

				if (total > 0) {
					success = true;
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


	@RequestMapping(method = RequestMethod.GET , value = "getBizGrpNameList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBizGrpNameListCtrl(@ModelAttribute("BizGrpVO") BizGrpVO vo, HttpServletRequest request) throws Exception
	{
		// 접속한 사용자의 기업권한
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;
		boolean success = false;

		try {
			logger.info(":::::::::::::::::::: getBizGrpNameList :::::::::::::::::::");

			SessionVO loginVO = SessionUtil.getSession(request);

			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				logger.info("bizId=>"+loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());

				List<BizGrpVO> result= bizService.getBizGrpNameList(vo);

				if(result==null || result.size()==0) {
					BizGrpVO bizGrpVO = new BizGrpVO();
					bizGrpVO.setBizId(loginVO.getBizId());
					bizGrpVO.setBizName(loginVO.getBizName());
					result.add(bizGrpVO);
				}

				jsonObject.put("total", result.size());
				jsonObject.put("data", result);
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

	@RequestMapping(method = RequestMethod.GET , value = "getBizEmail.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBizEmail(@ModelAttribute("BizVO") BizVO vo, HttpServletRequest request) throws Exception {
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");

		HttpStatus status = HttpStatus.OK;

		try {
			logger.info(":::::::::::::::::::: getBizEmail :::::::::::::::::::");
			logger.info("bizId=>"+vo.getBizId());

			List<BizVO> result = bizService.getBizEmail(vo);

			jsonObject.put("success", true);
			jsonObject.put("data", result);

		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));

			jsonObject.put("success", false);
			jsonObject.put("error", "서버에서 데이터를 가져오는 도중 오류가 발생했습니다.");

		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

}
