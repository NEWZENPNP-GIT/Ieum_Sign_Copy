package com.ezsign.framework.vo;

import java.io.Serializable;

import lombok.Data;

@Data
public class FileVO implements Serializable {
		
	private static final long serialVersionUID = -3460411919552942720L;
	
	String fileKey; // 파일키값
	String fileStrePath; // 파일저장경로
	String fileStreNm; // 저장파일명
	String fileStreOriNm; // 원본파일명
	String fileExt; // 파일확장자
	long fileSize; // 파일사이즈

	String fileParamName;
}
