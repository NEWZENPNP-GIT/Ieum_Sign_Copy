package com.ezsign.bbs.controller;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONObject;

import com.ezsign.bbs.service.BbsService;
import com.ezsign.bbs.vo.BbsCommentVO;
import com.ezsign.bbs.vo.BbsContentsVO;
import com.ezsign.bbs.vo.BbsFileVO;
import com.ezsign.common.controller.BaseController;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.contract.vo.ContractPersonVO;
import com.ezsign.framework.logger.LogService;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.HttpUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.SessionUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;
import com.jarvis.common.util.DateUtil;


@Controller
@RequestMapping("/bbs/")
public class BbsController extends BaseController {
	
	@Resource(name = "logService")
	private LogService logService;
	
	@Resource(name = "bbsService")
	private BbsService bbsService;
		
	// 게시글 전체 가져오기 (bbsId : 필수값)
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.GET} , value = "getBbsContentsList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBbsContentsListCtrl(@ModelAttribute("BbsContentsVO") BbsContentsVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getBbsContentsList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				System.out.println("bbsId=>"+vo.getBbsId());
				System.out.println("searchKey=>"+vo.getSearchKey());
				System.out.println("searchValue=>"+vo.getSearchValue());
				
				// 근로자는 본인 게시글만 조회
				int userType = Integer.parseInt(loginVO.getUserType());
				String userId = loginVO.getUserId();
				
				// FAQ 제외 (전체조회) 공지사항:180706135108002 (관리자간 문의제외 추가)
				if (!bbsService.FAQ.equals(vo.getBbsId())&&!bbsService.NOTICE.equals(vo.getBbsId())&&!bbsService.ADMIN.equals(vo.getBbsId())&&!bbsService.MAINNOTICE.equals(vo.getBbsId())) {
					vo.setBizId(loginVO.getBizId());
					// 근로자는 본인 게시글만 조회
					if(userType < 5) {
						vo.setUserId(userId);
					}
				}
				
				if(StringUtil.isNull(vo.getSortName())) {
					vo.setSortName("A.INS_DATE");
					vo.setSortOrder("DESC");
				}
				
				// 업무연락함 조회인 경우
				if(vo.getBbsId().equals("181226150423015")){
					// 시스템관리자인경우 모든 게시물 조회(기업 / 계정 제한 없음)
					if(loginVO.getUserType().equals("9")){
						vo.setUserId(null);
						vo.setBizId(null);
					} else {
						// 기업은 본인 기업에 해당하는 게시글은 모두 조회(계정 제한 없음)
						//vo.setUserId(null);
						//vo.setBizId(loginVO.getBizId());
						vo.setUserId(userId);
						vo.setBizId(null);
					}
				}
				
				List<BbsContentsVO> result= bbsService.getBbsContentsList(vo);
				
				total = bbsService.getBbsContentsListCount(vo);
				
				if(result != null){
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				}else{
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 메인 공지 가져오기
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.GET} , value = "getMainBbsContentsList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getMainBbsContentsListCtrl(@ModelAttribute("BbsContentsVO") BbsContentsVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getBbsContentsList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				
				List<BbsContentsVO> result= bbsService.getBbsContentsList(vo);
				List<BbsFileVO> resFileList = new ArrayList();
				
				for(BbsContentsVO tmpVO : result){
					BbsFileVO fileVO = new BbsFileVO();
					fileVO.setBizId(tmpVO.getBizId());
					fileVO.setBbsNo(tmpVO.getBbsNo());
					fileVO.setBbsId(tmpVO.getBbsId());
					List<BbsFileVO> resFileVO = bbsService.getBbsFileList(fileVO);
					for(BbsFileVO tmpVO1 : resFileVO){
						resFileList.add(tmpVO1);
					}
				}
				total = bbsService.getBbsContentsListCount(vo);
				
				jsonObject.put("success", true);
				jsonObject.put("total", total);
				jsonObject.put("fileList", resFileList);
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}
	
	// 게시글 본문정보 가져오기(첨부파일 포함)
	@RequestMapping(method = {RequestMethod.POST, RequestMethod.GET} , value = "getBbsContents.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBbsContentsCtrl(@ModelAttribute("BbsContentsVO") BbsContentsVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getBbsContents :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bbsId=>"+vo.getBbsId());
				System.out.println("bbsNo=>"+vo.getBbsNo());

				BbsContentsVO result= bbsService.getBbsContents(vo);
				if(result != null){

					jsonObject.put("success", true);
					jsonObject.put("data", result);
										
					// 게시판 파일정보 가져오기
					BbsFileVO bbsFileVO = new BbsFileVO();
					bbsFileVO.setBizId(result.getBizId());
					bbsFileVO.setBbsId(result.getBbsId());
					bbsFileVO.setBbsNo(result.getBbsNo());
					List<BbsFileVO> resultFileList = bbsService.getBbsFileList(bbsFileVO);					
					if(resultFileList.size()>0) {
						jsonObject.put("file", resultFileList);
					} else {
						jsonObject.put("file", null);
					}
					
					// 게시판 답변정보 가져오기
					BbsCommentVO bbsCommentVO  = new BbsCommentVO();
					bbsCommentVO.setBbsId(result.getBbsId());
					bbsCommentVO.setBbsNo(result.getBbsNo());
					bbsCommentVO.setStartPage(0);
					bbsCommentVO.setEndPage(9999);
					List<BbsCommentVO> resultCommentList = bbsService.getBbsCommentList(bbsCommentVO);
					if(resultCommentList.size() > 0) {
						jsonObject.put("comment", resultCommentList);
					} else {
						jsonObject.put("comment", null);
					}
				}else{
					jsonObject.put("success", false);
					jsonObject.put("data", null);
					jsonObject.put("file", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "insBbsContents.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insBbsContentsCtrl(@ModelAttribute("BbsContentsVO") BbsContentsVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		
		try {
			System.out.println(":::::::::::::::::::: insBbsContents :::::::::::::::::::");
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
				System.out.println("bbsId=>"+vo.getBbsId());
				
				// 기업정보 없을경우 로그인 사용자의 기업정보 설정
				if(StringUtil.isNull(vo.getBizId())) {
					vo.setBizId(loginVO.getBizId());
				}
				vo.setUserId(loginVO.getUserId());
				vo.setStatusCode("0");  // 처리상태코드
				int userType = Integer.parseInt(loginVO.getUserType());
				int fileCount = 0;
				
				// 전달받은 파일리스트
				for(int i=0;i<uploadFileList.size();i++) {
					FileVO fileVO = uploadFileList.get(i);

					//TEMP폴더 파일 위치
					String uploadPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
					
					//복사할 폴더 파일위치
					String newFileId = StringUtil.getUUID();
					String saveMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate()+MultipartFileUtil.SEPERATOR;
					//복사할 파일 FULLPATH
					String targetName = newFileId+"."+fileVO.getFileExt();
					
					//String targetPath = MultipartFileUtil.getFilePath("BBS_ATTACHED_FILE")+saveMonthPath+targetName;
					FileUtil.createDirectory(MultipartFileUtil.getFilePath("BBS_ATTACHED_FILE") + saveMonthPath);
					
					String targetPath = "";
					//메인 공지일 경우 파일 위치 변경
					if(vo.getBbsId().equals(BbsService.MAINNOTICE)){
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
					int result = bbsService.insBbsContents(vo, uploadFileList, userType);
					
					if(result == -100) {
						message = "게시글 등록 권한이 없습니다";
					}
					if(result > 0) {
						success = true;
						message = "게시글 등록 되었습니다.";
					}
				} else {
					message = "업로드한 파일 갯수와 처리한 파일갯수가 맞지 않습니다.";
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		jsonObject.put("message", message);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	@RequestMapping(method = RequestMethod.POST , value = "insBbsImage.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insBbsImageCtrl(@ModelAttribute("BbsContentsVO") BbsContentsVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		List<FileVO> uploadImageList = new ArrayList<FileVO>(); 
		
		try {
			System.out.println(":::::::::::::::::::: insBbsImage :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				// 파일생성
				String szSavePath = MultipartFileUtil.getSystemTempPath();
				List<FileVO> uploadFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = uploadFileList.size();
				System.out.println("이미지파일 File Count : "+uploadFileList.size());
				System.out.println("bizId=>"+loginVO.getBizId());
				
				// 전달받은 파일리스트
				for(int i=0;i<uploadFileList.size();i++) {
					FileVO fileVO = uploadFileList.get(i);

					//TEMP폴더 파일 위치
					String uploadPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
					
					//복사할 폴더 파일위치
					String newFileId = StringUtil.getUUID();
					String saveMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate()+MultipartFileUtil.SEPERATOR;
					//복사할 파일 FULLPATH
					String targetName = newFileId+"."+fileVO.getFileExt();
					
					// 첨부파일이 이미지 일때
					File imageFile = new File(targetName);
					String targetPath = MultipartFileUtil.getSystemPath()+"files"+MultipartFileUtil.SEPERATOR+"bbsImage"+saveMonthPath+targetName;
					FileUtil.createDirectory(MultipartFileUtil.getSystemPath() + "files"+MultipartFileUtil.SEPERATOR+"bbsImage"+saveMonthPath);
					if(FileUtil.fileCopy(uploadPath, targetPath)) {
						// 파일위치 변경
						FileVO imgeFileVO = new FileVO();
						imgeFileVO.setFileStreNm(targetName);
						imgeFileVO.setFileStrePath("files"+MultipartFileUtil.SEPERATOR+"bbsImage"+saveMonthPath);
						uploadImageList.add(imgeFileVO);
					}
				}

				success = true;
				jsonObject.put("data", uploadImageList);
				
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		jsonObject.put("message", message);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 게시글 변경(첨부파일 처리 ??)
	@RequestMapping(method = RequestMethod.POST , value = "updBbsContents.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updBbsContentsCtrl(@ModelAttribute("BbsContentsVO") BbsContentsVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		
		try {
			System.out.println(":::::::::::::::::::: updBbsContents :::::::::::::::::::");
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
				System.out.println("bbsId=>"+vo.getBbsId());
				System.out.println("bbsNo=>"+vo.getBbsNo());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				int userType = Integer.parseInt(loginVO.getUserType());
				int fileCount = 0;
				// 전달받은 파일리스트
				for(int i=0;i<uploadFileList.size();i++) {
					FileVO fileVO = uploadFileList.get(i);

					//TEMP폴더 파일 위치
					String uploadPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
					
					//복사할 폴더 파일위치
					String newFileId = StringUtil.getUUID();
					String saveMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate()+MultipartFileUtil.SEPERATOR;
					//복사할 파일 FULLPATH
					String targetName = newFileId+"."+fileVO.getFileExt();
					String targetPath = MultipartFileUtil.getFilePath("BBS_ATTACHED_FILE")+saveMonthPath+targetName;
					FileUtil.createDirectory(MultipartFileUtil.getFilePath("BBS_ATTACHED_FILE") + saveMonthPath);
					if(FileUtil.fileCopy(uploadPath, targetPath)) {
						// 파일위치 변경
						fileVO.setFileStreNm(targetName);
						fileVO.setFileStrePath(saveMonthPath);
						uploadFileList.set(i, fileVO);
						fileCount++;
					}
				}
				
				if(fileCount==total) {
					total = bbsService.updBbsContents(vo, uploadFileList, userType);
					
					if(total == -100) {
						jsonObject.put("message", "게시글 수정 권한이 없습니다.");
					}
					if(total > 0) {
						success = true;
						jsonObject.put("message", "게시글 수정 되었습니다.");
					}
					
				} else {
					message = "업로드한 파일 갯수와 처리한 파일갯수가 맞지 않습니다.";
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		jsonObject.put("message", message);
		
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 게시글 삭제하기(첨부파일, 댓글 삭제)
	@RequestMapping(method = RequestMethod.POST , value = "delBbsContents.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delBbsContentsCtrl(@ModelAttribute("BbsContentsVO") BbsContentsVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		
		try {
			System.out.println(":::::::::::::::::::: delBbsContents :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				System.out.println("bbsId=>"+vo.getBbsId());
				System.out.println("bbsNo=>"+vo.getBbsNo());
				vo.setBizId(loginVO.getBizId());
				int userType = Integer.parseInt(loginVO.getUserType());

				int result = 0;

				// 게시글 첨부파일 삭제(실제 파일처리)
				BbsFileVO bbsFileVO = new BbsFileVO();
				bbsFileVO.setBizId(vo.getBizId());
				bbsFileVO.setBbsId(vo.getBbsId());
				bbsFileVO.setBbsNo(vo.getBbsNo());
				bbsFileVO.setFileNo(vo.getFileNo());
				result = bbsService.delBbsFile(bbsFileVO);
				
				BbsCommentVO bbsCommentsVO = new BbsCommentVO();
				bbsCommentsVO.setBizId(vo.getBizId());
				bbsCommentsVO.setBbsId(vo.getBbsId());
				bbsCommentsVO.setBbsNo(vo.getBbsNo());
				result = bbsService.delBbsComment(bbsCommentsVO);
				
				// 본문 게시글 삭제처리
				result = bbsService.delBbsContents(vo, userType);

				if(result == -100) {
					message = "게시글 삭제 권한이 없습니다.";
				}
				if(result > 0) {
					success = true;
					message = "게시글 삭제 되었습니다.";
				} else {
					message = "삭제할 게시글이 없습니다.";
				}
				
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		jsonObject.put("message", message);
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 게시글 첨부파일 가져오기
	@RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}  , value = "getBbsFile.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBbsFileCtrl(@ModelAttribute("BbsFileVO") BbsFileVO vo, HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getBbsFile :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bbsId=>"+vo.getBbsId());
				System.out.println("bbsNo=>"+vo.getBbsNo());
				System.out.println("fileNo=>"+vo.getFileNo());
				vo.setStartPage(0);
				vo.setEndPage(1);
				
				List<BbsFileVO> resultList= bbsService.getBbsFileList(vo);
				if(resultList.size() > 0){
					BbsFileVO fileVO = resultList.get(0);

					String szSavePath = MultipartFileUtil.getFilePath("BBS_ATTACHED_FILE");
					String szFileName = szSavePath+fileVO.getFilePath()+fileVO.getFileName();
					FileUtil.createDirectory(szSavePath);
					File file = new File(szFileName);
					
					HttpUtil.setResponseFile(request, response, file, fileVO.getOrgFileName());
					
				} else {
					status = HttpStatus.BAD_REQUEST;
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		
        return new ResponseEntity(status);	
	}


	// 게시글 댓글 전체 가져오기 (bbsId, bbsNo : 필수값)
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST} , value = "getBbsCommentList.do")
	@ResponseBody
	public ResponseEntity<JSONObject> getBbsCommentListCtrl(@ModelAttribute("BbsCommentVO") BbsCommentVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		int total = 0;
		
		try {
			System.out.println(":::::::::::::::::::: getBbsCommentList :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bbsId=>"+vo.getBbsId());
				System.out.println("bbsNo=>"+vo.getBbsNo());
				System.out.println("searchKey=>"+vo.getSearchKey());
				System.out.println("searchValue=>"+vo.getSearchValue());

				if(StringUtil.isNull(vo.getSortName())) {
					vo.setSortName("A.INS_DATE");
					vo.setSortOrder("DESC");
				}
				
				List<BbsCommentVO> result= bbsService.getBbsCommentList(vo);
				
				total = bbsService.getBbsCommentListCount(vo);
				
				if(result != null && result.size() != 0){
					jsonObject.put("success", true);
					jsonObject.put("total", total);
					jsonObject.put("data", result);
				}else{
					jsonObject.put("success", false);
					jsonObject.put("total", 0);
					jsonObject.put("data", null);
				}
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	// 댓글 등록
	@RequestMapping(method = RequestMethod.POST , value = "insBbsComment.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insBbsCommentCtrl(@ModelAttribute("BbsCommentVO") BbsCommentVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: insBbsComment :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				System.out.println("bbsId=>"+vo.getBbsId());
				System.out.println("bbsNo=>"+vo.getBbsNo());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				
				int userType = Integer.parseInt(loginVO.getUserType());
				bbsService.insBbsComment(vo, userType);
				
				//노무 SOS 게시글의 답변일 경우 기업담당자 이메일로 알림 발송.
				if(vo.getBbsId().equals("190930155612016")){
					bbsService.sendCommentEmail(vo);
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
	
	// 댓글 수정
	@RequestMapping(method = RequestMethod.POST , value = "updBbsComment.do")
	@ResponseBody
	public ResponseEntity<JSONObject> updBbsCommentCtrl(@ModelAttribute("BbsCommentVO") BbsCommentVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: updBbsComment :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				System.out.println("bbsId=>"+vo.getBbsId());
				System.out.println("bbsNo=>"+vo.getBbsNo());
				System.out.println("commNo=>"+vo.getCommNo());
				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());
				
				int result = bbsService.updBbsComment(vo);
				
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
	
	// 게시글 삭제하기 댓글 삭제
	@RequestMapping(method = {RequestMethod.GET, RequestMethod.POST} , value = "delBbsComment.do")
	@ResponseBody
	public ResponseEntity<JSONObject> delBbsCommentCtrl(@ModelAttribute("BbsCommentVO") BbsCommentVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		
		try {
			System.out.println(":::::::::::::::::::: delBbsComment :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {
				System.out.println("bizId=>"+loginVO.getBizId());
				System.out.println("bbsId=>"+vo.getBbsId());
				System.out.println("bbsNo=>"+vo.getBbsNo());

				vo.setBizId(loginVO.getBizId());
				vo.setUserId(loginVO.getUserId());

				int result = 0;
				
				// 본문 게시글 댓글 삭제처리
				result = bbsService.delBbsComment(vo);

				success = true;
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

	//노무sos 샘플양식 업로드
	@RequestMapping(method = RequestMethod.POST , value = "insWorkSOSSampleUpload.do")
	@ResponseBody
	public ResponseEntity<JSONObject> insWorkSOSSampleUploadCtrl(@ModelAttribute("BbsContentsVO") BbsContentsVO vo, HttpServletRequest request) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		HttpHeaders responseHeaders = new HttpHeaders();
		responseHeaders.add("Content-Type", "application/json; charset=UTF-8");
		
		HttpStatus status = HttpStatus.OK;
		boolean success = false;
		String message = "";
		
		try {
			System.out.println(":::::::::::::::::::: insWorkSOSSampleUpload :::::::::::::::::::");
			SessionVO loginVO = SessionUtil.getSession(request);
			
			if (loginVO == null) {
				status = HttpStatus.UNAUTHORIZED;
			} else {

				// 파일생성
				String szSavePath = MultipartFileUtil.getSystemTempPath();
				List<FileVO> uploadFileList = MultipartFileUtil.getFileAddList(request, szSavePath, true);
				int total = uploadFileList.size();
				System.out.println("[insWorkSOSSampleUpload] File Count : "+uploadFileList.size());
				System.out.println("[insWorkSOSSampleUpload] sampleId=>"+vo.getSampleId());
				
				int fileCount = 0;
				
				// 전달받은 파일리스트
				for(int i=0;i<uploadFileList.size();i++) {
					
					FileVO fileVO = uploadFileList.get(i);
					
					if(!fileVO.getFileExt().equals("hwp")){
						message = ".hwp 파일만 등록 가능합니다.";

						jsonObject.put("success", false);
						jsonObject.put("message", message);

						return new ResponseEntity<>(jsonObject, responseHeaders, status);
						
					}

					//TEMP폴더 파일 위치
					String uploadPath = fileVO.getFileStrePath()+MultipartFileUtil.SEPERATOR+fileVO.getFileStreNm();
					
					//복사할 폴더 파일위치
					String newFileId = "";//StringUtil.getUUID();
					
					if(vo.getSampleId().equals("1")){
						newFileId="정규직_표준근로계약서";
					} else if(vo.getSampleId().equals("2")){
						newFileId="단시간근로자_표준근로계약서";
					} else if(vo.getSampleId().equals("3")){
						newFileId="연소자18세_표준근로계약서";
					} else if(vo.getSampleId().equals("4")){
						newFileId="기간제근로자_표준근로계약서";
					} else if(vo.getSampleId().equals("5")){
						newFileId="건설일용직_표준근로계약서";
					} else {
						newFileId="외국인근로자_표준근로계약서";
					}
					
					//String saveMonthPath  = MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate()+MultipartFileUtil.SEPERATOR;
					//복사할 파일 FULLPATH
					String targetName = newFileId+"."+fileVO.getFileExt();
					
					String targetPath = MultipartFileUtil.getSystemPath()+"/data/contract/sample/"+targetName;
					FileUtil.createDirectory(MultipartFileUtil.getSystemPath()+"/data/contract/sample");
					if(FileUtil.fileCopy(uploadPath, targetPath)) {
						success = true;
					}
				}
				
			}
		} catch (Exception ex) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
			logService.error(ex.getMessage(), new Throwable(ex));
			throw ex;
		}
		jsonObject.put("success", success);
		jsonObject.put("message", message);

		return new ResponseEntity<>(jsonObject, responseHeaders, status);
	}

	
}
