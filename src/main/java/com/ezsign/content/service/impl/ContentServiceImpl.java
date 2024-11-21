package com.ezsign.content.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.ezsign.content.dao.CabinetDAO;
import com.ezsign.content.dao.ClassDAO;
import com.ezsign.content.dao.ContentDAO;
import com.ezsign.content.service.ContentService;
import com.ezsign.content.vo.CabinetVO;
import com.ezsign.content.vo.ClassVO;
import com.ezsign.content.vo.ContentVO;
import com.ezsign.framework.util.MultipartFileUtil;
import com.ezsign.framework.util.ProcUtil;
import com.ezsign.framework.vo.FileVO;
import com.jarvis.common.util.DateUtil;
import com.jarvis.common.util.FileUtil;
import com.jarvis.common.util.StringUtil;
import com.jarvis.pdf.vo.PageVO;

@Service("contentService")
public class ContentServiceImpl implements ContentService {

	Logger logger = Logger.getLogger(this.getClass());

	@Resource(name="cabinetDAO")
	private CabinetDAO cabinetDAO;

	@Resource(name="classDAO")
	private ClassDAO classDAO;

	@Resource(name="contentDAO")
	private ContentDAO contentDAO;

	@Override
	public ContentVO getContent(ContentVO vo)  throws Exception {
		// 콘텐츠에서 정보 추출	
		ContentVO result = contentDAO.getContent(vo);
		if (result == null) { logger.info("[getContent] 콘텐츠 정보가 존재하지 않습니다."); return null; }

		// 저장위치
		CabinetVO cabinetVO = new CabinetVO();
		cabinetVO.setClassId(result.getClassId());
		cabinetVO.setCabinetId(result.getCabinetId());
		CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
		if (cabinetResultVO == null) { logger.info("[getContent] 캐비넷 정보가 존재하지 않습니다."); return null; }

		String originalPdfPath = cabinetResultVO.getCabinetPath()+result.getFilePath()+result.getFileName();
		String targetPdfName   = StringUtil.getUUID()+".pdf";
		String targetPdfPath   = MultipartFileUtil.getSystemTempPath()+targetPdfName;

		logger.info(originalPdfPath);
		logger.info(targetPdfPath);
		// 사용할 PDF복사
		FileUtil.createDirectory(MultipartFileUtil.getSystemTempPath());
		if (FileUtil.fileCopy(originalPdfPath, targetPdfPath)) result.setFileTitle(targetPdfPath);
		else												   result.setFileTitle("");

		return result;
	}

	@Override
	public int transContent(ContentVO vo) throws Exception {
		int result 				  = 0;
		InputStream inputStream   = null;
		OutputStream outputStream = null;

		String loadFilePath = vo.getFilePath() + vo.getFileName();
		String saveFilePath = "";
		logger.info("transContent.loadFilePath : "+loadFilePath);

		try {
			String newFileId     = StringUtil.getUUID();
			String saveMonthPath = MultipartFileUtil.SEPERATOR+DateUtil.getYearMonth()+MultipartFileUtil.SEPERATOR+DateUtil.getDate()+MultipartFileUtil.SEPERATOR;

			// 분류체계조회
			ClassVO classVO 	  = new ClassVO();
			classVO.setClassId(vo.getClassId());
			ClassVO resultClassVO = classDAO.getClass(classVO);
			if (resultClassVO == null) { logger.info("[transContent] 분류체계 정보가 존재하지 않습니다."); return ERR_READ_CLASS; }

			// 캐비넷정보조회
			CabinetVO cabinetVO = new CabinetVO();
			cabinetVO.setClassId(resultClassVO.getClassId());
			CabinetVO resultCabinetVO = cabinetDAO.getCabinetClass(cabinetVO);
			if (resultCabinetVO==null) { logger.info("[transContent] 캐비넷 정보가 존재하지 않습니다."); return ERR_READ_CABINET; }

			vo.setCabinetId(resultCabinetVO.getCabinetId());
			// 저장할 위치정보
			String saveCabinetPath = resultCabinetVO.getCabinetPath()+ saveMonthPath ;
			boolean resultMakeDir = FileUtil.createDirectory(saveCabinetPath);
			if (!resultMakeDir) { logger.info("[transContent] 폴더 생성에 실패하였습니다."); return ERR_MAKE_DIR; }

			saveFilePath = saveCabinetPath + newFileId + "." + vo.getFileType();

			// 파일 저장
			inputStream = new FileInputStream(loadFilePath);
			outputStream = Files.newOutputStream(new File(saveFilePath).toPath());
			int read = 0;
			byte[] bytes = new byte[BUFFER_SIZE];

			while ((read = inputStream.read(bytes)) != -1) { outputStream.write(bytes, 0, read); }

			// DB처리
			vo.setFilePath(saveMonthPath);
			vo.setFileId(newFileId);
			vo.setFileName(newFileId+"."+vo.getFileType());
			contentDAO.insContent(vo);
			logger.info("["+newFileId+"] COMPLETED!");
			result = COMPLETED;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				try 				  { inputStream.close(); }
				catch (IOException e) { e.printStackTrace(); }
			}
			if (outputStream != null) {
				try 				  { outputStream.close(); }
				catch (IOException e) { e.printStackTrace(); }
			}
		}
		return result;
	}

	@Override
	public int deleteContent(List<ContentVO> list) throws Exception {
		int result 			= 0;
		ContentVO contentVO = new ContentVO();
		CabinetVO cabinetVO = new CabinetVO();

        for (ContentVO vo : list) {
            logger.info("[deleteContent] 처리할 콘텐츠정보 ID:" + vo.getFileId());
            // 콘텐츠에서 정보 추출
            contentVO.setFileId(vo.getFileId());
            ContentVO resultVO = contentDAO.getContent(contentVO);
            if (resultVO == null) { logger.info("[deleteContent] 콘텐츠 정보가 존재하지 않습니다."); return ERR_READ_CLASS; }

            // 저장위치
            cabinetVO.setClassId(resultVO.getClassId());
            cabinetVO.setCabinetId(resultVO.getCabinetId());
            CabinetVO cabinetResultVO = cabinetDAO.getCabinet(cabinetVO);
            if (cabinetResultVO == null) { logger.info("[deleteContent] 캐비넷 정보가 존재하지 않습니다."); return ERR_READ_CABINET; }

            String originalPdfPath = cabinetResultVO.getCabinetPath() + resultVO.getFilePath() + resultVO.getFileName();
            logger.info(originalPdfPath);
            File file			   = new File(originalPdfPath);

            if (file.exists() && file.isFile()) {
                boolean deleteResult = FileUtil.deleteFile(originalPdfPath);
                if (!deleteResult) { logger.info("[deleteContent] 파일 삭제에 실패하였습니다."); return ERR_DELETE_FILE; }
            }

            result = contentDAO.delContent(contentVO);
            if (result == 0) { logger.info("[deleteContent] 파일정보 삭제처리에 실패하였습니다."); return ERR_DELETE_CONTENT; }
        }
		return result;
	}

	// PDF 이미지 변환
	@Override
	public List<PageVO> getPDFToImage(ContentVO vo, List<FileVO> fileList)  throws Exception {
		List<PageVO> pageList = new ArrayList<>();

		for (FileVO fileVO : fileList) {
			String targetPdfPath = fileVO.getFileStrePath() + MultipartFileUtil.SEPERATOR + fileVO.getFileStreNm();
			String tempPath 	 = MultipartFileUtil.getSystemTempPath();
			String exeName		 = MultipartFileUtil.getSystemPath() + "data/mudraw.exe";
			pageList = ProcUtil.readPDF(exeName, targetPdfPath, tempPath);
            for (PageVO pageVO : pageList) {
                String filePath = StringUtil.ReplaceAll(tempPath, MultipartFileUtil.getSystemPath(), "");
                pageVO.setFilePath(filePath);
            }
		}
		return pageList;
	}

}
