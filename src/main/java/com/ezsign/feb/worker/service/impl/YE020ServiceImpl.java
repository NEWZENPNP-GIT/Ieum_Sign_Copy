package com.ezsign.feb.worker.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.code.dao.CodeDAO;
import com.ezsign.content.dao.CabinetDAO;
import com.ezsign.content.dao.ContentDAO;
import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.CabinetVO;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.contract.dao.ContractDAO;
import com.ezsign.feb.master.dao.YE000DAO;
import com.ezsign.feb.master.dao.YE901DAO;
import com.ezsign.feb.master.vo.YE000VO;
import com.ezsign.feb.master.vo.YE901VO;
import com.ezsign.feb.worker.dao.YE020DAO;
import com.ezsign.feb.worker.service.YE020Service;
import com.ezsign.feb.worker.vo.YE020VO;
import com.ezsign.framework.exception.AngullarProcessException;
import com.ezsign.framework.util.DateUtil;
import com.ezsign.framework.util.FileUtil;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.StringUtil;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;

import egovframework.rte.fdl.cmmn.AbstractServiceImpl;

@Service("ye020Service")
public class YE020ServiceImpl extends AbstractServiceImpl implements YE020Service {

	Logger logger = Logger.getLogger(this.getClass());
	
	@Resource(name="codeDAO")
	private CodeDAO codeDAO;

	@Resource(name="ye020DAO")
	private YE020DAO ye020DAO;

	@Resource(name = "contentService")
	private ContentService contentService;
	
	@Resource(name="contentDAO")
	private ContentDAO contentDAO;
	
	@Resource(name="cabinetDAO")
	private CabinetDAO cabinetDAO;
	
	@Resource(name = "ye901DAO")
    private YE901DAO ye901DAO;

	@Resource(name = "ye000DAO")
    private YE000DAO ye000DAO;
	
	@Resource(name="contractDAO")
	private ContractDAO contractDAO;
	
	
	// 연말정산_추가제출서류 조회
	@Override
	public List<YE020VO> getYE020List(YE020VO vo) throws Exception {
		List<YE020VO> list = ye020DAO.getYE020List(vo);
		
		return list;
	}
	
	// 연말정산_추가제출서류 갯수
	@Override
	public int getYE020ListCount(YE020VO vo) throws Exception {
		return ye020DAO.getYE020ListCount(vo);
	}
	
	// 연말정산_추가제출서류 첨부파일 조회
	@Override
	public List<YE020VO> getYE020FileList(YE020VO vo) throws Exception {
		List<YE020VO> list = ye020DAO.getYE020FileList(vo);
		
		return list;
	}
	
	// 연말정산_추가제출서류 첨부파일 갯수
	@Override
	public int getYE020FileListCount(YE020VO vo) throws Exception {
		return ye020DAO.getYE020FileListCount(vo);
	}
	
	// 연말정산_추가제출서류 입력
	@Override
	public void insYE020(YE020VO vo, FileVO fileVO) throws Exception {

		ContentVO contentVO = new ContentVO();
		contentVO.setFileType(fileVO.getFileExt());
		contentVO.setFileName(fileVO.getFileStreNm());
		contentVO.setOrgFileName(fileVO.getFileStreOriNm());
		contentVO.setFileSize(Long.toString(fileVO.getFileSize()));
		contentVO.setFilePath(fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR);
		contentVO.setFileTitle(vo.get계약ID()+vo.get사용자ID());
		contentVO.setClassId(YE020Service.SAVE_CLASS_ID);
		contentVO.setFileVersion("0");
		contentVO.setParFileId("");
		contentVO.setEtcDesc("");
		contentVO.setCheckInOut("N");
		contentVO.setCheckUserId("");
		contentVO.setCheckCount("0");
		contentVO.setUseYn("Y");
		contentVO.setDelYn("N");
		
		System.out.println("fileName :" + contentVO.getFilePath() + contentVO.getFileName());
		int nResult = contentService.transContent(contentVO);
		if(contentService.COMPLETED==nResult) {
			vo.set파일ID(contentVO.getFileId());
			ye020DAO.insYE020(vo);
		}
		
		YE000VO selYE000VO = new YE000VO();
		selYE000VO.set계약ID(vo.get계약ID());
		selYE000VO.setBizId(vo.getBizId());
		selYE000VO.set사용자ID(vo.get사용자ID());
		
		// 근로자 진행상태코드 조회
		YE901VO selYE901VO = new YE901VO();
		selYE901VO.set계약ID(vo.get계약ID());
		selYE901VO.set사용자ID(vo.get사용자ID());
		selYE901VO.set작업자ID(vo.get작업자ID());
		
		YE000VO ye000VO = ye000DAO.getYE000(selYE000VO);
		String 진행상태코드 = null;
		
		if (ye000VO != null) {
			진행상태코드 = ye000VO.get진행상태코드();
		}
		
		if (StringUtil.isNotNull(진행상태코드)) {
			if ("2".equals(진행상태코드)) {
				selYE901VO.set진행현황코드("21");
			} else if ("3".equals(진행상태코드)) {
				selYE901VO.set진행현황코드("31");
			} else {
				selYE901VO.set진행현황코드("15");
			}
		}

		// 근로자 진행현황 등록
		if (StringUtil.isNotNull(vo.get계약ID()) && StringUtil.isNotNull(vo.get사용자ID())) {
			ye901DAO.insYE901(selYE901VO);
		}
		
	}

	// 연말정산_추가제출서류 수정
	@Override
	public int updYE020(YE020VO vo, FileVO fileVO) throws Exception {
		int result = 0;
		
		if(fileVO != null) {
			// 이전 첨부파일 정보
			YE020VO ye020VO = new YE020VO();
			ye020VO.setBizId(vo.getBizId());
			ye020VO.set계약ID(vo.get계약ID());
			ye020VO.set사용자ID(vo.get사용자ID());
			ye020VO.set일련번호(vo.get일련번호());
			
			List<YE020VO> ye020VOList = ye020DAO.getYE020FileList(ye020VO);
			System.out.println("ye020VOList : " + ye020VOList);
			YE020VO resultVO = ye020VOList.get(0);
			
			ContentVO preContentVO = new ContentVO();
			preContentVO.setFileId(resultVO.get파일ID());
			
			List<ContentVO> contentList = new ArrayList<>();
			contentList.add(preContentVO);
			
			// 수정된 첨부파일 정보
			ContentVO contentVO = new ContentVO();
			contentVO.setFileType(fileVO.getFileExt());
			contentVO.setFileName(fileVO.getFileStreNm());
			contentVO.setOrgFileName(fileVO.getFileStreOriNm());
			contentVO.setFileSize(Long.toString(fileVO.getFileSize()));
			contentVO.setFilePath(fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR);
			contentVO.setFileTitle(vo.get계약ID()+vo.get사용자ID());
			contentVO.setClassId(YE020Service.SAVE_CLASS_ID);
			contentVO.setFileVersion("0");
			contentVO.setParFileId("");
			contentVO.setEtcDesc("");
			contentVO.setCheckInOut("N");
			contentVO.setCheckUserId("");
			contentVO.setCheckCount("0");
			contentVO.setUseYn("Y");
			contentVO.setDelYn("N");
			
			System.out.println("fileName :" + contentVO.getFilePath() + contentVO.getFileName());
			int nResult = contentService.transContent(contentVO);
			if(contentService.COMPLETED==nResult) {
				
				// 이전 첨부된 원본 파일만 삭제  DB는 사용여부 N
				contentService.deleteContent(contentList);
				
				// 콘텐츠에서 정보 추출	
				ContentVO resultContentVO = contentDAO.getContent(preContentVO);
				
				// temp 파일 삭제 
				String tempFilePath = MultipartFileUtil.getSystemTempPath() + vo.getBizId() + MultipartFileUtil.SEPERATOR + resultContentVO.getOrgFileName();
				File file = new File(tempFilePath);
				if(file.exists()) {
					boolean deleteResult = FileUtil.deleteFile(tempFilePath);
					if(!deleteResult) {
						System.out.println("[updYE020] 파일 삭제에 실패하였습니다.");
						return 0;
					}
				}
				
				// 수정된 파일 DB정보 엡데이트
				vo.set파일ID(contentVO.getFileId());
				ye020DAO.updYE020(vo);
			}			
		} else {
			// 첨부파일 변경 없음
			ye020DAO.updYE020(vo);
		}
		result++;
		
		return result;
	}

	// 연말정산_추가제출서류 삭제
	@Override
	public int delYE020(YE020VO vo) throws Exception {
		
		List<YE020VO> ye020VOList = ye020DAO.getYE020FileList(vo);
		int result = 0;
		for (int i=0; i<ye020VOList.size(); i++) {
			YE020VO ye020VO = new YE020VO();
			ye020VO = ye020VOList.get(i);
			
			ContentVO contentVO = new ContentVO();
			contentVO.setFileId(ye020VO.get파일ID());
			
			List<ContentVO> contentList = new ArrayList<>();
			contentList.add(contentVO);
			
			// 실제 원본 파일만 삭제  DB는 사용여부 N
			contentService.deleteContent(contentList);

			ye020DAO.delYE020(ye020VO);
			/*
			// temp 폴더 삭제 정책 검토 필요
			// 콘텐츠에서 정보 추출	
			ContentVO resultContentVO = contentDAO.getContent(contentVO);
			
			// temp 파일 삭제 
			String tempFilePath = MultipartFileUtil.getSystemTempPath() + vo.getBizId() + MultipartFileUtil.SEPERATOR + resultContentVO.getOrgFileName();
			FileUtil.deleteFile(tempFilePath);
			*/
			result++;
		}
		
		return result;
		
	}
	
	// 연말정산_추가제출서류 처리확인
	@Override
	public int updYE020Confirm(YE020VO vo) throws Exception {
		
		YE000VO selYE000VO = new YE000VO();
		
		selYE000VO.set계약ID(vo.get계약ID());
		selYE000VO.setBizId(vo.getBizId());
		selYE000VO.set사용자ID(vo.get사용자ID());
		
		// 근로자 진행상태코드 조회
		YE901VO selYE901VO = new YE901VO();
		selYE901VO.set계약ID(vo.get계약ID());
		selYE901VO.set사용자ID(vo.get사용자ID());
		selYE901VO.set작업자ID(vo.get작업자ID());
		
		YE000VO ye000VO = ye000DAO.getYE000(selYE000VO);
		String 진행상태코드 = null;
		
		if (ye000VO != null) {
			진행상태코드 = ye000VO.get진행상태코드();
		}
		
		if (StringUtil.isNotNull(진행상태코드)) {
			if ("2".equals(진행상태코드)) {
				selYE901VO.set진행현황코드("21");
			} else if ("3".equals(진행상태코드)) {
				selYE901VO.set진행현황코드("31");
			} else {
				selYE901VO.set진행현황코드("15");
			}
		}

		// 근로자 진행현황 등록
		if (StringUtil.isNotNull(vo.get계약ID()) && StringUtil.isNotNull(vo.get사용자ID())) {
			ye901DAO.insYE901(selYE901VO);
		}
		
		return ye020DAO.updYE020(vo);
	}
	
	// 연말정산_추가제출서류 첨부파일 확인 처리
	@Override
	public void saveYE020(List<YE020VO> list) throws Exception {
		
		for(int i=0; i<list.size(); i++) {
			YE020VO ye020VO = list.get(i);
			if("U".equals(ye020VO.getDbMode())) {
				if("1".equals(ye020VO.get처리여부())) {
					ye020VO.set처리일시(DateUtil.getTimeStamp(7));
				}
				ye020DAO.updYE020(ye020VO);
			} else if("D".equals(ye020VO.getDbMode())) {
				delYE020(ye020VO);
			}
		}
	}
	
	// 연말정산_추가제출서류 첨부파일 가져오기
	@Override
	public String downloadAttachmentFile(YE020VO vo) throws Exception {
		FileVO fileVO = new FileVO();
		
		ContentVO contentVO = new ContentVO();
		contentVO.setFileId(vo.get파일ID());
		
		// 콘텐츠에서 정보 추출	
		ContentVO resultContentVO = contentDAO.getContent(contentVO);
		if(resultContentVO==null) {
			System.out.println("[downloadAttachmentFile] 콘텐츠 정보가 존재하지 않습니다.");
			return "";
		}
		
		// 저장위치
		CabinetVO cabinetVO = new CabinetVO();
		cabinetVO.setClassId(resultContentVO.getClassId());
		cabinetVO.setCabinetId(resultContentVO.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if(cabinetResultVO==null) {
			System.out.println("[downloadAttachmentFile] 캐비넷 정보가 존재하지 않습니다.");
			return "";
		}
		
		String originalFilePath = cabinetResultVO.getCabinetPath()+resultContentVO.getFilePath()+resultContentVO.getFileName();
		
		String targetFileName = DateUtil.getTimeStamp(7) + "_" + resultContentVO.getOrgFileName();
		String targetFilePath = MultipartFileUtil.getSystemTempPath()+vo.getBizId() + MultipartFileUtil.SEPERATOR + targetFileName;
		
		System.out.println(originalFilePath);
		System.out.println(targetFilePath);
		
		// 사용할 파일 복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath()+vo.getBizId());
		FileUtil.fileCopy(originalFilePath, targetFilePath);
		
		return targetFileName;
	}
	
	
	/**
	 * <pre>
	 * 1. 개요 : 추가제출서류 zip 파일을 생성한다.
	 * 2. 처리내용 : 
	 * </pre>
	 *
	 * @Method Name : downloadWorkerFileZip
	 * @date : 2019. 1. 4.
	 * @author : soybean0627
	 *
	 * @history : 
	 *	-----------------------------------------------------------------------
	 *	변경일						작성자					변경내용  
	 *	-----------------------------------------------------------------------
	 *	2019. 1. 4.				soybean0627					최초 작성 
	 *	-----------------------------------------------------------------------
	 *
	 *  @see com.ezsign.feb.worker.service.YE020Service#downloadWorkerFileZip(java.util.List, com.ezsign.framework.vo.SessionVO)
	 *  @param list
	 *  @param loginVO
	 *  @return
	 *  @throws Exception
	 */
	@Override
	public Map<String,String> makeYE020ZipDocument(List<YE020VO> list, SessionVO loginVO) throws Exception {
		
		Map<String,String> resultMap = new HashMap<String,String>();
		
		List<String> documentList = new ArrayList<String>();
		
		String toDate = DateUtil.getDate();								    //오늘날짜
		String systemPath = System.getProperty("system.feb.path")+File.separator+"temp"+File.separator;			//시스템 파일저장 경로
		
		String contractId = null;
		String bizName = null;
		
		if(list != null && list.size() > 0){
			contractId = list.get(0).get계약ID();
			bizName = list.get(0).get사업장명();
		}
		
		logger.info("# makeYE020ZipDocument -> 계약ID : " + contractId );
		logger.info("# makeYE020ZipDocument -> bizName : " + bizName );
		
		try{
			
			int idx = 1;
			for(YE020VO paramVO : list){				
				logger.debug("# paramVO.get파일ID() : " + paramVO.get파일ID() );
								
				ContentVO contentVO = new ContentVO();
				contentVO.setFileId(paramVO.get파일ID());
				// 콘텐츠에서 정보 추출	
				ContentVO resultContentVO = contentDAO.getContent(contentVO);
				if(resultContentVO == null) {
					logger.fatal("# [makeYE020ZipDocument => 파일ID : " +paramVO.get파일ID()+"] 콘텐츠 정보가 존재하지 않습니다. #");				
					throw new AngullarProcessException("[makeYE020ZipDocument => 파일ID : " +paramVO.get파일ID()+"] 콘텐츠 정보가 존재하지 않습니다.");
				}else{
					// 저장위치
					CabinetVO cabinetVO = new CabinetVO();
					cabinetVO.setClassId(resultContentVO.getClassId());
					cabinetVO.setCabinetId(resultContentVO.getCabinetId());
					CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
					
					if(cabinetResultVO == null) {
						logger.fatal("# [makeYE020ZipDocument => CabinetId : " +resultContentVO.getCabinetId()+"] 캐비넷 정보가 존재하지 않습니다. #");					
						throw new AngullarProcessException("[makeYE020ZipDocument => CabinetId : " +resultContentVO.getCabinetId()+"] 캐비넷 정보가 존재하지 않습니다.");
					}else{
						
						String originalFilePath = cabinetResultVO.getCabinetPath()+resultContentVO.getFilePath()+resultContentVO.getFileName();
						
						logger.debug("# originalFilePath : " + originalFilePath );
	
						if(StringUtils.isNotEmpty(originalFilePath)){
							File file = new File(originalFilePath);
							
							if(file.exists()){
								
								String fileExt = originalFilePath.substring(originalFilePath.lastIndexOf(".")+1,originalFilePath.length());
								
								String tempSavePath = systemPath + "FEB" + File.separator + "YE020" + File.separator + "TEMP" + File.separator + toDate + File.separator;
								String tempSaveFileName = String.format("%03d", idx++) + "_" + paramVO.get사업장명() +"_"+ paramVO.getEmpNo() +"_"+paramVO.getEmpName() + "." + fileExt; 
								
								//디렉토리 생성
								FileUtil.createDirectory(tempSavePath);
								
								
								if(FileUtil.fileCopy(originalFilePath, tempSavePath+tempSaveFileName)) {
									documentList.add(tempSavePath+tempSaveFileName);
								}							
								
							}												
						}
						
					}
									
				}
				
			}
				
			String zipFileName = "";		//zip 파일명		
			String zipFilePath = "";  //zip 파일생성경로
			
			if(documentList != null && documentList.size() > 0){
				
				zipFileName = bizName+ "_" + contractId + "_" + System.currentTimeMillis() + ".zip";		//zip 파일명		
	//			zipFilePath = systemPath + "FEB"+ File.separator +"YE020_ZIP" + File.separator + toDate + File.separator;  //zip 파일생성경로
				zipFilePath = systemPath + "FEB"+ File.separator +"YE020" + File.separator + "ZIP" + File.separator + toDate + File.separator;  //zip 파일생성경로			
				zipFilePath = zipFilePath.replaceAll("\\\\", "/");
				
				logger.debug("# zipFileName : " + zipFileName);
				logger.debug("# zipFilePath : " + zipFilePath);
				
				File filePath = new File(zipFilePath);		
				if(!filePath.exists()){
					filePath.mkdirs();
				}
				
				
				int result = FileUtil.ZipFile(zipFilePath + zipFileName, documentList);		
				
				//Temp 파일 삭제
				if(result > 0){					
					for(String tempPath : documentList){
						File tempFile = new File(tempPath);
						
						if(tempFile.exists() && tempFile.isFile()){
							tempFile.delete();
						}
					}
				}
			}
			
			resultMap.put("zipFilePath", zipFilePath + zipFileName);
			resultMap.put("success", "true");
			
		}catch(AngullarProcessException ex){			
			resultMap.put("success", "false");
			resultMap.put("message", ex.getMessage());
		}
		
		return resultMap;
		
	}
}
