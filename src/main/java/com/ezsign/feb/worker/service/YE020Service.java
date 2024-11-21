package com.ezsign.feb.worker.service;

import java.util.List;
import java.util.Map;

import com.ezsign.feb.worker.vo.YE020VO;
import com.ezsign.framework.vo.FileVO;
import com.ezsign.framework.vo.SessionVO;

public interface YE020Service {
	
	public static final String SAVE_CLASS_ID = "180713092250005";	// 연말정산_추가서류 class_id 
	
	public List<YE020VO> getYE020List(YE020VO vo) throws Exception;

	public int getYE020ListCount(YE020VO vo) throws Exception;
	
	public List<YE020VO> getYE020FileList(YE020VO vo) throws Exception;
	
	public int getYE020FileListCount(YE020VO vo) throws Exception;
	
	public void saveYE020(List<YE020VO> list) throws Exception;
	
	public void insYE020(YE020VO vo, FileVO fileVO) throws Exception;
	
	public int updYE020(YE020VO vo, FileVO fileVO) throws Exception;
	
	public int updYE020Confirm(YE020VO vo) throws Exception;
	
	public int delYE020(YE020VO vo) throws Exception;
	
	public String downloadAttachmentFile(YE020VO vo) throws Exception;
	

	/**
	 * 추가제출서류 zip 파일을 생성한다.
	 *
	 * @param list
	 * @param loginVO
	 * @return
	 * @throws Exception
	 */
	public Map<String,String> makeYE020ZipDocument(List<YE020VO> list, SessionVO loginVO) throws Exception;

}